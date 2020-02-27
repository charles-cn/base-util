package com.haocom.util;

import java.util.Arrays;

/**
 * �ֽ�ת������. <br>
 * �ֽ�ת������,����ʵ���ֽ��������������֮���ת��.
 * <p>
 * �޸���ʷ: 2009-8-10 ����05:03:37 ChengFan <br>
 * �޸�bytes2Hex������hex2Bytes��������дʵ�ַ������Ľ����ܡ�<br>
 * <p>
 * Copyright: Copyright (c) Sep 16, 2008
 * <p>
 * Company: 
 * <p>
 * Author: ����
 * <p>
 * Version: 1.0
 */

public class CByte {

	/** ��ʾ16���Ƶĸ������� */
	private static String HexCode[] = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };

	/**
	 * �ֽ�ת����16�����ַ���
	 * 
	 * @param b
	 *            �ֽ�
	 * @return 16�����ַ���<BR>
	 *         ���磺���루byte��11 ���ص��� 0b
	 */
	public static String byte2Hex(byte b) {
		int n = b;
		if (n < 0) {
			n = 256 + n;
		}
		int d1 = n / 16;
		int d2 = n % 16;
		return HexCode[d1] + HexCode[d2];
	}

	/**
	 * �ֽ�����ת��������
	 * 
	 * @param b
	 *            �ֽ�����
	 * @return 10��������<BR>
	 *         �ֽ�����,�����ǰ���4���ֽڣ���������byte b[4];���� b[0] = 1; b[1] = 2; b[2] = 3;
	 *         b[3] = 4;�򷵻�10��������16909060
	 */
	public static int byte2int(byte b[]) {
		return b[3] & 0xff | (b[2] & 0xff) << 8 | (b[1] & 0xff) << 16 | (b[0] & 0xff) << 24;
	}

	/**
	 * �ֽ�����ת��������
	 * 
	 * @param b
	 *            �ֽ�����
	 * @param offset
	 *            ƫ������������ƫ��λ�ÿ�ʼȡ4��Byte��ת��
	 * @return 10��������<BR>
	 *         ��������byte b[5];���� b[0] = 1; b[1] = 2; b[2] = 3; b[3] = 4;b[4] =
	 *         5;�򷵻�10��������33752069��ע��b�������ٰ�����4+offset�����ֽ�
	 */
	public static int byte2int(byte b[], int offset) {
		return b[offset + 3] & 0xff | (b[offset + 2] & 0xff) << 8 | (b[offset + 1] & 0xff) << 16 | (b[offset] & 0xff) << 24;
	}

	/**
	 * �ֽ�����ת���ɳ�����
	 * 
	 * @param b
	 *            �ֽ�����
	 * @return 10���Ƴ�����<BR>
	 *         �ֽ�����,�����ǰ���8���ֽڣ���������byte b[8];���� b[0] = 1; b[1] = 2; b[2] = 3;
	 *         b[3] = 4;b[4] = 5;b[5] = 6;b[6] = 7;b[7] =
	 *         8;�򷵻س�����72623859790382856
	 */
	public static long byte2long(byte b[]) {
		return ((long) b[7] & (long) 255) | ((long) b[6] & (long) 255) << 8 | ((long) b[5] & (long) 255) << 16 | ((long) b[4] & (long) 255) << 24
		        | ((long) b[3] & (long) 255) << 32 | ((long) b[2] & (long) 255) << 40 | ((long) b[1] & (long) 255) << 48
		        | ((long) b[0] & (long) 255) << 56;
	}

	/**
	 * �ֽ�����ת���ɳ�����
	 * 
	 * @param b
	 *            �ֽ�����
	 * @param offset
	 *            ƫ������������ƫ��λ�ÿ�ʼȡ8��Byte��ת��
	 * @return 10���Ƴ�����<BR>
	 *         ��������byte b[9];���� b[0] = 1; b[1] = 2; b[2] = 3; b[3] = 4;b[4] =
	 *         5;b[5] = 6;b[6] = 7;b[7]
	 *         =8;�򷵻س�����144964032628459529��ע��b�������ٰ�����7+offset�����ֽ�
	 */
	public static long byte2long(byte b[], int offset) {
		return (long) b[offset + 7] & (long) 255 | ((long) b[offset + 6] & (long) 255) << 8 | ((long) b[offset + 5] & (long) 255) << 16
		        | ((long) b[offset + 4] & (long) 255) << 24 | ((long) b[offset + 3] & (long) 255) << 32 | ((long) b[offset + 2] & (long) 255) << 40
		        | ((long) b[offset + 1] & (long) 255) << 48 | ((long) b[offset] & (long) 255) << 56;
	}

	/**
	 * �ֽ�����ת����16�����ַ���
	 * 
	 * @param b
	 *            �ֽ�����
	 * @return 16�����ַ���<BR>
	 *         ��������byte b[4],����b[0]=1;b[1]=2;b[2]=13;b[3]=14;�򷵻�16������01020d0e
	 */
	public static String bytes2Hex(byte b[]) {
		// �������
		char[] chars = new char[b.length * 2];
		int charsIndex = 0;
		int intValue;
		int intValueHi; // ��λ
		int intValueLo; // ��λ
		for (int bytesIndex = 0; bytesIndex < b.length; bytesIndex++) {
			intValue = b[bytesIndex];
			if (intValue < 0) {
				intValue += 256;
			}
			// hi
			intValueHi = (intValue & 0xf0) >> 4;
			if (intValueHi > 9) {
				chars[charsIndex] = (char) (intValueHi - 10 + 'a');
			} else {
				chars[charsIndex] = (char) (intValueHi + '0');
			}
			// lo
			intValueLo = (intValue & 0xf);
			if (intValueLo > 9) {
				chars[charsIndex + 1] = (char) (intValueLo - 10 + 'a');
			} else {
				chars[charsIndex + 1] = (char) (intValueLo + '0');
			}
			charsIndex += 2;
		}
		return new String(chars, 0, charsIndex);
	}

	/**
	 * ������ת��Ϊ2,8,16�����ַ���
	 * 
	 * @param iNum
	 *            ��ת�����֣�Ϊ������
	 * @param jz
	 *            ��Ҫת��Ϊʲô����
	 * @return ת�������ý��Ƶ��ַ���<BR>
	 *         ��������11,2 �򷵻�1011;������11,8 �򷵻�13;������11,16 �򷵻�b
	 * @throws Exception
	 */
	public static String DecToOther(long iNum, int jz) throws Exception {
		long consult = 0; // ��
		long rest = 0; // ����
		StringBuffer sbBinary = new StringBuffer();
		if (iNum >= jz) {
			do {
				consult = iNum / jz;
				rest = iNum % jz;
				iNum = consult;
				if (jz == 16) {
					sbBinary.append(HexCode[(int) rest]);
				} else {
					sbBinary.append(rest);
				}
			}
			while (consult >= jz);
			if (jz == 16) {
				sbBinary.append(HexCode[(int) consult]);
			} else {
				sbBinary.append(consult);
			}
		} else {
			rest = (int) iNum % jz;
			if (jz == 16) {
				sbBinary.append(HexCode[(int) rest]);
			} else {
				sbBinary.append(rest);
			}
		}
		sbBinary.reverse();
		return sbBinary.toString();
	}

	/**
	 * 16�����ַ���ת�����ֽ�����
	 * 
	 * @param s
	 *            16�����ַ���
	 * @return �ֽ�����<BR>
	 *         ��������1a,�򷵻ص��ֽ����������Ϊ26
	 */
	public static byte[] hex2Bytes(String s) {
		char[] chars = s.toCharArray();
		byte[] bytes = new byte[s.length() / 2];
		int byteSize = 0;
		int charIndex = 0;
		int byteValue = 0;
		int intValue = 0;
		char c;
		int position = 0;
		while (charIndex < chars.length) {
			c = chars[charIndex++];
			if (c >= '0' && c <= '9') {
				byteValue = c - '0';
			} else if (c >= 'a' && c <= 'f') {
				byteValue = c - 'a' + 10;
			} else if (c >= 'A' && c <= 'F') {
				byteValue = c - 'A' + 10;
			} else if (c >= '��' && c <= '��') {
				byteValue = c - '��';
			} else if (c >= '��' && c <= '��') {
				byteValue = c - '��' + 10;
			} else if (c >= '��' && c <= '��') {
				byteValue = c - '��' + 10;
			} else {
				continue;
			}
			if (position == 0) {
				intValue = byteValue;
				position++;
				bytes[byteSize] = (byte) intValue;
			} else {
				// �ڶ�λ
				intValue = intValue * 16 + byteValue;
				position = 0;
				bytes[byteSize] = (byte) intValue;
				byteSize++;
				if (byteSize >= bytes.length) {
					bytes = Arrays.copyOf(bytes, bytes.length + 64);
				}
			}
		}
		return Arrays.copyOf(bytes, byteSize);
	}

	/**
	 * ����ת�����ֽ�����
	 * 
	 * @param n
	 *            10��������
	 * @return �ֽ�����<BR>
	 *         ������������1,�򷵻��ֽ����������Ϊ0001
	 */
	public static byte[] int2byte(int n) {
		byte b[] = new byte[4];
		b[0] = (byte) (n >> 24);
		b[1] = (byte) (n >> 16);
		b[2] = (byte) (n >> 8);
		b[3] = (byte) n;
		return b;
	}

	/**
	 * ����ת�����ֽ�����
	 * 
	 * @param n
	 *            10��������
	 * @param buf
	 *            ת������ֽ�����,��С����С�ڣ�4+offset��
	 * @param offset
	 *            buf����洢ת���ֽ����ݵ���ʼλ��<BR>
	 *            ����ԭbufΪ6���ֽڣ�ֵΪ123456,�������1,buf,2,�򷵻�120009
	 */
	public static void int2byte(int n, byte buf[], int offset) {
		buf[offset] = (byte) (n >> 24);
		buf[offset + 1] = (byte) (n >> 16);
		buf[offset + 2] = (byte) (n >> 8);
		buf[offset + 3] = (byte) n;
	}

	/**
	 * ������ת����8���ֽڵ�����
	 * 
	 * @param n
	 *            ������
	 * @return ת������ֽ����飬���鳤��Ϊ8<BR>
	 *         �������볤����72623859790382856l���򷵻ص��ֽ���������Ϊ12345678
	 */
	public static byte[] long2byte(long n) {
		byte b[] = new byte[8];
		b[0] = (byte) (int) (n >> 56);
		b[1] = (byte) (int) (n >> 48);
		b[2] = (byte) (int) (n >> 40);
		b[3] = (byte) (int) (n >> 32);
		b[4] = (byte) (int) (n >> 24);
		b[5] = (byte) (int) (n >> 16);
		b[6] = (byte) (int) (n >> 8);
		b[7] = (byte) (int) n;
		return b;

	}

	/**
	 * ������ת����8���ֽڵ�����
	 * 
	 * @param n
	 *            ������
	 * @param buf
	 *            ת������ֽ�����,��С����С�ڣ�8+offset��
	 * @param offset
	 *            buf����洢ת���ֽ����ݵ���ʼλ��<BR>
	 *            ����ԭbufΪ9���ֽڣ�ֵΪ123456789,�������0,buf,1,�򷵻�100000000
	 */
	public static void long2byte(long n, byte buf[], int offset) {
		buf[offset] = (byte) (int) (n >> 56);
		buf[offset + 1] = (byte) (int) (n >> 48);
		buf[offset + 2] = (byte) (int) (n >> 40);
		buf[offset + 3] = (byte) (int) (n >> 32);
		buf[offset + 4] = (byte) (int) (n >> 24);
		buf[offset + 5] = (byte) (int) (n >> 16);
		buf[offset + 6] = (byte) (int) (n >> 8);
		buf[offset + 7] = (byte) (int) n;
	}

	// public static void main(String[] args) {
	// try {
	// System.out.println("10����ת��Ϊ��������...");
	// System.out.println("10����ת16���� ��" + DecToOther(123093, 16)); //
	// System.out.println("10����ת2���� ��" + DecToOther(123093, 2)); //
	// System.out.println("10����ת8���� ��" + DecToOther(123093, 8)); //
	// System.out.println("\r\n16����ת��Ϊ��������...");
	// System.out.println("16����ת10���� ��" + ToDec("1e0d5", 16)); //
	// System.out.println("16����ת2���� ��" + ToBin("1e0d5", 16)); //
	// System.out.println("16����ת8���� ��" + ToOct("1e0d5", 16)); //
	// System.out.println("\r\n2����ת��Ϊ��������...");
	// System.out.println("2����ת8���� ��" + ToOct("11110000011010101", 2)); //
	// System.out.println("2����ת10���� ��" + ToDec("11110000011010101", 2)); //
	// System.out.println("2����ת16���� ��" + ToHex("11110000011010101", 2)); //
	// System.out.println("\r\n8����ת��Ϊ��������...");
	// System.out.println("8����ת2���� ��" + ToBin("360325", 8)); //
	// System.out.println("8����ת10���� ��" + ToDec("360325", 8)); //
	// System.out.println("8����ת16���� ��" + ToHex("360325", 8)); //
	//
	// System.out.println("");
	// byte[] buf = long2byte(1234567890L);
	// System.out.println(bytes2Hex(buf)); //
	// System.out.println(byte2long(buf)); //
	// System.out.println("");
	// byte[] buf1 = new byte[10];
	// long2byte(1234567890L, buf1, 2); //
	// System.out.println(bytes2Hex(buf1));
	// System.out.println(byte2long(buf1, 2)); //
	// }
	// catch (Exception e) {
	// e.printStackTrace();
	// }
	// }

	/**
	 * ������ת����2���ֽڵ�����
	 * 
	 * @param n
	 *            ������
	 * @return ת������ֽ����飬���鳤��Ϊ2<BR>
	 *         ��������1���򷵻ص��ֽ���������Ϊ01
	 */
	public static byte[] short2byte(int n) {
		byte b[] = new byte[2];
		b[0] = (byte) (n >> 8);
		b[1] = (byte) n;
		return b;
	}

	/**
	 * ������ת����2���ֽڵ�����
	 * 
	 * @param n
	 *            ������
	 * @param buf
	 *            ת������ֽ�����,��С����С�ڣ�2+offset��
	 * @param offset
	 *            buf����洢ת���ֽ����ݵ���ʼλ��<BR>
	 *            ����ԭbufΪ9���ֽڣ�ֵΪ123456789,�������0,buf,3,�򷵻�123006789
	 */
	public static void short2byte(int n, byte buf[], int offset) {
		buf[offset] = (byte) (n >> 8);
		buf[offset + 1] = (byte) n;
	}

	/**
	 * 8,10,16����ת2����
	 * 
	 * @param sNum
	 *            ��ת������
	 * @param jz
	 *            ��ת��������ʲô����
	 * @return ת����Ķ������ַ���<BR>
	 *         ��������123,8 �򷵻�1010011;������123,10 �򷵻�1111011;������123,16,�򷵻�100100011
	 * @throws Exception
	 */
	public static String ToBin(String sNum, int jz) throws Exception {
		long tempDecimal = ToDec(sNum, jz);
		return DecToOther(tempDecimal, 2);
	}

	/**
	 * 2,8,16����ת10����
	 * 
	 * @param sNum
	 *            ��ת������
	 * @param jz
	 *            ��ת��������ʲô����
	 * @return ת�����10���Ƴ�����<BR>
	 *         ��������1111011,2 �򷵻�123;������123,8 �򷵻�83;������abc,16,�򷵻�2748
	 * @throws Exception
	 */
	public static long ToDec(String sNum, int jz) throws Exception {
		long temp = 0;
		long p = 0;
		char c;
		int num = 0;
		for (int i = sNum.length() - 1; i >= 0; i--) {
			c = sNum.charAt(i);

			if (jz == 16) {
				if (((byte) c >= 97 && (byte) c <= 102)) {
					num = c - 87;
				} else if (((byte) c >= 65 && (byte) c <= 70)) {
					num = c - 55;
				} else {
					num = Integer.parseInt(String.valueOf(c));
				}
			} else {
				num = Integer.parseInt(String.valueOf(c));
			}
			temp = (long) (temp + num * Math.pow(jz, p));
			p++;
		}
		return temp;
	}

	/**
	 * 2,8,10����ת16����
	 * 
	 * @param sNum
	 *            ��ת������
	 * @param jz
	 *            ��ת��������ʲô����
	 * @return ת�����16�����ַ��� <BR>
	 *         ��������1111011,2 �򷵻�7b;������123,8 �򷵻�53;������123,10,�򷵻�7b
	 * @throws Exception
	 */
	public static String ToHex(String sNum, int jz) throws Exception {
		long tempDecimal = ToDec(sNum, jz);
		return DecToOther(tempDecimal, 16);
	}

	/**
	 * 2,16,10����ת8����
	 * 
	 * @param sNum
	 *            ��ת������
	 * @param jz
	 *            ��ת��������ʲô����
	 * @return ת�����8�����ַ���<BR>
	 *         ��������1111011,2 �򷵻�173;������9,10 �򷵻�11;������a,16,�򷵻�12
	 * @throws Exception
	 */
	public static String ToOct(String sNum, int jz) throws Exception {
		long tempDecimal = ToDec(sNum, jz);
		return DecToOther(tempDecimal, 8);
	}
}
