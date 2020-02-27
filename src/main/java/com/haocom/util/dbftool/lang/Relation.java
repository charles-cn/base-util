package com.haocom.util.dbftool.lang;

/**
 * �����ֶ�������. <br>
 * �����ֶ�������.
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

	/** ���� */
	private String columnName;

	/** ֵ */
	private String value;

	/**
	 * ���췽��
	 * 
	 * @param columnName
	 *            ����
	 * @param value
	 *            ֵ
	 */
	public Relation(String columnName, String value) {
		this.columnName = columnName;
		this.value = value;
	}

	/**
	 * ��ȡ columnName
	 * 
	 * @return columnName
	 */
	public String getColumnName() {
		return columnName;
	}

	/**
	 * ��ȡ value
	 * 
	 * @return value
	 */
	public String getValue() {
		return value;
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
	 * ���� value
	 * 
	 * @param value
	 */
	public void setValue(String value) {
		this.value = value;
	}

}
