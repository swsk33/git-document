package com.gitee.swsk33.gitdocument.api;

import com.gitee.swsk33.gitdocument.model.Result;
import com.gitee.swsk33.gitdocument.param.CommonValue;
import com.gitee.swsk33.gitdocument.property.ConfigProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/system-info")
public class SystemInfoAPI {

	@Autowired
	private ConfigProperties configProperties;

	/**
	 * 获取运行应用的用户名
	 */
	@GetMapping("/system-user")
	public Result<String> getSystemUser() {
		return Result.resultSuccess("获取完成！", CommonValue.RUN_USER_NAME);
	}

	/**
	 * 获取宿主机的SSH端口
	 */
	@GetMapping("/ssh-port")
	public Result<Integer> getSSHPort() {
		return Result.resultSuccess("获取完成！", configProperties.getHostPort());
	}

}