package com.haocom.util;

import java.io.BufferedReader;
import java.io.Console;
import java.io.InputStreamReader;
import java.sql.Connection;

import com.haocom.util.security.DES;

/**
 * ���ݿ������. <br>
 * ���ݿ������,ʵ�ַ������ݿ�Ĺ���.
 * <p>
 * Copyright: Copyright (c) Sep 16, 2008
 * <p>
 * Company: 
 * <p>
 * Author: ywh
 * <p>
 * Version: 1.0
 * <p>
 * <h3><a href="doc-files/DBAccess���ʹ��˵��.doc">���ʹ��˵������[DBAccess���ʹ��˵��.doc]</a></h3>
 * <p>
 * <p>
 * <h3>���Ӷ�����ݿ����ӳ�</h3>
 * 
 * <pre>
 * Dbaccess ���֧�����Ӷ�����ݿ⡣��dbpool.xml�����ö�����ݿ����ӳؼ��ɡ�
 * ע�⣺
 * 1����ȡ����ʱ��Connection conn = _dba.getConnection(����Ӧ���ӳص����ơ�); 
 * 2���ͷ����ӳأ�_dba.freeConnection(conn������Ӧ���ӳص����ơ�);
 * 3�����Ӷ�����ݿ�ʱ��ҪǶ�ס�
 * </pre>
 * 
 * <h3>ʵ�����ݿ����ӵ��û��������벻��Ϊ���ĵĹ���</h3>
 * �������ݿ����ӵ��û����������ȡ��Ӧ�����ģ�����ԭʼ���ݿ���������dbpool.xml���£�<br>
 * 
 * <pre>
 * &lt;?xml version=&quot;1.0&quot; encoding=&quot;gb2312&quot;?&gt;
 * &lt;dbm&gt;
 * 	&lt;logFile&gt;log/db.log&lt;/logFile&gt;
 * 	&lt;pool&gt;
 *         	&lt;!-- ���ӳ����� --&gt;
 * 		&lt;name&gt;1st&lt;/name&gt;
 *         	&lt;!-- ���� --&gt;
 * 		&lt;driver&gt;oracle.jdbc.driver.OracleDriver&lt;/driver&gt;
 *         	&lt;!-- ���ݿ�url --&gt;
 * 		&lt;url&gt;jdbc:oracle:thin:@192.168.10.161:1521:cjldata&lt;/url&gt;
 *         	&lt;!-- �û��� --&gt;
 * 		&lt;user&gt;newcproduct&lt;/user&gt;
 *         	&lt;!-- ���� --&gt;
 * 		&lt;password&gt;newcproductpass&lt;/password&gt;
 *         	&lt;!-- ��������� --&gt;
 * 		&lt;maxConn&gt;2&lt;/maxConn&gt;
 *         	&lt;!-- ��С������ --&gt;
 * 		&lt;minConn&gt;2&lt;/minConn&gt;
 *         	&lt;!-- ��ʼ������ --&gt;
 * 		&lt;defConn&gt;2&lt;/defConn&gt;
 * 	&lt;/pool&gt;
 * &lt;/dbm&gt;
 * </pre>
 * 
 * ʹ�������磺java -Djava.endorsed.dirs=/usr/local/program/cproduct-000/japp/libs/
 * com.haocom.util.DBAccess<br>
 * �����Ƿ���jre������ģ������ֱ��ִ�������磺/usr/local/program/cproduct-000/japp/jdk1.6.0_17/jre/
 * bin/java com.haocom.util.DBAccess<br>
 * ��ʱ���λ������username:��������newcproduct���ûس�����ʱ��Ļ�������password:
 * ��������newcproductpass���ûس��������Ļ��ʾ���£�<br>
 * <ul>
 * <li>��Ӧ���ݿ������ļ��е�usernameΪ:d3026488e01b85a21c7b4b369e8d80fb</li>
 * <li>��Ӧ���ݿ������ļ��е�passwordΪ:d3026488e01b85a2a327d21a6ca8d921</li>
 * </ul>
 * ��������û���������ֱ��滻dbpool.xml�ж�Ӧ���û������뼴�ɡ�<br>
 */

public class DBAccess {

	public static void main(String[] args) {
		try {
			Console console = System.console();
			String username;
			String password;
			String raw = "";
			if (console != null) {
				System.out.print("username: ");
				username = new String(console.readLine());
				System.out.print("password: ");
				password = new String(console.readPassword());
				raw = password;
			} else {
				System.out.print("username: ");
				username = new BufferedReader(new InputStreamReader(System.in)).readLine();
				System.out.print("password: ");
				password = new BufferedReader(new InputStreamReader(System.in)).readLine();
				raw = password;
			}
			String key = CByte.bytes2Hex("�������".getBytes());
			username = DES.encrypt(key, username);
			password = DES.encrypt(key, password);
			System.out.println("��Ӧ���ݿ������ļ��е�usernameΪ:" + username);
			System.out.println("��Ӧ���ݿ������ļ��е�passwordΪ:" + password);

			try {
				raw = DES.decrypt(key, raw);
				System.out.println(raw);
			}
			catch (Exception ex) {
			}
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/** ���ݿ����ӳ� */
	private DatabasePools dbPools;

	/** Ĭ�����ӳ� */
	private String defPoolsName;

	/** ���ݿ������ */
	DBAccessMonitor monitor;

	/** ���ӳ����� */
	private String[] poolsName;

	/**
	 * Ĭ�Ϲ��캯�� ����Ĭ�����ӳ�����Ϊ�����ļ��еĵ�һ�����ӳ�
	 */
	public DBAccess() {
		this.dbPools = DatabasePools.getInstance();
		this.poolsName = dbPools.getPoolName();
		this.defPoolsName = dbPools.getDefaultPoolName();
		this.monitor = DBAccessMonitor.getInstance();
	}

	/**
	 * �����������ӳ������Ƿ���ȷ
	 * 
	 * @param name
	 *            ���ӳ�����
	 * @return ��ʾ��������ӳ������Ƿ���ȷ
	 */
	private boolean checkPoolName(String name) {
		boolean flag = false;
		int size = poolsName.length;
		for (int i = 0; i < size; i++) {
			if (poolsName[i].equals(name)) {
				flag = true;
				break;
			}
		}
		return flag;
	}

	/**
	 * �ͷ�Ĭ�ϵ����ӳص�����
	 * 
	 * @param con
	 *            �ͷŵ�����
	 */
	public void freeConnection(Connection con) { // �ͷ����ӵ�Ĭ�ϵ����ӳ�
		freeConnection(con, defPoolsName);
	}

	/**
	 * �ͷ����ӵ�ָ�������ӳ�
	 * 
	 * @param con
	 *            Ҫ�ͷŵ�����
	 * @param poolName
	 *            ���ӳ�����
	 */
	public void freeConnection(Connection con, String poolName) {
		// ��¼ִ�ж�ջ
		monitor.recordFreeConnection(poolName);
		//
		if (!checkPoolName(poolName)) {
			DatabasePools.log(Thread.currentThread().getName() + "	[" + poolName + "] is error in freeConnection()!");
			poolName = defPoolsName;
		}
		dbPools.freeConnection(poolName, con);
	}

	/**
	 * ȡ�����ݿ���ʵĴ���
	 * 
	 * @return ����
	 */
	public int getAccessNum() {
		return dbPools.getAccessNum();
	}

	/**
	 * ȡ��Ĭ�����ӳص�һ������
	 * 
	 * @return ���õ�����
	 */
	public Connection getConnection() { // ȡ��Ĭ�ϵ�����
		return getConnection(defPoolsName);
	}

	/**
	 * ȡ��ָ�����ӳص�����
	 * 
	 * @param poolName
	 *            ���ӳ�����
	 * @return ���õ�����
	 */
	public Connection getConnection(String poolName) {
		// ��¼ִ�ж�ջ
		monitor.recordGetConnection(poolName);
		// ȡ��ָ�����ӳص�����
		if (!checkPoolName(poolName)) {
			DatabasePools.log(Thread.currentThread().getName() + "	[" + poolName + "] is error in getConnetion()!");
			poolName = defPoolsName;
		}
		Connection con = null;
		try {
			while (true) {
				con = dbPools.getConnection(poolName, 1000);
				if (con == null) {
					Thread.sleep(1000); // ���û��ȡ����ЪЪ��ȡ
				} else {
					return con;
				}
			}
		}
		catch (Exception e) {
			return null;
		}
	}

	/**
	 * ȡ��Ĭ�����ӳ�����
	 * 
	 * @return ���ӳ�����
	 */
	public String getDefPoolName() {
		return defPoolsName;
	}

	/**
	 * ȡ��Ĭ�����ӳصĿ������Ӹ���
	 * 
	 * @return �������Ӹ���
	 */
	public int getFreeConNum() {
		return dbPools.getFreeConNum(defPoolsName);
	}

	/**
	 * ȡ��ָ�����ӳصĿ������Ӹ���
	 * 
	 * @param poolName
	 *            ���ӳ�����
	 * @return �������Ӹ���
	 */
	public int getFreeConNum(String poolName) {
		return dbPools.getFreeConNum(poolName);
	}

	/**
	 * ȡ��Ĭ�����ӳصı�ʹ�����Ӹ���
	 * 
	 * @return ��ʹ�����Ӹ���
	 */
	public int getUsedConNum() {
		return dbPools.getUsedConNum(defPoolsName);
	}

	/**
	 * ȡ��ָ�����ӳصı�ʹ�����Ӹ���
	 * 
	 * @param poolName
	 *            ���ӳ�����
	 * @return ��ʹ�����Ӹ���
	 */
	public int getUsedConNum(String poolName) {
		return dbPools.getUsedConNum(poolName);
	}

	/**
	 * �ͷ���������
	 */
	public void release() {
		dbPools.release();
	}

	/**
	 * ����Ĭ�����ӳ�����
	 * 
	 * @param name
	 *            Ĭ�����ӳ�����
	 * @return true,�������������ΪĬ�����ӳ�����false����ʾ��������ӳ����ֲ���ȷ��δ�������������ΪĬ�����ӳ���
	 */
	public boolean setDefPoolName(String name) {
		if (checkPoolName(name)) {
			defPoolsName = name;
			return true;
		} else {
			return false;
		}
	}
}
