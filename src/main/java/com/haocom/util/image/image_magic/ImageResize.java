package com.haocom.util.image.image_magic;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * ʹ��ImageMagicK��convert��ͼƬ�ߴ�ת���Ĺ���. <br>
 * <br>
 * <br>
 * <h2>ʹ�÷���</h2><br>
 * 
 * <pre>
 * import com.haocom.util.image.image_magic.ImageResize;
 * 
 * ....
 * ....
 * 
 * //����convert����·��
 * ImageResize.setImageMagicConvertPath(&quot;C:/Program Files/ImageMagick-6.5.8-Q16/convert &quot;);
 * 
 * //����ImageResize����
 * ImageResize imageResize = new ImageResize();
 * 
 * //���ò���
 * imageResize.setColorNumber(16); //������ɫ 
 * imageResize.setHeight(320); //���ø߶�
 * imageResize.setWidth(204); //���ÿ��
 * imageResize.setOnlySmaller(false); // �����Ƿ�ֻ��СͼƬ
 * imageResize.setKeepAspectRatio(true); //�����Ƿ񱣳�ԭ�б���
 * 
 * //ִ��resize
 * imageResize.resize(sourceFilename, destFilename);
 * 
 * </pre>
 * 
 * <h2>����GIF</h2><br>
 * ���GIF����ʱ����Ҫע����ɫ����.<br>
 * �����Ҫgif�ļ�С�������������ɫ����Ϊ16����������GIF��ʽʹ�ò�Ҫʹ�ô����� <br>
 * �������д����Ŀ������Զ�ʹ��256ɫ������Ҫע�⡣ <br>
 * �����������ʽ��ͼƬ������Ҫ������ɫ����. <br>
 * ��ע�⵱��ɫ�������ý�Сʱ�����ܻᵼ�´����쳣�������������⴦���������������ɫ����. <br>
 * <br>
 * <br>
 * <h2>ע��</h2> <br>
 * �˹�����Ҫʹ��ImageMagicK <br>
 * ImageMagicK��վ��http://www.imagemagick.org <br>
 * ��װ˵��Unix: http://www.imagemagick.org/script/install-source.php#unix <br>
 * ��װ˵��Windows: http://www.imagemagick.org/script/binary-releases.php#windows <br>
 * <br>
 * <p>
 * Copyright: Copyright (c) 2009-12-10 ����02:53:45
 * <p>
 * Company: 
 * <p>
 * 
 * @author chengfan@c-platform.com
 * @version 1.0.0
 */
public class ImageResize {

	/** Image Magic convert����·�� */
	private static String imageMagicConvertPath = "/usr/local/bin/convert";

	/**
	 * ��ȡ Image Magic convert����·��
	 * 
	 * @return Image Magic convert����·��
	 */
	public static String getImageMagicConvertPath() {
		return imageMagicConvertPath;
	}

	/**
	 * ���� Image Magic convert����·��
	 * 
	 * @param imageMagicConvertPath
	 *            Image Magic convert����·��
	 */
	public static void setImageMagicConvertPath(String imageMagicConvertPath) {
		ImageResize.imageMagicConvertPath = imageMagicConvertPath;
	}

	/** ��ɫ���� */
	private int colorNumber;

	/** height */
	private int height;

	/** ����ԭ�гߴ磬Ĭ��true */
	private boolean keepAspectRatio = true;

	/** lastCommand */
	private String lastCommand;

	/** �Ƿ�ֻ�Ŵ�Ĭ��false */
	private boolean onlyLarger = false;

	/** �Ƿ�ֻ��С��Ĭ��false */
	private boolean onlySmaller = false;

	/** width */
	private int width;

	/**
	 * ��ȡ ��ͼ��ɫ����
	 * 
	 * @return ��ɫ����
	 */
	public int getColorNumber() {
		return colorNumber;
	}

	/**
	 * ��ȡ height
	 * 
	 * @return height
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * ��ȡ lastCommand
	 * 
	 * @return lastCommand
	 */
	public String getLastCommand() {
		return lastCommand;
	}

	/**
	 * ��ȡ width
	 * 
	 * @return width
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * ��ȡ ����ԭ�гߴ磬Ĭ��true
	 * 
	 * @return ����ԭ�гߴ�
	 */
	public boolean isKeepAspectRatio() {
		return keepAspectRatio;
	}

	/**
	 * ��ȡ �Ƿ�ֻ�Ŵ�Ĭ��false
	 * 
	 * @return �Ƿ�ֻ�Ŵ�
	 */
	public boolean isOnlyLarger() {
		return onlyLarger;
	}

	/**
	 * ��ȡ �Ƿ�ֻ��С��Ĭ��false
	 * 
	 * @return �Ƿ�ֻ��С
	 */
	public boolean isOnlySmaller() {
		return onlySmaller;
	}

	/**
	 * ת���ߴ�
	 * 
	 * @param sourceFilename
	 *            ԭͼƬ�ļ���
	 * @param destFilename
	 *            ��ͼƬ�ļ���
	 * @throws Exception
	 *             Exception
	 */
	public void resize(String sourceFilename, String destFilename) throws Exception {
		StringBuilder buf = new StringBuilder();
		buf.append("").append(imageMagicConvertPath).append(" ");
		buf.append("").append(sourceFilename).append(" ");
		buf.append("-filter Gaussian ");
		buf.append("-resize ").append(width).append("x").append(height);
		if (!isKeepAspectRatio()) {
			buf.append("!");
		}
		if (onlyLarger) {
			buf.append("<");
		}
		if (onlySmaller) {
			buf.append(">");
		}
		buf.append(" ");

		buf.append("-dither none ");
		if (colorNumber > 0) {
			buf.append("-colors ").append(colorNumber).append(" ");
		}
		buf.append("").append(destFilename).append(" ");

		lastCommand = buf.toString();

		Process process = Runtime.getRuntime().exec(lastCommand);
		int resultCode = process.waitFor();
		if (resultCode != 0) {
			buf.setLength(0);
			buf.append("\r\nccommand: ").append(lastCommand).append("\r\n");

			InputStream inputStream;
			byte[] bs = new byte[1024 * 8];
			ByteArrayOutputStream baos = new ByteArrayOutputStream();

			inputStream = process.getErrorStream();
			if (inputStream != null) {
				int n = 0;
				baos.reset();
				while ((n = inputStream.read(bs)) >= 0) {
					baos.write(bs, 0, n);
				}
				String str = new String(baos.toByteArray());
				buf.append(str.trim()).append("\r\n");
			}

			inputStream = process.getInputStream();
			if (inputStream != null) {
				int n = 0;
				baos.reset();
				while ((n = inputStream.read(bs)) >= 0) {
					baos.write(bs, 0, n);
				}
				String str = new String(baos.toByteArray());
				buf.append(str.trim());
			}
			throw new Exception(buf.toString());
		}
	}

	/**
	 * ���� ��ͼ��ɫ������һ�����ڵ���GIF��ɫ����
	 * 
	 * @param colorNumber
	 *            ��ɫ����
	 */
	public void setColorNumber(int colorNumber) {
		this.colorNumber = colorNumber;
	}

	/**
	 * ���� height
	 * 
	 * @param height
	 *            height
	 */
	public void setHeight(int height) {
		this.height = height;
	}

	/**
	 * ���� ����ԭ�гߴ�
	 * 
	 * @param keepAspectRatio
	 *            ����ԭ�гߴ�
	 */
	public void setKeepAspectRatio(boolean keepAspectRatio) {
		this.keepAspectRatio = keepAspectRatio;
	}

	/**
	 * ���� onlyLarger
	 * 
	 * @param onlyLarger
	 *            onlyLarger
	 */
	public void setOnlyLarger(boolean onlyLarger) {
		this.onlyLarger = onlyLarger;
	}

	/**
	 * ���� �Ƿ�ֻ��С
	 * 
	 * @param onlySmaller
	 *            �Ƿ�ֻ��С
	 */
	public void setOnlySmaller(boolean onlySmaller) {
		this.onlySmaller = onlySmaller;
	}

	/**
	 * ���� width
	 * 
	 * @param width
	 *            width
	 */
	public void setWidth(int width) {
		this.width = width;
	}
}
