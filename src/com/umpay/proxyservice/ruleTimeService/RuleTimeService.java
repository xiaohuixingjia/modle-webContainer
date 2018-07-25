package com.umpay.proxyservice.ruleTimeService;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.umpay.proxyservice.config.ModelConfig;
import com.umpay.proxyservice.exception.BaseException;
import com.umpay.proxyservice.exception.Retcode;

/**
 * 实时服务
 * 
 * @author xuxiaojia
 */
public final class RuleTimeService {
	private final static Logger _log = LoggerFactory.getLogger(RuleTimeService.class);
	/* 实时对象 */
	private Map<Class<? extends InitService>, Object> ruleTimeInstanceMap;
	/* 备份对象 */
	private Map<Class<? extends InitService>, Object> reserveInstanceMap;
	/* 由实时服务初始化的配置类 */
	private Set<Class<? extends InitService>> initClasssSet;
	@SuppressWarnings("unchecked")
	private static RuleTimeService ruleTimeService = new RuleTimeService(new Class[] {ModelConfig.class });

	/*
	 * 是否在使用实时通道标识
	 */
	public AtomicBoolean canReadRuntime = new AtomicBoolean(true);

	public static RuleTimeService getRts() {
		return ruleTimeService;
	}

	private RuleTimeService(Class<? extends InitService>[] initClasss) {
		try {
			initRuleTime(initClasss);
		} catch (BaseException e) {
			throw new RuntimeException(e.getE());
		}
	}

	private void initRuleTime(Class<? extends InitService>[] initClasss) throws BaseException {
		ruleTimeInstanceMap = new HashMap<Class<? extends InitService>, Object>();
		if (initClasss == null || initClasss.length == 0) {
			throw new BaseException(Retcode.RELOAD_CLASS_IS_NULL);
		}
		initClasssSet = new HashSet<Class<? extends InitService>>();
		for (Class<? extends InitService> class1 : initClasss) {
			try {
				InitService newInstance = class1.newInstance();
				newInstance.init();
				initClasssSet.add(class1);
				ruleTimeInstanceMap.put(class1, newInstance);
			} catch (Exception e) {
				throw new BaseException(Retcode.CLASS_INIT_FAIL, e);
			}
		}
	}

	public <T> T getRuleTimeService(Class<T> class1) throws BaseException {
		if (canReadRuntime.get()) {
			return getT(ruleTimeInstanceMap, class1);
		} else {
			_log.info("当前使用备份对象");
			return getT(reserveInstanceMap, class1);
		}
	}

	@SuppressWarnings("unchecked")
	private <T> T getT(Map<Class<? extends InitService>, Object> map, Class<T> class1) throws BaseException {
		if (map.containsKey(class1)) {
			return (T) map.get(class1);
		}
		throw new BaseException(Retcode.CLASS_INSTANCE_NOT_FOUND);
	}

	@SuppressWarnings("unchecked")
	private void to_reload() throws BaseException {
		// 备份当前数据信息
		reserveInstanceMap = ruleTimeInstanceMap;
		// 设置当前通道信息为不可读
		canReadRuntime.set(false);
		// 休息一秒让所有现有的请求线程走完对 ruleTimeInstance 的使用
		sleep(1000);
		// 将集合中的
		Class<? extends InitService>[] arr = new Class[initClasssSet.size()];
		this.initClasssSet.toArray(arr);
		// 重新加载数据库中的配置
		initRuleTime(arr);
		// 设置当前通道信息为可读
		canReadRuntime.set(true);
		// 休息一秒让所有现有的请求线程走完对 reserveMapInfo 的使用
		sleep(1000);
		reserveInstanceMap.clear();
	}

	/**
	 * 线程休息 mills毫秒
	 * 
	 * @param mills
	 */
	private static void sleep(long mills) {
		try {
			Thread.sleep(mills);
		} catch (InterruptedException e) {
		}
	}

	/**
	 * 重载请求，已加锁，防止并发访问
	 * 
	 * @return true--重载成功 false--重载失败
	 */
	public synchronized boolean reload() {
		if (canReadRuntime.get()) {
			try {
				to_reload();
				return true;
			} catch (Exception e) {
				_log.error("ip重载出现异常：", e);
			}
		}
		return false;
	}

}
