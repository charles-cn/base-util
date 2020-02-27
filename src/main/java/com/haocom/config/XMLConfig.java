package com.haocom.config;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;

/**
 * xml保存的配置信息对象. <br>
 * 读取xml保存的配置信息.
 * <p>
 * Copyright: Copyright (c) 2009-3-18
 * <p>
 * Company: 
 * <p>
 * Author: 
 * <p>
 * Version: 1.0
 * <p>
 * 说明：
 * <p>
 * <ul>
 * <li>根节点为 &lt;project&gt; 的则按照传统xml格式读取<br/>
 * 格式： &lt;property name=&quot;参数名&quot;&gt;参数值&lt;/property&gt;</li>
 * <li>根节点为 &lt;config&gt; 的则按照新格式读取<br/>
 * 格式任意，节点路径作为参数名，节点的text作为参数值</li>
 * <li>范例:<br/>
 * (1)以下是一个配置文件config.xml
 * 
 * <pre>
 * &lt;?xml version=&quot;1.0&quot; encoding=&quot;gb2312&quot;?&gt;
 * &lt;config&gt;
 * 	&lt;!-- 设置超时时间 单位秒,若不填写则默认5秒--&gt;
 * 	&lt;re_time_out&gt;10&lt;/re_time_out&gt;
 * 
 * 	&lt;!-- 提交的网页地址,必填 --&gt;
 * 	&lt;re_url&gt;http://www.125600.com/CustomerReg&lt;/re_url&gt;
 * 
 * 	&lt;systime&gt;
 * 		&lt;!-- 程序运行间隔时间(单位秒),若不填写则默认为10秒 --&gt;
 * 		&lt;re_sleep_time&gt;2&lt;/re_sleep_time&gt;
 * 	&lt;/systime&gt;
 * &lt;/config&gt;
 * </pre>
 * 
 * (2)那么在程序中可以写一个获取配置文件值的类，如下所示：<br/>
 * 
 * <pre>
 * 
 * public class Config extends XMLConfig {
 * 
 * 	static final String CONFIG_FILENAME = &quot;config/config.xml&quot;;
 * 
 * 	//对象实例.
 * 	private static Config instance;
 * 
 * 	//获取对象实例.
 * 	public static Config getInstance() {
 * 		return instance;
 * 	}
 * 
 * 	//初始化对象实例.
 * 	public synchronized static void init() throws Exception {
 * 		if (instance != null) {
 * 			return;
 * 		}
 * 		instance = new Config();
 * 	}
 * 
 * 	//对象创建.
 * 	Config() throws Exception {
 * 		super(&quot;config&quot;, CONFIG_FILENAME, true);
 * 	}
 * 
 * 	//获取程序运行间隔时间.
 * 	public int getSleepTime() {
 * 		return getInteger(&quot;systime.re_sleep_time&quot;, 10);
 * 	}
 * 
 * 	//获取得到超时时间.
 * 	public int getTimeOut() {
 * 		return getInteger(&quot;re_time_out&quot;, 5);
 * 	}
 * 
 * 	//获取提交的网页地址.
 * 	public String getURL() {
 * 		return getValue(&quot;re_url&quot;);
 * 	}
 * }
 * </pre>
 * 
 * (3)在程序启动时需要初始化配置文件，即：Config.init();<br/>
 * (4)然后在程序的各个类中如果需要使用配置文件的值，及获取一个配置文件的实例，然后调用方法获取相应的值即可:<br/>
 * <ul>
 * 1、获取实例：Config sysconfig = Config.getInstance();<br/>
 * 2、调用方法：String url = sysconfig.getURL();<br/>
 * 3、获取结果：url = "http://www.125600.com/CustomerReg"; <br/>
 * </ul>
 * </ul>
 */
public class XMLConfig extends FileConfig {

	/**
	 * 构造方法
	 * 
	 * @param name
	 *            配置名称
	 * @param filename
	 *            文件名称
	 * @throws Exception
	 */
	public XMLConfig(String name, String filename) throws Exception {
		super(name, filename);
	}

	/**
	 * 构造方法
	 * 
	 * @param name
	 *            配置名称
	 * @param filename
	 *            文件名称
	 * @param monitor
	 *            是否跟踪文件变更
	 * @throws Exception
	 */
	public XMLConfig(String name, String filename, boolean monitor) throws Exception {
		super(name, filename, monitor);
	}

	@Override
	protected HashMap loadValues() throws Exception {
		SAXBuilder builder = new SAXBuilder();
		Document doc = builder.build(new File(getFilename()));
		Element root = doc.getRootElement();
		if (root.getName().equals("project")) {
			return loadValues_SimpleXML(root);
		}
		if (root.getName().equals("config")) {
			return loadValues_ComplexXML(root);
		}
		return new HashMap();
	}

	private HashMap loadValues_ComplexXML(Element root) {
		HashMap values = new HashMap();
		List elements = root.getChildren();
		for (int i = 0; i < elements.size(); i++) {
			loadValues_ComplexXML((Element) elements.get(i), "", values);
		}
		return values;
	}

	private void loadValues_ComplexXML(Element element, String name, HashMap values) {
		name = name + "." + element.getName();
		if (name.startsWith(".")) {
			name = name.substring(1);
		}
		name = name.toLowerCase();
		String value = element.getTextTrim();
		if (value != null && value.length() > 0) {
			values.put(name, value);
		}

		List elements = element.getChildren();
		for (int i = 0; i < elements.size(); i++) {
			Element sub = (Element) elements.get(i);
			loadValues_ComplexXML(sub, name, values);
		}

	}

	private HashMap loadValues_SimpleXML(Element root) {
		List elements = root.getChildren("property");
		HashMap values = new HashMap();
		for (int i = 0; i < elements.size(); i++) {
			Element element = (Element) elements.get(i);
			String key = element.getAttributeValue("name").toLowerCase();
			String value = element.getTextTrim();
			values.put(key, value);
		}
		return values;
	}

	@Override
	protected void writeToFile(File file) throws Exception {
		throw new Exception("XMLConfig暂不支持此方法");
	}
}
