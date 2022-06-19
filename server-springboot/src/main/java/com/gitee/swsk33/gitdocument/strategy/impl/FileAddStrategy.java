package com.gitee.swsk33.gitdocument.strategy.impl;

import com.gitee.swsk33.gitdocument.strategy.GitFileChangeStrategy;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.diff.DiffEntry;

/**
 * 文件增加策略
 */
@Slf4j
public class FileAddStrategy implements GitFileChangeStrategy {

	@Override
	public void doUpdate(long repositoryId, DiffEntry diffEntry) {
		// 把文件信息录入数据库
		log.info("增加文件：" + diffEntry.getNewPath());
	}

}