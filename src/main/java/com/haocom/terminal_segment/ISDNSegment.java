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
 * �Ŷλ���. <br>
 * ����<�ŶΡ����Ų�������ID>�Ķ�Ӧ��ϵ. <br>
 * ����<�ŶΡ���������>�Ķ�Ӧ��ϵ. <br>
 * ����<�ŶΡ���Ӫ�̱���>�Ķ�Ӧ��ϵ. <br>
 * ����<��������>�Ķ�Ӧ��ϵ. <br>
 * <p>
 * Copyright: Copyright (c) 2009-4-20 ����03:04:29
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
	 * �Զ����¼��ص����Ŷ���Ϣ�߳�. <br>
	 * <p>
	 * Copyright: Copyright (c) 2009-4-20 ����03:38:12
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
		 *            ���ؼ��(ms)
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
					logger.error("���غŶ���Ϣ����", ex);
				}
			}
		}
	}

	/** ���ݿ���ʶ��� */
	public static DBAccess _dba = new DBAccess();

	/** ����ʵ�� */
	private static ISDNSegment instance;

	static final int SEGMENT_INDEX_MAX = 1900000;

	static final int SEGMENT_INDEX_MIN = 1300000;

	/** ��ȡ����ʵ�� */
	public static ISDNSegment getInstance() {
		return instance;
	}

	/**
	 * ��ʼ������ʵ��
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

	/** ��־��¼�� */
	private Logger logger = Logger.getLogger(getClass().getSimpleName());

	private String[] mmscIds;

	private String[] operatorCodes;

	/** ���ؼ��(ms) */
	private long reloadInterval = 0;

	/**
	 * ���󴴽�
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
	 * ��ȡ�����Ӧ�ĵ�������
	 * 
	 * @param terminalId
	 * @return
	 */
	public String getAreaCode(String terminalId) {
		return getAreaCode(terminalId, defaultAreaCode);
	}

	/**
	 * ��ȡ�����Ӧ�ĵ�������
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
	 * ��ȡ defaultAreaCode
	 * 
	 * @return defaultAreaCode
	 */
	public String getDefaultAreaCode() {
		return this.defaultAreaCode;
	}

	/**
	 * ��ȡ defaultMmscId
	 * 
	 * @return defaultMmscId
	 */
	public String getDefaultMmscId() {
		return this.defaultMmscId;
	}

	/**
	 * ��ȡ defaultOperatorCode
	 * 
	 * @return defaultOperatorCode
	 */
	public String getDefaultOperatorCode() {
		return this.defaultOperatorCode;
	}

	/**
	 * ��ȡ�Ŷζ�Ӧ�Ĳ������ı��
	 * 
	 * @param terminalId
	 * @return
	 */
	public String getMmscId(String terminalId) {
		return getMmscId(terminalId, defaultMmscId);
	}

	/**
	 * ��ȡ�Ŷζ�Ӧ�Ĳ������ı��
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
	 * ��ȡ�������Ӫ�̱���
	 * 
	 * @param terminalId
	 * @return
	 */
	public String getOperatorCode(String terminalId) {
		return getOperatorCode(terminalId, defaultOperatorCode);
	}

	/**
	 * ��ȡ�������Ӫ�̱���
	 * 
	 * @param terminalId
	 *            ���루���ڵ���7λ������д�ŶΣ�
	 * @param defaultValue
	 *            Ĭ��ֵ
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
	 * ��ȡ reloadInterval
	 * 
	 * @return reloadInterval
	 */
	public long getReloadInterval() {
		return this.reloadInterval;
	}

	/**
	 * �жϵ��������Ƿ��ںŶα���
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
	 * �жϺ����Ƿ��ںŶα���
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
						logger.error("�Ƿ��Ŷ�:" + segment, ex);
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
	 * ���� defaultAreaCode
	 * 
	 * @param defaultAreaCode
	 */
	public void setDefaultAreaCode(String defaultAreaCode) {
		this.defaultAreaCode = defaultAreaCode;
	}

	/**
	 * ���� defaultMmscId
	 * 
	 * @param defaultMmscId
	 */
	public void setDefaultMmscId(String defaultMmscId) {
		this.defaultMmscId = defaultMmscId;
	}

	/**
	 * ���� defaultOperatorCode
	 * 
	 * @param defaultOperatorCode
	 */
	public void setDefaultOperatorCode(String defaultOperatorCode) {
		this.defaultOperatorCode = defaultOperatorCode;
	}

	/**
	 * ���� reloadInterval
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
