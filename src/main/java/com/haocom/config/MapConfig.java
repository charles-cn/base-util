package com.haocom.config;

import java.util.Collections;
import java.util.HashMap;
import java.util.Vector;

/**
 * ��Map��ʽ��ŵ�������Ϣ. <br>
 * ��Map��ʽ��ŵ�������Ϣ.
 * <p>
 * Copyright: Copyright (c) 2009-3-18
 * <p>
 * Company: 
 * <p>
 * Author: 
 * <p>
 * Version: 1.0
 */
public abstract class MapConfig implements Config {

	/** ���õ����� */
	private String name = null;

	/**
	 * ���������Ϣ��Map, ��Ÿ�ʽ<������(String), ����ֵ(String)>.
	 */
	protected HashMap<String, String> values = new HashMap();

	/**
	 * �������ö���.
	 * 
	 * @param name
	 *            ���õ�����
	 */
	public MapConfig(String name) {
		this.name = name;
	}

	/**
	 * �������ö���������Ϣ���ɲ�����clone����.
	 * 
	 * @param name
	 *            ���õ�����
	 * @param config
	 *            ����ʵ��
	 */
	public MapConfig(String name, MapConfig config) {
		this.name = name;
		this.values = (HashMap) config.values.clone();
	}

	public boolean getBoolean(String name) {
		return getBoolean(name, false);
	}

	/**
	 * ��ȡboolean������ֵ,���û���򷵻�Ĭ��ֵ;������ֵ����yes,y,true,t,1������һ��ʱ������true,�����������false.
	 */
	public boolean getBoolean(String name, boolean defValue) {
		String str = getValue(name);
		try {
			str = str.trim().toLowerCase();
			if ("yes".equals(str))
				return true;
			if ("y".equals(str))
				return true;
			if ("true".equals(str))
				return true;
			if ("t".equals(str))
				return true;
			if ("1".equals(str))
				return true;
			return false;
		}
		catch (Exception ex) {
			return defValue;
		}
	}

	public double getDouble(String name) {
		return getDouble(name, 0);
	}

	public double getDouble(String name, double defValue) {
		String str = getValue(name);
		try {
			return Double.parseDouble(str.trim());
		}
		catch (Exception ex) {
			return defValue;
		}
	}

	public float getFloat(String name) {
		return getFloat(name, 0);
	}

	public float getFloat(String name, float defValue) {
		String str = getValue(name);
		try {
			return Float.parseFloat(str.trim());
		}
		catch (Exception ex) {
			return defValue;
		}
	}

	public int getInteger(String name) {
		return getInteger(name, 0);
	}

	public int getInteger(String name, int defValue) {
		String str = getValue(name);
		try {
			return Integer.parseInt(str.trim());
		}
		catch (Exception ex) {
			return defValue;
		}
	}

	public long getLong(String name) {
		return getLong(name, 0);
	}

	public long getLong(String name, long defValue) {
		String str = getValue(name);
		try {
			return Long.parseLong(str.trim());
		}
		catch (Exception ex) {
			return defValue;
		}
	}

	/**
	 * ��ȡ���õ�����
	 * 
	 * @return �������õ�����
	 */
	public String getName() {
		return name;
	}

	public Vector getPropertyNameList() {
		HashMap values = (HashMap) this.values.clone();
		Vector result = new Vector(values.keySet());
		Collections.sort(result);
		return result;
	}

	public Vector getPropertyNameList(String perfix) {
		if (perfix == null)
			return new Vector();
		HashMap values = (HashMap) this.values.clone();
		Vector result = new Vector(values.keySet());
		for (int i = result.size() - 1; i >= 0; i--) {
			String str = (String) result.get(i);
			if (!str.startsWith(perfix))
				result.remove(i);
		}
		Collections.sort(result);
		return result;
	}

	public String getValue(String name) {
		try {
			name = name.trim().toLowerCase();
			return values.get(name);
		}
		catch (Exception ex) {
			return null;
		}
	}

	public String getValue(String name, String defValue) {
		String val = getValue(name);
		return (val == null) ? defValue : val;
	}

	public HashMap getValues() {
		return (HashMap) values.clone();
	}

	public void removeValues(String name) {
		this.values.remove(name);
	}

	/**
	 * ����������Ϣ��������Ϣ����clone
	 * 
	 * @param values
	 *            ������Ϣ
	 */
	public void setValues(HashMap values) {
		this.values = (HashMap) values.clone();
	}

	public void setValues(String name, boolean value) {
		this.values.put(name, Boolean.toString(value));
	}

	public void setValues(String name, double value) {
		this.values.put(name, Double.toString(value));
	}

	public void setValues(String name, float value) {
		this.values.put(name, Float.toString(value));
	}

	public void setValues(String name, int value) {
		this.values.put(name, Integer.toString(value));
	}

	public void setValues(String name, long value) {
		this.values.put(name, Long.toString(value));
	}

	public void setValues(String name, String value) {
		this.values.put(name, value);
	}

	@Override
	public String toString() {
		HashMap values = (HashMap) this.values.clone();
		Vector keys = new Vector(values.keySet());
		Collections.sort(keys);
		StringBuffer buf = new StringBuffer(500);
		buf.append("config name:").append(getName()).append("\r\n");
		buf.append("values:");
		for (int i = 0; i < keys.size(); i++) {
			Object key = keys.get(i);
			buf.append("\r\n\t");
			buf.append(key).append("\t= ").append(values.get(key));
		}
		return buf.toString();
	}
}
