package com.haocom.util.security;

import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import com.haocom.util.CByte;

/**
 * DES. <br>
 * DES�Գ��㷨,�����ṩDES�㷨����Կ�����Լ�ʹ��ͬһ��Կ���ܽ��ܵķ���.
 * <p>
 * Copyright: Copyright (c) Sep 16, 2008
 * <p>
 * Company: 
 * <p>
 * Author: ywh
 * <p>
 * Version: 1.0
 * <p>
 * ������һ���ṩ4����ʽ��DES���ܽ��ܷ������䱾����һ���ģ�ֻ�ǲ����������𡣴��뷶����
 * <ul>
 * <li>1</li>
 * 
 * <pre>
 * // ͬһ��key������������һ���ģ����ǵ�����������һ��SecretKeyʱ����������ݻ�ı�
 * SecretKey key = DES.generateKey();
 * 
 * // ���ܣ�ʹ�÷���Ϊ��public static byte[] encrypt(SecretKey key, byte[] info)
 * byte[] secret = DES.encrypt(key, &quot;������Ҫ��DES���ܵ�����&quot;.getBytes());//secret��Ϊ���ܺ���ֽ�����
 * 
 * // ���ܣ�ʹ�÷���Ϊ��public static byte[] decrypt(SecretKey key, byte[] encryptedInfo)
 * byte[] sample = DES.decrypt(key, secret);// sample��Ϊ���ܺ��ԭ����
 * System.out.println(new String(sample));// ��ӡ����Ϊ��&quot;������Ҫ��DES���ܵ�����&quot;
 * </pre>
 * 
 * <li>2</li>
 * 
 * <pre>
 * SecretKey key = DES.generateKey();
 * 
 * // ���ܣ�ʹ�÷���Ϊ��public static String encrypt(SecretKey key, String info)
 * String secret = DES.encrypt(key, &quot;������Ҫ��DES���ܵ�����&quot;);// secret��Ϊ���ܺ��16�����ַ���
 * 
 * // ���ܣ�ʹ�÷���Ϊ��public static String decrypt(SecretKey key, String encryptedInfo)
 * String sample = DES.decrypt(key, secret);// sample��Ϊ���ܺ��ԭ����
 * System.out.println(sample);// ��ӡ����Ϊ��&quot;������Ҫ��DES���ܵ�����&quot;
 * </pre>
 * 
 * <li>3</li>
 * 
 * <pre>
 * String key = DES.generateKeyStr();
 * 
 * // ���ܣ�ʹ�÷���Ϊ��public static String encrypt(String key, String info)
 * String secret = DES.encrypt(key, &quot;������Ҫ��DES���ܵ�����&quot;);// secret��Ϊ���ܺ���ֽ�����
 * 
 * // ���ܣ�ʹ�÷���Ϊ��public static String decrypt(String key, String encryptedInfo)
 * String sample = DES.decrypt(key, secret);// sample��Ϊ���ܺ��ԭ����
 * System.out.println(sample);// ��ӡ����Ϊ��&quot;������Ҫ��DES���ܵ�����&quot;
 * </pre>
 * 
 * <li>4</li>
 * 
 * <pre>
 * byte[] key = DES.generateKeyBytes();
 * 
 * // ���ܣ�ʹ�÷���Ϊ��public static byte[] encrypt(byte[] key, byte[] info)
 * byte[] secret = DES.encrypt(key, &quot;������Ҫ��DES���ܵ�����&quot;.getBytes());// secret��Ϊ���ܺ���ֽ�����
 * 
 * // ���ܣ�ʹ�÷���Ϊ��public static byte[] decrypt(byte[] key, byte[] encryptedInfo)
 * byte[] sample = DES.decrypt(key, secret);// sample��Ϊ���ܺ��ԭ����
 * System.out.println(new String(sample));// ��ӡ����Ϊ��&quot;������Ҫ��DES���ܵ�����&quot;
 * </pre>
 * 
 * </ul>
 */

public class DES {

	/** �㷨���� */
	private static final String algorithm = "DES";

	/**
	 * ʹ��������Կ��ָ��������Ϣ���н��ܲ���.
	 * 
	 * @param key
	 *            �ֽ�������ʽ��˽����Կ
	 * @param encryptedInfo
	 *            �ֽ�������ʽ�ļ�����Ϣ
	 * @return ���ܺ���ַ���Ϣ
	 * @throws Exception
	 */
	public static byte[] decrypt(byte[] key, byte[] encryptedInfo) throws Exception {
		SecretKey secretKey = encoded2Key(key);
		return decrypt(secretKey, encryptedInfo);
	}

	/**
	 * ʹ��������Կ��ָ��������Ϣ���н��ܲ���.
	 * 
	 * @param key
	 *            ������ʽ��˽����Կ
	 * @param encryptedInfo
	 *            �ֽ�������ʽ�ļ�����Ϣ
	 * @return ���ܺ���ֽ�����
	 * @throws Exception
	 */
	public static byte[] decrypt(SecretKey key, byte[] encryptedInfo) throws Exception {
		Cipher cipher = null;
		// ��ʼ��Cipher������DES�����㷨�϶����ڣ���NoSuchAlgorithmException��ԶҲ�����׳�
		try {
			cipher = Cipher.getInstance(algorithm);
		}
		catch (NoSuchAlgorithmException nae) {
			// do nothing
		}
		cipher.init(Cipher.DECRYPT_MODE, key);
		return cipher.doFinal(encryptedInfo);
	}

	/**
	 * ʹ��������Կ��ָ��������Ϣ���н��ܲ���.
	 * 
	 * @param key
	 *            ������ʽ��˽����Կ
	 * @param encryptedInfo
	 *            ʮ�������ַ�����ʽ�ļ�����Ϣ
	 * @return ���ܺ���ַ���
	 * @throws Exception
	 */
	public static String decrypt(SecretKey key, String encryptedInfo) throws Exception {
		return new String(decrypt(key, CByte.hex2Bytes(encryptedInfo)));
	}

	/**
	 * ʹ��������Կ��ָ��������Ϣ���н��ܲ���.
	 * 
	 * @param key
	 *            ʮ�������ַ�����ʽ��˽����Կ
	 * @param encryptedInfo
	 *            ʮ�������ַ�����ʽ�ļ�����Ϣ
	 * @return ���ܺ���ַ���
	 * @throws Exception
	 */
	public static String decrypt(String key, String encryptedInfo) throws Exception {
		byte[] keyBytes = CByte.hex2Bytes(key);
		byte[] infoBytes = CByte.hex2Bytes(encryptedInfo);
		byte[] bytes = decrypt(keyBytes, infoBytes);
		return new String(bytes);
	}

	/**
	 * ���ֽ��������Կ��Ϣת����������Կ����.
	 * 
	 * @param encoded
	 *            �ֽ�������ʽ����Կ��Ϣ
	 * @return ��Կ����
	 * @throws Exception
	 */
	private static SecretKey encoded2Key(byte[] encoded) throws Exception {
		SecretKeyFactory keyFactory = null;
		// ��ʼ��������Կ����������DES�����㷨�϶����ڣ���NoSuchAlgorithmException��ԶҲ�����׳�
		try {
			keyFactory = SecretKeyFactory.getInstance(algorithm);
		}
		catch (NoSuchAlgorithmException nae) {
			// do nothing
		}
		return keyFactory.generateSecret(new DESKeySpec(encoded));
	}

	/**
	 * ʹ��������Կ��ָ����Ϣ(info)��������ǩ������һ��ǩ���ַ�����.
	 * 
	 * @param key
	 *            �ֽ�������ʽ��˽����Կ
	 * @param info
	 *            ָ����Ϣ
	 * @return ǩ���ֽ�����
	 * @throws Exception
	 */
	public static byte[] encrypt(byte[] key, byte[] info) throws Exception {
		SecretKey secretKey = encoded2Key(key);
		return encrypt(secretKey, info);
	}

	/**
	 * ʹ��������Կ��ָ����Ϣ(info)��������ǩ������һ��ǩ���ֽ�����.
	 * 
	 * @param key
	 *            ������ʽ��˽����Կ
	 * @param info
	 *            ָ����Ϣ
	 * @return ǩ���ֽ�����
	 * @throws Exception
	 */
	public static byte[] encrypt(SecretKey key, byte[] info) throws Exception {
		Cipher cipher = null;
		// ��ʼ��Cipher������DES�����㷨�϶����ڣ���NoSuchAlgorithmException��ԶҲ�����׳�
		try {
			cipher = Cipher.getInstance(algorithm);
		}
		catch (NoSuchAlgorithmException nae) {
			// do nothing
		}
		cipher.init(Cipher.ENCRYPT_MODE, key);
		return cipher.doFinal(info);
	}

	/**
	 * ʹ��������Կ��ָ����Ϣ(info)��������ǩ������һ��ǩ���ַ���.
	 * 
	 * @param key
	 *            ������ʽ��˽����Կ
	 * @param info
	 *            ָ����Ϣ
	 * @return ǩ���ַ���
	 * @throws Exception
	 */
	public static String encrypt(SecretKey key, String info) throws Exception {
		return CByte.bytes2Hex(encrypt(key, info.getBytes()));
	}

	/**
	 * ʹ��������Կ��ָ����Ϣ(info)��������ǩ������һ��ǩ���ַ���.
	 * 
	 * @param key
	 *            ʮ�������ַ�����ʽ��˽����Կ
	 * @param info
	 *            ָ����Ϣ
	 * @return ǩ���ַ���
	 * @throws Exception
	 */
	public static String encrypt(String key, String info) throws Exception {
		byte[] bytes = encrypt(CByte.hex2Bytes(key), info.getBytes());
		return CByte.bytes2Hex(bytes);
	}

	/**
	 * ����������Կ.
	 * 
	 * @return ��Կ����
	 */
	public static SecretKey generateKey() {
		// ������Կ
		KeyGenerator keygen = null;
		try {
			keygen = KeyGenerator.getInstance(algorithm);
		}
		catch (NoSuchAlgorithmException nae) {
			// do nothing
		}

		return keygen.generateKey();
	}

	/**
	 * ����������Կ.
	 * 
	 * @return ��Կ�ֽ�����
	 */
	public static byte[] generateKeyBytes() {
		return generateKey().getEncoded();
	}

	/**
	 * ����������Կ.
	 * 
	 * @return ʮ�����Ƶ���Կ�ַ���
	 */
	public static String generateKeyStr() {
		SecretKey key = generateKey();
		return CByte.bytes2Hex(key.getEncoded());
	}

	// public static void main(String[] args) {
	// System.out.println(generateKeyStr());
	// }
}
