package com.gitee.swsk33.gitdocument.param;

/**
 * 存放常用Redis键的常量列表
 */
public class RedisKey {

	/**
	 * 前缀
	 */
	private static final String PREFIX = "git-doc:";

	/**
	 * 文集id和其文章目录树的对应哈希表名
	 */
	public static final String ANTHOLOGY_ID_ARTICLE_TREE_MAP = PREFIX + "anthology-article-map";

}