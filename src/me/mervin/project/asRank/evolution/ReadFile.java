package me.mervin.project.asRank.evolution;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import me.mervin.util.D;
import me.mervin.util.FileTool;
import me.mervin.util.PairList;


 /**
 *   ReadFile.java
 *    
 *  @author Mervin.Wong  DateTime 2014年3月24日 下午2:20:55    
 *  @version 0.4.0
 */
public class ReadFile {
	
	public PairList<Number, Number> readFile(String srcFile){
		PairList<Number, Number> edgeList = new PairList<Number, Number>();
		
		String line = null;
		String[] lineArr = null;
		try {
			BufferedReader reader = new BufferedReader(new FileReader(srcFile));
			while((line = reader.readLine())!= null){
				if(line.charAt(0) != '#'){
					lineArr = line.split("\\|");
					int l = Integer.parseInt(lineArr[0]);
					int r = Integer.parseInt(lineArr[1]);
					if(lineArr[2].equals("-1")){
						edgeList.add(l, r);
					}else{
						edgeList.add(l, r);
						edgeList.add(r, l);
					}
				}
			}
		} catch (FileNotFoundException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		return edgeList;
	}
	
	
	public static void main(String[] args){
		String srcDir = "/media/data/data/pathByM/2013/";
		String dstDir = "/media/data/data/pathByM/path/";
		FileTool ft = new FileTool();
		File[] fileArr = ft.fileArr(srcDir);
		String name = null;
		File f = null;
		String srcFile = null;
		String dstFile = null;
		for (int i = 0; i < fileArr.length; i++) {
			 f = fileArr[i];
			 name = f.getName();
			 D.p(name);
			 
			 if(f.isDirectory()){
				srcFile = f.getAbsolutePath()+"/201312.txt";
				dstFile = dstDir+name+"-201312.txt";
				new File(srcFile).renameTo(new File(dstFile));
			 }
/*			 
			 if(name.contains("-")){
				dstFile = f.getParent()+"/"+name.substring(0, name.indexOf("-"))+"/"+name.substring(name.indexOf("-")+1, name.length());
				File dstF = new File(dstFile);
				dstF.getParentFile().mkdir();
			 	D.p(dstFile);
			 	//D.e();
			 	//new File(f.getAbsolutePath()).renameTo(new File(dstFile));
			 	f.renameTo(new File(dstFile));
			 }*/
		}
		
		
		
		
		
		
	}
}
