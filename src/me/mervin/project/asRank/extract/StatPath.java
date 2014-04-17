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
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class StatPath extends Thread {
	private String srcFile = null;
	private String dstFile = null;
	public StatPath() {
		// TODO 自动生成的构造函数存根
	}
	public StatPath(String srcFile, String dstFile){
		this.srcFile = srcFile;
		this.dstFile = dstFile;
	}
	

	
	public void run(){
		this.statPath();
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
/*		BufferedWriter write;
		try {
			write = new BufferedWriter(new FileWriter(dstFile));
			write.write(sb.toString());
			write.flush();
			write.close();
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}*/
		
		RandomAccessFile write;
		try {
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
			write.writeBytes(sb.toString());
			flout.release();
			fcout.close();
			write.close();
		} catch (FileNotFoundException e2) {
			// TODO 自动生成的 catch 块
			e2.printStackTrace();
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}


	}
}
