package com.gitee.swsk33.gitdocument.service.impl;

import cn.hutool.core.util.ArrayUtil;
import com.gitee.swsk33.gitdocument.dao.UserDAO;
import com.gitee.swsk33.gitdocument.dataobject.User;
import com.gitee.swsk33.gitdocument.message.CreateEmailMessage;
import com.gitee.swsk33.gitdocument.message.UpdateEmailMessage;
import com.gitee.swsk33.gitdocument.model.ArticleDiff;
import com.gitee.swsk33.gitdocument.service.EmailService;
import io.github.swsk33.codepostcore.service.EmailNotifyService;
import io.github.swsk33.codepostcore.service.EmailVerifyCodeService;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.diff.DiffEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class EmailServiceImpl implements EmailService {

	@Autowired
	private EmailVerifyCodeService verifyCodeService;

	@Autowired
	private EmailNotifyService notifyService;

	@Autowired
	private UserDAO userDAO;

	@Async
	@Override
	public void sendRoleChangeEmail(User changedUser, User operator) {
		// 设定模板变量
		Map<String, Object> models = new HashMap<>();
		models.put("operator", operator.getNickname());
		models.put("newRole", changedUser.getRole().getShowName());
		notifyService.sendTemplateNotify("GitDocument - 用户权限变化", "role-changed.txt", models, changedUser.getEmail());
	}

	@Async
	@Override
	public void sendAnthologyUpdateNotify(UpdateEmailMessage message) {
		// 获取差异信息
		List<String> diffsMessage = message.getDiffEntries().stream()
				// 过滤掉非md文件的变动
				.filter(diff -> diff.getChangeType() == DiffEntry.ChangeType.DELETE || diff.getNewPath().endsWith(".md"))
				.map(ArticleDiff::toString).toList();
		// 设定模板变量
		Map<String, Object> models = new HashMap<>();
		models.put("anthology", message.getName());
		models.put("commitMessage", message.getCommitMessage());
		models.put("diffs", diffsMessage);
		// 群发通知邮件
		notifyService.sendTemplateNotify(message.getTitle(), "anthology-update.txt", models, ArrayUtil.toArray(message.getEmailList(), String.class));
	}

	@Async
	@Override
	public void sendAnthologyCreateNotify(CreateEmailMessage message) {
		// 设定模板变量
		Map<String, Object> models = new HashMap<>();
		models.put("publisher", message.getPublisher());
		models.put("anthologyName", message.getName());
		// 群发通知邮件
		notifyService.sendTemplateNotify(message.getTitle(), "anthology-create.txt", models, ArrayUtil.toArray(message.getEmailList(), String.class));
	}

	@Async
	@Override
	public void sendPasswordResetCode(int userId) {
		User resetUser = userDAO.getById(userId);
		if (resetUser == null) {
			return;
		}
		verifyCodeService.sendCode(resetUser.getId(), resetUser.getEmail(), 5, TimeUnit.MINUTES);
		log.info("已向" + resetUser.getEmail() + "发送密码重置验证码！");
	}

	@Override
	public boolean verifyPasswordResetCode(int userId, String code) {
		return verifyCodeService.verifyCode(userId, code);
	}

}