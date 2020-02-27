package com.haocom.util.data_scanner;

/**
 * Data Reader. <br>
 * ���ڶ�ȡ����.
 * <p>
 * Copyright: Copyright (c) 2009-4-16 ����04:00:03
 * <p>
 * Company: 
 * <p>
 * Author: ChengFan
 * <p>
 * Version: 1.0
 * <p>
 */
public interface DataReader<E> {

	/**
	 * ��ȡ��ǰ����
	 * 
	 * @return ����
	 */
	public E getData();

	/**
	 * ��ȡ��һ������,�������Ƿ��ȡ������
	 * 
	 * @return �Ƿ��ȡ������
	 * @throws Exception
	 */
	public boolean next() throws Exception;
}
