package me.mervin.module.feature;

import java.util.Map;

import me.mervin.core.Network;
import me.mervin.util.PairList;


 /**
 *   DegreeCorrelation.java
 *    
 *  @author Mervin.Wong  DateTime 2013-10-11 下午7:45:35    
 *  @version 0.4.0
 */

/*
 * 联合分布率 
 */
public class DegreeCorrelation {


	private Network net = null;
	
	public DegreeCorrelation(){
		
	}
	public DegreeCorrelation(Network net){
		this.net = net;
	}
	
	/**
	 * 联合概率分布
	 */
	public void jointProbabilityDistribution(){
		
	}
	
	/**
	 * 联合概率分布
	 *  
	 *  @param j
	 *  @param k
	 */
	public double jointProbabilityDistribution(Network net, int j, int k){
		
		PairList<Number, Number> edges = net.traverseEdge();//所有的边
		Degree d = new Degree();
		Map<Number, Number> degreeMap = d.nodeDegree(net, net.getAllNodeId());//所有节点的度
		

		Number l = null, r = null;
		int count = 0;
		for(int i = 0; i < edges.size(); i++){
			l = edges.getL(i);
			r = edges.getR(i);
			if( (degreeMap.get(l).equals(j) && degreeMap.get(r).equals(k))  || (degreeMap.get(l).equals(k)&&degreeMap.get(r).equals(j)) ){
				count++;
			}
		}		
		boolean flag = false;
		if(j == k ){
			flag = true;
		}
		
		if(flag){
			return (double)count*2/(2*edges.size());
		}else{
			return (double)count/(2*edges.size());
		}
	}
	
	
	
	/**
	 *  
	 *  @param args
	 */
	public static void main(String[] args) {
		// TODO 自动生成的方法存根

	}

	
	

}
