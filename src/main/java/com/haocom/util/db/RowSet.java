package com.haocom.util.db;

import java.util.ArrayList;
import java.util.List;

/**
 * 行对象集合. <br>
 * 定义行对象集合.
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

	/** 行集合 */
	private List<Row> eachRowList = new ArrayList<Row>(10);

	/**
	 * 默认构造函数
	 */
	public RowSet() {
	}

	/**
	 * 添加一行
	 * 
	 * @param row
	 *            一行
	 */
	public void add(Row row) {
		eachRowList.add(row);
	}

	/**
	 * 输出每行的信息
	 */
	public void debug() {
		
		for (Row row : eachRowList) {
			row.debug();
		}
	}

	/**
	 * 根据索引取出一行
	 * 
	 * @param index
	 *            索引
	 * @return 一行
	 */
	public Row get(int index) {
		if (length() < 1) {
			return null;
		} else {
			return eachRowList.get(index);
		}
	}

	/**
	 * 返回行数
	 * 
	 * @return 行数
	 */
	public int length() {
		return eachRowList.size();
	}
}
