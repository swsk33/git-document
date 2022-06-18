package com.gitee.swsk33.gitdocument.service;

import com.gitee.swsk33.gitdocument.dataobject.PublicKey;
import com.gitee.swsk33.gitdocument.model.Result;
import org.springframework.stereotype.Service;

@Service
public interface PublicKeyService {

	/**
	 * 添加公钥
	 *
	 * @param publicKey 公钥对象
	 */
	Result<PublicKey> add(PublicKey publicKey) throws Exception;

	/**
	 * 删除公钥
	 *
	 * @param id 公钥id
	 */
	Result<PublicKey> delete(int id);

	/**
	 * 获取一个用户的公钥
	 *
	 * @param userId 用户id
	 */
	Result<PublicKey> getByUser(int userId);

}