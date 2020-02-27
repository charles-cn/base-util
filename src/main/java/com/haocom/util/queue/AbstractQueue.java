package com.haocom.util.queue;

/**
 * 抽象队列类. <br>
 * 队列基本配置，包括队列名称和大小等.
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

	/** 队列大小上限 */
	int maxSize = -1;

	/** 队列名称 */
	private String name = null;

	/**
	 * 构造函数，定义队列的名称和大小
	 * 
	 * @param name
	 *            队列名称
	 * @param maxSize
	 *            队列大小上限
	 */
	public AbstractQueue(String name, int maxSize) {
		this.name = name;
		this.maxSize = maxSize;
	}

	/**
	 * 获取队列大小
	 * 
	 * @return 队列大小
	 */
	public int getMaxQueueSize() {
		return maxSize;
	}

	/**
	 * 获取队列名称
	 * 
	 * @return 队列名称
	 */
	public String getName() {
		return name;
	}

	/**
	 * 查看队列是否已满
	 * 
	 * @return true：队列已满；false：队列未满
	 */
	public boolean isFull() {
		return ((maxSize > 0) && (getSize() >= maxSize));
	}
}
