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
		return new Queue(CommonValue.MessageQueue.UPDATE_EMAIL_QUEUE);
	}

	/**
	 * 邮件主题交换机
	 */
	@Bean
	public TopicExchange emailExchange() {
		return new TopicExchange(CommonValue.MessageQueue.EMAIL_TOPIC_EXCHANGE);
	}

	/**
	 * 绑定邮件通知队列
	 */
	@Bean
	public Binding emailQueueBinding(Queue updateEmailQueue, TopicExchange emailExchange) {
		return BindingBuilder.bind(updateEmailQueue).to(emailExchange).with(CommonValue.RabbitMQRoutingKey.UPDATE_EMAIL);
	}

	/**
	 * Git创建任务消息队列
	 */
	@Bean
	public Queue gitCreateQueue() {
		return new Queue(CommonValue.MessageQueue.GIT_CREATE_TASK_QUEUE);
	}

	/**
	 * Git修改任务消息队列
	 */
	@Bean
	public Queue gitUpdateQueue() {
		return new Queue(CommonValue.MessageQueue.GIT_UPDATE_TASK_QUEUE);
	}

	/**
	 * Git任务话题交换机
	 */
	@Bean
	public TopicExchange gitTaskExchange() {
		return new TopicExchange(CommonValue.MessageQueue.GIT_TASK_TOPIC_EXCHANGE);
	}

	/**
	 * 绑定Git创建队列
	 */
	public Binding createQueueBinding(Queue gitCreateQueue, TopicExchange gitTaskExchange) {
		return BindingBuilder.bind(gitCreateQueue).to(gitTaskExchange).with(CommonValue.RabbitMQRoutingKey.GIT_CREATE);
	}

	/**
	 * 绑定Git更新队列
	 */
	public Binding updateQueueBinding(Queue gitUpdateQueue, TopicExchange gitTaskExchange) {
		return BindingBuilder.bind(gitUpdateQueue).to(gitTaskExchange).with(CommonValue.RabbitMQRoutingKey.GIT_UPDATE);
	}

}