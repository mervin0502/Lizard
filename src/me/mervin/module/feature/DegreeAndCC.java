/**
 * 
 */
package me.mervin.module.feature;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import me.mervin.core.Network;
import me.mervin.util.MathTool;

/**
 * 	DegreeAndCC.java
 * calculate the degree vs. clustercofficient
 * @author Mervin Time: 2014年4月16日 下午8:27:20
 * @email:mervin0502@163.com
 * @version 0.5.0
 */
public class DegreeAndCC {
	private Network  net = null;
	
	/**
	 * 
	 */
	public DegreeAndCC() {
		// TODO Auto-generated constructor stub
	}
	public DegreeAndCC(Network net){
		this.net = net;
	}
	
	/**
	 * 
	 * @return Map<Number, Number>
	 */
	public Map<Number, Number> script(){
		
		Map<Number, Number> v = null;	
		if(this.net == null){
			try {
				throw new Exception();
			} catch (Exception e) {
				// TODO: handle exception
			}
			return null;
		}
		v = new HashMap<Number, Number>();
		//the cluster cofficient of each node
		Map<Number, Number> ccMap = new ClusterCofficient().nodeClusterCofficient(this.net, this.net.getAllNodeId());
		//the degree of each node
		Map<Number, Number> degreeMap = new Degree().nodeDegree(this.net, this.net.getAllNodeId());
		//the frequent of each degree value 
		Map<Number, Number> degreeDistributionMap = MathTool.frequency(degreeMap);
		
		double k = 0;
		Number degree = null;		
		/*
		 * get the sum of each fixed degree value
		 */
		for(Number nodeId:ccMap.keySet()){
			degree = degreeMap.get(nodeId);
			if(v.containsKey(nodeId)){
				k = v.get(nodeId).doubleValue()+ccMap.get(nodeId).doubleValue();
			}else{
				k = ccMap.get(nodeId).doubleValue();
			}
			v.put(degree, k);
		}
		/*
		 * calculate the average value
		 */
		for(Number d:v.keySet()){
			double p = v.get(d).doubleValue()/degreeDistributionMap.get(d).doubleValue();
			v.put(d, p);
		}
		return v;
	}
	/**
	 * 
	 * @param net network object
	 * @return Map<Number, Number>
	 */
	public Map<Number, Number> script(Network net){
		this.net = net;
		return this.script();
	}
	
	
	
}
