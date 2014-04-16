package me.mervin.project.asRank.evolution;

import java.io.BufferedReader;
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

	private String srcFile = null;
	private String dstFile = null;
	private FileTool ft = new FileTool();
	private Map<Number, Number> betweennessMap = new HashMap<Number, Number>();
	
	public Betweenness(String srcFile, String dstFile){
		this.srcFile = srcFile;
		this.dstFile = dstFile;
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		String srcFile = "G:\\data\\201308-path.txt";
		String srcFile = "G:\\data\\test.txt";
		String dstFile = "G:\\data\\res.txt";
//		String dstFile = "G:\\data\\201308-betweenness.txt";
		Betweenness b = new Betweenness(srcFile, dstFile);
		b.script();
	}
	
	public void script(){
		Map<Pair<Integer>, HashSet<LinkedList<Integer>>> pathMap = null;
		/*
		 * 1,get the first column's node(AS) in the text
		 */
		Set<Number> firstNodeSet = this._getAllFirstNode();
		
		for(Number firstNode:firstNodeSet){
			D.p("firstNode=>"+firstNode);
			/*
			 * 2,get the path with the same first ndoe
			 */
			pathMap = this._getPathByFirtNode(firstNode.intValue());
			for(HashSet<LinkedList<Integer>> pathSet:pathMap.values()){
				
				/*
				 * 3, calculate the betweenness according the 
				 */
				this._calculateBetweenness(pathSet);
			}
		}
		/*
		 * write betweennessMap to the file 
		 */
		ft.write(betweennessMap, dstFile);
	}
	
	/******************************************************************
	 * 
	 * private method
	 * @return
	 */
	
	/*
	 * 获取文本文件中第一列的节点ID
	 */
	private Set<Number> _getAllFirstNode(){
		int[] col = {1};
		return this.ft.read2Set(srcFile, NumberType.INTEGER, col);
	}
	
	/*
	 * 获取第一个节点相同的路径
	 */
	private Map<Pair<Integer>, HashSet<LinkedList<Integer>>> _getPathByFirtNode(int firsNode){
		BufferedReader reader = null;
		Map<Pair<Integer>, HashSet<LinkedList<Integer>>> pathMap = new HashMap<Pair<Integer>, HashSet<LinkedList<Integer>>>();
		Pair<Integer> pair = null;
		HashSet<LinkedList<Integer>> pathSet = null;
		LinkedList<Integer> path = null;
		
		try {
			reader = new BufferedReader(new FileReader(this.srcFile));
			String line = null;
			String[] lineArr = null;
			while((line = reader.readLine())!= null){
				lineArr = line.split("\\s+");
				
				if(lineArr[0].equals(firsNode+"")){
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
				}//if
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
	private void _calculateBetweenness(HashSet<LinkedList<Integer>> pathSet) {
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
			if(this.betweennessMap.containsKey(k)){
				v = this.betweennessMap.get(k).doubleValue()+(double)map.get(k)/pathSet.size();
			}else{
				v = (double)map.get(k)/pathSet.size();
			}
			this.betweennessMap.put(k, v);
		}
		
	}
}
