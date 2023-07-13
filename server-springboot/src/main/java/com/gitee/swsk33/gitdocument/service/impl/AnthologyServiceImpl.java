package com.gitee.swsk33.gitdocument.service.impl;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.gitee.swsk33.gitdocument.context.GitFileListenerContext;
import com.gitee.swsk33.gitdocument.dao.AnthologyDAO;
import com.gitee.swsk33.gitdocument.dao.UserDAO;
import com.gitee.swsk33.gitdocument.dataobject.Anthology;
import com.gitee.swsk33.gitdocument.dataobject.User;
import com.gitee.swsk33.gitdocument.message.GitCreateTaskMessage;
import com.gitee.swsk33.gitdocument.message.GitUpdateTaskMessage;
import com.gitee.swsk33.gitdocument.model.ArticleDiff;
import com.gitee.swsk33.gitdocument.model.CommitInfo;
import com.gitee.swsk33.gitdocument.model.Result;
import com.gitee.swsk33.gitdocument.param.CommonValue;
import com.gitee.swsk33.gitdocument.property.ConfigProperties;
import com.gitee.swsk33.gitdocument.service.AnthologyService;
import com.gitee.swsk33.gitdocument.service.ImageService;
import com.gitee.swsk33.gitdocument.util.ClassExamine;
import com.gitee.swsk33.gitdocument.util.GitFileUtils;
import com.gitee.swsk33.gitdocument.util.GitRepositoryUtils;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.revwalk.RevCommit;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Slf4j
@Component
@DependsOn("SQLInitializeAutoConfigure")
public class AnthologyServiceImpl implements AnthologyService {

	@Autowired
	private AnthologyDAO anthologyDAO;

	@Autowired
	private UserDAO userDAO;

	@Autowired
	private ImageService imageService;

	@Autowired
	private GitFileListenerContext listenerContext;

	@Autowired
	private ConfigProperties configProperties;

	@Autowired
	private RabbitTemplate rabbitTemplate;

	/**
	 * 启动时，开始监听现有的每个仓库，并开启文件更新任务队列
	 */
	@PostConstruct
	private void startRepositoryUpdateTask() {
		// 从数据库获取文集仓库信息
		List<Anthology> anthologies;
		try {
			anthologies = anthologyDAO.getAll();
		} catch (Exception e) {
			log.error("连接数据库失败！请检查配置！终止！");
			return;
		}
		log.info("共获取到：" + anthologies.size() + "个文集仓库！");
		// 对比本地仓库和数据库中的仓库，若有不同则进行更新
		log.info("开始对比本地仓库和数据库仓库信息...");
		anthologies.forEach(anthology -> {
			// 若有commitId不同的情况，以本地仓库为准，刷新到数据库
			String newCommitId;
			try {
				newCommitId = GitRepositoryUtils.getHeadCommitId(anthology.getRepoPath());
				// 如果说刚创建的仓库，则不进行对比
				if (newCommitId == null) {
					log.warn("本地仓库：" + anthology.getName() + "的commitId为空！可能是刚创建的仓库，跳过比对！");
					listenerContext.addMonitor(anthology.getId(), anthology.getRepoPath());
					return;
				}
				// 如果数据库中commitId为空，说明需要执行创建任务
				if (anthology.getLatestCommitId() == null) {
					log.warn("发现本地仓库：" + anthology.getName() + "在数据库中的commitId为空，进行创建操作...");
					GitCreateTaskMessage createTaskMessage = new GitCreateTaskMessage();
					createTaskMessage.setRepositoryId(anthology.getId());
					createTaskMessage.setCommitId(newCommitId);
					createTaskMessage.setFileList(GitFileUtils.getLatestFileList(anthology.getRepoPath()));
					rabbitTemplate.convertAndSend(CommonValue.MessageQueue.GIT_TASK_TOPIC_EXCHANGE, CommonValue.RabbitMQRoutingKey.GIT_CREATE, createTaskMessage);
					log.info("已发布对本地仓库：" + anthology.getName() + " 的创建任务消息至消息队列！");
					listenerContext.addMonitor(anthology.getId(), anthology.getRepoPath());
					return;
				}
				// 如果只是单纯的两者不同，说明需要进行更新同步操作
				if (!anthology.getLatestCommitId().equals(newCommitId)) {
					log.warn("发现本地仓库：" + anthology.getName() + "与数据库的commit不同，进行更新操作...");
					// 获取差异
					List<DiffEntry> diffs = GitFileUtils.compareDiffBetweenTwoCommits(anthology.getRepoPath(), anthology.getLatestCommitId(), newCommitId);
					if (diffs.size() == 0) {
						log.info("没有需要更新的差异！");
						listenerContext.addMonitor(anthology.getId(), anthology.getRepoPath());
						return;
					}
					GitUpdateTaskMessage updateTaskMessage = new GitUpdateTaskMessage();
					updateTaskMessage.setRepositoryId(anthology.getId());
					updateTaskMessage.setCommitId(newCommitId);
					updateTaskMessage.setDiffs(ArticleDiff.toArticleDiff(diffs));
					rabbitTemplate.convertAndSend(CommonValue.MessageQueue.GIT_TASK_TOPIC_EXCHANGE, CommonValue.RabbitMQRoutingKey.GIT_UPDATE, updateTaskMessage);
					log.info("已发布对本地仓库：" + anthology.getName() + " 的更新任务消息至消息队列！");
				}
			} catch (Exception e) {
				log.error("发生错误！本地仓库" + anthology.getName() + "可能不存在！继续！");
				e.printStackTrace();
			} finally {
				// 检查更新完成后，对其加入监听
				listenerContext.addMonitor(anthology.getId(), anthology.getRepoPath());
			}
		});
		log.info("已开启全部文集仓库监听！");
		log.info("所有仓库位于：" + configProperties.getRepoPath());
	}

	@SaCheckPermission(CommonValue.Permission.EDIT_ANTHOLOGY)
	@Override
	public Result<Void> add(Anthology anthology) {
		Result<Void> result = new Result<>();
		Anthology getAnthology = anthologyDAO.getByName(anthology.getName());
		if (getAnthology != null) {
			result.setResultFailed("该文集名已被使用！");
			return result;
		}
		// 先去创建一个Git仓库，并加入到监听列表
		String repoPath = configProperties.getRepoPath() + File.separator + anthology.getName() + ".git";
		if (!GitRepositoryUtils.initGitBareRepository(repoPath)) {
			result.setResultFailed("创建文集仓库失败！请联系开发者！");
			return result;
		}
		log.info("成功创建文集仓库！位于：" + repoPath);
		// 补充信息
		anthology.setId(IdUtil.getSnowflakeNextId());
		anthology.setRepoPath(repoPath);
		// 加入监听
		listenerContext.addMonitor(anthology.getId(), repoPath);
		// 存入数据库
		anthologyDAO.add(anthology);
		result.setResultSuccess("创建文集仓库成功！");
		return result;
	}

	@SaCheckPermission(CommonValue.Permission.EDIT_ANTHOLOGY)
	@Override
	public Result<Void> batchAdd(List<Anthology> anthologies) {
		for (Anthology anthology : anthologies) {
			add(anthology);
		}
		Result<Void> result = new Result<>();
		result.setResultSuccess("批量添加完成！");
		return result;
	}

	@SaCheckPermission(CommonValue.Permission.EDIT_ANTHOLOGY)
	@Override
	public Result<Void> delete(long id) throws IOException {
		Result<Void> result = new Result<>();
		// 查找仓库
		Anthology getAnthology = anthologyDAO.getById(id);
		if (getAnthology == null) {
			result.setResultFailed("待删除文集不存在！");
			return result;
		}
		// 停止文件监听
		listenerContext.removeMonitor(id);
		// 删除仓库文件夹
		if (!FileUtil.del(getAnthology.getRepoPath())) {
			result.setResultFailed("删除文集仓库失败！请联系开发者！");
			return result;
		}
		log.info("成功删除文集仓库：" + getAnthology.getRepoPath());
		// 从数据库移除
		anthologyDAO.delete(id);
		result.setResultSuccess("删除文集成功！");
		return result;
	}

	@SaCheckPermission(CommonValue.Permission.EDIT_ANTHOLOGY)
	@Override
	public Result<Void> update(Anthology anthology) throws Exception {
		Result<Void> result = new Result<>();
		if (!StrUtil.isEmpty(anthology.getLatestCommitId())) {
			result.setResultFailed("不允许手动修改commit id！");
			return result;
		}
		// 补全
		Anthology getAnthology = anthologyDAO.getById(anthology.getId());
		ClassExamine.objectOverlap(anthology, getAnthology);
		anthologyDAO.update(anthology);
		result.setResultSuccess("修改文集信息成功！");
		return result;
	}

	@SaCheckPermission(CommonValue.Permission.BROWSE_ARTICLE)
	@Override
	public Result<Anthology> getById(long id) throws Exception {
		Result<Anthology> result = new Result<>();
		Anthology getAnthology = anthologyDAO.getById(id);
		if (getAnthology == null) {
			result.setResultFailed("该文集不存在！");
			return result;
		}
		// 填充信息
		getAnthology.setSystemUser(CommonValue.RUN_USER_NAME);
		getAnthology.setSshPort(configProperties.getHostPort());
		getAnthology.setUpdateTime(GitRepositoryUtils.getHeadCommit(getAnthology.getRepoPath()).getCommitTime());
		result.setResultSuccess("查找成功！", getAnthology);
		return result;
	}

	@SaCheckPermission(CommonValue.Permission.BROWSE_ARTICLE)
	@Override
	public Result<List<CommitInfo>> getAllCommits(long id) throws Exception {
		Result<List<CommitInfo>> result = new Result<>();
		Anthology getAnthology = anthologyDAO.getById(id);
		if (getAnthology == null) {
			result.setResultFailed("文集不存在！");
			return result;
		}
		List<RevCommit> getCommits = GitRepositoryUtils.getAllCommits(getAnthology.getRepoPath());
		// 结果列表
		List<CommitInfo> commitInfos = new ArrayList<>();
		// 填充信息
		getCommits.forEach(item -> {
			CommitInfo info = new CommitInfo();
			// 获取用户信息
			User getUser = userDAO.getByUsernameOrEmail(item.getAuthorIdent().getEmailAddress());
			if (getUser == null) {
				getUser = new User();
				getUser.setNickname("未知用户");
			}
			info.setCommitter(getUser);
			info.setMessage(item.getFullMessage());
			info.setTimestamp(item.getCommitTime());
			commitInfos.add(info);
		});
		result.setResultSuccess("获取成功！", commitInfos);
		return result;
	}

	@SaCheckPermission(CommonValue.Permission.BROWSE_ARTICLE)
	@Override
	public Result<List<Anthology>> getAll() {
		Result<List<Anthology>> result = new Result<>();
		List<Anthology> anthologies = anthologyDAO.getAll();
		// 填充信息
		anthologies.forEach(item -> {
			int timestamp = 0;
			try {
				RevCommit commit = GitRepositoryUtils.getHeadCommit(item.getRepoPath());
				if (commit != null) {
					timestamp = commit.getCommitTime();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			item.setSystemUser(CommonValue.RUN_USER_NAME);
			item.setSshPort(configProperties.getHostPort());
			item.setUpdateTime(timestamp);
		});
		result.setResultSuccess("查询成功！", anthologies);
		return result;
	}

	@SaCheckPermission(CommonValue.Permission.BROWSE_ARTICLE)
	@Override
	public Result<byte[]> getImageData(long id, String imageFilePath) {
		Result<byte[]> result = new Result<>();
		Anthology getAnthology = anthologyDAO.getById(id);
		if (getAnthology == null) {
			result.setResultFailed("文集不存在！");
			return result;
		}
		byte[] data;
		try {
			data = GitFileUtils.getFileBytesInLatestCommit(getAnthology.getRepoPath(), imageFilePath);
		} catch (Exception e) {
			e.printStackTrace();
			result.setResultFailed("图片文件获取失败！");
			return result;
		}
		result.setResultSuccess("获取成功！", data);
		return result;
	}

	@SaCheckPermission(CommonValue.Permission.EDIT_ANTHOLOGY)
	@Override
	public Result<List<Anthology>> getAnthologyNotInDatabase() {
		Result<List<Anthology>> result = new Result<>();
		List<Anthology> anthologyListInDB = anthologyDAO.getAll();
		List<Anthology> notInDB = new ArrayList<>();
		Stream<File> files = Stream.of(new File(configProperties.getRepoPath()).listFiles());
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
		result.setResultSuccess("查找到下列文集仓库没有录入数据库！", notInDB);
		return result;
	}

}