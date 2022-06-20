package com.gitee.swsk33.gitdocument.task;

import com.gitee.swsk33.gitdocument.dao.AnthologyDAO;
import com.gitee.swsk33.gitdocument.dao.ArticleDAO;
import com.gitee.swsk33.gitdocument.dataobject.Anthology;
import com.gitee.swsk33.gitdocument.dataobject.Article;
import com.gitee.swsk33.gitdocument.util.SnowflakeIdGenerator;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 新建分支时触发的Git更新任务
 */
@Slf4j
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Component
@Getter
@Setter
public class GitCreateTask implements Runnable {

	@Autowired
	private ArticleDAO articleDAO;

	@Autowired
	private AnthologyDAO anthologyDAO;

	/**
	 * 仓库id
	 */
	private long repositoryId;

	/**
	 * 新建的文件列表
	 */
	private List<String> fileList;

	/**
	 * 发生更改后的commitId
	 */
	private String commitId;

	@Override
	public void run() {
		// 把新的文件列表编入数据库
		log.info("新建文件正在编入数据库...");
		Anthology anthology = anthologyDAO.getById(repositoryId);
		List<Article> articles = new ArrayList<>();
		for (String path : fileList) {
			Article article = new Article();
			article.setId(SnowflakeIdGenerator.getSnowflakeId());
			article.setAnthology(anthology);
			article.setFilePath(path);
			articles.add(article);
		}
		articleDAO.batchAdd(articles);
		log.info("已将" + articles.size() + "个文件信息扫描进数据库！");
		// 刷新文集仓库信息
		anthology.setLatestCommitId(commitId);
		anthologyDAO.update(anthology);
		log.info("已完成文集仓库信息更新！");
	}

}