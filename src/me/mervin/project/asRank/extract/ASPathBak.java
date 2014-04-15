package me.mervin.project.asRank.extract;
/*package asRank.extract;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import util.D;
import util.FileTool;


 *//**
 *   ASPath.java
 *    在rib和update中提取as_path
 *  @author Mervin.Wong  DateTime 2014年3月16日 下午4:16:43    
 *  @version 0.4.0
 *//*
public class ASPathBak {
	
	private String srcDir = null;
	private String dstDir = null;
	
	private FileTool ft = new FileTool();
	
	ASPathBak(String srcDir, String dstDir){
		this.srcDir = srcDir;
		this.dstDir = dstDir;
	}


	*//**
	 *  
	 *  @param args
	 *//*
	public static void main(String[] args) {
		// TODO 自动生成的方法存根
		
		String srcDir = "/media/文档/data/RV/";
		String dstDir = "/media/娱乐/data/res/";
		
		ExecutorService pools = Executors.newFixedThreadPool(2);
		File[] fileArr = null;
		FileTool ft = new FileTool();
		String name = null;
		String logFile = null;
		String prefix = null;
		for(int y = 2003; y <= 2013; y++){
			//year
			for(int m = 1; m <= 12; m++){
				//month
				if(m < 10){
					prefix = y+"0"+m;
				}else{
					prefix = y+m+"";
				}
				fileArr = ft.fileArr(srcDir, prefix);
				for (int i = 0; i < fileArr.length; i++) {
					name = fileArr[i].getName();
					name = name.substring(0, name.length()-4);
					logFile = dstDir+y+"/"+"log.txt";
					Thread t = new SingleFileHandling(srcDir+y+"/", dstDir+y+"/", name, logFile);
					pools.execute(t);
				}
			}
			
			fileArr = ft.fileArr(srcDir+y+"/", "bz2");
			for (int i = 0; i < fileArr.length; i++) {
				name = fileArr[i].getName();
				name = name.substring(0, name.length()-4);
				logFile = dstDir+y+"/"+"log.txt";
				Thread t = new SingleFileHandling(srcDir+y+"/", dstDir+y+"/", name, logFile);
				pools.execute(t);
			}
			break;
		}
		pools.shutdown();
		
		
		String srcDir = "/media/文档/data/RV/test/";
		String dstDir = "/media/娱乐/data/res/";
		
		ExecutorService pools = Executors.newFixedThreadPool(2);
		File[] fileArr = null;
		FileTool ft = new FileTool();
		String name = null;
		String logFile = null;
			fileArr = ft.fileArr(srcDir, "bz2");
			for (int i = 0; i < fileArr.length; i++) {
				name = fileArr[i].getName();
				name = name.substring(0, name.length()-4);
				logFile = dstDir+"/"+"log.txt";
				Thread t = new SingleFileHandling(srcDir, dstDir, name, logFile);
				pools.execute(t);
			}
		pools.shutdown();
		
	}

}

class SingleFileHandling extends Thread{
	
	private String srcDir = null;
	private String dstDir = null;
	private String name = null;
	private String logFile = null;

	
	private FileTool ft = new FileTool();
	
	*//**
	 *//*
	public SingleFileHandling() {
		// TODO 自动生成的构造函数存根
	}
	public SingleFileHandling(String srcDir, String dstDir, String name, String logFile){
		this.srcDir = srcDir;
		this.dstDir = dstDir;
		this.name = name;
		this.logFile = logFile;
	}
	
	public void run(){
		D.p("File: "+ dstDir);
		this._unzip();
		this._extract();
		D.p("###################");
	}
	
	private void _extract(){
		String srcFile = this.dstDir+this.name;
		String dstFile = srcFile+"-path.txt";
		String line = null;
		StringBuffer res = new StringBuffer();
		StringBuffer sb = new StringBuffer();
		StringBuffer errPath = new StringBuffer();
		
		 * 提取
		 
		try {
			BufferedReader reader = new BufferedReader(new FileReader(srcFile));
			
//			dstFile = this.dstDir+name;
			ft.clear(dstFile);
			while((line = reader.readLine()) != null){
				if (line.contains("AS_PATH") || line.contains("ASPATH")){
					//D.p(line);
					
					//提取AS号
					line = line.substring(line.indexOf(':')+1).trim();
					//D.p(line);
					res = this._filter(line);
					if(res != null){
						if(!res.toString().trim().isEmpty()){
							//D.p(res);
							sb.append(res).append("\r\n");
							if(sb.length() >= 1024*1024*20){
								ft.write(sb, dstFile, true);
								sb.delete(0, sb.length());
							}
						}
					}else{
						errPath.append(line).append("\r\n");
					}
				}
			}
			ft.write(sb, dstFile, true);
			ft.write(errPath, this.dstDir+name+"-err.txt");
		} catch (FileNotFoundException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		D.p("extract file: "+srcFile);
		
		 * 删除解压的文件
		 
		new File(srcFile).delete();
		D.p("del file: "+srcFile);		
	}
	
	
	 * AS_SET {}
	 * AS_CONFED_SEQUENCE ()
	 * AS_CONFED_SET []
	 * AS paths with loop
	 * AS paths with unassigned AS
	 * =Remove the ASes used to operate IXP severs
	 * =discarded all paths from 167.142.3.6 for May and June 2003, 
	 * and from 198.32.132.97 between March and November 2012; 
	 * the former reported paths with ASes removed from the middle of the path, 
	 * and the latter reported paths inferred from traceroute.
	 * e.g.:(200 400 203) [101 401 102] 300 403 103 {201 402 302} 500 501 503
	 * ## 300 403 103 # 500 501 503
	 * 
	 
//	private String _filter(List<Integer> asList){
	private StringBuffer _filter(String asPath){
		StringBuffer res = new StringBuffer();
		List<Integer> asList = new LinkedList<Integer>();
		String[] asArr = null;
//		String[] asPathArr = null;
		
		
		 * AS_SET {}
		 * AS_CONFED_SEQUENCE ()
		 * AS_CONFED_SET []
		 
		String regEx = "\\([0-9\\s,]+\\)|\\[[0-9\\s,]+\\]|\\{[0-9\\s,]+\\}";
		Pattern pat = Pattern.compile(regEx);
		Matcher mat = pat.matcher(asPath);
		asPath = mat.replaceAll("#");
//		D.p("@@@@"+asPath);
		if(asPath.contains("#")){
			asPathArr = asPath.split("#");
			for (int i = 0; i < asPathArr.length; i++) {
				res.append(this._filter(asPathArr[i])).append("\r\n");
			}
		if(asPath.contains("(") || asPath.contains("[") || asPath.contains("{")){
			return null;
		}else{
			asArr = asPath.trim().split("\\s+");
			//分割并将转换成list
			for(int j = 0; j < asArr.length; j++){
				//去掉32位的AS
				if(!asArr[j].contains(".") && !asArr[j].isEmpty()){
					asList.add(Integer.parseInt(asArr[j]));
				}else{
					return null;
				}
			}
			
			 * remove the AS paths with unassigned AS
			 * 64512~65535 private AS
			 
			for (int i = 0; i < asList.size(); i++) {
				if (asList.get(i) >= 64512){
					//D.p("AS is private number!");
					return null;
				}
			}
			
			
			 * remove the AS paths with loop
			 
			List<Integer> temp = new LinkedList<Integer>();
			for(int i = 0, j = 0; i < asList.size(); i++){
				if(temp.contains(asList.get(i))){
					if(!temp.get(j-1).equals(asList.get(i))){
						//has a loop
						return null;
					}
				}else{
					temp.add(asList.get(i));
					j++;
					res.append(asList.get(i)).append("\t");
				}
			}			
		}
		return res;
	}
	
	private void _unzip(){
		String srcFile = this.srcDir+name+".bz2";
		String cmd1 = "bzip2 -d -k "+srcFile;
		//String cmd1 = "bzcat "+srcFile+" > "+this.srcDir+name;
		try {
			Process pid1 = Runtime.getRuntime().exec(cmd1);
			BufferedReader reader = new BufferedReader(new InputStreamReader(pid1.getErrorStream()));
			StringBuffer sb = new StringBuffer();
			String line = null;
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
			while((line = reader.readLine()) != null){
				sb.append(df.format(new Date())).append("\t").append(line).append("\r\n");
			}
			this.ft.write(sb, this.logFile, true);
		} catch (IOException e1) {
			// TODO 自动生成的 catch 块
			e1.printStackTrace();
		}
		srcFile = this.srcDir+name;
		String dstFile = this.dstDir+name;
		String cmd2 = "/home/mervin/libbgpdump/bgpdump -v -O"+dstFile+" "+srcFile;
		//D.p(cmd2);
		try {
			Process pid2 = Runtime.getRuntime().exec(cmd2);
			BufferedReader reader = new BufferedReader(new InputStreamReader(pid2.getErrorStream()));
			StringBuffer sb = new StringBuffer();
			String line = null;
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
			while((line = reader.readLine()) != null){
				sb.append(df.format(new Date())).append("\t").append(line).append("\r\n");
			}
			this.ft.write(sb, this.logFile, true);
			D.p(this.logFile);
			new File(srcFile).delete();
//			D.p(pid.getErrorStream());
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}	
		D.p("unzip file: "+this.srcDir+this.name+".bz2");
		
	}
}
*/