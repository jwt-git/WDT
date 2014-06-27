package com.hundsun.network.dbPlus.query;

import java.sql.Connection;

/**
 * 封装请求基类
 */
public class DbPlusBaseRequest {

	private String driverClassName; // JDBC加载类名
	private String url; // 数据库IP地址
	private String username; // 数据库用户名
	private String password; // 数据库密码
	private Connection connection; // 数据库连接
	
	public String getDriverClassName() {
		return driverClassName;
	}
	public void setDriverClassName(String driverClassName) {
		this.driverClassName = driverClassName;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Connection getConnection() {
		return connection;
	}
	public void setConnection(Connection connection) {
		this.connection = connection;
	}
	
	
}
