 此web容器现在支持 java代码写的模型 和 python代码写的模型
 
 Java语言开发的模型
详述
Java需要继承modelService.jar包中的com.umpay.model.service.ModelService 接口并实现其方法，该接口传入的是web容器接收到的报文，容器会将该接口实现类的返回当做返回值，返回给调用端。如果建模方jar包有任何校验未通过或者异常发生，则抛出异常即可，但是web容器会返回空字符串给调用端。

商户侧工作
1、提供实现了接口的jar包以及用到的第三方jar包
2、提供ModelService接口的类的全路径
3、实现接口的jar包中如果有配置文件，请单独提供一份，否则会出现读取不到配置文件的情况

注意事项
1、建模方的jar包可以用jdk版本为1.6到1.8的java编译器编译。 
2、建模方的jar包里实现ModelService接口的实现类在web容器应用时会生成单例对象来调用，所以需要注意多线程并发下是否存在线程安全问题
3、建模方需要自己实现解析请求的xml报文以及响应为xml的报文
jar包中接口对象实例生成说明
     建模方在建模后可以用以下代码测试自己封装的jar包是否可用
		String jarFullFilePath="";
		String className="";
		URL url = new URL("file:" + jarFullFilePath);
		URLClassLoader myClassLoader1=new URLClassLoader(new URL[]{url}, Thread.currentThread().getContextClassLoader());
		Class<?> myClass = null;
		myClass = myClassLoader1.loadClass(className);
		ModelService test = (ModelService) myClass.newInstance();
		test.process("<xml>...");

Demo
import com.umpay.model.service.ModelService;

public class ModelServiceImpl implements ModelService{

	@Override
	public String process(String arg0) throws Exception {
      # arg0 是请求的xml报文
		System.out.println("执行建模逻辑");
# 返回的xml不一定非要是resu标签，只是举个例子而已
		return “<resu>xxx</resu>”;
	}

}


3.2 Python语言开发的模型
详述
Web容器会以调用脚本的方式调用建模方提供的python脚本，web容器接收的报文会当做调用脚本的第一个参数传入，python脚本中所有print输出的信息会被web容器收集拼成一个大字符串返回给调用端

商户侧工作
1、	需要建模方先提供所使用的python版本以及类库， 以及写好的python脚本

注意事项
  1 python脚本的print只能输出要返回给调用端的信息，并且请以xml的格式进行返回，容器只负责将所有的print输出的信息做拼接，不负责转换
  2 python脚本运行时出现异常则容器会返回调用端空字符串
  3 python需要解析请求的xml报文以及生成响应的xml报文

Demo

#!/usr/bin/python
#coding:utf-8
import sys
import string
import time
# x为传入的请求报文
x=sys.argv[1]
#解析报文…..
#输出返回结果
# 返回的xml不一定非要是resu标签，只是举个例子而已
print '<resu>xxx</resu>'
 