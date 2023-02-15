package com.gitee.swsk33.gitdocument.task;

import com.gitee.swsk33.gitdocument.cache.ArticleTreeCache;
import com.gitee.swsk33.gitdocument.dao.AnthologyDAO;
import com.gitee.swsk33.gitdocument.dao.ArticleDAO;
import com.gitee.swsk33.gitdocument.dataobject.Anthology;
import com.gitee.swsk33.gitdocument.model.ArticleDirectory;
import com.gitee.swsk33.gitdocument.strategy.context.FileChangeStrategyContext;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.diff.DiffEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * HEAD被修改时触发的更新任务
 */
@Getter
@Setter
@Slf4j
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Component
public class GitUpdateTask implements Runnable {

	/**
	 * 仓库id
	 */
	private long repositoryId;

	/**
	 * 发生更改后的commitId
	 */
	private String commitId;

	/**
	 * 发生更改的文件列表
	 */
	private List<DiffEntry> diffEntries;

	@Autowired
	private AnthologyDAO anthologyDAO;

	@Autowired
	private ArticleDAO articleDAO;

	@Autowired
	private ArticleTreeCache articleTreeCache;

	@Override
	public void run() {
		log.info("正在修改数据库文件信息...");
		for (DiffEntry entry : diffEntries) {
			FileChangeStrategyContext.executeStrategy(repositoryId, entry);
		}
		// 完成更新后，更新文集仓库的最新commitId
		Anthology anthology = anthologyDAO.getById(repositoryId);
		anthology.setLatestCommitId(commitId);
		anthologyDAO.update(anthology);
		log.info("已完成文集仓库信息更新！");
		// 刷新文章目录树缓存
		ArticleDirectory directory = new ArticleDirectory(articleDAO.getByAnthology(repositoryId));
		articleTreeCache.setOrAdd(repositoryId, directory);
		log.info("已完成文集仓库文章目录树缓存刷新！");
	}

}