package com.umpay.proxyservice;

import java.util.HashMap;
import java.util.Map;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.filter.codec.http.MutableHttpRequest;
import org.apache.mina.filter.codec.http.MutableHttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bs3.inf.IProcessors.HSessionInf;
import com.bs3.nio.mina2.Mina2H4Rpc2;
import com.bs3.nio.mina2.codec.IHttp;
import com.umpay.proxyservice.bean.ResponseBean;
import com.umpay.proxyservice.config.ModelConfig;
import com.umpay.proxyservice.exception.Retcode;
import com.umpay.proxyservice.factory.ResponseFactory;
import com.umpay.proxyservice.ruleTimeService.VersionControlService;
import com.umpay.proxyservice.service.TaskService;
import com.umpay.proxyservice.util.HttpMap;
import com.umpay.proxyservice.util.JacksonUtil;
import com.umpay.proxyservice.util.LogUtil;
import com.umpay.proxyservice.util.TimeCountUtil;
import com.umpay.proxyservice.util.XmlUtils;

/**
 * 
 * @author xuxiaojia
 *
 */
public class NioServerHandler extends Mina2H4Rpc2 {
	private final static Logger _log = LoggerFactory.getLogger(NioServerHandler.class);

	@Override
	protected void onServerReadReq(HSessionInf session, Object req) {
		TimeCountUtil.setStartTime();
		String reqXML = null;
		try {
			MutableHttpRequest request = (MutableHttpRequest) req;
			String requestURL = "";
			if (request != null && request.getRequestUri() != null) {
				requestURL = request.getRequestUri().getPath();
			}
			_log.info("收到的url：" + requestURL);
			// 判断是否发布新版本
			if (requestURL.endsWith(Constant.PUB_NEW_VERSION)) {
				respJsonInfo(session, VersionControlService.getVCS().pubNewVersion());
				return;
				// 判断是否回退到备份
			} else if (requestURL.endsWith(Constant.BACK_2_RESERVE)) {
				respJsonInfo(session, VersionControlService.getVCS().back2reserve());
				return;
				// 判断是否将新版本定为可用
			} else if (requestURL.endsWith(Constant.RULE_TIME_CAN_USE)) {
				respJsonInfo(session, VersionControlService.getVCS().ruleTimeCanUse());
				return;
			} else if (requestURL.endsWith(Constant.VIEW_CURRENT_INFO)) {
				respJsonInfo(session, VersionControlService.getVCS().getCurrentInfo());
				return;
			}
			// 获取报文
			reqXML = getRequXml(req);
			// 接收到请求报文
			_log.info("模接收到的报文为：" + reqXML);
			// 解析报文为map格式
			Map<String, String> reqMap = getMustElemInfoMap(reqXML);
			// 记录此线程的日志参数
			LogUtil.getInstance().init(reqMap);
			// 获取处理对象
			TaskService taskServer = VersionControlService.getVCS()
					.getRuleTimeService(ModelConfig.class, "true".equals(reqMap.get(HttpMap.IS_TEST))).geTaskService();
			// 获取处理后的信息
			String responseStr = taskServer.execute(reqXML);
			// 返回给商户报文
			_log.info(LogUtil.getInstance().getLogPrefix() + "响应耗时:" + TimeCountUtil.getTimeConsuming() + ",返回的报文:"
					+ responseStr);
			// 将处理后的信息返回
			this.responseContent(session, responseStr != null ? responseStr : "");
		} catch (Exception e) {
			_log.error(LogUtil.getInstance().getLogPrefix() + "模型通道路由处理失败" + reqXML, e);
			this.responseContent(session, "");
		}
	}

	private void respJsonInfo(HSessionInf session, ResponseBean bean) {
		try {
			this.responseContent(session, JacksonUtil.obj2json(bean));
		} catch (Exception e) {
			_log.error("版本控制请求响应异常:", e);
			try {
				this.responseContent(session, JacksonUtil.obj2json(ResponseFactory.buildResponse(Retcode.ERROR)));
			} catch (Exception e1) {
			}
		}
	}

	/**
	 * 将xml报文解析为hashmap对象 解析出错返回一个空的hashmap
	 * 
	 * @param reqXml
	 *            解析的xml文本
	 * @return
	 */
	private Map<String, String> getMustElemInfoMap(String reqXml) {
		try {
			Map<String, String> reqXmlMap = XmlUtils.getMustElemInfoMap(reqXml);
			return reqXmlMap;
		} catch (Exception e) {
			_log.error("将请求报文解析为hashmap出错" + reqXml + e);
			return new HashMap<String, String>();
		}
	}

	/**
	 * 获取请求中的xml报文
	 * 
	 * @param req
	 * @return
	 */
	private String getRequXml(Object req) {
		/* 接收请求，解析POST报文体 */
		MutableHttpRequest request = (MutableHttpRequest) req;
		IoBuffer content = (IoBuffer) request.getContent();
		byte[] conBytes = new byte[content.limit()];
		content.get(conBytes);
		String reqXML = new String(conBytes);
		return reqXML;
	}

	/**
	 * 返回响应给商户的方法
	 * 
	 * @param session
	 * @param responseStr
	 */
	private void responseContent(HSessionInf session, String responseStr) {
		try {
			/* 第四步：返回 */
			_log.info("返回的报文如下:\n" + responseStr);
			MutableHttpResponse res = IHttp.makeResp(new IHttp.HResponse(), IHttp.HConst.SC_OK, "", null, "text/plain",
					responseStr.getBytes());
			session.write(res);
		} catch (Exception e) {
			_log.error("", e);
			session.close("");
		}
	}

}
