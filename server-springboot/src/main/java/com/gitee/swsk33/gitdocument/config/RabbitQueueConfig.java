package com.gitee.swsk33.gitdocument.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.gitee.swsk33.gitdocument.param.RabbitMessageQueue.Exchange.*;
import static com.gitee.swsk33.gitdocument.param.RabbitMessageQueue.Queue.*;
import static com.gitee.swsk33.gitdocument.param.RabbitMessageQueue.RoutingKey.*;

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
		return new Queue(UPDATE_EMAIL_QUEUE);
	}

	/**
	 * 新文集创建邮件消息队列
	 */
	@Bean
	public Queue createEmailQueue() {
		return new Queue(CREATE_EMAIL_QUEUE);
	}

	/**
	 * 邮件通知主题交换机
	 */
	@Bean
	public TopicExchange emailExchange() {
		return new TopicExchange(EMAIL_TOPIC_EXCHANGE);
	}

	/**
	 * 绑定更新邮件通知队列
	 */
	@Bean
	public Binding emailUpdateQueueBinding(Queue updateEmailQueue, TopicExchange emailExchange) {
		return BindingBuilder.bind(updateEmailQueue).to(emailExchange).with(UPDATE_EMAIL);
	}

	/**
	 * 绑定创建邮件通知队列
	 */
	@Bean
	public Binding emailCreateQueueBinding(Queue createEmailQueue, TopicExchange emailExchange) {
		return BindingBuilder.bind(createEmailQueue).to(emailExchange).with(CREATE_EMAIL);
	}

	/**
	 * Git创建任务消息队列
	 */
	@Bean
	public Queue gitCreateQueue() {
		return new Queue(GIT_CREATE_TASK_QUEUE);
	}

	/**
	 * Git修改任务消息队列
	 */
	@Bean
	public Queue gitUpdateQueue() {
		return new Queue(GIT_UPDATE_TASK_QUEUE);
	}

	/**
	 * Git任务话题交换机
	 */
	@Bean
	public TopicExchange gitTaskExchange() {
		return new TopicExchange(GIT_TASK_TOPIC_EXCHANGE);
	}

	/**
	 * 绑定Git创建队列
	 */
	@Bean
	public Binding createQueueBinding(Queue gitCreateQueue, TopicExchange gitTaskExchange) {
		return BindingBuilder.bind(gitCreateQueue).to(gitTaskExchange).with(GIT_CREATE);
	}

	/**
	 * 绑定Git更新队列
	 */
	@Bean
	public Binding updateQueueBinding(Queue gitUpdateQueue, TopicExchange gitTaskExchange) {
		return BindingBuilder.bind(gitUpdateQueue).to(gitTaskExchange).with(GIT_UPDATE);
	}

}