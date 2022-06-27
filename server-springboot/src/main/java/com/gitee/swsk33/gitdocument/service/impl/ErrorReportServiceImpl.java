package com.gitee.swsk33.gitdocument.service.impl;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import com.gitee.swsk33.gitdocument.dao.ArticleDAO;
import com.gitee.swsk33.gitdocument.dataobject.User;
import com.gitee.swsk33.gitdocument.model.ErrorReport;
import com.gitee.swsk33.gitdocument.model.Result;
import com.gitee.swsk33.gitdocument.param.CommonValue;
import com.gitee.swsk33.gitdocument.service.EmailService;
import com.gitee.swsk33.gitdocument.service.ErrorReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ErrorReportServiceImpl implements ErrorReportService {

	@Autowired
	private ArticleDAO articleDAO;

	@Autowired
	private EmailService emailService;

	@SaCheckLogin
	@Override
	public Result<ErrorReport> receiveReport(ErrorReport report) {
		Result<ErrorReport> result = new Result<>();
		// 填充信息
		report.setUser((User) StpUtil.getSession().get(CommonValue.SA_USER_SESSION_INFO_KEY));
		report.setArticle(articleDAO.getById(report.getArticle().getId()));
		emailService.sendReportToAdmin(report);
		result.setResultSuccess("已提交错误报告！");
		return result;
	}

}