/**
 * 
 */
package com.haocom.util.zip;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 虚拟内存文件. <br>
 * 虚拟内存文件.
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

    /** 是否目录. */
    private boolean isDirectory;

    /** 包含的数据. */
    private byte[] data;

    /**
     * 构造器
     * 
     * @param isDirectory
     *            是否目录
     * @param data
     *            包含的数据（目录时为null）
     */
    VirtualRamFile(boolean isDirectory, byte[] data) {
        this.isDirectory = isDirectory;
        this.data = data;
    }

    /**
     * 获取数据
     * 
     * @return 文件中包含的数据，目录时为null
     */
    byte[] getData() {
        return data;
    }

    /**
     * 是否目录
     * 
     * @return 是否目录
     */
    boolean isDirectory() {
        return isDirectory;
    }

    /**
     * 保存到磁盘
     * 
     * @param targetName
     *            目的路径
     * @throws Exception
     *             异常
     */
    void saveToDisk(String targetName) throws Exception {
        if (isDirectory) {
            File dir = new File(targetName);
            if (!dir.isDirectory()) {
                if (!dir.mkdirs()) {
                    throw new IOException("创建目录失败：" + targetName);
                }
            }
        } else {
            File temp = new File(targetName);
            if (temp.getParentFile() != null && !temp.getParentFile().exists()) {
                if (!temp.getParentFile().mkdirs()) {
                    throw new IOException("创建目录失败：" + temp.getParent());
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
