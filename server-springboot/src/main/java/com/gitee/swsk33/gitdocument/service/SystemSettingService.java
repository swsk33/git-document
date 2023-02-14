package com.gitee.swsk33.gitdocument.service;

import com.gitee.swsk33.gitdocument.model.Result;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * 用于全局系统设置的服务
 */
@Service
public interface SystemSettingService {

	/**
	 * 重置登录背景为默认背景
	 */
	Result<Void> resetLoginBackground();

	/**
	 * 重置主面板背景为默认背景
	 */
	Result<Void> resetMainBackground();

	/**
	 * 上传并设定自定义登录背景
	 */
	Result<Void> customLoginBackground(MultipartFile image);

	/**
	 * 上传并设定自定义主面板背景
	 */
	Result<Void> customMainBackground(MultipartFile image);

}