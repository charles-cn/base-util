package com.haocom.util.image;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import javax.imageio.stream.ImageOutputStream;

import com.haocom.util.FileTools;
import com.haocom.util.image.gif.AnimatedGifEncoder;
import com.haocom.util.image.gif.GifDecoder;

/**
 * 图像操作类. <br>
 * 定义了对图像和各种操作方法，包括新建，保存，剪裁等等.<br>
 * 若需实现图像尺寸转换，请使用组件 com.haocom.util.image.image_magic
 * 
 * <pre>
 * 
 * public class ImageDemo {
 * 
 * 	public static void main(String[] args) throws Exception {
 * 		// 创建字体.
 * 		if (1 == 2) {
 * 			ImageUtils.createFont(new File(&quot;d://STKAITI.TTF &quot;), 2, 20);
 * 			Font font = ImageUtils.createFont(&quot;d://STKAITI.TTF &quot;, 2, 20);
 * 			ImageUtils.createFont(font, 2, 20);
 * 		}
 * 		// 打开图片、创建图片和保存图片.
 * 		if (1 == 2) {
 * 			BufferedImage newImage1 = ImageUtils.createImage(400, 200);
 * 			// 保存图片，需指定格式.
 * 			ImageUtils.saveImage(newImage1, ImageFormat.BMP, new File(&quot;d://newImage1.bmp&quot;));
 * 
 * 			File file = new File(&quot;d://5.jpg&quot;);
 * 			BufferedImage image = ImageUtils.openImage(file);
 * 			BufferedImage newImage2 = ImageUtils.createImage(image);
 * 			// 保存为图片jpeg格式.
 * 			ImageUtils.saveImage(newImage2, ImageFormat.JPEG, new File(&quot;d://newImage2.jpg&quot;));
 * 			ImageUtils.saveImageAsJPEG(newImage2, new File(&quot;d://newImage3.jpg&quot;), 1);
 * 			// 保存图片为gif格式.
 * 			List&lt;BufferedImage&gt; imageList = new ArrayList&lt;BufferedImage&gt;();
 * 			imageList.add(ImageUtils.openImage(new File(&quot;d://1.jpg&quot;)));
 * 			imageList.add(ImageUtils.openImage(new File(&quot;d://2.jpg&quot;)));
 * 			imageList.add(ImageUtils.openImage(new File(&quot;d://3.jpg&quot;)));
 * 			imageList.add(ImageUtils.openImage(new File(&quot;d://4.jpg&quot;)));
 * 			imageList.add(ImageUtils.openImage(new File(&quot;d://5.jpg&quot;)));
 * 			ImageUtils.saveImageAsGIF(imageList, new File(&quot;d://all.gif&quot;), 800);
 * 		}
 * 		// 剪裁图片.
 * 		if (1 == 2) {
 * 			File file = new File(&quot;d://2.jpg&quot;);
 * 			BufferedImage image = ImageUtils.openImage(file);
 * 			BufferedImage newImage = ImageUtils.crop(image, 0, 0, 200, 200);
 * 			ImageUtils.saveImage(newImage, ImageFormat.JPEG, new File(&quot;d://crop.jpg&quot;));
 * 		}
 * 		// 在给定图片中写入文字.
 * 		if (1 == 2) {
 * 			File file = new File(&quot;d://crop.jpg&quot;);
 * 			BufferedImage image1 = ImageUtils.openImage(file);
 * 			ImageUtils.drawText(image1, 50, 50, &quot;HELLO&quot;);
 * 			ImageUtils.saveImage(image1, ImageFormat.JPEG, new File(&quot;d://drawText1.jpg&quot;));
 * 
 * 			BufferedImage image2 = ImageUtils.openImage(file);
 * 			Font font = ImageUtils.createFont(new File(&quot;d://STKAITI.TTF &quot;), 3, 20);
 * 			ImageUtils.drawText(image2, font, Color.red, 50, 100, &quot;HELLO&quot;);
 * 			ImageUtils.saveImage(image2, ImageFormat.JPEG, new File(&quot;d://drawText2.jpg&quot;));
 * 
 * 			String aa = &quot;今天\r\n你好吗1今天你好吗2今天你好吗3今天你好吗4今天你好吗5今天你好吗6今天你好吗7今天你好吗8今天你好吗9今天你好吗10今天你好吗11今天你好吗12今天你好吗13今天你好吗14今天你好吗15今天你好吗16今天你好吗17今天你好吗18今天你好吗19今天你好吗20今天你好吗21今天你好吗22今天你好吗23今天你好吗24今天你好吗25今天你好吗26今天你好吗27今天你好吗28今天你好吗29&quot;;
 * 			String bb = &quot;一二三四五六七五六七八&quot;;
 * 			String cc = &quot;今\r\n\r\n天\r\n你\r\n好\r\n吗\r\n你\r\n好\r\n吗\r\n好\r\n吗\r\n&quot;;
 * 			BufferedImage image3 = ImageUtils.openImage(file);
 * 			ImageUtils.drawText(image3, font, Color.red, 0, 0, 200, 200, HorizontalAlignment.RIGHT, VerticalAlignment.CENTER, bb);
 * 			ImageUtils.saveImage(image3, ImageFormat.JPEG, new File(&quot;d://drawText3.jpg&quot;));
 * 		}
 * 		// 重新设定图片大小.
 * 		if (1 == 2) {
 * 			File file = new File(&quot;d://3.jpg&quot;);
 * 			BufferedImage image = ImageUtils.openImage(file);
 * 			// BufferedImage newImage = ImageUtils.resize(image, 0.5);
 * 			BufferedImage newImage = ImageUtils.resize(image, 400, 100, FitType.KEEP_ASPECT_RATIO_AND_KEEP_FULL_IMGAGE);
 * 			ImageUtils.saveImage(newImage, ImageFormat.JPEG, new File(&quot;d://resize.jpg&quot;));
 * 		}
 * 		// 实现将一个gif图片中每一帧图作为一个image，返回一个imageList，并一一保存.
 * 		if (1 == 2) {
 * 			File file = new File(&quot;d://001.gif&quot;);
 * 			List&lt;BufferedImage&gt; imageList = ImageUtils.getGifImageList(file);
 * 			for (int i = 0; i &lt; imageList.size(); i++) {
 * 				ImageUtils.saveImage(imageList.get(i), ImageFormat.JPEG, new File(&quot;d://1111&quot; + i + &quot;.jpg&quot;));
 * 			}
 * 		}
 * 		// 实现将图像保存为gif格式，并设置每一帧不同的延迟时间.
 * 		if (1 == 2) {
 * 			List&lt;BufferedImage&gt; imageList = new ArrayList&lt;BufferedImage&gt;();
 * 			imageList.add(ImageUtils.openImage(new File(&quot;d://1.jpg&quot;)));
 * 			imageList.add(ImageUtils.openImage(new File(&quot;d://2.jpg&quot;)));
 * 			imageList.add(ImageUtils.openImage(new File(&quot;d://3.jpg&quot;)));
 * 			imageList.add(ImageUtils.openImage(new File(&quot;d://4.jpg&quot;)));
 * 			imageList.add(ImageUtils.openImage(new File(&quot;d://5.jpg&quot;)));
 * 			List&lt;Integer&gt; mm = new ArrayList&lt;Integer&gt;();
 * 			mm.add(500);
 * 			mm.add(1500);
 * 			mm.add(2500);
 * 			mm.add(3500);
 * 			mm.add(4500);
 * 			ImageUtils.saveImageAsGIF(imageList, new File(&quot;d://ttt.gif&quot;), mm);
 * 		}
 * 		// 实现获取gif图像每一帧的延迟时间.
 * 		if (1 == 2) {
 * 			List&lt;Integer&gt; aa = ImageUtils.getGifImageDelayList(new File(&quot;d://ttt.gif&quot;));
 * 			for (int i = 0; i &lt; aa.size(); i++) {
 * 				System.out.println(aa.get(i));
 * 			}
 * 		}
 * 	}
 * }
 * </pre>
 * <p>
 * Copyright: Copyright (c) Sep 25, 2008
 * <p>
 * Company: 
 * <p>
 * Author: ZhouYan
 * <p>
 * Version: 1.0
 */
public class ImageUtils {

	/** 图像填充方式，主要用于图像缩放 */
	public static enum FitType {
		/** 按比例填充，充满整个新的区域 */
		KEEP_ASPECT_RATIO_AND_FILL_NEWSIZE,

		/** 按比例填充，使新的区域仍然符合原图的比例尺寸 */
		KEEP_ASPECT_RATIO_AND_KEEP_FULL_IMGAGE,

		/** 拉伸或缩小，充满整个区域 */
		SCALE_TO_NEWSIZE;
	}

	/** 图像中文字的横向对齐方式 */
	public static enum HorizontalAlignment {
		/** 居中对齐 */
		CENTER,

		/** 左对齐 */
		LEFT,

		/** 右对齐 */
		RIGHT;
	}

	/** 图像的颜色分量类型 */
	public static enum ImageColorType {
		/** 表示一个图像，它具有合成整数像素的8位RGBA颜色分量 */
		ARGB,

		/** 表示一个图像，它具有合成整数像素的8位RGB颜色分量 */
		RGB;
	}

	/** 图像的格式 */
	public static enum ImageFormat {
		/** 图像格式为BMP */
		BMP,

		/** 图像格式为GIF */
		GIF,

		/** 图像格式为JPEG */
		JPEG,

		/** 图像格式为PNG */
		PNG,

		/** 图像格式为WBMP */
		WBMP;
	}

	/** 图像中文字的纵向对齐方式 */
	public static enum VerticalAlignment {
		/** 下对齐 */
		BOTTOM,

		/** 居中对齐 */
		CENTER,

		/** 上对齐 */
		TOP;
	}

	/**
	 * 从给定的字体文件中读取字体来创建新的字体.
	 * 
	 * @param fontFile
	 *            字体文件，例如：/data/mms/tahoma.ttf
	 * @param style
	 *            新字体的样式<BR>
	 *            其中：0表示普通样式；1表示粗体样式 ；2表示斜体样式；3表示粗体+斜体； 输入其余数字均等同于0
	 * @param size
	 *            新字体的磅值大小
	 * @return 读取的字体
	 * @throws IOException
	 * @throws FontFormatException
	 */
	public static Font createFont(File fontFile, int style, double size) throws FontFormatException, IOException {
		Font font = Font.createFont(Font.TRUETYPE_FONT, fontFile);
		font = font.deriveFont(style, (float) size);
		return font;
	}

	/**
	 * 用现有字体对象来创建新的字体.
	 * 
	 * @param font
	 *            现有字体对象
	 * @param style
	 *            新字体的样式<BR>
	 *            其中：0表示普通样式；1表示粗体样式 ；2表示斜体样式；3表示粗体+斜体； 输入其余数字均等同于0
	 * @param size
	 *            新字体的磅值大小
	 * @return 创建的新字体
	 */
	public static Font createFont(Font font, int style, double size) {
		return font.deriveFont(style, (float) size);
	}

	/**
	 * 从给定的字体文件中读取字体来创建新的字体.
	 * 
	 * @param fontFilename
	 *            字体文件，例如：/data/mms/tahoma.ttf
	 * @param style
	 *            新字体的样式<BR>
	 *            其中：0表示普通样式；1表示粗体样式 ；2表示斜体样式；3表示粗体+斜体； 输入其余数字均等同于0
	 * @param size
	 *            新字体的磅值大小
	 * @return 创建的新字体
	 * @throws FontFormatException
	 * @throws IOException
	 */
	public static Font createFont(String fontFilename, int style, double size) throws FontFormatException, IOException {
		Font font = Font.createFont(Font.TRUETYPE_FONT, new File(fontFilename));
		font = font.deriveFont(style, (float) size);
		return font;
	}

	/**
	 * 创建一个新的图像,新图像内容即为给定图像的内容.
	 * 
	 * @param image
	 *            给定图像
	 * @return 新创建的图像
	 * @throws Exception
	 */
	public static BufferedImage createImage(BufferedImage image) throws Exception {
		int dWidth = image.getWidth();
		int dHeight = image.getHeight();
		BufferedImage newImg = new BufferedImage(dWidth, dHeight, BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics = newImg.createGraphics();
		try {
			graphics.setColor(Color.white);
			graphics.fillRect(0, 0, dWidth, dHeight);
			graphics.drawImage(image, 0, 0, dWidth, dHeight, null);
			return newImg;
		}
		catch (Exception ex) {
			throw ex;
		}
		finally {
			graphics.dispose();
		}
	}

	/**
	 * 创建一个新图像.<BR>
	 * 新图像内容为空,默认颜色为白色,该图像的ColorSpace为默认的RGB空间.<BR>
	 * 
	 * <pre>
	 * 示例：ImageUtils.createImage(300, 500)
	 * </pre>
	 * 
	 * @param width
	 *            所创建图像的宽度
	 * @param height
	 *            所创建图像的高度
	 * @return 新创建的图像
	 * @throws Exception
	 */
	public static BufferedImage createImage(int width, int height) throws Exception {
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics graphics = image.createGraphics();
		try {
			graphics.setColor(Color.white);
			graphics.fillRect(0, 0, width, height);
			return image;
		}
		catch (Exception ex) {
			throw ex;
		}
		finally {
			graphics.dispose();
		}
	}

	/**
	 * 创建一个新图像.<BR>
	 * 新图像内容为空,该图像的ColorSpace为默认的RGB空间.<BR>
	 * 
	 * <pre>
	 * 示例：ImageUtils.createImage(300, 500, Color.yellow)
	 * </pre>
	 * 
	 * @param width
	 *            所创建图像的宽度
	 * @param height
	 *            所创建图像的高度
	 * @param color
	 *            所创建图像的颜色
	 * @return 新创建的图像
	 * @throws Exception
	 */
	public static BufferedImage createImage(int width, int height, Color color) throws Exception {
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics graphics = image.createGraphics();
		try {
			graphics.setColor(color);
			graphics.fillRect(0, 0, width, height);
			return image;
		}
		catch (Exception ex) {
			throw ex;
		}
		finally {
			graphics.dispose();
		}
	}

	/**
	 * 构造一个类型为预定义图像类型之一的图像.<BR>
	 * 图像类型由枚举类型ImageColorType指定，包括BMP,GIF,JPEG,PNG,WBMP.
	 * 
	 * @param width
	 *            所创建图像的宽度
	 * @param height
	 *            所创建图像的高度
	 * @param color
	 *            所创建图像的颜色
	 * @param imageType
	 *            所创建图像的类型
	 * @return 新创建的图像
	 * @throws Exception
	 */
	public static BufferedImage createImage(int width, int height, Color color, ImageColorType imageType) throws Exception {
		BufferedImage image;
		if (imageType == ImageColorType.ARGB) {
			image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		} else {
			image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		}
		Graphics graphics = image.createGraphics();
		try {
			graphics.setColor(color);
			graphics.fillRect(0, 0, width, height);
			return image;
		}
		catch (Exception ex) {
			throw ex;
		}
		finally {
			graphics.dispose();
		}
	}

	/**
	 * 裁剪图片中指定位置和大小的内容.<BR>
	 * 
	 * <pre>
	 * 示例：ImageUtils.crop(image, 20, 20, 100, 100);
	 * </pre>
	 * 
	 * @param image
	 *            被裁剪图片
	 * @param x
	 *            裁剪的起始x坐标
	 * @param y
	 *            裁剪的起始y坐标
	 * @param width
	 *            裁剪长度
	 * @param height
	 *            裁剪高度
	 * @return 裁剪所得的图片
	 * @throws Exception
	 */
	public static BufferedImage crop(BufferedImage image, int x, int y, int width, int height) throws Exception {
		BufferedImage newImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics = newImg.createGraphics();
		try {
			graphics.drawImage(image, 0, 0, width, height, x, y, x + width, y + height, null);
			return newImg;
		}
		catch (Exception ex) {
			throw ex;
		}
		finally {
			graphics.dispose();
		}
	}

	/**
	 * 给图片打水印.<br>
	 * 从(x,y)点作为打水印的起始点，(x,y)即为水印图片摆放位置的左上角点，水印大小为给定的水印图片大小。
	 * 
	 * @param destImage
	 *            原始图片
	 * @param waterMark
	 *            水印图片
	 * @param x
	 *            打水印的起始X坐标
	 * @param y
	 *            打水印的起始Y坐标
	 * @param alpha
	 *            透明度,范围[0.0-1.0]之间，0表示全透明，1表示不透明
	 * @throws Exception
	 */
	public static void drawImage(BufferedImage destImage, BufferedImage waterMark, int x, int y, double alpha) throws Exception {
		Graphics2D graphics = destImage.createGraphics();
		try {
			graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) alpha));
			graphics.drawImage(waterMark, x, y, null);
		}
		catch (Exception ex) {
			throw ex;
		}
		finally {
			graphics.dispose();
		}
	}

	/**
	 * 给图片打水印.<br>
	 * 从(x,y)点作为打水印的起始点，(x,y)即为水印图片摆放位置的左上角点，水印大小为width*hight。
	 * 
	 * @param destImage
	 *            原始图片
	 * @param waterMark
	 *            水印图片
	 * @param x
	 *            打水印的起始X坐标
	 * @param y
	 *            打水印的起始Y坐标
	 * @param width
	 *            水印的宽
	 * @param hight
	 *            水印的高
	 * @param alpha
	 *            透明度,范围[0.0-1.0]之间，0表示全透明，1表示不透明
	 * @throws Exception
	 */
	public static void drawImage(BufferedImage destImage, BufferedImage waterMark, int x, int y, int width, int hight, double alpha) throws Exception {
		Graphics2D graphics = destImage.createGraphics();
		try {
			graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) alpha));
			graphics.drawImage(waterMark, x, y, width, hight, null);
		}
		catch (Exception ex) {
			throw ex;
		}
		finally {
			graphics.dispose();
		}
	}

	/**
	 * 将指定文本写入图像上的指定区域中,并指定对齐方式,字体，颜色.<BR>
	 * 
	 * <pre>
	 * 示例：ImageUtils.drawText(image, font, Color.red, 0, 0, 300, 200,
	 * HorizontalAlignment.RIGHT, VerticalAlignment.BOTTOM, “一二三四五六七五六七八&quot;);
	 * </pre>
	 * 
	 * @param image
	 *            给定图像
	 * @param font
	 *            指定写入的文本的字体
	 * @param color
	 *            指定写入的文本的颜色
	 * @param x
	 *            区域起点的x坐标
	 * @param y
	 *            区域起点的y坐标
	 * @param width
	 *            区域的宽度
	 * @param height
	 *            区域的高度
	 * @param horizontalAlignment
	 *            横向对齐方式
	 * @param verticalAlignment
	 *            纵向对齐方式
	 * @param text
	 *            待写入的文本
	 * @throws Exception
	 */
	public static void drawText(BufferedImage image, Font font, Color color, int x, int y, int width, int height,
	        HorizontalAlignment horizontalAlignment, VerticalAlignment verticalAlignment, String text) throws Exception {
		// 在原图上剪裁出指定矩形框位置和大小的图片，以便于将文本内容绘制到这张新图片上
		BufferedImage newImage = crop(image, x, y, width, height);
		// 原图
		Graphics2D oldGraphics = image.createGraphics();
		// 新图，从原图中裁剪部分而得
		Graphics2D graphics = newImage.createGraphics();
		try {
			graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			graphics.setFont(font);
			graphics.setColor(color);
			FontMetrics fontMetrics = graphics.getFontMetrics(font);
			// 取得切割后的一行行文本内容
			List<String> lineInfos = splitStringByWidth(graphics, font, width, text);
			// 文本被切割的的行数
			int lineCount = lineInfos.size();
			// 字符所占的高度
			int yStep = fontMetrics.getHeight();
			// 字符起始x坐标
			int newX = 0;
			// 遍历一行行文本内容，并计算出坐标画入图像中
			for (String info : lineInfos) {
				layoutTextWithDifferentAlignment(graphics, fontMetrics, lineCount, info, newX, yStep, width, height, horizontalAlignment,
				                                 verticalAlignment);
				yStep += fontMetrics.getHeight();
			}

			// 将绘制后的新图覆盖到原图的相应位置上
			oldGraphics.drawImage(newImage, x, y, x + width, y + height, 0, 0, newImage.getWidth(), newImage.getHeight(), null);
		}
		catch (Exception ex) {
			throw ex;
		}
		finally {
			graphics.dispose();
			oldGraphics.dispose();
		}
	}

	/**
	 * 使用指定字体和颜色在图像中绘制指定文本.<BR>
	 * 最左侧字符的基线位于此图形上下文坐标系的 (x, y) 位置处.
	 * 
	 * <pre>
	 * 示例：ImageUtils.drawText(image, font, Color.red, 50, 100, &quot;HELLO&quot;);
	 * </pre>
	 * 
	 * @param image
	 *            给定图像
	 * @param font
	 *            字体
	 * @param color
	 *            颜色
	 * @param x
	 *            输入起始点的x坐标
	 * @param y
	 *            输入起始点的y坐标
	 * @param text
	 *            待写入的文本
	 * @throws Exception
	 */
	public static void drawText(BufferedImage image, Font font, Color color, int x, int y, String text) throws Exception {
		Graphics2D graphics = image.createGraphics();
		try {
			graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			graphics.setFont(font);
			graphics.setColor(color);
			graphics.drawString(text, x, y);
		}
		catch (Exception ex) {
			throw ex;
		}
		finally {
			graphics.dispose();
		}
	}

	/**
	 * 将指定文本写入图像上的指定区域中,并指定对齐方式.<BR>
	 * 
	 * <pre>
	 * 示例：ImageUtils.drawText(image, 0, 0, 300, 200, HorizontalAlignment.RIGHT,
	 * VerticalAlignment.BOTTOM, “一二三四五六七五六七八&quot;);
	 * </pre>
	 * 
	 * @param image
	 *            给定图像
	 * @param x
	 *            区域起点的x坐标
	 * @param y
	 *            区域起点的y坐标
	 * @param width
	 *            区域的宽度
	 * @param height
	 *            区域的高度
	 * @param horizontalAlignment
	 *            横向对齐方式
	 * @param verticalAlignment
	 *            纵向对齐方式
	 * @param text
	 *            待写入的文本
	 * @throws Exception
	 */
	public static void drawText(BufferedImage image, int x, int y, int width, int height, HorizontalAlignment horizontalAlignment,
	        VerticalAlignment verticalAlignment, String text) throws Exception {
		// 在原图上剪裁出指定矩形框位置和大小的图片，以便于将文本内容绘制到这张新图片上
		BufferedImage newImage = crop(image, x, y, width, height);
		// 新图，从原图中裁剪部分而得
		Graphics2D graphics = newImage.createGraphics();
		// 原图
		Graphics2D oldGraphics = image.createGraphics();
		try {
			graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			FontMetrics fontMetrics = graphics.getFontMetrics();
			// 取得切割后的一行行文本内容
			List<String> lineInfos = splitStringByWidth(graphics, null, width, text);
			// 文本被切割的的行数
			int lineCount = lineInfos.size();
			// 字符所占的高度
			int yStep = fontMetrics.getHeight();
			// 字符起始x坐标
			int newX = 0;
			// 遍历一行行文本内容，并计算出坐标画入图像中
			for (String info : lineInfos) {
				layoutTextWithDifferentAlignment(graphics, fontMetrics, lineCount, info, newX, yStep, width, height, horizontalAlignment,
				                                 verticalAlignment);
				yStep += fontMetrics.getHeight();
			}

			// 将绘制后的新图覆盖到原图的相应位置上
			oldGraphics.drawImage(newImage, x, y, x + width, y + height, 0, 0, newImage.getWidth(), newImage.getHeight(), null);
		}
		catch (Exception ex) {
			throw ex;
		}
		finally {
			graphics.dispose();
			oldGraphics.dispose();
		}
	}

	/**
	 * 使用图像中图形上下文的当前字体和颜色绘制指定文本.<BR>
	 * 最左侧字符的基线位于此图形上下文坐标系的 (x, y) 位置处.<BR>
	 * 
	 * <pre>
	 * 示例：ImageUtils.drawText(image, 50, 50, &quot;HELLO&quot;);
	 * </pre>
	 * 
	 * @param image
	 *            给定图像
	 * @param x
	 *            输入起始点的x坐标
	 * @param y
	 *            输入起始点的y坐标
	 * @param text
	 *            待写入的文本
	 * @throws Exception
	 */
	public static void drawText(BufferedImage image, int x, int y, String text) throws Exception {
		Graphics2D graphics = image.createGraphics();
		try {
			graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			graphics.drawString(text, x, y);
		}
		catch (Exception ex) {
			throw ex;
		}
		finally {
			graphics.dispose();
		}
	}

	/**
	 * 将指定文本写入图像上的指定区域中,并指定对齐方式.<BR>
	 * 
	 * @param graphics
	 *            给定图像
	 * @param x
	 *            区域起点的x坐标
	 * @param y
	 *            区域起点的y坐标
	 * @param width
	 *            区域的宽度
	 * @param height
	 *            区域的高度
	 * @param horizontalAlignment
	 *            横向对齐方式
	 * @param verticalAlignment
	 *            纵向对齐方式
	 * @param text
	 *            待写入的文本
	 * @throws Exception
	 */
	public static void drawText(Graphics graphics, int x, int y, int width, int height, HorizontalAlignment horizontalAlignment,
	        VerticalAlignment verticalAlignment, String text) throws Exception {
		try {
			if (graphics instanceof Graphics2D) {
				((Graphics2D) graphics).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			}
			FontMetrics fontMetrics = graphics.getFontMetrics();
			// 取得切割后的一行行文本内容
			List<String> lineInfos = splitStringByWidth(graphics, null, width, text);
			// 文本被切割的的行数
			int lineCount = lineInfos.size();
			// 字符所占的高度
			int yStep = fontMetrics.getHeight() + y;
			// 字符起始x坐标
			int newX = x;
			// 遍历一行行文本内容，并计算出坐标画入图像中
			for (String info : lineInfos) {
				layoutTextWithDifferentAlignment(graphics, fontMetrics, lineCount, info, newX, yStep, width, height, horizontalAlignment,
				                                 verticalAlignment);
				yStep += fontMetrics.getHeight();
			}
		}
		catch (Exception ex) {
			throw ex;
		}
	}

	/**
	 * 使用图像中图形上下文的当前字体和颜色绘制指定文本.<BR>
	 * 最左侧字符的基线位于此图形上下文坐标系的 (x, y) 位置处.<BR>
	 * 
	 * @param graphics
	 *            给定图像
	 * @param x
	 *            输入起始点的x坐标
	 * @param y
	 *            输入起始点的y坐标
	 * @param text
	 *            待写入的文本
	 * @throws Exception
	 */
	public static void drawText(Graphics graphics, int x, int y, String text) throws Exception {
		try {
			if (graphics instanceof Graphics2D) {
				((Graphics2D) graphics).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			}
			graphics.drawString(text, x, y);
		}
		catch (Exception ex) {
			throw ex;
		}
	}

	/**
	 * 返回gif图像中每一帧的延迟时间.<BR>
	 * 
	 * @param file
	 *            输入的gif图像文件
	 * @return 每一帧的延迟时间的集合
	 * @throws Exception
	 */
	public static List<Integer> getGifImageDelayList(File file) throws Exception {
		List<Integer> delayList = new ArrayList<Integer>();
		FileInputStream inputStream;
		try {
			inputStream = new FileInputStream(file);
		}
		catch (FileNotFoundException e) {
			throw e;
		}
		GifDecoder decoder = new GifDecoder();
		decoder.read(inputStream);

		int count = decoder.getFrameCount();
		for (int i = 0; i < count; i++) {
			delayList.add(decoder.getDelay(i));
		}
		return delayList;
	}

	/**
	 * 实现将一个gif图片中的每一帧作为一张图片，返回一个图片的集合.<BR>
	 * 
	 * @param file
	 *            输入的gif图片文件
	 * @return 由gif图片的每一帧组成的一个图片集合
	 * @throws Exception
	 */
	public static List<BufferedImage> getGifImageList(File file) throws Exception {
		List<BufferedImage> imageList = new ArrayList<BufferedImage>();
		FileInputStream inputStream;
		try {
			inputStream = new FileInputStream(file);
		}
		catch (FileNotFoundException e) {
			throw e;
		}
		GifDecoder decoder = new GifDecoder();
		decoder.read(inputStream);

		int count = decoder.getFrameCount();
		for (int i = 0; i < count; i++) {
			imageList.add(ImageUtils.createImage(decoder.getFrame(i)));
		}
		return imageList;
	}

	/**
	 * 获取图像中指定字体的字符宽度.<BR>
	 * 
	 * <pre>
	 * 示例：ImageUtils.getStringWidth(image.createGraphics(), font, text)
	 * </pre>
	 * 
	 * @param graphics
	 *            给定图像
	 * @param font
	 *            指定的字体
	 * @param text
	 *            文本内容
	 * @return 文本内容所占的字符宽度
	 */
	public static int getStringWidth(Graphics graphics, Font font, String text) {
		return graphics.getFontMetrics(font).stringWidth(text);
	}

	/**
	 * 获取图像中字符宽度.<BR>
	 * 
	 * <pre>
	 * 示例：ImageUtils.getStringWidth(image.createGraphics(), text)
	 * </pre>
	 * 
	 * @param graphics
	 *            图像
	 * @param text
	 *            文本内容
	 * @return 字符宽度
	 */
	public static int getStringWidth(Graphics graphics, String text) {
		return graphics.getFontMetrics().stringWidth(text);
	}

	/**
	 * 按指定对齐方式在矩形框内书写文本内容.
	 * 
	 * @param graphics
	 *            图像
	 * @param fontMetrics
	 *            字体
	 * @param lineCount
	 *            输入的文本在指定矩形框中的行数
	 * @param lineContent
	 *            一行文本内容
	 * @param yStep
	 *            (字符高度*行数)所计算出的应空余的y坐标的高度
	 * @param width
	 *            矩形框宽度
	 * @param height
	 *            矩形框高度
	 * @param horizontalAlignment
	 *            横向对齐方式
	 * @param verticalAlignment
	 *            纵向对齐方式
	 */
	private static void layoutTextWithDifferentAlignment(Graphics graphics, FontMetrics fontMetrics, int lineCount, String lineContent, int x,
	        int yStep, int width, int height, HorizontalAlignment horizontalAlignment, VerticalAlignment verticalAlignment) {
		// 默认格式为横向左对齐，纵向上对齐
		int newX = x;
		int newY = yStep;

		// 设定横向居中和右对齐的x坐标算法
		if (horizontalAlignment == HorizontalAlignment.CENTER) {
			newX = newX + (width - fontMetrics.stringWidth(lineContent)) / 2;
		} else if (horizontalAlignment == HorizontalAlignment.RIGHT) {
			newX = newX + width - fontMetrics.stringWidth(lineContent);
		}

		// 设定纵向居中和下对齐的y坐标算法
		if (verticalAlignment == VerticalAlignment.CENTER) {
			newY = yStep + (height - lineCount * fontMetrics.getHeight() - fontMetrics.getHeight()) / 2;
		} else if (verticalAlignment == VerticalAlignment.BOTTOM) {
			newY = yStep + height - lineCount * fontMetrics.getHeight() - fontMetrics.getHeight() / 2;
		}

		// 将指定文字写入图像中，写入的起始坐标为(newX, newY)
		graphics.drawString(lineContent, newX, newY);
	}

	/**
	 * 打开一个指定图像文件.
	 * 
	 * @param file
	 *            指定文件
	 * @return 包含解码的输入内容的BufferedImage
	 * @throws Exception
	 */
	public static BufferedImage openImage(File file) throws Exception {
		return ImageUtils.createImage(ImageIO.read(file));
	}

	/**
	 * 打开一个指定图像文件.
	 * 
	 * @param filename
	 *            指定文件名
	 * @return 包含解码的输入内容的BufferedImage
	 * @throws Exception
	 */
	public static BufferedImage openImage(String filename) throws Exception {
		return openImage(new File(filename));
	}

	/**
	 * 按比例重新设置图片大小.<BR>
	 * 
	 * <pre>
	 * 示例：ImageUtils.resize(image, 0.5);
	 * </pre>
	 * 
	 * @param image
	 *            原图片
	 * @param percent
	 *            比率
	 * @return 新图片
	 * @throws Exception
	 */
	public static BufferedImage resize(BufferedImage image, double percent) throws Exception {
		// 默认ResizeType = SCALE_TO_NEWSIZE
		int newWidth = (int) (image.getWidth() * percent);
		int newHeight = (int) (image.getHeight() * percent);

		return resize(image, newWidth, newHeight);
	}

	/**
	 * 重新设置图片大小，填充满新的图片.<BR>
	 * 
	 * <pre>
	 * 示例：ImageUtils.resize(image, 400, 100);
	 * </pre>
	 * 
	 * @param image
	 *            原图片
	 * @param newWidth
	 *            新图的长
	 * @param newHeight
	 *            新图的高
	 * @return 新图
	 * @throws Exception
	 */
	public static BufferedImage resize(BufferedImage image, int newWidth, int newHeight) throws Exception {
		// 默认ResizeType = SCALE_TO_NEWSIZE
		BufferedImage newImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics = newImage.createGraphics();
		try {
			Image img = image.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
			graphics.drawImage(img, 0, 0, null);
			return newImage;
		}
		catch (Exception ex) {
			throw ex;
		}
		finally {
			graphics.dispose();
		}
	}

	/**
	 * 重新设置图片大小，并选择填充方式.<BR>
	 * 其中：填充方式由枚举类型FitType指定.
	 * 
	 * <pre>
	 * 示例：ImageUtils.resize(image, 400, 100,
	 * FitType.KEEP_ASPECT_RATIO_AND_KEEP_FULL_IMGAGE);
	 * </pre>
	 * 
	 * @param image
	 *            原图片
	 * @param widthNew
	 *            新图的宽
	 * @param heightNew
	 *            新图的高
	 * @param fitType
	 *            填充方式
	 * @return 新图
	 * @throws Exception
	 */
	public static BufferedImage resize(BufferedImage image, int widthNew, int heightNew, FitType fitType) throws Exception {
		// 原图的宽
		int widthOri = image.getWidth();
		// 原图的高
		int heightOri = image.getHeight();
		// 原图的宽高比
		double percentOri = (double) widthOri / (double) heightOri;
		// 新图的宽高比
		double percentNew = (double) widthNew / (double) heightNew;

		Image scaledImage;

		// // 用于存放原图被截取的内容的坐标
		int[] drawPositionXY = new int[2];
		// double percent = 1.0;

		// 使用KEEP_ASPECT_RATIO_AND_FILL_NEWSIZE的填充方式,计算坐标
		if (fitType == FitType.KEEP_ASPECT_RATIO_AND_FILL_NEWSIZE) {
			if (percentOri < percentNew) {
				int h = (int) (widthNew / percentOri);
				scaledImage = image.getScaledInstance(widthNew, h, Image.SCALE_SMOOTH);
				drawPositionXY[0] = 0;
				drawPositionXY[1] = -((h - heightNew) / 2);
			} else {
				int w = (int) (heightNew * percentOri);
				scaledImage = image.getScaledInstance(w, heightNew, Image.SCALE_SMOOTH);
				drawPositionXY[0] = -((w - widthNew) / 2);
				drawPositionXY[1] = 0;
			}
		}
		// 使用KEEP_ASPECT_RATIO_AND_KEEP_FULL_IMGAGE的填充方式,计算坐标
		else if (fitType == FitType.KEEP_ASPECT_RATIO_AND_KEEP_FULL_IMGAGE) {

			if (percentOri < percentNew) {
				widthNew = (int) (heightNew * percentOri);
				scaledImage = image.getScaledInstance(widthNew, heightNew, Image.SCALE_SMOOTH);
			} else {
				heightNew = (int) (widthNew / percentOri);
				scaledImage = image.getScaledInstance(widthNew, heightNew, Image.SCALE_SMOOTH);
			}
			drawPositionXY[0] = 0;
			drawPositionXY[1] = 0;
		} else {
			// 使用SCALE_TO_NEWSIZE的填充方式
			return ImageUtils.resize(image, widthNew, heightNew);
		}

		// 绘制新图片
		BufferedImage newImage = new BufferedImage(widthNew, heightNew, BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics = newImage.createGraphics();
		try {
			graphics.drawImage(scaledImage, drawPositionXY[0], drawPositionXY[1], null);
			return newImage;
		}
		catch (Exception ex) {
			throw ex;
		}
		finally {
			graphics.dispose();
		}
	}

	/**
	 * 保存图片.<BR>
	 * 
	 * <pre>
	 * 示例：ImageUtils.saveImage(image, ImageFormat.BMP, new File(&quot;image.bmp&quot;))
	 * </pre>
	 * 
	 * @param image
	 *            图像
	 * @param type
	 *            图像类型
	 * @param file
	 *            保存文件
	 * @throws Exception
	 */
	public static void saveImage(BufferedImage image, ImageFormat type, File file) throws Exception {
		try {
			File parentFile = file.getParentFile();
			if (parentFile != null && !parentFile.exists()) {
				parentFile.mkdirs();
			}
		}
		catch (Exception e) {
			throw e;
		}
		// 如果是JPG则使用writeImageAsJPEG(quality=0.8)
		if (type == ImageFormat.JPEG) {
			saveImageAsJPEG(image, file, 0.8);
		} else {
			ImageIO.write(image, type.name(), file);
		}
	}

	/**
	 * 将图像保存为gif格式.<BR>
	 * 
	 * <pre>
	 * 示例：List&lt;BufferedImage&gt; imageList = new ArrayList&lt;BufferedImage&gt;();
	 * &lt;BR&gt;
	 * imageList.add(ImageUtils.openImage(new File(&quot;小猫.jpg&quot;)));
	 * &lt;BR&gt;
	 * imageList.add(ImageUtils.openImage(new File(&quot;小狗.jpg&quot;)));
	 * &lt;BR&gt;
	 * imageList.add(ImageUtils.openImage(new File(&quot;小花.jpg&quot;)));
	 * &lt;BR&gt;
	 * imageList.add(ImageUtils.openImage(new File(&quot;多多.jpg&quot;)));
	 * &lt;BR&gt;
	 * imageList.add(ImageUtils.openImage(new File(&quot;走走.jpg&quot;)));
	 * &lt;BR&gt;
	 * ImageUtils.saveImageAsGIF(imageList, new File(&quot;all.gif&quot;), 800);
	 * </pre>
	 * 
	 * @param imageList
	 *            图像集合
	 * @param file
	 *            保存文件
	 * @param millisecondsPerFrame
	 *            每帧图片显示的间隔时间
	 * @throws Exception
	 */
	public static void saveImageAsGIF(List<BufferedImage> imageList, File file, int millisecondsPerFrame) throws Exception {
		try {
			File parentFile = file.getParentFile();
			if (parentFile != null && !parentFile.exists()) {
				parentFile.mkdirs();
			}
		}
		catch (Exception e) {
			throw e;
		}
		try {
			if (imageList != null && imageList.size() > 0) {
				AnimatedGifEncoder e = new AnimatedGifEncoder();
				e.setRepeat(0);
				e.start(file.getPath()); // 开始处理
				for (int i = 0; i < imageList.size(); i++) {
					e.setDelay(millisecondsPerFrame); // 设置延迟时间
					e.addFrame(imageList.get(i)); // 加入Frame
				}
				e.finish();
			}
		}
		catch (Exception ex) {
			throw ex;
		}
	}

	/**
	 * 将图像保存为gif格式,并指定每一帧的不同的间隔时间.<BR>
	 * 
	 * @param imageList
	 *            图像集合
	 * @param file
	 *            保存文件
	 * @param millisecondsPerFrames
	 *            每帧图片显示的间隔时间的集合
	 * @throws Exception
	 */
	public static void saveImageAsGIF(List<BufferedImage> imageList, File file, List<Integer> millisecondsPerFrames) throws Exception {
		try {
			File parentFile = file.getParentFile();
			if (parentFile != null && !parentFile.exists()) {
				parentFile.mkdirs();
			}
		}
		catch (Exception e) {
			throw e;
		}
		try {
			if (imageList != null && imageList.size() > 0) {
				AnimatedGifEncoder e = new AnimatedGifEncoder();
				e.setRepeat(0);
				e.start(file.getPath()); // 开始处理
				for (int i = 0; i < imageList.size(); i++) {
					e.setDelay(millisecondsPerFrames.get(i)); // 设置延迟时间
					e.addFrame(imageList.get(i)); // 加入Frame
				}
				e.finish();
			}
		}
		catch (Exception ex) {
			throw ex;
		}
	}

	/**
	 * 将图像保存为jpeg格式,并指定图像压缩质量.<BR>
	 * 
	 * <pre>
	 * 示例：ImageUtils.saveImageAsJPEG(image, new File(&quot;image.jpg&quot;), 0.8);
	 * </pre>
	 * 
	 * @param image
	 *            图像
	 * @param file
	 *            保存文件
	 * @param compressionQuality
	 *            图像压缩质量(compressionQuality>=0&&compressionQuality<=1)
	 * @throws Exception
	 */
	public static void saveImageAsJPEG(BufferedImage image, File file, double compressionQuality) throws Exception {
		File parentFile = file.getParentFile();
		if (parentFile != null && !parentFile.exists()) {
			parentFile.mkdirs();
		}
		// 寻找jpg图像文件输出器
		ImageWriter writer = null;
		Iterator<ImageWriter> iter = ImageIO.getImageWritersByFormatName("jpg");
		if (iter.hasNext()) {
			writer = iter.next();
		}

		// 设置输出文件
		ImageOutputStream ios = ImageIO.createImageOutputStream(file);
		writer.setOutput(ios);

		// 设置压缩质量
		ImageWriteParam iwparam = new JPEGImageWriteParam(null);
		iwparam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
		iwparam.setCompressionQuality((float) compressionQuality);

		// 将图片写出
		writer.write(null, new IIOImage(image, null, null), iwparam);

		// 释放清除
		ios.flush();
		writer.dispose();
		ios.close();
	}

	/**
	 * 将图像保存为jpeg格式,并指定图像字节数大小,单位为字节.<BR>
	 * 首先将图像按质量1保存，若保存后文件大小>fileSize,则质量改为0.8重新保存，依次类推，质量分别为1 0.8 0.6 0.4
	 * 0.2，最小为0.2
	 * 
	 * @param image
	 *            图像
	 * @param file
	 *            保存文件
	 * @param fileSize
	 *            保存后的文件的最大大小
	 * @throws Exception
	 */
	public static void saveImageAsJPEG(BufferedImage image, File file, int fileSize) throws Exception {
		if (fileSize <= 1) {
			saveImageAsJPEG(image, file, (double) fileSize);
			return;
		}

		File parentFile = file.getParentFile();
		if (parentFile != null && !parentFile.exists()) {
			parentFile.mkdirs();
		}
		// 寻找jpg图像文件输出器
		ImageWriter writer = ImageIO.getImageWritersByFormatName("jpg").next();
		//
		ByteArrayOutputStream baos = new ByteArrayOutputStream(fileSize);
		ImageOutputStream ios = ImageIO.createImageOutputStream(baos);
		// 设置压缩质量
		ImageWriteParam iwparam = new JPEGImageWriteParam(null);
		iwparam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
		float quality = 1.0f;
		for (int i = 0; i < 5; i++) {
			// 设置输出文件
			baos.reset();
			writer.setOutput(ios);
			//
			iwparam.setCompressionQuality(quality);
			// 将图片写出
			writer.write(null, new IIOImage(image, null, null), iwparam);
			int size = baos.toByteArray().length;
			if (size > fileSize) {
				quality = quality - 0.2f;
			} else {
				break;
			}
		}
		// 输出到文件
		FileTools.write(baos.toByteArray(), file.getPath());

		// 释放清除
		ios.flush();
		writer.dispose();
		baos.close();
		ios.close();
	}

	/**
	 * 返回按指定宽度分割的一行行文本内容.<BR>
	 * 
	 * <pre>
	 * 示例：ImageUtils.splitStringByWidth(image, null, 100,
	 * &quot;今天\r\n你好吗1今天你好吗2今天你好吗&quot;);
	 * List中的内容为：
	 * line1：今天
	 * line2：你好吗1今天你好
	 * line3：吗2今天你好吗
	 * </pre>
	 * 
	 * @param graphics
	 *            图像
	 * @param font
	 *            指定字体，若无则为null
	 * @param width
	 *            一行宽度
	 * @param text
	 *            文本内容
	 * @return 存放一行行文本内容的list
	 */
	public static List<String> splitStringByWidth(Graphics graphics, Font font, int width, String text) {
		StringBuilder builder = new StringBuilder(500);
		StringBuilder textContent = new StringBuilder(500);
		FontMetrics fontMetrics = graphics.getFontMetrics();
		if (font != null) {
			fontMetrics = graphics.getFontMetrics(font);
		}
		// 定义一个List，用于存放一行行文本内容
		List<String> lineInfos = new ArrayList<String>();
		// 以换行符为分割拆分文本
		String[] texts = text.split("\r\n");
		for (int j = 0; j < texts.length; j++) {
			text = texts[j];
			// 如果为空，直接换下一行
			if (text.equals("")) {
				lineInfos.add("");
				continue;
			}
			for (int i = 0; i < text.length(); i++) {
				String charContent = text.substring(i, i + 1);
				builder.append(charContent);
				// 如果本行长度未超出矩形的宽，则继续添加文字
				if (fontMetrics.stringWidth(builder.toString()) <= width) {
					textContent.append(charContent);
				}
				// 如果再添加一个字符长度就超出的话，则将这行内容放入lineInfos
				else {
					lineInfos.add(textContent.toString());
					builder.setLength(0);
					textContent.setLength(0);
					builder.append(charContent);
					textContent.append(charContent);
				}
				// 到最后一个字符了，将这行内容加入lineInfos
				if (i == text.length() - 1) {
					lineInfos.add(textContent.toString());
				}
			}
			builder.setLength(0);
			textContent.setLength(0);
		}
		return lineInfos;
	}
}
