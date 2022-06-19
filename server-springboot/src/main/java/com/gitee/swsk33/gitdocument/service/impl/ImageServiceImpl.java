package com.gitee.swsk33.gitdocument.service.impl;

import com.gitee.swsk33.gitdocument.model.Result;
import com.gitee.swsk33.gitdocument.param.CommonValue;
import com.gitee.swsk33.gitdocument.service.ImageService;
import com.gitee.swsk33.gitdocument.util.SnowflakeIdGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
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
	}

	@Override
	public Result<String> upload(MultipartFile file) {
		Result<String> result = new Result<>();
		if (file == null) {
			result.setResultFailed("请上传图片！");
			return result;
		}
		if (file.getSize() > 5242880) {
			result.setResultFailed("图片大小不能大于5MB！");
			return result;
		}
		String fileFormat = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
		if (!fileFormat.equals(".jpg") && !fileFormat.equals(".jpeg") && !fileFormat.equals(".png") && !fileFormat.equals(".bmp") && !fileFormat.equals(".gif")) {
			result.setResultFailed("图片格式必须是jpg、jpeg、png、bmp或者gif格式！");
			return result;
		}
		String fileName = SnowflakeIdGenerator.getSnowflakeId() + fileFormat;
		try {
			// 写入到用户头像文件夹下
			file.transferTo(new File(CommonValue.ResourcePath.USER_AVATAR_PATH + File.separator + fileName).getAbsoluteFile());
		} catch (Exception e) {
			e.printStackTrace();
			result.setResultFailed("图片转存失败！请联系开发者！");
			return result;
		}
		String imageRequestPath = CommonValue.RequestPath.USER_AVATAR_REQUEST_PATH + fileName;
		result.setResultSuccess("上传头像成功！", imageRequestPath);
		return result;
	}

	@Override
	public Result<String> getRandomAvatar() {
		// 获取默认文件夹中的头像名
		String[] imageFileNames = new File(CommonValue.ResourcePath.DEFAULT_AVATAR_PATH).getAbsoluteFile().list();
		// 随机对象
		Random random = new Random();
		String getAvatar = CommonValue.RequestPath.DEFAULT_AVATAR_REQUEST_PATH + imageFileNames[random.nextInt(imageFileNames.length)];
		Result<String> result = new Result<>();
		result.setResultSuccess("获取随机头像成功！", getAvatar);
		return result;
	}

}