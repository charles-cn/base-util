package com.haocom.config;

import java.io.File;
import java.util.HashMap;

/**
 * 文件配置对象. <br>
 * 读取文件保存的配置.
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

	/** 检查文件是否变更的时间标记 */
	private long checkModifiedTimeMark = 0;

	/** 文件名 */
	private String filename;

	/** 是否监视文件变动 */
	private boolean monitor = false;

	/** 文件变更标记 */
	private long timeMark = 0;

	/**
	 * 创建文件配置对象，默认不监视文件变更.
	 * 
	 * @param name
	 *            配置名称
	 * @param filename
	 *            文件名称
	 * @throws Exception
	 */
	public FileConfig(String name, String filename) throws Exception {
		super(name);
		this.filename = filename;
		reload();
	}

	/**
	 * 创建文件配置对象.
	 * 
	 * @param name
	 *            配置名称
	 * @param filename
	 *            文件名称
	 * @param monitor
	 *            是否跟踪文件变更
	 * @throws Exception
	 */
	public FileConfig(String name, String filename, boolean monitor) throws Exception {
		super(name);
		this.filename = filename;
		this.monitor = monitor;
		reload();
	}

	/**
	 * 获取配置文件名称.
	 * 
	 * @return 配置文件名称
	 */
	public String getFilename() {
		return filename;
	}

	/**
	 * 获取文件变更时间标记.
	 * 
	 * @return 文件变更时间标记
	 */
	public long getTimeMark() {
		return timeMark;
	}

	/**
	 * 获取字符型参数，但是在获取前判断是否需要重新载入文件.
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
	 * 获取字符型参数，但是在获取前判断是否需要重新载入文件.
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
	 * 判断文件是否发生变更.
	 * 
	 * @return 文件是否发生变更
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
	 * 获取是否监视文件变更.
	 * 
	 * @return 是否监视文件变更
	 */
	public boolean isMonitor() {
		return monitor;
	}

	/**
	 * 从文件读取配置.
	 * 
	 * @return 文件配置
	 * @throws Exception
	 */
	abstract protected HashMap loadValues() throws Exception;

	/**
	 * 重新读取文件配置.
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
	 * 保存到文件.
	 * 
	 * @throws Exception
	 */
	public synchronized void saveToFile() throws Exception {
		saveToFile(this.filename);
	}

	/**
	 * 保存到文件.
	 * 
	 * @param filename
	 *            文件名
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
	 * toString前判断是否文件发生变更.
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
	 * 写入到文件.
	 * 
	 * @param file
	 *            文件
	 * @throws Exception
	 */
	abstract protected void writeToFile(File file) throws Exception;
}
