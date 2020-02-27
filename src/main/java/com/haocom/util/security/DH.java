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
 * Diffie-Hellman��Կ. <br>
 * ������Կ�������Ƶĵ����Diffie��Hellman������� "ָ����Կһ��Э��"(Exponential Key Agreement
 * Protocol),��Э�鲻Ҫ���İ�ȫ���Ⱦ�����,���������û��ڹ���ý���Ͻ�����Ϣ������"һ��"��,���Թ������Կ��<BR>
 * �����ṩ��Կ�Ե������Լ�˫�����ݶԷ���Կ����DES��Կ�ķ���.
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
 * ���뷶����
 * try {
 * 	// Alice������Կ��.
 * 	KeyStrPair aliceKeyPair = DH.generateAliceKeyPair();
 * 	System.out.println(&quot;aliceKeyPair\t&quot; + aliceKeyPair.toString());
 * 	// Alice����Կ���͸�Bob.
 * 	// ......
 * 	// Bobʹ��Alice�Ĺ�Կ������Կ��.
 * 	KeyStrPair bobKeyPair = DH.generateBobKeyPair(aliceKeyPair.getPublicKey());
 * 	System.out.println(&quot;bobKeyPair\t&quot; + bobKeyPair.toString());
 * 	// Bob����Կ���͸�Alice.
 * 	// ......
 * 	// Bobʹ��Alice�Ĺ�Կ���Լ���˽Կ����������Կ.
 * 	String bobSecretKey = DH.generateAgreementKey(bobKeyPair.getPrivateKey(), aliceKeyPair.getPublicKey());
 * 	System.out.println(&quot;bobSecretKey\t&quot; + bobSecretKey);
 * 	// Aliceʹ��Bob�Ĺ�Կ���Լ���˽Կ����������Կ.
 * 	String aliceSecretKey = DH.generateAgreementKey(aliceKeyPair.getPrivateKey(), bobKeyPair.getPublicKey());
 * 	System.out.println(&quot;aliceSecretKey\t&quot; + aliceSecretKey);
 * 	// �Ƚ�������ԿӦ����һ�µ�.
 * 	if (aliceSecretKey.equals(bobSecretKey)) {
 * 		System.out.println(&quot;��Կ�����ɹ���&quot;);
 * 	} else {
 * 		System.out.println(&quot;��Կ����ʧ�ܣ�&quot;);
 * 	}
 * }
 * catch (Exception e) {
 * 	e.printStackTrace();
 * }
 * </pre>
 */

public class DH {

	/** �㷨���� */
	private static final String algorithm = "DH";

	/**
	 * ������PKCS8������ֽ�����ת��˽����Կ.
	 * 
	 * @param encoded
	 *            ����PKCS8������ֽ�����
	 * @return ˽����Կ
	 * @throws Exception
	 */
	private static PrivateKey encoded2privateKey(byte[] encoded) throws Exception {
		PKCS8EncodedKeySpec priKeySpec = new PKCS8EncodedKeySpec(encoded);
		KeyFactory keyFactory = null;
		// ��ʼ����Կ����������DSA�����㷨�϶����ڣ���NoSuchAlgorithmException��ԶҲ�����׳�
		try {
			keyFactory = KeyFactory.getInstance(algorithm);
		}
		catch (NoSuchAlgorithmException nae) {
			// do nothing
		}
		return keyFactory.generatePrivate(priKeySpec);
	}

	/**
	 * ������X509������ֽ�����ת�ɹ�����Կ.
	 * 
	 * @param encoded
	 *            ����PKCS8������ֽ�����
	 * @return ������Կ
	 * @throws Exception
	 */
	private static PublicKey encoded2publicKey(byte[] encoded) throws Exception {
		X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(encoded);
		KeyFactory keyFactory = null;
		// ��ʼ����Կ����������DSA�����㷨�϶����ڣ���NoSuchAlgorithmException��ԶҲ�����׳�
		try {
			keyFactory = KeyFactory.getInstance(algorithm);
		}
		catch (NoSuchAlgorithmException nae) {
			// do nothing
		}
		return keyFactory.generatePublic(pubKeySpec);
	}

	/**
	 * ʹ�ø��Ե���Կ�ͶԷ��Ĺ�Կ������ͬ��������Կ.
	 * 
	 * @param privateKey
	 *            �Լ���˽Կ���ֽ�����
	 * @param publicKey
	 *            �Է��Ĺ�Կ���ֽ�����
	 * @return ������Կ���ֽ�����
	 * @throws Exception
	 */
	public static byte[] generateAgreementKey(byte[] privateKey, byte[] publicKey) throws Exception {
		PrivateKey priKey = encoded2privateKey(privateKey);
		PublicKey pubKey = encoded2publicKey(publicKey);
		SecretKey secretKey = generateAgreementKey(priKey, pubKey);
		return secretKey.getEncoded();
	}

	/**
	 * ʹ�ø��Ե���Կ�ͶԷ��Ĺ�Կ������ͬ��������Կ.
	 * 
	 * @param privateKey
	 *            �Լ���˽Կ������
	 * @param publicKey
	 *            �Է��Ĺ�Կ������
	 * @return ������Կ������
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
	 * ʹ�ø��Ե���Կ�ͶԷ��Ĺ�Կ������ͬ��������Կ.
	 * 
	 * @param privateKey
	 *            �Լ���˽Կ��ʮ�������ַ���
	 * @param publicKey
	 *            �Է��Ĺ�Կ��ʮ�������ַ���
	 * @return ������Կ��ʮ�������ַ���
	 * @throws Exception
	 */
	public static String generateAgreementKey(String privateKey, String publicKey) throws Exception {
		byte[] priBytes = CByte.hex2Bytes(privateKey);
		byte[] pubBytes = CByte.hex2Bytes(publicKey);
		byte[] secretKey = generateAgreementKey(priBytes, pubBytes);
		return CByte.bytes2Hex(secretKey);
	}

	/**
	 * ������Կ�ԣ���Կ�Զ�����ʽ����.<BR>
	 * ���У���Կ������publicKey�ֶΣ�˽Կ����privateKey�ֶ�.
	 * 
	 * @return ��Կ��
	 */
	public static KeyStrPair generateAliceKeyPair() {
		KeyPair keyPair = getAliceKeyPair(); // ������Կ��
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
	public static void generateAliceKeyPair(String savePath) throws IOException {
		KeyPair keyPair = getAliceKeyPair();
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
	 * ʹ��Alice�ṩ�Ĺ�����Կ����Bob������Կ�ԣ���Կ�Զ�����ʽ����.<BR>
	 * ���У���Կ������publicKey�ֶΣ�˽Կ����privateKey�ֶ�.
	 * 
	 * @param alicePubKey
	 *            Alice�ṩ�Ĺ�����Կ
	 * @return ʮ�������ַ�����ʽ����Կ��
	 * @throws IOException
	 */
	public static KeyStrPair generateBobKeyPair(byte[] alicePubKey) throws Exception {
		return generateBobKeyPair(encoded2publicKey(alicePubKey));
	}

	/**
	 * ʹ��Alice�ṩ�Ĺ�����Կ����Bob������Կ�ԣ���Կ�Զ�����ʽ����.<BR>
	 * ���У���Կ������publicKey�ֶΣ�˽Կ����privateKey�ֶ�.
	 * 
	 * @param alicePubKey
	 *            Alice�ṩ�Ĺ�����Կ������
	 * @return ʮ�������ַ�����ʽ����Կ��
	 * @throws IOException
	 */
	public static KeyStrPair generateBobKeyPair(PublicKey alicePubKey) {
		KeyPair keyPair = getBobKeyPair(alicePubKey); // ������Կ��
		PublicKey pubKey = keyPair.getPublic();
		PrivateKey priKey = keyPair.getPrivate();
		return new KeyStrPair(CByte.bytes2Hex(pubKey.getEncoded()), CByte.bytes2Hex(priKey.getEncoded()));
	}

	/**
	 * ʹ��Alice�ṩ�Ĺ�����Կ����Bob������Կ�ԣ���Կ�Զ�����ʽ����.<BR>
	 * ���У���Կ������publicKey�ֶΣ�˽Կ����privateKey�ֶ�.
	 * 
	 * @param alicePubKey
	 *            Alice�ṩ�Ĺ�����Կ��ʮ�������ַ���
	 * @return ʮ�������ַ�����ʽ����Կ��
	 * @throws IOException
	 */
	public static KeyStrPair generateBobKeyPair(String alicePubKey) throws Exception {
		byte[] bytes = CByte.hex2Bytes(alicePubKey);
		return generateBobKeyPair(encoded2publicKey(bytes));
	}

	/**
	 * ʹ��Alice�ṩ�Ĺ�����Կ����Bob������Կ�ԣ���Կ�����л������ʽ���浽�ļ���.<BR>
	 * ���У���Կ�����ڡ�public.key���ļ���˽Կ�����ڡ�private.key��.
	 * 
	 * @param savePath
	 *            �ļ�����·��
	 * @param alicePubKey
	 *            Alice�ṩ�Ĺ�����Կ���ֽ�������ʽ
	 * @throws IOException
	 */
	public static void generateBobKeyPair(String savePath, byte[] alicePubKey) throws Exception {
		generateBobKeyPair(savePath, encoded2publicKey(alicePubKey));
	}

	/**
	 * ʹ��Alice�ṩ�Ĺ�����Կ����Bob������Կ�ԣ���Կ�����л������ʽ���浽�ļ���.<BR>
	 * ���У���Կ�����ڡ�public.key���ļ���˽Կ�����ڡ�private.key��.
	 * 
	 * @param savePath
	 *            �ļ�����·��
	 * @param alicePubKey
	 *            Alice�ṩ�Ĺ�����Կ
	 * @throws IOException
	 */
	public static void generateBobKeyPair(String savePath, PublicKey alicePubKey) throws IOException {
		KeyPair keyPair = getBobKeyPair(alicePubKey);
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
	 * ʹ��Alice�ṩ�Ĺ�����Կ����Bob������Կ�ԣ���Կ�����л������ʽ���浽�ļ���.<BR>
	 * ���У���Կ�����ڡ�public.key���ļ���˽Կ�����ڡ�private.key��.
	 * 
	 * @param savePath
	 *            �ļ�����·��
	 * @param alicePubKey
	 *            Alice�ṩ�Ĺ�����Կ��ʮ�������ַ���
	 * @throws Exception
	 */
	public static void generateBobKeyPair(String savePath, String alicePubKey) throws Exception {
		byte[] bytes = CByte.hex2Bytes(alicePubKey);
		generateBobKeyPair(savePath, encoded2publicKey(bytes));
	}

	/**
	 * ����Alice����Կ��.
	 * 
	 * @return ��Կ��
	 */
	private static KeyPair getAliceKeyPair() {
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

	/**
	 * ����Bob����Կ��.
	 * 
	 * @param alicePubKey
	 *            Alice�ṩ�Ĺ�Կ
	 * @return ��Կ��
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
	// // Alice������Կ��
	// KeyStrPair aliceKeyPair = generateAliceKeyPair();
	// System.out.println("aliceKeyPair\t" + aliceKeyPair.toString());
	// // Alice����Կ���͸�Bob
	// // ....2..
	// // Bobʹ��Alice�Ĺ�Կ������Կ��
	// KeyStrPair bobKeyPair = generateBobKeyPair(aliceKeyPair.getPublicKey());
	// System.out.println("bobKeyPair\t" + bobKeyPair.toString());
	// // Bob����Կ���͸�Alice
	// // ......
	// // Bobʹ��Alice�Ĺ�Կ���Լ���˽Կ����������Կ
	// String bobSecretKey = generateAgreementKey(bobKeyPair.getPrivateKey(),
	// aliceKeyPair.getPublicKey());
	// System.out.println("bobSecretKey\t" + bobSecretKey);
	// // Aliceʹ��Bob�Ĺ�Կ���Լ���˽Կ����������Կ
	// String aliceSecretKey =
	// generateAgreementKey(aliceKeyPair.getPrivateKey(),
	// bobKeyPair.getPublicKey());
	// System.out.println("aliceSecretKey\t" + aliceSecretKey);
	// // �Ƚ�������ԿӦ����һ�µ�
	// if (aliceSecretKey.equals(bobSecretKey)) {
	// System.out.println("��Կ�����ɹ���");
	// } else {
	// System.out.println("��Կ����ʧ�ܣ�");
	// }
	// }
	// catch (Exception e) {
	// e.printStackTrace();
	// }
	//
	// }
}
