package com.hundsun.network.dbPlus.result;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * 应用功能开关控制返回
 */
public class DbPlusAppConfigResult extends DbPlusBaseResult {

	private ArrayList<HashMap<String, Boolean>> 			appConfigs;
	
	private Map<String, Boolean> 						appConfigMap;

	public ArrayList<HashMap<String, Boolean>> getAppConfigs() {
		return appConfigs;
	}

	public void setAppConfigs(ArrayList<HashMap<String, Boolean>> appConfigs) {
		this.appConfigs = appConfigs;
	}

	public Map<String, Boolean> getAppConfigMap() {
		return appConfigMap;
	}

	public void setAppConfigMap(Map<String, Boolean> map) {
		this.appConfigMap = map;
	}
	
	
}
