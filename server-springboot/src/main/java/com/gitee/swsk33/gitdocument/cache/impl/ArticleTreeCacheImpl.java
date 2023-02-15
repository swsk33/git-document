package com.gitee.swsk33.gitdocument.cache.impl;

import com.gitee.swsk33.gitdocument.cache.ArticleTreeCache;
import com.gitee.swsk33.gitdocument.model.ArticleDirectory;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ArticleTreeCacheImpl implements ArticleTreeCache {

	@Resource
	private RedisTemplate<String, Map<Long, ArticleDirectory>> redisTemplate;

	@Override
	public void setOrAdd(long id, ArticleDirectory directory) {
		redisTemplate.opsForHash().put(ArticleTreeCache.ANTHOLOGY_ID_ARTICLE_TREE_MAP, id, directory);
	}

	@Override
	public void delete(long id) {
		redisTemplate.opsForHash().delete(ArticleTreeCache.ANTHOLOGY_ID_ARTICLE_TREE_MAP, id);
	}

	@Override
	public ArticleDirectory getById(long id) {
		return (ArticleDirectory) redisTemplate.opsForHash().get(ArticleTreeCache.ANTHOLOGY_ID_ARTICLE_TREE_MAP, id);
	}

}