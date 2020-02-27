package com.haocom.util.data_scanner.file;

import com.haocom.util.data_scanner.DataReader;
import com.haocom.util.data_scanner.MutilDataScaner;

/**
 * �ļ����ݱȽ���. <br>
 * �������ڱȽ��ļ��ڵ�����.
 * <p>
 * Copyright: Copyright (c) 2009-4-23 ����09:11:02
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
	 * �����ļ���ȡ��
	 * 
	 * @param filenames
	 *            �ļ����б�
	 * @return �ļ���ȡ���б�
	 * @throws Exception
	 *             �쳣
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
	 * ������
	 * 
	 * @param filenames
	 *            �ļ����б�
	 * @throws Exception
	 *             �쳣
	 */
	@SuppressWarnings("unchecked")
	public FileDataScaner(String... filenames) throws Exception {
		super(makeFileReaders(filenames));
	}
}
