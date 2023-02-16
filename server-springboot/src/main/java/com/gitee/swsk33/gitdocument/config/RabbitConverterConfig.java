package com.gitee.swsk33.gitdocument.config;

import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMQ的消息转换器配置
 */
@Configuration
public class RabbitConverterConfig {

	/**
	 * 配置JSON序列化转换器
	 */
	@Bean
	public MessageConverter JSONConverter() {
		return new Jackson2JsonMessageConverter();
	}

}