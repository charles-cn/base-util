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
 * ImageUtils用例. <br>
 * 详细展示了ImageUtils中各方法的具体使用.
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
		// 创建字体
		if (1 == 2) {
			ImageUtils.createFont(new File("d://STKAITI.TTF "), 2, 20);
			Font font = ImageUtils.createFont("d://STKAITI.TTF ", 2, 20);
			ImageUtils.createFont(font, 2, 20);
		}
		// 打开图片、创建图片和保存图片
		if (1 == 2) {
			BufferedImage newImage1 = ImageUtils.createImage(400, 200);
			// 保存图片，需指定格式
			ImageUtils.saveImage(newImage1, ImageFormat.BMP, new File("d://newImage1.bmp"));

			File file = new File("d://走走.jpg");
			BufferedImage image = ImageUtils.openImage(file);
			BufferedImage newImage2 = ImageUtils.createImage(image);
			// 保存为图片jpeg格式
			ImageUtils.saveImage(newImage2, ImageFormat.JPEG, new File("d://newImage2.jpg"));
			ImageUtils.saveImageAsJPEG(newImage2, new File("d://newImage3.jpg"), 1);
			// 保存图片为gif格式
			List<BufferedImage> imageList = new ArrayList<BufferedImage>();
			imageList.add(ImageUtils.openImage(new File("d://小猫.jpg")));
			imageList.add(ImageUtils.openImage(new File("d://小狗.jpg")));
			imageList.add(ImageUtils.openImage(new File("d://小花.jpg")));
			imageList.add(ImageUtils.openImage(new File("d://多多.jpg")));
			imageList.add(ImageUtils.openImage(new File("d://走走.jpg")));
			ImageUtils.saveImageAsGIF(imageList, new File("d://all.gif"), 800);
		}
		// 剪裁图片
		if (1 == 2) {
			File file = new File("d://小花.jpg");
			BufferedImage image = ImageUtils.openImage(file);
			BufferedImage newImage = ImageUtils.crop(image, 0, 0, 200, 200);
			ImageUtils.saveImage(newImage, ImageFormat.JPEG, new File("d://crop.jpg"));
		}
		// 在给定图片中写入文字
		if (1 == 2) {
			File file = new File("d://crop.jpg");
			BufferedImage image1 = ImageUtils.openImage(file);
			ImageUtils.drawText(image1, 50, 50, "HELLO");
			ImageUtils.saveImage(image1, ImageFormat.JPEG, new File("d://drawText1.jpg"));

			BufferedImage image2 = ImageUtils.openImage(file);
			Font font = ImageUtils.createFont(new File("d://STKAITI.TTF "), 3, 20);
			ImageUtils.drawText(image2, font, Color.red, 50, 100, "HELLO");
			ImageUtils.saveImage(image2, ImageFormat.JPEG, new File("d://drawText2.jpg"));

			String aa = "今天\r\n你好吗1今天你好吗2今天你好吗3今天你好吗4今天你好吗5今天你好吗6今天你好吗7今天你好吗8今天你好吗9今天你好吗10今天你好吗11今天你好吗12今天你好吗13今天你好吗14今天你好吗15今天你好吗16今天你好吗17今天你好吗18今天你好吗19今天你好吗20今天你好吗21今天你好吗22今天你好吗23今天你好吗24今天你好吗25今天你好吗26今天你好吗27今天你好吗28今天你好吗29";
			String bb = "一二三四五六七五六七八";
			String cc = "今\r\n\r\n天\r\n你\r\n好\r\n吗\r\n你\r\n好\r\n吗\r\n好\r\n吗\r\n";
			BufferedImage image3 = ImageUtils.openImage(file);
			ImageUtils.drawText(image3, font, Color.red, 0, 0, 200, 200, HorizontalAlignment.RIGHT, VerticalAlignment.CENTER, bb);
			ImageUtils.saveImage(image3, ImageFormat.JPEG, new File("d://drawText3.jpg"));
		}
		// 重新设定图片大小
		if (1 == 2) {
			File file = new File("d://原版小猫.jpg");
			BufferedImage image = ImageUtils.openImage(file);
			// BufferedImage newImage = ImageUtils.resize(image, 0.5);
			BufferedImage newImage = ImageUtils.resize(image, 400, 100, FitType.KEEP_ASPECT_RATIO_AND_KEEP_FULL_IMGAGE);
			ImageUtils.saveImage(newImage, ImageFormat.JPEG, new File("d://resize.jpg"));
		}
		// 设置将指定文本按宽度切割
		if (1 == 2) {
			File file = new File("d://小狗.jpg");
			BufferedImage image = ImageUtils.openImage(file);
			List<String> list = ImageUtils.splitStringByWidth(image.getGraphics(), null, 100, "今天\r\n你好吗1今天你好吗2今天你好吗");
			for (String info : list) {
				System.out.println(info);
			}
		}
		// 获取指定文本内容所占的宽度
		if (1 == 2) {
			String text = "hello早安";
			Font font = ImageUtils.createFont(new File("d://STKAITI.TTF "), 3, 20);
			BufferedImage image = ImageUtils.openImage(new File("d://原版小猫.jpg"));
			System.out.println(ImageUtils.getStringWidth(image.createGraphics(), text));
		}
		// 实现将一个gif图片中每一帧图作为一个image，返回一个imageList，并一一保存
		if (1 == 2) {
			File file = new File("d://001.gif");
			List<BufferedImage> imageList = ImageUtils.getGifImageList(file);
			for (int i = 0; i < imageList.size(); i++) {
				ImageUtils.saveImage(imageList.get(i), ImageFormat.JPEG, new File("d://1111" + i + ".jpg"));
			}
		}
		// 实现将图像保存为gif格式，并设置每一帧不同的延迟时间
		if (1 == 2) {
			List<BufferedImage> imageList = new ArrayList<BufferedImage>();
			imageList.add(ImageUtils.openImage(new File("d://小猫.jpg")));
			imageList.add(ImageUtils.openImage(new File("d://小狗.jpg")));
			imageList.add(ImageUtils.openImage(new File("d://小花.jpg")));
			imageList.add(ImageUtils.openImage(new File("d://多多.jpg")));
			imageList.add(ImageUtils.openImage(new File("d://走走.jpg")));
			List<Integer> mm = new ArrayList<Integer>();
			mm.add(500);
			mm.add(1500);
			mm.add(2500);
			mm.add(3500);
			mm.add(4500);
			ImageUtils.saveImageAsGIF(imageList, new File("d://ttt.gif"), mm);
		}
		// 实现获取gif图像每一帧的延迟时间
		if (1 == 2) {
			List<Integer> aa = ImageUtils.getGifImageDelayList(new File("d://ttt.gif"));
			for (int i = 0; i < aa.size(); i++) {
				System.out.println(aa.get(i));
			}
		}
	}
}
