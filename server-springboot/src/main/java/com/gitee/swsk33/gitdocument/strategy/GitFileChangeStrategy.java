package com.gitee.swsk33.gitdocument.strategy;

import org.eclipse.jgit.diff.DiffEntry;

/**
 * Git文件改变策略
 */
public interface GitFileChangeStrategy {

	/**
	 * 执行对应的数据库更新操作
	 *
	 * @param repositoryId 仓库id
	 * @param diffEntry    差异信息
	 */
	void doUpdate(long repositoryId, DiffEntry diffEntry);

}