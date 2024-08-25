package com.gitee.swsk33.gitdocument.api;

import cn.dev33.satoken.stp.StpUtil;
import com.gitee.swsk33.gitdocument.dataobject.User;
import com.gitee.swsk33.gitdocument.model.Result;
import com.gitee.swsk33.gitdocument.param.ValidationRules;
import com.gitee.swsk33.gitdocument.service.LoginRecordService;
import com.gitee.swsk33.gitdocument.service.UserService;
import com.gitee.swsk33.gitdocument.session.UserSession;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserAPI {

	@Autowired
	private UserService userService;

	@Autowired
	private LoginRecordService loginRecordService;

	@Autowired
	private UserSession userSession;

	@PostMapping("/register")
	public Result<Void> register(@RequestBody @Validated(ValidationRules.DataAdd.class) User user, BindingResult errors) {
		if (errors.hasErrors()) {
			return Result.resultFailed(errors.getFieldError().getDefaultMessage());
		}
		return userService.register(user);
	}

	@DeleteMapping("/delete/{id}")
	public Result<Void> delete(@PathVariable("id") int id) {
		return userService.delete(id);
	}

	@PatchMapping("/update")
	public Result<Void> update(@RequestBody @Validated(ValidationRules.DataUpdate.class) User user, BindingResult errors) throws Exception {
		if (errors.hasErrors()) {
			return Result.resultFailed(errors.getFieldError().getDefaultMessage());
		}
		return userService.update(user);
	}


	@PostMapping("/login")
	public Result<Void> login(@RequestBody @Validated(ValidationRules.UserLogin.class) User user, BindingResult errors) {
		if (errors.hasErrors()) {
			return Result.resultFailed(errors.getFieldError().getDefaultMessage());
		}
		return userService.login(user);
	}

	@GetMapping("/logout")
	public Result<Void> logout() {
		StpUtil.logout();
		return Result.resultSuccess("退出登录成功！");
	}

	@GetMapping("/get-all")
	public Result<List<User>> getAll() {
		return userService.getAll();
	}

	@GetMapping("/is-login")
	public Result<User> isLogin(HttpServletRequest request) {
		if (!StpUtil.isLogin()) {
			return Result.resultFailed("用户没有登录！");
		}
		// 更新用户登录记录
		loginRecordService.update(request);
		return Result.resultSuccess("用户已登录！", userSession.getCurrentLoginSessionUser());
	}

	@PostMapping("/reset-password/{code}")
	public Result<Void> resetPassword(@RequestBody @Validated(ValidationRules.UserPasswordReset.class) User user, @PathVariable String code, BindingResult errors) {
		if (errors.hasErrors()) {
			return Result.resultFailed(errors.getFieldError().getDefaultMessage());
		}
		return userService.resetPassword(user, code);
	}

}