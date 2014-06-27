package com.hundsun.network.dbPlus.enums;

/**
 * 常量字典、文案、配置项名称    等常量信息
 * @author yueliang 2013-4-9
 */
public final class DbPlusDict {

	/**
	 * 配置项名称
	 */
	// 页面编码方式
	public static String 			Charset 				= "charset";//页面编码方式
	// 是否在控制台打印log
	public static String 			Log 					= "log";
	// 数据库连接默认JDBC类名、连接地址、用户名、密码
	public static String 			DriverClassName 		= "driverClassName";
	public static String 			Url 					= "url";
	public static String 			Username 				= "username";
	public static String 			Password 				= "password";
	// 功能开关配置
	public static String 			GlobalPermission 		= "GlobalPermission";
	public static String 			UseDefaultConnectionConfig = "UseDefaultConnectionConfig";
	public static String 			SELECT 					= "SELECT";
	public static String 			UPDATE 					= "UPDATE";
	public static String 			INSERT 					= "INSERT";
	public static String 			DELETE 					= "DELETE";
	public static String 			CREATE 					= "CREATE";
	public static String 			DROP 					= "DROP";
	public static String 			ALTER 					= "ALTER";
	
	/**
	 * 常量字典
	 */
	public static String 			TRUE 					= "true";
	public static String 			UTF8 					= "utf-8";
	
	private DbPlusDict(){
    }
	
}
