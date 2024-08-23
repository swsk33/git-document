package com.gitee.swsk33.gitdocument.dao;

import com.gitee.swsk33.gitdocument.dataobject.Anthology;
import com.mybatisflex.core.BaseMapper;
import com.mybatisflex.core.query.QueryWrapper;
import org.apache.ibatis.annotations.Mapper;

import static com.gitee.swsk33.gitdocument.dataobject.table.AnthologyTableDef.ANTHOLOGY;
import static com.mybatisflex.core.query.QueryMethods.number;

@Mapper
public interface AnthologyDAO extends BaseMapper<Anthology> {

	/**
	 * 根据文集名判断文集是否存在
	 *
	 * @param name 文集名
	 * @return 存在返回true
	 */
	default boolean existsByName(String name) {
		QueryWrapper wrapper = QueryWrapper.create().select(number(1)).where(ANTHOLOGY.NAME.eq(name));
		Long exists = selectObjectByQueryAs(wrapper, Long.class);
		return exists != null;
	}

}