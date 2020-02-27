package com.haocom.util.mail;

import java.util.Calendar;
import java.util.Properties;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeUtility;

import com.haocom.util.TimeStamp;

/**
 * �����ʼ�. <br>
 * ʵ���˴ӷ������Ͻ����ʼ��Ĺ���.
 * <p>
 * Copyright: Copyright (c) Sep 16, 2008
 * <p>
 * Company: 
 * <p>
 * Author:
 * <p>
 * Version: 1.0
 */
public class RecvMail {

	/**
	 * ��ȡ������
	 * 
	 * @param msg
	 *            �ʼ�����
	 * @return �����˵�ַ
	 */
	public static String getFrom(Message msg) {
		String from = "";
		try {
			if (msg.getFrom()[0] != null) {
				from = msg.getFrom()[0].toString();
			}

			from = from.replaceAll("\"", "");
			from = from.replaceAll(" ", "");

			if (from.startsWith("=?GB") || from.startsWith("=?gb")) {
				from = MimeUtility.decodeText(from);
			} else {
				// from = new String(from.getBytes("ISO-8859-1"), "GB2312");
			}
		}
		catch (Exception e) {
		}

		return from;
	}

	/** ���ʼ����� */
	private String[][] mailHead = null;

	/** Mail������Ϣ */
	private String popError = null;

	/** �ʼ��˻������� */
	private String popPassword = null;

	/** �ʼ��˷��������� */
	private String popServer = null;

	/** �ʼ��˻����û��� */
	private String popUser = null;

	/**
	 * Ĭ�Ϲ��캯��
	 */
	public RecvMail() {
	}

	/**
	 * ���췽�� ��Ҫ������֤
	 * 
	 * @param server
	 *            �ʼ��˷���������
	 * @param user
	 *            �ʼ��˻����û���
	 * @param password
	 *            �ʼ��˻�������
	 */
	public RecvMail(String server, String user, String password) {
		popServer = server;
		popUser = user;
		popPassword = password;
	}

	/**
	 * ���ش���˵��
	 * 
	 * @return ����˵��
	 */
	public String getError() {
		return popError;
	}

	/**
	 * �����ʼ������л�ȡָ���ʼ���ָ������
	 * 
	 * @param num
	 *            ��ʾ�ڼ����ʼ�
	 * @param id
	 *            ��ʾ��ȡ�ʼ����ĸ���Ϣ<BR>
	 *            ���У�0��ʾ�ʼ����⣬1��ʾ�ʼ��ķ���ʱ�䣬2��ʾ�����˵�ַ
	 * @return ��ȡ��������
	 */
	public String getMailParam(int num, int id) {
		if (num >= mailHead.length)
			return null;

		if (id > 2)
			return null;

		return mailHead[num][id];
	}

	/**
	 * ��ȡ�ʼ�����
	 * 
	 * @param msg
	 *            �ʼ�����
	 * @return �ʼ�����
	 */
	private String getSubject(Message msg) {
		String subject = "";
		try {
			if (msg.getSubject() != null) {
				subject = msg.getSubject();
			}

			subject = subject.replaceAll("\"", "");
			subject = subject.replaceAll(" ", "");
			if (subject.startsWith("=?GB") || subject.startsWith("=?gb")) {
				subject = MimeUtility.decodeText(subject);
			} else {
				// subject = new String(subject.getBytes("ISO-8859-1"),
				// "GB2312");
			}
		}
		catch (Exception e) {
		}

		return subject;
	}

	/**
	 * �������ʼ�
	 * 
	 * @param lastTime
	 *            �ϴν����ʼ�ʱ��
	 * @return ���ճɹ�����count���ʼ�����������ʧ�ܷ���-1
	 */
	public int receive(String lastTime) {
		/** ��մ���˵�� */
		popError = "";

		/** ����Mail API */
		Store store = null;
		Folder folder = null;
		try {
			Properties props = System.getProperties();
			Session session = Session.getDefaultInstance(props, null);
			store = session.getStore("pop3");

			/** �����û����� */
			try {
				store.connect(popServer, popUser, popPassword);
			}
			catch (Exception e) {
				popError = " POP3 : " + popServer + " ���� : " + popUser + " ���� : " + popPassword + " connect \r\n" + e.toString();

				return -1;
			}

			/** ��ȡȱʡĿ¼ */
			folder = store.getDefaultFolder();
			if (folder == null) {
				popError = " ���� : " + popUser + " No default folder";

				return -1;
			}

			/** ��ȡ�ռ��� */
			folder = folder.getFolder("INBOX");
			if (folder == null) {
				popError = " ���� : " + popUser + " No POP3 INBOX";

				return -1;
			}

			/** ���ռ��� */
			folder.open(Folder.READ_ONLY);

			/** ��ȡ�ʼ� */
			int count = 0;
			String subject = null;
			String mailTime = null;
			String fromAddr = null;
			Calendar cal = Calendar.getInstance();

			Message[] msgs = folder.getMessages();
			mailHead = new String[msgs.length][3];
			for (int i = 0; i < msgs.length; i++) {
				cal.setTime(msgs[i].getSentDate());
				subject = getSubject(msgs[i]);
				mailTime = TimeStamp.Calendar2Str(cal, 14);
				fromAddr = getFrom(msgs[i]);

				/** �ж��Ƿ�Ϊ���ʼ� */
				if (mailTime.compareTo(lastTime) > 0) {
					mailHead[i][0] = subject;
					mailHead[i][1] = mailTime;
					mailHead[i][2] = fromAddr;
					count++;
				}
			}

			return count;
		}
		catch (Exception ex) {
			popError = ex.toString();
			return -1;
		}
		finally {
			try {
				if (folder != null) {
					folder.close(false);
				}
			}
			catch (Exception e) {
			}

			try {
				if (store != null) {
					store.close();
				}
			}
			catch (Exception e) {
			}
		}
	}

	/**
	 * �����ʼ��˻�
	 * 
	 * @param server
	 *            �ʼ��˷���������
	 * @param user
	 *            �ʼ��˻����û���
	 * @param password
	 *            �ʼ��˻�������
	 */
	public void setAccount(String server, String user, String password) {
		popServer = server;
		popUser = user;
		popPassword = password;
	}
}
