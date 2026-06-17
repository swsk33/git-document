package com.gitee.swsk33.gitdocument.service;

import com.gitee.swsk33.gitdocument.dataobject.User;
import com.gitee.swsk33.gitdocument.model.CreateEmailMessage;
import com.gitee.swsk33.gitdocument.model.UpdateEmailMessage;
import org.springframework.stereotype.Service;

@Service
public interface EmailService {

	/**
	 * 发送角色更改邮件通知
	 *
	 * @param changedUser 被修改权限的角色
	 * @param operator    操作者
	 */
	void sendRoleChangeEmail(User changedUser, User operator);

	/**
	 * 发送文集更新邮件通知
	 *
	 * @param message 从消息队列获取到的更新通知邮件任务
	 */
	void sendAnthologyUpdateNotify(UpdateEmailMessage message);

	/**
	 * 发送新发布文集通知
	 *
	 * @param message 从消息队列获取到的新文集创建通知邮件任务
	 */
	void sendAnthologyCreateNotify(CreateEmailMessage message);



}