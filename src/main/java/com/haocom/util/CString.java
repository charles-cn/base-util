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
 * �ַ���������. <br>
 * �ַ���������,���Զ��ַ������ж��ִ���������������ң���䣬�ָ��ȵ�.
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
	 * �ж��ַ��Ƿ�Ϊnull����Ϊ��
	 * 
	 * @param val
	 *            �ַ���
	 * @return true�����ַ�����false�����ǿ��ַ�����
	 */
	public static boolean isEmpty(String val) {
		if (val == null) {
			return true;
		} else {
			return val.length() > 0 ? false : true;
		}
	}

	/**
	 * ����ַ����Ƿ�Ϊ�ֻ�����
	 * 
	 * @param sMobile
	 *            �����ַ���
	 * @return true���ֻ����룻false����Ч���룻
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
	 * ͨ��������ʽ����ַ����Ƿ�ȫ������
	 * 
	 * @param val
	 *            �����ַ���
	 * @return true��ȫ���֣�false������ȫ���֣�
	 */
	public static boolean checkNum(String val) {
		if (val == null) {
			return false;
		} else {
			return Pattern.matches("[-]?[0-9]+", val.trim());
		}
	}


	/**
	 * ����ַ����Ƿ�ƥ��������ʽ
	 * 
	 * @param rex
	 *            ������ʽ
	 * @param val
	 *            �ַ���
	 * @return true��ƥ�䣻false����ƥ�䣻
	 */
	public static boolean checkVal(String rex, String val) {
		if (val == null) {
			return false;
		} else {
			return Pattern.matches(rex, val.trim());
		}
	}

	/**
	 * �滻�ַ�����ȫ���ַ�Ϊ����ַ�
	 * 
	 * @param sStr
	 *            ԭ�ַ���
	 * @return �滻������ַ���
	 */
	public static String DBC2SBC(String sStr) {
		int code;
		StringBuffer result = new StringBuffer();
		for (int i = 0; i < sStr.length(); i++) {
			code = sStr.charAt(i);
			// ��65281���ǡ���������65373���ǡ�����
			if (code >= 65281 && code < 65373) {

				// ��65248����ת�����
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
	 * �������1�����룺һ����ĸ+����+������ŵ��ַ���.<br>
	 * �ַ�֧�ַ�ΧΪ0-9��a-z��A-Z��+-*=<>
	 * 
	 * @param length
	 *            ���볤��
	 * @return ����������
	 * @throws Exception
	 */
	public static String generatePassword(int length) throws Exception {
		if (length <= 0) {
			throw new Exception("���볤�Ȳ��ԣ�");
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
				case 3: // �����ַ�
					password[i] = spChars[rand.nextInt(spChars.length)];
					break;
			}
		}
		return new String(password);
	}

	/**
	 * ���������url��ȡ���ص����ݡ�����������һ���ֽ����顣Ĭ�ϳ�ʱʱ��Ϊ5��
	 * 
	 * @param url
	 *            url��ַ
	 * @return ���ظ���url��ȡ��������
	 * @throws Exception
	 *             �쳣��Ϣ
	 */
	public static byte[] getContentByUrl(String url) throws Exception {
		return getContentByUrl(url, 5);
	}

	/**
	 * ���������url��ȡ���ص����ݡ�����������һ���ֽ�����
	 * 
	 * @param url
	 *            url��ַ
	 * @param timeOut
	 *            ��ʱʱ�䣬��λΪ��
	 * @return ���ظ���url��ȡ��������
	 * @throws Exception
	 *             �쳣��Ϣ
	 */
	public static byte[] getContentByUrl(String url, int timeOut) throws Exception {
		URL contentUrl = new URL(url);
		HttpURLConnection connection = (HttpURLConnection) contentUrl.openConnection();
		try {
			// ���ö�ȡ��ʱʱ������ӳ�ʱʱ��
			connection.setReadTimeout(1000 * timeOut);
			connection.setConnectTimeout(1000 * timeOut);

			// �����Ƿ�����������
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
	 * ���ز�������
	 * 
	 * @param str
	 *            ������ַ���
	 * @param regex
	 *            ������ʽ
	 * @return ��������ַ���������������ʽ�ĵ����ݣ����ޣ��򷵻�null
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
	 * ���ز��ҵ�����
	 * 
	 * @param str
	 *            ������ַ���
	 * @param regex
	 *            ������ʽ
	 * @param groupIndex
	 *            ��ʾ��Ҫ���ҵ������ݵĵڼ���
	 * @return ��������ַ���������������ʽ�ļ��������еĵ�groupIndex������ݣ����������򷵻�null
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
	 * �ж��Ƿ�Ϊ�����ַ�
	 * 
	 * @param c
	 *            ������ַ�
	 * @return true���������ַ���false�����������ַ�
	 */
	public static boolean isChinese(char c) {
		if (c >= 19968 && c <= 40869) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * �ж��Ƿ�Ϊ�����ַ�
	 * 
	 * @param c
	 *            ������ַ�
	 * @return true���������ַ���false�����������ַ�
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
	 * �����
	 * 
	 * @param srcStr
	 *            ԭ�ַ���
	 * @param padChar
	 *            ����ַ���
	 * @param length
	 *            ���λ��
	 * @return ����������ַ���
	 */
	public static String lpad(String srcStr, String padChar, int length) {
		StringBuffer str = new StringBuffer();
		str.append(padding(srcStr, padChar, length));
		str.append(srcStr);
		return str.toString();
	}

	/**
	 * ���
	 * 
	 * @param oldStr
	 *            ԭ�ַ���
	 * @param padChar
	 *            ����ַ���
	 * @param length
	 *            ���λ��
	 * @return ���������ַ���
	 */
	private static String padding(String oldStr, String padChar, int length) {
		StringBuffer str = new StringBuffer();
		for (int i = 0; i < length - oldStr.length(); i++) {
			str.append(padChar);
		}
		return str.toString();
	}

	/**
	 * �滻ȫ���ַ�Ϊ����ַ�
	 * 
	 * @param chars
	 *            ������ַ�����
	 */
	public static void replaceFullCharToHalfChar(char[] chars) {
		replaceFullCharToHalfChar(chars, 0, chars.length);
	}

	/**
	 * �滻ȫ���ַ�Ϊ����ַ�
	 * 
	 * @param chars
	 *            ������ַ�����
	 * @param beginIndex
	 *            ��ʼ�滻��λ��
	 * @param endIndex
	 *            �����滻��λ��
	 */
	public static void replaceFullCharToHalfChar(char[] chars, int beginIndex, int endIndex) {
		char c;
		for (int i = beginIndex; i < endIndex; i++) {
			c = chars[i];
			if (c >= 0xFF01 && c <= 0xFF5E) {
				chars[i] = (char) (c - 0xFEE0);
			} else if (c == '��') {
				chars[i] = ' ';
			}
		}
	}

	/**
	 * �滻ȫ���ַ�Ϊ����ַ�
	 * 
	 * @param str
	 *            �����ַ���
	 * @return ����������ַ����İ�Ǳ�ʾ
	 */
	public static String replaceFullCharToHalfChar(String str) {
		char[] chars = str.toCharArray();
		replaceFullCharToHalfChar(chars, 0, chars.length);
		return new String(chars);
	}

	/**
	 * �滻����ַ�Ϊȫ���ַ�
	 * 
	 * @param chars
	 *            ������ַ�����
	 */
	public static void replaceHalfCharToFullChar(char[] chars) {
		replaceHalfCharToFullChar(chars, 0, chars.length);
	}

	/**
	 * �滻����ַ�Ϊȫ���ַ�
	 * 
	 * @param chars
	 *            ������ַ�����
	 * @param beginIndex
	 *            ��ʼ�滻��λ��
	 * @param endIndex
	 *            �����滻��λ��
	 */
	public static void replaceHalfCharToFullChar(char[] chars, int beginIndex, int endIndex) {
		char c;
		for (int i = beginIndex; i < endIndex; i++) {
			c = chars[i];
			if (c >= 0x0021 && c <= 0x007E) {
				chars[i] = (char) (c + 0xFEE0);
			} else if (c == ' ') {
				chars[i] = '��';
			}
		}
	}

	/**
	 * �滻����ַ�Ϊȫ���ַ�
	 * 
	 * @param str
	 *            �����ַ���
	 * @return ����������ַ�����ȫ�Ǳ�ʾ
	 */
	public static String replaceHalfCharToFullChar(String str) {
		char[] chars = str.toCharArray();
		replaceHalfCharToFullChar(chars, 0, chars.length);
		return new String(chars);
	}

	/**
	 * �滻�ַ�����ȫ������Ϊ�������
	 * 
	 * @param sReturn
	 *            ԭ�ַ���
	 * @return �滻������ַ���
	 */
	public static String replaceNumber(String sReturn) {
		try {
			String str = "FDs";
			str = str.replaceAll("", "");
			sReturn = sReturn.replace('��', '0');
			sReturn = sReturn.replace('��', '1');
			sReturn = sReturn.replace('��', '2');
			sReturn = sReturn.replace('��', '3');
			sReturn = sReturn.replace('��', '4');
			sReturn = sReturn.replace('��', '5');
			sReturn = sReturn.replace('��', '6');
			sReturn = sReturn.replace('��', '7');
			sReturn = sReturn.replace('��', '8');
			sReturn = sReturn.replace('��', '9');
		}
		catch (Exception ex) {
			return sReturn;
		}
		return sReturn;
	}

	/**
	 * �����
	 * 
	 * @param srcStr
	 *            ԭ�ַ�
	 * @param padChar
	 *            ����ַ���
	 * @param length
	 *            ���λ��
	 * @return ����������ַ���
	 */
	public static String rpad(String srcStr, String padChar, int length) {
		StringBuffer str = new StringBuffer();
		str.append(srcStr);
		str.append(padding(srcStr, padChar, length));
		return str.toString();
	}

	/**
	 * ���������url��ȡ���ص����ݣ������浽�����ļ���Ĭ�ϳ�ʱʱ��Ϊ5��
	 * 
	 * @param fileName
	 *            ���ڱ���ı����ļ�
	 * @param url
	 *            url��ַ
	 * @throws Exception
	 *             �쳣��Ϣ
	 */
	public static void saveContentByUrl(String fileName, String url) throws Exception {
		saveContentByUrl(fileName, url, 5);
	}

	/**
	 * ���������url��ȡ���ص����ݣ������浽�����ļ�
	 * 
	 * @param fileName
	 *            ���ڱ���ı����ļ�
	 * @param url
	 *            url��ַ
	 * @param timeOut
	 *            ��ʱʱ�䣬��λΪ��
	 * @throws Exception
	 *             �쳣��Ϣ
	 */
	public static void saveContentByUrl(String fileName, String url, int timeOut) throws Exception {
		// ��ȡָ��url��ַ���ص�����
		byte[] contents = getContentByUrl(url, timeOut);
		// ����������ļ�·���Ƿ��Ѵ��ڣ��������򴴽�
		File parent = new File(fileName).getParentFile();
		if (parent != null && !parent.exists()) {
			parent.mkdirs();
		}
		// ����ȡ�������ݱ��浽�����ļ�
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
	 * �ָ��ַ��������ݵ����������ָ֧�ֶ�������
	 * 
	 * @param msg
	 *            ��Ҫ�ָ���ַ���
	 * @param symbol
	 *            ��Ϊ�ָ������ķ�����ϣ���"' ;"��
	 * @return �ָ����ַ�������
	 */
	public static String[] splitByChar(String msg, String symbol) {
		String regex = "[" + symbol + "]";
		return msg.split(regex);
	}

	/**
	 * �ָ��ַ����������Ӵ����ָ�
	 * 
	 * @param msg
	 *            ��Ҫ�ָ���ַ���
	 * @param symbol
	 *            ��Ϊ�ָ��������Ӵ�
	 * @return �ָ����ַ�������
	 */
	public static String[] splitByString(String msg, String symbol) {
		return msg.split(symbol);
	}

	/**
	 * �ַ���ת��������
	 * 
	 * @param number
	 *            �ַ���
	 * @return ��������
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
	 * �ַ���ת����������
	 * 
	 * @param number
	 *            �ַ���
	 * @return ����������
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
	 * �޳��ַ����еĿո񣬰���ǰ���С�������Կո��ַ�
	 * 
	 * @param msg
	 *            ԭ�ַ���
	 * @return �޳�����������ַ���
	 */
	public static String trimAll(String msg) {
		return msg.replaceAll(" ", "");
	}
}
