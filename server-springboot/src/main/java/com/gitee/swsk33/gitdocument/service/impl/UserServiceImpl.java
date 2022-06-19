package com.gitee.swsk33.gitdocument.service.impl;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.stp.SaLoginConfig;
import cn.dev33.satoken.stp.StpUtil;
import com.gitee.swsk33.gitdocument.dao.UserDAO;
import com.gitee.swsk33.gitdocument.dataobject.User;
import com.gitee.swsk33.gitdocument.model.Result;
import com.gitee.swsk33.gitdocument.param.CommonValue;
import com.gitee.swsk33.gitdocument.service.UserService;
import com.gitee.swsk33.gitdocument.util.BCryptEncoder;
import com.gitee.swsk33.gitdocument.util.ClassExamine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;

@Slf4j
@Component
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDAO userDAO;

	@Override
	public Result<User> register(User user) {
		Result<User> result = new Result<>();
		// 先检查用户是否已存在
		User getUser = userDAO.getByUsernameOrEmail(user.getUsername());
		if (getUser != null) {
			result.setResultFailed("该用户名已存在！");
			return result;
		}
		// 然后检查用户邮箱是否被注册过
		getUser = userDAO.getByUsernameOrEmail(user.getEmail());
		if (getUser != null) {
			result.setResultFailed("该邮箱已被注册！");
			return result;
		}
		// 权限对比
		if (!StpUtil.hasRole(CommonValue.Role.ADMIN) && user.getRole().getId() == 1) {
			result.setResultFailed("不允许注册成管理员！");
			return result;
		}
		// 加密密码，存入数据库
		user.setPassword(BCryptEncoder.encode(user.getPassword()));
		userDAO.add(user);
		result.setResultSuccess("注册用户成功！");
		return result;
	}

	@SaCheckRole(CommonValue.Role.ADMIN)
	@Override
	public Result<User> delete(int id) {
		Result<User> result = new Result<>();
		if (userDAO.delete(id) < 1) {
			result.setResultFailed("删除用户失败！");
			return result;
		}
		result.setResultSuccess("删除用户成功！");
		return result;
	}

	@SaCheckLogin
	@Override
	public Result<User> update(User user) throws Exception {
		Result<User> result = new Result<>();
		User getUser = userDAO.getById(user.getId());
		if (getUser == null) {
			result.setResultFailed("找不到该用户！");
			return result;
		}
		// 权限对比
		if (!StpUtil.hasRole(CommonValue.Role.ADMIN)) {
			if (StpUtil.getLoginId(0) != user.getId()) {
				result.setResultFailed("非管理员不可以修改其它用户的信息！");
				return result;
			}
			if (user.getRole() != null && user.getRole().getId() == 1) {
				result.setResultFailed("非管理员不可以修改自己为管理员！");
				return result;
			}
		}
		// 信息覆盖
		ClassExamine.objectOverlap(user, getUser);
		// 检查头像是否修改
		if (!user.getAvatar().equals(getUser.getAvatar())) {
			log.info("头像被修改！");
			if (!getUser.getAvatar().contains("default")) {
				String originFilePath = getUser.getAvatar();
				originFilePath = CommonValue.ResourcePath.USER_AVATAR_PATH + File.separator + originFilePath.substring(originFilePath.lastIndexOf("/") + 1);
				new File(originFilePath).delete();
				log.info("删除原始文件：" + originFilePath);
			}
		}
		// 密码被修改则加密储存
		if (!user.getPassword().equals(getUser.getPassword())) {
			user.setPassword(BCryptEncoder.encode(user.getPassword()));
		}
		userDAO.update(user);
		// 修改信息后需要重新登录
		StpUtil.logout(user.getId());
		result.setResultSuccess("修改用户成功！被修改的用户需要重新登录！");
		return result;
	}

	@Override
	public Result<User> login(User user) {
		Result<User> result = new Result<>();
		// 登录传来的用户名可以是用户名也可以是邮箱
		User getUser = userDAO.getByUsernameOrEmail(user.getUsername());
		if (getUser == null) {
			result.setResultFailed("用户名或者邮箱不存在！");
			return result;
		}
		// 密码比对
		if (!BCryptEncoder.check(user.getPassword(), getUser.getPassword())) {
			result.setResultFailed("密码错误！");
			return result;
		}
		// 执行登录
		StpUtil.login(getUser.getId(), SaLoginConfig.setExtra(CommonValue.SA_EXTRA_USER_INFO_KEY, getUser));
		result.setResultSuccess("登录成功！");
		return result;
	}

	@SaCheckRole(CommonValue.Role.ADMIN)
	@Override
	public Result<List<User>> getAll() {
		Result<List<User>> result = new Result<>();
		List<User> users = userDAO.getAll();
		result.setResultSuccess("查询全部用户成功！", users);
		return result;
	}

}