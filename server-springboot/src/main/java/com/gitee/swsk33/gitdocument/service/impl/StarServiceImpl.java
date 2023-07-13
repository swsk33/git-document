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
		Result<Void> result = new Result<>();
		star.setId(IdUtil.getSnowflakeNextId());
		star.setUser((User) StpUtil.getSession().get(CommonValue.SA_USER_SESSION_INFO_KEY));
		if (starDAO.add(star) < 1) {
			result.setResultFailed("收藏失败！请联系开发者！");
			return result;
		}
		result.setResultSuccess("收藏成功！");
		return result;
	}

	@SaCheckLogin
	@Override
	public Result<Void> delete(long id) {
		Result<Void> result = new Result<>();
		Star getStar = starDAO.getById(id);
		if (getStar == null) {
			result.setResultFailed("这个收藏不存在！");
			return result;
		}
		// 校验是否是操作者本人
		User loginUser = (User) StpUtil.getSession().get(CommonValue.SA_USER_SESSION_INFO_KEY);
		if (loginUser.getId() != getStar.getUser().getId().intValue()) {
			result.setResultFailed("不可以替别人取消收藏！");
			return result;
		}
		starDAO.delete(id);
		result.setResultSuccess("取消收藏成功！");
		return result;
	}

	@SaCheckLogin
	@Override
	public Result<List<Star>> getByUser() {
		Result<List<Star>> result = new Result<>();
		// 获取当前登录用户的session缓存信息
		User getUser = (User) StpUtil.getSession().get(CommonValue.SA_USER_SESSION_INFO_KEY);
		List<Star> stars = starDAO.getByUserId(getUser.getId());
		result.setResultSuccess("获取用户收藏成功！", stars);
		return result;
	}

	@SaCheckLogin
	@Override
	public Result<Integer> getAnthologyStarCount(long anthologyId) {
		Result<Integer> result = new Result<>();
		result.setResultSuccess("获取文集收藏数完成！", starDAO.getAnthologyStarCount(anthologyId));
		return result;
	}

}