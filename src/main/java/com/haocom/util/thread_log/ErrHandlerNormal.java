package com.haocom.util.thread_log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.lang.Thread.UncaughtExceptionHandler;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * δ�����쳣������. <br>
 * �����̷߳�����δ������쳣��д�����ǰĿ¼�µ�log/thread_interrupt_log.yyyy-MM-dd�ļ���
 * <p>
 * Copyright: Copyright (c) 2009-9-16
 * <p>
 * Company: 
 * <p>
 * Author: ZhouYan
 * <p>
 * Version: 1.0
 */
class ErrHandlerNormal implements UncaughtExceptionHandler {

	/** ʱ���ʽ���� */
	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	/** �Զ����δ�����쳣������ */
	private UncaughtExceptionHandler preHandler;

	/**
	 * ���췽��
	 * 
	 * @param preHandler
	 *            �Զ����δ�����쳣������
	 */
	public ErrHandlerNormal(UncaughtExceptionHandler preHandler) {
		this.preHandler = preHandler;
	}

	public synchronized void uncaughtException(Thread thread, Throwable exception) {
		try {
			String time = dateFormat.format(new Date());
			String message = time + ", �߳�" + thread + "����ֹ������" + exception;
			String fileName = "./log/thread_interrupt_log." + time.substring(0, 10);
			// �����ļ�Ŀ¼
			File file = new File("./log");
			if (!file.exists()) {
				file.mkdirs();
			}
			// ���쳣д���ļ�
			FileWriter fw = new FileWriter(fileName, true);
			try {
				BufferedWriter bw = new BufferedWriter(fw, 1024 * 64);
				try {
					bw.write(message);
					bw.append("\r\n");
					StackTraceElement[] elements = exception.getStackTrace();
					for (int i = 0; i < elements.length; i++) {
						bw.append(String.valueOf(elements[i]));
						bw.append("\r\n");
					}
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
		catch (Exception ex) {
		}

		// �����Զ���Ĵ�����
		if (preHandler != null) {
			preHandler.uncaughtException(thread, exception);
		}
	}
}