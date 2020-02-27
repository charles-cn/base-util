package com.haocom.util.mail;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

/**
 * 邮箱用户名密码认证. <br>
 * 进行邮箱帐户的用户名密码认证.
 * <p>
 * Copyright: Copyright (c) Sep 17, 2008
 * <p>
 * Company: 
 * <p>
 * Author:
 * <p>
 * Version: 1.0
 */
public class PopupAuthenticator extends Authenticator {

	/** 邮件帐户的密码 */
	String password = null;

	/** 邮件帐户的用户名 */
	String username = null;

	/**
	 * 默认构造函数
	 */
	public PopupAuthenticator() {
	}

	@Override
	protected PasswordAuthentication getPasswordAuthentication() {
		return new PasswordAuthentication(username, password);
	}

	/**
	 * 核对用户名密码
	 * 
	 * @param user
	 *            邮箱用户名
	 * @param pass
	 *            邮箱密码
	 * @return PasswordAuthentication
	 */
	public PasswordAuthentication performCheck(String user, String pass) {
		username = user;
		password = pass;
		return getPasswordAuthentication();
	}
}
