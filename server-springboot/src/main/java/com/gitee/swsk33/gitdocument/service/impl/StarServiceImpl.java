package com.gitee.swsk33.gitdocument.service.impl;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.IdUtil;
import com.gitee.swsk33.gitdocument.dao.StarDAO;
import com.gitee.swsk33.gitdocument.dataobject.Star;
import com.gitee.swsk33.gitdocument.dataobject.User;
import com.gitee.swsk33.gitdocument.model.Result;
import com.gitee.swsk33.gitdocument.param.CommonValue;
import com.gitee.swsk33.gitdocument.service.StarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StarServiceImpl implements StarService {

	@Autowired
	private StarDAO starDAO;

	@SaCheckLogin
	@Override
	public Result<Void> add(Star star) {
		star.setId(IdUtil.getSnowflakeNextId());
		star.setUser((User) StpUtil.getSession().get(CommonValue.SA_USER_SESSION_INFO_KEY));
		if (starDAO.add(star) < 1) {
			return Result.resultFailed("收藏失败！请联系开发者！");
		}
		return Result.resultSuccess("收藏成功！");
	}

	@SaCheckLogin
	@Override
	public Result<Void> delete(long id) {
		Star getStar = starDAO.getById(id);
		if (getStar == null) {
			return Result.resultFailed("这个收藏不存在！");
		}
		// 校验是否是操作者本人
		User loginUser = (User) StpUtil.getSession().get(CommonValue.SA_USER_SESSION_INFO_KEY);
		if (loginUser.getId() != getStar.getUser().getId().intValue()) {
			return Result.resultFailed("不可以替别人取消收藏！");
		}
		starDAO.delete(id);
		return Result.resultSuccess("取消收藏成功！");
	}

	@SaCheckLogin
	@Override
	public Result<List<Star>> getByUser() {
		// 获取当前登录用户的session缓存信息
		User getUser = (User) StpUtil.getSession().get(CommonValue.SA_USER_SESSION_INFO_KEY);
		List<Star> stars = starDAO.getByUserId(getUser.getId());
		return Result.resultSuccess("获取用户收藏成功！", stars);
	}

	@SaCheckLogin
	@Override
	public Result<Integer> getAnthologyStarCount(long anthologyId) {
		return Result.resultSuccess("获取文集收藏数完成！", starDAO.getAnthologyStarCount(anthologyId));
	}

}