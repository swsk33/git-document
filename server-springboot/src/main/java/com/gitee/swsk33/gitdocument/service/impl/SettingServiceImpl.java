package com.gitee.swsk33.gitdocument.service.impl;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import com.gitee.swsk33.gitdocument.dao.SettingDAO;
import com.gitee.swsk33.gitdocument.dataobject.Setting;
import com.gitee.swsk33.gitdocument.model.Result;
import com.gitee.swsk33.gitdocument.service.SettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SettingServiceImpl implements SettingService {

	@Autowired
	private SettingDAO settingDAO;

	@SaCheckLogin
	@Override
	public Result<Void> update(Setting setting) {
		Setting getSetting = settingDAO.selectOneById(setting.getUserId());
		if (getSetting == null) {
			return Result.resultFailed("设置对象不存在！");
		}
		// 检测当前登录用户的id是否和被修改的设置一致
		if (StpUtil.getLoginIdAsInt() != getSetting.getUserId()) {
			return Result.resultFailed("不可以修改别人的设置！");
		}
		settingDAO.update(setting);
		return Result.resultSuccess("修改设置完成！");
	}

}