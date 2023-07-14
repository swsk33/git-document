package com.gitee.swsk33.gitdocument.cache;

import com.gitee.swsk33.gitdocument.annotation.RedisCache;
import com.gitee.swsk33.gitdocument.model.ArticleDirectory;

/**
 * 操作文集文章目录数据的缓存
 */
@RedisCache
public interface ArticleTreeCache {

	/**
	 * 添加或者修改目录缓存
	 *
	 * @param id        文集id
	 * @param directory 目录数据
	 */
	void setOrAdd(long id, ArticleDirectory directory);

	/**
	 * 删除目录缓存
	 *
	 * @param id 文集id
	 */
	void delete(long id);

	/**
	 * 从缓存中获取文集目录数据
	 *
	 * @param id 文集id
	 * @return 文件树目录数据
	 */
	ArticleDirectory getById(long id);

}