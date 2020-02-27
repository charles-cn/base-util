package com.haocom.util.queue;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Vector;

/**
 * �������. <br>
 * ������У�������һЩ����.
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
		 * ���бȽ���
		 * 
		 * @param o1
		 *            ����o1
		 * @param o2
		 *            ����o2
		 * @return ��o1==null�򷵻�1����o2==null�򷵻�-1��<BR>
		 *         ��֮�������������ƴ�С�ıȽ�,�����ֵ�˳��Ƚ������ַ����������Ǵ�Сд<BR>
		 *         ����o1.getName���ڡ����ڻ���С��o2.getName()���ֱ𷵻�һ����������0 ��һ����������
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

	/** ���д洢ʵ�� */
	private static QueueStorage instance = new QueueStorage();

	/**
	 * ���д洢ʵ��
	 * 
	 * @return ���д洢ʵ��
	 */
	public static QueueStorage getInstance() {
		return instance;
	}

	/** ���бȽ��� */
	private QueueComparator queueComparator = new QueueComparator();

	/** ��������嵥 <Name, Queue> */
	private HashMap<String, Queue<?>> queueList = new HashMap<String, Queue<?>>();

	/**
	 * Ĭ�Ϲ��캯��
	 */
	public QueueStorage() {
	}

	/**
	 * ����м�������Ӷ���
	 * 
	 * @param queue
	 *            Ҫ��ӵĶ���
	 */
	public void addQueue(Queue<?> queue) {
		synchronized (queueList) {
			queueList.put(queue.getName(), queue);
		}
	}

	/**
	 * ����м����л�ȡ����
	 * 
	 * @param name
	 *            Ҫ��ȡ�Ķ�����
	 * @return ���м����е�һ������
	 */
	public Queue<?> getQueue(String name) {
		synchronized (queueList) {
			return queueList.get(name);
		}
	}

	/**
	 * ��ȡ���м���
	 * 
	 * @return ���м���
	 */
	public Vector<Queue<?>> getQueueList() {
		Vector<Queue<?>> list = new Vector<Queue<?>>(queueList.values());
		Collections.sort(list, queueComparator);
		return list;

	}

	/**
	 * ��ȡ�������Ƽ���
	 * 
	 * @return �������Ƽ���
	 */
	public Vector<String> getQueueNameList() {
		Vector<String> list = new Vector<String>(queueList.keySet());
		return list;
	}

	/**
	 * �Ӷ��м�����ȡ��һ������
	 * 
	 * @param name
	 *            ��������
	 * @return ָ�����Ƶ�һ������
	 */
	public Queue<?> removeQueue(String name) {
		synchronized (queueList) {
			return queueList.remove(name);
		}
	}
}
