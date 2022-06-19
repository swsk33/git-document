package com.gitee.swsk33.gitdocument.strategy;

import com.gitee.swsk33.gitdocument.strategy.impl.FileAddStrategy;
import com.gitee.swsk33.gitdocument.strategy.impl.FileDeleteStrategy;
import com.gitee.swsk33.gitdocument.strategy.impl.FileRenameStrategy;
import org.eclipse.jgit.diff.DiffEntry;

import java.util.HashMap;
import java.util.Map;

/**
 * 文件改变策略上下文
 */
public class FileChangeStrategyContext {

	/**
	 * 存放策略的容器
	 */
	private static final Map<DiffEntry.ChangeType, GitFileChangeStrategy> strategyMap = new HashMap<>();

	/*
	  初始化所有
	 */
	static {
		strategyMap.put(DiffEntry.ChangeType.ADD, new FileAddStrategy());
		strategyMap.put(DiffEntry.ChangeType.DELETE, new FileDeleteStrategy());
		strategyMap.put(DiffEntry.ChangeType.RENAME, new FileRenameStrategy());
	}

	/**
	 * 传入差异对象并执行对应操作
	 *
	 * @param repositoryId 仓库id
	 * @param entry        差异对象
	 */
	public static void executeStrategy(long repositoryId, DiffEntry entry) {
		strategyMap.get(entry.getChangeType()).doUpdate(repositoryId, entry);
	}

}