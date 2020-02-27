package com.haocom.util.lunar;

import java.util.Calendar;
import java.util.Date;

/**
 * 农历类. <br>
 * 实现对农历的获取，根据输入的阳历日期，获取对应的农历年月日以及天干地支等.
 * <p>
 * Copyright: Copyright (c) 2009-7-24
 * <p>
 * Company: 
 * <p>
 * Author: ZhouYan
 * <p>
 * Version: 1.0
 * <p>
 * <h2>组件使用说明:</h2>
 * <ul>
 * <li>组件提供的方法的使用：</li>
 * 
 * <pre>
 * Lunar solarDate = new Lunar(&quot;19010219&quot;);
 * System.out.println(&quot;阳历:		&quot; + solarDate.getSolarYear() + &quot;,&quot; + solarDate.getSolarMonth() + &quot;,&quot; + solarDate.getSolarDay());
 * System.out.println(&quot;农历:		&quot; + solarDate.getLunarYear() + &quot;,&quot; + solarDate.getLunarMonth() + &quot;,&quot; + solarDate.getLunarDay());
 * System.out.println(&quot;农历:		&quot; + solarDate.getLunarYear() + &quot;,&quot; + solarDate.getLunarMonthCn() + &quot;,&quot; + solarDate.getLunarDayCn());
 * System.out.println(&quot;干支:		&quot; + solarDate.getGzYear() + &quot;年,&quot; + solarDate.getGzMonth() + &quot;月,&quot; + solarDate.getGzDay() + &quot;日&quot;);
 * System.out.println(&quot;生肖:		&quot; + solarDate.getAnimalYear());
 * System.out.println(&quot;星期(中):       &quot; + solarDate.getCnWeek());
 * System.out.println(&quot;星期(英):       &quot; + solarDate.getEnWeek());
 * System.out.println(&quot;节气:		&quot; + solarDate.getSoralTerm());
 * System.out.println(&quot;节日：	         &quot; + solarDate.getLunarFestival());
 * if (solarDate.isLeapYear()) {
 * 	System.out.println(&quot;是否闰年：	闰年&quot;);
 * } else {
 * 	System.out.println(&quot;是否闰年：	不是闰年&quot;);
 * }
 * System.out.println(&quot;toLunarCalday:	&quot; + solarDate.toLunarCalday());
 * System.out.println(&quot;toString:	&quot; + solarDate.toString());
 * </pre>
 * 
 * <li>控制台输出内容，请对应上述的代码了解各个方法的作用</li>
 * 
 * <pre>
 * 阳历:		1901,2,19
 * 农历:		1901,1,1
 * 农历:		1901,正月,初一
 * 干支:		辛丑年,庚寅月,戊辰日
 * 生肖:		牛
 * 星期(中):	       星期二
 * 星期(英):	       Tuesday
 * 节气:		雨水
 * 节日：		 春节
 * 是否闰年：	       不是闰年
 * toLunarCalday:	 农历正月初一,辛丑年,庚寅月,戊辰日,雨水
 * toString:	 北京时间1901年2月19日,星期二
 * </pre>
 * 
 * </ul>
 */
public class Lunar {

	/** 生肖 */
	private static final String[] Animals = { "鼠", "牛", "虎", "兔", "龙", "蛇", "马", "羊", "猴", "鸡", "狗", "猪" };

	/** 月份 */
	private static final String[] CnMonth = new String[] { "正月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "冬月", "腊月" };

	/** 天干 */
	private static final String[] Gan = new String[] { "甲", "乙", "丙", "丁", "戊", "己", "庚", "辛", "壬", "癸" };

	/** 农历节日 */
	private static final String[] LunarFtv = { "0101春节", "0115元宵节", "0505端午节", "0707七夕情人节", "0715中元节", "0815中秋节", "0909重阳节", "1208腊八节", "1223小年",
	        "1230除夕" };

	/** 计算农历使用的偏移量 */
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

	/** 中文星期 */
	private static final String[] sCnWeek = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };

	/** 节气 */
	private static final String[] SolarTerm = new String[] { "小寒", "大寒", "立春", "雨水", "惊蛰", "春分", "清明", "谷雨", "立夏", "小满", "芒种", "夏至", "小暑", "大暑",
	        "立秋", "处暑", "白露", "秋分", "寒露", "霜降", "立冬", "小雪", "大雪", "冬至" };

	/** 节气偏移量 */
	private static final long[] STermInfo = new long[] { 0, 21208, 42467, 63836, 85337, 107014, 128867, 150921, 173149, 195551, 218072, 240693,
	        263343, 285989, 308563, 331033, 353350, 375494, 397447, 419210, 440795, 462224, 483532, 504758 };

	/** 英文星期 */
	private static String[] sWeek = { "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday" };

	/** 地支 */
	private static String[] Zhi = new String[] { "子", "丑", "寅", "卯", "辰", "巳", "午", "未", "申", "酉", "戌", "亥" };

	/** 日历 */
	private Calendar calendar = Calendar.getInstance();

	/** 农历天 天干地支表示 */
	private String gzDay;

	/** 农历月 天干地支表示 */
	private String gzMonth;

	/** 农历年 天干地支表示 */
	private String gzYear;

	/** 是否为农历闰月 */
	private boolean isLunarLeapMonth;

	/** 农历 天 */
	private int lunarDay;

	/** 农历 月 */
	private int lunarMonth;

	/** 农历 年 */
	private int lunarYear;

	/** 阳历 天 */
	private int solarDay;

	/** 阳历 月 */
	private int solarMonth;

	/** 阳历 年 */
	private int solarYear;

	/**
	 * 构造方法，初始化年月日，默认为当前的年月日.
	 */
	public Lunar() {
		this.solarYear = calendar.get(Calendar.YEAR);
		this.solarMonth = calendar.get(Calendar.MONTH) + 1;
		this.solarDay = calendar.get(Calendar.DAY_OF_MONTH);
		init();
	}

	/**
	 * 构造方法，初始化年月日.
	 * 
	 * @param cal
	 *            Calendar日历
	 * @throws Exception
	 */
	public Lunar(Calendar cal) throws Exception {
		this.solarYear = cal.get(Calendar.YEAR);
		this.solarMonth = cal.get(Calendar.MONTH) + 1;
		this.solarDay = checkDay(cal.get(Calendar.DAY_OF_MONTH));
		init();
	}

	/**
	 * 构造方法，初始化年月日.
	 * 
	 * @param date
	 *            Date日期
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
	 * 构造方法，初始化年，默认月为当前月，默认天为当前天.
	 * 
	 * @param year
	 *            阳历年
	 * @throws Exception
	 */
	public Lunar(int year) throws Exception {
		this.solarYear = checkYear(year);
		this.solarMonth = calendar.get(Calendar.MONTH) + 1;
		this.solarDay = calendar.get(Calendar.DAY_OF_MONTH);
		init();
	}

	/**
	 * 构造方法，初始化年月，默认天为当前天.
	 * 
	 * @param year
	 *            阳历年
	 * @param month
	 *            阳历月
	 * @throws Exception
	 */
	public Lunar(int year, int month) throws Exception {
		this.solarYear = checkYear(year);
		this.solarMonth = checkMonth(month);
		this.solarDay = calendar.get(Calendar.DAY_OF_MONTH);
		init();
	}

	/**
	 * 构造方法，初始化年月日.
	 * 
	 * @param year
	 *            阳历年
	 * @param month
	 *            阳历月
	 * @param day
	 *            阳历日
	 * @throws Exception
	 */
	public Lunar(int year, int month, int day) throws Exception {
		this.solarYear = checkYear(year);
		this.solarMonth = checkMonth(month);
		this.solarDay = checkDay(day);
		init();
	}

	/**
	 * 构造方法，初始化年月日.
	 * 
	 * @param date
	 *            8位字符串阳历时间，如“20090806”
	 */
	public Lunar(String date) throws Exception {
		this.solarYear = checkYear(Integer.parseInt(date.substring(0, 4)));
		this.solarMonth = checkMonth(Integer.parseInt(date.substring(4, 6)));
		this.solarDay = checkDay(Integer.parseInt(date.substring(6, 8)));
		init();
	}

	/**
	 * 传出year年month月day日对应的农历.<BR>
	 * int[0]=用于计算农历年的天干地支<BR>
	 * int[1]=农历的月份<BR>
	 * int[2]=农历的天<BR>
	 * int[3]=用于计算农历月的天干地支<BR>
	 * int[4]=用于计算农历天的天干地支<BR>
	 * int[5]=isLeap 是否为闰月 1是 0不是<BR>
	 * 
	 * @param year
	 *            年
	 * @param month
	 *            月
	 * @param day
	 *            日
	 * @return year年month月day日对应的农历
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
		leap = leapMonth(i); // 闰哪个月
		nongDate[5] = 0;
		for (i = 1; i < 13 && offset > 0; i++) {
			// 闰月
			if (leap > 0 && i == (leap + 1) && nongDate[5] == 0) {
				--i;
				nongDate[5] = 1;
				temp = leapDays(nongDate[0]);
			} else {
				temp = monthDays(nongDate[0], i);
			}
			// 解除闰月
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
	 * 检查日是否在1-本月最后一天之间.
	 * 
	 * @param day
	 *            日
	 * @return 如果日输入正常，则返回此日，反之抛出异常
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
	 * 检查月份是否在1-12之间.
	 * 
	 * @param month
	 *            月
	 * @return 如果月是在1-12之间，则返回此年，反之抛出异常
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
	 * 检查年份是否在1900-2049之间.
	 * 
	 * @param year
	 *            年
	 * @return 如果年是在1900-2049之间，则返回此年，反之抛出异常
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
	 * 传入 月日的offset 传回干支.
	 * 
	 * @param num
	 *            月或者日
	 * @return 返回天干地支
	 */
	private String cyclicalm(int num) {
		return (Gan[num % 10] + Zhi[num % 12]);
	}

	/**
	 * 获取生肖.<br>
	 * 生肖："鼠", "牛", "虎", "兔", "龙", "蛇", "马", "羊", "猴", "鸡", "狗", "猪"
	 * 
	 * @return animalYear 生肖，返回值如"牛"
	 */
	public String getAnimalYear() {
		return Animals[(lunarYear - 4) % 12];
	}

	/**
	 * 转换为中文表示.
	 * 
	 * @param day
	 *            日
	 * @return 中文农历写法
	 */
	private String getCnDay(int day) {
		String str = "";
		if (day == 10) {
			return "初十";
		}
		switch (day / 10) {
			case 0:
				str = "初";
				break;
			case 1:
				str = "十";
				break;
			case 2:
				str = "廿";
				break;
			case 3:
				str = "卅";
				break;
		}
		switch (day % 10) {
			case 0:
				str += "十";
				break;
			case 1:
				str += "一";
				break;
			case 2:
				str += "二";
				break;
			case 3:
				str += "三";
				break;
			case 4:
				str += "四";
				break;
			case 5:
				str += "五";
				break;
			case 6:
				str += "六";
				break;
			case 7:
				str += "七";
				break;
			case 8:
				str += "八";
				break;
			case 9:
				str += "九";
				break;
		}
		return str;
	}

	/**
	 * 获取中文星期.<br>
	 * 中文星期："星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"
	 * 
	 * @return cnWeek 中文星期，返回值如"星期六"
	 */
	public String getCnWeek() {
		calendar.set(solarYear, solarMonth - 1, solarDay);
		return sCnWeek[calendar.get(Calendar.DAY_OF_WEEK) - 1];
	}

	/**
	 * 获取英文星期.<br>
	 * 英文星期："Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday",
	 * "Saturday"
	 * 
	 * @return enWeek 英文星期，返回值如"Friday"
	 */
	public String getEnWeek() {
		calendar.set(solarYear, solarMonth - 1, solarDay);
		return sWeek[calendar.get(Calendar.DAY_OF_WEEK) - 1];
	}

	/**
	 * 获取农历日的天干地支表示.<br>
	 * 天干："甲", "乙", "丙", "丁", "戊", "己", "庚", "辛", "壬", "癸"<br>
	 * 地支："子", "丑", "寅", "卯", "辰", "巳", "午", "未", "申", "酉", "戌", "亥"
	 * 
	 * @return gzDay 农历日的天干地支表示，返回值如"癸未"
	 */
	public String getGzDay() {
		return gzDay;
	}

	/**
	 * 获取农历月的天干地支表示.<br>
	 * 天干："甲", "乙", "丙", "丁", "戊", "己", "庚", "辛", "壬", "癸"<br>
	 * 地支："子", "丑", "寅", "卯", "辰", "巳", "午", "未", "申", "酉", "戌", "亥"
	 * 
	 * @return 农历月的天干地支表示，返回值如"丙寅"
	 */
	public String getGzMonth() {
		return gzMonth;
	}

	/**
	 * 获取农历年的天干地支表示.<br>
	 * 天干："甲", "乙", "丙", "丁", "戊", "己", "庚", "辛", "壬", "癸"<br>
	 * 地支："子", "丑", "寅", "卯", "辰", "巳", "午", "未", "申", "酉", "戌", "亥"
	 * 
	 * @return 农历年的天干地支表示，返回值如"己丑"
	 */
	public String getGzYear() {
		return gzYear;
	}

	/**
	 * 获取数字表示的农历日.
	 * 
	 * @return 农历日，返回值如"1"
	 */
	public int getLunarDay() {
		return lunarDay;
	}

	/**
	 * 获取文字表示的农历日.
	 * 
	 * @return 农历月，返回值如"初一"
	 */
	public String getLunarDayCn() {
		return getCnDay(getLunarDay());
	}

	/**
	 * 获取当前的农历节日.<br>
	 * 农历节日："春节", "元宵节", "端午节", "七夕情人节", "中元节", "中秋节", "重阳节", "腊八节", "小年", "除夕"
	 * 
	 * @return 农历节日，返回值如"春节"，若非节日则返回""
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
			// 判断12月29是否为农历最后一天，即除夕
			int[] lunarInfo = new int[6];
			lunarInfo = calElement(this.solarYear, this.solarMonth, this.solarDay + 1);
			// lunarInfo[2]:农历的日
			if (lunarInfo[2] == 1) {
				return "除夕";
			}
		}
		return "";
	}

	/**
	 * 获取数字表示的农历月.
	 * 
	 * @return 农历月，返回值如"12"
	 */
	public int getLunarMonth() {
		return lunarMonth;
	}

	/**
	 * 获取文字表示的农历月.<br>
	 * 农历月："正月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "冬月",
	 * "腊月"
	 * 
	 * @return 农历月，返回值如"腊月"
	 */
	public String getLunarMonthCn() {
		return CnMonth[getLunarMonth() - 1];
	}

	/**
	 * 获取数字表示的农历年.
	 * 
	 * @return 农历月，返回值如"2009"
	 */
	public int getLunarYear() {
		return lunarYear;
	}

	/**
	 * 获取数字表示的阳历日.
	 * 
	 * @return 阳历日，返回值如"1"
	 */
	public int getSolarDay() {
		return solarDay;
	}

	/**
	 * 获取数字表示的阳历月.
	 * 
	 * @return 阳历月，返回值如"12"
	 */
	public int getSolarMonth() {
		return solarMonth;
	}

	/**
	 * 获取数字表示的阳历年.
	 * 
	 * @return 阳历年，返回值如"2009"
	 */
	public int getSolarYear() {
		return solarYear;
	}

	/**
	 * 获取阳历某年某月的天数.
	 * 
	 * @return 天数
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
	 * 获取农历节气.<br>
	 * 节气："小寒", "大寒", "立春", "雨水", "惊蛰", "春分", "清明", "谷雨", "立夏", "小满", "芒种",
	 * "夏至", "小暑", "大暑", "立秋", "处暑", "白露", "秋分", "寒露", "霜降", "立冬", "小雪", "大雪",
	 * "冬至"
	 * 
	 * @return 农历节气，返回值如"立秋"，若非节气日则返回""
	 */
	public String getSoralTerm() {
		String solarTerms;
		if (solarDay == sTerm(solarYear, (solarMonth - 1) * 2)) {
			solarTerms = SolarTerm[(solarMonth - 1) * 2];
		} else if (solarDay == sTerm(solarYear, (solarMonth - 1) * 2 + 1)) {
			solarTerms = SolarTerm[(solarMonth - 1) * 2 + 1];
		} else {
			// 非节气时间
			solarTerms = "";
		}
		return solarTerms;
	}

	/**
	 * 初始化数据.
	 */
	private void init() {
		int[] lunarInfo = new int[6];
		lunarInfo = calElement(this.solarYear, this.solarMonth, this.solarDay);
		// lunarInfo[0]=用于计算农历年的天干地支
		this.lunarYear = lunarInfo[0];
		// lunarInfo[1]=农历的月份
		this.lunarMonth = lunarInfo[1];
		// lunarInfo[2]=农历的天
		this.lunarDay = lunarInfo[2];
		// 计算农历年的天干地支
		this.gzYear = cyclicalm(lunarInfo[0] - 1900 + 36);
		// lunarInfo[3]=用于计算农历月的天干地支
		this.gzMonth = cyclicalm(lunarInfo[3]);
		// lunarInfo[4]=用于计算农历天的天干地支
		this.gzDay = cyclicalm(lunarInfo[4]);
		// lunarInfo[5]=isLeap 是否为闰月 1是 0不是
		this.isLunarLeapMonth = lunarInfo[5] == 1 ? true : false;
	}

	/**
	 * 获取当前年是否为闰年.
	 * 
	 * @return 是否为闰年
	 */
	public boolean isLeapYear() {
		return isSolarLeapYear(solarYear);
	}

	/**
	 * 根据输入的年份计算是否为闰年.
	 * 
	 * @param 是否为闰年
	 */
	private boolean isSolarLeapYear(int year) {
		return ((year % 4 == 0) && (year % 100 != 0) || year % 400 == 0);
	}

	/**
	 * 传回农历year年闰月的天数.
	 * 
	 * @param year
	 *            年
	 * @return 农历year年闰月的天数
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
	 * 传回农历 y年闰哪个月 1-12 , 没闰传回 0.
	 * 
	 * @param year
	 *            年
	 * @return 农历 y年闰哪个月 1-12 , 没闰传回 0
	 */
	private int leapMonth(int year) {
		return (int) (lunarInfo[year - 1900] & 0xf);
	}

	/**
	 * 传回农历year年的总天数.
	 * 
	 * @param year
	 *            年
	 * @return 农历year年的总天数
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
	 * 传回农历year年month月的总天数.
	 * 
	 * @param year
	 *            年
	 * @param month
	 *            月
	 * @return 农历year年month月的总天数
	 */
	private int monthDays(int year, int month) {
		if ((lunarInfo[year - 1900] & (0x10000 >> month)) == 0) {
			return 29;
		} else {
			return 30;
		}
	}

	/**
	 * year年的第n个节气为几日(从0小寒起算).
	 * 
	 * @param year
	 *            年
	 * @param n
	 *            第n个节气
	 * @return year年的第n个节气为几日
	 */
	private int sTerm(int year, int n) {
		Calendar cal = Calendar.getInstance();
		cal.set(1900, 0, 6, 2, 5, 0);
		long temp = cal.getTime().getTime();
		cal.setTime(new Date((long) ((31556925974.7 * (year - 1900) + STermInfo[n] * 60000L) + temp)));
		return cal.get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 返回农历的详细描述.
	 * 
	 * @return 农历的详细描述，如"农历五月廿三,己丑年,庚午月,辛卯日,雨水"
	 */
	public String toLunarCalday() {
		StringBuilder buf = new StringBuilder(100);
		buf.append("农历").append(isLunarLeapMonth ? "闰" : "").append(getLunarMonthCn()).append(getLunarDayCn()).append(",");
		buf.append(getGzYear()).append("年,");
		buf.append(getGzMonth()).append("月,");
		buf.append(getGzDay()).append("日,");
		buf.append(getSoralTerm());
		return buf.toString();
	}

	/**
	 * 输出内容.<br>
	 * 例如："北京时间2009年6月15日,星期一"
	 */
	@Override
	public String toString() {
		StringBuilder buf = new StringBuilder(100);
		buf.append("北京时间");
		buf.append(getSolarYear()).append("年").append(getSolarMonth()).append("月").append(getSolarDay()).append("日").append(",");
		buf.append(getCnWeek());
		return buf.toString();
	}
}
