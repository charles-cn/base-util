package com.haocom.util;

import java.io.File;
import java.io.FileInputStream;
import java.util.LinkedHashMap;

/**
 * �ļ���Ŀ¼���ڴ滺����. <br>
 * ���ļ���Ŀ¼���������ڴ��У����Զ����£����ٴ��̶�д����.
 * <p>
 * Copyright: Copyright (c) Sep 16, 2008
 * <p>
 * Company: 
 * <p>
 * 
 * @author chengfan@c-platform.com
 * @version 1.0.0
 */
public class FileMemoryBuffer {

	/**
	 * Ŀ¼�ļ��嵥����.
	 */
	class DirBuffer extends LinkedHashMap<File, FileInfo> {

		/** serialVersionUID */
		private static final long serialVersionUID = 1L;

		@Override
		protected boolean removeEldestEntry(java.util.Map.Entry<File, FileInfo> eldest) {
			return size() > maxDirCount;
		}
	}

	/**
	 * �ļ����ݻ���.
	 */
	class FileBuffer extends LinkedHashMap<File, FileInfo> {

		/** serialVersionUID */
		private static final long serialVersionUID = 1L;

		@Override
		protected boolean removeEldestEntry(java.util.Map.Entry<File, FileInfo> eldest) {
			return size() > maxFileCount;
		}

	}

	/**
	 * ���⡢��Ҫ˵��. <br>
	 */
	class FileInfo {

		/** content */
		private byte[] content;

		/** file */
		private File file;

		/** fileList */
		private File[] fileList;

		/** lastModify */
		private long lastModify;

		/**
		 * 
		 */
		private FileInfo() {
		}
	}

	/** DEFAULT_MAX_DIR_COUNT */
	private static final int DEFAULT_MAX_DIR_COUNT = 1024;

	/** DEFAULT_MAX_FILE_COUNT */
	private static final int DEFAULT_MAX_FILE_COUNT = 1024 * 8;

	/** DEFAULT_MAX_FILE_SIZE */
	private static final int DEFAULT_MAX_FILE_SIZE = 1024 * 64;;

	/** ����ʵ�� */
	private static FileMemoryBuffer instance = null;

	/**
	 * ��ȡ����ʵ��
	 * 
	 * @return ����ʵ��
	 */
	public static synchronized FileMemoryBuffer getInstance() {
		if (instance == null) {
			instance = new FileMemoryBuffer();
		}
		return instance;
	}

	/**
	 * ��ȡ�ļ�Ϊ�ֽ�����
	 * 
	 * @param file
	 *            �ļ�
	 * @return �ļ����ݣ����ֽ�������ʽ����
	 * @throws Exception
	 *             Exception
	 */
	public static byte[] loadFile(File file) throws Exception {
		byte[] buf = null;
		FileInputStream in = new FileInputStream(file);
		try {
			buf = new byte[in.available()];
			in.read(buf);
		}
		catch (Exception ex) {
			throw ex;
		}
		finally {
			in.close();
		}
		return buf;
	}

	/**
	 * ��ȡ�ļ�Ϊ�ֽ�����
	 * 
	 * @param filename
	 *            �ļ���
	 * @return �ļ����ݣ����ֽ�������ʽ����
	 * @throws Exception
	 *             Exception
	 */
	public static byte[] loadFile(String filename) throws Exception {
		byte[] buf = null;
		FileInputStream in = new FileInputStream(filename);
		try {
			buf = new byte[in.available()];
			in.read(buf);
		}
		catch (Exception ex) {
			throw ex;
		}
		finally {
			in.close();
		}
		return buf;
	}

	/** Ŀ¼���� */
	private DirBuffer dirBuffer = new DirBuffer();

	/** �ļ����� */
	private FileBuffer fileBuffer = new FileBuffer();

	/** �ļ������С�ϼ����� */
	private int maxBufferSize = 0;

	/** Ŀ¼������������,Ĭ��1k */
	private int maxDirCount = DEFAULT_MAX_DIR_COUNT;

	/** �ļ�������������,Ĭ��8k */
	private int maxFileCount = DEFAULT_MAX_FILE_COUNT;

	/** �ļ�����������������ļ��Ĵ�С,Ĭ��64K */
	private int maxFileSize = DEFAULT_MAX_FILE_SIZE;

	/**
	 * �����ļ�����ȡĿ¼���ļ��嵥
	 * 
	 * @param dir
	 *            �ļ�
	 * @return �ļ��嵥����������ʽ����
	 * @throws Exception
	 *             Exception
	 */
	public synchronized File[] getDir(File dir) throws Exception {
		dir = dir.getCanonicalFile();

		//
		FileInfo fileInfo = null;
		// fileInfo = dirBuffer.get(dir);
		// // �ж��ļ��Ƿ��޸�
		// if (fileInfo != null) {
		// if (fileInfo.file.lastModified() != dir.lastModified()) {
		// fileInfo = null;
		// }
		// }

		// �Ӵ��̶�ȡ
		if (fileInfo == null) {
			fileInfo = new FileInfo();
			fileInfo.file = dir;
			fileInfo.lastModify = dir.lastModified();
			fileInfo.fileList = dir.listFiles();
			// dirBuffer.put(dir, fileInfo);
		}

		return fileInfo.fileList;
	}

	/**
	 * �����ļ�Ŀ¼��ȡĿ¼���ļ��嵥
	 * 
	 * @param dir
	 *            �ļ�Ŀ¼
	 * @return �ļ��嵥����������ʽ����
	 * @throws Exception
	 *             Exception
	 */
	public File[] getDir(String dir) throws Exception {
		File file = new File(dir);
		return getDir(file);
	}

	/**
	 * �����ļ���ȡ�ļ�������
	 * 
	 * @param file
	 *            �ļ�
	 * @return �ļ����ݣ���������ʽ����
	 * @throws Exception
	 *             Exception
	 */
	public synchronized byte[] getFile(File file) throws Exception {
		file = file.getCanonicalFile();

		//
		FileInfo info = null;
		// info = fileBuffer.get(file);

		// �ж��ļ��Ƿ��޸�
		if (info != null) {
			if (info.file.lastModified() != file.lastModified()) {
				info = null;
			}
		}

		// �Ӵ��̶�ȡ
		if (info == null) {
			info = new FileInfo();
			info.file = file;
			info.lastModify = file.lastModified();
			info.content = loadFile(file);
			if (info.content.length <= maxFileSize) {
				// fileBuffer.put(file, info);
			}
		}

		return info.content;
	}

	/**
	 * �����ļ�����ȡ�ļ�������
	 * 
	 * @param filename
	 *            �ļ���
	 * @return �ļ����ݣ���������ʽ����
	 * @throws Exception
	 *             Exception
	 */
	public byte[] getFile(String filename) throws Exception {
		File file = new File(filename);
		return getFile(file);
	}

	/**
	 * ��ȡ�ļ�����ϼƴ�С������
	 * 
	 * @return �ļ�����ϼƴ�С������
	 */
	@Deprecated
	public int getMaxBufferSize() {
		return maxBufferSize;
	}

	/**
	 * ��ȡĿ¼������������
	 * 
	 * @return Ŀ¼������������
	 */
	public int getMaxDirCount() {
		return maxDirCount;
	}

	/**
	 * ��ȡ�ļ�������������
	 * 
	 * @return �ļ�������������
	 */
	public int getMaxFileCount() {
		return maxFileCount;
	}

	/**
	 * ��ȡ�ɻ����ļ������ߴ�
	 * 
	 * @return �ļ���С
	 */
	public int getMaxFileSize() {
		return maxFileSize;
	}

	/**
	 * ��ȡ�ļ�����ı���
	 * 
	 * @return �ļ�����ı���
	 */
	public String getReport() {
		StringBuilder buf = new StringBuilder();
		buf.append("file count: ").append(fileBuffer.size()).append(", ");
		buf.append("dir count: ").append(dirBuffer.size()).append(", ");
		return buf.toString();
	}

	/**
	 * �����ļ�����ϼƴ�С������
	 * 
	 * @param bufferSize
	 *            �ϼƴ�С����
	 */
	@Deprecated
	public void setMaxBufferSize(int bufferSize) {
		this.maxBufferSize = bufferSize;
	}

	/**
	 * ����Ŀ¼������������,Ĭ��1k
	 * 
	 * @param maxDirCount
	 *            Ŀ¼������������
	 */
	public void setMaxDirCount(int maxDirCount) {
		this.maxDirCount = maxDirCount;
	}

	/**
	 * �����ļ�������������,Ĭ��8k
	 * 
	 * @param maxFileCount
	 *            �ļ�������������
	 */
	public void setMaxFileCount(int maxFileCount) {
		this.maxFileCount = maxFileCount;
	}

	/**
	 * ���ÿɻ����ļ������ߴ�,Ĭ��64K
	 * 
	 * @param maxFileSize
	 *            �ļ���С
	 */
	public void setMaxFileSize(int maxFileSize) {
		this.maxFileSize = maxFileSize;
	}
}
