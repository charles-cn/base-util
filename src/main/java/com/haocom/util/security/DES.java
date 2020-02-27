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
 * DES对称算法,本类提供DES算法的密钥生成以及使用同一密钥加密解密的方法.
 * <p>
 * Copyright: Copyright (c) Sep 16, 2008
 * <p>
 * Company: 
 * <p>
 * Author: ywh
 * <p>
 * Version: 1.0
 * <p>
 * 在这里一共提供4种形式的DES加密解密方法，其本质是一样的，只是参数略有区别。代码范例：
 * <ul>
 * <li>1</li>
 * 
 * <pre>
 * // 同一个key产生的密文是一样的，但是当重新生成另一个SecretKey时，则加密内容会改变
 * SecretKey key = DES.generateKey();
 * 
 * // 加密，使用方法为：public static byte[] encrypt(SecretKey key, byte[] info)
 * byte[] secret = DES.encrypt(key, &quot;这是需要用DES加密的内容&quot;.getBytes());//secret即为加密后的字节数组
 * 
 * // 解密，使用方法为：public static byte[] decrypt(SecretKey key, byte[] encryptedInfo)
 * byte[] sample = DES.decrypt(key, secret);// sample即为解密后的原内容
 * System.out.println(new String(sample));// 打印内容为：&quot;这是需要用DES加密的内容&quot;
 * </pre>
 * 
 * <li>2</li>
 * 
 * <pre>
 * SecretKey key = DES.generateKey();
 * 
 * // 加密，使用方法为：public static String encrypt(SecretKey key, String info)
 * String secret = DES.encrypt(key, &quot;这是需要用DES加密的内容&quot;);// secret即为加密后的16进制字符串
 * 
 * // 解密，使用方法为：public static String decrypt(SecretKey key, String encryptedInfo)
 * String sample = DES.decrypt(key, secret);// sample即为解密后的原内容
 * System.out.println(sample);// 打印内容为：&quot;这是需要用DES加密的内容&quot;
 * </pre>
 * 
 * <li>3</li>
 * 
 * <pre>
 * String key = DES.generateKeyStr();
 * 
 * // 加密，使用方法为：public static String encrypt(String key, String info)
 * String secret = DES.encrypt(key, &quot;这是需要用DES加密的内容&quot;);// secret即为加密后的字节数组
 * 
 * // 解密，使用方法为：public static String decrypt(String key, String encryptedInfo)
 * String sample = DES.decrypt(key, secret);// sample即为解密后的原内容
 * System.out.println(sample);// 打印内容为：&quot;这是需要用DES加密的内容&quot;
 * </pre>
 * 
 * <li>4</li>
 * 
 * <pre>
 * byte[] key = DES.generateKeyBytes();
 * 
 * // 加密，使用方法为：public static byte[] encrypt(byte[] key, byte[] info)
 * byte[] secret = DES.encrypt(key, &quot;这是需要用DES加密的内容&quot;.getBytes());// secret即为加密后的字节数组
 * 
 * // 解密，使用方法为：public static byte[] decrypt(byte[] key, byte[] encryptedInfo)
 * byte[] sample = DES.decrypt(key, secret);// sample即为解密后的原内容
 * System.out.println(new String(sample));// 打印内容为：&quot;这是需要用DES加密的内容&quot;
 * </pre>
 * 
 * </ul>
 */

public class DES {

	/** 算法名称 */
	private static final String algorithm = "DES";

	/**
	 * 使用秘密密钥对指定加密信息进行解密操作.
	 * 
	 * @param key
	 *            字节数组形式的私有密钥
	 * @param encryptedInfo
	 *            字节数组形式的加密信息
	 * @return 解密后的字符信息
	 * @throws Exception
	 */
	public static byte[] decrypt(byte[] key, byte[] encryptedInfo) throws Exception {
		SecretKey secretKey = encoded2Key(key);
		return decrypt(secretKey, encryptedInfo);
	}

	/**
	 * 使用秘密密钥对指定加密信息进行解密操作.
	 * 
	 * @param key
	 *            对象形式的私有密钥
	 * @param encryptedInfo
	 *            字节数组形式的加密信息
	 * @return 解密后的字节数组
	 * @throws Exception
	 */
	public static byte[] decrypt(SecretKey key, byte[] encryptedInfo) throws Exception {
		Cipher cipher = null;
		// 初始化Cipher，由于DES加密算法肯定存在，故NoSuchAlgorithmException永远也不会抛出
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
	 * 使用秘密密钥对指定加密信息进行解密操作.
	 * 
	 * @param key
	 *            对象形式的私有密钥
	 * @param encryptedInfo
	 *            十六进制字符串形式的加密信息
	 * @return 解密后的字符串
	 * @throws Exception
	 */
	public static String decrypt(SecretKey key, String encryptedInfo) throws Exception {
		return new String(decrypt(key, CByte.hex2Bytes(encryptedInfo)));
	}

	/**
	 * 使用秘密密钥对指定加密信息进行解密操作.
	 * 
	 * @param key
	 *            十六进制字符串形式的私有密钥
	 * @param encryptedInfo
	 *            十六进制字符串形式的加密信息
	 * @return 解密后的字符串
	 * @throws Exception
	 */
	public static String decrypt(String key, String encryptedInfo) throws Exception {
		byte[] keyBytes = CByte.hex2Bytes(key);
		byte[] infoBytes = CByte.hex2Bytes(encryptedInfo);
		byte[] bytes = decrypt(keyBytes, infoBytes);
		return new String(bytes);
	}

	/**
	 * 将字节数组的密钥信息转换成秘密密钥对象.
	 * 
	 * @param encoded
	 *            字节数组形式的密钥信息
	 * @return 密钥对象
	 * @throws Exception
	 */
	private static SecretKey encoded2Key(byte[] encoded) throws Exception {
		SecretKeyFactory keyFactory = null;
		// 初始化秘密密钥工厂，由于DES加密算法肯定存在，故NoSuchAlgorithmException永远也不会抛出
		try {
			keyFactory = SecretKeyFactory.getInstance(algorithm);
		}
		catch (NoSuchAlgorithmException nae) {
			// do nothing
		}
		return keyFactory.generateSecret(new DESKeySpec(encoded));
	}

	/**
	 * 使用秘密密钥对指定信息(info)进行数字签名产生一个签名字符数组.
	 * 
	 * @param key
	 *            字节数组形式的私有密钥
	 * @param info
	 *            指定信息
	 * @return 签名字节数组
	 * @throws Exception
	 */
	public static byte[] encrypt(byte[] key, byte[] info) throws Exception {
		SecretKey secretKey = encoded2Key(key);
		return encrypt(secretKey, info);
	}

	/**
	 * 使用秘密密钥对指定信息(info)进行数字签名产生一个签名字节数组.
	 * 
	 * @param key
	 *            对象形式的私有密钥
	 * @param info
	 *            指定信息
	 * @return 签名字节数组
	 * @throws Exception
	 */
	public static byte[] encrypt(SecretKey key, byte[] info) throws Exception {
		Cipher cipher = null;
		// 初始化Cipher，由于DES加密算法肯定存在，故NoSuchAlgorithmException永远也不会抛出
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
	 * 使用秘密密钥对指定信息(info)进行数字签名产生一个签名字符串.
	 * 
	 * @param key
	 *            对象形式的私有密钥
	 * @param info
	 *            指定信息
	 * @return 签名字符串
	 * @throws Exception
	 */
	public static String encrypt(SecretKey key, String info) throws Exception {
		return CByte.bytes2Hex(encrypt(key, info.getBytes()));
	}

	/**
	 * 使用秘密密钥对指定信息(info)进行数字签名产生一个签名字符串.
	 * 
	 * @param key
	 *            十六进制字符串形式的私有密钥
	 * @param info
	 *            指定信息
	 * @return 签名字符串
	 * @throws Exception
	 */
	public static String encrypt(String key, String info) throws Exception {
		byte[] bytes = encrypt(CByte.hex2Bytes(key), info.getBytes());
		return CByte.bytes2Hex(bytes);
	}

	/**
	 * 生成秘密密钥.
	 * 
	 * @return 密钥对象
	 */
	public static SecretKey generateKey() {
		// 生成密钥
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
	 * 生成秘密密钥.
	 * 
	 * @return 密钥字节数组
	 */
	public static byte[] generateKeyBytes() {
		return generateKey().getEncoded();
	}

	/**
	 * 生成秘密密钥.
	 * 
	 * @return 十六进制的密钥字符串
	 */
	public static String generateKeyStr() {
		SecretKey key = generateKey();
		return CByte.bytes2Hex(key.getEncoded());
	}

	// public static void main(String[] args) {
	// System.out.println(generateKeyStr());
	// }
}
