package com.gitee.swsk33.gitdocument.model;

import com.gitee.swsk33.gitdocument.dataobject.User;
import lombok.Data;

import java.io.Serializable;

/**
 * 仓库的提交记录
 */
@Data
public class CommitRecord implements Serializable {

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