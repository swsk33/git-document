package com.gitee.swsk33.gitdocument.context;

import com.gitee.swsk33.gitdocument.listener.GitRepositoryListener;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * 文件监听器上下文，管理所有的监听器
 */
@Slf4j
@Component
public class GitFileListenerContext {

	@Autowired
	private BeanFactory beanFactory;

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
	public void addObserver(long id, String dirPath) {
		// 新建文件观察者
		FileAlterationObserver observer = new FileAlterationObserver(new File(dirPath + "/refs/heads"));
		GitRepositoryListener listener = beanFactory.getBean(GitRepositoryListener.class);
		listener.setId(id);
		listener.setPath(dirPath);
		observer.addListener(listener);
		// 设定监视器
		monitor.addObserver(observer);
		observers.put(id, observer);
		log.info("加入监听文集仓库路径：" + dirPath);
	}

	/**
	 * 移除文件监视者
	 *
	 * @param id 待移除的仓库id
	 */
	public void removeObserver(long id) {
		monitor.removeObserver(observers.get(id));
		observers.remove(id);
		log.info("移除监听文集仓库id：" + id);
	}

	/**
	 * 启动全部监视
	 */
	public void start() throws Exception {
		monitor.start();
		log.info("已开启所有仓库监视！");
	}

	/**
	 * 停止全部监视
	 */
	public void stop() throws Exception {
		monitor.stop();
		log.info("已停止所有仓库监视！");
	}

}