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

}