package com.gitee.swsk33.gitdocument.dao;

import com.gitee.swsk33.gitdocument.dataobject.Article;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ArticleDAO {

	int add(Article article);

	int batchAdd(List<Article> articles);

	int delete(long id);

	int deleteByPath(String path);

	int update(Article article);

	Article getById(long id);

	Article getByPath(String path);

	List<Article> getByAnthology(long anthologyId);

}