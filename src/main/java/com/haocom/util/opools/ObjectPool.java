package com.haocom.util.opools;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 对象池. <br>
 * 对象池工具,可以用于创建对象池,以及管理对象池中的对象.
 * <p>
 * Copyright: Copyright (c) 2009-6-24 上午10:02:39
 * <p>
 * Company: 
 * <p>
 * Author: ChengFan
 * <p>
 * Version: 1.0
 * <p>
 * 使用范例如下:
 * <p>
 * <ul>
 * <li>组件说明</li>
 * <p>
 * 使用本组件，首先需要创建一个对象池，由这个对象池来管理对象。我们每次需要使用对象的时候就不需要再new对象了，直接去池里面取即可。
 * 用完后需要将对象归还到对象池。<br>
 * 在这里，当我们到对象池中去取对象的时候，如果对象池中没有对象，那么对象池会new一个对象给我们；反之，则直接从对象池中取一个现有的对象返回。
 * 这种方式避免了我们每次不断的new 对象的操作，节省了内存资源，提高了程序运行速度。
 * <p>
 * <li>组件使用场景</li>
 * <p>
 * 1、对于一个对象，当程序需要经常创建使用此对象的时候，建议使用对象池组件。这样可以很好的管理对象，大大减少创建新对象的次数，提升的程序的性能<br>
 * 2、对于一个对象，如果程序只是偶尔需要创建使用，或者使用频率不高，那么此时不推荐使用本组件。<br>
 * 3、对于本组件的使用，请大家根据程序的实际情况来进行选择。使用原则：以提升程序的性能和稳定性为基准。
 * <p>
 * <li>创建一个对象池</li>
 * 
 * <pre>
 * 
 * public class XXXXPool extends ObjectPool&lt;XXXX&gt; {
 * 
 * 	// poolSize:对象池存放对象的最大个数.
 * 	public XXXXPool(int poolSize) {
 * 		super(poolSize);
 * 	}
 * 
 * 	&#064;Override
 * 	protected XXXX newObject() {
 * 		// new一个对象XXXX，如.
 * 		return new XXXX();
 * 	}
 * 
 * 	&#064;Override
 * 	protected void resetObject(XXXX e) {
 * 		// 重置对象XXX的操作，如：e=new XXXX();又例如当对象为StringBuiler时也可e.setLength(0);等等.
 * 	}
 * }
 * </pre>
 * 
 * <li>调用创建的对象池</li>
 * 
 * <pre>
 * // 新建对象池，并设置大小.
 * XXXXPool pool = new XXXXPool(100);
 * // 从对象池中获取对象.
 * XXXX buf = pool.borrowObject();
 * try {
 * 	// 这里可进行对此对象XXXX的操作.
 * }
 * finally {
 * 	// 将对象归还到对象池.
 * 	pool.returnObject(buf);
 * }
 * </pre>
 * 
 * <li>实际范例</li>
 * 
 * <pre>
 * 
 * public class StringBuilderPool extends ObjectPool&lt;StringBuilder&gt; {
 * 
 * 	public static void main(String[] args) {
 * 		// 新建对象池，并设置大小.
 * 		StringBuilderPool sbp = new StringBuilderPool(100, 2048);
 * 		// 获取.
 * 		StringBuilder buf = sbp.borrowObject();
 * 		try {
 * 			// 操作.
 * 			buf.append(&quot;abc&quot;);
 * 		}
 * 		finally {
 * 			// 归还.
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
	 * 构造函数
	 */
	public ObjectPool() {
		this(1000);
	}

	/**
	 * 构造函数
	 * 
	 * @param maxSize
	 *            最大缓存数据数量
	 */
	public ObjectPool(int maxSize) {
		this.maxSize = maxSize;
		queue = new LinkedBlockingQueue<E>(maxSize);
	}

	/**
	 * 从池中获取一对象，如没有现成对象，则新建一个
	 * 
	 * @return 对象
	 */
	public E borrowObject() {
		E e = queue.poll();
		if (e == null) {
			e = newObject();
		}
		return e;
	}

	/**
	 * 创建新的对象实例
	 * 
	 * @return 对象
	 */
	protected abstract E newObject();

	/**
	 * 重置对象属性
	 * 
	 * @param e
	 *            对象
	 */
	protected abstract void resetObject(E e);

	/**
	 * 归还对象
	 * 
	 * @param e
	 *            对象
	 */
	public void returnObject(E e) {
		if (queue.size() < maxSize) {
			resetObject(e);
			queue.add(e);
		}
	}
}
