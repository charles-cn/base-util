package com.haocom.util.dbftool.lang;

/**
 * 条件数组. <br>
 * 条件数组.
 * <p>
 * Copyright: Copyright (c) Sep 16, 2008
 * <p>
 * Company: 
 * <p>
 * Author: liujun
 * <p>
 * Version: 1.0
 */
public class Condition {

	/** 相等，为0 */
	public static final int EQUAL = 0;

	/** 小于，为-1 */
	public static final int LESSTHAN = -1;

	/** 大于，为1 */
	public static final int MORETHAN = 1;

	/** 列名 */
	private String columnName;

	/** 列的值 */
	private String columnValue;

	/** 关系 */
	private int relation;

	/**
	 * 获取 columnName
	 * 
	 * @return columnName
	 */
	public String getColumnName() {
		return columnName;
	}

	/**
	 * 获取 columnValue
	 * 
	 * @return columnValue
	 */
	public String getColumnValue() {
		return columnValue;
	}

	/**
	 * 获取 relation
	 * 
	 * @return relation
	 */
	public int getRelation() {
		return relation;
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
	 * 设置 columnValue
	 * 
	 * @param columnValue
	 */
	public void setColumnValue(String columnValue) {
		this.columnValue = columnValue;
	}

	/**
	 * 设置 relation
	 * 
	 * @param relation
	 */
	public void setRelation(int relation) {
		this.relation = relation;
	}

}
