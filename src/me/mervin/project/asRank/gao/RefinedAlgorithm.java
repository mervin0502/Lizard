package me.mervin.project.asRank.gao;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import me.mervin.util.D;
import me.mervin.util.FileTool;
import me.mervin.util.Pair;


 /**
 *   BasicAlgorithm.java
 *    input:the BGP route tables
 *    output:Annotated AS graph G
 *  @author Mervin.Wong  DateTime 2014年3月17日 下午2:28:22    
 *  @version 0.4.0
 */
public class RefinedAlgorithm {
	
	public int L = 1;

	// the BGP route tables file
	private String srcFile = null;
	// save files directory
	private String dstDir = null;
	// Annotated AS graph G
	private String dstFile = null;
	// AS pair type
	public static enum EdgeType {P2C, C2P, S2S, P2P};
	private EdgeType edgeType = null;
	
	private FileTool ft = new FileTool();
	public RefinedAlgorithm(String srcFile, String dstDir){
		this.srcFile = srcFile;
		this.dstDir = dstDir;
	}
	
	/*
	 * compute the AS degree
	 */
	public Map<Number, Number> asDegree(){
		Map<Integer, HashSet<Integer>> asDegreeMap = new HashMap<Integer, HashSet<Integer>>();
		HashSet<Integer> degreeSet = null;
		Map<Number, Number> asDegree = new HashMap<Number, Number>();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(this.srcFile));
			String line = null;
			String[] lineArr = null;
			int p1, p2;
			while((line = reader.readLine())!= null){
				lineArr = line.split("\\t");
				if(lineArr.length > 1){
					for(int i = 0; i < lineArr.length-1; i++){
						p1 = Integer.parseInt(lineArr[i]);
						p2 = Integer.parseInt(lineArr[i+1]);
						
						if(asDegreeMap.containsKey(p1)){
							asDegreeMap.get(p1).add(p2);
						}else{
							degreeSet = new HashSet<Integer>();
							degreeSet.add(p2);
							asDegreeMap.put(p1, degreeSet);
						}
						
						if(asDegreeMap.containsKey(p2)){
							asDegreeMap.get(p2).add(p1);
						}else{
							degreeSet = new HashSet<Integer>();
							degreeSet.add(p1);
							asDegreeMap.put(p2, degreeSet);
						}
						
					}					
				}
			}
			reader.close();
			
			/*
			 * compute the asDegreeMap's values size
			 */
			int temp;
			for(Iterator<Integer> it = asDegreeMap.keySet().iterator();it.hasNext();){
				temp = it.next();
				asDegree.put(temp, asDegreeMap.get(temp).size());
			}
			
			//write file
			this.dstFile = this.dstDir+"asDegree.txt";
			this.ft.write(asDegree, dstFile);
		} catch (FileNotFoundException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	
		return asDegree;
	}
	
	/*
	 * Parse AS path into initialize consecutive AS pair's transit relationship
	 */
	public Map<Pair<Integer>, Integer> transitRelationship(Map<Number,Number> asDegree){
		Map<Pair<Integer>, Integer> transit = new HashMap<Pair<Integer>, Integer>();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(this.srcFile));
			String line = null;
			String[] lineArr = null;
			List<Integer> lineList = null;
			Pair<Integer> pair = null;
			int p1 = 0, p2 = 0;
			StringBuffer log = new StringBuffer();
			while((line = reader.readLine())!= null){
				lineArr = line.split("\\t");
				if(lineArr.length > 1){
					lineList = new LinkedList<Integer>();
					for(int i = 0; i < lineArr.length; i++){
						lineList.add(Integer.parseInt(lineArr[i]));
					}
					
					// find the index of list that this AS's degree is max in the lineList
					int maxV = 0, maxK = 0;
					for(int j = 0; j < lineList.size(); j++){
						if(lineList.get(j) > maxV){
							maxK = j;
							maxV = lineList.get(j);
						}
					}
					//log.append(((double)maxK/lineList.size())).append("\r\n");
					//assign the transit relationship
					for(int i = 0; i < maxK; i++){
						p1 = lineList.get(i);
						p2 = lineList.get(i+1);
						pair = new Pair<Integer>(p1, p2, false);
						if(transit.containsKey(pair)){
							transit.put(pair, transit.get(pair)+1);
						}else{
							transit.put(pair, 1);
						}
						
					}
					for(int j = maxK+1; j < lineList.size(); j++){
						p1 = lineList.get(j);
						p2 = lineList.get(j-1);
						pair = new Pair<Integer>(p1, p2, false);
						if(transit.containsKey(pair)){
							transit.put(pair, transit.get(pair)+1);
						}else{
							transit.put(pair, 1);
						}						
					}
					
				}
			}
			reader.close();
			
			
			//write file
			this.dstFile = this.dstDir+"transit.txt";
			StringBuffer sb = new StringBuffer();
			for(Iterator<Pair<Integer>> it = transit.keySet().iterator(); it.hasNext();){
				pair = it.next();
				sb.append(pair.getL()).append("\t").append(pair.getR()).append("\t").append(transit.get(pair)).append("\r\n");
			}
			this.ft.write(sb, dstFile);
			
			//log
			this.ft.write(log, this.dstDir+"transit.log");
		} catch (FileNotFoundException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		
		return transit;
	}
	

	/*
	 * assign relationship to AS pairs
	 */
	public Map<Pair<Integer>, EdgeType> assignRelationship(Map<Pair<Integer>, Integer> transit){
		Map<Pair<Integer>, EdgeType> edgeRelationship = new HashMap<Pair<Integer>, EdgeType>();		
	
		
		try {
			BufferedReader reader = new BufferedReader(new FileReader(this.srcFile));
			String line = null;
			String[] lineArr = null;
			int p1 = 0, p2 = 0, v1 = 0, v2 = 0;
			Pair<Integer> pair = null, pair1 = null, pair2 = null;
			while((line = reader.readLine())!= null){
				lineArr = line.split("\\t");
				if(lineArr.length > 1){
					for(int i = 0; i < lineArr.length-1; i++){
						p1 = Integer.parseInt(lineArr[i]);
						p2 = Integer.parseInt(lineArr[i+1]);
						pair1 = new Pair<Integer>(p1, p2, false);
						pair2 = new Pair<Integer>(p2, p1, false);
						
						if(transit.containsKey(pair1)){
							v1 = transit.get(pair1);
						}
						if(transit.containsKey(pair2)){
							v2 = transit.get(pair2);
						}
						
						if((v1 > this.L && v2 > this.L)||
								(v1 <= this.L && v1 > 0 && 
								v2 <= this.L && v2 > 0)){
							edgeRelationship.put(pair1, this.edgeType.S2S);
						}else if(v2 > this.L || v1 == 0){
							edgeRelationship.put(pair1, this.edgeType.P2C);
						}else if(v1 > this.L || v2 == 0){
							edgeRelationship.put(pair1, this.edgeType.C2P);
						}
					}//for					
				}//if
			}
			reader.close();
			
			/*
			 * write the assign as relationship to the destination file
			 */
/*			StringBuffer sb = new StringBuffer();
			for(Iterator<Pair<Integer>> it = edgeRelationship.keySet().iterator(); it.hasNext();){
				pair = it.next();
				sb.append(pair.getL()).append("\t").append(pair.getR()).append("\t");
				switch (edgeRelationship.get(pair)){
					case P2C:sb.append("p2c").append("\r\n");
						break;
					case C2P:sb.append("c2p").append("\r\n");
						break;
					case S2S:sb.append("s2s").append("\r\n");
						break;
				default:D.p("NO RELATIONSHIP!!!");
					break;
				}
			}
			this.dstFile = this.dstDir+"relationship.txt";
			this.ft.write(sb, dstFile);*/
			
		} catch (FileNotFoundException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		
		return edgeRelationship;
	}
	
	
	/**
	 *  
	 *  @param args
	 */
	public static void main(String[] args) {
		// TODO 自动生成的方法存根
//		BasicAlgorithm ba = new BasicAlgorithm("E:\\share\\data\\res\\rib.20140101.0000.txt", "E:\\share\\data\\res\\");
//		RefinedAlgorithm ba = new RefinedAlgorithm("E:\\share\\data\\res\\bview.20140101.000.txt", "E:\\share\\data\\res4\\");
//		BasicAlgorithm ba = new BasicAlgorithm("E:\\share\\data\\res\\updates.20140101.000.txt", "E:\\share\\data\\res1\\");
//		ba.assignRelationship(ba.transitRelationship(ba.asDegree()));
	}

}
