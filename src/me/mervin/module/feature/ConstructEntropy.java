package me.mervin.module.feature;

import java.util.Map;

import me.mervin.core.Network;

/**
 * 
 * 	ConstructEntropy.java
 * 
 * @author Mervin Time: 2014年4月16日 下午1:48:21
 * @email:mervin0502@163.com
 * @version 0.5.0
 */
public class ConstructEntropy {

	private Network net = null;
	
	/**
	 * 
	 */
	public ConstructEntropy() {
		// TODO Auto-generated constructor stub
	}
	public ConstructEntropy(Network net){
		this.net = net;
	}
	
	
	/**
	 * Construct entropy of the network
	 * @return double
	 */
	public double script(){
		Degree degree = new Degree(this.net);
		Map<Number, Number> degreeDistributionRatioMap = degree.netDegreeDistributionRatio();
		return this.script(degreeDistributionRatioMap);
	}
	/**
	 * construct entropy of the network
	 * @param net network
	 * @return double
	 */
	public double script(Network net){
		this.net = net;
		return this.script();
	}
	/**
	 * 
	 * @param degreeDistributionRatioMap network degree distribution ratio
	 * @return double
	 */
	public double script(Map<Number, Number> degreeDistributionRatioMap){
		double v = 0;
		for(Number p:degreeDistributionRatioMap.values()){
			v+= -p.doubleValue()*(Math.log10(p.doubleValue())/Math.log10(2));
		}
		return v;
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	
	
}
