package com.gitee.swsk33.gitdocument.message;

import com.gitee.swsk33.gitdocument.message.prototype.GitTaskMessage;
import com.gitee.swsk33.gitdocument.model.ArticleDiff;
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
	private List<ArticleDiff> diffs;

}