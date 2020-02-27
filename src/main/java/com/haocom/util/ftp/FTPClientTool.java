package com.haocom.util.ftp;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

/**
 * FTP��. <br>
 * ������ʵ��FTP�����ķ���.<br>
 * ��1.0�汾��ʼʹ��Apache commons net 2.0 API��<br>
 * <br>
 * <br>
 * ע�⣺
 * <ul>
 * <li>��������ṩCD���� ��<br/>
 * ��ֹ����ʹ�����·��ʱ�Ļ��ң����Ա�������з����е�·���������ǴӸ���ʼ�ľ���·����</li>
 * <li>��������ṩ�ϴ�������Ŀ¼�ķ�����<br/>
 * FTP����ʱ��ϳ�������࣬����Ϊ�˷�ֹ��ִ�г���ʱ�޷���ȷ�Ĵ����쳣�����Բ��ṩ�ϴ�������Ŀ¼�ķ�����<br/>
 * �ϴ������ļ�ǰӦ��ʹ��List�ȷ�������Ҫ�������ļ��嵥��¼�������������־��Ȼ����Ե����ļ�������</li>
 * <li>��������ṩ�ݹ�ɾ��Ŀ¼�ķ�����<br/>
 * Ϊ�˰�ȫԭ�򣬷�ֹ��ɾ������Ŀ¼��</li>
 * </ul>
 * <a href="doc-files/ftp���߽���.rar">FTP���ʹ��ʾ������[ftp���߽���.rar]</a>
 * 
 * <pre>
 * 
 * public class Test {
 * 
 * 	public static void main(String[] args) throws Exception {
 * 		if (1 == 1) {
 * 			// ����FTP�ͻ��˶���.
 * 			FTPClientTool ftp = new FTPClientTool();
 * 			// ���ӷ�����.
 * 			ftp.connect(&quot;192.168.10.85&quot;, 21, &quot;root&quot;, &quot;123456&quot;);
 * 			// ����Ŀ¼���༶��.
 * 			ftp.mkdir(&quot;/ftp/test//a/1/2/3/4/5/6/7/8/../9/&quot;);
 * 			// �ϴ��ļ�.
 * 			if (1 == 1) {
 * 				List&lt;File&gt; files = new ArrayList&lt;File&gt;();
 * 				files.addAll(FileFinder.findFiles(&quot;src&quot;, &quot;*&quot;, 0));
 * 				for (File file : files) {
 * 					// �ϴ��ļ���ָ��Ŀ¼.
 * 					ftp.put(file.getPath(), &quot;/ftp/test/&quot; + file.getParent());
 * 					// �ϴ��ļ���ָ��Ŀ¼�������޸�����.
 * 					ftp.put(file.getPath(), &quot;/ftp/test/&quot;, file.getPath() + &quot;.abc&quot;);
 * 				}
 * 			}
 * 			// �ϴ�ָ������.
 * 			ftp.put(&quot;�ĤŤ�&quot;.getBytes(), &quot;/ftp/test/&quot;, &quot;1.txt&quot;);
 * 			ftp.put(&quot;�ĤŤ�&quot;.getBytes(), &quot;/ftp/test/&quot;, &quot;2.txt&quot;);
 * 			// ��ȡָ��Ŀ¼�µ�Ŀ¼�嵥.
 * 			{
 * 				System.out.println(&quot;=============&quot;);
 * 				List&lt;String&gt; dirs = ftp.getDirList(&quot;/ftp/test&quot;);
 * 				for (String dir : dirs) {
 * 					System.out.println(dir);
 * 				}
 * 			}
 * 			// ��ȡָ��Ŀ¼�µ��ļ��嵥.
 * 			{
 * 				System.out.println(&quot;=============&quot;);
 * 				List&lt;String&gt; files = ftp.getFileList(&quot;/ftp/test&quot;);
 * 				for (String file : files)
 * 					System.out.println(file);
 * 			}
 * 			// ����ָ���ļ�.
 * 			{
 * 				System.out.println(&quot;=============&quot;);
 * 				String ftpDir = &quot;/ftp/test/&quot;;
 * 				String localDir = &quot;./temp/ftp/001&quot;;
 * 				List&lt;String&gt; files = ftp.getFileList(ftpDir);
 * 				for (String file : files)
 * 					ftp.get(ftpDir + &quot;/&quot; + file, localDir);
 * 			}
 * 			// ɾ��Ŀ¼���ļ�.
 * 			{
 * 				System.out.println(&quot;=============&quot;);
 * 				//
 * 				ftp.put(&quot;�ĤŤ�&quot;.getBytes(), &quot;/ftp/test/b&quot;, &quot;1.txt&quot;);
 * 				ftp.put(&quot;�ĤŤ�&quot;.getBytes(), &quot;/ftp/test/b&quot;, &quot;2.txt&quot;);
 * 				ftp.put(&quot;�ĤŤ�&quot;.getBytes(), &quot;/ftp/test/c&quot;, &quot;1.txt&quot;);
 * 				ftp.put(&quot;�ĤŤ�&quot;.getBytes(), &quot;/ftp/test/c&quot;, &quot;2.txt&quot;);
 * 				ftp.put(&quot;�ĤŤ�&quot;.getBytes(), &quot;/ftp/test/c/k&quot;, &quot;3.txt&quot;);
 * 				//
 * 				List&lt;String&gt; files;
 * 				files = ftp.getFileList(&quot;/ftp/test&quot;);
 * 				for (String file : files)
 * 					System.out.println(file);
 * 				// ɾ��Ŀ¼.
 * 				ftp.delDirFiles(&quot;/ftp/test/b&quot;);
 * 				// ɾ���ļ�.
 * 				ftp.deleteFile(&quot;/ftp/test/c/k/3.txt&quot;);
 * 				// ɾ���ļ�.
 * 				ftp.deleteFile(&quot;/ftp/test/c/1.txt&quot;);
 * 				//
 * 				files = ftp.getFileList(&quot;/ftp/test&quot;);
 * 				for (String file : files)
 * 					System.out.println(file);
 * 			}
 * 			//
 * 			ftp.close();
 * 		}
 * 	}
 * }
 * </pre>
 * 
 * <br>
 * <p>
 * Copyright: Copyright (c) 2009-1-8
 * <p>
 * Company: 
 * <p>
 * Author: ChenWei
 * <p>
 * Version: 1.0
 */
public class FTPClientTool {

	/** FTP���������� */
	private static final int FTP_ERROR_CODE = 550;

	/** IO��д�����С:64k */
	private final int BUFFER_SIZE = 1024 * 24;

	/** FTP�ͻ��� */
	private FTPClient ftpClient;

	/** ��ʱʱ�� */
	private int timeOut = 10 * 1000;

	/**
	 * ������
	 */
	public FTPClientTool() {
		ftpClient = new FTPClient();
		ftpClient.setConnectTimeout(timeOut);
		ftpClient.setDefaultTimeout(timeOut);
	}

	/**
	 * cd��ָ��ftpĿ¼�������ȵ�¼
	 * 
	 * @param remotePath
	 *            ftpĿ¼ ���磺/data/ftp
	 * @throws Exception
	 *             �쳣
	 */
	private boolean cd(String remotePath) throws Exception {
		remotePath = remotePath.replaceAll("\\\\", "/");
		if (!ftpClient.changeWorkingDirectory(remotePath)) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * �ر�ftpʵ������
	 * 
	 * @throws Exception
	 */
	public void close() throws Exception {
		if (ftpClient.isConnected()) {
			try {
				ftpClient.logout();
			}
			catch (Exception e) {
				throw e;
			}
			finally {
				ftpClient.disconnect();
			}
		}
	}

	/**
	 * �����������Զ�����ʹ��PassiveMode��ʧ����תΪActiveMode
	 * 
	 * @param hostName
	 *            ftp hostname
	 * @param port
	 *            �˿�
	 * @param userName
	 *            �û���
	 * @param passWord
	 *            ����
	 * @throws Exception
	 *             ����ʧ���׳��쳣
	 */
	public void connect(String hostName, int port, String userName, String passWord) throws Exception {
		try {
			if (ftpClient.isConnected()) {
				close();
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		ftpClient.connect(hostName, port);
		int code = ftpClient.getReplyCode();
		String reply = ftpClient.getReplyString();
		if (!FTPReply.isPositiveCompletion(code)) {
			ftpClient.disconnect();
			throw new Exception(reply);
		}
		ftpClient.setSoTimeout(timeOut);
		ftpClient.setDataTimeout(timeOut);
		ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
		ftpClient.setBufferSize(BUFFER_SIZE);
		if (ftpClient.login(userName, passWord)) {
			// ����Ϊ����ģʽ
			ftpClient.enterLocalPassiveMode();
			// �����Ŀ¼
			try {
				ftpClient.listFiles();
			}
			catch (Exception e) {
				// ����Ϊ����ģʽ
				connectActiveMode(hostName, port, userName, passWord);
			}
			if (ftpClient.getReplyCode() == FTP_ERROR_CODE) {
				// ����Ϊ����ģʽ
				connectActiveMode(hostName, port, userName, passWord);
			}
			ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
		} else {
			throw new Exception("login fail:" + ftpClient.getReplyString());
		}
	}

	/**
	 * ʹ������ģʽ����FTP
	 * 
	 * @param hostname
	 *            ip
	 * @param port
	 *            �˿ں�
	 * @param userName
	 *            �û���
	 * @param passWord
	 *            ����
	 * @throws Exception
	 *             �����쳣
	 */
	private void connectActiveMode(String hostname, int port, String userName, String passWord) throws Exception {
		// �ر��ϴ�����
		try {
			ftpClient.logout();
		}
		catch (Exception e) {
		}
		finally {
			try {
				ftpClient.disconnect();
			}
			catch (IOException e) {

			}
		}
		try {
			ftpClient.connect(hostname, port);
			int code = ftpClient.getReplyCode();
			String reply = ftpClient.getReplyString();
			if (!FTPReply.isPositiveCompletion(code)) {
				ftpClient.disconnect();
				throw new Exception(reply);
			}
			ftpClient.setSoTimeout(timeOut);
			ftpClient.setDataTimeout(timeOut);
			ftpClient.setBufferSize(BUFFER_SIZE);
			ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
			if (ftpClient.login(userName, passWord)) {
				ftpClient.enterLocalActiveMode();
			}
			ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
		}
		catch (Exception e) {
			throw e;
		}
	}

	/**
	 * ɾ��Զ��Ŀ¼��ֻɾ����Ŀ¼���ļ��͸�Ŀ¼.���ڰ�ȫԭ�򣬱���������ݹ�ɾ��ָ��Ŀ¼�µ���Ŀ¼��
	 * 
	 * @param dir
	 *            Ŀ¼�� ����:/data/ftp
	 * @throws Exception
	 *             ɾ��ʧ��
	 */
	public void delDirFiles(String dir) throws Exception {
		dir = dir.replaceAll("\\\\", "/");
		if (cd(dir)) {
			FTPFile[] ftpfiles = ftpClient.listFiles(dir);
			for (FTPFile file : ftpfiles) {
				if (!file.getName().startsWith(".") && file.isFile()) {
					// ɾ��Ŀ¼�е��ļ�
					if (!ftpClient.deleteFile(file.getName())) {
						throw new Exception("delete file fail :" + file.getName() + " info:" + ftpClient.getReplyString());
					}
				}
			}
			// ɾ��Ŀ¼
			ftpClient.removeDirectory(dir);
		} else {
			throw new Exception("entry into path fail :" + dir + " info:" + ftpClient.getReplyString());
		}
	}

	/**
	 * ɾ���ļ�
	 * 
	 * @param pathname
	 *            �ļ�·�� ����: /data/ftp/1.smil
	 * @return ָ���ļ��Ƿ�ɾ��
	 * @throws Exception
	 *             ɾ���쳣
	 */
	public boolean deleteFile(String pathname) throws Exception {
		pathname = pathname.replaceAll("\\\\", "/");
		boolean succFlag = ftpClient.deleteFile(pathname);
		if (!succFlag) {
			succFlag = ftpClient.removeDirectory(pathname);
		}
		return succFlag;
	}

	/**
	 * �����ļ�
	 * 
	 * @param remoteFilePath
	 *            Զ����Ҫ��ȡ���ļ���ȫ·�� ����:/data/ftp/1.txt
	 * @return �����ļ����ֽ�����
	 * @throws Exception
	 *             ����ʧ�����׳��쳣
	 */
	public byte[] get(String remoteFilePath) throws Exception {
		byte[] fileByte = null;
		if (remoteFilePath == null || remoteFilePath.trim().length() == 0) {
			throw new Exception("the param remoteFilePath can not be null or empty.");
		}
		remoteFilePath = remoteFilePath.replaceAll("\\\\", "/");
		File file = new File(remoteFilePath);
		// ��ȡԶ������·��
		String remotePath = file.getParent();
		// ��ȡԶ�������ļ���
		String remoteFileName = file.getName();
		// ����Զ������·��
		if (!cd(remotePath)) {
			throw new Exception("entry into remotePath fail:" + ftpClient.getReplyString());
		}
		//
		ByteArrayOutputStream fos = new ByteArrayOutputStream(1024 * 10);
		try {
			if (ftpClient.retrieveFile(remoteFileName, fos)) {
				fileByte = fos.toByteArray();
			} else {
				throw new Exception("download file byte[] fail:" + ftpClient.getReplyString());
			}
		}
		catch (Exception e) {
			throw e;
		}
		finally {
			fos.close();
		}
		//
		return fileByte;
	}

	/**
	 * �����ļ� �����ȵ�¼
	 * 
	 * @param remoteFilePath
	 *            Զ����Ҫ��ȡ���ļ���ȫ·�� ����:/data/ftp/0.smil
	 * @param localDir
	 *            ����·��������ص�·�� ���磺/data/mms/
	 * @throws Exception
	 *             ����ʧ�����׳��쳣
	 */
	public void get(String remoteFilePath, String localDir) throws Exception {
		get(remoteFilePath, localDir, null);
	}

	/**
	 * �����ļ� �����ȵ�¼
	 * 
	 * @param remoteFilePath
	 *            Զ����Ҫ��ȡ���ļ���ȫ·�� ����:/data/ftp/0.smil
	 * @param localDir
	 *            ����·��������ص�·�� ����:/data/mms
	 * @param localName
	 *            �����������ļ����� ����:1.smil
	 * @throws Exception
	 *             ����ʧ�����׳��쳣
	 */
	public void get(String remoteFilePath, String localDir, String localName) throws Exception {

		if (remoteFilePath == null || remoteFilePath.trim().length() == 0 || localDir == null) {
			throw new Exception("the param remoteFilePath can not be null or empty.");
		}
		remoteFilePath = remoteFilePath.replaceAll("\\\\", "/");
		File file = new File(remoteFilePath);
		// ��ȡԶ������·��
		String remotePath = file.getParent();
		// ��ȡԶ�������ļ���
		String remoteFileName = file.getName();
		if (localName == null || localName.trim().length() == 0) {
			localName = remoteFileName;
		}
		// ����Զ������·��
		if (!cd(remotePath)) {
			throw new Exception("entry into remotePath fail:" + ftpClient.getReplyString());
		}
		if (localDir == null || localDir.trim().length() == 0) {
			localDir = ".";
		}
		// �������Ŀ¼�������򴴽�
		if (!localDir.equals(".")) {
			File temp = new File(localDir);
			if (!temp.isDirectory()) {
				temp.mkdirs();
			}
		}
		if (!localDir.endsWith("/")) {
			localDir = localDir + "/";
		}

		FileOutputStream fos = new FileOutputStream(localDir + localName);
		try {
			if (!ftpClient.retrieveFile(remoteFileName, fos)) {
				throw new Exception("download file fail:" + ftpClient.getReplyString());
			}
		}
		catch (Exception e) {
			throw e;
		}
		finally {
			if (fos != null) {
				fos.close();
			}
		}
	}

	/**
	 * ���Զ��Ŀ¼�µ�Ŀ¼�б������ȵ�¼
	 * 
	 * @param remoteDir
	 *            Ŀ��Ŀ¼
	 * @return Ŀ¼���б�
	 * @throws Exception
	 *             �쳣
	 */
	public List<String> getDirList(String remoteDir) throws Exception {
		return getDirList(remoteDir, null);
	}

	/**
	 * ���Զ��Ŀ¼�µ�Ŀ¼�б������ȵ�¼
	 * 
	 * @param remoteDir
	 *            Ŀ��Ŀ¼
	 * @param dirNameRegex
	 *            Ŀ��Ŀ¼����������ʽ, ��Ϊ�գ����õ�ǰftpĿ¼������Ŀ¼�б� ����: \\d+.* ���������ֿ�ͷ��Ŀ¼��
	 * @return Ŀ¼���б�
	 * @throws Exception
	 *             �쳣
	 */
	public List<String> getDirList(String remoteDir, String dirNameRegex) throws Exception {
		List<String> nameList = new ArrayList<String>();
		List<FTPFile> dirList = getFTPDirList(remoteDir, dirNameRegex);
		for (int i = 0; i < dirList.size(); i++) {
			nameList.add(dirList.get(i).getName());
		}
		return nameList;
	}

	/**
	 * ��ȡ �ַ���
	 * 
	 * @return �ַ���
	 */
	public String getEncoding() {
		return ftpClient.getControlEncoding();
	}

	/**
	 * ���Զ��Ŀ¼�µ��ļ��б������ȵ�¼
	 * 
	 * @param remoteDir
	 *            Ŀ��Ŀ¼
	 * @return �ļ��б�
	 * @throws Exception
	 *             �쳣
	 */
	public List<String> getFileList(String remoteDir) throws Exception {
		return getFileList(remoteDir, null);
	}

	/**
	 * ���Զ��Ŀ¼�µ��ļ��б������ȵ�¼
	 * 
	 * @param remoteDir
	 *            Ŀ��Ŀ¼
	 * @param fileNameRegex
	 *            Ŀ���ļ�����������ʽ, ��Ϊ�գ����õ�ǰftpĿ¼�������ļ��б� ����: .+txt ������txt��β���ļ���
	 * @return �ļ��б�
	 * @throws Exception
	 *             �쳣
	 */
	public List<String> getFileList(String remoteDir, String fileNameRegex) throws Exception {
		List<String> al = new ArrayList<String>();
		List<FTPFile> fileList = getFTPFileList(remoteDir, fileNameRegex);
		for (int i = 0; i < fileList.size(); i++) {
			al.add(fileList.get(i).getName());
		}
		return al;
	}

	/**
	 * ��ȡԭFtpClient
	 * 
	 * @return ԭFtpClient
	 */
	public FTPClient getFtpClient() {
		return ftpClient;
	}

	/**
	 * ���Զ��Ŀ¼�µ�FTPĿ¼�б������ȵ�¼
	 * 
	 * @param remoteDir
	 *            Ŀ��Ŀ¼
	 * @return FTPĿ¼�б�
	 * @throws Exception
	 *             �쳣
	 */
	public List<FTPFile> getFTPDirList(String remoteDir) throws Exception {
		return getFTPDirList(remoteDir, null);
	}

	/**
	 * ���Զ��Ŀ¼�µ�FTPĿ¼�б������ȵ�¼
	 * 
	 * @param remoteDir
	 *            Ŀ��Ŀ¼
	 * @param dirNameRegex
	 *            Ŀ��Ŀ¼����������ʽ, ��Ϊ�գ����õ�ǰftpĿ¼������FTPĿ¼�б� ����: \\d+.* ���������ֿ�ͷ��Ŀ¼��
	 * @return FTPĿ¼�б�
	 * @throws Exception
	 *             �쳣
	 */
	public List<FTPFile> getFTPDirList(String remoteDir, String dirNameRegex) throws Exception {
		List<FTPFile> nameList = new ArrayList<FTPFile>();
		remoteDir = remoteDir.replaceAll("\\\\", "/");
		remoteDir = remoteDir.replaceAll("//", "/");
		if (!remoteDir.startsWith("/")) {
			remoteDir = "/" + remoteDir;
		}

		if (remoteDir != null && remoteDir.length() > 0) {
			if (cd(remoteDir)) {
				FTPFile[] fileList = ftpClient.listFiles();
				if (dirNameRegex != null && dirNameRegex.trim().length() != 0) {
					for (int i = 0; i < fileList.length; i++) {
						if (fileList[i].getName().equals(".") || fileList[i].getName().endsWith("..")) {
							continue;
						}
						if (fileList[i].isDirectory() && fileList[i].getName().matches(dirNameRegex)) {
							nameList.add(fileList[i]);
						}
					}
				} else {
					for (int i = 0; i < fileList.length; i++) {
						if (fileList[i].isDirectory()) {
							if (fileList[i].getName().equals(".") || fileList[i].getName().endsWith("..")) {
								continue;
							}
							nameList.add(fileList[i]);
						}
					}
				}
			}
		}
		return nameList;
	}

	/**
	 * ���Զ��Ŀ¼�µ�FTP�ļ��б������ȵ�¼
	 * 
	 * @param remoteDir
	 *            Ŀ��Ŀ¼
	 * @return FTP�ļ��б�
	 * @throws Exception
	 *             �쳣
	 */
	public List<FTPFile> getFTPFileList(String remoteDir) throws Exception {
		return getFTPFileList(remoteDir, null);
	}

	/**
	 * ���Զ��Ŀ¼�µ�FTP�ļ��б������ȵ�¼
	 * 
	 * @param remoteDir
	 *            Ŀ��Ŀ¼
	 * @param fileNameRegex
	 *            Ŀ���ļ�����������ʽ, ��Ϊ�գ����õ�ǰftpĿ¼������FTP�ļ��б� ����: .+txt ������txt��β���ļ���
	 * @return FTP�ļ��б�
	 * @throws Exception
	 *             �쳣
	 */
	public List<FTPFile> getFTPFileList(String remoteDir, String fileNameRegex) throws Exception {
		remoteDir = remoteDir.replaceAll("\\\\", "/");
		List<FTPFile> al = new ArrayList<FTPFile>();
		if (remoteDir != null && remoteDir.length() > 0) {
			if (cd(remoteDir)) {
				FTPFile[] fileList = ftpClient.listFiles();
				if (fileNameRegex != null && fileNameRegex.trim().length() != 0) {
					for (int i = 0; i < fileList.length; i++) {
						if (fileList[i].isFile() && fileList[i].getName().matches(fileNameRegex)) {
							al.add(fileList[i]);
						}
					}
				} else {
					for (int i = 0; i < fileList.length; i++) {
						if (fileList[i].isFile()) {
							al.add(fileList[i]);
						}
					}

				}
			}
		}
		return al;
	}

	/**
	 * ��ȡ��ʱʱ��
	 * 
	 * @return timeOut ��ʱʱ�䣬��λΪ����
	 */
	public final int getTimeOut() {
		return this.timeOut;
	}

	/**
	 * ����Զ��Ŀ¼(֧�ֶ༶Ŀ¼����),������򲻴���ֱ�ӽ���Ŀ¼
	 * 
	 * @param name
	 *            Ŀ¼·�� ���磺/data/ftp/mms/200812/0126
	 * @throws Exception
	 *             ����Ŀ¼ʧ�����׳��쳣
	 */
	public void mkdir(String name) throws Exception {

		if (name == null || name.trim().length() == 0) {
			throw new Exception("the param name can not be null or empty.");
		}
		name = name.replaceAll("\\\\", "/");
		name = name.replaceAll("//", "/");
		if (!name.startsWith("/")) {
			name = "/" + name;
		}
		if (!cd(name)) {
			String upDir = new File(name).getParent();
			if (upDir == null || upDir.equals("")) {
				return;
			}
			mkdir(upDir);
			ftpClient.makeDirectory(name);
		}
	}

	/**
	 * �ϴ��ļ���Զ�˷�����
	 * 
	 * @param inputDate
	 *            ���ϴ����ֽ�����
	 * @param remoteDirPath
	 *            Զ���ļ�·���������ļ������������ڻ��Զ�����Ŀ¼ ����:/data/ftp/1230/
	 * @param remoteFileName
	 *            �ϴ���Զ���ļ��� ����:do.txt
	 * @throws Exception
	 *             �ϴ�ʧ�����׳��쳣 �쳣
	 */
	public void put(byte[] inputDate, String remoteDirPath, String remoteFileName) throws Exception {
		if (inputDate == null || remoteFileName == null || remoteFileName.trim().length() == 0) {
			throw new Exception("params inputDate or remoteFileName  can not be null or empty.");
		}
		// ��������·�����ļ���
		File ftpFile = new File(remoteDirPath + "/" + remoteFileName);
		remoteFileName = ftpFile.getName();
		remoteDirPath = ftpFile.getParent();
		remoteDirPath = remoteDirPath.replaceAll("\\\\", "/");
		remoteDirPath = remoteDirPath.replaceAll("//", "/");

		if (!remoteDirPath.startsWith("/")) {
			remoteDirPath = "/" + remoteDirPath;
		}

		cd("/");
		mkdir(remoteDirPath);
		cd(remoteDirPath);

		ByteArrayInputStream fis = null;
		try {
			fis = new ByteArrayInputStream(inputDate);
			if (!ftpClient.storeFile(remoteFileName, fis)) {
				throw new Exception("upload byte[] file fail:" + ftpClient.getReplyString());
			}
		}
		catch (Exception e) {
			throw e;
		}
		finally {
			if (fis != null) {
				fis.close();
			}
		}
	}

	/**
	 * �ϴ��ļ���Զ�˷�����
	 * 
	 * @param localPathFile
	 *            �ı����ϴ��ļ�·�� ���磺/data/mms/1.txt
	 * @param remoteDirPath
	 *            Զ��Ŀ¼·�������������Զ�����Ŀ¼ ���磺/data/ftp/mms/200812/0126
	 * @throws Exception
	 *             �쳣
	 */
	public void put(String localPathFile, String remoteDirPath) throws Exception {
		put(localPathFile, remoteDirPath, null);
	}

	/**
	 * �ϴ��ļ���Զ�˷�����
	 * 
	 * @param localPathFile
	 *            �ı����ϴ��ļ�·�� ���磺/data/mms/1.txt
	 * @param remoteDirPath
	 *            Զ��Ŀ¼·���������ڻ��Զ�����Ŀ¼ ���磺/data/ftp/mms/200812/0126
	 * @param remoteFileName
	 *            �ϴ���Զ���ļ��� ���磺/data/mms/2.txt
	 * @throws Exception
	 *             �ϴ�ʧ�����׳��쳣 �쳣
	 */
	public void put(String localPathFile, String remoteDirPath, String remoteFileName) throws Exception {
		File localFile = new File(localPathFile);
		if (!localFile.isFile()) {
			throw new FileNotFoundException("�ļ�" + localFile.getAbsolutePath() + "�����ڻ���û�ж�ȡȨ��");
		}
		if (remoteFileName == null || remoteFileName.trim().length() == 0) {
			remoteFileName = localFile.getName();
		}

		// ��������·�����ļ���
		File ftpFile = new File(remoteDirPath + "/" + remoteFileName);
		remoteFileName = ftpFile.getName();
		remoteDirPath = ftpFile.getParent();
		remoteDirPath = remoteDirPath.replaceAll("\\\\", "/");
		remoteDirPath = remoteDirPath.replaceAll("//", "/");

		if (!remoteDirPath.startsWith("/")) {
			remoteDirPath = "/" + remoteDirPath;
		}

		cd("/");
		mkdir(remoteDirPath);
		cd(remoteDirPath);
		FileInputStream fis = new FileInputStream(localFile);
		try {
			if (!ftpClient.storeFile(remoteFileName, fis)) {
				throw new Exception("upload file fail:" + ftpClient.getReplyString());
			}
		}
		catch (Exception e) {
			throw e;
		}
		finally {
			if (fis != null) {
				fis.close();
			}
		}
	}

	/**
	 * �޸�ftpĿ¼�µ��ļ����������ȵ�¼
	 * 
	 * @param srcPathFile
	 *            ftp����Ҫ�޸ĵ��ļ�·�� ���磺/data/mms/1.txt
	 * @param destPathFile
	 *            ftp��Ŀ���ļ�·�� ���磺/data/mms2/3.txt
	 * @return �޸�ftp��ǰĿ¼�µ��ļ����Ƿ�ɹ�
	 * @throws Exception
	 *             �쳣
	 */
	public boolean rename(String srcPathFile, String destPathFile) throws Exception {
		srcPathFile = srcPathFile.replaceAll("\\\\", "/");
		destPathFile = destPathFile.replaceAll("\\\\", "/");
		return ftpClient.rename(srcPathFile, destPathFile);
	}

	/**
	 * ���� �ַ���
	 * 
	 * @param encoding
	 *            �ַ���
	 */
	public void setEncoding(String encoding) {
		ftpClient.setControlEncoding(encoding);
	}

	/**
	 * ���������뽻�����ݳ�ʱʱ�䡣Ĭ��ֵΪ10�롣
	 * 
	 * @param timeOut
	 *            ftp���������ӳ�ʱʱ�䣬��λΪ����
	 */
	public void setTimeOut(int timeOut) {
		this.timeOut = timeOut;
		ftpClient.setConnectTimeout(timeOut);
		ftpClient.setDefaultTimeout(timeOut);
	}
}
