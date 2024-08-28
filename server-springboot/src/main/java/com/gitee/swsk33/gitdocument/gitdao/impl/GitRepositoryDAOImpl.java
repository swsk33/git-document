package com.gitee.swsk33.gitdocument.gitdao.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.gitee.swsk33.gitdocument.dao.SystemSettingDAO;
import com.gitee.swsk33.gitdocument.dao.UserDAO;
import com.gitee.swsk33.gitdocument.dataobject.Anthology;
import com.gitee.swsk33.gitdocument.dataobject.User;
import com.gitee.swsk33.gitdocument.gitdao.GitCommitDAO;
import com.gitee.swsk33.gitdocument.gitdao.GitFileDAO;
import com.gitee.swsk33.gitdocument.gitdao.GitRepositoryDAO;
import com.gitee.swsk33.gitdocument.model.ArticleDifference;
import com.gitee.swsk33.gitdocument.model.GitCreateTaskMessage;
import com.gitee.swsk33.gitdocument.model.GitUpdateTaskMessage;
import com.gitee.swsk33.gitdocument.model.UpdateEmailMessage;
import com.gitee.swsk33.gitdocument.publisher.GitMessagePublisher;
import com.gitee.swsk33.gitdocument.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.diff.DiffEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;

import static com.gitee.swsk33.gitdocument.param.SystemSettingKey.ORGANIZATION_NAME;

@Slf4j
@Component
@DependsOn({"gitCreateTaskFlux", "gitUpdateTaskFlux"})
public class GitRepositoryDAOImpl implements GitRepositoryDAO {

	@Autowired
	private UserDAO userDAO;

	@Autowired
	private SystemSettingDAO systemSettingDAO;

	@Autowired
	private GitCommitDAO gitCommitDAO;

	@Autowired
	private GitFileDAO gitFileDAO;

	@Autowired
	private GitMessagePublisher gitCreateTaskPublisher;

	@Autowired
	private GitMessagePublisher gitUpdateTaskPublisher;

	@Autowired
	private EmailService emailService;

	@Override
	public boolean createGitBareRepository(String gitRepository) {
		// 裸仓库文件夹
		File repositoryFolder = FileUtil.file(gitRepository);
		// 创建仓库文件夹
		FileUtil.mkdir(repositoryFolder);
		try {
			// 初始化裸仓库
			Git.init().setDirectory(repositoryFolder).setBare(true).call().close();
		} catch (Exception e) {
			log.error("创建裸仓库出错！");
			log.error(e.getMessage());
			return false;
		}
		return repositoryFolder.exists();
	}

	@Override
	public void doCreateTask(long id, String gitRepository, String newId) {
		log.info("Git HEAD被创建！");
		GitCreateTaskMessage createTaskMessage = new GitCreateTaskMessage();
		createTaskMessage.setRepositoryId(id);
		createTaskMessage.setCommitId(newId);
		createTaskMessage.setFileList(gitFileDAO.getLatestFileList(gitRepository));
		gitCreateTaskPublisher.publishMessage(createTaskMessage);
		log.info("已发布Git仓库创建任务消息至Flux对象！");
	}

	@Override
	public void doUpdateTask(long id, String showName, String gitRepository, boolean sendEmail, String oldId, String newId) {
		log.info("Git HEAD被修改！");
		if (StrUtil.isEmpty(newId) || newId.equals(oldId)) {
			log.info("实际没有变化，无需修改！");
			return;
		}
		// 获取差异
		List<DiffEntry> diffs = gitFileDAO.compareDiffBetweenTwoCommits(gitRepository, oldId, newId);
		if (diffs.isEmpty()) {
			log.info("没有需要更新的差异！");
			return;
		}
		// 组装任务消息对象
		GitUpdateTaskMessage updateTaskMessage = new GitUpdateTaskMessage();
		updateTaskMessage.setRepositoryId(id);
		updateTaskMessage.setCommitId(newId);
		updateTaskMessage.setDiffs(ArticleDifference.toArticleDiff(diffs));
		gitUpdateTaskPublisher.publishMessage(updateTaskMessage);
		log.info("已发布Git仓库更新任务消息至Flux对象！");
		// 准备进行邮件通知
		if (sendEmail) {
			// 获取收藏这个文集的用户
			List<User> starUsers = userDAO.getByStarAnthology(id);
			List<String> emailList = starUsers.stream()
					// 过滤得到订阅更新邮件的用户
					.filter(user -> user.getSetting().getReceiveUpdateEmail())
					.map(User::getEmail).toList();
			// 无人订阅通知则不发送消息
			if (emailList.isEmpty()) {
				return;
			}
			// 准备邮件任务消息
			UpdateEmailMessage message = new UpdateEmailMessage();
			message.setTitle("GitDocument · " + systemSettingDAO.get(ORGANIZATION_NAME) + " - 文集更新通知");
			message.setName(showName);
			message.setCommitMessage(gitCommitDAO.getHeadCommit(gitRepository).getFullMessage());
			message.setDiffEntries(ArticleDifference.toArticleDiff(diffs));
			message.setEmailList(emailList);
			// 异步发送
			emailService.sendAnthologyUpdateNotify(message);
		}
	}

	@Override
	public void checkGitRepositoryUpdate(Anthology anthology) {
		// 获取本地仓库的commit信息
		String localCommitId = gitCommitDAO.getHeadCommitId(anthology.getRepoPath());
		// 如果说本地仓库commit为空，说明是刚创建的仓库，则不进行对比
		if (StrUtil.isEmpty(localCommitId)) {
			log.warn("本地仓库：{}的commitId为空！可能是刚创建的仓库，跳过比对！", anthology.getName());
			return;
		}
		// 如果数据库中commit为空，说明需要执行创建任务
		if (StrUtil.isEmpty(anthology.getLatestCommit())) {
			log.warn("发现本地仓库：{}在数据库中的commitId为空，进行创建操作...", anthology.getName());
			doCreateTask(anthology.getId(), anthology.getRepoPath(), localCommitId);
			return;
		}
		// 如果只是单纯的两者不同，说明需要进行更新同步操作
		if (!anthology.getLatestCommit().equals(localCommitId)) {
			log.warn("发现本地仓库：{}与数据库的commit不同，进行更新操作...", anthology.getName());
			doUpdateTask(anthology.getId(), anthology.getShowName(), anthology.getRepoPath(), false, anthology.getLatestCommit(), localCommitId);
		}
	}

}