package com.gitee.swsk33.gitdocument.model;

import com.gitee.swsk33.gitdocument.dataobject.User;
import lombok.Data;

@Data
public class CommitInfo {

	/**
	 * 贡献者
	 */
	private User committer;

	/**
	 * 消息
	 */
	private String message;

	/**
	 * 贡献时间的时间戳
	 */
	private long timestamp;

}