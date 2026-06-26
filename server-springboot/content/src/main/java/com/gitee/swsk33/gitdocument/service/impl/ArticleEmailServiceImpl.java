package com.gitee.swsk33.gitdocument.service.impl;

import cn.hutool.core.util.ArrayUtil;
import com.gitee.swsk33.gitdocument.model.ArticleDifference;
import com.gitee.swsk33.gitdocument.model.CreateArticleNotifyEmailMessage;
import com.gitee.swsk33.gitdocument.model.UpdateArticleNotifyEmailMessage;
import com.gitee.swsk33.gitdocument.service.ArticleEmailService;
import io.github.swsk33.codepostcore.service.EmailNotifyService;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.diff.DiffEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class ArticleEmailServiceImpl implements ArticleEmailService {

	@Autowired
	private EmailNotifyService notifyService;

	@Override
	public void sendAnthologyUpdateNotify(UpdateArticleNotifyEmailMessage message) {
		// 获取差异信息
		List<String> diffsMessage = message.getDiffEntries().stream()
				// 过滤掉非md文件的变动
				.filter(diff -> diff.getNewPath().endsWith(".md") || (diff.getChangeType() == DiffEntry.ChangeType.DELETE && diff.getOldPath().endsWith(".md")))
				.map(ArticleDifference::toString).toList();
		// 若没有要通知的差异，则不发送邮件
		if (diffsMessage.isEmpty()) {
			log.warn("没有需要通知的差异信息！");
			return;
		}
		// 设定模板变量
		Map<String, Object> models = new HashMap<>();
		models.put("anthology", message.getName());
		models.put("commitMessage", message.getCommitMessage());
		models.put("diffs", diffsMessage);
		// 群发通知邮件
		notifyService.sendTemplateNotifyAsync(message.getTitle(), "anthology-update.txt", models, ArrayUtil.toArray(message.getEmailList(), String.class));
	}

	@Override
	public void sendAnthologyCreateNotify(CreateArticleNotifyEmailMessage message) {
		// 设定模板变量
		Map<String, Object> models = new HashMap<>();
		models.put("publisher", message.getPublisher());
		models.put("anthologyName", message.getName());
		// 群发通知邮件
		notifyService.sendTemplateNotifyAsync(message.getTitle(), "anthology-create.txt", models, ArrayUtil.toArray(message.getEmailList(), String.class));
	}

}