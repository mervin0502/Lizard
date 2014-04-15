package me.mervin.module.feature;

import java.util.HashMap;
import java.util.Map;

import me.mervin.core.Network;
import me.mervin.core.Global.DegreeType;
import me.mervin.core.Global.NetType;
import me.mervin.util.D;
import me.mervin.util.Pair;
import me.mervin.util.PairList;

/**
 * 
 *  MixingCoefficient.java
 *   网络的匹配系数   
 *  @author Mervin.Wong  DateTime 2013-9-10 下午7:44:09    
 *  @version 0.4.1
 *  ***********************************************************
 *  Ref: 	http://en.wikipedia.org/wiki/Assortativity
 *  				assortative mixing in networks
 */
/*
 * ChangeLog
 * **********************************************************
 * +有向图
 * - class name
 */
public class AssortativityCoefficient {
	
	private Network net = null;//网络对象
	/**
	 * 构造函数
	 */
	public AssortativityCoefficient(){
		
	}
	public AssortativityCoefficient(Network net){
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
	 *  <p><strong>Ref:</strong>assortative mixing in networks</p>
	 */
	public double script(Network net){
		
		if(Network.netType.equals(NetType.UNDIRECTED)){
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
		}else{
			return this.script(net, DegreeType.OUT, DegreeType.OUT);
		}
	}

	/**
	 * 
	 *  有向网络的匹配系数 
	 *  @param net 网络
	 *  @param dt1 度的类型  入度/出度
	 *  @param dt2 度的类型  入度/出度
	 *  @return double
	 *  **************************************<br/>
	 *  Ref:Edge direction and the structure of networks
	 *  http://en.wikipedia.org/wiki/Assortativity
	 */
	public double script(Network net, DegreeType dt1, DegreeType dt2){
		try{
			if(Network.netType.equals(NetType.UNDIRECTED)){
				throw new Exception();
			}else{
				
				Degree d = new Degree();
				
				Map<Number, Pair<Integer>> edgeMap = new HashMap<Number, Pair<Integer>>();//边的dt1-度值和dt2-度值
				int edgeNum = net.edgeNum;
				Number l = null, r = null;
				
				int lDegree = 0, rDegree = 0;
				int lSum = 0, rSum = 0;
				
				PairList<Number, Number> edgeList = net.traverseEdge();
				//获取所有边的端点度
				for(int i = 0; i < edgeNum; i++){
					l = edgeList.getL(i);
					r = edgeList.getR(i);
					
					//l=>r  l的度
					if(dt1.equals(DegreeType.IN)){
						lDegree = d.nodeInDegree(net, l);
					}else if(dt1.equals(DegreeType.OUT)){
						lDegree = d.nodeOutDegree(net, l);
					}
					//r的度
					if(dt2.equals(DegreeType.IN)){
						rDegree = d.nodeInDegree(net, r);
					}else if(dt2.equals(DegreeType.OUT)){
						rDegree = d.nodeOutDegree(net, r);
					}
					
					
					edgeMap.put(i, new Pair<Integer>(lDegree, rDegree));
					lSum += lDegree;
					rSum += rDegree;
				}
				
				//计算平均值
				double lAvg = 0, rAvg = 0;
				lAvg = (double)lSum/edgeNum;
				rAvg = (double)rSum/edgeNum;
				double s1 = 0, s2 = 0, s3 = 0;
				//int i = 0;
				for(Pair<Integer> p:edgeMap.values()){
					s1 += (double)(p.getL()-lAvg)*(p.getR()-rAvg);
					s2 += Math.pow(p.getL()-lAvg, 2);
					s3 += Math.pow(p.getR()-rAvg, 2);
				}
				if(s2 == 0 || s3 == 0){
					return 0;
				}else{
					return (double)s1/(Math.sqrt(s2)*Math.sqrt(s3));
				}
			}
			
		}catch(Exception e){
			e.getStackTrace();
		}
		
		return 0;
	}
/*	public static void main(String[] args) {
		// TODO 自动生成的方法存根
		Network net = new Network("../data/test/karate.txt", NetType.UNDIRECTED);
		MixingCoefficient ac = new MixingCoefficient();
		D.p(ac.script(net));
	}*/

}
