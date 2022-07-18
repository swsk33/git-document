package com.gitee.swsk33.gitdocument.model;

import com.gitee.swsk33.gitdocument.param.FileIndexType;
import lombok.Data;

/**
 * 文章文件索引，不存放文章信息，只用于给前端解析的索引信息
 */
@Data
public class ArticleFile {

	/**
	 * 索引类型为文件
	 */
	private final FileIndexType type = FileIndexType.FILE;

	/**
	 * 文章id
	 */
	private long id;

	/**
	 * 文章名
	 */
	private String name;

}