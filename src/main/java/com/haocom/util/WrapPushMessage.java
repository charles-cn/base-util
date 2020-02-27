package com.haocom.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * WAP Push Proxy Gateway (PPG). <br>
 * ʵ��WAP PPG�Ļ������ܣ�Э���׼WAP-249-PPGService.<br>
 * ����WAP�淶�ĵ�����װPUSH��Ϣ�����Է���HEX����BYTE������������������PPG��Ӧ�ó���ͨ�����õ����巢�͵��ն�.
 * <p>
 * Copyright: Copyright (c) 2010-4-1 ����03:31:49
 * <p>
 * Company: 
 * <p>
 * Author: chenwei@c-platform.com
 * <p>
 * Version: 1.0
 * <p>
 * <h2>���ʹ��˵��:</h2>
 * 
 * <pre>
 * ��wappush��Ϣת��Ϊ��ͨ�Ķ�����Ϣ�����·�
 * 
 * //ʵ����wap������
 * WrapPushMessage wap = new WrapPushMessage();
 * //����wap����
 * wap.setSi_title(taskInfo.getTitle());
 * //����wap url����
 * wap.setSi_url(taskInfo.getContent());
 * //����wap���뷽��,���ر�����wap�ַ�������
 * //���鳤��һ��Ϊ1���������1��Ϊ������wappush
 * String[] content = wap.getPushMessage();
 * 
 * ���ص�ֱֵ�����õ����������ֶΣ�ʵ���û��Ӷ��������·�wap��Ϣ
 * </pre>
 */
public class WrapPushMessage {

	/**
	 * ��Byte�ڴ�������ת��ʮ�������ı���
	 * 
	 * @param arr
	 *            ��ת����Byte������
	 * @return Hex�ı���
	 */
	public static String bytes2HexStr(byte[] arr) {
		String tmp = "";
		StringBuffer sbuf = new StringBuffer();
		for (int i = 0; i < arr.length; i++) {
			// if (arr[i] < 0) {
			// arr[i] += 0x80;
			// }
			int j = arr[i];
			tmp = Integer.toHexString(j);
			if (j < 0) {
				tmp = tmp.substring(6, 8);
			}
			if (tmp.length() == 1) {
				tmp = "0" + tmp;
			}
			// System.out.print(tmp + "-");
			sbuf.append(tmp.toUpperCase());
		}

		return sbuf.toString();
	}

	/**
	 * ����ʮ�������ı����������ַ���ת��Byte������
	 * 
	 * @param vStr
	 *            Hex�ı���
	 * @return ת�����Byte������
	 */
	public static byte[] hexStrToBytesBuf(String vStr) {
		int MaxLen = vStr.length() / 2;
		byte[] btemp = new byte[MaxLen];
		vStr = vStr.trim();
		int strlen = vStr.length();
		int j = strlen / 2;
		if (j == MaxLen) {
			strlen = MaxLen * 2;
		} else if (j > MaxLen) {
			strlen = MaxLen * 2;
		} else if (j < MaxLen) {
			strlen = j * 2;
		}
		ByteBuffer buf = ByteBuffer.allocate(MaxLen);
		char[] c = vStr.toCharArray();
		byte bb;
		String s = null;
		for (int i = 0; i < strlen; i += 2) {
			s = new String(c, i, 2);
			try {
				// System.out.print(s + "=");
				bb = (byte) (Integer.parseInt(s, 16) & 0xFFFFFFFF);
				buf.put(bb);
				// System.out.print(bb + " ");
			}
			catch (NumberFormatException ex) {
				throw new NumberFormatException(ex.toString());
			}

		}
		btemp = buf.array();

		return btemp;
	}

	/** PUSH��Ϣ��ACTIONֵ */
	private int si_action = -1;

	/** PUSH��Ϣ�Ĵ���ʱ�� */
	private Date si_created = null;

	/** PUSH��Ϣ�Ĺ���ʱ�� */
	private Date si_expires = null;

	/** PUSH��Ϣ��Ψһ��ID��ʶ */
	private String si_id = null;

	/** PUSH��Ϣ��˵�������� */
	private String si_title = null;

	/** PUSH��Ϣ�е��û����ʵ�URL��ַ */
	private String si_url = null;

	/**
	 * ���캯��
	 */
	public WrapPushMessage() {
	}

	/**
	 * ����WAP-167-ServiceInd˵��������SI_PUSH��Ϣ��
	 * 
	 * @return ������SI_PUSH��Ϣ��
	 */
	private byte[] encodeContentBody() {
		ByteArrayOutputStream data = new ByteArrayOutputStream();
		try {
			data.write(0x02); // Version number - WBXML version 1.2
			data.write(0x05); // SI 1.0 Public Identifier
			data.write(0x6A); // Charset=UTF-8 (MIBEnum 106)
			data.write(0x00); // Strign table length
			data.write(0x45); // si, with content
			data.write(0xC6); // indication, with content and attributes
			/*
			 * ����Actionֵ��Ĭ��Ϊ8��singal-high signal-none 5 action signal-low 6
			 * action signal-medium 7
			 */
			if (si_action != -1) {
				data.write((byte) this.si_action); //
			} else {
				data.write(0x08); // //<action=signal-high>
			}
			/* ����si_idֵ������Ϊ�գ���ֵΪ��ѡ����Բ����ò�Ӱ��PUSH��Ϣ���� */
			if (si_id != null) {
				data.write(0x11); // si-id indication;
				data.write(0x03); // inline String follows
				data.write(this.si_id.getBytes()); // si-id value
				data.write(0x00); // end of string
			}
			/* ����ҵ����ʵ�URL��ַ,�������� */
			if (this.si_url != null) {
				data.write(0x0C); // equal "http://"
				data.write(0x03); // Inline string follows
				data.write(si_url.getBytes()); // the URL Value
				data.write(0x00); // String table Length
			}
			/* ����Createdʱ��ֵ����ѡ�� */
			if (si_created != null) {
				data.write(0x0A); // created
				data.write(0xC3); // OPAQUE data follows
				byte[] barr = this.encodeDateTime(si_created.getTime());
				data.write(barr.length); // Length field
				data.write(barr); // create date time;
			}
			if (si_expires != null) {
				data.write(0x10); // si-expired
				data.write(0xC3); // OPAQUE data follows
				byte[] barr = this.encodeDateTime(si_expires.getTime());
				data.write(barr.length); // Length field
				data.write(barr); // si-expired date time;
			}
			data.write(0x01); // END (of indication attribute list)
			// ����<indication>Value</indication>��valueֵ
			if (si_title != null) {
				data.write(0x03); // inline the String follows�������Ǳ����ݵ��ִ�����ѡ��
				data.write(si_title.getBytes("UTF-8"));
				data.write(0x00); // end of string
			}
			data.write(0x01); // END (of indication element)
			data.write(0x01); // End of SI element
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		return data.toByteArray();
	}

	/**
	 * ����WAP-167-ServiceInd.pdf�淶������ʱ���ʽ
	 * 
	 * @param ltime
	 *            ʱ��
	 * @return ������ʱ��
	 */
	private byte[] encodeDateTime(long ltime) {
		ByteArrayOutputStream data = new ByteArrayOutputStream();
		Date ts = new Date(ltime);
		java.text.SimpleDateFormat formater = new SimpleDateFormat("yy-MM-dd-HH-mm-ss");
		String stime = formater.format(ts);
		String[] sarr = stime.split("-");
		int hi, lw;
		byte b;
		hi = 2;
		lw = 0;
		b = (byte) ((hi << 4) | lw); // 0010,0000 = 20
		data.write(b);
		for (int i = 0; i < sarr.length; i++) {
			hi = Integer.parseInt(sarr[i].substring(0, 1));
			lw = Integer.parseInt(sarr[i].substring(1, 2));
			b = (byte) ((hi << 4) | lw); // 0010,0000 = 20
			if (i == (sarr.length - 1) && hi == 0 && lw == 0) {
				break;
			} else {
				data.write(b);
			}
		}
		byte[] b1 = data.toByteArray();
		ByteArrayOutputStream data1 = new ByteArrayOutputStream();
		// ɾ��β����00���磺2005 11 23 13��00��00��ӦΪ2005112313
		// ���ǵ�ֻ��ʱ������00��00��00���������ֻ�Ժ���λ����
		data1.write(b1, 0, 4); // ��������д��
		byte[] rebyte = null;
		if (b1[4] == 0 && b1[5] == 0 && b1[6] == 0) {
			rebyte = data1.toByteArray();
		} else {
			data1.write(b1[4]);
			if (b1[5] == 0 && b1[6] == 0) {
				rebyte = data1.toByteArray();
			} else {
				data1.write(b1[5]);
				if (b1[6] != 0) {
					data1.write(b1[6]);
				}
				rebyte = data1.toByteArray();
			}
		}
		// System.out.print("====encode date:");
		// this.byte2HexStr(data.toByteArray());
		return rebyte;
	}

	/**
	 * �Գ���һ���������ݵ���Ϣͷ���б���
	 * 
	 * @param total_packet
	 *            �ܵ���Ϣ����
	 * @param current_packet
	 *            ��ǰ�ڼ�����Ϣ��
	 * @return ����󷵻ص�����
	 */
	private byte[] encodeLWDPHerder(int total_packet, int current_packet) {
		ByteArrayOutputStream data = new ByteArrayOutputStream();
		try {
			data.write(0x0B); // length of UDH, the value not include this byte
			data.write(0x05); // information element identifier (05 =
			// application port addressing scheme, 16-bit
			// address)
			data.write(0x04); // information element length, the value not
			// include this byte
			data.write(0x0B); // High byte
			data.write(0x84); // low byte 0x0B84=Dec 2948 //Destination port
			// 2948 for Push Connectionless Session Service
			data.write(0x23); // High
			data.write(0xF0); // Low 0x23FO = Dec 9200//Source Port 9200 for WAP
			// Connectionless Session Service
			/*
			 * IE: Information Element SAR: Segmentation and Reassembly
			 */
			data.write(0x00); // UDH IE identifier: SAR (0)
			data.write(0x03); // UDH SAR IE length (3)
			data.write(0x01); // Datagram Reference number, random
			data.write((byte) total_packet); // Total number of segments in
			// Datagram
			data.write((byte) current_packet); // Segment count
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		return data.toByteArray();
	}

	/**
	 * ���볤��Ϣ���WSPЭ���PDU����ͷ
	 * 
	 * @param iBodyLength
	 *            ��Ϣ�峤��
	 * @return ����󷵻ص�����
	 */
	private byte[] encodeLWSPHeader(int iBodyLength) {
		byte iHeaderLen = 0;
		byte bodylen = (byte) (iBodyLength);
		ByteArrayOutputStream data = new ByteArrayOutputStream();
		/* ����WAP-230-WSP-20010705-a.pdf���б��� */
		int[] arri = this.encodeUintvarNumber(iBodyLength);
		// arri[0],7bit��ʾ����ֵ������5:������LENGTHֵ������ͷ�ֽ���
		iHeaderLen = (byte) (arri[0] + 5);
		/*
		 * if (iBodyLength < 128) { iHeaderLen = 0x06; // The Header Length } if
		 * (iBodyLength > 127 && iBodyLength < 256) { iHeaderLen = 0x07; // The
		 * Header Length } if (iBodyLength > 255) { iHeaderLen = 0x08; // The
		 * Header Length }
		 */

		try {
			data.write(0x25); // The Transaction id( connectionless WSP )��ѭ��ʹ��
			data.write(0x06); // The PDU Type = PUSH
			data.write(iHeaderLen); // The Header Length
			data.write(0x03); // The Type? or Accept language?
			data.write((byte) (0x2E + 0x80)); // application/vnd.wap.sic? or
			// Lagurage=American
			data.write((byte) (0x01 + 0x80)); // Well-known-charset tag charset=
			data.write((byte) (0x6A + 0x80)); // utf-8 0x6A = Dec 106
			data.write((byte) (0x0D + 0x80)); // The Content Length tag
			/*
			 * For example, the number 0x87A5 (1000 0111 1010 0101) is encoded
			 * in three octets as shown in Figure 12. 1 0000010 1 0001111 0
			 * 0100101 Figure 12. Long Field Length The unsigned integer MUST be
			 * encoded in the smallest encoding possible. In other words, the
			 * encoded value MUST NOT start with an octet with the value 0x80.
			 * In the data unit format descriptions, the data type uintvar will
			 * be used to indicate a variable length integer field. The maximum
			 * size of a uintvar is 32 bits. It will be encoded in no more than
			 * five octets. It MUST be present even if its value is zero. ������WAP
			 * WSP �ĵ��е�˵��������ֻ����ֵҪ������˵�����б��롣
			 */
			for (int j = 1; j <= arri[0]; j++) {
				data.write((byte) (arri[j] + 0x80));
			}
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		return data.toByteArray();
	}

	/**
	 * ��8bit��ֵת����7bit��ʽ�����Բμ�OMA�淶�е�˵��
	 * 
	 * @param lData
	 *            8bit��ֵ
	 * @return 8bit��ֵת����7bit��ʽ�������
	 */
	private int[] encodeUintvarNumber(long lData) {
		int data[] = new int[32];
		long lDivider = 1L;
		int nSize = 0;
		long lNumber = lData;

		for (int i = 0; i < 32; i++)
			data[i] = 0;

		for (int i = 4; i >= 0; i--) {
			lDivider = 1L;
			for (int j = 0; j < i; j++)
				lDivider *= 128L;

			int q = (int) (lNumber / lDivider);
			if (q != 0 || nSize != 0) {
				int r = (int) (lNumber % lDivider);
				data[nSize + 1] = q;
				if (i != 0)
					data[nSize + 1] += 128;
				lNumber = r;
				nSize++;
			}
		}

		data[0] = nSize;
		return data;
	}

	/**
	 * ����WDPЭ�����UDH��Ϣͷ
	 * 
	 * @return ������UDH��Ϣͷ
	 */
	private byte[] encodeWDPHeader() {
		/* �������wap-259-wdp-20010614-a.pdfʵ�� */
		ByteArrayOutputStream data = new ByteArrayOutputStream();
		try {
			data.write(0x06); // length of UDH, the value not include this byte
			data.write(0x05); // information element identifier (05 =
			// application port addressing scheme, 16-bit
			// address)
			data.write(0x04); // information element length, the value not
			// include this byte
			data.write(0x0B); // High byte
			data.write(0x84); // low byte 0x0B84=Dec 2948 //Destination port
			// 2948 for Push Connectionless Session Service
			data.write(0x23); // High
			data.write(0xF0); // Low 0x23FO = Dec 9200//Source Port 9200 for WAP
			// Connectionless Session Service
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		return data.toByteArray();
	}

	/**
	 * ��WSPЭ�����PDU��Ϣͷ��Ϣ
	 * 
	 * @param iBodyLength
	 *            ��Ϣ�峤��
	 * @return ������PDU��Ϣͷ
	 */
	private byte[] encodeWSPHeader(int iBodyLength) {
		/* ����WAP-230-WSP-20010705-a.pdf���б��� */
		ByteArrayOutputStream data = new ByteArrayOutputStream();
		try {
			data.write(0x25); // The Transaction id( connectionless WSP )��ѭ��ʹ��
			data.write(0x06); // The PDU Type = PUSH
			data.write(0x06); // The Header Length
			data.write(0x03); // The Type? or Accept language?
			data.write((byte) (0x2E + 0x80)); // application/vnd.wap.sic? or
			// Lagurage=American
			data.write((byte) (0x01 + 0x80)); // Well-known-charset tag charset=
			data.write((byte) (0x6A + 0x80)); // utf-8 0x6A = Dec 106
			data.write((byte) (0x0D + 0x80)); // The Content Length tag

			data.write((byte) (iBodyLength + 80)); // the Content Length Value
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		return data.toByteArray();
	}

	/**
	 * ����ȡ����Ϣ�ı���HEX��
	 * 
	 * @param si_id
	 *            PUSH��Ϣ��Ψһ��ID��ʶ
	 * @return ȡ����Ϣ�ı���HEX��
	 */
	public String getCancelMessaage(String si_id) {
		ByteArrayOutputStream data = new ByteArrayOutputStream();
		this.setSi_created(null);
		this.setSi_expires(null);
		this.setSi_id(si_id);
		this.setSi_title(null);
		this.setSi_url(null);
		this.setSi_action(0x09); // 0x09 = delete
		byte[] body = this.encodeContentBody();
		try {
			data.write(this.encodeWDPHeader()); // UDH
			data.write(this.encodeWSPHeader(body.length)); // PDU
			data.write(body); // the message body
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		return this.bytes2HexStr(data.toByteArray());
	}

	/**
	 * �����Ѿ�������PUSH��Ϣ��,Ĭ�Ϸ�ʽ
	 * 
	 * @return ������PUSH��Ϣ��
	 */
	public String[] getPushMessage() {
		byte[] body = this.encodeContentBody();
		int iBodyLength = body.length;
		System.out.println("body Length=" + iBodyLength);
		/*
		 * 7: short udh bytes length 9: short pdu bytes length
		 */
		// �ж�push�ܳ����Ƿ񳬹�140���ֽ�
		if ((7 + 9 + iBodyLength) <= 140) {
			byte[] barr_pdu = this.encodeWSPHeader(iBodyLength);
			byte[] barr_udh = this.encodeWDPHeader();
			ByteBuffer buf = ByteBuffer.allocate(barr_udh.length + barr_pdu.length + iBodyLength);
			buf.put(barr_udh).put(barr_pdu).put(body);
			String[] arrStr = new String[1];
			arrStr[0] = this.bytes2HexStr(buf.array());
			return arrStr;
		} else {
			int iTotal_Packet = 0;
			int iLUDH = 12; // ����Ϣ�����ÿ�����ĵ���ϢUDHͷ��ԭ����5���ֽ�
			// ���ڶ���ֽڱ���INTֵ��WSP���б���涨�����Ը���ContentLengthֵ
			// ����PDUͷ�ĳ���
			int iLPDU = 0;
			if (iBodyLength < 128) {
				iLPDU = 9;
			}
			if (iBodyLength > 127 && iBodyLength < 256) {
				iLPDU = 10;
			}
			if (iBodyLength > 255) {
				iLPDU = 11;
			}
			/* �����ܹ��Ĳ�ֺ����Ϣ���� */
			iTotal_Packet = this.getTotalPacket(iLUDH, iLPDU, iBodyLength);
			String[] arrStr = new String[iTotal_Packet];
			int ibodypos = 0;
			int ibodylen = body.length;
			int ilen = 0;
			for (int i = 0; i < arrStr.length; i++) {
				ByteArrayOutputStream data = new ByteArrayOutputStream();
				byte[] barr_udh = this.encodeLWDPHerder(iTotal_Packet, (i + 1));
				try {
					data.write(barr_udh); // ÿ����������WDP �е�UDH��Ϣ
					// ibodypos = 140 * i;
					if (i == 0) { // ��һ��������WSP HEASER��Ϣ
						ilen = 140 - iLUDH - iLPDU;
						byte[] barr_pdu = this.encodeLWSPHeader(iBodyLength);
						data.write(barr_pdu);
					} else {
						ilen = 140 - iLUDH; //
						if (ilen > (ibodylen - ibodypos)) {
							ilen = ibodylen - ibodypos;
						}
					}
					data.write(body, ibodypos, ilen);
					ibodypos += ilen;
				}
				catch (IOException ex) {
					ex.printStackTrace();
				}
				arrStr[i] = this.bytes2HexStr(data.toByteArray());
			}

			return arrStr;
		}
	}

	/**
	 * �����滻��ǰ���͵�PUSH��Ϣ�ı�����HEX��
	 * 
	 * @param title
	 *            wap����
	 * @param pushurl
	 *            wap url����
	 * @param si_id
	 *            ��ֵ�����뷢��PUSH��Ϣ��ֵһ����������ȷ�滻�������ǰ���͵�PUSH��Ϣû������SI_IDֵ�������Ϣ���ͺ�û���κ�Ч����
	 * @return �滻��ǰ���͵�PUSH��Ϣ�ı�����HEX��
	 */
	public String[] getReplaceMessage(String title, String pushurl, String si_id) {
		this.setSi_title(title);
		this.setSi_url(pushurl);
		this.setSi_id(si_id); // �滻�ն���SI_ID��ͬ��PUSH��Ϣ
		this.setSi_action(0x08); // single-high
		String[] sarr = this.getPushMessage();
		return sarr;
	}

	/**
	 * ���㳤��ϢӦ�ò�ɶ��ٸ���
	 * 
	 * @param iLUDH
	 *            UDHͷ����
	 * @param iLPDU
	 *            PDUͷ����
	 * @param iBodyLength
	 *            ��Ϣ�峤��
	 * @return ����ĸ���
	 */
	private int getTotalPacket(int iLUDH, int iLPDU, int iBodyLength) {
		int itotal = 0;
		int iLeft = iBodyLength - (140 - iLUDH - iLPDU);
		itotal = (iLeft + 140 - iLUDH - 1) / (140 - iLUDH);
		itotal = itotal + 1;
		return itotal;
	}

	/**
	 * ����PUSH��Ϣ��ACTIONֵ
	 * 
	 * @param si_action
	 *            PUSH��Ϣ��ACTIONֵ
	 */
	public void setSi_action(int si_action) {
		this.si_action = si_action;
	}

	/**
	 * ����PUSH��Ϣ�Ĵ���ʱ�䣬��Щ�ն�֧�֡����������ʱ���Ѿ��������ֻ��涨�Ĺ���ʱ�䣬���ն˲���ʾ�û��յ�PUSH��Ϣ������Ĭ����ɾ����
	 * 
	 * @param si_created
	 *            PUSH��Ϣ�Ĵ���ʱ��
	 */
	public void setSi_created(Date si_created) {
		this.si_created = si_created;
	}

	/**
	 * ����PUSH��Ϣ�Ĺ���ʱ�䣬�󲿷��ն˲�֧�ִ˹��ܡ���ѡ�
	 * 
	 * @param si_expires
	 *            PUSH��Ϣ�Ĺ���ʱ��
	 */
	public void setSi_expires(Date si_expires) {
		this.si_expires = si_expires;
	}

	/**
	 * ����PUSH��Ϣ��Ψһ��ID��ʶ����ѡ�<br>
	 * ��������ǰ���͵�һ��PUSH��Ϣ�����滻��ɾ������������ô�ֵ��<br>
	 * Ӧ�ó�����������ã�Ϊ�������������̵���Ϣ���֣������ʽΪnet.guodu/000001 ����/������ǲ���Ϊ1��������ֵ��ѭ��ʹ�á�
	 * 
	 * @param si_id
	 *            PUSH��Ϣ��Ψһ��ID��ʶ
	 */
	public void setSi_id(String si_id) {
		this.si_id = si_id;
	}

	/**
	 * ����PUSH��Ϣ��˵�������֣����Բ����á�
	 * 
	 * @param si_title
	 *            PUSH��Ϣ��˵��������
	 */
	public void setSi_title(String si_title) {
		this.si_title = si_title;
	}

	/**
	 * ����PUSH��Ϣ�е��û����ʵ�URL��ַ������Ϊ�����
	 * 
	 * @param si_url
	 *            ��ȥ"HTTP://"�Ժ�����ݣ��磺wap.guodu.net:8080/index
	 */
	public void setSi_url(String si_url) {
		this.si_url = si_url;
	}
}
