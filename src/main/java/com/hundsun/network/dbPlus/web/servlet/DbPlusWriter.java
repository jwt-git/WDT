package com.hundsun.network.dbPlus.web.servlet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hundsun.network.dbPlus.enums.DbPlusDict;
import com.hundsun.network.dbPlus.enums.DbPlusRequestUrl;
import com.hundsun.network.dbPlus.query.DbPlusBaseRequest;
import com.hundsun.network.dbPlus.query.DbPlusExecuteSqlRequest;
import com.hundsun.network.dbPlus.result.DbPlusAppConfigResult;
import com.hundsun.network.dbPlus.result.DbPlusBaseResult;
import com.hundsun.network.dbPlus.result.DbPlusExcuteResult;
import com.hundsun.network.dbPlus.result.DbPlusLoginResult;
import com.hundsun.network.dbPlus.result.DbPlusShowAllTablesResult;
import com.hundsun.network.dbPlus.result.DbPlusTableInfoResult;

/**
 * 页面的打印输出操作
 * @author yueliang 2013-2-25
 */
public class DbPlusWriter {

	/**
	 * 页面的简单信息输出
	 * @param out permissionResult 
	 */
	public static void printSingleMsg(PrintWriter out, String result, Map<String, String> config) {
		printHeader(out, config.get(DbPlusDict.Charset));
		out.println(result);
		printFooter(out);
	}
	
	/**
	 * SQL语句执行结果打印页面
	 * @param PrintWriter ExecuteRequest ExecuteResult
	 */
	public static void printExecuteSqlHTML(PrintWriter out, DbPlusExecuteSqlRequest request, 
			DbPlusExcuteResult result, Map<String, String> config) {

		// TODO JS文件待转移出
//		String executeJs = "";
//		try {
//			executeJs = bufferedReader("..\\execute.js");
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		out.println("<script type='text/javascript'>" + executeJs + "</script>");
		
		// ------------ javascript Start ------------ //
		out.println("<script type='text/javascript'>");
		out.println("var isIE=!(!document.all);");
		// 识别选中的区域（字符串前后索引）
		/* javascript code
		function getSelectPosition(){
			var oTextarea = document.getElementById('remark');
			var start=0,end=0;    
			if(isIE){   
				var sTextRange= document.selection.createRange();   
				if(sTextRange.parentElement()== oTextarea){   
					var oTextRange = document.body.createTextRange();   
					oTextRange.moveToElementText(oTextarea);   
					for (start=0; oTextRange.compareEndPoints("StartToStart", sTextRange) < 0; start++){    
						oTextRange.moveStart('character', 1);    
					}   
					//需要计算一下\n的数目(按字符移动的方式不计\n,所以这里加上)    
					for (var i = 0; i <= start; i ++){   
						if (oTextarea.value.charAt(i) == '\n'){    
							start++;    
						}   
					}    
					//再计算一次结束的位置   
					oTextRange.moveToElementText(oTextarea);    
					for (end = 0; oTextRange.compareEndPoints('StartToEnd', sTextRange) < 0; end ++){   
						oTextRange.moveStart('character', 1);   
					}   
					for (var i = 0; i <= end; i ++){   
						if (oTextarea.value.charAt(i) == '\n'){    
							end++;    
						}   
					}   
				}   
			}else{
				start = oTextarea.selectionStart;   
				end = oTextarea.selectionEnd;   
			}   
			document.getElementById('beginIndex').value=start;
		    document.getElementById('endIndex').value=end;
		}
		*/
		out.println("function getSelectPosition(){");
			out.println("var oTextarea=document.getElementById('sql');");
			out.println("var start=0,end=0;");
			out.println("if(isIE){");
				out.println("var sTextRange=document.selection.createRange();");
				out.println("if(sTextRange.parentElement()==oTextarea){");
					out.println("var oTextRange=document.body.createTextRange();");
					out.println("oTextRange.moveToElementText(oTextarea);");
					out.println("for(start=0;oTextRange.compareEndPoints('StartToStart',sTextRange)<0;start++){");
						out.println("oTextRange.moveStart('character',1);");
					out.println("}");
					out.println("for(var i=0;i<=start;i++){");
						out.println("if(oTextarea.value.charAt(i)=='\\n'){start++;}");
					out.println("}");
					out.println("oTextRange.moveToElementText(oTextarea);");
					out.println("for(end=0;oTextRange.compareEndPoints('StartToEnd',sTextRange)<0;end++){");
						out.println("oTextRange.moveStart('character',1);");
					out.println("}");
					out.println("for(var i=0;i<=end;i++){");
						out.println("if(oTextarea.value.charAt(i)=='\\n'){end++;}");
					out.println("}");
				out.println("}");
			out.println("}else{");
				out.println("start=oTextarea.selectionStart;end=oTextarea.selectionEnd;");
			out.println("}");
			out.println("document.getElementById('beginIndex').value=start;");
			out.println("document.getElementById('endIndex').value=end;");
			out.println("return true;");
			out.println("}");
			
		out.println("</script>");
		// ------------ javascript  End ------------ //
		
		
		// ------------ SQL 输入框 Start ------------ //
		out.println("<form action='" + DbPlusRequestUrl.ExecuteSQLPath + "' method='post' onsubmit='javascript:getSelectPosition();'>");
		out.println("<input type='hidden' id='beginIndex' name='beginIndex' value='" + request.getBeginIndex() + "'/>");
		out.println("<input type='hidden' id='endIndex' name='endIndex' value='" + request.getEndIndex() + "'/>");
		out.println("<input type='hidden' name='driverClassName' value='" + request.getDriverClassName() + "'/>");
		out.println("<input type='hidden' name='url' value='" + request.getUrl() + "'/>");
		out.println("<input type='hidden' name='username' value='" + request.getUsername() + "'/>");
		out.println("<input type='hidden' name='password' value='" + request.getPassword() + "'/>");
		out.println("<textarea id='sql' name='sql' cols='80' rows='10' onselect='javascript:getSelectPosition();'>" + request.getRequestSql() + "</textarea>");
		out.println("<input type='submit' value='执行SQL'/>");
		out.println("</form>");
		// 显示选中的区域
		/* javascript code
		$(function(){
		    var oTextarea = document.getElementById('remark');   
			var start = parseInt(1);    
			var end =  parseInt(2);   
			if(isIE){   
				var oTextRange = oTextarea.createTextRange();   
				var LStart = start;   
				var LEnd = end;   
				start = 0;   
				end = 0;   
				var value = oTextarea.value;   
				for(var i=0; i<value.length && i<LStart; i++){   
					var c = value.charAt(i);   
					if(c!='\n'){   
					start++;   
					}   
				}   
				for(var i=value.length-1; i>=LEnd && i>=0; i--){   
					var c = value.charAt(i);   
					if(c!='\n'){   
						end++;   
					}   
				}   
				oTextRange.moveStart('character', start);   
				oTextRange.moveEnd('character', -end);   
				//oTextRange.collapse(true);   
				oTextRange.select();   
				oTextarea.focus();   
			}else{   
				oTextarea.select();   
				oTextarea.selectionStart=start;   
				oTextarea.selectionEnd=end;   
			}   
		});
		 */
		if(request.getEndIndex() >= 0 && request.getEndIndex() > request.getBeginIndex()){ // 有部分SQL被选中
			// TODO JS文件待转移出
//			try {
//				executeJs = bufferedReader("..\\executeView.js");
//				executeJs.replaceFirst("$!{beginIndex}", request.getBeginIndex().toString());
//				executeJs.replaceFirst("$!{endIndex}", request.getEndIndex().toString());
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//			out.println("<script type='text/javascript'>" + executeJs + "</script>");
			
			out.println("<script type='text/javascript'>");
			out.println("var oTextarea=document.getElementById('sql');");
			out.println("var start=parseInt(" + request.getBeginIndex() + ");");
			out.println("var end=parseInt(" + request.getEndIndex() + ");");
			out.println("if(isIE){");
				out.println("var oTextRange=oTextarea.createTextRange();");
				out.println("var LStart=start,LEnd=end;");
				out.println("start=0;end=0;");
				out.println("var value=oTextarea.value;");
				out.println("for(var i=0;i<value.length&&i<LStart;i++){");
					out.println("var c=value.charAt(i);if(c!='\\n'){start++;}");
				out.println("}");
				out.println("for(var i=value.length-1;i>=LEnd&&i>=0;i--){");
					out.println("var c = value.charAt(i);if(c!='\\n'){end++;}");
				out.println("}");
				out.println("oTextRange.moveStart('character', start);");
				out.println("oTextRange.moveEnd('character', -end);");
				out.println("oTextRange.select();oTextarea.focus();");
			out.println("}else{");
				out.println("oTextarea.select();");
				out.println("oTextarea.selectionStart=start;oTextarea.selectionEnd=end;");
			out.println("}");
			out.println("</script>");
		}
		// ------------ SQL 输入框      End ------------ //
		
		// ------------ 查询列表或处理结果 Start ------------ //
		if(result.correct()){
			if(result.getResultList() != null && result.getResultList().size() > 0){
				out.println("<table border='1px' cellSpacing='0'>");
				for(List<String> list : result.getResultList()) {
					out.println("<tr>");
					for(String colName : list) {
						out.println("<td style='border:solid 1px black;cellSpacing:0px;'>" + colName + "</td>");
					}
					out.println("</tr>");
				}
				out.println("</table>");
			} else {
				out.println(result.getSum()); // 处理结果 影响记录条数
			}
		} else {
			out.println(result.getErrorInfo()); // 执行失败错误信息
		}
		// ------------ 查询列表或处理结果        End ------------ //
		
	}

	/**
	 * 登录页面
	 * @param out request result useDefaultConnectionConfig
	 */
	public static void printLoginHTML(PrintWriter out, DbPlusBaseRequest request, 
			DbPlusBaseResult result, Boolean useDefaultConnectionConfig, Map<String, String> config) {
		printHeader(out, config.get(DbPlusDict.Charset));
		out.println("<form action='" + DbPlusRequestUrl.LoginPath + "' method='post'>");
		out.println("<table>");
		
		String defaultDriverClassName = "";
		String defaultUrl = "";
		String defaultUsername = "";
//		String defaultPassword = "";
		
		// 首次进入登录页面，没有请求
		if(null == result) {
			// 如果功能配置DbPlusAppConfig.UseDefaultConnectionConfig开放，则可填入DB链接的默认信息，否则留空
			if(useDefaultConnectionConfig) {
				defaultDriverClassName = config.get(DbPlusDict.DriverClassName);
				defaultUrl = config.get(DbPlusDict.Url);
				defaultUsername = config.get(DbPlusDict.Username);
//				defaultPassword = config.get(DbPlusDict.Password);
			}
		}
		// 登录失败再次进入登录页面，填入上次输入的值
		else {
			defaultDriverClassName = request.getDriverClassName();
			defaultUrl = request.getUrl();
			defaultUsername = request.getUsername();
//			defaultPassword = request.getPassword();
		}
		
		out.println("<tr>");
		out.println("<td>driverClassName:</td>");
		out.println("<td><input type='text' name='driverClassName' value='" 
				+ defaultDriverClassName + "' style='width:300px;'/></td>");
		out.println("</tr>");
		out.println("<tr>");
		out.println("<td>url:</td>");
		out.println("<td><input type='text' name='url' value='" 
				+ defaultUrl + "' style='width:300px;'/></td>");
		out.println("</tr>");
		out.println("<tr>");
		out.println("<td>username:</td>");
		out.println("<td><input type='text' name='username' value='" 
				+ defaultUsername + "' style='width:300px;'/></td>");
		out.println("</tr>");
		out.println("<tr>");
		out.println("<td>password:</td>");
//				out.println("<td><input type='password' name='password' value='" 
//						+ defaultPassword + "' style='width:300px;'/></td>");
		out.println("<td><input type='password' name='password' value='' style='width:300px;'/></td>");
		out.println("</tr>");
		
		out.println("</table>");
		out.println("</br>");
		out.println("<input type='submit' value='登录'/>");
		out.println("</form>");
		if(null == result) {
			out.println("输入JDBC驱动类名、连接地址、用户名、密码");
		} else {
			out.println(result.getErrorInfo());
		}
		printFooter(out);
	}

	/**
	 * 所有表名列表页面
	 * @param out request result 
	 * @param config 
	 */
	public static void printAllTablesHTML(PrintWriter out, 
			DbPlusBaseRequest request, DbPlusShowAllTablesResult result, Map<String, String> config) {
//		String reInfo = getRequestInfoForUrl(request);
		out.println("<script type='text/javascript'>");
		out.println("function tableInfo(tableName){");
			out.println("window.top.document.getElementById('tableName').value=tableName;");
			out.println("window.top.document.getElementById('subForm').submit();");
		out.println("}");
		out.println("</script>");
//		out.println("表:<a href='" + DbPlusRequestUrl.ShowAllTables + "?" + reInfo + "'>刷新</a></br>");
		out.println("<form id='AllTables' action='" + DbPlusRequestUrl.ShowAllTables + "' method='post'>");
		getRequestInfoForForm(out, request, new HashMap<String, String>());
		out.println("</form>");
		out.println("表：<a href=\"javascript:document.getElementById('AllTables').submit();\">刷新</a></br>");
		
//		out.println("<form id='subForm' action='" + DbPlusRequestUrl.TableInfo + "' target='_blank' method='post'>");
//		Map<String, String> map = new HashMap<String, String>();
//		map.put("tableName", "");
//		getRequestInfoForForm(out, request, map);
//		out.println("</form>");
		for(String tableName : result.getTableList()) {
//			out.println("<a href='" + DbPlusRequestUrl.TableInfo + "?" + reInfo + "&tableName=" + tableName + "' target='_blank'>" + tableName + "</a></br>");
			out.println("<a href=\"javascript:tableInfo(\'" + tableName + "\');\" style='text-decoration:none;'>" + tableName + "</a></br>");
		}
	}

	/**
	 * 登录成功初始化页面
	 * @param out request result 
	 */
	public static void printLoginSuccessHTML(PrintWriter out,
			DbPlusBaseRequest request, DbPlusLoginResult result, Map<String, String> config) {
		// 登录初始化：注销、权限控制、表信息、SQL输入框、执行结果框
		printHeader(out, config.get(DbPlusDict.Charset));
		loginHeader(out, request);
		
		out.println("<form id='subForm' action='" + DbPlusRequestUrl.TableInfo + "' target='_blank' method='post'>");
		Map<String, String> map = new HashMap<String, String>();
		map.put("tableName", "");
		getRequestInfoForForm(out, request, map);
		out.println("</form>");
		
		String reInfo = getRequestInfoForUrl(request);
		out.println("<table>");
		out.println("<tr>");
		out.println("<td>");
		out.println("<iframe id='tabls' scrolling='auto' style='width:245px;height:680px;' frameborder='1' src='" + DbPlusRequestUrl.ShowAllTables + "?" + reInfo + "'>");
		out.println("</iframe>");
		out.println("</td>");
		out.println("<td>");
		out.println("<iframe id='executeSql' scrolling='auto' style='width:800px;height:680px;' frameborder='1' src='" + DbPlusRequestUrl.ExecuteSQLPath + "?" + reInfo + "'>");
		out.println("</iframe>");
		out.println("</td>");
		out.println("</tr>");
		out.println("</table>");

		printFooter(out);
	}
	
	/*
	 * 页头
	 * @param out
	 */
	private static void printHeader(PrintWriter out, String charset) {
		out.println("<!DOCTYPE html PUBLIC '-//W3C//DTD XHTML 1.0 Transitional//EN' 'http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd'>");
		out.println("<html xmlns='http://www.w3.org/1999/xhtml' xml:lang='en'>");
		out.println("<head>");
		out.println("<meta http-equiv='Content-Type' content='text/html; charset="
				+ charset + "'/>");
		out.println("<title>DbPlus</title>");
		out.println("</head>");
		out.println("<body>");
	}
	
	/*
	 * 注销、权限控制
	 * @param out request
	 */
	private static void loginHeader(PrintWriter out, DbPlusBaseRequest request) {
		if(null == request 
				|| null == request.getDriverClassName()
				|| null == request.getUrl()
				|| null == request.getUsername()
				|| null == request.getPassword()) {
		} else {
//			String reInfo = getRequestInfoForUrl(request);
//			out.println("<a href='" + DbPlusRequestUrl.LoginPath + "'>退出</a>");
//			out.println("<a href='" + DbPlusRequestUrl.CLOSE + "?" + reInfo + "'>关闭功能并退出</a>");
//			out.println("<a href='" + DbPlusRequestUrl.AppConfig + "?" + reInfo + "' target='_blank'>功能开关</a></br>");
			out.println("<form id='closeForm' action='" + DbPlusRequestUrl.CLOSE + "' method='post'>");
			getRequestInfoForForm(out, request, new HashMap<String, String>());
			out.println("</form>");
			out.println("<form id='appForm' action='" + DbPlusRequestUrl.AppConfig + "' target='_blank' method='post'>");
			getRequestInfoForForm(out, request, new HashMap<String, String>());
			out.println("</form>");
			out.println("<a href='" + DbPlusRequestUrl.LoginPath + "'>退出</a>");
			out.println("<a href='#' onclick=\"javascript:document.getElementById('closeForm').submit();\">关闭功能并退出</a>");
//			out.println("<a href='#' onclick=\"javascript:document.getElementById('appForm').submit();\">功能开关</a>");
		}
	}
	
	/*
	 * 页脚
	 * @param out
	 */
	private static void printFooter(PrintWriter out) {
		out.println("</body>");
		out.println("</html>");
		out.flush();
		out.close(); 
	}

	/**
	 * 应用功能开关控制页面
	 * @param out request result 
	 */
	public static void printAppConfigHTML(PrintWriter out,
			DbPlusBaseRequest request, DbPlusAppConfigResult result, Map<String, String> config) {
		printHeader(out, config.get(DbPlusDict.Charset));
		
		out.println("<table>");
			out.println("<tr>");
				out.println("<td>功能名称</td><td>状态</td><td>开关</td>");
			out.println("</tr>");
		for(String key : result.getAppConfigMap().keySet()) {
			out.println("<tr>");
			out.println("<td>" + key + "</td>");
			out.println("<td>" + (result.getAppConfigMap().get(key)? "<font color='red'>开启</font>" : "关闭") + "</td>");
			out.println("<td>");
				out.println("<form action='" + DbPlusRequestUrl.AppConfig + "?appConfig="
						+ key + "' method='post'>"); // 参数是要开启（或关闭）的功能名称
				out.println("<input type='hidden' name='driverClassName' value='" + request.getDriverClassName() + "'/>");
				out.println("<input type='hidden' name='url' value='" + request.getUrl() + "'/>");
				out.println("<input type='hidden' name='username' value='" + request.getUsername() + "'/>");
				out.println("<input type='hidden' name='password' value='" + request.getPassword() + "'/>");
				out.println("<input type='submit' value='" + (result.getAppConfigMap().get(key)? "关闭" : "开启") + "'/>");
				out.println("</form>");
			out.println("</td>");
			out.println("</tr>");
		}
		
		out.println("</table>");
		
		printFooter(out);
	}
	
	/*
	 * 拼装url请求的GET参数
	 */
	private static String getRequestInfoForUrl(DbPlusBaseRequest request) {
		return "driverClassName=" + request.getDriverClassName()
			+ "&url=" + request.getUrl()
			+ "&username=" + request.getUsername()
			+ "&password=" + request.getPassword();
	}
	/*
	 * 拼装Form请求的POST参数
	 */
	private static void getRequestInfoForForm(PrintWriter out, DbPlusBaseRequest request, Map<String, String> param) {
		out.println("<input type='hidden' name='driverClassName' value='"+request.getDriverClassName()+"'/>");
		out.println("<input type='hidden' name='url' value='"+request.getUrl()+"'/>");
		out.println("<input type='hidden' name='username' value='"+request.getUsername()+"'/>");
		out.println("<input type='hidden' name='password' value='"+request.getPassword()+"'/>");
		if(param != null && param.size() > 0) {
			for(String key :param.keySet()) {
				out.println("<input type='hidden' id='"+key+"' name='"+key+"' value='"+param.get(key)+"'/>");
			}
		}
	}
	
	/*
	 * 读取文件（JS等）
	 */
	private static String bufferedReader(String path) throws IOException{
        File file = new File(path);
        if(!file.exists() || file.isDirectory()) {
        	throw new FileNotFoundException();
        }
        BufferedReader br = new BufferedReader(new FileReader(file));
        String temp = null;
        StringBuffer sb = new StringBuffer();
        temp = br.readLine();
        while(temp != null) {
            sb.append(temp + " ");
            temp = br.readLine();
        }
        return sb.toString();
    }
	
	/**
	 * 查看表信息页面
	 * @param out request result 
	 * @param config 
	 */
	public static void printTableInfoHTML(PrintWriter out, 
			DbPlusBaseRequest request, DbPlusTableInfoResult result, Map<String, String> config) {
//		String reInfo = getRequestInfoForUrl(request);
		out.println("<!DOCTYPE html PUBLIC '-//W3C//DTD XHTML 1.0 Transitional//EN' 'http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd'>");
		out.println("<html xmlns='http://www.w3.org/1999/xhtml' xml:lang='en'>");
		out.println("<head>");
		out.println("<meta http-equiv='Content-Type' content='text/html; charset="
				+ config.get(DbPlusDict.Charset) + "'/>");
		out.println("<title>"+result.getTableName()+"</title>");
		out.println("<script type='text/javascript'>");
		out.println("function tableInfo(tableName, info){");
			out.println("window.top.document.getElementById('tableName').value=tableName;");
			out.println("window.top.document.getElementById('info').value=info;");
			out.println("window.top.document.getElementById('subForm').submit();");
		out.println("}");
		out.println("</script>");
		out.println("</head>");
		out.println("<body>");
		
		out.println("<form id='subForm' action='" + DbPlusRequestUrl.TableInfo + "' method='post'>");
		Map<String, String> map = new HashMap<String, String>();
		map.put("tableName", "");
		map.put("info", "");
		getRequestInfoForForm(out, request, map);
		out.println("</form>");
		
		List<List<String>> infoList = new ArrayList<List<String>>();
		if(result != null && result.correct()){
			if(result.getTableInfo() != null) {
				out.println("<ul>");
				out.println("<li>列</li>");
//				out.println("<li><a href='" + DbPlusRequestUrl.TableInfo + "?" + reInfo + "&tableName=" + result.getTableName() + "&info=DataInfo'>数据</a></li>");
				out.println("<li><a href=\"javascript:tableInfo(\'" + result.getTableName() + "\','DataInfo');\">数据</a></li>");
				out.println("</ul>");
//				out.println("<a href='" + DbPlusRequestUrl.TableInfo + "?" + reInfo + "&tableName=" + result.getTableName() + "'>刷新</a></br>");
				out.println("<a href=\"javascript:tableInfo(\'" + result.getTableName() + "\','');\">刷新</a>");
				infoList = result.getTableInfo();
			} else if(result.getDataInfo() != null) {
				out.println("<ul>");
//				out.println("<li><a href='" + DbPlusRequestUrl.TableInfo + "?" + reInfo + "&tableName=" + result.getTableName() + "'>列</a></li>");
				out.println("<li><a href=\"javascript:tableInfo(\'" + result.getTableName() + "\','');\">列</a></li>");
				out.println("<li>数据</li>");
				out.println("</ul>");
//				out.println("<a href='" + DbPlusRequestUrl.TableInfo + "?" + reInfo + "&tableName=" + result.getTableName() + "&info=DataInfo'>刷新</a></br>");
				out.println("<a href=\"javascript:tableInfo(\'" + result.getTableName() + "\','DataInfo');\">刷新</a>");
				infoList = result.getDataInfo();
			} else {
				out.println("ERROR!");
			}
			out.println("<table border='1px' cellSpacing='0'>");
			for(List<String> list : infoList) {
				out.println("<tr>");
				for(String e : list) {
					out.println("<td style='border:solid 1px black;cellSpacing:0px;'>" + e + "</td>");
				}
				out.println("</tr>");
			}
			out.println("</table>");
		} else {
			out.println(result.getErrorInfo()); // 执行失败错误信息
		}
		
		out.println("</body>");
		out.println("</html>");
		out.flush();
		out.close(); 
	}

	
}
