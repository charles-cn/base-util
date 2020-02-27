package com.haocom.util.data_scanner;

import java.io.Closeable;
import java.util.Arrays;

/**
 * �����ݼ����ͱȽ�. <br>
 * �������ڶ����ݼ�֮��ļ��ͱȽ�<br>
 * �������ʵ��{@link MutilDataScaner#compare(Object, Object)}���������ڱȽ��������ݵĴ�С��<br>
 * �������ʵ��{@link MutilDataScaner#dealCompareResult(boolean[], Object...)}
 * ���������ڴ���ȽϺ�Ľ����<br>
 * �������д{@link MutilDataScaner#doFinalize()}�����ڴ�����ɺ������Դ��<br>
 * �˷��������DataReader��java.io.Closeable�ģ�����Զ��رո�DataReader.<br>
 * �������д{@link MutilDataScaner#compareData()}������ʵ�ִ����߼���<br>
 * <p>
 * �޸���ʷ: 2009-4-23 ����11:00:02 gaowei <br>
 * ����
 * <p>
 * �޸���ʷ: 2009-6-14 ����05:15:51 chengfan <br>
 * �����Զ��ر�DataReader�Ĺ��ܡ����DataReader��java.io.Closeable�ģ�����Զ��رո�DataReader.<br>
 * <p>
 * Copyright: Copyright (c) 2009-4-23 ����11:00:02
 * <p>
 * Company: 
 * <p>
 * Author: gaowei
 * <p>
 * Version: 1.0
 * <p>
 */
public abstract class MutilDataScaner<E> {

	/** �ȽϽ��. */
	protected Object[] compareResult;

	/** ���ݼ�������. */
	protected DataReader<E>[] dataReaders;

	/** ���Ƚϵ�����. */
	protected Object[] datas;

	/** ���ݼ�����. */
	protected int length;

	/** ����������ֵ��. */
	protected boolean[] result;

	/**
	 * ������
	 * 
	 * @param dataReaders
	 *            �����ݼ��Ķ�ȡ��
	 */
	protected MutilDataScaner(DataReader<E>... dataReaders) {
		this.dataReaders = dataReaders;
		length = dataReaders.length;
		result = new boolean[length];

		datas = new Object[length];
		compareResult = new Object[length];

		// ��һ���ȴ���Ϊtrue�����ڻ�ȡȫ�����ݼ��ĵ�һ������
		Arrays.fill(result, true);
	}

	/**
	 * �Ƚ�����
	 * 
	 * @param e1
	 *            ����1
	 * @param e2
	 *            ����2
	 * @return С��0��ʾe1С��e2;<br>
	 *         ����0��ʾe1����e2;<br>
	 *         ����0��ʾe1����e2
	 */
	protected abstract int compare(E e1, E e2);

	/**
	 * �Ƚ����ݣ���{@link MutilDataScaner#datas}�е����ݽ��бȽϣ������������
	 * {@link MutilDataScaner#compareResult}��
	 * 
	 * @throws Exception
	 *             �쳣
	 */
	@SuppressWarnings("unchecked")
	protected void compareData() throws Exception {

		// �Ȱ���ֵ���ʼ��Ϊfalse
		Arrays.fill(result, false);

		// ��Сֵ
		E min = (E) datas[0];

		// ��ǰֵ
		E current;

		// ���
		int between;

		// �����������ݣ��ҳ���С��
		for (int i = 0; i < length; i++) {
			current = (E) datas[i];
			// ����null������
			if (current == null) {
				continue;
			}
			// ��СֵΪ�����õ�ǰ����Ϊ��С��
			if (min == null) {
				min = current;
				result[i] = true;
				continue;
			}
			// �Ƚ���Сֵ�͵�ǰֵ
			between = compare(min, current);

			// �����Сֵ���ڵ�ǰֵ���õ�ǰֵ��Ϊ��Сֵ����������ֵ��
			if (between > 0) {
				min = current;
				Arrays.fill(result, false);
				result[i] = true;
			} else if (between == 0) {
				// ��������ֻ�޸���ֵ��
				result[i] = true;
			}
		}

		// �����������ݣ�ȡ��������Сֵ�Ľ�����е�����
		for (int i = 0; i < length; i++) {
			if (result[i]) {
				compareResult[i] = datas[i];
			} else {
				compareResult[i] = null;
			}
		}
	}

	/**
	 * ����ȽϵĽ��
	 * 
	 * @param type
	 *            �ý������ֵ�����Ⱥ����ݼ�������ȣ�����[true,false,true]��ʾ�ý����1��3�������ݼ��д��ڣ���2�в�����
	 * @param resultDatas
	 *            �ȽϽ�������Ⱥ����ݼ�������ȣ���Ϊnullʱ����Ϊ���ݼ��е����ݣ�Ϊnullʱ��ʾ�����ݼ���û��
	 * @return �Ƿ����������Ҫ��������֮������ݣ��򷵻�True
	 * @throws Exception
	 *             �쳣
	 */
	protected abstract boolean dealCompareResult(boolean[] type, Object... resultDatas) throws Exception;

	/**
	 * ������Դ�����������ڻ��ո�����Դ��������ɺ��ִ��. <br>
	 * ���DataReader��Closeable�ģ�����Զ��رա�
	 * 
	 * @throws Exception
	 *             �쳣
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
	 * ����
	 * 
	 * @throws Exception
	 *             �쳣
	 */
	public void doScan() throws Exception {
		// ѭ�����÷ֲ�����������ֱ���ֲ������������ش������
		while (doScan(10000)) {
		}
	}

	/**
	 * �ֲ�����
	 * 
	 * @param stepCount
	 *            ִ�еĴ���
	 * @return true:��������;false:û��������
	 * @throws Exception
	 *             �쳣
	 */
	public boolean doScan(int stepCount) throws Exception {
		// ���ݼ��Ƿ�ȫ������
		boolean allNull;
		// ����������ʾ�����˶�����
		int count = 0;
		while (true) {
			count++;
			// �����������ݼ�
			for (int i = 0; i < length; i++) {
				// ����ǰһ�ε���ֵ������ȡ���ݣ����Ϊtrueʱ�Ż�ȡ��һ��
				if (result[i]) {
					dataReaders[i].next();
					datas[i] = dataReaders[i].getData();
				}
			}

			allNull = true;
			// �������н����ֻҪ��һ������null���ͽ�allNull��Ϊfalse
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
				// �ﵽִ��������ֹͣ
				if (count == stepCount) {
					return true;
				}
			} else {
				// ȫ��Ϊ��ʱ����ѭ��
				doFinalize();
				return false;
			}
		}
	}

}
