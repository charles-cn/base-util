package com.haocom.util;

import java.io.BufferedReader;
import java.io.Console;
import java.io.InputStreamReader;
import java.sql.Connection;

import com.haocom.util.security.DES;

/**
 * 数据库访问器. <br>
 * 数据库访问器,实现访问数据库的功能.
 * <p>
 * Copyright: Copyright (c) Sep 16, 2008
 * <p>
 * Company: 
 * <p>
 * Author: ywh
 * <p>
 * Version: 1.0
 * <p>
 * <h3><a href="doc-files/DBAccess组件使用说明.doc">组件使用说明下载[DBAccess组件使用说明.doc]</a></h3>
 * <p>
 * <p>
 * <h3>连接多个数据库连接池</h3>
 * 
 * <pre>
 * Dbaccess 组件支持连接多个数据库。在dbpool.xml中配置多个数据库连接池即可。
 * 注意：
 * 1、获取连接时，Connection conn = _dba.getConnection(“对应连接池的名称”); 
 * 2、释放连接池，_dba.freeConnection(conn，“对应连接池的名称”);
 * 3、连接多个数据库时不要嵌套。
 * </pre>
 * 
 * <h3>实现数据库连接的用户名和密码不能为明文的功能</h3>
 * 根据数据库连接的用户名和密码获取对应的密文，例如原始数据库连接配置dbpool.xml如下：<br>
 * 
 * <pre>
 * &lt;?xml version=&quot;1.0&quot; encoding=&quot;gb2312&quot;?&gt;
 * &lt;dbm&gt;
 * 	&lt;logFile&gt;log/db.log&lt;/logFile&gt;
 * 	&lt;pool&gt;
 *         	&lt;!-- 连接池名称 --&gt;
 * 		&lt;name&gt;1st&lt;/name&gt;
 *         	&lt;!-- 驱动 --&gt;
 * 		&lt;driver&gt;oracle.jdbc.driver.OracleDriver&lt;/driver&gt;
 *         	&lt;!-- 数据库url --&gt;
 * 		&lt;url&gt;jdbc:oracle:thin:@192.168.10.161:1521:cjldata&lt;/url&gt;
 *         	&lt;!-- 用户名 --&gt;
 * 		&lt;user&gt;newcproduct&lt;/user&gt;
 *         	&lt;!-- 密码 --&gt;
 * 		&lt;password&gt;newcproductpass&lt;/password&gt;
 *         	&lt;!-- 最大链接数 --&gt;
 * 		&lt;maxConn&gt;2&lt;/maxConn&gt;
 *         	&lt;!-- 最小链接数 --&gt;
 * 		&lt;minConn&gt;2&lt;/minConn&gt;
 *         	&lt;!-- 初始链接数 --&gt;
 * 		&lt;defConn&gt;2&lt;/defConn&gt;
 * 	&lt;/pool&gt;
 * &lt;/dbm&gt;
 * </pre>
 * 
 * 使用命令如：java -Djava.endorsed.dirs=/usr/local/program/cproduct-000/japp/libs/
 * com.haocom.util.DBAccess<br>
 * 若包是放在jre环境里的，则可以直接执行命令如：/usr/local/program/cproduct-000/japp/jdk1.6.0_17/jre/
 * bin/java com.haocom.util.DBAccess<br>
 * 此时屏蔽会输出“username:”，输入newcproduct后敲回车，此时屏幕会输出“password:
 * ”，输入newcproductpass后敲回车。最后屏幕显示如下：<br>
 * <ul>
 * <li>对应数据库配置文件中的username为:d3026488e01b85a21c7b4b369e8d80fb</li>
 * <li>对应数据库配置文件中的password为:d3026488e01b85a2a327d21a6ca8d921</li>
 * </ul>
 * 将这里的用户名和密码分别替换dbpool.xml中对应的用户名密码即可。<br>
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
			String key = CByte.bytes2Hex("香格里拉".getBytes());
			username = DES.encrypt(key, username);
			password = DES.encrypt(key, password);
			System.out.println("对应数据库配置文件中的username为:" + username);
			System.out.println("对应数据库配置文件中的password为:" + password);

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

	/** 数据库连接池 */
	private DatabasePools dbPools;

	/** 默认连接池 */
	private String defPoolsName;

	/** 数据库监视器 */
	DBAccessMonitor monitor;

	/** 连接池名称 */
	private String[] poolsName;

	/**
	 * 默认构造函数 定义默认连接池名称为配置文件中的第一个连接池
	 */
	public DBAccess() {
		this.dbPools = DatabasePools.getInstance();
		this.poolsName = dbPools.getPoolName();
		this.defPoolsName = dbPools.getDefaultPoolName();
		this.monitor = DBAccessMonitor.getInstance();
	}

	/**
	 * 检测输入的连接池名字是否正确
	 * 
	 * @param name
	 *            连接池名称
	 * @return 表示输入的连接池名字是否正确
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
	 * 释放默认的连接池的连接
	 * 
	 * @param con
	 *            释放的连接
	 */
	public void freeConnection(Connection con) { // 释放连接到默认的连接池
		freeConnection(con, defPoolsName);
	}

	/**
	 * 释放连接到指定的连接池
	 * 
	 * @param con
	 *            要释放的连接
	 * @param poolName
	 *            连接池名称
	 */
	public void freeConnection(Connection con, String poolName) {
		// 记录执行堆栈
		monitor.recordFreeConnection(poolName);
		//
		if (!checkPoolName(poolName)) {
			DatabasePools.log(Thread.currentThread().getName() + "	[" + poolName + "] is error in freeConnection()!");
			poolName = defPoolsName;
		}
		dbPools.freeConnection(poolName, con);
	}

	/**
	 * 取得数据库访问的次数
	 * 
	 * @return 次数
	 */
	public int getAccessNum() {
		return dbPools.getAccessNum();
	}

	/**
	 * 取得默认连接池的一个连接
	 * 
	 * @return 可用的连接
	 */
	public Connection getConnection() { // 取得默认的连接
		return getConnection(defPoolsName);
	}

	/**
	 * 取得指定连接池的连接
	 * 
	 * @param poolName
	 *            连接池名称
	 * @return 可用的连接
	 */
	public Connection getConnection(String poolName) {
		// 记录执行堆栈
		monitor.recordGetConnection(poolName);
		// 取得指定连接池的连接
		if (!checkPoolName(poolName)) {
			DatabasePools.log(Thread.currentThread().getName() + "	[" + poolName + "] is error in getConnetion()!");
			poolName = defPoolsName;
		}
		Connection con = null;
		try {
			while (true) {
				con = dbPools.getConnection(poolName, 1000);
				if (con == null) {
					Thread.sleep(1000); // 如果没有取到，歇歇再取
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
	 * 取得默认连接池名称
	 * 
	 * @return 连接池名称
	 */
	public String getDefPoolName() {
		return defPoolsName;
	}

	/**
	 * 取得默认连接池的空闲连接个数
	 * 
	 * @return 空闲连接个数
	 */
	public int getFreeConNum() {
		return dbPools.getFreeConNum(defPoolsName);
	}

	/**
	 * 取得指定连接池的空闲连接个数
	 * 
	 * @param poolName
	 *            连接池名称
	 * @return 空闲连接个数
	 */
	public int getFreeConNum(String poolName) {
		return dbPools.getFreeConNum(poolName);
	}

	/**
	 * 取得默认连接池的被使用连接个数
	 * 
	 * @return 被使用连接个数
	 */
	public int getUsedConNum() {
		return dbPools.getUsedConNum(defPoolsName);
	}

	/**
	 * 取得指定连接池的被使用连接个数
	 * 
	 * @param poolName
	 *            连接池名称
	 * @return 被使用连接个数
	 */
	public int getUsedConNum(String poolName) {
		return dbPools.getUsedConNum(poolName);
	}

	/**
	 * 释放所有连接
	 */
	public void release() {
		dbPools.release();
	}

	/**
	 * 设置默认连接池名称
	 * 
	 * @param name
	 *            默认连接池名称
	 * @return true,将输入参数设置为默认连接池名；false，表示输入的连接池名字不正确，未将输入参数设置为默认连接池名
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
