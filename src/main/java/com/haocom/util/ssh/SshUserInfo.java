package com.haocom.util.ssh;

import com.jcraft.jsch.UserInfo;

/**
 * Ssh登录用户设置. <br>
 * 实现com.jcraft.jsch.UserInfo接口。用于设置密码、登录方式、验证方式。
 * <p>
 * Copyright: Copyright (c) 2009-7-5 下午01:15:16
 * <p>
 * Company: 
 * <p>
 * Author: ChengFan
 * <p>
 * Version: 1.0
 * <p>
 */
class SshUserInfo implements UserInfo {

	/** passphrase */
	private String passphrase;

	/** 密码 */
	private String password;

	/**
	 * @param password
	 * @param passphrase
	 */
	public SshUserInfo(String password, String passphrase) {
		super();
		this.password = password;
		this.passphrase = passphrase;
	}

	/**
	 * 获取Passphrase
	 */
	@Override
	public String getPassphrase() {
		return passphrase;
	}

	/**
	 * 获取Password
	 */
	@Override
	public String getPassword() {
		return password;
	}

	/**
	 * 判断是否使用Passphrase解析Identity
	 */
	@Override
	public boolean promptPassphrase(String message) {
		return (passphrase != null);
	}

	/**
	 * 判断是否允许使用密码登录。<br>
	 * 默认：如果Password不为空，则返回True。否则返回False。 <br>
	 * message范例：
	 * 
	 * <pre>
	 * Password for cplatform@192.168.10.73
	 * </pre>
	 */
	@Override
	public boolean promptPassword(String message) {
		return (password != null);
	}

	/**
	 * 验证服务端fingerprint，判断是否允许登录。<br>
	 * 默认返回True。 <br>
	 * message范例：
	 * 
	 * <pre>
	 * The authenticity of host '192.168.10.73' can't be established.
	 * RSA key fingerprint is b4:81:96:7c:4b:8d:9d:a6:19:46:f7:0b:d3:9d:df:d5.
	 * Are you sure you want to continue connecting?
	 * </pre>
	 */
	@Override
	public boolean promptYesNo(String message) {
		return true;
	}

	/**
	 * 设置 passphrase
	 * 
	 * @param passphrase
	 */
	public void setPassphrase(String passphrase) {
		this.passphrase = passphrase;
	}

	/**
	 * 设置 password
	 * 
	 * @param password
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * 输出信息用方法。
	 */
	@Override
	public void showMessage(String message) {
	}

}
