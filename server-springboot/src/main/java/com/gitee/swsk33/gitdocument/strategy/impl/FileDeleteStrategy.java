package com.gitee.swsk33.gitdocument.strategy.impl;

import com.gitee.swsk33.gitdocument.strategy.GitFileChangeStrategy;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.diff.DiffEntry;

/**
 * 文件删除策略
 */
@Slf4j
public class FileDeleteStrategy implements GitFileChangeStrategy {

	@Override
	public void doUpdate(long repositoryId, DiffEntry diffEntry) {
		// 从数据库删除
		log.info("删除文件：" + diffEntry.getOldPath());
	}

}