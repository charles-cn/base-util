/**
 * tools:com.cplatform.v1.util.FileTools.java
 */
package com.haocom.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * �ļ���д����������. <br>
 * ��װ��ͨ�ļ���д������������д���������Լ���д�ı���ͬʱ���ṩ�˴���Ŀ¼�ķ���.
 * <p>
 * Copyright: Copyright (c) Sep 16, 2008
 * <p>
 * Company: 
 * <p>
 * Author: ywh
 * <p>
 * Version: 1.0
 */

public class FileTools {

	/**
	 * ���ַ���׷�ӵ��ı��ļ�������ļ��Ѵ��ڣ�׷�ӵ�ԭ�������ݺ�
	 * 
	 * @param str
	 *            Ҫд��������
	 * @param fileName
	 *            �ļ���
	 * @throws Exception
	 */
	public static void appendTxt(String str, String fileName) throws Exception {
		write(str, fileName, null, true);
	}

	/**
	 * ���ַ���׷�ӵ��ı��ļ�������ļ��Ѵ��ڣ�׷�ӵ�ԭ�������ݺ�
	 * 
	 * @param str
	 *            Ҫд��������
	 * @param fileName
	 *            �ļ���
	 * @param encoding
	 *            �ַ���
	 * @throws Exception
	 */
	public static void appendTxt(String str, String fileName, String encoding) throws Exception {
		write(str, fileName, encoding, true);
	}

	/**
	 * �ļ�����
	 * 
	 * @param from
	 *            Ҫ���Ƶ��ļ�
	 * @param to
	 *            ���Ƶ��ĸ��ļ�
	 * @throws Exception
	 */
	public static final void copy(String from, String to) throws Exception {
		makeParentDir(to);
		FileInputStream fis = new FileInputStream(from);
		try {
			FileOutputStream fos = new FileOutputStream(to, false);
			try {
				byte[] buf = new byte[1024 * 16];
				int size = 0;
				while ((size = fis.read(buf)) != -1) {
					fos.write(buf, 0, size);
				}
			}
			catch (Exception ex) {
				throw ex;
			}
			finally {
				fos.flush();
				fos.close();
			}
		}
		catch (Exception ex) {
			throw ex;
		}
		finally {
			fis.close();
		}
	}

	/**
	 * �ļ�����Ŀ¼
	 * 
	 * @param from
	 *            Ҫ���Ƶ��ļ�Ŀ¼
	 * @param to
	 *            ���Ƶ��ĸ��ļ�Ŀ¼
	 * @param recursive
	 *            �Ƿ�ݹ����
	 * @throws Exception
	 */
	public static final void copyDir(String from, String to, boolean recursive) throws Exception {
		if (from.endsWith("/")) {
			from = from.substring(0, from.length() - 1);
		}
		if (to.endsWith("/")) {
			to = to.substring(0, to.length() - 1);
		}
		File file = new File(from);
		if (file.isDirectory()) {
			String[] fileList = file.list();
			for (int i = 0; i < fileList.length; i++) {
				String tmp = from + "/" + fileList[i];
				if (new File(tmp).isFile() || recursive) {
					copyDir(tmp, to + "/" + fileList[i], recursive);
				}
			}
		} else {
			copy(from, to);
		}
	}

	/**
	 * �����ļ���ȡ�ļ���չ��.<br>
	 * ���磺1.txt����չ������txt
	 * 
	 * @param file
	 *            �ļ�
	 * @return �ļ���չ��
	 */
	public static String getExtFilename(File file) {
		return getExtFilename(file.getName());
	}

	/**
	 * �����ļ�����ȡ�ļ���չ��.<br>
	 * ���磺1.txt����չ������txt
	 * 
	 * @param fileName
	 *            �ļ���
	 * @return �ļ���չ��
	 */
	public static String getExtFilename(String fileName) {
		int i = fileName.lastIndexOf(".");
		if (i >= 0) {
			return fileName.substring(i + 1);
		} else {
			return fileName;
		}
	}

	/**
	 * �����ļ��Ĵ�С(��λΪ�ֽ�).<br>
	 * Ĭ�ϵݹ����Ϊ10�������������ļ��Ĵ�С�ܺ͡�
	 * 
	 * @param file
	 *            �ļ�/Ŀ¼��·��
	 * @return �ļ�/Ŀ¼�Ĵ�С(��λΪ�ֽ�).
	 */
	public static long getFileSize(File file) throws Exception {
		return getFileSize(file, true, 10);
	}

	/**
	 * �����ļ��Ĵ�С(��λΪ�ֽ�).<br>
	 * ����Ҫ�ݹ飬��ݹ����10�������������ļ��Ĵ�С�ܺ͡�
	 * 
	 * @param file
	 *            �ļ�/Ŀ¼��·��
	 * @param recursive
	 *            �Ƿ�ݹ�
	 * @return �ļ�/Ŀ¼�Ĵ�С(��λΪ�ֽ�).
	 * @throws Exception
	 */
	public static long getFileSize(File file, boolean recursive) throws Exception {
		return getFileSize(file, recursive, 10);
	}

	/**
	 * �����ļ��Ĵ�С(��λΪ�ֽ�).<br>
	 * ����ָ���ĵݹ鼶�������������ļ��Ĵ�С�ܺ͡�
	 * 
	 * @param file
	 *            �ļ�/Ŀ¼��·��
	 * @param recursive
	 *            �Ƿ�ݹ�
	 * @param maxDepth
	 *            �ݹ鼶��
	 * @return �ļ�/Ŀ¼�Ĵ�С(��λΪ�ֽ�).
	 * @throws Exception
	 */
	public static long getFileSize(File file, boolean recursive, int maxDepth) throws Exception {
		// �ж��ļ��Ƿ���ڣ������������׳��쳣
		if (!file.exists()) {
			String message = file + " does not exist";
			throw new IllegalArgumentException(message);
		}

		if (recursive) {
			return getFileSize(file, maxDepth, 0);// ʵ�ָ������õĵݹ鼶����ȡ�ļ���С
		} else {
			return getFileSize(file, 0, 0);// ���ݹ飬��ݹ鼶��Ϊ0
		}
	}

	/**
	 * ����ָ���ݹ鼶����ȡ�ļ���С�ܺ�(��λΪ�ֽ�)
	 * 
	 * @param file
	 *            �ļ�·��
	 * @param maxDepth
	 *            ���ĵݹ鼶��
	 * @param currentDepth
	 *            ��ǰ�ĵݹ鼶��
	 * @return �ļ���С�ܺ�
	 * @throws Exception
	 */
	private static long getFileSize(File file, int maxDepth, int currentDepth) throws Exception {
		long size = 0;

		// ����������File���ļ�, ��ֱ�ӷ����ļ���С
		if (!file.isDirectory()) {
			return file.length();
		}

		// �����ǰ�����Ѵﵽ�����ļ������򷵻�0
		if (currentDepth > maxDepth) {
			return 0L;
		}

		// ��ȡ��ǰ·���������ļ���Ŀ¼
		File[] files = file.listFiles();
		if (files == null) {// ��Ŀ¼�а�ȫ������᷵��null����ʱ��Ϊ���Ŀ¼��СΪ0
			return 0L;
		}
		for (int i = 0; i < files.length; i++) {
			File subFile = files[i];
			size += getFileSize(subFile, maxDepth, currentDepth + 1);
		}
		return size;
	}

	/**
	 * �г�ָ��Ŀ¼�µ��ļ��嵥
	 * 
	 * @param dir
	 *            �ļ�Ŀ¼
	 * @return �ļ��嵥
	 */
	public static List<File> listFile(File dir) {
		return listFile(dir, true);
	}

	/**
	 * �г�ָ��Ŀ¼�µ��ļ��嵥
	 * 
	 * @param dir
	 *            �ļ�Ŀ¼
	 * @param recursive
	 *            �Ƿ�ݹ����
	 * @return �ļ��嵥
	 */
	public static List<File> listFile(File dir, boolean recursive) {
		List<File> result = new ArrayList<File>();
		File[] files = dir.listFiles();
		for (File file : files) {
			if (file.isFile()) {
				result.add(file);
			} else if (file.isDirectory() && recursive) {
				result.addAll(listFile(file, true));
			}
		}
		return result;
	}

	/**
	 * ����ָ���ļ����ڵ�Ŀ¼
	 * 
	 * @param fileName
	 *            ָ���ļ���
	 * @throws Exception
	 */
	public static final void makeParentDir(String fileName) throws Exception {
		File file = new File(fileName);
		if (!file.exists()) {
			String parent = file.getParent();
			if (parent != null) {
				new File(parent).mkdirs();
			}
		}
	}

	/**
	 * ����Ŀ¼������Ŀ¼
	 * 
	 * @param dirName
	 *            Ŀ¼���������·���ļ�����
	 * @return �Ƿ񴴽��ɹ�
	 */
	public static boolean mkDir(String dirName) {
		File file = new File(dirName);
		if (file.exists() || file.getParentFile() == null) {
			return true;
		}
		if (dirName.endsWith("/") || dirName.endsWith("\\")) {
			return file.mkdirs();
		}
		return file.getParentFile().mkdirs();
	}

	/**
	 * ���ļ��ж�ȡ�������ֽ���
	 * 
	 * @param fileName
	 *            Ҫ��ȡ���ļ���
	 * @return ��ȡ������
	 */
	public static byte[] read(String fileName) throws Exception {
		if (fileName == null) {
			return null;
		}
		FileInputStream in = null;
		try {
			in = new FileInputStream(fileName);
			byte[] bytes = new byte[in.available()];
			in.read(bytes);
			return bytes;
		}
		catch (Exception e) {
			throw e;
		}
		finally {
			in.close();
		}
	}

	/**
	 * ���ļ��ж�ȡ��һ���ı�
	 * 
	 * @param fileName
	 *            �ļ���
	 * @return �ļ���һ������
	 */
	public static String readLine(String fileName) throws Exception {
		if (fileName == null) {
			return null;
		}

		FileReader fileReader = new FileReader(fileName);
		try {
			BufferedReader in = new BufferedReader(fileReader, 1024 * 64);
			String line = in.readLine();
			if (line != null) {
				line = line.trim();
			}
			return line;
		}
		catch (Exception ex) {
			throw ex;
		}
		finally {
			fileReader.close();
		}
	}

	/**
	 * ���ļ��ж�ȡ��һ���ı�
	 * 
	 * @param fileName
	 *            �ļ���
	 * @param encoding
	 *            �ַ���
	 * @return �ļ���һ������
	 * @throws Exception
	 */
	public static String readLine(String fileName, String encoding) throws Exception {
		if (fileName == null) {
			return null;
		}
		FileInputStream fileInputStream = new FileInputStream(fileName);
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(fileInputStream, encoding), 1024 * 64);
			String line = in.readLine();
			if (line != null) {
				line = line.trim();
			}
			return line;
		}
		catch (Exception ex) {
			throw ex;
		}
		finally {
			fileInputStream.close();
		}
	}

	/**
	 * ���ļ��ж�ȡ�����ı�
	 * 
	 * @param file
	 *            �ļ�
	 * @return �����ı��ļ���
	 */
	public static List<String> readLines(File file) throws Exception {
		List<String> result = new ArrayList<String>();
		FileReader fileReader = new FileReader(file);
		try {
			BufferedReader bufferedReader = new BufferedReader(fileReader, 1024 * 64);
			String line = null;
			while ((line = bufferedReader.readLine()) != null) {
				result.add(line);
			}
			return result;
		}
		catch (Exception ex) {
			throw ex;
		}
		finally {
			fileReader.close();
		}
	}

	/**
	 * ���ļ��ж�ȡ�����ı�
	 * 
	 * @param file
	 *            �ļ�
	 * @param encoding
	 *            �ַ���
	 * @return �����ı��ļ���
	 */
	public static List<String> readLines(File file, String encoding) throws Exception {
		List<String> result = new ArrayList<String>();
		FileInputStream fileInputStream = new FileInputStream(file);
		try {
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream, encoding), 1024 * 64);
			String line = null;
			while ((line = bufferedReader.readLine()) != null) {
				result.add(line);
			}
			return result;
		}
		catch (Exception ex) {
			throw ex;
		}
		finally {
			fileInputStream.close();
		}
	}

	/**
	 * ���ļ��ж�ȡ�����ı�
	 * 
	 * @param fileName
	 *            �ļ���
	 * @return ÿ���ı��ļ���
	 */
	public static List<String> readLines(String fileName) throws Exception {
		return readLines(new File(fileName));
	}

	/**
	 * ���ļ��ж�ȡ�����ı�
	 * 
	 * @param fileName
	 *            �ļ���
	 * @param encoding
	 *            �ַ���
	 * @return ÿ���ı��ļ���
	 */
	public static List<String> readLines(String fileName, String encoding) throws Exception {
		return readLines(new File(fileName), encoding);
	}

	/**
	 * ���ļ��ж�ȡ�ı�������
	 * 
	 * @param file
	 *            �ļ�
	 * @return �����ļ�����
	 * @throws Exception
	 */
	public static String readTxt(File file) throws Exception {
		return readTxt(file, null);
	}

	/**
	 * ���ļ��ж�ȡ�ı�������
	 * 
	 * @param file
	 *            �ļ�
	 * @param encoding
	 *            �ַ���
	 * @return �����ļ�����
	 * @throws Exception
	 */
	public static String readTxt(File file, String encoding) throws Exception {
		if (encoding == null) {
			encoding = Charset.defaultCharset().name();
		}
		FileInputStream fis = new FileInputStream(file);
		try {
			char[] cbuf = new char[100];
			StringBuilder buf = new StringBuilder();
			InputStreamReader isr = new InputStreamReader(fis, encoding);
			int n;
			while ((n = isr.read(cbuf)) >= 0) {
				buf.append(cbuf, 0, n);
			}
			return buf.toString();
		}
		catch (Exception ex) {
			throw ex;
		}
		finally {
			fis.close();
		}
	}

	/**
	 * ���ļ��ж�ȡ�ı�������
	 * 
	 * @param fileName
	 *            �ļ���
	 * @return �����ļ�����
	 * @throws Exception
	 */
	public static String readTxt(String fileName) throws Exception {
		return readTxt(new File(fileName), null);
	}

	/**
	 * ���ļ��ж�ȡ�ı�������
	 * 
	 * @param fileName
	 *            �ļ���
	 * @param encoding
	 *            �ַ���
	 * @return �����ļ�����
	 * @throws Exception
	 */
	public static String readTxt(String fileName, String encoding) throws Exception {
		return readTxt(new File(fileName), encoding);
	}

	/**
	 * ɾ��Ŀ¼
	 * 
	 * @param dir
	 *            �ļ�Ŀ¼
	 */
	public static void rmDir(File dir) {
		if (!dir.exists()) {
			return;
		}
		if (dir.isDirectory()) {
			File[] files = dir.listFiles();
			for (File file : files) {
				if (file.isDirectory()) {
					rmDir(file);
				}
				file.delete();
			}
		}
		dir.delete();
	}

	/**
	 * �Ѷ������ֽ���д���ļ�
	 * 
	 * @param bytes
	 *            Ҫд�����ֽ�����
	 * @param fileName
	 *            �ļ���
	 */
	public static void write(byte[] bytes, String fileName) throws Exception {
		if (bytes == null || fileName == null) {
			return;
		}
		mkDir(fileName); // д�ļ���Ҫ������Ŀ¼�Ƿ���ڣ�û�еĻ���Ҫ����
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(fileName);
			out.write(bytes);
		}
		catch (Exception e) {
			throw e;
		}
		finally {
			out.close();
		}
	}

	/**
	 * д�ļ�����
	 * 
	 * @param str
	 *            Ҫд��������
	 * @param fileName
	 *            �ļ���
	 * @param isAppend
	 *            �Ƿ�׷�ӣ����ֵΪfalse���򸲸ǵ���ǰ������
	 * @throws Exception
	 */
	public static void write(String str, String fileName, boolean isAppend) throws Exception {
		write(str, fileName, null, isAppend);
	}

	/**
	 * д�ļ�����
	 * 
	 * @param str
	 *            Ҫд��������
	 * @param fileName
	 *            �ļ���
	 * @param encoding
	 *            �ַ���
	 * @param isAppend
	 *            �Ƿ�׷�ӣ����ֵΪfalse���򸲸ǵ���ǰ������
	 * @throws Exception
	 */
	public static void write(String str, String fileName, String encoding, boolean isAppend) throws Exception {
		if (fileName == null || str == null) {
			return;
		}
		File dir = new File(fileName).getParentFile();
		if (dir != null && !dir.exists()) {
			dir.mkdirs();
		}
		if (encoding == null) {
			encoding = Charset.defaultCharset().name();
		}
		FileOutputStream fOutput = new FileOutputStream(fileName, isAppend);
		try {
			OutputStreamWriter osw = new OutputStreamWriter(fOutput, encoding);
			try {
				osw.write(str);
				osw.flush();
			}
			catch (Exception ex) {
				throw ex;
			}
			finally {
				osw.close();
			}
		}
		catch (Exception ex) {
			throw ex;
		}
		finally {
			fOutput.close();
		}
	}

	/**
	 * ���ַ���д�����ı��ļ�������ļ��Ѵ��ڣ�������ԭ��������
	 * 
	 * @param str
	 *            Ҫд��������
	 * @param fileName
	 *            �ļ���
	 * @throws Exception
	 */
	public static void writeTxt(String str, String fileName) throws Exception {
		write(str, fileName, null, false);
	}

	/**
	 * ���ַ���д�����ı��ļ�������ļ��Ѵ��ڣ�������ԭ��������
	 * 
	 * @param str
	 *            Ҫд��������
	 * @param fileName
	 *            �ļ���
	 * @param encoding
	 *            �ַ���
	 * @throws Exception
	 */
	public static void writeTxt(String str, String fileName, String encoding) throws Exception {
		write(str, fileName, encoding, false);
	}
}
