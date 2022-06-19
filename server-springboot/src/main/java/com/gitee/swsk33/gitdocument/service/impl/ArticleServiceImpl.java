package com.gitee.swsk33.gitdocument.service.impl;

import com.gitee.swsk33.gitdocument.dao.ArticleDAO;
import com.gitee.swsk33.gitdocument.dataobject.Article;
import com.gitee.swsk33.gitdocument.model.Result;
import com.gitee.swsk33.gitdocument.service.ArticleService;
import com.gitee.swsk33.gitdocument.util.GitFileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleServiceImpl implements ArticleService {

	@Autowired
	private ArticleDAO articleDAO;

	@Override
	public Result<Article> getById(long id) throws Exception {
		Result<Article> result = new Result<>();
		Article getArticle = articleDAO.getById(id);
		if (getArticle == null) {
			result.setResultFailed("文章不存在！");
			return result;
		}
		// 去文章仓库中取出文章内容
		getArticle.setContent(GitFileUtils.getFileContentInLatestCommit(getArticle.getAnthology().getRepoPath(), getArticle.getFilePath()));
		result.setResultSuccess("查找文章成功！", getArticle);
		return result;
	}

	@Override
	public Result<List<Article>> getByAnthology(long anthologyId) {
		Result<List<Article>> result = new Result<>();
		List<Article> articles = articleDAO.getByAnthology(anthologyId);
		// 这里仅仅返回仓库的文章列表，不返回每个文章的内容
		result.setResultSuccess("查询文章成功！", articles);
		return result;
	}

}