package com.gitee.swsk33.gitdocument.message.prototype;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 邮件任务消息抽象类
 */
@Data
public abstract class EmailMessage implements Serializable {

	/**
	 * 标题
	 */
	private String title;

	/**
	 * 收件人列表
	 */
	private List<String> emailList;

}