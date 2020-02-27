package com.haocom.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;

/**
 * properties�����������Ϣ����. <br>
 * ��ȡproperties�����������Ϣ.
 * <p>
 * Copyright: Copyright (c) 2009-3-18
 * <p>
 * Company: 
 * <p>
 * Author: 
 * <p>
 * Version: 1.0
 * <p>
 * ����:<br/>
 * (1)������һ�������ļ�sys.properties
 * <ul>
 * #�����ļ����޸ĺ��Զ�����<br/>
 * <p>
 * #����ip��ַ<br/>
 * host=192.168.10.57<br/>
 * <p>
 * #����˿�<br/>
 * port=7788<br/>
 * <p>
 * #����ʱ��<br/>
 * sys.sleep=20<br/>
 * </ul>
 * (2)��ô�ڳ����п���дһ����ȡ�����ļ�ֵ���࣬������ʾ��<br/>
 * 
 * <pre>
 * 
 * public class Config extends PropertiesConfig {
 * 
 * 	static final String CONFIG_FILENAME = &quot;config/sys.properties&quot;;
 * 
 * 	//����ʵ��.
 * 	private static Config instance;
 * 
 * 	//��ȡ����ʵ��.
 * 	public static Config getInstance() {
 * 		return instance;
 * 	}
 * 
 * 	//��ʼ������ʵ��.
 * 	public synchronized static void init() throws Exception {
 * 		if (instance != null) {
 * 			return;
 * 		}
 * 		instance = new Config();
 * 	}
 * 
 * 	//���󴴽�.
 * 	config() throws Exception {
 * 		super(&quot;config&quot;, CONFIG_FILENAME, true);
 * 	}
 * 
 * 	//��ȡ����ip��ַ.
 * 	public String getHost() {
 * 		return getValue(&quot;host&quot;);
 * 	}
 * 
 * 	//��ȡ����˿�.
 * 	public int getPort() {
 * 		return getInteger(&quot;port&quot;, 514);
 * 	}
 * 
 * 	//��ȡ����ʱ��.
 * 	public int getSysSleep() {
 * 		return getInteger(&quot;sys.sleep&quot;, 1000);
 * 	}
 * }
 * </pre>
 * 
 * (2)�ڳ�������ʱ��Ҫ��ʼ�������ļ�������<code>Config.init();</code><br/>
 * (3)Ȼ���ڳ���ĸ������������Ҫʹ�������ļ���ֵ������ȡһ�������ļ���ʵ����Ȼ����÷�����ȡ��Ӧ��ֵ����:
 * <ul>
 * 1����ȡʵ����Config sysconfig = Config.getInstance();<br/>
 * 2�����÷�����String host = sysconfig.getHost();<br/>
 * 3����ȡ�����host = &quot;192.168.10.57&quot;; <br/>
 * </ul>
 */
public class PropertiesConfig extends FileConfig {

	/** Properties�����ļ� */
	Properties properties = null;

	/**
	 * ���췽��
	 * 
	 * @param name
	 *            ��������
	 * @param filename
	 *            �ļ�����
	 * @throws Exception
	 */
	public PropertiesConfig(String name, String filename) throws Exception {
		super(name, filename);
	}

	/**
	 * ���췽��
	 * 
	 * @param name
	 *            ��������
	 * @param filename
	 *            �ļ�����
	 * @param monitor
	 *            �Ƿ�����ļ����
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
