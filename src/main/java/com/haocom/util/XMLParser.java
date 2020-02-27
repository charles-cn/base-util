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
 * XML�ĵ�������. <br>
 * XML�ĵ�������,���ð�ΪJDOM.
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
 * ע���������֧�������ռ䡣
 * 
 * ʹ�÷������£�
 * 
 * XML�ļ���my_test.xml����
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
 * 					&lt;content&gt;���Բ��ԣ����ǲ���ing&tilde;&lt;/content&gt;
 * 				&lt;/seed&gt;
 * 				&lt;playid&gt;&lt;/playid&gt;
 * 				&lt;filterreason&gt;&lt;/filterreason&gt;
 * 			&lt;/request&gt;
 * 		&lt;/regSeed&gt;
 * 	&lt;/Body&gt;
 * &lt;/Envelope&gt;
 * 
 * ���룺
 * XMLParser xParser = new XMLParser(&quot;./XMLParser/my_test.xml&quot;);
 * 
 * // ���ݽڵ�����ȡ�ڵ�����
 * xParser.getNodeValue(&quot;Body.regSeed.request.seed.content&quot;);
 * 
 * // ���ݸ��ڵ�����ȡ�����ӽڵ���
 * String[] children = xParser.getChildren(&quot;Body.regSeed.request&quot;);
 * 
 * // ���ݽڵ���ɾ���ڵ�
 * xParser.removeNode(&quot;Body.regSeed.request.servicetype&quot;);
 * 
 * // ����ָ���ڵ������
 * xParser.setNodeValue(&quot;Body.regSeed.request.version&quot;, &quot;1.0.7&quot;);
 * xParser.setNodeValue(&quot;Body.regSeed.request&quot;, &quot;userid&quot;, &quot;attr1&quot;, &quot;value1&quot;, &quot;nodeValue&quot;);
 * 
 * // ��ӽڵ�
 * xParser.addNode(&quot;Body.regSeed.request.version&quot;, &quot;zzz&quot;, &quot;1.0.7&quot;);
 * 
 * // ����xml�ļ�
 * xParser.saveAsFile(&quot;./XMLParser/SaveAs.xml&quot;, &quot;utf-8&quot;);
 * </pre>
 */

public class XMLParser {

	/** �ĵ� */
	private Document doc;

	/** �ļ� */
	private File file;

	/** ����һ��map�����ڴ��xml�ĵ��Ľڵ��ֵ */
	private Map<String, String> propertyCache;

	/**
	 * �����������������й���XML�ĵ�
	 * 
	 * @param in
	 *            ������
	 * @throws Exception
	 */
	public XMLParser(InputStream in) throws Exception {
		propertyCache = new ConcurrentHashMap<String, String>();
		SAXBuilder builder = new SAXBuilder();
		doc = builder.build(in);
	}

	/**
	 * ���������Ӷ�ȡ���й���XML�ĵ�
	 * 
	 * @param reader
	 *            ��ȡ��
	 * @throws Exception
	 */
	public XMLParser(Reader reader) throws Exception {
		propertyCache = new ConcurrentHashMap<String, String>();
		SAXBuilder builder = new SAXBuilder();
		doc = builder.build(reader);
	}

	/**
	 * �����������ļ��й���XML�ĵ�
	 * 
	 * @param fileName
	 *            �ļ���
	 * @throws Exception
	 */
	public XMLParser(String fileName) throws Exception {
		propertyCache = new ConcurrentHashMap<String, String>();
		file = new File(fileName);
		SAXBuilder builder = new SAXBuilder();
		doc = builder.build(file);
	}

	/**
	 * ��ӽڵ�
	 * 
	 * @param parent
	 *            ���ڵ�
	 * @param name
	 *            Ҫ��ӵ��ӽڵ�����
	 * @param value
	 *            �ӽڵ��ֵ
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
	 * ���ҽڵ�
	 * 
	 * @param propName
	 *            �ڵ����б�
	 * @return �ڵ�Ԫ��
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
	 * ��ȡ�ӽڵ���
	 * 
	 * @param parent
	 *            ���ڵ���
	 * @return �ӽڵ�����
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
	 * ��ýڵ����
	 * 
	 * @param name
	 *            �ڵ��ǩ��
	 * @return �ڵ����
	 */
	private Element getElement(String name) {
		return findNode(parsePropertyName(name));
	}

	/**
	 * ��ýڵ��ֵ
	 * 
	 * @param nodeName
	 *            �ڵ��ǩ��
	 * @return �ڵ�ֵ
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
	 * ��ȡָ���ڵ��ֵ
	 * 
	 * @param parentNode
	 *            ���ڵ���
	 * @param node
	 *            ָ���ӽڵ�����
	 * @param attrName
	 *            ָ���ӽڵ���������
	 * @param attrValue
	 *            ָ���ӽڵ�����ֵ
	 * @return ָ���ӽڵ��ֵ
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
	 * ��XML�ĵ������ָ�������
	 * 
	 * @param out
	 *            �����������IO�����IO
	 * @param encoding
	 *            ָ�������ʽ
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
	 * �ָ�ڵ�������"."Ϊ�ָ��
	 * 
	 * @param name
	 *            �ڵ���
	 * @return �����б�
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
	 * ɾ���ڵ�
	 * 
	 * @param name
	 *            �ڵ���
	 */
	public void removeNode(String name) {
		String[] propName = parsePropertyName(name.substring(0, name.lastIndexOf(".")));
		Element element = findNode(propName);
		if (element != null) {
			element.removeChild(name.substring(name.lastIndexOf(".") + 1));
		}
	}

	/**
	 * ��XML�ĵ����浽�ļ�
	 * 
	 * @param fileName
	 *            �ļ���
	 * @param encoding
	 *            ָ�������ʽ
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
	 * �޸Ľڵ��ֵ
	 * 
	 * @param name
	 *            Ҫ�޸ĵĽڵ������
	 * @param value
	 *            �޸ĺ�Ľڵ�ֵ
	 */
	public void setNodeValue(String name, String value) {
		propertyCache.put(name, value);
		Element element = findNode(parsePropertyName(name));
		if (element != null) {
			element.setText(value);

		}
	}

	/**
	 * ����ָ�����ڵ㡢ָ�����ԵĽڵ�ֵ
	 * 
	 * @param parentNode
	 *            ���ڵ�
	 * @param node
	 *            ָ���ڵ�����
	 * @param attrName
	 *            ָ���ڵ����������
	 * @param attrValue
	 *            ָ���ڵ������ֵ
	 * @param nodeValue
	 *            ָ���ڵ�ֵ
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
