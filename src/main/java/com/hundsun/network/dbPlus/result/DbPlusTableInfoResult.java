package com.hundsun.network.dbPlus.result;

import java.util.List;


/**
 * 表信息返回
 */
public class DbPlusTableInfoResult extends DbPlusBaseResult {

	private String 						tableName;
	private List<List<String>> 			dataInfo; // 查询数据信息结果集(静态)
	private List<List<String>> 			tableInfo; // 查询表信息结果集(静态)
	
	public List<List<String>> getDataInfo() {
		return dataInfo;
	}
	public void setDataInfo(List<List<String>> dataInfo) {
		this.dataInfo = dataInfo;
	}
	public List<List<String>> getTableInfo() {
		return tableInfo;
	}
	public void setTableInfo(List<List<String>> tableInfo) {
		this.tableInfo = tableInfo;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	
	
}
