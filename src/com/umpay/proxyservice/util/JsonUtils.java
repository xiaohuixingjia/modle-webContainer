package com.umpay.proxyservice.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;

public class JsonUtils {
	public static String getJsonMD5(String jsonStr) throws Exception{
		StringBuilder result = new StringBuilder();
		List<String> list = Json2List(jsonStr);
		Collections.sort(list);
		for(String str:list){
			result.append(str);
		}
		return MD5Utils.getMD5Str(result.toString());
	}
	public static List<String> Json2List(String json) throws Exception {
		List<String> resultList = new ArrayList<String>();
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		try{
			jsonMap = JSON.parseObject(json);
		}catch(Exception e){
			throw new Exception("该结果是字符串");
		}
		for(String key : jsonMap.keySet()){
			String value = jsonMap.get(key).toString();
			try{
				resultList.addAll(Json2List(value));
			}catch(Exception e){
				if(value.charAt(0)=='[' && value.charAt(value.length()-1)==']'){
					try{
						resultList.addAll(Json2List(value.substring(1, value.length()-2)));
					}catch(Exception e2){
						resultList.add(key + "_" + value);
					}
				}else{
					resultList.add(key + "_" + value);
				}
			}
		}
		return resultList;
	}
}
