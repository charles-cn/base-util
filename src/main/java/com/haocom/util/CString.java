package com.haocom.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串处理工具. <br>
 * 字符串处理工具,可以对字符串进行多种处理操作，包括查找，填充，分隔等等.
 * <p>
 * Copyright: Copyright (c) Sep 16, 2008
 * <p>
 * Company: 
 * <p>
 * Author: yuanwh
 * <p>
 * Version: 1.0
 */

public class CString {

	/**
	 * 判断字符是否为null或者为空
	 * 
	 * @param val
	 *            字符串
	 * @return true：空字符串；false：不是空字符串；
	 */
	public static boolean isEmpty(String val) {
		if (val == null) {
			return true;
		} else {
			return val.length() > 0 ? false : true;
		}
	}

	/**
	 * 检查字符串是否为手机号码
	 * 
	 * @param sMobile
	 *            号码字符串
	 * @return true：手机号码；false：无效号码；
	 */
	public static boolean checkMobile(String sMobile) {
		try {
			return sMobile.matches("1\\d{10}");
		}
		catch (Exception event) {
			return false;
		}
	}

	/**
	 * 通过正则表达式检查字符串是否全是数字
	 * 
	 * @param val
	 *            数字字符串
	 * @return true：全数字；false：不是全数字；
	 */
	public static boolean checkNum(String val) {
		if (val == null) {
			return false;
		} else {
			return Pattern.matches("[-]?[0-9]+", val.trim());
		}
	}


	/**
	 * 检查字符串是否匹配正则表达式
	 * 
	 * @param rex
	 *            正则表达式
	 * @param val
	 *            字符串
	 * @return true：匹配；false：不匹配；
	 */
	public static boolean checkVal(String rex, String val) {
		if (val == null) {
			return false;
		} else {
			return Pattern.matches(rex, val.trim());
		}
	}

	/**
	 * 替换字符串的全角字符为半角字符
	 * 
	 * @param sStr
	 *            原字符串
	 * @return 替换后的新字符串
	 */
	public static String DBC2SBC(String sStr) {
		int code;
		StringBuffer result = new StringBuffer();
		for (int i = 0; i < sStr.length(); i++) {
			code = sStr.charAt(i);
			// “65281”是“！”，“65373”是“｝”
			if (code >= 65281 && code < 65373) {

				// “65248”是转换码距
				result.append((char) (sStr.charAt(i) - 65248));
			} else if (code == 0) {// && code==' '
				result.append(' ');
			} else {
				result.append(sStr.charAt(i));
			}
		}
		return result.toString();
	}

	/**
	 * 随机产生1个密码：一组字母+数字+特殊符号的字符串.<br>
	 * 字符支持范围为0-9，a-z，A-Z和+-*=<>
	 * 
	 * @param length
	 *            密码长度
	 * @return 产生的密码
	 * @throws Exception
	 */
	public static String generatePassword(int length) throws Exception {
		if (length <= 0) {
			throw new Exception("密码长度不对！");
		}
		Random rand = new Random();
		char[] password = new char[length];
		char[] spChars = { '+', '-', '*', '<', '>', '=' };
		for (int i = 0; i < length; i++) {
			//
			switch (rand.nextInt(4)) {
				case 0: // 0-9
					password[i] = (char) ('0' + rand.nextInt(10));
					break;
				case 1: // a-z
					password[i] = (char) ('a' + rand.nextInt(26));
					break;
				case 2: // A-Z
					password[i] = (char) ('A' + rand.nextInt(26));
					break;
				case 3: // 特殊字符
					password[i] = spChars[rand.nextInt(spChars.length)];
					break;
			}
		}
		return new String(password);
	}

	/**
	 * 根据输入的url获取返回的内容。本方法返回一个字节数组。默认超时时间为5秒
	 * 
	 * @param url
	 *            url地址
	 * @return 返回根据url获取到的内容
	 * @throws Exception
	 *             异常信息
	 */
	public static byte[] getContentByUrl(String url) throws Exception {
		return getContentByUrl(url, 5);
	}

	/**
	 * 根据输入的url获取返回的内容。本方法返回一个字节数组
	 * 
	 * @param url
	 *            url地址
	 * @param timeOut
	 *            超时时间，单位为秒
	 * @return 返回根据url获取到的内容
	 * @throws Exception
	 *             异常信息
	 */
	public static byte[] getContentByUrl(String url, int timeOut) throws Exception {
		URL contentUrl = new URL(url);
		HttpURLConnection connection = (HttpURLConnection) contentUrl.openConnection();
		try {
			// 设置读取超时时间和连接超时时间
			connection.setReadTimeout(1000 * timeOut);
			connection.setConnectTimeout(1000 * timeOut);

			// 设置是否可以输入输出
			connection.setDoInput(true);
			connection.setDoOutput(true);

			// Get Response Code/Message
			int responseCode = connection.getResponseCode();
			String responseMessage = connection.getResponseMessage();
			// Read Response Content
			{
				ByteArrayOutputStream baos = new ByteArrayOutputStream(1024 * 64);
				InputStream inputStream = connection.getInputStream();
				try {
					byte[] buf = new byte[1024 * 64];
					int n;
					while ((n = inputStream.read(buf)) >= 0) {
						baos.write(buf, 0, n);
					}
				}
				catch (Exception ex) {
					ex.printStackTrace();
				}
				finally {
					inputStream.close();
				}
				byte[] responseContent = baos.toByteArray();
				return responseContent;
			}
		}
		catch (Exception ex) {
			throw ex;
		}
		finally {
			connection.disconnect();
		}
	}

	/**
	 * 返回查找内容
	 * 
	 * @param str
	 *            输入的字符串
	 * @param regex
	 *            正则表达式
	 * @return 在输入的字符串中满足正则表达式的的内容；若无，则返回null
	 */
	public static String getSubString(String str, String regex) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(str);
		if (matcher.find()) {
			return matcher.group();
		}
		return null;
	}

	/**
	 * 返回查找的内容
	 * 
	 * @param str
	 *            输入的字符串
	 * @param regex
	 *            正则表达式
	 * @param groupIndex
	 *            表示需要查找到的内容的第几组
	 * @return 在输入的字符串中满足正则表达式的几组内容中的第groupIndex组的内容；若无满足则返回null
	 */
	public static String getSubString(String str, String regex, int groupIndex) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(str);
		if (matcher.find()) {
			return matcher.group(groupIndex);
		}
		return null;
	}

	/**
	 * 判断是否为中文字符
	 * 
	 * @param c
	 *            输入的字符
	 * @return true：是中文字符；false：不是中文字符
	 */
	public static boolean isChinese(char c) {
		if (c >= 19968 && c <= 40869) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 判断是否为日文字符
	 * 
	 * @param c
	 *            输入的字符
	 * @return true：是日文字符；false：不是日文字符
	 */
	public static boolean isJapanese(char c) {
		if (c >= 12449 && c <= 12538) {
			return true;
		} else if (c >= 12353 && c <= 12436) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 左填充
	 * 
	 * @param srcStr
	 *            原字符串
	 * @param padChar
	 *            填充字符串
	 * @param length
	 *            填充位数
	 * @return 返回填充后的字符串
	 */
	public static String lpad(String srcStr, String padChar, int length) {
		StringBuffer str = new StringBuffer();
		str.append(padding(srcStr, padChar, length));
		str.append(srcStr);
		return str.toString();
	}

	/**
	 * 填充
	 * 
	 * @param oldStr
	 *            原字符串
	 * @param padChar
	 *            填充字符串
	 * @param length
	 *            填充位数
	 * @return 返回填充的字符串
	 */
	private static String padding(String oldStr, String padChar, int length) {
		StringBuffer str = new StringBuffer();
		for (int i = 0; i < length - oldStr.length(); i++) {
			str.append(padChar);
		}
		return str.toString();
	}

	/**
	 * 替换全角字符为半角字符
	 * 
	 * @param chars
	 *            输入的字符数组
	 */
	public static void replaceFullCharToHalfChar(char[] chars) {
		replaceFullCharToHalfChar(chars, 0, chars.length);
	}

	/**
	 * 替换全角字符为半角字符
	 * 
	 * @param chars
	 *            输入的字符数组
	 * @param beginIndex
	 *            开始替换的位置
	 * @param endIndex
	 *            结束替换的位置
	 */
	public static void replaceFullCharToHalfChar(char[] chars, int beginIndex, int endIndex) {
		char c;
		for (int i = beginIndex; i < endIndex; i++) {
			c = chars[i];
			if (c >= 0xFF01 && c <= 0xFF5E) {
				chars[i] = (char) (c - 0xFEE0);
			} else if (c == '　') {
				chars[i] = ' ';
			}
		}
	}

	/**
	 * 替换全角字符为半角字符
	 * 
	 * @param str
	 *            输入字符串
	 * @return 返回输入的字符串的半角表示
	 */
	public static String replaceFullCharToHalfChar(String str) {
		char[] chars = str.toCharArray();
		replaceFullCharToHalfChar(chars, 0, chars.length);
		return new String(chars);
	}

	/**
	 * 替换半角字符为全角字符
	 * 
	 * @param chars
	 *            输入的字符数组
	 */
	public static void replaceHalfCharToFullChar(char[] chars) {
		replaceHalfCharToFullChar(chars, 0, chars.length);
	}

	/**
	 * 替换半角字符为全角字符
	 * 
	 * @param chars
	 *            输入的字符数组
	 * @param beginIndex
	 *            开始替换的位置
	 * @param endIndex
	 *            结束替换的位置
	 */
	public static void replaceHalfCharToFullChar(char[] chars, int beginIndex, int endIndex) {
		char c;
		for (int i = beginIndex; i < endIndex; i++) {
			c = chars[i];
			if (c >= 0x0021 && c <= 0x007E) {
				chars[i] = (char) (c + 0xFEE0);
			} else if (c == ' ') {
				chars[i] = '　';
			}
		}
	}

	/**
	 * 替换半角字符为全角字符
	 * 
	 * @param str
	 *            输入字符串
	 * @return 返回输入的字符串的全角表示
	 */
	public static String replaceHalfCharToFullChar(String str) {
		char[] chars = str.toCharArray();
		replaceHalfCharToFullChar(chars, 0, chars.length);
		return new String(chars);
	}

	/**
	 * 替换字符串的全角数字为半角数字
	 * 
	 * @param sReturn
	 *            原字符串
	 * @return 替换后的新字符串
	 */
	public static String replaceNumber(String sReturn) {
		try {
			String str = "FDs";
			str = str.replaceAll("", "");
			sReturn = sReturn.replace('０', '0');
			sReturn = sReturn.replace('１', '1');
			sReturn = sReturn.replace('２', '2');
			sReturn = sReturn.replace('３', '3');
			sReturn = sReturn.replace('４', '4');
			sReturn = sReturn.replace('５', '5');
			sReturn = sReturn.replace('６', '6');
			sReturn = sReturn.replace('７', '7');
			sReturn = sReturn.replace('８', '8');
			sReturn = sReturn.replace('９', '9');
		}
		catch (Exception ex) {
			return sReturn;
		}
		return sReturn;
	}

	/**
	 * 右填充
	 * 
	 * @param srcStr
	 *            原字符
	 * @param padChar
	 *            填充字符串
	 * @param length
	 *            填充位数
	 * @return 返回填充后的字符串
	 */
	public static String rpad(String srcStr, String padChar, int length) {
		StringBuffer str = new StringBuffer();
		str.append(srcStr);
		str.append(padding(srcStr, padChar, length));
		return str.toString();
	}

	/**
	 * 根据输入的url获取返回的内容，并保存到本地文件。默认超时时间为5秒
	 * 
	 * @param fileName
	 *            用于保存的本地文件
	 * @param url
	 *            url地址
	 * @throws Exception
	 *             异常信息
	 */
	public static void saveContentByUrl(String fileName, String url) throws Exception {
		saveContentByUrl(fileName, url, 5);
	}

	/**
	 * 根据输入的url获取返回的内容，并保存到本地文件
	 * 
	 * @param fileName
	 *            用于保存的本地文件
	 * @param url
	 *            url地址
	 * @param timeOut
	 *            超时时间，单位为秒
	 * @throws Exception
	 *             异常信息
	 */
	public static void saveContentByUrl(String fileName, String url, int timeOut) throws Exception {
		// 获取指定url地址返回的内容
		byte[] contents = getContentByUrl(url, timeOut);
		// 检查待保存的文件路径是否已存在，不存在则创建
		File parent = new File(fileName).getParentFile();
		if (parent != null && !parent.exists()) {
			parent.mkdirs();
		}
		// 将获取到的内容保存到本地文件
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(fileName);
			out.write(contents);
			out.flush();
		}
		catch (Exception e) {
			throw e;
		}
		finally {
			out.close();
		}
	}

	/**
	 * 分割字符串，根据单个符号来分割，支持多符号组合
	 * 
	 * @param msg
	 *            需要分割的字符串
	 * @param symbol
	 *            作为分割条件的符号组合，如"' ;"等
	 * @return 分割后的字符串数组
	 */
	public static String[] splitByChar(String msg, String symbol) {
		String regex = "[" + symbol + "]";
		return msg.split(regex);
	}

	/**
	 * 分割字符串，根据子串来分割
	 * 
	 * @param msg
	 *            需要分割的字符串
	 * @param symbol
	 *            作为分割条件的子串
	 * @return 分割后的字符串数组
	 */
	public static String[] splitByString(String msg, String symbol) {
		return msg.split(symbol);
	}

	/**
	 * 字符串转整型数字
	 * 
	 * @param number
	 *            字符串
	 * @return 整型数字
	 */
	public static int string2Int(String number) {
		int n = 0;
		try {
			n = Integer.parseInt(number);
		}
		catch (Exception event) {
			return 0;
		}

		return n;
	}

	/**
	 * 字符串转长整型数字
	 * 
	 * @param number
	 *            字符串
	 * @return 长整型数字
	 */
	public static long string2Long(String number) {
		long n = 0;
		try {
			n = Long.parseLong(number);
		}
		catch (Exception event) {
			return 0;
		}

		return n;
	}

	/**
	 * 剔除字符串中的空格，包括前、中、后的所以空格字符
	 * 
	 * @param msg
	 *            原字符串
	 * @return 剔除操作后的新字符串
	 */
	public static String trimAll(String msg) {
		return msg.replaceAll(" ", "");
	}
}
