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
 * RSA�㷨. <br>
 * RSA�����㷨.
 * <p>
 * Copyright: Copyright (c) Sep 16, 2008
 * <p>
 * Company: 
 * <p>
 * Author: ywh
 * <p>
 * Version: 1.0
 * <p>
 * �򵥽�������ʹ�ô��룬����֮��Ψһ����������ڽ���ǩ��������֤ʱ�Ĳ������Ͳ�ͬ������������
 * <ul>
 * <li>1</li>
 * 
 * <pre>
 * String str = &quot;������ҪRSA���ܵ�����&quot;;
 * KeyStrPair keyStrPair = RSA.generateKeyPair();
 * //��������ǩ��
 * String secret = RSA.sign(keyStrPair.getPrivateKey(), str);
 * //��֤
 * boolean flag = RSA.verify(keyStrPair.getPublicKey(), str, secret);
 * if (flag) {
 * 	System.out.println(&quot;��֤�ɹ���&quot;);
 * } else {
 * 	System.out.println(&quot;��֤ʧ�ܣ�&quot;);
 * }
 * </pre>
 * 
 * <li>2</li>
 * 
 * <pre>
 * String str = &quot;������ҪRSA���ܵ�����&quot;;
 * KeyPair keyPair = getKeyPair(); // ������Կ��
 * PublicKey pubKey = keyPair.getPublic();
 * PrivateKey priKey = keyPair.getPrivate();
 * //��������ǩ��
 * byte[] secret = RSA.sign(priKey, str);
 * //��֤
 * boolean flag = RSA.verify(pubKey, str, CByte.bytes2Hex(secret));
 * if (flag) {
 * 	System.out.println(&quot;��֤�ɹ���&quot;);
 * } else {
 * 	System.out.println(&quot;��֤ʧ�ܣ�&quot;);
 * }
 * 
 * boolean flag2 = RSA.verify(pubKey, str.getBytes(), secret);
 * if (flag2) {
 * 	System.out.println(&quot;��֤�ɹ���&quot;);
 * } else {
 * 	System.out.println(&quot;��֤ʧ�ܣ�&quot;);
 * }
 * </pre>
 * 
 * <li>3</li>
 * 
 * <pre>
 * String str = &quot;������ҪRSA���ܵ�����&quot;;
 * KeyPair keyPair = getKeyPair(); // ������Կ��
 * PublicKey pubKey = keyPair.getPublic();
 * PrivateKey priKey = keyPair.getPrivate();
 * //��������ǩ��
 * byte[] secret = RSA.sign(priKey.getEncoded(), str.getBytes());
 * //��֤
 * boolean flag = RSA.verify(pubKey.getEncoded(), str, secret);
 * if (flag) {
 * 	System.out.println(&quot;��֤�ɹ���&quot;);
 * } else {
 * 	System.out.println(&quot;��֤ʧ�ܣ�&quot;);
 * }
 * </pre>
 */

public class RSA {

	/** �㷨���� */
	private static final String algorithm = "RSA";

	/**
	 * ������Կ�ԣ���Կ�Զ�����ʽ����.<BR>
	 * ���У���Կ������publicKey�ֶΣ�˽Կ����privateKey�ֶ�.
	 * 
	 * @return ��Կ��
	 */
	public static KeyStrPair generateKeyPair() {
		KeyPair keyPair = getKeyPair(); // ������Կ��
		PublicKey pubKey = keyPair.getPublic();
		PrivateKey priKey = keyPair.getPrivate();
		return new KeyStrPair(CByte.bytes2Hex(pubKey.getEncoded()), CByte.bytes2Hex(priKey.getEncoded()));
	}

	/**
	 * ������Կ�ԣ���Կ�����л������ʽ���浽�ļ���.<BR>
	 * ���У���Կ�����ڡ�public.key���ļ���˽Կ�����ڡ�private.key��.
	 * 
	 * @param savePath
	 *            �ļ�����·��
	 * @throws IOException
	 */
	public static void generateKeyPair(String savePath) throws IOException {
		KeyPair keyPair = getKeyPair();
		ObjectOutputStream out = null;
		// ��˽Կ���浽private.key�ļ�
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
		// ����Կ���浽public.key�ļ�
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
	 * ������Կ��.
	 * 
	 * @return ��Կ��
	 */
	private static KeyPair getKeyPair() {
		KeyPairGenerator keygen = null;
		// ��ʼ����Կ��������������DSA�����㷨�϶����ڣ���NoSuchAlgorithmException��ԶҲ�����׳�
		try {
			keygen = KeyPairGenerator.getInstance(algorithm);
		}
		catch (NoSuchAlgorithmException nae) {
			// do nothing
		}
		// ��ʼ����Կ������
		keygen.initialize(512);
		// ������Կ�ԣ�������Կ��˽Կ
		return keygen.generateKeyPair(); // ������Կ��
	}

	// public static void main(String[] args) {
	// System.out.println("RSA���ܲ���");
	//
	// String str = "RSA���ܲ���";
	// KeyStrPair keyPair = DSA.generateKeyPair();
	// System.out.println("keyPair\t" + keyPair.toString());
	// String signed = null;
	// try {
	// signed = DSA.sign(keyPair.getPrivateKey(), str);
	// System.out.println("signed\t" + signed);
	// if (verify(keyPair.getPublicKey(), str, signed)) {
	// System.out.println("��֤�ɹ���");
	// } else {
	// System.out.println("��֤ʧ�ܣ�");
	// }
	// }
	// catch (Exception e) {
	// e.printStackTrace();
	// }
	// }

	/**
	 * ʹ��˽����Կ��ָ����Ϣ(info)��������ǩ������һ��ǩ���ֽ�����.
	 * 
	 * @param privateKey
	 *            �ֽ�������ʽ��˽����Կ
	 * @param info
	 *            ָ����Ϣ
	 * @return ǩ���ֽ�����
	 * @throws Exception
	 */
	public static byte[] sign(byte[] privateKey, byte[] info) throws Exception {
		// ���´���ת������Ϊ��Ӧkey����
		PKCS8EncodedKeySpec priKeySpec = new PKCS8EncodedKeySpec(privateKey);
		KeyFactory keyFactory = null;
		// ��ʼ����Կ����������DSA�����㷨�϶����ڣ���NoSuchAlgorithmException��ԶҲ�����׳�
		try {
			keyFactory = KeyFactory.getInstance(algorithm);
		}
		catch (NoSuchAlgorithmException nae) {
			// do nothing
		}
		PrivateKey key = keyFactory.generatePrivate(priKeySpec);
		return sign(key, info);
	}

	/**
	 * ʹ��˽����Կ��ָ����Ϣ(info)��������ǩ������һ��ǩ���ֽ�����.
	 * 
	 * @param privateKey
	 *            ������ʽ��˽����Կ
	 * @param info
	 *            ָ����Ϣ
	 * @return ǩ���ֽ�����
	 * @throws Exception
	 */
	public static byte[] sign(PrivateKey privateKey, byte[] info) throws Exception {
		Signature signature = null;
		// ��ʼ��ǩ����������DSA�����㷨�϶����ڣ���NoSuchAlgorithmException��ԶҲ�����׳�
		try {
			signature = Signature.getInstance("MD5withRSA");
		}
		catch (NoSuchAlgorithmException nae) {
			// do nothing
		}
		signature.initSign(privateKey);
		signature.update(info);
		return signature.sign();
	}

	/**
	 * ʹ��˽����Կ��ָ����Ϣ(info)��������ǩ������һ��ǩ���ֽ�����.
	 * 
	 * @param privateKey
	 *            ������ʽ��˽����Կ
	 * @param info
	 *            ָ����Ϣ
	 * @return ǩ���ֽ�����
	 * @throws Exception
	 */
	public static byte[] sign(PrivateKey privateKey, String info) throws Exception {
		return sign(privateKey, info.getBytes());
	}

	/**
	 * ʹ��˽����Կ��ָ����Ϣ(info)��������ǩ������һ��ǩ���ַ���.
	 * 
	 * @param privateKey
	 *            ʮ�������ַ�����ʽ��˽����Կ
	 * @param info
	 *            ָ����Ϣ
	 * @return ǩ���ַ���
	 * @throws Exception
	 */
	public static String sign(String privateKey, String info) throws Exception {
		byte[] bytes = CByte.hex2Bytes(privateKey);
		return CByte.bytes2Hex(sign(bytes, info.getBytes()));
	}

	/**
	 * ʹ�ù�����Կ��ǩ����ָ����Ϣ����ǩ����֤.
	 * 
	 * @param publicKey
	 *            �ֽ�������ʽ�Ĺ�����Կ
	 * @param originInfo
	 *            ԭʼ��Ϣ
	 * @param signedInfo
	 *            �ֽ�������ʽ��ǩ����Ϣ
	 * @return �Ƿ�ͨ����֤
	 * @throws Exception
	 */
	public static boolean verify(byte[] publicKey, String originInfo, byte[] signedInfo) throws Exception {
		X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(publicKey);
		KeyFactory keyFactory = null;
		// ��ʼ����Կ����������DSA�����㷨�϶����ڣ���NoSuchAlgorithmException��ԶҲ�����׳�
		try {
			keyFactory = KeyFactory.getInstance(algorithm);
		}
		catch (NoSuchAlgorithmException nae) {
			// do nothing
		}
		PublicKey key = keyFactory.generatePublic(pubKeySpec);
		return verify(key, originInfo.getBytes(), signedInfo);
	}

	/**
	 * ʹ�ù�����Կ��ǩ����ָ����Ϣ����ǩ����֤.
	 * 
	 * @param publicKey
	 *            ������ʽ�Ĺ�����Կ
	 * @param originInfo
	 *            �ֽ�������ʽ��ԭʼ��Ϣ
	 * @param signedInfo
	 *            �ֽ�������ʽ��ǩ����Ϣ
	 * @return �Ƿ�ͨ����֤
	 * @throws Exception
	 */
	public static boolean verify(PublicKey publicKey, byte[] originInfo, byte[] signedInfo) throws Exception {
		Signature signetcheck = null;
		// ��ʼ��ǩ����������DSA�����㷨�϶����ڣ���NoSuchAlgorithmException��ԶҲ�����׳�
		try {
			// ��ʼһ��Signature����,���ù�Կ��ǩ��������֤
			signetcheck = Signature.getInstance("MD5withRSA");
		}
		catch (NoSuchAlgorithmException nae) {
			// do nothing
		}
		signetcheck.initVerify(publicKey);
		signetcheck.update(originInfo);
		return signetcheck.verify(signedInfo);
	}

	/**
	 * ʹ�ù�����Կ��ǩ����ָ����Ϣ����ǩ����֤.
	 * 
	 * @param publicKey
	 *            ������ʽ�Ĺ�����Կ
	 * @param originInfo
	 *            ԭʼ��Ϣ
	 * @param signedInfo
	 *            ʮ�������ַ�����ʽ��ǩ����Ϣ
	 * @return �Ƿ�ͨ����֤
	 * @throws Exception
	 */
	public static boolean verify(PublicKey publicKey, String originInfo, String signedInfo) throws Exception {
		return verify(publicKey, originInfo.getBytes(), CByte.hex2Bytes(signedInfo));
	}

	/**
	 * ʹ�ù�����Կ��ǩ����ָ����Ϣ����ǩ����֤.
	 * 
	 * @param publicKey
	 *            ʮ�������ַ�����ʽ�Ĺ�����Կ
	 * @param originInfo
	 *            ԭʼ��Ϣ
	 * @param signedInfo
	 *            ʮ�������ַ�����ʽ��ǩ����Ϣ
	 * @return �Ƿ�ͨ����֤
	 * @throws Exception
	 */
	public static boolean verify(String publicKey, String originInfo, String signedInfo) throws Exception {
		byte[] keyBytes = CByte.hex2Bytes(publicKey);
		byte[] signedInfoBytes = CByte.hex2Bytes(signedInfo);
		return verify(keyBytes, originInfo, signedInfoBytes);
	}
}
