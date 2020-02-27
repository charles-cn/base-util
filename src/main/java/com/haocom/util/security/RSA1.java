package com.haocom.util.security;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

import com.haocom.util.CByte;

/**
 * RSA算法. <br>
 * 最新RSA算法，替换原有com.cplatform.util2.security.RSA类的功能.
 * <p>
 * Copyright: Copyright (c) 2011-8-22 上午11:44:00
 * <p>
 * Company: 
 * <p>
 * 
 * @author chengfan@c-platform.com
 * @version 1.0.0 <br>
 *          <p>
 *          <h3>使用方法:</h3>
 * 
 *          <pre>
 * // 生成密钥对
 * String[] key = generateKeyPair(1024);
 * System.out.println(key[0]);
 * System.out.println(key[1]);
 * 
 * // 从字符串恢复密钥
 * Key priKey = getPrivateKey(key[0]);
 * Key pubKey = getPublicKey(key[1]);
 * 
 * // 分别用公钥或私钥加密
 * String str = &quot;新华社 利比亚班加西8月22日电，利比亚反对派武装已控制首都的黎波里，目前正在市内清除卡扎菲残余部队。&quot;;
 * String str_pub = encryptToString(pubKey, str);
 * String str_pri = encryptToString(priKey, str);
 * System.out.println(str_pub);
 * System.out.println(str_pri);
 * 
 * // 分别用私钥和公钥解密
 * String str_pub_pri = decryptToString(priKey, str_pub);
 * String str_pri_pub = decryptToString(pubKey, str_pri);
 * 
 * System.out.println(str_pub_pri);
 * System.out.println(str_pri_pub);
 * </pre>
 */
public class RSA1 {

	/**
	 * 使用私钥或公钥解密
	 * 
	 * @param privateKey
	 *            公钥或私钥
	 * @param obj
	 *            密文
	 * @return 解密后的内容（UTF-16）
	 * @throws Exception
	 */
	public static byte[] decrypt(Key privateKey, byte[] obj) throws Exception {
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ByteArrayInputStream bais = new ByteArrayInputStream(obj);
		byte[] buf = new byte[128];
		int bufSize = 0;
		while ((bufSize = bais.read(buf)) > 0) {
			baos.write(cipher.doFinal(buf, 0, bufSize));
		}
		return baos.toByteArray();
	}

	/**
	 * 使用私钥或公钥解密
	 * 
	 * @param privateKey
	 *            公钥或私钥
	 * @param hexString
	 *            16进制密文
	 * @return 解密后的内容
	 * @throws Exception
	 */
	public static String decryptToString(Key privateKey, String hexString) throws Exception {
		byte[] data = CByte.hex2Bytes(hexString);
		return new String(decrypt(privateKey, data), "UTF-16");
	}

	/**
	 * 分别用公钥或私钥加密
	 * 
	 * @param publicKey
	 *            公钥或私钥
	 * @param obj
	 *            待加密内容（UTF-16）
	 * @return 密文
	 * @throws Exception
	 */
	public static byte[] encrypt(Key publicKey, byte[] obj) throws Exception {
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ByteArrayInputStream bais = new ByteArrayInputStream(obj);
		byte[] buf = new byte[128];
		int bufSize;
		while ((bufSize = bais.read(buf)) > 0) {
			baos.write(cipher.doFinal(buf, 0, bufSize));
		}
		return baos.toByteArray();
	}

	/**
	 * 分别用公钥或私钥加密
	 * 
	 * @param publicKey
	 *            公钥或私钥
	 * @param obj
	 *            待加密内容（UTF-16）
	 * @return 16进制密文
	 * @throws Exception
	 */
	public static String encryptToString(Key publicKey, byte[] obj) throws Exception {
		byte[] data = encrypt(publicKey, obj);
		return CByte.bytes2Hex(data);
	}

	/**
	 * 分别用公钥或私钥加密
	 * 
	 * @param publicKey
	 *            公钥或私钥
	 * @param text
	 *            待加密内容
	 * @return 16进制密文
	 * @throws Exception
	 */
	public static String encryptToString(Key publicKey, String text) throws Exception {
		return encryptToString(publicKey, text.getBytes("UTF-16"));
	}

	/**
	 * 生成密钥对
	 * 
	 * @param keySize
	 *            keySize
	 * @return 16进制字符串形式的密钥对，String[0]是私钥，String[1]是公钥
	 * @throws Exception
	 */
	public static String[] generateKeyPair(int keySize) throws Exception {
		KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
		keyPairGen.initialize(keySize);
		KeyPair keyPair = keyPairGen.generateKeyPair();
		RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
		RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
		String[] keys = new String[2];
		keys[0] = CByte.bytes2Hex(privateKey.getEncoded());
		keys[1] = CByte.bytes2Hex(publicKey.getEncoded());
		return keys;
	}

	/**
	 * 从字符串恢复密钥
	 * 
	 * @param hexString
	 *            私钥的16进制字符串
	 * @return 私钥
	 * @throws Exception
	 */
	public static Key getPrivateKey(String hexString) throws Exception {
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		KeySpec keySpec = new PKCS8EncodedKeySpec(CByte.hex2Bytes(hexString));
		PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
		return privateKey;
	}

	/**
	 * 从字符串恢复密钥
	 * 
	 * @param hexString
	 *            公钥的16进制字符串
	 * @return 公钥
	 * @throws Exception
	 */
	public static Key getPublicKey(String hexString) throws Exception {
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		KeySpec keySpec = new X509EncodedKeySpec(CByte.hex2Bytes(hexString));
		PublicKey publicKey = keyFactory.generatePublic(keySpec);
		return publicKey;
	}

}
