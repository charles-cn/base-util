package com.haocom.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间戳. <br>
 * 时间戳的功能，可获取不同需求的时间.
 * <p>
 * Copyright: Copyright (c) Sep 16, 2008
 * <p>
 * Company: 
 * <p>
 * Author: dingwy
 * <p>
 * Version: 1.0
 */

public class TimeStamp {

	/** 表示日历里的天 */
	public static final int DAY = Calendar.DAY_OF_MONTH;

	/** 表示日历里的小时 */
	public static final int HOUR = Calendar.HOUR_OF_DAY;

	/** 表示日历里的分钟 */
	public static final int MINUTE = Calendar.MINUTE;

	/** 表示日历里的月 */
	public static final int MONTH = Calendar.MONTH;

	/** 表示日历里的秒 */
	public static final int SECOND = Calendar.SECOND;

	/** 表示日历里的年 */
	public static final int YEAR = Calendar.YEAR;

	/** 获取8位时间：年年年年月月日日（如20080910） */
	public static final int YYYYMMDD = 8;

	/** 获取10位时间：年年年年月月日日时时（如2008091012） */
	public static final int YYYYMMDDhh = 10;

	/** 获取12位时间：年年年年月月日日时时分分（如200809101211） */
	public static final int YYYYMMDDhhmm = 12;

	/** 获取14位时间：年年年年月月日日时时分分秒秒（如200809101211） */
	public static final int YYYYMMDDhhmmss = 14;

	/** 获取17位时间：年年年年月月日日时时分分秒秒毫秒毫秒毫秒（如200809101211000） */
	public static final int YYYYMMDDhhmmssxxx = 17;

	/**
	 * 将Calendar的时间转化为指定格式的时间字符串
	 * 
	 * @param time
	 *            需要转化的Calendar时间
	 * @param format
	 *            指定格式，整型，表示时间位数
	 * @return 指定格式的时间字符串<BR>
	 *         例如使用TimeStamp.Calendar2Str(calendar,
	 *         TimeStamp.YYYYMMDD)，则转化后返回当前8位时间（如20080917）
	 */
	public static String Calendar2Str(Calendar time, int format) {
		StringBuffer buff = new StringBuffer(20);
		int miltime = time.get(Calendar.MILLISECOND);
		int second = time.get(Calendar.SECOND);
		int minute = time.get(Calendar.MINUTE);
		int hour = time.get(Calendar.HOUR_OF_DAY);
		int day = time.get(Calendar.DAY_OF_MONTH);
		int month = time.get(Calendar.MONTH) + 1;
		int year = time.get(Calendar.YEAR);

		/** 年月日 */
		buff.append(year);
		buff.append(getFormatTime(month, 2));
		buff.append(getFormatTime(day, 2));
		/** 小时 */
		if (format >= 10) {
			buff.append(getFormatTime(hour, 2));
		}
		/** 分钟 */
		if (format >= 12) {
			buff.append(getFormatTime(minute, 2));
		}
		/** 秒种 */
		if (format >= 14) {
			buff.append(getFormatTime(second, 2));
		}
		/** 毫秒 */
		if (format >= 17) {
			buff.append(getFormatTime(miltime, 3));
		}
		return buff.toString();
	}

	/**
	 * 格式化时间,从一种格式转变为另一种格式
	 * 
	 * @param time
	 *            需转化的时间
	 * @param oldFormat
	 *            需转化的时间的当前时间格式
	 * @param newFormat
	 *            需转化为的新格式yyyymmddhhmmss
	 * @return 转化格式后的时间字符串<BR>
	 *         例如当前时间为2006-6-29 15:30:30，旧格式为yyyy-MM-dd
	 *         HH:mm:ss，新格式为yyyyMMddHHmmss，转化后返回新格式时间20060629153030
	 * @throws Exception
	 */
	public static String FormatTime(String time, String oldFormat, String newFormat) throws Exception {
		String strTime = null;
		try {
			SimpleDateFormat sdf = new java.text.SimpleDateFormat(oldFormat);
			Date date = sdf.parse(time);
			sdf = new java.text.SimpleDateFormat(newFormat);
			strTime = sdf.format(date);
		}
		catch (Exception e) {
			throw e;
		}
		return strTime;
	}

	/**
	 * 根据日历的规则，为给定的日历字段添加或减去指定的时间量
	 * 
	 * @param time
	 *            日历时间对象,填NULL则表示当前时间
	 * @param field
	 *            时间添加或减去元素
	 * @param amount
	 *            时间添加或减去时间量
	 * @return 时间添加或减去后的日历对象<BR>
	 *         例如使用TimeStamp.getAddTime(null,TimeStamp.YEAR,1),则返回下一年的当前时间
	 */
	public static Calendar getAddTime(Calendar time, int field, int amount) {
		if (time == null) {
			time = Calendar.getInstance();
		}

		time.add(field, amount);
		return time;
	}

	/**
	 * 根据日历的规则，为给定的日历字段添加或减去指定的时间量。
	 * 
	 * @param time
	 *            日历时间对象,填NULL则表示当前时间
	 * @param field
	 *            时间添加或减去元素
	 * @param amount
	 *            时间添加或减去时间量
	 * @param format
	 *            返回时间字符串的格式
	 * @return 返回时间字符串<BR>
	 *         
	 */
	public static String getAddTime(Calendar time, int field, int amount, int format) {
		if (time == null) {
			time = Calendar.getInstance();
		}

		/** add time */
		time.add(field, amount);

		/** 根据Format，返回时间字符串 */
		return Calendar2Str(time, format);

	}

	/**
	 * 生成定长的时间元素（月、日、时）
	 * 
	 * @param time
	 *            时间元素
	 * @param format
	 *            元素长度
	 * @return 定长时间元素
	 */
	private static String getFormatTime(int time, int format) {
		StringBuffer buff = new StringBuffer();
		int length = String.valueOf(time).length();
		if (length > format) {
			return "00";
		}

		for (int i = 0; i < format - length; i++) {
			buff.append("0");
		}
		buff.append(time);
		return buff.toString();
	}

	/**
	 * 获取当前格式化好的时间,只要是java支持的格式都可以
	 * 
	 * @param Format
	 *            yyyy-MM-dd HH:mm:ss yyyyMMddHHmmss yyyyMMddHHmmssSSS
	 * @return 当前格式化好的时间<BR>
	 *         例如使用TimeStamp.getFormatTime("yyyy-MM-dd
	 *         HH:mm:ss"),则返回当前时间：2008-09-17 11:58:03
	 * @throws Exception
	 */
	public static String getFormatTime(String Format) throws Exception {
		String strTime = null;
		try {
			SimpleDateFormat sdf = new java.text.SimpleDateFormat(Format);
			Date date = new Date();
			strTime = sdf.format(date);
		}
		catch (Exception e) {
			throw e;
		}
		return strTime;
	}

	/**
	 * 根据指定calendar获取格式化好的时间
	 * 
	 * @param field
	 *            用以表示是H(小时)还是m(分钟)，S(秒)，D(天)，W(周)，M(月)，Y(年)
	 * @param n
	 *            被加入时间的次数
	 * @param time
	 *            指定calendar
	 * @param format
	 *            几位时间格式
	 * @return 指定calendar获取格式化好的时间<BR>
	 *         例如当前时间是20080917,使用TimeStamp.getNFormatTime("M",1,calendar,TimeStamp.YYYYMMDD)后则返回20081017
	 */
	public synchronized static String getNFormatTime(String field, int n, Calendar time, int format) {
		String cTime = "";
		if (field != null && field.equals("S")) {
			time.add(Calendar.SECOND, n);
		}
		if (field != null && field.equals("m")) {
			time.add(Calendar.MINUTE, n);
		}
		if (field != null && field.equals("H")) {
			time.add(Calendar.HOUR, n);
		}
		if (field != null && field.equals("D")) {
			time.add(Calendar.DATE, n);
		}
		if (field != null && field.equals("W")) {
			time.add(Calendar.WEEK_OF_YEAR, n);
		}
		if (field != null && field.equals("M")) {
			time.add(Calendar.MONTH, n);
		}
		if (field != null && field.equals("Y")) {
			time.add(Calendar.YEAR, n);
		}
		cTime = Calendar2Str(time, format);
		return cTime;
	}

	/**
	 * 根据系统时间得到n分,小时,天,周,月,年以后或以前的日期
	 * 
	 * @param field
	 *            用以表示是H(小时)还是m(分钟)，S(秒)，D(天)，W(周)，M(月)，Y(年)
	 * @param n
	 *            增加或减少值,正数为增加，负数为减少
	 * @param format
	 *            时间的返回格式
	 * @return 返回得到的时间字符串<BR>
	 *         例如当前系统时间为20080917130520使用TimeStamp.getNSystemTime("Y",1,TimeStamp.YYYYMMDDhhmmss)后返回20090917130520
	 */
	public synchronized static String getNSystemTime(String field, int n, int format) {
		String cTime = "";
		Calendar time = Calendar.getInstance();
		cTime = getNFormatTime(field, n, time, format);
		return cTime;
	}

	/**
	 * 根据用户指定时间，获得指定时间前或后n分,小时,天,周,月,年以后或以前的日期
	 * 
	 * @param field
	 *            m分,H小时,D天,W星期,M月,Y年
	 * @param n
	 *            增加或减少值,正数为增加，负数为减少
	 * @param format
	 *            输出格式
	 * @param year
	 *            用户设定年
	 * @param month
	 *            用户设定月
	 * @param date
	 *            用户设定日
	 * @param hour
	 *            用户设定小时
	 * @param minute
	 *            用户设定分钟
	 * @return 指定时间前或后n分,小时,天,周,月,年以后或以前的日期
	 * @throws Exception
	 */
	public synchronized static String getNUserTime(String field, int n, int format, int year, int month, int date, int hour, int minute)
	        throws Exception {
		String cTime = "";
		Calendar time = Calendar.getInstance();
		time.set(year, month - 1, date, hour, minute);
		cTime = getNFormatTime(field, n, time, format);
		return cTime;
	}

	/**
	 * 根据用户指定时间，获得指定时间前或后n分,小时,天,周,月,年以后或以前的日期
	 * 
	 * @param field
	 *            m分,H小时,D天,W星期,M月,Y年
	 * @param n
	 *            增加或减少值,正数为增加，负数为减少
	 * @param format
	 *            输出时间格式 整型，表示时间位数
	 * @param UserTime
	 *            用户设定时间
	 * @param UserTimeFormat
	 *            用户设定时间格式
	 * @return 指定时间前或后n分,小时,天,周,月,年以后或以前的日期<BR>
	 *         使用TimeStamp.getNUserTime("Y",1,TimeStamp.YYYYMMDD,"20080917","yyyyMMdd"),返回20090917
	 * @throws Exception
	 */
	public synchronized static String getNUserTime(String field, int n, int format, String UserTime, String UserTimeFormat) throws Exception {
		SimpleDateFormat sdf = new java.text.SimpleDateFormat(UserTimeFormat);
		Date date = sdf.parse(UserTime);
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return getNFormatTime(field, n, cal, format);
	}

	/**
	 * 生成任意格式的时间字符串
	 * 
	 * @param format
	 *            时间格式 整型，代表时间位数
	 * @return 时间字符串<BR>
	 *         例如使用TimeStamp.getTime(TimeStamp.YYYYMMDD),则返回20080917
	 */
	public static String getTime(int format) {
		Calendar cal = Calendar.getInstance();
		return Calendar2Str(cal, format);
	}

	/**
	 * 将字符串表示的时间转化为日历对象
	 * 
	 * @param time
	 *            指定时间的字符串,格式为yyyyMMddHHmmssSSS，可仅为其中几位
	 * @return 转换的日历对象
	 */
	public static Calendar Str2Calendar(String time) {
		if (time == null) {
			return null;
		}

		Calendar cal = Calendar.getInstance();
		cal.set(2000, 1, 1, 0, 0, 0);
		if (time.length() >= 4) {
			cal.set(Calendar.YEAR, Integer.parseInt(time.substring(0, 4)));
		}
		if (time.length() >= 6) {
			cal.set(Calendar.MONTH, Integer.parseInt(time.substring(4, 6)) - 1);
		}
		if (time.length() >= 8) {
			cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(time.substring(6, 8)));
		}
		if (time.length() >= 10) {
			cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(time.substring(8, 10)));
		}
		if (time.length() >= 12) {
			cal.set(Calendar.MINUTE, Integer.parseInt(time.substring(10, 12)));
		}
		if (time.length() >= 14) {
			cal.set(Calendar.SECOND, Integer.parseInt(time.substring(12, 14)));
		}
		if (time.length() >= 17) {
			cal.set(Calendar.MILLISECOND, Integer.parseInt(time.substring(14, 17)));
		}
		return cal;
	}
}
