package com.gitee.swsk33.gitdocument.service.impl;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.StrUtil;
import com.gitee.swsk33.gitdocument.dao.SettingDAO;
import com.gitee.swsk33.gitdocument.dao.SystemSettingDAO;
import com.gitee.swsk33.gitdocument.dao.UserDAO;
import com.gitee.swsk33.gitdocument.dataobject.Setting;
import com.gitee.swsk33.gitdocument.dataobject.User;
import com.gitee.swsk33.gitdocument.model.Result;
import com.gitee.swsk33.gitdocument.param.CommonValue;
import com.gitee.swsk33.gitdocument.param.PermissionName;
import com.gitee.swsk33.gitdocument.param.RoleName;
import com.gitee.swsk33.gitdocument.service.EmailService;
import com.gitee.swsk33.gitdocument.service.ImageService;
import com.gitee.swsk33.gitdocument.service.UserService;
import com.gitee.swsk33.gitdocument.util.BCryptEncoder;
import com.gitee.swsk33.gitdocument.util.ClassExamine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.gitee.swsk33.gitdocument.param.SystemSettingKey.ALLOW_PUBLIC;

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
	private SystemSettingDAO systemSettingDAO;

	@Override
	public Result<Void> register(User user) {
		// 没有登录的时候，是用户注册行为
		if (!StpUtil.isLogin()) {
			// 根据配置判断是否运行注册
			if (!Boolean.parseBoolean(systemSettingDAO.get(ALLOW_PUBLIC))) {
				return Result.resultFailed("本站不允许访客注册！请联系管理员！");
			}
			// 注册权限检查
			if (user.getRole().getId() == 1 || user.getRole().getId() == 2) {
				return Result.resultFailed("不允许注册成管理员！");
			}
		} else { // 登录的时候，是管理员添加用户行为
			if (!StpUtil.hasPermission(PermissionName.EDIT_USER)) {
				return Result.resultFailed("您没有添加用户的权限！");
			}
			if (user.getRole().getId() == 1) {
				return Result.resultFailed("不允许增加预留管理员角色的用户！");
			}
		}
		// 先检查用户是否已存在
		User getUser = userDAO.getByUsernameOrEmail(user.getUsername());
		if (getUser != null) {
			return Result.resultFailed("该用户名已存在！");
		}
		// 然后检查用户邮箱是否被注册过
		getUser = userDAO.getByUsernameOrEmail(user.getEmail());
		if (getUser != null) {
			return Result.resultFailed("该邮箱已被注册！");
		}
		// 加密密码，存入数据库
		user.setPassword(BCryptEncoder.encode(user.getPassword()));
		// 新建默认用户配置
		Setting setting = new Setting();
		setting.setReceiveUpdateEmail(true);
		setting.setReceiveNewEmail(true);
		settingDAO.add(setting);
		user.setSetting(setting);
		userDAO.add(user);
		return Result.resultSuccess("注册用户成功！");
	}

	@SaCheckPermission(PermissionName.EDIT_USER)
	@Override
	public Result<Void> delete(int id) {
		User getUser = userDAO.getById(id);
		if (getUser == null) {
			return Result.resultFailed("用户不存在！");
		}
		if (getUser.getRole().getName().equals(RoleName.PRESERVE_ADMIN)) {
			return Result.resultFailed("预留管理员账户不可以删除！");
		}
		if (userDAO.delete(id) < 1) {
			return Result.resultFailed("删除用户失败！");
		}
		return Result.resultSuccess("删除用户成功！");
	}

	@SaCheckLogin
	@Override
	public Result<Void> update(User user) throws Exception {
		User getUser = userDAO.getById(user.getId());
		if (getUser == null) {
			return Result.resultFailed("找不到该用户！");
		}
		// 权限判断
		if (!StpUtil.hasPermission(PermissionName.EDIT_USER) && StpUtil.getLoginId(0) != user.getId().intValue()) {
			return Result.resultFailed("您没有修改其他用户信息的权限！");
		}
		// 若权限发生修改，则进行一些判断
		boolean roleChanged = false;
		if (user.getRole() != null && user.getRole().getId().intValue() != getUser.getRole().getId().intValue()) {
			// 非管理员不能修改权限
			if (!StpUtil.hasPermission(PermissionName.EDIT_USER)) {
				return Result.resultFailed("非管理员不可以修改角色！");
			}
			// 不允许修改保留管理员的权限
			if (getUser.getRole().getName().equals(RoleName.PRESERVE_ADMIN)) {
				return Result.resultFailed("不能修改预留管理员用户的权限！");
			}
			// 不能把用户修改成预留管理员
			if (user.getRole().getId() == 1) {
				return Result.resultFailed("不能把用户修改成预留管理员！");
			}
			roleChanged = true;
		}
		// 信息覆盖
		ClassExamine.objectOverlap(user, getUser);
		// 检查头像是否修改
		if (!StrUtil.isEmpty(user.getAvatar()) && !user.getAvatar().equals(getUser.getAvatar())) {
			log.info("头像被修改！");
			// 删除原来的头像文件
			if (!StrUtil.isEmpty(getUser.getAvatar())) {
				imageService.delete(getUser.getAvatar());
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
		return Result.resultSuccess("修改用户成功！");
	}

	@Override
	public Result<Void> login(User user) {
		// 登录传来的用户名可以是用户名也可以是邮箱
		User getUser = userDAO.getByUsernameOrEmail(user.getUsername());
		if (getUser == null) {
			return Result.resultFailed("用户名或者邮箱不存在！");
		}
		// 密码比对
		if (!BCryptEncoder.check(user.getPassword(), getUser.getPassword())) {
			return Result.resultFailed("密码错误！");
		}
		// 执行登录
		StpUtil.login(getUser.getId());
		// 登录后，把信息存入用户session
		StpUtil.getSession().set(CommonValue.SA_USER_SESSION_INFO_KEY, getUser);
		return Result.resultSuccess("登录成功！");
	}

	@SaCheckPermission(PermissionName.EDIT_USER)
	@Override
	public Result<List<User>> getAll() {
		List<User> users = userDAO.getAll();
		return Result.resultSuccess("查询全部用户成功！", users);
	}

	@Override
	public Result<Void> resetPassword(User user, String code) {
		// 获取对应的用户
		User getUser = userDAO.getByUsernameOrEmail(user.getEmail());
		if (getUser == null) {
			return Result.resultFailed("该邮箱未注册！");
		}
		// 检查验证码是否正确
		if (!emailService.verifyPasswordResetCode(getUser.getId(), code)) {
			return Result.resultFailed("验证码错误！");
		}
		// 修改密码加密保存
		getUser.setPassword(BCryptEncoder.encode(user.getPassword()));
		userDAO.update(getUser);
		return Result.resultSuccess("密码重置成功！");
	}

}