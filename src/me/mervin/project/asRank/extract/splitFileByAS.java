package me.mervin.project.asRank.extract;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import me.mervin.util.D;
import me.mervin.util.FileTool;
import me.mervin.util.Pair;


 /**
 *   splitFileByAS.java
 *    
 *  @author Mervin.Wong  DateTime 2014-4-13 上午11:07:36    
 *  @version 0.4.0
 */
//public class CombFileByAS {

	/**
	 */
/*	public CombFileByAS() {
		// TODO 自动生成的构造函数存根
	}

	*//**
	 *  
	 *  @param args
	 *//*
	public static void main(String[] args) {
		// TODO 自动生成的方法存根
		
		 * splitByAs
		 
		int[] years = {2014};
		int[] months = {4};
		String srcDir = "/media/data/data/pathByM/";
		String dstDir = "/media/data/data/pathByM/splitByAS/";
		String dstFile = null;
		String srcFile = null;
		FileTool ft = new FileTool();
		File[] fileArr = null;
		File f1 = null;
		String prefix = null;
		CombFileByAS o = null;
		ft.clear(dstDir);
		for(int y:years){
			for(int m:months){
				if(m<10){
					prefix = y+"0"+m;
				}else{
					prefix = y+""+m;
				}


			}
				
			}//for m
		}//for y

}*/

public class splitFileByAS extends Thread{
//	private FileTool ft = new FileTool();
	
	private String srcDir = null;
	private LinkedList<Integer> path = null;
	private String line = null;
	
	private String srcFile = null;
	private String dstFile = null;
	
	public splitFileByAS(){
		
	}
	public splitFileByAS(String srcDir, LinkedList<Integer> path){
		this.srcDir = srcDir;
		this.path = path;
	}
	public splitFileByAS(String srcFile, String dstFile){
		this.srcFile = srcFile;
		this.dstFile = dstFile;
	}

	private void _splitFileByAS(){
		String dstFile = null;
		BufferedReader read = null;
		String line = null;
		String[] lineArr = null;
		
		RandomAccessFile write = null;
		
		try {
			read = new BufferedReader(new FileReader(this.dstFile));
			while((line = read.readLine()) != null){
				lineArr = line.split("\\s+");
				dstFile = this.srcFile+lineArr[0];
				write = new RandomAccessFile(new File(dstFile), "rw");
				 //对该文件加锁  
				FileChannel fcout=write.getChannel();  
				FileLock flout=null;  
				while(true){  
				    try {  
				        flout = fcout.tryLock();  
				        break;  
				    } catch (Exception e) {  
				         System.out.println("有其他线程正在操作该文件，当前线程休眠1000毫秒");   
				         try {
							Thread.sleep(1000);
						} catch (InterruptedException e1) {
							// TODO 自动生成的 catch 块
							e1.printStackTrace();
						}    
				    }  
				      
				}  
				long size = write.length();
				write.seek(size);
				write.writeBytes(line+"\r\n");
				flout.release();
				fcout.close();
				write.close();
			}//while
		} catch (FileNotFoundException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
			
	}
	
	
	/*
	 * 统计路径
	 */
	public void statPath(){
		Map<LinkedList<Integer>, Integer> allPathMap = new HashMap<LinkedList<Integer>, Integer>();
		
		BufferedReader read = null;
		String line = null;
		String[] lineArr = null;
		LinkedList<Integer> list = null;
		boolean flag = false;
		try{
			read = new BufferedReader(new FileReader(this.srcFile));
			while((line = read.readLine()) != null){
				lineArr = line.split("\\s+");
				list = new LinkedList<Integer>();
/*				for(String item:lineArr){
					int temp = Integer.parseInt(item);
					if(list.contains(temp)){
						flag = true;
						break;
					}
					list.add(temp);
				}
				if(flag){
					flag = false;
					continue;
				}*/
/*				for(String item:lineArr){
					int temp = Integer.parseInt(item);
					list.add(temp);
				}*/
				for(String item:lineArr){
					int temp = Integer.parseInt(item);
					if(list.size() > 1 && list.peekLast().equals(temp)){
						continue;
					}
					list.add(temp);
				}
				if(allPathMap.containsKey(list)){
					allPathMap.put(list, allPathMap.get(list)+1);
				}else{
					allPathMap.put(list, 1);
				}
			}//while
			
			
		}catch(FileNotFoundException e){
			e.getStackTrace();
		}catch (IOException e){
			e.getStackTrace();
		}
		
		StringBuffer sb = new StringBuffer();
		for(LinkedList<Integer> key:allPathMap.keySet()){
			sb.append(allPathMap.get(key)).append("\t");
			for(Integer item : key){
				sb.append(item).append("\t");
			}
			sb.append("\r\n");
		}
		BufferedWriter write;
		try {
			write = new BufferedWriter(new FileWriter(dstFile));
			write.write(sb.toString());
			write.flush();
			write.close();
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}

	}

	/*
	 * 合并所有的文件
	 */
	public void combFile(){
		String line = null;
		BufferedReader read = null;
		StringBuffer sb = new StringBuffer();
		try {
			read = new BufferedReader(new FileReader(this.srcFile));
			while((line = read.readLine()) != null){
				sb.append(line.substring(line.indexOf("\t")+1, line.length())).append("\r\n");
			}
			new FileTool().write(sb, dstFile, true);
		} catch (FileNotFoundException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}

	}
	
	/*
	 * 统计边频
	 */
	public void statEdgeFreq(){
		
		Map<Pair<Integer>, Integer> edgeMap = new HashMap<Pair<Integer>, Integer>();
		Pair<Integer> pair = null;
		
		BufferedReader read = null;
		String line = null;
		String[] lineArr = null;
		
		try {
			read = new BufferedReader(new FileReader(this.srcFile));
			while((line = read.readLine()) != null){
				lineArr = line.split("\\s+");
				int l = Integer.parseInt(lineArr[0]);
				for(int i = 1; i < lineArr.length; i++){
					int r = Integer.parseInt(lineArr[i]);
					pair = new Pair<Integer>(l, r, false);
					if(edgeMap.containsKey(pair)){
						edgeMap.put(pair, edgeMap.get(pair)+1);
					}else{
						edgeMap.put(pair, 1);
					}
					l =  r;
				}

			}//while
			
			StringBuffer sb = new StringBuffer();
			for(Pair<Integer> p:edgeMap.keySet()){
				sb.append(p.getL()).append("\t").append(p.getR()).append("\t").append(edgeMap.get(p)).append("\r\n");
			}
			new FileTool().write(sb, dstFile);
		} catch (FileNotFoundException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
 	}
}

