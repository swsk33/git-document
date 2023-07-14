package com.gitee.swsk33.gitdocument.listener;

import cn.hutool.core.io.watch.Watcher;
import com.gitee.swsk33.gitdocument.dao.AnthologyDAO;
import com.gitee.swsk33.gitdocument.dao.SystemSettingDAO;
import com.gitee.swsk33.gitdocument.dao.UserDAO;
import com.gitee.swsk33.gitdocument.dataobject.Anthology;
import com.gitee.swsk33.gitdocument.dataobject.User;
import com.gitee.swsk33.gitdocument.message.GitCreateTaskMessage;
import com.gitee.swsk33.gitdocument.message.GitUpdateTaskMessage;
import com.gitee.swsk33.gitdocument.message.UpdateEmailMessage;
import com.gitee.swsk33.gitdocument.model.ArticleDiff;
import com.gitee.swsk33.gitdocument.util.GitFileUtils;
import com.gitee.swsk33.gitdocument.util.GitRepositoryUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.diff.DiffEntry;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.nio.file.WatchEvent;
import java.util.List;

import static com.gitee.swsk33.gitdocument.param.RabbitMessageQueue.Exchange.EMAIL_TOPIC_EXCHANGE;
import static com.gitee.swsk33.gitdocument.param.RabbitMessageQueue.Exchange.GIT_TASK_TOPIC_EXCHANGE;
import static com.gitee.swsk33.gitdocument.param.RabbitMessageQueue.RoutingKey.*;
import static com.gitee.swsk33.gitdocument.param.SystemSettingKey.ORGANIZATION_NAME;

/**
 * Git仓库头指针监听器
 */
@Data
@Slf4j
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Component
public class GitRepositoryListener implements Watcher {

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
	private SystemSettingDAO systemSettingDAO;

	@Override
	public void onCreate(WatchEvent<?> watchEvent, Path path) {
		log.info("Git分支被创建！");
		try {
			GitCreateTaskMessage createTaskMessage = new GitCreateTaskMessage();
			createTaskMessage.setRepositoryId(id);
			createTaskMessage.setCommitId(GitRepositoryUtils.getHeadCommitId(path.toAbsolutePath().toString()));
			createTaskMessage.setFileList(GitFileUtils.getLatestFileList(path.toAbsolutePath().toString()));
			rabbitTemplate.convertAndSend(GIT_TASK_TOPIC_EXCHANGE, GIT_CREATE, createTaskMessage);
			log.info("已发布Git仓库创建任务消息至消息队列！");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onModify(WatchEvent<?> watchEvent, Path path) {
		log.info("Git HEAD被修改！");
		try {
			// 从数据库获取最新仓库的commitId
			Anthology getAnthology = anthologyDAO.getById(id);
			String oldId = getAnthology.getLatestCommitId();
			// 获取文件系统中仓库新的commitId
			String newId = GitRepositoryUtils.getHeadCommitId(path.toAbsolutePath().toString());
			if (newId.equals(oldId)) {
				log.info("实际没有变化，无需修改！");
				return;
			}
			// 获取差异
			List<DiffEntry> diffs = GitFileUtils.compareDiffBetweenTwoCommits(path.toAbsolutePath().toString(), oldId, newId);
			if (diffs.size() == 0) {
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
			message.setName(getAnthology.getShowName());
			message.setCommitMessage(GitRepositoryUtils.getHeadCommitMessage(path.toAbsolutePath().toString()));
			message.setDiffEntries(ArticleDiff.toArticleDiff(diffs));
			message.setEmailList(emailList);
			// 投递到消息队列
			rabbitTemplate.convertAndSend(EMAIL_TOPIC_EXCHANGE, UPDATE_EMAIL, message);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onDelete(WatchEvent<?> watchEvent, Path path) {

	}

	@Override
	public void onOverflow(WatchEvent<?> watchEvent, Path path) {

	}

}