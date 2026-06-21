package com.gitee.swsk33.gitdocument.broker;

import com.gitee.swsk33.gitdocument.model.prototype.GitTaskMessage;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

/**
 * Git 仓库任务消息 Broker，基于 Flux 封装，支持消息发布和订阅者添加
 *
 * @param <T> 消息类型
 */
@Slf4j
public class GitMessageBroker<T> {

	/**
	 * 数据发布源对象，为热发布源
	 */
	private final Flux<T> source;

	/**
	 * 数据发布 API 对象，用于操作数据发布
	 */
	private FluxSink<T> sink;

	/**
	 * 构造函数，将完成发布源对象初始化
	 */
	public GitMessageBroker() {
		// 初始化sink对象
		this.source = Flux.<T>create(emitter -> this.sink = emitter).share();
	}

	/**
	 * 发布一个数据
	 *
	 * @param value 发布的数据对象
	 */
	public void publish(T value) {
		if (this.sink == null) {
			log.error("还没有订阅者！发布器没有初始化！请先至少添加一个订阅者！");
			return;
		}
		sink.next(value);
	}

	/**
	 * 订阅当前数据源，添加一个支持自定义扩展的订阅者
	 *
	 * @param subscriber  订阅者
	 * @param messageType 订阅者订阅的消息类型
	 */
	public void subscribe(BaseSubscriber<T> subscriber, Class<? extends GitTaskMessage> messageType) {
		this.source.filter(message -> message.getClass().isAssignableFrom(messageType)).subscribe(subscriber);
	}

}