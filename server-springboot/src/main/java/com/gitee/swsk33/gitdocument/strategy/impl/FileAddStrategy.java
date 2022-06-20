package com.gitee.swsk33.gitdocument.strategy.impl;

import com.gitee.swsk33.gitdocument.dao.ArticleDAO;
import com.gitee.swsk33.gitdocument.dataobject.Anthology;
import com.gitee.swsk33.gitdocument.dataobject.Article;
import com.gitee.swsk33.gitdocument.strategy.GitFileChangeStrategy;
import com.gitee.swsk33.gitdocument.util.SnowflakeIdGenerator;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.diff.DiffEntry;
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
	public void doUpdate(long repositoryId, DiffEntry diffEntry) {
		// 把文件信息录入数据库
		Anthology anthology = new Anthology();
		anthology.setId(repositoryId);
		Article article = new Article();
		article.setId(SnowflakeIdGenerator.getSnowflakeId());
		article.setFilePath(diffEntry.getNewPath());
		article.setAnthology(anthology);
		articleDAO.add(article);
		log.info("增加文件：" + diffEntry.getNewPath());
	}

}