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
import com.hundsun.network.dbPlus.result.DbPlusExcuteResult;
import com.hundsun.network.dbPlus.result.DbPlusLoginResult;
import com.hundsun.network.dbPlus.result.DbPlusShowAllTablesResult;
import com.hundsun.network.dbPlus.service.DbPlusBaseService;
import com.hundsun.network.dbPlus.service.DbPlusService;
import com.hundsun.network.dbPlus.web.servlet.DbPlusWriter;

/**
 * 登录、注销等操作业务类
 * @author yueliang 2013-2-25
 */
public class DbPlusLoginServiceImpl extends DbPlusBaseService implements DbPlusService<DbPlusLoginResult> {

	public DbPlusLoginResult service(Map<String, String[]> paraMap, Map<String, String> config) {
		DbPlusLoginResult result = new DbPlusLoginResult();
		
		// 初始化SQL输入框、执行结果框信息
		DbPlusExcuteResult excuteResult = new DbPlusExcuteResult();
		excuteResult.setErrorNOInfo(EnumDbPlusError.EXECUTE_SQL_NULL.getValue(), EnumDbPlusError.EXECUTE_SQL_NULL.getInfo());
		result.setExcuteResult(excuteResult);
		
		// 初始化表信息
		DbPlusShowAllTablesResult showAllTablesResult = new DbPlusShowAllTablesResult();
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
			showAllTablesResult.setTableList(tableList);
			connection.commit();
			stmt.close();
		} catch (SQLException e) {
			result.setErrorNOInfo(EnumDbPlusError.SHOW_TABLES_ERROR.getValue(), e.getMessage());
			e.printStackTrace();
		}
		stmt = null;
		result.setShowAllTablesResult(showAllTablesResult);
		
		return result;
	}

	public void print(PrintWriter writer, DbPlusLoginResult result, Map<String, String> config) {
		DbPlusWriter.printLoginSuccessHTML(writer, super.baseRequest, result, config); // 打印页面
	}
	
}
