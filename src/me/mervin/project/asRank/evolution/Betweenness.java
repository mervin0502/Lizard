package me.mervin.project.asRank.evolution;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import me.mervin.core.Global.NumberType;
import me.mervin.model.NWNetwork;
import me.mervin.util.D;
import me.mervin.util.FileTool;
import me.mervin.util.Pair;

/**
*   Betweenness.java
*    
*  @author Mervin.Wong  DateTime 2014年3月24日 上午10:03:20    
*  @version 0.4.0
*/
public class Betweenness {

	private String srcDir = null;
	private String dstDir = null;
	private String prefix = null;
	private FileTool ft = new FileTool();
	private Map<Number, Number> nodeBetweennessMap = new HashMap<Number, Number>();
	private Map<Pair<Number>, Number> edgeBetweennessMap = new HashMap<Pair<Number>, Number>();
	
	public Betweenness(String srcDir, String dstDir, String prefix){
		this.srcDir = srcDir;
		this.dstDir = dstDir;
		this.prefix = prefix;
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
/*//		String srcDir = "G:\\data\\201308-path.txt";
		String srcDir = "G:\\data\\test.txt";
		String dstDir = "G:\\data\\res.txt";
//		String dstDir = "G:\\data\\201308-betweenness.txt";
		Betweenness b = new Betweenness(srcDir, dstDir);
		b.script();*/
		
		String srcDir = "/media/data/data/path/";
		String dstDir = "/media/data/data/rank/";
		FileTool ft = new FileTool();
		File[] fileArr1 = ft.fileArr(srcDir);
		File[] fileArr2 = null;
		Betweenness b = null;
		for (int i = 0; i < fileArr1.length; i++) {
			fileArr2 = fileArr1[i].listFiles();
			for (int j = 0; j < fileArr2.length; j++) {
				D.p("current file:"+fileArr2[j].getAbsolutePath());
				b = new Betweenness(fileArr2[j].getAbsolutePath(), dstDir+fileArr1[i].getName()+"/", fileArr2[j].getName());
				b.script();
			}
			
		}
		
		
		
	}
	
	public void script(){
		Map<Pair<Integer>, HashSet<LinkedList<Integer>>> pathMap = null;
		/*
		 * 1,get the first column's node(AS) in the text
		 */

		File[] fileArr = ft.fileArr(this.srcDir);
		for (int i = 0; i < fileArr.length; i++) {
			File file = fileArr[i];
			//D.p("firstNode=>"+file.getName());
			/*
			 * 2,get the path with the same first ndoe
			 */
			pathMap = this._getPathByFirtNode(file);
			for(HashSet<LinkedList<Integer>> pathSet:pathMap.values()){
				
				/*
				 * 3, calculate the node betweenness according the 
				 */
				this._calculateNodeBetweenness(pathSet);
				/*
				 * 4, calculate the edge betweenness according the 
				 */
				this._calculateEdgeBetweenness(pathSet);
			}
		}
		String dstFile = null;
		/*
		 * write nodeBetweennessMap to the file 
		 */
		dstFile = this.dstDir+this.prefix+"-node.txt";
		ft.write(nodeBetweennessMap, dstFile);
		dstFile = this.dstDir+this.prefix+"-edge.txt";
		StringBuffer sb = new StringBuffer();
		for(Pair<Number> p:this.edgeBetweennessMap.keySet()){
			sb.append(p.getL()).append("\t").append(p.getR()).append("\t").append(this.edgeBetweennessMap.get(p)).append("\r\n");
		}
		ft.write(sb, dstFile);
	}
	
	/******************************************************************
	 * 
	 * private method
	 * @return Set<Number>
	 */
	
	/*
	 * 获取文本文件中第一列的节点ID
	 */
/*	private Set<Number> _getAllFirstNode(){
		//int[] col = {1};
		//return this.ft.read2Set(srcDir, NumberType.INTEGER, col);
	}
*/	
	/*
	 * 获取第一个节点相同的路径
	 */
	@SuppressWarnings("resource")
	private Map<Pair<Integer>, HashSet<LinkedList<Integer>>> _getPathByFirtNode(File file){
		BufferedReader reader = null;
		Map<Pair<Integer>, HashSet<LinkedList<Integer>>> pathMap = new HashMap<Pair<Integer>, HashSet<LinkedList<Integer>>>();
		Pair<Integer> pair = null;
		HashSet<LinkedList<Integer>> pathSet = null;
		LinkedList<Integer> path = null;
		
		try {
			reader = new BufferedReader(new FileReader(file));
			String line = null;
			String[] lineArr 
			= null;
			while((line = reader.readLine())!= null){
				lineArr = line.split("\\s+");
			
				int l = Integer.parseInt(lineArr[0]);
				int r = 0;
				path = new LinkedList<Integer>();
				for(String str:lineArr){
					r = Integer.parseInt(str);
					path.add(r);
				}//for
				pair = new Pair<Integer>(l, r, false);
				
				if(pathMap.containsKey(pair)){
					pathMap.get(pair).add(path);
				}else{
					pathSet = new HashSet<LinkedList<Integer>>();
					pathSet.add(path);
					pathMap.put(pair, pathSet);
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return pathMap;
	}
	
	/*
	 * 计算节点介数
	 */
	private void _calculateNodeBetweenness(HashSet<LinkedList<Integer>> pathSet) {
		Map<Integer, Integer> map = new HashMap<Integer, Integer>();
		
		for (LinkedList<Integer> ll : pathSet) {
			ll.pollFirst();
			ll.pollLast();
			if(!ll.isEmpty()){
				for(int l:ll){
					if(map.containsKey(l)){
						map.put(l, map.get(l)+1);
					}else{
						map.put(l, 1);
					}
				}
			}//if
		}//for
		
		
		for(int k:map.keySet()){
			double v = 0;
			if(this.nodeBetweennessMap.containsKey(k)){
				v = this.nodeBetweennessMap.get(k).doubleValue()+(double)map.get(k)/pathSet.size();
			}else{
				v = (double)map.get(k)/pathSet.size();
			}
			this.nodeBetweennessMap.put(k, v);
		}
		
	}
	
	/*
	 * edge betweenness
	 */
	private void _calculateEdgeBetweenness(HashSet<LinkedList<Integer>> pathSet){
		Map<Pair<Number>, Integer> map = new HashMap<Pair<Number>, Integer>();
		Pair<Number> pair = null;
		for (LinkedList<Integer> ll : pathSet) {
			if(ll.size() > 1){
				int l = ll.get(0);
				int r = 0;
				for (int i = 1; i < ll.size(); i++) {
					r = ll.get(i);
					pair = new Pair<Number>(l, r, false);
					if(map.containsKey(pair)){
						map.put(pair, map.get(pair)+1);
					}else{
						map.put(pair, 1);
					}
					
				}
			}
		}//for
		
		
		for(Pair<Number> p:map.keySet()){
			double v = 0;
			if(this.edgeBetweennessMap.containsKey(p)){
				v = this.edgeBetweennessMap.get(p).doubleValue()+(double)map.get(p)/pathSet.size();
			}else{
				v = (double)map.get(p)/pathSet.size();
			}
			this.edgeBetweennessMap.put(p, v);
		}
	}
}
