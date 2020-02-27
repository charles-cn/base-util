/**
 * ���ù��߰�֮---ssh�ĵ�¼����,����ʵ��ssh�ĵ�¼��ͨ�����÷�������ַ���ʺš��������Ϣ���е�½�����ҿ��Դ�Session��SFTP��EXECͨ��
 * �� ����SFTP�Ĵ��������EXEC�����ִ��.<br>
 * ע�⣺�������ʵ���������ⲿjar����<a
 * href="doc-files/jsch.jar">jsch.jar[�������]</a>��ʹ��ʱ��ע���jsch.jar�ļ���.
 * <p>
 * <ul>
 * <li>���˵��</li>
 * <p>
 * ���ù��߰�com.cplatform.util2.ssh��װ��Jsch�и��ӵĵ�¼��ʽ��<br>
 * ���ʺš����롢��ַ���˿ڵȵ�¼����Ϣ��װ��һ�����У�com.haocom.util.ssh.SessionConfig<br>
 * ����֧���ʺ�/�����¼��ʽ��Ҳ֧����Կ�Ե�¼��ʽ��<br>
 * ͨ��SessionConfig���Դ���Ssh Session�ʹ򿪶�ӦSession�ĸ��ֲ���ͨ����<br>
 * 
 * <pre>
 * ͨ������                          ��Ӧ��Channel��
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
 * <li>��¼SSH</li>
 * <p>
 * 1��SessionConfig���װ�˵�¼Ssh�������ز�������
 * 
 * <pre>
 *  ����                          Code                                  ��ע
 *  ����SSH��������ַ            SessionConfig.setHost(String)
 *  ����SSH����˿�              SessionConfig.setPort(int)
 *  ����SSH�ַ���                SessionConfig.setEncoding(String)     Ĭ��ʹ��GB18030
 *  ���õ�¼���û���              SessionConfig.setUsername(String)
 *  ���õ�¼����                  SessionConfig.setPassword(String)
 *  ������Կ�ļ���                SessionConfig.setIdentity(String)
 *  ������Կ�ļ�����              SessionConfig.setPassphrase(String
 * </pre>
 * <p>
 * 2����¼��ʽ����:
 * <p>
 * �ʺ������¼��ʽ: SessionConfig config = new SessionConfig(host, port, username,
 * password);<br>
 * ��Կ�Ե�¼��ʽ: SessionConfig config = new SessionConfig(host, port, username,
 * identity, passphrase);<br>
 * ��Ϸ�ʽ:����ָ����¼��ʽ֮�⣬������ʹ�û�Ϸ�ʽ�� ��Ϸ�ʽ������£�����ʹ����Կ�Է�ʽ��¼����¼ʧ�ܺ���ʹ���ʺ������¼��
 * <p>
 * 3����½������
 * <p>
 * ʹ��SessionConfig��openSession����������¼session��
 * Session�Ĵ�/�رգ�����ʹ��Try...catch...finally...�ṹ��������أ����ͨ������Զ���š�
 * 
 * <pre>
 * // configΪSessionConfigʵ��.
 * //��Session(��ʱ5��).
 * Session session = config.openSession(5000);
 * try {
 * 	//����Session.
 * }
 * catch (Exception ex) {
 * 	//�쳣����.
 * 	throw ex;
 * }
 * finally {
 * 	//�ر�session.
 * 	session.disconnect();
 * }
 * </pre>
 * <p>
 * <li>����SFTP</li>
 * <p>
 * ͨ��������SFTPͨ����ChannelSftp�����Բ���SFTP��
 * <p>
 * 1�����õĹ����У�
 * 
 * <pre>
 * cd      		�޸�Զ��Ŀ¼
 * lcd     		�޸ı���Ŀ¼
 * put     		�ϴ��ļ�
 * get     		�����ļ�
 * reneme  		�޸��ļ������ƶ��ļ���
 * </pre>
 * 
 * 2��ʵ�ֵ�������:
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
 * 3���������ر�SFTPͨ��
 * <p>
 * ʹ��SessionConfig����SFTPͨ�����������ȵô�һ��Session��<br>
 * ͨ���Ĵ�/�رգ�����ʹ��Try...catch...finally...�ṹ��������أ����ͨ������Զ���š�<br>
 * 
 * <pre>
 * // session��SessionConfigʵ����.
 * // ����SFTPͨ��(��ʱ5��).
 * ChannelSftp channel = config.openChannelSftp(session, 5000);
 * try {
 * 	// ����SFTP.
 * 	channel.cd(&quot;/&quot;);
 * }
 * catch (Exception ex) {
 * 	// �����쳣.
 * 	throw ex;
 * }
 * finally {
 * 	// �ر�SFTPͨ��.
 * 	channel.disconnect();
 * }
 * </pre>
 * 
 * 4��SFTP�����򵥷���
 * 
 * <pre>
 * // ����sessionConfig�����÷�������ַ���˿ڡ��ʺš��������Ϣ.
 * SessionConfig sessionConfig = new SessionConfig(&quot;192.168.10.85&quot;, 22, &quot;root&quot;, &quot;123456&quot;);
 * sessionConfig.setEncoding(&quot;UTF-8&quot;);
 * 
 * // ��Sesesion.Ĭ�ϵĳ�ʱʱ��Ϊ5��. &lt;br&gt;
 * // �򿪵�Session����رգ���������ӻ�һֱ����.����ʹ��try...finally...�ṹ.
 * Session session = sessionConfig.openSession();
 * try {
 * 	// ����Session.
 * 	// ����SFTPͨ��(��ʱ5��).
 * 	ChannelSftp channel = sessionConfig.openChannelSftp(session, 5000);
 * 	try {
 * 		// ����SFTP
 * 		channel.cd(&quot;/usr/local/program&quot;);
 * 		channel.lcd(&quot;e:\\ssh&quot;);
 * 
 * 		// ���ز���channel.get(�����ļ���, ���ر�����ļ���).
 * 		channel.get(&quot;online.txt&quot;, &quot;local.txt&quot;);
 * 
 * 		// �ϴ�����channel.get(�����ļ���, ���ϱ�����ļ���).
 * 		channel.put(&quot;local.txt&quot;, &quot;online.txt&quot;);
 * 	}
 * 	catch (Exception ex) {
 * 		throw ex;
 * 	}
 * 	finally {
 * 		// �ر�SFTPͨ��.
 * 		channel.disconnect();
 * 	}
 * }
 * catch (Exception ex) {
 * 	throw ex;
 * }
 * finally {
 * 	// �ر�session.
 * 	session.disconnect();
 * }
 * </pre>
 * 
 * <li>����EXEC</li>
 * <p>
 * ͨ��������EXECͨ����ChannelExec�����Բ���EXEC��
 * <p>
 * 1�����õĹ���<br>
 * Linux��exec�ĳ��ù��ܻ�����֧�֡�<br>
 * ���磺 pwd��ls��ps aux��cd��mkdir���ȵȡ�<br>
 * <p>
 * 2���������ر�EXECͨ��
 * <p>
 * ʹ��SessionConfig����EXECͨ�����������ȵô�һ��Session��<br>
 * ͨ���Ĵ�/�رգ�����ʹ��Try...catch...finally...�ṹ��������أ����ͨ������Զ���š�<br>
 * 
 * <pre>
 * //��������.
 * String command = &quot;pwd&quot;;
 * 
 * // session��SessionConfigʵ����.
 * // ����EXECͨ��(��ʱ5��).
 * ChannelExec channel = config.openChannelExec(session, command, 5000);
 * try {
 * 	// ����EXEC����ȡ�����.
 * }
 * catch (Exception ex) {
 * 	// �����쳣.
 * 	throw ex;
 * }
 * finally {
 * 	// �ر�EXECͨ�� .
 * 	channel.disconnect();
 * }
 * </pre>
 * 
 * 3��EXEC�����򵥷���
 * 
 * <pre>
 * // ����sessionConfig�����÷�������ַ���˿ڡ��ʺš��������Ϣ.
 * SessionConfig sessionConfig = new SessionConfig(&quot;192.168.10.85&quot;, 22, &quot;root&quot;, &quot;123456&quot;);
 * sessionConfig.setEncoding(&quot;UTF-8&quot;);
 * 
 * // ��Sesesion.Ĭ�ϵĳ�ʱʱ��Ϊ5��. &lt;br&gt;
 * // �򿪵�Session����رգ���������ӻ�һֱ����.����ʹ��try...finally...�ṹ.
 * Session session = sessionConfig.openSession();
 * try {
 * 	// ������Ҫִ�е�����.
 * 	String command = &quot;ls -l /usr/local/program/&quot;;
 * 	// ����Execͨ��(��ʱ5��).
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
 * 		// �ر�SFTPͨ��.
 * 		channel.disconnect();
 * 	}
 * }
 * catch (Exception ex) {
 * 	throw ex;
 * }
 * finally {
 * 	// �ر�session.
 * 	session.disconnect();
 * }
 * 
 * ִ���������뷶������ʹ������ls -l /usr/local/program ����������ݴ�ӡ���£�
 * total 2476
 * -rwxr-xr-x    1 root     root         3867 Jul  8 21:47 1.pcap
 * drwxr-xr-x    9 root     root         4096 Jul  9 21:34 Tomcat6
 * -rwxr--r--    1 root     root      2008736 Aug 14  2008 WebService��������.zip
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

