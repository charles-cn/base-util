package com.haocom.util.ssh;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.UserInfo;

/**
 * Session配置. <br>
 * ssh的登录工具,可以实现ssh的登录：通过配置服务器地址、帐号、密码等信息进行登陆，并且可以打开Session和SFTP、EXEC通道，
 * 进行SFTP的传输或者是EXEC命令的执行.
 * <p>
 * Copyright: Copyright (c) 2009-7-5 下午02:41:40
 * <p>
 * Company: 
 * <p>
 * Author: ChengFan
 * <p>
 * Version: 1.0
 * <p>
 */
public class SessionConfig {

	/** 终端字符集 */
	private String encoding = "GB18030";

	/** 服务器地址 */
	private String host;

	/** 密钥文件名 */
	private String identity;

	/** 密钥文件密码 */
	private String passphrase;

	/** 登录密码 */
	private String password;

	/** 服务器端口 */
	private int port;

	/** 登录帐号名 */
	private String username;

	/**
	 * 构造函数.
	 */
	public SessionConfig() {
	}

	/**
	 * 构造函数.
	 * 
	 * @param host
	 *            服务器地址
	 * @param port
	 *            服务器端口
	 */
	public SessionConfig(String host, int port) {
		super();
		this.host = host;
		this.port = port;
	}

	/**
	 * 构造函数.<br>
	 * 适合于用帐号、密码登录方式.
	 * 
	 * @param host
	 *            服务器地址
	 * @param port
	 *            服务器端口
	 * @param username
	 *            登录帐号名
	 * @param password
	 *            登录密码
	 */
	public SessionConfig(String host, int port, String username, String password) {
		super();
		this.host = host;
		this.port = port;
		this.username = username;
		this.password = password;
	}

	/**
	 * 构造函数.<br>
	 * 适合于用密钥文件登录方式.
	 * 
	 * @param host
	 *            服务器地址
	 * @param port
	 *            服务器端口
	 * @param username
	 *            登录帐号名
	 * @param identity
	 *            密钥文件名
	 * @param passphrase
	 *            密钥文件密码 ，如没有设置，可以填写null
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
	 * 获取 终端字符集
	 * 
	 * @return 终端字符集
	 */
	public String getEncoding() {
		return encoding;
	}

	/**
	 * 获取 服务器地址
	 * 
	 * @return host 服务器地址
	 */
	public String getHost() {
		return host;
	}

	/**
	 * 获取 密钥文件名
	 * 
	 * @return 密钥文件名
	 */
	public String getIdentity() {
		return identity;
	}

	/**
	 * 获取 密钥文件密码
	 * 
	 * @return 密钥文件密码
	 */
	public String getPassphrase() {
		return passphrase;
	}

	/**
	 * 获取 登录密码
	 * 
	 * @return 登录密码
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * 获取 服务器端口
	 * 
	 * @return 服务器端口
	 */
	public int getPort() {
		return port;
	}

	/**
	 * 获取 登录帐号名
	 * 
	 * @return 登录帐号名
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * 新建通道
	 * 
	 * @param session
	 *            SSH Session，可由openSession方法获取
	 * @param type
	 *            通道类型，允许以下类型：session, shell, exec, x11, auth-agent@openssh.com,
	 *            direct-tcpip, forwarded-tcpip, sftp, subsystem<br>
	 *            各对应的Channel类型为：ChannelSession, ChannelShell, ChannelExec,
	 *            ChannelX11, ChannelAgentForwarding, ChannelDirectTCPIP,
	 *            ChannelForwardedTCPIP, ChannelSftp, ChannelSubsystem <br>
	 * @param timeout
	 *            超时时间（毫秒）
	 * @return 新创建的通道
	 * @throws Exception
	 */
	public Channel openChannel(Session session, String type, int timeout) throws Exception {
		Channel channel = session.openChannel(type);
		channel.connect(timeout);
		return channel;
	}

	/**
	 * 打开Exec通道.<br>
	 * 返回的通道为尚未连接状态，需要另外再设置操作命令并且打开连接, 注意必须先设置好操作命令channel.setCommand(String
	 * command)，然后再打开连接channel.connect(5000).<br>
	 * 打开的通道必须关闭，否则该链接会一直保持.必须使用try...finally...结构. <code>
	 * <pre>
	 * // 创建Exec通道(超时5秒)
	 * ChannelExec channel = openChannelExec(session);
	 * try {
	 *  // 操作Exec.
	 *  channel.setCommand("pwd");
	 *  channel.connect(5000);
	 * }
	 * catch (Exception ex) {
	 * 	// 处理异常
	 * 	throw ex;
	 * }
	 * finally {
	 * 	// 关闭Exec通道
	 * 	channel.disconnect();
	 * }
	 * </pre>
	 * </code>
	 * 
	 * @param session
	 *            SSH Session，可由openSession方法获取
	 * @return 新打开Exec通道
	 * @throws Exception
	 */
	public ChannelExec openChannelExec(Session session) throws Exception {
		ChannelExec channel = (ChannelExec) session.openChannel("exec");
		return channel;
	}

	/**
	 * 打开Exec通道，默认超时5秒.<br>
	 * 返回的通道为已连接状态，无需再执行channel.connect(timeout)方法.<br>
	 * 打开的通道必须关闭，否则该链接会一直保持.必须使用try...finally...结构. <code>
	 * <pre>
	 * // 创建Exec通道(默认超时5秒)
	 * ChannelExec channel = openChannelExec(session, command);
	 * try {
	 * 	// 操作Exec
	 * }
	 * catch (Exception ex) {
	 * 	// 处理异常
	 * 	throw ex;
	 * }
	 * finally {
	 * 	// 关闭Exec通道
	 * 	channel.disconnect();
	 * }
	 * </pre>
	 * </code>
	 * 
	 * @param session
	 *            SSH Session，可由openSession方法获取
	 * @param command
	 *            执行的命令
	 * @return 新打开Exec通道
	 * @throws Exception
	 */
	public ChannelExec openChannelExec(Session session, String command) throws Exception {
		return openChannelExec(session, command, 5000);
	}

	/**
	 * 打开Exec通道.<br>
	 * 返回的通道为已连接状态，无需再执行channel.connect(timeout)方法.<br>
	 * 打开的通道必须关闭，否则该链接会一直保持.必须使用try...finally...结构. <code>
	 * <pre>
	 * // 创建Exec通道(超时5秒)
	 * ChannelExec channel = openChannelExec(session, command, 5000);
	 * try {
	 * 	// 操作Exec
	 * }
	 * catch (Exception ex) {
	 * 	// 处理异常
	 * 	throw ex;
	 * }
	 * finally {
	 * 	// 关闭Exec通道
	 * 	channel.disconnect();
	 * }
	 * </pre>
	 * </code>
	 * 
	 * @param session
	 *            SSH Session，可由openSession方法获取
	 * @param command
	 *            执行的命令
	 * @param timeout
	 *            超时时间（毫秒）
	 * @return 新打开Exec通道
	 * @throws Exception
	 */
	public ChannelExec openChannelExec(Session session, String command, int timeout) throws Exception {
		ChannelExec channel = (ChannelExec) session.openChannel("exec");
		channel.setCommand(command);
		channel.connect(timeout);
		return channel;
	}

	/**
	 * 打开FTP通道，默认超时5秒.<br>
	 * 打开的通道必须关闭，否则该链接会一直保持.必须使用try...finally...结构. <code>
	 * <pre>
	 * // 创建SFTP通道(超时5秒)
	 * ChannelSftp channel = config.openChannelSftp(session);
	 * try {
	 * 	// 操作SFTP
	 * 	channel.cd(&quot;/&quot;);
	 * }
	 * catch (Exception ex) {
	 * 	// 处理异常
	 * 	throw ex;
	 * }
	 * finally {
	 * 	// 关闭SFTP通道 
	 * 	channel.disconnect();
	 * }
	 * </pre>
	 * </code>
	 * 
	 * @param session
	 *            SSH Session，可由openSession方法获取
	 * @return 新打开FTP通道
	 * @throws Exception
	 */
	public ChannelSftp openChannelSftp(Session session) throws Exception {
		return openChannelSftp(session, 5000);
	}

	/**
	 * 打开FTP通道.<br>
	 * 打开的通道必须关闭，否则该链接会一直保持.必须使用try...finally...结构. <code>
	 * <pre>
	 * // 创建SFTP通道(超时5秒)
	 * ChannelSftp channel = config.openChannelSftp(session, 5000);
	 * try {
	 * 	// 操作SFTP
	 * 	channel.cd(&quot;/&quot;);
	 * }
	 * catch (Exception ex) {
	 * 	// 处理异常
	 * 	throw ex;
	 * }
	 * finally {
	 * 	// 关闭SFTP通道
	 * 	channel.disconnect();
	 * }
	 * </pre>
	 * </code>
	 * 
	 * @param session
	 *            SSH Session，可由openSession方法获取
	 * @param timeout
	 *            超时时间（毫秒）
	 * @return 新打开FTP通道
	 * @throws Exception
	 */
	public ChannelSftp openChannelSftp(Session session, int timeout) throws Exception {
		ChannelSftp channel = (ChannelSftp) session.openChannel("sftp");
		channel.connect(timeout);
		channel.setFilenameEncoding(encoding);
		return channel;
	}

	/**
	 * 打开Sesesion.默认的超时时间为5秒. <br>
	 * 打开的Session必须关闭，否则该链接会一直保持.必须使用try...finally...结构.
	 * 
	 * <pre>
	 * //打开Session
	 * Session session = config.openSession();
	 * try {
	 * 	//操作Session
	 * }
	 * catch (Exception ex) {
	 * 	//异常处理
	 * 	throw ex;
	 * }
	 * finally {
	 * 	//关闭session
	 * 	session.disconnect();
	 * }
	 * </pre>
	 * 
	 * @return 新建Session
	 * @throws Exception
	 */
	public Session openSession() throws Exception {
		return openSession(5000);
	}

	/**
	 * 打开Sesesion.<br>
	 * 打开的Session必须关闭，必须使用try...finally...结构.
	 * 
	 * <pre>
	 * //打开Session(超时5秒)
	 * Session session = config.openSession(5000);
	 * try {
	 * 	//操作Session
	 * }
	 * catch (Exception ex) {
	 * 	//异常处理
	 * 	throw ex;
	 * }
	 * finally {
	 * 	//关闭session
	 * 	session.disconnect();
	 * }
	 * </pre>
	 * 
	 * @param timeout
	 *            超时时间（毫秒）
	 * @return 新建的Session
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
	 * 设置 终端字符集
	 * 
	 * @param encoding
	 *            终端字符集
	 */
	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	/**
	 * 设置 服务器地址
	 * 
	 * @param host
	 *            服务器地址
	 */
	public void setHost(String host) {
		this.host = host;
	}

	/**
	 * 设置 密钥文件名
	 * 
	 * @param identity
	 *            密钥文件名
	 */
	public void setIdentity(String identity) {
		this.identity = identity;
	}

	/**
	 * 设置 密钥文件密码
	 * 
	 * @param passphrase
	 *            密钥文件密码
	 */
	public void setPassphrase(String passphrase) {
		this.passphrase = passphrase;
	}

	/**
	 * 设置 登录密码
	 * 
	 * @param password
	 *            登录密码
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * 设置 服务器端口
	 * 
	 * @param port
	 *            服务器端口
	 */
	public void setPort(int port) {
		this.port = port;
	}

	/**
	 * 设置 登录帐号名
	 * 
	 * @param username
	 *            登录帐号名
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
