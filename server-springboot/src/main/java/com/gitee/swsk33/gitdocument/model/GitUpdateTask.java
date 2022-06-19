package com.gitee.swsk33.gitdocument.model;

import com.gitee.swsk33.gitdocument.strategy.FileChangeStrategyContext;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.diff.DiffEntry;

import java.util.List;

/**
 * HEAD被修改时触发的更新任务
 */
@Getter
@Setter
@AllArgsConstructor
@Slf4j
public class GitUpdateTask implements Runnable {

	/**
	 * 仓库id
	 */
	private long repositoryId;

	/**
	 * 发生更改后的commitId
	 */
	private String commitId;

	/**
	 * 发生更改的文件列表
	 */
	private List<DiffEntry> diffEntries;

	@Override
	public void run() {
		for (DiffEntry entry : diffEntries) {
			log.info("正在修改数据库文件信息...");
			FileChangeStrategyContext.executeStrategy(repositoryId, entry);
		}
	}

}