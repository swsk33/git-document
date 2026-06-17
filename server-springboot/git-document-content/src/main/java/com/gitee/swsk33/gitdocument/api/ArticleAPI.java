package com.gitee.swsk33.gitdocument.api;

import com.gitee.swsk33.gitdocument.dataobject.Article;
import com.gitee.swsk33.gitdocument.model.ArticleDirectory;
import com.gitee.swsk33.gitdocument.model.Result;
import com.gitee.swsk33.gitdocument.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/article")
public class ArticleAPI {

	@Autowired
	private ArticleService articleService;

	@GetMapping("/get/{id}")
	public Result<Article> get(@PathVariable long id) throws Exception {
		return articleService.getById(id);
	}

	@GetMapping("/get-article-list/{anthologyId}")
	public Result<ArticleDirectory> getList(@PathVariable long anthologyId) {
		return articleService.getByAnthology(anthologyId);
	}

}