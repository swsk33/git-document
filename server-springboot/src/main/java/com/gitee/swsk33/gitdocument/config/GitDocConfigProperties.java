package com.gitee.swsk33.gitdocument.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "com.gitee.swsk33.git-doc")
@Setter
@Getter
public class GitDocConfigProperties {

	/**
	 * 组织名
	 */
	private String organizationName = "git-doc-org";

	/**
	 * 允许访客注册
	 */
	private boolean allowPublic = true;

}