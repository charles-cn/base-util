package com.haocom.util.buffer;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 长整形数据内存缓存组件。 <br>
 * 此组件用于大批量号码缓存，64M内存大约可缓存3500万个号码（长整型），例如黑名单过滤等。<br>
 * 存放时长整形数据拆分后缓存在内存中。<br>
 * 缓存方式为Map<Hi, Lo>, 其中 <br>
 * Hi = value / 5000 <br>
 * Lo = (value % 5000); <br>
 * <br>
 * <h4>初始化代码范例:</h4>
 * 
 * <pre>
 * LongDataBuffer buffer = new LongDataBuffer(); //创建实例
 * buffer.put(13770697389L); //添加数据
 * buffer.put(13770697390L); //添加数据
 * ...  //添加数据
 * </pre>
 * 
 * <br>
 * <h4>判断数据是否在缓存中的范例:</h4>
 * 
 * <pre>
 *  if(buffer.exists(13851654278L)) {...}
 * </pre>
 * 
 * <h3>注意1：在首次执行exists方法时，组件会整理、压缩数据。所以，一旦执行了exists方法，切勿执行put方法。</h3> <br>
 * <h3>注意2：此组件的Put方法非线程安全。</h3> <br>
 * <h3>注意3：加载1000万记录时，平均耗时10s</h3> <br>
 * <p>
 * Copyright: Copyright (c) 2009-10-13 下午05:14:22
 * <p>
 * Company: 
 * <p>
 * 
 * @author chengfan@c-platform.com
 * @version 1.0.0
 * @History: Mar 23, 2012 11:09:24 AM gaowei 为了保持兼容性，简单的将{@link #exists(long)}
 *           方法改为同步<br>
 */
public class LongDataBuffer {

	/** 缓存数组每次增长长度 */
	static int BUFFER_SIZE_INC_STEP = 50;

	/** 每个缓存数组数值上限 */
	static int SPLITE_SIZE = 5000;

	/** 数据缓存 */
	private Map<Integer, short[]> dataBuffer = new HashMap<Integer, short[]>();

	/** 记录数量 */
	private int size = 0;

	/** 数据是否已经排序 */
	private boolean sorted = false;

	/**
	 * 对象创建
	 */
	public LongDataBuffer() throws Exception {
	}

	/**
	 * 判断数值是否在缓存中
	 * 
	 * @param value
	 *            数值
	 * @return 是否在缓存中
	 */
	public boolean exists(long value) {
		// 如果没有排序，则对数据进行排序
		// 同步，避免多线程问题 edit by gaowei 2012-3-23
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
	 * 获取 size
	 * 
	 * @return size
	 */
	public int getSize() {
		return size;
	}

	/**
	 * 将数据放入缓存中
	 * 
	 * @param value
	 *            数值
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
	 * 对数据进行排序
	 */
	private void sortData() {
		for (short[] buf : dataBuffer.values()) {
			Arrays.sort(buf);
		}
		sorted = true;
	}
}
