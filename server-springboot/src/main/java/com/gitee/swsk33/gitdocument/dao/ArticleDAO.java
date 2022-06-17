package com.gitee.swsk33.gitdocument.dao;

import com.gitee.swsk33.gitdocument.dataobject.Article;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ArticleDAO {

	int batchAdd(List<Article> articles);

	int batchDelete(List<Long> ids);

	int update(Article article);

	Article getById(long id);

	Article getByPath(String path);

	List<Article> getByAnthology(long anthologyId);

}