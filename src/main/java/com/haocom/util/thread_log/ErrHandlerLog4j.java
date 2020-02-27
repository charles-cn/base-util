package com.haocom.util.thread_log;

import java.lang.Thread.UncaughtExceptionHandler;

import org.apache.log4j.Logger;

/**
 * 未捕获异常处理类. <br>
 * 捕获线程发生的未捕获的异常，写入info和error日志
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

	/** 日志记录器 */
	Logger logger = Logger.getLogger("UncaughtExceptionHandler");

	/** 自定义的未捕获异常处理类 */
	private UncaughtExceptionHandler preHandler;

	/**
	 * 构造方法
	 * 
	 * @param preHandler
	 *            自定义的未捕获异常处理类
	 */
	public ErrHandlerLog4j(UncaughtExceptionHandler preHandler) {
		this.preHandler = preHandler;
	}

	public synchronized void uncaughtException(Thread thread, Throwable exception) {
		// 将异常记录日志
		logger.error("线程" + thread + "已终止！", exception);

		// 调用自定义的处理类
		if (preHandler != null) {
			preHandler.uncaughtException(thread, exception);
		}
	}
}