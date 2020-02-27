package com.haocom.util.lunar;

import java.util.Calendar;
import java.util.Date;

/**
 * ũ����. <br>
 * ʵ�ֶ�ũ���Ļ�ȡ������������������ڣ���ȡ��Ӧ��ũ���������Լ���ɵ�֧��.
 * <p>
 * Copyright: Copyright (c) 2009-7-24
 * <p>
 * Company: 
 * <p>
 * Author: ZhouYan
 * <p>
 * Version: 1.0
 * <p>
 * <h2>���ʹ��˵��:</h2>
 * <ul>
 * <li>����ṩ�ķ�����ʹ�ã�</li>
 * 
 * <pre>
 * Lunar solarDate = new Lunar(&quot;19010219&quot;);
 * System.out.println(&quot;����:		&quot; + solarDate.getSolarYear() + &quot;,&quot; + solarDate.getSolarMonth() + &quot;,&quot; + solarDate.getSolarDay());
 * System.out.println(&quot;ũ��:		&quot; + solarDate.getLunarYear() + &quot;,&quot; + solarDate.getLunarMonth() + &quot;,&quot; + solarDate.getLunarDay());
 * System.out.println(&quot;ũ��:		&quot; + solarDate.getLunarYear() + &quot;,&quot; + solarDate.getLunarMonthCn() + &quot;,&quot; + solarDate.getLunarDayCn());
 * System.out.println(&quot;��֧:		&quot; + solarDate.getGzYear() + &quot;��,&quot; + solarDate.getGzMonth() + &quot;��,&quot; + solarDate.getGzDay() + &quot;��&quot;);
 * System.out.println(&quot;��Ф:		&quot; + solarDate.getAnimalYear());
 * System.out.println(&quot;����(��):       &quot; + solarDate.getCnWeek());
 * System.out.println(&quot;����(Ӣ):       &quot; + solarDate.getEnWeek());
 * System.out.println(&quot;����:		&quot; + solarDate.getSoralTerm());
 * System.out.println(&quot;���գ�	         &quot; + solarDate.getLunarFestival());
 * if (solarDate.isLeapYear()) {
 * 	System.out.println(&quot;�Ƿ����꣺	����&quot;);
 * } else {
 * 	System.out.println(&quot;�Ƿ����꣺	��������&quot;);
 * }
 * System.out.println(&quot;toLunarCalday:	&quot; + solarDate.toLunarCalday());
 * System.out.println(&quot;toString:	&quot; + solarDate.toString());
 * </pre>
 * 
 * <li>����̨������ݣ����Ӧ�����Ĵ����˽��������������</li>
 * 
 * <pre>
 * ����:		1901,2,19
 * ũ��:		1901,1,1
 * ũ��:		1901,����,��һ
 * ��֧:		������,������,�쳽��
 * ��Ф:		ţ
 * ����(��):	       ���ڶ�
 * ����(Ӣ):	       Tuesday
 * ����:		��ˮ
 * ���գ�		 ����
 * �Ƿ����꣺	       ��������
 * toLunarCalday:	 ũ�����³�һ,������,������,�쳽��,��ˮ
 * toString:	 ����ʱ��1901��2��19��,���ڶ�
 * </pre>
 * 
 * </ul>
 */
public class Lunar {

	/** ��Ф */
	private static final String[] Animals = { "��", "ţ", "��", "��", "��", "��", "��", "��", "��", "��", "��", "��" };

	/** �·� */
	private static final String[] CnMonth = new String[] { "����", "����", "����", "����", "����", "����", "����", "����", "����", "ʮ��", "����", "����" };

	/** ��� */
	private static final String[] Gan = new String[] { "��", "��", "��", "��", "��", "��", "��", "��", "��", "��" };

	/** ũ������ */
	private static final String[] LunarFtv = { "0101����", "0115Ԫ����", "0505�����", "0707��Ϧ���˽�", "0715��Ԫ��", "0815�����", "0909������", "1208���˽�", "1223С��",
	        "1230��Ϧ" };

	/** ����ũ��ʹ�õ�ƫ���� */
	private static final long[] lunarInfo = new long[] { 0x04bd8, 0x04ae0, 0x0a570, 0x054d5, 0x0d260, 0x0d950, 0x16554, 0x056a0, 0x09ad0, 0x055d2,
	        0x04ae0, 0x0a5b6, 0x0a4d0, 0x0d250, 0x1d255, 0x0b540, 0x0d6a0, 0x0ada2, 0x095b0, 0x14977, 0x04970, 0x0a4b0, 0x0b4b5, 0x06a50, 0x06d40,
	        0x1ab54, 0x02b60, 0x09570, 0x052f2, 0x04970, 0x06566, 0x0d4a0, 0x0ea50, 0x06e95, 0x05ad0, 0x02b60, 0x186e3, 0x092e0, 0x1c8d7, 0x0c950,
	        0x0d4a0, 0x1d8a6, 0x0b550, 0x056a0, 0x1a5b4, 0x025d0, 0x092d0, 0x0d2b2, 0x0a950, 0x0b557, 0x06ca0, 0x0b550, 0x15355, 0x04da0, 0x0a5d0,
	        0x14573, 0x052d0, 0x0a9a8, 0x0e950, 0x06aa0, 0x0aea6, 0x0ab50, 0x04b60, 0x0aae4, 0x0a570, 0x05260, 0x0f263, 0x0d950, 0x05b57, 0x056a0,
	        0x096d0, 0x04dd5, 0x04ad0, 0x0a4d0, 0x0d4d4, 0x0d250, 0x0d558, 0x0b540, 0x0b5a0, 0x195a6, 0x095b0, 0x049b0, 0x0a974, 0x0a4b0, 0x0b27a,
	        0x06a50, 0x06d40, 0x0af46, 0x0ab60, 0x09570, 0x04af5, 0x04970, 0x064b0, 0x074a3, 0x0ea50, 0x06b58, 0x055c0, 0x0ab60, 0x096d5, 0x092e0,
	        0x0c960, 0x0d954, 0x0d4a0, 0x0da50, 0x07552, 0x056a0, 0x0abb7, 0x025d0, 0x092d0, 0x0cab5, 0x0a950, 0x0b4a0, 0x0baa4, 0x0ad50, 0x055d9,
	        0x04ba0, 0x0a5b0, 0x15176, 0x052b0, 0x0a930, 0x07954, 0x06aa0, 0x0ad50, 0x05b52, 0x04b60, 0x0a6e6, 0x0a4e0, 0x0d260, 0x0ea65, 0x0d530,
	        0x05aa0, 0x076a3, 0x096d0, 0x04bd7, 0x04ad0, 0x0a4d0, 0x1d0b6, 0x0d250, 0x0d520, 0x0dd45, 0x0b5a0, 0x056d0, 0x055b2, 0x049b0, 0x0a577,
	        0x0a4b0, 0x0aa50, 0x1b255, 0x06d20, 0x0ada0 };

	/** �������� */
	private static final String[] sCnWeek = { "������", "����һ", "���ڶ�", "������", "������", "������", "������" };

	/** ���� */
	private static final String[] SolarTerm = new String[] { "С��", "��", "����", "��ˮ", "����", "����", "����", "����", "����", "С��", "â��", "����", "С��", "����",
	        "����", "����", "��¶", "���", "��¶", "˪��", "����", "Сѩ", "��ѩ", "����" };

	/** ����ƫ���� */
	private static final long[] STermInfo = new long[] { 0, 21208, 42467, 63836, 85337, 107014, 128867, 150921, 173149, 195551, 218072, 240693,
	        263343, 285989, 308563, 331033, 353350, 375494, 397447, 419210, 440795, 462224, 483532, 504758 };

	/** Ӣ������ */
	private static String[] sWeek = { "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday" };

	/** ��֧ */
	private static String[] Zhi = new String[] { "��", "��", "��", "î", "��", "��", "��", "δ", "��", "��", "��", "��" };

	/** ���� */
	private Calendar calendar = Calendar.getInstance();

	/** ũ���� ��ɵ�֧��ʾ */
	private String gzDay;

	/** ũ���� ��ɵ�֧��ʾ */
	private String gzMonth;

	/** ũ���� ��ɵ�֧��ʾ */
	private String gzYear;

	/** �Ƿ�Ϊũ������ */
	private boolean isLunarLeapMonth;

	/** ũ�� �� */
	private int lunarDay;

	/** ũ�� �� */
	private int lunarMonth;

	/** ũ�� �� */
	private int lunarYear;

	/** ���� �� */
	private int solarDay;

	/** ���� �� */
	private int solarMonth;

	/** ���� �� */
	private int solarYear;

	/**
	 * ���췽������ʼ�������գ�Ĭ��Ϊ��ǰ��������.
	 */
	public Lunar() {
		this.solarYear = calendar.get(Calendar.YEAR);
		this.solarMonth = calendar.get(Calendar.MONTH) + 1;
		this.solarDay = calendar.get(Calendar.DAY_OF_MONTH);
		init();
	}

	/**
	 * ���췽������ʼ��������.
	 * 
	 * @param cal
	 *            Calendar����
	 * @throws Exception
	 */
	public Lunar(Calendar cal) throws Exception {
		this.solarYear = cal.get(Calendar.YEAR);
		this.solarMonth = cal.get(Calendar.MONTH) + 1;
		this.solarDay = checkDay(cal.get(Calendar.DAY_OF_MONTH));
		init();
	}

	/**
	 * ���췽������ʼ��������.
	 * 
	 * @param date
	 *            Date����
	 * @throws Exception
	 */
	@SuppressWarnings("deprecation")
	public Lunar(Date date) throws Exception {
		this.solarYear = date.getYear() + 1900;
		this.solarMonth = date.getMonth() + 1;
		this.solarDay = checkDay(date.getDate());
		init();
	}

	/**
	 * ���췽������ʼ���꣬Ĭ����Ϊ��ǰ�£�Ĭ����Ϊ��ǰ��.
	 * 
	 * @param year
	 *            ������
	 * @throws Exception
	 */
	public Lunar(int year) throws Exception {
		this.solarYear = checkYear(year);
		this.solarMonth = calendar.get(Calendar.MONTH) + 1;
		this.solarDay = calendar.get(Calendar.DAY_OF_MONTH);
		init();
	}

	/**
	 * ���췽������ʼ�����£�Ĭ����Ϊ��ǰ��.
	 * 
	 * @param year
	 *            ������
	 * @param month
	 *            ������
	 * @throws Exception
	 */
	public Lunar(int year, int month) throws Exception {
		this.solarYear = checkYear(year);
		this.solarMonth = checkMonth(month);
		this.solarDay = calendar.get(Calendar.DAY_OF_MONTH);
		init();
	}

	/**
	 * ���췽������ʼ��������.
	 * 
	 * @param year
	 *            ������
	 * @param month
	 *            ������
	 * @param day
	 *            ������
	 * @throws Exception
	 */
	public Lunar(int year, int month, int day) throws Exception {
		this.solarYear = checkYear(year);
		this.solarMonth = checkMonth(month);
		this.solarDay = checkDay(day);
		init();
	}

	/**
	 * ���췽������ʼ��������.
	 * 
	 * @param date
	 *            8λ�ַ�������ʱ�䣬�硰20090806��
	 */
	public Lunar(String date) throws Exception {
		this.solarYear = checkYear(Integer.parseInt(date.substring(0, 4)));
		this.solarMonth = checkMonth(Integer.parseInt(date.substring(4, 6)));
		this.solarDay = checkDay(Integer.parseInt(date.substring(6, 8)));
		init();
	}

	/**
	 * ����year��month��day�ն�Ӧ��ũ��.<BR>
	 * int[0]=���ڼ���ũ�������ɵ�֧<BR>
	 * int[1]=ũ�����·�<BR>
	 * int[2]=ũ������<BR>
	 * int[3]=���ڼ���ũ���µ���ɵ�֧<BR>
	 * int[4]=���ڼ���ũ�������ɵ�֧<BR>
	 * int[5]=isLeap �Ƿ�Ϊ���� 1�� 0����<BR>
	 * 
	 * @param year
	 *            ��
	 * @param month
	 *            ��
	 * @param day
	 *            ��
	 * @return year��month��day�ն�Ӧ��ũ��
	 */
	@SuppressWarnings("deprecation")
	private int[] calElement(int year, int month, int day) {
		int[] nongDate = new int[6];
		int i = 0, temp = 0, leap = 0;
		Date baseDate = new Date(0, 0, 31);
		Date objDate = new Date(year - 1900, month - 1, day);
		int offset = (int) ((objDate.getTime() - baseDate.getTime()) / 86400000L);
		nongDate[4] = offset + 40;
		nongDate[3] = 14;
		for (i = 1900; i < 2050 && offset > 0; i++) {
			temp = lunarYearDays(i);
			offset -= temp;
			nongDate[3] += 12;
		}
		if (offset < 0) {
			offset += temp;
			i--;
			nongDate[3] -= 12;
		}
		nongDate[0] = i;
		leap = leapMonth(i); // ���ĸ���
		nongDate[5] = 0;
		for (i = 1; i < 13 && offset > 0; i++) {
			// ����
			if (leap > 0 && i == (leap + 1) && nongDate[5] == 0) {
				--i;
				nongDate[5] = 1;
				temp = leapDays(nongDate[0]);
			} else {
				temp = monthDays(nongDate[0], i);
			}
			// �������
			if (nongDate[5] == 1 && i == (leap + 1))
				nongDate[5] = 0;
			offset -= temp;
			if (nongDate[5] == 0)
				nongDate[3]++;
		}
		if (offset == 0 && leap > 0 && i == leap + 1) {
			if (nongDate[5] == 1) {
				nongDate[5] = 0;
			} else {
				nongDate[5] = 1;
				--i;
				--nongDate[3];
			}
		}
		if (offset < 0) {
			offset += temp;
			--i;
			--nongDate[3];
		}
		nongDate[1] = i;
		nongDate[2] = offset + 1;
		return nongDate;
	}

	/**
	 * ������Ƿ���1-�������һ��֮��.
	 * 
	 * @param day
	 *            ��
	 * @return ����������������򷵻ش��գ���֮�׳��쳣
	 * @throws Exception
	 */
	private int checkDay(int day) throws Exception {
		if (solarYear == 1900 && solarMonth == 1 && day < 31) {
			throw new Exception("Lunar Date start from 1900-01-31");
		}
		if ((day > 0) && (day <= getSolarYearMonthDays())) {
			return day;
		} else {
			throw new Exception("The Day out of range, Day should be in [1-" + getSolarYearMonthDays() + "]");
		}
	}

	/**
	 * ����·��Ƿ���1-12֮��.
	 * 
	 * @param month
	 *            ��
	 * @return ���������1-12֮�䣬�򷵻ش��꣬��֮�׳��쳣
	 * @throws Exception
	 */
	private int checkMonth(int month) throws Exception {
		if ((month > 0) && (month < 13))
			return month;
		else {
			throw new Exception("The Month out of range, Month should be in [1-12]");
		}
	}

	/**
	 * �������Ƿ���1900-2049֮��.
	 * 
	 * @param year
	 *            ��
	 * @return ���������1900-2049֮�䣬�򷵻ش��꣬��֮�׳��쳣
	 * @throws Exception
	 */
	private int checkYear(int year) throws Exception {
		if ((year > 1899) && (year < 2050))
			return year;
		else {
			throw new Exception("The Year out of range, Year should be in [1900-2049]");
		}
	}

	/**
	 * ���� ���յ�offset ���ظ�֧.
	 * 
	 * @param num
	 *            �»�����
	 * @return ������ɵ�֧
	 */
	private String cyclicalm(int num) {
		return (Gan[num % 10] + Zhi[num % 12]);
	}

	/**
	 * ��ȡ��Ф.<br>
	 * ��Ф��"��", "ţ", "��", "��", "��", "��", "��", "��", "��", "��", "��", "��"
	 * 
	 * @return animalYear ��Ф������ֵ��"ţ"
	 */
	public String getAnimalYear() {
		return Animals[(lunarYear - 4) % 12];
	}

	/**
	 * ת��Ϊ���ı�ʾ.
	 * 
	 * @param day
	 *            ��
	 * @return ����ũ��д��
	 */
	private String getCnDay(int day) {
		String str = "";
		if (day == 10) {
			return "��ʮ";
		}
		switch (day / 10) {
			case 0:
				str = "��";
				break;
			case 1:
				str = "ʮ";
				break;
			case 2:
				str = "إ";
				break;
			case 3:
				str = "ئ";
				break;
		}
		switch (day % 10) {
			case 0:
				str += "ʮ";
				break;
			case 1:
				str += "һ";
				break;
			case 2:
				str += "��";
				break;
			case 3:
				str += "��";
				break;
			case 4:
				str += "��";
				break;
			case 5:
				str += "��";
				break;
			case 6:
				str += "��";
				break;
			case 7:
				str += "��";
				break;
			case 8:
				str += "��";
				break;
			case 9:
				str += "��";
				break;
		}
		return str;
	}

	/**
	 * ��ȡ��������.<br>
	 * �������ڣ�"������", "����һ", "���ڶ�", "������", "������", "������", "������"
	 * 
	 * @return cnWeek �������ڣ�����ֵ��"������"
	 */
	public String getCnWeek() {
		calendar.set(solarYear, solarMonth - 1, solarDay);
		return sCnWeek[calendar.get(Calendar.DAY_OF_WEEK) - 1];
	}

	/**
	 * ��ȡӢ������.<br>
	 * Ӣ�����ڣ�"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday",
	 * "Saturday"
	 * 
	 * @return enWeek Ӣ�����ڣ�����ֵ��"Friday"
	 */
	public String getEnWeek() {
		calendar.set(solarYear, solarMonth - 1, solarDay);
		return sWeek[calendar.get(Calendar.DAY_OF_WEEK) - 1];
	}

	/**
	 * ��ȡũ���յ���ɵ�֧��ʾ.<br>
	 * ��ɣ�"��", "��", "��", "��", "��", "��", "��", "��", "��", "��"<br>
	 * ��֧��"��", "��", "��", "î", "��", "��", "��", "δ", "��", "��", "��", "��"
	 * 
	 * @return gzDay ũ���յ���ɵ�֧��ʾ������ֵ��"��δ"
	 */
	public String getGzDay() {
		return gzDay;
	}

	/**
	 * ��ȡũ���µ���ɵ�֧��ʾ.<br>
	 * ��ɣ�"��", "��", "��", "��", "��", "��", "��", "��", "��", "��"<br>
	 * ��֧��"��", "��", "��", "î", "��", "��", "��", "δ", "��", "��", "��", "��"
	 * 
	 * @return ũ���µ���ɵ�֧��ʾ������ֵ��"����"
	 */
	public String getGzMonth() {
		return gzMonth;
	}

	/**
	 * ��ȡũ�������ɵ�֧��ʾ.<br>
	 * ��ɣ�"��", "��", "��", "��", "��", "��", "��", "��", "��", "��"<br>
	 * ��֧��"��", "��", "��", "î", "��", "��", "��", "δ", "��", "��", "��", "��"
	 * 
	 * @return ũ�������ɵ�֧��ʾ������ֵ��"����"
	 */
	public String getGzYear() {
		return gzYear;
	}

	/**
	 * ��ȡ���ֱ�ʾ��ũ����.
	 * 
	 * @return ũ���գ�����ֵ��"1"
	 */
	public int getLunarDay() {
		return lunarDay;
	}

	/**
	 * ��ȡ���ֱ�ʾ��ũ����.
	 * 
	 * @return ũ���£�����ֵ��"��һ"
	 */
	public String getLunarDayCn() {
		return getCnDay(getLunarDay());
	}

	/**
	 * ��ȡ��ǰ��ũ������.<br>
	 * ũ�����գ�"����", "Ԫ����", "�����", "��Ϧ���˽�", "��Ԫ��", "�����", "������", "���˽�", "С��", "��Ϧ"
	 * 
	 * @return ũ�����գ�����ֵ��"����"�����ǽ����򷵻�""
	 */
	public String getLunarFestival() {
		for (int i = 0; i < LunarFtv.length; i++) {
			int m = Integer.parseInt(LunarFtv[i].substring(0, 2));
			int d = Integer.parseInt(LunarFtv[i].substring(2, 4));
			if (m == getLunarMonth() && d == getLunarDay()) {
				return LunarFtv[i].substring(4);
			}
		}
		if (getLunarMonth() == 12 && getLunarDay() == 29) {
			// �ж�12��29�Ƿ�Ϊũ�����һ�죬����Ϧ
			int[] lunarInfo = new int[6];
			lunarInfo = calElement(this.solarYear, this.solarMonth, this.solarDay + 1);
			// lunarInfo[2]:ũ������
			if (lunarInfo[2] == 1) {
				return "��Ϧ";
			}
		}
		return "";
	}

	/**
	 * ��ȡ���ֱ�ʾ��ũ����.
	 * 
	 * @return ũ���£�����ֵ��"12"
	 */
	public int getLunarMonth() {
		return lunarMonth;
	}

	/**
	 * ��ȡ���ֱ�ʾ��ũ����.<br>
	 * ũ���£�"����", "����", "����", "����", "����", "����", "����", "����", "����", "ʮ��", "����",
	 * "����"
	 * 
	 * @return ũ���£�����ֵ��"����"
	 */
	public String getLunarMonthCn() {
		return CnMonth[getLunarMonth() - 1];
	}

	/**
	 * ��ȡ���ֱ�ʾ��ũ����.
	 * 
	 * @return ũ���£�����ֵ��"2009"
	 */
	public int getLunarYear() {
		return lunarYear;
	}

	/**
	 * ��ȡ���ֱ�ʾ��������.
	 * 
	 * @return �����գ�����ֵ��"1"
	 */
	public int getSolarDay() {
		return solarDay;
	}

	/**
	 * ��ȡ���ֱ�ʾ��������.
	 * 
	 * @return �����£�����ֵ��"12"
	 */
	public int getSolarMonth() {
		return solarMonth;
	}

	/**
	 * ��ȡ���ֱ�ʾ��������.
	 * 
	 * @return �����꣬����ֵ��"2009"
	 */
	public int getSolarYear() {
		return solarYear;
	}

	/**
	 * ��ȡ����ĳ��ĳ�µ�����.
	 * 
	 * @return ����
	 */
	private int getSolarYearMonthDays() {
		if ((solarMonth == 1) || (solarMonth == 3) || (solarMonth == 5) || (solarMonth == 7) || (solarMonth == 8) || (solarMonth == 10)
		        || (solarMonth == 12)) {
			return 31;
		} else if ((solarMonth == 4) || (solarMonth == 6) || (solarMonth == 9) || (solarMonth == 11)) {
			return 30;
		} else if (solarMonth == 2) {
			if (isSolarLeapYear(solarYear)) {
				return 29;
			} else {
				return 28;
			}
		} else {
			return -1;
		}
	}

	/**
	 * ��ȡũ������.<br>
	 * ������"С��", "��", "����", "��ˮ", "����", "����", "����", "����", "����", "С��", "â��",
	 * "����", "С��", "����", "����", "����", "��¶", "���", "��¶", "˪��", "����", "Сѩ", "��ѩ",
	 * "����"
	 * 
	 * @return ũ������������ֵ��"����"�����ǽ������򷵻�""
	 */
	public String getSoralTerm() {
		String solarTerms;
		if (solarDay == sTerm(solarYear, (solarMonth - 1) * 2)) {
			solarTerms = SolarTerm[(solarMonth - 1) * 2];
		} else if (solarDay == sTerm(solarYear, (solarMonth - 1) * 2 + 1)) {
			solarTerms = SolarTerm[(solarMonth - 1) * 2 + 1];
		} else {
			// �ǽ���ʱ��
			solarTerms = "";
		}
		return solarTerms;
	}

	/**
	 * ��ʼ������.
	 */
	private void init() {
		int[] lunarInfo = new int[6];
		lunarInfo = calElement(this.solarYear, this.solarMonth, this.solarDay);
		// lunarInfo[0]=���ڼ���ũ�������ɵ�֧
		this.lunarYear = lunarInfo[0];
		// lunarInfo[1]=ũ�����·�
		this.lunarMonth = lunarInfo[1];
		// lunarInfo[2]=ũ������
		this.lunarDay = lunarInfo[2];
		// ����ũ�������ɵ�֧
		this.gzYear = cyclicalm(lunarInfo[0] - 1900 + 36);
		// lunarInfo[3]=���ڼ���ũ���µ���ɵ�֧
		this.gzMonth = cyclicalm(lunarInfo[3]);
		// lunarInfo[4]=���ڼ���ũ�������ɵ�֧
		this.gzDay = cyclicalm(lunarInfo[4]);
		// lunarInfo[5]=isLeap �Ƿ�Ϊ���� 1�� 0����
		this.isLunarLeapMonth = lunarInfo[5] == 1 ? true : false;
	}

	/**
	 * ��ȡ��ǰ���Ƿ�Ϊ����.
	 * 
	 * @return �Ƿ�Ϊ����
	 */
	public boolean isLeapYear() {
		return isSolarLeapYear(solarYear);
	}

	/**
	 * �����������ݼ����Ƿ�Ϊ����.
	 * 
	 * @param �Ƿ�Ϊ����
	 */
	private boolean isSolarLeapYear(int year) {
		return ((year % 4 == 0) && (year % 100 != 0) || year % 400 == 0);
	}

	/**
	 * ����ũ��year�����µ�����.
	 * 
	 * @param year
	 *            ��
	 * @return ũ��year�����µ�����
	 */
	private int leapDays(int year) {
		if (leapMonth(year) != 0) {
			if ((lunarInfo[year - 1900] & 0x10000) != 0) {
				return 30;
			} else {
				return 29;
			}
		} else {
			return 0;
		}
	}

	/**
	 * ����ũ�� y�����ĸ��� 1-12 , û�򴫻� 0.
	 * 
	 * @param year
	 *            ��
	 * @return ũ�� y�����ĸ��� 1-12 , û�򴫻� 0
	 */
	private int leapMonth(int year) {
		return (int) (lunarInfo[year - 1900] & 0xf);
	}

	/**
	 * ����ũ��year���������.
	 * 
	 * @param year
	 *            ��
	 * @return ũ��year���������
	 */
	private int lunarYearDays(int year) {
		int i, sum = 348;
		for (i = 0x8000; i > 0x8; i >>= 1) {
			if ((lunarInfo[year - 1900] & i) != 0)
				sum += 1;
		}
		return (sum + leapDays(year));
	}

	/**
	 * ����ũ��year��month�µ�������.
	 * 
	 * @param year
	 *            ��
	 * @param month
	 *            ��
	 * @return ũ��year��month�µ�������
	 */
	private int monthDays(int year, int month) {
		if ((lunarInfo[year - 1900] & (0x10000 >> month)) == 0) {
			return 29;
		} else {
			return 30;
		}
	}

	/**
	 * year��ĵ�n������Ϊ����(��0С������).
	 * 
	 * @param year
	 *            ��
	 * @param n
	 *            ��n������
	 * @return year��ĵ�n������Ϊ����
	 */
	private int sTerm(int year, int n) {
		Calendar cal = Calendar.getInstance();
		cal.set(1900, 0, 6, 2, 5, 0);
		long temp = cal.getTime().getTime();
		cal.setTime(new Date((long) ((31556925974.7 * (year - 1900) + STermInfo[n] * 60000L) + temp)));
		return cal.get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * ����ũ������ϸ����.
	 * 
	 * @return ũ������ϸ��������"ũ������إ��,������,������,��î��,��ˮ"
	 */
	public String toLunarCalday() {
		StringBuilder buf = new StringBuilder(100);
		buf.append("ũ��").append(isLunarLeapMonth ? "��" : "").append(getLunarMonthCn()).append(getLunarDayCn()).append(",");
		buf.append(getGzYear()).append("��,");
		buf.append(getGzMonth()).append("��,");
		buf.append(getGzDay()).append("��,");
		buf.append(getSoralTerm());
		return buf.toString();
	}

	/**
	 * �������.<br>
	 * ���磺"����ʱ��2009��6��15��,����һ"
	 */
	@Override
	public String toString() {
		StringBuilder buf = new StringBuilder(100);
		buf.append("����ʱ��");
		buf.append(getSolarYear()).append("��").append(getSolarMonth()).append("��").append(getSolarDay()).append("��").append(",");
		buf.append(getCnWeek());
		return buf.toString();
	}
}
