package com.haocom.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * 数据库监控器. <br>
 * 数据库监控器，监控数据库连接池使用情况.
 * <p>
 * Copyright: Copyright (c) Sep 17, 2008
 * <p>
 * Company: 
 * <p>
 * Author: ZhouYan
 * <p>
 * Version: 1.0
 */
class DBAccessMonitor extends Thread {

	/**
	 * 定义了堆栈的各项信息（info,poolName,threadId）
	 */
	class StackInfo {

		/** 堆栈信息 */
		String info;

		/** 连接池名称 */
		String poolName;

		/** 线程Id */
		long threadId;
	}

	/** 对象实例 */
	private static DBAccessMonitor instance;

	public static boolean monitor = false;

	/**
	 * 获取对象实例
	 * 
	 * @return DBAccessMonitor 对象实例
	 */
	public static DBAccessMonitor getInstance() {
		return instance;
	}

	/**
	 * 获取数据库监控器实例，为单例
	 */
	public synchronized static void init() {
		if (instance != null) {
			return;
		}
		instance = new DBAccessMonitor();
		instance.start();

		String enable = System.getProperty("com.cplatform.util.DBAccessMonitor.enable", "no");
		monitor = "yes".equals(enable);
	}

	/** 存放需要释放的连接池 */
	private HashMap<String, Object> callStackRecord_Free = new HashMap<String, Object>();

	/** 存放需要获取连接的连接池 */
	private HashMap<String, Object> callStackRecord_Get = new HashMap<String, Object>();

	/** 存放正在运行的连接池 */
	private HashMap<Long, Stack<StackInfo>> callStackRecord_Thread = new HashMap<Long, Stack<StackInfo>>();

	/** 存放使用发生警告的连接池 */
	private HashMap<String, Object> callStackRecord_Warning = new HashMap<String, Object>();

	/** 日志文件名 */
	private String logFilename;

	/**
	 * 对象创建
	 */
	DBAccessMonitor() {
		setName("DBAccess.Monitor");
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd.HHmmss");
		logFilename = "./log/dba/" + format.format(new Date()) + ".stack";
		this.setDaemon(true);
	}

	/**
	 * 记录FreeConnection的执行堆栈
	 * 
	 * @param poolName
	 *            连接池名
	 */
	public synchronized void recordFreeConnection(String poolName) {
		if (monitor) {
			Map<Thread, StackTraceElement[]> threadStackMap = Thread.getAllStackTraces();
			Thread currentThread = Thread.currentThread();
			StackTraceElement[] stackTraceElements = threadStackMap.get(currentThread);
			String info = stackToString(stackTraceElements, poolName);
			if (!callStackRecord_Free.containsKey(info)) {
				callStackRecord_Free.put(info, poolName);
			}
			recordStackPop(currentThread.getId(), poolName, info);
		}
	}

	/**
	 * 记录GetConnection的执行堆栈
	 * 
	 * @param poolName
	 *            连接池名
	 */
	public synchronized void recordGetConnection(String poolName) {
		if (monitor) {
			Map<Thread, StackTraceElement[]> threadStackMap = Thread.getAllStackTraces();
			Thread currentThread = Thread.currentThread();
			StackTraceElement[] stackTraceElements = threadStackMap.get(currentThread);
			String info = stackToString(stackTraceElements, poolName);
			if (!callStackRecord_Get.containsKey(info)) {
				callStackRecord_Get.put(info, poolName);
			}
			recordStackPush(currentThread.getId(), poolName, info);
		}
	}

	/**
	 * 记录数据库连接池出栈使用情况
	 * 
	 * @param threadId
	 *            正在运行的连接池的线程Id
	 * @param poolName
	 *            连接池名
	 * @param info
	 *            连接池使用信息
	 */
	private void recordStackPop(long threadId, String poolName, String info) {
		Stack<StackInfo> stack = callStackRecord_Thread.get(threadId);
		if (stack == null || stack.peek() == null) {
			// 未获取数据库连接，而直接释放连接
			StringBuilder buf = new StringBuilder(500);
			buf.append("未获取数据库连接，而直接释放连接:").append(poolName).append("\r\n");
			buf.append(info);
			String str = buf.toString();
			if (!callStackRecord_Warning.containsKey(str)) {
				callStackRecord_Warning.put(str, null);
			}
			return;
		}
		//
		StackInfo last = stack.peek();
		//
		StackInfo now = new StackInfo();
		now.threadId = threadId;
		now.poolName = poolName;
		now.info = info;
		stack.push(now);
		// 判断是否交叉释放
		if (!last.poolName.equals(poolName)) {
			StringBuilder buf = new StringBuilder(500);
			buf.append("pool name:").append(poolName).append(":").append("交叉释放").append(":").append(last.poolName);
			for (StackInfo s : stack) {
				buf.append(s.info);
			}
			String str = buf.toString();
			if (!callStackRecord_Warning.containsKey(str)) {
				callStackRecord_Warning.put(str, null);
			}
		}
		stack.pop();
		stack.pop();
		//
		if (stack.isEmpty()) {
			callStackRecord_Thread.remove(threadId);
		}
	}

	/**
	 * 记录数据库连接池入栈使用情况
	 * 
	 * @param threadId
	 *            正在运行的连接池的线程Id
	 * @param poolName
	 *            连接池名
	 * @param info
	 *            连接池使用信息
	 */
	private void recordStackPush(long threadId, String poolName, String info) {
		Stack<StackInfo> stack = callStackRecord_Thread.get(threadId);
		if (stack == null) {
			stack = new Stack<StackInfo>();
			callStackRecord_Thread.put(threadId, stack);
		}
		//
		StackInfo now = new StackInfo();
		now.threadId = threadId;
		now.poolName = poolName;
		now.info = info;
		//
		if (stack.isEmpty()) {
			stack.push(now);
			return;
		}
		StackInfo last = stack.peek();
		stack.push(now);
		// 判断是否嵌套
		if (last.poolName.equals(now.poolName)) {
			StringBuilder buf = new StringBuilder(500);
			buf.append("pool name:").append(poolName).append(":").append("嵌套调用\r\n");
			for (StackInfo s : stack) {
				buf.append(s.info);
			}
			String str = buf.toString();
			if (!callStackRecord_Warning.containsKey(str)) {
				callStackRecord_Warning.put(str, null);
			}
		}
	}

	@Override
	public void run() {
		while (true) {
			try {
				sleep(1000);
				saveStackRecord();
			}
			catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	/**
	 * 将所有的执行堆栈记录存入文件
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private void saveStackRecord() throws Exception {
		// 写文件
		File file = new File(logFilename);
		File dir = file.getParentFile();
		if (dir != null && !dir.exists()) {
			dir.mkdirs();
		}
		FileWriter fw = new FileWriter(logFilename, false);
		try {
			BufferedWriter bw = new BufferedWriter(fw, 1024 * 64);
			try {
				// 调用获取数据库连接的代码堆栈
				{
					HashMap<String, Object> map = (HashMap<String, Object>) this.callStackRecord_Get.clone();
					bw.write("\r\n\r\n");
					bw.write("调用获取数据库连接的代码堆栈：");
					bw.write(Integer.toString(map.size()));
					bw.write("\r\n");
					for (String info : map.keySet()) {
						bw.write(info);
					}
				}
				// 调用释放数据库连接的代码堆栈
				{
					HashMap<String, Object> map = (HashMap<String, Object>) this.callStackRecord_Free.clone();
					bw.write("\r\n\r\n");
					bw.write("调用释放数据库连接的代码堆栈：");
					bw.write(Integer.toString(map.size()));
					bw.write("\r\n");
					for (String info : map.keySet()) {
						bw.write(info);
					}
				}
				// 
				{
					HashMap<String, Object> map = (HashMap<String, Object>) this.callStackRecord_Warning.clone();
					bw.write("\r\n\r\n");
					bw.write("数据库调用警告：");
					bw.write(Integer.toString(map.size()));
					bw.write("\r\n");
					for (String info : map.keySet()) {
						bw.write(info);
						bw.write("\r\n");
					}
				}
				//
				bw.flush();
			}
			catch (Exception ex) {
				throw ex;
			}
			finally {
				bw.close();
			}
		}
		catch (Exception ex) {
			throw ex;
		}
		finally {
			fw.close();
		}
	}

	/**
	 * 依据执行堆栈创建字符说明
	 * 
	 * @param stackTraceElements
	 *            执行堆栈
	 * @param poolName
	 *            连接池名
	 * @return 字符说明
	 */
	private String stackToString(StackTraceElement[] stackTraceElements, String poolName) {
		StringBuilder bufAll = new StringBuilder(500);
		bufAll.append("pool name:").append(poolName).append("\r\n");
		StringBuilder bufOne = new StringBuilder(500);
		int i = 0;
		for (StackTraceElement element : stackTraceElements) {
			i++;
			if (i < 4) {
				continue;
			}
			bufOne.setLength(0);
			bufOne.append("\t");
			bufOne.append(element.getClassName()).append(".");
			bufOne.append(element.getMethodName()).append("  (");
			bufOne.append(element.getFileName()).append(":");
			bufOne.append(element.getLineNumber()).append(")\r\n");
			bufAll.append(bufOne);
		}
		return bufAll.toString();
	}
}
