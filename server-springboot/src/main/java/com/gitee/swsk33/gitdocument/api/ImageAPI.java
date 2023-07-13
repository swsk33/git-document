package com.gitee.swsk33.gitdocument.api;

import com.gitee.swsk33.gitdocument.model.Result;
import com.gitee.swsk33.gitdocument.service.ImageService;
import io.github.swsk33.fileliftcore.model.BinaryContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/image")
public class ImageAPI {

	@Autowired
	private ImageService imageService;

	/**
	 * 上传图片文件
	 */
	@PostMapping("/upload")
	public Result<String> upload(@RequestParam("image") MultipartFile file) {
		return imageService.upload(file);
	}

	/**
	 * 通过完整图片名直接获取文件并下载
	 */
	@GetMapping("/get/{fullName}")
	public ResponseEntity<byte[]> get(@PathVariable String fullName) {
		Result<BinaryContent> result = imageService.get(fullName);
		if (!result.isSuccess()) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok()
				// 设定content-type
				.contentType(MediaType.parseMediaType(result.getData().getContentType()))
				// 设定响应体
				.body(result.getData().getByteAndClose());
	}

}