package com.umpay.proxyservice.service;

import com.umpay.proxyservice.exception.BaseException;

/**
 * 接收到请求的任务处理实现
 * 
 * @author xuxiaojia
 */
public interface TaskService {
	/**
	 * 执行
	 * @param reqInfo
	 * @return
	 */
	public String execute(String reqInfo);
	
    /**
     * 初始化当前服务类 不符合规则则抛出异常
     * @throws BaseException
     */
	public void init() throws BaseException;
}
