package com.haocom.terminal_segment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.haocom.util.DBAccess;

/**
 * 号段缓存. <br>
 * 缓存<号段、彩信彩信中心ID>的对应关系. <br>
 * 缓存<号段、地区编码>的对应关系. <br>
 * 缓存<号段、运营商编码>的对应关系. <br>
 * 缓存<地区编码>的对应关系. <br>
 * <p>
 * Copyright: Copyright (c) 2009-4-20 下午03:04:29
 * <p>
 * Company: 
 * <p>
 * Author: ChengFan
 * <p>
 * Version: 1.0
 * <p>
 */
public class ISDNSegment {

	/**
	 * 自动重新加载地区号段信息线程. <br>
	 * <p>
	 * Copyright: Copyright (c) 2009-4-20 下午03:38:12
	 * <p>
	 * Company: 
	 * <p>
	 * Author: ChengFan
	 * <p>
	 * Version: 1.0
	 * <p>
	 */
	class Loader extends Thread {

		/**
		 * @param loadInterval
		 *            加载间隔(ms)
		 */
		public Loader() {
			setName("ISDNSegment.Loader");
			setDaemon(true);
		}

		@Override
		public void run() {
			long timeMark = System.currentTimeMillis();
			while (true) {
				try {
					sleep(1000 * 10);
					if (Math.abs(timeMark - System.currentTimeMillis()) > reloadInterval) {
						instance.reload();
						timeMark = System.currentTimeMillis();
					}
				}
				catch (Exception ex) {
					logger.error("加载号段信息出错", ex);
				}
			}
		}
	}

	/** 数据库访问对象 */
	public static DBAccess _dba = new DBAccess();

	/** 对象实例 */
	private static ISDNSegment instance;

	static final int SEGMENT_INDEX_MAX = 1900000;

	static final int SEGMENT_INDEX_MIN = 1300000;

	/** 获取对象实例 */
	public static ISDNSegment getInstance() {
		return instance;
	}

	/**
	 * 初始化对象实例
	 * 
	 * @param defaultAreaCode
	 * @param reloadInterval
	 * @throws Exception
	 */
	public synchronized static void init(String defaultAreaCode, String defaultOperatorCode, String defaultMmscId, long reloadInterval)
	        throws Exception {
		if (instance != null) {
			return;
		}
		instance = new ISDNSegment(defaultAreaCode, defaultOperatorCode, defaultMmscId, reloadInterval);
	}

	private String[] areaCodes;

	private Set<String> areaCodeSet;

	private String defaultAreaCode;

	private String defaultMmscId;

	private String defaultOperatorCode;

	/** 日志记录器 */
	private Logger logger = Logger.getLogger(getClass().getSimpleName());

	private String[] mmscIds;

	private String[] operatorCodes;

	/** 加载间隔(ms) */
	private long reloadInterval = 0;

	/**
	 * 对象创建
	 */
	private ISDNSegment(String defaultAreaCode, String defaultOperatorCode, String defaultMmscId, long reloadInterval) throws Exception {
		this.defaultAreaCode = defaultAreaCode;
		this.defaultMmscId = defaultMmscId;
		this.defaultOperatorCode = defaultOperatorCode;
		this.reloadInterval = reloadInterval;
		reload();
		new Loader().start();
	}

	/**
	 * 获取号码对应的地区编码
	 * 
	 * @param terminalId
	 * @return
	 */
	public String getAreaCode(String terminalId) {
		return getAreaCode(terminalId, defaultAreaCode);
	}

	/**
	 * 获取号码对应的地区编码
	 * 
	 * @param terminalId
	 * @param defaultValue
	 * @return
	 */
	public String getAreaCode(String terminalId, String defaultValue) {
		try {
			String segment;
			if (terminalId.length() > 7) {
				segment = terminalId.substring(0, 7);
			} else {
				segment = terminalId;
			}
			int index = Integer.parseInt(segment) - SEGMENT_INDEX_MIN;
			String data = areaCodes[index];
			return (data == null) ? defaultValue : data;
		}
		catch (Exception ex) {
			return defaultValue;
		}
	}

	/**
	 * 获取 defaultAreaCode
	 * 
	 * @return defaultAreaCode
	 */
	public String getDefaultAreaCode() {
		return this.defaultAreaCode;
	}

	/**
	 * 获取 defaultMmscId
	 * 
	 * @return defaultMmscId
	 */
	public String getDefaultMmscId() {
		return this.defaultMmscId;
	}

	/**
	 * 获取 defaultOperatorCode
	 * 
	 * @return defaultOperatorCode
	 */
	public String getDefaultOperatorCode() {
		return this.defaultOperatorCode;
	}

	/**
	 * 获取号段对应的彩信中心编号
	 * 
	 * @param terminalId
	 * @return
	 */
	public String getMmscId(String terminalId) {
		return getMmscId(terminalId, defaultMmscId);
	}

	/**
	 * 获取号段对应的彩信中心编号
	 * 
	 * @param terminalId
	 * @param defaultValue
	 * @return
	 */
	public String getMmscId(String terminalId, String defaultValue) {
		try {
			String segment;
			if (terminalId.length() > 7) {
				segment = terminalId.substring(0, 7);
			} else {
				segment = terminalId;
			}
			int index = Integer.parseInt(segment) - SEGMENT_INDEX_MIN;
			String data = mmscIds[index];
			return (data == null) ? defaultValue : data;
		}
		catch (Exception ex) {
			return defaultValue;
		}
	}

	/**
	 * 获取号码的运营商编码
	 * 
	 * @param terminalId
	 * @return
	 */
	public String getOperatorCode(String terminalId) {
		return getOperatorCode(terminalId, defaultOperatorCode);
	}

	/**
	 * 获取号码的运营商编码
	 * 
	 * @param terminalId
	 *            号码（大于等于7位，可填写号段）
	 * @param defaultValue
	 *            默认值
	 * @return
	 */
	public String getOperatorCode(String terminalId, String defaultValue) {
		try {
			String segment;
			if (terminalId.length() > 7) {
				segment = terminalId.substring(0, 7);
			} else {
				segment = terminalId;
			}
			int index = Integer.parseInt(segment) - SEGMENT_INDEX_MIN;
			String data = operatorCodes[index];
			return (data == null) ? defaultValue : data;
		}
		catch (Exception ex) {
			return defaultValue;
		}
	}

	/**
	 * 获取 reloadInterval
	 * 
	 * @return reloadInterval
	 */
	public long getReloadInterval() {
		return this.reloadInterval;
	}

	/**
	 * 判断地区编码是否在号段表中
	 * 
	 * @param terminalId
	 * @return
	 */
	public boolean inAreaCodes(String areaCode) {
		try {
			return areaCodeSet.contains(areaCode);
		}
		catch (Exception ex) {
			return false;
		}
	}

	/**
	 * 判断号码是否在号段表中
	 * 
	 * @param terminalId
	 * @return
	 */
	public boolean inSegment(String terminalId) {
		try {
			String segment;
			if (terminalId.length() > 7) {
				segment = terminalId.substring(0, 7);
			} else {
				segment = terminalId;
			}
			int index = Integer.parseInt(segment) - SEGMENT_INDEX_MIN;
			return null != operatorCodes[index];
		}
		catch (Exception ex) {
			return false;
		}
	}

	private void reload() throws Exception {
		Connection conn = _dba.getConnection();
		try {
			PreparedStatement st = conn.prepareStatement("select segment_code, mmsc_id, area_code, operator_code from t_sys_segment");
			try {
				st.setFetchSize(500);
				//
				String[] tmpMmscIds = new String[SEGMENT_INDEX_MAX - SEGMENT_INDEX_MIN];
				String[] tmpAreaCodes = new String[SEGMENT_INDEX_MAX - SEGMENT_INDEX_MIN];
				String[] tmpOperatorCodes = new String[SEGMENT_INDEX_MAX - SEGMENT_INDEX_MIN];
				Map<String, String> mmscMap = new HashMap<String, String>();
				Map<String, String> areaCodeMap = new HashMap<String, String>();
				Map<String, String> operatorCodeMap = new HashMap<String, String>();
				//
				ResultSet rs = st.executeQuery();
				int segment;
				String areaCode;
				String mmscId;
				String operatorCode;
				//
				while (rs.next()) {
					segment = rs.getInt("segment_code");
					int index = segment - SEGMENT_INDEX_MIN;
					try {
						//
						areaCode = rs.getString("area_code");
						if (areaCodeMap.containsKey(areaCode)) {
							areaCode = areaCodeMap.get(areaCode);
						} else {
							areaCodeMap.put(areaCode, areaCode);
						}
						tmpAreaCodes[index] = areaCode;
						//
						mmscId = rs.getString("mmsc_id");
						if (mmscMap.containsKey(mmscId)) {
							mmscId = mmscMap.get(mmscId);
						} else {
							mmscMap.put(mmscId, mmscId);
						}
						tmpMmscIds[index] = mmscId;
						//
						operatorCode = rs.getString("operator_code");
						if (operatorCodeMap.containsKey(operatorCode)) {
							operatorCode = operatorCodeMap.get(operatorCode);
						} else {
							operatorCodeMap.put(operatorCode, operatorCode);
						}
						tmpOperatorCodes[index] = operatorCode;
					}
					catch (Exception ex) {
						logger.error("非法号段:" + segment, ex);
					}
				}
				//
				this.areaCodes = tmpAreaCodes;
				this.mmscIds = tmpMmscIds;
				this.operatorCodes = tmpOperatorCodes;
				this.areaCodeSet = areaCodeMap.keySet();
			}
			catch (Exception ex) {
				throw ex;
			}
			finally {
				st.close();
			}
		}
		catch (Exception ex) {
			throw ex;
		}
		finally {
			_dba.freeConnection(conn);
		}
	}

	/**
	 * 设置 defaultAreaCode
	 * 
	 * @param defaultAreaCode
	 */
	public void setDefaultAreaCode(String defaultAreaCode) {
		this.defaultAreaCode = defaultAreaCode;
	}

	/**
	 * 设置 defaultMmscId
	 * 
	 * @param defaultMmscId
	 */
	public void setDefaultMmscId(String defaultMmscId) {
		this.defaultMmscId = defaultMmscId;
	}

	/**
	 * 设置 defaultOperatorCode
	 * 
	 * @param defaultOperatorCode
	 */
	public void setDefaultOperatorCode(String defaultOperatorCode) {
		this.defaultOperatorCode = defaultOperatorCode;
	}

	/**
	 * 设置 reloadInterval
	 * 
	 * @param reloadInterval
	 */
	public void setReloadInterval(long reloadInterval) {
		if (reloadInterval < 1000 * 10) {
			reloadInterval = 1000 * 10;
		}
		this.reloadInterval = reloadInterval;
	}
}
