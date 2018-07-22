package com.mooc.house.house;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

import com.mooc.house.house.common.model.User;
import com.mooc.house.house.service.UserService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class AuthTest {

	@Autowired
	private UserService userService;
	
	@Test
	public void testAuth() {
		User user = userService.auth("spring-", "111111");
		assert user != null;
		System.out.println(user.getName());
	}
}
