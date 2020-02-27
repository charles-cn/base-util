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
 * Excel文件操作类. <br>
 * Excel文件操作类,可进行各种对Excel的操作.
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
 * 代码示例：
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

	/** xls临时文件名 */
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

	/** 列数 */
	private int columnNum;

	/** 是否可写 */
	private boolean isWrite;

	/** 日志 */
	private Logger logger;

	/** 行数 */
	private int rowNum;

	/** xls表单 */
	private Sheet rSheet;

	/** xls Workbook */
	private Workbook rwb;

	/** 表单编号 */
	private int sheetNo;

	/** 表单数量 */
	private int sheetNum;

	/** xls临时文件 */
	private File tmpFile;

	/** 可写的xls表单 */
	private WritableSheet wSheet;

	/** 可写的xls Workbook */
	private WritableWorkbook wwb;

	/** xls文件 */
	private File xlsFile;

	/**
	 * 初始化xlsTools类
	 */
	public XlsTool() {
		this.columnNum = 0;
		this.sheetNum = 0;
		this.rowNum = 0;
		this.sheetNo = 0;

		logger = Logger.getLogger(this.getClass());

	}

	/**
	 * 在指定添加位置添加元素，内部方法
	 * 
	 * @param x
	 *            横向座标(从0开始)
	 * @param y
	 *            纵向座标(从0开始)
	 * @param value
	 *            单元格值
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
	 * 添加一行到当前表单，若列数超过当前列数，则截取，若不足，则补空
	 * 
	 * @param line
	 *            单元值列
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
	 * 增加一个表单到当前xls文件
	 * 
	 * @param sheetName
	 *            表单名(不重复)
	 * @throws ErrorOpenModeException
	 */
	public void addSheet(String sheetName) throws ErrorOpenModeException {
		if (!isWrite)
			throw new ErrorOpenModeException();
		wwb.createSheet(sheetName, this.sheetNum);
		this.sheetNum++;
	}

	/**
	 * 关闭已打开的文件
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
	 * 提交对xls文件的修改，若本操作未被调用则所有修改均无效
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
	 * 得到指定位置的元素值
	 * 
	 * @param x
	 *            横向座标(从0开始)
	 * @param y
	 *            纵向座标(从0开始)
	 * @return 指定位置的元素值
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
	 * 得到当前表单最大列数
	 * 
	 * @return 当前表单最大列数
	 */
	public int getColumnNum() {
		return columnNum;
	}

	/**
	 * 得到当前表单名
	 * 
	 * @return 当前表单名
	 */
	public String getCurrSheetName() {
		return this.rSheet.getName();
	}

	/**
	 * 获取Excel文件的最后修改时间
	 * 
	 * @return Excel文件的最后修改时间
	 */
	public long getLastModified() {
		return xlsFile.lastModified();
	}

	/**
	 * 获取Excel文件的最后修改时间.<br>
	 * 例如输入参数format="yyyy-MM-dd HH:mm:ss"，返回时间如："2010-07-19 15:59:16"
	 * 
	 * @param format
	 *            时间格式
	 * @return 返回指定时间格式的Excel文件的最后修改时间
	 * @throws Exception
	 *             时间格式错误将抛出异常
	 */
	public String getLastModified(String format) throws Exception {
		Date date = new Date(xlsFile.lastModified());
		DateFormat dateFormat = new SimpleDateFormat(format);
		return dateFormat.format(date);
	}

	/**
	 * 得到指定行的所有单元格的值
	 * 
	 * @param y
	 *            行号(从0开始)
	 * @return 指定行的所有单元格的值
	 */
	public String[] getRow(int y) {
		String[] result = new String[this.columnNum];
		for (int i = 0; i < this.columnNum; i++) {
			result[i] = this.getCellValue(i, y);
		}
		return result;
	}

	/**
	 * 得到当前表单的行数
	 * 
	 * @return 当前表单的行数
	 */
	public int getRowNum() {
		return rowNum;
	}

	/**
	 * 得到xls文件表单总数目
	 * 
	 * @return xls文件表单总数目
	 */
	public int getSheetNum() {
		return sheetNum;
	}

	/**
	 * 打开已存在的xls文件
	 * 
	 * @param xlsName
	 *            文件名
	 * @param mode
	 *            打开模式: r只读 rw读写
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
	 * 更新指定位置的元素值，如元素值不符合标准，则填入默认值
	 * 
	 * @param x
	 *            横向座标(从0开始)
	 * @param y
	 *            纵向座标(从0开始)
	 * @param value
	 *            更新值
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
	 * 设置操作的表单
	 * 
	 * @param pageNo
	 *            表单序号(从0开始)
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
	 * 更新指定行的值，若列数超过当前列数，则截取，若不足，则不对剩余记录做任何操作
	 * 
	 * @param y
	 *            行号(从0开始)
	 * @param line
	 *            单元值列
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
