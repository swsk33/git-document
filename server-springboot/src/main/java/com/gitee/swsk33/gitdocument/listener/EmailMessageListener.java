package com.gitee.swsk33.gitdocument.listener;

import com.gitee.swsk33.gitdocument.message.CreateEmailMessage;
import com.gitee.swsk33.gitdocument.message.UpdateEmailMessage;
import com.gitee.swsk33.gitdocument.param.RabbitMessageQueue;
import com.gitee.swsk33.gitdocument.service.EmailService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 接收通知任务的消息订阅者
 */
@Component
public class EmailMessageListener {

	@Autowired
	private EmailService emailService;

	/**
	 * 从消息队列拉取更新通知邮件任务消息
	 *
	 * @param message 更新通知邮件任务消息
	 */
	@RabbitListener(queues = RabbitMessageQueue.Queue.UPDATE_EMAIL_QUEUE)
	public void getUpdateEmailTask(UpdateEmailMessage message) {
		emailService.sendAnthologyUpdateNotify(message);
	}

	/**
	 * 从消息队列拉取新文集创建通知邮件任务消息
	 *
	 * @param message 新文集创建通知邮件任务消息
	 */
	@RabbitListener(queues = RabbitMessageQueue.Queue.CREATE_EMAIL_QUEUE)
	public void getCreateEmailTask(CreateEmailMessage message) {
		emailService.sendAnthologyCreateNotify(message);
	}

}