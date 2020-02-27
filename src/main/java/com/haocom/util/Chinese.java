package com.haocom.util;

/**
 * �����ַ�ת��. <br>
 * ������ݿ��ַ�����ϵͳ�ַ�����һ��,��ȡ����ʱ��Ҫϵͳ�ַ��������ݿ��ַ������ֱ���֮����ת��,����ϵͳ����ʱ��̬�����Ƿ���Ҫת��.
 * <p>
 * Copyright: Copyright (c) Sep 16, 2008
 * <p>
 * Company: 
 * <p>
 * Author: ywh
 * <p>
 * Version: 1.0
 */

public class Chinese {

	/** ϵͳ����ʶ���Ƿ���Ҫת�룬Ĭ��Ϊ����Ҫת�� */
	private static boolean isChangeEncoding = false;

	/**
	 * �����ݿ���ȡ�����ַ�����iso-8859-1��ת��Ϊ�����������ַ�����gb18030��
	 * 
	 * @param change
	 *            ��Ҫת�������ݿ��ַ�
	 * @return �����Ҫת�����򷵻�ת����ĳ����е������ַ�����֮��ֱ�ӷ�������ֵ
	 */
	public static String fromDatabase(String change) {
		if (isChangeEncoding) {
			try {
				return new String(change.getBytes("iso-8859-1"), "gb18030");
			}
			catch (Exception e) {
				return change;
			}
		} else {
			return change;
		}
	}

	/**
	 * �趨�Ƿ�����ת�빦�ܣ�Ĭ��Ϊ������
	 * 
	 * @param bool
	 *            �Ƿ�����ת�빦��
	 */
	public static void setChangeEncoding(boolean bool) {
		isChangeEncoding = bool;
	}

	/**
	 * �������ַ�����gb18030��ת��Ϊ�������ݿ��е��ַ�����iso-8859-1��
	 * 
	 * @param change
	 *            ��Ҫת���ĳ����е������ַ���
	 * @return ת��������ݿ��ַ���
	 */
	public static String toDatabase(String change) {
		if (isChangeEncoding) {
			try {
				return new String(change.getBytes("gb18030"), "iso-8859-1");
			}
			catch (Exception e) {
				return change;
			}
		} else {
			return change;
		}
	}
}
