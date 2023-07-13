package com.gitee.swsk33.gitdocument.strategy.impl;

import com.gitee.swsk33.gitdocument.dao.ArticleDAO;
import com.gitee.swsk33.gitdocument.dataobject.Anthology;
import com.gitee.swsk33.gitdocument.dataobject.Article;
import com.gitee.swsk33.gitdocument.model.ArticleDiff;
import com.gitee.swsk33.gitdocument.strategy.GitFileChangeStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 文件增加策略
 */
@Slf4j
@Component
public class FileAddStrategy implements GitFileChangeStrategy {

	@Autowired
	private ArticleDAO articleDAO;

	@Override
	public void doUpdate(long repositoryId, ArticleDiff diff) {
		// 排除掉增加的非md文件
		if (!diff.getNewPath().endsWith(".md")) {
			log.info(diff.getNewPath() + "不是一个md文件，不录入数据库！");
			return;
		}
		// 把文件信息录入数据库
		Anthology anthology = new Anthology();
		anthology.setId(repositoryId);
		Article article = new Article();
		article.setId(SnowflakeIdGenerator.getSnowflakeId());
		article.setFilePath(diff.getNewPath());
		article.setAnthology(anthology);
		articleDAO.add(article);
		log.info("增加文件：" + diff.getNewPath());
	}

}