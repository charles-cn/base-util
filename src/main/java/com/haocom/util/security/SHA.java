package com.haocom.util.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.haocom.util.CByte;

/**
 * ����ժҪ��SHA. <br>
 * �ṩSHA-1�㷨�ı���ժҪ����.
 * <p>
 * Copyright: Copyright (c) Sep 16, 2008
 * <p>
 * Company: 
 * <p>
 * Author: ywh
 * <p>
 * Version: 1.0
 * <p>
 * ���뷶����
 * <ul>
 * <li>����Ϊ�ֽ����飬��������Ϊ�ֽ�����</li>
 * 
 * <pre>
 * byte[] sample = &quot;������ҪSHA���ܵ�����&quot;.getBytes();
 * byte[] secret = SHA.digest2Bytes(sample);// secret��Ϊ���ܺ���ַ�����
 * System.out.println(CByte.bytes2Hex(secret));//��ӡ����Ϊ���ܺ��16�����ַ���
 * </pre>
 * 
 * <li>����Ϊ�ֽ����飬��������Ϊ�ַ���</li>
 * 
 * <pre>
 * 
 * byte[] sample = &quot;������ҪSHA���ܵ�����&quot;.getBytes();
 * 
 * String secret = SHA.digest2Str(sample);// secret��Ϊ���ܺ��16�����ַ���
 * </pre>
 * 
 * <li>����Ϊ�ַ�������������Ϊ�ַ���</li>
 * 
 * <pre>
 * 
 * String sample = &quot;������ҪSHA���ܵ�����&quot;;
 * 
 * String secret = SHA.digest2Str(sample);// secret��Ϊ���ܺ��16�����ַ���
 * </pre>
 * 
 * </ul>
 */

public class SHA {

	/** �㷨�� */
	private static final String algorithm = "SHA-1";

	/**
	 * ���ֽ����鱨�Ľ���ժҪ��������ժҪ�ֽ�����.
	 * 
	 * @param bytes
	 *            ������Ϣ
	 * @return ժҪ��Ϣ
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
	 * ���ֽ����鱨�Ľ���ժҪ��������ժҪ�ַ���.
	 * 
	 * @param bytes
	 *            ������Ϣ
	 * @return ժҪ��Ϣ��ʮ�����Ƶ��ַ���
	 */
	public static String digest2Str(byte[] bytes) {
		return CByte.bytes2Hex(digest2Bytes(bytes));
	}

	/**
	 * ���ַ������Ľ���ժҪ��������ժҪ�ַ���.
	 * 
	 * @param str
	 *            ������Ϣ
	 * @return ժҪ��Ϣ��ʮ�����Ƶ��ַ���
	 */
	public static String digest2Str(String str) {
		return digest2Str(str.getBytes());
	}

	// public static void main(String[] args) {
	//
	// }
}
