package com.haocom.config.demo;

import java.util.Date;

import com.haocom.config.FileConfig;
import com.haocom.config.PropertiesConfig;
import com.haocom.config.XMLConfig;

public class DemoConfig
{
	public static void main(String[] args)
	{
		try
		{
			FileConfig c1 = new XMLConfig("console", "./config/console.xml", true);
			FileConfig c2 = new XMLConfig("mmsclub", "./config/mmsclub.xml", true);
			FileConfig c3 = new PropertiesConfig("mmscom", "./config/mmscom.properties", true);
			while (true)
			{
				System.out.println(c1.toString());
				System.out.println(c2.toString());
				System.out.println(c3.toString());
				c3.setValues("time", new Date().toString());
				c3.setValues("my", "ÄãºÃ");
				c3.saveToFile();
				Thread.sleep(5000);
			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}
}
