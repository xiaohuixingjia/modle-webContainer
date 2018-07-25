package com.umpay.proxyservice.config;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.umpay.proxyservice.bean.ModelInfo;
import com.umpay.proxyservice.ruleTimeService.InitService;
import com.umpay.proxyservice.ruleTimeService.RuleTimeService;
import com.umpay.proxyservice.service.TaskService;
import com.umpay.proxyservice.service.impl.JavaTaskServiceImpl;
import com.umpay.proxyservice.service.impl.PythonTaskServiceImpl;
import com.umpay.proxyservice.util.PropertiesUtil;

public class ModelConfig implements InitService {
	private static Map<String, AnalyzeType> map;

	static {
		map = new HashMap<String, AnalyzeType>();
		map.put("java", AnalyzeType.JAVA);
		map.put("JAVA", AnalyzeType.JAVA);
		map.put("python", AnalyzeType.PYTHON);
		map.put("PYTHON", AnalyzeType.PYTHON);
	}

	/* 服务者 */
	private TaskService service;
	/* 版本信息 */
	private ModelInfo info;

	
	public ModelInfo getInfo() {
		return info;
	}

	@Override
	public void init() throws Exception {
		Properties versionConfig = PropertiesUtil.getInstance("version_control.properties").getProperties();
		String version = versionConfig.getProperty("version");
		String modelType = versionConfig.getProperty("modelType");
		info=new ModelInfo();
		info.setVersion(version);
		info.setModelType(modelType);
		service = map.get(modelType).getModelService(
				"version_config" + File.separator + version + File.separator + "config.properties", versionConfig);
	}

	private enum AnalyzeType {
		JAVA {
			@Override
			TaskService getModelService(String fullConfigPath, Properties versionConfig) throws Exception {
				String fullClassName = PropertiesUtil.getInstance(fullConfigPath).getProperties()
						.getProperty("fullClassName");
				String fullJarPath = PropertiesUtil.getInstance(fullConfigPath).getProperties()
						.getProperty("fullJarPath");
				JavaTaskServiceImpl serviceImpl = new JavaTaskServiceImpl(fullClassName, fullJarPath);
				serviceImpl.init();
				return serviceImpl;
			}
		},
		PYTHON {
			@Override
			TaskService getModelService(String fullConfigPath, Properties versionConfig) throws Exception {
				String threadNums = versionConfig.getProperty("threadNums");
				String pythonFilePath = PropertiesUtil.getInstance(fullConfigPath).getProperties()
						.getProperty("pythonFilePath");
				PythonTaskServiceImpl serviceImpl = new PythonTaskServiceImpl(Integer.parseInt(threadNums),
						pythonFilePath);
				serviceImpl.init();
				return serviceImpl;
			}
		};

		abstract TaskService getModelService(String fullConfigPath, Properties versionConfig) throws Exception;
	}

	/**
	 * 获取任务处理服务
	 * 
	 * @return
	 */
	public TaskService geTaskService() {
		return service;
	}

	public static void main(String[] args) throws Exception {
		RuleTimeService.getRts().getRuleTimeService(ModelConfig.class).geTaskService().execute("<xml>aabbccdd</xml>");
	}


	/**
	 * 将当前版本落地到配置文件中
	 * @return
	 */
	public boolean putVersion2config() {
		PropertiesUtil instance = PropertiesUtil.getInstance("version_control.properties");
		instance.getProperties().put("version", info.getVersion());
		instance.getProperties().put("modelType", info.getModelType());
		return instance.writeNewProp2local();
	}

}
