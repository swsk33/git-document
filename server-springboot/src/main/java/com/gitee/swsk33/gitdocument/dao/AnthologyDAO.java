package com.gitee.swsk33.gitdocument.dao;

import com.gitee.swsk33.gitdocument.dataobject.Anthology;
import com.mybatisflex.core.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AnthologyDAO extends BaseMapper<Anthology> {

	/**
	 * 修改文集信息
	 *
	 * @param anthology 要修改的文集对象，需要包含id字段
	 * @return 修改记录条数
	 */
	int update(Anthology anthology);

	/**
	 * 根据文集id获取文集
	 *
	 * @param id 文集id
	 * @return 查询到的文集对象
	 */
	Anthology getById(long id);

	/**
	 * 根据文集名获取文集
	 *
	 * @param name 文集名
	 * @return 查询到的文集对象
	 */
	Anthology getByName(String name);

	/**
	 * 获取全部文集列表
	 *
	 * @return 全部文集
	 */
	List<Anthology> getAll();

}