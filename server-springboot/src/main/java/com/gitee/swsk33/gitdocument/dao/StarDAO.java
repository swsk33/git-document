package com.gitee.swsk33.gitdocument.dao;

import com.gitee.swsk33.gitdocument.dataobject.Star;
import com.mybatisflex.core.BaseMapper;
import com.mybatisflex.core.query.QueryWrapper;
import org.apache.ibatis.annotations.Mapper;

import static com.gitee.swsk33.gitdocument.dataobject.table.StarTableDef.STAR;
import static com.mybatisflex.core.query.QueryMethods.count;

@Mapper
public interface StarDAO extends BaseMapper<Star> {

	/**
	 * 获得一个文集的收藏数
	 *
	 * @param anthologyId 文集id
	 * @return 对应文集的收藏数量
	 */
	default Integer getAnthologyStarCount(long anthologyId) {
		return selectObjectByQueryAs(QueryWrapper.create().select(count(STAR.ALL_COLUMNS)).from(STAR).where(STAR.ANTHOLOGY_ID.eq(anthologyId)), Integer.class);
	}

}