package com.gitee.swsk33.gitdocument.dao;

import com.gitee.swsk33.gitdocument.dataobject.PublicKey;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PublicKeyDAO {

	int add(PublicKey publicKey);

	int delete(int id);

	List<PublicKey> getByUserId(int userId);

}