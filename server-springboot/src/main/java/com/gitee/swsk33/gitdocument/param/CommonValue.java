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
	 * 角色名
	 */
	public static class Role {

		/**
		 * 管理员的角色名
		 */
		public static final String ADMIN = "ROLE_ADMIN";

		/**
		 * 普通成员的角色名
		 */
		public static final String MEMBER = "ROLE_MEMBER";

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

	}

	/**
	 * 实际资源相对路径
	 */
	public static class ResourcePath {

		/**
		 * 用户头像存放路径
		 */
		public static final String USER_AVATAR_PATH = "external-resource" + File.separator + "avatar" + File.separator + "user";

		/**
		 * 默认头像存放路径
		 */
		public static final String DEFAULT_AVATAR_PATH = "external-resource" + File.separator + "avatar" + File.separator + "default";

		/**
		 * 自定义封面存放路径
		 */
		public static final String CUSTOM_COVER_PATH = "external-resource" + File.separator + "cover" + File.separator + "custom";

		/**
		 * 默认封面存放路径
		 */
		public static final String DEFAULT_COVER_PATH = "external-resource" + File.separator + "cover" + File.separator + "default";

		/**
		 * 默认仓库存放位置
		 */
		public static final String GIT_REPO_PATH = System.getProperty("user.home") + File.separator + "git-doc-repos";

	}

}