package com.haocom.util;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;

import com.haocom.util.security.DES;

/**
 * 数据库连接管理. <br>
 * 实现数据库连接池管理，支持多类数据库，支持多个数据库.
 * <p>
 * Copyright: Copyright (c) Sep 16, 2008
 * <p>
 * Company:
 * <p>
 * Author: ywh
 * <p>
 * Version: 1.0
 */

class DatabasePools {

	/**
	 * 内部类，创建和保持数据库链接
	 */
	class DBConnectionPool {

		/** 已用连接数 */
		private int checkedOut;

		/** 默认连接数 */
		private int defaultConn;

		/** 数据库驱动 */
		private Driver driver;

		/** 可用连接数 */
		private Vector freeConnections = new Vector();

		/** 最大连接数 */
		private int maxConn;

		/** 最小连接数 */
		private int minConn;

		/** 数据库名称 */
		private String name;

		/** 登陆密码 */
		private String password;

		/** 数据库路径 */
		private String URL;

		/** 用户名称 */
		private String user;

		/**
		 * 内部类构造函数
		 * 
		 * @param drv
		 *            使用的数据库连接驱动
		 * @param name
		 *            连接池名
		 * @param URL
		 *            数据库实例URL
		 * @param user
		 *            用户名
		 * @param password
		 *            密码
		 * @param maxConn
		 *            最大连接数
		 * @param minConn
		 *            最小连接数
		 * @param defaultConn
		 *            默认连接数
		 */
		public DBConnectionPool(Driver drv, String name, String URL, String user, String password, int maxConn, int minConn, int defaultConn) {
			this.driver = drv;
			this.name = name;
			this.URL = URL;
			this.user = user;
			this.password = password;
			this.maxConn = maxConn;
			this.minConn = minConn;
			this.defaultConn = defaultConn;
			initConnections();
		}

		/**
		 * 释放数据库链接，把链接还原到池中，完了以后调用notifyAll()方法
		 * 
		 * @param con
		 *            被使用的连接
		 */
		public synchronized void freeConnection(Connection con) {
			int free = freeConnections.size();
			if (con == null || ((checkedOut + free > maxConn) && (free >= minConn))) {
				try {
					if (con != null) {
						con.close();
						log("Closed connection for pool " + name);
					} else {
						log("Thread Name :" + Thread.currentThread().getName() + " Closed Connecion is null " + name);
					}
				}
				catch (SQLException e) {
					log(e.getMessage());
				}
			} else {
				Statement stmt = null;
				try {
					stmt = con.createStatement();
					stmt.close();
					freeConnections.addElement(con);
				}
				catch (Exception e) {
					if (stmt != null) {

						try {
							stmt.close();
						}
						catch (SQLException ex) {
						}

						try {
							con.close();
						}
						catch (SQLException ex) {
						}
					}
				}
			}
			checkedOut--;
			notifyAll();
		}

		/**
		 * 获取一个链接
		 * 
		 * @return 数据库连接
		 */
		public synchronized Connection getConnection() {
			Connection con = getFreeConnection();
			if (con != null) {
				checkedOut++;
			}
			return con;
		}

		/**
		 * 获取数据库链接，如果链接都被占用的话wait，直到其它占用链接的地方把链接释放回来。
		 * 
		 * @param timeout
		 *            wait的超时时间
		 * @return 数据库连接
		 */
		public synchronized Connection getConnection(long timeout) {
			long startTime = System.currentTimeMillis();
			Connection con = null;
			while ((con = getConnection()) == null) {
				try {
					wait(timeout); // 由notifyAll()唤醒或时间到自动唤醒
				}
				catch (InterruptedException e) {
				}
				if ((System.currentTimeMillis() - startTime) >= timeout) {
					// Timeout has expired
					return null;
				}
			}
			return con;
		}

		/**
		 * 获取一个链接
		 * 
		 * @return 数据库连接
		 */
		private Connection getFreeConnection() {
			Connection con = null;
			if (freeConnections.size() > 0) {
				con = (Connection) freeConnections.firstElement();
				freeConnections.removeElementAt(0);
				Statement stmt = null;
				try {

					if (con == null || con.isClosed()) {
						log("Removed bad connection in:" + Thread.currentThread().getName());

						con = getFreeConnection();
					} else {
						stmt = con.createStatement();
						stmt.close();
					}
				}
				catch (SQLException e) {
					log(e.getMessage());
					if (stmt != null) {
						try {
							stmt.close();
						}
						catch (Exception ex) {
						}

						try {
							con.close();
						}
						catch (Exception ex) {
						}
					}

					con = getFreeConnection();
				}
			} else if (maxConn == 0 || checkedOut < maxConn) { // 如果maxCon为零则可无限增大
				con = newConnection();
			}
			return con;
		}

		/**
		 * 获取空闲的连接个数
		 * 
		 * @return 空闲的连接个数
		 */
		public int getFreeConNum() {
			return freeConnections.size();
		}

		/**
		 * 获取正在被使用的连接个数
		 * 
		 * @return 正在被使用的连接个数
		 */
		public int getUsedConNum() {
			return checkedOut;
		}

		/**
		 * 初始化数据库链接
		 */
		private void initConnections() {
			for (int i = 0; i < defaultConn; i++) {
				freeConnections.addElement(newConnection());
			}
		}

		/**
		 * 创建一个新的链接
		 * 
		 * @return 数据库连接
		 */
		private Connection newConnection() {
			Connection con = null;
			try {
				DriverManager.registerDriver(this.driver);
				if (user == null) {
					con = DriverManager.getConnection(URL);
				} else {
					con = DriverManager.getConnection(URL, user, password);
				}
				log("Created a new connection in pool " + name);
			}
			catch (SQLException e) {
				System.err.println(e.getMessage());
				log(e.getMessage());
				return null;
			}
			return con;
		}

		/**
		 * 关闭所有链接
		 */
		public synchronized void release() {
			Enumeration allConnections = freeConnections.elements();
			while (allConnections.hasMoreElements()) {
				Connection con = (java.sql.Connection) allConnections.nextElement();
				try {
					con.close();
					log("Closed connection for pool " + name);
				}
				catch (SQLException e) {
					log(e.getMessage());
				}
			}
			freeConnections.removeAllElements();
		}
	}

	/** 连接池使用者的个数 */
	private static int clients;

	/** 配置文件名 */
	private static String confFileName;

	/** 数据库连接池实例 */
	private static DatabasePools instance; // The single instance

	/** 日志文件名 */
	private static String logFile;

	/**
	 * 获得数据库连接池管理器实例
	 * 
	 * @return 一个数据库连接池实例
	 */
	static synchronized public DatabasePools getInstance() {
		if (instance == null) {
			confFileName = "config/dbpool.xml";
			instance = new DatabasePools();
			DBAccessMonitor.init();
		}
		clients++;
		return instance;
	}

	/**
	 * 获得数据库连接池管理器实例
	 * 
	 * @param fileName
	 *            配置文件名
	 * @return 一个数据库连接池实例
	 */
	static synchronized public DatabasePools getInstance(String fileName) {
		if (instance == null) {
			confFileName = fileName;
			instance = new DatabasePools();
			DBAccessMonitor.init();
		}
		clients++;
		return instance;
	}

	/**
	 * 数据库连接池的内部日志输出
	 * 
	 * @param msg
	 *            日志信息
	 */
	public static void log(String msg) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(new Date().toString());
		buffer.append("	");
		buffer.append(msg);
		buffer.append("\r\n");
		FileTools.mkDir(logFile);
		try {
			FileTools.appendTxt(buffer.toString(), logFile);
		}
		catch (Exception e) {
			// do nothing
		}
	}

	/** 默认连接池名称 */
	private String defaultPoolName;

	/** 连接池组 */
	private Hashtable pools = new Hashtable();

	/** 连接池相关配置 */
	private Hashtable poolsCfg = new Hashtable();

	/**
	 * 单例模式为私有构造器
	 */
	private DatabasePools() {
		initConfig();
		createPools();
	}

	/**
	 * 创建数据库链接池。根据配置项，实例化DatabasePools
	 */
	private void createPools() {
		Enumeration dbNames = poolsCfg.keys();
		Vector dbElement = new Vector(7);
		while (dbNames.hasMoreElements()) {
			String poolName = (String) dbNames.nextElement();
			dbElement = (Vector) poolsCfg.get(poolName);
			Driver drv = (Driver) dbElement.get(0);
			String url = (String) dbElement.get(1);
			String user = (String) dbElement.get(2);
			String password = (String) dbElement.get(3);
			String maxconn = (String) dbElement.get(4);
			String minconn = (String) dbElement.get(5);
			String defconn = (String) dbElement.get(6);
			int max, min, def;
			try {
				max = Integer.valueOf(maxconn.trim()).intValue();
				min = Integer.valueOf(minconn.trim()).intValue();
				def = Integer.valueOf(defconn.trim()).intValue();
			}
			catch (Exception e) {
				System.err.println("Invalid maxconn value " + maxconn + " for " + poolName);
				System.err.println("Invalid minconn value " + minconn + " for " + poolName);
				System.err.println("Invalid minconn value " + defconn + " for " + poolName);
				max = 3;
				min = 1;
				def = 2;
			}
			DBConnectionPool pool = new DBConnectionPool(drv, poolName, url.trim(), user, password, max, min, def);
			pools.put(poolName, pool);
			log("Initialized pool " + poolName);
		}
	}

	/**
	 * 释放指定连接池里的指定连接
	 * 
	 * @param name
	 *            连接池名称
	 * @param con
	 *            指定连接
	 */
	public void freeConnection(String name, Connection con) {
		DBConnectionPool pool = (DBConnectionPool) pools.get(name);
		if (pool != null) {
			pool.freeConnection(con);
		}
	}

	/**
	 * 获得数据库连接池的使用者个数
	 * 
	 * @return 使用者个数
	 */
	public int getAccessNum() {
		return clients;
	}

	/**
	 * 获得指定连接池的一个连接
	 * 
	 * @param name
	 *            连接池名称
	 * @return 数据库连接
	 */
	public Connection getConnection(String name) {
		DBConnectionPool pool = (DBConnectionPool) pools.get(name);
		if (pool != null) {
			return pool.getConnection();
		}
		return null;
	}

	/**
	 * 在指定时间内获取指定连接池的一个连接
	 * 
	 * @param name
	 *            连接池名称
	 * @param time
	 *            时间
	 * @return 数据库连接
	 */
	public Connection getConnection(String name, long time) {

		DBConnectionPool pool = (DBConnectionPool) pools.get(name);
		if (pool != null) {
			return pool.getConnection(time);
		}
		return null;
	}

	/**
	 * 获取 defaultPoolName
	 * 
	 * @return defaultPoolName
	 */
	public String getDefaultPoolName() {
		return defaultPoolName;
	}

	/**
	 * 获得指定连接池空闲的连接个数
	 * 
	 * @param poolName
	 *            连接池名
	 * @return 指定连接池空闲的连接个数
	 */
	public int getFreeConNum(String poolName) {
		DBConnectionPool pool = (DBConnectionPool) pools.get(poolName);
		int size = 0;
		if (pool != null) {
			size = pool.getFreeConNum();
		}
		return size;
	}

	/**
	 * 枚举连接池名
	 * 
	 * @return 枚举连接池名
	 */
	public String[] getPoolName() {
		Enumeration names = pools.keys();
		String[] nameList = new String[pools.size()];
		int i = 0;
		while (names.hasMoreElements()) {
			nameList[i++] = (String) names.nextElement();
		}
		return nameList;
	}

	/**
	 * 获得指定连接池正在使用的连接个数
	 * 
	 * @param poolName
	 *            连接池名称
	 * @return 指定连接池正在使用的连接个数
	 */
	public int getUsedConNum(String poolName) {
		DBConnectionPool pool = (DBConnectionPool) pools.get(poolName);
		int size = 0;
		if (pool != null) {
			size = pool.getUsedConNum();
		}
		return size;
	}

	/**
	 * 初始化配置参数
	 */
	private void initConfig() {
		FileInputStream in = null;
		SAXBuilder sax = null;
		Document doc = null;
		try {
			in = new FileInputStream(confFileName);
			sax = new SAXBuilder();
			doc = sax.build(in);
			Element root = doc.getRootElement();
			logFile = root.getChildText("logFile");
			if (logFile == null || logFile.equals("")) {
				logFile = "log/dbm/log.output";
			}
			List e_pool = root.getChildren("pool");
			if (e_pool == null || e_pool.size() <= 0) {
				System.err.println("database config error,system already exit!");
				System.exit(-1);
			}
			String drv = null;
			String name = null;
			String url = null;
			String user = null;
			String pass = null;
			String maxconn = null;
			String minconn = null;
			String defconn = null;
			Vector poolElement = null;
			for (int i = 0; i < e_pool.size(); i++) {
				poolElement = new Vector(8);
				Element pool = (Element) e_pool.get(i);
				drv = pool.getChildText("driver");
				name = pool.getChildText("name");
				if (defaultPoolName == null) {
					defaultPoolName = name;
				}
				url = pool.getChildText("url");
				user = pool.getChildText("user");
				try {
					if (user.length() % 16 == 0) {
						String key = CByte.bytes2Hex("香格里拉".getBytes());
						user = DES.decrypt(key, user);
					}
				}
				catch (Exception ex) {
				}
				pass = pool.getChildText("password").trim();
				try {
					if (pass.length() % 16 == 0) {
						String key = CByte.bytes2Hex("香格里拉".getBytes());
						pass = DES.decrypt(key, pass);
					}
				}
				catch (Exception ex) {
				}
				maxconn = pool.getChildText("maxConn");
				minconn = pool.getChildText("minConn");
				defconn = pool.getChildText("defConn");
				Driver driver = null;
				try {
					driver = (Driver) Class.forName(drv).newInstance();
					log("Registered JDBC driver " + drv);
				}
				catch (Exception e) {
					log(e.getMessage());
				}
				poolElement.add(driver);
				poolElement.add(url);
				poolElement.add(user);
				poolElement.add(pass);
				poolElement.add(maxconn);
				poolElement.add(minconn);
				poolElement.add(defconn);
				poolsCfg.put(name, poolElement);
			}
		}
		catch (Exception e) {
			System.err.println("database config error,system already exit!");
			System.exit(-1);
		}
	}

	/**
	 * 释放所有连接
	 */
	public synchronized void release() {
		if (--clients != 0) {
			return;
		}
		Enumeration allPools = pools.elements();
		while (allPools.hasMoreElements()) {
			DBConnectionPool pool = (DBConnectionPool) allPools.nextElement();
			pool.release();
		}
		Enumeration allPoolElements = poolsCfg.keys();
		while (allPoolElements.hasMoreElements()) {
			Vector poolElements = (Vector) allPoolElements.nextElement();
			Driver driver = (Driver) poolElements.get(0);
			try {
				DriverManager.deregisterDriver(driver);
				log("Deregistered JDBC driver " + driver.getClass().getName());
			}
			catch (SQLException e) {
				log(e.getMessage());
			}
		}
	}

	/**
	 * 设置 defaultPoolName
	 * 
	 * @param defaultPoolName
	 */
	public void setDefaultPoolName(String defaultPoolName) {
		this.defaultPoolName = defaultPoolName;
	}
}
