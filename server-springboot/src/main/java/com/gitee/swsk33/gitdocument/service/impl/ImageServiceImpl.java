package com.gitee.swsk33.gitdocument.service.impl;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.hutool.core.io.file.FileNameUtil;
import com.gitee.swsk33.gitdocument.model.Result;
import com.gitee.swsk33.gitdocument.service.ImageService;
import io.github.swsk33.fileliftcore.model.BinaryContent;
import io.github.swsk33.fileliftcore.model.file.UploadFile;
import io.github.swsk33.fileliftcore.model.result.FileResult;
import io.github.swsk33.fileliftcore.service.UploadFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Component
public class ImageServiceImpl implements ImageService {

	@Autowired
	private UploadFileService uploadFileService;

	@SaCheckLogin
	@Override
	public Result<String> upload(MultipartFile file) {
		FileResult<UploadFile> uploadResult = uploadFileService.upload(file);
		if (!uploadResult.isSuccess()) {
			return Result.resultFailed(uploadResult.getMessage());
		}
		return Result.resultSuccess("上传图片成功！", uploadResult.getData().getName() + "." + uploadResult.getData().getFormat());
	}

	@SaCheckLogin
	@Override
	public Result<Void> delete(String filename) {
		uploadFileService.delete(FileNameUtil.mainName(filename));
		return Result.resultSuccess("删除完成！");
	}

	@Override
	public Result<BinaryContent> get(String fullName) {
		FileResult<BinaryContent> downloadResult = uploadFileService.downloadFileByFullName(fullName);
		if (!downloadResult.isSuccess()) {
			return Result.resultFailed(downloadResult.getMessage());
		}
		return Result.resultSuccess("获取图片完成！", downloadResult.getData());
	}

}