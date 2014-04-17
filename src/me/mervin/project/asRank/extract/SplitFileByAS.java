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
import me.mervin.util.LRU;
import me.mervin.util.Pair;


 /**
 *   splitFileByAS.java
 *    
 *  @author Mervin.Wong  DateTime 2014-4-13 上午11:07:36    
 *  @version 0.5.0
 */

public class SplitFileByAS extends Thread{
//	private FileTool ft = new FileTool();
	
	private String srcDir = null;
	private LinkedList<Integer> path = null;
	private String line = null;
	
	private String srcFile = null;
	private String dstFile = null;
	
	private static LRU<Integer, RandomAccessFile> fileCacheLru = new LRU(50);
	public SplitFileByAS(){
		
	}
	public SplitFileByAS(String srcDir, LinkedList<Integer> path){
		this.srcDir = srcDir;
		this.path = path;
	}
	public SplitFileByAS(String srcFile, String dstFile){
		this.srcFile = srcFile;
		this.dstFile = dstFile;
	}

	public void run(){
		this._splitFileByAS();
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
				int key = Integer.parseInt(lineArr[0]);
				if(SplitFileByAS.fileCacheLru.containsKey(key)){
					write = SplitFileByAS.fileCacheLru.get(key);
				}else{
					write = new RandomAccessFile(new File(dstFile), "rw");
					SplitFileByAS.fileCacheLru.put(key, write);
				}
				
				 //对该文件加锁  
				FileChannel fcout=write.getChannel();  
				FileLock flout=null;  
				while(true){  
				    try {  
				        flout = fcout.lock();  
				        break;  
				    } catch (Exception e) {  
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

