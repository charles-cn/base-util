/**
 * 
 */
package com.haocom.util.zip;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ѹ���ļ�. <br>
 * ���������ͽ�ѹѹ���ļ��ĸ��ַ���:<br>
 * <br>
 * 1.������Ҫʹ�ù��캯������ʵ��<br>
 * 2.��ѹ��������Ӧ�Ľ�ѹ������(unZip)<br>
 * 3.��ӵ����ļ���ѹ��������zipFile����<br>
 * 4.��Ӷ���ļ���ѹ��������zipFiles����<br>
 * 5.�����ɺ����saveZip�������Խ�ѹ�������浽��Ӧλ��<br>
 * <p>
 * <a href="doc-files/zip���߽���.rar">ZIP���ʹ��ʾ������[zip���߽���.rar]</a>
 * 
 * <pre>
 * 
 * public class Test implements Serializable {
 * 
 * 	private static final long serialVersionUID = -7097778930679492434L;
 * 
 * 	public static void main(String[] args) throws Exception {
 * 		String encode = &quot;GBK&quot;;
 * 		// Zip
 * 		if (1 == 1) {
 * 			//
 * 			FileTools.rmDir(new File(&quot;./temp/zip/001.zip&quot;));
 * 			FileTools.rmDir(new File(&quot;./temp/zip/002.zip&quot;));
 * 			//
 * 			ZipFile zipFile = new ZipFile(encode);
 * 			// ѹ�������ݵ�ָ���ļ�.
 * 			zipFile.zipFile(&quot;���&quot;.getBytes(), &quot;A/B/1.txt&quot;);
 * 			zipFile.zipFile(&quot;�ĤŤ�&quot;.getBytes(), &quot;1.txt&quot;);
 * 
 * 			// ѹ�����ļ���File����ָ���ļ�.
 * 			zipFile.zipFile(new File(&quot;./temp/zip/mms/1.jpg&quot;), &quot;mms/001/1-ͼƬ.jpg&quot;);
 * 			zipFile.zipFile(new File(&quot;./temp/zip/mms/1.txt&quot;), &quot;mms/001/1-�ı�.txt&quot;);
 * 
 * 			// ѹ�����ļ����ļ�������ָ���ļ�.
 * 			zipFile.zipFile(&quot;./temp/zip/mms/2.jpg&quot;, &quot;mms/001/2-ͼƬ.jpg&quot;);
 * 			zipFile.zipFile(&quot;./temp/zip/mms/2.txt&quot;, &quot;mms/001/2-�ı�.txt&quot;);
 * 
 * 			// ѹ����InputStream��ָ���ļ�.
 * 			zipFile.zipFile(new FileInputStream(&quot;./temp/zip/mms/3.jpg&quot;), &quot;mms/001/3-ͼƬ.jpg&quot;);
 * 			zipFile.zipFile(new FileInputStream(&quot;./temp/zip/mms/3.txt&quot;), &quot;mms/001/3-�ı�.txt&quot;);
 * 
 * 			// ѹ�����ļ���File����ָ���ļ�.
 * 			zipFile.zipFile(new File(&quot;./temp/zip/mms/1.jpg&quot;), &quot;mms/002/1-ͼƬ.jpg&quot;);
 * 			zipFile.zipFile(new File(&quot;./temp/zip/mms/1.txt&quot;), &quot;mms/002&quot;, &quot;1-�ı�.txt&quot;);
 * 
 * 			// ѹ�����ļ����ļ�������ָ���ļ�.
 * 			zipFile.zipFile(&quot;./temp/zip/mms/2.jpg&quot;, &quot;mms/002/2-a.jpg&quot;);
 * 			zipFile.zipFile(&quot;./temp/zip/mms/2.txt&quot;, &quot;mms/002&quot;, &quot;2-a.txt&quot;);
 * 
 * 			// ѹ����InputStream��ָ���ļ�.
 * 			zipFile.zipFile(new FileInputStream(&quot;./temp/zip/mms/3.jpg&quot;), &quot;mms/002/3-a.jpg&quot;);
 * 			zipFile.zipFile(new FileInputStream(&quot;./temp/zip/mms/3.txt&quot;), &quot;mms/002&quot;, &quot;3-a.txt&quot;);
 * 
 * 			//
 * 			zipFile.zipFiles(new File(&quot;./temp/zip/mms&quot;), &quot;mms/003&quot;);
 * 			//
 * 			zipFile.zipFiles(&quot;./temp/zip/mms&quot;, &quot;mms/004&quot;);
 * 			//
 * 			zipFile.zipFiles(new File(&quot;./temp/zip/mms&quot;), &quot;(.*txt)|(4.*)&quot;, &quot;mms/005&quot;);
 * 			//
 * 			zipFile.zipFiles(&quot;./temp/zip/mms&quot;, &quot;jpg&quot;, &quot;mms/006&quot;);
 * 			//
 * 			zipFile.createDir(&quot;mms/007/temp&quot;);
 * 			//��ȡ�ļ����б�.
 * 			{
 * 				System.out.println(&quot;====================================&quot;);
 * 				List&lt;String&gt; filenameList = zipFile.getFileList();
 * 				Collections.sort(filenameList);
 * 				for (String filename : filenameList) {
 * 					System.out.println(filename);
 * 				}
 * 			}
 * 			//��ȡ�ļ����б�����.
 * 			{
 * 				System.out.println(&quot;====================================&quot;);
 * 				List&lt;String&gt; filenameList = zipFile.getFileList(&quot;(ͼƬ)|(txt)&quot;);
 * 				Collections.sort(filenameList);
 * 				for (String filename : filenameList) {
 * 					System.out.println(filename);
 * 				}
 * 			}
 * 			// ����Zip�ļ����ļ�(Filename).
 * 			zipFile.saveZip(&quot;./temp/zip/001.zip&quot;);
 * 			// ����Zip�ļ����ļ�(File).
 * 			zipFile.saveZip(new File(&quot;./temp/zip/001.zip&quot;));
 * 			// ����Zip�ļ�����.
 * 			zipFile.saveZip(new FileOutputStream(&quot;./temp/zip/001.zip&quot;));
 * 			// ����Zip�ļ����ֽ�����.
 * 			zipFile.saveZip();
 * 		}
 * 		// Unzip
 * 		if (1 == 1) {
 * 			ZipFile zipFile = new ZipFile(new File(&quot;./temp/zip/001.zip&quot;), encode);
 * 			//
 * 			{
 * 				System.out.println(&quot;====================================&quot;);
 * 				List&lt;String&gt; filenameList = zipFile.getFileList();
 * 				Collections.sort(filenameList);
 * 				for (String filename : filenameList) {
 * 					System.out.println(filename);
 * 				}
 * 			}
 * 			//
 * 			FileTools.rmDir(new File(&quot;./temp/zip/out&quot;));
 * 			//
 * 			{
 * 				System.out.println(&quot;====================================&quot;);
 * 				Map&lt;String, byte[]&gt; map = zipFile.unzipAllFileToByteArray();
 * 				String[] filenames = map.keySet().toArray(new String[0]);
 * 				Arrays.sort(filenames);
 * 				for (String filename : filenames) {
 * 					System.out.println(filename + &quot;\t&quot; + map.get(filename).length);
 * 				}
 * 			}
 * 			// ��ѹָ���ļ�.
 * 			zipFile.unzipFileAs(&quot;A/B/1.txt&quot;, &quot;./temp/zip/out/�ı�.txt&quot;);
 * 			// ��ѹ�����ļ���ָ��Ŀ¼.
 * 			zipFile.unzipFilesToDir(&quot;./temp/zip/out/000&quot;);
 * 			// ��ѹָ���ļ���ָ��Ŀ¼.
 * 			zipFile.unzipFileToDir(&quot;mms/004/1.jpg&quot;, &quot;./temp/zip/out/4&quot;);
 * 			// ��ѹ��Ŀ¼�µ��ļ���ָ��Ŀ¼��.
 * 			zipFile.unzipFilesToDir(&quot;mms/003&quot;, &quot;./temp/zip/out/3&quot;);
 * 			// ��ѹ��Ŀ¼�µģ�������������ļ���ָ��Ŀ¼��.
 * 			zipFile.unzipFilesToDir(&quot;mms/003&quot;, &quot;jpg|txt&quot;, &quot;./temp/zip/out/4&quot;);
 * 			// ѹ��Ŀ¼���ļ�.
 * 			ZipFileUtil.zip(&quot;./temp/zip/002.zip&quot;, &quot;./temp/zip/out/3&quot;, &quot;UTF-8&quot;);
 * 			// ��ѹ��Zip.
 * 			ZipFileUtil.unzip(&quot;./temp/zip/002.zip&quot;, &quot;./temp/zip/out/4&quot;, &quot;UTF-8&quot;);
 * 		}
 * 		if (1 == 2) {
 * 			byte[] data = FileTools.read(&quot;./temp/zip/001.zip&quot;);
 * 			long timemark = System.currentTimeMillis();
 * 			int count = 0;
 * 			while (true) {
 * 				ZipFile zipFile = new ZipFile(data);
 * 				zipFile.unzipAllFileToByteArray();
 * 				if (Math.abs(System.currentTimeMillis() - timemark) &gt;= 1000)
 * 					break;
 * 				count++;
 * 			}
 * 			System.out.println(count);
 * 		}
 * 		if (1 == 1) {
 * 			byte[] data = FileTools.read(&quot;./temp/zip/001.zip&quot;);
 * 			ZipFile zipFile = new ZipFile(data);
 * 			long timemark = System.currentTimeMillis();
 * 			int count = 0;
 * 			while (true) {
 * 				zipFile.saveZip(&quot;./temp/zip/001.zip&quot;);
 * 				if (Math.abs(System.currentTimeMillis() - timemark) &gt;= 1000)
 * 					break;
 * 				count++;
 * 			}
 * 			System.out.println(count);
 * 		}
 * 	}
 * }
 * 
 * </pre>
 * 
 * Copyright: Copyright (c) 2008-12-23
 * <p>
 * Company: 
 * <p>
 * Author: gaowei
 * <p>
 * Version: 1.0
 */
public class ZipFile {

	/**
	 * ������󵥸��ļ���С��Ĭ��Ϊ2��
	 * 
	 * @param length
	 *            �����ļ���С����λ��
	 */
	public static void setMaxSingleFileLength(long length) {
		CodeDef.MAX_FILE_LENGTH = length * 1024 * 1024L;
	}

	/**
	 * ���ȫ���ļ���С��Ĭ��Ϊ10��
	 * 
	 * @param length
	 *            ȫ���ļ���С����λ��
	 */
	public static void setMaxTotleFileLength(long length) {
		CodeDef.MAX_TOTAL_FILE_LENGTH = length * 1024 * 1024L;
	}

	/** ������. */
	private byte[] buffer = new byte[1024 * 10];

	/** �����ڴ�ѹ���ļ�. */
	private VirtualZipFile ramZipFile;

	/** �ֽ������. */
	private ByteArrayOutputStream tempOutputStream = new ByteArrayOutputStream();

	/**
	 * ���������½�ѹ���ļ�ʱ�ã�.
	 */
	public ZipFile() {
		ramZipFile = new VirtualZipFile();
	}

	/**
	 * ������.
	 * 
	 * @param b
	 *            ѹ���ļ�������ȫ������
	 * @throws Exception
	 *             �쳣
	 */
	public ZipFile(byte[] b) throws Exception {
		ramZipFile = new VirtualZipFile(b);
	}

	/**
	 * ������.
	 * 
	 * @param b
	 *            ѹ���ļ�������ȫ������
	 * @param encoding
	 *            �����ʽ
	 * @throws Exception
	 *             �쳣
	 */
	public ZipFile(byte[] b, String encoding) throws Exception {
		ramZipFile = new VirtualZipFile(b, encoding);
	}

	/**
	 * ������.
	 * 
	 * @param zipFile
	 *            ѹ���ļ�
	 * @throws Exception
	 *             �쳣
	 */
	public ZipFile(File zipFile) throws Exception {
		ramZipFile = new VirtualZipFile(zipFile);
	}

	/**
	 * ������.
	 * 
	 * @param zipFile
	 *            ѹ���ļ�
	 * @param encoding
	 *            �����ʽ
	 * @throws Exception
	 *             �쳣
	 */
	public ZipFile(File zipFile, String encoding) throws Exception {
		ramZipFile = new VirtualZipFile(zipFile, encoding);
	}

	/**
	 * ������.
	 * 
	 * @param in
	 *            ѹ���ļ���������
	 * @throws Exception
	 *             �쳣
	 */
	public ZipFile(InputStream in) throws Exception {
		ramZipFile = new VirtualZipFile(in);
	}

	/**
	 * ������.
	 * 
	 * @param in
	 *            ѹ���ļ���������
	 * @param encoding
	 *            �����ʽ
	 * @throws Exception
	 *             �쳣
	 */
	public ZipFile(InputStream in, String encoding) throws Exception {
		ramZipFile = new VirtualZipFile(in, encoding);
	}

	/**
	 * ���������½�ѹ���ļ�ʱ�ã�.
	 * 
	 * @param encoding
	 *            �����ʽ
	 */
	public ZipFile(String encoding) {
		ramZipFile = new VirtualZipFile(encoding);
	}

	/**
	 * ��ZIP�д���һ����Ŀ¼.
	 * 
	 * @param dirName
	 *            Ŀ¼����
	 */
	public void createDir(String dirName) throws Exception {
		ramZipFile.addFileToZip(null, dirName);
	}

	/**
	 * ��ȡ��ǰ�ı���.
	 * 
	 * @return ����
	 */
	public String getEncoding() {
		return ramZipFile.getEncoding();
	}

	/**
	 * ��ȡ�ļ��б�.
	 * 
	 * @return �ļ��б���"/"��β��ʾĿ¼�����б���̰߳�ȫ
	 */
	public List<String> getFileList() {
		return ramZipFile.getFileList(null);
	}

	/**
	 * ��ȡ�ļ��б�
	 * 
	 * @param filterRegex
	 *            ������ļ�������Ϊ����
	 * @return �ļ��б���"/"��β��ʾĿ¼�����б���̰߳�ȫ
	 */
	public List<String> getFileList(String filterRegex) {
		return ramZipFile.getFileList(filterRegex);
	}

	/**
	 * ��Ŀ¼�����ļ�����ȡ������·��.
	 * 
	 * @param zipDirName
	 *            ZIPĿ¼��
	 * @param zipFileName
	 *            ZIP�ļ���
	 * @return ZIP���ļ�·��
	 * @throws Exception
	 *             �쳣
	 */
	private String getZipPath(String zipDirName, String zipFileName) throws Exception {
		if (zipDirName == null) {
			zipDirName = "";
		} else {
			if (!zipDirName.endsWith("/")) {
				zipDirName = zipDirName + "/";
			}
		}
		if (zipFileName == null || zipFileName.trim().length() == 0) {
			throw new Exception("�ļ�������Ϊ��");
		}
		return zipDirName + zipFileName;
	}

	/**
	 * ����������ȡ����.
	 * 
	 * @param in
	 *            ������
	 * @return ����
	 * @throws Exception
	 *             �쳣
	 */
	private byte[] readByteFromStream(InputStream in) throws Exception {
		int n;
		synchronized (tempOutputStream) {
			tempOutputStream.reset();
			while ((n = in.read(buffer)) != -1) {
				tempOutputStream.write(buffer, 0, n);
			}

			return tempOutputStream.toByteArray();
		}
	}

	/**
	 * ����ZIP���ֽ�����
	 * 
	 * @return �ֽ�����
	 * @throws Exception
	 */
	public byte[] saveZip() throws Exception {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			ramZipFile.save(baos);
			return baos.toByteArray();
		}
		catch (Exception ex) {
			throw ex;
		}
		finally {
			baos.close();
		}
	}

	/**
	 * ����ZIP���ļ�.
	 * 
	 * @param file
	 *            �ļ�
	 * @throws Exception
	 *             �쳣
	 */
	public void saveZip(File file) throws Exception {
		if (file.isDirectory()) {
			throw new Exception("���ܽ�ѹ���ļ����浽Ŀ¼��" + file.getPath());
		}
		if (file.getParentFile() != null && !file.getParentFile().exists()) {
			if (!file.getParentFile().mkdirs()) {
				throw new Exception("����Ŀ¼ʧ�ܣ�" + file.getParent());
			}
		}
		FileOutputStream fos = new FileOutputStream(file);
		try {
			saveZip(fos);
		}
		catch (Exception e) {
			throw e;
		}
		finally {
			fos.close();
		}
	}

	/**
	 * ����ZIP�������.
	 * 
	 * @param outputStream
	 *            �����
	 * @throws Exception
	 *             �쳣
	 */
	public void saveZip(OutputStream outputStream) throws Exception {
		ramZipFile.save(outputStream);
	}

	/**
	 * ����ZIP���ļ�.
	 * 
	 * @param fileName
	 *            �ļ�·��
	 */
	public void saveZip(String fileName) throws Exception {
		if (!fileName.toLowerCase().endsWith(".zip")) {
			fileName = fileName + ".zip";
		}
		saveZip(new File(fileName));
	}

	/**
	 * ���ñ���.
	 * 
	 * @param encoding
	 *            ����
	 */
	public void setEncoding(String encoding) {
		ramZipFile.setEncoding(encoding);
	}

	/**
	 * ��ѹ�����ļ����ֽ�����
	 * 
	 * @return �ļ�·��(ZIP�е�)�����ݵ�ӳ�䣬��ӳ�䲻���̰߳�ȫ�ģ�ÿ���ñ�����һ�ξͻ����һ���µ�ӳ��
	 */
	public Map<String, byte[]> unzipAllFileToByteArray() {
		return ramZipFile.getAllFileInRam();
	}

	/**
	 * ��ѹָ���ļ�.
	 * 
	 * <pre>
	 * ��������:
	 * ZipFile zipFile = new ZipFile(new File(&quot;test/001.zip&quot;));
	 * zipFile.unzipFileAs(&quot;mms/001/1-�ı�.txt&quot;,&quot;mms/test/2.txt&quot;);
	 *  
	 * test/001.zip�����ļ�����:
	 * A/B/1.txt
	 * mms/003/56875.smil
	 * mms/003/4.txt
	 * mms/003/1.jpg
	 * mms/003/2.jpg
	 * mms/003/3.jpg
	 * mms/003/2.txt
	 * mms/003/4.jpg
	 * mms/003/3.txt
	 * mms/003/1.txt
	 * mms/001/3-�ı�.txt
	 * mms/001/1-�ı�.txt
	 * mms/001/1-ͼƬ.jpg
	 * mms/001/3-ͼƬ.jpg
	 * mms/001/2-�ı�.txt
	 * mms/001/2-ͼƬ.jpg
	 * mms/002/2-a.jpg
	 * mms/002/1-�ı�.txt
	 * mms/002/2-a.txt
	 * mms/002/1-ͼƬ.jpg
	 * mms/002/3-a.jpg
	 * mms/002/3-a.txt
	 * 
	 * ִ�к�mms/testĿ¼�����ļ�����:
	 * 2.txt
	 * </pre>
	 * 
	 * @param zipFileName
	 *            Ҫ��ѹ���ļ�
	 * @param localFileName
	 *            �����ļ�
	 * @throws Exception
	 *             �쳣
	 */
	public void unzipFileAs(String zipFileName, String localFileName) throws Exception {
		ramZipFile.unzipToDisk(zipFileName, null, localFileName);
	}

	/**
	 * ��ѹ�����ļ���Ŀ¼.
	 * 
	 * <pre>
	 * ��������:
	 * ZipFile zipFile = new ZipFile(new File(&quot;test/001.zip&quot;));
	 * zipFile.unzipFilesToDir(&quot;mms/test&quot;);
	 * 
	 * test/001.zip�����ļ�����:
	 * A/B/1.txt
	 * mms/003/56875.smil
	 * mms/003/4.txt
	 * mms/003/1.jpg
	 * mms/003/2.jpg
	 * mms/003/3.jpg
	 * mms/003/2.txt
	 * mms/003/4.jpg
	 * mms/003/3.txt
	 * mms/003/1.txt
	 * mms/001/3-�ı�.txt
	 * mms/001/1-�ı�.txt
	 * mms/001/1-ͼƬ.jpg
	 * mms/001/3-ͼƬ.jpg
	 * mms/001/2-�ı�.txt
	 * mms/001/2-ͼƬ.jpg
	 * mms/002/2-a.jpg
	 * mms/002/1-�ı�.txt
	 * mms/002/2-a.txt
	 * mms/002/1-ͼƬ.jpg
	 * mms/002/3-a.jpg
	 * mms/002/3-a.txt
	 * 
	 * ִ�к�mms/testĿ¼�����ļ�����:
	 * A/B/1.txt
	 * mms/003/56875.smil
	 * mms/003/4.txt
	 * mms/003/1.jpg
	 * mms/003/2.jpg
	 * mms/003/3.jpg
	 * mms/003/2.txt
	 * mms/003/4.jpg
	 * mms/003/3.txt
	 * mms/003/1.txt
	 * mms/001/3-�ı�.txt
	 * mms/001/1-�ı�.txt
	 * mms/001/1-ͼƬ.jpg
	 * mms/001/3-ͼƬ.jpg
	 * mms/001/2-�ı�.txt
	 * mms/001/2-ͼƬ.jpg
	 * mms/002/2-a.jpg
	 * mms/002/1-�ı�.txt
	 * mms/002/2-a.txt
	 * mms/002/1-ͼƬ.jpg
	 * mms/002/3-a.jpg
	 * mms/002/3-a.txt
	 * </pre>
	 * 
	 * @param localDirName
	 *            ����Ŀ¼
	 * @throws Exception
	 *             �쳣
	 */
	public void unzipFilesToDir(String localDirName) throws Exception {
		unzipFilesToDir(null, localDirName);
	}

	/**
	 * ��ѹ��Ŀ¼�µ��ļ���Ŀ¼.
	 * 
	 * <pre>
	 * ��������:
	 * ZipFile zipFile = new ZipFile(new File(&quot;test/001.zip&quot;));
	 * zipFile.unzipFilesToDir(&quot;mms/001&quot;,&quot;mms/test&quot;);
	 * 
	 * test/001.zip�����ļ�����:
	 * A/B/1.txt
	 * mms/003/56875.smil
	 * mms/003/4.txt
	 * mms/003/1.jpg
	 * mms/003/2.jpg
	 * mms/003/3.jpg
	 * mms/003/2.txt
	 * mms/003/4.jpg
	 * mms/003/3.txt
	 * mms/003/1.txt
	 * mms/001/3-�ı�.txt
	 * mms/001/1-�ı�.txt
	 * mms/001/1-ͼƬ.jpg
	 * mms/001/3-ͼƬ.jpg
	 * mms/001/2-�ı�.txt
	 * mms/001/2-ͼƬ.jpg
	 * mms/002/2-a.jpg
	 * mms/002/1-�ı�.txt
	 * mms/002/2-a.txt
	 * mms/002/1-ͼƬ.jpg
	 * mms/002/3-a.jpg
	 * mms/002/3-a.txt
	 * 
	 * ִ�к�mms/testĿ¼�����ļ�����:
	 * 3-�ı�.txt
	 * 1-�ı�.txt
	 * 1-ͼƬ.jpg
	 * 3-ͼƬ.jpg
	 * 2-�ı�.txt
	 * 2-ͼƬ.jpg
	 * </pre>
	 * 
	 * @param zipDirName
	 *            Ҫ��ѹ��ZIPĿ¼
	 * @param localDirName
	 *            ����Ŀ¼
	 * @throws Exception
	 *             �쳣
	 */
	public void unzipFilesToDir(String zipDirName, String localDirName) throws Exception {
		unzipFilesToDir(zipDirName, null, localDirName);
	}

	/**
	 * ��ѹ��Ŀ¼�µ��ļ���Ŀ¼��������.
	 * 
	 * <pre>
	 * ��������:
	 * ZipFile zipFile = new ZipFile(new File(&quot;test/001.zip&quot;));
	 * zipFile.unzipFilesToDir(&quot;mms/001&quot;,&quot;jpg&quot;,&quot;mms/test&quot;);
	 * 
	 * test/001.zip�����ļ�����:
	 * A/B/1.txt
	 * mms/003/56875.smil
	 * mms/003/4.txt
	 * mms/003/1.jpg
	 * mms/003/2.jpg
	 * mms/003/3.jpg
	 * mms/003/2.txt
	 * mms/003/4.jpg
	 * mms/003/3.txt
	 * mms/003/1.txt
	 * mms/001/3-�ı�.txt
	 * mms/001/1-�ı�.txt
	 * mms/001/1-ͼƬ.jpg
	 * mms/001/3-ͼƬ.jpg
	 * mms/001/2-�ı�.txt
	 * mms/001/2-ͼƬ.jpg
	 * mms/002/2-a.jpg
	 * mms/002/1-�ı�.txt
	 * mms/002/2-a.txt
	 * mms/002/1-ͼƬ.jpg
	 * mms/002/3-a.jpg
	 * mms/002/3-a.txt
	 * 
	 * ִ�к�mms/testĿ¼�����ļ�����:
	 * 1-ͼƬ.jpg
	 * 3-ͼƬ.jpg
	 * 2-ͼƬ.jpg
	 * </pre>
	 * 
	 * @param zipDirName
	 *            Ҫ��ѹ��ZIPĿ¼
	 * @param fileFilterRegex
	 *            ������ļ���������Ϊ����
	 * @param localDirName
	 *            ����Ŀ¼
	 * @throws Exception
	 *             �쳣
	 */
	public void unzipFilesToDir(String zipDirName, String fileFilterRegex, String localDirName) throws Exception {
		ramZipFile.unzipToDisk(zipDirName, fileFilterRegex, localDirName, null);
	}

	/**
	 * ��ѹָ���ļ���Ŀ¼.
	 * 
	 * <pre>
	 * ��������:
	 * ZipFile zipFile = new ZipFile(new File(&quot;test/001.zip&quot;));
	 * zipFile.unzipFileToDir(&quot;mms/001/2-�ı�.txt&quot;,&quot;mms/test&quot;);
	 * 
	 * test/001.zip�����ļ�����:
	 * A/B/1.txt
	 * mms/003/56875.smil
	 * mms/003/4.txt
	 * mms/003/1.jpg
	 * mms/003/2.jpg
	 * mms/003/3.jpg
	 * mms/003/2.txt
	 * mms/003/4.jpg
	 * mms/003/3.txt
	 * mms/003/1.txt
	 * mms/001/3-�ı�.txt
	 * mms/001/1-�ı�.txt
	 * mms/001/1-ͼƬ.jpg
	 * mms/001/3-ͼƬ.jpg
	 * mms/001/2-�ı�.txt
	 * mms/001/2-ͼƬ.jpg
	 * mms/002/2-a.jpg
	 * mms/002/1-�ı�.txt
	 * mms/002/2-a.txt
	 * mms/002/1-ͼƬ.jpg
	 * mms/002/3-a.jpg
	 * mms/002/3-a.txt
	 * 
	 * ִ�к�mms/testĿ¼�����ļ�����: 
	 * 2-�ı�.txt
	 * </pre>
	 * 
	 * @param zipFileName
	 *            Ҫ��ѹ���ļ�
	 * @param localDirName
	 *            ����Ŀ¼
	 * @throws Exception
	 *             �쳣
	 */
	public void unzipFileToDir(String zipFileName, String localDirName) throws Exception {
		ramZipFile.unzipToDisk(zipFileName, localDirName, null);
	}

	/**
	 * ѹ�����ݵ�ZIP·��.
	 * 
	 * <pre>
	 * ��������:
	 * ZipFile zipFile = new ZipFile();
	 * zipFile.zipFile(b, &quot;mms/002/1.jpg&quot;);
	 * zipFile.saveZip(&quot;test/test.zip&quot;);
	 * 
	 * ִ�к�test/test.zip�����ļ�����:
	 * mms/002/1.jpg
	 * </pre>
	 * 
	 * @param data
	 *            ����
	 * @param zipPath
	 *            ZIP��·��
	 * @throws Exception
	 *             �쳣
	 */
	public void zipFile(byte[] data, String zipPath) throws Exception {
		if (ramZipFile.getTotalFileLength() >= CodeDef.MAX_TOTAL_FILE_LENGTH) {
			throw new Exception("ѹ���ļ��ѹ��󣬵�ǰ��СΪ" + ramZipFile.getTotalFileLength() + "�ֽڣ�����������ļ�");
		}
		if (data.length > CodeDef.MAX_FILE_LENGTH) {
			throw new Exception("�ļ��������Ϊ" + CodeDef.MAX_FILE_LENGTH + "�ֽ�");
		}
		if (zipPath == null || zipPath.trim().length() == 0) {
			throw new Exception("ZIP·������Ϊ��");
		}
		ramZipFile.addFileToZip(data, zipPath);
	}

	/**
	 * ѹ�����ݵ�ZIP·��.
	 * 
	 * <pre>
	 * ��������:
	 * ZipFile zipFile = new ZipFile();
	 * zipFile.zipFile(b,&quot;mms/003&quot;,&quot;1.jpg&quot;);
	 * zipFile.saveZip(&quot;test/test.zip&quot;);
	 * 
	 * ִ�к�test/test.zip�����ļ�����:
	 * mms/003/1.jpg
	 * </pre>
	 * 
	 * @param data
	 *            ����
	 * @param zipDirName
	 *            ZIPĿ¼��
	 * @param zipFileName
	 *            ZIP�ļ���
	 * @throws Exception
	 *             �쳣
	 */
	public void zipFile(byte[] data, String zipDirName, String zipFileName) throws Exception {
		String zipPath = getZipPath(zipDirName, zipFileName);
		zipFile(data, zipPath);
	}

	/**
	 * ѹ���ļ���ѹ����ָ��·��.
	 * 
	 * <pre>
	 * �μ�{@link #zipFile(byte[], String)}
	 * </pre>
	 * 
	 * @param localFile
	 *            �����ļ�
	 * @param zipPath
	 *            ZIP��·��
	 * @throws Exception
	 *             �쳣
	 */
	public void zipFile(File localFile, String zipPath) throws Exception {
		if (ramZipFile.getTotalFileLength() >= CodeDef.MAX_TOTAL_FILE_LENGTH) {
			throw new Exception("ѹ���ļ��ѹ��󣬵�ǰ��СΪ" + ramZipFile.getTotalFileLength() + "�ֽڣ�����������ļ�");
		}
		if (!localFile.isFile()) {
			throw new FileNotFoundException(localFile.getAbsolutePath() + "������");
		}
		if (localFile.length() > CodeDef.MAX_FILE_LENGTH) {
			throw new Exception("�ļ��������Ϊ" + CodeDef.MAX_FILE_LENGTH + "�ֽ�");
		}
		FileInputStream fis = new FileInputStream(localFile);
		try {
			zipFile(fis, zipPath);
		}
		catch (Exception e) {
			throw e;
		}
		finally {
			fis.close();
		}
	}

	/**
	 * ѹ���ļ���ѹ����ָ��·��.
	 * 
	 * <pre>
	 * �μ�{@link #zipFile(byte[], String, String)}
	 * </pre>
	 * 
	 * @param localFile
	 *            �����ļ�
	 * @param zipDirName
	 *            ZIPĿ¼��
	 * @param zipFileName
	 *            ZIP�ļ���
	 * @throws Exception
	 *             �쳣
	 */
	public void zipFile(File localFile, String zipDirName, String zipFileName) throws Exception {
		String zipPath = getZipPath(zipDirName, zipFileName);
		zipFile(localFile, zipPath);
	}

	/**
	 * ѹ���������ݵ�ѹ����ָ��·��.
	 * 
	 * <pre>
	 * �μ�{@link #zipFile(byte[], String)}
	 * </pre>
	 * 
	 * @param inputStream
	 *            ������
	 * @param zipPath
	 *            ZIP��·��
	 * @throws Exception
	 *             �쳣
	 */
	public void zipFile(InputStream inputStream, String zipPath) throws Exception {
		if (ramZipFile.getTotalFileLength() >= CodeDef.MAX_TOTAL_FILE_LENGTH) {
			throw new Exception("ѹ���ļ��ѹ��󣬵�ǰ��СΪ" + ramZipFile.getTotalFileLength() + "�ֽڣ�����������ļ�");
		}
		if (inputStream.available() > CodeDef.MAX_FILE_LENGTH) {
			throw new Exception("�ļ��������Ϊ" + CodeDef.MAX_FILE_LENGTH + "�ֽ�");
		}
		byte[] b = readByteFromStream(inputStream);
		zipFile(b, zipPath);
	}

	/**
	 * ѹ���������ݵ�ѹ����ָ��·��.
	 * 
	 * <pre>
	 * �μ�{@link #zipFile(byte[], String, String)}
	 * </pre>
	 * 
	 * @param inputStream
	 *            ������
	 * @param zipDirName
	 *            ZIPĿ¼
	 * @param zipFileName
	 *            ZIP�ļ���
	 * @throws Exception
	 *             �쳣
	 */
	public void zipFile(InputStream inputStream, String zipDirName, String zipFileName) throws Exception {
		String zipPath = getZipPath(zipDirName, zipFileName);
		zipFile(inputStream, zipPath);
	}

	/**
	 * ѹ���ļ���ѹ����ָ��·��.
	 * 
	 * <pre>
	 * �μ�{@link #zipFile(File, String)}
	 * </pre>
	 * 
	 * @param localFileName
	 *            �����ļ�·��
	 * @param zipPath
	 *            ZIP��·��
	 * @throws Exception
	 *             �쳣
	 */
	public void zipFile(String localFileName, String zipPath) throws Exception {
		File file = new File(localFileName);
		zipFile(file, zipPath);
	}

	/**
	 * ѹ���ļ���ѹ����ָ��·��.
	 * 
	 * <pre>
	 * �μ�{@link #zipFile(File, String, String)}
	 * </pre>
	 * 
	 * @param localFileName
	 *            �����ļ�·��
	 * @param zipDirName
	 *            ZIPĿ¼
	 * @param zipFileName
	 *            ZIP�ļ���
	 * @throws Exception
	 *             �쳣
	 */
	public void zipFile(String localFileName, String zipDirName, String zipFileName) throws Exception {
		String zipPath = getZipPath(zipDirName, zipFileName);
		zipFile(localFileName, zipPath);
	}

	/**
	 * ѹ��Ŀ¼�µ������ļ���ָ��ZIPĿ¼.
	 * 
	 * <pre>
	 * ��������:
	 * ZipFile zipFile = new ZipFile(); 
	 * File dir = new File(&quot;mms/001&quot;); 
	 * zipFile.zipFiles(dir, &quot;001&quot;);
	 * zipFile.saveZip(&quot;test/test.zip&quot;);
	 *  
	 * mms/001Ŀ¼�����ļ����£�
	 * mms/001/3-�ı�.txt
	 * mms/001/1-�ı�.txt
	 * mms/001/1-ͼƬ.jpg
	 * mms/001/3-ͼƬ.jpg
	 * mms/001/2-�ı�.txt
	 * mms/001/2-ͼƬ.jpg
	 * 
	 * ִ�к�test/test.zip�����ļ����£�
	 * 001/3-�ı�.txt
	 * 001/1-�ı�.txt
	 * 001/1-ͼƬ.jpg
	 * 001/3-ͼƬ.jpg
	 * 001/2-�ı�.txt
	 * 001/2-ͼƬ.jpg
	 * </pre>
	 * 
	 * @param localDir
	 *            Ҫѹ���ļ�����Ŀ¼
	 * @param zipDirName
	 *            ZIPĿ¼��������ʱ�ᴴ����
	 */
	public void zipFiles(File localDir, String zipDirName) throws Exception {
		zipFiles(localDir, null, zipDirName);
	}

	/**
	 * ѹ��Ŀ¼�µ�ָ���ļ���ָ��ZIPĿ¼.
	 * 
	 * <pre>
	 * �μ�{@link #zipFiles(File, String)}��������ֻѹ������������ļ�
	 * </pre>
	 * 
	 * @param localDir
	 *            Ҫѹ���ļ�����Ŀ¼
	 * @param fileFilterRegex
	 *            ������ļ�������
	 * @param zipDirName
	 *            ZIPĿ¼��������ʱ�ᴴ����
	 */
	public void zipFiles(File localDir, String fileFilterRegex, String zipDirName) throws Exception {
		if (!localDir.isDirectory()) {
			throw new FileNotFoundException(localDir.getAbsolutePath() + "Ŀ¼������");
		}
		if (ramZipFile.getTotalFileLength() >= CodeDef.MAX_TOTAL_FILE_LENGTH) {
			throw new Exception("ѹ���ļ��ѹ��󣬵�ǰ��СΪ" + ramZipFile.getTotalFileLength() + "�ֽڣ�����������ļ�");
		}
		if (zipDirName != null) {
			zipDirName = zipDirName.replaceAll("^/*", "").trim();
		}
		if (zipDirName != null && zipDirName.length() == 0) {
			zipDirName = null;
		}
		if (zipDirName != null && !zipDirName.endsWith("/")) {
			zipDirName = zipDirName + "/";
		}
		if (zipDirName != null) {
			ramZipFile.addFileToZip(null, zipDirName);
		} else {
			zipDirName = "";
		}
		Pattern pattern = null;
		Matcher matcher = null;
		if (fileFilterRegex != null) {
			pattern = Pattern.compile(fileFilterRegex);
		}
		for (File file : localDir.listFiles()) {
			if (file.isFile()) {
				if (pattern != null) {
					matcher = pattern.matcher(file.getPath());
					if (!matcher.find()) {
						continue;
					}
				}
				zipFile(file, zipDirName + file.getName());
			} else {
				zipFiles(file, fileFilterRegex, zipDirName + file.getName());
			}
		}
	}

	/**
	 * ѹ��Ŀ¼�µ������ļ���ָ��ZIPĿ¼.
	 * 
	 * <pre>
	 * �μ�{@link #zipFiles(File, String)}
	 * </pre>
	 * 
	 * @param localDirName
	 *            ����Ŀ¼��
	 * @param zipDirName
	 *            ZIPĿ¼���������ڻᴴ��
	 */
	public void zipFiles(String localDirName, String zipDirName) throws Exception {
		zipFiles(localDirName, null, zipDirName);
	}

	/**
	 * ѹ��Ŀ¼�µ�ָ���ļ���ָ��ZIPĿ¼.
	 * 
	 * <pre>
	 * �μ�{@link #zipFiles(File, String, String)}
	 * </pre>
	 * 
	 * @param localDirName
	 *            ����Ŀ¼��
	 * @param fileFilterRegex
	 *            ������ļ�������
	 * @param zipDirName
	 *            ZIPĿ¼���������ڻᴴ��
	 */
	public void zipFiles(String localDirName, String fileFilterRegex, String zipDirName) throws Exception {
		File file = new File(localDirName);
		zipFiles(file, fileFilterRegex, zipDirName);
	}
}
