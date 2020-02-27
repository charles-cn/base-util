/**
 * 
 */
package com.haocom.util.zip;

import java.io.File;

import com.haocom.util.zip.ZipFile;

/**
 * 文件压缩解压缩工具. <br>
 * 提供文件压缩解压缩的静态方法，可以完全替代以前的Zipper.
 * <p>
 * Copyright: Copyright (c) 2009-1-6
 * <p>
 * Company: 
 * <p>
 * Author: gaowei
 * <p>
 * Version: 1.0
 */
public class ZipFileUtil {

    /**
     * 解压文件
     * 
     * @param zipFilePath
     *            压缩文件路径
     * @param localDir
     *            希望解压到的本地目录
     * @param encoding
     *            编码类型，为null时使用默认编码
     * @throws Exception
     *             异常
     */
    public static void unzip(String zipFilePath, String localDir, String encoding) throws Exception {
        ZipFile zf = new ZipFile(new File(zipFilePath), encoding);
        zf.unzipFilesToDir(localDir);
    }

    /**
     * 压缩文件或目录
     * 
     * @param zipFilePath
     *            压缩文件路径
     * @param srcFile
     *            要压缩的文件或目录
     * @param encoding
     *            编码类型，为null时使用默认编码
     * @throws Exception
     *             异常
     */
    public static void zip(String zipFilePath, String srcFile, String encoding) throws Exception {
        ZipFile zf = new ZipFile(encoding);
        File file = new File(srcFile);
        if (file.isFile()) {
            zf.zipFile(file, file.getName());
        } else {
            zf.zipFiles(file, null);
        }
        zf.saveZip(zipFilePath);
    }
}
