package com.haocom.util.data_scanner;

/**
 * Data Reader. <br>
 * 用于读取数据.
 * <p>
 * Copyright: Copyright (c) 2009-4-16 下午04:00:03
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
	 * 获取当前数据
	 * 
	 * @return 数据
	 */
	public E getData();

	/**
	 * 读取下一个数据,并返回是否读取到数据
	 * 
	 * @return 是否读取到数据
	 * @throws Exception
	 */
	public boolean next() throws Exception;
}
