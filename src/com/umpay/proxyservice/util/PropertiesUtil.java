package com.umpay.proxyservice.util;

/**
 * @name PropertiesReader
 * @description properties文件读取,根据文件�?���?��修改时间判定是否要读取文件�?
 * @author tyf
 * @company umpay
 * @date 2013-8-06
 */
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PropertiesUtil {

	private static final Logger _log = LoggerFactory
			.getLogger("PropertiesReader");

	public static final String PFILE = System.getProperty("user.dir")
			+ "/resource/";// 路径

	private Properties m_props = null;// 配置文件
	private String fileName;// 文件�?
	private File m_file = null;// 文件
	private long m_lastModifiedTime = 0;// 修改时间

	/**
	 * 加载配置文件
	 * 
	 * @param filename
	 */
	private PropertiesUtil(String filename) {
		this.fileName = filename;
		m_file = new File(PFILE + fileName);
		m_lastModifiedTime = m_file.lastModified();

		if (m_lastModifiedTime == 0) {
			_log.error("file[" + PFILE + fileName + "] not found . ");
		}
		m_props = new Properties();
		try {
			m_props.load(new FileInputStream(PFILE + fileName));
		} catch (FileNotFoundException e) {
			_log.error("file[" + PFILE + fileName + "] not found : ", e);
		} catch (IOException e) {
			_log.error("Properties IO error : ", e);
		}
	}

	/**
	 * 单例
	 * 
	 * @param filename
	 * @return
	 */
	synchronized public static PropertiesUtil getInstance(String filename) {
		return new PropertiesUtil(filename);
	}

	/**
	 * 获取配置文件
	 * 
	 * @return
	 */
	public Properties getProperties() {
		return m_props;
	}

	/**
	 * 获取配置文件参数�?
	 * 
	 * @param name
	 * @return
	 */
	public String getConfigItem(String name) {
		long newTime = m_file.lastModified();

		// 判断�?��修改时间，如果变化则重新加载文件�?
		if (newTime == 0) {
			if (m_lastModifiedTime == 0) {
				_log.error("file[" + PFILE + fileName + "] not found . ");
			} else {
				_log.error("file[" + PFILE + fileName + "] was deleted . ");
			}
			// return defaultVal;
		} else if (newTime > m_lastModifiedTime) {
			m_props.clear();
			try {
				m_props.load(new FileInputStream(PFILE + fileName));
			} catch (FileNotFoundException e) {
				_log.error("file[" + PFILE + fileName + "] not found : ", e);
			} catch (IOException e) {
				_log.error("Properties IO error : ", e);
			}
		}

		m_lastModifiedTime = newTime;

		// 获取参数�?
		String val = m_props.getProperty(name);
		return val;
	}
	
	/**
	 * 写到本地配置中
	 * @return
	 */
	public boolean writeNewProp2local(){
		OutputStream out=null;
		try {
			out=new FileOutputStream(m_file);
			m_props.store(out, "changed on "+DateUtil.DateTostring19(new Date()));
			return true;
		} catch (Exception e) {
			return false;
		} finally {
			if(out!=null){
				try {
					out.close();
				} catch (IOException e) {
				}
			}
		}
	}
}
