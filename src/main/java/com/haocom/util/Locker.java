package com.haocom.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * ������. <br>
 * �����ڽ����ڲ���ֹ��ĳ�������ظ����ʣ����������ظ�ִ��.<br>
 * 
 * <pre>
 * 1. ��ȡ����
 * Locker locker = Locker.getInstance(&quot;MmsBatchSubTask&quot;);
 * </pre>
 * 
 * <pre>
 *  2. �ж�ĳ�������Ƿ�����
 * locker.isLocked(task.getTaskId());
 * </pre>
 * 
 * <pre>
 * 3. ʹ�����Ĵ�����뷶����
 * if(!locker.lock(task.getTaskId())
 *     return;
 * try{
 *     // deal the task
 * } finally {
 * locker.unlock(task.getTaskId());
 * }
 * 
 * </pre>
 * 
 * <p>
 * Copyright: Copyright (c) 2008-8-5
 * <p>
 * Company: 
 * <p>
 * Author: ChengFan
 * <p>
 * Version: 1.0
 */
public class Locker {

	/** ����ʵ���嵥 */
	private static Map<String, Locker> instanceMap = new HashMap<String, Locker>();

	/**
	 * ��ȡ������ʵ��. <br>
	 * �������������ᴴ��һ���µ�ʵ����
	 * 
	 * @param lockerName
	 *            �������ƣ���������������������
	 * @return ������ʵ��
	 */
	public synchronized static Locker getInstance(String lockerName) {
		Locker instance = instanceMap.get(lockerName);
		if (instance == null) {
			instance = new Locker(lockerName);
			instanceMap.put(lockerName, instance);
		}
		return instance;
	}

	/** �������� */
	String lockerName;

	/** �����Ķ����嵥 */
	private Set<Object> targetMap = new HashSet<Object>();

	/**
	 * ���󴴽�
	 * 
	 * @param lockerName
	 *            ��������
	 */
	Locker(String lockerName) {
		this.lockerName = lockerName;
	}

	/**
	 * ��ȡ��������
	 * 
	 * @return ��������
	 */
	public String getLockerName() {
		return this.lockerName;
	}

	/**
	 * ��ȡ��ǰ����������嵥�ĸ���
	 * 
	 * @return ��ǰ����������嵥�ĸ���
	 */
	public synchronized Set<Object> getTargetMapCopy() {
		return new HashSet<Object>(this.targetMap);
	}

	/**
	 * ��ȡ�˶����Ƿ��Ѿ�������
	 * 
	 * @param target
	 *            ����
	 * @return �˶����Ƿ��Ѿ�����������Ѿ������򷵻�true����֮����false
	 */
	public synchronized boolean isLocked(Object target) {
		return targetMap.contains(target);
	}

	/**
	 * ��������ǰ����
	 * 
	 * @param target
	 *            ����
	 * @return ����ɹ��򷵻�true����������Ѿ��������򷵻�false
	 */
	public synchronized boolean lock(Object target) {
		if (targetMap.contains(target)) {
			return false;
		} else {
			targetMap.add(target);
			return true;
		}
	}

	/**
	 * ����ָ���Ķ���
	 * 
	 * @param target
	 *            ����
	 * @return ��������������ҽ����ɹ��򷵻�true���������δ�������򷵻�false��
	 */
	public synchronized boolean unlock(Object target) {
		if (targetMap.contains(target)) {
			targetMap.remove(target);
			return true;
		} else {
			return false;
		}
	}
}
