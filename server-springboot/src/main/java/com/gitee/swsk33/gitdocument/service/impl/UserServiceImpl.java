package com.gitee.swsk33.gitdocument.service.impl;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.StrUtil;
import com.gitee.swsk33.gitdocument.dao.LoginRecordDAO;
import com.gitee.swsk33.gitdocument.dao.SettingDAO;
import com.gitee.swsk33.gitdocument.dao.SystemSettingDAO;
import com.gitee.swsk33.gitdocument.dao.UserDAO;
import com.gitee.swsk33.gitdocument.dataobject.LoginRecord;
import com.gitee.swsk33.gitdocument.dataobject.Setting;
import com.gitee.swsk33.gitdocument.dataobject.User;
import com.gitee.swsk33.gitdocument.model.Result;
import com.gitee.swsk33.gitdocument.param.PermissionName;
import com.gitee.swsk33.gitdocument.param.RoleIdName;
import com.gitee.swsk33.gitdocument.service.EmailService;
import com.gitee.swsk33.gitdocument.service.ImageService;
import com.gitee.swsk33.gitdocument.service.UserService;
import com.gitee.swsk33.gitdocument.session.UserSession;
import com.gitee.swsk33.gitdocument.util.BCryptEncoder;
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
	private LoginRecordDAO loginRecordDAO;

	@Autowired
	private SystemSettingDAO systemSettingDAO;

	@Autowired
	private ImageService imageService;

	@Autowired
	private EmailService emailService;

	@Autowired
	private UserSession userSession;

	@Override
	public Result<Void> register(User user) {
		// 没有登录的时候，是用户注册行为
		if (!StpUtil.isLogin()) {
			// 根据配置判断是否运行注册
			if (!Boolean.parseBoolean(systemSettingDAO.get(ALLOW_PUBLIC))) {
				return Result.resultFailed("本站不允许访客注册！请联系管理员！");
			}
			// 注册权限检查
			if (user.getRoleId() != RoleIdName.MEMBER_ID) {
				return Result.resultFailed("不允许注册为除了团队成员之外的角色！");
			}
		} else { // 登录的时候，是管理员添加用户行为
			if (!StpUtil.hasPermission(PermissionName.EDIT_USER)) {
				return Result.resultFailed("您没有添加用户的权限！");
			}
			if (user.getRoleId() == RoleIdName.PRESERVE_ADMIN_ID) {
				return Result.resultFailed("不允许增加预留管理员角色的用户！");
			}
		}
		// 先检查用户名是否已存在
		if (userDAO.existsByUsername(user.getUsername())) {
			return Result.resultFailed("该用户名已存在！");
		}
		// 然后检查用户邮箱是否被注册过
		if (userDAO.existsByEmail(user.getEmail())) {
			return Result.resultFailed("该邮箱已被注册！");
		}
		// 加密密码，存入数据库
		user.setPassword(BCryptEncoder.encode(user.getPassword()));
		userDAO.insert(user);
		// 新建默认用户配置并插入
		Setting setting = new Setting();
		setting.setUserId(user.getId());
		settingDAO.insert(setting);
		// 新建用户登录记录对象插入
		LoginRecord record = new LoginRecord();
		record.setUserId(user.getId());
		loginRecordDAO.insert(record);
		return Result.resultSuccess("注册用户成功！");
	}

	@SaCheckPermission(PermissionName.EDIT_USER)
	@Override
	public Result<Void> delete(int id) {
		User getUser = userDAO.selectOneWithRelationsById(id);
		if (getUser == null) {
			return Result.resultFailed("用户不存在！");
		}
		if (getUser.getRole().getName().equals(RoleIdName.PRESERVE_ADMIN_NAME)) {
			return Result.resultFailed("预留管理员账户不可以删除！");
		}
		if (userDAO.deleteById(id) < 1) {
			return Result.resultFailed("删除用户失败！");
		}
		// 使该用户退出登录
		StpUtil.logout(id);
		return Result.resultSuccess("删除用户成功！");
	}

	@SaCheckLogin
	@Override
	public Result<Void> update(User user) {
		User getUser = userDAO.selectOneWithRelationsById(user.getId());
		if (getUser == null) {
			return Result.resultFailed("找不到该用户！");
		}
		// 权限判断
		if (StpUtil.getLoginIdAsInt() != user.getId() && !StpUtil.hasPermission(PermissionName.EDIT_USER)) {
			return Result.resultFailed("您没有修改其他用户信息的权限！");
		}
		// 若权限发生修改，则进行一些判断
		if (user.getRoleId() != null && user.getRoleId().intValue() != getUser.getRoleId().intValue()) {
			// 非管理员不能修改权限
			if (!StpUtil.hasPermission(PermissionName.EDIT_USER)) {
				return Result.resultFailed("非管理员不可以修改角色！");
			}
			// 不允许修改保留管理员的权限
			if (getUser.getRole().getName().equals(RoleIdName.PRESERVE_ADMIN_NAME)) {
				return Result.resultFailed("不能修改预留管理员用户的权限！");
			}
			// 不能把用户修改成预留管理员
			if (user.getRoleId() == RoleIdName.PRESERVE_ADMIN_ID) {
				return Result.resultFailed("不能把用户修改成预留管理员！");
			}
		}
		// 检查头像是否修改
		if (!StrUtil.isEmpty(user.getAvatar()) && !user.getAvatar().equals(getUser.getAvatar())) {
			log.info("头像被修改！");
			// 删除原来的头像文件
			if (!StrUtil.isEmpty(getUser.getAvatar())) {
				imageService.delete(getUser.getAvatar());
			}
		}
		// 密码被修改则加密储存
		if (!StrUtil.isEmpty(user.getPassword()) && !user.getPassword().equals(getUser.getPassword())) {
			user.setPassword(BCryptEncoder.encode(user.getPassword()));
		}
		userDAO.update(user);
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
		// 登录后，把信息存入用户Session
		userSession.saveUserSession(getUser);
		return Result.resultSuccess("登录成功！");
	}

	@SaCheckPermission(PermissionName.EDIT_USER)
	@Override
	public Result<List<User>> getAll() {
		List<User> users = userDAO.selectAllWithRelations();
		// 去除部分属性
		users.forEach(user -> {
			user.setKeys(null);
			user.setSetting(null);
			user.setStars(null);
			user.setEmail(null);
		});
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