package com.gitee.swsk33.gitdocument.service;

import com.gitee.swsk33.gitdocument.dataobject.Setting;
import com.gitee.swsk33.gitdocument.model.Result;
import org.springframework.stereotype.Service;

@Service
public interface SettingService {

	/**
	 * 修改用户偏好设置
	 */
	Result<Void> update(Setting setting);

}