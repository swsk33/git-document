package com.gitee.swsk33.gitdocument.service.impl;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.gitee.swsk33.gitdocument.context.GitFileListenerContext;
import com.gitee.swsk33.gitdocument.dao.AnthologyDAO;
import com.gitee.swsk33.gitdocument.dao.SystemSettingDAO;
import com.gitee.swsk33.gitdocument.dao.UserDAO;
import com.gitee.swsk33.gitdocument.dataobject.Anthology;
import com.gitee.swsk33.gitdocument.dataobject.User;
import com.gitee.swsk33.gitdocument.gitdao.GitCommitDAO;
import com.gitee.swsk33.gitdocument.gitdao.GitFileDAO;
import com.gitee.swsk33.gitdocument.gitdao.GitRepositoryDAO;
import com.gitee.swsk33.gitdocument.model.CommitRecord;
import com.gitee.swsk33.gitdocument.model.CreateEmailMessage;
import com.gitee.swsk33.gitdocument.model.Result;
import com.gitee.swsk33.gitdocument.param.AnthologyStatus;
import com.gitee.swsk33.gitdocument.param.PermissionName;
import com.gitee.swsk33.gitdocument.property.GitRepositoryProperties;
import com.gitee.swsk33.gitdocument.service.AnthologyService;
import com.gitee.swsk33.gitdocument.service.EmailService;
import com.gitee.swsk33.gitdocument.service.ImageService;
import com.gitee.swsk33.gitdocument.session.UserSession;
import com.mybatisflex.core.relation.RelationManager;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.revwalk.RevCommit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static com.gitee.swsk33.gitdocument.param.SystemSettingKey.ORGANIZATION_NAME;

@Slf4j
@Component
@DependsOn("SQLInitializeAutoConfigure")
public class AnthologyServiceImpl implements AnthologyService {

	@Autowired
	private AnthologyDAO anthologyDAO;

	@Autowired
	private UserDAO userDAO;

	@Autowired
	private SystemSettingDAO systemSettingDAO;

	@Autowired
	private GitRepositoryDAO gitRepositoryDAO;

	@Autowired
	private GitCommitDAO gitCommitDAO;

	@Autowired
	private GitFileDAO gitFileDAO;

	@Autowired
	private ImageService imageService;

	@Autowired
	private EmailService emailService;

	@Autowired
	private GitFileListenerContext listenerContext;

	@Autowired
	private GitRepositoryProperties gitRepositoryProperties;

	@Autowired
	private UserSession userSession;

	/**
	 * 启动时，开始监听现有的每个仓库，并开启文件更新任务队列
	 */
	@PostConstruct
	public void checkRepository() {
		// 文集仓库根目录不存在则创建
		if (gitRepositoryProperties.getRepositoryPath().equals("null")) {
			log.warn("文集仓库根路径未配置！重置为默认值！");
			gitRepositoryProperties.setRepositoryPath(System.getProperty("user.home") + File.separator + "git-doc-repos");
		}
		if (!FileUtil.exist(gitRepositoryProperties.getRepositoryPath())) {
			log.warn("文集仓库根路径{}不存在！即将创建...", gitRepositoryProperties.getRepositoryPath());
			FileUtil.mkdir(gitRepositoryProperties.getRepositoryPath());
		}
		// 从数据库获取文集仓库信息
		List<Anthology> anthologies;
		try {
			anthologies = anthologyDAO.selectAll();
		} catch (Exception e) {
			log.error("连接数据库失败！请检查配置！终止！");
			log.error(e.getMessage());
			return;
		}
		log.info("共获取到：{}个文集仓库！", anthologies.size());
		// 对比本地仓库和数据库中的仓库，若有不同则进行更新
		log.info("开始对比本地仓库和数据库仓库信息...");
		anthologies.forEach(anthology -> {
			try {
				// 检查每个文集仓库本地和数据库的差异，并进行同步
				gitRepositoryDAO.checkGitRepositoryUpdate(anthology);
			} catch (Exception e) {
				log.error("发生错误！本地仓库{}可能不存在！继续！", anthology.getName());
				log.error(e.getMessage());
			} finally {
				// 检查更新完成后，对其加入监听
				listenerContext.addMonitor(anthology.getId(), anthology.getRepoPath());
			}
		});
		log.info("已开启全部文集仓库监听！");
		log.info("所有仓库位于：{}", gitRepositoryProperties.getRepositoryPath());
	}

	@SaCheckPermission(PermissionName.EDIT_ANTHOLOGY)
	@Override
	public Result<Void> add(Anthology anthology) {
		if (anthologyDAO.existsByName(anthology.getName())) {
			return Result.resultFailed("该文集名已被使用！");
		}
		// 计算创建的仓库路径
		String repoPath = gitRepositoryProperties.getRepositoryPath() + File.separator + anthology.getName() + ".git";
		// 创建裸仓库
		if (!gitRepositoryDAO.createGitBareRepository(repoPath)) {
			return Result.resultFailed("创建文集仓库失败！请联系开发者！");
		}
		log.info("成功创建文集仓库！位于：{}", repoPath);
		// 补充信息
		anthology.setRepoPath(repoPath);
		anthology.setStatus(AnthologyStatus.UPDATING);
		// 存入数据库
		anthologyDAO.insert(anthology);
		// 加入监听
		listenerContext.addMonitor(anthology.getId(), repoPath);
		// 发送通知
		// 获取订阅新文集创建通知的用户
		List<User> receivers = userDAO.getByReceiveCreate();
		if (!receivers.isEmpty()) {
			List<String> emails = receivers.stream().map(User::getEmail).toList();
			// 准备任务消息
			CreateEmailMessage message = new CreateEmailMessage();
			message.setTitle("GitDocument · " + systemSettingDAO.get(ORGANIZATION_NAME) + " - 新文集发布通知");
			message.setName(anthology.getShowName());
			message.setEmailList(emails);
			message.setPublisher(userSession.getCurrentLoginSessionUser().getNickname());
			// 异步发送邮件
			emailService.sendAnthologyCreateNotify(message);
		}
		return Result.resultSuccess("创建文集仓库成功！");
	}

	@SaCheckPermission(PermissionName.EDIT_ANTHOLOGY)
	@Override
	public Result<Void> delete(long id) {
		// 查找仓库
		Anthology getAnthology = anthologyDAO.selectOneById(id);
		if (getAnthology == null) {
			return Result.resultFailed("待删除文集不存在！");
		}
		// 停止文件监听
		listenerContext.removeMonitor(id);
		// 删除仓库文件夹
		if (!FileUtil.del(getAnthology.getRepoPath())) {
			return Result.resultFailed("删除文集仓库失败！请联系开发者！");
		}
		log.info("成功删除文集仓库：{}", getAnthology.getRepoPath());
		// 从数据库移除
		anthologyDAO.deleteById(id);
		return Result.resultSuccess("删除文集成功！");
	}

	@SaCheckPermission(PermissionName.EDIT_ANTHOLOGY)
	@Override
	public Result<Void> update(Anthology anthology) {
		Anthology getAnthology = anthologyDAO.selectOneById(anthology.getId());
		if (getAnthology == null) {
			return Result.resultFailed("对应的文集id不存在！");
		}
		// 检查封面是否修改，若修改删除原封面
		if (!StrUtil.isEmpty(anthology.getCover()) && !anthology.getCover().equals(getAnthology.getCover())) {
			log.info("文集封面修改！");
			if (!StrUtil.isEmpty(getAnthology.getCover())) {
				imageService.delete(getAnthology.getCover());
			}
		}
		anthologyDAO.update(anthology);
		return Result.resultSuccess("修改文集信息成功！");
	}

	@SaCheckPermission(PermissionName.BROWSE_ARTICLE)
	@Override
	public Result<Anthology> getById(long id) {
		RelationManager.addQueryRelations("stars");
		Anthology getAnthology = anthologyDAO.selectOneWithRelationsById(id);
		if (getAnthology == null) {
			return Result.resultFailed("该文集不存在！");
		}
		// 获取更新时间
		RevCommit commit = gitCommitDAO.getHeadCommit(getAnthology.getRepoPath());
		getAnthology.setUpdateTime(commit != null ? commit.getCommitTime() : null);
		return Result.resultSuccess("查找成功！", getAnthology);
	}

	@SaCheckPermission(PermissionName.BROWSE_ARTICLE)
	@Override
	public Result<List<CommitRecord>> getAllCommits(long id) {
		Anthology getAnthology = anthologyDAO.selectOneWithRelationsById(id);
		if (getAnthology == null) {
			return Result.resultFailed("文集不存在！");
		}
		List<RevCommit> getCommits = gitCommitDAO.getAllCommits(getAnthology.getRepoPath());
		// 结果列表
		List<CommitRecord> commitRecords = new ArrayList<>();
		// 填充信息
		getCommits.forEach(item -> {
			CommitRecord info = new CommitRecord();
			// 获取用户信息
			User getUser = userDAO.getByUsernameOrEmail(item.getAuthorIdent().getEmailAddress());
			if (getUser == null) {
				getUser = new User();
				getUser.setNickname("未知用户");
			}
			info.setCommitter(getUser);
			info.setMessage(item.getFullMessage());
			info.setTimestamp(item.getCommitTime());
			commitRecords.add(info);
		});
		return Result.resultSuccess("获取成功！", commitRecords);
	}

	@SaCheckPermission(PermissionName.BROWSE_ARTICLE)
	@Override
	public Result<List<Anthology>> getAll() {
		RelationManager.addQueryRelations("stars");
		List<Anthology> anthologies = anthologyDAO.selectAllWithRelations();
		// 填充时间信息
		anthologies.forEach(item -> {
			RevCommit commit = gitCommitDAO.getHeadCommit(item.getRepoPath());
			item.setUpdateTime(commit != null ? commit.getCommitTime() : null);
		});
		return Result.resultSuccess("查询成功！", anthologies);
	}

	@SaCheckPermission(PermissionName.BROWSE_ARTICLE)
	@Override
	public Result<byte[]> getImageData(long id, String imageFilePath) {
		Anthology getAnthology = anthologyDAO.selectOneById(id);
		if (getAnthology == null) {
			return Result.resultFailed("文集不存在！");
		}
		byte[] data;
		try {
			data = gitFileDAO.getFileBytesInLatestCommit(getAnthology.getRepoPath(), imageFilePath);
		} catch (Exception e) {
			e.printStackTrace();
			return Result.resultFailed("图片文件获取失败！");
		}
		return Result.resultSuccess("获取成功！", data);
	}

	@SaCheckPermission(PermissionName.EDIT_ANTHOLOGY)
	@Override
	public Result<List<Anthology>> getAnthologyNotInDatabase() {
		List<Anthology> anthologyListInDB = anthologyDAO.selectAll();
		List<Anthology> notInDB = new ArrayList<>();
		Stream<File> files = Stream.of(FileUtil.file(gitRepositoryProperties.getRepositoryPath()).listFiles());
		files.filter(file -> {
			if (file.isFile()) {
				return false;
			}
			if (!file.getAbsolutePath().endsWith(".git")) {
				return false;
			}
			for (Anthology anthology : anthologyListInDB) {
				if (anthology.getRepoPath().equals(file.getAbsolutePath())) {
					return false;
				}
			}
			return true;
		}).forEach(file -> {
			Anthology anthology = new Anthology();
			String filePath = file.getAbsolutePath();
			String name = filePath.substring(filePath.lastIndexOf(File.separator) + 1, filePath.lastIndexOf(".git"));
			anthology.setName(name);
			anthology.setShowName(name);
			notInDB.add(anthology);
		});
		return Result.resultSuccess("查找到下列文集仓库没有录入数据库！", notInDB);
	}

	@SaCheckPermission(PermissionName.EDIT_ANTHOLOGY)
	@Override
	public Result<Void> restoreAnthologyNotInDatabase(List<Anthology> anthologies) {
		for (Anthology anthology : anthologies) {
			// 计算创建的仓库路径
			String repoPath = gitRepositoryProperties.getRepositoryPath() + File.separator + anthology.getName() + ".git";
			if (!FileUtil.exist(repoPath)) {
				continue;
			}
			// 补充信息
			anthology.setRepoPath(repoPath);
			anthology.setStatus(AnthologyStatus.UPDATING);
			// 存入数据库
			anthologyDAO.insert(anthology);
			// 比对差异
			gitRepositoryDAO.checkGitRepositoryUpdate(anthology);
			// 加入监听
			listenerContext.addMonitor(anthology.getId(), repoPath);
		}
		return Result.resultSuccess("已恢复对应的文集仓库！");
	}

}