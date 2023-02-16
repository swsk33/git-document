package com.gitee.swsk33.gitdocument.service;

import com.gitee.swsk33.gitdocument.dataobject.User;
import com.gitee.swsk33.gitdocument.model.ErrorReport;
import org.springframework.stereotype.Service;

@Service
public interface EmailService {

	/**
	 * 发送文本通知邮件
	 *
	 * @param email 目的邮件地址
	 * @param title 邮件标题
	 * @param text  邮件内容
	 */
	void sendNotifyMail(String email, String title, String text);

	/**
	 * 给所有管理员发送错误信息
	 *
	 * @param errorReport 错误报告对象
	 */
	void sendReportToAdmin(ErrorReport errorReport);

	/**
	 * 发送密码重置邮件
	 *
	 * @param email    被重置密码的用户邮箱
	 * @param password 重置后的密码
	 */
	void sendPasswordResetEmail(String email, String password);

	/**
	 * 发送角色更改邮件通知
	 *
	 * @param changedUser 被修改权限的角色
	 * @param operator    操作者
	 */
	void sendRoleChangeEmail(User changedUser, User operator);

}