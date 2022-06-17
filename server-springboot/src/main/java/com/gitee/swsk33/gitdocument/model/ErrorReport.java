package com.gitee.swsk33.gitdocument.model;

import com.gitee.swsk33.gitdocument.dataobject.Article;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 问题报告
 */
@Getter
@Setter
public class ErrorReport implements Serializable {

	/**
	 * 名字
	 */
	private String name;

	/**
	 * 内容
	 */
	private String content;

	/**
	 * 问题文章
	 */
	private Article article;

	/**
	 * 创建时间
	 */
	private LocalDateTime gmtCreated;

}