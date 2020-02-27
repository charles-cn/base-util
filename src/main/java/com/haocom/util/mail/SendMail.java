package com.haocom.util.mail;

import java.util.Date;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 * �����ʼ�. <br>
 * �����˷����ʼ��ķ���������������ͨ�ʼ���html�ʼ���������Ӹ���.
 * <p>
 * Copyright: Copyright (c) Sep 16, 2008
 * <p>
 * Company: 
 * <p>
 * Author:
 * <p>
 * Version: 1.0
 * <p>
 * 
 * <pre>
 * // �ʼ�����
 * String subject = &quot;mail����&quot;;
 * // �ʼ���������
 * String sendMsg = FileTools.readTxt(&quot;d:/mail.txt&quot;);
 * // �ռ��˵�ַ��֧�ֶ���ռ���(�Զ��ŷָ�)
 * String toAddr = &quot;zhouyan@c-platform.com&quot;;
 * 
 * // �ʼ�����
 * Vector&lt;String&gt; fileList = new Vector&lt;String&gt;();
 * fileList.add(&quot;d:/mail.txt&quot;);
 * 
 * // �����ʼ����������ʼ��˻����û��������ʼ��˻������룩
 * SendMail sendMail = new SendMail(&quot;mail.c-platform.com&quot;, &quot;zhouyan@c-platform.com&quot;);
 * // �����Ƿ���Ҫdebug��ӡ����
 * sendMail.setIsDebug(false);
 * 
 * // ����HTML�ʼ�,�����ó��͡�����
 * boolean flag = sendMail.sendHtmlMail(&quot;����������&quot;, toAddr, null, null, subject, sendMsg, fileList);
 * 
 * // ������ͨ�ʼ��������Ƿ��ͳɹ�
 * flag = sendMail.send(&quot;zhouyan@c-platform.com&quot;, subject, sendMsg, &quot;d:/mail.txt&quot;);
 * </pre>
 */
public class SendMail {

	/** �����Ƿ���Ҫdebug��ӡ���� */
	private boolean isDebug = true;

	/** Mail������Ϣ */
	private String popError = null;

	/** �ʼ��ʻ������� */
	private String popPassword = null;

	/** �ʼ����������� */
	private String popServer = null;

	/** �ʼ��ʻ����û��� */
	private String popUser = null;

	/**
	 * Ĭ�Ϲ��캯��
	 */
	public SendMail() {
	}

	/**
	 * ���췽�� ����Ҫ������֤
	 * 
	 * @param server
	 *            �ʼ�����������
	 * @param user
	 *            �ʼ��ʻ����û���
	 */
	public SendMail(String server, String user) {
		popServer = server;
		popUser = user;
		popPassword = null;
	}

	/**
	 * ���췽�� ��Ҫ������֤
	 * 
	 * @param server
	 *            �ʼ�����������
	 * @param user
	 *            �ʼ��ʻ����û���
	 * @param password
	 *            �ʼ��ʻ�������
	 */
	public SendMail(String server, String user, String password) {
		popServer = server;
		popUser = user;
		popPassword = password;
	}

	/**
	 * ������ת��ΪISO-8859-1�ַ�
	 * 
	 * @param filename
	 *            �ļ���
	 * @return ת�����ַ���
	 */
	private String gb2iso(String filename) {
		try {
			return new String(filename.getBytes(), "ISO-8859-1");
		}
		catch (Exception e) {
			return "";
		}
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
	 * �����ʼ�
	 * 
	 * @param toAddr
	 *            �ռ��˵�ַ
	 * @param subject
	 *            �ʼ�����
	 * @param sendMsg
	 *            �ʼ�����
	 * @param mimeFile
	 *            ����
	 * @return �Ƿ��ͳɹ�
	 */
	public boolean send(String toAddr, String subject, String sendMsg, String mimeFile) {
		try {
			// �����ʼ�����
			Properties props = new Properties();
			props.put("mail.smtp.host", popServer);
			props.put("mail.smtp.user", popUser);// �Լ�������û���
			if (popPassword != null) {
				props.put("mail.smtp.password", popPassword);// ��������
			}
			if (popPassword == null) {
				props.put("mail.smtp.auth", "false");
			} else {
				props.put("mail.smtp.auth", "true");
			}
			PopupAuthenticator popAuthenticator = new PopupAuthenticator();

			// �����µ����ӻỰ
			popAuthenticator.performCheck(popUser, popPassword);
			Session mailSession = Session.getDefaultInstance(props, popAuthenticator);
			mailSession.setDebug(isDebug);
			Message msg = new MimeMessage(mailSession);

			// �趨�����ʼ��ķ�����
			InternetAddress from = new InternetAddress(popUser);
			msg.setFrom(from);

			// �趨�����ʼ���������
			InternetAddress[] address = InternetAddress.parse(toAddr, false);
			msg.setRecipients(Message.RecipientType.TO, address);

			// �趨���е�����
			msg.setSubject(subject);

			// �趨���ŵ�ʱ��
			msg.setSentDate(new Date());

			// �趨�����ŵ�MIME Type
			if (mimeFile == null) {
				// û�и���
				msg.setText(sendMsg);
			} else {
				// �趨�ʼ�����
				FileDataSource fds = null;
				MimeBodyPart mbpart = new MimeBodyPart();
				mbpart.setText(sendMsg);
				Multipart mpart = new MimeMultipart();
				mpart.addBodyPart(mbpart);

				String[] files = mimeFile.split(",");
				for (int i = 0; i < files.length; i++) {
					mbpart = new MimeBodyPart();
					fds = new FileDataSource(files[i]);
					mbpart.setDataHandler(new DataHandler(fds));
					mbpart.setFileName(gb2iso(fds.getName()));
					mpart.addBodyPart(mbpart);
				}
				msg.setContent(mpart);
			}
			// �����ʼ�
			Transport.send(msg);
			return true;
		}
		catch (MessagingException ex) {
			popError = ex.toString();
			return false;
		}
	}

	/**
	 * ����html��ʽ���ʼ�
	 * 
	 * @param fromName
	 *            �������ʼ�����ʾ�ķ����˵�����
	 * @param toAddr
	 *            �ռ��˵�ַ��֧�ֶ���ռ���
	 * @param ccAddr
	 *            �����˵�ַ
	 * @param bccAddr
	 *            �����˵�ַ
	 * @param subject
	 *            ����
	 * @param sendMsg
	 *            �ʼ����ݣ�֧��html��ʽ
	 * @param fileList
	 *            �����б�
	 * @return �Ƿ��ͳɹ�
	 */
	public boolean sendHtmlMail(String fromName, String toAddr, String ccAddr, String bccAddr, String subject, String sendMsg, Vector<String> fileList) {
		// �����ʼ�����
		Properties props = new Properties();
		props.put("mail.smtp.host", popServer);
		props.put("mail.smtp.user", popUser);// �Լ�������û���
		if (popPassword != null) {
			props.put("mail.smtp.password", popPassword);// ��������
		}
		if (popPassword == null) {
			props.put("mail.smtp.auth", "false");
		} else {
			props.put("mail.smtp.auth", "true");
		}
		PopupAuthenticator popAuthenticator = new PopupAuthenticator();
		// �õ�Ĭ�ϵĶԻ�����
		popAuthenticator.performCheck(popUser, popPassword);
		Session session = Session.getDefaultInstance(props, popAuthenticator);
		session.setDebug(isDebug);

		try {
			// ����һ����Ϣ������ʼ������Ϣ�ĸ���Ԫ��
			MimeMessage msg = new MimeMessage(session);

			MimeMultipart multipart = new MimeMultipart();
			MimeBodyPart htmlodyPart = new MimeBodyPart();

			htmlodyPart.setContent(sendMsg, "text/html;charset=GB2312");

			htmlodyPart.setHeader("Content-Transfer-Encoding", "base64");
			multipart.addBodyPart(htmlodyPart);

			if (fromName != null && !"".equals(fromName)) {
				msg.setFrom(new InternetAddress(popUser, fromName));
			} else {
				msg.setFrom(new InternetAddress(popUser));
			}

			setToAddr(msg, toAddr);// ���շ�
			// ���ͷ�
			if (ccAddr != null && ccAddr.trim().length() > 0) {
				setCcAddr(msg, ccAddr);

			}
			// ���ͷ�
			if (bccAddr != null && bccAddr.trim().length() > 0) {
				setBccAddr(msg, bccAddr);
			}

			msg.setSubject(subject);// ����
			// �����BodyPart�����뵽�˴�������Multipart��
			Multipart mp = new MimeMultipart();
			if (fileList != null && fileList.size() > 0) {
				String filename = "";
				// ����ö��������ı�������
				Enumeration efile = fileList.elements();
				// ����������Ƿ��и���Ķ���
				while (efile.hasMoreElements()) {
					try {
						MimeBodyPart mbp = new MimeBodyPart();
						// ѡ���ÿһ��������
						filename = efile.nextElement().toString();
						// �õ�����Դ
						FileDataSource fds = new FileDataSource(filename);
						// �õ�������������BodyPart
						mbp.setDataHandler(new DataHandler(fds));
						// �õ��ļ���ͬ������BodyPart
						mbp.setFileName(new String(fds.getName().getBytes("gb2312"), "iso-8859-1"));
						mp.addBodyPart(mbp);
					}
					catch (Exception e) {
						e.printStackTrace();
					}
				}
				// ���߼����е�����Ԫ��
				fileList.removeAllElements();
			}

			// ����ʼ�����
			mp.addBodyPart(htmlodyPart);
			// Multipart���뵽�ż�
			msg.setContent(mp);
			msg.saveChanges();
			// �����ż�ͷ�ķ�������
			msg.setSentDate(new Date());
			// �����ż�
			Transport.send(msg);
		}
		catch (MessagingException mex) {
			mex.printStackTrace();
			Exception ex = null;
			if ((ex = mex.getNextException()) != null) {
				ex.printStackTrace();
			}
			return false;
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * ����html��ʽ���ʼ�
	 * 
	 * @param toAddr
	 *            �ռ��˵�ַ��֧�ֶ���ռ���
	 * @param ccAddr
	 *            �����˵�ַ
	 * @param bccAddr
	 *            �����˵�ַ
	 * @param subject
	 *            ����
	 * @param sendMsg
	 *            �ʼ����ݣ�֧��html��ʽ
	 * @param fileList
	 *            �����б�
	 * @return �Ƿ��ͳɹ�
	 */
	public boolean sendHtmlMail(String toAddr, String ccAddr, String bccAddr, String subject, String sendMsg, Vector<String> fileList) {
		return sendHtmlMail(null, toAddr, ccAddr, bccAddr, subject, sendMsg, fileList);
	}

	/**
	 * �����ʼ��˻�
	 * 
	 * @param server
	 *            �ʼ�����������
	 * @param user
	 *            �ʼ��ʻ����û���
	 * @param password
	 *            �ʼ��ʻ�������
	 */
	public void setAccount(String server, String user, String password) {
		popServer = server;
		popUser = user;
		popPassword = password;
	}

	/**
	 * ���ð���
	 * 
	 * @param msg
	 *            ��������
	 * @param bccAddr
	 *            �����˵�ַ
	 * @throws Exception
	 */
	private void setBccAddr(Message msg, String bccAddr) throws Exception {
		try {
			String[] bccAddrs = bccAddr.split(",");// ���ŷָ�
			if (bccAddrs.length > 1) {
				InternetAddress[] bcc = new InternetAddress[bccAddrs.length];
				for (int i = 0; i < bccAddrs.length; i++) {
					bcc[i] = new InternetAddress(bccAddrs[i]);
				}
				msg.setRecipients(Message.RecipientType.BCC, bcc);
			} else {
				bccAddrs = bccAddr.split(";");// �ֺŷָ�
				if (bccAddrs.length > 1) {
					InternetAddress[] bcc = new InternetAddress[bccAddrs.length];
					for (int i = 0; i < bccAddrs.length; i++) {
						bcc[i] = new InternetAddress(bccAddrs[i]);
					}
					msg.setRecipients(Message.RecipientType.BCC, bcc);
				} else {
					InternetAddress bcc = new InternetAddress(bccAddr);
					msg.setRecipient(Message.RecipientType.BCC, bcc);
				}
			}
		}
		catch (Exception ex) {
		}
	}

	/**
	 * ���ó���
	 * 
	 * @param msg
	 *            ��������
	 * @param ccAddr
	 *            �����˵�ַ
	 * @throws Exception
	 */
	private void setCcAddr(Message msg, String ccAddr) throws Exception {
		try {
			String[] ccAddrs = ccAddr.split(",");// ���ŷָ�
			if (ccAddrs.length > 1) {
				InternetAddress[] cc = new InternetAddress[ccAddrs.length];
				for (int i = 0; i < ccAddrs.length; i++) {
					cc[i] = new InternetAddress(ccAddrs[i]);
				}
				msg.setRecipients(Message.RecipientType.CC, cc);
			} else {
				ccAddrs = ccAddr.split(";");// �ֺŷָ�
				if (ccAddrs.length > 1) {
					InternetAddress[] cc = new InternetAddress[ccAddrs.length];
					for (int i = 0; i < ccAddrs.length; i++) {
						cc[i] = new InternetAddress(ccAddrs[i]);
					}
					msg.setRecipients(Message.RecipientType.CC, cc);
				} else {
					InternetAddress cc = new InternetAddress(ccAddr);
					msg.setRecipient(Message.RecipientType.CC, cc);
				}
			}
		}
		catch (Exception ex) {
		}
	}

	/**
	 * �����Ƿ���Ҫdebug��ӡ����
	 * 
	 * @param isDebug
	 *            �Ƿ���Ҫdebug��ӡ����
	 */
	public void setIsDebug(boolean isDebug) {
		this.isDebug = isDebug;
	}

	/**
	 * ���ý��շ�����
	 * 
	 * @param msg
	 *            ��������
	 * @param toAddr
	 *            �ռ��˵�ַ
	 * @throws Exception
	 */
	private void setToAddr(Message msg, String toAddr) throws Exception {
		String[] toAddrs = toAddr.split(",");// ���ŷָ�
		if (toAddrs.length > 1) {
			InternetAddress[] to = new InternetAddress[toAddrs.length];
			for (int i = 0; i < toAddrs.length; i++) {
				to[i] = new InternetAddress(toAddrs[i]);
			}
			msg.setRecipients(Message.RecipientType.TO, to);
		} else {
			toAddrs = toAddr.split(";");// �ֺŷָ�
			if (toAddrs.length > 1) {
				InternetAddress[] to = new InternetAddress[toAddrs.length];
				for (int i = 0; i < toAddrs.length; i++) {
					to[i] = new InternetAddress(toAddrs[i]);
				}
				msg.setRecipients(Message.RecipientType.TO, to);
			} else {
				InternetAddress to = new InternetAddress(toAddr);
				msg.setRecipient(Message.RecipientType.TO, to);
			}
		}
	}
}
