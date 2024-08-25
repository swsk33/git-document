package com.gitee.swsk33.gitdocument.aop;

import com.gitee.swsk33.gitdocument.dataobject.User;
import com.gitee.swsk33.gitdocument.model.Result;
import com.gitee.swsk33.gitdocument.session.UserSession;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 用于刷新用户Session的切面操作
 */
@Aspect
@Component
public class UserSessionRefreshAspect {

	@Autowired
	private UserSession userSession;

	/**
	 * 登录记录更新切面
	 */
	@Pointcut("execution(* com.gitee.swsk33.gitdocument.service.LoginRecordService.*(..))")
	public void loginRecordUpdate() {
	}

	/**
	 * 公钥更新切面
	 */
	@Pointcut("execution(* com.gitee.swsk33.gitdocument.service.PublicKeyService.*(..))")
	public void publicKeyUpdate() {
	}

	/**
	 * 个人设置更新切面
	 */
	@Pointcut("execution(* com.gitee.swsk33.gitdocument.service.SettingService.*(..)))")
	public void settingUpdate() {
	}

	/**
	 * 用户收藏更新切面
	 */
	@Pointcut("execution(* com.gitee.swsk33.gitdocument.service.StarService.add(..)) || execution(* com.gitee.swsk33.gitdocument.service.StarService.delete(..))")
	public void starUpdate() {
	}

	/**
	 * 刷新当前登录的用户Session切入方法
	 */
	@Around("loginRecordUpdate() || publicKeyUpdate() || settingUpdate() || starUpdate()")
	public Object refreshCurrentLoginAfter(ProceedingJoinPoint joinPoint) throws Throwable {
		Object result = joinPoint.proceed();
		if (((Result<?>) result).isSuccess()) {
			userSession.refreshCurrentLoginUserSession();
		}
		return result;
	}

	/**
	 * 用户信息更新后的刷新Session切入方法
	 */
	@Around("execution(* com.gitee.swsk33.gitdocument.service.UserService.update(..)) && args(user)")
	public Object userUpdate(ProceedingJoinPoint joinPoint, User user) throws Throwable {
		Object result = joinPoint.proceed();
		if (((Result<?>) result).isSuccess()) {
			userSession.refreshUserSessionById(user.getId());
			System.out.println(user.getId());
		}
		return result;
	}

}