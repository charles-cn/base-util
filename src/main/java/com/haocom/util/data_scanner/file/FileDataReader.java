package com.haocom.util.data_scanner.file;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import com.haocom.util.data_scanner.DataReader;

/**
 * 读取文件数据. <br>
 * 用于读取文件数据.
 * <p>
 * Copyright: Copyright (c) 2009-4-16 下午04:08:05
 * <p>
 * Company: 
 * <p>
 * Author: ChengFan
 * <p>
 * Version: 1.0
 * <p>
 */
public class FileDataReader<E> implements DataReader<E> {

	/** 当前内容 */
	protected E currentData = null;

	private String filename;

	protected BufferedReader reader;

	/**
	 * 构造器
	 * 
	 * @param filename
	 *            需要读取的文件名
	 * @throws Exception
	 */
	public FileDataReader(String filename) throws Exception {
		this(filename, 1024 * 64, "ISO8859-1");
	}

	/**
	 * 构造器
	 * 
	 * @param filename
	 *            需要读取的文件名
	 * @param bufferSize
	 *            输入缓冲区的大小
	 * @throws Exception
	 */
	public FileDataReader(String filename, int bufferSize) throws Exception {
		this(filename, bufferSize, "ISO8859-1");
	}

	/**
	 * 构造器
	 * 
	 * @param filename
	 *            需要读取的文件名
	 * @param bufferSize
	 *            输入缓冲区的大小
	 * @param encoding
	 *            编码
	 * @throws Exception
	 */
	public FileDataReader(String filename, int bufferSize, String encoding) throws Exception {
		this.filename = filename;
		reader = new BufferedReader(new InputStreamReader(new FileInputStream(filename), encoding), bufferSize);
	}

	@Override
	public E getData() {
		return currentData;
	}

	@Override
	@SuppressWarnings("unchecked")
	public boolean next() throws Exception {
		currentData = (E) reader.readLine();
		return (currentData != null);
	}

	@Override
	public String toString() {
		StringBuilder buf = new StringBuilder(500);
		buf.append(this.getClass().getSimpleName()).append(":").append(filename);
		return buf.toString();
	}
}
