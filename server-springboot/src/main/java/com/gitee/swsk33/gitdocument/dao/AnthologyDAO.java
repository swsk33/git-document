package com.gitee.swsk33.gitdocument.dao;

import com.gitee.swsk33.gitdocument.dataobject.Anthology;
import com.mybatisflex.core.BaseMapper;
import com.mybatisflex.core.query.QueryWrapper;
import org.apache.ibatis.annotations.Mapper;

import static com.gitee.swsk33.gitdocument.dataobject.table.AnthologyTableDef.ANTHOLOGY;

@Mapper
public interface AnthologyDAO extends BaseMapper<Anthology> {

	/**
	 * 根据文集名获取文集
	 *
	 * @param name 文集名
	 * @return 查询到的文集对象
	 */
	default Anthology getByName(String name) {
		return selectOneByQuery(QueryWrapper.create().where(ANTHOLOGY.NAME.eq(name)));
	}

}