package com.gitee.swsk33.gitdocument.dao;

import com.gitee.swsk33.gitdocument.dataobject.Role;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RoleDAO {

	Role getById(int id);

}