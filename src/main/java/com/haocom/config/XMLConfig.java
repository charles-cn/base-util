package com.haocom.config;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;

/**
 * xml�����������Ϣ����. <br>
 * ��ȡxml�����������Ϣ.
 * <p>
 * Copyright: Copyright (c) 2009-3-18
 * <p>
 * Company: 
 * <p>
 * Author: 
 * <p>
 * Version: 1.0
 * <p>
 * ˵����
 * <p>
 * <ul>
 * <li>���ڵ�Ϊ &lt;project&gt; �����մ�ͳxml��ʽ��ȡ<br/>
 * ��ʽ�� &lt;property name=&quot;������&quot;&gt;����ֵ&lt;/property&gt;</li>
 * <li>���ڵ�Ϊ &lt;config&gt; �������¸�ʽ��ȡ<br/>
 * ��ʽ���⣬�ڵ�·����Ϊ���������ڵ��text��Ϊ����ֵ</li>
 * <li>����:<br/>
 * (1)������һ�������ļ�config.xml
 * 
 * <pre>
 * &lt;?xml version=&quot;1.0&quot; encoding=&quot;gb2312&quot;?&gt;
 * &lt;config&gt;
 * 	&lt;!-- ���ó�ʱʱ�� ��λ��,������д��Ĭ��5��--&gt;
 * 	&lt;re_time_out&gt;10&lt;/re_time_out&gt;
 * 
 * 	&lt;!-- �ύ����ҳ��ַ,���� --&gt;
 * 	&lt;re_url&gt;http://www.125600.com/CustomerReg&lt;/re_url&gt;
 * 
 * 	&lt;systime&gt;
 * 		&lt;!-- �������м��ʱ��(��λ��),������д��Ĭ��Ϊ10�� --&gt;
 * 		&lt;re_sleep_time&gt;2&lt;/re_sleep_time&gt;
 * 	&lt;/systime&gt;
 * &lt;/config&gt;
 * </pre>
 * 
 * (2)��ô�ڳ����п���дһ����ȡ�����ļ�ֵ���࣬������ʾ��<br/>
 * 
 * <pre>
 * 
 * public class Config extends XMLConfig {
 * 
 * 	static final String CONFIG_FILENAME = &quot;config/config.xml&quot;;
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
 * 	Config() throws Exception {
 * 		super(&quot;config&quot;, CONFIG_FILENAME, true);
 * 	}
 * 
 * 	//��ȡ�������м��ʱ��.
 * 	public int getSleepTime() {
 * 		return getInteger(&quot;systime.re_sleep_time&quot;, 10);
 * 	}
 * 
 * 	//��ȡ�õ���ʱʱ��.
 * 	public int getTimeOut() {
 * 		return getInteger(&quot;re_time_out&quot;, 5);
 * 	}
 * 
 * 	//��ȡ�ύ����ҳ��ַ.
 * 	public String getURL() {
 * 		return getValue(&quot;re_url&quot;);
 * 	}
 * }
 * </pre>
 * 
 * (3)�ڳ�������ʱ��Ҫ��ʼ�������ļ�������Config.init();<br/>
 * (4)Ȼ���ڳ���ĸ������������Ҫʹ�������ļ���ֵ������ȡһ�������ļ���ʵ����Ȼ����÷�����ȡ��Ӧ��ֵ����:<br/>
 * <ul>
 * 1����ȡʵ����Config sysconfig = Config.getInstance();<br/>
 * 2�����÷�����String url = sysconfig.getURL();<br/>
 * 3����ȡ�����url = "http://www.125600.com/CustomerReg"; <br/>
 * </ul>
 * </ul>
 */
public class XMLConfig extends FileConfig {

	/**
	 * ���췽��
	 * 
	 * @param name
	 *            ��������
	 * @param filename
	 *            �ļ�����
	 * @throws Exception
	 */
	public XMLConfig(String name, String filename) throws Exception {
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
		throw new Exception("XMLConfig�ݲ�֧�ִ˷���");
	}
}
