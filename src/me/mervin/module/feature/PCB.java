package me.mervin.module.feature;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

import me.mervin.core.Network;
import me.mervin.core.Global.NetType;
import me.mervin.core.Global.NumberType;
import me.mervin.util.D;
import me.mervin.util.FileTool;


/**
 * 
 *   PCB.java
 *  <li>path length, closeness, betweenness</li>
 *  <li>edge without weight</li>   
 *  @author Mervin.Wong  DateTime 2013-8-22 下午2:16:00    
 *  @version 0.4.0
 */
public class PCB{
	
	//private Map<Number, Integer> distance = null;

	private Network net = null;
	public PCB(){
		
	}
	public PCB(Network net){
		this.net = net;
	}

	/*
	 * 源节点到其他所有节点的最短路径
	 * 没有权重
	 */
	public Map<Number, Number> shortestPath(Network net, Number preNodeId){
		Map<Number, Number> distance = new HashMap<Number, Number>();//从源节点到其他节点的跳数
		
		Set<Number> visited = new HashSet<Number>();//已访问的节点
		Queue<Number> queue = new LinkedList<Number>();//已访问未遍历的节点
		visited.add(preNodeId);
		queue.offer(preNodeId);
		queue.offer(-1);
		
		Number nodeId = null, adjNodeId = null;
		long level = 0;//层数
		Set<Number> adjNodeIdSet = null;
		//广度优先搜索
		while(!queue.isEmpty()){
			//遍历栈中的节点，访问它们的邻接点
			nodeId = queue.poll();
			
			
			if(nodeId.equals(-1)){
				if(queue.size() > 0){
					level++;
					queue.offer(-1);
					continue;
				}else{
					break;
				}
			}
			
			
			//遍历邻居节点
			adjNodeIdSet = net.getAdjNodeId(nodeId);
			for(Iterator<Number> it = adjNodeIdSet.iterator();it.hasNext();){
				adjNodeId = it.next();
				if(!visited.contains(adjNodeId)){
					visited.add(adjNodeId);
					queue.offer(adjNodeId);
					distance.put(adjNodeId, level+1);
				}
			}//for
		}//while
		
		return distance;
	}

	
	/*
	 * 源节点到其他所有节点的最短路径网络
	 * 从s->t,可能存在多条最短路径
	 * 没有权重
	 */
/*	public Map<Number, Integer> _shortestPathNet(Network net, Number preNodeId){

	}*/
	
	private Map<Number, Integer> _betweenness(Network net, Number srcNodeId, Map<Number, Double> betweenness){
	 	Map<Number, Integer> distance = null;//路径长度
	 	Map<Number, Integer> count = null;//最短路径中，经过v的次数
		
	 	Map<Number, Set<Number>> parents = null;//节点v的父节点 
	
	 	Set<Number> parent = null;
		Set<Number> allAdjNodeId = null;//所有邻接点
		Stack<Number> stack = null;
		
		Queue<Number> queue = null;
		
		Number nodeId = null, adjNodeId = null;
		stack = new Stack<Number>();
		distance = new HashMap<Number, Integer>();//路径长度
		count = new HashMap<Number, Integer>();//最短路径中，经过v的次数
		parents = new HashMap<Number, Set<Number>>();//父节点
		
		distance.put(srcNodeId, 0);
		count.put(srcNodeId, 1);
		
		queue = new LinkedList<Number>();
		queue.offer(srcNodeId);
		
		
		while(!queue.isEmpty()){
			nodeId = queue.poll();
			stack.push(nodeId);
			allAdjNodeId = net.getAdjNodeId(nodeId);
			for(Iterator<Number> it = allAdjNodeId.iterator(); it.hasNext();){
				adjNodeId = it.next();
				//adjNodeId第一次遍历到
				if(!distance.containsKey(adjNodeId)){
					queue.offer(adjNodeId);
					distance.put(adjNodeId, distance.get(nodeId)+1);
				}
				//最短路径 nodeId->adjNodeId
				if(distance.get(adjNodeId).equals(distance.get(nodeId)+1)){
					if(count.containsKey(adjNodeId)){
						count.put(adjNodeId, count.get(adjNodeId)+count.get(nodeId));
						parents.get(adjNodeId).add(nodeId);
					}else{
						count.put(adjNodeId, count.get(nodeId));
						parent = new HashSet<Number>();
						parent.add(nodeId);
						parents.put(adjNodeId, parent);
					}//if
				}//if
			}//for
		}//while
		
		
		
		Map<Number, Double> temp = new HashMap<Number, Double>();
		Number parentNodeId = null;
		double i = 0, j = 0, k = 0;
		while(!stack.isEmpty()){
			nodeId = stack.pop();
			i = temp.containsKey(nodeId)?temp.get(nodeId):0;
			if(parents.containsKey(nodeId)){
				for(Iterator<Number> it = parents.get(nodeId).iterator(); it.hasNext();){
					parentNodeId = it.next();
					j = temp.containsKey(parentNodeId)?temp.get(parentNodeId):0;
					k = ((double)count.get(parentNodeId)/count.get(nodeId))*(1+i);
					temp.put(parentNodeId, j+k);
					
				}//for
			}
			if(!nodeId.equals(srcNodeId)){
				betweenness.put(nodeId, i);
			}
		}//while
		return distance;
	}
	
	/*
	 * 在已经取得源点到其他所有节点的最短路径的基础之上，求该源点的紧密度
	 */
	private Double _closeness(Map<Number, Integer> distance){
		double sum = 0;
		for(Iterator<Integer> it = distance.values().iterator(); it.hasNext();){
			sum += it.next();
		}
		return 1/sum;
	}

	/**
	 * 
	 * 平均路径长度 紧密度 节点介数 
	 *  @param net
	 */
	public void script(Network net, String dstDir, String string) {
		Map<Number, Number> closeness = new HashMap<Number, Number>();//紧密度
		Map<Number, Number> allBetweenness = new HashMap<Number, Number>();//介数
		Map<Number, Double> nodeBetweenness = null;//介数
		Map<Number, Integer> distance = null;//路径长度
		
		Set<Number> allNodeId = net.getAllNodeId();
		
		double  sum = 0;//
		long pathLengthSum = 0;
		long pathCount = 0;
		long length = 0;
		long diameter = 0;//网络直径
		Number nodeId2 = 0;
		for(Number nodeId:allNodeId){
			nodeBetweenness = new HashMap<Number, Double>();//介数
			distance = this._betweenness(net, nodeId, nodeBetweenness);
			
			sum = 0;
			for(Iterator<Number> it = nodeBetweenness.keySet().iterator(); it.hasNext();){
				nodeId2 = it.next();
				if(allBetweenness.containsKey(nodeId2)){
					allBetweenness.put(nodeId2, allBetweenness.get(nodeId2).doubleValue()+nodeBetweenness.get(nodeId2)/2);
				}else{
					allBetweenness.put(nodeId2, nodeBetweenness.get(nodeId2)/2);
				}
				length = distance.get(nodeId2);
				sum += length;
				//网络直径
				if(diameter < length){
					diameter = length;
				}
			}
			closeness.put(nodeId, 1/sum);
			pathLengthSum +=sum;
			pathCount += nodeBetweenness.size();
		}
		
		FileTool ft = new FileTool();
		ft.write(allBetweenness, dstDir+string+"-betweenness.txt");
		ft.write(closeness, dstDir+string+"-closeness.txt");
		ft.write(string+"\t"+((double)pathLengthSum/pathCount)+"\r\n", dstDir+"apl.txt", true);
		ft.write(diameter+"\r\n", dstDir+"diameter.txt", true);
		D.p("path:"+pathCount);
	}
	
	
/*	public static void main(String[] args){
		Network net = new Network("../data/test/48.txt", NetType.UNDIRECTED, NumberType.INTEGER);
		D.p(net.edgeNum+"###"+net.nodeNum);
		//new PCB().script(net, "../data/test/t2/", 3+"");
	}*/
	
}
