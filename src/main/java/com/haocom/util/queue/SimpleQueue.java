package com.haocom.util.queue;

import java.util.LinkedList;
import java.util.List;

/**
 * 简单队列. <br>
 * 定义了一个简单队列的基本属性.
 * <p>
 * Copyright: Copyright (c) Sep 17, 2008
 * <p>
 * Company: 
 * <p>
 * Author:
 * <p>
 * Version: 1.0
 */
public class SimpleQueue<E> extends AbstractQueue<E> {

	/** 缓冲队列 */
	List<E> queue = new LinkedList<E>();

	public SimpleQueue(String name, int maxSize) {
		super(name, maxSize);
	}

	public synchronized boolean add(E obj) {
		if (maxSize < 0 || queue.size() < maxSize) {
			return queue.add(obj);
		}
		return false;
	}

	public synchronized boolean addForce(E obj) {
		return queue.add(obj);
	}

	public synchronized void clear() {
		queue.clear();
	}

	public int getSize() {
		return queue.size();
	}

	public boolean isEmpty() {
		return queue.isEmpty();
	}

	public synchronized E peek() {
		if (!queue.isEmpty()) {
			return queue.get(0);
		} else {
			return null;
		}
	}

	public synchronized E remove() {
		if (!queue.isEmpty()) {
			return queue.remove(0);
		} else {
			return null;
		}
	}

	public synchronized Object[] toArray() {
		return queue.toArray().clone();
	}

	public synchronized E[] toArray(E[] a) {
		return queue.toArray(a);
	}
}
