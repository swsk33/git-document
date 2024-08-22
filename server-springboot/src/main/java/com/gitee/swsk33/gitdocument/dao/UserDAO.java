package com.gitee.swsk33.gitdocument.dao;

import com.gitee.swsk33.gitdocument.dataobject.User;
import com.mybatisflex.core.BaseMapper;
import com.mybatisflex.core.query.QueryWrapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

import static com.gitee.swsk33.gitdocument.dataobject.table.SettingTableDef.SETTING;
import static com.gitee.swsk33.gitdocument.dataobject.table.StarTableDef.STAR;
import static com.gitee.swsk33.gitdocument.dataobject.table.UserTableDef.USER;
import static com.mybatisflex.core.query.QueryMethods.number;

@Mapper
public interface UserDAO extends BaseMapper<User> {

	/**
	 * 根据用户名或者邮箱获取用户
	 *
	 * @param credential 用户名或者邮箱
	 * @return 查得的用户对象
	 */
	default User getByUsernameOrEmail(String credential) {
		return selectOneWithRelationsByQuery(QueryWrapper.create().where(USER.USERNAME.eq(credential).or(USER.EMAIL.eq(credential))));
	}

	/**
	 * 根据一个文集id获取收藏这个文集的用户列表
	 *
	 * @param anthologyId 文集id
	 * @return 收藏这个文集的用户列表
	 */
	default List<User> getByStarAnthology(long anthologyId) {
		return selectListByQuery(QueryWrapper.create().select(USER.ALL_COLUMNS).from(USER)
				.innerJoin(STAR).on(USER.ID.eq(STAR.USER_ID))
				.where(STAR.ANTHOLOGY_ID.eq(anthologyId)));
	}

	/**
	 * 获取全部接收新文集创建通知的用户
	 *
	 * @return 接受新文集创建通知的用户
	 */
	default List<User> getByReceiveCreate() {
		return selectListByQuery(QueryWrapper.create().select(USER.ALL_COLUMNS).from(USER)
				.innerJoin(SETTING).on(USER.ID.eq(STAR.USER_ID))
				.where(SETTING.RECEIVE_NEW_EMAIL.eq(true)));
	}

	/**
	 * 根据用户id判断用户是否存在
	 *
	 * @param id 用户id
	 * @return 存在返回true
	 */
	default boolean existsById(int id) {
		QueryWrapper wrapper = QueryWrapper.create().select(number(1)).where(USER.ID.eq(id));
		Long exists = selectObjectByQueryAs(wrapper, Long.class);
		return exists != null;
	}

	/**
	 * 根据用户名判断用户是否存在
	 *
	 * @param username 用户名
	 * @return 存在返回true
	 */
	default boolean existsByUsername(String username) {
		QueryWrapper wrapper = QueryWrapper.create().select(number(1)).where(USER.USERNAME.eq(username));
		Long exists = selectObjectByQueryAs(wrapper, Long.class);
		return exists != null;
	}

	/**
	 * 根据用户邮箱字段判断用户是否存在
	 *
	 * @param email 用户邮箱
	 * @return 存在返回true
	 */
	default boolean existsByEmail(String email) {
		QueryWrapper wrapper = QueryWrapper.create().select(number(1)).where(USER.EMAIL.eq(email));
		Long exists = selectObjectByQueryAs(wrapper, Long.class);
		return exists != null;
	}

}