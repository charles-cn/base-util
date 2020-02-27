package com.haocom.util.dbftool;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import com.haocom.util.dbftool.exception.ColumnTooLargeException;
import com.haocom.util.dbftool.lang.Column;
import com.haocom.util.dbftool.lang.Condition;
import com.haocom.util.dbftool.lang.DbfResultSet;
import com.haocom.util.dbftool.lang.Record;
import com.haocom.util.dbftool.lang.Relation;

/**
 * DBF操作类(DBF4.0). <br>
 * DBF操作类(DBF4.0),DBF文件以"\r"作为分隔符.
 * <p>
 * Copyright: Copyright (c) Sep 16, 2008
 * <p>
 * Company: 
 * <p>
 * Author: liujun
 * <p>
 * Version: 1.0
 */

public class DbfTool {

	/** 临时文件文件名 */
	private static String TMP_FILE = "runtime.dat";

	// public static void main(String[] args) {
	// try {
	// DbfTool dbfTool = new DbfTool();
	// dbfTool.openTable("d:\\2.dbf");
	//
	// }
	// catch (Exception ex) {
	//
	// }
	//
	// }

	/** 列数 */
	private int columnNumber = 0;

	/** 列集合 */
	private Vector columns = null;

	/** 数据库文件名 */
	private String dbfName = null;

	/** 日志 */
	private Logger logger = null;

	/** 行的长度 */
	private int rowLength = 0;

	/** 结果集 */
	private DbfResultSet rs = null;

	/**
	 * 默认构造函数，初始化日志
	 * 
	 * @throws IOException
	 */
	public DbfTool() throws IOException {
		// 整合入程序时请修改下一行
		DOMConfigurator.configure("config/dbflog.xml");

		logger = Logger.getLogger(this.getClass());

		initColumns();
	}

	/**
	 * 向文件内添加记录
	 * 
	 * @param record
	 *            构建的记录
	 * @return 返回添加的记录数
	 * @throws ColumnTooLargeException
	 */
	public int add(Record record) throws ColumnTooLargeException {
		File file = null;
		FileOutputStream fos = null;
		int result = 0;
		try {
			file = new File(this.dbfName);
			fos = new FileOutputStream(file, true);
		}
		catch (Exception ex) {
			return -1;
		}

		if (record == null)
			return 0;
		byte[] buff = buildRecord(record);

		try {
			fos.write(buff);
			fos.flush();
			fos.close();
			result++;
		}
		catch (Exception ex) {

			logger.warn(null, ex);
			return -1;
		}

		return result;
	}

	/**
	 * 向文件内添加记录
	 * 
	 * @param rows
	 *            记录数组
	 * @return 返回添加的记录数
	 * @throws ColumnTooLargeException
	 */
	public int add(Record[] rows) throws ColumnTooLargeException {
		File file = null;
		FileOutputStream fos = null;
		int result = 0;
		try {
			file = new File(this.dbfName);
			fos = new FileOutputStream(file, true);
		}
		catch (Exception ex) {
			return -1;
		}

		for (int i = 0; i < rows.length; i++) {
			Record record = rows[i];
			if (record == null)
				continue;
			byte[] buff = buildRecord(record);
			try {
				fos.write(buff);
				result++;
			}
			catch (Exception ex) {
				logger.warn(null, ex);
				continue;
			}
		}
		try {
			fos.flush();
			fos.close();
		}
		catch (IOException ex) {
			logger.error(null, ex);
			return -1;
		}
		return result;
	}

	/**
	 * 根据输入构建记录
	 * 
	 * @param record
	 *            构建的记录
	 * @return 记录内容
	 * @throws ColumnTooLargeException
	 */
	private byte[] buildRecord(Record record) throws ColumnTooLargeException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		for (int i = 0; i < columns.size(); i++) {
			Column column = (Column) columns.get(i);
			if (column == null)
				continue;
			String name = column.getColumnName();
			int len = column.getColumnLength();

			try {
				byte[] buff = new byte[len];
				String value = record.getValue(name);

				if (value == null || value.length() == 0) {
					bos.write(buff);
				} else if (value.length() > len) {
					bos.close();
					throw new ColumnTooLargeException();
				} else {
					System.arraycopy(value.getBytes(), 0, buff, 0, value.length());
					bos.write(buff);
				}
			}
			catch (IOException ex) {
				logger.error(null, ex);
			}
		}

		bos.write(0x0d);

		byte[] result = bos.toByteArray();
		try {
			bos.close();
		}
		catch (IOException ex) {
			logger.error(null, ex);
		}
		return result;
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
	 * 关闭所打开的文件
	 * 
	 * @return -1 关闭成功
	 * @throws IOException
	 */
	public int closeTable() throws IOException {
		this.dbfName = null;
		columns.clear();
		columns = null;
		if (this.rs != null)
			rs.close();
		this.rs = null;
		return -1;
	}

	/**
	 * 从文件内删除指定条件记录
	 * 
	 * @param conditions
	 *            条件数组
	 * @return 删除成功：返回删除的条数；删除失败：返回-1
	 */
	public int delete(Condition[] conditions) {
		File tmpFile = new File(TMP_FILE);
		File targetFile = new File(this.dbfName);
		FileOutputStream fos = null;
		DbfResultSet drs = null;
		int result = 0;
		RandomAccessFile fis = null;

		try {
			fos = new FileOutputStream(tmpFile);
			drs = new DbfResultSet(this.dbfName, this.columns, this.rowLength, null);
			fis = new RandomAccessFile(targetFile, "r");
			byte[] buff = new byte[(columns.size() + 1) * 32 + 1];
			fis.readFully(buff);
			fis.close();
			fis = null;
			fos.write(buff);
			while (drs.next()) {
				boolean tmp = false;
				for (int i = 0; i < conditions.length; i++) {
					Condition condition = conditions[i];
					if (condition == null)
						continue;
					String value = drs.getString(condition.getColumnName());
					int relation = condition.getRelation();
					if (relation == Condition.EQUAL) {
						if (value.equals(condition.getColumnValue())) {
							result++;
							tmp = true;
							continue;
						} else {
							tmp = false;
							break;
						}
					} else if (relation == Condition.LESSTHAN) {
						if (value.compareTo(condition.getColumnValue()) < 0) {
							result++;
							tmp = true;
							continue;
						} else {
							tmp = false;
							break;
						}
					} else if (relation == Condition.MORETHAN) {
						if (value.compareTo(condition.getColumnValue()) > 0) {
							result++;
							tmp = true;
							continue;
						} else {
							tmp = false;
							break;
						}
					} else
						tmp = false;
				}

				if (!tmp) {
					fos.write(buildRecord(drs.getCurrRecord()));
				}
			}

			fos.flush();
		}
		catch (IOException ex) {
			logger.error(null, ex);
			return -1;
		}
		catch (ColumnTooLargeException ex) {
			logger.error(null, ex);
			return -1;
		}
		finally {
			if (drs != null) {
				try {
					drs.close();
				}
				catch (Exception ex) {
					logger.error(null, ex);
				}
			}
			if (fos != null) {
				try {
					fos.close();
					fos = null;
				}
				catch (Exception ex) {
					logger.error(null, ex);
				}
			}

			if (fis != null) {
				try {
					fis.close();
					fis = null;
				}
				catch (Exception ex) {
					logger.error(null, ex);
				}
			}
		}

		try {
			targetFile.delete();
			tmpFile.renameTo(targetFile);
		}
		catch (Exception ex) {
			logger.error(null, ex);
			return -1;
		}

		return result;
	}

	/**
	 * 初始化表列信息
	 * 
	 * @throws IOException
	 */
	private void initColumns() throws IOException {
		DataInputStream dis = null;
		try {
			dis = new DataInputStream(new FileInputStream(new File(this.dbfName)));
			if (columns == null)
				columns = new Vector();
			else
				columns.clear();
			this.columnNumber = 0;
			this.rowLength = 0;
			Column column = null;
			byte[] bColumn = new byte[32];
			byte b1;
			byte[] buffer = new byte[31];
			// 如果字段定义以0x0d('\r')开头，则表示字段信息已经读取完
			while (true) {
				b1 = dis.readByte();
				if (b1 == 0x0d) {
					break;
				}
				dis.readFully(buffer);

				bColumn[0] = b1;
				System.arraycopy(buffer, 0, bColumn, 1, 31);
				column = new Column();

				int i = 0;
				for (; i < 10; i++) { // Field name,10位
					if (bColumn[i] == 0) {
						break;
					}
				}

				char c = (char) bColumn[11]; // 字段类型

				int len = bColumn[16]; // Field length
				int cou = bColumn[17]; // Decimal count
				if (len < 0) { // len<256,java中byte为-128——127，大于127的为负
					len += 256;
				}
				if (cou < 0) {
					cou += 256;
				}

				column.setColumnName(new String(bColumn, 0, i)); // 字段名
				column.setColumnType(String.valueOf(c));
				column.setColumnLength(len);
				column.setDecimalCount(String.valueOf(cou));
				columns.add(column);
				columnNumber++; // 字段个数
				rowLength += len; // 所有字段的总长度即一条记录的长度
				column = null;
			}
		}
		catch (IOException ex) {
			logger.error(null, ex);
			throw ex;
		}
		finally {
			try {
				if (dis != null)
					dis.close();
			}
			catch (Exception ex) {
				logger.warn(null, ex);
			}
		}
	}

	/**
	 * 打开指定dbf文件
	 * 
	 * @param dbfName
	 *            所打开的dbf文件全路径名
	 */
	public void openTable(String dbfName) {
		this.dbfName = dbfName;
	}

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

	/**
	 * 根据查询条件返回结果集 完成后应调用结果集的关闭方法，否则会发生IO异常
	 * 
	 * @param conditions
	 *            条件数组
	 * @return 结果集
	 * @throws FileNotFoundException
	 */
	public DbfResultSet select(Condition[] conditions) throws FileNotFoundException {
		try {
			if (rs != null)
				rs.close();
		}
		catch (Exception ex) {
			logger.warn(null, ex);
		}
		rs = new DbfResultSet(this.dbfName, this.columns, this.rowLength, conditions);

		return rs;
	}

	/**
	 * 返回表中所有记录 完成后应调用结果集的关闭方法，否则会发生IO异常
	 * 
	 * @return 结果集
	 * @throws FileNotFoundException
	 */
	public DbfResultSet selectAll() throws FileNotFoundException {
		try {
			if (rs != null)
				rs.close();
		}
		catch (Exception ex) {
			logger.warn(null, ex);
		}
		rs = new DbfResultSet(this.dbfName, this.columns, this.rowLength, null);

		return rs;
	}

	/**
	 * 更新指定条件的记录
	 * 
	 * @param relations
	 *            字段值数组
	 * @param conditions
	 *            条件数组
	 * @return 更新的条数
	 */
	public int update(Relation[] relations, Condition[] conditions) {
		RandomAccessFile raf = null;
		int result = 0;
		try {
			raf = new RandomAccessFile(this.dbfName, "rw");
			long cursor = (columns.size() + 1) * 32;
			raf.seek(cursor);
			byte bBlank = 0x00;
			while (bBlank == 0x00) {
				cursor++;
				bBlank = raf.readByte();
				continue;
			}
			byte[] row = new byte[this.rowLength];
			while (true) {
				raf.readFully(row);
				Record record = parseRecord(row);
				if (checkIsValid(record, conditions)) {
					record.addRelations(relations);
					try {
						raf.seek(cursor);
						raf.write(buildRecord(record));
						result++;
					}
					catch (Exception ex) {
						logger.error(null, ex);
					}
				}
				cursor += this.rowLength;
			}
		}
		catch (EOFException ex) {
			return result;
		}
		catch (Exception ex) {
			logger.error(null, ex);
			return 0 - result;
		}

		finally {
			if (raf != null) {
				try {
					raf.close();
				}
				catch (Exception ex) {
					logger.error(null, ex);
				}
			}
		}
	}
}
