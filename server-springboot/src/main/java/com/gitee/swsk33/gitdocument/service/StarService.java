package com.gitee.swsk33.gitdocument.service;

import com.gitee.swsk33.gitdocument.dataobject.Star;
import com.gitee.swsk33.gitdocument.model.Result;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 收藏服务
 */
@Service
public interface StarService {

	/**
	 * 添加收藏
	 *
	 * @param star 收藏信息
	 */
	Result<Star> add(Star star);

	/**
	 * 取消收藏
	 *
	 * @param id 取消的收藏id
	 */
	Result<Star> delete(long id);

	/**
	 * 查询一个用户所有的收藏
	 */
	Result<List<Star>> getByUser();

	/**
	 * 查询一个文集的被收藏数
	 *
	 * @param anthologyId 文集id
	 */
	Result<Integer> getAnthologyStarCount(long anthologyId);

}