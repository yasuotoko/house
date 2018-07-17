package com.mooc.house.house.filter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterBeanConfig {

	/**
	 * 1.构造filter
	 * 2.配置拦截url
	 * 3.
	 * @return
	 */
	@Bean
	public FilterRegistrationBean logFilter(){
		FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
		filterRegistrationBean.setFilter(new ClassFilter());
		List<String> urllist = new ArrayList<>();
		urllist.add("*");
		filterRegistrationBean.setUrlPatterns(urllist);
		return filterRegistrationBean;

	}
}
