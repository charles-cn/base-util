package com.haocom.util.dbftool.exception;

/**
 * �쳣����. <br>
 * ʹ��DBF�����з������쳣����.
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

	/** ����ID */
	private static final long serialVersionUID = 1L;

	@Override
	public String getMessage() {
		return "Too more Columns input!";
	}

}
