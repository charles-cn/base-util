package com.haocom.util.image;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import com.haocom.util.image.ImageUtils.HorizontalAlignment;
import com.haocom.util.image.ImageUtils.VerticalAlignment;

public class ImageTextUtils {

	/**
	 * 将指定文本写入图像上的指定区域中,并指定对齐方式,字体，颜色.<BR>
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
}
