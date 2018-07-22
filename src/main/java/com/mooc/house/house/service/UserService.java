package com.mooc.house.house.service;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalListener;
import com.google.common.collect.Lists;
import com.mooc.house.house.common.model.User;
import com.mooc.house.house.common.utils.BeanHelper;
import com.mooc.house.house.common.utils.HashUtils;
import com.mooc.house.house.mapper.UserMapper;

@Service
public class UserService {
	


	@Autowired
	public UserMapper userMapper;

	@Autowired
	public FileService fileService;
	
	@Autowired
	private MailService mailService;

	@Value("${file.prefix}")
	private String imgPrefix;
	
	public List<User> getUsers(){
		return userMapper.selectUsers();
	}

	/**
	 * 1 插入数据库，非激活 ;密码加密md5;保存头像到本地
	 * 2 生成key,绑定email
	 * 3 发送邮件
	 * @param account
	 * @return
	 */
	@Transactional(rollbackFor=Exception.class)
	public boolean addAcount(User account) {
		account.setPassword(HashUtils.encryPassword(account.getPassword()));
		List<String> imgList = fileService.getImgPath(Lists.newArrayList(account.getAvatorFile()));
		if (!imgList.isEmpty()) {
			account.setAvator(imgList.get(0));
		}
		BeanHelper.setDefaultProp(account,User.class);
		BeanHelper.onInsert(account);
		account.setEnable(0);
		userMapper.insert(account);
		mailService.registerNotify(account.getEmail());
		return true;
	}


	public boolean enable(String key) {		
		return mailService.enable(key);
	}
	
	public User getUserByQuery(User query){
		List<User> list = userMapper.getUserByQuery(query);
		return list.isEmpty() ? null : list.get(0);
	}

	public User auth(String uname, String password) {
		User user = new User();
		user.setEmail(uname);
		user.setPassword(password);
		user.setEnable(1);
		List<User> list = userMapper.getUserByQuery(user);
		list.forEach(u -> {
			u.setAvator(imgPrefix+u.getAvator());
		});
		if (!list.isEmpty()) {
			return list.get(0);
		}
		return null;
	}

	public void updateUser(User updateUser, String email) {
		updateUser.setEmail(email);
		BeanHelper.onUpdate(updateUser);
		userMapper.update(updateUser);
	}

	
	
}
