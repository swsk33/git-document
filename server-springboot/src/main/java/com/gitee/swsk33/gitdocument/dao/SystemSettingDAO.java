package com.gitee.swsk33.gitdocument.dao;

import org.apache.ibatis.annotations.Mapper;

/**
 * 系统设置键值对操作
 */
@Mapper
public interface SystemSettingDAO {

	/**
	 * 设定指定的键值对
	 *
	 * @param key   要设置的键名
	 * @param value 要设定的值
	 * @return 更新记录条数
	 */
	int set(String key, String value);

	/**
	 * 获取一个键的值
	 *
	 * @param key 键名
	 * @return 对应的值
	 */
	String get(String key);

}