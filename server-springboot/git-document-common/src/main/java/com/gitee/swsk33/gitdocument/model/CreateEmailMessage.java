package com.gitee.swsk33.gitdocument.model;

import com.gitee.swsk33.gitdocument.model.prototype.EmailMessage;
import lombok.Data;

/**
 * 用于发送创建新文集的通知邮件的任务消息
 */
@Data
public class CreateEmailMessage extends EmailMessage {

	/**
	 * 文集创建者昵称
	 */
	private String publisher;

	/**
	 * 新文集名
	 */
	private String name;

}