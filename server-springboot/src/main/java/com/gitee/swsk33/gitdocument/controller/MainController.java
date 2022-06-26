package com.gitee.swsk33.gitdocument.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 以下所有的路径变量patterns是为了兼容vue router而使用的保留路径
 * 重载方法是用于防止访问某个页面的“根路径”时导致404
 */
@Controller
public class MainController {

	@GetMapping("/login")
	public String login() {
		return "/web/login/index.html";
	}

	@GetMapping("/login/{patterns}")
	public String login(@PathVariable String patterns) {
		return "/web/login/index.html";
	}

	@GetMapping("/")
	public String panel() {
		return "/web/index.html";
	}

	@GetMapping("/{patterns}")
	public String panel(@PathVariable String patterns) {
		return "/web/index.html";
	}

	@GetMapping("/article")
	public String article() {
		return "/web/article/index.html";
	}

	@GetMapping("/article/{patterns}")
	public String article(@PathVariable String patterns) {
		return "/web/article/index.html";
	}

}