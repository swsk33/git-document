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

	@PutMapping("/set-organization/{name}")
	public Result<Void> setOrgName(@PathVariable String name) {
		return systemSettingService.setOrganizationName(name);
	}

	@GetMapping("/get-organization")
	public Result<String> getOrgName() {
		return systemSettingService.getOrganizationName();
	}

	@PutMapping("/set-allow-public/{allow}")
	public Result<Void> setAllowPublic(@PathVariable String allow) {
		return systemSettingService.setAllowPublic(Boolean.parseBoolean(allow));
	}

	@GetMapping("/get-allow-public")
	public Result<Boolean> getAllowPublic() {
		return systemSettingService.getAllowPublic();
	}

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

	@GetMapping("/get-login-image")
	public Result<String> getLoginImage() {
		return systemSettingService.getLoginBackground();
	}

	@GetMapping("/get-main-image")
	public Result<String> getMainImage() {
		return systemSettingService.getMainBackground();
	}

}