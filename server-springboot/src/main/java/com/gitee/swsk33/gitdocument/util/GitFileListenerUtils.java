package com.gitee.swsk33.gitdocument.util;

import com.gitee.swsk33.gitdocument.listener.GitRepositoryListener;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * 文件监听器实用类
 */
@Slf4j
public class GitFileListenerUtils {

	/**
	 * 存放所有的文件观察者
	 */
	private static final Map<Long, FileAlterationObserver> observers = new HashMap<>();

	/**
	 * 文件监视器
	 */
	private static final FileAlterationMonitor monitor = new FileAlterationMonitor(1000);

	/**
	 * 添加要监视的Git仓库文件夹
	 *
	 * @param id      设定观察者id（仓库id）
	 * @param dirPath 设定要监视的Git仓库文件夹
	 */
	public static void addObserver(long id, String dirPath) {
		// 新建文件观察者
		FileAlterationObserver observer = new FileAlterationObserver(new File(dirPath + "/refs/heads"));
		observer.addListener(new GitRepositoryListener(id, dirPath));
		log.info("加入监听路径：" + dirPath);
		// 设定监视器
		monitor.addObserver(observer);
		observers.put(id, observer);
	}

	/**
	 * 移除文件监视者
	 *
	 * @param id 待移除的仓库id
	 */
	public static void removeObserver(long id) {
		monitor.removeObserver(observers.get(id));
		observers.remove(id);
	}

	/**
	 * 启动全部监视
	 */
	public static void start() throws Exception {
		monitor.start();
	}

	/**
	 * 停止全部监视
	 */
	public static void stop() throws Exception {
		monitor.stop();
	}

}