package com.gitee.swsk33.gitdocument.strategy.impl;


import com.gitee.swsk33.gitdocument.strategy.GitFileChangeStrategy;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.diff.DiffEntry;

/**
 * 文件重命名策略
 */
@Slf4j
public class FileRenameStrategy implements GitFileChangeStrategy {

	@Override
	public void doUpdate(long repositoryId, DiffEntry diffEntry) {
		// 修改数据库中对应的记录
		log.info("文件重命名：" + diffEntry.getOldPath() + " -> " + diffEntry.getNewPath());
	}

}