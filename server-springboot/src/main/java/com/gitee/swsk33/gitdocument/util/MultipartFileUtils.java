package com.gitee.swsk33.gitdocument.util;

import com.gitee.swsk33.gitdocument.model.Result;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

/**
 * 用于上传的文件的实用类
 */
public class MultipartFileUtils {

	/**
	 * 把上传的文件保存到硬盘
	 *
	 * @param file        文件对象
	 * @param path        保存到目录下，可传入相对路径（末尾不带/）
	 * @param fileName    保存文件名（不带扩展名）
	 * @param formatLimit 允许上传的扩展名（不带.）
	 * @param sizeLimit   大小限制，单位MB
	 * @return 结果对象，若结果是成功状态，则返回数据体是文件名
	 */
	public static Result<String> saveFileToDisk(MultipartFile file, String path, String fileName, String[] formatLimit, int sizeLimit) {
		Result<String> result = new Result<>();
		if (file == null) {
			result.setResultFailed("请上传图片！");
			return result;
		}
		if (file.getSize() > (long) sizeLimit * 1024 * 1024) {
			result.setResultFailed("图片大小不能大于" + sizeLimit + "MB！");
			return result;
		}
		// 判断文件格式
		String fileFormat = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);
		boolean formatContain = false;
		for (String eachFormat : formatLimit) {
			if (eachFormat.equals(fileFormat)) {
				formatContain = true;
				break;
			}
		}
		if (!formatContain) {
			result.setResultFailed("图片格式必须是" + String.join(", ", formatLimit) + "格式！");
			return result;
		}
		try {
			// 转存文件至硬盘
			file.transferTo(new File(path + File.separator + fileName + "." + fileFormat).getAbsoluteFile());
		} catch (Exception e) {
			e.printStackTrace();
			result.setResultFailed("图片转存失败！请联系开发者！");
			return result;
		}
		result.setResultSuccess("上传图片成功！", fileName);
		return result;
	}

	/**
	 * 存放上传的图片文件到硬盘，使用雪花id文件名
	 *
	 * @param file      文件对象
	 * @param path      保存到目录下，可传入相对路径
	 * @param sizeLimit 大小限制，单位MB
	 * @return 结果对象，若结果是成功状态，则返回数据体是文件名
	 */
	public static Result<String> saveImageFileToDisk(MultipartFile file, String path, int sizeLimit) {
		return saveFileToDisk(file, path, String.valueOf(SnowflakeIdGenerator.getSnowflakeId()), new String[]{"jpg", "jpeg", "png", "bmp", "gif"}, sizeLimit);
	}

}