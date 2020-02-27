package com.haocom.config;

import java.util.HashMap;
import java.util.Vector;

/**
 * 各方法的抽象定义. <br>
 * 各方法的抽象定义,其中包括获取配置、设置参数值等.
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
	 * 获取boolean型配置值，如果没有则返回false.
	 * 
	 * @param name
	 *            配置名称
	 * @return boolean型配置值
	 */
	public boolean getBoolean(String name);

	/**
	 * 获取boolean型配置值，如果没有则返回默认值.
	 * 
	 * @param name
	 *            配置名称
	 * @param defValue
	 *            默认值
	 * @return boolean型配置值
	 */
	public boolean getBoolean(String name, boolean defValue);

	/**
	 * 获取double型配置值，如果没有则返回0.
	 * 
	 * @param name
	 *            配置名称
	 * @return double型配置值
	 */
	public double getDouble(String name);

	/**
	 * 获取double型配置值，如果没有则返回默认值.
	 * 
	 * @param name
	 *            配置名称
	 * @param defValue
	 *            默认值
	 * @return double型配置值
	 */
	public double getDouble(String name, double defValue);

	/**
	 * 获取float型配置值，如果没有则返回0.
	 * 
	 * @param name
	 *            配置名称
	 * @return float型配置值
	 */
	public float getFloat(String name);

	/**
	 * 获取float型配置值，如果没有则返回默认值.
	 * 
	 * @param name
	 *            配置名称
	 * @param defValue
	 *            默认值
	 * @return float型配置值
	 */
	public float getFloat(String name, float defValue);

	/**
	 * 获取int型配置值，如果没有则返回0.
	 * 
	 * @param name
	 *            配置名称
	 * @return Integer型配置值
	 */
	public int getInteger(String name);

	/**
	 * 获取int型配置值，如果没有则返回默认值.
	 * 
	 * @param name
	 *            配置名称
	 * @param defValue
	 *            默认值
	 * @return Integer型配置值
	 */
	public int getInteger(String name, int defValue);

	/**
	 * 获取long型配置值，如果没有则返回0.
	 * 
	 * @param name
	 *            配置名称
	 * @return long型配置值
	 */
	public long getLong(String name);

	/**
	 * 获取long型配置值，如果没有则返回默认值.
	 * 
	 * @param name
	 *            配置名称
	 * @param defValue
	 *            默认值
	 * @return long型配置值
	 */
	public long getLong(String name, long defValue);

	/**
	 * 获取配置名清单.
	 * 
	 * @return 配置名清单Vector<配置名(String)>
	 */
	public Vector getPropertyNameList();

	/**
	 * 获取配置项清单.
	 * 
	 * @param perfix
	 *            配置名前缀
	 * @return 配置名清单Vector<配置名(String)>
	 */
	public Vector getPropertyNameList(String perfix);

	/**
	 * 获取String型配置值，如果没有则返回null.
	 * 
	 * @param name
	 *            配置名称
	 * @return String型配置值
	 */
	public String getValue(String name);

	/**
	 * 获取String型配置值，如果没有则返回默认值.
	 * 
	 * @param name
	 *            配置名称
	 * @param defValue
	 *            默认值
	 * @return String型配置值
	 */
	public String getValue(String name, String defValue);

	/**
	 * 获取所有参数.
	 * 
	 * @return 参数Map <参数名称(String), 参数值(String)>
	 */
	public HashMap getValues();

	/**
	 * 删除参数.
	 * 
	 * @param name
	 *            参数名
	 */
	public void removeValues(String name);

	/**
	 * 设置参数值.
	 * 
	 * @param name
	 *            参数名
	 * @param value
	 *            参数值
	 */
	public void setValues(String name, boolean value);

	/**
	 * 设置参数值.
	 * 
	 * @param name
	 *            参数名
	 * @param value
	 *            参数值
	 */
	public void setValues(String name, double value);

	/**
	 * 设置参数值.
	 * 
	 * @param name
	 *            参数名
	 * @param value
	 *            参数值
	 */
	public void setValues(String name, float value);

	/**
	 * 设置参数值.
	 * 
	 * @param name
	 *            参数名
	 * @param value
	 *            参数值
	 */
	public void setValues(String name, int value);

	/**
	 * 设置参数值.
	 * 
	 * @param name
	 *            参数名
	 * @param value
	 *            参数值
	 */
	public void setValues(String name, long value);

	/**
	 * 设置参数值.
	 * 
	 * @param name
	 *            参数名
	 * @param value
	 *            参数值
	 */
	public void setValues(String name, String value);
}
