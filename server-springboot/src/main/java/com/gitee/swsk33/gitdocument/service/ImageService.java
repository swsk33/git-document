package com.gitee.swsk33.gitdocument.service;

import com.gitee.swsk33.gitdocument.model.Result;
import io.github.swsk33.fileliftcore.model.BinaryContent;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * 图像服务
 */
@Service
public interface ImageService {

	/**
	 * 上传图片
	 *
	 * @param file 图片文件
	 * @return 结果，包含图片文件名
	 */
	Result<String> upload(MultipartFile file);

	/**
	 * 删除图片
	 *
	 * @param filename 文件名（不带扩展名）
	 * @return 结果
	 */
	Result<Void> delete(String filename);

	/**
	 * 获取图片
	 *
	 * @param fullName 图片文件名（带扩展名）
	 * @return 结果，包含获取到的图片文件二进制数据
	 */
	Result<BinaryContent> get(String fullName);

}