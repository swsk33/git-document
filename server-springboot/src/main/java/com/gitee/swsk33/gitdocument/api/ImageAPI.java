package com.gitee.swsk33.gitdocument.api;

import com.gitee.swsk33.gitdocument.model.Result;
import com.gitee.swsk33.gitdocument.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/image")
public class ImageAPI {

	@Autowired
	private ImageService imageService;

	@PostMapping("/upload-avatar")
	private Result<String> uploadAvatar(@RequestParam("avatar") MultipartFile file) {
		return imageService.uploadAvatar(file);
	}

	@GetMapping("/random-avatar")
	private Result<String> getRandomAvatar() {
		return imageService.getRandomAvatar();
	}

	@PostMapping("/upload-cover")
	private Result<String> uploadCover(@RequestParam("cover") MultipartFile file) {
		return imageService.uploadCover(file);
	}

	@GetMapping("/random-cover")
	private Result<String> getRandomCover() {
		return imageService.getRandomCover();
	}

}