package com.haocom.util.data_scanner.file;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import com.haocom.util.data_scanner.DataReader;

/**
 * ��ȡ�ļ�����. <br>
 * ���ڶ�ȡ�ļ�����.
 * <p>
 * Copyright: Copyright (c) 2009-4-16 ����04:08:05
 * <p>
 * Company: 
 * <p>
 * Author: ChengFan
 * <p>
 * Version: 1.0
 * <p>
 */
public class FileDataReader<E> implements DataReader<E> {

	/** ��ǰ���� */
	protected E currentData = null;

	private String filename;

	protected BufferedReader reader;

	/**
	 * ������
	 * 
	 * @param filename
	 *            ��Ҫ��ȡ���ļ���
	 * @throws Exception
	 */
	public FileDataReader(String filename) throws Exception {
		this(filename, 1024 * 64, "ISO8859-1");
	}

	/**
	 * ������
	 * 
	 * @param filename
	 *            ��Ҫ��ȡ���ļ���
	 * @param bufferSize
	 *            ���뻺�����Ĵ�С
	 * @throws Exception
	 */
	public FileDataReader(String filename, int bufferSize) throws Exception {
		this(filename, bufferSize, "ISO8859-1");
	}

	/**
	 * ������
	 * 
	 * @param filename
	 *            ��Ҫ��ȡ���ļ���
	 * @param bufferSize
	 *            ���뻺�����Ĵ�С
	 * @param encoding
	 *            ����
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
