package com.umpay.proxyservice.factory;

import com.umpay.proxyservice.bean.ResponseBean;
import com.umpay.proxyservice.exception.Retcode;

/**
 * 响应信息创建工厂
 * 
 * @author xuxiaojia
 */
public class ResponseFactory {
	/**
	 * 
	 * @param retcode
	 * @return
	 */
	public static ResponseBean buildResponse(Retcode retcode) {
		ResponseBean bean = new ResponseBean();
		bean.setRetcode(retcode.getCode());
		bean.setErrorMes(retcode.getName());
		return bean;
	}

	/**
	 * 
	 * @param retcode
	 * @param obj
	 * @return
	 */
	public static ResponseBean buildResponse(Retcode retcode, Object obj) {
		ResponseBean bean = new ResponseBean();
		bean.setRetcode(retcode.getCode());
		bean.setErrorMes(retcode.getName());
		bean.setRespInfo(obj);
		return bean;
	}
}
