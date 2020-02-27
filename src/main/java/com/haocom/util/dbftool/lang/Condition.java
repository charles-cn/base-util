package com.haocom.util.dbftool.lang;

/**
 * ��������. <br>
 * ��������.
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

	/** ��ȣ�Ϊ0 */
	public static final int EQUAL = 0;

	/** С�ڣ�Ϊ-1 */
	public static final int LESSTHAN = -1;

	/** ���ڣ�Ϊ1 */
	public static final int MORETHAN = 1;

	/** ���� */
	private String columnName;

	/** �е�ֵ */
	private String columnValue;

	/** ��ϵ */
	private int relation;

	/**
	 * ��ȡ columnName
	 * 
	 * @return columnName
	 */
	public String getColumnName() {
		return columnName;
	}

	/**
	 * ��ȡ columnValue
	 * 
	 * @return columnValue
	 */
	public String getColumnValue() {
		return columnValue;
	}

	/**
	 * ��ȡ relation
	 * 
	 * @return relation
	 */
	public int getRelation() {
		return relation;
	}

	/**
	 * ���� columnName
	 * 
	 * @param columnName
	 */
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	/**
	 * ���� columnValue
	 * 
	 * @param columnValue
	 */
	public void setColumnValue(String columnValue) {
		this.columnValue = columnValue;
	}

	/**
	 * ���� relation
	 * 
	 * @param relation
	 */
	public void setRelation(int relation) {
		this.relation = relation;
	}

}
