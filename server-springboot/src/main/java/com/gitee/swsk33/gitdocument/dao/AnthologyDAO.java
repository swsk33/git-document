package com.gitee.swsk33.gitdocument.dao;

import com.gitee.swsk33.gitdocument.dataobject.Anthology;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AnthologyDAO {

	int add(Anthology anthology);

	int delete(long id);

	int update(Anthology anthology);

	Anthology getById(long id);

	Anthology getByName(String name);

	List<Anthology> getAll();

}