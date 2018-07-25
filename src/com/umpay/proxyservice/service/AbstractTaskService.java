package com.umpay.proxyservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.umpay.proxyservice.exception.BaseException;
import com.umpay.proxyservice.util.LogUtil;

/**
 * 抽象包装 taskService
 * 
 * @author xuxiaojia
 */
public abstract class AbstractTaskService implements TaskService {
	private final static Logger _log = LoggerFactory.getLogger(AbstractTaskService.class);

	@Override
	public String execute(String reqInfo) {
		try {
			return absExecute(reqInfo);
		}catch (BaseException e) {
			_log.error(LogUtil.getInstance().getLogPrefix()+"调用模型服务异常:",e);
		}catch (Exception e) {
			_log.error(LogUtil.getInstance().getLogPrefix()+"调用模型服务异常:",e);
		}
		return "";
	}

	/**
	 * 抽象实现
	 * 
	 * @param reqInfo
	 * @return
	 */
	protected abstract String absExecute(String reqInfo)throws Exception ;
	
}
