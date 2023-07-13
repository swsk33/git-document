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
		Result<String> result = new Result<>();
		if (!uploadResult.isSuccess()) {
			result.setResultFailed(uploadResult.getMessage());
			return result;
		}
		result.setResultSuccess("上传图片成功！", uploadResult.getData().getName() + "." + uploadResult.getData().getFormat());
		return result;
	}

	@SaCheckLogin
	@Override
	public Result<Void> delete(String filename) {
		uploadFileService.delete(FileNameUtil.mainName(filename));
		Result<Void> result = new Result<>();
		result.setResultSuccess("删除完成！");
		return result;
	}

	@Override
	public Result<BinaryContent> get(String fullName) {
		FileResult<BinaryContent> downloadResult = uploadFileService.downloadFileByFullName(fullName);
		Result<BinaryContent> result = new Result<>();
		if (!downloadResult.isSuccess()) {
			result.setResultFailed(downloadResult.getMessage());
			return result;
		}
		result.setResultSuccess("获取图片完成！", downloadResult.getData());
		return result;
	}

}