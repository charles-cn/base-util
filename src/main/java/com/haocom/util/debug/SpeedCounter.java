package com.haocom.util.debug;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 计数器. <br>
 * 计数器工具,作用是用于在测试性能时统计程序每秒钟的处理能力.<br>
 * 组件在控制台上输出数据，数据显示的就是对应的程序每秒钟的处理能力.
 * <p>
 * 修改历史: 2009-6-19 下午06:53:48 ChengFan <br>
 * <p>
 * 修改历史: 2009-6-19 下午06:54:06 ChengFan <br>
 * <p>
 * Copyright: Copyright (c) 2009-6-19 下午06:53:22
 * <p>
 * Company: 
 * <p>
 * Author: ChengFan
 * <p>
 * Version: 1.0
 * <p>
 * <li>使用说明</li>
 * <p>
 * 1、创建一个对象：SpeedCounter counter = new SpeedCounter(String counterName);<br>
 * 2、在线程中调用方法inc方法即可。eg：counter.inc(int idx, int count);或counter.inc(int idx,
 * int count, String name);
 * <p>
 * <li>具体范例</li>
 * 
 * <pre>
 * 
 * public class Demo extends Thread {
 * 
 * 	static SpeedCounter speedCounter;
 * 
 * 	public static void main(String[] args) {
 * 		speedCounter = new SpeedCounter(&quot;Counter&quot;);
 * 		new Demo().start();
 * 	}
 * 
 * 	&#064;Override
 * 	public void run() {
 * 
 * 		while (true) {
 * 			try {
 * 				// 执行操作.
 * 				Thread.sleep(10);
 * 				// 输出每秒钟能够执行多少次sleep(10)的操作.
 * 				Demo.speedCounter.inc(1, 1, &quot;sleepTimes&quot;);
 * 			}
 * 			catch (Exception e) {
 * 				e.printStackTrace();
 * 			}
 * 		}
 * 	}
 * }
 * </pre>
 * 
 * <li>计数器打印数据说明</li>
 * <p>
 * 在Main方法中我们创建了一个计数器： static SpeedCounter speedCounter = new
 * SpeedCounter(&quot;Counter&quot;);<br>
 * 然后在线程中调用这个计数器进行计数，设置名称为sleepTimes。<br>
 * 从上面的代码中我们可以看到，此计数器会输出每秒钟线程执行sleep(10)这个操作的次数<br>
 * 整个输出过程如下所示：
 * 
 * <pre>
 * 2009-7-10 16:51:35: Counter:&gt;&gt; 
 * 2009-7-10 16:51:36: Counter:&gt;&gt; sleepTimes=81	
 * 2009-7-10 16:51:37: Counter:&gt;&gt; sleepTimes=93	
 * 2009-7-10 16:51:38: Counter:&gt;&gt; sleepTimes=92	
 * 2009-7-10 16:51:39: Counter:&gt;&gt; sleepTimes=94	
 * 2009-7-10 16:51:40: Counter:&gt;&gt; sleepTimes=92	
 * 2009-7-10 16:51:41: Counter:&gt;&gt; sleepTimes=93	
 * 2009-7-10 16:51:42: Counter:&gt;&gt; sleepTimes=93	
 * 2009-7-10 16:51:43: Counter:&gt;&gt; sleepTimes=92	
 * 2009-7-10 16:51:44: Counter:&gt;&gt; sleepTimes=93
 * ......
 * </pre>
 * 
 * </ul>
 */
public class SpeedCounter extends TimerTask {

	/** 计数器数量 */
	protected static final int MAX_COUNT_COUNT = 50;

	/** 计数器Timer */
	protected static Timer timer = new Timer("SpeedCounterTimer", true);

	/** 计数器名称 */
	protected String counterName;

	/** 计数器 */
	protected AtomicInteger[] counts = new AtomicInteger[MAX_COUNT_COUNT];

	/** 计数器名称 */
	protected String[] names = new String[MAX_COUNT_COUNT];

	/** 字符缓存 */
	protected StringBuilder strBuf = new StringBuilder(500);

	/**
	 * @param counterName
	 *            计数器名称
	 */
	public SpeedCounter(String counterName) {
		timer.schedule(this, 0, 1000);
		this.counterName = counterName;
		counts = new AtomicInteger[MAX_COUNT_COUNT];
		for (int i = 0; i < counts.length; i++) {
			counts[i] = new AtomicInteger(0);
		}
	}

	/**
	 * 清除计数器中数值
	 */
	public void clear() {
		for (int i = 0; i < MAX_COUNT_COUNT; i++) {
			if (names[i] != null) {
				counts[i].set(0);
			}
		}
	}

	/**
	 * 增加计数器数值
	 * 
	 * @param idx
	 *            计数器序号
	 * @param count
	 *            增加数量
	 */
	public void inc(int idx, int count) {
		counts[idx].addAndGet(count);
	}

	/**
	 * 增加计数器数值
	 * 
	 * @param idx
	 *            计数器序号
	 * @param count
	 *            增加数量
	 * @param name
	 *            计数器名称
	 */
	public void inc(int idx, int count, String name) {
		if (names[idx] == null) {
			names[idx] = name;
		}
		counts[idx].addAndGet(count);
	}

	@Override
	public void run() {
		showResult();
		clear();
	}

	/**
	 * 设置计数器名称
	 * 
	 * @param idx
	 *            计数器序号
	 * @param name
	 *            计数器名称
	 */
	public void setName(int idx, String name) {
		names[idx] = name;
	}

	/**
	 * 输出统计结果
	 */
	@SuppressWarnings("deprecation")
	public void showResult() {
		strBuf.setLength(0);
		strBuf.append(new Date().toLocaleString()).append(": ").append(counterName).append(":>> ");
		for (int i = 0; i < MAX_COUNT_COUNT; i++) {
			if (names[i] != null) {
				strBuf.append(names[i]).append("=").append(counts[i].get()).append("\t");
			}
		}
		System.out.println(strBuf);
	}
}
