package com.haocom.util.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 行对象. <br>
 * 定义行对象.
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

	/** 定义一个HashMap */
	private HashMap<String,Object> map = new HashMap<String,Object>();

	/** 定义一个List */
	private List<String> ordering = new ArrayList<String>(20);

	/**
	 * 默认构造函数
	 */
	public Row() {
	}

	/**
	 * 打印，用于调试
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
	 * 根据字段在HashMap中的序号取得字段值
	 * 
	 * @param index
	 *            序号
	 * @return 字段值
	 */
	public Object get(int index) {
		String key =  ordering.get(index);
		return map.get(key);
	}

	/**
	 * 根据字段名称取得字段值
	 * 
	 * @param fieldName
	 *            字段名称
	 * @return 字段值
	 */
	public Object get(String fieldName) {
		return map.get(fieldName.toUpperCase());
	}

	/**
	 * 根据字段序号取得字段名称
	 * 
	 * @param index
	 *            序号
	 * @return 字段名称
	 */
	public String getKey(int index) {
		String key = ordering.get(index);
		return key;
	}

	/**
	 * 得到行对象中字段的个数
	 * 
	 * @return 行对象中字段的个数
	 */
	public int length() {
		return map.size();
	}

	/**
	 * 向HashMap中追加键值对，即字段名称与字段值
	 * 
	 * @param name
	 *            字段名称
	 * @param value
	 *            字段值
	 */
	public void put(String name, Object value) {
		if (!map.containsKey(name.toUpperCase())) {
			ordering.add(name.toUpperCase());
		}
		
		map.put(name.toUpperCase(), value);
	}
}
