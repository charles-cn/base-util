package com.haocom.util.dbftool.lang;

import java.io.EOFException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Vector;

import org.apache.log4j.Logger;

/**
 * DBF��ѯ�������. <br>
 * DBF��ѯ������࣬������ʹ�ú�ر�.
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

	/** ��־ */
	private static Logger logger;

	/** �м��� */
	private Vector columns;

	/** �������� */
	private Condition[] conditions;

	/** ��ǰ��¼ */
	private Record currRecord;

	/** ��������������ָ�� */
	private long cursor;

	/** �ļ��� */
	private String fileName;

	/** ������رձ�־��true�򿪣�false�ر� */
	private boolean isOpenRaf;

	/** ������ݿ��ļ� */
	private RandomAccessFile raf;

	/** �еĳ��� */
	private int rowLength;

	/**
	 * ���캯������DbfTools��ʹ�ã�����һ��ʵ��
	 * 
	 * @param fileName
	 *            �ļ���
	 * @param columns
	 *            �м���
	 * @param len
	 *            �еĳ���
	 * @param conditions
	 *            ��������
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
	 * ����Ƿ�Ϸ�
	 * 
	 * @param record
	 *            �����ļ�¼
	 * @param conditions
	 *            ��������
	 * @return �Ƿ�Ϸ�
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
	 * �رս����,ÿ��ʹ�ñ���������ã��������IO����
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
	 * ��ȡ��ǰ������¼
	 * 
	 * @return ��ǰ��¼
	 */
	public Record getCurrRecord() {
		return currRecord;
	}

	/**
	 * ��ȡ����0
	 * 
	 * @param colum
	 *            ��
	 * @return 0
	 */
	public int getInt(String colum) {
		return 0;
	}

	/**
	 * ���ص�ǰ��¼ָ����������Ӧ��ֵ
	 * 
	 * @param columnName
	 *            ����
	 * @return ��ǰ��¼ָ����������Ӧ��ֵ
	 */
	public String getString(String columnName) {
		String value = null;
		value = this.currRecord.getValue(columnName);
		return value;
	}

	/**
	 * �Ƿ������һ����¼��ͬʱ��ǰָ��������һ��
	 * 
	 * @return ������һ����¼��ͬʱ��ǰָ��������һ��������true����֮����false
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
	 * �������ֽ����������,����һ����¼
	 * 
	 * @param row
	 *            ���ֽ�����
	 * @return ��¼
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
