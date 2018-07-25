package com.umpay.proxyservice.service.impl;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.umpay.proxyservice.exception.BaseException;
import com.umpay.proxyservice.exception.Retcode;
import com.umpay.proxyservice.service.AbstractTaskService;
import com.umpay.proxyservice.util.LogUtil;

/**
 * 支持python的定制类
 * 
 * @author xuxiaojia
 */
public class PythonTaskServiceImpl extends AbstractTaskService {
	private final static Logger _log = LoggerFactory.getLogger(PythonTaskServiceImpl.class);

	/**
	 * 线程池
	 */
	private ExecutorService cachedThreadPool;
	/**
	 * python文件全路径
	 */
	private String pythonFilePath;
	/**
	 * 如果传入参数小于等于0 则创建无界缓存线程池 否则则创建固定最大数的线程池
	 * 
	 * @param threadNums
	 *            线程池线程数
	 * @param pythonFilePath
	 *            python文件全路径
	 */
	public PythonTaskServiceImpl(int threadNums, String pythonFilePath) {
		super();
		this.pythonFilePath = pythonFilePath;
		if (threadNums <= 0) {
			cachedThreadPool = Executors.newCachedThreadPool();
		} else {
			cachedThreadPool = Executors.newFixedThreadPool(threadNums);
		}
	}

	@Override
	protected String absExecute(String reqInfo) throws Exception {
		//将请求报文去掉换行符、回车换行符。并将所有的双引号替换成单引号
		reqInfo = reqInfoForPython(reqInfo);
		// 获取命令行
		String[] cmdArray = initCmdarray(reqInfo);
		_log.info(LogUtil.getInstance().getLogPrefix() + "开始执行脚本:");
		// 执行命令行
		Process process = Runtime.getRuntime().exec(cmdArray);
		// 创建正常输入流读取线程和异常输入流读取线程并执行
		GetInfoFromProcess normalCt = createProssThread(process, StreamType.getInputStream);
		GetInfoFromProcess errorCt = createProssThread(process, StreamType.getErrorStream);
		// 设置耗时标识并等待线程结束
		wait(process, normalCt, errorCt);
		// 结束子处理流线程运行
		normalCt.setStop(true);
		errorCt.setStop(true);
		if (normalCt.isOccurError() || errorCt.isOccurError()) {
			// 异常中断则抛异常
			throw new BaseException(Retcode.STREAM_READ_ERROR);
		} else if (errorCt.isEnd()) {
			// 异常流读取线程 抛出中断返回
			throw new BaseException(Retcode.ERROR_STREAM_INFO, errorCt.getResString());
		} else {
			return normalCt.getResString();
		}
	}
	
	/**
	 * 由于在控制台执行python脚本时要求参数文本都在一行，一旦有换行符相当于按了回车，执行了命令。
	 * 所以要去掉换行符和回车换行符。\r\n和\n(Linux和windows环境)。
	 * 而且，需要将参数文本中所有的"换成'
	 * @param reqInfo
	 * @return
	 */
	private String reqInfoForPython(String reqInfo) {
		//去掉所有的换行符、回车换行符。并将所有的双引号替换成单引号
		return reqInfo.replaceAll("\r\n", "").replaceAll("\n", "").replaceAll("\"", "'");
	}

	/**
	 * 等待线程执行结束
	 * 
	 * @param normalCt
	 * @param errorCt
	 * @return
	 * @throws InterruptedException
	 */
	private void wait(Process process, GetInfoFromProcess normalCt, GetInfoFromProcess errorCt)
			throws InterruptedException {
		_log.info("等待脚本执行结束。");
		process.waitFor();
		while (normalCt.isProcess()&& errorCt.isProcess()) {
			Thread.sleep(1);
		}
		process.destroy();
	}

	private GetInfoFromProcess createProssThread(Process process, StreamType streamType) {
		GetInfoFromProcess normalCt = new GetInfoFromProcess(process, streamType, LogUtil.getInstance().getLogPrefix());
		cachedThreadPool.execute(normalCt);
		return normalCt;
	}

	private String[] initCmdarray(String reqInfo) {
		List<String> list = new ArrayList<String>();
		list.add("python");
		list.add(pythonFilePath);
		list.add(reqInfo);
		String[] strs = new String[list.size()];
		return list.toArray(strs);
	}

	enum StreamType {
		getInputStream {
			@Override
			public InputStream getStream(Process process) {
				return process.getInputStream();
			}
			@Override
			public String streamName() {
				return "正常输出流";
			}
			@Override
			public boolean isEnd(boolean end, String info) {
				// 正产输出流只需要判断end
				return end;
			}
		},
		getErrorStream {
			@Override
			public InputStream getStream(Process process) {
				return process.getErrorStream();
			}
			@Override
			public String streamName() {
				return "异常输出流";
			}
			@Override
			public boolean isEnd(boolean end, String info) {
				// 异常输出流判断是否有输出信息
				return StringUtils.isNotEmpty(info);
			}
		};
		public abstract InputStream getStream(Process process);

		public abstract String streamName();

		public abstract boolean isEnd(boolean end, String info);

	}

	/**
	 * 获取进行输出的信息
	 * 
	 * @author xuxiaojia
	 */
	class GetInfoFromProcess extends Thread {
		// 执行进程
		private Process process;
		// 是否结束标识
		private AtomicBoolean end;
		// 是否停止执行标识
		private AtomicBoolean stop;
		// 是否异常停止标识
		private AtomicBoolean occurError;
		// 线程安全的获取进程打印的信息
		private StringBuffer buffer;
		// 流类型
		private StreamType type;
		// 日志头
		private String logPrfix;

		public String getLogPrifix() {
			return this.logPrfix;
		}

		public void setStop(boolean stop) {
			this.stop.set(stop);
		}

		public boolean isOccurError() {
			return occurError.get();
		}

		public GetInfoFromProcess(Process process, StreamType streamType, String logPrfix) {
			super(logPrfix + streamType.name());
			this.process = process;
			this.end = new AtomicBoolean(false);
			this.stop = new AtomicBoolean(false);
			this.occurError = new AtomicBoolean(false);
			this.buffer = new StringBuffer();
			this.type = streamType;
			this.logPrfix = logPrfix + "获取线程" + this.type.streamName();
		}

		/**
		 * 线程正在执行
		 * 
		 * @return
		 */
		public boolean isProcess() {
			return !isEnd()&&!isOccurError();
		}

		/**
		 * 线程是否结束
		 * 
		 * @return
		 */
		public boolean isEnd() {
			// 防止出现多线程操作list的情况，所以先判断是否end
			return type.isEnd(end.get(), getResString());
		}

		@SuppressWarnings("resource")
		@Override
		public void run() {
			if (process == null) {
				return;
			}
			InputStream stream = type.getStream(process);
			try {
				Scanner scanner = new Scanner(stream);
				//最少执行50次，防止进程执行完毕但是输入流没有读完
				int times=0;
				while (process.isAlive()||times<50) {
					times++;
					while (!stop.get() && scanner.hasNextLine()) {
						buffer.append(scanner.nextLine());
					}
				}
				end.set(true);
				_log.info(logPrfix + "执行完毕，打印:" + getResString());
			} catch (Exception e) {
				this.occurError.set(true);
				_log.error(logPrfix + "执行异常", e);
			} finally {
				// 关闭流
				closeStream(stream);
			}
		}

		private void closeStream(InputStream stream) {
			try {
				if (stream != null) {
					stream.close();
				}
			} catch (Exception e) {
			}
		}

		public String getResString() {
			return buffer.toString();
		}
	}

	@Override
	public void init() throws BaseException {
		_log.info("输入的python路径："+pythonFilePath);
		if (StringUtils.isEmpty(pythonFilePath) || !new File(pythonFilePath).exists()) {
			throw new BaseException(Retcode.PYTHON_FILE_NOT_FOUND);
		}
	}

}
