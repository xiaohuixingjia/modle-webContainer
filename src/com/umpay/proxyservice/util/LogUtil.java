package com.umpay.proxyservice.util;

import java.util.Map;

public class LogUtil {
	private static LogUtil instance = new LogUtil();

	private LogUtil() {
	}

	public static LogUtil getInstance() {
		return instance;
	}

	private ThreadLocal<String> logPrefix = new ThreadLocal<String>();
	private ThreadLocal<String> funcode = new ThreadLocal<String>();
	private ThreadLocal<String> mobileid = new ThreadLocal<String>();

	public void init(Map<String, String> xmlMap) {
		String transid = xmlMap.get(HttpMap.TRANSID);
		String dateTime = xmlMap.get(HttpMap.DATETIME);
		funcode.set(xmlMap.get(HttpMap.FUNCODE));
		mobileid.set(xmlMap.get(HttpMap.MOBILEID));
		logPrefix.set(transid + "," + dateTime + "," + funcode.get() + "," + mobileid.get());
	}

	public String getLogPrefix() {
		if (this.logPrefix.get() == null)
			return "";
		return "[" + this.logPrefix.get() + "]";
	}

	public String getFuncode() {
		return funcode.get();
	}

	public String getMobileid() {
		return mobileid.get();
	}

}
