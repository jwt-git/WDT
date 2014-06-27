package com.hundsun.network.dbPlus.service.impl;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import oracle.sql.TIMESTAMP;

import com.hundsun.network.dbPlus.enums.EnumDbPlusError;
import com.hundsun.network.dbPlus.result.DbPlusTableInfoResult;
import com.hundsun.network.dbPlus.service.DbPlusBaseService;
import com.hundsun.network.dbPlus.service.DbPlusService;
import com.hundsun.network.dbPlus.web.servlet.DbPlusWriter;

/**
 * 查看表信息 业务类
 * @author yueliang 2013-3-27
 */
public class DbPlusTableInfoServiceImpl extends DbPlusBaseService implements DbPlusService<DbPlusTableInfoResult> {
	
	public DbPlusTableInfoResult service(Map<String, String[]> paraMap, Map<String, String> config) {
		if(null == paraMap) {
			return null;
		}
		String tableName; // 目标表名
		String[] tableNames = paraMap.get("tableName");
		if(tableNames != null && tableNames.length > 0 && tableNames[0].trim().length() > 0){
			tableName = tableNames[0];
		} else {
			return null;
		}
		DbPlusTableInfoResult result = new DbPlusTableInfoResult();
		result.setTableName(tableName);
		Connection connection = super.getConnection();
		Statement stmt;
		try {
			stmt = connection.createStatement();
			connection.commit();
			String[] infos = paraMap.get("info");
			// 参数info是DataInfo说明查的是表数据
			if(infos != null && infos.length > 0 && "DataInfo".equalsIgnoreCase(infos[0].trim())){
				ResultSet resultSet = stmt.executeQuery("select * from " + tableName);
				convertDataInfo(resultSet, result); // 静态化查询结果 - 数据信息
				connection.commit();
				resultSet.close();
			}
			// 参数info无效说明查的是列信息
			else {
				DatabaseMetaData metaData = connection.getMetaData();
				// 获得主键map，参数：1数据库实例、2模式、3表名称
				ResultSet pkSet = metaData.getPrimaryKeys(connection.getCatalog(), null, tableName);
				Map<String, String> pkMap = new HashMap<String, String>();
				while(pkSet.next()) { 
					pkMap.put(pkSet.getString("COLUMN_NAME"), pkSet.getString("KEY_SEQ"));
				}
				// 获得所有列信息的set，参数：1数据库实例、2模式、3表名称、4列类型
				ResultSet resultSet = metaData.getColumns(connection.getCatalog(), metaData.getUserName(), tableName, null);
//				ResultSet resultSet = metaData.getColumns(null, config.get(DbPlusDict.Username), tableName, null);
				convertTableInfo(pkMap, resultSet, result);// 静态化查询结果 - 列信息
			}
			
			stmt.close();
		} catch (SQLException e) {
			result.setErrorNOInfo(EnumDbPlusError.SHOW_TABLE_INFO.getValue(), e.getMessage());
			e.printStackTrace();
		}
		stmt = null;
		super.closeConnection(); // 全部执行完毕后，关闭连接
		return result;
	}
	
	public void print(PrintWriter writer, DbPlusTableInfoResult result, Map<String, String> config) {
		DbPlusWriter.printTableInfoHTML(writer, super.baseRequest, result, config); // 打印页面
	}
	
	/**
	 * 静态化查询结果 - 数据信息
	 * @param result resultSet
	 * @throws SQLException 
	 */
	private void convertDataInfo(ResultSet resultSet,
			DbPlusTableInfoResult result) throws SQLException {
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
		result.setDataInfo(resultList);
	}
	
	/**
	 * 静态化查询结果 - 表信息
	 * @param pkMap resultSet resultSet
	 * @throws SQLException 
	 */
	private void convertTableInfo(Map<String, String> pkMap,
			ResultSet resultSet, DbPlusTableInfoResult result) throws SQLException {
		List<List<String>> resultList = new ArrayList<List<String>>();
		
		List<String> thead = new ArrayList<String>();
		thead.add("Column Name"); // 列名
		thead.add("Date Type"); // 类型
		thead.add("Nullable"); // 是否可为空
		thead.add("Date Default"); // 默认值
		thead.add("Ordinal Position"); // 列索引
		thead.add("Primary Key"); // 是否主键
		thead.add("Comments"); // 描述
		resultList.add(thead);
		
		while(resultSet.next()) {
			List<String> tdata = new ArrayList<String>();
			String colSizeDes = ""; // 字段大小、或精度的描述
			// DATA_TYPE返回字段类型代号：3-NUMBER、12-VARCHAR2 ……
			String dataType = resultSet.getString("DATA_TYPE");
			if("12".equals(dataType)) {
				colSizeDes = "(" + resultSet.getString("COLUMN_SIZE") + ")";
			} else if("3".equals(dataType)) {
				colSizeDes = "(" + resultSet.getString("COLUMN_SIZE") + "," + resultSet.getString("DECIMAL_DIGITS") + ")";
			}
			String colName = resultSet.getString("COLUMN_NAME");
			tdata.add(colName); // 列名
			tdata.add(resultSet.getString("TYPE_NAME") + colSizeDes); // 类型  + 精度描述
			tdata.add(resultSet.getString("NULLABLE")); // 是否可为空
			tdata.add(resultSet.getString("COLUMN_DEF")); // 默认值
			tdata.add(resultSet.getString("ORDINAL_POSITION")); // 列索引
			String isPk = "null"; // 是否主键
			if(pkMap.get(colName) != null) { // 其实就是KEY_SEQ
				isPk = "1";
			}
			tdata.add(isPk);
			
//			tdata.add(resultSet.getString(12));
			tdata.add(resultSet.getString("REMARKS")); // 描述
			resultList.add(tdata);
		}

		result.setTableInfo(resultList);
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
