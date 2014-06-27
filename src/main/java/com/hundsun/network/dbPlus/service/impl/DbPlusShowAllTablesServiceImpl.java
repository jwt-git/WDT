package com.hundsun.network.dbPlus.service.impl;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.hundsun.network.dbPlus.enums.EnumDbPlusError;
import com.hundsun.network.dbPlus.result.DbPlusShowAllTablesResult;
import com.hundsun.network.dbPlus.service.DbPlusBaseService;
import com.hundsun.network.dbPlus.service.DbPlusService;
import com.hundsun.network.dbPlus.web.servlet.DbPlusWriter;

/**
 * 浏览所有表 业务类
 * @author yueliang 2013-3-4
 */
public class DbPlusShowAllTablesServiceImpl extends DbPlusBaseService implements DbPlusService<DbPlusShowAllTablesResult> {
	
	public DbPlusShowAllTablesResult service(Map<String, String[]> paraMap, Map<String, String> config) {
		if(null == paraMap) {
			return null;
		}
		DbPlusShowAllTablesResult result = new DbPlusShowAllTablesResult();
		Connection connection = super.getConnection();
		Statement stmt;
		try {
			stmt = connection.createStatement();
			connection.commit();
			
			ResultSet resultSet = stmt.executeQuery("select table_name from user_tables order by table_name");
			List<String> tableList = new ArrayList<String>();
			while(resultSet.next()) {    
				tableList.add(resultSet.getString("table_name"));
			}
			result.setTableList(tableList);
			connection.commit();
			stmt.close();
		} catch (SQLException e) {
			result.setErrorNOInfo(EnumDbPlusError.SHOW_TABLES_ERROR.getValue(), e.getMessage());
			e.printStackTrace();
		}
		stmt = null;
		return result;
	}
	
	public void print(PrintWriter writer, DbPlusShowAllTablesResult result, Map<String, String> config) {
		DbPlusWriter.printAllTablesHTML(writer, super.baseRequest, result, config); // 打印页面
	}
	
}
