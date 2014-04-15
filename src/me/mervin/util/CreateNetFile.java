/*********************************************************************************
 *
 *
 *
 **********************************************************************************/
package me.mervin.util;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashSet;

/**
 * CreateNetFile.java
 * 
 *@author 王进法<Mervin.Wong>
 *@version 
 *@Date 2013-1-18上午9:55:36
 */
/*********************************************************************************
 *
 * 生成.net文件
 *
 **********************************************************************************/

public class CreateNetFile {
	private String fileName = null;
	private String dstPath = null;
	
	public CreateNetFile(String fileName, String dstPath){
		this.fileName = fileName;
		this.dstPath = dstPath;
	}
	
	public void script(){
		try {
			RandomAccessFile reader = new RandomAccessFile(this.fileName, "r");
			BufferedWriter writer = new BufferedWriter(new FileWriter(this.dstPath));
			HashSet<Number> set = new HashSet<Number>();
			String line = null;
			StringBuffer str = new StringBuffer();
			String[] lineArr = null;
			int num = 1;
			while((line = reader.readLine()) != null){
				D.p(line);
				lineArr = line.split("\t|(\\s{1,})");
				if(set.add(Long.parseLong(lineArr[0]))){
					str.append(num).append("\t").append(lineArr[0]).append("\r\n");
					num++;
				}
				if(set.add(Long.parseLong(lineArr[1]))){
					str.append(num).append("\t").append(lineArr[1]).append("\r\n");
					num++;
				}
			}
			str.insert(0, "*Vertices\t"+num+"\r\n");
			writer.append(str);
			str.delete(0, num);
			
			Thread.sleep(5000);
			
			str.append("*Edges\r\n");
			reader.seek(0);
			D.m();
			while((line = reader.readLine()) != null){
				D.p(line);
				str.append(line+"\r\n");
				if(str.length() > 1024*1024){
					writer.append(str);
					str.delete(0, str.length());
				}
			}
			writer.append(str);
			writer.flush();
			reader.close();
			writer.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
