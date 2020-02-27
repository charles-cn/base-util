package com.haocom.util.dbftool.lang;

/**
 * ��������е���Ϣ. <br>
 * ��������е���Ϣ.
 * <p>
 * Copyright: Copyright (c) Sep 16, 2008
 * <p>
 * Company: 
 * <p>
 * Author: liujun
 * <p>
 * Version: 1.0
 */

public class Column {

	/**
	 * Value Description 00h No key for this field (ignored) 01h Key exists for
	 * this field (in MDX)<BR>
	 */

	/**
	 * �����ݵ�ַ<BR>
	 * 12-15
	 */
	private String columnDataAddress = "";

	/**
	 * �б�־<BR>
	 * 31
	 */
	private String columnFlag = "";

	/**
	 * �г�<BR>
	 * 16
	 */
	private int columnLength = 0;

	/**
	 * ����<BR>
	 * 0-10
	 */
	private String columnName = "";

	/**
	 * ������<BR>
	 * 11
	 */
	private String columnType = "";

	/**
	 * λ��<BR>
	 * 17
	 */
	private String decimalCount = "";

	/**
	 * ������ı�־<BR>
	 * 23
	 */
	private String flagForSetFields = "";

	/**
	 * �����ֶ�1<BR>
	 * 18-19
	 */
	private String reserved1ForMultiUser = "";

	/**
	 * �����ֶ�2<BR>
	 * 21-22
	 */
	private String reserved2ForMultiUser = "";

	/**
	 * �����ֶ�3<BR>
	 * 24-30
	 */
	private String reserved3ForMultiUser = "";

	/**
	 * ����Id<BR>
	 * 20
	 */
	private String workAreaID = "";

	/**
	 * ��ȡ columnDataAddress
	 * 
	 * @return columnDataAddress
	 */
	public String getColumnDataAddress() {
		return columnDataAddress;
	}

	/**
	 * ��ȡ columnFlag
	 * 
	 * @return columnFlag
	 */
	public String getColumnFlag() {
		return columnFlag;
	}

	/**
	 * ��ȡ columnLength
	 * 
	 * @return columnLength
	 */
	public int getColumnLength() {
		return columnLength;
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
	 * ��ȡ columnType
	 * 
	 * @return columnType
	 */
	public String getColumnType() {
		return columnType;
	}

	/**
	 * ��ȡ decimalCount
	 * 
	 * @return decimalCount
	 */
	public String getDecimalCount() {
		return decimalCount;
	}

	/**
	 * ��ȡ flagForSetFields
	 * 
	 * @return flagForSetFields
	 */
	public String getFlagForSetFields() {
		return flagForSetFields;
	}

	/**
	 * ��ȡ reserved1ForMultiUser
	 * 
	 * @return reserved1ForMultiUser
	 */
	public String getReserved1ForMultiUser() {
		return reserved1ForMultiUser;
	}

	/**
	 * ��ȡ reserved2ForMultiUser
	 * 
	 * @return reserved2ForMultiUser
	 */
	public String getReserved2ForMultiUser() {
		return reserved2ForMultiUser;
	}

	/**
	 * ��ȡ reserved3ForMultiUser
	 * 
	 * @return reserved3ForMultiUser
	 */
	public String getReserved3ForMultiUser() {
		return reserved3ForMultiUser;
	}

	/**
	 * ��ȡ workAreaID
	 * 
	 * @return workAreaID
	 */
	public String getWorkAreaID() {
		return workAreaID;
	}

	/**
	 * ���� columnDataAddress
	 * 
	 * @param columnDataAddress
	 */
	public void setColumnDataAddress(String columnDataAddress) {
		this.columnDataAddress = columnDataAddress;
	}

	/**
	 * ���� columnFlag
	 * 
	 * @param columnFlag
	 */
	public void setColumnFlag(String columnFlag) {
		this.columnFlag = columnFlag;
	}

	/**
	 * ���� columnLength
	 * 
	 * @param columnLength
	 */
	public void setColumnLength(int columnLength) {
		this.columnLength = columnLength;
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
	 * ���� columnType
	 * 
	 * @param columnType
	 */
	public void setColumnType(String columnType) {
		this.columnType = columnType;
	}

	/**
	 * ���� decimalCount
	 * 
	 * @param decimalCount
	 */
	public void setDecimalCount(String decimalCount) {
		this.decimalCount = decimalCount;
	}

	/**
	 * ���� flagForSetFields
	 * 
	 * @param flagForSetFields
	 */
	public void setFlagForSetFields(String flagForSetFields) {
		this.flagForSetFields = flagForSetFields;
	}

	/**
	 * ���� reserved1ForMultiUser
	 * 
	 * @param reserved1ForMultiUser
	 */
	public void setReserved1ForMultiUser(String reserved1ForMultiUser) {
		this.reserved1ForMultiUser = reserved1ForMultiUser;
	}

	/**
	 * ���� reserved2ForMultiUser
	 * 
	 * @param reserved2ForMultiUser
	 */
	public void setReserved2ForMultiUser(String reserved2ForMultiUser) {
		this.reserved2ForMultiUser = reserved2ForMultiUser;
	}

	/**
	 * ���� reserved3ForMultiUser
	 * 
	 * @param reserved3ForMultiUser
	 */
	public void setReserved3ForMultiUser(String reserved3ForMultiUser) {
		this.reserved3ForMultiUser = reserved3ForMultiUser;
	}

	/**
	 * ���� workAreaID
	 * 
	 * @param workAreaID
	 */
	public void setWorkAreaID(String workAreaID) {
		this.workAreaID = workAreaID;
	}

}
