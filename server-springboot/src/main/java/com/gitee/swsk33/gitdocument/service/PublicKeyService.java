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
	Result<Void> add(PublicKey publicKey);

	/**
	 * 删除公钥
	 *
	 * @param id 公钥id
	 */
	Result<Void> delete(int id);

}