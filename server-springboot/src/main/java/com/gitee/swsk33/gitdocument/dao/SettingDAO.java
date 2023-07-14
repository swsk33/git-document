package com.gitee.swsk33.gitdocument.dao;

import com.gitee.swsk33.gitdocument.dataobject.Setting;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SettingDAO {

	/**
	 * 插入一个设置对象
	 *
	 * @param setting 设置对象
	 * @return 插入记录条数
	 */
	int add(Setting setting);

	/**
	 * 更新一个设置对象
	 *
	 * @param setting 设置对象
	 * @return 更新记录条数
	 */
	int update(Setting setting);

	/**
	 * 根据id获取设置对象
	 *
	 * @param id 设置id
	 * @return 设置对象
	 */
	Setting getById(int id);

}