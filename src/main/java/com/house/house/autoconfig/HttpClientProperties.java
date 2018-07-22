package com.house.house.autoconfig;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix="spring.httpclient")
public class HttpClientProperties {

	private Integer connectTimeOut = 1000;
	private Integer socketTimeOut = 10000;
	private String agent="agent";
	private Integer maxConnperRoute = 10;
	private Integer maxConnTotal = 50;
	
	public Integer getConnectTimeOut() {
		return connectTimeOut;
	}
	public void setConnectTimeOut(Integer connectTimeOut) {
		this.connectTimeOut = connectTimeOut;
	}
	public Integer getSocketTimeOut() {
		return socketTimeOut;
	}
	public void setSocketTimeOut(Integer socketTimeOut) {
		this.socketTimeOut = socketTimeOut;
	}
	public String getAgent() {
		return agent;
	}
	public void setAgent(String agent) {
		this.agent = agent;
	}
	public Integer getMaxConnperRoute() {
		return maxConnperRoute;
	}
	public void setMaxConnperRoute(Integer maxConnperRoute) {
		this.maxConnperRoute = maxConnperRoute;
	}
	public Integer getMaxConnTotal() {
		return maxConnTotal;
	}
	public void setMaxConnTotal(Integer maxConnTotal) {
		this.maxConnTotal = maxConnTotal;
	}
	
	
	
}
