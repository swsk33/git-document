package com.gitee.swsk33.gitdocument.param;

/**
 * 存放常用的常量值
 */
public class CommonValue {

	/**
	 * sa-token的用户登录缓存额外信息键
	 */
	public static final String SA_USER_SESSION_INFO_KEY = "userSession";

	/**
	 * 运行程序的用户名
	 */
	public static final String RUN_USER_NAME = System.getProperty("user.name");

	/**
	 * 角色名
	 */
	public static class Role {

		/**
		 * 预留管理员
		 */
		public static final String PRESERVE_ADMIN = "ROLE_PRESERVE_ADMIN";

		/**
		 * 管理员
		 */
		public static final String ADMIN = "ROLE_ADMIN";

		/**
		 * 普通成员
		 */
		public static final String MEMBER = "ROLE_MEMBER";

	}

	/**
	 * 权限名
	 */
	public static class Permission {

		/**
		 * 修改用户
		 */
		public static final String EDIT_USER = "edit_user";

		/**
		 * 编辑文集
		 */
		public static final String EDIT_ANTHOLOGY = "edit_anthology";

		/**
		 * 修改系统设置
		 */
		public static final String ALTER_SYSTEM_SETTING = "alter_system_setting";

		/**
		 * 浏览内部文章
		 */
		public static final String BROWSE_ARTICLE = "browse_article";

	}

	/**
	 * 存放消息队列的一些常量
	 */
	public static class MessageQueue {

		/**
		 * 名称前缀
		 */
		public static final String PREFIX = "git-document";

		/**
		 * 收藏更新通知邮件消息队列
		 */
		public static final String UPDATE_EMAIL_QUEUE = PREFIX + ".update-email-queue";

		/**
		 * 新文集发布通知邮件消息队列
		 */
		public static final String CREATE_EMAIL_QUEUE = PREFIX + ".create-email-queue";

		/**
		 * 邮件主题模式交换机
		 */
		public static final String EMAIL_TOPIC_EXCHANGE = PREFIX + ".email-topic";

		/**
		 * Git创建任务消息队列
		 */
		public static final String GIT_CREATE_TASK_QUEUE = PREFIX + ".git-create-queue";

		/**
		 * Git更新任务消息队列
		 */
		public static final String GIT_UPDATE_TASK_QUEUE = PREFIX + ".git-update-queue";

		/**
		 * Git仓库任务交换机
		 */
		public static final String GIT_TASK_TOPIC_EXCHANGE = PREFIX + ".git-task-topic";

	}

	/**
	 * 存放RabbitMQ中的一些RoutingKey
	 */
	public static class RabbitMQRoutingKey {

		/**
		 * 邮件话题
		 */
		public static final String EMAIL_TASK = "email";

		/**
		 * 更新通知邮件
		 */
		public static final String UPDATE_EMAIL = EMAIL_TASK + ".update";

		/**
		 * 文集创建通知邮件
		 */
		public static final String CREATE_EMAIL = EMAIL_TASK + ".create";

		/**
		 * Git仓库任务话题
		 */
		public static final String GIT_TASK = "git-task";

		/**
		 * Git创建任务
		 */
		public static final String GIT_CREATE = GIT_TASK + ".create";

		/**
		 * Git更新任务
		 */
		public static final String GIT_UPDATE = GIT_TASK + ".update";

	}

}