package com.gitee.swsk33.gitdocument.config;

import com.gitee.swsk33.gitdocument.broker.GitMessageBroker;
import com.gitee.swsk33.gitdocument.model.GitCreateTaskMessage;
import com.gitee.swsk33.gitdocument.model.GitUpdateTaskMessage;
import com.gitee.swsk33.gitdocument.model.prototype.GitTaskMessage;
import com.gitee.swsk33.gitdocument.subscriber.GitCreateTaskSubscriber;
import com.gitee.swsk33.gitdocument.subscriber.GitUpdateTaskSubscriber;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 完成 Git 消息 Broker 订阅者配置
 */
@Slf4j
@Configuration
public class GitArticleBrokerConfig {

	@Bean
	public GitMessageBroker<GitTaskMessage> gitMessageBroker(GitCreateTaskSubscriber createSubscriber, GitUpdateTaskSubscriber updateSubscriber) {
		// 实例化 Git 仓库变化任务 Broker
		GitMessageBroker<GitTaskMessage> broker = new GitMessageBroker<>();
		// 订阅变化消息
		broker.subscribe(createSubscriber, GitCreateTaskMessage.class);
		broker.subscribe(updateSubscriber, GitUpdateTaskMessage.class);
		log.info("已完成 Git 文集仓库变化消息 Broker 初始化以及订阅任务");
		return broker;
	}

}