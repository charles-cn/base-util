package com.haocom.util;

/**
 * 中文字符转码. <br>
 * 如果数据库字符集和系统字符集不一致,存取数据时需要系统字符集和数据库字符集汉字编码之间做转换,可在系统启动时动态设置是否需要转码.
 * <p>
 * Copyright: Copyright (c) Sep 16, 2008
 * <p>
 * Company: 
 * <p>
 * Author: ywh
 * <p>
 * Version: 1.0
 */

public class Chinese {

	/** 系统级标识，是否需要转码，默认为不需要转码 */
	private static boolean isChangeEncoding = false;

	/**
	 * 把数据库中取出的字符串（iso-8859-1）转换为程序中中文字符串（gb18030）
	 * 
	 * @param change
	 *            需要转换的数据库字符
	 * @return 如果需要转换，则返回转换后的程序中的中文字符；反之，直接返回输入值
	 */
	public static String fromDatabase(String change) {
		if (isChangeEncoding) {
			try {
				return new String(change.getBytes("iso-8859-1"), "gb18030");
			}
			catch (Exception e) {
				return change;
			}
		} else {
			return change;
		}
	}

	/**
	 * 设定是否启动转码功能，默认为不启用
	 * 
	 * @param bool
	 *            是否启动转码功能
	 */
	public static void setChangeEncoding(boolean bool) {
		isChangeEncoding = bool;
	}

	/**
	 * 把中文字符串（gb18030）转换为存入数据库中的字符串（iso-8859-1）
	 * 
	 * @param change
	 *            需要转换的程序中的中文字符串
	 * @return 转换后的数据库字符串
	 */
	public static String toDatabase(String change) {
		if (isChangeEncoding) {
			try {
				return new String(change.getBytes("gb18030"), "iso-8859-1");
			}
			catch (Exception e) {
				return change;
			}
		} else {
			return change;
		}
	}
}
