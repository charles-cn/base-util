/**
 * 
 */
package com.haocom.util.zip;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * �����ڴ��ļ�. <br>
 * �����ڴ��ļ�.
 * <p>
 * Copyright: Copyright (c) 2008-12-25
 * <p>
 * Company: 
 * <p>
 * Author: gaowei
 * <p>
 * Version: 1.0
 */
class VirtualRamFile {

    /** �Ƿ�Ŀ¼. */
    private boolean isDirectory;

    /** ����������. */
    private byte[] data;

    /**
     * ������
     * 
     * @param isDirectory
     *            �Ƿ�Ŀ¼
     * @param data
     *            ���������ݣ�Ŀ¼ʱΪnull��
     */
    VirtualRamFile(boolean isDirectory, byte[] data) {
        this.isDirectory = isDirectory;
        this.data = data;
    }

    /**
     * ��ȡ����
     * 
     * @return �ļ��а��������ݣ�Ŀ¼ʱΪnull
     */
    byte[] getData() {
        return data;
    }

    /**
     * �Ƿ�Ŀ¼
     * 
     * @return �Ƿ�Ŀ¼
     */
    boolean isDirectory() {
        return isDirectory;
    }

    /**
     * ���浽����
     * 
     * @param targetName
     *            Ŀ��·��
     * @throws Exception
     *             �쳣
     */
    void saveToDisk(String targetName) throws Exception {
        if (isDirectory) {
            File dir = new File(targetName);
            if (!dir.isDirectory()) {
                if (!dir.mkdirs()) {
                    throw new IOException("����Ŀ¼ʧ�ܣ�" + targetName);
                }
            }
        } else {
            File temp = new File(targetName);
            if (temp.getParentFile() != null && !temp.getParentFile().exists()) {
                if (!temp.getParentFile().mkdirs()) {
                    throw new IOException("����Ŀ¼ʧ�ܣ�" + temp.getParent());
                }
            }
            FileOutputStream fw = new FileOutputStream(targetName);
            try {
                fw.write(data);
                fw.flush();
            }
            catch (Exception ex) {
                throw ex;
            }
            finally {
                fw.close();
            }
        }
    }
}
