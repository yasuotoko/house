package com.mooc.house.house.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.buf.UEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mooc.house.house.common.constant.CommonConstants;
import com.mooc.house.house.common.model.User;
import com.mooc.house.house.common.result.ResultMsg;
import com.mooc.house.house.common.utils.HashUtils;
import com.mooc.house.house.service.UserService;

@Controller
public class UserController {

	@Autowired
	private UserService userService;

	@RequestMapping("user")
	public List<User> getUsers(){
		return userService.getUsers();
	}
	
	/**
	 * 1注册验证 2 发送邮件 3 验证失败重定向到注册页面
	 * @param account
	 * @param modelMap
	 * @return
	 */
	@RequestMapping("accounts/register")
	public String accountRegister(User account,ModelMap modelMap) {
		if (account == null || account.getName()==null) {
			return "/user/accounts/register";
		}
		
		// 用户验证
		ResultMsg resultMsg = UserHelper.validate(account);
		
		if (resultMsg.isSuccess() && userService.addAcount(account)) {
			modelMap.put("email", account.getEmail());
			return "/user/accounts/registerSubmit";
		} else {
			return "redirect:/accounts/register?"+resultMsg.asUrlParams();
		}
		
	}
	
	@RequestMapping("accounts/verify")
	public String verify(String key) {
		boolean result = userService.enable(key);
		if(result) {
			return "redirect:/index?"+ResultMsg.successMsg("激活成功").asUrlParams();
		}else {
			return "redirect:/accouts/register?"+ResultMsg.errorMsg("激活失败,请确认是否国企");
		}
	}
	
	@RequestMapping("/accounts/signin")
	public String singin(HttpServletRequest request) {
		String uname = request.getParameter("username");
		String password = request.getParameter("password");
		String target = request.getParameter("target");
		if (uname == null || password == null) {
			request.setAttribute("target", target);
			return "/user/accounts/sign";
		}
		User user = userService.auth(uname,password);
		if (user == null) {
			return "redirect:/account/singin" + "target=" +target 
					+ "&username="+uname +"&" + ResultMsg.errorMsg("用户名或密码错误").asUrlParams();
		} else {
			HttpSession session = request.getSession(true);
			session.setAttribute(CommonConstants.USER_ATTRIBUTE, user);
			session.setAttribute(CommonConstants.PLAIN_USER_ATTRIBUTE, user);
			return StringUtils.isBlank(target)? "redirect:"+target : "redirect:/index";
		}
	}
	
	/**
	 * 登出
	 * @param request
	 * @return
	 */
	@RequestMapping("accounts/logout")
	public String logout(HttpServletRequest request) {
		HttpSession session = request.getSession(true);
		session.invalidate();
		return "redirect:/index";
	}
	
	@RequestMapping("accounts/profile")
	public String profile(HttpServletRequest request,User updateUser,ModelMap modelMap) {
		if (updateUser.getEmail()==null) {
			return "/user/acconuts/profile";
		}
		userService.updateUser(updateUser,updateUser.getEmail());
		User query = new User();
		query.setEmail(updateUser.getEmail());
		User user = userService.getUserByQuery(query);
		request.getSession(true).setAttribute("user", user);
		return "redirect:/accounts/profile?" + ResultMsg.successMsg("更新成功").asUrlParams();
	}
	
	@RequestMapping("accounts/changePassword")
	public String changePassword(String email,String password,String newPassword,String confirmPassword,ModelMap modelMap) {
		User user = userService.auth(email,password);
		if (user == null || !confirmPassword.equals(newPassword)) {
			return "redirect:/accounts/profile?" + ResultMsg.errorMsg("密码错误").asUrlParams();
		}
		User updateUser = new User();
		updateUser.setPasswd(HashUtils.encryPassword(newPassword));
		userService.updateUser(updateUser, email);
		return "redirect:/accounts/profile" + ResultMsg.successMsg("更新成功").asUrlParams();
	}
}
