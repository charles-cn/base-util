package com.haocom.util;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.HashMap;
import java.util.Map;

/**
 * �ļ���. <br>
 * ʹ��FileLockʵ�ֵ��ļ���. ���ڷ�ֹ�����ظ�����, �����ظ�ִ�е����.
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
 * ʹ�ó��������翪����̨��������ʱ��ʹ��FileLockʵ���ļ����������ڷ�ֹ�����ظ�������
 * ʹ��ʾ�����ڳ������MainEntry���У�������´��룺
 * 
 * // �жϳ����Ƿ��ظ�������isLocked�������ж��ļ��Ƿ�����, ����ļ�δ�������򽫸��ļ������ļ���.
 * if (FileLocker.isLocked(&quot;./.process.lock&quot;)) {
 * 	System.out.println(&quot;���棺�����Ѿ�������&quot;);
 * 	return;
 * }
 * </pre>
 */

public class FileLocker {

	/** �ļ������� */
	private static Map<File, FileLock> lockMap = new HashMap<File, FileLock>();

	/**
	 * �ж��ļ��Ƿ�����, ����ļ�δ�������򽫸��ļ������ļ���.
	 * 
	 * @param file
	 *            �ļ�
	 * @return �ļ��Ƿ�����
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
	 * �ж��ļ��Ƿ�����, ����ļ�δ�������򽫸��ļ������ļ���.
	 * 
	 * @param filename
	 *            �ļ���
	 * @return �ļ��Ƿ�����
	 */
	public static synchronized boolean isLocked(String filename) throws Exception {
		return isLocked(new File(filename));
	}

	/**
	 * �⿪ָ���ļ�����
	 * 
	 * @param file
	 *            �ļ�
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
	 * �⿪ָ���ļ�����
	 * 
	 * @param filename
	 *            �ļ���
	 */
	public static synchronized void unlock(String filename) {
		unlock(new File(filename));
	}
}
