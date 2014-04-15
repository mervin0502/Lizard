package me.mervin.project.linkPrediction.similarity.quasi;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import me.mervin.core.Network;
import me.mervin.core.Global.NetType;
import me.mervin.project.linkPrediction.similarity.Similarity;
import me.mervin.util.D;
import me.mervin.util.E;
import me.mervin.util.MathTool;
import me.mervin.util.Pair;
import me.mervin.util.PairList;

/**
 * 
 *   LP.java
 *  局部链路预测算法  
 *  @author Mervin.Wong  DateTime 2013-7-29 下午6:24:07    
 *  @version 0.4.1
 *  
 *  *******************************************************
 *  ChangeLog
 *  ==2013/10/13
 *  # core.global.NetType{DIRECTED, UNDIRECTED}
 */
public class LP extends Similarity {
	/**
	 * 自由参数，增加区分度，同时降低影响
	 */
	public double p = 0.001;
	/**
	 * 构造函数
	 * @param net1 网络
	 */
	public LP(Network net1) {
		super(net1);
		this.p = 0.001;
		// TODO Auto-generated constructor stub
	}
	/**
	 * 构造函数
	 * @param net1 网络
	 * @param k 存在边集合的划分个数
	 * @param p 参数
	 * @throws E 异常输出
	 */
	public LP(Network net1, int k, double p) throws E {
		super(net1, k);
		this.p = p;
		// TODO Auto-generated constructor stub
	}
	/**
	 * 
	 *  通过算法对未知边赋值
	 *  @return Map<Pair<Number>, Double>
	 */
	@Override
/*	public Map<Pair<Number>, Double> run() {
		// TODO Auto-generated method stub
		Map<Pair<Number>, Double> map = new HashMap<Pair<Number>, Double>();
		PairList<Number, Number> unknowEdge = this.unknowEdgeSet();//未知边的集合
		Network net2 = this.deleteProbeEdge();//删除探测集中的边后的网络
		int size = unknowEdge.size();
		Number l, r;
		Pair<Number> p = null;
		double temp = 0;
		for(int i = 0; i < size; i++){
			l = unknowEdge.getL(i);
			r = unknowEdge.getR(i);
			p = new Pair<Number>(l, r);
			temp = this.commonAdjNodeId(net2, l, r).size()+this.pathCountByTriple(net2, l, r).size()*this.p;
			map.put(p, temp);
		}
		return map;
	}*/
	public Map<Pair<Number>, Double> script() {
		// TODO Auto-generated method stub
		Map<Pair<Number>, Double> score = new HashMap<Pair<Number>, Double>();//从x到y的概率值
		Network net2 = this.deleteProbeEdge();//删除探测集中的边后的网络
		Set<Number> allNodeIdSet = net2.getAllNodeId();//所有节点
		
		Pair<Number> p = null;
		double temp = 0;
		Set<Number> adjNodeIdSet = null;
		//遍历所有的未知边，找到其概率值
		if(Network.netType.equals(NetType.UNDIRECTED)){
			//无向 (l, r) == (r, l)
			for(Number nodeId1:allNodeIdSet){
				adjNodeIdSet = net2.getAdjNodeId(nodeId1);//nodeId1的邻接点
				
				//从nodeId1出发，到达所有其他节点的概率
				for(Number nodeId2:allNodeIdSet){
					//nodeId1与nodeId2没有直接连边
					if(!nodeId2.equals(nodeId1) && !adjNodeIdSet.contains(nodeId2)){
						p = new Pair<Number>(nodeId1, nodeId2);
						temp = this.commonAdjNodeId(net2,nodeId1, nodeId2).size()+this._pathCountByTriple(net2, nodeId1, nodeId2).size()*this.p;
						score.put(p, temp);
					}
				}
			}
		}else{
			//有向
			for(Number nodeId1:allNodeIdSet){
				adjNodeIdSet = net2.getAdjNodeId(nodeId1);//nodeId1的邻接点
				
				//从nodeId1出发，到达所有其他节点的概率
				for(Number nodeId2:allNodeIdSet){
					//nodeId1与nodeId2没有直接连边
					if(!nodeId2.equals(nodeId1) && !adjNodeIdSet.contains(nodeId2)){
						p = new Pair<Number>(nodeId1, nodeId2, false);
						temp = this.commonAdjNodeId(net2,nodeId1, nodeId2).size()+this._pathCountByTriple(net2, nodeId1, nodeId2).size()*this.p;
						score.put(p, temp);
					}
				}
			}
		}
		return score;
	}
	/*
	 * 
	 *  preNodeId经过3条到达postNodeId的路径数
	 *  @param net2 网络
	 *  @param preNodeId 网络节点
	 *  @param postNodeId 网络节点
	 *  @return Set<Number>
	 */
	private Set<Number> _pathCountByTriple(Network net2, Number preNodeId, Number postNodeId){
		Set<Number> nodeIdSet = null;
		Set<Number> nodeIdSet1 = net2.getAdjNodeId(preNodeId);//preNodeId邻接点
		Set<Number> nodeIdSet2 = net2.getAdjNodeId(postNodeId);//postNodeId的邻接点
		nodeIdSet = MathTool.intersection(net2.getAdjNodeId(nodeIdSet1), net2.getAdjNodeId(nodeIdSet2));
		return nodeIdSet;
	}

}
