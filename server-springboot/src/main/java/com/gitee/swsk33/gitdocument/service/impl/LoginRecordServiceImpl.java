package com.gitee.swsk33.gitdocument.service.impl;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import com.gitee.swsk33.gitdocument.dao.LoginRecordDAO;
import com.gitee.swsk33.gitdocument.dataobject.LoginRecord;
import com.gitee.swsk33.gitdocument.model.Result;
import com.gitee.swsk33.gitdocument.service.LoginRecordService;
import com.gitee.swsk33.gitdocument.util.IPAddressUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.lionsoul.ip2region.xdb.Searcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LoginRecordServiceImpl implements LoginRecordService {

	@Autowired
	private LoginRecordDAO loginRecordDAO;

	@Autowired
	private Searcher searcher;

	@SaCheckLogin
	@Override
	public Result<Void> update(HttpServletRequest request) {
		// 解析IP地址和归属地
		String ip = IPAddressUtils.resolveIPAddress(request);
		String location = "未知";
		try {
			location = IPAddressUtils.resolveIPLocation(searcher.search(ip));
		} catch (Exception e) {
			log.error("解析IP归属地失败！IP地址为：{}", ip);
			log.error(e.getMessage());
		}
		// 获取登录的用户id
		int userId = StpUtil.getLoginIdAsInt();
		// 准备更新记录
		LoginRecord record = new LoginRecord();
		record.setUserId(userId);
		record.setIp(ip);
		record.setLocation(location);
		// 写入数据库
		loginRecordDAO.update(record);
		return Result.resultSuccess("更新用户登录记录成功！");
	}

}