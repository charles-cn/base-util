package com.haocom.util.opools;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * �����. <br>
 * ����ع���,�������ڴ��������,�Լ����������еĶ���.
 * <p>
 * Copyright: Copyright (c) 2009-6-24 ����10:02:39
 * <p>
 * Company: 
 * <p>
 * Author: ChengFan
 * <p>
 * Version: 1.0
 * <p>
 * ʹ�÷�������:
 * <p>
 * <ul>
 * <li>���˵��</li>
 * <p>
 * ʹ�ñ������������Ҫ����һ������أ������������������������ÿ����Ҫʹ�ö����ʱ��Ͳ���Ҫ��new�����ˣ�ֱ��ȥ������ȡ���ɡ�
 * �������Ҫ������黹������ء�<br>
 * ����������ǵ��������ȥȡ�����ʱ������������û�ж�����ô����ػ�newһ����������ǣ���֮����ֱ�ӴӶ������ȡһ�����еĶ��󷵻ء�
 * ���ַ�ʽ����������ÿ�β��ϵ�new ����Ĳ�������ʡ���ڴ���Դ������˳��������ٶȡ�
 * <p>
 * <li>���ʹ�ó���</li>
 * <p>
 * 1������һ�����󣬵�������Ҫ��������ʹ�ô˶����ʱ�򣬽���ʹ�ö����������������ԺܺõĹ�����󣬴����ٴ����¶���Ĵ����������ĳ��������<br>
 * 2������һ�������������ֻ��ż����Ҫ����ʹ�ã�����ʹ��Ƶ�ʲ��ߣ���ô��ʱ���Ƽ�ʹ�ñ������<br>
 * 3�����ڱ������ʹ�ã����Ҹ��ݳ����ʵ�����������ѡ��ʹ��ԭ����������������ܺ��ȶ���Ϊ��׼��
 * <p>
 * <li>����һ�������</li>
 * 
 * <pre>
 * 
 * public class XXXXPool extends ObjectPool&lt;XXXX&gt; {
 * 
 * 	// poolSize:����ش�Ŷ����������.
 * 	public XXXXPool(int poolSize) {
 * 		super(poolSize);
 * 	}
 * 
 * 	&#064;Override
 * 	protected XXXX newObject() {
 * 		// newһ������XXXX����.
 * 		return new XXXX();
 * 	}
 * 
 * 	&#064;Override
 * 	protected void resetObject(XXXX e) {
 * 		// ���ö���XXX�Ĳ������磺e=new XXXX();�����統����ΪStringBuilerʱҲ��e.setLength(0);�ȵ�.
 * 	}
 * }
 * </pre>
 * 
 * <li>���ô����Ķ����</li>
 * 
 * <pre>
 * // �½�����أ������ô�С.
 * XXXXPool pool = new XXXXPool(100);
 * // �Ӷ�����л�ȡ����.
 * XXXX buf = pool.borrowObject();
 * try {
 * 	// ����ɽ��жԴ˶���XXXX�Ĳ���.
 * }
 * finally {
 * 	// ������黹�������.
 * 	pool.returnObject(buf);
 * }
 * </pre>
 * 
 * <li>ʵ�ʷ���</li>
 * 
 * <pre>
 * 
 * public class StringBuilderPool extends ObjectPool&lt;StringBuilder&gt; {
 * 
 * 	public static void main(String[] args) {
 * 		// �½�����أ������ô�С.
 * 		StringBuilderPool sbp = new StringBuilderPool(100, 2048);
 * 		// ��ȡ.
 * 		StringBuilder buf = sbp.borrowObject();
 * 		try {
 * 			// ����.
 * 			buf.append(&quot;abc&quot;);
 * 		}
 * 		finally {
 * 			// �黹.
 * 
 * 			sbp.returnObject(buf);
 * 		}
 * 	}
 * 
 * 	int maxCapacity = 2000;
 * 
 * 	public StringBuilderPool(int poolSize, int maxStringBuilderCapacity) {
 * 		super(poolSize);
 * 		this.maxCapacity = maxStringBuilderCapacity;
 * 	}
 * 
 * 	&#064;Override
 * 	protected StringBuilder newObject() {
 * 		return new StringBuilder(500);
 * 	}
 * 
 * 	&#064;Override
 * 	protected void resetObject(StringBuilder e) {
 * 		if (e.capacity() &gt; maxCapacity) {
 * 			e.setLength(maxCapacity);
 * 			e.trimToSize();
 * 		}
 * 		e.setLength(0);
 * 	}
 * }
 * </pre>
 * 
 * </ul>
 */
public abstract class ObjectPool<E> {

	private int maxSize;

	private Queue<E> queue;

	/**
	 * ���캯��
	 */
	public ObjectPool() {
		this(1000);
	}

	/**
	 * ���캯��
	 * 
	 * @param maxSize
	 *            ��󻺴���������
	 */
	public ObjectPool(int maxSize) {
		this.maxSize = maxSize;
		queue = new LinkedBlockingQueue<E>(maxSize);
	}

	/**
	 * �ӳ��л�ȡһ������û���ֳɶ������½�һ��
	 * 
	 * @return ����
	 */
	public E borrowObject() {
		E e = queue.poll();
		if (e == null) {
			e = newObject();
		}
		return e;
	}

	/**
	 * �����µĶ���ʵ��
	 * 
	 * @return ����
	 */
	protected abstract E newObject();

	/**
	 * ���ö�������
	 * 
	 * @param e
	 *            ����
	 */
	protected abstract void resetObject(E e);

	/**
	 * �黹����
	 * 
	 * @param e
	 *            ����
	 */
	public void returnObject(E e) {
		if (queue.size() < maxSize) {
			resetObject(e);
			queue.add(e);
		}
	}
}
