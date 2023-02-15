package com.gitee.swsk33.gitdocument.dao;

import com.gitee.swsk33.gitdocument.dataobject.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserDAO {

	int add(User user);

	int delete(int id);

	int update(User user);

	User getById(int id);

	/**
	 * 根据用户名或者邮箱获取用户
	 *
	 * @param credential 用户名或者邮箱
	 * @return 查得的用户对象
	 */
	User getByUsernameOrEmail(String credential);

	/**
	 * 根据设置id获取这个设置的用户对象
	 *
	 * @param settingId 设置id
	 * @return 用户id
	 */
	User getUserBySettingId(int settingId);

	List<User> getAll();

	List<User> getAllAdmin();

}