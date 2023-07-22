package com.gitee.swsk33.gitdocument.api;

import com.gitee.swsk33.gitdocument.dataobject.Setting;
import com.gitee.swsk33.gitdocument.model.Result;
import com.gitee.swsk33.gitdocument.service.SettingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/setting")
public class SettingAPI {

	@Autowired
	private SettingService settingService;

	@PatchMapping("/update")
	public Result<Void> updateSetting(@RequestBody @Valid Setting setting, BindingResult error) throws Exception {
		if (error.hasErrors()) {
			return Result.resultFailed(error.getFieldError().getDefaultMessage());
		}
		return settingService.update(setting);
	}

}