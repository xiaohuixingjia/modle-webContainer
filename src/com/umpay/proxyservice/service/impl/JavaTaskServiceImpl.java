package com.umpay.proxyservice.service.impl;

import java.net.URL;
import java.net.URLClassLoader;

import com.umpay.model.service.ModelService;
import com.umpay.proxyservice.exception.BaseException;
import com.umpay.proxyservice.exception.Retcode;
import com.umpay.proxyservice.service.AbstractTaskService;

/**
 * 调用java模型
 * 
 * @author xuxiaojia
 */
public class JavaTaskServiceImpl extends AbstractTaskService {

	private ModelService model;

	private String fullClassName;
	private String fullJarPath;

	public JavaTaskServiceImpl(String fullClassName, String fullJarPath) {
		this.fullClassName = fullClassName;
		this.fullJarPath = fullJarPath;
	}

	@Override
	protected String absExecute(String reqInfo) throws Exception {
		return model.process(reqInfo);
	}

	@SuppressWarnings("resource")
	@Override
	public void init() throws BaseException {
		try {
			URL url = new URL("file:" + fullJarPath);
			URLClassLoader myClassLoader1=new URLClassLoader(new URL[]{url}, Thread.currentThread().getContextClassLoader());
			Class<?> myClass = null;
			myClass = myClassLoader1.loadClass(fullClassName);
			model = (ModelService) myClass.newInstance();
		} catch (Exception e) {
			throw new BaseException(Retcode.CLASS_INIT_FAIL, e);
		}
	}

	public static void main(String[] args) throws Exception {
		String jarFullFilePath="";
		String className="";
		URL url = new URL("file:" + jarFullFilePath);
		@SuppressWarnings("resource")
		URLClassLoader myClassLoader1=new URLClassLoader(new URL[]{url}, Thread.currentThread().getContextClassLoader());
		Class<?> myClass = null;
		myClass = myClassLoader1.loadClass(className);
		ModelService test = (ModelService) myClass.newInstance();
		test.process("<xml>...");
	}
}
