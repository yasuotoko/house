package com.mooc.house.house.controller;

import com.alibaba.druid.util.StringUtils;
import com.mooc.house.house.common.model.User;
import com.mooc.house.house.common.result.ResultMsg;

public class UserHelper {
 
	public static ResultMsg validate(User account) {
		if (StringUtils.isEmpty(account.getEmail())) {
			return ResultMsg.errorMsg("email 有误");
		}
		if (StringUtils.isEmpty(account.getName())) {
			return ResultMsg.errorMsg("用户名有误");
		}
		if (StringUtils.isEmpty(account.getConfirmPasswd() )) {
			return ResultMsg.errorMsg("用户名有误");
		}
		
		if (account.getPassword().length()<6) {
			return ResultMsg.errorMsg("密码大于6位");
		}
		return new ResultMsg();
	}
}
