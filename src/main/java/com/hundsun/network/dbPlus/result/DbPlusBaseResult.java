package com.hundsun.network.dbPlus.result;

/**
 * 远程服务返回对象基类 子类必须有无参构造函数
 */
public class DbPlusBaseResult {

    private Integer           errorNO;                                  // 错误代码,缺省为null,无错误

    private String            errorInfo;                                // 错误原因

    private String            serviceIp;                                // 执行service的服务器地址
    
    private Long              bizId;                                    // 处理的业务数据的id
    
    private String 			  url; 										// 请求url

    /**
     * 是否有错误
     * 
     * @return
     */
    public boolean error() {
        return !correct();
    }

    /**
     * 是否正确
     * 
     * @return
     */
    public boolean correct() {
        return this.errorNO == null;
    }

    public Integer getErrorNO() {
        return errorNO;
    }

    public void setErrorNO(Integer errorNO) {
        this.errorNO = errorNO;
    }

    public String getErrorInfo() {
        return errorInfo;
    }

    public void setErrorInfo(String errorInfo) {
        this.errorInfo = errorInfo;
    }

    public String getServiceIp() {
        return serviceIp;
    }

    public void setServiceIp(String serviceIp) {
        this.serviceIp = serviceIp;
    }

    public void setErrorNOInfo(Integer errorNO, String errorInfo) {
        this.errorNO = errorNO;
        this.errorInfo = errorInfo;
    }

    public void setBizId(Long bizId) {
        this.bizId = bizId;
    }

    public Long getBizId() {
        return bizId;
    }
    
    public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
