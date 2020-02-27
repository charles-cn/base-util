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
	 * ��ָ���ı�д��ͼ���ϵ�ָ��������,��ָ�����뷽ʽ,���壬��ɫ.<BR>
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
}
