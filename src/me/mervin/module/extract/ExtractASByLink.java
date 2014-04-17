package me.mervin.module.extract;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import me.mervin.core.Global.ExtractAS;
import me.mervin.util.D;
import me.mervin.util.FileTool;
import me.mervin.util.MathTool;

/*****************************************************
 * 在文本文件中提取AS连接
 * @author mervin
 * ***************************************************
 * 提取选项：直接连接 非直接连接  gad
 * ***************************************************
 *
 */
public class ExtractASByLink {
	private File file = null;
	private String srcDir = null;
	private String dstDir = null;
	
	private ExtractAS content = ExtractAS.ALL;
	private BufferedReader reader = null;
	/**
	 * 构造函数
	 */
	public ExtractASByLink(ExtractAS content){
		this.content = content;
	}
	public ExtractASByLink(ExtractAS content, String path){
		this.content = content;
		file = new File(path);
		if(file.isFile()){
			this.srcDir = file.getParent();
		}else{
			this.srcDir = path;
		}
		this.dstDir = this.srcDir;
	}
	
	public ExtractASByLink(ExtractAS content, String path, String dstDir){
		this.content = content;
		file = new File(path);
		if(file.isFile()){
			this.srcDir = file.getParent();
		}else{
			this.srcDir = path;
		}		
		this.dstDir = dstDir;
	}
	
	/**
	 *  script
	 *  读取文件，提取网络，并写入文件
	 */
	public void script(){
		FileTool f = new FileTool();
		if(this.file.isFile()){
			f.write(this.extractNet(this.file), this.dstDir+file.getName().substring(file.getName().lastIndexOf('.')-8, file.getName().lastIndexOf('.'))+".txt", false);
		}else{
			D.p(this.file);
			File[] fileArr = this.file.listFiles();
			for (int i = 0; i < fileArr.length; i++) {
				f.write(this.extractNet(fileArr[i]), this.dstDir+fileArr[i].getName().substring(fileArr[i].getName().lastIndexOf('.')-8, fileArr[i].getName().lastIndexOf('.'))+".txt", false);
			}
		}
		D.s("成功了……");
	}
	/**
	 *  extract
	 *   在文件中提取网络
	 * @param String
	 * @return StringBuffer
	 */	
	public StringBuffer extractNet(String srcFile){
		File file = new File(srcFile);
		if(file.isFile()){
			return this.extractNet(file);
		}else{
			return new StringBuffer(srcFile+"不是一个文件！");
		}
	}
	/**
	 *  extract
	 *   在文件中提取网络
	 * @param file
	 * @return StringBuffer
	 */
	public StringBuffer extractNet(File file){
		StringBuffer sb = new StringBuffer();
		try {
			reader = new BufferedReader(new FileReader(file));
			String line = null;
			while((line = reader.readLine()) != null){
				if(!line.contains(".")){
					switch(this.content){
					case I:
						//非直接连接
						 if(line.charAt(0) == 'I'){
							 sb.append(this._extractEdge(line));
						 }
						 break;
					case D:
						//直接连接
						if(line.charAt(0) == 'D'){
							sb.append(this._extractEdge(line));
						}
						break;
					case ALL:
					default :
						if(line.charAt(0) == 'D' || line.charAt(0) == 'I'){
							sb.append(this._extractEdge(line));
						}
						break;
					}					
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sb;
	}

	
	/*
	 * ***************************************************************************************
	 * public mthod
	 * 按检测点提取文件里的网络
	 * 
	 * ***************************************************************************************
	 */	
	
	public void extractNetByMonitor(String srcFile){
		File file = new File(srcFile);
		if(file.isFile()){
			this.extractNetByMonitor(file, null);
		}else{
			D.p(srcFile+"文件不存在！");
		}
	}
	public Map<Integer, StringBuffer> extractNetByMonitor(File file){
		return this.extractNetByMonitor(file, null);
	}
	public Map<Integer, StringBuffer> extractNetByMonitor(File file, Vector<Integer> monitor){
		String line = null;
		String[] lineArr = null;
		boolean flag = false;
		if(monitor != null){
			//有自定义的monitor AS,那么只提取指定的monitor AS网络
			flag = true;
		}
		
		Map<Integer, Vector<Integer>> monitorMap = new HashMap<Integer,Vector<Integer>>();//需要提取的monitor AS
		Map<Integer, StringBuffer> monitorEdge = new HashMap<Integer, StringBuffer>();//对应monitor AS所能探测到的边
		
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			int ASN = 0, index = 0;
			while((line = reader.readLine()) != null){
				if(line.charAt(0) == 'M'){
					//提取检测点
					lineArr = line.split("\\s+");
					//D.p(lineArr.toString());
					if(!lineArr[2].equalsIgnoreCase("UNKNOWN")){
						if(lineArr[2].contains("_")){
							//多源
							for(String str:lineArr[2].split("_")){
								ASN = Integer.parseInt(str);
								
								if(flag){
									//提取指定
									if(monitor.contains(ASN)){
										index = Integer.parseInt(lineArr[3]);
										if(monitorMap.containsKey(index)){
											monitorMap.get(index).add(ASN);
											monitorEdge.put(ASN, new StringBuffer("#"+ASN+"\r\n"));
										}else{
											Vector<Integer> v = new Vector<Integer>();
											v.add(ASN);
											monitorMap.put(index, v);
											monitorEdge.put(ASN, new StringBuffer("#"+ASN+"\r\n"));
										}
										
									}
								}else{
									//提取所有
									index = Integer.parseInt(lineArr[3]);
									if(monitorMap.containsKey(index)){
										monitorMap.get(index).add(ASN);
										monitorEdge.put(ASN, new StringBuffer("#"+ASN+"\r\n"));
									}else{
										Vector<Integer> v = new Vector<Integer>();
										v.add(ASN);
										monitorMap.put(index, v);
										monitorEdge.put(ASN, new StringBuffer("#"+ASN+"\r\n"));
									}
								}
							}
						}else{
							ASN = Integer.parseInt(lineArr[2]);
							
							if(flag){
								//提取指定
								if(monitor.contains(ASN)){
									index = Integer.parseInt(lineArr[3]);
									if(monitorMap.containsKey(index)){
										monitorMap.get(index).add(ASN);
										monitorEdge.put(ASN, new StringBuffer("#"+ASN+"\r\n"));
									}else{
										Vector<Integer> v = new Vector<Integer>();
										v.add(ASN);
										monitorMap.put(index, v);
										monitorEdge.put(ASN, new StringBuffer("#"+ASN+"\r\n"));
									}
								}
							}else{
								//提取所有
								index = Integer.parseInt(lineArr[3]);
								if(monitorMap.containsKey(index)){
									monitorMap.get(index).add(ASN);
									monitorEdge.put(ASN, new StringBuffer("#"+ASN+"\r\n"));
								}else{
									Vector<Integer> v = new Vector<Integer>();
									v.add(ASN);
									monitorMap.put(index, v);
									monitorEdge.put(ASN, new StringBuffer("#"+ASN+"\r\n"));
								}
							}
						}
					}
				}else{
					if(!line.contains(".")){
						switch(this.content){
						case I:
							//非直接连接
							 if(line.charAt(0) == 'I'){
								 this._extractEdge(line, monitorMap, monitorEdge);
							 }
							 break;
						case D:
							//直接连接
							if(line.charAt(0) == 'D'){
								this._extractEdge(line, monitorMap, monitorEdge);
							}
							break;
						case ALL:
						default :
							if(line.charAt(0) == 'D' || line.charAt(0) == 'I'){
								this._extractEdge(line, monitorMap, monitorEdge);
							}
							break;
						}						
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
		
		return monitorEdge;
	}
	/*
	 * ***************************************************************************************
	 * private mthod
	 * 
	 * ***************************************************************************************
	 */
	
	/*
	 *  对每行提取边关系 
	 *  @param line 每行的内容
	 *  @return
	 *  注：
	 *  对于多源AS（multi-origin as）随机的选取一个AS
	 *  对于AS-set中的每个AS，均参与构造边
	 *  
	 */
	private StringBuffer _extractEdge(String line){
		String[] lineArr = line.split("\\s+");
		String preASes = lineArr[1];
		String postASes = lineArr[2];
		
		//PairList<String, String> pl = new PairList<String, String>();//边
		StringBuffer edges = new StringBuffer();
		ArrayList<String> preASList = new ArrayList<String>();//边的一端的节点
		ArrayList<String> postASList = new ArrayList<String>();//边的另一端的节点
		
		//preASes
		if(preASes.contains("_")){
			//multi-origin AS
			String[] asArr=preASes.split("_");
			//随机选择一个AS
			preASes = asArr[MathTool.random(0, asArr.length-1).intValue()];
		}
		if(preASes.contains(",")){
			String[] asArr = preASes.split(",");
			for(int i = 0; i < asArr.length; i++){
				preASList.add(asArr[i]);
			}
		}else{
			preASList.add(preASes);
		}
		
		//postASes
		if(postASes.contains("_")){
			//multi-origin AS
			String[] asArr=postASes.split("_");
			//随机选择一个AS
			postASes = asArr[MathTool.random(0, asArr.length-1).intValue()];
		}
		if(postASes.contains(",")){
			String[] asArr = postASes.split(",");
			for(int i = 0; i < asArr.length; i++){
				postASList.add(asArr[i]);
			}
		}else{
			postASList.add(postASes);
		}
		
		//构建边
		for(String preAS:preASList){
			for(String postAS:postASList){
				//pl.add(preAS.trim(), postAS.trim());
				edges.append(preAS).append("\t").append(postAS).append("\r\n");
			}
		}
		return edges;
	}
	
	
	private void _extractEdge(String line, Map<Integer, Vector<Integer>> monitorMap, Map<Integer, StringBuffer> monitorEdge){
		String[] lineArr = line.split("\\s+");
		String preASes = lineArr[1];
		String postASes = lineArr[2];
		
		//PairList<String, String> pl = new PairList<String, String>();//边
		StringBuffer edges = new StringBuffer();
		ArrayList<String> preASList = new ArrayList<String>();//边的一端的节点
		ArrayList<String> postASList = new ArrayList<String>();//边的另一端的节点
		
		//preASes
		if(preASes.contains("_")){
			//multi-origin AS
			String[] asArr=preASes.split("_");
			//随机选择一个AS
			preASes = asArr[MathTool.random(0, asArr.length-1).intValue()];
		}
		if(preASes.contains(",")){
			String[] asArr = preASes.split(",");
			for(int i = 0; i < asArr.length; i++){
				preASList.add(asArr[i]);
			}
		}else{
			preASList.add(preASes);
		}
		
		//postASes
		if(postASes.contains("_")){
			//multi-origin AS
			String[] asArr=postASes.split("_");
			//随机选择一个AS
			postASes = asArr[MathTool.random(0, asArr.length-1).intValue()];
		}
		if(postASes.contains(",")){
			String[] asArr = postASes.split(",");
			for(int i = 0; i < asArr.length; i++){
				postASList.add(asArr[i]);
			}
		}else{
			postASList.add(postASes);
		}
		
		//构建边
		for(String preAS:preASList){
			for(String postAS:postASList){
				//pl.add(preAS.trim(), postAS.trim());
				edges.append(preAS.trim()).append("\t").append(postAS.trim()).append("\r\n");
			}
		}
		
		Vector<Integer> v = null;
		//添加到对应的monitor AS
		int i = 0;
		int ASN = 0;
		if(line.charAt(0) == 'I'){
			i = 4;
		}else{
			i = 3;
		}
		for(; i< lineArr.length; i++){
			//D.p(monitorMap);
			v = monitorMap.get(Integer.parseInt(lineArr[i]));//
			//D.p(lineArr[i]+"###"+v.size());
			if(v != null){
				for(int j = 0; j < v.size(); j++){
					ASN = v.get(j);
					monitorEdge.get(ASN).append(edges);
				}
			}
		}
	}
	
}
