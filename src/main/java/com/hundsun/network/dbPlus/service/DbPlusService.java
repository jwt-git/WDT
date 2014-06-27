package com.hundsun.network.dbPlus.service;

import java.io.PrintWriter;
import java.util.Map;

import com.hundsun.network.dbPlus.result.DbPlusBaseResult;

/**
 * Service接口
 * 先判定有无权限，如果有权限connection放入service里
 * @author yueliang 2013-2-25
 */
public interface DbPlusService<T extends DbPlusBaseResult> {

	/**
	 * 实际业务处理，需子类实现
	 * @param paraMap config 
	 * @return DbPlusBaseResult
	 */
	public DbPlusBaseResult service(Map<String, String[]> paraMap, Map<String, String> config);
	
	/**
	 * 结果打印，需子类实现
	 * @param writer result
	 */
	public void print(PrintWriter writer, T result, Map<String, String> config);
	
	/**
	 * 判断有无权限
	 * @param paraMap
	 * @return DbPlusBaseResult
	 */
	public DbPlusBaseResult hasPermission(Map<String, String[]> paraMap);
	
	/**
	 * 无权限时的打印登录页面
	 * @param writer result
	 */
	public void printLoginHTML(PrintWriter writer, DbPlusBaseResult result,
			Boolean useDefaultConnectionConfig, Map<String, String> config);
	
	/**
	 * 关闭数据库连接
	 * @param writer result
	 */
	public void closeConnection();
	
}
