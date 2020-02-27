package com.haocom.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * WAPPush转短信格式. <br>
 * WAPPush转为短信格式.
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

	/** WAPPush短信分段长度 */
	public static int pageSize = 170;

	/** WAPPush信息有效时间长度 */
	public static int timeLength = 14;

	/**
	 * 将制定标题、网址的WAPPush转为分段的短信内容
	 * 
	 * @param title
	 *            标题
	 * @param url
	 *            URL
	 * @return 分段的短信内容
	 */
	public static String[] encode(String title, String url) {
		Calendar cal = Calendar.getInstance();
		String starttime = TimeStamp.Calendar2Str(cal, 14);
		cal.add(Calendar.MINUTE, timeLength);
		String endtime = TimeStamp.Calendar2Str(cal, 14);
		return encode(title, url, starttime, endtime);
	}

	/**
	 * 将制定标题、网址的WAPPush转为分段的短信内容，并设置开始、结束时间
	 * 
	 * @param title
	 *            标题
	 * @param url
	 *            URL
	 * @param starttime
	 *            开始时间
	 * @param endtime
	 *            结束时间
	 * @return 分段的短信内容
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
		body += "00"; // 字符串结束
		body += "45"; // <si>
		body += "C6"; // <indication
		body += "08"; // <action=signal-high>
		body += "0C"; // href="http://
		body += "03"; // 字符串开始
		body += CByte.bytes2Hex(url.getBytes()); // 实际地址
		body += "00"; // 字符串结束
		body += "0A"; // created=
		body += "C3"; // '时间
		body += "07"; // 时间字节数
		body += starttime; // YYYYMMDDHHMMSS
		body += "10"; // si_expires=
		body += "C3"; // 时间
		body += "07"; // 时间字节数
		body += endtime; // YYYYMMDDHHMMSS
		body += "01"; // >
		body += "03"; // 字符串开始
		try {
			body += CByte.bytes2Hex(title.getBytes("UTF-8")); // 显示给用户的内容，用utf-8编码。utf-8编码，英文字符直接用ascii码；中文如果unicode是（二进制）
		}
		catch (Exception ex) {
		}
		body += "00"; // 字符串结束
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

		// 将pushData按照pageSize的长度分割
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
