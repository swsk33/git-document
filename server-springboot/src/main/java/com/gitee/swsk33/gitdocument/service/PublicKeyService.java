package com.gitee.swsk33.gitdocument.service;

import com.gitee.swsk33.gitdocument.dataobject.PublicKey;
import com.gitee.swsk33.gitdocument.model.Result;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PublicKeyService {

	/**
	 * 添加公钥
	 *
	 * @param publicKey 公钥对象
	 */
	Result<Void> add(PublicKey publicKey) throws Exception;

	/**
	 * 删除公钥
	 *
	 * @param id 公钥id
	 */
	Result<Void> delete(int id) throws Exception;

	/**
	 * 获取一个用户的公钥
	 */
	Result<List<PublicKey>> getByUser() throws Exception;

}