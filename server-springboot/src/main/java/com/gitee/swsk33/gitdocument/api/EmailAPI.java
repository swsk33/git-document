package com.gitee.swsk33.gitdocument.api;

import com.gitee.swsk33.gitdocument.model.Result;
import com.gitee.swsk33.gitdocument.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/email")
public class EmailAPI {

	@Autowired
	private EmailService emailService;

	@GetMapping("/password-reset/{userId}")
	public Result<Void> sendPasswordResetCode(@PathVariable int userId) {
		emailService.sendPasswordResetCode(userId);
		return Result.resultSuccess("已发送密码重置验证码！");
	}

}