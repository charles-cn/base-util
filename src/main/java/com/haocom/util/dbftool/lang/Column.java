package com.haocom.util.dbftool.lang;

/**
 * 储存表中列的信息. <br>
 * 储存表中列的信息.
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
	 * 列数据地址<BR>
	 * 12-15
	 */
	private String columnDataAddress = "";

	/**
	 * 列标志<BR>
	 * 31
	 */
	private String columnFlag = "";

	/**
	 * 列长<BR>
	 * 16
	 */
	private int columnLength = 0;

	/**
	 * 列名<BR>
	 * 0-10
	 */
	private String columnName = "";

	/**
	 * 列类型<BR>
	 * 11
	 */
	private String columnType = "";

	/**
	 * 位数<BR>
	 * 17
	 */
	private String decimalCount = "";

	/**
	 * 设置域的标志<BR>
	 * 23
	 */
	private String flagForSetFields = "";

	/**
	 * 保留字段1<BR>
	 * 18-19
	 */
	private String reserved1ForMultiUser = "";

	/**
	 * 保留字段2<BR>
	 * 21-22
	 */
	private String reserved2ForMultiUser = "";

	/**
	 * 保留字段3<BR>
	 * 24-30
	 */
	private String reserved3ForMultiUser = "";

	/**
	 * 工作Id<BR>
	 * 20
	 */
	private String workAreaID = "";

	/**
	 * 获取 columnDataAddress
	 * 
	 * @return columnDataAddress
	 */
	public String getColumnDataAddress() {
		return columnDataAddress;
	}

	/**
	 * 获取 columnFlag
	 * 
	 * @return columnFlag
	 */
	public String getColumnFlag() {
		return columnFlag;
	}

	/**
	 * 获取 columnLength
	 * 
	 * @return columnLength
	 */
	public int getColumnLength() {
		return columnLength;
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
	 * 获取 columnType
	 * 
	 * @return columnType
	 */
	public String getColumnType() {
		return columnType;
	}

	/**
	 * 获取 decimalCount
	 * 
	 * @return decimalCount
	 */
	public String getDecimalCount() {
		return decimalCount;
	}

	/**
	 * 获取 flagForSetFields
	 * 
	 * @return flagForSetFields
	 */
	public String getFlagForSetFields() {
		return flagForSetFields;
	}

	/**
	 * 获取 reserved1ForMultiUser
	 * 
	 * @return reserved1ForMultiUser
	 */
	public String getReserved1ForMultiUser() {
		return reserved1ForMultiUser;
	}

	/**
	 * 获取 reserved2ForMultiUser
	 * 
	 * @return reserved2ForMultiUser
	 */
	public String getReserved2ForMultiUser() {
		return reserved2ForMultiUser;
	}

	/**
	 * 获取 reserved3ForMultiUser
	 * 
	 * @return reserved3ForMultiUser
	 */
	public String getReserved3ForMultiUser() {
		return reserved3ForMultiUser;
	}

	/**
	 * 获取 workAreaID
	 * 
	 * @return workAreaID
	 */
	public String getWorkAreaID() {
		return workAreaID;
	}

	/**
	 * 设置 columnDataAddress
	 * 
	 * @param columnDataAddress
	 */
	public void setColumnDataAddress(String columnDataAddress) {
		this.columnDataAddress = columnDataAddress;
	}

	/**
	 * 设置 columnFlag
	 * 
	 * @param columnFlag
	 */
	public void setColumnFlag(String columnFlag) {
		this.columnFlag = columnFlag;
	}

	/**
	 * 设置 columnLength
	 * 
	 * @param columnLength
	 */
	public void setColumnLength(int columnLength) {
		this.columnLength = columnLength;
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
	 * 设置 columnType
	 * 
	 * @param columnType
	 */
	public void setColumnType(String columnType) {
		this.columnType = columnType;
	}

	/**
	 * 设置 decimalCount
	 * 
	 * @param decimalCount
	 */
	public void setDecimalCount(String decimalCount) {
		this.decimalCount = decimalCount;
	}

	/**
	 * 设置 flagForSetFields
	 * 
	 * @param flagForSetFields
	 */
	public void setFlagForSetFields(String flagForSetFields) {
		this.flagForSetFields = flagForSetFields;
	}

	/**
	 * 设置 reserved1ForMultiUser
	 * 
	 * @param reserved1ForMultiUser
	 */
	public void setReserved1ForMultiUser(String reserved1ForMultiUser) {
		this.reserved1ForMultiUser = reserved1ForMultiUser;
	}

	/**
	 * 设置 reserved2ForMultiUser
	 * 
	 * @param reserved2ForMultiUser
	 */
	public void setReserved2ForMultiUser(String reserved2ForMultiUser) {
		this.reserved2ForMultiUser = reserved2ForMultiUser;
	}

	/**
	 * 设置 reserved3ForMultiUser
	 * 
	 * @param reserved3ForMultiUser
	 */
	public void setReserved3ForMultiUser(String reserved3ForMultiUser) {
		this.reserved3ForMultiUser = reserved3ForMultiUser;
	}

	/**
	 * 设置 workAreaID
	 * 
	 * @param workAreaID
	 */
	public void setWorkAreaID(String workAreaID) {
		this.workAreaID = workAreaID;
	}

}
