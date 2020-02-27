package com.haocom.util;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashSet;

/**
 * Dump������. <br>
 * Dump�����࣬ʵ�ֽ�����������Dump��������������ദʹ�õĹ���.
 * <p>
 * Copyright: Copyright (c) Sep 17, 2008
 * <p>
 * Company: 
 * <p>
 * Author: ChengFan
 * <p>
 * Version: 1.0
 */
public class DumpOutputStream extends OutputStream {

	/** dump��ż��� */
	private HashSet<OutputStream> dumpList = new HashSet<OutputStream>();

	/** ������ */
	private OutputStream outputStream;

	/**
	 * ���캯��
	 * 
	 * @param outputStream
	 *            һ�������
	 */
	public DumpOutputStream(OutputStream outputStream) {
		this.outputStream = outputStream;
	}

	/**
	 * ���һ��dump����������ڲ�������.<br>
	 * �ڲ���HashSet����������������ظ�
	 * 
	 * @param dump
	 *            һ��dump�������
	 */
	public void addDump(OutputStream dump) {
		dumpList.add(dump);
	}

	/**
	 * �ر���������ͷ�������йص�����ϵͳ��Դ
	 */
	@Override
	public void close() throws IOException {
		outputStream.close();
	}

	/**
	 * ˢ�µ�ǰ������������е��������������ǿ��д�����л��������ֽ�
	 */
	@Override
	public void flush() throws IOException {
		outputStream.flush();
		for (OutputStream outputStream : dumpList) {
			try {
				outputStream.flush();
			}
			catch (Exception ex) {
			}
		}
	}

	/**
	 * ��b.length���ֽڴ�ָ��byte����д�뵱ǰ������������������������
	 * 
	 * @param b
	 *            ����
	 */
	@Override
	public void write(byte[] b) throws IOException {
		outputStream.write(b);
		for (OutputStream outputStream : dumpList) {
			try {
				outputStream.write(b);
			}
			catch (Exception ex) {
			}
		}
	}

	/**
	 * ��ָ��byte�����д�ƫ����off��ʼ��len���ֽ�д�뵱ǰ������������������������
	 * 
	 * @param b
	 *            ����
	 * @param off
	 *            �����е���ʼƫ����
	 * @param len
	 *            Ҫд����ֽ���
	 */
	@Override
	public void write(byte[] b, int off, int len) throws IOException {
		outputStream.write(b, off, len);
		for (OutputStream outputStream : dumpList) {
			try {
				outputStream.write(b, off, len);
			}
			catch (Exception ex) {
			}
		}
	}

	/**
	 * ��ָ���ֽ�д�뵱ǰ�����������������������С�ʵ��OutputStream��write����
	 * 
	 * @param b
	 *            Ҫд����ֽ�
	 */
	@Override
	public void write(int b) throws IOException {
		outputStream.write(b);
		for (OutputStream outputStream : dumpList) {
			try {
				outputStream.write(b);
			}
			catch (Exception ex) {
			}
		}
	}
}
