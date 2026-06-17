package com.gitee.swsk33.gitdocument.service.impl;

import com.gitee.swsk33.gitdocument.dao.UserDAO;
import com.gitee.swsk33.gitdocument.dataobject.User;
import com.gitee.swsk33.gitdocument.param.UserEmailServiceName;
import com.gitee.swsk33.gitdocument.service.UserEmailCodeService;
import io.github.swsk33.codepostcore.context.ServiceNameContext;
import io.github.swsk33.codepostcore.service.EmailVerifyCodeService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class UserEmailCodeServiceImpl implements UserEmailCodeService, InitializingBean {

	@Autowired
	private UserDAO userDAO;

	@Autowired
	private EmailVerifyCodeService verifyCodeService;

	@Override
	public void afterPropertiesSet() {
		// 验证码服务名注册
		ServiceNameContext.register(UserEmailServiceName.PASSWORD_RESET, "密码重置");
	}

	@Override
	public void sendPasswordResetCode(String email) {
		User resetUser = userDAO.getByUsernameOrEmail(email);
		if (resetUser == null) {
			return;
		}
		verifyCodeService.sendCodeAsync(UserEmailServiceName.PASSWORD_RESET, resetUser.getId(), resetUser.getEmail(), 5, TimeUnit.MINUTES);
	}

	@Override
	public boolean verifyPasswordResetCode(int userId, String code) {
		return verifyCodeService.verifyCode(UserEmailServiceName.PASSWORD_RESET, userId, code);
	}

}