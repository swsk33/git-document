package com.gitee.swsk33.gitdocument.publisher;

import com.gitee.swsk33.gitdocument.model.prototype.GitTaskMessage;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.FluxSink;

/**
 * 用于发布Git仓库创建、更新消息的发布者对象，配合Flux使用
 */
@Data
@Slf4j
public class GitMessagePublisher {

	/**
	 * 发布者名称
	 */
	private String name;

	/**
	 * 用于发布操作的sink对象，从Flux的发布方法传入
	 */
	private FluxSink<GitTaskMessage> sink;

	/**
	 * 构造函数
	 *
	 * @param name 初始化发布者名称
	 */
	public GitMessagePublisher(String name) {
		this.name = name;
	}

	/**
	 * 发布一个Git任务对象
	 *
	 * @param message Git任务对象，可能是仓库创建或更新
	 */
	public void publishMessage(GitTaskMessage message) {
		sink.next(message);
	}

	/**
	 * 停止发布，终止订阅者订阅
	 */
	public void stopPublish() {
		log.info("{}正在向下游发出结束订阅信号...", name);
		sink.complete();
	}

}