package com.haocom.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;

/**
 * properties保存的配置信息对象. <br>
 * 读取properties保存的配置信息.
 * <p>
 * Copyright: Copyright (c) 2009-3-18
 * <p>
 * Company: 
 * <p>
 * Author: 
 * <p>
 * Version: 1.0
 * <p>
 * 范例:<br/>
 * (1)以下是一个配置文件sys.properties
 * <ul>
 * #配置文件：修改后自动更新<br/>
 * <p>
 * #传输ip地址<br/>
 * host=192.168.10.57<br/>
 * <p>
 * #传输端口<br/>
 * port=7788<br/>
 * <p>
 * #休眠时间<br/>
 * sys.sleep=20<br/>
 * </ul>
 * (2)那么在程序中可以写一个获取配置文件值的类，如下所示：<br/>
 * 
 * <pre>
 * 
 * public class Config extends PropertiesConfig {
 * 
 * 	static final String CONFIG_FILENAME = &quot;config/sys.properties&quot;;
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
 * 	config() throws Exception {
 * 		super(&quot;config&quot;, CONFIG_FILENAME, true);
 * 	}
 * 
 * 	//获取传输ip地址.
 * 	public String getHost() {
 * 		return getValue(&quot;host&quot;);
 * 	}
 * 
 * 	//获取传输端口.
 * 	public int getPort() {
 * 		return getInteger(&quot;port&quot;, 514);
 * 	}
 * 
 * 	//获取休眠时间.
 * 	public int getSysSleep() {
 * 		return getInteger(&quot;sys.sleep&quot;, 1000);
 * 	}
 * }
 * </pre>
 * 
 * (2)在程序启动时需要初始化配置文件，即：<code>Config.init();</code><br/>
 * (3)然后在程序的各个类中如果需要使用配置文件的值，及获取一个配置文件的实例，然后调用方法获取相应的值即可:
 * <ul>
 * 1、获取实例：Config sysconfig = Config.getInstance();<br/>
 * 2、调用方法：String host = sysconfig.getHost();<br/>
 * 3、获取结果：host = &quot;192.168.10.57&quot;; <br/>
 * </ul>
 */
public class PropertiesConfig extends FileConfig {

	/** Properties配置文件 */
	Properties properties = null;

	/**
	 * 构造方法
	 * 
	 * @param name
	 *            配置名称
	 * @param filename
	 *            文件名称
	 * @throws Exception
	 */
	public PropertiesConfig(String name, String filename) throws Exception {
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
	public PropertiesConfig(String name, String filename, boolean monitor) throws Exception {
		super(name, filename, monitor);
	}

	@Override
	public String getValue(String name) {
		// TODO Auto-generated method stub
		return super.getValue(name);
	}

	@Override
	protected HashMap loadValues() throws Exception {
		properties = new Properties();
		FileInputStream inputStream = new FileInputStream(getFilename());
		try {
			properties.load(inputStream);
		}
		catch (Exception ex) {
			throw ex;
		}
		finally {
			inputStream.close();
		}
		HashMap result = new HashMap();
		Object[] keys = properties.keySet().toArray();
		for (int i = 0; i < keys.length; i++) {
			String key = (String) keys[i];
			String value = properties.getProperty(key);
			if (value != null)
				value = new String(value.getBytes("ISO8859-1"));
			result.put(key.toLowerCase(), value);
		}
		return result;
	}

	@Override
	protected void writeToFile(File file) throws Exception {
		Properties properties = new Properties();
		properties.putAll(this.values);
		FileOutputStream fw = new FileOutputStream(file);
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
			StringBuilder buf = new StringBuilder(500);
			buf.append("name : ").append(this.getName()).append("\r\n");
			buf.append("time : ").append(dateFormat.format(new Date())).append("\r\n");
			properties.store(fw, buf.toString());
			fw.flush();
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		finally {
			fw.close();
		}
	}
}
