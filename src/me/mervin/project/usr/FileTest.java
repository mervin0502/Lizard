package me.mervin.project.usr;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileTest {

	public static void main(String[] args) {
		// TODO 自动生成的方法存根
		
		Date date1;
		try {
			date1 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse("2013/10/14 00:00:00");
			long timeStamp = date1.getTime();
			FileTest ft = new FileTest();
			File f = new File("目录");
			ft.getModifiedFile(f, timeStamp);
		} catch (ParseException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}
	
	public void getModifiedFile(File f, long timeStamp){
		if(f.isFile()){
			if(f.lastModified() > timeStamp){
				System.out.println(f.getName());
			}
		}else if(f.isDirectory()){
			for(File f1:f.listFiles()){
				this.getModifiedFile(f1, timeStamp);
			}
		}
	}

}
