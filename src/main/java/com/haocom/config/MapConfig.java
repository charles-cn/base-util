package com.haocom.config;

import java.util.Collections;
import java.util.HashMap;
import java.util.Vector;

/**
 * 以Map方式存放的配置信息. <br>
 * 以Map方式存放的配置信息.
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

	/** 配置的名称 */
	private String name = null;

	/**
	 * 存放配置信息的Map, 存放格式<配置名(String), 配置值(String)>.
	 */
	protected HashMap<String, String> values = new HashMap();

	/**
	 * 创建配置对象.
	 * 
	 * @param name
	 *            配置的名称
	 */
	public MapConfig(String name) {
		this.name = name;
	}

	/**
	 * 创建配置对象，配置信息将由参数中clone而来.
	 * 
	 * @param name
	 *            配置的名称
	 * @param config
	 *            配置实例
	 */
	public MapConfig(String name, MapConfig config) {
		this.name = name;
		this.values = (HashMap) config.values.clone();
	}

	public boolean getBoolean(String name) {
		return getBoolean(name, false);
	}

	/**
	 * 获取boolean型配置值,如果没有则返回默认值;当参数值满足yes,y,true,t,1中任意一个时均返回true,其它情况返回false.
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
	 * 获取配置的名称
	 * 
	 * @return 返回配置的名称
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
	 * 设置配置信息，配置信息将被clone
	 * 
	 * @param values
	 *            配置信息
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
