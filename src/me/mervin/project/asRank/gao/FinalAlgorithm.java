package me.mervin.project.asRank.gao;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import me.mervin.project.asRank.gao.RefinedAlgorithm.EdgeType;
import me.mervin.util.D;
import me.mervin.util.FileTool;
import me.mervin.util.Pair;


 /**
 *   FinalAlgorithm.java
 *    
 *  @author Mervin.Wong  DateTime 2014年3月17日 下午7:21:24    
 *  @version 0.4.0
 */
public class FinalAlgorithm {

	private String srcFile = null;
	private String dstFile = null;
	private String dstDir = null;
	
	private FileTool ft = new FileTool();
	public int R = 60;
	public FinalAlgorithm(String srcFile, String dstDir){
		this.srcFile = srcFile;
		this.dstDir = dstDir;
	}
	
	
	public Map<Pair<Integer>, Integer> notPeeringRelationship(Map<Number, Number> asDegree, Map<Pair<Integer>, EdgeType> edgeRelationship){
		Map<Pair<Integer>, Integer> notPeering = new HashMap<Pair<Integer>, Integer>();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(this.srcFile));
			String line = null;
			String[] lineArr = null;
			List<Integer> lineList = null;
			Pair<Integer> pair = null, pair1 = null, pair2 = null;
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

					//assign the not peering relationship
					for(int i = 0; i < maxK-1; i++){
						p1 = lineList.get(i);
						p2 = lineList.get(i+1);
						pair = new Pair<Integer>(p1, p2, false);
						notPeering.put(pair, 1);
						
					}
					for(int j = maxK+1; j < lineList.size()-1; j++){
						p1 = lineList.get(j);
						p2 = lineList.get(j+1);
						pair = new Pair<Integer>(p1, p2, false);
						notPeering.put(pair, 1);
					}
					
					if(maxK > 0 && maxK < lineList.size()-1){
						pair1 = new Pair<Integer>(lineList.get(maxK-1), lineList.get(maxK));
						pair2 = new Pair<Integer>(lineList.get(maxK), lineList.get(maxK+1));
						if(!edgeRelationship.get(pair1).equals(RefinedAlgorithm.EdgeType.S2S) && 
								!edgeRelationship.get(pair2).equals(RefinedAlgorithm.EdgeType.S2S)){
							if (asDegree.get(lineList.get(maxK-1)).intValue() > asDegree.get(lineList.get(maxK+1)).intValue()){
								notPeering.put(pair2, 1);
							}else{
								notPeering.put(pair1, 1);
							}
						}//if
					}//if

				}
			}
			reader.close();
			
			//write file

		} catch (FileNotFoundException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		
		return notPeering;
	}
	
	public void assignPeering(Map<Number, Number> asDegree, Map<Pair<Integer>, Integer> notPeering, Map<Pair<Integer>, EdgeType> edgeRelationship){
		Map<Pair<Integer>, Integer> peering = new HashMap<Pair<Integer>, Integer>();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(this.srcFile));
			String line = null;
			String[] lineArr = null;
			List<Integer> lineList = null;
			Pair<Integer> pair = null, pair1 = null, pair2 = null;
			int p1 = 0, p2 = 0;
			StringBuffer log = new StringBuffer();
			while((line = reader.readLine())!= null){
				lineArr = line.split("\\t");
				if(lineArr.length > 1){
					lineList = new LinkedList<Integer>();
					for(int i = 0; i < lineArr.length; i++){
						lineList.add(Integer.parseInt(lineArr[i]));
					}
					
					for(int j = 0; j < lineList.size()-1; j++){
						p1 = lineList.get(j);
						p2 = lineList.get(j+1);
						pair1 = new Pair<Integer>(p1, p2, false);
						pair2 = new Pair<Integer>(p2, p1, false);
						
						if(!notPeering.containsKey(pair1) &&
								!notPeering.containsKey(pair2)&&
								((double)asDegree.get(p1).intValue()/asDegree.get(p2).intValue()) < this.R &&
								((double)asDegree.get(p1).intValue()/asDegree.get(p2).intValue()) < (double)1/this.R){
							D.p("P2P");
							edgeRelationship.put(pair1, EdgeType.P2P);
						}
					}

					
				}
			}
			reader.close();
			
			
			/*
			 * write the assign as relationship to the destination file
			 */
			StringBuffer sb = new StringBuffer();
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
					case P2P:sb.append("p2p").append("\r\n");
				default:D.p("NO RELATIONSHIP!!!");
					break;
				}
			}
			this.dstFile = this.dstDir+"relationship.txt";
			this.ft.write(sb, dstFile);
		} catch (FileNotFoundException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}
	public static void main(String[] args){
		
	}
}
