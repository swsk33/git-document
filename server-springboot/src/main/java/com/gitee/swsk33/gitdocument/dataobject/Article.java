package com.gitee.swsk33.gitdocument.dataobject;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 文章索引信息
 */
@Data
public class Article {

	/**
	 * 主键id
	 */
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
	private LocalDateTime gmtCreated;

	/**
	 * 修改时间
	 */
	private LocalDateTime gmtModified;

}