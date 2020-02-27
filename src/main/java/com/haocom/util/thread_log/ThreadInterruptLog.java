package com.haocom.util.thread_log;

import java.lang.Thread.UncaughtExceptionHandler;

/**
 * 设置未捕获异常处理. <br>
 * 可用于捕获线程中未能捕获到的异常，例如可用于监控线程丢失的原因.
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
 * <li>一：使用Log4j记录日志时本组件的使用说明</li>
 * <p>
 * 使用时需要在主程序启动线程前，实现以下两项设置：<br>
 * 1、初始化log4j：DOMConfigurator.configureAndWatch("config/log4j.xml", 10 * 1000);<br>
 * 2、在初始化log4j后初始化本组件，用于实现未捕获异常处理机制：ThreadInterruptLog.init();<br>
 * <p>
 * 异常捕获效果：
 * 
 * <pre>
 * 当程序发生未能捕获到的异常时，就会被组件中的默认处理机制进行捕获记录，会在error日志中打印出发生的异常信息。例如：
 * UncaughtExceptionHandler 2009-09-30 15:04:27,343 -- ERROR -- 线程Thread[DBAccess.Monitor,5,main]已终止！
 * java.lang.OutOfMemoryError: Java heap space
 * 	at java.io.BufferedWriter.&lt;init&gt;(BufferedWriter.java:87)
 * 	at com.cplatform.util.DBAccessMonitor.saveStackRecord(DBAccessMonitor.java:250)
 * 	at com.cplatform.util.DBAccessMonitor.run(DBAccessMonitor.java:227)
 * </pre>
 * 
 * <li>二、不使用Log4j记录日志时本组件的使用说明</li>
 * <p>
 * 不使用log4j的情况，必须满足下列3点：<br>
 * 1、程序中不使用log4j<br>
 * 2、程序自身打入的jar包不能含有log4j<br>
 * 3、启动程序使用的jre中包含的公共jar中不能含有log4j<br>
 * <p>
 * 异常捕获效果：
 * 
 * <pre>
 * 当程序发生未能捕获到的异常时，就会被组件中的默认处理机制进行捕获记录，生成文件./log/thread_interrupt_log.yyyy-MM-dd。例如：
 * 2009-09-30 15:20:20, 线程Thread[Thread-10,5,main]已终止！报错：/ by zero
 * com.cplatform.cmpp.jms.ToMtLogQueueThread.run(ToMtLogQueueThread.java:63)
 * </pre>
 * 
 * <li>注意：使用本组件，请大家务必选择使用Log4j记录日志！</li>
 * <p>
 * 1、错误使用场景：当程序并未使用log4j，但是启动程序的jre中含有log4j的组件时
 * ，那么就不满足第二种使用方式的限制了，故不会生成thread_interrupt_log
 * .yyyy-MM-dd记录错误。并且此时由于程序并未使用log4j，所以也不会有log4j的error日志来记录错误。所以在这种情况下，
 * 这个异常不会有任何地方对其进行记录。请大家注意这一点！<br>
 * 2、另外，本组件对于捕获到的异常仅是做了一个记录的功能，并未做任何补救措施。<br>
 * 3、本组件支持程序中自定义的对于未捕获异常的处理机制。若程序中有自定义的未捕获异常处理类，请在设置完成后再进行对组件init方法的调用。<br>
 * 例如：程序有自定义的处理类MYErrHandler()，使用时：<br>
 * 
 * <pre>
 * 这是正确的：
 * 		Thread.setDefaultUncaughtExceptionHandler(new MYErrHandler());
 * 		ThreadInterruptLog.init();
 * </pre>
 * 
 * <pre>
 * 这是错误的：
 * 		ThreadInterruptLog.init();
 * 		Thread.setDefaultUncaughtExceptionHandler(new MYErrHandler());
 * </pre>
 * 
 * </ul>
 */
public class ThreadInterruptLog {

	/**
	 * 初始化设置，设置默认的未捕获异常处理类.<br>
	 * 默认处理机制：1，若可用log4j，则记录到log4j的error日志中；若不可用log4j，则记录到文件log/
	 * thread_interrupt_log.yyyy-MM-dd中.
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
