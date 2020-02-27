package com.haocom.util.thread_log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.lang.Thread.UncaughtExceptionHandler;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 未捕获异常处理类. <br>
 * 捕获线程发生的未捕获的异常，写入程序当前目录下的log/thread_interrupt_log.yyyy-MM-dd文件中
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

	/** 时间格式定义 */
	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	/** 自定义的未捕获异常处理类 */
	private UncaughtExceptionHandler preHandler;

	/**
	 * 构造方法
	 * 
	 * @param preHandler
	 *            自定义的未捕获异常处理类
	 */
	public ErrHandlerNormal(UncaughtExceptionHandler preHandler) {
		this.preHandler = preHandler;
	}

	public synchronized void uncaughtException(Thread thread, Throwable exception) {
		try {
			String time = dateFormat.format(new Date());
			String message = time + ", 线程" + thread + "已终止！报错：" + exception;
			String fileName = "./log/thread_interrupt_log." + time.substring(0, 10);
			// 创建文件目录
			File file = new File("./log");
			if (!file.exists()) {
				file.mkdirs();
			}
			// 将异常写入文件
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

		// 调用自定义的处理类
		if (preHandler != null) {
			preHandler.uncaughtException(thread, exception);
		}
	}
}