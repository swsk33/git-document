package com.gitee.swsk33.gitdocument.api;

import cn.dev33.satoken.stp.StpUtil;
import com.gitee.swsk33.gitdocument.dataobject.User;
import com.gitee.swsk33.gitdocument.model.Result;
import com.gitee.swsk33.gitdocument.param.CommonValue;
import com.gitee.swsk33.gitdocument.param.ValidationRules;
import com.gitee.swsk33.gitdocument.service.UserService;
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

	@PostMapping("/register")
	public Result<User> register(@RequestBody @Validated(ValidationRules.DataAdd.class) User user, BindingResult errors) {
		if (errors.hasErrors()) {
			Result<User> result = new Result<>();
			result.setResultFailed(errors.getFieldError().getDefaultMessage());
			return result;
		}
		return userService.register(user);
	}

	@DeleteMapping("/delete/{id}")
	public Result<User> delete(@PathVariable("id") int id) {
		return userService.delete(id);
	}

	@PutMapping("/update")
	public Result<User> update(@RequestBody @Validated(ValidationRules.DataUpdate.class) User user, BindingResult errors) throws Exception {
		if (errors.hasErrors()) {
			Result<User> result = new Result<>();
			result.setResultFailed(errors.getFieldError().getDefaultMessage());
			return result;
		}
		return userService.update(user);
	}


	@PostMapping("/login")
	public Result<User> login(@RequestBody @Validated(ValidationRules.UserLogin.class) User user, BindingResult errors) {
		if (errors.hasErrors()) {
			Result<User> result = new Result<>();
			result.setResultFailed(errors.getFieldError().getDefaultMessage());
			return result;
		}
		return userService.login(user);
	}

	@GetMapping("/logout")
	public Result<User> logout() {
		StpUtil.logout();
		Result<User> result = new Result<>();
		result.setResultSuccess("?????????????????????");
		return result;
	}

	@GetMapping("/get-all")
	public Result<List<User>> getAll() {
		return userService.getAll();
	}

	@GetMapping("/is-login")
	public Result<User> isLogin() {
		Result<User> result = new Result<>();
		if (!StpUtil.isLogin()) {
			result.setResultFailed("?????????????????????");
			return result;
		}
		result.setResultSuccess("??????????????????", (User) StpUtil.getSession().get(CommonValue.SA_USER_SESSION_INFO_KEY));
		return result;
	}

	@GetMapping("/admin-reset-password/{id}")
	public Result<User> adminResetPassword(@PathVariable("id") int id) {
		return userService.adminResetPassword(id);
	}

}