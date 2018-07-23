package com.mooc.house.house.service;

import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalListener;
import com.mooc.house.house.common.model.User;
import com.mooc.house.house.mapper.UserMapper;

@Service
public class MailService {

	private final Cache<String, String> registerCache = CacheBuilder.newBuilder().maximumSize(100)
			.expireAfterAccess(15, TimeUnit.MINUTES)
			.removalListener(new RemovalListener<String, String>() {
				public void onRemoval(com.google.common.cache.RemovalNotification<String,String> notification) {
					userMapper.delete(notification.getKey());
				}		
			}).build() ;
	@Autowired
	public UserMapper userMapper;
	
	@Autowired
	private JavaMailSender mailSender;

	
	@Value("${spring.mail.username}")
	private String from;
	
	public void sendMail(String title, String url, String email) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom(from);
		message.setTo(email);
		message.setText(url);
		mailSender.send(message);
	}

	@Value("${domain.name}")
	private String domainName;
	
	/**
	 * 1 缓存key-mail的关系
	 * 2 借助spring mail 发送邮件
	 * 3 借助异步框架进异步操作
	 * @param email
	 */
	@Async
	public void registerNotify(String email) {
		String randomkey = RandomStringUtils.randomAlphabetic(10);
		registerCache.put(randomkey, email);
		String url = "http://"+ domainName +"/accounts/verify/?key"+randomkey;
		sendMail("this is a test mail",url,email);
	}

	
	public boolean enable(String key) {
		String mail = registerCache.getIfPresent(key);
		if (StringUtils.isBlank(mail)) {
			return false;
		}
		User updateUser = new User();
		updateUser.setEmail(mail);
		updateUser.setEnable(1);
		userMapper.update(updateUser);
		registerCache.invalidate(key);
		return false;
	}

	
}
