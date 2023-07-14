package com.gitee.swsk33.gitdocument.dao;

import com.gitee.swsk33.gitdocument.dataobject.Role;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RoleDAO {

	/**
	 * 根据id获取角色对象
	 *
	 * @param id 角色id
	 * @return 角色对象
	 */
	Role getById(int id);

	/**
	 * 获取全部角色对象
	 *
	 * @return 全部角色对象
	 */
	List<Role> getAll();

}