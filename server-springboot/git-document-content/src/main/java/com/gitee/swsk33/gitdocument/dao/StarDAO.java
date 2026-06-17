package com.gitee.swsk33.gitdocument.dao;

import com.gitee.swsk33.gitdocument.dataobject.Star;
import com.mybatisflex.core.BaseMapper;
import com.mybatisflex.core.query.QueryWrapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

import static com.gitee.swsk33.gitdocument.dataobject.table.StarTableDef.STAR;

@Mapper
public interface StarDAO extends BaseMapper<Star> {

	/**
	 * 获取一个用户的全部收藏列表
	 *
	 * @param userId 用户id
	 * @return 对应用户收藏列表
	 */
	default List<Star> getByUser(int userId) {
		return selectListByQuery(QueryWrapper.create().where(STAR.USER_ID.eq(userId)));
	}

}