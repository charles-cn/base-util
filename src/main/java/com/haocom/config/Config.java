package com.haocom.config;

import java.util.HashMap;
import java.util.Vector;

/**
 * �������ĳ�����. <br>
 * �������ĳ�����,���а�����ȡ���á����ò���ֵ��.
 * <p>
 * Copyright: Copyright (c) 2009-3-18
 * <p>
 * Company: 
 * <p>
 * Author: 
 * <p>
 * Version: 1.0
 */
public interface Config {

	/**
	 * ��ȡboolean������ֵ�����û���򷵻�false.
	 * 
	 * @param name
	 *            ��������
	 * @return boolean������ֵ
	 */
	public boolean getBoolean(String name);

	/**
	 * ��ȡboolean������ֵ�����û���򷵻�Ĭ��ֵ.
	 * 
	 * @param name
	 *            ��������
	 * @param defValue
	 *            Ĭ��ֵ
	 * @return boolean������ֵ
	 */
	public boolean getBoolean(String name, boolean defValue);

	/**
	 * ��ȡdouble������ֵ�����û���򷵻�0.
	 * 
	 * @param name
	 *            ��������
	 * @return double������ֵ
	 */
	public double getDouble(String name);

	/**
	 * ��ȡdouble������ֵ�����û���򷵻�Ĭ��ֵ.
	 * 
	 * @param name
	 *            ��������
	 * @param defValue
	 *            Ĭ��ֵ
	 * @return double������ֵ
	 */
	public double getDouble(String name, double defValue);

	/**
	 * ��ȡfloat������ֵ�����û���򷵻�0.
	 * 
	 * @param name
	 *            ��������
	 * @return float������ֵ
	 */
	public float getFloat(String name);

	/**
	 * ��ȡfloat������ֵ�����û���򷵻�Ĭ��ֵ.
	 * 
	 * @param name
	 *            ��������
	 * @param defValue
	 *            Ĭ��ֵ
	 * @return float������ֵ
	 */
	public float getFloat(String name, float defValue);

	/**
	 * ��ȡint������ֵ�����û���򷵻�0.
	 * 
	 * @param name
	 *            ��������
	 * @return Integer������ֵ
	 */
	public int getInteger(String name);

	/**
	 * ��ȡint������ֵ�����û���򷵻�Ĭ��ֵ.
	 * 
	 * @param name
	 *            ��������
	 * @param defValue
	 *            Ĭ��ֵ
	 * @return Integer������ֵ
	 */
	public int getInteger(String name, int defValue);

	/**
	 * ��ȡlong������ֵ�����û���򷵻�0.
	 * 
	 * @param name
	 *            ��������
	 * @return long������ֵ
	 */
	public long getLong(String name);

	/**
	 * ��ȡlong������ֵ�����û���򷵻�Ĭ��ֵ.
	 * 
	 * @param name
	 *            ��������
	 * @param defValue
	 *            Ĭ��ֵ
	 * @return long������ֵ
	 */
	public long getLong(String name, long defValue);

	/**
	 * ��ȡ�������嵥.
	 * 
	 * @return �������嵥Vector<������(String)>
	 */
	public Vector getPropertyNameList();

	/**
	 * ��ȡ�������嵥.
	 * 
	 * @param perfix
	 *            ������ǰ׺
	 * @return �������嵥Vector<������(String)>
	 */
	public Vector getPropertyNameList(String perfix);

	/**
	 * ��ȡString������ֵ�����û���򷵻�null.
	 * 
	 * @param name
	 *            ��������
	 * @return String������ֵ
	 */
	public String getValue(String name);

	/**
	 * ��ȡString������ֵ�����û���򷵻�Ĭ��ֵ.
	 * 
	 * @param name
	 *            ��������
	 * @param defValue
	 *            Ĭ��ֵ
	 * @return String������ֵ
	 */
	public String getValue(String name, String defValue);

	/**
	 * ��ȡ���в���.
	 * 
	 * @return ����Map <��������(String), ����ֵ(String)>
	 */
	public HashMap getValues();

	/**
	 * ɾ������.
	 * 
	 * @param name
	 *            ������
	 */
	public void removeValues(String name);

	/**
	 * ���ò���ֵ.
	 * 
	 * @param name
	 *            ������
	 * @param value
	 *            ����ֵ
	 */
	public void setValues(String name, boolean value);

	/**
	 * ���ò���ֵ.
	 * 
	 * @param name
	 *            ������
	 * @param value
	 *            ����ֵ
	 */
	public void setValues(String name, double value);

	/**
	 * ���ò���ֵ.
	 * 
	 * @param name
	 *            ������
	 * @param value
	 *            ����ֵ
	 */
	public void setValues(String name, float value);

	/**
	 * ���ò���ֵ.
	 * 
	 * @param name
	 *            ������
	 * @param value
	 *            ����ֵ
	 */
	public void setValues(String name, int value);

	/**
	 * ���ò���ֵ.
	 * 
	 * @param name
	 *            ������
	 * @param value
	 *            ����ֵ
	 */
	public void setValues(String name, long value);

	/**
	 * ���ò���ֵ.
	 * 
	 * @param name
	 *            ������
	 * @param value
	 *            ����ֵ
	 */
	public void setValues(String name, String value);
}
