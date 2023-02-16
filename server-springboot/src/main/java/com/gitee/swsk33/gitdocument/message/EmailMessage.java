package com.gitee.swsk33.gitdocument.message;

import lombok.Data;

import java.io.Serializable;

/**
 * 邮件消息抽象类
 */
@Data
public abstract class EmailMessage implements Serializable {

	/**
	 * 标题
	 */
	private String title;

	/**
	 * 收件人
	 */
	private String email;

}