package com.hundsun.network.dbPlus.service.impl;

import java.io.PrintWriter;
import java.util.Map;

import com.hundsun.network.dbPlus.enums.DbPlusDict;
import com.hundsun.network.dbPlus.result.DbPlusBaseResult;
import com.hundsun.network.dbPlus.service.DbPlusBaseService;
import com.hundsun.network.dbPlus.service.DbPlusService;
import com.hundsun.network.dbPlus.web.servlet.DbPlusDispatcher;
import com.hundsun.network.dbPlus.web.servlet.DbPlusWriter;

/**
 * 关闭功能并退出
 * @author yueliang 2013-4-9
 */
public class DbPlusCloseServiceImpl extends DbPlusBaseService implements DbPlusService<DbPlusBaseResult> {

	public DbPlusBaseResult service(Map<String, String[]> paraMap, Map<String, String> config) {
		DbPlusDispatcher.setDbPlusAppConfig(DbPlusDict.GlobalPermission, false); // 关闭全局功能
		return new DbPlusBaseResult();
	}

	public void print(PrintWriter writer, DbPlusBaseResult result, Map<String, String> config) {
		DbPlusWriter.printSingleMsg(writer, "功能已经关闭", config); // 打印页面
	}
	
}
