package com.gitee.swsk33.gitdocument.service.impl;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.gitee.swsk33.gitdocument.dao.RoleDAO;
import com.gitee.swsk33.gitdocument.dataobject.Role;
import com.gitee.swsk33.gitdocument.model.Result;
import com.gitee.swsk33.gitdocument.param.PermissionName;
import com.gitee.swsk33.gitdocument.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RoleServiceImpl implements RoleService {

	@Autowired
	private RoleDAO roleDAO;

	@SaCheckPermission(PermissionName.EDIT_USER)
	@Override
	public Result<List<Role>> getAll() {
		List<Role> roles = roleDAO.selectAllWithRelations();
		// 移除重合的部分属性
		roles.forEach(role -> {
			role.getPermissions().forEach(permission -> {
				permission.setRoles(null);
			});
		});
		return Result.resultSuccess("获取角色完成！", roles);
	}

}