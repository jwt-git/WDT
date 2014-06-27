package com.hundsun.network.dbPlus.service;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;
import java.util.Properties;

import com.hundsun.network.dbPlus.enums.DbPlusDict;
import com.hundsun.network.dbPlus.enums.EnumDbPlusError;
import com.hundsun.network.dbPlus.query.DbPlusBaseRequest;
import com.hundsun.network.dbPlus.result.DbPlusBaseResult;
import com.hundsun.network.dbPlus.web.servlet.DbPlusWriter;

/**
 * Service基类
 * 先判定有无权限，如果有权限connection放入service里
 * @author yueliang 2013-2-25
 */
public abstract class DbPlusBaseService {

	private Connection connection;
	
	protected DbPlusBaseRequest baseRequest;
	
	/**
	 * 判断有无权限（含登录操作）
	 * 数据库连接所用的JDBC类名、连接地址、用户名、密码暂时都加到request里
	 * @param paraMap
	 * @return DbPlusBaseResult
	 */
	public DbPlusBaseResult hasPermission(Map<String, String[]> paraMap) {
		baseRequest = new DbPlusBaseRequest();
		DbPlusBaseResult result = new DbPlusBaseResult();
		if(null == paraMap) {
			return null;
		}
		String[] driverClassName = paraMap.get(DbPlusDict.DriverClassName); // JDBC加载类名
		String[] url = paraMap.get(DbPlusDict.Url); // 数据库IP地址
		String[] username = paraMap.get(DbPlusDict.Username); // 数据库用户名
		String[] password = paraMap.get(DbPlusDict.Password); // 数据库密码
		if(username != null && username.length > 0) {
			baseRequest.setUsername(username[0]);
		}
		if(password != null && password.length > 0) {
			baseRequest.setPassword(password[0]);
		}
		if(url != null && url.length > 0) {
			baseRequest.setUrl(url[0]);
		}
		if(driverClassName != null && driverClassName.length > 0) {
			baseRequest.setDriverClassName(driverClassName[0]);
		}
		
		// request里没有，则肯定无权限
		if(null == username || username.length < 1
				|| null == password || password.length < 1
				|| null == url || url.length < 1
				|| null == driverClassName || driverClassName.length < 1){
			return null;
		}
		// request里有，则判定能否连接，并返回连接（含登录操作）
		else {
			try {
				Properties props =new Properties();
				props.put("remarksReporting","true"); // 字段描述开放
				props.put("user", username[0]);
				props.put("password", password[0]);
				Class.forName(driverClassName[0]);
				// 有权限或登录成功，service的connection赋值
				connection = DriverManager.getConnection(url[0], props);
//				connection = DriverManager.getConnection(url[0], username[0], password[0]);

			} catch (ClassNotFoundException e) {
				result.setErrorNOInfo(EnumDbPlusError.CLASS_NOT_FOUND.getValue(), 
						EnumDbPlusError.CLASS_NOT_FOUND.getInfo());
				e.printStackTrace();
			} catch (SQLException e) {
				// 登录失败
				result.setErrorNOInfo(EnumDbPlusError.CONNECTION_ERROR.getValue(), 
						EnumDbPlusError.CONNECTION_ERROR.getInfo());
				e.printStackTrace();
			}
		}
		return result;
	}
	
	/**
	 * 无权限时的打印登录页面
	 * @param writer result
	 */
	public void printLoginHTML(PrintWriter writer, DbPlusBaseResult result, 
			Boolean useDefaultConnectionConfig, Map<String, String> config) {
		DbPlusWriter.printLoginHTML(writer, baseRequest, result, 
				useDefaultConnectionConfig, config); // 打印登录页面
	}

	public Connection getConnection() {
		return connection;
	}
	
	public void closeConnection() {
		if(connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		connection = null;
	}

}
