package com.gitee.swsk33.gitdocument.service.impl;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.RandomUtil;
import com.gitee.swsk33.gitdocument.dao.SettingDAO;
import com.gitee.swsk33.gitdocument.dao.UserDAO;
import com.gitee.swsk33.gitdocument.dataobject.Setting;
import com.gitee.swsk33.gitdocument.dataobject.User;
import com.gitee.swsk33.gitdocument.model.Result;
import com.gitee.swsk33.gitdocument.param.CommonValue;
import com.gitee.swsk33.gitdocument.property.ConfigProperties;
import com.gitee.swsk33.gitdocument.service.EmailService;
import com.gitee.swsk33.gitdocument.service.ImageService;
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

	@Autowired
	private SettingDAO settingDAO;

	@Autowired
	private ImageService imageService;

	@Autowired
	private EmailService emailService;

	@Autowired
	private ConfigProperties configProperties;

	@Override
	public Result<Void> register(User user) {
		Result<Void> result = new Result<>();
		// 没有登录的时候，是用户注册行为
		if (!StpUtil.isLogin()) {
			// 根据配置判断是否运行注册
			if (!configProperties.isAllowPublic()) {
				result.setResultFailed("本站不允许访客注册！请联系管理员！");
				return result;
			}
			// 注册权限检查
			if (user.getRole().getId() == 1 || user.getRole().getId() == 2) {
				result.setResultFailed("不允许注册成管理员！");
				return result;
			}
		} else { // 登录的时候，是管理员添加用户行为
			if (!StpUtil.hasPermission(CommonValue.Permission.EDIT_USER)) {
				result.setResultFailed("您没有添加用户的权限！");
				return result;
			}
			if (user.getRole().getId() == 1) {
				result.setResultFailed("不允许增加预留管理员角色的用户！");
				return result;
			}
		}
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
		// 加密密码，存入数据库
		user.setPassword(BCryptEncoder.encode(user.getPassword()));
		// 新建默认用户配置
		Setting setting = new Setting();
		setting.setReceiveUpdateEmail(true);
		settingDAO.add(setting);
		user.setSetting(setting);
		userDAO.add(user);
		result.setResultSuccess("注册用户成功！");
		return result;
	}

	@SaCheckPermission(CommonValue.Permission.EDIT_USER)
	@Override
	public Result<Void> delete(int id) {
		Result<Void> result = new Result<>();
		User getUser = userDAO.getById(id);
		if (getUser == null) {
			result.setResultFailed("用户不存在！");
			return result;
		}
		if (getUser.getRole().getName().equals(CommonValue.Role.PRESERVE_ADMIN)) {
			result.setResultFailed("预留管理员账户不可以删除！");
			return result;
		}
		if (userDAO.delete(id) < 1) {
			result.setResultFailed("删除用户失败！");
			return result;
		}
		result.setResultSuccess("删除用户成功！");
		return result;
	}

	@SaCheckLogin
	@Override
	public Result<Void> update(User user) throws Exception {
		Result<Void> result = new Result<>();
		User getUser = userDAO.getById(user.getId());
		if (getUser == null) {
			result.setResultFailed("找不到该用户！");
			return result;
		}
		// 权限判断
		if (!StpUtil.hasPermission(CommonValue.Permission.EDIT_USER) && StpUtil.getLoginId(0) != user.getId().intValue()) {
			result.setResultFailed("您没有修改其他用户信息的权限！");
			return result;
		}
		// 若权限发生修改，则进行一些判断
		boolean roleChanged = false;
		if (user.getRole() != null && user.getRole().getId().intValue() != getUser.getRole().getId().intValue()) {
			// 非管理员不能修改权限
			if (!StpUtil.hasPermission(CommonValue.Permission.EDIT_USER)) {
				result.setResultFailed("非管理员不可以修改角色！");
				return result;
			}
			// 不允许修改保留管理员的权限
			if (getUser.getRole().getName().equals(CommonValue.Role.PRESERVE_ADMIN)) {
				result.setResultFailed("不能修改预留管理员用户的权限！");
				return result;
			}
			// 不能把用户修改成预留管理员
			if (user.getRole().getId() == 1) {
				result.setResultFailed("不能把用户修改成预留管理员！");
				return result;
			}
			roleChanged = true;
		}
		// 信息覆盖
		ClassExamine.objectOverlap(user, getUser);
		// 检查头像是否修改
		if (!user.getAvatar().equals(getUser.getAvatar())) {
			log.info("头像被修改！");
			if (!getUser.getAvatar().contains("default")) {
				String originFilePath = getUser.getAvatar();
				originFilePath = CommonValue.ResourcePath.USER_AVATAR_FOLDER + File.separator + originFilePath.substring(originFilePath.lastIndexOf("/") + 1);
				new File(originFilePath).delete();
				log.info("删除原始文件：" + originFilePath);
			}
		}
		// 密码被修改则加密储存
		if (!user.getPassword().equals(getUser.getPassword())) {
			user.setPassword(BCryptEncoder.encode(user.getPassword()));
		}
		userDAO.update(user);
		// 重新获取用户数据用于刷新缓存
		User getNewUser = userDAO.getById(user.getId());
		// 角色发生变化则发送邮件
		if (roleChanged) {
			emailService.sendRoleChangeEmail(getNewUser, (User) StpUtil.getSession().get(CommonValue.SA_USER_SESSION_INFO_KEY));
		}
		// 刷新用户session数据
		StpUtil.getSessionByLoginId(user.getId()).set(CommonValue.SA_USER_SESSION_INFO_KEY, getNewUser);
		result.setResultSuccess("修改用户成功！");
		return result;
	}

	@Override
	public Result<Void> login(User user) {
		Result<Void> result = new Result<>();
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
		StpUtil.login(getUser.getId());
		// 登录后，把信息存入用户session
		StpUtil.getSession().set(CommonValue.SA_USER_SESSION_INFO_KEY, getUser);
		result.setResultSuccess("登录成功！");
		return result;
	}

	@SaCheckPermission(CommonValue.Permission.EDIT_USER)
	@Override
	public Result<List<User>> getAll() {
		Result<List<User>> result = new Result<>();
		List<User> users = userDAO.getAll();
		result.setResultSuccess("查询全部用户成功！", users);
		return result;
	}

}