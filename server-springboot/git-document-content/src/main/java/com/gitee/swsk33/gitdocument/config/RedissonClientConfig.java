package com.gitee.swsk33.gitdocument.config;

import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJackson3Codec;
import org.redisson.config.Config;
import org.springframework.boot.data.redis.autoconfigure.DataRedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Redisson客户端配置
 */
@Slf4j
@Configuration
public class RedissonClientConfig {

	/**
	 * 配置Redisson客户端
	 *
	 * @param redisProperties 用于获取Spring Boot Redis配置文件中配置
	 * @return 配置的客户端对象
	 */
	@Bean
	public RedissonClient redissonClient(DataRedisProperties redisProperties) {
		// 创建配置对象
		Config config = new Config();
		// 从Spring Boot配置读取并配置连接，这里以单节点为例
		config.setPassword(redisProperties.getPassword())
				.useSingleServer()
				.setAddress(String.format("redis://%s:%d", redisProperties.getHost(), redisProperties.getPort()))
				.setDatabase(redisProperties.getDatabase());
		// 配置序列化工具为Jackson3 JSON编码器
		config.setCodec(new JsonJackson3Codec());
		// 创建并返回客户端对象
		log.info("Redisson客户端已完成配置");
		return Redisson.create(config);
	}

}