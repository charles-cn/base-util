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
 * ���ݿ����ӹ���. <br>
 * ʵ�����ݿ����ӳع���֧�ֶ������ݿ⣬֧�ֶ�����ݿ�.
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
	 * �ڲ��࣬�����ͱ������ݿ�����
	 */
	class DBConnectionPool {

		/** ���������� */
		private int checkedOut;

		/** Ĭ�������� */
		private int defaultConn;

		/** ���ݿ����� */
		private Driver driver;

		/** ���������� */
		private Vector freeConnections = new Vector();

		/** ��������� */
		private int maxConn;

		/** ��С������ */
		private int minConn;

		/** ���ݿ����� */
		private String name;

		/** ��½���� */
		private String password;

		/** ���ݿ�·�� */
		private String URL;

		/** �û����� */
		private String user;

		/**
		 * �ڲ��๹�캯��
		 * 
		 * @param drv
		 *            ʹ�õ����ݿ���������
		 * @param name
		 *            ���ӳ���
		 * @param URL
		 *            ���ݿ�ʵ��URL
		 * @param user
		 *            �û���
		 * @param password
		 *            ����
		 * @param maxConn
		 *            ���������
		 * @param minConn
		 *            ��С������
		 * @param defaultConn
		 *            Ĭ��������
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
		 * �ͷ����ݿ����ӣ������ӻ�ԭ�����У������Ժ����notifyAll()����
		 * 
		 * @param con
		 *            ��ʹ�õ�����
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
		 * ��ȡһ������
		 * 
		 * @return ���ݿ�����
		 */
		public synchronized Connection getConnection() {
			Connection con = getFreeConnection();
			if (con != null) {
				checkedOut++;
			}
			return con;
		}

		/**
		 * ��ȡ���ݿ����ӣ�������Ӷ���ռ�õĻ�wait��ֱ������ռ�����ӵĵط��������ͷŻ�����
		 * 
		 * @param timeout
		 *            wait�ĳ�ʱʱ��
		 * @return ���ݿ�����
		 */
		public synchronized Connection getConnection(long timeout) {
			long startTime = System.currentTimeMillis();
			Connection con = null;
			while ((con = getConnection()) == null) {
				try {
					wait(timeout); // ��notifyAll()���ѻ�ʱ�䵽�Զ�����
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
		 * ��ȡһ������
		 * 
		 * @return ���ݿ�����
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
			} else if (maxConn == 0 || checkedOut < maxConn) { // ���maxConΪ�������������
				con = newConnection();
			}
			return con;
		}

		/**
		 * ��ȡ���е����Ӹ���
		 * 
		 * @return ���е����Ӹ���
		 */
		public int getFreeConNum() {
			return freeConnections.size();
		}

		/**
		 * ��ȡ���ڱ�ʹ�õ����Ӹ���
		 * 
		 * @return ���ڱ�ʹ�õ����Ӹ���
		 */
		public int getUsedConNum() {
			return checkedOut;
		}

		/**
		 * ��ʼ�����ݿ�����
		 */
		private void initConnections() {
			for (int i = 0; i < defaultConn; i++) {
				freeConnections.addElement(newConnection());
			}
		}

		/**
		 * ����һ���µ�����
		 * 
		 * @return ���ݿ�����
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
		 * �ر���������
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

	/** ���ӳ�ʹ���ߵĸ��� */
	private static int clients;

	/** �����ļ��� */
	private static String confFileName;

	/** ���ݿ����ӳ�ʵ�� */
	private static DatabasePools instance; // The single instance

	/** ��־�ļ��� */
	private static String logFile;

	/**
	 * ������ݿ����ӳع�����ʵ��
	 * 
	 * @return һ�����ݿ����ӳ�ʵ��
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
	 * ������ݿ����ӳع�����ʵ��
	 * 
	 * @param fileName
	 *            �����ļ���
	 * @return һ�����ݿ����ӳ�ʵ��
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
	 * ���ݿ����ӳص��ڲ���־���
	 * 
	 * @param msg
	 *            ��־��Ϣ
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

	/** Ĭ�����ӳ����� */
	private String defaultPoolName;

	/** ���ӳ��� */
	private Hashtable pools = new Hashtable();

	/** ���ӳ�������� */
	private Hashtable poolsCfg = new Hashtable();

	/**
	 * ����ģʽΪ˽�й�����
	 */
	private DatabasePools() {
		initConfig();
		createPools();
	}

	/**
	 * �������ݿ����ӳء����������ʵ����DatabasePools
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
	 * �ͷ�ָ�����ӳ����ָ������
	 * 
	 * @param name
	 *            ���ӳ�����
	 * @param con
	 *            ָ������
	 */
	public void freeConnection(String name, Connection con) {
		DBConnectionPool pool = (DBConnectionPool) pools.get(name);
		if (pool != null) {
			pool.freeConnection(con);
		}
	}

	/**
	 * ������ݿ����ӳص�ʹ���߸���
	 * 
	 * @return ʹ���߸���
	 */
	public int getAccessNum() {
		return clients;
	}

	/**
	 * ���ָ�����ӳص�һ������
	 * 
	 * @param name
	 *            ���ӳ�����
	 * @return ���ݿ�����
	 */
	public Connection getConnection(String name) {
		DBConnectionPool pool = (DBConnectionPool) pools.get(name);
		if (pool != null) {
			return pool.getConnection();
		}
		return null;
	}

	/**
	 * ��ָ��ʱ���ڻ�ȡָ�����ӳص�һ������
	 * 
	 * @param name
	 *            ���ӳ�����
	 * @param time
	 *            ʱ��
	 * @return ���ݿ�����
	 */
	public Connection getConnection(String name, long time) {

		DBConnectionPool pool = (DBConnectionPool) pools.get(name);
		if (pool != null) {
			return pool.getConnection(time);
		}
		return null;
	}

	/**
	 * ��ȡ defaultPoolName
	 * 
	 * @return defaultPoolName
	 */
	public String getDefaultPoolName() {
		return defaultPoolName;
	}

	/**
	 * ���ָ�����ӳؿ��е����Ӹ���
	 * 
	 * @param poolName
	 *            ���ӳ���
	 * @return ָ�����ӳؿ��е����Ӹ���
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
	 * ö�����ӳ���
	 * 
	 * @return ö�����ӳ���
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
	 * ���ָ�����ӳ�����ʹ�õ����Ӹ���
	 * 
	 * @param poolName
	 *            ���ӳ�����
	 * @return ָ�����ӳ�����ʹ�õ����Ӹ���
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
	 * ��ʼ�����ò���
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
						String key = CByte.bytes2Hex("�������".getBytes());
						user = DES.decrypt(key, user);
					}
				}
				catch (Exception ex) {
				}
				pass = pool.getChildText("password").trim();
				try {
					if (pass.length() % 16 == 0) {
						String key = CByte.bytes2Hex("�������".getBytes());
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
	 * �ͷ���������
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
	 * ���� defaultPoolName
	 * 
	 * @param defaultPoolName
	 */
	public void setDefaultPoolName(String defaultPoolName) {
		this.defaultPoolName = defaultPoolName;
	}
}
