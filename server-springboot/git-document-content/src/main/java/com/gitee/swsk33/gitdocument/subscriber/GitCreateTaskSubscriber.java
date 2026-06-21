package com.gitee.swsk33.gitdocument.subscriber;

import cn.hutool.core.util.IdUtil;
import com.gitee.swsk33.gitdocument.cache.ArticleTreeCache;
import com.gitee.swsk33.gitdocument.dao.AnthologyDAO;
import com.gitee.swsk33.gitdocument.dao.ArticleDAO;
import com.gitee.swsk33.gitdocument.dataobject.Anthology;
import com.gitee.swsk33.gitdocument.dataobject.Article;
import com.gitee.swsk33.gitdocument.model.ArticleDirectory;
import com.gitee.swsk33.gitdocument.model.GitCreateTaskMessage;
import com.gitee.swsk33.gitdocument.model.prototype.GitTaskMessage;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Subscription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.BaseSubscriber;

import java.util.ArrayList;
import java.util.List;

/**
 * 订阅接收并处理 Git 仓库创建消息的订阅者
 */
@Slf4j
@Component
public class GitCreateTaskSubscriber extends BaseSubscriber<GitTaskMessage> {

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
		log.info("开始订阅Git创建消息！");
		request(1);
	}

	/**
	 * 每次订阅操作（获取到订阅元素时）会执行的方法
	 *
	 * @param value 当前订阅到的元素
	 */
	@Override
	public void hookOnNext(GitTaskMessage value) {
		// 解析创建任务
		log.info("订阅到到Git仓库创建任务！");
		// 获取对应文集仓库
		Anthology anthology = anthologyDAO.getAnthologyByPath(value.getRepoPath());
		if (anthology == null) {
			log.error("不存在路径为{}的文集仓库", value.getRepoPath());
			request(1);
			return;
		}
		// 类型校验并向下转型
		if (value instanceof GitCreateTaskMessage message) {
			List<Article> articles = new ArrayList<>();
			// 将创建消息中新增文集解析为 Article 对象
			message.getFileList().forEach(path -> {
				Article article = new Article();
				article.setId(IdUtil.getSnowflakeNextId());
				article.setAnthologyId(anthology.getId());
				article.setFilePath(path);
				articles.add(article);
			});
			if (articles.isEmpty()) {
				log.info("没有可录入的新文件，退出！");
				return;
			}
			articleDAO.insertBatch(articles);
			log.info("已将{}个文件信息扫描进数据库！", articles.size());
			// 刷新文集仓库信息
			anthology.setLatestCommit(value.getCommitId());
			anthologyDAO.update(anthology);
			log.info("已完成对文集仓库：{}的创建任务！", anthology.getName());
			// 刷新目录树缓存
			ArticleDirectory directory = new ArticleDirectory(articles);
			articleTreeCache.setOrAdd(anthology.getId(), directory);
			log.info("已完成对文集仓库：{}的文章目录树缓存刷新！", anthology.getName());
		} else {
			log.warn("消息类型错误，本次不进行任何创建任务！");
		}
		// 等待订阅下一个任务
		request(1);
	}

}