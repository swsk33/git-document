package com.gitee.swsk33.gitdocument.listener;

import com.gitee.swsk33.gitdocument.dao.AnthologyDAO;
import com.gitee.swsk33.gitdocument.dao.UserDAO;
import com.gitee.swsk33.gitdocument.dataobject.Anthology;
import com.gitee.swsk33.gitdocument.dataobject.User;
import com.gitee.swsk33.gitdocument.message.GitCreateTaskMessage;
import com.gitee.swsk33.gitdocument.message.GitUpdateTaskMessage;
import com.gitee.swsk33.gitdocument.message.UpdateEmailMessage;
import com.gitee.swsk33.gitdocument.model.ArticleDiff;
import com.gitee.swsk33.gitdocument.param.CommonValue;
import com.gitee.swsk33.gitdocument.property.ConfigProperties;
import com.gitee.swsk33.gitdocument.util.GitFileUtils;
import com.gitee.swsk33.gitdocument.util.GitRepositoryUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.eclipse.jgit.diff.DiffEntry;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;

/**
 * Git仓库头指针监听器
 */
@Data
@Slf4j
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Component
public class GitRepositoryListener extends FileAlterationListenerAdaptor {

	/**
	 * 仓库id，对应一个被监听的目录
	 */
	private long id;

	/**
	 * 被监听的路径
	 */
	private String path;

	@Autowired
	private AnthologyDAO anthologyDAO;

	@Autowired
	private UserDAO userDAO;

	@Autowired
	private RabbitTemplate rabbitTemplate;

	@Autowired
	private ConfigProperties configProperties;

	@Override
	public void onStart(FileAlterationObserver observer) {
		super.onStart(observer);
	}

	@Override
	public void onDirectoryCreate(File directory) {

	}

	@Override
	public void onDirectoryChange(File directory) {

	}

	@Override
	public void onDirectoryDelete(File directory) {

	}

	@Override
	public void onFileCreate(File file) {
		log.info("Git分支被创建！");
		try {
			GitCreateTaskMessage createTaskMessage = new GitCreateTaskMessage();
			createTaskMessage.setRepositoryId(id);
			createTaskMessage.setCommitId(GitRepositoryUtils.getHeadCommitId(path));
			createTaskMessage.setFileList(GitFileUtils.getLatestFileList(path));
			rabbitTemplate.convertAndSend(CommonValue.MessageQueue.GIT_TASK_TOPIC_EXCHANGE, CommonValue.RabbitMQRoutingKey.GIT_CREATE, createTaskMessage);
			log.info("已发布Git仓库创建任务消息至消息队列！");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onFileChange(File file) {
		log.info("Git HEAD被修改！");
		try {
			// 从数据库获取最新仓库的commitId
			Anthology getAnthology = anthologyDAO.getById(id);
			String oldId = getAnthology.getLatestCommitId();
			// 获取文件系统中仓库新的commitId
			String newId = GitRepositoryUtils.getHeadCommitId(path);
			if (newId.equals(oldId)) {
				log.info("实际没有变化，无需修改！");
				return;
			}
			// 获取差异
			List<DiffEntry> diffs = GitFileUtils.compareDiffBetweenTwoCommits(path, oldId, newId);
			if (diffs.size() == 0) {
				log.info("没有需要更新的差异！");
				return;
			}
			// 组装任务消息对象
			GitUpdateTaskMessage updateTaskMessage = new GitUpdateTaskMessage();
			updateTaskMessage.setRepositoryId(id);
			updateTaskMessage.setCommitId(newId);
			updateTaskMessage.setDiffs(ArticleDiff.toArticleDiff(diffs));
			rabbitTemplate.convertAndSend(CommonValue.MessageQueue.GIT_TASK_TOPIC_EXCHANGE, CommonValue.RabbitMQRoutingKey.GIT_UPDATE, updateTaskMessage);
			log.info("已发布Git仓库更新任务消息至消息队列！");
			// 通知收藏这个文集的用户
			List<User> starUser = userDAO.getByStarAnthology(id);
			starUser.stream().filter(user -> user.getSetting().getReceiveUpdateEmail()).forEach(user -> {
				UpdateEmailMessage message = new UpdateEmailMessage();
				message.setTitle("GitDocument · " + configProperties.getOrganizationName() + " - 文集更新通知");
				message.setEmail(user.getEmail());
				message.setName(getAnthology.getShowName());
				try {
					message.setCommitMessage(GitRepositoryUtils.getHeadCommitMessage(path));
				} catch (Exception e) {
					e.printStackTrace();
				}
				message.setDiffEntries(ArticleDiff.toArticleDiff(diffs));
				rabbitTemplate.convertAndSend(CommonValue.MessageQueue.EMAIL_TOPIC_EXCHANGE, CommonValue.RabbitMQRoutingKey.UPDATE_EMAIL, message);
				log.info("已发布邮件通知任务到消息队列！通知用户名：" + user.getUsername());
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onFileDelete(File file) {

	}

	@Override
	public void onStop(FileAlterationObserver observer) {
		super.onStop(observer);
	}

}