package com.haocom.util.image.image_magic;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * 使用ImageMagicK的convert做图片尺寸转换的工具. <br>
 * <br>
 * <br>
 * <h2>使用范例</h2><br>
 * 
 * <pre>
 * import com.haocom.util.image.image_magic.ImageResize;
 * 
 * ....
 * ....
 * 
 * //设置convert所在路径
 * ImageResize.setImageMagicConvertPath(&quot;C:/Program Files/ImageMagick-6.5.8-Q16/convert &quot;);
 * 
 * //创建ImageResize对象
 * ImageResize imageResize = new ImageResize();
 * 
 * //设置参数
 * imageResize.setColorNumber(16); //设置颜色 
 * imageResize.setHeight(320); //设置高度
 * imageResize.setWidth(204); //设置宽度
 * imageResize.setOnlySmaller(false); // 设置是否只缩小图片
 * imageResize.setKeepAspectRatio(true); //设置是否保持原有比例
 * 
 * //执行resize
 * imageResize.resize(sourceFilename, destFilename);
 * 
 * </pre>
 * 
 * <h2>处理GIF</h2><br>
 * 针对GIF处理时，需要注意颜色数量.<br>
 * 如果需要gif文件小，则可以设置颜色数量为16，如其他非GIF格式使用不要使用此设置 <br>
 * 如果不填写此项目，则会自动使用256色，所以要注意。 <br>
 * 如果是其他格式的图片，则不需要设置颜色数量. <br>
 * 请注意当颜色数量设置较小时，可能会导致处理异常，所以如无特殊处理需求请勿减少颜色数量. <br>
 * <br>
 * <br>
 * <h2>注意</h2> <br>
 * 此功能需要使用ImageMagicK <br>
 * ImageMagicK网站：http://www.imagemagick.org <br>
 * 安装说明Unix: http://www.imagemagick.org/script/install-source.php#unix <br>
 * 安装说明Windows: http://www.imagemagick.org/script/binary-releases.php#windows <br>
 * <br>
 * <p>
 * Copyright: Copyright (c) 2009-12-10 下午02:53:45
 * <p>
 * Company: 
 * <p>
 * 
 * @author chengfan@c-platform.com
 * @version 1.0.0
 */
public class ImageResize {

	/** Image Magic convert命令路径 */
	private static String imageMagicConvertPath = "/usr/local/bin/convert";

	/**
	 * 获取 Image Magic convert命令路径
	 * 
	 * @return Image Magic convert命令路径
	 */
	public static String getImageMagicConvertPath() {
		return imageMagicConvertPath;
	}

	/**
	 * 设置 Image Magic convert命令路径
	 * 
	 * @param imageMagicConvertPath
	 *            Image Magic convert命令路径
	 */
	public static void setImageMagicConvertPath(String imageMagicConvertPath) {
		ImageResize.imageMagicConvertPath = imageMagicConvertPath;
	}

	/** 颜色数量 */
	private int colorNumber;

	/** height */
	private int height;

	/** 保持原有尺寸，默认true */
	private boolean keepAspectRatio = true;

	/** lastCommand */
	private String lastCommand;

	/** 是否只放大，默认false */
	private boolean onlyLarger = false;

	/** 是否只缩小，默认false */
	private boolean onlySmaller = false;

	/** width */
	private int width;

	/**
	 * 获取 新图颜色数量
	 * 
	 * @return 颜色数量
	 */
	public int getColorNumber() {
		return colorNumber;
	}

	/**
	 * 获取 height
	 * 
	 * @return height
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * 获取 lastCommand
	 * 
	 * @return lastCommand
	 */
	public String getLastCommand() {
		return lastCommand;
	}

	/**
	 * 获取 width
	 * 
	 * @return width
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * 获取 保持原有尺寸，默认true
	 * 
	 * @return 保持原有尺寸
	 */
	public boolean isKeepAspectRatio() {
		return keepAspectRatio;
	}

	/**
	 * 获取 是否只放大，默认false
	 * 
	 * @return 是否只放大
	 */
	public boolean isOnlyLarger() {
		return onlyLarger;
	}

	/**
	 * 获取 是否只缩小，默认false
	 * 
	 * @return 是否只缩小
	 */
	public boolean isOnlySmaller() {
		return onlySmaller;
	}

	/**
	 * 转换尺寸
	 * 
	 * @param sourceFilename
	 *            原图片文件名
	 * @param destFilename
	 *            新图片文件名
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
	 * 设置 新图颜色数量，一般用于调整GIF颜色数量
	 * 
	 * @param colorNumber
	 *            颜色数量
	 */
	public void setColorNumber(int colorNumber) {
		this.colorNumber = colorNumber;
	}

	/**
	 * 设置 height
	 * 
	 * @param height
	 *            height
	 */
	public void setHeight(int height) {
		this.height = height;
	}

	/**
	 * 设置 保持原有尺寸
	 * 
	 * @param keepAspectRatio
	 *            保持原有尺寸
	 */
	public void setKeepAspectRatio(boolean keepAspectRatio) {
		this.keepAspectRatio = keepAspectRatio;
	}

	/**
	 * 设置 onlyLarger
	 * 
	 * @param onlyLarger
	 *            onlyLarger
	 */
	public void setOnlyLarger(boolean onlyLarger) {
		this.onlyLarger = onlyLarger;
	}

	/**
	 * 设置 是否只缩小
	 * 
	 * @param onlySmaller
	 *            是否只缩小
	 */
	public void setOnlySmaller(boolean onlySmaller) {
		this.onlySmaller = onlySmaller;
	}

	/**
	 * 设置 width
	 * 
	 * @param width
	 *            width
	 */
	public void setWidth(int width) {
		this.width = width;
	}
}
