package com.haocom.util.ssh;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.UserInfo;

/**
 * Session����. <br>
 * ssh�ĵ�¼����,����ʵ��ssh�ĵ�¼��ͨ�����÷�������ַ���ʺš��������Ϣ���е�½�����ҿ��Դ�Session��SFTP��EXECͨ����
 * ����SFTP�Ĵ��������EXEC�����ִ��.
 * <p>
 * Copyright: Copyright (c) 2009-7-5 ����02:41:40
 * <p>
 * Company: 
 * <p>
 * Author: ChengFan
 * <p>
 * Version: 1.0
 * <p>
 */
public class SessionConfig {

	/** �ն��ַ��� */
	private String encoding = "GB18030";

	/** ��������ַ */
	private String host;

	/** ��Կ�ļ��� */
	private String identity;

	/** ��Կ�ļ����� */
	private String passphrase;

	/** ��¼���� */
	private String password;

	/** �������˿� */
	private int port;

	/** ��¼�ʺ��� */
	private String username;

	/**
	 * ���캯��.
	 */
	public SessionConfig() {
	}

	/**
	 * ���캯��.
	 * 
	 * @param host
	 *            ��������ַ
	 * @param port
	 *            �������˿�
	 */
	public SessionConfig(String host, int port) {
		super();
		this.host = host;
		this.port = port;
	}

	/**
	 * ���캯��.<br>
	 * �ʺ������ʺš������¼��ʽ.
	 * 
	 * @param host
	 *            ��������ַ
	 * @param port
	 *            �������˿�
	 * @param username
	 *            ��¼�ʺ���
	 * @param password
	 *            ��¼����
	 */
	public SessionConfig(String host, int port, String username, String password) {
		super();
		this.host = host;
		this.port = port;
		this.username = username;
		this.password = password;
	}

	/**
	 * ���캯��.<br>
	 * �ʺ�������Կ�ļ���¼��ʽ.
	 * 
	 * @param host
	 *            ��������ַ
	 * @param port
	 *            �������˿�
	 * @param username
	 *            ��¼�ʺ���
	 * @param identity
	 *            ��Կ�ļ���
	 * @param passphrase
	 *            ��Կ�ļ����� ����û�����ã�������дnull
	 */
	public SessionConfig(String host, int port, String username, String identity, String passphrase) {
		super();
		this.host = host;
		this.port = port;
		this.username = username;
		this.identity = identity;
		this.passphrase = passphrase;
	}

	/**
	 * ��ȡ �ն��ַ���
	 * 
	 * @return �ն��ַ���
	 */
	public String getEncoding() {
		return encoding;
	}

	/**
	 * ��ȡ ��������ַ
	 * 
	 * @return host ��������ַ
	 */
	public String getHost() {
		return host;
	}

	/**
	 * ��ȡ ��Կ�ļ���
	 * 
	 * @return ��Կ�ļ���
	 */
	public String getIdentity() {
		return identity;
	}

	/**
	 * ��ȡ ��Կ�ļ�����
	 * 
	 * @return ��Կ�ļ�����
	 */
	public String getPassphrase() {
		return passphrase;
	}

	/**
	 * ��ȡ ��¼����
	 * 
	 * @return ��¼����
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * ��ȡ �������˿�
	 * 
	 * @return �������˿�
	 */
	public int getPort() {
		return port;
	}

	/**
	 * ��ȡ ��¼�ʺ���
	 * 
	 * @return ��¼�ʺ���
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * �½�ͨ��
	 * 
	 * @param session
	 *            SSH Session������openSession������ȡ
	 * @param type
	 *            ͨ�����ͣ������������ͣ�session, shell, exec, x11, auth-agent@openssh.com,
	 *            direct-tcpip, forwarded-tcpip, sftp, subsystem<br>
	 *            ����Ӧ��Channel����Ϊ��ChannelSession, ChannelShell, ChannelExec,
	 *            ChannelX11, ChannelAgentForwarding, ChannelDirectTCPIP,
	 *            ChannelForwardedTCPIP, ChannelSftp, ChannelSubsystem <br>
	 * @param timeout
	 *            ��ʱʱ�䣨���룩
	 * @return �´�����ͨ��
	 * @throws Exception
	 */
	public Channel openChannel(Session session, String type, int timeout) throws Exception {
		Channel channel = session.openChannel(type);
		channel.connect(timeout);
		return channel;
	}

	/**
	 * ��Execͨ��.<br>
	 * ���ص�ͨ��Ϊ��δ����״̬����Ҫ���������ò�������Ҵ�����, ע����������úò�������channel.setCommand(String
	 * command)��Ȼ���ٴ�����channel.connect(5000).<br>
	 * �򿪵�ͨ������رգ���������ӻ�һֱ����.����ʹ��try...finally...�ṹ. <code>
	 * <pre>
	 * // ����Execͨ��(��ʱ5��)
	 * ChannelExec channel = openChannelExec(session);
	 * try {
	 *  // ����Exec.
	 *  channel.setCommand("pwd");
	 *  channel.connect(5000);
	 * }
	 * catch (Exception ex) {
	 * 	// �����쳣
	 * 	throw ex;
	 * }
	 * finally {
	 * 	// �ر�Execͨ��
	 * 	channel.disconnect();
	 * }
	 * </pre>
	 * </code>
	 * 
	 * @param session
	 *            SSH Session������openSession������ȡ
	 * @return �´�Execͨ��
	 * @throws Exception
	 */
	public ChannelExec openChannelExec(Session session) throws Exception {
		ChannelExec channel = (ChannelExec) session.openChannel("exec");
		return channel;
	}

	/**
	 * ��Execͨ����Ĭ�ϳ�ʱ5��.<br>
	 * ���ص�ͨ��Ϊ������״̬��������ִ��channel.connect(timeout)����.<br>
	 * �򿪵�ͨ������رգ���������ӻ�һֱ����.����ʹ��try...finally...�ṹ. <code>
	 * <pre>
	 * // ����Execͨ��(Ĭ�ϳ�ʱ5��)
	 * ChannelExec channel = openChannelExec(session, command);
	 * try {
	 * 	// ����Exec
	 * }
	 * catch (Exception ex) {
	 * 	// �����쳣
	 * 	throw ex;
	 * }
	 * finally {
	 * 	// �ر�Execͨ��
	 * 	channel.disconnect();
	 * }
	 * </pre>
	 * </code>
	 * 
	 * @param session
	 *            SSH Session������openSession������ȡ
	 * @param command
	 *            ִ�е�����
	 * @return �´�Execͨ��
	 * @throws Exception
	 */
	public ChannelExec openChannelExec(Session session, String command) throws Exception {
		return openChannelExec(session, command, 5000);
	}

	/**
	 * ��Execͨ��.<br>
	 * ���ص�ͨ��Ϊ������״̬��������ִ��channel.connect(timeout)����.<br>
	 * �򿪵�ͨ������رգ���������ӻ�һֱ����.����ʹ��try...finally...�ṹ. <code>
	 * <pre>
	 * // ����Execͨ��(��ʱ5��)
	 * ChannelExec channel = openChannelExec(session, command, 5000);
	 * try {
	 * 	// ����Exec
	 * }
	 * catch (Exception ex) {
	 * 	// �����쳣
	 * 	throw ex;
	 * }
	 * finally {
	 * 	// �ر�Execͨ��
	 * 	channel.disconnect();
	 * }
	 * </pre>
	 * </code>
	 * 
	 * @param session
	 *            SSH Session������openSession������ȡ
	 * @param command
	 *            ִ�е�����
	 * @param timeout
	 *            ��ʱʱ�䣨���룩
	 * @return �´�Execͨ��
	 * @throws Exception
	 */
	public ChannelExec openChannelExec(Session session, String command, int timeout) throws Exception {
		ChannelExec channel = (ChannelExec) session.openChannel("exec");
		channel.setCommand(command);
		channel.connect(timeout);
		return channel;
	}

	/**
	 * ��FTPͨ����Ĭ�ϳ�ʱ5��.<br>
	 * �򿪵�ͨ������رգ���������ӻ�һֱ����.����ʹ��try...finally...�ṹ. <code>
	 * <pre>
	 * // ����SFTPͨ��(��ʱ5��)
	 * ChannelSftp channel = config.openChannelSftp(session);
	 * try {
	 * 	// ����SFTP
	 * 	channel.cd(&quot;/&quot;);
	 * }
	 * catch (Exception ex) {
	 * 	// �����쳣
	 * 	throw ex;
	 * }
	 * finally {
	 * 	// �ر�SFTPͨ�� 
	 * 	channel.disconnect();
	 * }
	 * </pre>
	 * </code>
	 * 
	 * @param session
	 *            SSH Session������openSession������ȡ
	 * @return �´�FTPͨ��
	 * @throws Exception
	 */
	public ChannelSftp openChannelSftp(Session session) throws Exception {
		return openChannelSftp(session, 5000);
	}

	/**
	 * ��FTPͨ��.<br>
	 * �򿪵�ͨ������رգ���������ӻ�һֱ����.����ʹ��try...finally...�ṹ. <code>
	 * <pre>
	 * // ����SFTPͨ��(��ʱ5��)
	 * ChannelSftp channel = config.openChannelSftp(session, 5000);
	 * try {
	 * 	// ����SFTP
	 * 	channel.cd(&quot;/&quot;);
	 * }
	 * catch (Exception ex) {
	 * 	// �����쳣
	 * 	throw ex;
	 * }
	 * finally {
	 * 	// �ر�SFTPͨ��
	 * 	channel.disconnect();
	 * }
	 * </pre>
	 * </code>
	 * 
	 * @param session
	 *            SSH Session������openSession������ȡ
	 * @param timeout
	 *            ��ʱʱ�䣨���룩
	 * @return �´�FTPͨ��
	 * @throws Exception
	 */
	public ChannelSftp openChannelSftp(Session session, int timeout) throws Exception {
		ChannelSftp channel = (ChannelSftp) session.openChannel("sftp");
		channel.connect(timeout);
		channel.setFilenameEncoding(encoding);
		return channel;
	}

	/**
	 * ��Sesesion.Ĭ�ϵĳ�ʱʱ��Ϊ5��. <br>
	 * �򿪵�Session����رգ���������ӻ�һֱ����.����ʹ��try...finally...�ṹ.
	 * 
	 * <pre>
	 * //��Session
	 * Session session = config.openSession();
	 * try {
	 * 	//����Session
	 * }
	 * catch (Exception ex) {
	 * 	//�쳣����
	 * 	throw ex;
	 * }
	 * finally {
	 * 	//�ر�session
	 * 	session.disconnect();
	 * }
	 * </pre>
	 * 
	 * @return �½�Session
	 * @throws Exception
	 */
	public Session openSession() throws Exception {
		return openSession(5000);
	}

	/**
	 * ��Sesesion.<br>
	 * �򿪵�Session����رգ�����ʹ��try...finally...�ṹ.
	 * 
	 * <pre>
	 * //��Session(��ʱ5��)
	 * Session session = config.openSession(5000);
	 * try {
	 * 	//����Session
	 * }
	 * catch (Exception ex) {
	 * 	//�쳣����
	 * 	throw ex;
	 * }
	 * finally {
	 * 	//�ر�session
	 * 	session.disconnect();
	 * }
	 * </pre>
	 * 
	 * @param timeout
	 *            ��ʱʱ�䣨���룩
	 * @return �½���Session
	 * @throws Exception
	 */
	public Session openSession(int timeout) throws Exception {
		JSch jsch = new JSch();
		if (identity != null) {
			jsch.addIdentity(identity);
		}
		Session session = jsch.getSession(username, host, port);
		UserInfo userInfo = new SshUserInfo(password, passphrase);
		session.setUserInfo(userInfo);
		session.setDaemonThread(true);
		session.connect(timeout);
		return session;
	}

	/**
	 * ���� �ն��ַ���
	 * 
	 * @param encoding
	 *            �ն��ַ���
	 */
	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	/**
	 * ���� ��������ַ
	 * 
	 * @param host
	 *            ��������ַ
	 */
	public void setHost(String host) {
		this.host = host;
	}

	/**
	 * ���� ��Կ�ļ���
	 * 
	 * @param identity
	 *            ��Կ�ļ���
	 */
	public void setIdentity(String identity) {
		this.identity = identity;
	}

	/**
	 * ���� ��Կ�ļ�����
	 * 
	 * @param passphrase
	 *            ��Կ�ļ�����
	 */
	public void setPassphrase(String passphrase) {
		this.passphrase = passphrase;
	}

	/**
	 * ���� ��¼����
	 * 
	 * @param password
	 *            ��¼����
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * ���� �������˿�
	 * 
	 * @param port
	 *            �������˿�
	 */
	public void setPort(int port) {
		this.port = port;
	}

	/**
	 * ���� ��¼�ʺ���
	 * 
	 * @param username
	 *            ��¼�ʺ���
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public String toString() {
		StringBuilder buf = new StringBuilder(500);
		buf.append("host=").append(host).append(", ");
		buf.append("port=").append(port).append(", ");
		buf.append("username=").append(username).append(", ");
		if (password != null) {
			buf.append("password=").append(password).append(", ");
		}
		if (identity != null) {
			buf.append("identity=").append(identity).append(", ");
		}
		if (passphrase != null) {
			buf.append("passphrase=").append(passphrase).append(", ");
		}
		return buf.toString();
	}
}
