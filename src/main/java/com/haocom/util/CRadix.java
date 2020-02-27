
package com.haocom.util;


/**
 * 
 * Title.��ʮ�������ڵ����ֻ�ת <br>
 * Description.62�����ڵ����ֻ�ת
 * <p>
 * Copyright: Copyright (c) 2014��12��24�� ����2:05:00
 * <p>
 * Company:
 * <p>
 * Author: nishu
 * <p>
 * Version: 1.0
 * <p>
 */
public class CRadix {

	public static void main(String[] args) {

		// ��λΪbit
		System.out.println("char:" + Character.SIZE);
		System.out.println("int:" + Integer.SIZE);
		System.out.println("short:" + Short.SIZE);
		System.out.println("long:" + Long.SIZE);
		System.out.println("byte:" + Byte.SIZE);
		System.out.println("float:" + Float.SIZE);
		System.out.println("double:" + Double.SIZE);

		System.out.println(144782689616211L);

		String a = dec2Any(144782689616211L, 62);
		System.out.println(a);

		long b = any2Dec(a, 62);
		System.out.println("" + b);

	}

	/**
	 * ֧�ֵ���С����
	 */
	public static int MIN_RADIX = 2;

	/**
	 * ֧�ֵ�������
	 */
	public static int MAX_RADIX = 62;

	/**
	 * ��������
	 */
	private CRadix() {
	}

	/**
	 * 0-9a-zA-Z��ʾ62�����ڵ� 0��61��
	 */
	private static final String num62 = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

	/**
	 * ����һ�ַ��������� number �� toRadix ���Ƶı�ʾ��<br />
	 * number ����Ľ����� fromRadix ָ����fromRadix �� toRadix ��ֻ���� 2 �� 62 ֮�䣨���� 2 �� 62����<br />
	 * ����ʮ���Ƶ���������ĸ a-zA-Z ��ʾ������ a ��ʾ 10��b ��ʾ 11 �Լ� Z ��ʾ 62��<br />
	 * 
	 * @param number
	 *            ��Ҫת��������
	 * @param fromRadix
	 *            �������
	 * @param toRadix
	 *            �������
	 * @return ָ��������Ƶ�����
	 */
	public static String baseConver(String number, int fromRadix, int toRadix) {
		long dec = any2Dec(number, fromRadix);
		return dec2Any(dec, toRadix);
	}

	/**
	 * ����һ�ַ��������� ʮ���� number �� radix ���Ƶı�ʾ��
	 * 
	 * @param dec
	 *            ��Ҫת��������
	 * @param toRadix
	 *            ������ơ�������ת����Χ��ʱ���˲����ᱻ�趨Ϊ 2���Ա㼰ʱ���֡�
	 * @return ָ��������Ƶ�����
	 */
	public static String dec2Any(long dec, int toRadix) {
		if (toRadix < MIN_RADIX || toRadix > MAX_RADIX) {
			toRadix = 2;
		}
		if (toRadix == 10) {
			return String.valueOf(dec);
		}
		// -Long.MIN_VALUE ת��Ϊ 2 ����ʱ����Ϊ65
		char[] buf = new char[65]; //
		int charPos = 64;
		boolean isNegative = (dec < 0);
		if (!isNegative) {
			dec = -dec;
		}
		while (dec <= -toRadix) {
			buf[charPos--] = num62.charAt((int) (-(dec % toRadix)));
			dec = dec / toRadix;
		}
		buf[charPos] = num62.charAt((int) (-dec));
		if (isNegative) {
			buf[--charPos] = '-';
		}
		return new String(buf, charPos, (65 - charPos));
	}

	/**
	 * ����һ�ַ��������� number �� 10 ���Ƶı�ʾ��<br />
	 * fromBase ֻ���� 2 �� 62 ֮�䣨���� 2 �� 62����
	 * 
	 * @param number
	 *            ��������
	 * @param fromRadix
	 *            �������
	 * @return ʮ��������
	 */
	public static long any2Dec(String number, int fromRadix) {
		long dec = 0;
		long digitValue = 0;
		int len = number.length() - 1;
		for (int t = 0; t <= len; t++) {
			digitValue = num62.indexOf(number.charAt(t));
			dec = dec * fromRadix + digitValue;
		}
		return dec;
	}
}
