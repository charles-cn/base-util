package com.haocom.util.security;

/**
 * BSAE64. <br>
 * 实现BASE64加密算法，提供加密、解密方法.
 * <p>
 * Copyright: Copyright (c) Sep 16, 2008
 * <p>
 * Company: 
 * <p>
 * Author: ywh
 * <p>
 * Version: 1.0
 * <p>
 * 代码范例：
 * <ul>
 * <li>Base64编码，参数为字节数组</li>
 * 
 * <pre>
 * 
 * byte[] sample = &quot;这是要用Base64加密的内容&quot;.getBytes();
 * 
 * String secret = Base64.encode(sample);//secret即为加密后的内容：&quot;1eLKx9Kq08NCYXNlNjS808PctcTE2sjd&quot;
 * </pre>
 * 
 * <li>Base64编码，参数为字符串</li>
 * 
 * <pre>
 * 
 * String sample = &quot;这是要用Base64加密的内容&quot;;
 * 
 * String secret = Base64.encode(sample);//secret即为加密后的内容：&quot;1eLKx9Kq08NCYXNlNjS808PctcTE2sjd&quot;
 * </pre>
 * 
 * <li>Base64解码</li>
 * 
 * <pre>
 * String secret = &quot;1eLKx9Kq08NCYXNlNjS808PctcTE2sjd&quot;;
 * byte[] sample = Base64.decode(secret);//smaple即为解码后的字节数组
 * System.out.println(new String(sample));// 打印出来的内容为：&quot;这是要用Base64加密的内容&quot;
 * </pre>
 * 
 * </ul>
 */

public class Base64 {

	/** Base64密码 */
	private static char Base64Code[] = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U',
	        'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v',
	        'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/' };

	/** Base64解码 */
	private static byte Base64Decode[] = { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
	        -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1, 63, -1, 63, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -1, -1,
	        -1, 0, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, -1, -1,
	        26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -1, -1, -1, -1, -1 };

	/**
	 * Base64解码.
	 * 
	 * @param s
	 *            需要用Base64解码的加密字符串
	 * @return 解码后的字节数组
	 */
	public static byte[] decode(String s) {
		int fillNum = 0;
		if (s.endsWith("==")) {
			fillNum = 2;
		}

		else if (s.endsWith("=")) {
			fillNum = 1;
		}

		int size = s.length() / 4 * 3 - fillNum;
		byte[] bRet = new byte[size];
		byte[] bAfterBase64Encode = s.getBytes();
		int j = 0;
		for (int i = 3; i < bAfterBase64Encode.length; i += 4) {
			int p0 = bAfterBase64Encode[i - 3];
			int p1 = bAfterBase64Encode[i - 2];
			int p2 = bAfterBase64Encode[i - 1];
			int p3 = bAfterBase64Encode[i];

			bRet[j] = (byte) ((Base64Decode[p0] << 2) | ((Base64Decode[p1] & 0x30) >>> 4));
			if (j + 1 < size) {
				bRet[j + 1] = (byte) (((Base64Decode[p1] & 0x0f) << 4) | ((Base64Decode[p2] & 0x3c) >>> 2));
			}
			if (j + 2 < size) {
				bRet[j + 2] = (byte) (((Base64Decode[p2] & 0x3) << 6) | (Base64Decode[p3] & 0x3f));
			}
			j += 3;
		}
		return bRet;
	}

	/**
	 * Base64编码.
	 * 
	 * @param b
	 *            需要加密的字节数组
	 * @return 加密后的16进制字符串
	 */
	public static String encode(byte[] b) {
		int fillNum = 0;
		if (b.length % 3 != 0) {
			fillNum = 3 - b.length % 3;
		}
		// 经过Base64编码后的字符串长度，放大1/3左右
		int size = b.length + b.length / 3 + fillNum;
		StringBuilder sbResult = new StringBuilder(size);
		int cou = 1;
		for (int i = 0; i < b.length; i++) {
			if (cou == 1) {
				sbResult.append(Base64Code[(b[i] & 0xfc) >>> 2]);
			}
			if (cou == 2) {
				sbResult.append(Base64Code[((b[i - 1] & 0x3) << 4) | ((b[i] & 0xf0) >>> 4)]);
			}
			if (cou == 3) {
				sbResult.append(Base64Code[((b[i - 1] & 0x0f) << 2) | ((b[i] & 0xc0) >>> 6)]);
				sbResult.append(Base64Code[b[i] & 0x3f]); // 取第三个字符的右6位
				cou = 0;
			}
			cou++;
		}
		if (b.length % 3 == 1) { // 余1，需补两位 =
			sbResult.append(Base64Code[(b[b.length - 1] & 0x3) << 4]);
			sbResult.append("==");
		}
		if (b.length % 3 == 2) { // 余2，补了一位，替换最后一个字符为'='
			sbResult.append(Base64Code[(b[b.length - 1] & 0x0f) << 2]);
			sbResult.append("=");
		}

		return sbResult.toString();
	}

	/**
	 * Base64编码.
	 * 
	 * @param s
	 *            需要加密的字符串
	 * @return 加密后的16进制字符串
	 */
	public static String encode(String s) {
		return encode(s.getBytes());
	}
}
