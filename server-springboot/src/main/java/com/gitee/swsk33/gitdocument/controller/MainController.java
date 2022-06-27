package com.gitee.swsk33.gitdocument.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class MainController {

	@GetMapping("/")
	public String panel() {
		return "/web/index.html";
	}

	// 重载带参方法以兼容vue router
	@GetMapping("/{router}")
	public String panel(@PathVariable("router") String router) {
		return "/web/index.html";
	}

	// 兼容文章目录的vue router视图
	@GetMapping("/article-menu/{router}")
	public String panelMenu(@PathVariable("router") String router) {
		return "/web/index.html";
	}

	// 兼容用户登录注册的vue router视图
	@GetMapping("/login")
	public String login() {
		return "/web/index.html";
	}

	// 重载带参方法以兼容用户登录注册的vue router视图
	@GetMapping("/login/{router}")
	public String login(@PathVariable("router") String router) {
		return "/web/index.html";
	}

	// 兼容文档阅读的vue router视图
	@GetMapping("/article/{router}")
	public String article(@PathVariable("router") String router) {
		return "/web/index.html";
	}

}