package com.gitee.swsk33.gitdocument.dataobject;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gitee.swsk33.gitdocument.serializer.LongToStringSerializer;
import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import com.mybatisflex.core.keygen.KeyGenerators;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 文章索引信息
 */
@Data
@Table("article")
public class Article {

	/**
	 * 主键id
	 */
	@Id(keyType = KeyType.Generator, value = KeyGenerators.snowFlakeId)
	@JsonSerialize(using = LongToStringSerializer.class)
	private Long id;

	/**
	 * 对应文件路径（文章在对应仓库中的路径）
	 */
	private String filePath;

	/**
	 * 文章内容
	 */
	@Column(ignore = true)
	private String content;

	/**
	 * 所属文集id（外键）
	 */
	@JsonSerialize(using = LongToStringSerializer.class)
	private Long anthologyId;

	/**
	 * 创建时间
	 */
	@Column(onInsertValue = "now()")
	private LocalDateTime gmtCreated;

	/**
	 * 修改时间
	 */
	@Column(onUpdateValue = "now()")
	private LocalDateTime gmtModified;

}