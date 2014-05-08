package me.mervin.module.feature;

import java.util.Map;
import java.util.Set;

import me.mervin.core.Network;
import me.mervin.util.D;

/**
 * 
 * 	ConstructEntropy.java
 * 
 * @author Mervin Time: 2014年4月16日 下午1:48:21
 * @email:mervin0502@163.com
 * @version 0.5.0
 * @changLog
 * 	
 */
public class StructureEntropy {

	private Network net = null;
	
	/**
	 * 
	 */
	public StructureEntropy() {
		// TODO Auto-generated constructor stub
	}
	public StructureEntropy(Network net){
		this.net = net;
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
	 * construct entropy of the network
	 * @param net network
	 * @param flag
	 * @return double
	 */
	public double script(Network net, boolean flag){
		this.net = net;
		return this.script(flag);
	}
	/**
	 * Construct entropy of the network
	 * @return double
	 */
	public double script(){
		return this.script(true);
	}
	/**
	 * Construct entropy of the network
	 * @param flag
	 * @return double
	 */
	public double script(boolean flag){
	//	if(net.isConnectedNet()){
			Degree degree = new Degree(this.net);
			Map<Number, Number> degreeDistributionRatioMap = degree.netDegreeDistributionRatio();
			return this._script(degreeDistributionRatioMap, flag);
/*		}else{
			D.p("no connected net:");
			Set<Network> netSet = this.net.getAllSubNet();
			D.p("no connected net:"+netSet.size());
			Degree degree = new Degree();
			Map<Number, Number> degreeDistributionRatioMap = null;
			double p = 0;
			for(Network subNet:netSet){
				degreeDistributionRatioMap = degree.netDegreeDistributionRatio(subNet);
				p += this._script(degreeDistributionRatioMap, flag);
			}
			return (double)p/netSet.size();
		}*/
	}

	/**
	 * 
	 * @param degreeDistributionRatioMap network degree distribution ratio
	 * @return double
	 */
	private double _script(Map<Number, Number> degreeDistributionRatioMap, boolean flag){
		double v = 0;
		for(Number p:degreeDistributionRatioMap.values()){
			//v+= -p.doubleValue()*(Math.log10(p.doubleValue())/Math.log10(2));
			//D.p(p.doubleValue());
			v+= -p.doubleValue()*(Math.log10(p.doubleValue())/Math.log10(2));
//			v+= -p.doubleValue()*(Math.log(p.doubleValue()));
		}
		if(flag){
			int nodeNum = this.net.nodeNum;
			double vMax = Math.log(nodeNum)/Math.log(2);
			double vMin = vMax-(double)(nodeNum-1)/nodeNum*(Math.log(nodeNum-1)/Math.log(2));
//			double vMax = Math.log(nodeNum);
//			double vMin = vMax-(double)(nodeNum-1)/nodeNum*(Math.log(nodeNum-1));
			if(vMax-vMin == 0){
				return 0;
			}else{
				return (double)(v-vMin)/(vMax-vMin);
			}
		}else{
			return v;
		}
		
		
	}
	
/*	public double script(Network net, boolean flag){
		
	}*/
}
