package com.gitee.swsk33.gitdocument.service.impl;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import com.gitee.swsk33.gitdocument.dao.SettingDAO;
import com.gitee.swsk33.gitdocument.dao.UserDAO;
import com.gitee.swsk33.gitdocument.dataobject.Setting;
import com.gitee.swsk33.gitdocument.model.Result;
import com.gitee.swsk33.gitdocument.param.CommonValue;
import com.gitee.swsk33.gitdocument.service.SettingService;
import com.gitee.swsk33.gitdocument.util.ClassExamine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SettingServiceImpl implements SettingService {

	@Autowired
	private SettingDAO settingDAO;

	@Autowired
	private UserDAO userDAO;

	@SaCheckLogin
	@Override
	public Result<Void> update(Setting setting) throws Exception {
		Setting getSetting = settingDAO.selectOneById(setting.getUserId());
		if (getSetting == null) {
			return Result.resultFailed("设置对象不存在！");
		}
		// 检测当前登录用户的id是否和被修改的设置一致
		if (StpUtil.getLoginIdAsInt() != getSetting.getUserId()) {
			return Result.resultFailed("你不能修改别人的设置！");
		}
		// 对象堆叠
		ClassExamine.objectOverlap(setting, getSetting);
		settingDAO.update(setting);
		// 刷新session的用户对象
		StpUtil.getSession().set(CommonValue.SA_USER_SESSION_INFO_KEY, userDAO.selectOneWithRelationsById(setting.getUserId()));
		return Result.resultSuccess("修改设置完成！");
	}

}