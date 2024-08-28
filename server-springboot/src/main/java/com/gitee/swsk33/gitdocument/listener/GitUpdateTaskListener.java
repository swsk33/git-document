package com.gitee.swsk33.gitdocument.listener;

import com.gitee.swsk33.gitdocument.cache.ArticleTreeCache;
import com.gitee.swsk33.gitdocument.dao.AnthologyDAO;
import com.gitee.swsk33.gitdocument.dao.ArticleDAO;
import com.gitee.swsk33.gitdocument.dataobject.Anthology;
import com.gitee.swsk33.gitdocument.model.ArticleDirectory;
import com.gitee.swsk33.gitdocument.model.GitUpdateTaskMessage;
import com.gitee.swsk33.gitdocument.model.prototype.GitTaskMessage;
import com.gitee.swsk33.gitdocument.strategy.context.FileChangeStrategyContext;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.diff.DiffEntry;
import org.reactivestreams.Subscription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;
import reactor.core.publisher.BaseSubscriber;

/**
 * 订阅接收Git仓库更新消息的订阅者
 */
@Slf4j
@Component
@DependsOn("fileChangeStrategyContext")
public class GitUpdateTaskListener extends BaseSubscriber<GitTaskMessage> {

	@Autowired
	private AnthologyDAO anthologyDAO;

	@Autowired
	private ArticleDAO articleDAO;

	@Autowired
	private ArticleTreeCache articleTreeCache;

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
		// 过滤掉内容更改的差异
		// 然后对每个差异执行不同策略（策略模式）
		((GitUpdateTaskMessage) value).getDiffs().stream().filter(diff -> diff.getChangeType() != DiffEntry.ChangeType.MODIFY).forEach(diff -> FileChangeStrategyContext.executeStrategy(value.getRepositoryId(), diff));
		// 完成更新后刷新文集仓库信息
		Anthology anthology = anthologyDAO.selectOneById(value.getRepositoryId());
		anthology.setLatestCommit(value.getCommitId());
		anthologyDAO.update(anthology);
		log.info("已完成对文集仓库：{}的信息更新任务！", anthology.getName());
		// 刷新目录树缓存
		ArticleDirectory directory = new ArticleDirectory(articleDAO.getByAnthologyId(value.getRepositoryId()));
		articleTreeCache.setOrAdd(anthology.getId(), directory);
		log.info("已完成对文集仓库：{}的文章目录树缓存刷新！", anthology.getName());
		// 等待订阅下一个任务
		request(1);
	}

}