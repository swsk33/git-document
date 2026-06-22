package com.gitee.swsk33.gitdocument.model;

import com.gitee.swsk33.gitdocument.model.prototype.ArticleNotifyEmailMessage;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用于发送创建新文集的通知邮件的任务消息
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CreateArticleNotifyEmailMessage extends ArticleNotifyEmailMessage {

	/**
	 * 文集创建者昵称
	 */
	private String publisher;

	/**
	 * 新文集名
	 */
	private String name;

}