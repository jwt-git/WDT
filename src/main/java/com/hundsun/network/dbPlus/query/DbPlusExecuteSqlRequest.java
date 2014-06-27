package com.hundsun.network.dbPlus.query;

import java.util.ArrayList;
import java.util.List;

/**
 * SQL执行封装请求
 */
public class DbPlusExecuteSqlRequest extends DbPlusBaseRequest {
	
	private String requestSql = ""; // 所有SQL语句
	private List<String> executeSql = new ArrayList<String>(); // 选中的需执行SQL语句
	private Integer beginIndex = 0; // 选中的需执行SQL语句起点索引
	private Integer endIndex = 0; // 选中的需执行SQL语句终点索引
	
	
	public String getRequestSql() {
		return requestSql;
	}
	public void setRequestSql(String requestSql) {
		this.requestSql = requestSql;
	}
	public List<String> getExecuteSql() {
		return executeSql;
	}
	public void setExecuteSql(List<String> executeSql) {
		this.executeSql = executeSql;
	}
	public Integer getBeginIndex() {
		return beginIndex;
	}
	public void setBeginIndex(Integer beginIndex) {
		this.beginIndex = beginIndex;
	}
	public Integer getEndIndex() {
		return endIndex;
	}
	public void setEndIndex(Integer endIndex) {
		this.endIndex = endIndex;
	}
	
}
