package com.haocom.util.queue;

import java.lang.reflect.Array;

/**
 * ���ٶ���. <br>
 * ʹ��Object������Ϊ�������ݵ�����������������û���κ�new���������������<br>
 * ���Խ��Ϊÿ���������800������SimpleQueueΪ200��. <br>
 * ����addForce����Ϊ�����С�����ƣ�������ʧ�ܡ�<br>
 * <p>
 * Copyright: Copyright (c) Sep 17, 2008
 * <p>
 * Company: 
 * <p>
 * Author:
 * <p>
 * Version: 1.0
 */
public class SpeedQueue<E> extends AbstractQueue<E> {

	/** Reader�α�λ�� */
	private int positionR;

	/** Writer�α�λ�� */
	private int positionW;

	/** ������� */
	public Object[] queue;

	/** ������г��� */
	private int queueLength;

	/**
	 * SpeedQueue���캯���� <br>
	 * Ĭ�ϻ������Ϊ (maxSize + maxSize * 0.3)
	 * 
	 * @param name
	 *            ��������
	 * @param maxSize
	 *            �������ޣ�����0
	 */
	public SpeedQueue(String name, int maxSize) {
		this(name, maxSize, (int) (maxSize + maxSize * 0.3));
	}

	/**
	 * SpeedQueue���캯���� <br>
	 * 
	 * @param name
	 *            ��������
	 * @param maxSize
	 *            �������ޣ�����0
	 * @param initialCapacity
	 *            ������г���
	 */
	public SpeedQueue(String name, int maxSize, int initialCapacity) {
		super(name, maxSize);
		if (initialCapacity < maxSize) {
			initialCapacity = (int) (maxSize + maxSize * 0.5);
		}
		queue = new Object[initialCapacity];
		queueLength = initialCapacity;
		positionR = 0;
		positionW = 0;
	}

	/**
	 * 
	 */
	public synchronized boolean add(E obj) {
		if (maxSize < 0 || (positionW - positionR) < maxSize) {
			if (positionW < queueLength) {
				queue[positionW++] = obj;
			} else {
				queue[(positionW++) % queueLength] = obj;
			}
			return true;
		}
		return false;
	}

	public synchronized boolean addForce(E obj) {
		if (positionW > positionR && positionW - queueLength == positionR) {
			return false;
		} else {
			if (positionW < queueLength) {
				queue[positionW++] = obj;
			} else {
				queue[(positionW++) % queueLength] = obj;
			}
			return true;
		}
	}

	public synchronized void clear() {
		for (int i = 0; i < queueLength; i++) {
			queue[i] = null;
		}
		positionR = 0;
		positionW = 0;
	}

	public int getSize() {
		return positionW - positionR;
	}

	public boolean isEmpty() {
		return (positionW - positionR) == 0;
	}

	@SuppressWarnings("unchecked")
	public synchronized E peek() {
		if (positionR == positionW) {
			return null;
		} else {
			return (E) queue[(positionR) % queueLength];
		}
	}

	@SuppressWarnings("unchecked")
	public synchronized E remove() {
		if (positionR == positionW) {
			return null;
		} else {
			int pos = (positionR++);
			if (pos >= queueLength) {
				pos = pos % queueLength;
			}
			Object obj = queue[pos];
			queue[pos] = null;
			if (positionR == positionW) {
				positionR = 0;
				positionW = 0;
			}
			return (E) obj;
		}
	}

	public synchronized Object[] toArray() {
		Object[] result = new Object[getSize()];
		if (positionR != positionW) {
			for (int i = positionR, n = 0; i < positionW; i++, n++) {
				result[n] = queue[i % queueLength];
			}
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	public synchronized E[] toArray(E[] a) {
		E[] result = (E[]) Array.newInstance(a.getClass().getComponentType(), getSize());
		if (positionR != positionW) {
			for (int i = positionR, n = 0; i < positionW; i++, n++) {
				result[n] = (E) queue[i % queueLength];
			}
		}
		return result;
	}
}
