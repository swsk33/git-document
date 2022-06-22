package com.gitee.swsk33.gitdocument.service;

import com.gitee.swsk33.gitdocument.model.ErrorReport;
import org.springframework.stereotype.Service;

@Service
public interface EmailService {

	/**
	 * 给所有管理员发送错误信息
	 * @param errorReport 错误报告对象
	 */
	void SendReportToAdmin(ErrorReport errorReport);

}