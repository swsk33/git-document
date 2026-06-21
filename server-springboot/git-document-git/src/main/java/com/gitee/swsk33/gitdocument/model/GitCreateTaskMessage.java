package com.gitee.swsk33.gitdocument.model;

import com.gitee.swsk33.gitdocument.model.prototype.GitTaskMessage;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * Git 仓库创建任务消息
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class GitCreateTaskMessage extends GitTaskMessage {

	/**
	 * 新建的文件列表
	 */
	private List<String> fileList;

}