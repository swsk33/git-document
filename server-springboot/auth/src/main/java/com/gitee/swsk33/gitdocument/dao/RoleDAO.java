package com.gitee.swsk33.gitdocument.dao;

import com.gitee.swsk33.gitdocument.dataobject.Role;
import com.mybatisflex.core.BaseMapper;
import com.mybatisflex.core.query.QueryWrapper;
import org.apache.ibatis.annotations.Mapper;

import static com.gitee.swsk33.gitdocument.dataobject.table.RoleTableDef.ROLE;

@Mapper
public interface RoleDAO extends BaseMapper<Role> {

	/**
	 * 根据角色id获取角色显示名称
	 *
	 * @param id 角色id
	 * @return 角色显示名称
	 */
	default String getRoleShowNameById(Integer id) {
		QueryWrapper wrapper = QueryWrapper.create()
				.select(ROLE.SHOW_NAME)
				.where(ROLE.ID.eq(id));
		return selectObjectByQueryAs(wrapper, String.class);
	}

}