package com.umpay.proxyservice.util;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * 文字处理工具
 * 
 * @author huaxiaoqiang
 * @date 2016年6月15日 上午10:18:47
 */
public class StringUtil {
	private static SimpleDateFormat sdf = new SimpleDateFormat(
			"yyyyMMddHHmmssSSS");

	private static final String DEFAULT_END = "end";
	private static final String SEP_DOT = ",";
	private static final String DEFAULT_VALUE = "0";

	/**
	 * 判断是不是文件, .后面有文字
	 * 
	 * @author huaxiaoqiang
	 * @param str
	 * @return
	 * @date 2016年5月17日 上午11:24:35
	 */
	public static boolean isFileName(String str) {
		Pattern pattern = Pattern.compile("\\.\\w+", Pattern.CASE_INSENSITIVE);
		return pattern.matcher(str).find();
	}

	/**
	 * 正则校验
	 * 
	 * @author huaxiaoqiang
	 * @param str
	 * @param reg
	 * @return
	 * @date 2016年5月17日 下午4:26:15|2016年6月13日 下午2:47:15 修改
	 */
	public static boolean match(String str, String reg) {
		Pattern pattern = Pattern.compile(reg);
		return pattern.matcher(str).matches();
	}
	
	
	/**
	 * 正则校验find
	 * 
	 * @author huaxiaoqiang
	 * @param str
	 * @param reg
	 * @return
	 * @date 2016年7月12日 下午4:26:15
	 */
	public static boolean find(String str, String reg) {
		Pattern pattern = Pattern.compile(reg);
		return pattern.matcher(str).find();
	}

	/**
	 * 整数类型,最多10位
	 * 
	 * @author huaxiaoqiang
	 * @param str
	 * @return
	 * @date 2016年5月17日 下午5:49:47
	 */
	public static boolean isInteger(String str) {
		Pattern pattern = Pattern
				.compile("\\d{1,10}", Pattern.CASE_INSENSITIVE);
		return pattern.matcher(str).matches();
	}

	/**
	 * 是否为空
	 * 
	 * @author huaxiaoqiang
	 * @param strs
	 * @return
	 * @date 2016年5月18日 上午9:44:06 修改
	 */
	public static boolean isEmpty(String... strs) {
		if (strs==null) {
			return true;
		}
		
		for (String str : strs)
			if (str == null || str.equals(""))
				return true;
		return false;
	}

	/**
	 * 将集合转换为字符串，
	 * 
	 * @author huaxiaoqiang
	 * @param list
	 * @param separator
	 *            分隔符
	 * @return
	 * @date 2016年5月18日 下午2:54:01 | 2016年6月13日 下午2:26:01 修改,最后一个分隔符去掉
	 * 
	 */
	public static String toSpecificLine(List<String> list, String separator) {
		if (list == null || list.isEmpty() || isEmpty(separator))
			return null;
		StringBuffer buffer = new StringBuffer();
		int size = list.size();
		for (int index = 0; index < size - 1; index++) {
			String e = list.get(index);
			buffer.append(e + separator);
		}
		buffer.append(list.get(size - 1));
		return buffer.toString();
	}

	/**
	 * 将集合转换为字符串，
	 * 
	 * @author huaxiaoqiang
	 * @param strs
	 * @param separator
	 * @return
	 * @date 2016年5月18日 下午5:01:43
	 */
	public static String toSpecificLine(String[] strs, String separator) {
		if (strs == null || strs.length < 1 ||  separator==null)
			return null;
		StringBuffer buffer = new StringBuffer();

		int index = 0;
		for (index = 0; index < strs.length - 1; index++) {
			buffer.append(strs[index] + separator);
		}
		buffer.append(strs[index]);
		return buffer.toString();
	}

	/**
	 * 生成日志前缀 记录简要日志:联动时间,时间，流水号，funcode,商户号，序列号，
	 * 
	 * @author huaxiaoqiang
	 * @param separotor 分隔符
	 * @param reqXML
	 * @date 2016年6月21日 下午4:51:43
	 */
	public static String prepareLogArg(Map<String, String> xmlMap,
			String separotor) {
		String result = "";

		if (xmlMap == null) {
			return "";
		}
		String dateTime = xmlMap.get(HttpMap.DATETIME);
		String transId = xmlMap.get(HttpMap.TRANSID);
		String sequenceId = xmlMap.get(HttpMap.SEQUENCE);
		String merId = xmlMap.get(HttpMap.MERID);
		String funcode = xmlMap.get(HttpMap.FUNCODE);
		String umpTime = sdf.format(new Date());
		String[] reqArgs = { umpTime, dateTime, transId, funcode, merId,
				sequenceId };
		result = StringUtil.toSpecificLine(reqArgs, separotor);
		return result;
	}
	/**
	 * 生成日志前缀 
	 * @author huaxiaoqiang
	 * @param xmlMap
	 * @return
	* @date 2016年7月11日 下午1:34:34
	 */
	public static String prepareLogArg(Map<String, String> xmlMap) {
		return prepareLogArg(xmlMap, ",");
	}
	
	/**
	 * 对特定字符串求和<br>
	 * 例如:1,2,2,3,end
	 * 
	 * @author huaxiaoqiang
	 * @param nvs    例如： 1,2,2,3,end
	 * @param ovs   例如: 1,2,2,3,end
	 * @return
	* @date 2016年7月11日 下午1:36:17
	 */
	public static String sumValue(String nvs, String ovs) {
		StringBuilder sv = new StringBuilder();
		String nv[] = nvs.split(SEP_DOT);
		String ov[] = ovs.split(SEP_DOT);

		for (int i = 0; i < nv.length - 1; i++) {
			if (nv[i].length() > 0 && ov[i].length() > 0) {
				long a = new BigDecimal(nv[i]).longValue();
				long b = new BigDecimal(ov[i]).longValue();
				sv.append((a + b));
			} else if (nv[i].length() == 0 && ov[i].length() > 0) {
				sv.append(new BigDecimal(ov[i]).longValue());
			} else if (ov[i].length() == 0 && nv[i].length() > 0) {
				sv.append(new BigDecimal(nv[i]).longValue());
			} else {
				sv.append(DEFAULT_VALUE);
			}
			sv.append(SEP_DOT);
		}
		sv.append(DEFAULT_END);
		return sv.toString();
	}
	/**
	 * 转换成double类型
	 * @author huaxiaoqiang
	 * @param s
	 * @return
	 * @throws Exception
	* @date 2016年7月11日 下午1:42:18
	 */
	public static Double toDouble(String s) throws Exception{
		double result = 0;
		if (!StringUtil.match(s, "^[0-9]+(.[0-9|E]*)?$")) {
			return result;
		}
		try {
			result = Double.valueOf(s);
		} catch (Exception e) {
			throw e;
		}
		return result;

	}
	
	/**
	 * 调用传入对象的tostring 方法，如果传入对象为null则返回空字符串""
	 * @param object
	 * @return
	 */
	public static String obj2str(Object object) {
		if(object==null){
			return "";
		}
		return object.toString();
	}
}
