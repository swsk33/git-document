package com.gitee.swsk33.gitdocument.dataobject;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gitee.swsk33.gitdocument.serializer.LongToStringSerializer;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 文章索引信息
 */
@Data
public class Article implements Serializable {

	/**
	 * 主键id
	 */
	@JsonSerialize(using = LongToStringSerializer.class)
	private long id;

	/**
	 * 对应文件路径（文章在对应仓库中的路径）
	 */
	private String filePath;

	/**
	 * 文章内容
	 */
	private String content;

	/**
	 * 所属文集
	 */
	private Anthology anthology;

	/**
	 * 创建时间
	 */
	@JsonIgnore
	private LocalDateTime gmtCreated;

	/**
	 * 修改时间
	 */
	@JsonIgnore
	private LocalDateTime gmtModified;

}