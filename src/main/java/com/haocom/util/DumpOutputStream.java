package com.haocom.util;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashSet;

/**
 * Dump功能类. <br>
 * Dump功能类，实现将数据流存入Dump，并可再输出到多处使用的功能.
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

	/** dump存放集合 */
	private HashSet<OutputStream> dumpList = new HashSet<OutputStream>();

	/** 数据流 */
	private OutputStream outputStream;

	/**
	 * 构造函数
	 * 
	 * @param outputStream
	 *            一个输出流
	 */
	public DumpOutputStream(OutputStream outputStream) {
		this.outputStream = outputStream;
	}

	/**
	 * 添加一个dump的输出流到内部集合中.<br>
	 * 内部由HashSet保存输出流，不可重复
	 * 
	 * @param dump
	 *            一个dump的输出流
	 */
	public void addDump(OutputStream dump) {
		dumpList.add(dump);
	}

	/**
	 * 关闭输出流并释放与此流有关的所有系统资源
	 */
	@Override
	public void close() throws IOException {
		outputStream.close();
	}

	/**
	 * 刷新当前输出流及集合中的所有输出流，并强制写出所有缓冲的输出字节
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
	 * 将b.length个字节从指定byte数组写入当前输出流及集合中所有输出流中
	 * 
	 * @param b
	 *            数据
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
	 * 将指定byte数组中从偏移量off开始的len个字节写入当前输出流及集合中所有输出流中
	 * 
	 * @param b
	 *            数据
	 * @param off
	 *            数据中的起始偏移量
	 * @param len
	 *            要写入的字节数
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
	 * 将指定字节写入当前输出流及集合中所有输出流中。实现OutputStream的write方法
	 * 
	 * @param b
	 *            要写入的字节
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
