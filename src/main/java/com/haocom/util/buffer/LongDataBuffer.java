package com.haocom.util.buffer;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * �����������ڴ滺������� <br>
 * ��������ڴ��������뻺�棬64M�ڴ��Լ�ɻ���3500������루�����ͣ���������������˵ȡ�<br>
 * ���ʱ���������ݲ�ֺ󻺴����ڴ��С�<br>
 * ���淽ʽΪMap<Hi, Lo>, ���� <br>
 * Hi = value / 5000 <br>
 * Lo = (value % 5000); <br>
 * <br>
 * <h4>��ʼ�����뷶��:</h4>
 * 
 * <pre>
 * LongDataBuffer buffer = new LongDataBuffer(); //����ʵ��
 * buffer.put(13770697389L); //�������
 * buffer.put(13770697390L); //�������
 * ...  //�������
 * </pre>
 * 
 * <br>
 * <h4>�ж������Ƿ��ڻ����еķ���:</h4>
 * 
 * <pre>
 *  if(buffer.exists(13851654278L)) {...}
 * </pre>
 * 
 * <h3>ע��1�����״�ִ��exists����ʱ�����������ѹ�����ݡ����ԣ�һ��ִ����exists����������ִ��put������</h3> <br>
 * <h3>ע��2���������Put�������̰߳�ȫ��</h3> <br>
 * <h3>ע��3������1000���¼ʱ��ƽ����ʱ10s</h3> <br>
 * <p>
 * Copyright: Copyright (c) 2009-10-13 ����05:14:22
 * <p>
 * Company: 
 * <p>
 * 
 * @author chengfan@c-platform.com
 * @version 1.0.0
 * @History: Mar 23, 2012 11:09:24 AM gaowei Ϊ�˱��ּ����ԣ��򵥵Ľ�{@link #exists(long)}
 *           ������Ϊͬ��<br>
 */
public class LongDataBuffer {

	/** ��������ÿ���������� */
	static int BUFFER_SIZE_INC_STEP = 50;

	/** ÿ������������ֵ���� */
	static int SPLITE_SIZE = 5000;

	/** ���ݻ��� */
	private Map<Integer, short[]> dataBuffer = new HashMap<Integer, short[]>();

	/** ��¼���� */
	private int size = 0;

	/** �����Ƿ��Ѿ����� */
	private boolean sorted = false;

	/**
	 * ���󴴽�
	 */
	public LongDataBuffer() throws Exception {
	}

	/**
	 * �ж���ֵ�Ƿ��ڻ�����
	 * 
	 * @param value
	 *            ��ֵ
	 * @return �Ƿ��ڻ�����
	 */
	public boolean exists(long value) {
		// ���û������������ݽ�������
		// ͬ����������߳����� edit by gaowei 2012-3-23
		synchronized (this) {
			if (!sorted) {
				sortData();
			}
		}
		//
		int hi = (int) value / SPLITE_SIZE;
		short lo = (short) (value % SPLITE_SIZE);

		short[] buf = dataBuffer.get(hi);
		if (buf == null) {
			return false;
		} else {
			return Arrays.binarySearch(buf, lo) >= 0;
		}
	}

	/**
	 * ��ȡ size
	 * 
	 * @return size
	 */
	public int getSize() {
		return size;
	}

	/**
	 * �����ݷ��뻺����
	 * 
	 * @param value
	 *            ��ֵ
	 */
	public void put(long value) {
		int hi = (int) value / SPLITE_SIZE;
		short lo = (short) (value % SPLITE_SIZE);
		short[] buf = dataBuffer.get(hi);
		if (buf == null) {
			buf = new short[BUFFER_SIZE_INC_STEP];
			Arrays.fill(buf, (short) -1);
			dataBuffer.put(hi, buf);
		}
		boolean needResize = true;
		for (int i = 0; i < BUFFER_SIZE_INC_STEP; i++) {
			if (buf[buf.length - 1 - i] < 0) {
				buf[buf.length - 1 - i] = lo;
				needResize = false;
				break;
			}
		}
		if (needResize) {
			short[] newBuf = new short[buf.length + BUFFER_SIZE_INC_STEP];
			Arrays.fill(newBuf, (short) -1);
			System.arraycopy(buf, 0, newBuf, 0, buf.length);
			buf = newBuf;
			dataBuffer.put(hi, buf);
			buf[buf.length - 1] = lo;
		}
		size++;
		sorted = false;
	}

	/**
	 * �����ݽ�������
	 */
	private void sortData() {
		for (short[] buf : dataBuffer.values()) {
			Arrays.sort(buf);
		}
		sorted = true;
	}
}
