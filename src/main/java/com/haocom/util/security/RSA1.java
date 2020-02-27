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
 * RSA�㷨. <br>
 * ����RSA�㷨���滻ԭ��com.cplatform.util2.security.RSA��Ĺ���.
 * <p>
 * Copyright: Copyright (c) 2011-8-22 ����11:44:00
 * <p>
 * Company: 
 * <p>
 * 
 * @author chengfan@c-platform.com
 * @version 1.0.0 <br>
 *          <p>
 *          <h3>ʹ�÷���:</h3>
 * 
 *          <pre>
 * // ������Կ��
 * String[] key = generateKeyPair(1024);
 * System.out.println(key[0]);
 * System.out.println(key[1]);
 * 
 * // ���ַ����ָ���Կ
 * Key priKey = getPrivateKey(key[0]);
 * Key pubKey = getPublicKey(key[1]);
 * 
 * // �ֱ��ù�Կ��˽Կ����
 * String str = &quot;�»��� �����ǰ����8��22�յ磬�����Ƿ�������װ�ѿ����׶����貨�Ŀǰ����������������Ʋ��ಿ�ӡ�&quot;;
 * String str_pub = encryptToString(pubKey, str);
 * String str_pri = encryptToString(priKey, str);
 * System.out.println(str_pub);
 * System.out.println(str_pri);
 * 
 * // �ֱ���˽Կ�͹�Կ����
 * String str_pub_pri = decryptToString(priKey, str_pub);
 * String str_pri_pub = decryptToString(pubKey, str_pri);
 * 
 * System.out.println(str_pub_pri);
 * System.out.println(str_pri_pub);
 * </pre>
 */
public class RSA1 {

	/**
	 * ʹ��˽Կ��Կ����
	 * 
	 * @param privateKey
	 *            ��Կ��˽Կ
	 * @param obj
	 *            ����
	 * @return ���ܺ�����ݣ�UTF-16��
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
	 * ʹ��˽Կ��Կ����
	 * 
	 * @param privateKey
	 *            ��Կ��˽Կ
	 * @param hexString
	 *            16��������
	 * @return ���ܺ������
	 * @throws Exception
	 */
	public static String decryptToString(Key privateKey, String hexString) throws Exception {
		byte[] data = CByte.hex2Bytes(hexString);
		return new String(decrypt(privateKey, data), "UTF-16");
	}

	/**
	 * �ֱ��ù�Կ��˽Կ����
	 * 
	 * @param publicKey
	 *            ��Կ��˽Կ
	 * @param obj
	 *            ���������ݣ�UTF-16��
	 * @return ����
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
	 * �ֱ��ù�Կ��˽Կ����
	 * 
	 * @param publicKey
	 *            ��Կ��˽Կ
	 * @param obj
	 *            ���������ݣ�UTF-16��
	 * @return 16��������
	 * @throws Exception
	 */
	public static String encryptToString(Key publicKey, byte[] obj) throws Exception {
		byte[] data = encrypt(publicKey, obj);
		return CByte.bytes2Hex(data);
	}

	/**
	 * �ֱ��ù�Կ��˽Կ����
	 * 
	 * @param publicKey
	 *            ��Կ��˽Կ
	 * @param text
	 *            ����������
	 * @return 16��������
	 * @throws Exception
	 */
	public static String encryptToString(Key publicKey, String text) throws Exception {
		return encryptToString(publicKey, text.getBytes("UTF-16"));
	}

	/**
	 * ������Կ��
	 * 
	 * @param keySize
	 *            keySize
	 * @return 16�����ַ�����ʽ����Կ�ԣ�String[0]��˽Կ��String[1]�ǹ�Կ
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
	 * ���ַ����ָ���Կ
	 * 
	 * @param hexString
	 *            ˽Կ��16�����ַ���
	 * @return ˽Կ
	 * @throws Exception
	 */
	public static Key getPrivateKey(String hexString) throws Exception {
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		KeySpec keySpec = new PKCS8EncodedKeySpec(CByte.hex2Bytes(hexString));
		PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
		return privateKey;
	}

	/**
	 * ���ַ����ָ���Կ
	 * 
	 * @param hexString
	 *            ��Կ��16�����ַ���
	 * @return ��Կ
	 * @throws Exception
	 */
	public static Key getPublicKey(String hexString) throws Exception {
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		KeySpec keySpec = new X509EncodedKeySpec(CByte.hex2Bytes(hexString));
		PublicKey publicKey = keyFactory.generatePublic(keySpec);
		return publicKey;
	}

}
