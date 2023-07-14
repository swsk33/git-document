package com.gitee.swsk33.gitdocument.dao;

import com.gitee.swsk33.gitdocument.dataobject.PublicKey;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PublicKeyDAO {

	/**
	 * 插入一个公钥对象
	 *
	 * @param publicKey 公钥对象
	 * @return 插入记录条数
	 */
	int add(PublicKey publicKey);

	/**
	 * 删除一个公钥对象
	 *
	 * @param id 删除的公钥id
	 * @return 删除记录条数
	 */
	int delete(int id);

	/**
	 * 根据id获取公钥对象
	 *
	 * @param id 公钥id
	 * @return 公钥对象
	 */
	PublicKey getById(int id);

	/**
	 * 根据用户id获取公钥对象
	 *
	 * @param userId 用户id
	 * @return 公钥对象
	 */
	List<PublicKey> getByUserId(int userId);

}