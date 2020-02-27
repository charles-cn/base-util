package com.haocom.util.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

/**
 * ���ݿ����. <br>
 * �������ݿ����.
 * <p>
 * Copyright:
 * <p>
 * Company:
 * <p>
 * Author: nishu
 * <p>
 * Version: 1.0
 */

public class DBTools {

	/**
	 * Ĭ�Ϲ��캯��
	 */
	public DBTools() {
	}

	/**
	 * ɾ������ʹ��Ĭ�����ӳ�
	 * 
	 * @param conn
	 *            ����
	 * @param sSql
	 *            Ҫִ�е�sql���
	 * @param params
	 *            PreparedStatement����
	 * @return �ɹ�ɾ��������
	 * @throws Exception
	 */
	public int delete(Connection conn, String sSql, Object[] params) throws Exception {
		return execute(conn, sSql, params);
	}

	/**
	 * ִ��insert��update��delete
	 * 
	 * @param conn
	 *            ����
	 * @param sSql
	 *            Ҫִ�е�sql���
	 * @param params
	 *            PreparedStatement����
	 * @return �ɹ�insert��update��delete������
	 * @throws Exception
	 */
	private int execute(Connection conn, String sSql, Object[] params) throws Exception {
		int affectableRow = 0;
		if (sSql == null) {
			return affectableRow;
		}

		PreparedStatement pStmt = null;
		try {
			pStmt = conn.prepareStatement(sSql);
			int j = 0;
			if (params != null) {
				for (int i = 0; i < params.length; i++) {
					pStmt.setObject(++j, params[i]);
				}
			}
			affectableRow = pStmt.executeUpdate();
		}
		catch (Exception ex) {
			throw ex;
		}
		finally {
			try {
				if (pStmt != null) {
					pStmt.close();
				}
			}
			catch (Exception e) {
			}
		}
		return affectableRow;
	}

	/**
	 * ִ�в�ѯ����
	 * 
	 * @param conn
	 *            ����
	 * @param sSql
	 *            Ҫִ�е�sql���
	 * @param params
	 *            PreparedStatement����
	 * @return RowSet����
	 * @throws Exception
	 */
	private RowSet executeQuery(Connection conn, String sSql, Object[] params) throws Exception {
		PreparedStatement pStmt = null;
		ResultSet rs = null;
		RowSet rows = new RowSet();
		if (sSql == null) {
			sSql = null;
		}

		try {
			pStmt = conn.prepareStatement(sSql);
			if (params != null) {
				// ��������
				for (int i = 0; i < params.length; i++) {
					pStmt.setObject(i + 1, params[i]);
				}
			}
			rs = pStmt.executeQuery();
			ResultSetMetaData rsmd = rs.getMetaData();
			int cols = rsmd.getColumnCount();
			/** ���Ϊ�� */
			if (cols == 0) {
				return null;
			}
			while (rs.next()) {
				Row row = new Row();
				for (int i = 1; i <= cols; i++) {
					String name = rsmd.getColumnName(i);
					Object value = rs.getObject(i);
					row.put(name, value);
				}
				rows.add(row);
			}
		}
		catch (Exception ex) {
			throw ex;
		}
		finally {
			try {
				if (rs != null) {
					rs.close();
				}
			}
			catch (Exception e) {
			}

			try {
				if (pStmt != null) {
					pStmt.close();
				}
			}
			catch (Exception e) {
			}
		}
		return rows;
	}

	/**
	 * ��ѯһ�м�¼,ʹ��Ĭ�����ӳ�
	 * 
	 * @param conn
	 *            ����
	 * @param sSql
	 *            Ҫִ�е�sql���
	 * @param params
	 *            PreparedStatement����
	 * @return Row����
	 * @throws Exception
	 */
	public Row getRow(Connection conn, String sSql, Object[] params) throws Exception {
		RowSet rows = executeQuery(conn, sSql, params);
		if (rows == null) {
			return null;
		}
		return rows.get(0);
	}

	/**
	 * ��ѯ���м�¼,ʹ��Ĭ�����ӳ�
	 * 
	 * @param conn
	 *            ����
	 * @param sSql
	 *            Ҫִ�е�sql���
	 * @param params
	 *            PreparedStatement����
	 * @return RowSet����
	 * @throws Exception
	 */
	public RowSet getRows(Connection conn, String sSql, Object[] params) throws Exception {
		return executeQuery(conn, sSql, params);
	}

	/**
	 * �������,ʹ��Ĭ�����ӳ�
	 * 
	 * @param conn
	 *            ����
	 * @param sSql
	 *            Ҫִ�е�sql���
	 * @param params
	 *            PreparedStatement����
	 * @return �ɹ���������
	 * @throws Exception
	 */
	public int insert(Connection conn, String sSql, Object[] params) throws Exception {
		return execute(conn, sSql, params);
	}

	/**
	 * ���²���ʹ��Ĭ�����ӳ�
	 * 
	 * @param conn
	 *            ����
	 * @param sSql
	 *            Ҫִ�е�sql���
	 * @param params
	 *            PreparedStatement����
	 * @return �ɹ���������
	 * @throws Exception
	 */
	public int update(Connection conn, String sSql, Object[] params) throws Exception {
		return execute(conn, sSql, params);
	}
}
