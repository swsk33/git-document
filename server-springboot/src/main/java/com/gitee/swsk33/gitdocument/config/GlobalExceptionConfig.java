package com.gitee.swsk33.gitdocument.config;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotPermissionException;
import cn.dev33.satoken.exception.NotRoleException;
import com.gitee.swsk33.gitdocument.model.Result;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 自定义全局异常，例如鉴权注解的异常等等
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionConfig {

	// 全局异常拦截（拦截项目中的所有异常）
	@ExceptionHandler
	public Result<Void> handlerException(Exception e, HttpServletResponse response) {
		// 打印堆栈，以供调试
		log.error("发生全局异常！" + e.getClass().getName() + ":" + e.getMessage());
		// 如果是未登录异常
		if (e instanceof NotLoginException) {
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			return Result.resultFailed("用户未登录！");
		}
		// 如果是角色异常
		if (e instanceof NotRoleException) {
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			return Result.resultFailed("用户角色权限不足！");
		}
		// 如果是权限异常
		if (e instanceof NotPermissionException) {
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			return Result.resultFailed("用户没有权限！");
		}
		// 其余为服务器错误
		e.printStackTrace();
		response.setStatus(HttpServletResponse.SC_BAD_GATEWAY);
		return Result.resultFailed("服务器错误！请联系开发者！");
	}

}