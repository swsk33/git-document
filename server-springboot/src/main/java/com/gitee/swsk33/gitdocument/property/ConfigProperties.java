package com.gitee.swsk33.gitdocument.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.File;

@Data
@Component
@ConfigurationProperties(prefix = "com.gitee.swsk33.git-doc")
public class ConfigProperties {

	/**
	 * 内嵌ssh服务端端口，默认情况下请配置为非22端口防止冲突
	 */
	private int sshServerPort;

	/**
	 * 存放所有文集仓库的目录，尽量不要修改
	 */
	private String repoPath = System.getProperty("user.home") + File.separator + "git-doc-repos";

}