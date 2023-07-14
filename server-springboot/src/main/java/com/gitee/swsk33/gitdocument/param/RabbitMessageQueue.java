package com.gitee.swsk33.gitdocument.param;

/**
 * 存放RabbitMQ的一些交换机名称、队列名称等等的常量类
 */
public class RabbitMessageQueue {

	/**
	 * 名称前缀
	 */
	private static final String PREFIX = "git-document";

	/**
	 * 存放交换机名称
	 */
	public static class Exchange {

		/**
		 * 邮件主题模式交换机
		 */
		public static final String EMAIL_TOPIC_EXCHANGE = PREFIX + ".email-topic";
		/**
		 * Git仓库任务交换机
		 */
		public static final String GIT_TASK_TOPIC_EXCHANGE = PREFIX + ".git-task-topic";

	}

	/**
	 * 存放消息队列名称
	 */
	public static class Queue {

		/**
		 * 收藏更新通知邮件消息队列
		 */
		public static final String UPDATE_EMAIL_QUEUE = PREFIX + ".update-email-queue";
		/**
		 * 新文集发布通知邮件消息队列
		 */
		public static final String CREATE_EMAIL_QUEUE = PREFIX + ".create-email-queue";
		/**
		 * Git创建任务消息队列
		 */
		public static final String GIT_CREATE_TASK_QUEUE = PREFIX + ".git-create-queue";
		/**
		 * Git更新任务消息队列
		 */
		public static final String GIT_UPDATE_TASK_QUEUE = PREFIX + ".git-update-queue";

	}

	/**
	 * 存放RoutingKey
	 */
	public static class RoutingKey {

		/**
		 * 邮件话题
		 */
		public static final String EMAIL_TASK = "email";
		/**
		 * 文集创建通知邮件
		 */
		public static final String CREATE_EMAIL = EMAIL_TASK + ".create";
		/**
		 * 更新通知邮件
		 */
		public static final String UPDATE_EMAIL = EMAIL_TASK + ".update";
		/**
		 * Git仓库任务话题
		 */
		public static final String GIT_TASK = "git-task";
		/**
		 * Git更新任务
		 */
		public static final String GIT_UPDATE = GIT_TASK + ".update";
		/**
		 * Git创建任务
		 */
		public static final String GIT_CREATE = GIT_TASK + ".create";

	}

}