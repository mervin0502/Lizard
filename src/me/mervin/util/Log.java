package me.mervin.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;



 /**
 *   Log.java
 *    
 *  @author Mervin.Wong  DateTime 2014年3月23日 上午10:53:55    
 *  @version 0.4.0
 */
public class Log {

	/**
	 * 
	 *  
	 *  @param str
	 *  @param dstFile
	 */
	public static void w(String str, String dstFile){
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(dstFile));
			writer.write(str);
			writer.flush();
			writer.close();
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}
	/**
	 * 
	 *  
	 *  @param str
	 *  @param dstFile
	 *  @param append
	 */
	public static void w(String str, String dstFile, boolean append){
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(dstFile));
			writer.append(str);
			writer.flush();
			writer.close();
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		
	}
	/**
	 * 
	 *  
	 *  @param str
	 *  @param dstFile
	 *  @param append
	 */
	public static void a(String str, String dstFile){
		File f = new File(dstFile);
		if(!(f.getParentFile().isDirectory())){
			f.getParentFile().mkdirs();
		}
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(dstFile, true));
			writer.write(str);
			writer.flush();
			writer.close();
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		
	}
}
