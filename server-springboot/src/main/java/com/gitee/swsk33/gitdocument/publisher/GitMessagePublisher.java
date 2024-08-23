package com.gitee.swsk33.gitdocument.publisher;

import com.gitee.swsk33.gitdocument.model.prototype.GitTaskMessage;
import lombok.Data;
import reactor.core.publisher.FluxSink;

/**
 * 用于发布Git仓库创建、更新消息的发布者对象，配合Flux使用
 */
@Data
public class GitMessagePublisher {

	/**
	 * 用于发布操作的sink对象，从Flux的发布方法传入
	 */
	private FluxSink<GitTaskMessage> sink;

	/**
	 * 发布一个Git任务对象
	 *
	 * @param message Git任务对象，可能是仓库创建或更新
	 */
	public void publishMessage(GitTaskMessage message) {
		sink.next(message);
	}

}