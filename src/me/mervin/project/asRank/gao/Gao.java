package me.mervin.project.asRank.gao;

import java.util.Map;

import me.mervin.project.asRank.gao.RefinedAlgorithm.EdgeType;
import me.mervin.util.Pair;


 /**
 *   Gao.java
 *    
 *  @author
 *   Mervin.Wong  DateTime 2014年3月16日 下午4:14:18    
 *  @version 0.4.0
 */
public class Gao {

	/**
	 *  
	 *  @param args
	 */
	public static void main(String[] args) {
		// TODO 自动生成的方法存根
		String srcFile = "E:\\share\\data\\res\\bview.20140101.000.txt";
		String dstDir = "E:\\share\\data\\res4\\";
		RefinedAlgorithm refined = new RefinedAlgorithm(srcFile, dstDir);
		Map<Number,Number> asDegree = refined.asDegree();
		Map<Pair<Integer>, Integer> transit = refined.transitRelationship(asDegree);
		Map<Pair<Integer>, EdgeType> edgeRelationship = refined.assignRelationship(transit);
		
		FinalAlgorithm fa = new FinalAlgorithm(srcFile, dstDir);
		Map<Pair<Integer>, Integer> notPeering = fa.notPeeringRelationship(asDegree, edgeRelationship);
		fa.assignPeering(asDegree, notPeering, edgeRelationship);
	}

}
