package com.gitee.swsk33.gitdocument.context;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Git仓库文件更新任务上下文（单例）
 */
@Slf4j
@Component
public class GitTaskContext {

	/**
	 * 存放任务的队列
	 */
	public static Queue<Runnable> taskQueue = new LinkedList<>();

	/**
	 * 当前任务
	 */
	private static Thread currentTask;

	@Scheduled(fixedRate = 2000)
	public void taskSchedule() {
		// 空闲时或者有任务正在执行时，不进行任何操作
		if (taskQueue.isEmpty() || (currentTask != null && currentTask.isAlive())) {
			return;
		}
		// 否则，从任务队列取出下一个任务并执行
		log.info("从任务队列取出一个任务！");
		currentTask = new Thread(taskQueue.poll());
		currentTask.setName("gitTask");
		currentTask.start();
	}

}