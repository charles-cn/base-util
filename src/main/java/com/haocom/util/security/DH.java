package com.haocom.util.security;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.KeyAgreement;
import javax.crypto.SecretKey;
import javax.crypto.interfaces.DHPublicKey;
import javax.crypto.spec.DHParameterSpec;

import com.haocom.util.CByte;

/**
 * Diffie-Hellman密钥. <br>
 * 公开密钥密码体制的奠基人Diffie和Hellman所提出的 "指数密钥一致协议"(Exponential Key Agreement
 * Protocol),该协议不要求别的安全性先决条件,允许两名用户在公开媒体上交换信息以生成"一致"的,可以共享的密钥。<BR>
 * 本类提供密钥对的生成以及双方根据对方公钥生成DES密钥的方法.
 * <p>
 * Copyright: Copyright (c) Sep 16, 2008
 * <p>
 * Company: 
 * <p>
 * Author: ywh
 * <p>
 * Version: 1.0
 * <P>
 * 
 * <pre>
 * 代码范例：
 * try {
 * 	// Alice生成密钥对.
 * 	KeyStrPair aliceKeyPair = DH.generateAliceKeyPair();
 * 	System.out.println(&quot;aliceKeyPair\t&quot; + aliceKeyPair.toString());
 * 	// Alice将公钥发送给Bob.
 * 	// ......
 * 	// Bob使用Alice的公钥生成密钥对.
 * 	KeyStrPair bobKeyPair = DH.generateBobKeyPair(aliceKeyPair.getPublicKey());
 * 	System.out.println(&quot;bobKeyPair\t&quot; + bobKeyPair.toString());
 * 	// Bob将公钥发送给Alice.
 * 	// ......
 * 	// Bob使用Alice的公钥和自己的私钥生成秘密密钥.
 * 	String bobSecretKey = DH.generateAgreementKey(bobKeyPair.getPrivateKey(), aliceKeyPair.getPublicKey());
 * 	System.out.println(&quot;bobSecretKey\t&quot; + bobSecretKey);
 * 	// Alice使用Bob的公钥和自己的私钥生成秘密密钥.
 * 	String aliceSecretKey = DH.generateAgreementKey(aliceKeyPair.getPrivateKey(), bobKeyPair.getPublicKey());
 * 	System.out.println(&quot;aliceSecretKey\t&quot; + aliceSecretKey);
 * 	// 比较两个密钥应该是一致的.
 * 	if (aliceSecretKey.equals(bobSecretKey)) {
 * 		System.out.println(&quot;密钥交换成功！&quot;);
 * 	} else {
 * 		System.out.println(&quot;密钥交换失败！&quot;);
 * 	}
 * }
 * catch (Exception e) {
 * 	e.printStackTrace();
 * }
 * </pre>
 */

public class DH {

	/** 算法名称 */
	private static final String algorithm = "DH";

	/**
	 * 将经过PKCS8编码的字节数组转成私有密钥.
	 * 
	 * @param encoded
	 *            经过PKCS8编码的字节数组
	 * @return 私有密钥
	 * @throws Exception
	 */
	private static PrivateKey encoded2privateKey(byte[] encoded) throws Exception {
		PKCS8EncodedKeySpec priKeySpec = new PKCS8EncodedKeySpec(encoded);
		KeyFactory keyFactory = null;
		// 初始化密钥工厂，由于DSA加密算法肯定存在，故NoSuchAlgorithmException永远也不会抛出
		try {
			keyFactory = KeyFactory.getInstance(algorithm);
		}
		catch (NoSuchAlgorithmException nae) {
			// do nothing
		}
		return keyFactory.generatePrivate(priKeySpec);
	}

	/**
	 * 将经过X509编码的字节数组转成公共密钥.
	 * 
	 * @param encoded
	 *            经过PKCS8编码的字节数组
	 * @return 公共密钥
	 * @throws Exception
	 */
	private static PublicKey encoded2publicKey(byte[] encoded) throws Exception {
		X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(encoded);
		KeyFactory keyFactory = null;
		// 初始化密钥工厂，由于DSA加密算法肯定存在，故NoSuchAlgorithmException永远也不会抛出
		try {
			keyFactory = KeyFactory.getInstance(algorithm);
		}
		catch (NoSuchAlgorithmException nae) {
			// do nothing
		}
		return keyFactory.generatePublic(pubKeySpec);
	}

	/**
	 * 使用各自的密钥和对方的公钥生成相同的秘密密钥.
	 * 
	 * @param privateKey
	 *            自己的私钥，字节数组
	 * @param publicKey
	 *            对方的公钥，字节数组
	 * @return 秘密密钥，字节数组
	 * @throws Exception
	 */
	public static byte[] generateAgreementKey(byte[] privateKey, byte[] publicKey) throws Exception {
		PrivateKey priKey = encoded2privateKey(privateKey);
		PublicKey pubKey = encoded2publicKey(publicKey);
		SecretKey secretKey = generateAgreementKey(priKey, pubKey);
		return secretKey.getEncoded();
	}

	/**
	 * 使用各自的密钥和对方的公钥生成相同的秘密密钥.
	 * 
	 * @param privateKey
	 *            自己的私钥，对象
	 * @param publicKey
	 *            对方的公钥，对象
	 * @return 秘密密钥，对象
	 * @throws Exception
	 */
	public static SecretKey generateAgreementKey(PrivateKey privateKey, PublicKey publicKey) throws Exception {
		KeyAgreement keyAgree = null;
		try {
			keyAgree = KeyAgreement.getInstance(algorithm);
		}
		catch (NoSuchAlgorithmException nae) {
			// do nothing
		}
		keyAgree.init(privateKey);
		keyAgree.doPhase(publicKey, true);
		return keyAgree.generateSecret("DES");
	}

	/**
	 * 使用各自的密钥和对方的公钥生成相同的秘密密钥.
	 * 
	 * @param privateKey
	 *            自己的私钥，十六进制字符串
	 * @param publicKey
	 *            对方的公钥，十六进制字符串
	 * @return 秘密密钥，十六进制字符串
	 * @throws Exception
	 */
	public static String generateAgreementKey(String privateKey, String publicKey) throws Exception {
		byte[] priBytes = CByte.hex2Bytes(privateKey);
		byte[] pubBytes = CByte.hex2Bytes(publicKey);
		byte[] secretKey = generateAgreementKey(priBytes, pubBytes);
		return CByte.bytes2Hex(secretKey);
	}

	/**
	 * 生成密钥对，密钥以对象形式返回.<BR>
	 * 其中：公钥保存在publicKey字段；私钥保存privateKey字段.
	 * 
	 * @return 密钥对
	 */
	public static KeyStrPair generateAliceKeyPair() {
		KeyPair keyPair = getAliceKeyPair(); // 生成密钥组
		PublicKey pubKey = keyPair.getPublic();
		PrivateKey priKey = keyPair.getPrivate();
		return new KeyStrPair(CByte.bytes2Hex(pubKey.getEncoded()), CByte.bytes2Hex(priKey.getEncoded()));
	}

	/**
	 * 生成密钥对，密钥以序列化对象格式保存到文件中.<BR>
	 * 其中：公钥保存在“public.key”文件；私钥保存在“private.key”.
	 * 
	 * @param savePath
	 *            文件保存路径
	 * @throws IOException
	 */
	public static void generateAliceKeyPair(String savePath) throws IOException {
		KeyPair keyPair = getAliceKeyPair();
		ObjectOutputStream out = null;
		// 将私钥保存到private.key文件
		try {
			out = new ObjectOutputStream(new FileOutputStream(savePath + File.separator + "private.key"));
			out.writeObject(keyPair.getPrivate());
		}
		catch (IOException ioe) {
			throw ioe;
		}
		finally {
			out.close();
		}
		// 将公钥保存到public.key文件
		try {
			out = new ObjectOutputStream(new FileOutputStream(savePath + File.separator + "public.key"));
			out.writeObject(keyPair.getPublic());
		}
		catch (IOException ioe) {
			throw ioe;
		}
		finally {
			out.close();
		}
	}

	/**
	 * 使用Alice提供的公共密钥生成Bob方的密钥对，密钥以对象形式返回.<BR>
	 * 其中：公钥保存在publicKey字段；私钥保存privateKey字段.
	 * 
	 * @param alicePubKey
	 *            Alice提供的公共密钥
	 * @return 十六进制字符串形式的密钥对
	 * @throws IOException
	 */
	public static KeyStrPair generateBobKeyPair(byte[] alicePubKey) throws Exception {
		return generateBobKeyPair(encoded2publicKey(alicePubKey));
	}

	/**
	 * 使用Alice提供的公共密钥生成Bob方的密钥对，密钥以对象形式返回.<BR>
	 * 其中：公钥保存在publicKey字段；私钥保存privateKey字段.
	 * 
	 * @param alicePubKey
	 *            Alice提供的公共密钥，对象
	 * @return 十六进制字符串形式的密钥对
	 * @throws IOException
	 */
	public static KeyStrPair generateBobKeyPair(PublicKey alicePubKey) {
		KeyPair keyPair = getBobKeyPair(alicePubKey); // 生成密钥对
		PublicKey pubKey = keyPair.getPublic();
		PrivateKey priKey = keyPair.getPrivate();
		return new KeyStrPair(CByte.bytes2Hex(pubKey.getEncoded()), CByte.bytes2Hex(priKey.getEncoded()));
	}

	/**
	 * 使用Alice提供的公共密钥生成Bob方的密钥对，密钥以对象形式返回.<BR>
	 * 其中：公钥保存在publicKey字段；私钥保存privateKey字段.
	 * 
	 * @param alicePubKey
	 *            Alice提供的公共密钥，十六进制字符串
	 * @return 十六进制字符串形式的密钥对
	 * @throws IOException
	 */
	public static KeyStrPair generateBobKeyPair(String alicePubKey) throws Exception {
		byte[] bytes = CByte.hex2Bytes(alicePubKey);
		return generateBobKeyPair(encoded2publicKey(bytes));
	}

	/**
	 * 使用Alice提供的公共密钥生成Bob方的密钥对，密钥以序列化对象格式保存到文件中.<BR>
	 * 其中：公钥保存在“public.key”文件；私钥保存在“private.key”.
	 * 
	 * @param savePath
	 *            文件保存路径
	 * @param alicePubKey
	 *            Alice提供的公共密钥，字节数组形式
	 * @throws IOException
	 */
	public static void generateBobKeyPair(String savePath, byte[] alicePubKey) throws Exception {
		generateBobKeyPair(savePath, encoded2publicKey(alicePubKey));
	}

	/**
	 * 使用Alice提供的公共密钥生成Bob方的密钥对，密钥以序列化对象格式保存到文件中.<BR>
	 * 其中：公钥保存在“public.key”文件；私钥保存在“private.key”.
	 * 
	 * @param savePath
	 *            文件保存路径
	 * @param alicePubKey
	 *            Alice提供的公共密钥
	 * @throws IOException
	 */
	public static void generateBobKeyPair(String savePath, PublicKey alicePubKey) throws IOException {
		KeyPair keyPair = getBobKeyPair(alicePubKey);
		ObjectOutputStream out = null;
		// 将私钥保存到private.key文件
		try {
			out = new ObjectOutputStream(new FileOutputStream(savePath + File.separator + "private.key"));
			out.writeObject(keyPair.getPrivate());
		}
		catch (IOException ioe) {
			throw ioe;
		}
		finally {
			out.close();
		}
		// 将公钥保存到public.key文件
		try {
			out = new ObjectOutputStream(new FileOutputStream(savePath + File.separator + "public.key"));
			out.writeObject(keyPair.getPublic());
		}
		catch (IOException ioe) {
			throw ioe;
		}
		finally {
			out.close();
		}
	}

	/**
	 * 使用Alice提供的公共密钥生成Bob方的密钥对，密钥以序列化对象格式保存到文件中.<BR>
	 * 其中：公钥保存在“public.key”文件；私钥保存在“private.key”.
	 * 
	 * @param savePath
	 *            文件保存路径
	 * @param alicePubKey
	 *            Alice提供的公共密钥，十六进制字符串
	 * @throws Exception
	 */
	public static void generateBobKeyPair(String savePath, String alicePubKey) throws Exception {
		byte[] bytes = CByte.hex2Bytes(alicePubKey);
		generateBobKeyPair(savePath, encoded2publicKey(bytes));
	}

	/**
	 * 生成Alice方密钥对.
	 * 
	 * @return 密钥对
	 */
	private static KeyPair getAliceKeyPair() {
		KeyPairGenerator keygen = null;
		// 初始化密钥对生成器，由于DSA加密算法肯定存在，故NoSuchAlgorithmException永远也不会抛出
		try {
			keygen = KeyPairGenerator.getInstance(algorithm);
		}
		catch (NoSuchAlgorithmException nae) {
			// do nothing
		}
		// 初始化密钥生成器
		keygen.initialize(512);
		// 生成密钥对，包含公钥和私钥
		return keygen.generateKeyPair(); // 生成密钥组
	}

	/**
	 * 生成Bob方密钥对.
	 * 
	 * @param alicePubKey
	 *            Alice提供的公钥
	 * @return 密钥对
	 */
	private static KeyPair getBobKeyPair(PublicKey alicePubKey) {
		DHParameterSpec dhParamSpec = ((DHPublicKey) alicePubKey).getParams();
		KeyPairGenerator bobKpairGen = null;
		try {
			bobKpairGen = KeyPairGenerator.getInstance(algorithm);
			bobKpairGen.initialize(dhParamSpec);
		}
		catch (NoSuchAlgorithmException nae) {
			// do nothing
		}
		catch (InvalidAlgorithmParameterException iape) {
			// do nothing
		}

		return bobKpairGen.generateKeyPair();
	}

	// public static void main(String[] args) {
	// try {
	// // Alice生成密钥对
	// KeyStrPair aliceKeyPair = generateAliceKeyPair();
	// System.out.println("aliceKeyPair\t" + aliceKeyPair.toString());
	// // Alice将公钥发送给Bob
	// // ....2..
	// // Bob使用Alice的公钥生成密钥对
	// KeyStrPair bobKeyPair = generateBobKeyPair(aliceKeyPair.getPublicKey());
	// System.out.println("bobKeyPair\t" + bobKeyPair.toString());
	// // Bob将公钥发送给Alice
	// // ......
	// // Bob使用Alice的公钥和自己的私钥生成秘密密钥
	// String bobSecretKey = generateAgreementKey(bobKeyPair.getPrivateKey(),
	// aliceKeyPair.getPublicKey());
	// System.out.println("bobSecretKey\t" + bobSecretKey);
	// // Alice使用Bob的公钥和自己的私钥生成秘密密钥
	// String aliceSecretKey =
	// generateAgreementKey(aliceKeyPair.getPrivateKey(),
	// bobKeyPair.getPublicKey());
	// System.out.println("aliceSecretKey\t" + aliceSecretKey);
	// // 比较两个密钥应该是一致的
	// if (aliceSecretKey.equals(bobSecretKey)) {
	// System.out.println("密钥交换成功！");
	// } else {
	// System.out.println("密钥交换失败！");
	// }
	// }
	// catch (Exception e) {
	// e.printStackTrace();
	// }
	//
	// }
}
