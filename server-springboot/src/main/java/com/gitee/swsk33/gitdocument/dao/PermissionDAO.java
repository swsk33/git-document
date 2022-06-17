package com.gitee.swsk33.gitdocument.dao;

import com.gitee.swsk33.gitdocument.dataobject.Permission;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PermissionDAO {

	Permission getById(int id);

}