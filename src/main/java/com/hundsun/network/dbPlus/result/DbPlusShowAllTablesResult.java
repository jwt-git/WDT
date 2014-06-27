package com.hundsun.network.dbPlus.result;

import java.util.List;

/**
 * 表信息返回
 */
public class DbPlusShowAllTablesResult extends DbPlusBaseResult {

	private List<String> 			tableList; // 表名列表

	public List<String> getTableList() {
		return tableList;
	}

	public void setTableList(List<String> tableList) {
		this.tableList = tableList;
	}
	
}
