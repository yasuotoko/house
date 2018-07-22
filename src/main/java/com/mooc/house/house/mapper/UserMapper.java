package com.mooc.house.house.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.mooc.house.house.common.model.User;

@Mapper
public interface UserMapper {

	public List<User> selectUsers();

	public int insert(User account);

	public void delete(String key);

	public void update(User updateUser);

	public List<User> getUserByQuery(User user);

}
