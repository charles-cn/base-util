package com.haocom.util.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.haocom.util.CByte;

/**
 * 报文摘要－SHA. <br>
 * 提供SHA-1算法的报文摘要方法.
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
 * <li>参数为字节数组，返回类型为字节数组</li>
 * 
 * <pre>
 * byte[] sample = &quot;这是需要SHA加密的内容&quot;.getBytes();
 * byte[] secret = SHA.digest2Bytes(sample);// secret即为加密后的字符数组
 * System.out.println(CByte.bytes2Hex(secret));//打印内容为加密后的16进制字符串
 * </pre>
 * 
 * <li>参数为字节数组，返回类型为字符串</li>
 * 
 * <pre>
 * 
 * byte[] sample = &quot;这是需要SHA加密的内容&quot;.getBytes();
 * 
 * String secret = SHA.digest2Str(sample);// secret即为加密后的16进制字符串
 * </pre>
 * 
 * <li>参数为字符串，返回类型为字符串</li>
 * 
 * <pre>
 * 
 * String sample = &quot;这是需要SHA加密的内容&quot;;
 * 
 * String secret = SHA.digest2Str(sample);// secret即为加密后的16进制字符串
 * </pre>
 * 
 * </ul>
 */

public class SHA {

	/** 算法名 */
	private static final String algorithm = "SHA-1";

	/**
	 * 将字节数组报文进行摘要处理，返回摘要字节数字.
	 * 
	 * @param bytes
	 *            报文信息
	 * @return 摘要信息
	 */
	public static byte[] digest2Bytes(byte[] bytes) {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance(algorithm);

		}
		catch (NoSuchAlgorithmException nae) {
			// do nothing
		}
		return md.digest(bytes);
	}

	/**
	 * 将字节数组报文进行摘要处理，返回摘要字符串.
	 * 
	 * @param bytes
	 *            报文信息
	 * @return 摘要信息，十六进制的字符串
	 */
	public static String digest2Str(byte[] bytes) {
		return CByte.bytes2Hex(digest2Bytes(bytes));
	}

	/**
	 * 将字符串报文进行摘要处理，返回摘要字符串.
	 * 
	 * @param str
	 *            报文信息
	 * @return 摘要信息，十六进制的字符串
	 */
	public static String digest2Str(String str) {
		return digest2Str(str.getBytes());
	}

	// public static void main(String[] args) {
	//
	// }
}
