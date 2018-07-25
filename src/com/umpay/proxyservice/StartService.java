package com.umpay.proxyservice;import java.net.URISyntaxException;import org.slf4j.Logger;import org.slf4j.LoggerFactory;import com.bs3.ioc.core.BeansContext;import com.umpay.proxyservice.ruleTimeService.VersionControlService;/** * 启动服务 *  * @author xuxiaojia * @date 2016年6月15日 上午10:18:15 */public class StartService {	private final static Logger log = LoggerFactory.getLogger("NioServerHandler");	public static void main(String[] args) throws URISyntaxException, Exception {		try {			BeansContext ctx = BeansContext.getInstance();			VersionControlService.getVCS();			ctx.setMappingFile("resource/mina2niocs.properties");			ctx.start();			log.info("模型web容器启动成功");		} catch (Exception e) {			log.error("模型web容器启动失败", e);		}	}}