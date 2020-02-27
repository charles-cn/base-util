package com.haocom.util.dbftool.lang;

import java.util.Hashtable;

/**
 * ��¼��. <br>
 * ��¼�࣬��ӦDBF����һ����¼���ֶο�ȱ.
 * <p>
 * Copyright: Copyright (c) Sep 16, 2008
 * <p>
 * Company: 
 * <p>
 * Author: liujun
 * <p>
 * Version: 1.0
 */
public class Record {

	/** ����һ��Hashtable������ֶ����� */
	private Hashtable table = null;

	/**
	 * ���췽��
	 */
	public Record() {
		table = new Hashtable();
	}

	/**
	 * ����ֶ�����
	 * 
	 * @param relation
	 *            �ֶ�����
	 */
	public void addRelation(Relation relation) {
		if (table.get(relation.getColumnName()) != null)
			table.remove(relation.getColumnName());
		table.put(relation.getColumnName(), relation.getValue());
	}

	/**
	 * ��Ӷ���ֶ�����
	 * 
	 * @param relations
	 *            �ֶ���������
	 */
	public void addRelations(Relation[] relations) {
		// TODO Auto-generated method stub
		for (int i = 0; i < relations.length; i++) {
			this.addRelation(relations[i]);
		}
	}

	/**
	 * ���ָ��������ֵ
	 * 
	 * @param columnName
	 *            ����
	 * @param value
	 *            ֵ
	 */
	public void addValue(String columnName, String value) {
		if (table.get(columnName) != null)
			table.remove(columnName);
		table.put(columnName, value);
	}

	/**
	 * ��ȡָ���е�ֵ
	 * 
	 * @param column
	 *            ��
	 * @return ָ���е�ֵ
	 */
	public String getValue(String column) {
		return (String) table.get(column);
	}
}
