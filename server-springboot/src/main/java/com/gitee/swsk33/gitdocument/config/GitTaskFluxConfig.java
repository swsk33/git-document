package com.gitee.swsk33.gitdocument.config;

import com.gitee.swsk33.gitdocument.listener.GitCreateTaskListener;
import com.gitee.swsk33.gitdocument.listener.GitUpdateTaskListener;
import com.gitee.swsk33.gitdocument.model.prototype.GitTaskMessage;
import com.gitee.swsk33.gitdocument.publisher.GitMessagePublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;

/**
 * Git任务消息发布者和订阅者的Reactor配置类
 */
@Slf4j
@Configuration
public class GitTaskFluxConfig {

	@Autowired
	private BeanFactory beanFactory;

	/**
	 * 用于发布仓库创建消息的发布者
	 */
	@Bean
	public GitMessagePublisher gitCreateTaskPublisher() {
		return new GitMessagePublisher();
	}

	/**
	 * 用于发布创建消息的Flux对象
	 */
	@Bean
	public Flux<GitTaskMessage> gitCreateTaskFlux(GitMessagePublisher gitCreateTaskPublisher) {
		// 创建Flux
		Flux<GitTaskMessage> flux = Flux.create(gitCreateTaskPublisher::setSink);
		// 订阅
		flux.subscribe(beanFactory.getBean(GitCreateTaskListener.class));
		log.info("Git仓库创建任务消息发布者已准备完毕！");
		return flux;
	}

	/**
	 * 用于发布仓库更新消息的发布者
	 */
	@Bean
	public GitMessagePublisher gitUpdateTaskPublisher() {
		return new GitMessagePublisher();
	}

	@Bean
	public Flux<GitTaskMessage> gitUpdateTaskFlux(GitMessagePublisher gitUpdateTaskPublisher) {
		// 创建Flux
		Flux<GitTaskMessage> flux = Flux.create(gitUpdateTaskPublisher::setSink);
		// 订阅
		flux.subscribe(beanFactory.getBean(GitUpdateTaskListener.class));
		log.info("Git仓库更新任务消息发布者已准备完毕！");
		return flux;
	}

}