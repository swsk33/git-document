package com.gitee.swsk33.gitdocument.service.impl;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.gitee.swsk33.gitdocument.model.Result;
import com.gitee.swsk33.gitdocument.param.CommonValue;
import com.gitee.swsk33.gitdocument.service.ImageService;
import com.gitee.swsk33.gitdocument.util.SnowflakeIdGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.PostConstruct;
import java.io.File;
import java.util.Random;

@Slf4j
@Component
public class ImageServiceImpl implements ImageService {

	@PostConstruct
	public void fileInitialize() {
		File userAvatar = new File(CommonValue.ResourcePath.USER_AVATAR_PATH).getAbsoluteFile();
		if (!userAvatar.exists()) {
			log.info("用户头像存放文件夹不存在！将创建一个新的：" + userAvatar.getAbsolutePath());
			userAvatar.mkdirs();
		}
		File customCover = new File(CommonValue.ResourcePath.CUSTOM_COVER_PATH).getAbsoluteFile();
		if (!customCover.exists()) {
			log.info("自定义封面存放文件夹不存在！将创建一个新的：" + customCover.getAbsolutePath());
			customCover.mkdirs();
		}
	}

	/**
	 * 存放上传的图片文件到硬盘
	 *
	 * @param file      文件对象
	 * @param path      保存到目录下
	 * @param sizeLimit 大小限制，单位MB
	 * @return 结果对象，若结果是成功状态，则返回数据体是文件名
	 */
	private Result<String> saveFileToDisk(MultipartFile file, String path, int sizeLimit) {
		Result<String> result = new Result<>();
		if (file == null) {
			result.setResultFailed("请上传图片！");
			return result;
		}
		if (file.getSize() > (long) sizeLimit * 1024 * 1024) {
			result.setResultFailed("图片大小不能大于" + sizeLimit + "MB！");
			return result;
		}
		String fileFormat = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
		if (!fileFormat.equals(".jpg") && !fileFormat.equals(".jpeg") && !fileFormat.equals(".png") && !fileFormat.equals(".bmp") && !fileFormat.equals(".gif")) {
			result.setResultFailed("图片格式必须是jpg、jpeg、png、bmp或者gif格式！");
			return result;
		}
		String fileName = SnowflakeIdGenerator.getSnowflakeId() + fileFormat;
		try {
			// 转存文件至硬盘
			file.transferTo(new File(path + File.separator + fileName).getAbsoluteFile());
		} catch (Exception e) {
			e.printStackTrace();
			result.setResultFailed("图片转存失败！请联系开发者！");
			return result;
		}
		result.setResultSuccess("上传图片成功！", fileName);
		return result;
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
		Result<String> result = saveFileToDisk(file, CommonValue.ResourcePath.USER_AVATAR_PATH, 5);
		if (result.isSuccess()) {
			result.setData(CommonValue.RequestPath.USER_AVATAR_REQUEST_PATH + result.getData());
		}
		return result;
	}

	@Override
	public Result<String> getRandomAvatar() {
		Result<String> result = new Result<>();
		result.setResultSuccess("获取随机头像成功！", CommonValue.RequestPath.DEFAULT_AVATAR_REQUEST_PATH + getRandomImage(CommonValue.ResourcePath.DEFAULT_AVATAR_PATH));
		return result;
	}

	@SaCheckLogin
	@Override
	public Result<String> uploadCover(MultipartFile file) {
		Result<String> result = saveFileToDisk(file, CommonValue.ResourcePath.CUSTOM_COVER_PATH, 8);
		if (result.isSuccess()) {
			result.setData(CommonValue.RequestPath.CUSTOM_COVER_REQUEST_PATH + result.getData());
		}
		return result;
	}

	@Override
	public Result<String> getRandomCover() {
		Result<String> result = new Result<>();
		result.setResultSuccess("获取随机封面成功！", CommonValue.RequestPath.DEFAULT_COVER_REQUEST_PATH + getRandomImage(CommonValue.ResourcePath.DEFAULT_COVER_PATH));
		return result;
	}

}