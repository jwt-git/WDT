package com.hundsun.network.dbPlus.web.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hundsun.network.dbPlus.enums.DbPlusDict;
import com.hundsun.network.dbPlus.enums.DbPlusRequestUrl;
import com.hundsun.network.dbPlus.enums.EnumDbPlusError;
import com.hundsun.network.dbPlus.result.DbPlusAppConfigResult;
import com.hundsun.network.dbPlus.result.DbPlusBaseResult;
import com.hundsun.network.dbPlus.result.DbPlusExcuteResult;
import com.hundsun.network.dbPlus.result.DbPlusLoginResult;
import com.hundsun.network.dbPlus.result.DbPlusShowAllTablesResult;
import com.hundsun.network.dbPlus.result.DbPlusTableInfoResult;
import com.hundsun.network.dbPlus.service.DbPlusService;
import com.hundsun.network.dbPlus.service.impl.DbPlusAppConfigServiceImpl;
import com.hundsun.network.dbPlus.service.impl.DbPlusCloseServiceImpl;
import com.hundsun.network.dbPlus.service.impl.DbPlusExecuteSqlServiceImpl;
import com.hundsun.network.dbPlus.service.impl.DbPlusLoginServiceImpl;
import com.hundsun.network.dbPlus.service.impl.DbPlusShowAllTablesServiceImpl;
import com.hundsun.network.dbPlus.service.impl.DbPlusTableInfoServiceImpl;

/**
 * 数据库管理系统-前置请求分发
 * @author yueliang 2013-2-25
 */
public class DbPlusDispatcher extends HttpServlet {

	private static final long serialVersionUID = 1860100630026075997L;
	
	/**
	 * service容器
	 */
	private static Map<String, DbPlusService<? extends DbPlusBaseResult>> serviceMap = new HashMap<String, DbPlusService<? extends DbPlusBaseResult>>();
	
	/**
	 * 应用功能配置
	 */
	private static Map<String, Boolean> dbPlusAppConfig = new HashMap<String, Boolean>();
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	} 
	
	/**
	 * 所有请求入口POST方法：DbPlusRequestUrlConfig里看请求url配置
	 * @param HttpServletRequest, HttpServletResponse
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Map<String, String> cfg = getConnectionDefaultConfig();
		
		request.setCharacterEncoding(cfg.get(DbPlusDict.Charset));
		response.setContentType("text/html; charset=" + cfg.get(DbPlusDict.Charset));
		
		String path = request.getServletPath(); // DbPlusRequestUrl里看请求url目录
		
		// 判断系统功能是否开启
		if(!dbPlusAppConfig.get(DbPlusDict.GlobalPermission)) {
			DbPlusWriter.printSingleMsg(response.getWriter(), 
					EnumDbPlusError.APP_FORBIDDEN.getInfo(), cfg);
			return;
		}
		
		DbPlusService hsdbService = getService(path);
		
		// 404错误
		if(null == hsdbService) {
			DbPlusWriter.printSingleMsg(response.getWriter(), 
					EnumDbPlusError.URL_ERROR.getInfo(), cfg);
			return;
		}
		
		// 请求封装  统一用map
		Map<String, String[]> paraMap = request.getParameterMap();
		
		// 判断有无权限（含登录操作）
		DbPlusBaseResult permissionResult = hsdbService.hasPermission(paraMap);
		if(permissionResult != null && permissionResult.correct()){
			try {
				DbPlusBaseResult result = hsdbService.service(paraMap, cfg);
//				result.setUrl(path);
				hsdbService.print(response.getWriter(), result, cfg); // 打印页面
			} catch(Exception e) {
				if(cfg.get(DbPlusDict.Log) != null){
					System.out.println(e.getStackTrace());
				}
			} finally {
				hsdbService.closeConnection(); // 执行完毕后，关闭可能的数据库连接
			}
		} else {
			hsdbService.printLoginHTML(response.getWriter(), permissionResult, 
					dbPlusAppConfig.get(DbPlusDict.UseDefaultConnectionConfig), cfg);
		}
	}
	
	
	public void init() throws ServletException { 
		// -------- 初始化service容器 -------- //
		DbPlusService<DbPlusLoginResult> loginService = new DbPlusLoginServiceImpl();
		DbPlusService<DbPlusExcuteResult> executeSqlService = new DbPlusExecuteSqlServiceImpl();
		DbPlusService<DbPlusShowAllTablesResult> showAllTablesService = new DbPlusShowAllTablesServiceImpl();
		DbPlusService<DbPlusAppConfigResult> permissionService = new DbPlusAppConfigServiceImpl();
		DbPlusService<DbPlusTableInfoResult> tableInfoService = new DbPlusTableInfoServiceImpl();
		DbPlusService<DbPlusBaseResult> closeService = new DbPlusCloseServiceImpl();
		serviceMap.put(DbPlusRequestUrl.LoginPath, loginService);
		serviceMap.put(DbPlusRequestUrl.ExecuteSQLPath, executeSqlService);
		serviceMap.put(DbPlusRequestUrl.ShowAllTables, showAllTablesService);
		serviceMap.put(DbPlusRequestUrl.AppConfig, permissionService);
		serviceMap.put(DbPlusRequestUrl.TableInfo, tableInfoService);
		serviceMap.put(DbPlusRequestUrl.CLOSE, closeService);
		
		// -------- 初始化各应用功能配置 -------- //
		// 全局功能开关
		Boolean GlobalPermission = DbPlusDict.TRUE.equalsIgnoreCase(this.getInitParameter(DbPlusDict.GlobalPermission));
		// 是否使用默认配置的JDBC类名、连接地址、用户名、密码 值
		Boolean UseDefaultConnectionConfig = DbPlusDict.TRUE.equalsIgnoreCase(this.getInitParameter(DbPlusDict.UseDefaultConnectionConfig));
		// 其它功能配置
		Boolean SELECT = DbPlusDict.TRUE.equalsIgnoreCase(this.getInitParameter(DbPlusDict.SELECT));
		Boolean UPDATE = DbPlusDict.TRUE.equalsIgnoreCase(this.getInitParameter(DbPlusDict.UPDATE));
		Boolean INSERT = DbPlusDict.TRUE.equalsIgnoreCase(this.getInitParameter(DbPlusDict.INSERT));
		Boolean DELETE = DbPlusDict.TRUE.equalsIgnoreCase(this.getInitParameter(DbPlusDict.DELETE));
		Boolean CREATE = DbPlusDict.TRUE.equalsIgnoreCase(this.getInitParameter(DbPlusDict.DELETE));
		Boolean DROP = DbPlusDict.TRUE.equalsIgnoreCase(this.getInitParameter(DbPlusDict.DROP));
		Boolean ALTER = DbPlusDict.TRUE.equalsIgnoreCase(this.getInitParameter(DbPlusDict.ALTER));
		dbPlusAppConfig.put(DbPlusDict.GlobalPermission, GlobalPermission);
		dbPlusAppConfig.put(DbPlusDict.UseDefaultConnectionConfig, UseDefaultConnectionConfig);
		dbPlusAppConfig.put(DbPlusDict.SELECT, SELECT);
		dbPlusAppConfig.put(DbPlusDict.UPDATE, UPDATE);
		dbPlusAppConfig.put(DbPlusDict.INSERT, INSERT);
		dbPlusAppConfig.put(DbPlusDict.DELETE, DELETE);
		dbPlusAppConfig.put(DbPlusDict.CREATE, CREATE);
		dbPlusAppConfig.put(DbPlusDict.DROP, DROP);
		dbPlusAppConfig.put(DbPlusDict.ALTER, ALTER);
	} 
	
	/**
	 * 添加新的service，可供外部系统使用
	 */
	public static void addService(String key, DbPlusService<? extends DbPlusBaseResult> newService) {
		serviceMap.put(key, newService);
	}

	/**
	 * 获取service实例，可供外部系统使用
	 */
	public static DbPlusService<? extends DbPlusBaseResult> getService(String key) {
		return serviceMap.get(key);
	}
	
	/**
	 * 添加新的dbPlusAppConfig，可供外部系统使用
	 */
	public static void addDbPlusAppConfig(String appName, Boolean aBoolean) {
		dbPlusAppConfig.put(appName, aBoolean);
	}
	
	/**
	 * 更改某项dbPlusAppConfig配置，可供外部系统使用
	 */
	public static void setDbPlusAppConfig(String appName, Boolean aBoolean) {
		dbPlusAppConfig.put(appName, aBoolean);
	}

	/**
	 * 获取dDbPlusAppConfig某一功能是否可用，可供外部系统使用
	 */
	public static Boolean getDbPlusAppConfig(String appName) {
		return dbPlusAppConfig.get(appName);
	}
	
	/**
	 * 获取dDbPlusAppConfig所有功能Map，可供外部系统使用
	 */
	public static Map<String, Boolean> getAllDbPlusAppConfig() {
		return dbPlusAppConfig;
	}
	
	/**
	 * 开启全局功能，可供外部系统使用
	 */
	public static void openGlobalPermission() {
		dbPlusAppConfig.put(DbPlusDict.GlobalPermission, true);
	}
	/**
	 * 关闭全局功能，可供外部系统使用
	 */
	public static void closeGlobalPermission() {
		dbPlusAppConfig.put(DbPlusDict.GlobalPermission, false);
	}
	
	/**
	 * 开启可使用默认配置的JDBC类名、连接地址、用户名、密码 值，可供外部系统使用
	 */
	public static void openDefaultConnectionConfig() {
		dbPlusAppConfig.put(DbPlusDict.UseDefaultConnectionConfig, true);
	}
	/**
	 * 关闭不使用默认配置的JDBC类名、连接地址、用户名、密码 值，可供外部系统使用
	 */
	public static void closeDefaultConnectionConfig() {
		dbPlusAppConfig.put(DbPlusDict.UseDefaultConnectionConfig, false);
	}
	
	/**
	 * 开启SQL的SELECT关键字开关，可供外部系统使用
	 */
	public static void openSelectSQL() {
		dbPlusAppConfig.put(DbPlusDict.SELECT, true);
	}
	/**
	 * 关闭SQL的SELECT关键字开关，可供外部系统使用
	 */
	public static void closeSelectSQL() {
		dbPlusAppConfig.put(DbPlusDict.SELECT, false);
	}
	/**
	 * 开启SQL的UPDATE关键字开关，可供外部系统使用
	 */
	public static void openUpdateSQL() {
		dbPlusAppConfig.put(DbPlusDict.UPDATE, true);
	}
	/**
	 * 关闭SQL的UPDATE关键字开关，可供外部系统使用
	 */
	public static void closeUpdateSQL() {
		dbPlusAppConfig.put(DbPlusDict.UPDATE, false);
	}
	/**
	 * 开启SQL的INSERT关键字开关，可供外部系统使用
	 */
	public static void openInsertSQL() {
		dbPlusAppConfig.put(DbPlusDict.INSERT, true);
	}
	/**
	 * 关闭SQL的INSERT关键字开关，可供外部系统使用
	 */
	public static void closeInsertSQL() {
		dbPlusAppConfig.put(DbPlusDict.INSERT, false);
	}
	/**
	 * 开启SQL的DELETE关键字开关，可供外部系统使用
	 */
	public static void openDeleteSQL() {
		dbPlusAppConfig.put(DbPlusDict.DELETE, true);
	}
	/**
	 * 关闭SQL的DELETE关键字开关，可供外部系统使用
	 */
	public static void closeDeleteSQL() {
		dbPlusAppConfig.put(DbPlusDict.DELETE, false);
	}
	/**
	 * 开启SQL的CREATE关键字开关，可供外部系统使用
	 */
	public static void openCreateSQL() {
		dbPlusAppConfig.put(DbPlusDict.CREATE, true);
	}
	/**
	 * 关闭SQL的CREATE关键字开关，可供外部系统使用
	 */
	public static void closeCreateSQL() {
		dbPlusAppConfig.put(DbPlusDict.CREATE, false);
	}
	/**
	 * 开启SQL的DROP关键字开关，可供外部系统使用
	 */
	public static void openDropSQL() {
		dbPlusAppConfig.put(DbPlusDict.DROP, true);
	}
	/**
	 * 关闭SQL的DROP关键字开关，可供外部系统使用
	 */
	public static void closeDropSQL() {
		dbPlusAppConfig.put(DbPlusDict.DROP, false);
	}
	/**
	 * 开启SQL的ALTER关键字开关，可供外部系统使用
	 */
	public static void openAlterSQL() {
		dbPlusAppConfig.put(DbPlusDict.ALTER, true);
	}
	/**
	 * 关闭SQL的ALTER关键字开关，可供外部系统使用
	 */
	public static void closeAlterSQL() {
		dbPlusAppConfig.put(DbPlusDict.ALTER, false);
	}
	
	/**
	 * 获取默认设置的map格式
	 */
	public Map<String, String> getConnectionDefaultConfig() {
		Map<String, String> map = new HashMap<String, String>();
		String charset = this.getInitParameter(DbPlusDict.Charset) == null? "utf-8" : this.getInitParameter(DbPlusDict.Charset);
		String driverClassName = this.getInitParameter(DbPlusDict.DriverClassName) == null? "" : this.getInitParameter(DbPlusDict.DriverClassName);
		String url = this.getInitParameter(DbPlusDict.Url) == null? "" : this.getInitParameter(DbPlusDict.Url);
		String username = this.getInitParameter(DbPlusDict.Username) == null? "" : this.getInitParameter(DbPlusDict.Username);
//		String password = this.getInitParameter(DbPlusDict.Password) == null? "" : this.getInitParameter(DbPlusDict.Password);
		map.put(DbPlusDict.Charset, charset);
		map.put(DbPlusDict.DriverClassName, driverClassName);
		map.put(DbPlusDict.Url, url);
		map.put(DbPlusDict.Username, username);
//		map.put(DbPlusDict.Password, password);
		
		if(DbPlusDict.TRUE.equalsIgnoreCase(this.getInitParameter(DbPlusDict.Log))) {
			map.put(DbPlusDict.Log, this.getInitParameter(DbPlusDict.Log));
		}
		return map;
	}
	
}
