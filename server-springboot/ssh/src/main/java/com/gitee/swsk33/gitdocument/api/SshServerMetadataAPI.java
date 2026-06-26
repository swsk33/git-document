package com.gitee.swsk33.gitdocument.api;

import com.gitee.swsk33.gitdocument.model.Result;
import com.gitee.swsk33.gitdocument.property.SshServerProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ssh-server")
public class SshServerMetadataAPI {

	/**
	 * 运行程序的用户名
	 */
	private static final String RUN_USER_NAME = System.getProperty("user.name");

	@Autowired
	private SshServerProperties sshServerProperties;

	/**
	 * 获取运行应用的用户名
	 */
	@GetMapping("/user")
	public Result<String> getSystemUser() {
		return Result.resultSuccess("获取完成！", RUN_USER_NAME);
	}

	/**
	 * 获取广播的SSH端口
	 */
	@GetMapping("/port")
	public Result<Integer> getSSHPort() {
		return Result.resultSuccess("获取完成！", sshServerProperties.getAdvertisedSshServerPort());
	}

}