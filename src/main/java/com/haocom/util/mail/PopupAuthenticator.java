package com.haocom.util.mail;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

/**
 * �����û���������֤. <br>
 * ���������ʻ����û���������֤.
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

	/** �ʼ��ʻ������� */
	String password = null;

	/** �ʼ��ʻ����û��� */
	String username = null;

	/**
	 * Ĭ�Ϲ��캯��
	 */
	public PopupAuthenticator() {
	}

	@Override
	protected PasswordAuthentication getPasswordAuthentication() {
		return new PasswordAuthentication(username, password);
	}

	/**
	 * �˶��û�������
	 * 
	 * @param user
	 *            �����û���
	 * @param pass
	 *            ��������
	 * @return PasswordAuthentication
	 */
	public PasswordAuthentication performCheck(String user, String pass) {
		username = user;
		password = pass;
		return getPasswordAuthentication();
	}
}
