package com.haocom.util.security;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import com.haocom.util.CByte;

/**
 * 数字签名－DSA算法. <br>
 * 提供DSA算法的数字签名方法.
 * <p>
 * Copyright: Copyright (c) Sep 16, 2008
 * <p>
 * Company: 
 * <p>
 * Author: ywh
 * <p>
 * Version: 1.0
 * <p>
 * 简单介绍三段使用代码，它们之间唯一的区别仅在于进行签名或是认证时的参数类型不同。代码样例：
 * <ul>
 * <li>1</li>
 * 
 * <pre>
 * String str = &quot;这是需要DSA加密的内容&quot;;
 * KeyStrPair keyStrPair = DSA.generateKeyPair();
 * //进行数字签名
 * String secret = DSA.sign(keyStrPair.getPrivateKey(), str);
 * //认证
 * boolean flag = DSA.verify(keyStrPair.getPublicKey(), str, secret);
 * if (flag) {
 * 	System.out.println(&quot;认证成功！&quot;);
 * } else {
 * 	System.out.println(&quot;认证失败！&quot;);
 * }
 * </pre>
 * 
 * <li>2</li>
 * 
 * <pre>
 * String str = &quot;这是需要DSA加密的内容&quot;;
 * KeyPair keyPair = getKeyPair(); // 生成密钥组
 * PublicKey pubKey = keyPair.getPublic();
 * PrivateKey priKey = keyPair.getPrivate();
 * //进行数字签名
 * byte[] secret = DSA.sign(priKey, str);
 * //认证
 * boolean flag = DSA.verify(pubKey, str, CByte.bytes2Hex(secret));
 * if (flag) {
 * 	System.out.println(&quot;认证成功！&quot;);
 * } else {
 * 	System.out.println(&quot;认证失败！&quot;);
 * }
 * 
 * boolean flag2 = DSA.verify(pubKey, str.getBytes(), secret);
 * if (flag2) {
 * 	System.out.println(&quot;认证成功！&quot;);
 * } else {
 * 	System.out.println(&quot;认证失败！&quot;);
 * }
 * </pre>
 * 
 * <li>3</li>
 * 
 * <pre>
 * String str = &quot;这是需要DSA加密的内容&quot;;
 * KeyPair keyPair = getKeyPair(); // 生成密钥组
 * PublicKey pubKey = keyPair.getPublic();
 * PrivateKey priKey = keyPair.getPrivate();
 * //进行数字签名
 * byte[] secret = DSA.sign(priKey.getEncoded(), str.getBytes());
 * //认证
 * boolean flag = DSA.verify(pubKey.getEncoded(), str, secret);
 * if (flag) {
 * 	System.out.println(&quot;认证成功！&quot;);
 * } else {
 * 	System.out.println(&quot;认证失败！&quot;);
 * }
 * </pre>
 */

public class DSA {

	/** 算法名称 */
	private static final String algorithm = "DSA";

	/**
	 * 生成密钥对，密钥以对象形式返回.<BR>
	 * 其中：公钥保存在publicKey字段；私钥保存privateKey字段.
	 * 
	 * @return 密钥组
	 */
	public static KeyStrPair generateKeyPair() {
		KeyPair keyPair = getKeyPair(); // 生成密钥组
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
	public static void generateKeyPair(String savePath) throws IOException {
		KeyPair keyPair = getKeyPair();
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
	 * 生成密钥对.
	 * 
	 * @return 密钥对
	 */
	private static KeyPair getKeyPair() {
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

	// public static void main(String[] args) {
	// System.out.println("DSA加密测试");
	//
	// String str = "DSA加密测试";
	// KeyStrPair keyPair = DSA.generateKeyPair();
	// System.out.println("keyPair\t" + keyPair.toString());
	// String signed = null;
	// try {
	// signed = DSA.sign(keyPair.getPrivateKey(), str);
	// System.out.println("signed\t" + signed);
	// if (verify(keyPair.getPublicKey(), str, signed)) {
	// System.out.println("验证成功！");
	// } else {
	// System.out.println("验证失败！");
	// }
	// }
	// catch (Exception e) {
	// e.printStackTrace();
	// }
	//
	// }

	/**
	 * 使用私有密钥对指定信息(info)进行数字签名产生一个签名字节数组.
	 * 
	 * @param privateKey
	 *            字节数组形式的私有密钥
	 * @param info
	 *            指定信息
	 * @return 签名字节数组
	 * @throws Exception
	 */
	public static byte[] sign(byte[] privateKey, byte[] info) throws Exception {
		// 以下代码转换编码为相应key对象
		PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(privateKey);
		KeyFactory keyFactory = null;
		// 初始化密钥工厂，由于DSA加密算法肯定存在，故NoSuchAlgorithmException永远也不会抛出
		try {
			keyFactory = KeyFactory.getInstance(algorithm);
		}
		catch (NoSuchAlgorithmException nae) {
			// do nothing
		}
		PrivateKey key = keyFactory.generatePrivate(priPKCS8);
		return sign(key, info);
	}

	/**
	 * 使用私有密钥对指定信息(info)进行数字签名产生一个签名字节数组.
	 * 
	 * @param privateKey
	 *            对象形式的私有密钥
	 * @param info
	 *            指定信息
	 * @return 签名字节数组
	 * @throws Exception
	 */
	public static byte[] sign(PrivateKey privateKey, byte[] info) throws Exception {
		Signature signature = null;
		// 初始化签名器，由于DSA加密算法肯定存在，故NoSuchAlgorithmException永远也不会抛出
		try {
			signature = Signature.getInstance(algorithm);
		}
		catch (NoSuchAlgorithmException nae) {
			// do nothing
		}
		signature.initSign(privateKey);
		signature.update(info);
		return signature.sign();
	}

	/**
	 * 使用私有密钥对指定信息(info)进行数字签名产生一个签名字节数组.
	 * 
	 * @param privateKey
	 *            对象形式的私有密钥
	 * @param info
	 *            指定信息
	 * @return 签名字节数组
	 * @throws Exception
	 */
	public static byte[] sign(PrivateKey privateKey, String info) throws Exception {
		return sign(privateKey, info.getBytes());
	}

	/**
	 * 使用私有密钥对指定信息(info)进行数字签名产生一个签名字符串.
	 * 
	 * @param privateKey
	 *            十六进制字符串形式的私有密钥
	 * @param info
	 *            指定信息
	 * @return 签名字符串
	 * @throws Exception
	 */
	public static String sign(String privateKey, String info) throws Exception {
		byte[] bytes = CByte.hex2Bytes(privateKey);
		return CByte.bytes2Hex(sign(bytes, info.getBytes()));
	}

	/**
	 * 使用公共密钥和签名对指定信息进行签名验证.
	 * 
	 * @param publicKey
	 *            字节数组形式的公共密钥
	 * @param originInfo
	 *            原始信息
	 * @param signedInfo
	 *            字节数组形式的签名信息
	 * @return 是否验证通过
	 * @throws Exception
	 */
	public static boolean verify(byte[] publicKey, String originInfo, byte[] signedInfo) throws Exception {
		X509EncodedKeySpec bobPubKeySpec = new X509EncodedKeySpec(publicKey);
		KeyFactory keyFactory = null;
		// 初始化密钥工厂，由于DSA加密算法肯定存在，故NoSuchAlgorithmException永远也不会抛出
		try {
			keyFactory = KeyFactory.getInstance(algorithm);
		}
		catch (NoSuchAlgorithmException nae) {
			// do nothing
		}
		PublicKey key = keyFactory.generatePublic(bobPubKeySpec);
		return verify(key, originInfo.getBytes(), signedInfo);
	}

	/**
	 * 使用公共密钥和签名对指定信息进行签名验证.
	 * 
	 * @param publicKey
	 *            对象形式的公共密钥
	 * @param originInfo
	 *            原始信息
	 * @param signedInfo
	 *            字节数组形式的签名信息
	 * @return 是否验证通过
	 * @throws Exception
	 */
	public static boolean verify(PublicKey publicKey, byte[] originInfo, byte[] signedInfo) throws Exception {
		Signature signetcheck = null;
		// 初始化签名器，由于DSA加密算法肯定存在，故NoSuchAlgorithmException永远也不会抛出
		try {
			// 初始一个Signature对象,并用公钥和签名进行验证
			signetcheck = Signature.getInstance(algorithm);
		}
		catch (NoSuchAlgorithmException nae) {
			// do nothing
		}
		signetcheck.initVerify(publicKey);
		signetcheck.update(originInfo);
		return signetcheck.verify(signedInfo);
	}

	/**
	 * 使用公共密钥和签名对指定信息进行签名验证.
	 * 
	 * @param publicKey
	 *            对象形式的公共密钥
	 * @param originInfo
	 *            原始信息
	 * @param signedInfo
	 *            十六进制字符串形式的签名信息
	 * @return 是否验证通过
	 * @throws Exception
	 */
	public static boolean verify(PublicKey publicKey, String originInfo, String signedInfo) throws Exception {
		return verify(publicKey, originInfo.getBytes(), CByte.hex2Bytes(signedInfo));
	}

	/**
	 * 使用公共密钥和签名对指定信息进行签名验证.
	 * 
	 * @param publicKey
	 *            十六进制字符串形式的公共密钥
	 * @param originInfo
	 *            原始信息
	 * @param signedInfo
	 *            十六进制字符串形式的签名信息
	 * @return 是否验证通过
	 * @throws Exception
	 */
	public static boolean verify(String publicKey, String originInfo, String signedInfo) throws Exception {
		byte[] keyBytes = CByte.hex2Bytes(publicKey);
		byte[] signedInfoBytes = CByte.hex2Bytes(signedInfo);
		return verify(keyBytes, originInfo, signedInfoBytes);
	}
}
