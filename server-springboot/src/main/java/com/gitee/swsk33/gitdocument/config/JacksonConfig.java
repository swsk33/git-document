package com.gitee.swsk33.gitdocument.config;

import org.springframework.boot.jackson.autoconfigure.JsonMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tools.jackson.databind.module.SimpleModule;
import tools.jackson.databind.ser.std.ToStringSerializer;

/**
 * 配置全局 Jackson 序列化行为
 */
@Configuration
public class JacksonConfig {

	@Bean
	JsonMapperBuilderCustomizer longToStringCustomizer() {
		return builder -> {
			// 配置将 Long 序列化为 String
			SimpleModule module = new SimpleModule();
			module.addSerializer(Long.class, ToStringSerializer.instance);
			module.addSerializer(Long.TYPE, ToStringSerializer.instance);
			builder.addModule(module);
		};
	}

}