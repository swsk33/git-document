package com.gitee.swsk33.gitdocument.service;

import com.gitee.swsk33.gitdocument.model.ErrorReport;
import org.springframework.stereotype.Service;

@Service
public interface EmailService {

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

}