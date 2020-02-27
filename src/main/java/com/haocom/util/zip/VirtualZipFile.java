/**
 * 
 */
package com.haocom.util.zip;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;
import java.util.zip.ZipException;

/**
 * Title. <br>
 * Description.
 * <p>
 * Copyright: Copyright (c) 2008-12-24
 * <p>
 * Company: 
 * <p>
 * Author: gaowei
 * <p>
 * Version: 1.0
 */
class VirtualZipFile {

    /**
     * InputStream that delegates requests to the underlying RandomAccessFile,
     * making sure that only bytes from a certain range can be read.
     */
    private class BoundedInputStream extends InputStream {

        private boolean addDummyByte = false;

        private long loc;

        private long remaining;

        BoundedInputStream(long start, long remaining) {
            this.remaining = remaining;
            loc = start;
        }

        /**
         * Inflater needs an extra dummy byte for nowrap - see Inflater's
         * javadocs.
         */
        void addDummy() {
            addDummyByte = true;
        }

        public int read() throws IOException {
            if (remaining-- <= 0) {
                if (addDummyByte) {
                    addDummyByte = false;
                    return 0;
                }
                return -1;
            }
            synchronized (zipInputStream) {
                zipInputStream.reset();
                zipInputStream.skip(loc++);
                return zipInputStream.read();
            }
        }

        public int read(byte[] b, int off, int len) throws IOException {
            if (remaining <= 0) {
                if (addDummyByte) {
                    addDummyByte = false;
                    b[off] = 0;
                    return 1;
                }
                return -1;
            }

            if (len <= 0) {
                return 0;
            }

            if (len > remaining) {
                len = (int) remaining;
            }
            int ret = -1;
            synchronized (zipInputStream) {
                zipInputStream.reset();
                zipInputStream.skip(loc);
                ret = zipInputStream.read(b, off, len);
            }
            if (ret > 0) {
                loc += ret;
                remaining -= ret;
            }
            return ret;
        }
    }

    private class OffsetEntry {

        private long dataOffset = -1;

        private long headerOffset = -1;
    }

    /** �������ֽ�����. */
    private byte[] buffer = new byte[1024];

    /** �����ʽ. */
    private String encoding;

    /** ����Zip��ں�ƫ������ӳ��. */
    private Map<ZipEntry, OffsetEntry> entries;

    /** �������ƺ�Zip��ڵ�ӳ��. */
    private Map<String, ZipEntry> nameMap;

    private Map<String, VirtualRamFile> ramFileMap;

    private ByteArrayOutputStream tempOutputStream;

    /** �ۼ��ļ���С. */
    private long totalFileLength;

    /** ѹ����������ݳ���. */
    private int zipDataLength;

    /** �������������. */
    private ByteArrayInputStream zipInputStream;

    /**
     * ������
     */
    VirtualZipFile() {
        tempOutputStream = new ByteArrayOutputStream();
        ramFileMap = new ConcurrentHashMap<String, VirtualRamFile>();
        nameMap = new ConcurrentHashMap<String, ZipEntry>(CodeDef.HASH_SIZE);
    }

    /**
     * ������
     * 
     * @param b
     *            ����
     * @throws Exception
     *             �쳣
     */
    VirtualZipFile(byte[] b) throws Exception {
        tempOutputStream = new ByteArrayOutputStream();
        initialization(b);
    }

    /**
     * ������
     * 
     * @param b
     *            ����
     * @param encoding
     *            �����ʽ
     * @throws Exception
     *             �쳣
     */
    VirtualZipFile(byte[] b, String encoding) throws Exception {
        this.encoding = encoding;
        tempOutputStream = new ByteArrayOutputStream();
        initialization(b);
    }

    /**
     * ������
     * 
     * @param zipFile
     *            ѹ���ļ�
     * @throws Exception
     *             �쳣
     */
    VirtualZipFile(File zipFile) throws Exception {
        tempOutputStream = new ByteArrayOutputStream();
        FileInputStream fis = new FileInputStream(zipFile);
        try {
            initialization(readByteFromStream(fis));
        }
        catch (Exception e) {
            throw e;
        }
        finally {
            fis.close();
        }
    }

    /**
     * ������
     * 
     * @param zipFile
     *            ѹ���ļ�
     * @param encoding
     *            �����ʽ
     * @throws Exception
     *             �쳣
     */
    VirtualZipFile(File zipFile, String encoding) throws Exception {
        this.encoding = encoding;
        tempOutputStream = new ByteArrayOutputStream();
        FileInputStream fis = new FileInputStream(zipFile);
        try {
            initialization(readByteFromStream(fis));
        }
        catch (Exception e) {
            throw e;
        }
        finally {
            fis.close();
        }
    }

    /**
     * ������
     * 
     * @param in
     *            ������
     * @throws Exception
     *             �쳣
     */
    VirtualZipFile(InputStream in) throws Exception {
        tempOutputStream = new ByteArrayOutputStream();
        initialization(readByteFromStream(in));
    }

    /**
     * ������
     * 
     * @param in
     *            ������
     * @param encoding
     *            �����ʽ
     * @throws Exception
     *             �쳣
     */
    VirtualZipFile(InputStream in, String encoding) throws Exception {
        this.encoding = encoding;
        tempOutputStream = new ByteArrayOutputStream();
        initialization(readByteFromStream(in));
    }

    /**
     * ������
     * 
     * @param encoding
     *            �����ʽ
     * @throws Exception
     *             �쳣
     */
    VirtualZipFile(String encoding) {
        this.encoding = encoding;
        tempOutputStream = new ByteArrayOutputStream();
        ramFileMap = new ConcurrentHashMap<String, VirtualRamFile>();
        nameMap = new ConcurrentHashMap<String, ZipEntry>(CodeDef.HASH_SIZE);
    }

    /**
     * ����ļ���ZIP
     * 
     * @param b
     *            �ֽ�
     * @param zipPath
     *            ѹ���ļ�·��
     * @throws Exception
     *             �쳣
     */
    void addFileToZip(byte[] b, String zipPath) throws Exception {
        VirtualRamFile virtualRamFile;
        if (b == null) {
            virtualRamFile = new VirtualRamFile(true, b);
            if (!zipPath.endsWith("/")) {
                zipPath = zipPath + "/";
            }
        } else {
            totalFileLength = totalFileLength + b.length;
            virtualRamFile = new VirtualRamFile(false, b);
        }
        ramFileMap.put(zipPath, virtualRamFile);
    }

    /*
     * Converts DOS time to Java time (number of milliseconds since epoch).
     */
    private long dosToJavaTime(long dosTime) {
        Calendar cal = Calendar.getInstance();
        // CheckStyle:MagicNumberCheck OFF - no point
        cal.set(Calendar.YEAR, (int) ((dosTime >> 25) & 0x7f) + 1980);
        cal.set(Calendar.MONTH, (int) ((dosTime >> 21) & 0x0f) - 1);
        cal.set(Calendar.DATE, (int) (dosTime >> 16) & 0x1f);
        cal.set(Calendar.HOUR_OF_DAY, (int) (dosTime >> 11) & 0x1f);
        cal.set(Calendar.MINUTE, (int) (dosTime >> 5) & 0x3f);
        cal.set(Calendar.SECOND, (int) (dosTime << 1) & 0x3e);
        // CheckStyle:MagicNumberCheck ON
        return cal.getTime().getTime();
    }

    /**
     * ��ȡ�ڴ������е��ļ���ֻ�����ļ���
     * 
     * @return �ļ�·�������ݵ�ӳ�䣬��ӳ�䲻���̰߳�ȫ�ģ�ÿ���ñ�����һ�ξͻ����һ���µ�ӳ��
     */
    Map<String, byte[]> getAllFileInRam() {
        HashMap<String, byte[]> hm = new HashMap<String, byte[]>();
        VirtualRamFile value;
        for (Map.Entry<String, VirtualRamFile> entry : ramFileMap.entrySet()) {
            value = entry.getValue();
            if (value.isDirectory()) {
                continue;
            }
            hm.put(entry.getKey(), value.getData());
        }
        return hm;
    }

    /**
     * ��ZIP��ڻ�����ݣ�����Ϊ�ļ���
     * 
     * @param zipEntry
     *            ZIP���
     * @return ����
     * @throws Exception
     *             �쳣
     */
    private byte[] getDataFromEntry(ZipEntry zipEntry) throws Exception {
        InputStream in = getInputStream(zipEntry);

        try {
            return readByteFromStream(in);

        }
        catch (Exception e) {
            throw e;
        }
        finally {
            in.close();
        }
    }

    /**
     * ��ȡ��ǰ�ı���
     * 
     * @return ����
     */
    String getEncoding() {
        return encoding;
    }

    /**
     * ��ȡ�ļ��б�
     * 
     * @param fileName
     *            ��Ҫ���ļ���������Ϊ����
     * @return �ļ��б�
     */
    List<String> getFileList(String fileName) {
        List<String> list = new ArrayList<String>();
        Set<String> set = ramFileMap.keySet();
        if (fileName == null) {
            list.addAll(set);
        } else {
            Pattern pattern = Pattern.compile(fileName);
            Matcher matcher;
            for (String name : set) {
                matcher = pattern.matcher(name);
                if (matcher.find()) {
                    list.add(name);
                }
            }
        }
        return list;
    }

    /**
     * Returns an InputStream for reading the contents of the given entry.
     * 
     * @param ze
     *            the entry to get the stream for.
     * @return a stream to read the entry from.
     * @throws IOException
     *             if unable to create an input stream from the zipenty
     * @throws ZipException
     *             if the zipentry has an unsupported compression method
     */
    private InputStream getInputStream(ZipEntry ze) throws IOException, ZipException {
        OffsetEntry offsetEntry = entries.get(ze);
        if (offsetEntry == null) {
            return null;
        }
        long start = offsetEntry.dataOffset;
        BoundedInputStream bis = new BoundedInputStream(start, ze.getCompressedSize());
        switch (ze.getMethod()) {
            case ZipEntry.STORED:
                return bis;
            case ZipEntry.DEFLATED:
                bis.addDummy();
                return new InflaterInputStream(bis, new Inflater(true));
            default:
                throw new ZipException("Found unsupported compression method " + ze.getMethod());
        }
    }

    /**
     * Retrieve a String from the given bytes using the encoding set for this
     * ZipFile.
     * 
     * @param bytes
     *            the byte array to transform
     * @return String obtained by using the given encoding
     * @throws ZipException
     *             if the encoding cannot be recognized.
     */
    private String getString(byte[] bytes) throws ZipException {
        if (encoding == null) {
            return new String(bytes);
        } else {
            try {
                return new String(bytes, encoding);
            }
            catch (UnsupportedEncodingException uee) {
                throw new ZipException(uee.getMessage());
            }
        }
    }

    /**
     * ��ȡ�ܼ��ļ���С
     * 
     * @return �ܼ��ļ���С
     */
    long getTotalFileLength() {
        return totalFileLength;
    }

    /**
     * ��ʼ��
     * 
     * @param b
     *            zip���ֽ�����
     * @throws Exception
     *             �쳣
     */
    private void initialization(byte[] b) throws Exception {
        ramFileMap = new ConcurrentHashMap<String, VirtualRamFile>();
        entries = new ConcurrentHashMap<ZipEntry, OffsetEntry>(CodeDef.HASH_SIZE);
        nameMap = new ConcurrentHashMap<String, ZipEntry>(CodeDef.HASH_SIZE);
        zipInputStream = new ByteArrayInputStream(b);
        zipDataLength = b.length;
        try {
            populateFromCentralDirectory();
            resolveLocalFileHeaderData();
            unzipInRam();
        }
        catch (Exception e) {
            throw e;
        }
        finally {
            zipInputStream.close();
        }
    }

    /**
     * Reads the central directory of the given archive and populates the
     * internal tables with ZipEntry instances.
     * <p>
     * The ZipEntrys will know all data that can be obtained from the central
     * directory alone, but not the data that requires the local file header or
     * additional data to be read.
     * </p>
     */
    private void populateFromCentralDirectory() throws IOException {
        positionAtCentralDirectory();

        byte[] cfh = new byte[CodeDef.CFH_LEN];

        byte[] signatureBytes = new byte[CodeDef.WORD];
        readFully(signatureBytes);
        long sig = ZipLong.getValue(signatureBytes);
        final long cfhSig = ZipLong.getValue(ZipOutputStream.CFH_SIG);
        while (sig == cfhSig) {
            readFully(cfh);
            int off = 0;
            ZipEntry ze = new ZipEntry();

            int versionMadeBy = ZipShort.getValue(cfh, off);
            off += CodeDef.SHORT;
            ze.setPlatform((versionMadeBy >> CodeDef.BYTE_SHIFT) & CodeDef.NIBLET_MASK);

            off += CodeDef.WORD; // skip version info and general purpose byte

            ze.setMethod(ZipShort.getValue(cfh, off));
            off += CodeDef.SHORT;

            // FIXME this is actually not very cpu cycles friendly as we are
            // converting from
            // dos to java while the underlying Sun implementation will convert
            // from java to dos time for internal storage...
            long time = dosToJavaTime(ZipLong.getValue(cfh, off));
            ze.setTime(time);
            off += CodeDef.WORD;

            ze.setCrc(ZipLong.getValue(cfh, off));
            off += CodeDef.WORD;

            ze.setCompressedSize(ZipLong.getValue(cfh, off));
            off += CodeDef.WORD;

            ze.setSize(ZipLong.getValue(cfh, off));
            off += CodeDef.WORD;

            int fileNameLen = ZipShort.getValue(cfh, off);
            off += CodeDef.SHORT;

            int extraLen = ZipShort.getValue(cfh, off);
            off += CodeDef.SHORT;

            int commentLen = ZipShort.getValue(cfh, off);
            off += CodeDef.SHORT;

            off += CodeDef.SHORT; // disk number

            ze.setInternalAttributes(ZipShort.getValue(cfh, off));
            off += CodeDef.SHORT;

            ze.setExternalAttributes(ZipLong.getValue(cfh, off));
            off += CodeDef.WORD;

            byte[] fileName = new byte[fileNameLen];
            readFully(fileName);
            ze.setName(getString(fileName));

            // LFH offset,
            OffsetEntry offset = new OffsetEntry();
            offset.headerOffset = ZipLong.getValue(cfh, off);
            // data offset will be filled later
            entries.put(ze, offset);

            nameMap.put(ze.getName(), ze);

            zipInputStream.skip(extraLen);

            byte[] comment = new byte[commentLen];
            readFully(comment);
            ze.setComment(getString(comment));

            readFully(signatureBytes);
            sig = ZipLong.getValue(signatureBytes);
        }
    }

    /**
     * Searches for the &quot;End of central dir record&quot;, parses it and
     * positions the stream at the first central directory record.
     */
    private void positionAtCentralDirectory() throws IOException {
        boolean found = false;
        long off = zipDataLength - CodeDef.MIN_EOCD_SIZE;
        if (off >= 0) {
            zipInputStream.skip(off);
            byte[] sig = ZipOutputStream.EOCD_SIG;
            int curr = zipInputStream.read();
            while (curr != -1) {
                if (curr == sig[CodeDef.POS_0]) {
                    curr = zipInputStream.read();
                    if (curr == sig[CodeDef.POS_1]) {
                        curr = zipInputStream.read();
                        if (curr == sig[CodeDef.POS_2]) {
                            curr = zipInputStream.read();
                            if (curr == sig[CodeDef.POS_3]) {
                                found = true;
                                break;
                            }
                        }
                    }
                }
                zipInputStream.reset();
                zipInputStream.skip(--off);
                curr = zipInputStream.read();
                if (off < 0) {
                    break;
                }
            }
        }
        if (!found) {
            throw new ZipException("archive is not a ZIP archive");
        }
        zipInputStream.reset();
        zipInputStream.skip(off + CodeDef.CFD_LOCATOR_OFFSET);
        byte[] cfdOffset = new byte[CodeDef.WORD];
        readFully(cfdOffset);
        zipInputStream.reset();
        zipInputStream.skip(ZipLong.getValue(cfdOffset));
    }

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

    private void readFully(byte b[]) throws IOException {
        readFully(b, 0, b.length);
    }

    private void readFully(byte b[], int off, int len) throws IOException {
        int n = 0;
        do {
            int count = zipInputStream.read(b, off + n, len - n);
            if (count < 0)
                throw new EOFException();
            n += count;
        }
        while (n < len);
    }

    /**
     * Walks through all recorded entries and adds the data available from the
     * local file header.
     * <p>
     * Also records the offsets for the data to read from the entries.
     * </p>
     */
    private void resolveLocalFileHeaderData() throws IOException {

        for (Map.Entry<ZipEntry, OffsetEntry> entry : entries.entrySet()) {
            ZipEntry ze = entry.getKey();
            OffsetEntry offsetEntry = entry.getValue();
            long offset = offsetEntry.headerOffset;
            zipInputStream.reset();
            zipInputStream.skip(offset + CodeDef.LFH_OFFSET_FOR_FILENAME_LENGTH);
            byte[] b = new byte[CodeDef.SHORT];
            readFully(b);
            int fileNameLen = ZipShort.getValue(b);
            readFully(b);
            int extraFieldLen = ZipShort.getValue(b);
            zipInputStream.skip(fileNameLen);
            byte[] localExtraData = new byte[extraFieldLen];
            readFully(localExtraData);
            ze.setExtra(localExtraData);
            offsetEntry.dataOffset = offset + CodeDef.LFH_OFFSET_FOR_FILENAME_LENGTH + CodeDef.SHORT + CodeDef.SHORT + fileNameLen + extraFieldLen;
        }
    }

    /**
     * ����ѹ�����������
     * 
     * @param outputStream
     *            �����
     * @throws Exception
     *             �쳣
     */
    void save(OutputStream outputStream) throws Exception {
        ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream);
        String key;
        for (Map.Entry<String, VirtualRamFile> entry : ramFileMap.entrySet()) {
            if (encoding != null) {
                key = new String(entry.getKey().getBytes(encoding));
            } else {
                key = entry.getKey();
            }
            zipOutputStream.putNextEntry(new ZipEntry(key));
            if (!entry.getValue().isDirectory()) {
                zipOutputStream.write(entry.getValue().getData());
            }
            zipOutputStream.closeEntry();
        }
        zipOutputStream.close();

    }

    /**
     * @param encoding
     *            the encoding to set
     */
    void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    /**
     * ��ѹ�����ļ����ڴ���
     * 
     * @throws Exception
     *             �쳣
     */
    private void unzipInRam() throws Exception {
        String key;
        ZipEntry value;
        // ��������ZIP���
        for (Map.Entry<String, ZipEntry> entry : nameMap.entrySet()) {
            key = entry.getKey();
            value = entry.getValue();
            // ����������Ͳ�����ͬ�������ڴ��ļ�
            if (value.isDirectory()) {
                VirtualRamFile dir = new VirtualRamFile(true, null);
                ramFileMap.put(key, dir);
            } else {
                byte[] b = getDataFromEntry(value);
                VirtualRamFile file = new VirtualRamFile(false, b);
                ramFileMap.put(key, file);
                totalFileLength = totalFileLength + b.length;
            }
        }
    }

    /**
     * ��ѹ���ļ�
     * 
     * @param fileName
     *            �ļ�·��
     * @param localDirName
     *            ����Ŀ¼��
     * @param localFileName
     *            �����ļ���
     * @throws Exception
     *             �쳣
     */
    void unzipToDisk(String fileName, String localDirName, String localFileName) throws Exception {
        VirtualRamFile virtualRamFile = ramFileMap.get(fileName);
        if (virtualRamFile == null) {
            throw new FileNotFoundException(fileName + "������");
        }
        if (localDirName == null) {
            localDirName = "";
        } else {
            // �������Ŀ¼��Ϊ����֤��"/"��β
            if (!localDirName.endsWith("/")) {
                localDirName = localDirName + "/";
            }
        }
        if (localFileName == null || localFileName.trim().length() == 0) {
            localFileName = new File(fileName).getName();
        }
        virtualRamFile.saveToDisk(localDirName + localFileName);
    }

    /**
     * ��ѹ���ļ�
     * 
     * @param zipDirName
     *            zipĿ¼
     * @param fileFilterRegex
     *            ��Ҫ���ļ�����֧������
     * @param localDirName
     *            ����Ŀ¼��
     * @param localFileName
     *            �����ļ���
     * @throws Exception
     *             �쳣
     */
    void unzipToDisk(String zipDirName, String fileFilterRegex, String localDirName, String localFileName) throws Exception {
        // ���Ŀ¼��Ϊ����֤��"/"��β
        if (zipDirName != null && !zipDirName.endsWith("/")) {
            zipDirName = zipDirName + "/";
        }

        if (localDirName == null) {
            localDirName = "";
        } else {
            // �������Ŀ¼��Ϊ����֤��"/"��β
            if (!localDirName.endsWith("/")) {
                localDirName = localDirName + "/";
            }
        }
        String key;
        VirtualRamFile value;
        Pattern pattern = null;
        Matcher matcher = null;
        if (fileFilterRegex != null) {
            pattern = Pattern.compile(fileFilterRegex);
        }

        // �����ڴ��б���������ļ�
        for (Map.Entry<String, VirtualRamFile> entry : ramFileMap.entrySet()) {
            key = entry.getKey();
            value = entry.getValue();
            // ������ZIPĿ¼Ҫ����Թ�
            if (zipDirName != null && !key.startsWith(zipDirName)) {
                continue;
            }
            // ������������Թ�
            if (pattern != null) {
                matcher = pattern.matcher(key);
                if (!matcher.find()) {
                    continue;
                }
            }

            // ������Ҫ���浽���ʵ�·�����ļ�����û��ָ���ļ���ʱ��ʹ��Ĭ�ϵ�
            if (localFileName == null || localFileName.trim().length() == 0) {
                if (zipDirName != null && zipDirName.equals(key)) {
                    continue;
                }
                if (zipDirName != null) {
                    key = key.substring(zipDirName.length());
                } else if (fileFilterRegex != null) {
                    if (value.isDirectory()) {
                        continue;
                    }
                    key = new File(key).getName();
                }
                value.saveToDisk(localDirName + key);
            } else {
                value.saveToDisk(localDirName + localFileName);
            }
        }
    }
}
