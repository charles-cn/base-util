package com.haocom.util.xlstool.exception;

/**
 * �쳣����. <br>
 * ʹ��XLS�����з������쳣����.
 * <p>
 * Copyright: Copyright (c) Sep 17, 2008
 * <p>
 * Company: 
 * <p>
 * Author:
 * <p>
 * Version: 1.0
 */
public class ErrorOpenModeException extends Exception {

	/**
	 * ����ID
	 */
	private static final long serialVersionUID = -8838355481704465899L;

	@Override
	public String getMessage() {
		return "You open this xls just for read, not for write!";
	}

}
