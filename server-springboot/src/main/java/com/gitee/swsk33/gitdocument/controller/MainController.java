package com.gitee.swsk33.gitdocument.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

	@GetMapping("/")
	public String panel() {
		return "/index.html";
	}

}