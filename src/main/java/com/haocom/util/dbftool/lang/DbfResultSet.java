package com.haocom.util.dbftool.lang;

import java.io.EOFException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Vector;

import org.apache.log4j.Logger;

/**
 * DBF查询结果集类. <br>
 * DBF查询结果集类，必须在使用后关闭.
 * <p>
 * Copyright: Copyright (c) Sep 16, 2008
 * <p>
 * Company: 
 * <p>
 * Author: liujun
 * <p>
 * Version: 1.0
 */

public class DbfResultSet {

	/** 日志 */
	private static Logger logger;

	/** 列集合 */
	private Vector columns;

	/** 条件数组 */
	private Condition[] conditions;

	/** 当前记录 */
	private Record currRecord;

	/** 长整型数，用作指针 */
	private long cursor;

	/** 文件名 */
	private String fileName;

	/** 结果集关闭标志，true打开，false关闭 */
	private boolean isOpenRaf;

	/** 随机数据库文件 */
	private RandomAccessFile raf;

	/** 行的长度 */
	private int rowLength;

	/**
	 * 构造函数，供DbfTools类使用，创建一个实例
	 * 
	 * @param fileName
	 *            文件名
	 * @param columns
	 *            列集合
	 * @param len
	 *            行的长度
	 * @param conditions
	 *            条件数组
	 * @throws FileNotFoundException
	 */
	public DbfResultSet(String fileName, Vector columns, int len, Condition[] conditions) throws FileNotFoundException {
		this.fileName = fileName;
		this.conditions = conditions;
		this.columns = columns;
		this.rowLength = len;
		logger = Logger.getLogger(this.getClass());
		isOpenRaf = false;
		cursor = 0;
	}

	/**
	 * 检查是否合法
	 * 
	 * @param record
	 *            构建的记录
	 * @param conditions
	 *            条件数组
	 * @return 是否合法
	 */
	private boolean checkIsValid(Record record, Condition[] conditions) {
		if (conditions == null)
			return true;
		for (int i = 0; i < conditions.length; i++) {
			String name = conditions[i].getColumnName();
			String value = conditions[i].getColumnValue();
			int relation = conditions[i].getRelation();

			if (relation == Condition.EQUAL) {
				if (!(value.equals(record.getValue(name)))) {
					return false;
				}
			} else if (relation == Condition.LESSTHAN) {
				if (value.compareTo(record.getValue(name)) >= 0) {
					return false;
				}
			} else if (relation == Condition.MORETHAN) {
				if (value.compareTo(record.getValue(name)) <= 0) {
					return false;
				}
			}
		}

		return true;
	}

	/**
	 * 关闭结果集,每次使用本类后必须调用，否则会有IO错误
	 * 
	 * @throws IOException
	 */
	public void close() throws IOException {
		try {
			if (raf != null)
				raf.close();
			isOpenRaf = false;
		}
		catch (Exception ex) {
			logger.warn(null, ex);
		}
		isOpenRaf = false;
		raf = null;
	}

	public boolean getBoolean(String colum) {
		return false;
	}

	/**
	 * 获取当前整条记录
	 * 
	 * @return 当前记录
	 */
	public Record getCurrRecord() {
		return currRecord;
	}

	/**
	 * 获取整数0
	 * 
	 * @param colum
	 *            列
	 * @return 0
	 */
	public int getInt(String colum) {
		return 0;
	}

	/**
	 * 返回当前记录指定属性名对应的值
	 * 
	 * @param columnName
	 *            列名
	 * @return 当前记录指定属性名对应的值
	 */
	public String getString(String columnName) {
		String value = null;
		value = this.currRecord.getValue(columnName);
		return value;
	}

	/**
	 * 是否存在下一条记录，同时当前指针移向下一条
	 * 
	 * @return 存在下一条记录且同时当前指针移向下一条：返回true；反之返回false
	 * @throws IOException
	 */
	public boolean next() throws IOException {
		if (!isOpenRaf) {
			raf = new RandomAccessFile(this.fileName, "r");
			isOpenRaf = true;
			try {
				cursor = (columns.size() + 1) * 32 + 1;
				raf.seek(cursor);
				byte bBlank = 0x00;
				while (bBlank == 0x00) {
					cursor++;
					bBlank = raf.readByte();
					continue;
				}
			}
			catch (EOFException ex) {
				return false;
			}
		}
		raf.seek(cursor);
		byte[] row = new byte[this.rowLength];
		try {
			while (true) {
				raf.readFully(row);
				Record record = parseRecord(row);
				cursor += this.rowLength;
				if (checkIsValid(record, conditions)) {
					this.currRecord = record;
					return true;
				}
			}
		}
		catch (EOFException ex) {
			return false;
		}
	}

	/**
	 * 解析行字节数组的内容,返回一个记录
	 * 
	 * @param row
	 *            行字节数组
	 * @return 记录
	 */
	private Record parseRecord(byte[] row) {
		int cursor = 0;
		String line;
		Record record = new Record();
		try {
			line = new String(row, "utf-8");
		}
		catch (Exception ex) {
			logger.error(null, ex);
			return null;
		}
		for (int i = 0; i < columns.size(); i++) {
			Column column = (Column) columns.get(i);
			int len = column.getColumnLength();
			String value = line.substring(cursor, len).trim();
			record.addValue(column.getColumnName(), value);
			cursor = cursor + len;
		}
		return record;
	}
}
