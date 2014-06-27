package com.hundsun.network.dbPlus.result;

import java.sql.ResultSet;
import java.util.List;

/**
 * ExcuteSQL返回
 */
public class DbPlusExcuteResult extends DbPlusBaseResult {

	private int 						sum; // 操作影响的记录条数
	private ResultSet 					resultSet; // 查询结果集(动态)
	private List<List<String>> 			resultList; // 查询结果集(静态)
	
	public int getSum() {
		return sum;
	}
	
	public void setSum(int sum) {
		this.sum = sum;
	}
	
	public ResultSet getResultSet() {
		return resultSet;
	}
	
	public void setResultSet(ResultSet resultSet) {
		this.resultSet = resultSet;
	}
	
	public List<List<String>> getResultList() {
		return resultList;
	}
	
	public void setResultList(List<List<String>> resultList) {
		this.resultList = resultList;
	}

}
