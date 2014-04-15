package me.mervin.module.feature;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import me.mervin.core.Network;
import me.mervin.util.D;
import me.mervin.util.FibonacciHeap;
import me.mervin.util.MathTool;
import me.mervin.util.PairList;
import me.mervin.util.other.INode;

/****************************************************************
 * 
 * 紧密度
 * 
 *
/****************************************************************
 */
public class Closeness {
	
	/*
	 * 网络
	 */
	private Network net;
	
	public Closeness(){
		
	}
	public Closeness(Network net){
		this.net = net;
	}
	
	public double nodeCloseness(Number nodeId){
		return this.nodeCloseness(this.net, nodeId);
	}
	/**
	 * 
	 *  节点紧密度
	 * @param net
	 * @param nodeId
	 * @return double
	 */
	public double nodeCloseness(Network net, Number nodeId){
		HashMap<Number, Integer> level = this.shortestPathNet(net, nodeId);
		
		double sum = 0;
		for(Iterator<Number> it = level.keySet().iterator(); it.hasNext();){
			sum += level.get(it.next());
		}
		return (double)1/sum;
	}
	/**
	 *  
	 *  节点集的紧密度
	 * @param nodesId
	 * @return Map<Number, Number>
	 */
	public Map<Number, Number> nodeCloseness(Set<Number> nodesId){
		return this.nodeCloseness(this.net, nodesId);
	}
	/**
	 * 紧密度中心化
	 * @param net
	 * @param nodesId
	 * @return Map<Number, Number>
	 */
	public Map<Number, Number> nodeCloseness(Network net, Set<Number> nodesId){
		HashMap<Number, Number> nodesCloseness = new HashMap<Number, Number>();
		
		Number nodeId = null;
		for (Iterator<Number> iterator = nodesId.iterator(); iterator.hasNext();) {
			nodeId = (Number) iterator.next();
			nodesCloseness.put(nodeId, this.nodeCloseness(net, nodeId));
		}
		return nodesCloseness;
	}
	
	/**
	 * 
	 *  Function: 全网的紧密度
	 * 
	 *  @return Map<Number, Number>
	 */
	public Map<Number, Number> netCloseness(){
		return this.netCloseness(net);
	}
	/**
	 * 
	 *  Function: 全网的紧密度
	 * 
	 *  @param net
	 *  @return Map<Number, Number>
	 */
	public Map<Number, Number> netCloseness(Network net){
		HashMap<Number, Number> nodesCloseness = new HashMap<Number, Number>();
		Set<Number> nodesId = net.getAllNodeId();
		Number nodeId = null;
		for (Iterator<Number> iterator = nodesId.iterator(); iterator.hasNext();) {
			nodeId = (Number) iterator.next();
			nodesCloseness.put(nodeId, this.nodeCloseness(net, nodeId));
		}
		return nodesCloseness;
	}
	/*
	 * **********************************************************************************
	 *  private method
	 *
	***********************************************************************************/
	/*
	 *  shortestPathNet
	 *  从一个节点到所有节点的最短路径网
	 * @param net
	 * @param preNodeId
	 * @return
	 */
	private HashMap<Number, Integer> shortestPathNet(Network net, Number preNodeId){
		preNodeId = MathTool.str2Number(Network.getNumberType(), preNodeId.toString());
		
		/**
		 * 基本变量
		 */
		HashSet<Number> s = new HashSet<Number>();//已遍历的节点
		
		HashMap<Number, Double> distance = new HashMap<Number, Double>();//有preNodeId->v的距离 
		PairList<Number, Number> edges = new PairList<Number, Number>();
		
		HashMap<Number, Integer> level = new HashMap<Number, Integer>();//有preNodeId->v的层数 
		
		/**
		 * 初始化数据结构
		 */
		Set<Number> q = net.getAllNodeId();//所有的节点
		FibonacciHeap<Number> heap = new FibonacciHeap<Number>();//斐波那契堆
		Iterator<Number> it = q.iterator();
		
		Queue<Number> queue = new LinkedList<Number>();
		
		queue.offer(preNodeId);
		queue.offer(-1);//表示一层的结束
		level.put(preNodeId, 0);
		heap.insert(0, preNodeId);
		distance.put(preNodeId, (double) 0);
		
		Number nodeId = null;
		
		Set<Number> adjNodesId = null;
		Number adjNodeId = null;
		double weight = 0;

		while(it.hasNext()){
			nodeId = it.next();
			if(nodeId.equals(preNodeId)){
				heap.insert(0, nodeId);
				distance.put(nodeId, (double) 0);
			}else{
				heap.insert(Integer.MAX_VALUE, nodeId);
				distance.put(nodeId, (double) Integer.MAX_VALUE);
			}
		}
		
		INode<Number> node = null;
 		while(heap.elCount > 0){
			node = heap.extractMin();
			nodeId = node.value();
			s.add(nodeId);
			adjNodesId = net.getAdjNodeId(nodeId);
			for (Iterator<Number> iterator = adjNodesId.iterator(); iterator.hasNext();) {
				adjNodeId = (Number) iterator.next();
				if(!s.contains(adjNodeId)){
					weight = net.getEdgeWeight(nodeId, adjNodeId)+distance.get(nodeId);
					if(weight < distance.get(adjNodeId)){
						//小于
						distance.put(adjNodeId, weight);
						
						heap.decreseKey(heap.getNode(adjNodeId), weight);
						//删除原来的目的节点是adjNodeId边
						edges.removeByR(adjNodeId);
						//添加新边
						edges.add(nodeId, adjNodeId);
						
						//节点所在的层是否发生变化
						//D.p("@@@@@@@"+nodeId+"######"+adjNodeId);
						level.put(adjNodeId, level.get(nodeId)+1);
					}else if(weight == distance.get(adjNodeId)){
						//等于
						edges.add(nodeId, adjNodeId);
					}else{
						// 大于
					}
					//D.p(preNodeId+"######"+adjNodeId);
				}
			}
		}
		return level;
	}
}
