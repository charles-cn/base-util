package com.haocom.util.data_scanner.file;

import com.haocom.util.data_scanner.DataReader;
import com.haocom.util.data_scanner.MutilDataScaner;

/**
 * 文件数据比较器. <br>
 * 该类用于比较文件内的数据.
 * <p>
 * Copyright: Copyright (c) 2009-4-23 上午09:11:02
 * <p>
 * Company: 
 * <p>
 * Author: gaowei
 * <p>
 * Version: 1.0
 * <p>
 */
public abstract class FileDataScaner<E> extends MutilDataScaner<E> {

	/**
	 * 产生文件读取器
	 * 
	 * @param filenames
	 *            文件名列表
	 * @return 文件读取器列表
	 * @throws Exception
	 *             异常
	 */
	@SuppressWarnings("unchecked")
	private static DataReader[] makeFileReaders(String... filenames) throws Exception {
		FileDataReader[] fileDataReaders = new FileDataReader[filenames.length];
		for (int i = 0; i < filenames.length; i++) {
			fileDataReaders[i] = new FileDataReader(filenames[i]);
		}
		return fileDataReaders;
	}

	/**
	 * 构造器
	 * 
	 * @param filenames
	 *            文件名列表
	 * @throws Exception
	 *             异常
	 */
	@SuppressWarnings("unchecked")
	public FileDataScaner(String... filenames) throws Exception {
		super(makeFileReaders(filenames));
	}
}
