package com.gitee.swsk33.gitdocument.api;

import com.gitee.swsk33.gitdocument.model.Result;
import com.gitee.swsk33.gitdocument.service.SystemSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/system-setting")
public class SystemSettingAPI {

	@Autowired
	private SystemSettingService systemSettingService;

	@GetMapping("/reset-login-image")
	public Result<Void> resetLoginImage() {
		return systemSettingService.resetLoginBackground();
	}

	@GetMapping("/reset-main-image")
	public Result<Void> resetMainImage() {
		return systemSettingService.resetMainBackground();
	}

	@PostMapping("/set-login-image")
	public Result<Void> setLoginImage(@RequestParam("image") MultipartFile image) {
		return systemSettingService.customLoginBackground(image);
	}

	@PostMapping("/set-main-image")
	public Result<Void> setMainImage(@RequestParam("image") MultipartFile image) {
		return systemSettingService.customMainBackground(image);
	}

}