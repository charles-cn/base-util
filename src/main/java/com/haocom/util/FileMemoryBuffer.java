package com.haocom.util;

import java.io.File;
import java.io.FileInputStream;
import java.util.LinkedHashMap;

/**
 * 文件（目录）内存缓存类. <br>
 * 将文件（目录）缓存在内存中，并自动更新，减少磁盘读写次数.
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
	 * 目录文件清单缓存.
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
	 * 文件内容缓存.
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
	 * 标题、简要说明. <br>
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

	/** 对象实例 */
	private static FileMemoryBuffer instance = null;

	/**
	 * 获取对象实例
	 * 
	 * @return 对象实例
	 */
	public static synchronized FileMemoryBuffer getInstance() {
		if (instance == null) {
			instance = new FileMemoryBuffer();
		}
		return instance;
	}

	/**
	 * 读取文件为字节数组
	 * 
	 * @param file
	 *            文件
	 * @return 文件内容，以字节数组形式返回
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
	 * 读取文件为字节数组
	 * 
	 * @param filename
	 *            文件名
	 * @return 文件内容，以字节数组形式返回
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

	/** 目录缓存 */
	private DirBuffer dirBuffer = new DirBuffer();

	/** 文件缓存 */
	private FileBuffer fileBuffer = new FileBuffer();

	/** 文件缓存大小合计上限 */
	private int maxBufferSize = 0;

	/** 目录缓存数量上限,默认1k */
	private int maxDirCount = DEFAULT_MAX_DIR_COUNT;

	/** 文件缓存数量上限,默认8k */
	private int maxFileCount = DEFAULT_MAX_FILE_COUNT;

	/** 文件缓存中允许缓存最大文件的大小,默认64K */
	private int maxFileSize = DEFAULT_MAX_FILE_SIZE;

	/**
	 * 根据文件来获取目录下文件清单
	 * 
	 * @param dir
	 *            文件
	 * @return 文件清单，用数组形式返回
	 * @throws Exception
	 *             Exception
	 */
	public synchronized File[] getDir(File dir) throws Exception {
		dir = dir.getCanonicalFile();

		//
		FileInfo fileInfo = null;
		// fileInfo = dirBuffer.get(dir);
		// // 判断文件是否被修改
		// if (fileInfo != null) {
		// if (fileInfo.file.lastModified() != dir.lastModified()) {
		// fileInfo = null;
		// }
		// }

		// 从磁盘读取
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
	 * 根据文件目录获取目录下文件清单
	 * 
	 * @param dir
	 *            文件目录
	 * @return 文件清单，用数组形式返回
	 * @throws Exception
	 *             Exception
	 */
	public File[] getDir(String dir) throws Exception {
		File file = new File(dir);
		return getDir(file);
	}

	/**
	 * 根据文件获取文件的内容
	 * 
	 * @param file
	 *            文件
	 * @return 文件内容，用数组形式返回
	 * @throws Exception
	 *             Exception
	 */
	public synchronized byte[] getFile(File file) throws Exception {
		file = file.getCanonicalFile();

		//
		FileInfo info = null;
		// info = fileBuffer.get(file);

		// 判断文件是否被修改
		if (info != null) {
			if (info.file.lastModified() != file.lastModified()) {
				info = null;
			}
		}

		// 从磁盘读取
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
	 * 根据文件名获取文件的内容
	 * 
	 * @param filename
	 *            文件名
	 * @return 文件内容，用数组形式返回
	 * @throws Exception
	 *             Exception
	 */
	public byte[] getFile(String filename) throws Exception {
		File file = new File(filename);
		return getFile(file);
	}

	/**
	 * 获取文件缓存合计大小的上限
	 * 
	 * @return 文件缓存合计大小的上限
	 */
	@Deprecated
	public int getMaxBufferSize() {
		return maxBufferSize;
	}

	/**
	 * 获取目录缓存数量上限
	 * 
	 * @return 目录缓存数量上限
	 */
	public int getMaxDirCount() {
		return maxDirCount;
	}

	/**
	 * 获取文件缓存数量上限
	 * 
	 * @return 文件缓存数量上限
	 */
	public int getMaxFileCount() {
		return maxFileCount;
	}

	/**
	 * 获取可缓存文件的最大尺寸
	 * 
	 * @return 文件大小
	 */
	public int getMaxFileSize() {
		return maxFileSize;
	}

	/**
	 * 获取文件缓存的报告
	 * 
	 * @return 文件缓存的报告
	 */
	public String getReport() {
		StringBuilder buf = new StringBuilder();
		buf.append("file count: ").append(fileBuffer.size()).append(", ");
		buf.append("dir count: ").append(dirBuffer.size()).append(", ");
		return buf.toString();
	}

	/**
	 * 设置文件缓存合计大小的上限
	 * 
	 * @param bufferSize
	 *            合计大小上限
	 */
	@Deprecated
	public void setMaxBufferSize(int bufferSize) {
		this.maxBufferSize = bufferSize;
	}

	/**
	 * 设置目录缓存数量上限,默认1k
	 * 
	 * @param maxDirCount
	 *            目录缓存数量上限
	 */
	public void setMaxDirCount(int maxDirCount) {
		this.maxDirCount = maxDirCount;
	}

	/**
	 * 设置文件缓存数量上限,默认8k
	 * 
	 * @param maxFileCount
	 *            文件缓存数量上限
	 */
	public void setMaxFileCount(int maxFileCount) {
		this.maxFileCount = maxFileCount;
	}

	/**
	 * 设置可缓存文件的最大尺寸,默认64K
	 * 
	 * @param maxFileSize
	 *            文件大小
	 */
	public void setMaxFileSize(int maxFileSize) {
		this.maxFileSize = maxFileSize;
	}
}
