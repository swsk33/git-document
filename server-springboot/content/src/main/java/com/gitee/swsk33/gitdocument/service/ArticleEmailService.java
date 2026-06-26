package com.gitee.swsk33.gitdocument.service;

import com.gitee.swsk33.gitdocument.model.CreateArticleNotifyEmailMessage;
import com.gitee.swsk33.gitdocument.model.UpdateArticleNotifyEmailMessage;

/**
 * 用于文集文章通知相关邮件服务
 */
public interface ArticleEmailService {

	/**
	 * 发送文集更新邮件通知
	 *
	 * @param message 从消息队列获取到的更新通知邮件任务
	 */
	void sendAnthologyUpdateNotify(UpdateArticleNotifyEmailMessage message);

	/**
	 * 发送新发布文集通知
	 *
	 * @param message 从消息队列获取到的新文集创建通知邮件任务
	 */
	void sendAnthologyCreateNotify(CreateArticleNotifyEmailMessage message);

}