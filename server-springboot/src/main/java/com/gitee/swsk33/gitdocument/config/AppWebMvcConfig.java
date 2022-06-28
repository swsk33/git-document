package com.gitee.swsk33.gitdocument.config;

import com.gitee.swsk33.gitdocument.param.CommonValue;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AppWebMvcConfig implements WebMvcConfigurer {

	/**
	 * 重写资源路径配置
	 */
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		// 默认包内静态资源
		registry.addResourceHandler(CommonValue.RequestPath.INNER_STATIC + "**").addResourceLocations("classpath:/static/");
		// 外部静态资源
		registry.addResourceHandler(CommonValue.RequestPath.EXTERNAL_REQUEST_PATH + "/**").addResourceLocations("file:external-resource/");
	}

}