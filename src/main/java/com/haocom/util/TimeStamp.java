package com.haocom.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * ʱ���. <br>
 * ʱ����Ĺ��ܣ��ɻ�ȡ��ͬ�����ʱ��.
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

	/** ��ʾ��������� */
	public static final int DAY = Calendar.DAY_OF_MONTH;

	/** ��ʾ�������Сʱ */
	public static final int HOUR = Calendar.HOUR_OF_DAY;

	/** ��ʾ������ķ��� */
	public static final int MINUTE = Calendar.MINUTE;

	/** ��ʾ��������� */
	public static final int MONTH = Calendar.MONTH;

	/** ��ʾ��������� */
	public static final int SECOND = Calendar.SECOND;

	/** ��ʾ��������� */
	public static final int YEAR = Calendar.YEAR;

	/** ��ȡ8λʱ�䣺���������������գ���20080910�� */
	public static final int YYYYMMDD = 8;

	/** ��ȡ10λʱ�䣺����������������ʱʱ����2008091012�� */
	public static final int YYYYMMDDhh = 10;

	/** ��ȡ12λʱ�䣺����������������ʱʱ�ַ֣���200809101211�� */
	public static final int YYYYMMDDhhmm = 12;

	/** ��ȡ14λʱ�䣺����������������ʱʱ�ַ����루��200809101211�� */
	public static final int YYYYMMDDhhmmss = 14;

	/** ��ȡ17λʱ�䣺����������������ʱʱ�ַ�������������루��200809101211000�� */
	public static final int YYYYMMDDhhmmssxxx = 17;

	/**
	 * ��Calendar��ʱ��ת��Ϊָ����ʽ��ʱ���ַ���
	 * 
	 * @param time
	 *            ��Ҫת����Calendarʱ��
	 * @param format
	 *            ָ����ʽ�����ͣ���ʾʱ��λ��
	 * @return ָ����ʽ��ʱ���ַ���<BR>
	 *         ����ʹ��TimeStamp.Calendar2Str(calendar,
	 *         TimeStamp.YYYYMMDD)����ת���󷵻ص�ǰ8λʱ�䣨��20080917��
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

		/** ������ */
		buff.append(year);
		buff.append(getFormatTime(month, 2));
		buff.append(getFormatTime(day, 2));
		/** Сʱ */
		if (format >= 10) {
			buff.append(getFormatTime(hour, 2));
		}
		/** ���� */
		if (format >= 12) {
			buff.append(getFormatTime(minute, 2));
		}
		/** ���� */
		if (format >= 14) {
			buff.append(getFormatTime(second, 2));
		}
		/** ���� */
		if (format >= 17) {
			buff.append(getFormatTime(miltime, 3));
		}
		return buff.toString();
	}

	/**
	 * ��ʽ��ʱ��,��һ�ָ�ʽת��Ϊ��һ�ָ�ʽ
	 * 
	 * @param time
	 *            ��ת����ʱ��
	 * @param oldFormat
	 *            ��ת����ʱ��ĵ�ǰʱ���ʽ
	 * @param newFormat
	 *            ��ת��Ϊ���¸�ʽyyyymmddhhmmss
	 * @return ת����ʽ���ʱ���ַ���<BR>
	 *         ���統ǰʱ��Ϊ2006-6-29 15:30:30���ɸ�ʽΪyyyy-MM-dd
	 *         HH:mm:ss���¸�ʽΪyyyyMMddHHmmss��ת���󷵻��¸�ʽʱ��20060629153030
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
	 * ���������Ĺ���Ϊ�����������ֶ���ӻ��ȥָ����ʱ����
	 * 
	 * @param time
	 *            ����ʱ�����,��NULL���ʾ��ǰʱ��
	 * @param field
	 *            ʱ����ӻ��ȥԪ��
	 * @param amount
	 *            ʱ����ӻ��ȥʱ����
	 * @return ʱ����ӻ��ȥ�����������<BR>
	 *         ����ʹ��TimeStamp.getAddTime(null,TimeStamp.YEAR,1),�򷵻���һ��ĵ�ǰʱ��
	 */
	public static Calendar getAddTime(Calendar time, int field, int amount) {
		if (time == null) {
			time = Calendar.getInstance();
		}

		time.add(field, amount);
		return time;
	}

	/**
	 * ���������Ĺ���Ϊ�����������ֶ���ӻ��ȥָ����ʱ������
	 * 
	 * @param time
	 *            ����ʱ�����,��NULL���ʾ��ǰʱ��
	 * @param field
	 *            ʱ����ӻ��ȥԪ��
	 * @param amount
	 *            ʱ����ӻ��ȥʱ����
	 * @param format
	 *            ����ʱ���ַ����ĸ�ʽ
	 * @return ����ʱ���ַ���<BR>
	 *         
	 */
	public static String getAddTime(Calendar time, int field, int amount, int format) {
		if (time == null) {
			time = Calendar.getInstance();
		}

		/** add time */
		time.add(field, amount);

		/** ����Format������ʱ���ַ��� */
		return Calendar2Str(time, format);

	}

	/**
	 * ���ɶ�����ʱ��Ԫ�أ��¡��ա�ʱ��
	 * 
	 * @param time
	 *            ʱ��Ԫ��
	 * @param format
	 *            Ԫ�س���
	 * @return ����ʱ��Ԫ��
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
	 * ��ȡ��ǰ��ʽ���õ�ʱ��,ֻҪ��java֧�ֵĸ�ʽ������
	 * 
	 * @param Format
	 *            yyyy-MM-dd HH:mm:ss yyyyMMddHHmmss yyyyMMddHHmmssSSS
	 * @return ��ǰ��ʽ���õ�ʱ��<BR>
	 *         ����ʹ��TimeStamp.getFormatTime("yyyy-MM-dd
	 *         HH:mm:ss"),�򷵻ص�ǰʱ�䣺2008-09-17 11:58:03
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
	 * ����ָ��calendar��ȡ��ʽ���õ�ʱ��
	 * 
	 * @param field
	 *            ���Ա�ʾ��H(Сʱ)����m(����)��S(��)��D(��)��W(��)��M(��)��Y(��)
	 * @param n
	 *            ������ʱ��Ĵ���
	 * @param time
	 *            ָ��calendar
	 * @param format
	 *            ��λʱ���ʽ
	 * @return ָ��calendar��ȡ��ʽ���õ�ʱ��<BR>
	 *         ���統ǰʱ����20080917,ʹ��TimeStamp.getNFormatTime("M",1,calendar,TimeStamp.YYYYMMDD)���򷵻�20081017
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
	 * ����ϵͳʱ��õ�n��,Сʱ,��,��,��,���Ժ����ǰ������
	 * 
	 * @param field
	 *            ���Ա�ʾ��H(Сʱ)����m(����)��S(��)��D(��)��W(��)��M(��)��Y(��)
	 * @param n
	 *            ���ӻ����ֵ,����Ϊ���ӣ�����Ϊ����
	 * @param format
	 *            ʱ��ķ��ظ�ʽ
	 * @return ���صõ���ʱ���ַ���<BR>
	 *         ���統ǰϵͳʱ��Ϊ20080917130520ʹ��TimeStamp.getNSystemTime("Y",1,TimeStamp.YYYYMMDDhhmmss)�󷵻�20090917130520
	 */
	public synchronized static String getNSystemTime(String field, int n, int format) {
		String cTime = "";
		Calendar time = Calendar.getInstance();
		cTime = getNFormatTime(field, n, time, format);
		return cTime;
	}

	/**
	 * �����û�ָ��ʱ�䣬���ָ��ʱ��ǰ���n��,Сʱ,��,��,��,���Ժ����ǰ������
	 * 
	 * @param field
	 *            m��,HСʱ,D��,W����,M��,Y��
	 * @param n
	 *            ���ӻ����ֵ,����Ϊ���ӣ�����Ϊ����
	 * @param format
	 *            �����ʽ
	 * @param year
	 *            �û��趨��
	 * @param month
	 *            �û��趨��
	 * @param date
	 *            �û��趨��
	 * @param hour
	 *            �û��趨Сʱ
	 * @param minute
	 *            �û��趨����
	 * @return ָ��ʱ��ǰ���n��,Сʱ,��,��,��,���Ժ����ǰ������
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
	 * �����û�ָ��ʱ�䣬���ָ��ʱ��ǰ���n��,Сʱ,��,��,��,���Ժ����ǰ������
	 * 
	 * @param field
	 *            m��,HСʱ,D��,W����,M��,Y��
	 * @param n
	 *            ���ӻ����ֵ,����Ϊ���ӣ�����Ϊ����
	 * @param format
	 *            ���ʱ���ʽ ���ͣ���ʾʱ��λ��
	 * @param UserTime
	 *            �û��趨ʱ��
	 * @param UserTimeFormat
	 *            �û��趨ʱ���ʽ
	 * @return ָ��ʱ��ǰ���n��,Сʱ,��,��,��,���Ժ����ǰ������<BR>
	 *         ʹ��TimeStamp.getNUserTime("Y",1,TimeStamp.YYYYMMDD,"20080917","yyyyMMdd"),����20090917
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
	 * ���������ʽ��ʱ���ַ���
	 * 
	 * @param format
	 *            ʱ���ʽ ���ͣ�����ʱ��λ��
	 * @return ʱ���ַ���<BR>
	 *         ����ʹ��TimeStamp.getTime(TimeStamp.YYYYMMDD),�򷵻�20080917
	 */
	public static String getTime(int format) {
		Calendar cal = Calendar.getInstance();
		return Calendar2Str(cal, format);
	}

	/**
	 * ���ַ�����ʾ��ʱ��ת��Ϊ��������
	 * 
	 * @param time
	 *            ָ��ʱ����ַ���,��ʽΪyyyyMMddHHmmssSSS���ɽ�Ϊ���м�λ
	 * @return ת������������
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
