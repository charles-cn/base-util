package com.haocom.util.security;

/**
 * 密钥对. <br>
 * 存放一对密钥：公钥和私钥.
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

	/** 密钥 */
	private String privateKey;

	/** 公钥 */
	private String publicKey;

	/**
	 * 默认构造器
	 */
	public KeyStrPair() {

	}

	/**
	 * 构造器
	 * 
	 * @param publicKey
	 *            公钥
	 * @param privateKey
	 *            密钥
	 */
	public KeyStrPair(String publicKey, String privateKey) {
		this.publicKey = publicKey;
		this.privateKey = privateKey;
	}

	/**
	 * 获取私钥
	 * 
	 * @return 私钥
	 */
	public String getPrivateKey() {
		return privateKey;
	}

	/**
	 * 获取公钥
	 * 
	 * @return 公钥
	 */
	public String getPublicKey() {
		return publicKey;
	}

	/**
	 * 设置私钥
	 * 
	 * @param privateKey
	 *            私钥
	 */
	public void setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
	}

	/**
	 * 设置公钥
	 * 
	 * @param publicKey
	 *            公钥
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
