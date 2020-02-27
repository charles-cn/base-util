package com.haocom.util.dbftool.lang;

import java.util.Hashtable;

/**
 * 记录类. <br>
 * 记录类，对应DBF表中一条记录，字段可缺.
 * <p>
 * Copyright: Copyright (c) Sep 16, 2008
 * <p>
 * Company: 
 * <p>
 * Author: liujun
 * <p>
 * Version: 1.0
 */
public class Record {

	/** 定义一个Hashtable，存放字段属性 */
	private Hashtable table = null;

	/**
	 * 构造方法
	 */
	public Record() {
		table = new Hashtable();
	}

	/**
	 * 添加字段属性
	 * 
	 * @param relation
	 *            字段属性
	 */
	public void addRelation(Relation relation) {
		if (table.get(relation.getColumnName()) != null)
			table.remove(relation.getColumnName());
		table.put(relation.getColumnName(), relation.getValue());
	}

	/**
	 * 添加多个字段属性
	 * 
	 * @param relations
	 *            字段属性数组
	 */
	public void addRelations(Relation[] relations) {
		// TODO Auto-generated method stub
		for (int i = 0; i < relations.length; i++) {
			this.addRelation(relations[i]);
		}
	}

	/**
	 * 添加指定列名的值
	 * 
	 * @param columnName
	 *            列名
	 * @param value
	 *            值
	 */
	public void addValue(String columnName, String value) {
		if (table.get(columnName) != null)
			table.remove(columnName);
		table.put(columnName, value);
	}

	/**
	 * 获取指定列的值
	 * 
	 * @param column
	 *            列
	 * @return 指定列的值
	 */
	public String getValue(String column) {
		return (String) table.get(column);
	}
}
