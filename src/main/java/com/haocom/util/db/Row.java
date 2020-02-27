package com.haocom.util.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * �ж���. <br>
 * �����ж���.
 * <p>
 * Copyright: Copyright (c) Sep 16, 2008
 * <p>
 * Company: 
 * <p>
 * Author: nishu
 * <p>
 * Version: 1.0
 */

public class Row {

	/** ����һ��HashMap */
	private HashMap<String,Object> map = new HashMap<String,Object>();

	/** ����һ��List */
	private List<String> ordering = new ArrayList<String>(20);

	/**
	 * Ĭ�Ϲ��캯��
	 */
	public Row() {
	}

	/**
	 * ��ӡ�����ڵ���
	 */
	public void debug() {
		
		for (Map.Entry<String, Object> entry : map.entrySet()) {	
			String name = entry.getKey();
			Object value = entry.getValue();
			System.out.print(name + "=" + value + ", ");
		}

		
		System.out.println("");
	}

	/**
	 * �����ֶ���HashMap�е����ȡ���ֶ�ֵ
	 * 
	 * @param index
	 *            ���
	 * @return �ֶ�ֵ
	 */
	public Object get(int index) {
		String key =  ordering.get(index);
		return map.get(key);
	}

	/**
	 * �����ֶ�����ȡ���ֶ�ֵ
	 * 
	 * @param fieldName
	 *            �ֶ�����
	 * @return �ֶ�ֵ
	 */
	public Object get(String fieldName) {
		return map.get(fieldName.toUpperCase());
	}

	/**
	 * �����ֶ����ȡ���ֶ�����
	 * 
	 * @param index
	 *            ���
	 * @return �ֶ�����
	 */
	public String getKey(int index) {
		String key = ordering.get(index);
		return key;
	}

	/**
	 * �õ��ж������ֶεĸ���
	 * 
	 * @return �ж������ֶεĸ���
	 */
	public int length() {
		return map.size();
	}

	/**
	 * ��HashMap��׷�Ӽ�ֵ�ԣ����ֶ��������ֶ�ֵ
	 * 
	 * @param name
	 *            �ֶ�����
	 * @param value
	 *            �ֶ�ֵ
	 */
	public void put(String name, Object value) {
		if (!map.containsKey(name.toUpperCase())) {
			ordering.add(name.toUpperCase());
		}
		
		map.put(name.toUpperCase(), value);
	}
}
