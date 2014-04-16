/**
 * 
 */
package me.mervin.project.asRank.evolution;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;

import me.mervin.util.FileTool;

/**
 * 	TransitDegree.java
 * 
 * @author Mervin Time: 2014年4月16日 下午3:22:12
 * @email:mervin0502@163.com
 * @version 0.5.0
 */
public class TransitDegree {

	private String srcFile = null;
	private String dstFile = null;
	private FileTool ft = new FileTool();
	private Map<Number, Number> transitDegreeMap = new HashMap<Number, Number>();
	
	/**
	 * @param srcFile
	 * @param dstFile
	 */
	public TransitDegree(String srcFile, String dstFile) {
		// TODO Auto-generated constructor stub
		this.srcFile = srcFile;
		this.dstFile = dstFile;
	}


	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String srcFile = "G:\\data\\201308-path.txt";;
		String dstFile = "G:\\data\\201308-transiteDegree.txt";;
		FileTool ft = new FileTool();
		TransitDegree td = new TransitDegree(srcFile, dstFile);
		ft.write(td.script(), dstFile);
	}
	
	
	public Map<Number, Number> script(){
		Map<Integer, HashSet<Integer>> transitNodeMap = new HashMap<Integer, HashSet<Integer>>();
		BufferedReader reader = null;
		
		try {
			reader = new BufferedReader(new FileReader(this.srcFile));
			String line = null;
			String[] lineArr = null;
			LinkedList<Integer> list = null;
			HashSet<Integer> nodeSet = null;
			while((line = reader.readLine()) != null){
				lineArr = line.split("\\s+");
				list = new LinkedList<Integer>();
				for (int i = 0; i < lineArr.length; i++) {
					list.add(Integer.parseInt(lineArr[i]));
				}
				
				for (int j = 1; j < list.size()-1; j++) {
					int cur = list.get(j);
					int l = list.get(j-1);
					int r = list.get(j+1);
					if(transitNodeMap.containsKey(cur)){
						nodeSet = transitNodeMap.get(cur);
						if(!nodeSet.contains(l)){
							nodeSet.add(l);
						}
						if(!nodeSet.contains(r)){
							nodeSet.add(r);
						}
					}else {
						nodeSet = new HashSet<Integer>();
						nodeSet.add(l);
						nodeSet.add(r);
						transitNodeMap.put(cur, nodeSet);
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
		
		
		for(Integer item:transitNodeMap.keySet()){
			this.transitDegreeMap.put(item, transitNodeMap.get(item).size());
		}
		
		return transitDegreeMap;
	}

}
