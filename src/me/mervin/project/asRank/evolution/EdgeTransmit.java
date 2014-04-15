package me.mervin.project.asRank.evolution;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import me.mervin.util.FileTool;
import me.mervin.util.Pair;


/**
 *   EdgeTransmit.java
 *    
 *  @author Mervin.Wong  DateTime 2014年3月23日 下午5:54:19    
 *  @version 0.4.0
 */
public class EdgeTransmit {
	
	private String srcDir = null;
	private String dstDir = null;
	
	private String srcFile = null;
	private String dstFile = null;
	private FileTool ft = new FileTool();
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		EdgeTransmit et = new EdgeTransmit();
		String srcFile = "Z:\\data\\pathByM\\201308-edgeFreq.txt";
		String dstFile = "Z:\\data\\pathByM\\201308-IXP-edgeFreq.txt";
		
//		int[] nodeArr = {174,209,701,1239,1299,2828,2914,3257,3320,3356,3549,6453,6762,7018};
		int[] nodeArr = {1200,4635,5507,6695,7606,8714,9355,9439,9560,9722,9989,11670,17819,18398,21371,24029,24115,24990,35054,40633,42476,43100,47886,48850,55818};
		et.edgeFreqByNode(srcFile, dstFile, nodeArr);
		
	}

	/*
	 * 统计节点集的边传输
	 */
	public void edgeFreqByNode(String srcFile, String dstFile, int[] nodeSet){
		BufferedReader reader = null;
		
		try {
			reader = new BufferedReader(new FileReader(srcFile));
			String line = null;
			String[] lineArr = null;
			Map<Pair<Integer>, Integer> edgeFreqMap = new HashMap<Pair<Integer>, Integer>();
			Pair<Integer> pair = null;
			while((line = reader.readLine())!= null){
				lineArr = line.split("\\s+");
				int l = Integer.parseInt(lineArr[0]);
				int r = Integer.parseInt(lineArr[1]);
				pair = new Pair<Integer>(l, r, false);
				edgeFreqMap.put(pair, Integer.parseInt(lineArr[2]));
			}
			
			/*
			 * 获取核心节点
			 */
			StringBuffer sb = new StringBuffer();
			for(int l: nodeSet){
				for(int r: nodeSet){
					if(l == r){
						continue;
					}
					pair = new Pair<Integer>(l, r, false);
					if(edgeFreqMap.containsKey(pair)){
						sb.append(l).append("\t").append(r).append("\t").append(edgeFreqMap.get(pair)).append("\r\n");
				
					}
				}
			}
			ft.write(sb, dstFile);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
