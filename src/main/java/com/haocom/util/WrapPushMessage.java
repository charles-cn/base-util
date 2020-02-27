package com.haocom.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * WAP Push Proxy Gateway (PPG). <br>
 * 实现WAP PPG的基本功能，协议标准WAP-249-PPGService.<br>
 * 根据WAP规范文档，封装PUSH消息，可以返回HEX串或BYTE数据流，便于真正的PPG或应用程序通过可用的载体发送到终端.
 * <p>
 * Copyright: Copyright (c) 2010-4-1 下午03:31:49
 * <p>
 * Company: 
 * <p>
 * Author: chenwei@c-platform.com
 * <p>
 * Version: 1.0
 * <p>
 * <h2>组件使用说明:</h2>
 * 
 * <pre>
 * 将wappush消息转换为普通的短信消息用于下发
 * 
 * //实例化wap编码类
 * WrapPushMessage wap = new WrapPushMessage();
 * //设置wap标题
 * wap.setSi_title(taskInfo.getTitle());
 * //设置wap url连接
 * wap.setSi_url(taskInfo.getContent());
 * //调用wap编码方法,返回编码后的wap字符串数组
 * //数组长度一般为1，如果大于1则为长短信wappush
 * String[] content = wap.getPushMessage();
 * 
 * 返回的值直接设置到短信内容字段，实现用户从短信网关下发wap消息
 * </pre>
 */
public class WrapPushMessage {

	/**
	 * 将Byte内存数据流转成十六进制文本串
	 * 
	 * @param arr
	 *            待转换的Byte数据流
	 * @return Hex文本串
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
	 * 将用十六进制文本串描述的字符串转成Byte数据流
	 * 
	 * @param vStr
	 *            Hex文本串
	 * @return 转换后的Byte数据流
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

	/** PUSH消息的ACTION值 */
	private int si_action = -1;

	/** PUSH消息的创建时间 */
	private Date si_created = null;

	/** PUSH消息的过期时间 */
	private Date si_expires = null;

	/** PUSH消息的唯一性ID标识 */
	private String si_id = null;

	/** PUSH消息的说明性文字 */
	private String si_title = null;

	/** PUSH消息中的用户访问的URL地址 */
	private String si_url = null;

	/**
	 * 构造函数
	 */
	public WrapPushMessage() {
	}

	/**
	 * 根据WAP-167-ServiceInd说明，编码SI_PUSH消息体
	 * 
	 * @return 编码后的SI_PUSH消息体
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
			 * 设置Action值，默认为8，singal-high signal-none 5 action signal-low 6
			 * action signal-medium 7
			 */
			if (si_action != -1) {
				data.write((byte) this.si_action); //
			} else {
				data.write(0x08); // //<action=signal-high>
			}
			/* 设置si_id值，可以为空，此值为可选项，可以不设置不影响PUSH消息发送 */
			if (si_id != null) {
				data.write(0x11); // si-id indication;
				data.write(0x03); // inline String follows
				data.write(this.si_id.getBytes()); // si-id value
				data.write(0x00); // end of string
			}
			/* 设置业务访问的URL地址,必须设置 */
			if (this.si_url != null) {
				data.write(0x0C); // equal "http://"
				data.write(0x03); // Inline string follows
				data.write(si_url.getBytes()); // the URL Value
				data.write(0x00); // String table Length
			}
			/* 设置Created时间值，可选项 */
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
			// 设置<indication>Value</indication>中value值
			if (si_title != null) {
				data.write(0x03); // inline the String follows，下面是标内容的字串，可选项
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
	 * 根据WAP-167-ServiceInd.pdf规范，设置时间格式
	 * 
	 * @param ltime
	 *            时间
	 * @return 编码后的时间
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
		// 删除尾部的00，如：2005 11 23 13：00：00，应为2005112313
		// 考虑到只有时间会出现00：00：00的情况，则只对后三位操作
		data1.write(b1, 0, 4); // 将年月日写入
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
	 * 对超过一条短信内容的消息头进行编码
	 * 
	 * @param total_packet
	 *            总得消息包数
	 * @param current_packet
	 *            当前第几个消息包
	 * @return 编码后返回的内容
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
	 * 编码长消息体的WSP协议的PDU数据头
	 * 
	 * @param iBodyLength
	 *            消息体长度
	 * @return 编码后返回的内容
	 */
	private byte[] encodeLWSPHeader(int iBodyLength) {
		byte iHeaderLen = 0;
		byte bodylen = (byte) (iBodyLength);
		ByteArrayOutputStream data = new ByteArrayOutputStream();
		/* 根据WAP-230-WSP-20010705-a.pdf进行编码 */
		int[] arri = this.encodeUintvarNumber(iBodyLength);
		// arri[0],7bit表示的数值个数，5:不包括LENGTH值的其它头字节数
		iHeaderLen = (byte) (arri[0] + 5);
		/*
		 * if (iBodyLength < 128) { iHeaderLen = 0x06; // The Header Length } if
		 * (iBodyLength > 127 && iBodyLength < 256) { iHeaderLen = 0x07; // The
		 * Header Length } if (iBodyLength > 255) { iHeaderLen = 0x08; // The
		 * Header Length }
		 */

		try {
			data.write(0x25); // The Transaction id( connectionless WSP )可循环使用
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
			 * five octets. It MUST be present even if its value is zero. 以上是WAP
			 * WSP 文档中的说明，这里只大数值要按上述说明进行编码。
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
	 * 将8bit数值转换成7bit方式，可以参见OMA规范中的说明
	 * 
	 * @param lData
	 *            8bit数值
	 * @return 8bit数值转换成7bit方式后的数据
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
	 * 根据WDP协议编码UDH消息头
	 * 
	 * @return 编码后的UDH消息头
	 */
	private byte[] encodeWDPHeader() {
		/* 编码根据wap-259-wdp-20010614-a.pdf实现 */
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
	 * 以WSP协议编码PDU消息头信息
	 * 
	 * @param iBodyLength
	 *            消息体长度
	 * @return 编码后的PDU消息头
	 */
	private byte[] encodeWSPHeader(int iBodyLength) {
		/* 根据WAP-230-WSP-20010705-a.pdf进行编码 */
		ByteArrayOutputStream data = new ByteArrayOutputStream();
		try {
			data.write(0x25); // The Transaction id( connectionless WSP )可循环使用
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
	 * 返回取消消息的编码HEX串
	 * 
	 * @param si_id
	 *            PUSH消息的唯一性ID标识
	 * @return 取消消息的编码HEX串
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
	 * 返回已经编好码的PUSH消息串,默认方式
	 * 
	 * @return 编好码的PUSH消息串
	 */
	public String[] getPushMessage() {
		byte[] body = this.encodeContentBody();
		int iBodyLength = body.length;
		System.out.println("body Length=" + iBodyLength);
		/*
		 * 7: short udh bytes length 9: short pdu bytes length
		 */
		// 判断push总长度是否超过140个字节
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
			int iLUDH = 12; // 长消息拆包后，每个报文的消息UDH头比原来多5个字节
			// 由于多个字节表表的INT值在WSP中有编码规定，所以根据ContentLength值
			// 设置PDU头的长度
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
			/* 计算总共的拆分后的消息包数 */
			iTotal_Packet = this.getTotalPacket(iLUDH, iLPDU, iBodyLength);
			String[] arrStr = new String[iTotal_Packet];
			int ibodypos = 0;
			int ibodylen = body.length;
			int ilen = 0;
			for (int i = 0; i < arrStr.length; i++) {
				ByteArrayOutputStream data = new ByteArrayOutputStream();
				byte[] barr_udh = this.encodeLWDPHerder(iTotal_Packet, (i + 1));
				try {
					data.write(barr_udh); // 每个包都包括WDP 中的UDH信息
					// ibodypos = 140 * i;
					if (i == 0) { // 第一个包包括WSP HEASER信息
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
	 * 返回替换先前发送的PUSH消息的编码后的HEX串
	 * 
	 * @param title
	 *            wap标题
	 * @param pushurl
	 *            wap url连接
	 * @param si_id
	 *            此值必须与发送PUSH消息的值一样，才能正确替换。如果先前发送的PUSH消息没有设置SI_ID值，则此消息发送后没有任何效果。
	 * @return 替换先前发送的PUSH消息的编码后的HEX串
	 */
	public String[] getReplaceMessage(String title, String pushurl, String si_id) {
		this.setSi_title(title);
		this.setSi_url(pushurl);
		this.setSi_id(si_id); // 替换终端中SI_ID相同的PUSH消息
		this.setSi_action(0x08); // single-high
		String[] sarr = this.getPushMessage();
		return sarr;
	}

	/**
	 * 计算长消息应该拆成多少个包
	 * 
	 * @param iLUDH
	 *            UDH头长度
	 * @param iLPDU
	 *            PDU头长度
	 * @param iBodyLength
	 *            消息体长度
	 * @return 拆包的个数
	 */
	private int getTotalPacket(int iLUDH, int iLPDU, int iBodyLength) {
		int itotal = 0;
		int iLeft = iBodyLength - (140 - iLUDH - iLPDU);
		itotal = (iLeft + 140 - iLUDH - 1) / (140 - iLUDH);
		itotal = itotal + 1;
		return itotal;
	}

	/**
	 * 设置PUSH消息的ACTION值
	 * 
	 * @param si_action
	 *            PUSH消息的ACTION值
	 */
	public void setSi_action(int si_action) {
		this.si_action = si_action;
	}

	/**
	 * 设置PUSH消息的创建时间，有些终端支持。如果创建的时间已经超过了手机规定的过期时间，则终端不提示用户收到PUSH消息，将沉默处理删除。
	 * 
	 * @param si_created
	 *            PUSH消息的创建时间
	 */
	public void setSi_created(Date si_created) {
		this.si_created = si_created;
	}

	/**
	 * 设置PUSH消息的过期时间，大部分终端不支持此功能。可选项。
	 * 
	 * @param si_expires
	 *            PUSH消息的过期时间
	 */
	public void setSi_expires(Date si_expires) {
		this.si_expires = si_expires;
	}

	/**
	 * 设置PUSH消息的唯一性ID标识。可选项。<br>
	 * 如果想对先前发送的一条PUSH消息进行替换或删除，则必须设置此值。<br>
	 * 应用程序可自行设置，为了与其它服务商的消息区分，建议格式为net.guodu/000001 其中/后面的是步长为1的增长数值，循环使用。
	 * 
	 * @param si_id
	 *            PUSH消息的唯一性ID标识
	 */
	public void setSi_id(String si_id) {
		this.si_id = si_id;
	}

	/**
	 * 设置PUSH消息的说明性文字，可以不设置。
	 * 
	 * @param si_title
	 *            PUSH消息的说明性文字
	 */
	public void setSi_title(String si_title) {
		this.si_title = si_title;
	}

	/**
	 * 设置PUSH消息中的用户访问的URL地址，此项为必填项。
	 * 
	 * @param si_url
	 *            除去"HTTP://"以后的内容，如：wap.guodu.net:8080/index
	 */
	public void setSi_url(String si_url) {
		this.si_url = si_url;
	}
}
