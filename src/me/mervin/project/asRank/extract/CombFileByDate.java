package me.mervin.project.asRank.extract;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import me.mervin.model.NWNetwork;
import me.mervin.util.D;
import me.mervin.util.FileTool;


 /**
 *   CombFileByDate.java
 *    
 *  @author Mervin.Wong  DateTime 2014年4月7日 下午6:38:18    
 *  @version 0.4.0
 */

public class CombFileByDate extends Thread{

	private String srcDir = null;
	private String dstDir = null;
	private String prefix = null;
	private String logFile = null;
	private FileTool ft = new FileTool();
	public CombFileByDate(){
		
	}
	public CombFileByDate(String srcDir, String dstDir,  String prefix, String logFile){
		this.srcDir = srcDir;
		this.dstDir = dstDir;
		this.prefix = prefix;
		this.logFile = logFile;
	}
	/**
	 *  
	 *  @param args
	 */
/*	public static void main(String[] args) {
		// TODO 自动生成的方法存根
		String srcDir = "/media/data/data/res/";
		String dstDir = "/media/data/data/pathByM/";
		int[] years = {2014};
		int[] months = {4};
		
		String prefix = null;
		String logFile = null;
		ExecutorService poolService = Executors.newFixedThreadPool(2);
		Thread t = null;
		File[] fileArr = null;
		FileTool ft = new FileTool();
		String name = null;
		for(int y:years){
			for(int m:months){
				if(m < 10){
					prefix = y+"0"+m;
				}else{
					prefix = y+""+m;
				}
				fileArr = ft.fileArr(srcDir+y+"/"+prefix+"/");
				for (int i = 0; i < fileArr.length; i++) {
					if(fileArr[i].isDirectory()){
						name = fileArr[i].getName();
						logFile = dstDir+y+"/"+prefix+"/log.txt";
						t = new CombFileByDate(srcDir+y+"/"+prefix+"/"+name+"/", dstDir+y+"/"+prefix+"/"+name+"/", prefix, logFile);
						poolService.execute(t);
					}
				}//for fileArr
			}//for m

		}

		poolService.shutdown();
	}*/
	
	public void run(){
		Collection<String> collection = null;
		File[] fileArr = null;
		//File[] fileArr0 = new File[];
		ArrayList<File> fileList = new ArrayList<File>();
		String dstFile= null;
		String prefix1 = null;
		for(int i = 1; i <= 5; i++){
			prefix1 = this.prefix+"0"+i;
			D.p(this.srcDir+"#####"+prefix1);
			collection = new HashSet<String>();
			collection.add(prefix1);
			collection.add("path");
			fileArr = this.ft.fileArr(this.srcDir, collection);
			if(fileArr.length > 0){
				dstFile = this.dstDir+"/"+prefix1+".txt";
				File dst = new File(dstFile);
				if(dst.exists()){
					D.p(dstFile+"  Exist!");
				}else{
					this._combByDay(dstFile, fileArr);
				}
				fileList.add(dst);
			}else{
				D.p("NOT FIND");
			}
		}
		File[] fileArr0 = fileList.toArray(new File[fileList.size()]);
		if(fileArr0.length > 0){
			dstFile = this.dstDir+this.prefix+".txt";
			File dst = new File(dstFile);
			if(dst.exists()){
				D.p(dstFile+" Exist");
			}else{
				this._combByMonth(dstFile, fileArr0);
			}
		}
	}
	
	/*
	 * 合并每天的文件，BGP路由表和BGP更新数据
	 * 注:取交集
	 */
	private void _combByDay(String dstFile, File[] fileArr1){
		Set<LinkedList<Integer>> pathSet = new HashSet<LinkedList<Integer>>();
		LinkedList<Integer> path = null;
		String line = null;
		String[] lineArr = null;
		BufferedReader reader = null;
		for (int i = 0; i < fileArr1.length; i++) {
			try {
				reader = new BufferedReader(new FileReader(fileArr1[i]));
				D.p(fileArr1[i].getAbsolutePath());
				while((line = reader.readLine())!= null){
					lineArr = line.split("\\s+");
					path = new LinkedList<Integer>();
					for (int j = 0; j < lineArr.length; j++) {
						path.add(Integer.parseInt(lineArr[j]));
					}
					if(!pathSet.contains(path)){
						pathSet.add(path);
					}
				}//while
			} catch (FileNotFoundException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			} catch (IOException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
			
			this.ft.write(D.t()+":\t comByDay "+fileArr1[i].getAbsolutePath()+"\r\n", logFile, true);
			D.p(D.t()+":\t comByDay "+fileArr1[i].getAbsolutePath());
		}//for
		
		/*
		 * writer file
		 */
		StringBuffer sb = new StringBuffer();
		this.ft.clear(dstFile);
		for(LinkedList<Integer> list:pathSet){
			if(list.size() > 1){
				for(Integer temp:list){
					sb.append(temp).append("\t");
				}
				sb.append("\r\n");
			}
			
			if(sb.length() > 1024*1024*50){
				this.ft.write(sb, dstFile, true);
				sb.delete(0, sb.length());
			}
		}
		this.ft.write(sb, dstFile, true);
		this.ft.write(D.t()+":\t comOverOnDay "+dstFile+"\r\n", logFile, true);
		D.p(D.t()+":\t comOverOnDay"+dstFile);
	}
	
	private void _combByMonth(String dstFile, File[] fileArr2){
		Map<LinkedList<Integer>, Integer> allPath =  new HashMap<LinkedList<Integer>,Integer>();
		LinkedList<Integer> l = null;
		BufferedReader reader = null;
		String line = null;
		String[] lineArr = null;
		for (int i = 0; i < fileArr2.length; i++) {
			D.p(fileArr2[i].getAbsolutePath());
			try {
				reader = new BufferedReader(new FileReader(fileArr2[i]));
				while((line = reader.readLine())!= null){
					lineArr = line.split("\\s+");
					l = new LinkedList<Integer>();
					for(String str:lineArr){
						l.add(Integer.parseInt(str));
					}
					if(allPath.containsKey(l)){
						allPath.put(l, allPath.get(l)+1);
					}else{
						allPath.put(l, 1);
					}
				}
			} catch (FileNotFoundException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			} catch (IOException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
			this.ft.write(D.t()+":\t comByMonth "+fileArr2[i].getAbsolutePath()+"\r\n", logFile, true);
			D.p(D.t()+":\t comByMonth "+fileArr2[i].getAbsolutePath());
		}//for
		
		StringBuffer sb1 = new StringBuffer();
		StringBuffer sb2 = new StringBuffer();
		int v = 0;
		this.ft.clear(dstFile);
		for(LinkedList<Integer> ll:allPath.keySet()){
			v = allPath.get(ll);
			if(v == fileArr2.length){
				for(Integer temp:ll){
					sb1.append(temp).append("\t");
				}
				sb1.append("\r\n");
				
				if(sb1.length() > 1024*1024*50){
					this.ft.write(sb1, dstFile, true);
					sb1.delete(0, sb1.length());
				}
			}else{
				sb2.append(v+"\t");
				for(Integer temp:ll){
					sb2.append(temp).append("\t");
				}
				sb2.append("\r\n");
			}

		}//for
		this.ft.write(sb1, dstFile, true);
		this.ft.write(sb2, dstFile+".bak");
		if(logFile != null){
			this.ft.write(D.t()+":\t comOverOnMonth "+dstFile+"\r\n", logFile, true);
		}
		D.p(D.t()+":\t comOverOnMonth "+dstFile);
	}//fun
}