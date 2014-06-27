package com.hundsun.network.dbPlus.result;


/**
 * 登录（注销）操作返回
 */
public class DbPlusLoginResult extends DbPlusBaseResult {

	private DbPlusShowAllTablesResult 			showAllTablesResult; // 表信息
	private DbPlusExcuteResult 					excuteResult; // ExcuteSQL结果
	
	public DbPlusShowAllTablesResult getShowAllTablesResult() {
		return showAllTablesResult;
	}
	public void setShowAllTablesResult(DbPlusShowAllTablesResult showAllTablesResult) {
		this.showAllTablesResult = showAllTablesResult;
	}
	public DbPlusExcuteResult getExcuteResult() {
		return excuteResult;
	}
	public void setExcuteResult(DbPlusExcuteResult excuteResult) {
		this.excuteResult = excuteResult;
	}
	
}
