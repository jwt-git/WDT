package com.hundsun.network.dbPlus.dao.impl.execute;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import oracle.sql.TIMESTAMP;

import com.hundsun.network.dbPlus.result.DbPlusExcuteResult;

/**
 * SQL语句Query实际执行类
 * @author yueliang 2013-2-26
 */
public class ExecuteQueryDAO {
	
	/**
	 * SELECT操作
	 * @param Statement SQL
	 * @throws SQLException 
	 */
	public DbPlusExcuteResult executeQuery(Statement stmt, String sql) throws SQLException {
		DbPlusExcuteResult result = new DbPlusExcuteResult();
		ResultSet resultSet = stmt.executeQuery(sql);
		// 静态化查询结果 (Map是无序的，换成List)
//		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		List<List<String>> resultList = new ArrayList<List<String>>();
		ResultSetMetaData meta = resultSet.getMetaData();
		int colCount = meta.getColumnCount();
		List<String> columnNameList = new ArrayList<String>();
		for(int i = 1; i <= colCount; i ++){
			String columnName = meta.getColumnName(i); // 列名
			columnNameList.add(columnName);
		}
		resultList.add(columnNameList);
		while(resultSet.next()) {
			List<String> columnList = new ArrayList<String>();
			for(int i = 1; i <= colCount; i ++){
				String columnClassName = meta.getColumnClassName(i); // 各字段类型名
				Object columnValue = resultSet.getObject(i); // 各列字段值
				columnList.add(columnToStr(columnClassName, columnValue));
			}
			resultList.add(columnList);
		}
		result.setResultList(resultList);
		resultSet.close();
		return result;
	}
	
	/**
	 * 根据字段值的类型名字，在ResultSet里取得该值的实际类型的toString()
	 * @param columnClassName resultSet
	 * @throws SQLException 
	 */
	private String columnToStr(String columnClassName, Object columnValue) throws SQLException {
		if(null == columnClassName || null == columnValue) return "";
		// 除oracle.sql.TIMESTAMP日期格式字段外，其它类型都有默认的toString()方法，因此暂不用处理
		if("oracle.sql.TIMESTAMP".equals(columnClassName)){
			TIMESTAMP timeStamp = (TIMESTAMP)columnValue;
			return timeStamp.timestampValue().toString(); // 暂且把TIMESTAMP转成Timestamp
		}
		return columnValue.toString();
	}
	
}
