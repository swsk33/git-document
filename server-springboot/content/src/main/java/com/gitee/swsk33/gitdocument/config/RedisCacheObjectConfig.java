package com.gitee.swsk33.gitdocument.config;

import com.gitee.swsk33.gitdocument.model.ArticleDirectory;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.gitee.swsk33.gitdocument.param.RedisArticleKey.ANTHOLOGY_ID_ARTICLE_TREE_MAP;

/**
 * 用于获取Redis缓存对象并注册为Bean的自动配置
 */
@Configuration
public class RedisCacheObjectConfig {

	/**
	 * 文集文章缓存目录树列表
	 */
	@Bean("articleCacheTreeMap")
	public RMap<Long, ArticleDirectory> articleTreeMap(RedissonClient redissonClient) {
		return redissonClient.getMap(ANTHOLOGY_ID_ARTICLE_TREE_MAP);
	}

}