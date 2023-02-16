package com.gitee.swsk33.gitdocument.strategy.impl;

import com.gitee.swsk33.gitdocument.dao.ArticleDAO;
import com.gitee.swsk33.gitdocument.model.ArticleDiff;
import com.gitee.swsk33.gitdocument.strategy.GitFileChangeStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 文件删除策略
 */
@Slf4j
@Component
public class FileDeleteStrategy implements GitFileChangeStrategy {

	@Autowired
	private ArticleDAO articleDAO;

	@Override
	public void doUpdate(long repositoryId, ArticleDiff diff) {
		// 从数据库删除
		articleDAO.deleteByPath(diff.getOldPath());
		log.info("删除文件：" + diff.getOldPath());
	}

}