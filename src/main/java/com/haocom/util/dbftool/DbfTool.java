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
 * DBF������(DBF4.0). <br>
 * DBF������(DBF4.0),DBF�ļ���"\r"��Ϊ�ָ���.
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

	/** ��ʱ�ļ��ļ��� */
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

	/** ���� */
	private int columnNumber = 0;

	/** �м��� */
	private Vector columns = null;

	/** ���ݿ��ļ��� */
	private String dbfName = null;

	/** ��־ */
	private Logger logger = null;

	/** �еĳ��� */
	private int rowLength = 0;

	/** ����� */
	private DbfResultSet rs = null;

	/**
	 * Ĭ�Ϲ��캯������ʼ����־
	 * 
	 * @throws IOException
	 */
	public DbfTool() throws IOException {
		// ���������ʱ���޸���һ��
		DOMConfigurator.configure("config/dbflog.xml");

		logger = Logger.getLogger(this.getClass());

		initColumns();
	}

	/**
	 * ���ļ�����Ӽ�¼
	 * 
	 * @param record
	 *            �����ļ�¼
	 * @return ������ӵļ�¼��
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
	 * ���ļ�����Ӽ�¼
	 * 
	 * @param rows
	 *            ��¼����
	 * @return ������ӵļ�¼��
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
	 * �������빹����¼
	 * 
	 * @param record
	 *            �����ļ�¼
	 * @return ��¼����
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
	 * �ر����򿪵��ļ�
	 * 
	 * @return -1 �رճɹ�
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
	 * ���ļ���ɾ��ָ��������¼
	 * 
	 * @param conditions
	 *            ��������
	 * @return ɾ���ɹ�������ɾ����������ɾ��ʧ�ܣ�����-1
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
	 * ��ʼ��������Ϣ
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
			// ����ֶζ�����0x0d('\r')��ͷ�����ʾ�ֶ���Ϣ�Ѿ���ȡ��
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
				for (; i < 10; i++) { // Field name,10λ
					if (bColumn[i] == 0) {
						break;
					}
				}

				char c = (char) bColumn[11]; // �ֶ�����

				int len = bColumn[16]; // Field length
				int cou = bColumn[17]; // Decimal count
				if (len < 0) { // len<256,java��byteΪ-128����127������127��Ϊ��
					len += 256;
				}
				if (cou < 0) {
					cou += 256;
				}

				column.setColumnName(new String(bColumn, 0, i)); // �ֶ���
				column.setColumnType(String.valueOf(c));
				column.setColumnLength(len);
				column.setDecimalCount(String.valueOf(cou));
				columns.add(column);
				columnNumber++; // �ֶθ���
				rowLength += len; // �����ֶε��ܳ��ȼ�һ����¼�ĳ���
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
	 * ��ָ��dbf�ļ�
	 * 
	 * @param dbfName
	 *            ���򿪵�dbf�ļ�ȫ·����
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
	 * ���ݲ�ѯ�������ؽ���� ��ɺ�Ӧ���ý�����Ĺرշ���������ᷢ��IO�쳣
	 * 
	 * @param conditions
	 *            ��������
	 * @return �����
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
	 * ���ر������м�¼ ��ɺ�Ӧ���ý�����Ĺرշ���������ᷢ��IO�쳣
	 * 
	 * @return �����
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
	 * ����ָ�������ļ�¼
	 * 
	 * @param relations
	 *            �ֶ�ֵ����
	 * @param conditions
	 *            ��������
	 * @return ���µ�����
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
