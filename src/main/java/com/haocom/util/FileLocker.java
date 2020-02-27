package com.haocom.util;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.HashMap;
import java.util.Map;

/**
 * 文件锁. <br>
 * 使用FileLock实现的文件锁. 用于防止程序重复启动, 任务重复执行等情况.
 * <p>
 * Copyright: Copyright (c) Sep 16, 2008
 * <p>
 * Company: 
 * <p>
 * Author: ChengFan
 * <p>
 * Version: 1.0
 * <p>
 * 
 * <pre>
 * 使用场景：例如开发后台独立进程时，使用FileLock实现文件锁，可用于防止程序重复启动。
 * 使用示例：在程序入口MainEntry类中，添加如下代码：
 * 
 * // 判断程序是否重复启动。isLocked方法：判断文件是否锁定, 如果文件未被锁定则将该文件加上文件锁.
 * if (FileLocker.isLocked(&quot;./.process.lock&quot;)) {
 * 	System.out.println(&quot;警告：程序已经启动！&quot;);
 * 	return;
 * }
 * </pre>
 */

public class FileLocker {

	/** 文件锁缓存 */
	private static Map<File, FileLock> lockMap = new HashMap<File, FileLock>();

	/**
	 * 判断文件是否锁定, 如果文件未被锁定则将该文件加上文件锁.
	 * 
	 * @param file
	 *            文件
	 * @return 文件是否锁定
	 */
	public static synchronized boolean isLocked(File file) {
		File dir = file.getParentFile();
		if (dir != null && !dir.exists()) {
			dir.mkdirs();
		}
		try {
			if (!file.exists()) {
				file.createNewFile();
			}
			if (file.canWrite()) {
				RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
				FileChannel channel = randomAccessFile.getChannel();
				FileLock lock = channel.tryLock();
				lockMap.put(file, lock);
				if (lock == null || lock.isValid() == false) {
					return true;
				} else {
					return false;
				}
			}
		}
		catch (Exception ex) {
		}
		return true;
	}

	/**
	 * 判断文件是否锁定, 如果文件未被锁定则将该文件加上文件锁.
	 * 
	 * @param filename
	 *            文件名
	 * @return 文件是否锁定
	 */
	public static synchronized boolean isLocked(String filename) throws Exception {
		return isLocked(new File(filename));
	}

	/**
	 * 解开指定文件的锁
	 * 
	 * @param file
	 *            文件
	 */
	public static synchronized void unlock(File file) {
		try {
			FileLock fileLock = lockMap.get(file);
			if (fileLock != null) {
				fileLock.release();
				fileLock.channel().close();
				lockMap.remove(file);
			} else {
				System.out.println("Not In The Map");
			}
		}
		catch (Exception ex) {
		}
	}

	/**
	 * 解开指定文件的锁
	 * 
	 * @param filename
	 *            文件名
	 */
	public static synchronized void unlock(String filename) {
		unlock(new File(filename));
	}
}
