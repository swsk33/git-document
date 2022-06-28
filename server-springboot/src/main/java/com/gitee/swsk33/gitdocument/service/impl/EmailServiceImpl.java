package com.gitee.swsk33.gitdocument.service.impl;

import com.gitee.swsk33.gitdocument.config.ConfigProperties;
import com.gitee.swsk33.gitdocument.dao.UserDAO;
import com.gitee.swsk33.gitdocument.dataobject.User;
import com.gitee.swsk33.gitdocument.model.ErrorReport;
import com.gitee.swsk33.gitdocument.service.EmailService;
import com.gitee.swsk33.readandwrite.param.NewLineCharacter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class EmailServiceImpl implements EmailService {

	@Value("${spring.mail.username}")
	private String sender;

	@Autowired
	private ConfigProperties configProperties;

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private UserDAO userDAO;

	/**
	 * 发送文本通知邮件
	 *
	 * @param email 目的邮件地址
	 * @param title 邮件标题
	 * @param text  邮件内容
	 */
	@Async
	public void sendNotifyMail(String email, String title, String text) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom(sender);
		message.setTo(email);
		message.setSubject(title);
		message.setText(text);
		try {
			mailSender.send(message);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void sendReportToAdmin(ErrorReport errorReport) {
		StringBuilder content = new StringBuilder();
		// 内容头
		content.append("收到错误报告！").append(NewLineCharacter.defaultNewLineChar);
		content.append("- 报告名：").append(errorReport.getName()).append(NewLineCharacter.defaultNewLineChar);
		content.append("- 报告正文：").append(errorReport.getContent()).append(NewLineCharacter.defaultNewLineChar).append(NewLineCharacter.defaultNewLineChar);
		// 文章信息
		content.append("错误涉及文章信息：").append(NewLineCharacter.defaultNewLineChar);
		content.append("- 文章名：").append(errorReport.getArticle().getAnthology().getName()).append(" - ").append(errorReport.getArticle().getFilePath());
		content.append(NewLineCharacter.defaultNewLineChar).append(NewLineCharacter.defaultNewLineChar);
		// 报告人信息
		content.append("来自用户：").append(NewLineCharacter.defaultNewLineChar);
		content.append("- 用户昵称：").append(errorReport.getUser().getNickname()).append(NewLineCharacter.defaultNewLineChar);
		content.append("- 用户邮箱：").append(errorReport.getUser().getEmail());
		List<User> admins = userDAO.getAllAdmin();
		for (User user : admins) {
			sendNotifyMail(user.getEmail(), "GitDocument · " + configProperties.getOrganizationName() + " - 错误报告", content.toString());
			log.info("已向管理员：" + user.getNickname() + " 发送了错误报告通知邮件！");
		}
	}

	@Override
	public void sendPasswordResetEmail(String email, String password) {
		String content = "您的密码被重置为：" + password + NewLineCharacter.defaultNewLineChar + "请先用该密码登录，然后尽快登录并修改您的密码！";
		sendNotifyMail(email, "GitDocument · " + configProperties.getOrganizationName() + " - 密码重置", content);
		log.info("已向邮箱：" + email + " 发送了密码重置邮件！");
	}

}