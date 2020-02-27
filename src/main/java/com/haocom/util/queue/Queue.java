package com.haocom.util.queue;

/**
 * 队列基本操作. <br>
 * 队列基本操作，包括添加，获取大小，等等.
 * <p>
 * Copyright: Copyright (c) Sep 16, 2008
 * <p>
 * Company: 
 * <p>
 * Author:
 * <p>
 * Version: 1.0
 */
public interface Queue<E> {

	/**
	 * 向缓冲队列中添加数据，并返回是否添加成功。<br>
	 * 如果队列已经满了则添加不成功。<br>
	 * 
	 * @param obj
	 *            缓冲对象
	 * @return 是否插入成功
	 */
	public boolean add(E obj);

	/**
	 * 向缓冲队列中强制添加数据，并返回是否添加成功。<br>
	 * 
	 * @param obj
	 *            缓冲对象
	 * @return 是否插入成功
	 */
	public boolean addForce(E obj);

	/**
	 * 清空队列
	 */
	public void clear();

	/**
	 * 获取缓冲最大队列
	 * 
	 * @return 队列大小上限
	 */
	public int getMaxQueueSize();

	/**
	 * 获取此队列名称
	 * 
	 * @return 队列名称
	 */
	public String getName();

	/**
	 * 获取队列大小
	 * 
	 * @return 队列大小
	 */
	public int getSize();

	/**
	 * 获取队列是否为空
	 * 
	 * @return 队列是否为空
	 */
	public boolean isEmpty();

	/**
	 * 获取队列是否已经满了
	 * 
	 * @return 队列是否已满
	 */
	public boolean isFull();

	/**
	 * 从缓冲队列中窥视一个数据
	 * 
	 * @return 队列中的一个数据
	 */
	public E peek();

	/**
	 * 从缓冲队列中获取一个数据
	 * 
	 * @return 队列中的一个数据
	 */
	public E remove();

	/**
	 * 将队列中的对象转化为数组的表现形式
	 * 
	 * @return 数组存放对象
	 */
	public Object[] toArray();

	/**
	 * 返回一个包含此 set 所有元素的数组；返回数组的运行时类型是指定数组的类型。如果指定的数组能容纳该 set，则将 set
	 * 返回此处。否则，将分配一个具有指定数组的运行时类型和此 set 大小的新数组。<BR>
	 * 如果指定的数组能容纳 set，并有剩余的空间（即数组的元素比 set 多），那么会将接 set 尾部的元素设置为 null。（仅 当调用者知道此
	 * set 不包含任何 null 元素时，才可使用此方法来确定此 set 的长度。）<BR>
	 * 如果此 set 对其迭代器返回的元素顺序做出了某些保证，那么此方法必须以相同的顺序返回这些元素。<BR>
	 * 像 toArray() 方法一样，此方法充当基于数组 的 API 与基于 collection 的 API
	 * 之间的桥梁。更进一步说，此方法允许对输出数组的运行时类型进行精确控制，在某些情况下，可以用来节省分配开销。<BR>
	 * 假定 x 是只包含字符串的一个已知 set。以下代码用来将该 set 转储到一个新分配的 String 数组：<BR>
	 * String[] y = x.toArray(new String[0]);<BR>
	 * 注意，toArray(new Object[0]) 和 toArray() 在功能上是相同的。
	 * 
	 * @param a
	 *            存储此 - set 元素的数组（如果该数组足够大）；否则为此分配一个具有相同运行时类型的新数组
	 * @return 包含此 set 所有元素的数组
	 */
	public E[] toArray(E[] a);
}
