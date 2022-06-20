package com.gitee.swsk33.gitdocument.strategy.impl;


import com.gitee.swsk33.gitdocument.dao.ArticleDAO;
import com.gitee.swsk33.gitdocument.dataobject.Article;
import com.gitee.swsk33.gitdocument.strategy.GitFileChangeStrategy;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.diff.DiffEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 文件重命名策略
 */
@Slf4j
@Component
public class FileRenameStrategy implements GitFileChangeStrategy {

	@Autowired
	private ArticleDAO articleDAO;

	@Override
	public void doUpdate(long repositoryId, DiffEntry diffEntry) {
		// 修改数据库中对应的记录
		Article oldArticle = articleDAO.getByPath(diffEntry.getOldPath());
		// 若重命名为其它则删除
		if (!diffEntry.getNewPath().endsWith(".md")) {
			articleDAO.delete(oldArticle.getId());
			log.info("删除一个非md格式的变更文件");
			return;
		}
		// 否则，修改至数据库
		oldArticle.setFilePath(diffEntry.getNewPath());
		articleDAO.update(oldArticle);
		log.info("文章重命名：" + diffEntry.getOldPath() + " -> " + diffEntry.getNewPath());
	}

}