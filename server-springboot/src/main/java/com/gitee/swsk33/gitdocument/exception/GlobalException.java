package com.gitee.swsk33.gitdocument.exception;

import cn.dev33.satoken.exception.DisableLoginException;
import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotPermissionException;
import cn.dev33.satoken.exception.NotRoleException;
import com.gitee.swsk33.gitdocument.model.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

/**
 * 自定义全局异常，例如鉴权注解的异常等等
 */
@Slf4j
@ControllerAdvice
public class GlobalException {

	// 全局异常拦截（拦截项目中的所有异常）
	@ResponseBody
	@ExceptionHandler
	public Result handlerException(Exception e, HttpServletResponse response) {
		Result result = new Result();
		// 打印堆栈，以供调试
		log.error("发生全局异常！");
		// 如果是未登录异常
		if (e instanceof NotLoginException) {
			result.setResultFailed("用户未登录！");
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			return result;
		}
		// 如果是角色异常
		if (e instanceof NotRoleException) {
			result.setResultFailed("用户角色权限不足！");
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			return result;
		}
		// 如果是权限异常
		if (e instanceof NotPermissionException) {
			result.setResultFailed("用户没有权限！");
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			return result;
		}
		// 如果是被封禁异常
		if (e instanceof DisableLoginException) {
			result.setResultFailed("用户被封禁！");
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			return result;
		}
		// 其余为服务器错误
		e.printStackTrace();
		response.setStatus(HttpServletResponse.SC_BAD_GATEWAY);
		result.setResultFailed("服务器错误！请联系开发者！");
		return result;
	}

}