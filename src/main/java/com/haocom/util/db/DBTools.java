package com.haocom.util.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

/**
 * 数据库操作. <br>
 * 定义数据库操作.
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
	 * 默认构造函数
	 */
	public DBTools() {
	}

	/**
	 * 删除操作使用默认连接池
	 * 
	 * @param conn
	 *            连接
	 * @param sSql
	 *            要执行的sql语句
	 * @param params
	 *            PreparedStatement参数
	 * @return 成功删除的行数
	 * @throws Exception
	 */
	public int delete(Connection conn, String sSql, Object[] params) throws Exception {
		return execute(conn, sSql, params);
	}

	/**
	 * 执行insert，update，delete
	 * 
	 * @param conn
	 *            连接
	 * @param sSql
	 *            要执行的sql语句
	 * @param params
	 *            PreparedStatement参数
	 * @return 成功insert或update或delete的行数
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
	 * 执行查询操作
	 * 
	 * @param conn
	 *            连接
	 * @param sSql
	 *            要执行的sql语句
	 * @param params
	 *            PreparedStatement参数
	 * @return RowSet对象
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
				// 参数设置
				for (int i = 0; i < params.length; i++) {
					pStmt.setObject(i + 1, params[i]);
				}
			}
			rs = pStmt.executeQuery();
			ResultSetMetaData rsmd = rs.getMetaData();
			int cols = rsmd.getColumnCount();
			/** 如果为零 */
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
	 * 查询一行记录,使用默认连接池
	 * 
	 * @param conn
	 *            连接
	 * @param sSql
	 *            要执行的sql语句
	 * @param params
	 *            PreparedStatement参数
	 * @return Row对象
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
	 * 查询多行记录,使用默认连接池
	 * 
	 * @param conn
	 *            连接
	 * @param sSql
	 *            要执行的sql语句
	 * @param params
	 *            PreparedStatement参数
	 * @return RowSet对象
	 * @throws Exception
	 */
	public RowSet getRows(Connection conn, String sSql, Object[] params) throws Exception {
		return executeQuery(conn, sSql, params);
	}

	/**
	 * 插入操作,使用默认连接池
	 * 
	 * @param conn
	 *            连接
	 * @param sSql
	 *            要执行的sql语句
	 * @param params
	 *            PreparedStatement参数
	 * @return 成功插入行数
	 * @throws Exception
	 */
	public int insert(Connection conn, String sSql, Object[] params) throws Exception {
		return execute(conn, sSql, params);
	}

	/**
	 * 更新操作使用默认连接池
	 * 
	 * @param conn
	 *            连接
	 * @param sSql
	 *            要执行的sql语句
	 * @param params
	 *            PreparedStatement参数
	 * @return 成功更新行数
	 * @throws Exception
	 */
	public int update(Connection conn, String sSql, Object[] params) throws Exception {
		return execute(conn, sSql, params);
	}
}
