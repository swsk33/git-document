package com.gitee.swsk33.gitdocument.model.prototype;

import lombok.Data;

/**
 * 用于Git仓库任务的消息抽象类
 */
@Data
public abstract class GitTaskMessage {

	/**
	 * Git 仓库绝对路径
	 */
	private String repoPath;

	/**
	 * 发生更改后的commitId
	 */
	private String commitId;

}