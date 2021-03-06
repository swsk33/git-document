package com.gitee.swsk33.gitdocument.api;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.gitee.swsk33.gitdocument.config.ConfigProperties;
import com.gitee.swsk33.gitdocument.model.Result;
import com.gitee.swsk33.gitdocument.param.CommonValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.time.LocalDateTime;

@Controller
@RequestMapping("/api/config-get")
public class ConfigGetAPI {

	@Autowired
	private ConfigProperties configProperties;

	@GetMapping("/organization")
	@ResponseBody
	public Result<String> getOrg() {
		Result<String> result = new Result<>();
		result.setResultSuccess("获取成功！", configProperties.getOrganizationName());
		return result;
	}

	@GetMapping("/allow-public")
	@ResponseBody
	public Result<Boolean> allowPublic() {
		Result<Boolean> result = new Result<>();
		result.setResultSuccess("获取成功！", configProperties.isAllowPublic());
		return result;
	}

	@SaCheckLogin
	@GetMapping("/background")
	public String getBackground() {
		// 若自定义图片文件不存在，则根据季节返回背景图片
		if (new File(CommonValue.ResourcePath.CUSTOM_BACKGROUND).exists()) {
			return CommonValue.RequestPath.CUSTOM_BACKGROUND_REQUEST_PATH;
		}
		int month = LocalDateTime.now().getMonthValue();
		if (month >= 3 && month < 6) {
			return CommonValue.RequestPath.DEFAULT_BACKGROUND_REQUEST_PATH + "spring.jpg";
		}
		if (month >= 6 && month < 9) {
			return CommonValue.RequestPath.DEFAULT_BACKGROUND_REQUEST_PATH + "summer.jpg";
		}
		if (month >= 9 && month < 12) {
			return CommonValue.RequestPath.DEFAULT_BACKGROUND_REQUEST_PATH + "autumn.jpg";
		}
		return CommonValue.RequestPath.DEFAULT_BACKGROUND_REQUEST_PATH + "winter.jpg";
	}

	@GetMapping("/login-background")
	public String getLoginBackground() {
		// 若自定义图片不存在则返回默认
		if (new File(CommonValue.ResourcePath.CUSTOM_LOGIN_BACKGROUND).exists()) {
			return CommonValue.RequestPath.CUSTOM_LOGIN_BACKGROUND_PATH;
		}
		int time = LocalDateTime.now().getHour();
		if (time >= 7 && time < 18) {
			return CommonValue.RequestPath.DEFAULT_LOGIN_BACKGROUND_PATH + "daytime.jpg";
		}
		if (time >= 18 && time <= 20) {
			return CommonValue.RequestPath.DEFAULT_LOGIN_BACKGROUND_PATH + "evening.jpg";
		}
		return CommonValue.RequestPath.DEFAULT_LOGIN_BACKGROUND_PATH + "night.jpg";
	}

}