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
 * 压缩文件. <br>
 * 包含创建和解压压缩文件的各种方法:<br>
 * <br>
 * 1.根据需要使用构造函数创建实例<br>
 * 2.解压缩调用相应的解压缩方法(unZip)<br>
 * 3.添加单个文件到压缩包调用zipFile方法<br>
 * 4.添加多个文件到压缩包调用zipFiles方法<br>
 * 5.添加完成后调用saveZip方法可以将压缩包保存到相应位置<br>
 * <p>
 * <a href="doc-files/zip工具介绍.rar">ZIP组件使用示例下载[zip工具介绍.rar]</a>
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
 * 			// 压缩：内容到指定文件.
 * 			zipFile.zipFile(&quot;你好&quot;.getBytes(), &quot;A/B/1.txt&quot;);
 * 			zipFile.zipFile(&quot;つづく&quot;.getBytes(), &quot;1.txt&quot;);
 * 
 * 			// 压缩：文件（File）到指定文件.
 * 			zipFile.zipFile(new File(&quot;./temp/zip/mms/1.jpg&quot;), &quot;mms/001/1-图片.jpg&quot;);
 * 			zipFile.zipFile(new File(&quot;./temp/zip/mms/1.txt&quot;), &quot;mms/001/1-文本.txt&quot;);
 * 
 * 			// 压缩：文件（文件名）到指定文件.
 * 			zipFile.zipFile(&quot;./temp/zip/mms/2.jpg&quot;, &quot;mms/001/2-图片.jpg&quot;);
 * 			zipFile.zipFile(&quot;./temp/zip/mms/2.txt&quot;, &quot;mms/001/2-文本.txt&quot;);
 * 
 * 			// 压缩：InputStream到指定文件.
 * 			zipFile.zipFile(new FileInputStream(&quot;./temp/zip/mms/3.jpg&quot;), &quot;mms/001/3-图片.jpg&quot;);
 * 			zipFile.zipFile(new FileInputStream(&quot;./temp/zip/mms/3.txt&quot;), &quot;mms/001/3-文本.txt&quot;);
 * 
 * 			// 压缩：文件（File）到指定文件.
 * 			zipFile.zipFile(new File(&quot;./temp/zip/mms/1.jpg&quot;), &quot;mms/002/1-图片.jpg&quot;);
 * 			zipFile.zipFile(new File(&quot;./temp/zip/mms/1.txt&quot;), &quot;mms/002&quot;, &quot;1-文本.txt&quot;);
 * 
 * 			// 压缩：文件（文件名）到指定文件.
 * 			zipFile.zipFile(&quot;./temp/zip/mms/2.jpg&quot;, &quot;mms/002/2-a.jpg&quot;);
 * 			zipFile.zipFile(&quot;./temp/zip/mms/2.txt&quot;, &quot;mms/002&quot;, &quot;2-a.txt&quot;);
 * 
 * 			// 压缩：InputStream到指定文件.
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
 * 			//获取文件名列表.
 * 			{
 * 				System.out.println(&quot;====================================&quot;);
 * 				List&lt;String&gt; filenameList = zipFile.getFileList();
 * 				Collections.sort(filenameList);
 * 				for (String filename : filenameList) {
 * 					System.out.println(filename);
 * 				}
 * 			}
 * 			//获取文件名列表（正则）.
 * 			{
 * 				System.out.println(&quot;====================================&quot;);
 * 				List&lt;String&gt; filenameList = zipFile.getFileList(&quot;(图片)|(txt)&quot;);
 * 				Collections.sort(filenameList);
 * 				for (String filename : filenameList) {
 * 					System.out.println(filename);
 * 				}
 * 			}
 * 			// 保存Zip文件到文件(Filename).
 * 			zipFile.saveZip(&quot;./temp/zip/001.zip&quot;);
 * 			// 保存Zip文件到文件(File).
 * 			zipFile.saveZip(new File(&quot;./temp/zip/001.zip&quot;));
 * 			// 保存Zip文件到流.
 * 			zipFile.saveZip(new FileOutputStream(&quot;./temp/zip/001.zip&quot;));
 * 			// 保存Zip文件到字节数组.
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
 * 			// 解压指定文件.
 * 			zipFile.unzipFileAs(&quot;A/B/1.txt&quot;, &quot;./temp/zip/out/文本.txt&quot;);
 * 			// 解压所有文件到指定目录.
 * 			zipFile.unzipFilesToDir(&quot;./temp/zip/out/000&quot;);
 * 			// 解压指定文件到指定目录.
 * 			zipFile.unzipFileToDir(&quot;mms/004/1.jpg&quot;, &quot;./temp/zip/out/4&quot;);
 * 			// 解压缩目录下的文件到指定目录下.
 * 			zipFile.unzipFilesToDir(&quot;mms/003&quot;, &quot;./temp/zip/out/3&quot;);
 * 			// 解压缩目录下的，符合用正则的文件到指定目录下.
 * 			zipFile.unzipFilesToDir(&quot;mms/003&quot;, &quot;jpg|txt&quot;, &quot;./temp/zip/out/4&quot;);
 * 			// 压缩目录、文件.
 * 			ZipFileUtil.zip(&quot;./temp/zip/002.zip&quot;, &quot;./temp/zip/out/3&quot;, &quot;UTF-8&quot;);
 * 			// 解压缩Zip.
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
	 * 设置最大单个文件大小，默认为2兆
	 * 
	 * @param length
	 *            单个文件大小，单位兆
	 */
	public static void setMaxSingleFileLength(long length) {
		CodeDef.MAX_FILE_LENGTH = length * 1024 * 1024L;
	}

	/**
	 * 最大全部文件大小，默认为10兆
	 * 
	 * @param length
	 *            全部文件大小，单位兆
	 */
	public static void setMaxTotleFileLength(long length) {
		CodeDef.MAX_TOTAL_FILE_LENGTH = length * 1024 * 1024L;
	}

	/** 缓冲区. */
	private byte[] buffer = new byte[1024 * 10];

	/** 虚拟内存压缩文件. */
	private VirtualZipFile ramZipFile;

	/** 字节输出流. */
	private ByteArrayOutputStream tempOutputStream = new ByteArrayOutputStream();

	/**
	 * 构造器（新建压缩文件时用）.
	 */
	public ZipFile() {
		ramZipFile = new VirtualZipFile();
	}

	/**
	 * 构造器.
	 * 
	 * @param b
	 *            压缩文件包含的全部数据
	 * @throws Exception
	 *             异常
	 */
	public ZipFile(byte[] b) throws Exception {
		ramZipFile = new VirtualZipFile(b);
	}

	/**
	 * 构造器.
	 * 
	 * @param b
	 *            压缩文件包含的全部数据
	 * @param encoding
	 *            编码格式
	 * @throws Exception
	 *             异常
	 */
	public ZipFile(byte[] b, String encoding) throws Exception {
		ramZipFile = new VirtualZipFile(b, encoding);
	}

	/**
	 * 构造器.
	 * 
	 * @param zipFile
	 *            压缩文件
	 * @throws Exception
	 *             异常
	 */
	public ZipFile(File zipFile) throws Exception {
		ramZipFile = new VirtualZipFile(zipFile);
	}

	/**
	 * 构造器.
	 * 
	 * @param zipFile
	 *            压缩文件
	 * @param encoding
	 *            编码格式
	 * @throws Exception
	 *             异常
	 */
	public ZipFile(File zipFile, String encoding) throws Exception {
		ramZipFile = new VirtualZipFile(zipFile, encoding);
	}

	/**
	 * 构造器.
	 * 
	 * @param in
	 *            压缩文件的输入流
	 * @throws Exception
	 *             异常
	 */
	public ZipFile(InputStream in) throws Exception {
		ramZipFile = new VirtualZipFile(in);
	}

	/**
	 * 构造器.
	 * 
	 * @param in
	 *            压缩文件的输入流
	 * @param encoding
	 *            编码格式
	 * @throws Exception
	 *             异常
	 */
	public ZipFile(InputStream in, String encoding) throws Exception {
		ramZipFile = new VirtualZipFile(in, encoding);
	}

	/**
	 * 构造器（新建压缩文件时用）.
	 * 
	 * @param encoding
	 *            编码格式
	 */
	public ZipFile(String encoding) {
		ramZipFile = new VirtualZipFile(encoding);
	}

	/**
	 * 在ZIP中创建一个空目录.
	 * 
	 * @param dirName
	 *            目录名称
	 */
	public void createDir(String dirName) throws Exception {
		ramZipFile.addFileToZip(null, dirName);
	}

	/**
	 * 获取当前的编码.
	 * 
	 * @return 编码
	 */
	public String getEncoding() {
		return ramZipFile.getEncoding();
	}

	/**
	 * 获取文件列表.
	 * 
	 * @return 文件列表，以"/"结尾表示目录，该列表非线程安全
	 */
	public List<String> getFileList() {
		return ramZipFile.getFileList(null);
	}

	/**
	 * 获取文件列表
	 * 
	 * @param filterRegex
	 *            允许的文件，可以为正则
	 * @return 文件列表，以"/"结尾表示目录，该列表非线程安全
	 */
	public List<String> getFileList(String filterRegex) {
		return ramZipFile.getFileList(filterRegex);
	}

	/**
	 * 从目录名和文件名获取完整的路径.
	 * 
	 * @param zipDirName
	 *            ZIP目录名
	 * @param zipFileName
	 *            ZIP文件名
	 * @return ZIP内文件路径
	 * @throws Exception
	 *             异常
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
			throw new Exception("文件名不能为空");
		}
		return zipDirName + zipFileName;
	}

	/**
	 * 从输入流获取数据.
	 * 
	 * @param in
	 *            输入流
	 * @return 数据
	 * @throws Exception
	 *             异常
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
	 * 保存ZIP到字节数组
	 * 
	 * @return 字节数组
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
	 * 保存ZIP到文件.
	 * 
	 * @param file
	 *            文件
	 * @throws Exception
	 *             异常
	 */
	public void saveZip(File file) throws Exception {
		if (file.isDirectory()) {
			throw new Exception("不能将压缩文件保存到目录：" + file.getPath());
		}
		if (file.getParentFile() != null && !file.getParentFile().exists()) {
			if (!file.getParentFile().mkdirs()) {
				throw new Exception("创建目录失败：" + file.getParent());
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
	 * 保存ZIP到输出流.
	 * 
	 * @param outputStream
	 *            输出流
	 * @throws Exception
	 *             异常
	 */
	public void saveZip(OutputStream outputStream) throws Exception {
		ramZipFile.save(outputStream);
	}

	/**
	 * 保存ZIP到文件.
	 * 
	 * @param fileName
	 *            文件路径
	 */
	public void saveZip(String fileName) throws Exception {
		if (!fileName.toLowerCase().endsWith(".zip")) {
			fileName = fileName + ".zip";
		}
		saveZip(new File(fileName));
	}

	/**
	 * 设置编码.
	 * 
	 * @param encoding
	 *            编码
	 */
	public void setEncoding(String encoding) {
		ramZipFile.setEncoding(encoding);
	}

	/**
	 * 解压所有文件到字节数组
	 * 
	 * @return 文件路径(ZIP中的)和数据的映射，该映射不是线程安全的，每调用本方法一次就会产生一个新的映射
	 */
	public Map<String, byte[]> unzipAllFileToByteArray() {
		return ramZipFile.getAllFileInRam();
	}

	/**
	 * 解压指定文件.
	 * 
	 * <pre>
	 * 代码样例:
	 * ZipFile zipFile = new ZipFile(new File(&quot;test/001.zip&quot;));
	 * zipFile.unzipFileAs(&quot;mms/001/1-文本.txt&quot;,&quot;mms/test/2.txt&quot;);
	 *  
	 * test/001.zip包含文件如下:
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
	 * mms/001/3-文本.txt
	 * mms/001/1-文本.txt
	 * mms/001/1-图片.jpg
	 * mms/001/3-图片.jpg
	 * mms/001/2-文本.txt
	 * mms/001/2-图片.jpg
	 * mms/002/2-a.jpg
	 * mms/002/1-文本.txt
	 * mms/002/2-a.txt
	 * mms/002/1-图片.jpg
	 * mms/002/3-a.jpg
	 * mms/002/3-a.txt
	 * 
	 * 执行后mms/test目录包含文件如下:
	 * 2.txt
	 * </pre>
	 * 
	 * @param zipFileName
	 *            要解压的文件
	 * @param localFileName
	 *            本地文件
	 * @throws Exception
	 *             异常
	 */
	public void unzipFileAs(String zipFileName, String localFileName) throws Exception {
		ramZipFile.unzipToDisk(zipFileName, null, localFileName);
	}

	/**
	 * 解压所有文件到目录.
	 * 
	 * <pre>
	 * 代码样例:
	 * ZipFile zipFile = new ZipFile(new File(&quot;test/001.zip&quot;));
	 * zipFile.unzipFilesToDir(&quot;mms/test&quot;);
	 * 
	 * test/001.zip包含文件如下:
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
	 * mms/001/3-文本.txt
	 * mms/001/1-文本.txt
	 * mms/001/1-图片.jpg
	 * mms/001/3-图片.jpg
	 * mms/001/2-文本.txt
	 * mms/001/2-图片.jpg
	 * mms/002/2-a.jpg
	 * mms/002/1-文本.txt
	 * mms/002/2-a.txt
	 * mms/002/1-图片.jpg
	 * mms/002/3-a.jpg
	 * mms/002/3-a.txt
	 * 
	 * 执行后mms/test目录包含文件如下:
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
	 * mms/001/3-文本.txt
	 * mms/001/1-文本.txt
	 * mms/001/1-图片.jpg
	 * mms/001/3-图片.jpg
	 * mms/001/2-文本.txt
	 * mms/001/2-图片.jpg
	 * mms/002/2-a.jpg
	 * mms/002/1-文本.txt
	 * mms/002/2-a.txt
	 * mms/002/1-图片.jpg
	 * mms/002/3-a.jpg
	 * mms/002/3-a.txt
	 * </pre>
	 * 
	 * @param localDirName
	 *            本地目录
	 * @throws Exception
	 *             异常
	 */
	public void unzipFilesToDir(String localDirName) throws Exception {
		unzipFilesToDir(null, localDirName);
	}

	/**
	 * 解压缩目录下的文件到目录.
	 * 
	 * <pre>
	 * 代码样例:
	 * ZipFile zipFile = new ZipFile(new File(&quot;test/001.zip&quot;));
	 * zipFile.unzipFilesToDir(&quot;mms/001&quot;,&quot;mms/test&quot;);
	 * 
	 * test/001.zip包含文件如下:
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
	 * mms/001/3-文本.txt
	 * mms/001/1-文本.txt
	 * mms/001/1-图片.jpg
	 * mms/001/3-图片.jpg
	 * mms/001/2-文本.txt
	 * mms/001/2-图片.jpg
	 * mms/002/2-a.jpg
	 * mms/002/1-文本.txt
	 * mms/002/2-a.txt
	 * mms/002/1-图片.jpg
	 * mms/002/3-a.jpg
	 * mms/002/3-a.txt
	 * 
	 * 执行后mms/test目录包含文件如下:
	 * 3-文本.txt
	 * 1-文本.txt
	 * 1-图片.jpg
	 * 3-图片.jpg
	 * 2-文本.txt
	 * 2-图片.jpg
	 * </pre>
	 * 
	 * @param zipDirName
	 *            要解压的ZIP目录
	 * @param localDirName
	 *            本地目录
	 * @throws Exception
	 *             异常
	 */
	public void unzipFilesToDir(String zipDirName, String localDirName) throws Exception {
		unzipFilesToDir(zipDirName, null, localDirName);
	}

	/**
	 * 解压缩目录下的文件到目录（带正则）.
	 * 
	 * <pre>
	 * 代码样例:
	 * ZipFile zipFile = new ZipFile(new File(&quot;test/001.zip&quot;));
	 * zipFile.unzipFilesToDir(&quot;mms/001&quot;,&quot;jpg&quot;,&quot;mms/test&quot;);
	 * 
	 * test/001.zip包含文件如下:
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
	 * mms/001/3-文本.txt
	 * mms/001/1-文本.txt
	 * mms/001/1-图片.jpg
	 * mms/001/3-图片.jpg
	 * mms/001/2-文本.txt
	 * mms/001/2-图片.jpg
	 * mms/002/2-a.jpg
	 * mms/002/1-文本.txt
	 * mms/002/2-a.txt
	 * mms/002/1-图片.jpg
	 * mms/002/3-a.jpg
	 * mms/002/3-a.txt
	 * 
	 * 执行后mms/test目录包含文件如下:
	 * 1-图片.jpg
	 * 3-图片.jpg
	 * 2-图片.jpg
	 * </pre>
	 * 
	 * @param zipDirName
	 *            要解压的ZIP目录
	 * @param fileFilterRegex
	 *            允许的文件名（可以为正则）
	 * @param localDirName
	 *            本地目录
	 * @throws Exception
	 *             异常
	 */
	public void unzipFilesToDir(String zipDirName, String fileFilterRegex, String localDirName) throws Exception {
		ramZipFile.unzipToDisk(zipDirName, fileFilterRegex, localDirName, null);
	}

	/**
	 * 解压指定文件到目录.
	 * 
	 * <pre>
	 * 代码样例:
	 * ZipFile zipFile = new ZipFile(new File(&quot;test/001.zip&quot;));
	 * zipFile.unzipFileToDir(&quot;mms/001/2-文本.txt&quot;,&quot;mms/test&quot;);
	 * 
	 * test/001.zip包含文件如下:
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
	 * mms/001/3-文本.txt
	 * mms/001/1-文本.txt
	 * mms/001/1-图片.jpg
	 * mms/001/3-图片.jpg
	 * mms/001/2-文本.txt
	 * mms/001/2-图片.jpg
	 * mms/002/2-a.jpg
	 * mms/002/1-文本.txt
	 * mms/002/2-a.txt
	 * mms/002/1-图片.jpg
	 * mms/002/3-a.jpg
	 * mms/002/3-a.txt
	 * 
	 * 执行后mms/test目录包含文件如下: 
	 * 2-文本.txt
	 * </pre>
	 * 
	 * @param zipFileName
	 *            要解压的文件
	 * @param localDirName
	 *            本地目录
	 * @throws Exception
	 *             异常
	 */
	public void unzipFileToDir(String zipFileName, String localDirName) throws Exception {
		ramZipFile.unzipToDisk(zipFileName, localDirName, null);
	}

	/**
	 * 压缩数据到ZIP路径.
	 * 
	 * <pre>
	 * 代码样例:
	 * ZipFile zipFile = new ZipFile();
	 * zipFile.zipFile(b, &quot;mms/002/1.jpg&quot;);
	 * zipFile.saveZip(&quot;test/test.zip&quot;);
	 * 
	 * 执行后test/test.zip包含文件如下:
	 * mms/002/1.jpg
	 * </pre>
	 * 
	 * @param data
	 *            数据
	 * @param zipPath
	 *            ZIP中路径
	 * @throws Exception
	 *             异常
	 */
	public void zipFile(byte[] data, String zipPath) throws Exception {
		if (ramZipFile.getTotalFileLength() >= CodeDef.MAX_TOTAL_FILE_LENGTH) {
			throw new Exception("压缩文件已过大，当前大小为" + ramZipFile.getTotalFileLength() + "字节，不能再添加文件");
		}
		if (data.length > CodeDef.MAX_FILE_LENGTH) {
			throw new Exception("文件过大，最大为" + CodeDef.MAX_FILE_LENGTH + "字节");
		}
		if (zipPath == null || zipPath.trim().length() == 0) {
			throw new Exception("ZIP路径不能为空");
		}
		ramZipFile.addFileToZip(data, zipPath);
	}

	/**
	 * 压缩数据到ZIP路径.
	 * 
	 * <pre>
	 * 代码样例:
	 * ZipFile zipFile = new ZipFile();
	 * zipFile.zipFile(b,&quot;mms/003&quot;,&quot;1.jpg&quot;);
	 * zipFile.saveZip(&quot;test/test.zip&quot;);
	 * 
	 * 执行后test/test.zip包含文件如下:
	 * mms/003/1.jpg
	 * </pre>
	 * 
	 * @param data
	 *            数据
	 * @param zipDirName
	 *            ZIP目录名
	 * @param zipFileName
	 *            ZIP文件名
	 * @throws Exception
	 *             异常
	 */
	public void zipFile(byte[] data, String zipDirName, String zipFileName) throws Exception {
		String zipPath = getZipPath(zipDirName, zipFileName);
		zipFile(data, zipPath);
	}

	/**
	 * 压缩文件到压缩包指定路径.
	 * 
	 * <pre>
	 * 参见{@link #zipFile(byte[], String)}
	 * </pre>
	 * 
	 * @param localFile
	 *            本地文件
	 * @param zipPath
	 *            ZIP中路径
	 * @throws Exception
	 *             异常
	 */
	public void zipFile(File localFile, String zipPath) throws Exception {
		if (ramZipFile.getTotalFileLength() >= CodeDef.MAX_TOTAL_FILE_LENGTH) {
			throw new Exception("压缩文件已过大，当前大小为" + ramZipFile.getTotalFileLength() + "字节，不能再添加文件");
		}
		if (!localFile.isFile()) {
			throw new FileNotFoundException(localFile.getAbsolutePath() + "不存在");
		}
		if (localFile.length() > CodeDef.MAX_FILE_LENGTH) {
			throw new Exception("文件过大，最大为" + CodeDef.MAX_FILE_LENGTH + "字节");
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
	 * 压缩文件到压缩包指定路径.
	 * 
	 * <pre>
	 * 参见{@link #zipFile(byte[], String, String)}
	 * </pre>
	 * 
	 * @param localFile
	 *            本地文件
	 * @param zipDirName
	 *            ZIP目录名
	 * @param zipFileName
	 *            ZIP文件名
	 * @throws Exception
	 *             异常
	 */
	public void zipFile(File localFile, String zipDirName, String zipFileName) throws Exception {
		String zipPath = getZipPath(zipDirName, zipFileName);
		zipFile(localFile, zipPath);
	}

	/**
	 * 压缩流中数据到压缩包指定路径.
	 * 
	 * <pre>
	 * 参见{@link #zipFile(byte[], String)}
	 * </pre>
	 * 
	 * @param inputStream
	 *            输入流
	 * @param zipPath
	 *            ZIP内路径
	 * @throws Exception
	 *             异常
	 */
	public void zipFile(InputStream inputStream, String zipPath) throws Exception {
		if (ramZipFile.getTotalFileLength() >= CodeDef.MAX_TOTAL_FILE_LENGTH) {
			throw new Exception("压缩文件已过大，当前大小为" + ramZipFile.getTotalFileLength() + "字节，不能再添加文件");
		}
		if (inputStream.available() > CodeDef.MAX_FILE_LENGTH) {
			throw new Exception("文件过大，最大为" + CodeDef.MAX_FILE_LENGTH + "字节");
		}
		byte[] b = readByteFromStream(inputStream);
		zipFile(b, zipPath);
	}

	/**
	 * 压缩流中数据到压缩包指定路径.
	 * 
	 * <pre>
	 * 参见{@link #zipFile(byte[], String, String)}
	 * </pre>
	 * 
	 * @param inputStream
	 *            输入流
	 * @param zipDirName
	 *            ZIP目录
	 * @param zipFileName
	 *            ZIP文件名
	 * @throws Exception
	 *             异常
	 */
	public void zipFile(InputStream inputStream, String zipDirName, String zipFileName) throws Exception {
		String zipPath = getZipPath(zipDirName, zipFileName);
		zipFile(inputStream, zipPath);
	}

	/**
	 * 压缩文件到压缩包指定路径.
	 * 
	 * <pre>
	 * 参见{@link #zipFile(File, String)}
	 * </pre>
	 * 
	 * @param localFileName
	 *            本地文件路径
	 * @param zipPath
	 *            ZIP内路径
	 * @throws Exception
	 *             异常
	 */
	public void zipFile(String localFileName, String zipPath) throws Exception {
		File file = new File(localFileName);
		zipFile(file, zipPath);
	}

	/**
	 * 压缩文件到压缩包指定路径.
	 * 
	 * <pre>
	 * 参见{@link #zipFile(File, String, String)}
	 * </pre>
	 * 
	 * @param localFileName
	 *            本地文件路径
	 * @param zipDirName
	 *            ZIP目录
	 * @param zipFileName
	 *            ZIP文件名
	 * @throws Exception
	 *             异常
	 */
	public void zipFile(String localFileName, String zipDirName, String zipFileName) throws Exception {
		String zipPath = getZipPath(zipDirName, zipFileName);
		zipFile(localFileName, zipPath);
	}

	/**
	 * 压缩目录下的所有文件到指定ZIP目录.
	 * 
	 * <pre>
	 * 代码样例:
	 * ZipFile zipFile = new ZipFile(); 
	 * File dir = new File(&quot;mms/001&quot;); 
	 * zipFile.zipFiles(dir, &quot;001&quot;);
	 * zipFile.saveZip(&quot;test/test.zip&quot;);
	 *  
	 * mms/001目录包含文件如下：
	 * mms/001/3-文本.txt
	 * mms/001/1-文本.txt
	 * mms/001/1-图片.jpg
	 * mms/001/3-图片.jpg
	 * mms/001/2-文本.txt
	 * mms/001/2-图片.jpg
	 * 
	 * 执行后test/test.zip包含文件如下：
	 * 001/3-文本.txt
	 * 001/1-文本.txt
	 * 001/1-图片.jpg
	 * 001/3-图片.jpg
	 * 001/2-文本.txt
	 * 001/2-图片.jpg
	 * </pre>
	 * 
	 * @param localDir
	 *            要压缩文件所在目录
	 * @param zipDirName
	 *            ZIP目录（不存在时会创建）
	 */
	public void zipFiles(File localDir, String zipDirName) throws Exception {
		zipFiles(localDir, null, zipDirName);
	}

	/**
	 * 压缩目录下的指定文件到指定ZIP目录.
	 * 
	 * <pre>
	 * 参见{@link #zipFiles(File, String)}，本方法只压缩符合正则的文件
	 * </pre>
	 * 
	 * @param localDir
	 *            要压缩文件所在目录
	 * @param fileFilterRegex
	 *            允许的文件的正则
	 * @param zipDirName
	 *            ZIP目录（不存在时会创建）
	 */
	public void zipFiles(File localDir, String fileFilterRegex, String zipDirName) throws Exception {
		if (!localDir.isDirectory()) {
			throw new FileNotFoundException(localDir.getAbsolutePath() + "目录不存在");
		}
		if (ramZipFile.getTotalFileLength() >= CodeDef.MAX_TOTAL_FILE_LENGTH) {
			throw new Exception("压缩文件已过大，当前大小为" + ramZipFile.getTotalFileLength() + "字节，不能再添加文件");
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
	 * 压缩目录下的所有文件到指定ZIP目录.
	 * 
	 * <pre>
	 * 参见{@link #zipFiles(File, String)}
	 * </pre>
	 * 
	 * @param localDirName
	 *            本地目录名
	 * @param zipDirName
	 *            ZIP目录名，不存在会创建
	 */
	public void zipFiles(String localDirName, String zipDirName) throws Exception {
		zipFiles(localDirName, null, zipDirName);
	}

	/**
	 * 压缩目录下的指定文件到指定ZIP目录.
	 * 
	 * <pre>
	 * 参见{@link #zipFiles(File, String, String)}
	 * </pre>
	 * 
	 * @param localDirName
	 *            本地目录名
	 * @param fileFilterRegex
	 *            允许的文件的正则
	 * @param zipDirName
	 *            ZIP目录名，不存在会创建
	 */
	public void zipFiles(String localDirName, String fileFilterRegex, String zipDirName) throws Exception {
		File file = new File(localDirName);
		zipFiles(file, fileFilterRegex, zipDirName);
	}
}
