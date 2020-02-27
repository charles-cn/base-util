package com.haocom.config;

import java.io.File;
import java.util.HashMap;

/**
 * �ļ����ö���. <br>
 * ��ȡ�ļ����������.
 * <p>
 * Copyright: Copyright (c) 2009-3-18
 * <p>
 * Company: 
 * <p>
 * Author: 
 * <p>
 * Version: 1.0
 */
public abstract class FileConfig extends MapConfig {

	/** ����ļ��Ƿ�����ʱ���� */
	private long checkModifiedTimeMark = 0;

	/** �ļ��� */
	private String filename;

	/** �Ƿ�����ļ��䶯 */
	private boolean monitor = false;

	/** �ļ������� */
	private long timeMark = 0;

	/**
	 * �����ļ����ö���Ĭ�ϲ������ļ����.
	 * 
	 * @param name
	 *            ��������
	 * @param filename
	 *            �ļ�����
	 * @throws Exception
	 */
	public FileConfig(String name, String filename) throws Exception {
		super(name);
		this.filename = filename;
		reload();
	}

	/**
	 * �����ļ����ö���.
	 * 
	 * @param name
	 *            ��������
	 * @param filename
	 *            �ļ�����
	 * @param monitor
	 *            �Ƿ�����ļ����
	 * @throws Exception
	 */
	public FileConfig(String name, String filename, boolean monitor) throws Exception {
		super(name);
		this.filename = filename;
		this.monitor = monitor;
		reload();
	}

	/**
	 * ��ȡ�����ļ�����.
	 * 
	 * @return �����ļ�����
	 */
	public String getFilename() {
		return filename;
	}

	/**
	 * ��ȡ�ļ����ʱ����.
	 * 
	 * @return �ļ����ʱ����
	 */
	public long getTimeMark() {
		return timeMark;
	}

	/**
	 * ��ȡ�ַ��Ͳ����������ڻ�ȡǰ�ж��Ƿ���Ҫ���������ļ�.
	 */
	@Override
	public String getValue(String name) {
		try {
			if (monitor && isModified())
				reload();
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		return super.getValue(name);
	}

	/**
	 * ��ȡ�ַ��Ͳ����������ڻ�ȡǰ�ж��Ƿ���Ҫ���������ļ�.
	 */
	@Override
	public String getValue(String name, String defValue) {
		try {
			if (monitor && isModified())
				reload();
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		return super.getValue(name, defValue);
	}

	/**
	 * �ж��ļ��Ƿ������.
	 * 
	 * @return �ļ��Ƿ������
	 */
	public boolean isModified() {
		File file = new File(filename);
		if (monitor) {
			if (Math.abs(checkModifiedTimeMark - System.currentTimeMillis()) > 1000 * 5) {
				checkModifiedTimeMark = System.currentTimeMillis();
				return (timeMark != file.lastModified());
			} else
				return false;
		} else {
			return (timeMark != file.lastModified());
		}
	}

	/**
	 * ��ȡ�Ƿ�����ļ����.
	 * 
	 * @return �Ƿ�����ļ����
	 */
	public boolean isMonitor() {
		return monitor;
	}

	/**
	 * ���ļ���ȡ����.
	 * 
	 * @return �ļ�����
	 * @throws Exception
	 */
	abstract protected HashMap loadValues() throws Exception;

	/**
	 * ���¶�ȡ�ļ�����.
	 * 
	 * @throws Exception
	 */
	public synchronized void reload() throws Exception {
		try {
			HashMap tmp = loadValues();
			setValues(tmp);
		}
		catch (Exception ex) {
			throw ex;
		}
		finally {
			this.timeMark = System.currentTimeMillis();
		}
	}

	/**
	 * ���浽�ļ�.
	 * 
	 * @throws Exception
	 */
	public synchronized void saveToFile() throws Exception {
		saveToFile(this.filename);
	}

	/**
	 * ���浽�ļ�.
	 * 
	 * @param filename
	 *            �ļ���
	 * @throws Exception
	 */
	public synchronized void saveToFile(String filename) throws Exception

	{
		File file = new File(filename);
		File dir = file.getParentFile();
		if (!dir.exists())
			dir.mkdirs();
		writeToFile(file);
	}

	/**
	 * toStringǰ�ж��Ƿ��ļ��������.
	 */
	@Override
	public String toString() {
		try {
			if (monitor && isModified())
				reload();
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		return super.toString();
	}

	/**
	 * д�뵽�ļ�.
	 * 
	 * @param file
	 *            �ļ�
	 * @throws Exception
	 */
	abstract protected void writeToFile(File file) throws Exception;
}
