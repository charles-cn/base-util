package com.haocom.util.debug;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * ������. <br>
 * ����������,�����������ڲ�������ʱͳ�Ƴ���ÿ���ӵĴ�������.<br>
 * ����ڿ���̨��������ݣ�������ʾ�ľ��Ƕ�Ӧ�ĳ���ÿ���ӵĴ�������.
 * <p>
 * �޸���ʷ: 2009-6-19 ����06:53:48 ChengFan <br>
 * <p>
 * �޸���ʷ: 2009-6-19 ����06:54:06 ChengFan <br>
 * <p>
 * Copyright: Copyright (c) 2009-6-19 ����06:53:22
 * <p>
 * Company: 
 * <p>
 * Author: ChengFan
 * <p>
 * Version: 1.0
 * <p>
 * <li>ʹ��˵��</li>
 * <p>
 * 1������һ������SpeedCounter counter = new SpeedCounter(String counterName);<br>
 * 2�����߳��е��÷���inc�������ɡ�eg��counter.inc(int idx, int count);��counter.inc(int idx,
 * int count, String name);
 * <p>
 * <li>���巶��</li>
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
 * 				// ִ�в���.
 * 				Thread.sleep(10);
 * 				// ���ÿ�����ܹ�ִ�ж��ٴ�sleep(10)�Ĳ���.
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
 * <li>��������ӡ����˵��</li>
 * <p>
 * ��Main���������Ǵ�����һ���������� static SpeedCounter speedCounter = new
 * SpeedCounter(&quot;Counter&quot;);<br>
 * Ȼ�����߳��е���������������м�������������ΪsleepTimes��<br>
 * ������Ĵ��������ǿ��Կ������˼����������ÿ�����߳�ִ��sleep(10)��������Ĵ���<br>
 * �����������������ʾ��
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

	/** ���������� */
	protected static final int MAX_COUNT_COUNT = 50;

	/** ������Timer */
	protected static Timer timer = new Timer("SpeedCounterTimer", true);

	/** ���������� */
	protected String counterName;

	/** ������ */
	protected AtomicInteger[] counts = new AtomicInteger[MAX_COUNT_COUNT];

	/** ���������� */
	protected String[] names = new String[MAX_COUNT_COUNT];

	/** �ַ����� */
	protected StringBuilder strBuf = new StringBuilder(500);

	/**
	 * @param counterName
	 *            ����������
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
	 * �������������ֵ
	 */
	public void clear() {
		for (int i = 0; i < MAX_COUNT_COUNT; i++) {
			if (names[i] != null) {
				counts[i].set(0);
			}
		}
	}

	/**
	 * ���Ӽ�������ֵ
	 * 
	 * @param idx
	 *            ���������
	 * @param count
	 *            ��������
	 */
	public void inc(int idx, int count) {
		counts[idx].addAndGet(count);
	}

	/**
	 * ���Ӽ�������ֵ
	 * 
	 * @param idx
	 *            ���������
	 * @param count
	 *            ��������
	 * @param name
	 *            ����������
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
	 * ���ü���������
	 * 
	 * @param idx
	 *            ���������
	 * @param name
	 *            ����������
	 */
	public void setName(int idx, String name) {
		names[idx] = name;
	}

	/**
	 * ���ͳ�ƽ��
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
