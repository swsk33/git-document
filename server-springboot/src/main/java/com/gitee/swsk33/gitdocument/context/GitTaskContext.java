package com.gitee.swsk33.gitdocument.context;

import lombok.extern.slf4j.Slf4j;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Git仓库文件更新任务上下文（单例）
 */
@Slf4j
public class GitTaskContext extends Thread {

	private static GitTaskContext instance = new GitTaskContext();

	/**
	 * 存放任务的队列
	 */
	public static Queue<Runnable> taskQueue = new LinkedList<>();

	/**
	 * 当前任务
	 */
	private static Thread currentTask;

	private GitTaskContext() {

	}

	/**
	 * 获取任务上下文单实例
	 *
	 * @return 任务上下文实例
	 */
	public static GitTaskContext getInstance() {
		return instance;
	}

	@Override
	public void run() {
		log.info("启动Git文件数据库刷新任务队列！");
		while (true) {
			try {
				// 延时2s再来进行检查
				Thread.sleep(2000);
				// 空闲时或者有任务正在执行时，不进行任何操作
				if (taskQueue.isEmpty() || (currentTask != null && currentTask.isAlive())) {
					log.info("当前不需要编排任务！");
					continue;
				}
				// 否则，从任务队列取出下一个任务并执行
				log.info("从任务队列取出一个任务！");
				currentTask = new Thread(taskQueue.poll());
				currentTask.start();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}