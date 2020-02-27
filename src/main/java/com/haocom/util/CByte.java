package com.haocom.util;

import java.util.Arrays;

/**
 * 字节转换工具. <br>
 * 字节转换工具,可以实现字节与各个进制数字之间的转换.
 * <p>
 * 修改历史: 2009-8-10 下午05:03:37 ChengFan <br>
 * 修改bytes2Hex方法、hex2Bytes方法，重写实现方法，改进性能。<br>
 * <p>
 * Copyright: Copyright (c) Sep 16, 2008
 * <p>
 * Company: 
 * <p>
 * Author: 周燕
 * <p>
 * Version: 1.0
 */

public class CByte {

	/** 表示16进制的各个数字 */
	private static String HexCode[] = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };

	/**
	 * 字节转换成16进制字符串
	 * 
	 * @param b
	 *            字节
	 * @return 16进制字符串<BR>
	 *         例如：输入（byte）11 返回的是 0b
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
	 * 字节数组转换成整数
	 * 
	 * @param b
	 *            字节数组
	 * @return 10进制整数<BR>
	 *         字节数组,必须是包含4个字节，例如输入byte b[4];其中 b[0] = 1; b[1] = 2; b[2] = 3;
	 *         b[3] = 4;则返回10进制整数16909060
	 */
	public static int byte2int(byte b[]) {
		return b[3] & 0xff | (b[2] & 0xff) << 8 | (b[1] & 0xff) << 16 | (b[0] & 0xff) << 24;
	}

	/**
	 * 字节数组转换成整数
	 * 
	 * @param b
	 *            字节数组
	 * @param offset
	 *            偏移量，从数组偏移位置开始取4个Byte做转换
	 * @return 10进制整数<BR>
	 *         例如输入byte b[5];其中 b[0] = 1; b[1] = 2; b[2] = 3; b[3] = 4;b[4] =
	 *         5;则返回10进制整数33752069。注意b必须至少包含（4+offset）个字节
	 */
	public static int byte2int(byte b[], int offset) {
		return b[offset + 3] & 0xff | (b[offset + 2] & 0xff) << 8 | (b[offset + 1] & 0xff) << 16 | (b[offset] & 0xff) << 24;
	}

	/**
	 * 字节数组转换成长整数
	 * 
	 * @param b
	 *            字节数组
	 * @return 10进制长整数<BR>
	 *         字节数组,必须是包含8个字节，例如输入byte b[8];其中 b[0] = 1; b[1] = 2; b[2] = 3;
	 *         b[3] = 4;b[4] = 5;b[5] = 6;b[6] = 7;b[7] =
	 *         8;则返回长整数72623859790382856
	 */
	public static long byte2long(byte b[]) {
		return ((long) b[7] & (long) 255) | ((long) b[6] & (long) 255) << 8 | ((long) b[5] & (long) 255) << 16 | ((long) b[4] & (long) 255) << 24
		        | ((long) b[3] & (long) 255) << 32 | ((long) b[2] & (long) 255) << 40 | ((long) b[1] & (long) 255) << 48
		        | ((long) b[0] & (long) 255) << 56;
	}

	/**
	 * 字节数组转换成长整数
	 * 
	 * @param b
	 *            字节数组
	 * @param offset
	 *            偏移量，从数组偏移位置开始取8个Byte做转换
	 * @return 10进制长整数<BR>
	 *         例如输入byte b[9];其中 b[0] = 1; b[1] = 2; b[2] = 3; b[3] = 4;b[4] =
	 *         5;b[5] = 6;b[6] = 7;b[7]
	 *         =8;则返回长整数144964032628459529。注意b必须至少包含（7+offset）个字节
	 */
	public static long byte2long(byte b[], int offset) {
		return (long) b[offset + 7] & (long) 255 | ((long) b[offset + 6] & (long) 255) << 8 | ((long) b[offset + 5] & (long) 255) << 16
		        | ((long) b[offset + 4] & (long) 255) << 24 | ((long) b[offset + 3] & (long) 255) << 32 | ((long) b[offset + 2] & (long) 255) << 40
		        | ((long) b[offset + 1] & (long) 255) << 48 | ((long) b[offset] & (long) 255) << 56;
	}

	/**
	 * 字节数组转换成16进制字符串
	 * 
	 * @param b
	 *            字节数组
	 * @return 16进制字符串<BR>
	 *         例如输入byte b[4],其中b[0]=1;b[1]=2;b[2]=13;b[3]=14;则返回16进制数01020d0e
	 */
	public static String bytes2Hex(byte b[]) {
		// 结果数组
		char[] chars = new char[b.length * 2];
		int charsIndex = 0;
		int intValue;
		int intValueHi; // 高位
		int intValueLo; // 低位
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
	 * 长整数转换为2,8,16进制字符串
	 * 
	 * @param iNum
	 *            待转换数字，为长整型
	 * @param jz
	 *            需要转换为什么进制
	 * @return 转换后所得进制的字符串<BR>
	 *         例如输入11,2 则返回1011;若输入11,8 则返回13;若输入11,16 则返回b
	 * @throws Exception
	 */
	public static String DecToOther(long iNum, int jz) throws Exception {
		long consult = 0; // 商
		long rest = 0; // 余数
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
	 * 16进制字符串转换成字节数组
	 * 
	 * @param s
	 *            16进制字符串
	 * @return 字节数组<BR>
	 *         例如输入1a,则返回的字节数组的内容为26
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
			} else if (c >= '０' && c <= '９') {
				byteValue = c - '０';
			} else if (c >= 'ａ' && c <= 'ｆ') {
				byteValue = c - 'ａ' + 10;
			} else if (c >= 'Ａ' && c <= 'Ｆ') {
				byteValue = c - 'Ａ' + 10;
			} else {
				continue;
			}
			if (position == 0) {
				intValue = byteValue;
				position++;
				bytes[byteSize] = (byte) intValue;
			} else {
				// 第二位
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
	 * 整数转换成字节数组
	 * 
	 * @param n
	 *            10进制整数
	 * @return 字节数组<BR>
	 *         例如输入数字1,则返回字节数组的内容为0001
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
	 * 整数转换成字节数组
	 * 
	 * @param n
	 *            10进制整数
	 * @param buf
	 *            转换后的字节数组,大小不得小于（4+offset）
	 * @param offset
	 *            buf数组存储转换字节数据的起始位置<BR>
	 *            例如原buf为6个字节，值为123456,输入参数1,buf,2,则返回120009
	 */
	public static void int2byte(int n, byte buf[], int offset) {
		buf[offset] = (byte) (n >> 24);
		buf[offset + 1] = (byte) (n >> 16);
		buf[offset + 2] = (byte) (n >> 8);
		buf[offset + 3] = (byte) n;
	}

	/**
	 * 长整数转换成8个字节的数组
	 * 
	 * @param n
	 *            长整数
	 * @return 转换后的字节数组，数组长度为8<BR>
	 *         例如输入长整数72623859790382856l，则返回的字节数组内容为12345678
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
	 * 长整数转换成8个字节的数组
	 * 
	 * @param n
	 *            长整数
	 * @param buf
	 *            转换后的字节数组,大小不得小于（8+offset）
	 * @param offset
	 *            buf数组存储转换字节数据的起始位置<BR>
	 *            例如原buf为9个字节，值为123456789,输入参数0,buf,1,则返回100000000
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
	// System.out.println("10进制转换为其它进制...");
	// System.out.println("10进制转16进制 ：" + DecToOther(123093, 16)); //
	// System.out.println("10进制转2进制 ：" + DecToOther(123093, 2)); //
	// System.out.println("10进制转8进制 ：" + DecToOther(123093, 8)); //
	// System.out.println("\r\n16进制转换为其它进制...");
	// System.out.println("16进制转10进制 ：" + ToDec("1e0d5", 16)); //
	// System.out.println("16进制转2进制 ：" + ToBin("1e0d5", 16)); //
	// System.out.println("16进制转8进制 ：" + ToOct("1e0d5", 16)); //
	// System.out.println("\r\n2进制转换为其它进制...");
	// System.out.println("2进制转8进制 ：" + ToOct("11110000011010101", 2)); //
	// System.out.println("2进制转10进制 ：" + ToDec("11110000011010101", 2)); //
	// System.out.println("2进制转16进制 ：" + ToHex("11110000011010101", 2)); //
	// System.out.println("\r\n8进制转换为其它进制...");
	// System.out.println("8进制转2进制 ：" + ToBin("360325", 8)); //
	// System.out.println("8进制转10进制 ：" + ToDec("360325", 8)); //
	// System.out.println("8进制转16进制 ：" + ToHex("360325", 8)); //
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
	 * 短整数转换成2个字节的数组
	 * 
	 * @param n
	 *            短整数
	 * @return 转换后的字节数组，数组长度为2<BR>
	 *         例如输入1，则返回的字节数组内容为01
	 */
	public static byte[] short2byte(int n) {
		byte b[] = new byte[2];
		b[0] = (byte) (n >> 8);
		b[1] = (byte) n;
		return b;
	}

	/**
	 * 短整数转换成2个字节的数组
	 * 
	 * @param n
	 *            短整数
	 * @param buf
	 *            转换后的字节数组,大小不得小于（2+offset）
	 * @param offset
	 *            buf数组存储转换字节数据的起始位置<BR>
	 *            例如原buf为9个字节，值为123456789,输入参数0,buf,3,则返回123006789
	 */
	public static void short2byte(int n, byte buf[], int offset) {
		buf[offset] = (byte) (n >> 8);
		buf[offset + 1] = (byte) n;
	}

	/**
	 * 8,10,16进制转2进制
	 * 
	 * @param sNum
	 *            待转换数字
	 * @param jz
	 *            待转换数字是什么进制
	 * @return 转换后的二进制字符串<BR>
	 *         例如输入123,8 则返回1010011;若输入123,10 则返回1111011;若输入123,16,则返回100100011
	 * @throws Exception
	 */
	public static String ToBin(String sNum, int jz) throws Exception {
		long tempDecimal = ToDec(sNum, jz);
		return DecToOther(tempDecimal, 2);
	}

	/**
	 * 2,8,16进制转10进制
	 * 
	 * @param sNum
	 *            待转换数字
	 * @param jz
	 *            待转换数字是什么进制
	 * @return 转换后的10进制长整数<BR>
	 *         例如输入1111011,2 则返回123;若输入123,8 则返回83;若输入abc,16,则返回2748
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
	 * 2,8,10进制转16进制
	 * 
	 * @param sNum
	 *            待转换数字
	 * @param jz
	 *            待转换数字是什么进制
	 * @return 转换后的16进制字符串 <BR>
	 *         例如输入1111011,2 则返回7b;若输入123,8 则返回53;若输入123,10,则返回7b
	 * @throws Exception
	 */
	public static String ToHex(String sNum, int jz) throws Exception {
		long tempDecimal = ToDec(sNum, jz);
		return DecToOther(tempDecimal, 16);
	}

	/**
	 * 2,16,10进制转8进制
	 * 
	 * @param sNum
	 *            待转换数字
	 * @param jz
	 *            待转换数字是什么进制
	 * @return 转换后的8进制字符串<BR>
	 *         例如输入1111011,2 则返回173;若输入9,10 则返回11;若输入a,16,则返回12
	 * @throws Exception
	 */
	public static String ToOct(String sNum, int jz) throws Exception {
		long tempDecimal = ToDec(sNum, jz);
		return DecToOther(tempDecimal, 8);
	}
}
