package com.haocom.util.thread_log;

import java.lang.Thread.UncaughtExceptionHandler;

import org.apache.log4j.Logger;

/**
 * δ�����쳣������. <br>
 * �����̷߳�����δ������쳣��д��info��error��־
 * <p>
 * Copyright: Copyright (c) 2009-9-16
 * <p>
 * Company: 
 * <p>
 * Author: ZhouYan
 * <p>
 * Version: 1.0
 */
class ErrHandlerLog4j implements UncaughtExceptionHandler {

	/** ��־��¼�� */
	Logger logger = Logger.getLogger("UncaughtExceptionHandler");

	/** �Զ����δ�����쳣������ */
	private UncaughtExceptionHandler preHandler;

	/**
	 * ���췽��
	 * 
	 * @param preHandler
	 *            �Զ����δ�����쳣������
	 */
	public ErrHandlerLog4j(UncaughtExceptionHandler preHandler) {
		this.preHandler = preHandler;
	}

	public synchronized void uncaughtException(Thread thread, Throwable exception) {
		// ���쳣��¼��־
		logger.error("�߳�" + thread + "����ֹ��", exception);

		// �����Զ���Ĵ�����
		if (preHandler != null) {
			preHandler.uncaughtException(thread, exception);
		}
	}
}