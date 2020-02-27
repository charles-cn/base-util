package com.haocom.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentHashMap;

import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

/**
 * XML文档解析器. <br>
 * XML文档解析器,引用包为JDOM.
 * <p>
 * Copyright: Copyright (c) Sep 16, 2008
 * <p>
 * Company:
 * <p>
 * Author: ywh
 * <p>
 * Version: 1.0
 * <p>
 * 
 * <pre>
 * 注：本组件不支持命名空间。
 * 
 * 使用范例如下：
 * 
 * XML文件（my_test.xml）：
 * &lt;?xml version=&quot;1.0&quot; encoding=&quot;UTF-8&quot;?&gt;
 * &lt;Envelope&gt;
 * 	&lt;Body&gt;
 * 		&lt;regSeed&gt;
 * 			&lt;request&gt;
 * 				&lt;accessType&gt;ssdsds&lt;/accessType&gt;
 * 				&lt;sessionID&gt;&lt;/sessionID&gt;
 * 				&lt;transactionID&gt;&lt;/transactionID&gt;
 * 				&lt;userType&gt;&lt;/userType&gt;
 * 				&lt;userid attr1=&quot;value1&quot;&gt;&lt;/userid&gt;
 * 				&lt;version&gt;&lt;/version&gt;
 * 				&lt;servicetype&gt;getNodeValue&lt;/servicetype&gt;
 * 				&lt;seed&gt;
 * 					&lt;name&gt;&lt;/name&gt;
 * 					&lt;introduce&gt;&lt;/introduce&gt;
 * 					&lt;type&gt;&lt;/type&gt;
 * 					&lt;size&gt;&lt;/size&gt;
 * 					&lt;policy&gt;&lt;/policy&gt;
 * 					&lt;textcontent&gt;&lt;/textcontent&gt;
 * 					&lt;content&gt;测试测试，这是测试ing&tilde;&lt;/content&gt;
 * 				&lt;/seed&gt;
 * 				&lt;playid&gt;&lt;/playid&gt;
 * 				&lt;filterreason&gt;&lt;/filterreason&gt;
 * 			&lt;/request&gt;
 * 		&lt;/regSeed&gt;
 * 	&lt;/Body&gt;
 * &lt;/Envelope&gt;
 * 
 * 代码：
 * XMLParser xParser = new XMLParser(&quot;./XMLParser/my_test.xml&quot;);
 * 
 * // 根据节点名获取节点内容
 * xParser.getNodeValue(&quot;Body.regSeed.request.seed.content&quot;);
 * 
 * // 根据父节点名获取所有子节点名
 * String[] children = xParser.getChildren(&quot;Body.regSeed.request&quot;);
 * 
 * // 根据节点名删除节点
 * xParser.removeNode(&quot;Body.regSeed.request.servicetype&quot;);
 * 
 * // 设置指定节点的内容
 * xParser.setNodeValue(&quot;Body.regSeed.request.version&quot;, &quot;1.0.7&quot;);
 * xParser.setNodeValue(&quot;Body.regSeed.request&quot;, &quot;userid&quot;, &quot;attr1&quot;, &quot;value1&quot;, &quot;nodeValue&quot;);
 * 
 * // 添加节点
 * xParser.addNode(&quot;Body.regSeed.request.version&quot;, &quot;zzz&quot;, &quot;1.0.7&quot;);
 * 
 * // 保存xml文件
 * xParser.saveAsFile(&quot;./XMLParser/SaveAs.xml&quot;, &quot;utf-8&quot;);
 * </pre>
 */

public class XMLParser {

	/** 文档 */
	private Document doc;

	/** 文件 */
	private File file;

	/** 定义一个map，用于存放xml文档的节点和值 */
	private Map<String, String> propertyCache;

	/**
	 * 构造器，从输入流中构造XML文档
	 * 
	 * @param in
	 *            输入流
	 * @throws Exception
	 */
	public XMLParser(InputStream in) throws Exception {
		propertyCache = new ConcurrentHashMap<String, String>();
		SAXBuilder builder = new SAXBuilder();
		doc = builder.build(in);
	}

	/**
	 * 构造器，从读取器中构造XML文档
	 * 
	 * @param reader
	 *            读取器
	 * @throws Exception
	 */
	public XMLParser(Reader reader) throws Exception {
		propertyCache = new ConcurrentHashMap<String, String>();
		SAXBuilder builder = new SAXBuilder();
		doc = builder.build(reader);
	}

	/**
	 * 构造器，从文件中构造XML文档
	 * 
	 * @param fileName
	 *            文件名
	 * @throws Exception
	 */
	public XMLParser(String fileName) throws Exception {
		propertyCache = new ConcurrentHashMap<String, String>();
		file = new File(fileName);
		SAXBuilder builder = new SAXBuilder();
		doc = builder.build(file);
	}

	/**
	 * 添加节点
	 * 
	 * @param parent
	 *            父节点
	 * @param name
	 *            要添加的子节点名称
	 * @param value
	 *            子节点的值
	 */
	public void addNode(String parent, String name, String value) {
		String elementName = parent + "." + name;
		Element element = findNode(parsePropertyName(elementName));
		if (element == null) {
			element = findNode(parsePropertyName(parent));
			element.addContent(new Element(name));
			element = element.getChild(name);
			element.setText(value);
		} else {
			element.setText(value);
		}
		propertyCache.put(elementName, value);
	}

	/**
	 * 查找节点
	 * 
	 * @param propName
	 *            节点名列表
	 * @return 节点元素
	 */
	private Element findNode(String[] propName) {
		Element element = doc.getRootElement();
		int index = 0;
		for (int i = 0; i < propName.length; i++) {
			index = propName[i].indexOf(":");
			if (index != -1) {
				element = element.getChild(propName[i].substring(index + 1), element.getNamespace());
			} else {
				element = element.getChild(propName[i]);
			}
		}

		return element;
	}

	/**
	 * 获取子节点名
	 * 
	 * @param parent
	 *            父节点名
	 * @return 子节点名称
	 */
	public String[] getChildren(String parent) {
		Element element = findNode(parsePropertyName(parent));
		List<?> children = element.getChildren();
		int childCount = children.size();
		String childrenNames[] = new String[childCount];
		for (int i = 0; i < childCount; i++) {
			childrenNames[i] = ((Element) children.get(i)).getName();
		}

		return childrenNames;
	}

	/**
	 * 获得节点对象
	 * 
	 * @param name
	 *            节点标签名
	 * @return 节点对象
	 */
	private Element getElement(String name) {
		return findNode(parsePropertyName(name));
	}

	/**
	 * 获得节点的值
	 * 
	 * @param nodeName
	 *            节点标签名
	 * @return 节点值
	 */
	public String getNodeValue(String nodeName) {
		Element e = getElement(nodeName);
		if (e == null) {
			return null;
		}
		String value = e.getText();
		if (value.equals("")) {
			return null;
		} else {
			value = value.trim();
			propertyCache.put(nodeName, value);
			return value;
		}
	}

	/**
	 * 获取指定节点的值
	 * 
	 * @param parentNode
	 *            父节点名
	 * @param node
	 *            指定子节点名称
	 * @param attrName
	 *            指定子节点属性名称
	 * @param attrValue
	 *            指定子节点属性值
	 * @return 指定子节点的值
	 */
	public String getNodeValue(String parentNode, String node, String attrName, String attrValue) {

		Element element = findNode(parsePropertyName(parentNode));
		List<?> eList = element.getChildren();
		Iterator<?> it = eList.iterator();
		Element ce = null;
		Attribute attr = null;

		while (it.hasNext()) {
			ce = (Element) it.next();
			if (node.equals(ce.getName())) {
				attr = ce.getAttribute(attrName);
				if (attr != null) {
					if (attrValue.equals(attr.getValue())) {
						return ce.getText();
					}
				}
			}
		}
		return null;
	}

	/**
	 * 将XML文档输出到指定输出流
	 * 
	 * @param out
	 *            输出流，网络IO或磁盘IO
	 * @param encoding
	 *            指定编码格式
	 * @throws Exception
	 */
	public synchronized void output(OutputStream out, String encoding) throws Exception {
		Format format = Format.getCompactFormat();
		format.setEncoding(encoding);
		XMLOutputter xmlOut = new XMLOutputter();
		xmlOut.setFormat(format);
		xmlOut.output(doc, out);
	}

	/**
	 * 分割节点名，以"."为分割符
	 * 
	 * @param name
	 *            节点名
	 * @return 属性列表
	 */
	private String[] parsePropertyName(String name) {
		int size = 1;
		for (int i = 0; i < name.length(); i++) {
			if (name.charAt(i) == '.') {
				size++;
			}
		}

		String propName[] = new String[size];
		StringTokenizer tokenizer = new StringTokenizer(name, ".");
		for (int i = 0; tokenizer.hasMoreTokens(); i++) {
			propName[i] = tokenizer.nextToken();
		}

		return propName;
	}

	/**
	 * 删除节点
	 * 
	 * @param name
	 *            节点名
	 */
	public void removeNode(String name) {
		String[] propName = parsePropertyName(name.substring(0, name.lastIndexOf(".")));
		Element element = findNode(propName);
		if (element != null) {
			element.removeChild(name.substring(name.lastIndexOf(".") + 1));
		}
	}

	/**
	 * 将XML文档保存到文件
	 * 
	 * @param fileName
	 *            文件名
	 * @param encoding
	 *            指定编码格式
	 * @throws Exception
	 */
	public synchronized void saveAsFile(String fileName, String encoding) throws Exception {
		BufferedOutputStream out = null;

		try {
			File f = new File(fileName);
			out = new BufferedOutputStream(new FileOutputStream(f));
			output(out, encoding);
		}
		catch (Exception e) {
			throw e;
		}
		finally {
			out.close();
		}
	}

	/**
	 * 修改节点的值
	 * 
	 * @param name
	 *            要修改的节点的名称
	 * @param value
	 *            修改后的节点值
	 */
	public void setNodeValue(String name, String value) {
		propertyCache.put(name, value);
		Element element = findNode(parsePropertyName(name));
		if (element != null) {
			element.setText(value);

		}
	}

	/**
	 * 设置指定父节点、指定属性的节点值
	 * 
	 * @param parentNode
	 *            父节点
	 * @param node
	 *            指定节点名称
	 * @param attrName
	 *            指定节点的属性名称
	 * @param attrValue
	 *            指定节点的属性值
	 * @param nodeValue
	 *            指定节点值
	 */
	public void setNodeValue(String parentNode, String node, String attrName, String attrValue, String nodeValue) {
		Element element = findNode(parsePropertyName(parentNode));
		List<?> eList = element.getChildren();
		Iterator<?> it = eList.iterator();
		Element ce = null;
		Attribute attr = null;

		while (it.hasNext()) {
			ce = (Element) it.next();
			if (node.equals(ce.getName())) {
				attr = ce.getAttribute(attrName);
				if (attr != null) {
					if (attrValue.equals(attr.getValue())) {
						ce.setText(nodeValue);
						return;
					}
				}
			}
		}
	}
}
