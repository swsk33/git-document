package com.gitee.swsk33.gitdocument.service.impl;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.gitee.swsk33.gitdocument.model.Result;
import com.gitee.swsk33.gitdocument.param.CommonValue;
import com.gitee.swsk33.gitdocument.service.ImageService;
import com.gitee.swsk33.gitdocument.util.MultipartFileUtils;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Random;

@Slf4j
@Component
public class ImageServiceImpl implements ImageService {

	@PostConstruct
	public void fileInitialize() {
		File userAvatar = new File(CommonValue.ResourcePath.USER_AVATAR_FOLDER).getAbsoluteFile();
		if (!userAvatar.exists()) {
			log.info("用户头像存放文件夹不存在！将创建一个新的：" + userAvatar.getAbsolutePath());
			userAvatar.mkdirs();
		}
		File customCover = new File(CommonValue.ResourcePath.CUSTOM_COVER_FOLDER).getAbsoluteFile();
		if (!customCover.exists()) {
			log.info("自定义封面存放文件夹不存在！将创建一个新的：" + customCover.getAbsolutePath());
			customCover.mkdirs();
		}
	}

	/**
	 * 获取默认随机图片
	 *
	 * @param path 从哪个路径获取
	 * @return 获取到的结果，为文件名
	 */
	private String getRandomImage(String path) {
		// 获取默认文件夹中的头像名
		String[] imageFileNames = new File(path).getAbsoluteFile().list();
		// 随机对象
		Random random = new Random();
		return imageFileNames[random.nextInt(imageFileNames.length)];
	}

	@SaCheckLogin
	@Override
	public Result<String> uploadAvatar(MultipartFile file) {
		Result<String> result = MultipartFileUtils.saveImageFileToDisk(file, CommonValue.ResourcePath.USER_AVATAR_FOLDER, 5);
		if (result.isSuccess()) {
			result.setData(CommonValue.RequestPath.USER_AVATAR_REQUEST_PATH + result.getData());
		}
		return result;
	}

	@Override
	public Result<String> getRandomAvatar() {
		Result<String> result = new Result<>();
		result.setResultSuccess("获取随机头像成功！", CommonValue.RequestPath.DEFAULT_AVATAR_REQUEST_PATH + getRandomImage(CommonValue.ResourcePath.DEFAULT_AVATAR_FOLDER));
		return result;
	}

	@SaCheckLogin
	@Override
	public Result<String> uploadCover(MultipartFile file) {
		Result<String> result = MultipartFileUtils.saveImageFileToDisk(file, CommonValue.ResourcePath.CUSTOM_COVER_FOLDER, 8);
		if (result.isSuccess()) {
			result.setData(CommonValue.RequestPath.CUSTOM_COVER_REQUEST_PATH + result.getData());
		}
		return result;
	}

	@Override
	public Result<String> getRandomCover() {
		Result<String> result = new Result<>();
		result.setResultSuccess("获取随机封面成功！", CommonValue.RequestPath.DEFAULT_COVER_REQUEST_PATH + getRandomImage(CommonValue.ResourcePath.DEFAULT_COVER_FOLDER));
		return result;
	}

}