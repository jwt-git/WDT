package com.hundsun.network.dbPlus.enums;

/**
 * 数据库通用外挂模块-请求url枚举
 * @author yueliang 2013-2-25
 */
public final class DbPlusRequestUrl {

	/**
	 * 登录请求
	 */
	public static String 			LoginPath 				= "/dbSys/login.db";
	
	/**
	 * SQL执行请求
	 */
	public static String 			ExecuteSQLPath 			= "/dbSys/executeSql.db";
	
	/**
	 * 表信息请求
	 */
	public static String 			ShowAllTables 			= "/dbSys/showAllTables.db";
	
	/**
	 * 权限设置请求
	 */
	public static String 			AppConfig 				= "/dbSys/appConfig.db";
	
	/**
	 * 查看表信息请求
	 */
	public static String 			TableInfo 				= "/dbSys/tableInfo.db";
	
	/**
	 * 关闭功能并退出
	 */
	public static String 			CLOSE 					= "/dbSys/close.db";
	
	private DbPlusRequestUrl(){
    }
	
}
