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
	 * @return 新增的收藏对象
	 */
	Result<Star> add(Star star);

	/**
	 * 取消收藏
	 *
	 * @param id 取消的收藏id
	 */
	Result<Void> delete(long id);

	/**
	 * 获取当前登录的用户的全部收藏列表
	 */
	Result<List<Star>> getByLoginUser();

}