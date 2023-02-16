package com.gitee.swsk33.gitdocument.message;

import com.gitee.swsk33.gitdocument.message.prototype.GitTaskMessage;
import lombok.Data;

import java.util.List;

/**
 * Git仓库创建任务消息
 */
@Data
public class GitCreateTaskMessage extends GitTaskMessage {

	/**
	 * 新建的文件列表
	 */
	private List<String> fileList;

}