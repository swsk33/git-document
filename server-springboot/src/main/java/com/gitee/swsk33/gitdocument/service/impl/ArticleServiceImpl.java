package com.gitee.swsk33.gitdocument.service.impl;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.gitee.swsk33.gitdocument.cache.ArticleTreeCache;
import com.gitee.swsk33.gitdocument.dao.ArticleDAO;
import com.gitee.swsk33.gitdocument.dataobject.Article;
import com.gitee.swsk33.gitdocument.model.ArticleDirectory;
import com.gitee.swsk33.gitdocument.model.Result;
import com.gitee.swsk33.gitdocument.param.CommonValue;
import com.gitee.swsk33.gitdocument.service.ArticleService;
import com.gitee.swsk33.gitdocument.util.GitFileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ArticleServiceImpl implements ArticleService {

	@Autowired
	private ArticleDAO articleDAO;

	@Autowired
	private ArticleTreeCache articleTreeCache;

	@SaCheckPermission(CommonValue.Permission.BROWSE_ARTICLE)
	@Override
	public Result<Article> getById(long id) throws Exception {
		Result<Article> result = new Result<>();
		Article getArticle = articleDAO.getById(id);
		if (getArticle == null) {
			result.setResultFailed("文章不存在！");
			return result;
		}
		// 去文章仓库中取出文章内容
		getArticle.setContent(GitFileUtils.getFileTextContentInLatestCommit(getArticle.getAnthology().getRepoPath(), getArticle.getFilePath()));
		result.setResultSuccess("查找文章成功！", getArticle);
		return result;
	}

	@SaCheckPermission(CommonValue.Permission.BROWSE_ARTICLE)
	@Override
	public Result<ArticleDirectory> getByAnthology(long anthologyId) {
		Result<ArticleDirectory> result = new Result<>();
		// 先去Redis里面取
		ArticleDirectory getDirectory = articleTreeCache.getById(anthologyId);
		// 若Redis为空再去MySQL取出文章列表并进行目录树转换
		if (getDirectory == null) {
			getDirectory = new ArticleDirectory(articleDAO.getByAnthology(anthologyId));
			// 存入缓存
			articleTreeCache.setOrAdd(anthologyId, getDirectory);
		}
		result.setResultSuccess("获取文章目录树成功！", getDirectory);
		// 仅仅返回文章列表，不返回每个文章本身
		return result;
	}

}