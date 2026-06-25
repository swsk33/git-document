package com.gitee.swsk33.gitdocument.subscriber;

import com.gitee.swsk33.gitdocument.broker.GitMessageBroker;
import com.gitee.swsk33.gitdocument.cache.ArticleTreeCache;
import com.gitee.swsk33.gitdocument.dao.AnthologyDAO;
import com.gitee.swsk33.gitdocument.dao.ArticleDAO;
import com.gitee.swsk33.gitdocument.dao.SystemSettingDAO;
import com.gitee.swsk33.gitdocument.dao.UserDAO;
import com.gitee.swsk33.gitdocument.dataobject.Anthology;
import com.gitee.swsk33.gitdocument.dataobject.User;
import com.gitee.swsk33.gitdocument.gitdao.GitCommitDAO;
import com.gitee.swsk33.gitdocument.model.ArticleDirectory;
import com.gitee.swsk33.gitdocument.model.GitUpdateTaskMessage;
import com.gitee.swsk33.gitdocument.model.UpdateArticleNotifyEmailMessage;
import com.gitee.swsk33.gitdocument.model.prototype.GitTaskMessage;
import com.gitee.swsk33.gitdocument.service.ArticleEmailService;
import com.gitee.swsk33.gitdocument.strategy.context.FileChangeStrategyContext;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.diff.DiffEntry;
import org.reactivestreams.Subscription;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.BaseSubscriber;

import java.util.List;

import static com.gitee.swsk33.gitdocument.param.SystemSettingKey.ORGANIZATION_NAME;

/**
 * 订阅接收并处理 Git 仓库更新消息的订阅者
 */
@Slf4j
@Component
public class GitUpdateTaskSubscriber extends BaseSubscriber<GitTaskMessage> implements InitializingBean {

	@Autowired
	private AnthologyDAO anthologyDAO;

	@Autowired
	private ArticleDAO articleDAO;

	@Autowired
	private UserDAO userDAO;

	@Autowired
	private ArticleTreeCache articleTreeCache;

	@Autowired
	private FileChangeStrategyContext fileChangeStrategyContext;

	@Autowired
	private ArticleEmailService emailService;

	@Autowired
	private SystemSettingDAO systemSettingDAO;

	@Autowired
	private GitCommitDAO gitCommitDAO;

	@Autowired
	private GitMessageBroker messageBroker;

	@Override
	public void afterPropertiesSet() {
		// 订阅更新消息
		messageBroker.subscribe(this, GitUpdateTaskMessage.class);
		log.info("已订阅并准备接收Git更新消息");
	}

	/**
	 * 开始订阅时会执行的方法
	 */
	@Override
	public void hookOnSubscribe(Subscription subscription) {
		log.info("开始订阅Git更新消息！");
		request(1);
	}

	/**
	 * 每次订阅操作（获取到订阅元素时）会执行的方法
	 *
	 * @param value 当前订阅到的元素
	 */
	@Override
	public void hookOnNext(GitTaskMessage value) {
		log.info("接收到Git仓库更新任务！");
		Anthology anthology = anthologyDAO.getAnthologyByPath(value.getRepoPath());
		if (anthology == null) {
			log.info("不存在路径为{}的文集仓库", value.getRepoPath());
			request(1);
			return;
		}
		// 过滤掉内容更改的差异
		// 然后对每个差异执行不同策略（策略模式）
		if (value instanceof GitUpdateTaskMessage message) {
			message.getDiffs().stream()
					.filter(diff -> diff.getChangeType() != DiffEntry.ChangeType.MODIFY)
					.forEach(diff -> fileChangeStrategyContext.executeStrategy(anthology.getId(), diff));
			// 完成更新后刷新文集仓库信息
			anthology.setLatestCommit(message.getCommitId());
			anthologyDAO.update(anthology);
			log.info("已完成对文集仓库：{}的信息更新任务！", anthology.getName());
			// 刷新目录树缓存
			ArticleDirectory directory = new ArticleDirectory(articleDAO.getByAnthologyId(anthology.getId()));
			articleTreeCache.setOrAdd(anthology.getId(), directory);
			log.info("已完成对文集仓库：{}的文章目录树缓存刷新！", anthology.getName());
			// 对收藏了该文集且开启了更新通知的用户，发送邮件通知
			if (message.isSendEmail()) {
				// 获取收藏了该文集的用户
				List<User> starUsers = userDAO.getByStarAnthology(anthology.getId());
				List<String> emailList = starUsers.stream()
						// 过滤得到开启了邮件通知的用户
						.filter(user -> user.getSetting().getReceiveUpdateEmail())
						.map(User::getEmail).toList();
				// 邮件列表非空才发消息
				if (!emailList.isEmpty()) {
					// 创建消息
					UpdateArticleNotifyEmailMessage notifyMessage = new UpdateArticleNotifyEmailMessage();
					notifyMessage.setTitle(String.format("GitDocument · %s - 文集更新通知", systemSettingDAO.get(ORGANIZATION_NAME)));
					notifyMessage.setName(anthology.getShowName());
					notifyMessage.setCommitMessage(gitCommitDAO.getHeadCommit(anthology.getRepoPath()).getFullMessage());
					notifyMessage.setDiffEntries(message.getDiffs());
					notifyMessage.setEmailList(emailList);
					// 异步发送
					emailService.sendAnthologyUpdateNotify(notifyMessage);
				}
			}
		} else {
			log.warn("消息类型错误，本次不进行任何更新任务！");
		}
		// 等待订阅下一个任务
		request(1);
	}

}