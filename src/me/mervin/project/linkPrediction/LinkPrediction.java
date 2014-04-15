package me.mervin.project.linkPrediction;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import me.mervin.core.Network;
import me.mervin.core.Global.NetType;
import me.mervin.project.linkPrediction.evaluation.AUC;
import me.mervin.project.linkPrediction.evaluation.Precision;
import me.mervin.util.D;
import me.mervin.util.E;
import me.mervin.util.Pair;
import me.mervin.util.PairList;

/**
 * 
 *   LinkPrediction.java
 *    链路预测中算法
 *    
 *  @author Mervin.Wong  DateTime 2013-7-25 下午6:53:43    
 *  @version 0.4.1
 *  
 *  *******************************************************
 *  ChangeLog
 *  ==2013/10/13
 *  # core.global.NetType{DIRECTED, UNDIRECTED}
 */
public abstract class LinkPrediction {

	/**
	 * 划分边集合的子集个数
	 */
	public int k = 0;
	/**
	 * 预测的网络
	 */
	public Network net1 = null;

	/*
	 * 将已存在的边划分成k个子集
	 */
	private Map<Integer, PairList<Number, Number>> edgeSetDiv = null;
	/*
	 * 选中m个子集为探测集
	 */
	private int probeIndex = 1;
	/**
	 * 构造函数
	 * @param net1 网络
	 */
	public LinkPrediction(Network net1){
		this.net1 = net1;
		this.k = (int) Math.sqrt(this.net1.edgeNum);//开方
		this.divEdge();
	}
	/**
	 * 构造函数
	 * @param net1 网络
	 * @param k 划分集合的子集个数
	 * @throws E k异常
	 */
	public LinkPrediction(Network net1, int k) throws E{
		this.net1 = net1;
		if(net1.edgeNum < k){
			throw new E("k大于网络的边数");
		}else{
			this.k = k;	
		}
		this.divEdge();
	}
	
	/**
	 * 
	 * 选中第m个自己为探测集 
	 *  @param index 划分的子集的序号
	 */
	public void setProbeIndex(int index){
		this.probeIndex = index;
	}
	/**
	 * 
	 *  获取探测集的序号
	 *  @return int
	 */
	public int getProbeIndex(){
		return this.probeIndex;
	}
	/*
	 * ***********************************************************************
	 * 对网络中边集合的操作
	 * 
	 * ***********************************************************************
	 */
	/**
	 * 网络中边的全集
	 * @return PairList<Number, Number>
	 */
	public PairList<Number, Number> allEdgeSet(){
		PairList<Number, Number> pl = new PairList<Number, Number>();
		Set<Number> nodeIdSet = this.net1.getAllNodeId();
		if(Network.netType.equals(NetType.UNDIRECTED)){
			Set<Pair<Number>> set = new HashSet<Pair<Number>>();
			Number nodeId1 = null, nodeId2 = null;
			for (Iterator<Number> it1 = nodeIdSet.iterator();it1.hasNext();) {
				nodeId1 = it1.next();
				//for (Number nodeId2 : nodeIdSet) {
				for (Iterator<Number> it2 = nodeIdSet.iterator();it2.hasNext();) {
					nodeId2 = it2.next();
					if(!nodeId1.equals(nodeId2)){
						//pl.add(nodeId1, nodeId2);
						// 取出 (l, r)=(r, l)
						//D.p(nodeId1+"@@"+nodeId2);
						
						set.add(new Pair<Number>(nodeId1, nodeId2));
					}
				}
			}
			
			Pair<Number> p = null;
			for(Iterator<Pair<Number>> it = set.iterator(); it.hasNext();){
				p = it.next();
				pl.add(p.getL(), p.getR());
			}
		}else{
			//有向
			for (Number nodeId1 : nodeIdSet) {
				for (Number nodeId2 : nodeIdSet) {
					if(!nodeId1.equals(nodeId2)){
						pl.add(nodeId1, nodeId2);
					}
				}
			}
		}
		return pl;		
	}
	/**
	 * 
	 * 网络中已存在边的集合 
	 *  @return PairList<Number, Number>
	 */
	public PairList<Number, Number> existEdgeSet(){
		return this.net1.traverseEdge();
	}
	/**
	 * 
	 *  获取测试集
	 *  @return PairList<Number, Number>
	 */
	public PairList<Number, Number> testEdgeSet(){
		PairList<Number, Number> pl = new PairList<Number, Number>();
		for(int i = 1; i< this.k; i++){
			if(i != this.probeIndex){
				pl.add(this.edgeSetDiv.get(i));
			}
		}
		return pl;	
	}
	/**
	 * 
	 *  获取训练集 
	 *  @return PairList<Number, Number>
	 */
	public PairList<Number, Number> probeEdgeSet(){
		return this.edgeSetDiv.get(this.probeIndex);		
	}
	/**
	 * 
	 *  不存在边的集合 
	 *  @return PairList<Number, Number>
	 */
	public PairList<Number, Number> nonexistEdgeSet(){
		PairList<Number, Number> all = this.allEdgeSet();//所有边集合
		PairList<Number, Number> nonexist = new PairList<Number, Number>();//不存在的边
		PairList<Number, Number> exist = this.existEdgeSet();//已存在的边
		Set<Pair<Number>> exit2 = exist.list2Set();
		
		Number l, r;
		for(int i = 0; i < all.size(); i++){
			l = all.getL(i);
			r = all.getR(i);
			if(!exit2.contains(new Pair<Number>(l,r))){
				nonexist.add(l, r);
			}
		}
		return nonexist;			
	}
	/**
	 * 
	 *  未知边的集合 
	 *  @return PairList<Number, Number>
	 */
	public PairList<Number, Number> unknowEdgeSet(){
		PairList<Number, Number> pl = new PairList<Number, Number>();
		pl.add(this.nonexistEdgeSet());
		pl.add(this.probeEdgeSet());
//		D.p(pl.size());
		return pl;	
	}
	/*
	 * 将已存在的边集合划分成k个子集
	 */
	private void divEdge(){
		PairList<Number, Number> pl = this.existEdgeSet();
		Number l, r;
		int j = 0;
		/*
		 * 初始化
		 * 1~k
		 */
//		D.p("##:"+this.k);
		this.edgeSetDiv = new HashMap<Integer, PairList<Number, Number>>();
		for(int i = 1; i <= this.k; i++){
			this.edgeSetDiv.put(i, new PairList<Number, Number>());
		}
		/*
		 * 划分到子集
		 */
		for(int i = 0; i < pl.size(); i++){
			l = pl.getL(i);
			r = pl.getR(i);
			j = i % k+1;
			this.edgeSetDiv.get(j).add(l, r);
		}
	}

	
	/*
	 ******************************************************************************* 
	 * 链路预测算法，对于未知边进行赋值
	 *
	 ******************************************************************************* 
	 */
	/**
	 * 从net1网络中删除探测集的边后赋值给net2
	 * @return Network 删除边后的网络
	 */
	public Network deleteProbeEdge(){
		/*
		 * 删除探测集中的边后的网络
		 */
		Network net2 = this.net1.copyNet();
		PairList<Number, Number> probeEdgeSet = this.probeEdgeSet();
		Number l, r;
		for(int i = 0; i < probeEdgeSet.size(); i++){
			l = probeEdgeSet.getL(i);
			r = probeEdgeSet.getR(i);
			//D.p("@@"+l+"-"+r);
			net2.deleteEdge(l, r);
		}
		//D.p("Adj"+net2.getAdjNodeId(5));
		return net2;
	}
	/**
	 * 
	 *  通过算法对未知边赋值
	 *  @return Map<Pair<Number>, Double>
	 */
	public abstract Map<Pair<Number>, Double> script();

	/*
	 * ********************************************************************************
	 * 评价指标
	 * ********************************************************************************
	 */
	/**
	 * 
	 *  AUC评价指标 
	 *  @return double
	 *  @throws E
	 */
	public double auc() throws E{
		return this.auc(this.probeEdgeSet().size()-1);
	}
	/**
	 * 
	 *  AUC评价指标 
	 *  @param n 随机选取的节点对的数量
	 *  @return double
	 *  @throws E
	 */
	public double auc(int n) throws E{
		double quota = 0;
		AUC auc = new AUC(this.script(), this.probeEdgeSet(), n);
		quota = auc.script();
		return quota;
	}
	/**
	 * 
	 *  AUC评价指标 多次的平均值
	 *  @return double
	 *  @throws E
	 */
	public double aucAvg() throws E{
		return this.aucAvg(this.probeEdgeSet().size()-1);
	}
	/**
	 * 
	 *  AUC评价指标 多次的平均值 
	 *  @param n 随机选取的节点对的数量
	 *  @return double
	 *  @throws E
	 */
	public double aucAvg(int n) throws E{
		double quota = 0;
		double sum = 0;
		for(int i = 1; i < this.k; i++){
			this.setProbeIndex(i);
			sum += this.auc(n);
		}
		quota = sum/this.k;
		return quota;
	}
	/**
	 * 
	 *  Precision评价指标 
	 *  @return double
	 *  @throws E
	 */
	public double precision() throws E{
		return this.precision(this.probeEdgeSet().size()-1);
	}
	/**
	 * 
	 *  Precision评价指标 
	 *  @param n 随机选取的节点对的数量
	 *  @return double
	 *  @throws E
	 */
	public double precision(int n) throws E{
		double quota = 0;
		Precision precision = new Precision(this.script(), this.probeEdgeSet(), n);
		quota = precision.script();
		return quota;
	}
	/**
	 * 
	 *  Precision评价指标 多次的平均值
	 *  @return double
	 *  @throws E
	 */
	public double precisionAvg() throws E{
		return this.precisionAvg(this.probeEdgeSet().size()-1);
	}
	/**
	 * 
	 *  Precision评价指标 多次的平均值 
	 *  @param n 随机选取的节点对的数量
	 *  @return double
	 *  @throws E
	 */
	public double precisionAvg(int n) throws E{
		double quota = 0;
		double sum = 0;
		for(int i = 1; i < this.k; i++){
			this.setProbeIndex(i);
			D.p(this.probeEdgeSet().size()+"###"+n);
			sum += this.precision(n);
		}
		quota = sum/this.k;
		return quota;
	}

}
