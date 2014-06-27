package com.hundsun.network.dbPlus.service.impl;

import java.io.PrintWriter;
import java.util.Map;

import com.hundsun.network.dbPlus.result.DbPlusAppConfigResult;
import com.hundsun.network.dbPlus.result.DbPlusBaseResult;
import com.hundsun.network.dbPlus.service.DbPlusBaseService;
import com.hundsun.network.dbPlus.service.DbPlusService;
import com.hundsun.network.dbPlus.web.servlet.DbPlusDispatcher;
import com.hundsun.network.dbPlus.web.servlet.DbPlusWriter;

/**
 * 应用功能开关控制业务类
 * @author yueliang 2013-3-5
 */
public class DbPlusAppConfigServiceImpl extends DbPlusBaseService implements DbPlusService<DbPlusAppConfigResult> {

	public DbPlusBaseResult service(Map<String, String[]> paraMap, Map<String, String> config) {
		
		DbPlusAppConfigResult result = new DbPlusAppConfigResult();
		
		String appFc = ""; // 关闭（或开启）功能的名称
		String[] appArrs = paraMap.get("appConfig");
		if(appArrs != null && appArrs.length > 0 && appArrs[0].trim().length() > 0){ // 请求里有无开关动作
			appFc = appArrs[0];
			// 设置 关闭（或开启）
			DbPlusDispatcher.setDbPlusAppConfig(appFc, !DbPlusDispatcher.getDbPlusAppConfig(appFc));
		}
		result.setAppConfigMap(DbPlusDispatcher.getAllDbPlusAppConfig());
			
		return result;
	}

	public void print(PrintWriter writer, DbPlusAppConfigResult result, Map<String, String> config) {
		DbPlusWriter.printAppConfigHTML(writer, super.baseRequest, result, config); // 打印页面
	}
	
}
