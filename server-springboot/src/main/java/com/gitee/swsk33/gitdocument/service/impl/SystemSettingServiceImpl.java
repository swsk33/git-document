package com.gitee.swsk33.gitdocument.service.impl;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.gitee.swsk33.gitdocument.model.Result;
import com.gitee.swsk33.gitdocument.param.CommonValue;
import com.gitee.swsk33.gitdocument.service.SystemSettingService;
import com.gitee.swsk33.gitdocument.util.MultipartFileUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@Component
public class SystemSettingServiceImpl implements SystemSettingService {

	@SaCheckPermission(CommonValue.Permission.ALTER_SYSTEM_SETTING)
	@Override
	public Result<Void> resetLoginBackground() {
		Result<Void> result = new Result<>();
		File imageFile = new File(CommonValue.ResourcePath.CUSTOM_LOGIN_BACKGROUND_FILE);
		if (imageFile.exists() && !imageFile.delete()) {
			result.setResultFailed("重置失败！请联系开发者！");
			return result;
		}
		result.setResultSuccess("重置登录背景图成功！刷新网页生效！");
		return result;
	}

	@SaCheckPermission(CommonValue.Permission.ALTER_SYSTEM_SETTING)
	@Override
	public Result<Void> resetMainBackground() {
		Result<Void> result = new Result<>();
		File imageFile = new File(CommonValue.ResourcePath.CUSTOM_BACKGROUND_FILE);
		if (imageFile.exists() && !imageFile.delete()) {
			result.setResultFailed("重置失败！请联系开发者！");
			return result;
		}
		result.setResultSuccess("重置主面板背景图成功！刷新网页生效！");
		return result;
	}

	@SaCheckPermission(CommonValue.Permission.ALTER_SYSTEM_SETTING)
	@Override
	public Result<Void> customLoginBackground(MultipartFile image) {
		Result<Void> result = new Result<>();
		File imageFile = new File(CommonValue.ResourcePath.CUSTOM_LOGIN_BACKGROUND_FILE);
		if (imageFile.exists() && !imageFile.delete()) {
			result.setResultFailed("删除旧图片失败！请联系开发者！");
			return result;
		}
		Result<String> uploadResult = MultipartFileUtils.saveFileToDisk(image, CommonValue.ResourcePath.CUSTOM_LOGIN_BACKGROUND_FOLDER, "custom", new String[]{"jpg"}, 10);
		if (!uploadResult.isSuccess()) {
			result.setResultFailed(uploadResult.getMessage());
			return result;
		}
		result.setResultSuccess("设定登录背景图成功！刷新网页后生效！");
		return result;
	}

	@SaCheckPermission(CommonValue.Permission.ALTER_SYSTEM_SETTING)
	@Override
	public Result<Void> customMainBackground(MultipartFile image) {
		Result<Void> result = new Result<>();
		File imageFile = new File(CommonValue.ResourcePath.CUSTOM_BACKGROUND_FILE);
		if (imageFile.exists() && !imageFile.delete()) {
			result.setResultFailed("删除旧图片失败！请联系开发者！");
			return result;
		}
		Result<String> uploadResult = MultipartFileUtils.saveFileToDisk(image, CommonValue.ResourcePath.CUSTOM_BACKGROUND_FOLDER, "custom", new String[]{"jpg"}, 10);
		if (!uploadResult.isSuccess()) {
			result.setResultFailed(uploadResult.getMessage());
			return result;
		}
		result.setResultSuccess("设定主面板背景图成功！刷新网页后生效！");
		return result;
	}

}