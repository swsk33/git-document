package com.gitee.swsk33.gitdocument.service.impl;

import cn.dev33.satoken.stp.StpInterface;
import cn.dev33.satoken.stp.StpUtil;
import com.gitee.swsk33.gitdocument.dataobject.Permission;
import com.gitee.swsk33.gitdocument.dataobject.User;
import com.gitee.swsk33.gitdocument.param.CommonValue;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义sa-token框架用户权限获取
 */
@Component
public class StpInterFaceImpl implements StpInterface {

	/**
	 * 定义如何获取一个用户的权限
	 *
	 * @param loginId   用户登录id，这里就以用户的主键id作为登录id
	 * @param loginType 登录类型
	 * @return 权限列表
	 */
	@Override
	public List<String> getPermissionList(Object loginId, String loginType) {
		User getUser = (User) StpUtil.getSession().get(CommonValue.SA_USER_SESSION_INFO_KEY);
		List<Permission> permissions = getUser.getRole().getPermissions();
		List<String> result = new ArrayList<>();
		for (Permission permission : permissions) {
			result.add(permission.getName());
		}
		return result;
	}

	/**
	 * 定义如何获取一个用户的角色
	 *
	 * @param loginId   用户登录id，这里就以用户的主键id作为登录id
	 * @param loginType 登录类型
	 * @return 角色列表
	 */
	@Override
	public List<String> getRoleList(Object loginId, String loginType) {
		User getUser = (User) StpUtil.getSession().get(CommonValue.SA_USER_SESSION_INFO_KEY);
		List<String> result = new ArrayList<>();
		result.add(getUser.getRole().getName());
		return result;
	}

}