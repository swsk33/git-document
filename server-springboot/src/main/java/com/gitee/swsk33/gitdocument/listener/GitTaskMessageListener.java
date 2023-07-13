package com.gitee.swsk33.gitdocument.listener;

import com.gitee.swsk33.gitdocument.cache.ArticleTreeCache;
import com.gitee.swsk33.gitdocument.dao.AnthologyDAO;
import com.gitee.swsk33.gitdocument.dao.ArticleDAO;
import com.gitee.swsk33.gitdocument.dataobject.Anthology;
import com.gitee.swsk33.gitdocument.dataobject.Article;
import com.gitee.swsk33.gitdocument.message.GitCreateTaskMessage;
import com.gitee.swsk33.gitdocument.message.GitUpdateTaskMessage;
import com.gitee.swsk33.gitdocument.model.ArticleDirectory;
import com.gitee.swsk33.gitdocument.param.CommonValue;
import com.gitee.swsk33.gitdocument.strategy.context.FileChangeStrategyContext;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.diff.DiffEntry;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 接收Git仓库任务的消息订阅者
 */
@Slf4j
@Component
public class GitTaskMessageListener {

	@Autowired
	private AnthologyDAO anthologyDAO;

	@Autowired
	private ArticleDAO articleDAO;

	@Autowired
	private ArticleTreeCache articleTreeCache;

	/**
	 * Git的创建消息任务处理
	 */
	@RabbitListener(queues = CommonValue.MessageQueue.GIT_CREATE_TASK_QUEUE)
	public void getCreateTask(GitCreateTaskMessage message) {
		log.info("接收到Git仓库创建任务！");
		Anthology anthology = anthologyDAO.getById(message.getRepositoryId());
		List<Article> articles = new ArrayList<>();
		message.getFileList().forEach(path -> {
			Article article = new Article();
			article.setId(SnowflakeIdGenerator.getSnowflakeId());
			article.setAnthology(anthology);
			article.setFilePath(path);
			articles.add(article);
		});
		if (articles.size() == 0) {
			log.info("没有可录入的新文件，退出！");
			return;
		}
		articleDAO.batchAdd(articles);
		log.info("已将" + articles.size() + "个文件信息扫描进数据库！");
		// 刷新文集仓库信息
		anthology.setLatestCommitId(message.getCommitId());
		anthologyDAO.update(anthology);
		log.info("已完成对文集仓库：" + anthology.getName() + "的创建任务！");
		// 刷新目录树缓存
		ArticleDirectory directory = new ArticleDirectory(articleDAO.getByAnthology(anthology.getId()));
		articleTreeCache.setOrAdd(anthology.getId(), directory);
		log.info("已完成对文集仓库：" + anthology.getName() + "的文章目录树缓存刷新！");
	}

	/**
	 * Git的更新消息任务处理
	 */
	@RabbitListener(queues = CommonValue.MessageQueue.GIT_UPDATE_TASK_QUEUE)
	public void getUpdateTask(GitUpdateTaskMessage message) {
		log.info("接收到Git仓库更新任务！");
		// 过滤掉内容更改的差异
		// 然后对每个差异执行不同策略（策略模式）
		message.getDiffs().stream().filter(diff -> diff.getChangeType() != DiffEntry.ChangeType.MODIFY).forEach(diff -> {
			FileChangeStrategyContext.executeStrategy(message.getRepositoryId(), diff);
		});
		// 完成更新后刷新文集仓库信息
		Anthology anthology = anthologyDAO.getById(message.getRepositoryId());
		anthology.setLatestCommitId(message.getCommitId());
		anthologyDAO.update(anthology);
		log.info("已完成对文集仓库：" + anthology.getName() + "的信息更新任务！");
		// 刷新目录树缓存
		ArticleDirectory directory = new ArticleDirectory(articleDAO.getByAnthology(anthology.getId()));
		articleTreeCache.setOrAdd(anthology.getId(), directory);
		log.info("已完成对文集仓库：" + anthology.getName() + "的文章目录树缓存刷新！");
	}

}