package com.gitee.swsk33.gitdocument.service;

import com.gitee.swsk33.gitdocument.model.ErrorReport;
import com.gitee.swsk33.gitdocument.model.Result;
import org.springframework.stereotype.Service;

@Service
public interface ErrorReportService {

	/**
	 * 接受错误报告
	 *
	 * @param report 错误报告
	 */
	Result<ErrorReport> receiveReport(ErrorReport report);

}