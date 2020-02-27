package com.haocom.util.xlstool;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import jxl.Cell;
import jxl.CellType;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.WritableCell;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import org.apache.log4j.Logger;

import com.haocom.util.xlstool.exception.ErrorOpenModeException;

/**
 * Excel�ļ�������. <br>
 * Excel�ļ�������,�ɽ��и��ֶ�Excel�Ĳ���.
 * <p>
 * Copyright: Copyright (c) Sep 16, 2008
 * <p>
 * Company: 
 * <p>
 * Author: liujun
 * <p>
 * Version: 1.0
 * <p>
 * 
 * <pre>
 * ����ʾ����
 * XlsTool tool = new XlsTool();
 * try {
 * 	tool.openXls(&quot;d:/xls_test/2011.05.xls&quot;, &quot;rw&quot;);
 * 	tool.setSheetNo(0);
 * 	tool.getCellValue(0, 1);
 * 	tool.getCurrSheetName();
 * 	tool.getRow(0);
 * 	tool.setCellValue(0, 1, &quot;test&quot;);
 * 	tool.addRow(new String[] { &quot;1&quot;, &quot;2&quot; });
 * 	tool.addSheet(&quot;sheet_test&quot;);
 * 	tool.updateRow(2, new String[] { &quot;3&quot;, &quot;4&quot; });
 * 	tool.getColumnNum();
 * 	tool.getCurrSheetName();
 * 	tool.getRowNum();
 * 	tool.commit();
 * 	tool.closeXls();
 * }
 * catch (Exception ex) {
 * 	ex.printStackTrace();
 * }
 * </pre>
 */

public class XlsTool {

	/** xls��ʱ�ļ��� */
	private static String TMP_FILE = "~tmp.xls";

	// public static void main(String[] args) {
	// XlsTool tool = new XlsTool();
	// try {
	// tool.openXls("D:\\1.xls", "rw");
	// tool.setSheetNo(0);
	// tool.getCellValue(0, 1);
	// tool.getCurrSheetName();
	// tool.getRow(0);
	// tool.setCellValue(0, 1, "test");
	// tool.addRow(new String[] { "1", "2" });
	// tool.addSheet("sheet_test");
	// tool.updateRow(2, new String[] { "3", "4" });
	// tool.getColumnNum();
	// tool.getCurrSheetName();
	// tool.getRowNum();
	// tool.commit();
	// tool.closeXls();
	// }
	// catch (Exception ex) {
	// ex.printStackTrace();
	// }
	// }

	/** ���� */
	private int columnNum;

	/** �Ƿ��д */
	private boolean isWrite;

	/** ��־ */
	private Logger logger;

	/** ���� */
	private int rowNum;

	/** xls�� */
	private Sheet rSheet;

	/** xls Workbook */
	private Workbook rwb;

	/** ����� */
	private int sheetNo;

	/** ������ */
	private int sheetNum;

	/** xls��ʱ�ļ� */
	private File tmpFile;

	/** ��д��xls�� */
	private WritableSheet wSheet;

	/** ��д��xls Workbook */
	private WritableWorkbook wwb;

	/** xls�ļ� */
	private File xlsFile;

	/**
	 * ��ʼ��xlsTools��
	 */
	public XlsTool() {
		this.columnNum = 0;
		this.sheetNum = 0;
		this.rowNum = 0;
		this.sheetNo = 0;

		logger = Logger.getLogger(this.getClass());

	}

	/**
	 * ��ָ�����λ�����Ԫ�أ��ڲ�����
	 * 
	 * @param x
	 *            ��������(��0��ʼ)
	 * @param y
	 *            ��������(��0��ʼ)
	 * @param value
	 *            ��Ԫ��ֵ
	 * @throws ErrorOpenModeException
	 * @throws RowsExceededException
	 * @throws WriteException
	 */
	private void addCellValue(int x, int y, String value) throws ErrorOpenModeException, RowsExceededException, WriteException {
		if (!isWrite)
			throw new ErrorOpenModeException();
		if (value == null)
			value = "";
		WritableCell wc = new jxl.write.Label(x, y, value);
		wSheet.addCell(wc);
	}

	/**
	 * ���һ�е���ǰ����������������ǰ���������ȡ�������㣬�򲹿�
	 * 
	 * @param line
	 *            ��Ԫֵ��
	 * @throws ErrorOpenModeException
	 * @throws RowsExceededException
	 * @throws WriteException
	 */
	public void addRow(String[] line) throws ErrorOpenModeException, RowsExceededException, WriteException {
		if (!isWrite)
			throw new ErrorOpenModeException();
		int len = line.length > this.columnNum ? this.columnNum : line.length;
		for (int i = 0; i < len; i++) {
			this.addCellValue(i, this.rowNum, line[i]);
		}
		this.rowNum++;
	}

	/**
	 * ����һ��������ǰxls�ļ�
	 * 
	 * @param sheetName
	 *            ����(���ظ�)
	 * @throws ErrorOpenModeException
	 */
	public void addSheet(String sheetName) throws ErrorOpenModeException {
		if (!isWrite)
			throw new ErrorOpenModeException();
		wwb.createSheet(sheetName, this.sheetNum);
		this.sheetNum++;
	}

	/**
	 * �ر��Ѵ򿪵��ļ�
	 */
	public void closeXls() {
		if (rwb != null) {
			rwb.close();
		}
		if (wwb != null) {
			try {
				wwb.close();
			}
			catch (Exception ex) {
				logger.warn(null, ex);
			}
		}
		if (tmpFile.exists())
			tmpFile.delete();
	}

	/**
	 * �ύ��xls�ļ����޸ģ���������δ�������������޸ľ���Ч
	 * 
	 * @throws IOException
	 * @throws ErrorOpenModeException
	 * @throws WriteException
	 */
	public void commit() throws IOException, ErrorOpenModeException, WriteException {
		if (!isWrite)
			throw new ErrorOpenModeException();
		wwb.write();
		wwb.close();
		rwb.close();
		xlsFile.delete();
		tmpFile.renameTo(xlsFile);

		this.openXls(xlsFile.getPath(), "rw");
		this.setSheetNo(this.sheetNo);
	}

	/**
	 * �õ�ָ��λ�õ�Ԫ��ֵ
	 * 
	 * @param x
	 *            ��������(��0��ʼ)
	 * @param y
	 *            ��������(��0��ʼ)
	 * @return ָ��λ�õ�Ԫ��ֵ
	 */
	public String getCellValue(int x, int y) {
		Cell cell = rSheet.getCell(x, y);
		/*
		 * CellType type = cell.getType();
		 * if(type.toString().equals(CellType.LABEL.toString())) { return
		 * ""+((Label)cell).getString(); } else
		 * if(type.toString().equals(CellType.BOOLEAN.toString())) { return
		 * ""+((Boolean)cell).getValue(); } else
		 * if(type.toString().equals(CellType.NUMBER.toString())) { return
		 * ""+((Number)cell).getValue(); } else {
		 */
		return cell.getContents();
		// }
	}

	/**
	 * �õ���ǰ���������
	 * 
	 * @return ��ǰ���������
	 */
	public int getColumnNum() {
		return columnNum;
	}

	/**
	 * �õ���ǰ����
	 * 
	 * @return ��ǰ����
	 */
	public String getCurrSheetName() {
		return this.rSheet.getName();
	}

	/**
	 * ��ȡExcel�ļ�������޸�ʱ��
	 * 
	 * @return Excel�ļ�������޸�ʱ��
	 */
	public long getLastModified() {
		return xlsFile.lastModified();
	}

	/**
	 * ��ȡExcel�ļ�������޸�ʱ��.<br>
	 * �����������format="yyyy-MM-dd HH:mm:ss"������ʱ���磺"2010-07-19 15:59:16"
	 * 
	 * @param format
	 *            ʱ���ʽ
	 * @return ����ָ��ʱ���ʽ��Excel�ļ�������޸�ʱ��
	 * @throws Exception
	 *             ʱ���ʽ�����׳��쳣
	 */
	public String getLastModified(String format) throws Exception {
		Date date = new Date(xlsFile.lastModified());
		DateFormat dateFormat = new SimpleDateFormat(format);
		return dateFormat.format(date);
	}

	/**
	 * �õ�ָ���е����е�Ԫ���ֵ
	 * 
	 * @param y
	 *            �к�(��0��ʼ)
	 * @return ָ���е����е�Ԫ���ֵ
	 */
	public String[] getRow(int y) {
		String[] result = new String[this.columnNum];
		for (int i = 0; i < this.columnNum; i++) {
			result[i] = this.getCellValue(i, y);
		}
		return result;
	}

	/**
	 * �õ���ǰ��������
	 * 
	 * @return ��ǰ��������
	 */
	public int getRowNum() {
		return rowNum;
	}

	/**
	 * �õ�xls�ļ�������Ŀ
	 * 
	 * @return xls�ļ�������Ŀ
	 */
	public int getSheetNum() {
		return sheetNum;
	}

	/**
	 * ���Ѵ��ڵ�xls�ļ�
	 * 
	 * @param xlsName
	 *            �ļ���
	 * @param mode
	 *            ��ģʽ: rֻ�� rw��д
	 * @throws IOException
	 */
	public void openXls(String xlsName, String mode) throws IOException {
		xlsFile = new File(xlsName);
		try {
			rwb = Workbook.getWorkbook(xlsFile);
			if (mode.indexOf("w") >= 0) {
				tmpFile = new File(xlsFile.getParent().concat(File.separator).concat(TMP_FILE));
				wwb = Workbook.createWorkbook(tmpFile, rwb);
				isWrite = true;
			} else {
				wwb = null;
				isWrite = false;
			}
			this.sheetNum = rwb.getNumberOfSheets();
		}
		catch (BiffException e) {
			throw new IOException();
		}
	}

	/**
	 * ����ָ��λ�õ�Ԫ��ֵ����Ԫ��ֵ�����ϱ�׼��������Ĭ��ֵ
	 * 
	 * @param x
	 *            ��������(��0��ʼ)
	 * @param y
	 *            ��������(��0��ʼ)
	 * @param value
	 *            ����ֵ
	 * @throws ErrorOpenModeException
	 * @throws ParseException
	 */
	public void setCellValue(int x, int y, String value) throws Exception {
		if (!isWrite)
			throw new ErrorOpenModeException();
		if (value == null)
			value = "";
		WritableCell wc = wSheet.getWritableCell(x, y);
		CellType ct = wc.getType();
		String type = ct.toString();
		if (type.equals(CellType.BOOLEAN.toString()) || type.equals(CellType.BOOLEAN_FORMULA.toString())) {
			boolean bValue = false;
			try {
				bValue = java.lang.Boolean.parseBoolean(value);
			}
			catch (Exception ex) {
				logger.error(null, ex);
			}
			((jxl.write.Boolean) wc).setValue(bValue);
		} else if (type.equals(CellType.DATE.toString()) || type.equals(CellType.DATE_FORMULA.toString())) {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
			((jxl.write.DateTime) wc).setDate(simpleDateFormat.parse(value));
		} else if (type.equals(CellType.EMPTY.toString()) || type.equals(CellType.ERROR.toString()) || type.equals(CellType.FORMULA_ERROR.toString())) {
			((jxl.write.Label) wc).setString(value);
		} else if (type.equals(CellType.LABEL.toString())) {
			((jxl.write.Label) wc).setString(value);
		} else if (type.equals(CellType.STRING_FORMULA.toString())) {
			((jxl.write.Label) wc).setString(value);
		} else if (type.equals(CellType.NUMBER.toString()) || type.equals(CellType.NUMBER_FORMULA.toString())) {
			double dValue = 0;
			try {
				dValue = java.lang.Double.parseDouble(value);
			}
			catch (Exception ex) {
				logger.error(null, ex);
			}
			((jxl.write.Number) wc).setValue(dValue);
		} else {
			return;
		}
	}

	/**
	 * ���ò����ı�
	 * 
	 * @param pageNo
	 *            �����(��0��ʼ)
	 */
	public void setSheetNo(int pageNo) {
		if (pageNo < 0)
			return;
		rSheet = rwb.getSheet(pageNo);
		columnNum = rSheet.getColumns();
		rowNum = rSheet.getRows();
		if (wwb != null)
			wSheet = wwb.getSheet(pageNo);

		this.sheetNo = pageNo;
	}

	/**
	 * ����ָ���е�ֵ��������������ǰ���������ȡ�������㣬�򲻶�ʣ���¼���κβ���
	 * 
	 * @param y
	 *            �к�(��0��ʼ)
	 * @param line
	 *            ��Ԫֵ��
	 * @throws Exception
	 */
	public void updateRow(int y, String[] line) throws Exception {
		if (!isWrite)
			throw new ErrorOpenModeException();
		int len = line.length > this.columnNum ? this.columnNum : line.length;
		for (int i = 0; i < len; i++) {
			this.setCellValue(i, y, line[i]);
		}
	}
}
