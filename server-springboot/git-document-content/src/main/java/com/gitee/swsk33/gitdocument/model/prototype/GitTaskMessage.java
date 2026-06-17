package com.gitee.swsk33.gitdocument.model.prototype;

import lombok.Data;

import java.io.Serializable;

/**
 * 用于Git仓库任务的消息抽象类
 */
@Data
public abstract class GitTaskMessage implements Serializable {

	/**
	 * 仓库id
	 */
	private long repositoryId;

	/**
	 * 发生更改后的commitId
	 */
	private String commitId;

}