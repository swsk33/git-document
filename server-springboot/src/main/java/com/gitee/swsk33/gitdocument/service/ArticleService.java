package com.gitee.swsk33.gitdocument.service;

import com.gitee.swsk33.gitdocument.dataobject.Article;
import com.gitee.swsk33.gitdocument.model.Result;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ArticleService {

	/**
	 * 根据文章id获取一个文章对象
	 *
	 * @param id 文章id
	 */
	Result<Article> getById(long id) throws Exception;

	/**
	 * 获取一个文集的所有文章
	 *
	 * @param anthologyId 文集的id
	 */
	Result<List<Article>> getByAnthology(long anthologyId);

}