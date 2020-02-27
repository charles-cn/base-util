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
 * ͼ�������. <br>
 * �����˶�ͼ��͸��ֲ��������������½������棬���õȵ�.<br>
 * ����ʵ��ͼ��ߴ�ת������ʹ����� com.haocom.util.image.image_magic
 * 
 * <pre>
 * 
 * public class ImageDemo {
 * 
 * 	public static void main(String[] args) throws Exception {
 * 		// ��������.
 * 		if (1 == 2) {
 * 			ImageUtils.createFont(new File(&quot;d://STKAITI.TTF &quot;), 2, 20);
 * 			Font font = ImageUtils.createFont(&quot;d://STKAITI.TTF &quot;, 2, 20);
 * 			ImageUtils.createFont(font, 2, 20);
 * 		}
 * 		// ��ͼƬ������ͼƬ�ͱ���ͼƬ.
 * 		if (1 == 2) {
 * 			BufferedImage newImage1 = ImageUtils.createImage(400, 200);
 * 			// ����ͼƬ����ָ����ʽ.
 * 			ImageUtils.saveImage(newImage1, ImageFormat.BMP, new File(&quot;d://newImage1.bmp&quot;));
 * 
 * 			File file = new File(&quot;d://5.jpg&quot;);
 * 			BufferedImage image = ImageUtils.openImage(file);
 * 			BufferedImage newImage2 = ImageUtils.createImage(image);
 * 			// ����ΪͼƬjpeg��ʽ.
 * 			ImageUtils.saveImage(newImage2, ImageFormat.JPEG, new File(&quot;d://newImage2.jpg&quot;));
 * 			ImageUtils.saveImageAsJPEG(newImage2, new File(&quot;d://newImage3.jpg&quot;), 1);
 * 			// ����ͼƬΪgif��ʽ.
 * 			List&lt;BufferedImage&gt; imageList = new ArrayList&lt;BufferedImage&gt;();
 * 			imageList.add(ImageUtils.openImage(new File(&quot;d://1.jpg&quot;)));
 * 			imageList.add(ImageUtils.openImage(new File(&quot;d://2.jpg&quot;)));
 * 			imageList.add(ImageUtils.openImage(new File(&quot;d://3.jpg&quot;)));
 * 			imageList.add(ImageUtils.openImage(new File(&quot;d://4.jpg&quot;)));
 * 			imageList.add(ImageUtils.openImage(new File(&quot;d://5.jpg&quot;)));
 * 			ImageUtils.saveImageAsGIF(imageList, new File(&quot;d://all.gif&quot;), 800);
 * 		}
 * 		// ����ͼƬ.
 * 		if (1 == 2) {
 * 			File file = new File(&quot;d://2.jpg&quot;);
 * 			BufferedImage image = ImageUtils.openImage(file);
 * 			BufferedImage newImage = ImageUtils.crop(image, 0, 0, 200, 200);
 * 			ImageUtils.saveImage(newImage, ImageFormat.JPEG, new File(&quot;d://crop.jpg&quot;));
 * 		}
 * 		// �ڸ���ͼƬ��д������.
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
 * 			String aa = &quot;����\r\n�����1���������2���������3���������4���������5���������6���������7���������8���������9���������10���������11���������12���������13���������14���������15���������16���������17���������18���������19���������20���������21���������22���������23���������24���������25���������26���������27���������28���������29&quot;;
 * 			String bb = &quot;һ�����������������߰�&quot;;
 * 			String cc = &quot;��\r\n\r\n��\r\n��\r\n��\r\n��\r\n��\r\n��\r\n��\r\n��\r\n��\r\n&quot;;
 * 			BufferedImage image3 = ImageUtils.openImage(file);
 * 			ImageUtils.drawText(image3, font, Color.red, 0, 0, 200, 200, HorizontalAlignment.RIGHT, VerticalAlignment.CENTER, bb);
 * 			ImageUtils.saveImage(image3, ImageFormat.JPEG, new File(&quot;d://drawText3.jpg&quot;));
 * 		}
 * 		// �����趨ͼƬ��С.
 * 		if (1 == 2) {
 * 			File file = new File(&quot;d://3.jpg&quot;);
 * 			BufferedImage image = ImageUtils.openImage(file);
 * 			// BufferedImage newImage = ImageUtils.resize(image, 0.5);
 * 			BufferedImage newImage = ImageUtils.resize(image, 400, 100, FitType.KEEP_ASPECT_RATIO_AND_KEEP_FULL_IMGAGE);
 * 			ImageUtils.saveImage(newImage, ImageFormat.JPEG, new File(&quot;d://resize.jpg&quot;));
 * 		}
 * 		// ʵ�ֽ�һ��gifͼƬ��ÿһ֡ͼ��Ϊһ��image������һ��imageList����һһ����.
 * 		if (1 == 2) {
 * 			File file = new File(&quot;d://001.gif&quot;);
 * 			List&lt;BufferedImage&gt; imageList = ImageUtils.getGifImageList(file);
 * 			for (int i = 0; i &lt; imageList.size(); i++) {
 * 				ImageUtils.saveImage(imageList.get(i), ImageFormat.JPEG, new File(&quot;d://1111&quot; + i + &quot;.jpg&quot;));
 * 			}
 * 		}
 * 		// ʵ�ֽ�ͼ�񱣴�Ϊgif��ʽ��������ÿһ֡��ͬ���ӳ�ʱ��.
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
 * 		// ʵ�ֻ�ȡgifͼ��ÿһ֡���ӳ�ʱ��.
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

	/** ͼ����䷽ʽ����Ҫ����ͼ������ */
	public static enum FitType {
		/** ��������䣬���������µ����� */
		KEEP_ASPECT_RATIO_AND_FILL_NEWSIZE,

		/** ��������䣬ʹ�µ�������Ȼ����ԭͼ�ı����ߴ� */
		KEEP_ASPECT_RATIO_AND_KEEP_FULL_IMGAGE,

		/** �������С�������������� */
		SCALE_TO_NEWSIZE;
	}

	/** ͼ�������ֵĺ�����뷽ʽ */
	public static enum HorizontalAlignment {
		/** ���ж��� */
		CENTER,

		/** ����� */
		LEFT,

		/** �Ҷ��� */
		RIGHT;
	}

	/** ͼ�����ɫ�������� */
	public static enum ImageColorType {
		/** ��ʾһ��ͼ�������кϳ��������ص�8λRGBA��ɫ���� */
		ARGB,

		/** ��ʾһ��ͼ�������кϳ��������ص�8λRGB��ɫ���� */
		RGB;
	}

	/** ͼ��ĸ�ʽ */
	public static enum ImageFormat {
		/** ͼ���ʽΪBMP */
		BMP,

		/** ͼ���ʽΪGIF */
		GIF,

		/** ͼ���ʽΪJPEG */
		JPEG,

		/** ͼ���ʽΪPNG */
		PNG,

		/** ͼ���ʽΪWBMP */
		WBMP;
	}

	/** ͼ�������ֵ�������뷽ʽ */
	public static enum VerticalAlignment {
		/** �¶��� */
		BOTTOM,

		/** ���ж��� */
		CENTER,

		/** �϶��� */
		TOP;
	}

	/**
	 * �Ӹ����������ļ��ж�ȡ�����������µ�����.
	 * 
	 * @param fontFile
	 *            �����ļ������磺/data/mms/tahoma.ttf
	 * @param style
	 *            ���������ʽ<BR>
	 *            ���У�0��ʾ��ͨ��ʽ��1��ʾ������ʽ ��2��ʾб����ʽ��3��ʾ����+б�壻 �����������־���ͬ��0
	 * @param size
	 *            ������İ�ֵ��С
	 * @return ��ȡ������
	 * @throws IOException
	 * @throws FontFormatException
	 */
	public static Font createFont(File fontFile, int style, double size) throws FontFormatException, IOException {
		Font font = Font.createFont(Font.TRUETYPE_FONT, fontFile);
		font = font.deriveFont(style, (float) size);
		return font;
	}

	/**
	 * ��������������������µ�����.
	 * 
	 * @param font
	 *            �����������
	 * @param style
	 *            ���������ʽ<BR>
	 *            ���У�0��ʾ��ͨ��ʽ��1��ʾ������ʽ ��2��ʾб����ʽ��3��ʾ����+б�壻 �����������־���ͬ��0
	 * @param size
	 *            ������İ�ֵ��С
	 * @return ������������
	 */
	public static Font createFont(Font font, int style, double size) {
		return font.deriveFont(style, (float) size);
	}

	/**
	 * �Ӹ����������ļ��ж�ȡ�����������µ�����.
	 * 
	 * @param fontFilename
	 *            �����ļ������磺/data/mms/tahoma.ttf
	 * @param style
	 *            ���������ʽ<BR>
	 *            ���У�0��ʾ��ͨ��ʽ��1��ʾ������ʽ ��2��ʾб����ʽ��3��ʾ����+б�壻 �����������־���ͬ��0
	 * @param size
	 *            ������İ�ֵ��С
	 * @return ������������
	 * @throws FontFormatException
	 * @throws IOException
	 */
	public static Font createFont(String fontFilename, int style, double size) throws FontFormatException, IOException {
		Font font = Font.createFont(Font.TRUETYPE_FONT, new File(fontFilename));
		font = font.deriveFont(style, (float) size);
		return font;
	}

	/**
	 * ����һ���µ�ͼ��,��ͼ�����ݼ�Ϊ����ͼ�������.
	 * 
	 * @param image
	 *            ����ͼ��
	 * @return �´�����ͼ��
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
	 * ����һ����ͼ��.<BR>
	 * ��ͼ������Ϊ��,Ĭ����ɫΪ��ɫ,��ͼ���ColorSpaceΪĬ�ϵ�RGB�ռ�.<BR>
	 * 
	 * <pre>
	 * ʾ����ImageUtils.createImage(300, 500)
	 * </pre>
	 * 
	 * @param width
	 *            ������ͼ��Ŀ��
	 * @param height
	 *            ������ͼ��ĸ߶�
	 * @return �´�����ͼ��
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
	 * ����һ����ͼ��.<BR>
	 * ��ͼ������Ϊ��,��ͼ���ColorSpaceΪĬ�ϵ�RGB�ռ�.<BR>
	 * 
	 * <pre>
	 * ʾ����ImageUtils.createImage(300, 500, Color.yellow)
	 * </pre>
	 * 
	 * @param width
	 *            ������ͼ��Ŀ��
	 * @param height
	 *            ������ͼ��ĸ߶�
	 * @param color
	 *            ������ͼ�����ɫ
	 * @return �´�����ͼ��
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
	 * ����һ������ΪԤ����ͼ������֮һ��ͼ��.<BR>
	 * ͼ��������ö������ImageColorTypeָ��������BMP,GIF,JPEG,PNG,WBMP.
	 * 
	 * @param width
	 *            ������ͼ��Ŀ��
	 * @param height
	 *            ������ͼ��ĸ߶�
	 * @param color
	 *            ������ͼ�����ɫ
	 * @param imageType
	 *            ������ͼ�������
	 * @return �´�����ͼ��
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
	 * �ü�ͼƬ��ָ��λ�úʹ�С������.<BR>
	 * 
	 * <pre>
	 * ʾ����ImageUtils.crop(image, 20, 20, 100, 100);
	 * </pre>
	 * 
	 * @param image
	 *            ���ü�ͼƬ
	 * @param x
	 *            �ü�����ʼx����
	 * @param y
	 *            �ü�����ʼy����
	 * @param width
	 *            �ü�����
	 * @param height
	 *            �ü��߶�
	 * @return �ü����õ�ͼƬ
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
	 * ��ͼƬ��ˮӡ.<br>
	 * ��(x,y)����Ϊ��ˮӡ����ʼ�㣬(x,y)��ΪˮӡͼƬ�ڷ�λ�õ����Ͻǵ㣬ˮӡ��СΪ������ˮӡͼƬ��С��
	 * 
	 * @param destImage
	 *            ԭʼͼƬ
	 * @param waterMark
	 *            ˮӡͼƬ
	 * @param x
	 *            ��ˮӡ����ʼX����
	 * @param y
	 *            ��ˮӡ����ʼY����
	 * @param alpha
	 *            ͸����,��Χ[0.0-1.0]֮�䣬0��ʾȫ͸����1��ʾ��͸��
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
	 * ��ͼƬ��ˮӡ.<br>
	 * ��(x,y)����Ϊ��ˮӡ����ʼ�㣬(x,y)��ΪˮӡͼƬ�ڷ�λ�õ����Ͻǵ㣬ˮӡ��СΪwidth*hight��
	 * 
	 * @param destImage
	 *            ԭʼͼƬ
	 * @param waterMark
	 *            ˮӡͼƬ
	 * @param x
	 *            ��ˮӡ����ʼX����
	 * @param y
	 *            ��ˮӡ����ʼY����
	 * @param width
	 *            ˮӡ�Ŀ�
	 * @param hight
	 *            ˮӡ�ĸ�
	 * @param alpha
	 *            ͸����,��Χ[0.0-1.0]֮�䣬0��ʾȫ͸����1��ʾ��͸��
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
	 * ��ָ���ı�д��ͼ���ϵ�ָ��������,��ָ�����뷽ʽ,���壬��ɫ.<BR>
	 * 
	 * <pre>
	 * ʾ����ImageUtils.drawText(image, font, Color.red, 0, 0, 300, 200,
	 * HorizontalAlignment.RIGHT, VerticalAlignment.BOTTOM, ��һ�����������������߰�&quot;);
	 * </pre>
	 * 
	 * @param image
	 *            ����ͼ��
	 * @param font
	 *            ָ��д����ı�������
	 * @param color
	 *            ָ��д����ı�����ɫ
	 * @param x
	 *            ��������x����
	 * @param y
	 *            ��������y����
	 * @param width
	 *            ����Ŀ��
	 * @param height
	 *            ����ĸ߶�
	 * @param horizontalAlignment
	 *            ������뷽ʽ
	 * @param verticalAlignment
	 *            ������뷽ʽ
	 * @param text
	 *            ��д����ı�
	 * @throws Exception
	 */
	public static void drawText(BufferedImage image, Font font, Color color, int x, int y, int width, int height,
	        HorizontalAlignment horizontalAlignment, VerticalAlignment verticalAlignment, String text) throws Exception {
		// ��ԭͼ�ϼ��ó�ָ�����ο�λ�úʹ�С��ͼƬ���Ա��ڽ��ı����ݻ��Ƶ�������ͼƬ��
		BufferedImage newImage = crop(image, x, y, width, height);
		// ԭͼ
		Graphics2D oldGraphics = image.createGraphics();
		// ��ͼ����ԭͼ�вü����ֶ���
		Graphics2D graphics = newImage.createGraphics();
		try {
			graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			graphics.setFont(font);
			graphics.setColor(color);
			FontMetrics fontMetrics = graphics.getFontMetrics(font);
			// ȡ���и���һ�����ı�����
			List<String> lineInfos = splitStringByWidth(graphics, font, width, text);
			// �ı����и�ĵ�����
			int lineCount = lineInfos.size();
			// �ַ���ռ�ĸ߶�
			int yStep = fontMetrics.getHeight();
			// �ַ���ʼx����
			int newX = 0;
			// ����һ�����ı����ݣ�����������껭��ͼ����
			for (String info : lineInfos) {
				layoutTextWithDifferentAlignment(graphics, fontMetrics, lineCount, info, newX, yStep, width, height, horizontalAlignment,
				                                 verticalAlignment);
				yStep += fontMetrics.getHeight();
			}

			// �����ƺ����ͼ���ǵ�ԭͼ����Ӧλ����
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
	 * ʹ��ָ���������ɫ��ͼ���л���ָ���ı�.<BR>
	 * ������ַ��Ļ���λ�ڴ�ͼ������������ϵ�� (x, y) λ�ô�.
	 * 
	 * <pre>
	 * ʾ����ImageUtils.drawText(image, font, Color.red, 50, 100, &quot;HELLO&quot;);
	 * </pre>
	 * 
	 * @param image
	 *            ����ͼ��
	 * @param font
	 *            ����
	 * @param color
	 *            ��ɫ
	 * @param x
	 *            ������ʼ���x����
	 * @param y
	 *            ������ʼ���y����
	 * @param text
	 *            ��д����ı�
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
	 * ��ָ���ı�д��ͼ���ϵ�ָ��������,��ָ�����뷽ʽ.<BR>
	 * 
	 * <pre>
	 * ʾ����ImageUtils.drawText(image, 0, 0, 300, 200, HorizontalAlignment.RIGHT,
	 * VerticalAlignment.BOTTOM, ��һ�����������������߰�&quot;);
	 * </pre>
	 * 
	 * @param image
	 *            ����ͼ��
	 * @param x
	 *            ��������x����
	 * @param y
	 *            ��������y����
	 * @param width
	 *            ����Ŀ��
	 * @param height
	 *            ����ĸ߶�
	 * @param horizontalAlignment
	 *            ������뷽ʽ
	 * @param verticalAlignment
	 *            ������뷽ʽ
	 * @param text
	 *            ��д����ı�
	 * @throws Exception
	 */
	public static void drawText(BufferedImage image, int x, int y, int width, int height, HorizontalAlignment horizontalAlignment,
	        VerticalAlignment verticalAlignment, String text) throws Exception {
		// ��ԭͼ�ϼ��ó�ָ�����ο�λ�úʹ�С��ͼƬ���Ա��ڽ��ı����ݻ��Ƶ�������ͼƬ��
		BufferedImage newImage = crop(image, x, y, width, height);
		// ��ͼ����ԭͼ�вü����ֶ���
		Graphics2D graphics = newImage.createGraphics();
		// ԭͼ
		Graphics2D oldGraphics = image.createGraphics();
		try {
			graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			FontMetrics fontMetrics = graphics.getFontMetrics();
			// ȡ���и���һ�����ı�����
			List<String> lineInfos = splitStringByWidth(graphics, null, width, text);
			// �ı����и�ĵ�����
			int lineCount = lineInfos.size();
			// �ַ���ռ�ĸ߶�
			int yStep = fontMetrics.getHeight();
			// �ַ���ʼx����
			int newX = 0;
			// ����һ�����ı����ݣ�����������껭��ͼ����
			for (String info : lineInfos) {
				layoutTextWithDifferentAlignment(graphics, fontMetrics, lineCount, info, newX, yStep, width, height, horizontalAlignment,
				                                 verticalAlignment);
				yStep += fontMetrics.getHeight();
			}

			// �����ƺ����ͼ���ǵ�ԭͼ����Ӧλ����
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
	 * ʹ��ͼ����ͼ�������ĵĵ�ǰ�������ɫ����ָ���ı�.<BR>
	 * ������ַ��Ļ���λ�ڴ�ͼ������������ϵ�� (x, y) λ�ô�.<BR>
	 * 
	 * <pre>
	 * ʾ����ImageUtils.drawText(image, 50, 50, &quot;HELLO&quot;);
	 * </pre>
	 * 
	 * @param image
	 *            ����ͼ��
	 * @param x
	 *            ������ʼ���x����
	 * @param y
	 *            ������ʼ���y����
	 * @param text
	 *            ��д����ı�
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
	 * ��ָ���ı�д��ͼ���ϵ�ָ��������,��ָ�����뷽ʽ.<BR>
	 * 
	 * @param graphics
	 *            ����ͼ��
	 * @param x
	 *            ��������x����
	 * @param y
	 *            ��������y����
	 * @param width
	 *            ����Ŀ��
	 * @param height
	 *            ����ĸ߶�
	 * @param horizontalAlignment
	 *            ������뷽ʽ
	 * @param verticalAlignment
	 *            ������뷽ʽ
	 * @param text
	 *            ��д����ı�
	 * @throws Exception
	 */
	public static void drawText(Graphics graphics, int x, int y, int width, int height, HorizontalAlignment horizontalAlignment,
	        VerticalAlignment verticalAlignment, String text) throws Exception {
		try {
			if (graphics instanceof Graphics2D) {
				((Graphics2D) graphics).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			}
			FontMetrics fontMetrics = graphics.getFontMetrics();
			// ȡ���и���һ�����ı�����
			List<String> lineInfos = splitStringByWidth(graphics, null, width, text);
			// �ı����и�ĵ�����
			int lineCount = lineInfos.size();
			// �ַ���ռ�ĸ߶�
			int yStep = fontMetrics.getHeight() + y;
			// �ַ���ʼx����
			int newX = x;
			// ����һ�����ı����ݣ�����������껭��ͼ����
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
	 * ʹ��ͼ����ͼ�������ĵĵ�ǰ�������ɫ����ָ���ı�.<BR>
	 * ������ַ��Ļ���λ�ڴ�ͼ������������ϵ�� (x, y) λ�ô�.<BR>
	 * 
	 * @param graphics
	 *            ����ͼ��
	 * @param x
	 *            ������ʼ���x����
	 * @param y
	 *            ������ʼ���y����
	 * @param text
	 *            ��д����ı�
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
	 * ����gifͼ����ÿһ֡���ӳ�ʱ��.<BR>
	 * 
	 * @param file
	 *            �����gifͼ���ļ�
	 * @return ÿһ֡���ӳ�ʱ��ļ���
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
	 * ʵ�ֽ�һ��gifͼƬ�е�ÿһ֡��Ϊһ��ͼƬ������һ��ͼƬ�ļ���.<BR>
	 * 
	 * @param file
	 *            �����gifͼƬ�ļ�
	 * @return ��gifͼƬ��ÿһ֡��ɵ�һ��ͼƬ����
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
	 * ��ȡͼ����ָ��������ַ����.<BR>
	 * 
	 * <pre>
	 * ʾ����ImageUtils.getStringWidth(image.createGraphics(), font, text)
	 * </pre>
	 * 
	 * @param graphics
	 *            ����ͼ��
	 * @param font
	 *            ָ��������
	 * @param text
	 *            �ı�����
	 * @return �ı�������ռ���ַ����
	 */
	public static int getStringWidth(Graphics graphics, Font font, String text) {
		return graphics.getFontMetrics(font).stringWidth(text);
	}

	/**
	 * ��ȡͼ�����ַ����.<BR>
	 * 
	 * <pre>
	 * ʾ����ImageUtils.getStringWidth(image.createGraphics(), text)
	 * </pre>
	 * 
	 * @param graphics
	 *            ͼ��
	 * @param text
	 *            �ı�����
	 * @return �ַ����
	 */
	public static int getStringWidth(Graphics graphics, String text) {
		return graphics.getFontMetrics().stringWidth(text);
	}

	/**
	 * ��ָ�����뷽ʽ�ھ��ο�����д�ı�����.
	 * 
	 * @param graphics
	 *            ͼ��
	 * @param fontMetrics
	 *            ����
	 * @param lineCount
	 *            ������ı���ָ�����ο��е�����
	 * @param lineContent
	 *            һ���ı�����
	 * @param yStep
	 *            (�ַ��߶�*����)���������Ӧ�����y����ĸ߶�
	 * @param width
	 *            ���ο���
	 * @param height
	 *            ���ο�߶�
	 * @param horizontalAlignment
	 *            ������뷽ʽ
	 * @param verticalAlignment
	 *            ������뷽ʽ
	 */
	private static void layoutTextWithDifferentAlignment(Graphics graphics, FontMetrics fontMetrics, int lineCount, String lineContent, int x,
	        int yStep, int width, int height, HorizontalAlignment horizontalAlignment, VerticalAlignment verticalAlignment) {
		// Ĭ�ϸ�ʽΪ��������룬�����϶���
		int newX = x;
		int newY = yStep;

		// �趨������к��Ҷ����x�����㷨
		if (horizontalAlignment == HorizontalAlignment.CENTER) {
			newX = newX + (width - fontMetrics.stringWidth(lineContent)) / 2;
		} else if (horizontalAlignment == HorizontalAlignment.RIGHT) {
			newX = newX + width - fontMetrics.stringWidth(lineContent);
		}

		// �趨������к��¶����y�����㷨
		if (verticalAlignment == VerticalAlignment.CENTER) {
			newY = yStep + (height - lineCount * fontMetrics.getHeight() - fontMetrics.getHeight()) / 2;
		} else if (verticalAlignment == VerticalAlignment.BOTTOM) {
			newY = yStep + height - lineCount * fontMetrics.getHeight() - fontMetrics.getHeight() / 2;
		}

		// ��ָ������д��ͼ���У�д�����ʼ����Ϊ(newX, newY)
		graphics.drawString(lineContent, newX, newY);
	}

	/**
	 * ��һ��ָ��ͼ���ļ�.
	 * 
	 * @param file
	 *            ָ���ļ�
	 * @return ����������������ݵ�BufferedImage
	 * @throws Exception
	 */
	public static BufferedImage openImage(File file) throws Exception {
		return ImageUtils.createImage(ImageIO.read(file));
	}

	/**
	 * ��һ��ָ��ͼ���ļ�.
	 * 
	 * @param filename
	 *            ָ���ļ���
	 * @return ����������������ݵ�BufferedImage
	 * @throws Exception
	 */
	public static BufferedImage openImage(String filename) throws Exception {
		return openImage(new File(filename));
	}

	/**
	 * ��������������ͼƬ��С.<BR>
	 * 
	 * <pre>
	 * ʾ����ImageUtils.resize(image, 0.5);
	 * </pre>
	 * 
	 * @param image
	 *            ԭͼƬ
	 * @param percent
	 *            ����
	 * @return ��ͼƬ
	 * @throws Exception
	 */
	public static BufferedImage resize(BufferedImage image, double percent) throws Exception {
		// Ĭ��ResizeType = SCALE_TO_NEWSIZE
		int newWidth = (int) (image.getWidth() * percent);
		int newHeight = (int) (image.getHeight() * percent);

		return resize(image, newWidth, newHeight);
	}

	/**
	 * ��������ͼƬ��С��������µ�ͼƬ.<BR>
	 * 
	 * <pre>
	 * ʾ����ImageUtils.resize(image, 400, 100);
	 * </pre>
	 * 
	 * @param image
	 *            ԭͼƬ
	 * @param newWidth
	 *            ��ͼ�ĳ�
	 * @param newHeight
	 *            ��ͼ�ĸ�
	 * @return ��ͼ
	 * @throws Exception
	 */
	public static BufferedImage resize(BufferedImage image, int newWidth, int newHeight) throws Exception {
		// Ĭ��ResizeType = SCALE_TO_NEWSIZE
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
	 * ��������ͼƬ��С����ѡ����䷽ʽ.<BR>
	 * ���У���䷽ʽ��ö������FitTypeָ��.
	 * 
	 * <pre>
	 * ʾ����ImageUtils.resize(image, 400, 100,
	 * FitType.KEEP_ASPECT_RATIO_AND_KEEP_FULL_IMGAGE);
	 * </pre>
	 * 
	 * @param image
	 *            ԭͼƬ
	 * @param widthNew
	 *            ��ͼ�Ŀ�
	 * @param heightNew
	 *            ��ͼ�ĸ�
	 * @param fitType
	 *            ��䷽ʽ
	 * @return ��ͼ
	 * @throws Exception
	 */
	public static BufferedImage resize(BufferedImage image, int widthNew, int heightNew, FitType fitType) throws Exception {
		// ԭͼ�Ŀ�
		int widthOri = image.getWidth();
		// ԭͼ�ĸ�
		int heightOri = image.getHeight();
		// ԭͼ�Ŀ�߱�
		double percentOri = (double) widthOri / (double) heightOri;
		// ��ͼ�Ŀ�߱�
		double percentNew = (double) widthNew / (double) heightNew;

		Image scaledImage;

		// // ���ڴ��ԭͼ����ȡ�����ݵ�����
		int[] drawPositionXY = new int[2];
		// double percent = 1.0;

		// ʹ��KEEP_ASPECT_RATIO_AND_FILL_NEWSIZE����䷽ʽ,��������
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
		// ʹ��KEEP_ASPECT_RATIO_AND_KEEP_FULL_IMGAGE����䷽ʽ,��������
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
			// ʹ��SCALE_TO_NEWSIZE����䷽ʽ
			return ImageUtils.resize(image, widthNew, heightNew);
		}

		// ������ͼƬ
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
	 * ����ͼƬ.<BR>
	 * 
	 * <pre>
	 * ʾ����ImageUtils.saveImage(image, ImageFormat.BMP, new File(&quot;image.bmp&quot;))
	 * </pre>
	 * 
	 * @param image
	 *            ͼ��
	 * @param type
	 *            ͼ������
	 * @param file
	 *            �����ļ�
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
		// �����JPG��ʹ��writeImageAsJPEG(quality=0.8)
		if (type == ImageFormat.JPEG) {
			saveImageAsJPEG(image, file, 0.8);
		} else {
			ImageIO.write(image, type.name(), file);
		}
	}

	/**
	 * ��ͼ�񱣴�Ϊgif��ʽ.<BR>
	 * 
	 * <pre>
	 * ʾ����List&lt;BufferedImage&gt; imageList = new ArrayList&lt;BufferedImage&gt;();
	 * &lt;BR&gt;
	 * imageList.add(ImageUtils.openImage(new File(&quot;Сè.jpg&quot;)));
	 * &lt;BR&gt;
	 * imageList.add(ImageUtils.openImage(new File(&quot;С��.jpg&quot;)));
	 * &lt;BR&gt;
	 * imageList.add(ImageUtils.openImage(new File(&quot;С��.jpg&quot;)));
	 * &lt;BR&gt;
	 * imageList.add(ImageUtils.openImage(new File(&quot;���.jpg&quot;)));
	 * &lt;BR&gt;
	 * imageList.add(ImageUtils.openImage(new File(&quot;����.jpg&quot;)));
	 * &lt;BR&gt;
	 * ImageUtils.saveImageAsGIF(imageList, new File(&quot;all.gif&quot;), 800);
	 * </pre>
	 * 
	 * @param imageList
	 *            ͼ�񼯺�
	 * @param file
	 *            �����ļ�
	 * @param millisecondsPerFrame
	 *            ÿ֡ͼƬ��ʾ�ļ��ʱ��
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
				e.start(file.getPath()); // ��ʼ����
				for (int i = 0; i < imageList.size(); i++) {
					e.setDelay(millisecondsPerFrame); // �����ӳ�ʱ��
					e.addFrame(imageList.get(i)); // ����Frame
				}
				e.finish();
			}
		}
		catch (Exception ex) {
			throw ex;
		}
	}

	/**
	 * ��ͼ�񱣴�Ϊgif��ʽ,��ָ��ÿһ֡�Ĳ�ͬ�ļ��ʱ��.<BR>
	 * 
	 * @param imageList
	 *            ͼ�񼯺�
	 * @param file
	 *            �����ļ�
	 * @param millisecondsPerFrames
	 *            ÿ֡ͼƬ��ʾ�ļ��ʱ��ļ���
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
				e.start(file.getPath()); // ��ʼ����
				for (int i = 0; i < imageList.size(); i++) {
					e.setDelay(millisecondsPerFrames.get(i)); // �����ӳ�ʱ��
					e.addFrame(imageList.get(i)); // ����Frame
				}
				e.finish();
			}
		}
		catch (Exception ex) {
			throw ex;
		}
	}

	/**
	 * ��ͼ�񱣴�Ϊjpeg��ʽ,��ָ��ͼ��ѹ������.<BR>
	 * 
	 * <pre>
	 * ʾ����ImageUtils.saveImageAsJPEG(image, new File(&quot;image.jpg&quot;), 0.8);
	 * </pre>
	 * 
	 * @param image
	 *            ͼ��
	 * @param file
	 *            �����ļ�
	 * @param compressionQuality
	 *            ͼ��ѹ������(compressionQuality>=0&&compressionQuality<=1)
	 * @throws Exception
	 */
	public static void saveImageAsJPEG(BufferedImage image, File file, double compressionQuality) throws Exception {
		File parentFile = file.getParentFile();
		if (parentFile != null && !parentFile.exists()) {
			parentFile.mkdirs();
		}
		// Ѱ��jpgͼ���ļ������
		ImageWriter writer = null;
		Iterator<ImageWriter> iter = ImageIO.getImageWritersByFormatName("jpg");
		if (iter.hasNext()) {
			writer = iter.next();
		}

		// ��������ļ�
		ImageOutputStream ios = ImageIO.createImageOutputStream(file);
		writer.setOutput(ios);

		// ����ѹ������
		ImageWriteParam iwparam = new JPEGImageWriteParam(null);
		iwparam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
		iwparam.setCompressionQuality((float) compressionQuality);

		// ��ͼƬд��
		writer.write(null, new IIOImage(image, null, null), iwparam);

		// �ͷ����
		ios.flush();
		writer.dispose();
		ios.close();
	}

	/**
	 * ��ͼ�񱣴�Ϊjpeg��ʽ,��ָ��ͼ���ֽ�����С,��λΪ�ֽ�.<BR>
	 * ���Ƚ�ͼ������1���棬��������ļ���С>fileSize,��������Ϊ0.8���±��棬�������ƣ������ֱ�Ϊ1 0.8 0.6 0.4
	 * 0.2����СΪ0.2
	 * 
	 * @param image
	 *            ͼ��
	 * @param file
	 *            �����ļ�
	 * @param fileSize
	 *            �������ļ�������С
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
		// Ѱ��jpgͼ���ļ������
		ImageWriter writer = ImageIO.getImageWritersByFormatName("jpg").next();
		//
		ByteArrayOutputStream baos = new ByteArrayOutputStream(fileSize);
		ImageOutputStream ios = ImageIO.createImageOutputStream(baos);
		// ����ѹ������
		ImageWriteParam iwparam = new JPEGImageWriteParam(null);
		iwparam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
		float quality = 1.0f;
		for (int i = 0; i < 5; i++) {
			// ��������ļ�
			baos.reset();
			writer.setOutput(ios);
			//
			iwparam.setCompressionQuality(quality);
			// ��ͼƬд��
			writer.write(null, new IIOImage(image, null, null), iwparam);
			int size = baos.toByteArray().length;
			if (size > fileSize) {
				quality = quality - 0.2f;
			} else {
				break;
			}
		}
		// ������ļ�
		FileTools.write(baos.toByteArray(), file.getPath());

		// �ͷ����
		ios.flush();
		writer.dispose();
		baos.close();
		ios.close();
	}

	/**
	 * ���ذ�ָ����ȷָ��һ�����ı�����.<BR>
	 * 
	 * <pre>
	 * ʾ����ImageUtils.splitStringByWidth(image, null, 100,
	 * &quot;����\r\n�����1���������2���������&quot;);
	 * List�е�����Ϊ��
	 * line1������
	 * line2�������1�������
	 * line3����2���������
	 * </pre>
	 * 
	 * @param graphics
	 *            ͼ��
	 * @param font
	 *            ָ�����壬������Ϊnull
	 * @param width
	 *            һ�п��
	 * @param text
	 *            �ı�����
	 * @return ���һ�����ı����ݵ�list
	 */
	public static List<String> splitStringByWidth(Graphics graphics, Font font, int width, String text) {
		StringBuilder builder = new StringBuilder(500);
		StringBuilder textContent = new StringBuilder(500);
		FontMetrics fontMetrics = graphics.getFontMetrics();
		if (font != null) {
			fontMetrics = graphics.getFontMetrics(font);
		}
		// ����һ��List�����ڴ��һ�����ı�����
		List<String> lineInfos = new ArrayList<String>();
		// �Ի��з�Ϊ�ָ����ı�
		String[] texts = text.split("\r\n");
		for (int j = 0; j < texts.length; j++) {
			text = texts[j];
			// ���Ϊ�գ�ֱ�ӻ���һ��
			if (text.equals("")) {
				lineInfos.add("");
				continue;
			}
			for (int i = 0; i < text.length(); i++) {
				String charContent = text.substring(i, i + 1);
				builder.append(charContent);
				// ������г���δ�������εĿ�������������
				if (fontMetrics.stringWidth(builder.toString()) <= width) {
					textContent.append(charContent);
				}
				// ��������һ���ַ����Ⱦͳ����Ļ������������ݷ���lineInfos
				else {
					lineInfos.add(textContent.toString());
					builder.setLength(0);
					textContent.setLength(0);
					builder.append(charContent);
					textContent.append(charContent);
				}
				// �����һ���ַ��ˣ����������ݼ���lineInfos
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
