package com.gitee.swsk33.gitdocument.param;

import java.io.File;

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
	 * 常用资源路径
	 */
	public static class RequestPath {

		/**
		 * 访问包内部静态资源的资源路径
		 */
		public static final String INNER_STATIC = "/";

		/**
		 * 访问外部静态资源的路径
		 */
		public static final String EXTERNAL_REQUEST_PATH = "/static";

		/**
		 * 用户头像外部访问路径
		 */
		public static final String USER_AVATAR_REQUEST_PATH = EXTERNAL_REQUEST_PATH + "/avatar/user/";

		/**
		 * 默认头像外部访问路径
		 */
		public static final String DEFAULT_AVATAR_REQUEST_PATH = EXTERNAL_REQUEST_PATH + "/avatar/default/";

		/**
		 * 自定义封面外部访问路径
		 */
		public static final String CUSTOM_COVER_REQUEST_PATH = EXTERNAL_REQUEST_PATH + "/cover/custom/";

		/**
		 * 默认封面外部访问路径
		 */
		public static final String DEFAULT_COVER_REQUEST_PATH = EXTERNAL_REQUEST_PATH + "/cover/default/";

		/**
		 * 默认主面板背景图片请求路径
		 */
		public static final String DEFAULT_BACKGROUND_REQUEST_PATH = EXTERNAL_REQUEST_PATH + "/background/default/";

		/**
		 * 自定义主面板背景图片请求路径
		 */
		public static final String CUSTOM_BACKGROUND_REQUEST_PATH = EXTERNAL_REQUEST_PATH + "/background/custom.jpg";

		/**
		 * 默认登录背景图
		 */
		public static final String DEFAULT_LOGIN_BACKGROUND_PATH = EXTERNAL_REQUEST_PATH + "/login-background/default/";

		/**
		 * 自定义登录背景图
		 */
		public static final String CUSTOM_LOGIN_BACKGROUND_PATH = EXTERNAL_REQUEST_PATH + "/login-background/custom.jpg";

	}

	/**
	 * 实际资源相对路径
	 */
	public static class ResourcePath {

		/**
		 * 用户头像存放文件夹
		 */
		public static final String USER_AVATAR_FOLDER = "external-resource" + File.separator + "avatar" + File.separator + "user";

		/**
		 * 默认头像存放文件夹
		 */
		public static final String DEFAULT_AVATAR_FOLDER = "external-resource" + File.separator + "avatar" + File.separator + "default";

		/**
		 * 自定义封面存放文件夹
		 */
		public static final String CUSTOM_COVER_FOLDER = "external-resource" + File.separator + "cover" + File.separator + "custom";

		/**
		 * 默认封面存放文件夹
		 */
		public static final String DEFAULT_COVER_FOLDER = "external-resource" + File.separator + "cover" + File.separator + "default";

		/**
		 * 自定义面板背景图所在目录
		 */
		public static final String CUSTOM_BACKGROUND_FOLDER = "external-resource" + File.separator + "background";

		/**
		 * 自定义主面板背景图片路径
		 */
		public static final String CUSTOM_BACKGROUND_FILE = CUSTOM_BACKGROUND_FOLDER + File.separator + "custom.jpg";

		/**
		 * 自定义登录背景图所在目录
		 */
		public static final String CUSTOM_LOGIN_BACKGROUND_FOLDER = "external-resource" + File.separator + "login-background";

		/**
		 * 自定义登录背景图片路径
		 */
		public static final String CUSTOM_LOGIN_BACKGROUND_FILE = CUSTOM_LOGIN_BACKGROUND_FOLDER + File.separator + "custom.jpg";

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
		public static final String UPDATE_EMAIL_QUEUE_NAME = PREFIX + ".update-email-queue";

		/**
		 * 主题模式交换机名
		 */
		public static final String EMAIL_TOPIC_EXCHANGE_NAME = PREFIX + ".email-topic";

	}

	/**
	 * 存放RabbitMQ中的一些RoutingKey
	 */
	public static class RabbitMQRoutingKey {

		/**
		 * 表示邮件
		 */
		public static final String EMAIL_TASK_KEY = "email";

		/**
		 * 表示更新通知邮件
		 */
		public static final String UPDATE_EMAIL = EMAIL_TASK_KEY + ".update";

	}

}