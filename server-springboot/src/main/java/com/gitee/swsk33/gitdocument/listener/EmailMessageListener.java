package com.gitee.swsk33.gitdocument.listener;

import com.gitee.swsk33.gitdocument.message.UpdateEmailMessage;
import com.gitee.swsk33.gitdocument.param.CommonValue;
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

	@RabbitListener(queues = CommonValue.MessageQueue.UPDATE_EMAIL_QUEUE)
	public void getUpdateEmailTask(UpdateEmailMessage message) {
		emailService.sendAnthologyUpdateNotify(message);
	}

}