package com.gitee.swsk33.gitdocument.git.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.gitee.swsk33.gitdocument.dao.SystemSettingDAO;
import com.gitee.swsk33.gitdocument.dao.UserDAO;
import com.gitee.swsk33.gitdocument.dataobject.User;
import com.gitee.swsk33.gitdocument.git.GitCommitDAO;
import com.gitee.swsk33.gitdocument.git.GitFileDAO;
import com.gitee.swsk33.gitdocument.git.GitRepositoryInfoDAO;
import com.gitee.swsk33.gitdocument.message.GitCreateTaskMessage;
import com.gitee.swsk33.gitdocument.message.GitUpdateTaskMessage;
import com.gitee.swsk33.gitdocument.message.UpdateEmailMessage;
import com.gitee.swsk33.gitdocument.model.ArticleDiff;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.diff.DiffEntry;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;

import static com.gitee.swsk33.gitdocument.param.RabbitMessageQueue.Exchange.EMAIL_TOPIC_EXCHANGE;
import static com.gitee.swsk33.gitdocument.param.RabbitMessageQueue.Exchange.GIT_TASK_TOPIC_EXCHANGE;
import static com.gitee.swsk33.gitdocument.param.RabbitMessageQueue.RoutingKey.*;
import static com.gitee.swsk33.gitdocument.param.SystemSettingKey.ORGANIZATION_NAME;

@Slf4j
@Component
public class GitRepositoryInfoDAOImpl implements GitRepositoryInfoDAO {

	@Autowired
	private UserDAO userDAO;

	@Autowired
	private SystemSettingDAO systemSettingDAO;

	@Autowired
	private RabbitTemplate rabbitTemplate;

	@Autowired
	private GitCommitDAO gitCommitDAO;

	@Autowired
	private GitFileDAO gitFileDAO;


	@Override
	public boolean initGitBareRepository(String gitRepository) {
		File dir = FileUtil.file(gitRepository);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		try {
			Git.init().setDirectory(dir).setBare(true).call().close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return dir.exists();
	}

	@Override
	public void doCreateTask(long id, String gitRepository, String newId) {
		log.info("Git HEAD被创建！");
		GitCreateTaskMessage createTaskMessage = new GitCreateTaskMessage();
		createTaskMessage.setRepositoryId(id);
		createTaskMessage.setCommitId(newId);
		createTaskMessage.setFileList(gitFileDAO.getLatestFileList(gitRepository));
		rabbitTemplate.convertAndSend(GIT_TASK_TOPIC_EXCHANGE, GIT_CREATE, createTaskMessage);
		log.info("已发布Git仓库创建任务消息至消息队列！");
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
		updateTaskMessage.setDiffs(ArticleDiff.toArticleDiff(diffs));
		rabbitTemplate.convertAndSend(GIT_TASK_TOPIC_EXCHANGE, GIT_UPDATE, updateTaskMessage);
		log.info("已发布Git仓库更新任务消息至消息队列！");
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
			message.setDiffEntries(ArticleDiff.toArticleDiff(diffs));
			message.setEmailList(emailList);
			// 投递到消息队列
			rabbitTemplate.convertAndSend(EMAIL_TOPIC_EXCHANGE, UPDATE_EMAIL, message);
		}
	}

}