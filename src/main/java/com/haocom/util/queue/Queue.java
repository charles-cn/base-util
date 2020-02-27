package com.haocom.util.queue;

/**
 * ���л�������. <br>
 * ���л���������������ӣ���ȡ��С���ȵ�.
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
	 * �򻺳������������ݣ��������Ƿ���ӳɹ���<br>
	 * ��������Ѿ���������Ӳ��ɹ���<br>
	 * 
	 * @param obj
	 *            �������
	 * @return �Ƿ����ɹ�
	 */
	public boolean add(E obj);

	/**
	 * �򻺳������ǿ��������ݣ��������Ƿ���ӳɹ���<br>
	 * 
	 * @param obj
	 *            �������
	 * @return �Ƿ����ɹ�
	 */
	public boolean addForce(E obj);

	/**
	 * ��ն���
	 */
	public void clear();

	/**
	 * ��ȡ����������
	 * 
	 * @return ���д�С����
	 */
	public int getMaxQueueSize();

	/**
	 * ��ȡ�˶�������
	 * 
	 * @return ��������
	 */
	public String getName();

	/**
	 * ��ȡ���д�С
	 * 
	 * @return ���д�С
	 */
	public int getSize();

	/**
	 * ��ȡ�����Ƿ�Ϊ��
	 * 
	 * @return �����Ƿ�Ϊ��
	 */
	public boolean isEmpty();

	/**
	 * ��ȡ�����Ƿ��Ѿ�����
	 * 
	 * @return �����Ƿ�����
	 */
	public boolean isFull();

	/**
	 * �ӻ�������п���һ������
	 * 
	 * @return �����е�һ������
	 */
	public E peek();

	/**
	 * �ӻ�������л�ȡһ������
	 * 
	 * @return �����е�һ������
	 */
	public E remove();

	/**
	 * �������еĶ���ת��Ϊ����ı�����ʽ
	 * 
	 * @return �����Ŷ���
	 */
	public Object[] toArray();

	/**
	 * ����һ�������� set ����Ԫ�ص����飻�������������ʱ������ָ����������͡����ָ�������������ɸ� set���� set
	 * ���ش˴������򣬽�����һ������ָ�����������ʱ���ͺʹ� set ��С�������顣<BR>
	 * ���ָ�������������� set������ʣ��Ŀռ䣨�������Ԫ�ر� set �ࣩ����ô�Ὣ�� set β����Ԫ������Ϊ null������ ��������֪����
	 * set �������κ� null Ԫ��ʱ���ſ�ʹ�ô˷�����ȷ���� set �ĳ��ȡ���<BR>
	 * ����� set ������������ص�Ԫ��˳��������ĳЩ��֤����ô�˷�����������ͬ��˳�򷵻���ЩԪ�ء�<BR>
	 * �� toArray() ����һ�����˷����䵱�������� �� API ����� collection �� API
	 * ֮�������������һ��˵���˷��������������������ʱ���ͽ��о�ȷ���ƣ���ĳЩ����£�����������ʡ���俪����<BR>
	 * �ٶ� x ��ֻ�����ַ�����һ����֪ set�����´����������� set ת����һ���·���� String ���飺<BR>
	 * String[] y = x.toArray(new String[0]);<BR>
	 * ע�⣬toArray(new Object[0]) �� toArray() �ڹ���������ͬ�ġ�
	 * 
	 * @param a
	 *            �洢�� - set Ԫ�ص����飨����������㹻�󣩣�����Ϊ�˷���һ��������ͬ����ʱ���͵�������
	 * @return ������ set ����Ԫ�ص�����
	 */
	public E[] toArray(E[] a);
}
