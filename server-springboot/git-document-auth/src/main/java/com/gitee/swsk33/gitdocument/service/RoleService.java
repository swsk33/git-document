package com.gitee.swsk33.gitdocument.service;

import com.gitee.swsk33.gitdocument.dataobject.Role;
import com.gitee.swsk33.gitdocument.model.Result;

import java.util.List;

public interface RoleService {

	/**
	 * 获取全部角色
	 */
	Result<List<Role>> getAll();

}