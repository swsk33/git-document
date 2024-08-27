package com.gitee.swsk33.gitdocument.session.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.gitee.swsk33.gitdocument.dao.UserDAO;
import com.gitee.swsk33.gitdocument.dataobject.User;
import com.gitee.swsk33.gitdocument.session.UserSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserSessionImpl implements UserSession {

	/**
	 * Sa-Token的用户登录缓存额外信息键
	 */
	private final String SA_USER_SESSION_INFO_KEY = "userSession";

	@Autowired
	private UserDAO userDAO;

	@Override
	public void saveUserSession(User user) {
		StpUtil.getSessionByLoginId(user.getId()).set(SA_USER_SESSION_INFO_KEY, user);
	}

	@Override
	public User getCurrentLoginSessionUser() {
		if (!StpUtil.isLogin()) {
			return null;
		}
		return (User) StpUtil.getSession().get(SA_USER_SESSION_INFO_KEY);
	}

	@Override
	public User getSessionUserById(int id) {
		if (!StpUtil.isLogin(id)) {
			return null;
		}
		return (User) StpUtil.getSessionByLoginId(id).get(SA_USER_SESSION_INFO_KEY);
	}

	@Override
	public void refreshCurrentLoginUserSession() {
		refreshUserSessionById(StpUtil.getLoginIdAsInt());
	}

	@Override
	public void refreshUserSessionById(int id) {
		if (!StpUtil.isLogin(id)) {
			return;
		}
		User getUser = userDAO.selectOneWithRelationsById(id);
		saveUserSession(getUser);
	}

}