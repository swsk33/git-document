package com.gitee.swsk33.gitdocument.strategy.context;

import com.gitee.swsk33.gitdocument.strategy.GitFileChangeStrategy;
import com.gitee.swsk33.gitdocument.strategy.impl.FileAddStrategy;
import com.gitee.swsk33.gitdocument.strategy.impl.FileDeleteStrategy;
import com.gitee.swsk33.gitdocument.strategy.impl.FileRenameStrategy;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.diff.DiffEntry;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * 文件改变策略上下文
 */
@Component
@Slf4j
public class FileChangeStrategyContext {

	/**
	 * 存放策略的容器
	 */
	private static final Map<DiffEntry.ChangeType, GitFileChangeStrategy> strategyMap = new HashMap<>();

	@Autowired
	private BeanFactory beanFactory;

	/**
	 * 初始化策略
	 */
	@PostConstruct
	public void initStrategy() {
		strategyMap.put(DiffEntry.ChangeType.ADD, beanFactory.getBean(FileAddStrategy.class));
		strategyMap.put(DiffEntry.ChangeType.DELETE, beanFactory.getBean(FileDeleteStrategy.class));
		strategyMap.put(DiffEntry.ChangeType.RENAME, beanFactory.getBean(FileRenameStrategy.class));
		log.info("所有文件策略初始化完成！");
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