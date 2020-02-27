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
 * 接收邮件. <br>
 * 实现了从服务器上接收邮件的功能.
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
	 * 获取发件人
	 * 
	 * @param msg
	 *            邮件内容
	 * @return 发件人地址
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

	/** 新邮件缓存 */
	private String[][] mailHead = null;

	/** Mail错误信息 */
	private String popError = null;

	/** 邮件账户的密码 */
	private String popPassword = null;

	/** 邮件账服务器参数 */
	private String popServer = null;

	/** 邮件账户的用户名 */
	private String popUser = null;

	/**
	 * 默认构造函数
	 */
	public RecvMail() {
	}

	/**
	 * 构造方法 需要密码验证
	 * 
	 * @param server
	 *            邮件账服务器参数
	 * @param user
	 *            邮件账户的用户名
	 * @param password
	 *            邮件账户的密码
	 */
	public RecvMail(String server, String user, String password) {
		popServer = server;
		popUser = user;
		popPassword = password;
	}

	/**
	 * 返回错误说明
	 * 
	 * @return 错误说明
	 */
	public String getError() {
		return popError;
	}

	/**
	 * 从新邮件缓存中获取指定邮件的指定内容
	 * 
	 * @param num
	 *            表示第几封邮件
	 * @param id
	 *            表示获取邮件的哪个信息<BR>
	 *            其中：0表示邮件主题，1表示邮件的发送时间，2表示发送人地址
	 * @return 获取到的内容
	 */
	public String getMailParam(int num, int id) {
		if (num >= mailHead.length)
			return null;

		if (id > 2)
			return null;

		return mailHead[num][id];
	}

	/**
	 * 获取邮件主题
	 * 
	 * @param msg
	 *            邮件内容
	 * @return 邮件主题
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
	 * 检查接收邮件
	 * 
	 * @param lastTime
	 *            上次接收邮件时间
	 * @return 接收成功返回count：邮件数量；接收失败返回-1
	 */
	public int receive(String lastTime) {
		/** 清空错误说明 */
		popError = "";

		/** 调用Mail API */
		Store store = null;
		Folder folder = null;
		try {
			Properties props = System.getProperties();
			Session session = Session.getDefaultInstance(props, null);
			store = session.getStore("pop3");

			/** 连接用户邮箱 */
			try {
				store.connect(popServer, popUser, popPassword);
			}
			catch (Exception e) {
				popError = " POP3 : " + popServer + " 邮箱 : " + popUser + " 密码 : " + popPassword + " connect \r\n" + e.toString();

				return -1;
			}

			/** 获取缺省目录 */
			folder = store.getDefaultFolder();
			if (folder == null) {
				popError = " 邮箱 : " + popUser + " No default folder";

				return -1;
			}

			/** 获取收件箱 */
			folder = folder.getFolder("INBOX");
			if (folder == null) {
				popError = " 邮箱 : " + popUser + " No POP3 INBOX";

				return -1;
			}

			/** 打开收件箱 */
			folder.open(Folder.READ_ONLY);

			/** 获取邮件 */
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

				/** 判断是否为新邮件 */
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
	 * 设置邮件账户
	 * 
	 * @param server
	 *            邮件账服务器参数
	 * @param user
	 *            邮件账户的用户名
	 * @param password
	 *            邮件账户的密码
	 */
	public void setAccount(String server, String user, String password) {
		popServer = server;
		popUser = user;
		popPassword = password;
	}
}
