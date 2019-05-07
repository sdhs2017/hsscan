package com.ruin.masscan.action;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class WriteToFileExample {
	
	public static void Write(String path ,String result) {
		try {

			Date date =new Date();
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			String da=format.format(date);
			path=path.replace(".", da+".");

			File file = new File(path);

			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}

			FileWriter fw = new FileWriter(file.getAbsoluteFile(),true);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(result);
			bw.close();

//			System.out.println("Done");

		} catch (IOException e) {
			System.out.println("输入的存储路径不存在");
//			e.printStackTrace();
		}
		
	}
	public static void main(String[] args) {
		WriteToFileExample.Write("/asdf/sf", "adsf");
//		try {
//
//			String content = "This is the content to write into file";
//
//			File file = new File("/opt/jzlg/filename.txt");
//
//			// if file doesnt exists, then create it
//			if (!file.exists()) {
//				file.createNewFile();
//			}
//
//			FileWriter fw = new FileWriter(file.getAbsoluteFile(),true);
//			BufferedWriter bw = new BufferedWriter(fw);
//			bw.write(content);
//			bw.close();
//
//			System.out.println("Done");
//
//		} catch (IOException e) {
////			System.out.println("输入的路径不存在");
////			e.printStackTrace();
//		}
	}
}