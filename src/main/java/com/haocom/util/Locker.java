package com.haocom.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 对象锁. <br>
 * 用于在进程内部防止对某个对象重复访问，例如任务被重复执行.<br>
 * 
 * <pre>
 * 1. 获取锁：
 * Locker locker = Locker.getInstance(&quot;MmsBatchSubTask&quot;);
 * </pre>
 * 
 * <pre>
 *  2. 判断某个对象是否被锁：
 * locker.isLocked(task.getTaskId());
 * </pre>
 * 
 * <pre>
 * 3. 使用锁的处理代码范例：
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

	/** 对象实例清单 */
	private static Map<String, Locker> instanceMap = new HashMap<String, Locker>();

	/**
	 * 获取对象锁实例. <br>
	 * 如果锁不存在则会创建一个新的实例。
	 * 
	 * @param lockerName
	 *            锁的名称，例如任务锁，号码锁等
	 * @return 对象锁实例
	 */
	public synchronized static Locker getInstance(String lockerName) {
		Locker instance = instanceMap.get(lockerName);
		if (instance == null) {
			instance = new Locker(lockerName);
			instanceMap.put(lockerName, instance);
		}
		return instance;
	}

	/** 锁的名称 */
	String lockerName;

	/** 被锁的对象清单 */
	private Set<Object> targetMap = new HashSet<Object>();

	/**
	 * 对象创建
	 * 
	 * @param lockerName
	 *            锁的名称
	 */
	Locker(String lockerName) {
		this.lockerName = lockerName;
	}

	/**
	 * 获取锁的名称
	 * 
	 * @return 锁的名称
	 */
	public String getLockerName() {
		return this.lockerName;
	}

	/**
	 * 获取当前被锁对象的清单的副本
	 * 
	 * @return 当前被锁对象的清单的副本
	 */
	public synchronized Set<Object> getTargetMapCopy() {
		return new HashSet<Object>(this.targetMap);
	}

	/**
	 * 获取此对象是否已经被锁。
	 * 
	 * @param target
	 *            对象
	 * @return 此对象是否已经被锁。如果已经被锁则返回true；反之返回false
	 */
	public synchronized boolean isLocked(Object target) {
		return targetMap.contains(target);
	}

	/**
	 * 锁定将当前对象
	 * 
	 * @param target
	 *            对象
	 * @return 如果成功则返回true；如果对象已经被锁，则返回false
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
	 * 解锁指定的对象
	 * 
	 * @param target
	 *            对象
	 * @return 如果对象锁定并且解锁成功则返回true；如果对象未被锁，则返回false。
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
