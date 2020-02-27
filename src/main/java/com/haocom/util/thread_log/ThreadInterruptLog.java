package com.haocom.util.thread_log;

import java.lang.Thread.UncaughtExceptionHandler;

/**
 * ����δ�����쳣����. <br>
 * �����ڲ����߳���δ�ܲ��񵽵��쳣����������ڼ���̶߳�ʧ��ԭ��.
 * <p>
 * Copyright: Copyright (c) 2009-9-30
 * <p>
 * Company: 
 * <p>
 * Author: ZhouYan
 * <p>
 * Version: 1.0
 * <p>
 * <ul>
 * <li>һ��ʹ��Log4j��¼��־ʱ�������ʹ��˵��</li>
 * <p>
 * ʹ��ʱ��Ҫ�������������߳�ǰ��ʵ�������������ã�<br>
 * 1����ʼ��log4j��DOMConfigurator.configureAndWatch("config/log4j.xml", 10 * 1000);<br>
 * 2���ڳ�ʼ��log4j���ʼ�������������ʵ��δ�����쳣������ƣ�ThreadInterruptLog.init();<br>
 * <p>
 * �쳣����Ч����
 * 
 * <pre>
 * ��������δ�ܲ��񵽵��쳣ʱ���ͻᱻ����е�Ĭ�ϴ�����ƽ��в����¼������error��־�д�ӡ���������쳣��Ϣ�����磺
 * UncaughtExceptionHandler 2009-09-30 15:04:27,343 -- ERROR -- �߳�Thread[DBAccess.Monitor,5,main]����ֹ��
 * java.lang.OutOfMemoryError: Java heap space
 * 	at java.io.BufferedWriter.&lt;init&gt;(BufferedWriter.java:87)
 * 	at com.cplatform.util.DBAccessMonitor.saveStackRecord(DBAccessMonitor.java:250)
 * 	at com.cplatform.util.DBAccessMonitor.run(DBAccessMonitor.java:227)
 * </pre>
 * 
 * <li>������ʹ��Log4j��¼��־ʱ�������ʹ��˵��</li>
 * <p>
 * ��ʹ��log4j�������������������3�㣺<br>
 * 1�������в�ʹ��log4j<br>
 * 2��������������jar�����ܺ���log4j<br>
 * 3����������ʹ�õ�jre�а����Ĺ���jar�в��ܺ���log4j<br>
 * <p>
 * �쳣����Ч����
 * 
 * <pre>
 * ��������δ�ܲ��񵽵��쳣ʱ���ͻᱻ����е�Ĭ�ϴ�����ƽ��в����¼�������ļ�./log/thread_interrupt_log.yyyy-MM-dd�����磺
 * 2009-09-30 15:20:20, �߳�Thread[Thread-10,5,main]����ֹ������/ by zero
 * com.cplatform.cmpp.jms.ToMtLogQueueThread.run(ToMtLogQueueThread.java:63)
 * </pre>
 * 
 * <li>ע�⣺ʹ�ñ�������������ѡ��ʹ��Log4j��¼��־��</li>
 * <p>
 * 1������ʹ�ó�����������δʹ��log4j���������������jre�к���log4j�����ʱ
 * ����ô�Ͳ�����ڶ���ʹ�÷�ʽ�������ˣ��ʲ�������thread_interrupt_log
 * .yyyy-MM-dd��¼���󡣲��Ҵ�ʱ���ڳ���δʹ��log4j������Ҳ������log4j��error��־����¼������������������£�
 * ����쳣�������κεط�������м�¼������ע����һ�㣡<br>
 * 2�����⣬��������ڲ��񵽵��쳣��������һ����¼�Ĺ��ܣ���δ���κβ��ȴ�ʩ��<br>
 * 3�������֧�ֳ������Զ���Ķ���δ�����쳣�Ĵ�����ơ������������Զ����δ�����쳣�����࣬����������ɺ��ٽ��ж����init�����ĵ��á�<br>
 * ���磺�������Զ���Ĵ�����MYErrHandler()��ʹ��ʱ��<br>
 * 
 * <pre>
 * ������ȷ�ģ�
 * 		Thread.setDefaultUncaughtExceptionHandler(new MYErrHandler());
 * 		ThreadInterruptLog.init();
 * </pre>
 * 
 * <pre>
 * ���Ǵ���ģ�
 * 		ThreadInterruptLog.init();
 * 		Thread.setDefaultUncaughtExceptionHandler(new MYErrHandler());
 * </pre>
 * 
 * </ul>
 */
public class ThreadInterruptLog {

	/**
	 * ��ʼ�����ã�����Ĭ�ϵ�δ�����쳣������.<br>
	 * Ĭ�ϴ�����ƣ�1��������log4j�����¼��log4j��error��־�У���������log4j�����¼���ļ�log/
	 * thread_interrupt_log.yyyy-MM-dd��.
	 */
	public static void init() {
		UncaughtExceptionHandler handler = null;
		try {
			Class.forName("org.apache.log4j.Logger");
			handler = new ErrHandlerLog4j(Thread.getDefaultUncaughtExceptionHandler());
		}
		catch (Exception ex) {
			handler = new ErrHandlerNormal(Thread.getDefaultUncaughtExceptionHandler());
		}
		Thread.setDefaultUncaughtExceptionHandler(handler);
	}
}
