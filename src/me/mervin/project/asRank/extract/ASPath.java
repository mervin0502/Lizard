package me.mervin.project.asRank.extract;

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
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.mervin.util.D;
import me.mervin.util.FileTool;


 /**
 *   ASPath.java
 *    在rib和update中提取as_path
 *  @author Mervin.Wong  DateTime 2014年3月16日 下午4:16:43    
 *  @version 0.4.0
 */

public class ASPath extends Thread{
	
	private String srcDir = null;
	private String dstDir = null;
	private String name1 = null;
	private String name2 = null;
	private String ext = null;
	private String logFile = null;

	
	private FileTool ft = new FileTool();
	private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
	
	/**
	 */
	public ASPath() {
		// TODO 自动生成的构造函数存根
	}
	public ASPath(String srcDir, String dstDir, String name1, String name2, String logFile, String ext){
		this.srcDir = srcDir;
		this.dstDir = dstDir;
		this.name1 = name1;
		this.name2 = name2;
		this.ext = ext;
		this.logFile = logFile;
	}
	
	public void run(){
		D.p("File: "+ srcDir+name2);
		this._unzip();
		this._extract();
		D.p("###################");
	}
	
	private void _extract(){
		String srcFile = this.dstDir+this.name2;
		String dstFile = this.dstDir+this.name1+"/"+this.name2+"-path.txt";
		String line = null;
		LinkedList<Integer> res = null;
		Map<Integer, HashSet<LinkedList<Integer>>> allPathMap =  new HashMap<Integer, HashSet<LinkedList<Integer>>>();
		HashSet<LinkedList<Integer>> pathSet = null;
		StringBuffer sb = new StringBuffer();
		StringBuffer errPath = new StringBuffer();
		/*
		 * 提取
		 */
		try {
			BufferedReader reader = new BufferedReader(new FileReader(srcFile));
			
//			dstFile = this.dstDir+name;
			ft.clear(dstFile);
			while((line = reader.readLine()) != null){
				if (line.contains("ASPATH") || line.contains("AS_PATH")){
					//D.p(line);
					
					//提取AS号
					line = line.substring(line.indexOf(':')+1).trim();
//					D.p(line);
					res = this._filter(line);
					if(res != null){
						int asNum = res.getFirst();
						if(allPathMap.containsKey(asNum)){
							pathSet = allPathMap.get(asNum);
							if(!pathSet.contains(res)){
								pathSet.add(res);
							}
						}else{
							pathSet = new HashSet<LinkedList<Integer>>();
							pathSet.add(res);
							allPathMap.put(asNum, pathSet);
						}
						
					}else{
						errPath.append(line).append("\r\n");
					}
				}
			}
			
			for(HashSet<LinkedList<Integer>> allPathSet:allPathMap.values()){
				for(LinkedList<Integer> list:allPathSet){
					for(Integer temp:list){
						sb.append(temp).append("\t");
					}
					if(sb.length() >= 1024*1024*20){
						ft.write(sb, dstFile, true);
						sb.delete(0, sb.length());
					}
					sb.append("\r\n");
				}
			}
			ft.write(sb, dstFile, true);
			ft.write(errPath, this.dstDir+this.name1+"/"+this.name2+"-err.txt");
			this.ft.write(this.df.format(new Date())+"\t Extract:"+dstFile+"\r\n", logFile, true);
		} catch (FileNotFoundException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		D.p("extract file: "+srcFile);
		/*
		 * 删除解压的文件
		 */
		new File(srcFile).delete();
		D.p("del file: "+srcFile);		
	}
	
	/*
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
	 */
//	private String _filter(List<Integer> asList){
	private LinkedList<Integer> _filter(String asPath){
		StringBuffer res = new StringBuffer();
		LinkedList<Integer> asList = new LinkedList<Integer>();
		String[] asArr = null;
//		String[] asPathArr = null;
		
		/*
		 * AS_SET {}
		 * AS_CONFED_SEQUENCE ()
		 * AS_CONFED_SET []
		 */
/*		String regEx = "\\([0-9\\s,]+\\)|\\[[0-9\\s,]+\\]|\\{[0-9\\s,]+\\}";
		Pattern pat = Pattern.compile(regEx);
		Matcher mat = pat.matcher(asPath);
		asPath = mat.replaceAll("#");
//		D.p("@@@@"+asPath);
		if(asPath.contains("#")){
			asPathArr = asPath.split("#");
			for (int i = 0; i < asPathArr.length; i++) {
				res.append(this._filter(asPathArr[i])).append("\r\n");
			}*/
		if(asPath.contains("(") || asPath.contains("[") || asPath.contains("{")){
			return null;
		}else{
			asArr = asPath.trim().split("\\s+");
			if(asArr.length <= 1){
				return null;
			}
			//分割并将转换成list
			for(int j = 0; j < asArr.length; j++){
				/*
				 * remove 32-bit AS
				 */
				if(asArr[j].contains(".")){
					return null;
				}
				int asNum = 0 ;
				try{
					asNum = Integer.parseInt(asArr[j]);
				}catch(Exception e){
					ft.write(this.srcDir+this.name2+this.ext, this.logFile);
					D.p(asPath);
				}
				/*
				 * remove the AS paths with unassigned AS
				 * 64512~65535 private AS
				 */
				if(asNum >= 64512){
					return null;
				}
				
				/*
				 * remove the AS paths with loop
				 */
				if(asList.contains(asNum)){
					if(!asList.peekLast().equals(asNum)){
						return null;
					}
				}
				
				asList.add(asNum);
			}		
		}
		return asList;
	}
	
	private void _unzip(){
		String srcFile = this.srcDir+name2+"."+this.ext;
		String dstFile = this.dstDir+this.name2;
		//String cmd2 = "/home/mervin/tools/libbgpdump-dev/bgpdump -v -O"+dstFile+" "+srcFile;
		String cmd2 = "/home/mervin/libbgpdump/bgpdump -v -O"+dstFile+" "+srcFile;
		//D.p(cmd2);
		try {
			this.ft.write(this.df.format(new Date())+"\t Bgpdump:"+srcFile+"\r\n", logFile, true);
			Process pid2 = Runtime.getRuntime().exec(cmd2);
			BufferedReader reader = new BufferedReader(new InputStreamReader(pid2.getErrorStream()));
			StringBuffer sb = new StringBuffer();
			String line = null;
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
			while((line = reader.readLine()) != null){
				sb.append(df.format(new Date())).append("\t").append(line).append("\r\n");
			}
			this.ft.write(sb, this.logFile, true);
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}	
		D.p("unzip file: "+this.srcDir+this.name2+".bz2 => "+dstFile);
	}
	
	
	public static void main(String[] args) {
		// TODO 自动生成的方法存根
		
		String srcDir = "/media/data/data/src/";
		String dstDir = "/media/data/data/res/";
		int[] years = {2014};
		int[] months = {4};
		
		ExecutorService pools = Executors.newFixedThreadPool(2);
		File[] fileArr1 = null, fileArr2;
		FileTool ft = new FileTool();
		String name1 = null, name2 = null;
		String logFile = null;
		String date = null;
		//fileArr1 = ft.fileArr(srcDir);
		//for (int j = 0; j < fileArr1.length; j++) {
		
		for(int y:years){
			for(int m:months){
				if(m < 10){
					date = y+"/"+y+"0"+m;
				}else{
					date = y+"/"+y+""+m;
					
				}
				fileArr1 = ft.fileArr(srcDir+date+"/");
				for (int j = 0; j < fileArr1.length; j++) {
					fileArr2 = ft.fileArr(fileArr1[j].getAbsolutePath());
					name1 = fileArr1[j].getName();
					for (int i = 0; i < fileArr2.length; i++) {
						name2 = fileArr2[i].getName();
						if(name2.contains("bz2")){
							//route-view data package compress format
							name2 = name2.substring(0, name2.length()-4);
							logFile = dstDir+date+"/"+"log.txt";
							if(!ft.isExist(dstDir+date+"/"+name1+"/"+name2+"-path.txt")){
								Thread t = new ASPath(srcDir+date+"/"+name1+"/", dstDir+date+"/", name1, name2, logFile, "bz2");
								pools.execute(t);
							}else{
								D.p("File Exist:"+dstDir+date+"/"+name1+"/"+name2+"-path.txt");
							}
						}else if(name2.contains("gz")){
							// RIPE data package compress format
							name2 = name2.substring(0, name2.length()-3);
							logFile = dstDir+date+"/"+"log.txt";
							if(!ft.isExist(dstDir+date+"/"+name1+"/"+name2+"-path.txt")){
								Thread t = new ASPath(srcDir+date+"/"+name1+"/", dstDir+date+"/", name1, name2, logFile, "gz");
								pools.execute(t);
							}else{
								D.p("File Exist:"+dstDir+date+"/"+name1+"/"+name2+"-path.txt");
							}
						}
					}//for fileArr2
				}//for fileArr1
				
			}
		}			

		pools.shutdown();
		
		
/*		String srcDir = "/media/文档/data/RV/test/";
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
		*/
	}
}