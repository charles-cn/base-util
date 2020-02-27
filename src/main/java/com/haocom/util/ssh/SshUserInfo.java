package com.haocom.util.ssh;

import com.jcraft.jsch.UserInfo;

/**
 * Ssh��¼�û�����. <br>
 * ʵ��com.jcraft.jsch.UserInfo�ӿڡ������������롢��¼��ʽ����֤��ʽ��
 * <p>
 * Copyright: Copyright (c) 2009-7-5 ����01:15:16
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

	/** ���� */
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
	 * ��ȡPassphrase
	 */
	@Override
	public String getPassphrase() {
		return passphrase;
	}

	/**
	 * ��ȡPassword
	 */
	@Override
	public String getPassword() {
		return password;
	}

	/**
	 * �ж��Ƿ�ʹ��Passphrase����Identity
	 */
	@Override
	public boolean promptPassphrase(String message) {
		return (passphrase != null);
	}

	/**
	 * �ж��Ƿ�����ʹ�������¼��<br>
	 * Ĭ�ϣ����Password��Ϊ�գ��򷵻�True�����򷵻�False�� <br>
	 * message������
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
	 * ��֤�����fingerprint���ж��Ƿ������¼��<br>
	 * Ĭ�Ϸ���True�� <br>
	 * message������
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
	 * ���� passphrase
	 * 
	 * @param passphrase
	 */
	public void setPassphrase(String passphrase) {
		this.passphrase = passphrase;
	}

	/**
	 * ���� password
	 * 
	 * @param password
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * �����Ϣ�÷�����
	 */
	@Override
	public void showMessage(String message) {
	}

}
