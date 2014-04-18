package me.mervin.project.asRank.extract;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashSet;
import java.util.Set;

import me.mervin.util.D;
import me.mervin.util.FileTool;


 /**
 *   Widgets.java
 *    
 *  @author Mervin.Wong  DateTime 2014-4-18 上午9:12:33  
 *  @email:mervin0502@163.com  
 *  @version 0.5.0
 */
public class Widgets {

	private FileTool ft = new FileTool();
	/**
	 */
	public Widgets() {
		// TODO 自动生成的构造函数存根
	}
	
	public static void main(String[] arg){
		Widgets w = new Widgets();
		w.splitByAS();
	}
	
	
	public void delFirsColumn(){
		String srcDir = "/media/data/data/path/2014/";
		String dstDir = "/media/data/data/path/2014/0";
		String file = null;
		//BufferedReader read  = null;
		RandomAccessFile f = null;
		File[] fileArr1 = ft.fileArr(srcDir);
		for (int i = 0; i < fileArr1.length; i++) {
			File file2 = fileArr1[i];
			File[] fileArr2 = file2.listFiles();
			for (int j = 0; j < fileArr2.length; j++) {
				File file3 = fileArr2[j];
				D.p("read:"+file3.getAbsolutePath());
				try {
					StringBuffer sb = new StringBuffer();
					//read = new BufferedReader(new FileReader(file3));
					f = new RandomAccessFile(file3, "rw");
					String line = null;
					while((line = f.readLine()) != null){
						sb.append(line.substring(line.indexOf("\t")+1, line.length())).append("\r\n");
					}
					f.close();
					File f2 = new File(dstDir+file2.getName()+"/");
					f2.mkdirs();
					D.p("write:"+f2.getAbsolutePath()+"/"+file3.getName());
					f = new RandomAccessFile(f2.getAbsolutePath()+"/"+file3.getName(), "rw"); 
					f.writeBytes(sb.toString());
					f.close();
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
			
		}
		
	}
	
	
	public  void splitByAS(){
		String srcDir = "/media/data/data/temp/path/";
		String dstDir = "/media/data/data/path/";
		
		File[] fileArr = ft.fileArr(srcDir);
		for (int i = 0; i < fileArr.length; i++) {
			File file = fileArr[i];
			BufferedReader reader = null;
			D.p("Read:"+file.getAbsolutePath());
			try {
				reader = new BufferedReader(new FileReader(file));
				String line = null;
				String name1 = null;
				RandomAccessFile f = null;
				Set<Integer> nodeSet = new HashSet<Integer>();
				while((line = reader.readLine())!= null){
					String name2 = line.substring(0, line.indexOf("\t"));
/*					if(!name2.equals(name1)){
						if(name1 != null){
							f.close();
						}
						name1 = name2;
						f = new RandomAccessFile(new File(dstDir+file.getName().substring(0, file.getName().indexOf("-"))+"/"+name2), "rw");
						f.seek(f.length());
					}
					f.writeBytes(line+"\r\n");*/
					nodeSet.add(Integer.parseInt(name2));
				}
				D.p(file.getAbsolutePath()+""+nodeSet.size());
//				f.close();
			} catch (FileNotFoundException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			} catch (IOException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
			
			
		}
	}

}
