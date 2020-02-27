package com.haocom.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * WAPPushת���Ÿ�ʽ. <br>
 * WAPPushתΪ���Ÿ�ʽ.
 * <p>
 * Copyright: Copyright (c) Sep 16, 2008
 * <p>
 * Company: 
 * <p>
 * Author: ChengFan
 * <p>
 * Version: 1.0
 */

public class WAPPush {

	/** WAPPush���ŷֶγ��� */
	public static int pageSize = 170;

	/** WAPPush��Ϣ��Чʱ�䳤�� */
	public static int timeLength = 14;

	/**
	 * ���ƶ����⡢��ַ��WAPPushתΪ�ֶεĶ�������
	 * 
	 * @param title
	 *            ����
	 * @param url
	 *            URL
	 * @return �ֶεĶ�������
	 */
	public static String[] encode(String title, String url) {
		Calendar cal = Calendar.getInstance();
		String starttime = TimeStamp.Calendar2Str(cal, 14);
		cal.add(Calendar.MINUTE, timeLength);
		String endtime = TimeStamp.Calendar2Str(cal, 14);
		return encode(title, url, starttime, endtime);
	}

	/**
	 * ���ƶ����⡢��ַ��WAPPushתΪ�ֶεĶ������ݣ������ÿ�ʼ������ʱ��
	 * 
	 * @param title
	 *            ����
	 * @param url
	 *            URL
	 * @param starttime
	 *            ��ʼʱ��
	 * @param endtime
	 *            ����ʱ��
	 * @return �ֶεĶ�������
	 */
	public static String[] encode(String title, String url, String starttime, String endtime) {
		url = ("" + url).trim();
		if (url.toLowerCase().startsWith("http://")) {
			url = url.substring(7).trim();
		}
		String pushString = "";
		String body = "";
		body += "02";
		body += "05"; // -//WAPFORUM//DTD SI 1.0//EN
		body += "6A"; // UTF-8
		body += "00"; // �ַ�������
		body += "45"; // <si>
		body += "C6"; // <indication
		body += "08"; // <action=signal-high>
		body += "0C"; // href="http://
		body += "03"; // �ַ�����ʼ
		body += CByte.bytes2Hex(url.getBytes()); // ʵ�ʵ�ַ
		body += "00"; // �ַ�������
		body += "0A"; // created=
		body += "C3"; // 'ʱ��
		body += "07"; // ʱ���ֽ���
		body += starttime; // YYYYMMDDHHMMSS
		body += "10"; // si_expires=
		body += "C3"; // ʱ��
		body += "07"; // ʱ���ֽ���
		body += endtime; // YYYYMMDDHHMMSS
		body += "01"; // >
		body += "03"; // �ַ�����ʼ
		try {
			body += CByte.bytes2Hex(title.getBytes("UTF-8")); // ��ʾ���û������ݣ���utf-8���롣utf-8���룬Ӣ���ַ�ֱ����ascii�룻�������unicode�ǣ������ƣ�
		}
		catch (Exception ex) {
		}
		body += "00"; // �ַ�������
		body += "01"; // </indication>"
		body += "01"; // '</si>
		int length = body.length();
		String pud = "";
		pud += "81"; // transaction id (connectionless WSP)
		pud += "06"; // 'pdu type (06=push)
		pud += "06"; // Headers len
		pud += "03";
		pud += "AE";
		pud += "81";
		pud += "EA"; // content type: application/vnd.wap.sic; charset=utf-8
		pud += "8D"; // content-length
		pud += Integer.toHexString(length).toUpperCase();
		// pushString = pud + body;
		pushString = "B30601AE" + body;

		String udh = "";
		udh += "0B"; // User Data Header Length
		udh += "05"; // UDH IE: Port numbers
		udh += "04"; // Port number length
		udh += "0B"; // Destination port (high)
		udh += "84"; // Destination port (low)
		udh += "23"; // Originating port (high)
		udh += "F0"; // Originating port (low)
		udh += "00"; // UDH IE SAR
		udh += "03"; // UDH SAR IE length
		udh += "84"; // Data gram ref number
		// udh += "01"; // Total number of segments in data gram
		// udh += "01"; // Segment count

		// ��pushData����pageSize�ĳ��ȷָ�
		List<String> vtr = new ArrayList<String>();
		Matcher m = Pattern.compile("(\\w{1," + pageSize + "})").matcher(pushString);
		while (m.find()) {
			vtr.add(m.group());
		}
		udh += ((vtr.size() + 100) + "").substring(1, 3);
		for (int i = 0; i < vtr.size(); i++) {
			String str = vtr.get(i).toString();
			String index = ((i + 1 + 100) + "").substring(1, 3);
			str = udh + index + str;
			vtr.set(i, str);
		}
		String[] result = new String[vtr.size()];
		for (int i = 0; i < vtr.size(); i++) {
			result[i] = vtr.get(i).toString();
		}
		return result;
	}

}
