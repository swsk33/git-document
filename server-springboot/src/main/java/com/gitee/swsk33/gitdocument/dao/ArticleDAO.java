package com.gitee.swsk33.gitdocument.dao;

import com.gitee.swsk33.gitdocument.dataobject.Article;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ArticleDAO {

	/**
	 * 添加文章记录至数据库
	 *
	 * @param article 文章对象
	 * @return 插入记录条数
	 */
	int add(Article article);

	/**
	 * 批量插入文章记录
	 *
	 * @param articles 文章列表
	 * @return 插入记录条数
	 */
	int batchAdd(List<Article> articles);

	/**
	 * 删除文章
	 *
	 * @param id 要删除的文章id
	 * @return 删除记录条数
	 */
	int delete(long id);

	/**
	 * 根据文章文件路径删除文章
	 *
	 * @param path 文章文件路径
	 * @return 删除记录条数
	 */
	int deleteByPath(String path);

	/**
	 * 修改文章信息
	 *
	 * @param article 文章对象
	 * @return 修改记录条数
	 */
	int update(Article article);

	/**
	 * 根据id获取文章
	 *
	 * @param id 文章id
	 * @return 文章对象
	 */
	Article getById(long id);

	/**
	 * 根据文件路径获取文章
	 *
	 * @param path 文章文件路径
	 * @return 文章对象
	 */
	Article getByPath(String path);

	/**
	 * 获取一个文集中全部的文章
	 *
	 * @param anthologyId 文集id
	 * @return 文集中的文章列表
	 */
	List<Article> getByAnthology(long anthologyId);

}