package com.haocom.util.queue;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Vector;

/**
 * 缓冲队列. <br>
 * 缓冲队列，定义了一些操作.
 * <p>
 * Copyright: Copyright (c) Sep 16, 2008
 * <p>
 * Company: 
 * <p>
 * Author:
 * <p>
 * Version: 1.0
 */
public class QueueStorage {

	class QueueComparator implements Comparator<Queue<?>> {

		/**
		 * 队列比较器
		 * 
		 * @param o1
		 *            队列o1
		 * @param o2
		 *            队列o2
		 * @return 若o1==null则返回1；若o2==null则返回-1；<BR>
		 *         反之返回两队列名称大小的比较,即按字典顺序比较两个字符串，不考虑大小写<BR>
		 *         根据o1.getName大于、等于还是小于o2.getName()，分别返回一个负整数、0 或一个正整数。
		 */
		public int compare(Queue<?> o1, Queue<?> o2) {
			if (o1 == null) {
				return 1;
			}
			if (o2 == null) {
				return -1;
			}
			String n1 = o1.getName();
			String n2 = o2.getName();
			return n1.compareToIgnoreCase(n2);
		}
	}

	/** 队列存储实例 */
	private static QueueStorage instance = new QueueStorage();

	/**
	 * 队列存储实例
	 * 
	 * @return 队列存储实例
	 */
	public static QueueStorage getInstance() {
		return instance;
	}

	/** 队列比较器 */
	private QueueComparator queueComparator = new QueueComparator();

	/** 缓冲队列清单 <Name, Queue> */
	private HashMap<String, Queue<?>> queueList = new HashMap<String, Queue<?>>();

	/**
	 * 默认构造函数
	 */
	public QueueStorage() {
	}

	/**
	 * 向队列集合中添加队列
	 * 
	 * @param queue
	 *            要添加的队列
	 */
	public void addQueue(Queue<?> queue) {
		synchronized (queueList) {
			queueList.put(queue.getName(), queue);
		}
	}

	/**
	 * 向队列集合中获取队列
	 * 
	 * @param name
	 *            要获取的队列名
	 * @return 队列集合中的一个队列
	 */
	public Queue<?> getQueue(String name) {
		synchronized (queueList) {
			return queueList.get(name);
		}
	}

	/**
	 * 获取队列集合
	 * 
	 * @return 队列集合
	 */
	public Vector<Queue<?>> getQueueList() {
		Vector<Queue<?>> list = new Vector<Queue<?>>(queueList.values());
		Collections.sort(list, queueComparator);
		return list;

	}

	/**
	 * 获取队列名称集合
	 * 
	 * @return 队列名称集合
	 */
	public Vector<String> getQueueNameList() {
		Vector<String> list = new Vector<String>(queueList.keySet());
		return list;
	}

	/**
	 * 从队列集合中取出一个队列
	 * 
	 * @param name
	 *            队列名称
	 * @return 指定名称的一个队列
	 */
	public Queue<?> removeQueue(String name) {
		synchronized (queueList) {
			return queueList.remove(name);
		}
	}
}
