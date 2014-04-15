/*********************************************************************************
 *
 *
 *
 **********************************************************************************/
package me.mervin.module.feature;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import me.mervin.core.Network;
import me.mervin.util.FibonacciHeap;
import me.mervin.util.MathTool;
import me.mervin.util.Pair;
import me.mervin.util.PairList;
import me.mervin.util.PairMap;
import me.mervin.util.other.INode;

/**
 * Betweenness.java
 * 
 *@author 王进法<Mervin.Wong>
 *@version 0.4
 */
/*
 * ********************************************************************************
 *
 *
 *
 **********************************************************************************/

public class Betweenness {
	private Network net = null;
	
	private double netBetweennessMaxValue = 0;
	private Number netBetweennessMaxId = null;
	
	public Betweenness(){
		
	}
	public Betweenness(Network net){
		this.net = net;
	}
	public void set(Network net){
		this.net = net;
	}
	public Network get(){
		return this.net;
	}
	/*
	 * *******************************************************************************************
	 * 
	 * 
	 * 
	********************************************************************************************/
	public double getNetBetweennessMaxValue(){
		if(this.netBetweennessMaxValue == 0){
			this.nodeBetweenness(net);
		}
		////D.p(this.netBetweennessMaxValue);
		return this.netBetweennessMaxValue/2;
	}
	public Number getNetBetweennessMaxId(){
		if(this.netBetweennessMaxId == null){
			this.nodeBetweenness(net);
		}
		return this.netBetweennessMaxId;
	}
	/**
	 *  
	 *  网络中所有节点介数
	 * @param net
	 * @return HashMap<Number, Double>
	 */
	@SuppressWarnings("unchecked")
	public HashMap<Number, Double> nodeBetweenness(Network net){
//		HashMap<Number, Double> betweenness = new HashMap<Number, Double>();
//		Map<String, Object> args = new HashMap<String, Object>();
		Map<String, Object>  netArgs= null;
		Set<Number> nodeSet = net.getAllNodeId();
		//HashMap<Number, Integer> nodeBetweenness = new HashMap<Number, Integer>();//某个节点的介数
		HashMap<Number, Double> nodeBetweenness = new HashMap<Number, Double>();//某个节点的介数
//		HashMap<Number, Integer> nodeNum = new HashMap<Number, Integer>();//v节点的从属节点
//		HashMap<Number, Integer> visit = null;
		
		 
//		int temp = 0;
		Number nodeId = null;
//		Number nodeId2 = null;
		for (Iterator<Number> iterator = nodeSet.iterator(); iterator.hasNext();) {
			nodeId = (Number) iterator.next();
			netArgs = this.shortestPathNet(net, nodeId);
			//accumulateChildNodes((Network)netArgs.get("network"), nodeId, nodeNum, (HashMap<Number, LinkedList<Number>>)netArgs.get("parent"));
			HashSet<Number> s = new HashSet<Number>();
			s.add(nodeId);
			getNodeBetweennessByRecursion((Network)netArgs.get("network"), nodeId, nodeId, nodeBetweenness, (HashMap<Number, LinkedList<Number>>)netArgs.get("parent"), (HashMap<Number, Integer>)netArgs.get("visit"),s);
		}
		////D.p(nodeBetweenness.get(2)+"@@");
		for(Iterator<Number> iterator = nodeBetweenness.keySet().iterator(); iterator.hasNext();){
			nodeId = (Number) iterator.next();
			nodeBetweenness.put(nodeId, nodeBetweenness.get(nodeId)/2);
		}
		return nodeBetweenness;
	}
	
	/**
	 *  
	 *  网络中所有边的介数
	 * @param net
	 * @return  PairMap<Number, Double>
	 */
	@SuppressWarnings("unchecked")
//	public HashMap<Pair<Number>, Double> edgesBetweenness(Network net){
	public PairMap<Number, Double> edgeBetweenness(Network net){
//		HashMap<Number, Double> betweenness = new HashMap<Number, Double>();
//		Map<String, Object> args = new HashMap<String, Object>();
		Map<String, Object>  netArgs= null;
		//Set<Number> nodeSet = net.topology.keySet();
		Set<Number> nodeSet = net.getAllNodeId();
		//HashMap<Number, Integer> nodeBetweenness = new HashMap<Number, Integer>();//某个节点的介数
		PairMap<Number, Double> edgeBetweenness = new PairMap<Number, Double>();//某个节点的介数
//		HashMap<Number, Integer> nodeNum = new HashMap<Number, Integer>();//v节点的从属节点
//		HashMap<Number, Integer> visit = null;
		
		 
//		int temp = 0;
		Number nodeId = null;
//		Number nodeId2 = null;
		for (Iterator<Number> iterator = nodeSet.iterator(); iterator.hasNext();) {
			nodeId = (Number) iterator.next();
			netArgs = this.shortestPathNet(net, nodeId);
			//accumulateChildNodes((Network)netArgs.get("network"), nodeId, nodeNum, (HashMap<Number, LinkedList<Number>>)netArgs.get("parent"));
			HashSet<Pair<Number>> s = new HashSet<Pair<Number>>();
			//s.add(nodeId);
			getEdgeBetweennessByRecursion((Network)netArgs.get("network"), nodeId, nodeId, edgeBetweenness, (HashMap<Number, LinkedList<Number>>)netArgs.get("parent"), (HashMap<Number, Integer>)netArgs.get("visit"),s);
		}
		Pair<Number> edge = null;
		for(Iterator<Pair<Number>> iterator = edgeBetweenness.keySet().iterator(); iterator.hasNext();){
			edge = (Pair<Number>) iterator.next();
			edgeBetweenness.put(edge, edgeBetweenness.get(edge)/2);
		}
		
		return edgeBetweenness;
	}
	/******************************************************************************************
	 * private method
	 * 
	******************************************************************************************
	 */
	
	/*
	 *  shortestPathNet
	 *  从一个节点到所有节点的最短路径网
	 * @param net
	 * @param preNodeId
	 * @return
	 */
	private Map<String, Object> shortestPathNet(Network net, Number preNodeId){
		preNodeId = MathTool.str2Number(Network.getNumberType(), preNodeId.toString());
		
		/**
		 * 基本变量
		 */
		HashSet<Number> s = new HashSet<Number>();//已遍历的节点
		
		HashMap<Number, Double> distance = new HashMap<Number, Double>();//有preNodeId->v的距离 
		HashMap<Number, Integer> visit = new HashMap<Number, Integer>();//在最短路径网中由preNodeId->v有几条路径可走 
		PairList<Number, Number> edges = new PairList<Number, Number>();
		
		HashMap<Number, Integer> level = new HashMap<Number, Integer>();//有preNodeId->v的层数 
		HashMap<Number, LinkedList<Number>> parent = new HashMap<Number, LinkedList<Number>>();//节点v的父节点
		
		/**
		 * 初始化数据结构
		 */
		Set<Number> q = net.getAllNodeId();//所有的节点
		FibonacciHeap<Number> heap = new FibonacciHeap<Number>();//斐波那契堆
		Iterator<Number> it = q.iterator();
		
		//Queue<Number> queue = new LinkedList<Number>();
		
		//queue.offer(preNodeId);
		//queue.offer(-1);//表示一层的结束
		level.put(preNodeId, 0);
		//heap.insert(0, preNodeId);
		//distance.put(preNodeId, 0);
		visit.put(preNodeId, 1);
		parent.put(preNodeId, null);//根节点的父节点列表为空
		Number nodeId = null;
		//int curLevel = 0;//当前的层数
		
		
		Set<Number> adjNodesId = null;
		Number adjNodeId = null;
		double weight =  0;
		//int maxLevel = 0;//最短路径中的最大边数

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
		LinkedList<Number> parentList = null;
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
						//////D.p(preNodeId+"@@@@"+adjNodeId+"###"+weight);
						heap.decreseKey(heap.getNode(adjNodeId), weight);
						//删除原来的目的节点是adjNodeId边
						edges.removeByR(adjNodeId);
						//添加新边
						edges.add(nodeId, adjNodeId);
						
						//节点所在的层是否发生变化
						////D.p("@@@@@@@"+nodeId+"######"+adjNodeId);
						level.put(adjNodeId, level.get(nodeId)+1);
						
						//preNode->v的路径条数
						visit.put(adjNodeId, visit.get(nodeId));
						
						//v的父节点parent， 添加新的
						parentList = new LinkedList<Number>();
						parentList.add(nodeId);
						parent.put(adjNodeId, parentList);
					//}else if(weight == distance.get(adjNodeId)){
					}else if(weight ==  distance.get(adjNodeId)){
						//等于
						edges.add(nodeId, adjNodeId);
						//preNode->v的路径条数
						visit.put(adjNodeId, visit.get(nodeId)+visit.get(adjNodeId));
						//v的父节点parent,追加
						parentList = parent.get(adjNodeId);
						parentList.add(nodeId);
					}else{
						// 大于
					}
					////D.p(preNodeId+"###"+nodeId+"###"+adjNodeId);
				}
			}
			//D.m();
		}
		Network net1 = new Network(edges, Network.netType, Network.getNumberType());
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("visit", visit);
		map.put("parent", parent);
		map.put("network", net1);
		return map;
		//return level;
	}
	
	/*
	 * recursion get the number of dependency nodes of the parent node 
	 * @param net
	 * @param preNodeId
	 * @param nodeNum
	 * @param parent
	 * @return
	 */
	private int accumulateChildNodes(Network net, Number preNodeId, HashMap<Number, Integer> nodeNum,HashMap<Number, LinkedList<Number>> parent){
		
		Number adjNodeId = null;
		int num = 0;
		//获取邻接点
		Set<Number> adjNodeSet = net.getAdjNodeId(preNodeId);
		for (Iterator<Number> iterator = adjNodeSet.iterator(); iterator.hasNext();) {
			adjNodeId = (Number) iterator.next();
			////D.p("@@@"+preNodeId);
			if(parent.get(preNodeId) == null || !parent.get(preNodeId).contains(adjNodeId)){
				//preNode的父节点不包含此节点
				num += accumulateChildNodes(net, adjNodeId, nodeNum, parent);
				//加上自己的子节点
				num++;
			}
		}
		//加上自己的子节点
		//num += adjNodeSet.size()-parent.get(preNodeId).size();
		//找到了节点preNodeId的从属字节点数量，
		nodeNum.put(preNodeId, num);
		//返回，以便于父节点统计
		return num;
	}
	
	/*
	 * 获取边介数
	 */
	private double getEdgeBetweennessByRecursion(Network net, Number preNodeId, Number rootId, PairMap<Number, Double> edgeBetweenness, HashMap<Number, LinkedList<Number>> parent, HashMap<Number, Integer> visit, HashSet<Pair<Number>> s){
		Number adjNodeId = null;
		double temp1 = 0, temp2 = 0, temp3 = 0;
		//获取邻接点
		Set<Number> adjNodeSet = net.getAdjNodeId(preNodeId);
		for (Iterator<Number> iterator = adjNodeSet.iterator(); iterator.hasNext();) {
			adjNodeId = (Number) iterator.next();
			if(parent.get(preNodeId) == null || !parent.get(preNodeId).contains(adjNodeId)){
				temp3 = this.getEdgeBetweennessByRecursion(net, adjNodeId, rootId, edgeBetweenness, parent, visit, s);
				////D.p(preNodeId+"####"+visit.get(preNodeId)+"####"+visit.get(adjNodeId)+"####"+adjNodeId+"#####"+temp3+"####");
				temp2 = ((double)visit.get(preNodeId)/visit.get(adjNodeId))*(temp3+1);
				temp1 += temp2;
				Pair<Number> edge = null;
				if(preNodeId.longValue() > adjNodeId.longValue()){
					 edge= new Pair<Number>(adjNodeId, preNodeId);
				}else{
					edge = new Pair<Number>(preNodeId, adjNodeId);
				}
				
				if(!s.contains(edge)){
					if(edgeBetweenness.get(edge) != null){
						edgeBetweenness.put(edge, edgeBetweenness.get(edge)+temp2);
						
					}else{
						edgeBetweenness.put(edge, temp2);
					}					
					s.add(edge);
				}
				////D.p(edgeBetweenness.get(edge)+"@@@@"+temp1);
			}
		}
		//返回，以便于父节点统计
		return temp1;
	}
	/*
	 * 获取点介数
	 */
	private double getNodeBetweennessByRecursion(Network net, Number preNodeId, Number rootId, HashMap<Number, Double> nodeBetweenness, HashMap<Number, LinkedList<Number>> parent, HashMap<Number, Integer> visit, HashSet<Number> s){
		Number adjNodeId = null;
		double temp1 = 0, temp2 = 0;
		//获取邻接点
		Set<Number> adjNodeSet = net.getAdjNodeId(preNodeId);
//		//D.p("$$$$$$$$$$$$$$"+preNodeId+"$$$$$$$$$$$$");
		for (Iterator<Number> iterator = adjNodeSet.iterator(); iterator.hasNext();) {
			adjNodeId = (Number) iterator.next();
			////D.p("@@@"+preNodeId);
			if(parent.get(preNodeId) == null || !parent.get(preNodeId).contains(adjNodeId)){
				//preNode的父节点不包含此节点
				//num += accumulateChildNodes(net, adjNodeId, nodeNum, parent);
				//加上自己的子节点
				//num++;
				temp2 = this.getNodeBetweennessByRecursion(net, adjNodeId, rootId, nodeBetweenness, parent, visit, s);
//				//D.p(preNodeId+"####"+visit.get(preNodeId)+"####"+visit.get(adjNodeId)+"####"+adjNodeId+"#####"+temp2);
				temp1 += ((double)visit.get(preNodeId)/visit.get(adjNodeId))*(1+temp2);
//				//D.p("@@@@"+temp1);
			}
		}
		//加上自己的子节点
		//num += adjNodeSet.size()-parent.get(preNodeId).size();
		//找到了节点preNodeId的从属字节点数量，
		//nodeNum.put(preNodeId, num);
		if(!s.contains(preNodeId)){
			if(nodeBetweenness.get(preNodeId) != null){
				//temp2 = nodeBetweenness.get(adjNodeId)+((double)visit.get(adjNodeSet)/visit.get(preNodeId))*(1+nodeBetweenness.get(preNodeId));
				double tempBetweenness =  nodeBetweenness.get(preNodeId)+temp1;
				nodeBetweenness.put(preNodeId,tempBetweenness);
				if(tempBetweenness > this.netBetweennessMaxValue){
					this.netBetweennessMaxValue = tempBetweenness;
					this.netBetweennessMaxId = preNodeId;
				}
			}else{
			//temp2 = ((double)visit.get(adjNodeSet)/visit.get(preNodeId))*(1+nodeBetweenness.get(preNodeId));
			//temp2 = ((double)visit.get(adjNodeSet)/visit.get(preNodeId))*(1+nodeBetweenness.get(preNodeId));
				nodeBetweenness.put(preNodeId, temp1);
				if(temp1 > this.netBetweennessMaxValue){
					this.netBetweennessMaxValue = temp1;
					this.netBetweennessMaxId = preNodeId;
				}
			}
			s.add(preNodeId);
		}
		return temp1;
	}
	@SuppressWarnings("unchecked")
	private Map<String, Object> betweenArgs(){
		Map<String, Object> args = new HashMap<String, Object>();
		Map<String, Object>  netArgs= null;
		Set<Number> nodeSet = net.getAllNodeId();
		//HashMap<Number, Integer> nodeBetweenness = new HashMap<Number, Integer>();//某个节点的介数
		HashMap<Number, Double> nodeBetweenness = new HashMap<Number, Double>();//某个节点的介数
		HashMap<Number, Integer> nodeNum = new HashMap<Number, Integer>();//v节点的从属节点
		HashMap<Number, Integer> visit = null;
		
		 
		int temp = 0;
		Number nodeId = null;
		Number nodeId2 = null;
		for (Iterator<Number> iterator = nodeSet.iterator(); iterator.hasNext();) {
			nodeId = (Number) iterator.next();
			netArgs = this.shortestPathNet(net, nodeId);
			//accumulateChildNodes((Network)netArgs.get("network"), nodeId, nodeNum, (HashMap<Number, LinkedList<Number>>)netArgs.get("parent"));
			HashSet<Number> s = new HashSet<Number>();
			s.add(nodeId);
			getNodeBetweennessByRecursion((Network)netArgs.get("network"), nodeId, nodeId, nodeBetweenness, (HashMap<Number, LinkedList<Number>>)netArgs.get("parent"), (HashMap<Number, Integer>)netArgs.get("visit"),s);
		}
		return args;
	}
	
}
