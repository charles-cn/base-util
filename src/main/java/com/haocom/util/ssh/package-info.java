/**
 * 常用工具包之---ssh的登录工具,可以实现ssh的登录：通过配置服务器地址、帐号、密码等信息进行登陆，并且可以打开Session和SFTP、EXEC通道
 * ， 进行SFTP的传输或者是EXEC命令的执行.<br>
 * 注意：本组件的实现引用了外部jar包：<a
 * href="doc-files/jsch.jar">jsch.jar[点击下载]</a>。使用时请注意对jsch.jar的加载.
 * <p>
 * <ul>
 * <li>组件说明</li>
 * <p>
 * 常用工具包com.cplatform.util2.ssh封装了Jsch中复杂的登录方式。<br>
 * 将帐号、密码、地址、端口等登录用信息封装到一个类中：com.haocom.util.ssh.SessionConfig<br>
 * 该类支持帐号/密码登录方式，也支持密钥对登录方式。<br>
 * 通过SessionConfig可以创建Ssh Session和打开对应Session的各种操作通道。<br>
 * 
 * <pre>
 * 通道类型                          对应的Channel类
 *  session                       ChannelSession
 *  shell                         ChannelShell
 *  exec                          ChannelExec
 *  x11                           ChannelX11
 *  auth-agent@openssh.com        ChannelAgentForwarding
 *  direct-tcpip                  ChannelDirectTCPIP
 *  forwarded-tcpip               ChannelForwardedTCPIP
 *  sftp                          ChannelSftp
 *  subsystem                     ChannelSubsystem
 * </pre>
 * <p>
 * <li>登录SSH</li>
 * <p>
 * 1、SessionConfig类封装了登录Ssh所需的相关参数设置
 * 
 * <pre>
 *  方法                          Code                                  备注
 *  设置SSH服务器地址            SessionConfig.setHost(String)
 *  设置SSH服务端口              SessionConfig.setPort(int)
 *  设置SSH字符集                SessionConfig.setEncoding(String)     默认使用GB18030
 *  设置登录的用户名              SessionConfig.setUsername(String)
 *  设置登录密码                  SessionConfig.setPassword(String)
 *  设置密钥文件名                SessionConfig.setIdentity(String)
 *  设置密钥文件密码              SessionConfig.setPassphrase(String
 * </pre>
 * <p>
 * 2、登录方式如下:
 * <p>
 * 帐号密码登录方式: SessionConfig config = new SessionConfig(host, port, username,
 * password);<br>
 * 密钥对登录方式: SessionConfig config = new SessionConfig(host, port, username,
 * identity, passphrase);<br>
 * 混合方式:除了指定登录方式之外，还可以使用混合方式。 混合方式的情况下，优先使用密钥对方式登录，登录失败后再使用帐号密码登录。
 * <p>
 * 3、登陆范例：
 * <p>
 * 使用SessionConfig的openSession方法创建登录session。
 * Session的打开/关闭，必须使用Try...catch...finally...结构。如果不关，则该通道将永远开着。
 * 
 * <pre>
 * // config为SessionConfig实例.
 * //打开Session(超时5秒).
 * Session session = config.openSession(5000);
 * try {
 * 	//操作Session.
 * }
 * catch (Exception ex) {
 * 	//异常处理.
 * 	throw ex;
 * }
 * finally {
 * 	//关闭session.
 * 	session.disconnect();
 * }
 * </pre>
 * <p>
 * <li>操作SFTP</li>
 * <p>
 * 通过创建的SFTP通道（ChannelSftp）可以操作SFTP。
 * <p>
 * 1、常用的功能有：
 * 
 * <pre>
 * cd      		修改远地目录
 * lcd     		修改本地目录
 * put     		上传文件
 * get     		下载文件
 * reneme  		修改文件名（移动文件）
 * </pre>
 * 
 * 2、实现的命令有:
 * 
 * <pre>
 * cd path                       Change remote directory to 'path'
 * lcd path                      Change local directory to 'path'
 * chgrp grp path                Change group of file 'path' to 'grp'
 * chmod mode path               Change permissions of file 'path' to 'mode'
 * chown own path                Change owner of file 'path' to 'own'
 * df [path]                     Display statistics for current directory or
 *                               filesystem containing 'path'
 * help                          Display this help text
 * get remote-path [local-path]  Download file
 * lls [ls-options [path]]       Display local directory listing
 * ln oldpath newpath            Symlink remote file
 * lmkdir path                   Create local directory
 * lpwd                          Print local working directory
 * ls [path]                     Display remote directory listing
 * lumask umask                  Set local umask to 'umask'
 * mkdir path                    Create remote directory
 * progress                      Toggle display of progress meter
 * put local-path [remote-path]  Upload file
 * pwd                           Display remote working directory
 * exit                          Quit sftp
 * quit                          Quit sftp
 * rename oldpath newpath        Rename remote file
 * rmdir path                    Remove remote directory
 * rm path                       Delete remote file
 * symlink oldpath newpath       Symlink remote file
 * version                       Show SFTP version
 * 
 * </pre>
 * 
 * 3、创建、关闭SFTP通道
 * <p>
 * 使用SessionConfig创建SFTP通道，但是首先得打开一个Session。<br>
 * 通道的打开/关闭，必须使用Try...catch...finally...结构。如果不关，则该通道将永远开着。<br>
 * 
 * <pre>
 * // session用SessionConfig实例打开.
 * // 创建SFTP通道(超时5秒).
 * ChannelSftp channel = config.openChannelSftp(session, 5000);
 * try {
 * 	// 操作SFTP.
 * 	channel.cd(&quot;/&quot;);
 * }
 * catch (Exception ex) {
 * 	// 处理异常.
 * 	throw ex;
 * }
 * finally {
 * 	// 关闭SFTP通道.
 * 	channel.disconnect();
 * }
 * </pre>
 * 
 * 4、SFTP操作简单范例
 * 
 * <pre>
 * // 创建sessionConfig，设置服务器地址、端口、帐号、密码等信息.
 * SessionConfig sessionConfig = new SessionConfig(&quot;192.168.10.85&quot;, 22, &quot;root&quot;, &quot;123456&quot;);
 * sessionConfig.setEncoding(&quot;UTF-8&quot;);
 * 
 * // 打开Sesesion.默认的超时时间为5秒. &lt;br&gt;
 * // 打开的Session必须关闭，否则该链接会一直保持.必须使用try...finally...结构.
 * Session session = sessionConfig.openSession();
 * try {
 * 	// 操作Session.
 * 	// 创建SFTP通道(超时5秒).
 * 	ChannelSftp channel = sessionConfig.openChannelSftp(session, 5000);
 * 	try {
 * 		// 操作SFTP
 * 		channel.cd(&quot;/usr/local/program&quot;);
 * 		channel.lcd(&quot;e:\\ssh&quot;);
 * 
 * 		// 下载操作channel.get(线上文件名, 本地保存的文件名).
 * 		channel.get(&quot;online.txt&quot;, &quot;local.txt&quot;);
 * 
 * 		// 上传操作channel.get(本地文件名, 线上保存的文件名).
 * 		channel.put(&quot;local.txt&quot;, &quot;online.txt&quot;);
 * 	}
 * 	catch (Exception ex) {
 * 		throw ex;
 * 	}
 * 	finally {
 * 		// 关闭SFTP通道.
 * 		channel.disconnect();
 * 	}
 * }
 * catch (Exception ex) {
 * 	throw ex;
 * }
 * finally {
 * 	// 关闭session.
 * 	session.disconnect();
 * }
 * </pre>
 * 
 * <li>操作EXEC</li>
 * <p>
 * 通过创建的EXEC通道（ChannelExec）可以操作EXEC。
 * <p>
 * 1、常用的功能<br>
 * Linux中exec的常用功能基本都支持。<br>
 * 例如： pwd、ls、ps aux、cd、mkdir，等等。<br>
 * <p>
 * 2、创建、关闭EXEC通道
 * <p>
 * 使用SessionConfig创建EXEC通道，但是首先得打开一个Session。<br>
 * 通道的打开/关闭，必须使用Try...catch...finally...结构。如果不关，则该通道将永远开着。<br>
 * 
 * <pre>
 * //设置命令.
 * String command = &quot;pwd&quot;;
 * 
 * // session用SessionConfig实例打开.
 * // 创建EXEC通道(超时5秒).
 * ChannelExec channel = config.openChannelExec(session, command, 5000);
 * try {
 * 	// 操作EXEC，获取输出等.
 * }
 * catch (Exception ex) {
 * 	// 处理异常.
 * 	throw ex;
 * }
 * finally {
 * 	// 关闭EXEC通道 .
 * 	channel.disconnect();
 * }
 * </pre>
 * 
 * 3、EXEC操作简单范例
 * 
 * <pre>
 * // 创建sessionConfig，设置服务器地址、端口、帐号、密码等信息.
 * SessionConfig sessionConfig = new SessionConfig(&quot;192.168.10.85&quot;, 22, &quot;root&quot;, &quot;123456&quot;);
 * sessionConfig.setEncoding(&quot;UTF-8&quot;);
 * 
 * // 打开Sesesion.默认的超时时间为5秒. &lt;br&gt;
 * // 打开的Session必须关闭，否则该链接会一直保持.必须使用try...finally...结构.
 * Session session = sessionConfig.openSession();
 * try {
 * 	// 设置需要执行的命令.
 * 	String command = &quot;ls -l /usr/local/program/&quot;;
 * 	// 创建Exec通道(超时5秒).
 * 	ChannelExec channel = sessionConfig.openChannelExec(session, command, 5000);
 * 	try {
 * 		channel.setInputStream(null);
 * 		channel.setErrStream(System.err);
 * 		InputStream in = channel.getInputStream();
 * 		byte[] tmp = new byte[1024];
 * 		while (true) {
 * 			while (in.available() &gt; 0) {
 * 				int i = in.read(tmp, 0, 1024);
 * 				if (i &lt; 0) {
 * 					break;
 * 				}
 * 				System.out.print(new String(tmp, 0, i));
 * 			}
 * 			if (channel.isClosed()) {
 * 				System.out.println(&quot;exit-status: &quot; + channel.getExitStatus());
 * 				break;
 * 			}
 * 			Thread.sleep(1000);
 * 		}
 * 	}
 * 	catch (Exception ex) {
 * 		throw ex;
 * 	}
 * 	finally {
 * 		// 关闭SFTP通道.
 * 		channel.disconnect();
 * 	}
 * }
 * catch (Exception ex) {
 * 	throw ex;
 * }
 * finally {
 * 	// 关闭session.
 * 	session.disconnect();
 * }
 * 
 * 执行上述代码范例，即使用命令ls -l /usr/local/program ，则输出内容打印如下：
 * total 2476
 * -rwxr-xr-x    1 root     root         3867 Jul  8 21:47 1.pcap
 * drwxr-xr-x    9 root     root         4096 Jul  9 21:34 Tomcat6
 * -rwxr--r--    1 root     root      2008736 Aug 14  2008 WebService开发范例.zip
 * drwxr-xr-x    4 root     root         4096 Jan 24 10:25 asg
 * -rw-r--r--    1 root     root          145 Feb  6 09:49 content_20090205.txt.gz
 * drwxr-xr-x    3 root     root         4096 May 15  2008 cproduct-000
 * drwxr-xr-x    2 root     root         4096 Apr  7 18:00 jtdx
 * drwxr-xr-x    6 root     root         4096 Jul  7 16:02 sftp
 * drwxr-xr-x    9 root     root         4096 Feb 20 18:10 t123
 * -rw-r--r--    1 root     root       470960 Sep  5  2008 test.jar
 * drwxr-xr-x    5 root     root         4096 Feb 16 16:31 tt
 * drwxr-xr-x    7 root     root         4096 Mar  5 16:35 zjyd
 * -rw-r--r--    1 root     root         3867 Jul  9 13:56 zzzzz.pcap
 * exit-status: 0
 * 
 * </pre>
 * 
 * </ul>
 */
package com.haocom.util.ssh;

