package com.mooc.house.house.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mooc.house.house.common.model.User;
import com.mooc.house.house.mapper.UserMapper;

@Service
public class UserService {

	@Autowired
	public UserMapper userMapper;

	public List<User> getUsers(){
		return userMapper.selectUsers();
	}
}
