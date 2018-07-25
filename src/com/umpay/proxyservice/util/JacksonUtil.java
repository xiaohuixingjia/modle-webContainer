package com.umpay.proxyservice.util;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

/**
 * 将json串和对象之间相互转化的常用工具类
 * 
 * @author xuxiaojia
 */
public class JacksonUtil {
	private static ObjectMapper mapper=new ObjectMapper();;


	/**
	 * 将json串转换为指定的java对象 并创建该对象的实例进行返回
	 * @param jsonString  要转换的json字符串
	 * @param classOft    要转换成的对象的类
	 * @return
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 * @throws Exception
	 */
	public static <T> T jsonString2Obj(String jsonString, Class<T> classOft) throws JsonParseException, JsonMappingException, IOException {
		T t = mapper.readValue(jsonString, classOft);
		return t;
	}
	
	/**
	 * 将java对象转换为json字符串
	 * @param t  java对象
	 * @return
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public static <T> String obj2json(T t) throws JsonGenerationException, JsonMappingException, IOException{
		return mapper.writeValueAsString(t);
	}

}
