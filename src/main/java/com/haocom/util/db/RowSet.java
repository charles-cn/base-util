package com.haocom.util.db;

import java.util.ArrayList;
import java.util.List;

/**
 * �ж��󼯺�. <br>
 * �����ж��󼯺�.
 * <p>
 * Copyright:
 * <p>
 * Company:
 * <p>
 * Author:
 * <p>
 * Version: 1.0
 */

public class RowSet {

	/** �м��� */
	private List<Row> eachRowList = new ArrayList<Row>(10);

	/**
	 * Ĭ�Ϲ��캯��
	 */
	public RowSet() {
	}

	/**
	 * ���һ��
	 * 
	 * @param row
	 *            һ��
	 */
	public void add(Row row) {
		eachRowList.add(row);
	}

	/**
	 * ���ÿ�е���Ϣ
	 */
	public void debug() {
		
		for (Row row : eachRowList) {
			row.debug();
		}
	}

	/**
	 * ��������ȡ��һ��
	 * 
	 * @param index
	 *            ����
	 * @return һ��
	 */
	public Row get(int index) {
		if (length() < 1) {
			return null;
		} else {
			return eachRowList.get(index);
		}
	}

	/**
	 * ��������
	 * 
	 * @return ����
	 */
	public int length() {
		return eachRowList.size();
	}
}
