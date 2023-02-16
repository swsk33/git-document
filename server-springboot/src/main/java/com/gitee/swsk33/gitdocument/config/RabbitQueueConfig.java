package com.gitee.swsk33.gitdocument.config;

import com.gitee.swsk33.gitdocument.param.CommonValue;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMQ的队列和交换机配置
 */
@Configuration
public class RabbitQueueConfig {

	/**
	 * 更新邮件消息队列
	 */
	@Bean
	public Queue updateEmailQueue() {
		return new Queue(CommonValue.MessageQueue.UPDATE_EMAIL_QUEUE_NAME);
	}

	/**
	 * 邮件主题交换机
	 */
	@Bean
	public TopicExchange emailExchange() {
		return new TopicExchange(CommonValue.MessageQueue.EMAIL_TOPIC_EXCHANGE_NAME);
	}

	/**
	 * 绑定关系
	 */
	@Bean
	public Binding emailQueueBinding(Queue updateEmailQueue, TopicExchange emailExchange) {
		return BindingBuilder.bind(updateEmailQueue).to(emailExchange).with(CommonValue.RabbitMQRoutingKey.UPDATE_EMAIL);
	}

}