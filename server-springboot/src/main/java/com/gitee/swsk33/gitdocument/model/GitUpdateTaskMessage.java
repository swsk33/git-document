package com.gitee.swsk33.gitdocument.model;

import com.gitee.swsk33.gitdocument.model.prototype.GitTaskMessage;
import lombok.Data;

import java.util.List;

/**
 * Git仓库更新任务消息
 */
@Data
public class GitUpdateTaskMessage extends GitTaskMessage {

	/**
	 * 更新后的差异列表
	 */
	private List<ArticleDifference> diffs;

}