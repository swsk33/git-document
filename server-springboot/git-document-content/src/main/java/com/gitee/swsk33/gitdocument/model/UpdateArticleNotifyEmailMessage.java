package com.gitee.swsk33.gitdocument.model;

import com.gitee.swsk33.gitdocument.model.prototype.ArticleNotifyEmailMessage;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 用于发送更新内容的通知邮件的任务消息
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UpdateArticleNotifyEmailMessage extends ArticleNotifyEmailMessage {

	/**
	 * 更新的文集显示名
	 */
	private String name;

	/**
	 * commit 信息
	 */
	private String commitMessage;

	/**
	 * 变更列表
	 */
	private List<ArticleDifference> diffEntries;

}