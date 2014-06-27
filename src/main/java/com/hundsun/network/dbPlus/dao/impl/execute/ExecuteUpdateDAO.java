package com.hundsun.network.dbPlus.dao.impl.execute;

import java.sql.SQLException;
import java.sql.Statement;

import com.hundsun.network.dbPlus.result.DbPlusExcuteResult;

/**
 * SQL语句Update实际执行类
 * @author yueliang 2013-2-26
 */
public class ExecuteUpdateDAO {
	
	/**
	 * INSERT、UPDATE、DELETE、CREATE、DROP、ALTER操作
	 * @param Statement SQL
	 * @throws SQLException 
	 */
	public DbPlusExcuteResult executeUpdate(Statement stmt, String sql) throws SQLException {
		DbPlusExcuteResult result = new DbPlusExcuteResult();
		int rs = stmt.executeUpdate(sql);
		result.setSum(rs);
		return result;
	}
	
}
