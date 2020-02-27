package com.haocom.util.data_scanner.file;

import java.io.BufferedWriter;
import java.io.FileWriter;

/**
 * �ļ�����. <br>
 * <p>
 * Copyright: Copyright (c) 2009-4-18 ����10:50:17
 * <p>
 * Company: 
 * <p>
 * Author: ChengFan
 * <p>
 * Version: 1.0
 * <p>
 */
public class FileUnionAll<E> extends FileDataScaner<E> {

	/** ���ؽ�������� */
	int resultCount = 0;

	/** ����� */
	BufferedWriter writer;

	/**
	 * ������
	 * 
	 * @param filename1
	 *            ��һ���ļ���
	 * @param filename2
	 *            �ڶ����ļ���
	 * @param outFilename
	 *            ���������ļ���
	 * @throws Exception
	 */
	public FileUnionAll(String filename1, String filename2, String outFilename) throws Exception {
		super(new String[] { filename1, filename2 });
		writer = new BufferedWriter(new FileWriter(outFilename), 1024 * 256);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected int compare(E e1, E e2) {
		if (e1 instanceof Comparable) {
			return ((Comparable) e1).compareTo(e2);
		} else {
			return 0;
		}
	}

	@Override
	protected boolean dealCompareResult(boolean[] type, Object... resultDatas) throws Exception {
		for (Object obj : resultDatas) {
			if (obj != null) {
				writer.write(obj.toString());
				writer.write('\r');
				writer.write('\n');
				resultCount++;
			}
		}
		return true;
	}

	@Override
	protected void doFinalize() throws Exception {
		writer.flush();
		writer.close();
	}

	/**
	 * ��ȡ resultCount
	 * 
	 * @return resultCount
	 */
	public int getResultCount() {
		return this.resultCount;
	}
}
