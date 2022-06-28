package com.gitee.swsk33.gitdocument.controller;

import com.gitee.swsk33.gitdocument.model.Result;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@CrossOrigin
@RequestMapping("/error")
public class AppErrorController {

	@GetMapping("/404")
	public String redirectIndex(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 对于API请求则返回JSON数据
		if (request.getHeader("accept").contains(MediaType.APPLICATION_JSON_VALUE)) {
			response.sendRedirect("/error/404-api");
		}
		return "/index.html";
	}

	@GetMapping("/404-api")
	@ResponseBody
	public Result<String> notFoundApi() {
		Result<String> result = new Result<>();
		result.setResultFailed("找不到对应的接口！");
		return result;
	}

}