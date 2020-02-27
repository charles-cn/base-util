package com.haocom.util.dbftool.lang;

/**
 * 设置字段属性类. <br>
 * 设置字段属性类.
 * <p>
 * Copyright: Copyright (c) Sep 16, 2008
 * <p>
 * Company: 
 * <p>
 * Author: liujun
 * <p>
 * Version: 1.0
 */
public class Relation {

	/** 列名 */
	private String columnName;

	/** 值 */
	private String value;

	/**
	 * 构造方法
	 * 
	 * @param columnName
	 *            列名
	 * @param value
	 *            值
	 */
	public Relation(String columnName, String value) {
		this.columnName = columnName;
		this.value = value;
	}

	/**
	 * 获取 columnName
	 * 
	 * @return columnName
	 */
	public String getColumnName() {
		return columnName;
	}

	/**
	 * 获取 value
	 * 
	 * @return value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * 设置 columnName
	 * 
	 * @param columnName
	 */
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	/**
	 * 设置 value
	 * 
	 * @param value
	 */
	public void setValue(String value) {
		this.value = value;
	}

}
