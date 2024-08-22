package com.gitee.swsk33.gitdocument.param;

/**
 * 存放所有系统设置的键名常量
 */
public class SystemSettingKey {

	/**
	 * 组织名<br>
	 * 值为字符串形式
	 */
	public static final String ORGANIZATION_NAME = "organization_name";

	/**
	 * 是否允许公开注册<br>
	 * 值需要解析为布尔型
	 */
	public static final String ALLOW_PUBLIC = "allow_public";

	/**
	 * 登录页的背景图文件名<br>
	 * 值为字符串形式，为null时表示使用默认背景图
	 */
	public static final String LOGIN_BACKGROUND_IMAGE = "login_background_image";

	/**
	 * 主页的背景图文件名<br>
	 * 值为字符串形式，为null时表示使用默认背景图
	 */
	public static final String MAIN_BACKGROUND_IMAGE = "main_background_image";

}