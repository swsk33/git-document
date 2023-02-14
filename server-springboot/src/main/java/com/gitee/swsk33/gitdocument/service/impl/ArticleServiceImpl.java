package com.gitee.swsk33.gitdocument.service.impl;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.gitee.swsk33.gitdocument.dao.ArticleDAO;
import com.gitee.swsk33.gitdocument.dataobject.Article;
import com.gitee.swsk33.gitdocument.model.ArticleDirectory;
import com.gitee.swsk33.gitdocument.model.ArticleFile;
import com.gitee.swsk33.gitdocument.model.Result;
import com.gitee.swsk33.gitdocument.param.CommonValue;
import com.gitee.swsk33.gitdocument.service.ArticleService;
import com.gitee.swsk33.gitdocument.util.GitFileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleServiceImpl implements ArticleService {

	@Autowired
	private ArticleDAO articleDAO;

	/**
	 * 扁平字符串路径转为树状结构
	 *
	 * @param articles 传入文章对象列表
	 * @return 转换后的树状结构
	 */
	private ArticleDirectory parseDirectory(List<Article> articles) {
		ArticleDirectory root = new ArticleDirectory();
		for (Article article : articles) {
			String[] paths = article.getFilePath().split("/");
			// 目录指针，用于标识当前遍历的时候进入到了哪个目录中
			ArticleDirectory pointer = root;
			for (int i = 0; i < paths.length; i++) {
				if (i == paths.length - 1) {
					ArticleFile file = new ArticleFile();
					file.setId(article.getId());
					file.setName(paths[i]);
					pointer.getArticles().add(file);
					break;
				}
				if (pointer.getDirectoryByName(paths[i]) == null) {
					ArticleDirectory directory = new ArticleDirectory();
					directory.setName(paths[i]);
					pointer.getDirectories().add(directory);
				}
				// 指针指向下一级目录
				pointer = pointer.getDirectoryByName(paths[i]);
			}
		}
		return root;
	}

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
		getArticle.setContent(GitFileUtils.getFileContentInLatestCommit(getArticle.getAnthology().getRepoPath(), getArticle.getFilePath()));
		result.setResultSuccess("查找文章成功！", getArticle);
		return result;
	}

	@SaCheckPermission(CommonValue.Permission.BROWSE_ARTICLE)
	@Override
	public Result<ArticleDirectory> getByAnthology(long anthologyId) {
		Result<ArticleDirectory> result = new Result<>();
		List<Article> articles = articleDAO.getByAnthology(anthologyId);
		result.setResultSuccess("查找成功！", parseDirectory(articles));
		// 仅仅返回文章列表，不返回每个文章本身
		return result;
	}

}