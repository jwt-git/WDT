package com.hundsun.network.dbPlus.enums;

/**
 * 错误枚举
 * @author yueliang 2013-02-26
 */
public enum EnumDbPlusError {
    
	INTERNAL_ERROR(500,"内部错误"),
	PARAM_ERROR(401,"参数错误"),
	CLASS_NOT_FOUND(402,"文件加载失败"),
	APP_FORBIDDEN(403,"功能关闭"),
	URL_ERROR(404,"404"),
	CONNECTION_ERROR(405,"数据库连接错误"),
	EXECUTE_SQL_NULL(406,"请输入SQL语句"),
	SHOW_TABLES_ERROR(407,"查询表信息失败"),
	EXECUTE_SQL_ERROR(408,"SQL语句执行错误"),
	SHOW_TABLE_INFO(409,"查看表信息失败"),
    ;
    
	 private int    value;

	 private String info;
    
	 private EnumDbPlusError(int value, String info) {
	        this.value = value;
	        this.info = info;
	    }

	    public int getValue() {
	        return value;
	    }

	    public String getInfo() {
	        return info;
	    }

}
