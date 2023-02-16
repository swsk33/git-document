package com.gitee.swsk33.gitdocument.strategy;

import com.gitee.swsk33.gitdocument.model.ArticleDiff;
import org.springframework.stereotype.Service;

/**
 * Git文件改变策略
 */
@Service
public interface GitFileChangeStrategy {

	/**
	 * 执行对应的数据库更新操作
	 *
	 * @param repositoryId 仓库id
	 * @param diff         差异信息
	 */
	void doUpdate(long repositoryId, ArticleDiff diff);

}