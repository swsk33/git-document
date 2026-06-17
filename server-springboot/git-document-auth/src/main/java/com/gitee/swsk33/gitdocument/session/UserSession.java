package com.gitee.swsk33.gitdocument.session;

import com.gitee.swsk33.gitdocument.annotation.SaTokenSession;
import com.gitee.swsk33.gitdocument.dataobject.User;

/**
 * 操作用户Sa-Token Session操作的封装
 */
@SaTokenSession
public interface UserSession {

	/**
	 * 将一个现有的用户对象存放至其对应的Session缓存中
	 *
	 * @param user 现有的用户对象，将会根据其id属性来判断保存的用户会话
	 */
	void saveUserSession(User user);

	/**
	 * 获取当前登录的缓存在Session中的用户信息
	 *
	 * @return 当前缓存在Session中的用户信息，未登录返回null
	 */
	User getCurrentLoginSessionUser();

	/**
	 * 根据用户的id返回对应用户缓存在Session中的用户信息
	 *
	 * @param id 用户id
	 * @return 对应id的用户的登录Session缓存信息，若该用户未登录返回null
	 */
	User getSessionUserById(int id);

	/**
	 * 从数据库完整获取当前登录的用户信息，并刷新至登录Session，未登录则不会做任何操作
	 */
	void refreshCurrentLoginUserSession();

	/**
	 * 从数据库完整获取对应id的用户信息，并刷新至该用户的登录Session，未登录则不会做任何操作
	 *
	 * @param id 用户id
	 */
	void refreshUserSessionById(int id);

}