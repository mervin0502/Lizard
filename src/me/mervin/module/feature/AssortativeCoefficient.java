package me.mervin.module.feature;

import me.mervin.core.Network;
import me.mervin.core.Global.NetType;
import me.mervin.util.D;
import me.mervin.util.PairList;

/**
 * 
 *   AssortativeCoefficient.java
 *  网络的匹配系数   
 *  @author Mervin.Wong  DateTime 2013-9-10 下午7:44:09    
 *  @version 0.4.0
 */
public class AssortativeCoefficient {
	
	private Network net = null;//网络对象
	/**
	 * 构造函数
	 */
	public AssortativeCoefficient(){
		
	}
	public AssortativeCoefficient(Network net){
		this.net = net;
	}

	/**
	 * 获取网络的匹配系数
	 *  
	 *  @return double
	 */
	public double script(){
		return this.script(this.net);
	}
	
	/**
	 * 
	 *  获取网络的匹配系数
	 *  @param net
	 *  @return double
	 */
	public double script(Network net){
		double coefficient = 0;
		long edgeNum = net.edgeNum;
		
		PairList<Number, Number> pl = net.traverseEdge();
		Degree d = new Degree();
		
		double p1 = 0, p2 = 0, p3 = 0;
		int count = pl.size();
		int preDegree = 0, postDegree = 0;
		
		for(int i = 0; i < count; i++){
			preDegree = d.nodeDegree(net, pl.getL(i));
			postDegree = d.nodeDegree(net, pl.getR(i));
			
			p1 += preDegree*postDegree;
			p2 += preDegree+postDegree;
			p3 += Math.pow(preDegree, 2)+Math.pow(postDegree, 2);
		}
		p2 = p2*0.5;
		p3 = p3*0.5;
		double p4 = (double)1/edgeNum;
		//D.p(p1+"##"+p2+"###"+p3+"###"+p4);
		coefficient = (p4*p1-Math.pow(p4*p2, 2))/(p4*p3-Math.pow(p4*p2, 2));
		return coefficient;
	}

	public static void main(String[] args) {
		// TODO 自动生成的方法存根

	}

}
