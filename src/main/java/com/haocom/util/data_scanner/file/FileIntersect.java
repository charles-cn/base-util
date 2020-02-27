package com.haocom.util.data_scanner.file;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Arrays;

/**
 * 文件交集. <br>
 * <p>
 * Copyright: Copyright (c) 2009-4-18 上午10:52:29
 * <p>
 * Company: 
 * <p>
 * Author: ChengFan
 * <p>
 * Version: 1.0
 * <p>
 */
public class FileIntersect<E> extends FileDataScaner<E> {

	/** 真值表 */
	static final boolean[] TRUE_TABLE = new boolean[] { true, true };

	/** 返回结果的行数 */
	int resultCount = 0;

	/** 输出器 */
	BufferedWriter writer;

	/**
	 * 构造器
	 * 
	 * @param filename1
	 *            第一个文件名
	 * @param filename2
	 *            第二个文件名
	 * @param outFilename
	 *            输出结果的文件名
	 * @throws Exception
	 */
	public FileIntersect(String filename1, String filename2, String outFilename) throws Exception {
		super(new String[] { filename1, filename2 });
		writer = new BufferedWriter(new FileWriter(outFilename), 1024 * 256);
	}

	@Override
	@SuppressWarnings("unchecked")
	protected int compare(E e1, E e2) {
		if (e1 instanceof Comparable) {
			return ((Comparable) e1).compareTo(e2);
		} else {
			return 0;
		}
	}

	@Override
	protected boolean dealCompareResult(boolean[] type, Object... resultDatas) throws Exception {
		if (Arrays.equals(TRUE_TABLE, type)) {
			writer.write(resultDatas[0].toString());
			writer.write('\r');
			writer.write('\n');
			resultCount++;
		}
		return true;
	}

	@Override
	protected void doFinalize() throws Exception {
		writer.flush();
		writer.close();
	}

	/**
	 * 获取 resultCount
	 * 
	 * @return resultCount
	 */
	public int getResultCount() {
		return this.resultCount;
	}
}
