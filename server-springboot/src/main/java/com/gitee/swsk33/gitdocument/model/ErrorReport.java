package com.gitee.swsk33.gitdocument.model;

import com.gitee.swsk33.gitdocument.dataobject.Article;
import com.gitee.swsk33.gitdocument.dataobject.User;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
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
	@NotEmpty(message = "名字不能为空！")
	private String name;

	/**
	 * 内容
	 */
	@NotEmpty(message = "内容不能为空！")
	private String content;

	/**
	 * 问题文章
	 */
	@NotNull(message = "文章不能为空！其中只需要id字段！")
	private Article article;

	/**
	 * 发送的用户
	 */
	private User user;

	/**
	 * 创建时间
	 */
	private LocalDateTime gmtCreated;

}