/**
 * 
 */
package com.haocom.util.zip;

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
final class CodeDef {

    static final int NIBLET_MASK = 0x0f;

    // private RandomAccessFile archive;

    static final int POS_0 = 0;

    static final int POS_1 = 1;

    static final int POS_2 = 2;

    static final int POS_3 = 3;

    static final int SHORT = 2;

    static final int WORD = 4;

    static final int BYTE_SHIFT = 8;

    static final int CFD_LOCATOR_OFFSET =
    /* end of central dir signature */WORD
    /* number of this disk */+ SHORT
    /* number of the disk with the */
    /* start of the central directory */+ SHORT
    /* total number of entries in */
    /* the central dir on this disk */+ SHORT
    /* total number of entries in */
    /* the central dir */+ SHORT
    /* size of the central directory */+ WORD;

    static final int CFH_LEN =
    /* version made by */SHORT
    /* version needed to extract */+ SHORT
    /* general purpose bit flag */+ SHORT
    /* compression method */+ SHORT
    /* last mod file time */+ SHORT
    /* last mod file date */+ SHORT
    /* crc-32 */+ WORD
    /* compressed size */+ WORD
    /* uncompressed size */+ WORD
    /* filename length */+ SHORT
    /* extra field length */+ SHORT
    /* file comment length */+ SHORT
    /* disk number start */+ SHORT
    /* internal file attributes */+ SHORT
    /* external file attributes */+ WORD
    /* relative offset of local header */+ WORD;

    static final int HASH_SIZE = 509;

    /**
     * Number of bytes in local file header up to the &quot;length of
     * filename&quot; entry.
     */
    static final long LFH_OFFSET_FOR_FILENAME_LENGTH =
    /* local file header signature */WORD
    /* version needed to extract */+ SHORT
    /* general purpose bit flag */+ SHORT
    /* compression method */+ SHORT
    /* last mod file time */+ SHORT
    /* last mod file date */+ SHORT
    /* crc-32 */+ WORD
    /* compressed size */+ WORD
    /* uncompressed size */+ WORD;

    static final int MIN_EOCD_SIZE =
    /* end of central dir signature */WORD
    /* number of this disk */+ SHORT
    /* number of the disk with the */
    /* start of the central directory */+ SHORT
    /* total number of entries in */
    /* the central dir on this disk */+ SHORT
    /* total number of entries in */
    /* the central dir */+ SHORT
    /* size of the central directory */+ WORD
    /* offset of start of central */
    /* directory with respect to */
    /* the starting disk number */+ WORD
    /* zipfile comment length */+ SHORT;

    /** 最大单个文件大小. */
    static long MAX_FILE_LENGTH = 2 * 1024 * 1024L;

    /** 最大全部文件大小. */
    static long MAX_TOTAL_FILE_LENGTH = 10 * 1024 * 1024L;

}
