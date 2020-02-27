package com.haocom.util.data_scanner;

import java.io.Closeable;
import java.util.Arrays;

/**
 * 多数据集检查和比较. <br>
 * 本类用于多数据集之间的检查和比较<br>
 * 子类必须实现{@link MutilDataScaner#compare(Object, Object)}方法，用于比较两个数据的大小；<br>
 * 子类必须实现{@link MutilDataScaner#dealCompareResult(boolean[], Object...)}
 * 方法，用于处理比较后的结果；<br>
 * 子类可重写{@link MutilDataScaner#doFinalize()}，用于处理完成后回收资源；<br>
 * 此方法中如果DataReader是java.io.Closeable的，则会自动关闭该DataReader.<br>
 * 子类可重写{@link MutilDataScaner#compareData()}，用于实现处理逻辑。<br>
 * <p>
 * 修改历史: 2009-4-23 上午11:00:02 gaowei <br>
 * 创建
 * <p>
 * 修改历史: 2009-6-14 下午05:15:51 chengfan <br>
 * 增加自动关闭DataReader的功能。如果DataReader是java.io.Closeable的，则会自动关闭该DataReader.<br>
 * <p>
 * Copyright: Copyright (c) 2009-4-23 上午11:00:02
 * <p>
 * Company: 
 * <p>
 * Author: gaowei
 * <p>
 * Version: 1.0
 * <p>
 */
public abstract class MutilDataScaner<E> {

	/** 比较结果. */
	protected Object[] compareResult;

	/** 数据集读入器. */
	protected DataReader<E>[] dataReaders;

	/** 待比较的数据. */
	protected Object[] datas;

	/** 数据集个数. */
	protected int length;

	/** 处理结果的真值表. */
	protected boolean[] result;

	/**
	 * 构造器
	 * 
	 * @param dataReaders
	 *            各数据集的读取器
	 */
	protected MutilDataScaner(DataReader<E>... dataReaders) {
		this.dataReaders = dataReaders;
		length = dataReaders.length;
		result = new boolean[length];

		datas = new Object[length];
		compareResult = new Object[length];

		// 第一次先处理化为true，用于获取全部数据集的第一条数据
		Arrays.fill(result, true);
	}

	/**
	 * 比较数据
	 * 
	 * @param e1
	 *            数据1
	 * @param e2
	 *            数据2
	 * @return 小于0表示e1小于e2;<br>
	 *         等于0表示e1等于e2;<br>
	 *         大于0表示e1大于e2
	 */
	protected abstract int compare(E e1, E e2);

	/**
	 * 比较数据，将{@link MutilDataScaner#datas}中的数据进行比较，并将结果存入
	 * {@link MutilDataScaner#compareResult}中
	 * 
	 * @throws Exception
	 *             异常
	 */
	@SuppressWarnings("unchecked")
	protected void compareData() throws Exception {

		// 先把真值表初始化为false
		Arrays.fill(result, false);

		// 最小值
		E min = (E) datas[0];

		// 当前值
		E current;

		// 差距
		int between;

		// 遍历所有数据，找出最小的
		for (int i = 0; i < length; i++) {
			current = (E) datas[i];
			// 跳过null的数据
			if (current == null) {
				continue;
			}
			// 最小值为空则用当前的做为最小的
			if (min == null) {
				min = current;
				result[i] = true;
				continue;
			}
			// 比较最小值和当前值
			between = compare(min, current);

			// 如果最小值大于当前值，用当前值作为最小值，并更新真值表
			if (between > 0) {
				min = current;
				Arrays.fill(result, false);
				result[i] = true;
			} else if (between == 0) {
				// 如果相等则只修改真值表
				result[i] = true;
			}
		}

		// 遍历所有数据，取出包含最小值的结果集中的数据
		for (int i = 0; i < length; i++) {
			if (result[i]) {
				compareResult[i] = datas[i];
			} else {
				compareResult[i] = null;
			}
		}
	}

	/**
	 * 处理比较的结果
	 * 
	 * @param type
	 *            该结果的真值表，长度和数据集个数相等，例如[true,false,true]表示该结果在1、3两个数据集中存在，在2中不存在
	 * @param resultDatas
	 *            比较结果，长度和数据集个数相等，不为null时内容为数据集中的数据，为null时表示该数据集中没有
	 * @return 是否继续处理，如要继续处理之后的数据，则返回True
	 * @throws Exception
	 *             异常
	 */
	protected abstract boolean dealCompareResult(boolean[] type, Object... resultDatas) throws Exception;

	/**
	 * 回收资源，本方法用于回收各种资源，搜索完成后会执行. <br>
	 * 如果DataReader是Closeable的，则会自动关闭。
	 * 
	 * @throws Exception
	 *             异常
	 */
	protected void doFinalize() throws Exception {
		for (DataReader<E> reader : dataReaders) {
			if (reader instanceof Closeable) {
				Closeable closeable = (Closeable) reader;
				closeable.close();
			}
		}
	}

	/**
	 * 搜索
	 * 
	 * @throws Exception
	 *             异常
	 */
	public void doScan() throws Exception {
		// 循环调用分步搜索方法，直到分步搜索方法返回处理结束
		while (doScan(10000)) {
		}
	}

	/**
	 * 分步搜索
	 * 
	 * @param stepCount
	 *            执行的次数
	 * @return true:还有数据;false:没有数据了
	 * @throws Exception
	 *             异常
	 */
	public boolean doScan(int stepCount) throws Exception {
		// 数据集是否全部结束
		boolean allNull;
		// 计数器，表示处理了多少条
		int count = 0;
		while (true) {
			count++;
			// 遍历所有数据集
			for (int i = 0; i < length; i++) {
				// 根据前一次的真值表来获取内容，如果为true时才获取下一条
				if (result[i]) {
					dataReaders[i].next();
					datas[i] = dataReaders[i].getData();
				}
			}

			allNull = true;
			// 遍历所有结果，只要有一个不是null，就将allNull置为false
			for (Object o : datas) {
				if (o != null) {
					allNull = false;
					break;
				}
			}

			if (!allNull) {
				compareData();
				if (!dealCompareResult(result, compareResult)) {
					doFinalize();
					return false;
				}
				// 达到执行限制则停止
				if (count == stepCount) {
					return true;
				}
			} else {
				// 全部为空时结束循环
				doFinalize();
				return false;
			}
		}
	}

}
