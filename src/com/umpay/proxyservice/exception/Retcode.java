package com.umpay.proxyservice.exception;

/**
 * 返回码常量类
 * 
 */
public enum Retcode {
	SUCCESS {
		public String getName() {
			return "成功 ";
		}

		public String getCode() {
			return "0000";
		}
	},
	RELOAD_CLASS_IS_NULL {
		public String getName() {
			return "实时加载服务创建失败，需要实时加载的类为空";
		}

		public String getCode() {
			return "0101";
		}
	},
	CLASS_INIT_FAIL {
		public String getName() {
			return "配置类初始化失败";
		}

		public String getCode() {
			return "0102";
		}
	},
	CLASS_INSTANCE_NOT_FOUND {
		public String getName() {
			return "配置类未找到";
		}

		public String getCode() {
			return "0103";
		}
	},
	DS_INFO_NOT_FOUND {
		public String getName() {
			return "数据源信息未找到";
		}

		public String getCode() {
			return "0104";
		}
	},
	ROUTING_NOT_FOUND {
		public String getName() {
			return "通道未找到";
		}

		public String getCode() {
			return "0105";
		}
	},
	PYTHON_FILE_NOT_FOUND {
		public String getName() {
			return "python文件未找到";
		}

		public String getCode() {
			return "0106";
		}
	},
	WAIT_TIME_NOT_SET {
		public String getName() {
			return "等待时间未设置";
		}

		public String getCode() {
			return "0107";
		}
	},
	STREAM_READ_ERROR {
		public String getName() {
			return "进程输入流读取异常";
		}

		public String getCode() {
			return "0108";
		}
	},
	STREAM_TEAD_TIME_OUT {
		public String getName() {
			return "进程输入流读取超时";
		}

		public String getCode() {
			return "0109";
		}
	},
	ERROR_STREAM_INFO {
		public String getName() {
			return "异常读入流有值";
		}

		public String getCode() {
			return "0110";
		}
	},
	BACK_2_RESERVE_ERROR {
		public String getName() {
			return "实时版本回退到备份版本异常";
		}

		public String getCode() {
			return "0111";
		}
	},
	PUB_NEW_VERSION_ERROR {
		public String getName() {
			return "版本发布失败";
		}

		public String getCode() {
			return "0112";
		}
	},
	NEED_CHECK_VERSION {
		public String getName() {
			return "从新发布版本之前请先确认当前两个版本中哪个是可用版本";
		}

		public String getCode() {
			return "0113";
		}
	},
	NO_RESERVE_CAN_BANK {
		public String getName() {
			return "没有备份版本可以回退";
		}

		public String getCode() {
			return "0114";
		}
	},
	VERSION_CONFIG_EXCHANGE_ERROR {
		public String getName() {
			return "版本配置信息更换失败";
		}

		public String getCode() {
			return "0114";
		}
	},
	ERROR {
		public String getName() {
			return "内部错误";
		}

		public String getCode() {
			return "9999";
		}
	};
	public abstract String getName();

	public abstract String getCode();
}
