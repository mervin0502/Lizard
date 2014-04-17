package me.mervin.project.asRank.extract;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import me.mervin.util.D;
import me.mervin.util.FileTool;


 /**
 *   Extract.java
 *    
 *  @author Mervin.Wong  DateTime 2014-4-15 上午11:10:57    
 *  @version 0.4.0
 */
public class Extract {
	/**
	 *  
	 *  @param args
	 */
	public static void main(String[] args) {
		// TODO 自动生成的方法存根
		String srcDir = null;
		String dstDir = null;
		String srcFile = null;
		String dstFile = null;
		int[] years = {2014};
		int[] months = {1, 2, 3, 4};

		ExecutorService pools = null;
		File[] fileArr1 = null, fileArr2 = null;
		FileTool ft = new FileTool();
		String name1 = null, name2 = null;
		String logFile = null;
		String date = null;
		Thread t = null;

		
		for(int y:years){
			for(int m:months){
				if(m < 10){
					date = y+"0"+m;
				}else{
					date = y+""+m;
					
				}
				/*
				 * 1, extract all the path
				 */
/*				srcDir = "/media/data/data/src/";
				dstDir = "/media/data/data/res/";
				//pools = Executors.newFixedThreadPool(2);
				
				
				fileArr1 = ft.fileArr(srcDir+y+"/"+date+"/");
				for (int j = 0; j < fileArr1.length; j++) {
					fileArr2 = ft.fileArr(fileArr1[j].getAbsolutePath());
					name1 = fileArr1[j].getName();
					for (int i = 0; i < fileArr2.length; i++) {
						name2 = fileArr2[i].getName();
						if(name2.contains("bz2")){
							//route-view data package compress format
							name2 = name2.substring(0, name2.length()-4);
							logFile = dstDir+y+"/"+date+"/"+"log.txt";
							if(!ft.isExist(dstDir+y+"/"+date+"/"+name1+"/"+name2+"-path.txt")){
								t = new ASPath(srcDir+y+"/"+date+"/"+name1+"/", dstDir+y+"/"+date+"/", name1, name2, logFile, "bz2");
								t.run();
								//pools.execute(t);
							}else{
								D.p("File Exist:"+dstDir+y+"/"+date+"/"+name1+"/"+name2+"-path.txt");
							}
						}else if(name2.contains("gz")){
							// RIPE data package compress format
							name2 = name2.substring(0, name2.length()-3);
							logFile = dstDir+y+"/"+date+"/"+"log.txt";
							if(!ft.isExist(dstDir+y+"/"+date+"/"+name1+"/"+name2+"-path.txt")){
								t = new ASPath(srcDir+y+"/"+date+"/"+name1+"/", dstDir+y+"/"+date+"/", name1, name2, logFile, "gz");
								t.run();
								//pools.execute(t);
							}else{
								D.p("File Exist:"+dstDir+y+"/"+date+"/"+name1+"/"+name2+"-path.txt");
							}
						}
					}//for fileArr2
				}//for fileArr1
				
				 * 2, combine the file by date
				 
				srcDir = "/media/data/data/res/";
				dstDir = "/media/data/data/temp/";
				fileArr1 = ft.fileArr(srcDir+y+"/"+date+"/");
				String name = null;
				for (int i = 0; i < fileArr1.length; i++) {
					if(fileArr1[i].isDirectory()){
						name = fileArr1[i].getName();
						logFile = dstDir+y+"/"+date+"/log.txt";
						t = new CombFileByDate(srcDir+y+"/"+date+"/"+name+"/", dstDir+y+"/"+date+"/"+name+"/", date, logFile);
						t.run();
						//pools.execute(t);
					}
				}//for fileArr
*/				
				/*
				 * 3, combine the file by as
				 */
				/*
				 * 3.1 split file by as
				 */
				srcDir = "/media/data/data/temp/";
				dstDir = "/media/data/data/temp/splitByAS/";
				//CombFileByAS o = null;
				File f = null;
				fileArr1 = ft.fileArr(srcDir+y+"/"+date+"/");
				ft.clear(dstDir);
				for (int i = 0; i < fileArr1.length; i++) {
					f = fileArr1[i];
					if(f.isDirectory()){
						D.p("split file by AS=>"+f.getAbsolutePath()+"/"+date+".txt");
						t = new splitFileByAS(dstDir, f.getAbsolutePath()+"/"+date+".txt");
						t.run();
						//pools.execute(t);
						//o.splitFileByAS();
					}//if
				}//for i
				
				/*
				 * 3.2 stat path
				 */
				
				srcDir = dstDir;
				dstDir = "/media/data/data/path/"+y+"/"+date+"/";
				ft.clear(dstDir);
				fileArr1 = new File(srcDir).listFiles();
				
				for (int i = 0; i < fileArr1.length; i++) {
					f = fileArr1[i];
					D.p("statistics path=>"+f.getAbsolutePath());
					srcFile = f.getAbsolutePath();
					dstFile = dstDir+f.getName();
					t = new StatPath(srcFile, dstFile);
					t.run();
					//pools.execute(t);
					//o.statPath();
				}
				
			}//months
		}//years			

	}
}
