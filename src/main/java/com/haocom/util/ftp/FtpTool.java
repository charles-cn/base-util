package com.haocom.util.ftp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

/**
 * FTP��. <br>
 * ������ʵ��FTP�����ķ���.<br>
 * ��1.1�汾��ʼʹ��Apache commons net 2.0 API<br>
 * <br>
 * History 2008-12-31 ����11:30:01 gaowei<br>
 * ����ģʽ����(enterActiveMode��enterPassiveMode)<br>
 * �����Զ��ϴ��ļ�������(putToFile)<br>
 * ��ʼʹ��ApacheCommonsNet2.0�汾API <br>
 * <p>
 * Copyright: Copyright (c) Sep 16, 2008
 * <p>
 * Company: 
 * <p>
 * 
 * @author gaowei
 * @version 1.1
 *@deprecated Ӧ��ʹ��{@link com.haocom.util.ftp.FTPClientTool} ʵ�ֹ���
 */
@Deprecated
public class FtpTool {

	/** ��ʱʱ�� */
	private static final int TIMEOUT = 10 * 1000;

	/** FTP�ͻ��� */
	private FTPClient ftpClient;

	/** ����·�� */
	private String localDir;

	/**
	 * ������
	 */
	public FtpTool() {
		ftpClient = new FTPClient();
		ftpClient.setConnectTimeout(TIMEOUT);
		ftpClient.setDefaultTimeout(TIMEOUT);
	}

	/**
	 * cd��ָ��ftpĿ¼�������ȵ�¼
	 * 
	 * @param remotePath
	 *            ftpĿ¼
	 * @return cd��ָ��ftpĿ¼�Ƿ�ɹ�
	 * @throws Exception
	 *             �쳣
	 */
	public boolean cd(String remotePath) throws Exception {
		return ftpClient.changeWorkingDirectory(remotePath);
	}

	/**
	 * �����ϼ�ftpĿ¼�������ȵ�¼
	 * 
	 * @return �����ϼ�ftpĿ¼�Ƿ�ɹ�
	 * @throws Exception
	 *             �쳣
	 */
	public boolean cdUp() throws Exception {
		return ftpClient.changeToParentDirectory();
	}

	/**
	 * ɾ���ļ�
	 * 
	 * @param pathname
	 *            �ļ�·��
	 * @return �Ƿ�ɾ���ɹ�
	 * @throws Exception
	 *             �쳣
	 */
	public boolean deleteFile(String pathname) throws Exception {
		if (!ftpClient.deleteFile(pathname)) {
			return ftpClient.removeDirectory(pathname);
		} else {
			return true;
		}
	}

	/**
	 * ��������ģʽ
	 */
	public void enterActiveMode() {
		ftpClient.enterLocalActiveMode();
	}

	/**
	 * ���뱻��ģʽ
	 */
	public void enterPassiveMode() {
		ftpClient.enterLocalPassiveMode();
	}

	/**
	 * �˳�FTP
	 */
	public void exit() throws Exception {
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
	 * �����ļ� �����ȵ�¼
	 * 
	 * @param localDir
	 *            ����·������Ϊ����ΪԤ��·������Ԥ��·��Ϊ�գ���Ϊ����Ŀ¼
	 * @param remoteFileName
	 *            ��Ҫ��ȡ�� �ļ���
	 * @return �Ƿ����سɹ�
	 * @throws Exception
	 *             �쳣
	 */
	public boolean get(String localDir, String remoteFileName) throws Exception {
		if (localDir == null || localDir.trim().length() == 0) {
			localDir = this.localDir;
		}
		if (localDir == null || localDir.trim().length() == 0) {
			localDir = ".";
		}
		if (!localDir.equals(".")) {
			File temp = new File(localDir);
			if (!temp.isDirectory()) {
				temp.mkdirs();
			}
		}
		if (!localDir.endsWith("/")) {
			localDir = localDir + "/";
		}

		FileOutputStream fos = new FileOutputStream(localDir + remoteFileName);
		try {
			return ftpClient.retrieveFile(remoteFileName, fos);
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
	 * ֱ�������ļ� �����ȵ�¼
	 * 
	 * @param localFilePath
	 *            �����ļ�·��
	 * @param remoteFilePath
	 *            Զ���ļ�·��
	 * @return �Ƿ����سɹ�
	 * @throws Exception
	 *             �쳣
	 */
	public boolean getAbsoluteFile(String localFilePath, String remoteFilePath) throws Exception {

		File localFile = new File(localFilePath).getParentFile();
		if (localFile != null && !localFile.exists()) {
			localFile.mkdirs();
		}

		FileOutputStream fos = new FileOutputStream(localFilePath);
		try {
			return ftpClient.retrieveFile(remoteFilePath, fos);
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
	 *            Ŀ��Ŀ¼����Ϊ�գ���Ϊ��ǰftpĿ¼
	 * @param dirNameRegex
	 *            Ŀ��Ŀ¼����������ʽ, ��Ϊ�գ����õ�ǰftpĿ¼������Ŀ¼�б�
	 * @return Ŀ¼���б�
	 * @throws Exception
	 *             �쳣
	 */
	public String[] getDirList(String remoteDir, String dirNameRegex) throws Exception {
		if (remoteDir != null && remoteDir.length() > 0) {
			cd(remoteDir);
		}
		FTPFile[] fileList = ftpClient.listFiles();
		ArrayList<String> al = new ArrayList<String>();

		if (dirNameRegex != null && dirNameRegex.trim().length() != 0) {
			for (int i = 0; i < fileList.length; i++) {
				if (fileList[i].getName().equals(".") || fileList[i].getName().endsWith("..")) {
					continue;
				}
				if (fileList[i].isDirectory() && fileList[i].getName().matches(dirNameRegex)) {
					al.add(fileList[i].getName());
				}
			}
		} else {
			for (int i = 0; i < fileList.length; i++) {
				if (fileList[i].isDirectory()) {
					if (fileList[i].getName().equals(".") || fileList[i].getName().endsWith("..")) {
						continue;
					}
					al.add(fileList[i].getName());
				}
			}
		}
		String[] nameList = new String[al.size()];
		for (int i = 0; i < al.size(); i++) {
			nameList[i] = al.get(i);
		}
		return nameList;
	}

	/**
	 * ���ط������ı����ʽ
	 * 
	 * @return �������ı����ʽ
	 */
	public String getEncoding() {
		return ftpClient.getControlEncoding();
	}

	/**
	 * ���Զ��Ŀ¼�µ��ļ��б������ȵ�¼
	 * 
	 * @param remoteDir
	 *            Ŀ��Ŀ¼����Ϊ�գ���Ϊ��ǰftpĿ¼
	 * @param fileNameRegex
	 *            Ŀ���ļ�����������ʽ, ��Ϊ�գ����õ�ǰftpĿ¼�������ļ��б�
	 * @return �ļ��б�
	 * @throws Exception
	 *             �쳣
	 */
	public String[] getFileList(String remoteDir, String fileNameRegex) throws Exception {
		if (remoteDir != null && remoteDir.length() > 0) {
			cd(remoteDir);
		}
		FTPFile[] fileList = ftpClient.listFiles();

		ArrayList<String> al = new ArrayList<String>();
		if (fileNameRegex != null && fileNameRegex.trim().length() != 0) {
			for (int i = 0; i < fileList.length; i++) {
				if (fileList[i].isFile() && fileList[i].getName().matches(fileNameRegex)) {
					al.add(fileList[i].getName());
				}
			}
		} else {
			for (int i = 0; i < fileList.length; i++) {
				if (fileList[i].isFile()) {
					al.add(fileList[i].getName());
				}
			}

		}
		String[] nameList = new String[al.size()];
		for (int i = 0; i < al.size(); i++) {
			nameList[i] = al.get(i);
		}
		return nameList;
	}

	/**
	 * ȡ�ñ���·��
	 * 
	 * @return ����·��
	 */
	public String getLocalDir() {
		return localDir;
	}

	/**
	 * ��¼FTP
	 * 
	 * @param url
	 *            Ŀ��url����www.sina.com
	 * @param port
	 *            �˿�
	 * @param userName
	 *            �û���
	 * @param password
	 *            ����
	 * @return �Ƿ��½�ɹ�
	 * @throws Exception
	 *             �쳣
	 */
	public boolean login(String url, int port, String userName, String password) throws Exception {
		try {
			if (ftpClient.isConnected()) {
				exit();
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		ftpClient.connect(url, port);
		int code = ftpClient.getReplyCode();
		String reply = ftpClient.getReplyString();

		if (!FTPReply.isPositiveCompletion(code)) {
			ftpClient.disconnect();
			throw new Exception(reply);
		}

		ftpClient.setSoTimeout(TIMEOUT);
		ftpClient.setDataTimeout(TIMEOUT);
		setBinary();
		return ftpClient.login(userName, password);
	}

	/**
	 * ��¼FTP��Ĭ��21�˿ڣ�
	 * 
	 * @param url
	 *            Ŀ��url����www.sina.com
	 * @param userName
	 *            �û���
	 * @param password
	 *            ����
	 * @return �Ƿ��½�ɹ�
	 * @throws Exception
	 *             �쳣
	 */
	public boolean login(String url, String userName, String password) throws Exception {

		return login(url, 21, userName, password);
	}

	/**
	 * ����Ŀ¼
	 * 
	 * @param name
	 *            ����
	 * @return ��������Ƿ�ɹ�
	 * @throws Exception
	 *             �쳣
	 */
	public boolean mkdir(String name) throws Exception {
		return ftpClient.makeDirectory(name);
	}

	/**
	 * ���������ļ��������ȵ�¼
	 * 
	 * @param localDir
	 *            ����·������Ϊ����ΪԤ��·������Ԥ��·��Ϊ�գ���Ϊ����Ŀ¼
	 * @param remoteFileNames
	 *            �����ļ�����������ʽ����Ϊ�������ص�ǰftpĿ¼�������ļ�(�������ļ���)
	 * @return ���������ļ��Ƿ�ɹ�
	 */
	public boolean multiGet(String localDir, String remoteFileNames) throws Exception {
		String[] files = this.getFileList(null, remoteFileNames);
		if (files == null) {
			return false;
		}
		for (int i = 0; i < files.length; i++) {
			get(localDir, files[i]);
		}
		return true;
	}

	/**
	 * �����ϴ��ļ��������ȵ�¼
	 * 
	 * @param localDir
	 *            ����·������Ϊ����ΪԤ��·������Ԥ��·��Ϊ�գ���Ϊ����Ŀ¼
	 * @param localFileNames
	 *            �ϴ��ļ����б�
	 * @return �����ϴ��ļ��Ƿ�ɹ�
	 * @throws Exception
	 *             �쳣
	 */
	public boolean multiPut(String localDir, String[] localFileNames) throws Exception {
		for (int i = 0; i < localFileNames.length; i++) {
			this.put(localDir, localFileNames[i]);
		}
		return true;
	}

	/**
	 * �ϴ��ļ��������ȵ�¼
	 * 
	 * @param localDir
	 *            ����·������Ϊ����ΪԤ��·������Ԥ��·��Ϊ�գ���Ϊ����Ŀ¼
	 * @param localFileName
	 *            ���ϴ������ļ���
	 * @return �ϴ��ļ��Ƿ�ɹ�
	 * @throws Exception
	 *             �쳣
	 */
	public boolean put(String localDir, String localFileName) throws Exception {
		if (localDir == null || localDir.trim().length() == 0) {
			localDir = this.localDir;
		}
		if (localDir == null || localDir.trim().length() == 0) {
			localDir = ".";
		}
		File file;
		if (localFileName.startsWith("/")) {
			file = new File(localDir + localFileName);
		} else {
			file = new File(localDir + "/" + localFileName);
		}
		FileInputStream fis = new FileInputStream(file);
		try {
			return ftpClient.storeFile(file.getName(), fis);
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
	 * @param localFile
	 *            �����ļ�
	 * @param remoteFilePath
	 *            Զ���ļ�·���������ļ������������ڻ��Զ�����
	 * @return �ϴ��ļ���Զ�˷������Ƿ�ɹ�
	 * @throws Exception
	 *             �쳣
	 */
	public boolean putToFile(File localFile, String remoteFilePath) throws Exception {
		if (!localFile.isFile()) {
			throw new FileNotFoundException("�ļ�" + localFile.getAbsolutePath() + "�����ڻ���û�ж�ȡȨ��");
		}
		String[] temp = remoteFilePath.split("/");
		String currentDir = ftpClient.printWorkingDirectory();
		int length = temp.length;
		String fileName = temp[length - 1];
		if (fileName.indexOf(".") == -1) {
			throw new Exception("�ļ�·�����Ϸ�");
		}
		if (remoteFilePath.startsWith("/")) {
			cd("/");
		}
		for (String dir : temp) {
			if (dir.trim().length() > 0) {
				if (dir.indexOf(".") == -1) {
					if (!cd(dir)) {
						mkdir(dir);
						cd(dir);
					}
				}
			}
		}
		FileInputStream fis = new FileInputStream(localFile);
		try {
			boolean b = ftpClient.storeFile(fileName, fis);
			cd(currentDir);
			return b;
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
	 * @param localFilePath
	 *            �����ļ�·��
	 * @param remoteFilePath
	 *            Զ���ļ�·���������ļ������������ڻ��Զ�����
	 * @return �ϴ��ļ���Զ�˷������Ƿ�ɹ�
	 * @throws Exception
	 *             �쳣
	 */
	public boolean putToFile(String localFilePath, String remoteFilePath) throws Exception {
		File file = new File(localFilePath);
		return putToFile(file, remoteFilePath);
	}

	/**
	 * �ϴ��ļ���Զ��Ŀ¼
	 * 
	 * @param localFile
	 *            �����ļ�
	 * @param remoteDirPath
	 *            Զ��Ŀ¼·���������ڻ��Զ�����
	 * @return �ϴ��ļ���Զ��Ŀ¼�Ƿ�ɹ�
	 * @throws Exception
	 */
	public boolean putToPath(File localFile, String remoteDirPath) throws Exception {
		if (!localFile.isFile()) {
			throw new FileNotFoundException("�ļ�" + localFile.getAbsolutePath() + "�����ڻ���û�ж�ȡȨ��");
		}
		String[] temp = remoteDirPath.split("/");
		String currentDir = ftpClient.printWorkingDirectory();
		if (remoteDirPath.startsWith("/")) {
			cd("/");
		}
		for (String dir : temp) {
			if (dir.trim().length() > 0) {
				if (!cd(dir)) {
					mkdir(dir);
					cd(dir);
				}
			}
		}
		FileInputStream fis = new FileInputStream(localFile);
		try {
			boolean b = ftpClient.storeFile(localFile.getName(), fis);
			cd(currentDir);
			return b;
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
	 * �ϴ��ļ���Զ��Ŀ¼
	 * 
	 * @param localFilePath
	 *            �����ļ�·��
	 * @param remoteDirPath
	 *            Զ��Ŀ¼·���������ڻ��Զ�����
	 * @return �ϴ��ļ���Զ��Ŀ¼�Ƿ�ɹ�
	 * @throws Exception
	 *             �쳣
	 */
	public boolean putToPath(String localFilePath, String remoteDirPath) throws Exception {
		File file = new File(localFilePath);
		return putToPath(file, remoteDirPath);
	}

	/**
	 * �޸�ftp��ǰĿ¼�µ��ļ����������ȵ�¼
	 * 
	 * @param srcFileName
	 *            ԭ�ļ���
	 * @param destFileName
	 *            Ŀ���ļ���
	 * @return �޸�ftp��ǰĿ¼�µ��ļ����Ƿ�ɹ�
	 * @throws Exception
	 *             �쳣
	 */
	public boolean rename(String srcFileName, String destFileName) throws Exception {
		return ftpClient.rename(srcFileName, destFileName);
	}

	/**
	 * �����ļ�����ģʽΪ�ı�ģʽ,�����ȵ�¼
	 * 
	 * @return �Ƿ����óɹ�
	 * @throws Exception
	 *             �쳣
	 */
	public boolean setAscii() throws Exception {

		return ftpClient.setFileType(FTPClient.ASCII_FILE_TYPE);

	}

	/**
	 * �����ļ�����ģʽΪ������ģʽ����ģʽΪĬ��ģʽ,�����ȵ�¼
	 * 
	 * @return �Ƿ����óɹ�
	 * @throws Exception
	 *             �쳣
	 */
	public boolean setBinary() throws Exception {
		return ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
	}

	/**
	 * ���÷������ı����ʽ
	 * 
	 * @param encoding
	 *            �������ı����ʽ
	 */
	public void setEncoding(String encoding) {
		ftpClient.setControlEncoding(encoding);
	}

	/**
	 * ���ñ���·��
	 * 
	 * @param path
	 *            ����·��
	 */
	public void setLocalDir(String path) {
		this.localDir = path;
	}
}
