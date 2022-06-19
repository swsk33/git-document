package com.gitee.swsk33.gitdocument.dao;

import com.gitee.swsk33.gitdocument.dataobject.Star;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface StarDAO {

	int add(Star star);

	int delete(int id);

	List<Star> getByUserId(int userId);

	/**
	 * 获得一个文集的收藏数
	 */
	Integer getAnthologyStarCount(int anthologyId);

}