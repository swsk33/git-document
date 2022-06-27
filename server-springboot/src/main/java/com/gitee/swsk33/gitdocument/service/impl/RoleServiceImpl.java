package com.gitee.swsk33.gitdocument.service.impl;

import cn.dev33.satoken.annotation.SaCheckRole;
import com.gitee.swsk33.gitdocument.dao.RoleDAO;
import com.gitee.swsk33.gitdocument.dataobject.Role;
import com.gitee.swsk33.gitdocument.model.Result;
import com.gitee.swsk33.gitdocument.param.CommonValue;
import com.gitee.swsk33.gitdocument.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RoleServiceImpl implements RoleService {

	@Autowired
	private RoleDAO roleDAO;

	@SaCheckRole(CommonValue.Role.ADMIN)
	@Override
	public Result<List<Role>> getAll() {
		Result<List<Role>> result = new Result<>();
		result.setResultSuccess("获取角色完成！", roleDAO.getAll());
		return result;
	}

}