package com.gitee.swsk33.gitdocument.message;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.eclipse.jgit.diff.DiffEntry;

import java.util.List;

/**
 * 用于发送更新内容的通知邮件的任务消息
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UpdateEmailMessage extends EmailMessage {

	/**
	 * 更新的文集名
	 */
	private String name;

	/**
	 * commit信息
	 */
	private String commitMessage;

	/**
	 * 变更列表
	 */
	private List<DiffEntry> diffEntries;

}