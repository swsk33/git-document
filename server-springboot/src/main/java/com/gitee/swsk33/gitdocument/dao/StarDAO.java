package com.gitee.swsk33.gitdocument.dao;

import com.gitee.swsk33.gitdocument.dataobject.Star;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface StarDAO {

	/**
	 * 插入一个收藏信息
	 *
	 * @param star 收藏信息
	 * @return 插入记录条数
	 */
	int add(Star star);

	/**
	 * 删除一个收藏信息
	 *
	 * @param id 收藏信息id
	 * @return 删除记录条数
	 */
	int delete(long id);

	/**
	 * 根据id获取收藏信息
	 *
	 * @param id 收藏信息id
	 * @return 收藏信息
	 */
	Star getById(long id);

	/**
	 * 获取一个用户的全部收藏信息
	 *
	 * @param userId 用户id
	 * @return 收藏信息列表
	 */
	List<Star> getByUserId(int userId);

	/**
	 * 获得一个文集的收藏数
	 *
	 * @param anthologyId 文集id
	 * @return 对应文集的收藏数量
	 */
	Integer getAnthologyStarCount(long anthologyId);

}