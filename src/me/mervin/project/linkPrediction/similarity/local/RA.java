package me.mervin.project.linkPrediction.similarity.local;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import me.mervin.core.Network;
import me.mervin.core.Global.NetType;
import me.mervin.project.linkPrediction.similarity.Similarity;
import me.mervin.util.E;
import me.mervin.util.Pair;
import me.mervin.util.PairList;

/**
 * 
 *   RA.java
 *    
 *  @author Mervin.Wong  DateTime 2013-10-13 下午3:38:17    
 *  @version 0.4.1
 *  
 *  *******************************************************
 *  ChangeLog
 *  ==2013/10/13
 *  # core.global.NetType{DIRECTED, UNDIRECTED}
 */
public class RA extends Similarity {

	/**
	 * 构造函数
	 * @param net1 网络
	 */
	public RA(Network net1) {
		super(net1);
		// TODO Auto-generated constructor stub
	}
	/**
	 * 构造函数
	 * @param net1 网络
	 * @param k 存在边集合的划分个数
	 * @throws E 异常输出
	 */
	public RA(Network net1, int k) throws E {
		super(net1, k);
		// TODO Auto-generated constructor stub
	}
	@Override
	/*public Map<Pair<Number>, Double> run() {
		// TODO Auto-generated method stub
		Map<Pair<Number>, Double> map = new HashMap<Pair<Number>, Double>();
		PairList<Number, Number> unknowEdge = this.unknowEdgeSet();//未知边的集合
		Network net2 = this.deleteProbeEdge();//删除探测集中的边后的网络
		net2.traverseEdge();
		
		int size = unknowEdge.size();
		Number l, r;
		Pair<Number> p = null;
		Set<Number> commonAdj = null;//共同邻居
		double score = 0;
		for(int i = 0; i < size; i++){
			l = unknowEdge.getL(i);
			r = unknowEdge.getR(i);
			p = new Pair<Number>(l, r);
			
			score = 0;
			commonAdj = this.commonAdjNodeId(net2, l, r);
			for(Number nodeId:commonAdj){
				score += (double)1/net2.getAdjNodeId(nodeId).size();
			}
			map.put(p, score);
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
		Set<Number> commonAdj = null;//共同邻居
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
						commonAdj = this.commonAdjNodeId(net2, nodeId1, nodeId2);
						temp = 0;
						for(Number nodeId3:commonAdj){
							temp += (double)1/net2.getAdjNodeId(nodeId3).size();
						}
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
						commonAdj = this.commonAdjNodeId(net2, nodeId1, nodeId2);
						temp = 0;
						for(Number nodeId3:commonAdj){
							temp += (double)1/net2.getAdjNodeId(nodeId3).size();
						}
						score.put(p, temp);
					}
				}
			}
		}
		return score;
	}
}
