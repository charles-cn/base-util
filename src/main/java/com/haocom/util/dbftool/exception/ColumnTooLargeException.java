package com.haocom.util.dbftool.exception;

/**
 * 异常描述. <br>
 * 使用DBF工具中发生的异常描述.
 * <p>
 * Copyright: Copyright (c) Sep 17, 2008
 * <p>
 * Company: 
 * <p>
 * Author:
 * <p>
 * Version: 1.0
 */
public class ColumnTooLargeException extends Exception {

	/** 序列ID */
	private static final long serialVersionUID = 1L;

	@Override
	public String getMessage() {
		return "Too more Columns input!";
	}

}
