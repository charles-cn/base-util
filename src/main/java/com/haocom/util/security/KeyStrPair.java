package com.haocom.util.security;

/**
 * ��Կ��. <br>
 * ���һ����Կ����Կ��˽Կ.
 * <p>
 * Copyright: Copyright (c) Sep 16, 2008
 * <p>
 * Company: 
 * <p>
 * Author: ywh
 * <p>
 * Version: 1.0
 */

public class KeyStrPair {

	/** ��Կ */
	private String privateKey;

	/** ��Կ */
	private String publicKey;

	/**
	 * Ĭ�Ϲ�����
	 */
	public KeyStrPair() {

	}

	/**
	 * ������
	 * 
	 * @param publicKey
	 *            ��Կ
	 * @param privateKey
	 *            ��Կ
	 */
	public KeyStrPair(String publicKey, String privateKey) {
		this.publicKey = publicKey;
		this.privateKey = privateKey;
	}

	/**
	 * ��ȡ˽Կ
	 * 
	 * @return ˽Կ
	 */
	public String getPrivateKey() {
		return privateKey;
	}

	/**
	 * ��ȡ��Կ
	 * 
	 * @return ��Կ
	 */
	public String getPublicKey() {
		return publicKey;
	}

	/**
	 * ����˽Կ
	 * 
	 * @param privateKey
	 *            ˽Կ
	 */
	public void setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
	}

	/**
	 * ���ù�Կ
	 * 
	 * @param publicKey
	 *            ��Կ
	 */
	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}

	@Override
	public String toString() {
		StringBuilder buf = new StringBuilder(500);
		buf.append("PublicKey = ").append(publicKey).append("\t");
		buf.append("PrivateKey = ").append(privateKey);
		return buf.toString();
	}

}
