package com.gitee.swsk33.gitdocument.service.impl;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.gitee.swsk33.gitdocument.cache.ArticleTreeCache;
import com.gitee.swsk33.gitdocument.dao.AnthologyDAO;
import com.gitee.swsk33.gitdocument.dao.ArticleDAO;
import com.gitee.swsk33.gitdocument.dataobject.Article;
import com.gitee.swsk33.gitdocument.gitdao.GitFileDAO;
import com.gitee.swsk33.gitdocument.model.ArticleDirectory;
import com.gitee.swsk33.gitdocument.model.Result;
import com.gitee.swsk33.gitdocument.param.PermissionName;
import com.gitee.swsk33.gitdocument.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ArticleServiceImpl implements ArticleService {

	@Autowired
	private ArticleDAO articleDAO;

	@Autowired
	private AnthologyDAO anthologyDAO;

	@Autowired
	private ArticleTreeCache articleTreeCache;

	@Autowired
	private GitFileDAO gitFileDAO;

	@SaCheckPermission(PermissionName.BROWSE_ARTICLE)
	@Override
	public Result<Article> getById(long id) {
		Article getArticle = articleDAO.selectOneById(id);
		if (getArticle == null) {
			return Result.resultFailed("文章不存在！");
		}
		// 去文章仓库中取出文章内容
		String anthologyPath = anthologyDAO.selectOneById(getArticle.getAnthologyId()).getRepoPath();
		getArticle.setContent(gitFileDAO.getFileTextContentInLatestCommit(anthologyPath, getArticle.getFilePath()));
		return Result.resultSuccess("查找文章成功！", getArticle);
	}

	@SaCheckPermission(PermissionName.BROWSE_ARTICLE)
	@Override
	public Result<ArticleDirectory> getByAnthology(long anthologyId) {
		// 先去Redis里面取
		ArticleDirectory getDirectory = articleTreeCache.getById(anthologyId);
		// 若Redis为空再去数据库取出文章列表并进行目录树转换
		if (getDirectory == null) {
			getDirectory = new ArticleDirectory(articleDAO.getByAnthologyId(anthologyId));
			// 存入缓存
			articleTreeCache.setOrAdd(anthologyId, getDirectory);
		}
		// 仅仅返回文章列表，不返回每个文章本身
		return Result.resultSuccess("获取文章目录树成功！", getDirectory);
	}

}