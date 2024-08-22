package com.gitee.swsk33.gitdocument.service.impl;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import com.gitee.swsk33.gitdocument.dao.StarDAO;
import com.gitee.swsk33.gitdocument.dataobject.Star;
import com.gitee.swsk33.gitdocument.model.Result;
import com.gitee.swsk33.gitdocument.service.StarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StarServiceImpl implements StarService {

	@Autowired
	private StarDAO starDAO;

	@SaCheckLogin
	@Override
	public Result<Void> add(Star star) {
		// 获取登录的用户id并设定给Star对象
		star.setUserId(StpUtil.getLoginIdAsInt());
		if (starDAO.insert(star) < 1) {
			return Result.resultFailed("收藏失败！请联系开发者！");
		}
		return Result.resultSuccess("收藏成功！");
	}

	@SaCheckLogin
	@Override
	public Result<Void> delete(long id) {
		Star getStar = starDAO.selectOneById(id);
		if (getStar == null) {
			return Result.resultFailed("这个收藏不存在！");
		}
		// 校验是否是操作者本人
		if (StpUtil.getLoginIdAsInt() != getStar.getUserId()) {
			return Result.resultFailed("不可以替别人取消收藏！");
		}
		starDAO.deleteById(id);
		return Result.resultSuccess("取消收藏成功！");
	}

	@SaCheckLogin
	@Override
	public Result<Integer> getAnthologyStarCount(long anthologyId) {
		return Result.resultSuccess("获取文集收藏数完成！", starDAO.getAnthologyStarCount(anthologyId));
	}

}