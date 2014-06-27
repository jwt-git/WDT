package com.hundsun.network.dbPlus.service.impl;

import java.io.PrintWriter;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.hundsun.network.dbPlus.dao.impl.execute.ExecuteQueryDAO;
import com.hundsun.network.dbPlus.dao.impl.execute.ExecuteUpdateDAO;
import com.hundsun.network.dbPlus.enums.DbPlusDict;
import com.hundsun.network.dbPlus.enums.EnumDbPlusError;
import com.hundsun.network.dbPlus.query.DbPlusExecuteSqlRequest;
import com.hundsun.network.dbPlus.result.DbPlusExcuteResult;
import com.hundsun.network.dbPlus.service.DbPlusBaseService;
import com.hundsun.network.dbPlus.service.DbPlusService;
import com.hundsun.network.dbPlus.util.Executer;
import com.hundsun.network.dbPlus.web.servlet.DbPlusDispatcher;
import com.hundsun.network.dbPlus.web.servlet.DbPlusWriter;

/**
 * SQL语句执行业务类
 * @author yueliang 2013-2-25
 */
public class DbPlusExecuteSqlServiceImpl extends DbPlusBaseService implements DbPlusService<DbPlusExcuteResult> {
	
	private DbPlusExecuteSqlRequest executeSqlRequest;

	public DbPlusExcuteResult service(Map<String, String[]> paraMap, Map<String, String> config) {
		if(null == paraMap) {
			return null;
		}
		executeSqlRequest = convertExecuteSqlRequest(paraMap); // 封装请求
		DbPlusExcuteResult result = new DbPlusExcuteResult(); // 操作结果
		
		// 全部请求SQL
		if(executeSqlRequest != null){
			// 逐条执行选中的SQL列表
			for(String sql : executeSqlRequest.getExecuteSql()){
				if(config.get(DbPlusDict.Log) != null){
					System.out.println(" >>>>>> ExecuteSql:" + sql);
				}
				int firstIndex = sql.indexOf(" ");
				String key = firstIndex > 0? sql.substring(0, firstIndex) : sql;
				
				// 根据DbPlusAppConfig配置，判断key的功能权限是否被启用
				if(!DbPlusDispatcher.getDbPlusAppConfig(key.toUpperCase())){
					result.setErrorNOInfo(EnumDbPlusError.APP_FORBIDDEN.getValue(), 
							key.toUpperCase() + EnumDbPlusError.APP_FORBIDDEN.getInfo());
					return result;
				}
				
				Executer executer = getExecuterInstance(key);// 取SQL语句的第一个词
				result = executer.doExecute(super.getConnection(), sql);
			}
		} else { // 无输入SQL时
			result.setErrorNOInfo(EnumDbPlusError.EXECUTE_SQL_NULL.getValue(), EnumDbPlusError.EXECUTE_SQL_NULL.getInfo());
		}
		return result;
	}
	
	/**
	 * 请求封装
	 * @param map
	 * @return DbPlusExecuteSqlRequest
	 */
	private DbPlusExecuteSqlRequest convertExecuteSqlRequest(Map<String, String[]> paraMap) {
		DbPlusExecuteSqlRequest executeSqlRequest = new DbPlusExecuteSqlRequest();
		
		executeSqlRequest.setDriverClassName(super.baseRequest.getDriverClassName());
		executeSqlRequest.setUrl(super.baseRequest.getUrl());
		executeSqlRequest.setUsername(super.baseRequest.getUsername());
		executeSqlRequest.setPassword(super.baseRequest.getPassword());
		
		// 全部请求SQL
		String[] sqlArrs = paraMap.get("sql");
		if(sqlArrs != null && sqlArrs.length > 0 && sqlArrs[0].trim().length() > 0){
			String requestSql = sqlArrs[0];
			
			// 选中的起始索引
			String[] beginIndexArrs = paraMap.get("beginIndex");
			int beginIndex = 0;
			if(beginIndexArrs != null && beginIndexArrs.length > 0 && beginIndexArrs[0].trim().length() > 0){
				beginIndex = Integer.valueOf(beginIndexArrs[0]);
			}
			
			// 选中的结束索引
			String[] endIndexArrs = paraMap.get("endIndex");
			int endIndex = 0;
			if(endIndexArrs != null && endIndexArrs.length > 0 && endIndexArrs[0].trim().length() > 0){
				endIndex = Integer.valueOf(endIndexArrs[0]);
			}
			
			// 选中的SQL列表
			List<String> executeSql = new ArrayList<String>();
			if(endIndex > beginIndex){ // 有选中的需执行SQL语句
				for(String sql : requestSql.substring(beginIndex, endIndex).split(";")){
					String eSql = sql.trim(); // 唯一的一次trim操作，为防止丢失选择的空格
					if(eSql.length() > 0){
						executeSql.add(eSql);
					}
				}
			} else {
				for(String sql : requestSql.split(";")){
					String eSql = sql.trim();
					if(eSql.length() > 0){
						executeSql.add(eSql);
					}
				}
			}
			
			executeSqlRequest.setRequestSql(requestSql);
			executeSqlRequest.setExecuteSql(executeSql);
			executeSqlRequest.setBeginIndex(beginIndex);
			executeSqlRequest.setEndIndex(endIndex);
		}
		
		return executeSqlRequest;
	}
	
	/**
	 * SQL语句执行类工厂方法(SQL的语句分析、适配)
	 * @param key(取SQL语句的第一个词)
	 */
	private Executer getExecuterInstance(String key) {
		// SELECT操作
		if("SELECT".equalsIgnoreCase(key)) {
			return new Executer(){
				public DbPlusExcuteResult execute(Statement stmt, String sql) throws SQLException {
					ExecuteQueryDAO executeQueryDAO = new ExecuteQueryDAO();
					return executeQueryDAO.executeQuery(stmt, sql);
				}
			};
		}
		// INSERT、UPDATE、DELETE、CREATE、DROP、ALTER操作
		else if("INSERT".equalsIgnoreCase(key)
					|| "UPDATE".equalsIgnoreCase(key)
					|| "DELETE".equalsIgnoreCase(key)
					|| "CREATE".equalsIgnoreCase(key)
					|| "DROP".equalsIgnoreCase(key)
					|| "ALTER".equalsIgnoreCase(key)) {
			return new Executer(){
				public DbPlusExcuteResult execute(Statement stmt, String sql) throws SQLException {
					ExecuteUpdateDAO executeUpdateDAO = new ExecuteUpdateDAO();
					return executeUpdateDAO.executeUpdate(stmt, sql);
				}
			};
		}
		
		// TODO 如有其它SQL操作可在这里添加实现
		
		// 未知的操作
		else {
			return new Executer(){
				public DbPlusExcuteResult execute(Statement stmt, String sql) throws SQLException {
					throw new SQLException("错误：未知的SQL语句");
				}
			};
		}
	}

	public void print(PrintWriter writer, DbPlusExcuteResult result, Map<String, String> config) {
		DbPlusWriter.printExecuteSqlHTML(writer, executeSqlRequest, result, config); // 打印页面
	}
	
}
