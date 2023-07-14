package com.gitee.swsk33.gitdocument.dao;

import com.gitee.swsk33.gitdocument.dataobject.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserDAO {

	/**
	 * 添加一个用户
	 *
	 * @param user 用户对象
	 * @return 插入记录条数
	 */
	int add(User user);

	/**
	 * 删除一个用户
	 *
	 * @param id 用户id
	 * @return 删除记录条数
	 */
	int delete(int id);

	/**
	 * 修改一个用户信息
	 *
	 * @param user 修改的用户对象
	 * @return 修改记录条数
	 */
	int update(User user);

	/**
	 * 根据id获取用户对象
	 *
	 * @param id 用户id
	 * @return 用户对象
	 */
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

	/**
	 * 获取全部用户
	 *
	 * @return 所有用户
	 */
	List<User> getAll();

	/**
	 * 根据一个文集id获取收藏这个文集的用户列表
	 *
	 * @param anthologyId 文集id
	 * @return 收藏这个文集的用户列表
	 */
	List<User> getByStarAnthology(long anthologyId);

	/**
	 * 获取全部接收新文集创建通知的用户
	 *
	 * @return 接受新文集创建通知的用户
	 */
	List<User> getByReceiveCreate();

}