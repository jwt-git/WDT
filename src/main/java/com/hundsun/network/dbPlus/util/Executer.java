package com.hundsun.network.dbPlus.util;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import com.hundsun.network.dbPlus.enums.EnumDbPlusError;
import com.hundsun.network.dbPlus.result.DbPlusExcuteResult;

/**
 * SQL语句实际执行抽象父类
 * @author yueliang 2013-2-26
 */
public abstract class Executer {
	
	protected abstract DbPlusExcuteResult execute(Statement stmt, String sql) throws SQLException ;
	
	public DbPlusExcuteResult doExecute(Connection con, String sql) {
		DbPlusExcuteResult result = new DbPlusExcuteResult();
		try {
			Statement stmt = con.createStatement();
			con.commit();
			result = execute(stmt, sql); // 根据不同SQL语句，执行不同操作
			con.commit();
			stmt.close();
			stmt = null;
		} catch (SQLException e) {
			result.setErrorNOInfo(EnumDbPlusError.EXECUTE_SQL_ERROR.getValue(), e.getMessage());
			e.printStackTrace();
		}
		return result;
	}
}
