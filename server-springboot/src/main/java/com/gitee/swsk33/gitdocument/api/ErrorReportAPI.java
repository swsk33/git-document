package com.gitee.swsk33.gitdocument.api;

import com.gitee.swsk33.gitdocument.model.ErrorReport;
import com.gitee.swsk33.gitdocument.model.Result;
import com.gitee.swsk33.gitdocument.service.ErrorReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/error")
public class ErrorReportAPI {

	@Autowired
	private ErrorReportService errorReportService;

	@PostMapping("/submit")
	public Result<ErrorReport> submit(@Valid @RequestBody ErrorReport report, BindingResult errors) {
		if (errors.hasErrors()) {
			Result<ErrorReport> result = new Result<>();
			result.setResultFailed(errors.getFieldError().getDefaultMessage());
			return result;
		}
		return errorReportService.receiveReport(report);
	}

}