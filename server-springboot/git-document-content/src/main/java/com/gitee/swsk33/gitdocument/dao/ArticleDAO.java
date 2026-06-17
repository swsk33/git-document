package com.gitee.swsk33.gitdocument.dao;

import com.gitee.swsk33.gitdocument.dataobject.Article;
import com.mybatisflex.core.BaseMapper;
import com.mybatisflex.core.query.QueryWrapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

import static com.gitee.swsk33.gitdocument.dataobject.table.ArticleTableDef.ARTICLE;

@Mapper
public interface ArticleDAO extends BaseMapper<Article> {

	/**
	 * 根据文章文件路径删除文章
	 *
	 * @param path 文章文件路径
	 * @return 删除记录条数
	 */
	default int deleteByPath(String path) {
		return deleteByQuery(QueryWrapper.create().where(ARTICLE.FILE_PATH.eq(path)));
	}

	/**
	 * 根据文件路径获取文章
	 *
	 * @param path 文章文件路径
	 * @return 文章对象
	 */
	default Article getByPath(String path) {
		return selectOneWithRelationsByQuery(QueryWrapper.create().where(ARTICLE.FILE_PATH.eq(path)));
	}

	/**
	 * 获取一个文集的全部文章
	 *
	 * @param anthologyId 文集id
	 * @return 文章列表
	 */
	default List<Article> getByAnthologyId(long anthologyId) {
		return selectListByQuery(QueryWrapper.create().where(ARTICLE.ANTHOLOGY_ID.eq(anthologyId)));
	}

}