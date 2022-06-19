package com.gitee.swsk33.gitdocument.service;

import com.gitee.swsk33.gitdocument.model.Result;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * 图像服务
 */
@Service
public interface ImageService {

	/**
	 * 上传图片
	 */
	Result<String> upload(MultipartFile file);

	/**
	 * 获取一个随机的默认头像
	 */
	Result<String> getRandomAvatar();

}