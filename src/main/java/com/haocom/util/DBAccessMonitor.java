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
 * ���ݿ�����. <br>
 * ���ݿ�������������ݿ����ӳ�ʹ�����.
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
	 * �����˶�ջ�ĸ�����Ϣ��info,poolName,threadId��
	 */
	class StackInfo {

		/** ��ջ��Ϣ */
		String info;

		/** ���ӳ����� */
		String poolName;

		/** �߳�Id */
		long threadId;
	}

	/** ����ʵ�� */
	private static DBAccessMonitor instance;

	public static boolean monitor = false;

	/**
	 * ��ȡ����ʵ��
	 * 
	 * @return DBAccessMonitor ����ʵ��
	 */
	public static DBAccessMonitor getInstance() {
		return instance;
	}

	/**
	 * ��ȡ���ݿ�����ʵ����Ϊ����
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

	/** �����Ҫ�ͷŵ����ӳ� */
	private HashMap<String, Object> callStackRecord_Free = new HashMap<String, Object>();

	/** �����Ҫ��ȡ���ӵ����ӳ� */
	private HashMap<String, Object> callStackRecord_Get = new HashMap<String, Object>();

	/** ����������е����ӳ� */
	private HashMap<Long, Stack<StackInfo>> callStackRecord_Thread = new HashMap<Long, Stack<StackInfo>>();

	/** ���ʹ�÷�����������ӳ� */
	private HashMap<String, Object> callStackRecord_Warning = new HashMap<String, Object>();

	/** ��־�ļ��� */
	private String logFilename;

	/**
	 * ���󴴽�
	 */
	DBAccessMonitor() {
		setName("DBAccess.Monitor");
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd.HHmmss");
		logFilename = "./log/dba/" + format.format(new Date()) + ".stack";
		this.setDaemon(true);
	}

	/**
	 * ��¼FreeConnection��ִ�ж�ջ
	 * 
	 * @param poolName
	 *            ���ӳ���
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
	 * ��¼GetConnection��ִ�ж�ջ
	 * 
	 * @param poolName
	 *            ���ӳ���
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
	 * ��¼���ݿ����ӳس�ջʹ�����
	 * 
	 * @param threadId
	 *            �������е����ӳص��߳�Id
	 * @param poolName
	 *            ���ӳ���
	 * @param info
	 *            ���ӳ�ʹ����Ϣ
	 */
	private void recordStackPop(long threadId, String poolName, String info) {
		Stack<StackInfo> stack = callStackRecord_Thread.get(threadId);
		if (stack == null || stack.peek() == null) {
			// δ��ȡ���ݿ����ӣ���ֱ���ͷ�����
			StringBuilder buf = new StringBuilder(500);
			buf.append("δ��ȡ���ݿ����ӣ���ֱ���ͷ�����:").append(poolName).append("\r\n");
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
		// �ж��Ƿ񽻲��ͷ�
		if (!last.poolName.equals(poolName)) {
			StringBuilder buf = new StringBuilder(500);
			buf.append("pool name:").append(poolName).append(":").append("�����ͷ�").append(":").append(last.poolName);
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
	 * ��¼���ݿ����ӳ���ջʹ�����
	 * 
	 * @param threadId
	 *            �������е����ӳص��߳�Id
	 * @param poolName
	 *            ���ӳ���
	 * @param info
	 *            ���ӳ�ʹ����Ϣ
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
		// �ж��Ƿ�Ƕ��
		if (last.poolName.equals(now.poolName)) {
			StringBuilder buf = new StringBuilder(500);
			buf.append("pool name:").append(poolName).append(":").append("Ƕ�׵���\r\n");
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
	 * �����е�ִ�ж�ջ��¼�����ļ�
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private void saveStackRecord() throws Exception {
		// д�ļ�
		File file = new File(logFilename);
		File dir = file.getParentFile();
		if (dir != null && !dir.exists()) {
			dir.mkdirs();
		}
		FileWriter fw = new FileWriter(logFilename, false);
		try {
			BufferedWriter bw = new BufferedWriter(fw, 1024 * 64);
			try {
				// ���û�ȡ���ݿ����ӵĴ����ջ
				{
					HashMap<String, Object> map = (HashMap<String, Object>) this.callStackRecord_Get.clone();
					bw.write("\r\n\r\n");
					bw.write("���û�ȡ���ݿ����ӵĴ����ջ��");
					bw.write(Integer.toString(map.size()));
					bw.write("\r\n");
					for (String info : map.keySet()) {
						bw.write(info);
					}
				}
				// �����ͷ����ݿ����ӵĴ����ջ
				{
					HashMap<String, Object> map = (HashMap<String, Object>) this.callStackRecord_Free.clone();
					bw.write("\r\n\r\n");
					bw.write("�����ͷ����ݿ����ӵĴ����ջ��");
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
					bw.write("���ݿ���þ��棺");
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
	 * ����ִ�ж�ջ�����ַ�˵��
	 * 
	 * @param stackTraceElements
	 *            ִ�ж�ջ
	 * @param poolName
	 *            ���ӳ���
	 * @return �ַ�˵��
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
