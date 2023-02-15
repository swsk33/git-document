package com.gitee.swsk33.gitdocument.dao;

import com.gitee.swsk33.gitdocument.dataobject.Setting;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SettingDAO {

	int add(Setting setting);

	int update(Setting setting);

	Setting getById(int id);

}