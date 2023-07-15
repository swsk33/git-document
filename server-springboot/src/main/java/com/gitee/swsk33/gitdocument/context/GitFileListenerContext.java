package com.gitee.swsk33.gitdocument.context;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.watch.WatchMonitor;
import cn.hutool.core.io.watch.watchers.DelayWatcher;
import com.gitee.swsk33.gitdocument.listener.GitRepositoryListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static cn.hutool.core.io.watch.WatchMonitor.ENTRY_MODIFY;

/**
 * 文件监听器上下文，管理所有的监听器
 */
@Slf4j
@Component
public class GitFileListenerContext {

	@Autowired
	private BeanFactory beanFactory;

	/**
	 * 存放所有的文件监听器
	 */
	private static final Map<Long, WatchMonitor> MONITOR_MAP = new HashMap<>();

	/**
	 * 添加要监视的Git仓库文件夹
	 *
	 * @param id            设定观察者id（仓库id）
	 * @param gitRepository 设定要监视的Git仓库文件夹（绝对路径）
	 */
	public void addMonitor(long id, String gitRepository) {
		// 新建文件观察者
		GitRepositoryListener watcher = beanFactory.getBean(GitRepositoryListener.class);
		watcher.setId(id);
		watcher.setGitRepository(gitRepository);
		// 创建监视器
		WatchMonitor monitor = WatchMonitor.create(FileUtil.file(gitRepository + "/refs/heads"), ENTRY_MODIFY);
		// 使用延时观察者防止记录多次改变
		monitor.setWatcher(new DelayWatcher(watcher, 1000));
		monitor.start();
		MONITOR_MAP.put(id, monitor);
		log.info("加入监听文集仓库路径：" + gitRepository);
	}

	/**
	 * 移除文件监视者
	 *
	 * @param id 待移除的仓库id
	 */
	public void removeMonitor(long id) {
		MONITOR_MAP.remove(id).close();
		log.info("移除监听文集仓库id：" + id);
	}

}