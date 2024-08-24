package com.gitee.swsk33.gitdocument.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 关于Git仓库相关的配置
 */
@Data
@Component
@ConfigurationProperties(prefix = "com.gitee.swsk33.git-doc.git")
public class GitRepositoryProperties {

	/**
	 * 存放所有文集仓库的根目录
	 */
	private String repositoryPath;

}