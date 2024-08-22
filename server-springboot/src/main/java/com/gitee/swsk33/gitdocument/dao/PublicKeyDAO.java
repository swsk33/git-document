package com.gitee.swsk33.gitdocument.dao;

import com.gitee.swsk33.gitdocument.dataobject.PublicKey;
import com.mybatisflex.core.BaseMapper;
import com.mybatisflex.core.query.QueryWrapper;
import org.apache.ibatis.annotations.Mapper;

import static com.gitee.swsk33.gitdocument.dataobject.table.PublicKeyTableDef.PUBLIC_KEY;
import static com.mybatisflex.core.query.QueryMethods.number;

@Mapper
public interface PublicKeyDAO extends BaseMapper<PublicKey> {

	/**
	 * 判断对应内容的公钥记录是否存在
	 *
	 * @param content 公钥内容
	 * @return 存在返回true
	 */
	default boolean existsByContent(String content) {
		QueryWrapper wrapper = QueryWrapper.create().select(number(1)).from(PUBLIC_KEY).where(PUBLIC_KEY.CONTENT.eq(content));
		Long exists = selectObjectByQueryAs(wrapper, Long.class);
		return exists != null;
	}

	/**
	 * 根据id判断公钥是否存在
	 *
	 * @param id 公钥id
	 * @return 存在返回true
	 */
	default boolean existsById(int id) {
		QueryWrapper wrapper = QueryWrapper.create().select(number(1)).from(PUBLIC_KEY).where(PUBLIC_KEY.ID.eq(id));
		Long exists = selectObjectByQueryAs(wrapper, Long.class);
		return exists != null;
	}

}