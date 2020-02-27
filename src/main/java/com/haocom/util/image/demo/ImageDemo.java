package com.haocom.util.image.demo;

import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.haocom.util.image.ImageUtils;
import com.haocom.util.image.ImageUtils.FitType;
import com.haocom.util.image.ImageUtils.HorizontalAlignment;
import com.haocom.util.image.ImageUtils.ImageFormat;
import com.haocom.util.image.ImageUtils.VerticalAlignment;

/**
 * ImageUtils����. <br>
 * ��ϸչʾ��ImageUtils�и������ľ���ʹ��.
 * <p>
 * Copyright: Copyright (c) Sep 25, 2008
 * <p>
 * Company: 
 * <p>
 * Author: ZhouYan
 * <p>
 * Version: 1.0
 */
public class ImageDemo {

	public static void main(String[] args) throws Exception {
		// ��������
		if (1 == 2) {
			ImageUtils.createFont(new File("d://STKAITI.TTF "), 2, 20);
			Font font = ImageUtils.createFont("d://STKAITI.TTF ", 2, 20);
			ImageUtils.createFont(font, 2, 20);
		}
		// ��ͼƬ������ͼƬ�ͱ���ͼƬ
		if (1 == 2) {
			BufferedImage newImage1 = ImageUtils.createImage(400, 200);
			// ����ͼƬ����ָ����ʽ
			ImageUtils.saveImage(newImage1, ImageFormat.BMP, new File("d://newImage1.bmp"));

			File file = new File("d://����.jpg");
			BufferedImage image = ImageUtils.openImage(file);
			BufferedImage newImage2 = ImageUtils.createImage(image);
			// ����ΪͼƬjpeg��ʽ
			ImageUtils.saveImage(newImage2, ImageFormat.JPEG, new File("d://newImage2.jpg"));
			ImageUtils.saveImageAsJPEG(newImage2, new File("d://newImage3.jpg"), 1);
			// ����ͼƬΪgif��ʽ
			List<BufferedImage> imageList = new ArrayList<BufferedImage>();
			imageList.add(ImageUtils.openImage(new File("d://Сè.jpg")));
			imageList.add(ImageUtils.openImage(new File("d://С��.jpg")));
			imageList.add(ImageUtils.openImage(new File("d://С��.jpg")));
			imageList.add(ImageUtils.openImage(new File("d://���.jpg")));
			imageList.add(ImageUtils.openImage(new File("d://����.jpg")));
			ImageUtils.saveImageAsGIF(imageList, new File("d://all.gif"), 800);
		}
		// ����ͼƬ
		if (1 == 2) {
			File file = new File("d://С��.jpg");
			BufferedImage image = ImageUtils.openImage(file);
			BufferedImage newImage = ImageUtils.crop(image, 0, 0, 200, 200);
			ImageUtils.saveImage(newImage, ImageFormat.JPEG, new File("d://crop.jpg"));
		}
		// �ڸ���ͼƬ��д������
		if (1 == 2) {
			File file = new File("d://crop.jpg");
			BufferedImage image1 = ImageUtils.openImage(file);
			ImageUtils.drawText(image1, 50, 50, "HELLO");
			ImageUtils.saveImage(image1, ImageFormat.JPEG, new File("d://drawText1.jpg"));

			BufferedImage image2 = ImageUtils.openImage(file);
			Font font = ImageUtils.createFont(new File("d://STKAITI.TTF "), 3, 20);
			ImageUtils.drawText(image2, font, Color.red, 50, 100, "HELLO");
			ImageUtils.saveImage(image2, ImageFormat.JPEG, new File("d://drawText2.jpg"));

			String aa = "����\r\n�����1���������2���������3���������4���������5���������6���������7���������8���������9���������10���������11���������12���������13���������14���������15���������16���������17���������18���������19���������20���������21���������22���������23���������24���������25���������26���������27���������28���������29";
			String bb = "һ�����������������߰�";
			String cc = "��\r\n\r\n��\r\n��\r\n��\r\n��\r\n��\r\n��\r\n��\r\n��\r\n��\r\n";
			BufferedImage image3 = ImageUtils.openImage(file);
			ImageUtils.drawText(image3, font, Color.red, 0, 0, 200, 200, HorizontalAlignment.RIGHT, VerticalAlignment.CENTER, bb);
			ImageUtils.saveImage(image3, ImageFormat.JPEG, new File("d://drawText3.jpg"));
		}
		// �����趨ͼƬ��С
		if (1 == 2) {
			File file = new File("d://ԭ��Сè.jpg");
			BufferedImage image = ImageUtils.openImage(file);
			// BufferedImage newImage = ImageUtils.resize(image, 0.5);
			BufferedImage newImage = ImageUtils.resize(image, 400, 100, FitType.KEEP_ASPECT_RATIO_AND_KEEP_FULL_IMGAGE);
			ImageUtils.saveImage(newImage, ImageFormat.JPEG, new File("d://resize.jpg"));
		}
		// ���ý�ָ���ı�������и�
		if (1 == 2) {
			File file = new File("d://С��.jpg");
			BufferedImage image = ImageUtils.openImage(file);
			List<String> list = ImageUtils.splitStringByWidth(image.getGraphics(), null, 100, "����\r\n�����1���������2���������");
			for (String info : list) {
				System.out.println(info);
			}
		}
		// ��ȡָ���ı�������ռ�Ŀ��
		if (1 == 2) {
			String text = "hello�簲";
			Font font = ImageUtils.createFont(new File("d://STKAITI.TTF "), 3, 20);
			BufferedImage image = ImageUtils.openImage(new File("d://ԭ��Сè.jpg"));
			System.out.println(ImageUtils.getStringWidth(image.createGraphics(), text));
		}
		// ʵ�ֽ�һ��gifͼƬ��ÿһ֡ͼ��Ϊһ��image������һ��imageList����һһ����
		if (1 == 2) {
			File file = new File("d://001.gif");
			List<BufferedImage> imageList = ImageUtils.getGifImageList(file);
			for (int i = 0; i < imageList.size(); i++) {
				ImageUtils.saveImage(imageList.get(i), ImageFormat.JPEG, new File("d://1111" + i + ".jpg"));
			}
		}
		// ʵ�ֽ�ͼ�񱣴�Ϊgif��ʽ��������ÿһ֡��ͬ���ӳ�ʱ��
		if (1 == 2) {
			List<BufferedImage> imageList = new ArrayList<BufferedImage>();
			imageList.add(ImageUtils.openImage(new File("d://Сè.jpg")));
			imageList.add(ImageUtils.openImage(new File("d://С��.jpg")));
			imageList.add(ImageUtils.openImage(new File("d://С��.jpg")));
			imageList.add(ImageUtils.openImage(new File("d://���.jpg")));
			imageList.add(ImageUtils.openImage(new File("d://����.jpg")));
			List<Integer> mm = new ArrayList<Integer>();
			mm.add(500);
			mm.add(1500);
			mm.add(2500);
			mm.add(3500);
			mm.add(4500);
			ImageUtils.saveImageAsGIF(imageList, new File("d://ttt.gif"), mm);
		}
		// ʵ�ֻ�ȡgifͼ��ÿһ֡���ӳ�ʱ��
		if (1 == 2) {
			List<Integer> aa = ImageUtils.getGifImageDelayList(new File("d://ttt.gif"));
			for (int i = 0; i < aa.size(); i++) {
				System.out.println(aa.get(i));
			}
		}
	}
}
