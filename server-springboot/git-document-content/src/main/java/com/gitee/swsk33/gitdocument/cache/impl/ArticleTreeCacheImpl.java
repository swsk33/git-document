package com.gitee.swsk33.gitdocument.cache.impl;

import com.gitee.swsk33.gitdocument.cache.ArticleTreeCache;
import com.gitee.swsk33.gitdocument.model.ArticleDirectory;
import jakarta.annotation.Resource;
import org.redisson.api.RMap;
import org.springframework.stereotype.Component;

@Component
public class ArticleTreeCacheImpl implements ArticleTreeCache {

	@Resource(name = "articleCacheTreeMap")
	private RMap<Long, ArticleDirectory> articleTreeMap;

	@Override
	public void setOrAdd(long id, ArticleDirectory directory) {
		articleTreeMap.put(id, directory);
	}

	@Override
	public void delete(long id) {
		articleTreeMap.remove(id);
	}

	@Override
	public ArticleDirectory getById(long id) {
		return articleTreeMap.get(id);
	}

}