package com.gitee.swsk33.gitdocument.dao;

import com.gitee.swsk33.gitdocument.dataobject.Permission;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PermissionDAO {

	/**
	 * 根据id获取权限对象
	 *
	 * @param id 权限id
	 * @return 权限对象
	 */
	Permission getById(int id);

}