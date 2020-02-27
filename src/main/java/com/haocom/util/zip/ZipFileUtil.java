/**
 * 
 */
package com.haocom.util.zip;

import java.io.File;

import com.haocom.util.zip.ZipFile;

/**
 * �ļ�ѹ����ѹ������. <br>
 * �ṩ�ļ�ѹ����ѹ���ľ�̬������������ȫ�����ǰ��Zipper.
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
     * ��ѹ�ļ�
     * 
     * @param zipFilePath
     *            ѹ���ļ�·��
     * @param localDir
     *            ϣ����ѹ���ı���Ŀ¼
     * @param encoding
     *            �������ͣ�Ϊnullʱʹ��Ĭ�ϱ���
     * @throws Exception
     *             �쳣
     */
    public static void unzip(String zipFilePath, String localDir, String encoding) throws Exception {
        ZipFile zf = new ZipFile(new File(zipFilePath), encoding);
        zf.unzipFilesToDir(localDir);
    }

    /**
     * ѹ���ļ���Ŀ¼
     * 
     * @param zipFilePath
     *            ѹ���ļ�·��
     * @param srcFile
     *            Ҫѹ�����ļ���Ŀ¼
     * @param encoding
     *            �������ͣ�Ϊnullʱʹ��Ĭ�ϱ���
     * @throws Exception
     *             �쳣
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
