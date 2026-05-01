package com.gitee.swsk33.gitdocument.config;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;

/**
 * Redis 序列化配置
 */
@Configuration
public class RedisSerdeConfig {

	@Bean
	public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
		// 创建 ObjectMapper 定义序列化行为
		ObjectMapper mapper = JsonMapper.builder()
				// 忽略 null 字段
				.changeDefaultPropertyInclusion(value ->
						value.withValueInclusion(JsonInclude.Include.NON_NULL)
								.withContentInclusion(JsonInclude.Include.NON_NULL)
				)
				// 基于字段序列化，不依赖 getter/setter
				.changeDefaultVisibility(visibility ->
						visibility.withVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE)
								.withVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY)
				)
				.build();
		// 创建 Redis JSON 序列化对象
		GenericJacksonJsonRedisSerializer jsonSerializer = new GenericJacksonJsonRedisSerializer(mapper);
		// 构建 RedisTemplate 对象
		RedisTemplate<String, Object> template = new RedisTemplate<>();
		template.setConnectionFactory(redisConnectionFactory);
		// key 一律用字符串
		template.setKeySerializer(StringRedisSerializer.UTF_8);
		template.setHashKeySerializer(StringRedisSerializer.UTF_8);
		// value 用 JSON
		template.setValueSerializer(jsonSerializer);
		template.setHashValueSerializer(jsonSerializer);
		// 最后初始化
		template.afterPropertiesSet();
		return template;
	}

}