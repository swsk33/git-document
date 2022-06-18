package com.gitee.swsk33.gitdocument.service.impl;

import com.gitee.swsk33.gitdocument.dao.UserDAO;
import com.gitee.swsk33.gitdocument.dataobject.User;
import com.gitee.swsk33.gitdocument.model.Result;
import com.gitee.swsk33.gitdocument.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDAO userDAO;

	@Override
	public Result<User> register(User user) {
		return null;
	}

	@Override
	public Result<User> delete(int id) {
		return null;
	}

	@Override
	public Result<User> update(User user) {
		return null;
	}

	@Override
	public Result<User> login(User user) {
		return null;
	}

	@Override
	public Result<List<User>> getAll() {
		return null;
	}

}