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
 * 发送邮件. <br>
 * 定义了发送邮件的方法，包括发送普通邮件和html邮件，并可添加附件.
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
 * // 邮件主题
 * String subject = &quot;mail测试&quot;;
 * // 邮件正文内容
 * String sendMsg = FileTools.readTxt(&quot;d:/mail.txt&quot;);
 * // 收件人地址，支持多个收件人(以逗号分隔)
 * String toAddr = &quot;zhouyan@c-platform.com&quot;;
 * 
 * // 邮件附件
 * Vector&lt;String&gt; fileList = new Vector&lt;String&gt;();
 * fileList.add(&quot;d:/mail.txt&quot;);
 * 
 * // 设置邮件服务器、邮件账户的用户名、（邮件账户的密码）
 * SendMail sendMail = new SendMail(&quot;mail.c-platform.com&quot;, &quot;zhouyan@c-platform.com&quot;);
 * // 设置是否需要debug打印调试
 * sendMail.setIsDebug(false);
 * 
 * // 发送HTML邮件,可设置抄送、暗抄
 * boolean flag = sendMail.sendHtmlMail(&quot;发件人姓名&quot;, toAddr, null, null, subject, sendMsg, fileList);
 * 
 * // 发送普通邮件，返回是否发送成功
 * flag = sendMail.send(&quot;zhouyan@c-platform.com&quot;, subject, sendMsg, &quot;d:/mail.txt&quot;);
 * </pre>
 */
public class SendMail {

	/** 设置是否需要debug打印调试 */
	private boolean isDebug = true;

	/** Mail错误信息 */
	private String popError = null;

	/** 邮件帐户的密码 */
	private String popPassword = null;

	/** 邮件服务器参数 */
	private String popServer = null;

	/** 邮件帐户的用户名 */
	private String popUser = null;

	/**
	 * 默认构造函数
	 */
	public SendMail() {
	}

	/**
	 * 构造方法 不需要密码验证
	 * 
	 * @param server
	 *            邮件服务器参数
	 * @param user
	 *            邮件帐户的用户名
	 */
	public SendMail(String server, String user) {
		popServer = server;
		popUser = user;
		popPassword = null;
	}

	/**
	 * 构造方法 需要密码验证
	 * 
	 * @param server
	 *            邮件服务器参数
	 * @param user
	 *            邮件帐户的用户名
	 * @param password
	 *            邮件帐户的密码
	 */
	public SendMail(String server, String user, String password) {
		popServer = server;
		popUser = user;
		popPassword = password;
	}

	/**
	 * 将汉字转化为ISO-8859-1字符
	 * 
	 * @param filename
	 *            文件名
	 * @return 转码后的字符串
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
	 * 返回错误说明
	 * 
	 * @return 错误说明
	 */
	public String getError() {
		return popError;
	}

	/**
	 * 发送邮件
	 * 
	 * @param toAddr
	 *            收件人地址
	 * @param subject
	 *            邮件主题
	 * @param sendMsg
	 *            邮件内容
	 * @param mimeFile
	 *            附件
	 * @return 是否发送成功
	 */
	public boolean send(String toAddr, String subject, String sendMsg, String mimeFile) {
		try {
			// 设置邮件参数
			Properties props = new Properties();
			props.put("mail.smtp.host", popServer);
			props.put("mail.smtp.user", popUser);// 自己信箱的用户名
			if (popPassword != null) {
				props.put("mail.smtp.password", popPassword);// 信箱密码
			}
			if (popPassword == null) {
				props.put("mail.smtp.auth", "false");
			} else {
				props.put("mail.smtp.auth", "true");
			}
			PopupAuthenticator popAuthenticator = new PopupAuthenticator();

			// 设置新的连接会话
			popAuthenticator.performCheck(popUser, popPassword);
			Session mailSession = Session.getDefaultInstance(props, popAuthenticator);
			mailSession.setDebug(isDebug);
			Message msg = new MimeMessage(mailSession);

			// 设定传送邮件的发信人
			InternetAddress from = new InternetAddress(popUser);
			msg.setFrom(from);

			// 设定传送邮件的收信人
			InternetAddress[] address = InternetAddress.parse(toAddr, false);
			msg.setRecipients(Message.RecipientType.TO, address);

			// 设定信中的主题
			msg.setSubject(subject);

			// 设定送信的时间
			msg.setSentDate(new Date());

			// 设定传送信的MIME Type
			if (mimeFile == null) {
				// 没有附件
				msg.setText(sendMsg);
			} else {
				// 设定邮件附件
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
			// 发送邮件
			Transport.send(msg);
			return true;
		}
		catch (MessagingException ex) {
			popError = ex.toString();
			return false;
		}
	}

	/**
	 * 发送html格式的邮件
	 * 
	 * @param fromName
	 *            设置在邮件中显示的发件人的姓名
	 * @param toAddr
	 *            收件人地址，支持多个收件人
	 * @param ccAddr
	 *            抄送人地址
	 * @param bccAddr
	 *            暗送人地址
	 * @param subject
	 *            标题
	 * @param sendMsg
	 *            邮件内容，支持html格式
	 * @param fileList
	 *            附件列表
	 * @return 是否发送成功
	 */
	public boolean sendHtmlMail(String fromName, String toAddr, String ccAddr, String bccAddr, String subject, String sendMsg, Vector<String> fileList) {
		// 设置邮件参数
		Properties props = new Properties();
		props.put("mail.smtp.host", popServer);
		props.put("mail.smtp.user", popUser);// 自己信箱的用户名
		if (popPassword != null) {
			props.put("mail.smtp.password", popPassword);// 信箱密码
		}
		if (popPassword == null) {
			props.put("mail.smtp.auth", "false");
		} else {
			props.put("mail.smtp.auth", "true");
		}
		PopupAuthenticator popAuthenticator = new PopupAuthenticator();
		// 得到默认的对话对象
		popAuthenticator.performCheck(popUser, popPassword);
		Session session = Session.getDefaultInstance(props, popAuthenticator);
		session.setDebug(isDebug);

		try {
			// 创建一个消息，并初始化该消息的各项元素
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

			setToAddr(msg, toAddr);// 接收方
			// 抄送方
			if (ccAddr != null && ccAddr.trim().length() > 0) {
				setCcAddr(msg, ccAddr);

			}
			// 暗送方
			if (bccAddr != null && bccAddr.trim().length() > 0) {
				setBccAddr(msg, bccAddr);
			}

			msg.setSubject(subject);// 标题
			// 后面的BodyPart将加入到此处创建的Multipart中
			Multipart mp = new MimeMultipart();
			if (fileList != null && fileList.size() > 0) {
				String filename = "";
				// 利用枚举器方便的遍历集合
				Enumeration efile = fileList.elements();
				// 检查序列中是否还有更多的对象
				while (efile.hasMoreElements()) {
					try {
						MimeBodyPart mbp = new MimeBodyPart();
						// 选择出每一个附件名
						filename = efile.nextElement().toString();
						// 得到数据源
						FileDataSource fds = new FileDataSource(filename);
						// 得到附件本身并至入BodyPart
						mbp.setDataHandler(new DataHandler(fds));
						// 得到文件名同样至入BodyPart
						mbp.setFileName(new String(fds.getName().getBytes("gb2312"), "iso-8859-1"));
						mp.addBodyPart(mbp);
					}
					catch (Exception e) {
						e.printStackTrace();
					}
				}
				// 移走集合中的所有元素
				fileList.removeAllElements();
			}

			// 添加邮件内容
			mp.addBodyPart(htmlodyPart);
			// Multipart加入到信件
			msg.setContent(mp);
			msg.saveChanges();
			// 设置信件头的发送日期
			msg.setSentDate(new Date());
			// 发送信件
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
	 * 发送html格式的邮件
	 * 
	 * @param toAddr
	 *            收件人地址，支持多个收件人
	 * @param ccAddr
	 *            抄送人地址
	 * @param bccAddr
	 *            暗送人地址
	 * @param subject
	 *            标题
	 * @param sendMsg
	 *            邮件内容，支持html格式
	 * @param fileList
	 *            附件列表
	 * @return 是否发送成功
	 */
	public boolean sendHtmlMail(String toAddr, String ccAddr, String bccAddr, String subject, String sendMsg, Vector<String> fileList) {
		return sendHtmlMail(null, toAddr, ccAddr, bccAddr, subject, sendMsg, fileList);
	}

	/**
	 * 设置邮件账户
	 * 
	 * @param server
	 *            邮件服务器参数
	 * @param user
	 *            邮件帐户的用户名
	 * @param password
	 *            邮件帐户的密码
	 */
	public void setAccount(String server, String user, String password) {
		popServer = server;
		popUser = user;
		popPassword = password;
	}

	/**
	 * 设置暗送
	 * 
	 * @param msg
	 *            发送内容
	 * @param bccAddr
	 *            暗送人地址
	 * @throws Exception
	 */
	private void setBccAddr(Message msg, String bccAddr) throws Exception {
		try {
			String[] bccAddrs = bccAddr.split(",");// 逗号分隔
			if (bccAddrs.length > 1) {
				InternetAddress[] bcc = new InternetAddress[bccAddrs.length];
				for (int i = 0; i < bccAddrs.length; i++) {
					bcc[i] = new InternetAddress(bccAddrs[i]);
				}
				msg.setRecipients(Message.RecipientType.BCC, bcc);
			} else {
				bccAddrs = bccAddr.split(";");// 分号分隔
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
	 * 设置抄送
	 * 
	 * @param msg
	 *            发送内容
	 * @param ccAddr
	 *            抄送人地址
	 * @throws Exception
	 */
	private void setCcAddr(Message msg, String ccAddr) throws Exception {
		try {
			String[] ccAddrs = ccAddr.split(",");// 逗号分隔
			if (ccAddrs.length > 1) {
				InternetAddress[] cc = new InternetAddress[ccAddrs.length];
				for (int i = 0; i < ccAddrs.length; i++) {
					cc[i] = new InternetAddress(ccAddrs[i]);
				}
				msg.setRecipients(Message.RecipientType.CC, cc);
			} else {
				ccAddrs = ccAddr.split(";");// 分号分隔
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
	 * 设置是否需要debug打印调试
	 * 
	 * @param isDebug
	 *            是否需要debug打印调试
	 */
	public void setIsDebug(boolean isDebug) {
		this.isDebug = isDebug;
	}

	/**
	 * 设置接收方号码
	 * 
	 * @param msg
	 *            发送内容
	 * @param toAddr
	 *            收件人地址
	 * @throws Exception
	 */
	private void setToAddr(Message msg, String toAddr) throws Exception {
		String[] toAddrs = toAddr.split(",");// 逗号分隔
		if (toAddrs.length > 1) {
			InternetAddress[] to = new InternetAddress[toAddrs.length];
			for (int i = 0; i < toAddrs.length; i++) {
				to[i] = new InternetAddress(toAddrs[i]);
			}
			msg.setRecipients(Message.RecipientType.TO, to);
		} else {
			toAddrs = toAddr.split(";");// 分号分隔
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
