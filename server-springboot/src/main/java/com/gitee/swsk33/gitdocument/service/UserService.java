package com.gitee.swsk33.gitdocument.service;

import com.gitee.swsk33.gitdocument.dataobject.User;
import com.gitee.swsk33.gitdocument.model.Result;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {

	/**
	 * 用户注册
	 *
	 * @param user 用户数据对象
	 */
	Result<Void> register(User user);

	/**
	 * 删除用户
	 *
	 * @param id 待删除的id
	 */
	Result<Void> delete(int id);

	/**
	 * 用户信息更新
	 *
	 * @param user 新的用户对象
	 */
	Result<Void> update(User user) throws Exception;

	/**
	 * 用户登录
	 *
	 * @param user 传入用户对象，用户对象中username字段可以是用户名或者邮箱，password是密码明文
	 */
	Result<Void> login(User user);

	/**
	 * 获取所有的用户
	 */
	Result<List<User>> getAll();

	/**
	 * 用户重置密码
	 * @param email 重置验证码的用户邮箱
	 * @param code 密码重置验证码
	 * @param newPassword 新的密码
	 */
	Result<Void> resetPassword(String email, String code, String newPassword);

}