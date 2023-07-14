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
	 * 设定组织名
	 */
	Result<Void> setOrganizationName(String name);

	/**
	 * 获取组织名
	 */
	Result<String> getOrganizationName();

	/**
	 * 设定是否允许公开登录
	 */
	Result<Void> setAllowPublic(boolean allow);

	/**
	 * 获取是否允许公开登录
	 */
	Result<Boolean> getAllowPublic();

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

	/**
	 * 获取登录页面背景图片文件，返回文件名
	 */
	Result<String> getLoginBackground();

	/**
	 * 获取主页面背景图片文件，返回文件名
	 */
	Result<String> getMainBackground();

}