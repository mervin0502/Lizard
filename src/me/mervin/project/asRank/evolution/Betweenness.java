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
	private Map<Integer, Integer> betweennessMap = new HashMap<Integer, Integer>();
	public static void main(String[] args) {
		// TODO Auto-generated method stub
			
	}
	
	
	
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
}
