package com.haocom.util.queue;

/**
 * ���������. <br>
 * ���л������ã������������ƺʹ�С��.
 * <p>
 * Copyright: Copyright (c) Sep 16, 2008
 * <p>
 * Company: 
 * <p>
 * Author:
 * <p>
 * Version: 1.0
 */
public abstract class AbstractQueue<E> implements Queue<E> {

	/** ���д�С���� */
	int maxSize = -1;

	/** �������� */
	private String name = null;

	/**
	 * ���캯����������е����ƺʹ�С
	 * 
	 * @param name
	 *            ��������
	 * @param maxSize
	 *            ���д�С����
	 */
	public AbstractQueue(String name, int maxSize) {
		this.name = name;
		this.maxSize = maxSize;
	}

	/**
	 * ��ȡ���д�С
	 * 
	 * @return ���д�С
	 */
	public int getMaxQueueSize() {
		return maxSize;
	}

	/**
	 * ��ȡ��������
	 * 
	 * @return ��������
	 */
	public String getName() {
		return name;
	}

	/**
	 * �鿴�����Ƿ�����
	 * 
	 * @return true������������false������δ��
	 */
	public boolean isFull() {
		return ((maxSize > 0) && (getSize() >= maxSize));
	}
}
