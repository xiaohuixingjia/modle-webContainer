package com.umpay.proxyservice.bean;

/**
 * 响应信息
 * 
 * @author xuxiaojia
 */
public class ResponseBean {
	/* 响应码 */
	private String retcode;
	/* 响应错误信息 */
	private String errorMes;
	/* 其他信息 */
	private Object respInfo;

	public String getRetcode() {
		return retcode;
	}

	public void setRetcode(String retcode) {
		this.retcode = retcode;
	}

	public String getErrorMes() {
		return errorMes;
	}

	public void setErrorMes(String errorMes) {
		this.errorMes = errorMes;
	}

	public Object getRespInfo() {
		return respInfo;
	}

	public void setRespInfo(Object respInfo) {
		this.respInfo = respInfo;
	}

}
