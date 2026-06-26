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
 * 鉴权相关全局异常配置
 */
@Slf4j
@RestControllerAdvice
public class GlobalAuthExceptionConfig {

	// 全局异常拦截（拦截项目中的所有异常）
	@ExceptionHandler
	public Result<Void> handlerException(Exception e, HttpServletResponse response) {
		// 打印堆栈，以供调试
		log.error("发生全局异常！{}:{}", e.getClass().getName(), e.getMessage());
		// 如果是未登录异常
		switch (e) {
			case NotLoginException ignored -> {
				response.setStatus(HttpServletResponse.SC_FORBIDDEN);
				return Result.resultFailed("用户未登录！");
			}

			// 如果是角色异常
			case NotRoleException ignored -> {
				response.setStatus(HttpServletResponse.SC_FORBIDDEN);
				return Result.resultFailed("用户角色权限不足！");
			}

			// 如果是权限异常
			case NotPermissionException ignored -> {
				response.setStatus(HttpServletResponse.SC_FORBIDDEN);
				return Result.resultFailed("用户没有权限！");
			}
			default -> {
			}
		}
		// 其余为服务器错误
		log.error(e.getMessage());
		response.setStatus(HttpServletResponse.SC_BAD_GATEWAY);
		return Result.resultFailed("服务器错误！请联系开发者！");
	}

}