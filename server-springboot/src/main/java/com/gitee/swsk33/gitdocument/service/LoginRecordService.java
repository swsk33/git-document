package com.gitee.swsk33.gitdocument.service;

import com.gitee.swsk33.gitdocument.model.Result;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

/**
 * 关于登录记录的服务
 */
@Service
public interface LoginRecordService {

	/**
	 * 更新一个登录记录
	 *
	 * @param request 用户某次访问的网络请求
	 */
	Result<Void> update(HttpServletRequest request);

}