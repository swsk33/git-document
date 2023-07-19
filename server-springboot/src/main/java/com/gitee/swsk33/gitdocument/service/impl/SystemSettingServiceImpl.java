package com.gitee.swsk33.gitdocument.service.impl;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.hutool.core.util.StrUtil;
import com.gitee.swsk33.gitdocument.dao.SystemSettingDAO;
import com.gitee.swsk33.gitdocument.model.Result;
import com.gitee.swsk33.gitdocument.param.PermissionName;
import com.gitee.swsk33.gitdocument.service.ImageService;
import com.gitee.swsk33.gitdocument.service.SystemSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import static com.gitee.swsk33.gitdocument.param.SystemSettingKey.*;

@Component
public class SystemSettingServiceImpl implements SystemSettingService {

	@Autowired
	private SystemSettingDAO systemSettingDAO;

	@Autowired
	private ImageService imageService;

	/**
	 * 删除背景图
	 *
	 * @param configKey 配置键，指定是登录页还是主页背景图
	 */
	private void deleteBackgroundImage(String configKey) {
		String lastImage = systemSettingDAO.get(configKey);
		if (!StrUtil.isEmpty(lastImage)) {
			imageService.delete(lastImage);
		}
	}

	@SaCheckPermission(PermissionName.ALTER_SYSTEM_SETTING)
	@Override
	public Result<Void> setOrganizationName(String name) {
		return systemSettingDAO.set(ORGANIZATION_NAME, name) < 1 ? Result.resultFailed("设定组织名失败！") : Result.resultSuccess("设定组织名成功！");
	}

	@Override
	public Result<String> getOrganizationName() {
		String getName = systemSettingDAO.get(ORGANIZATION_NAME);
		return getName == null ? Result.resultFailed("获取组织名失败！") : Result.resultSuccess("获取组织名成功！", getName);
	}

	@SaCheckPermission(PermissionName.ALTER_SYSTEM_SETTING)
	@Override
	public Result<Void> setAllowPublic(boolean allow) {
		return systemSettingDAO.set(ALLOW_PUBLIC, String.valueOf(allow)) < 1 ? Result.resultFailed("设定允许公开失败！") : Result.resultSuccess("设定允许公开成功！");
	}

	@Override
	public Result<Boolean> getAllowPublic() {
		String getAllow = systemSettingDAO.get(ALLOW_PUBLIC);
		return getAllow == null ? Result.resultFailed("获取是否允许公开失败！") : Result.resultSuccess("获取是否允许公开成功！", Boolean.parseBoolean(getAllow));
	}

	@SaCheckPermission(PermissionName.ALTER_SYSTEM_SETTING)
	@Override
	public Result<Void> resetLoginBackground() {
		// 删除上一个背景图
		deleteBackgroundImage(LOGIN_BACKGROUND_IMAGE);
		systemSettingDAO.set(LOGIN_BACKGROUND_IMAGE, null);
		return Result.resultSuccess("重置完成！");
	}

	@SaCheckPermission(PermissionName.ALTER_SYSTEM_SETTING)
	@Override
	public Result<Void> resetMainBackground() {
		// 删除上一个背景图
		deleteBackgroundImage(MAIN_BACKGROUND_IMAGE);
		systemSettingDAO.set(MAIN_BACKGROUND_IMAGE, null);
		return Result.resultSuccess("重置完成！");
	}

	@SaCheckPermission(PermissionName.ALTER_SYSTEM_SETTING)
	@Override
	public Result<Void> customLoginBackground(MultipartFile image) {
		Result<String> uploadResult = imageService.upload(image);
		if (!uploadResult.isSuccess()) {
			return Result.resultFailed(uploadResult.getMessage());
		}
		// 删除上一个背景图
		deleteBackgroundImage(LOGIN_BACKGROUND_IMAGE);
		systemSettingDAO.set(LOGIN_BACKGROUND_IMAGE, uploadResult.getData());
		return Result.resultSuccess("设定登录页背景图成功！");
	}

	@SaCheckPermission(PermissionName.ALTER_SYSTEM_SETTING)
	@Override
	public Result<Void> customMainBackground(MultipartFile image) {
		Result<String> uploadResult = imageService.upload(image);
		if (!uploadResult.isSuccess()) {
			return Result.resultFailed(uploadResult.getMessage());
		}
		// 删除上一个背景图
		deleteBackgroundImage(MAIN_BACKGROUND_IMAGE);
		systemSettingDAO.set(MAIN_BACKGROUND_IMAGE, uploadResult.getData());
		return Result.resultSuccess("设定主页背景图成功！");
	}

	@Override
	public Result<String> getLoginBackground() {
		return Result.resultSuccess("获取完成！", systemSettingDAO.get(LOGIN_BACKGROUND_IMAGE));
	}

	@Override
	public Result<String> getMainBackground() {
		return Result.resultSuccess("获取完成！", systemSettingDAO.get(MAIN_BACKGROUND_IMAGE));
	}

}