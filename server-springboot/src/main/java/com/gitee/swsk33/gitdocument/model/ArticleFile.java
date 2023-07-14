package com.gitee.swsk33.gitdocument.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gitee.swsk33.gitdocument.param.FileIndexType;
import com.gitee.swsk33.gitdocument.serializer.LongToStringSerializer;
import lombok.Data;

import java.io.Serializable;

/**
 * 文章文件索引，不存放文章信息，只用于给前端解析的索引信息
 */
@Data
public class ArticleFile implements Serializable {

	/**
	 * 索引类型为文件
	 */
	private final FileIndexType type = FileIndexType.FILE;

	/**
	 * 文章id
	 */
	@JsonSerialize(using = LongToStringSerializer.class)
	private long id;

	/**
	 * 文章名
	 */
	private String name;

}