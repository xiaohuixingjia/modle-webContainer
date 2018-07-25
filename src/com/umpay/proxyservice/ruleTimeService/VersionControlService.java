package com.umpay.proxyservice.ruleTimeService;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.umpay.proxyservice.bean.ResponseBean;
import com.umpay.proxyservice.config.ModelConfig;
import com.umpay.proxyservice.exception.BaseException;
import com.umpay.proxyservice.exception.Retcode;
import com.umpay.proxyservice.factory.ResponseFactory;

/**
 * 版本控制服务
 * 
 * @author xuxiaojia
 */
public final class VersionControlService {
	private final static Logger _log = LoggerFactory.getLogger(VersionControlService.class);
	/* 实时对象 */
	private Map<Class<? extends InitService>, Object> ruleTimeInstanceMap;
	/* 备份对象 */
	private Map<Class<? extends InitService>, Object> reserveInstanceMap;
	@SuppressWarnings("unchecked")
	private static VersionControlService versionControlService = new VersionControlService(
			new Class[] { ModelConfig.class });

	/*
	 * 是否用实时服务
	 */
	public AtomicBoolean canReadRuntime = new AtomicBoolean(true);

	public static VersionControlService getVCS() {
		return versionControlService;
	}

	private VersionControlService(Class<? extends InitService>[] initClasss) {
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
		for (Class<? extends InitService> class1 : initClasss) {
			try {
				InitService newInstance = class1.newInstance();
				newInstance.init();
				ruleTimeInstanceMap.put(class1, newInstance);
			} catch (Exception e) {
				ruleTimeInstanceMap.clear();
				throw new BaseException(Retcode.CLASS_INIT_FAIL, e);
			}
		}
	}

	public <T> T getRuleTimeService(Class<T> class1, boolean isTest) throws BaseException {
		if (canReadRuntime.get()) {
			return getT(ruleTimeInstanceMap, class1);
		} else {
			if (isTest) {
				_log.info("走测试的实时服务");
				return getT(ruleTimeInstanceMap, class1);
			}
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
	private void to_pub() throws Exception {
		// 备份当前数据信息
		reserveInstanceMap = ruleTimeInstanceMap;
		// 设置当前通道信息为不可读
		canReadRuntime.set(false);
		// 休息一秒让所有现有的请求线程走完对 ruleTimeInstance 的使用
		sleep(1000);
		// 将集合中的
		Class<? extends InitService>[] arr = new Class[reserveInstanceMap.size()];
		reserveInstanceMap.keySet().toArray(arr);
		// 重新加载数据库中的配置
		try {
			initRuleTime(arr);
		} catch (Exception e) {
			_log.error("信息重载出现异常,主备切换失败：", e);
			ruleTimeInstanceMap = reserveInstanceMap;
			// 恢复配置类
			clearReserve();
			throw e;
		}
		_log.info("信息重载成功，实时通道用于测试，备份通道用于处理请求");
	}

	/**
	 * 线程休息 mills毫秒
	 * 
	 * @param mills
	 */
	private void sleep(long mills) {
		try {
			Thread.sleep(mills);
		} catch (InterruptedException e) {
		}
	}

	public synchronized ResponseBean getCurrentInfo() {
		try {
			Map<String, Object> respMap=new HashMap<String, Object>();
			boolean flag=canReadRuntime.get();
			//是否有稳定版本
			respMap.put("haveSteadyVersion", flag);
			if(!flag){
				respMap.put("reserve", ((ModelConfig) reserveInstanceMap.get(ModelConfig.class)).getInfo());
			}
			respMap.put("ruleTime", ((ModelConfig) ruleTimeInstanceMap.get(ModelConfig.class)).getInfo());
			return ResponseFactory.buildResponse(Retcode.SUCCESS,respMap);
		} catch (Exception e) {
			_log.info("查询版本失败：",e);
			return ResponseFactory.buildResponse(Retcode.ERROR);
		}
	}
	
	
	/**
	 * 实时版本可用
	 */
	public synchronized ResponseBean ruleTimeCanUse() {
		clearReserve();
		if (!((ModelConfig) ruleTimeInstanceMap.get(ModelConfig.class)).putVersion2config()) {
			return ResponseFactory.buildResponse(Retcode.VERSION_CONFIG_EXCHANGE_ERROR);
		}
		return ResponseFactory.buildResponse(Retcode.SUCCESS);
	}

	/**
	 * 清楚备份，启用实时
	 */
	private void clearReserve() {
		// 设置当前通道信息为不可读
		canReadRuntime.set(true);
		// 休息一秒让所有现有的请求线程走完对 ruleTimeInstance 的使用
		sleep(1000);
		if (reserveInstanceMap != null) {
			reserveInstanceMap = null;
		}
	}

	/**
	 * 回退实时版本
	 * 
	 * @throws BaseException
	 */
	public synchronized ResponseBean back2reserve() throws BaseException {
		if (canReadRuntime.get()) {
			return ResponseFactory.buildResponse(Retcode.NO_RESERVE_CAN_BANK);
		}
		ruleTimeInstanceMap = reserveInstanceMap;
		if (!((ModelConfig) reserveInstanceMap.get(ModelConfig.class)).putVersion2config()) {
			return ResponseFactory.buildResponse(Retcode.VERSION_CONFIG_EXCHANGE_ERROR);
		}
		clearReserve();
		return ResponseFactory.buildResponse(Retcode.SUCCESS);
	}

	/**
	 * 发布新版本请求，已加锁，防止并发访问
	 * 
	 * @return true--重载成功 false--重载失败
	 */
	public synchronized ResponseBean pubNewVersion() {
		if (canReadRuntime.get()) {
			try {
				to_pub();
				return ResponseFactory.buildResponse(Retcode.SUCCESS);
			} catch (Exception e) {
				_log.error("重载出现异常：", e);
				return ResponseFactory.buildResponse(Retcode.PUB_NEW_VERSION_ERROR);
			}
		}
		return ResponseFactory.buildResponse(Retcode.NEED_CHECK_VERSION);
	}

}
