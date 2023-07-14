package com.gitee.swsk33.gitdocument.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
@ConfigurationProperties(prefix = "com.gitee.swsk33.git-doc")
@Data
public class ConfigProperties {

	/**
	 * 宿主机ssh端口，当部署在容器中时，请配置为非22端口防止冲突
	 */
	private int hostPort = 22;

	/**
	 * 存放所有文集仓库的目录，尽量不要修改
	 */
	private String repoPath = System.getProperty("user.home") + File.separator + "git-doc-repos";

}