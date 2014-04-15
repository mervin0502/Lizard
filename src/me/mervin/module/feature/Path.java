package me.mervin.module.feature;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

import me.mervin.core.Network;
//import util.D;
import me.mervin.util.D;
import me.mervin.util.FibonacciHeap;
import me.mervin.util.Link;
import me.mervin.util.MathTool;
import me.mervin.util.PairList;
import me.mervin.util.other.INode;


/**
 * 
 * Y1，有权中的使用Dijsktra算法
 * N2，无权的使用BFS算法
 * 
 * +++++++++++++++++++++++++++++++
 * ChangLog:
 * ==2013/10/16
 * # method: nodeShortestPath()
 */

public class Path {

	//private Network net = null;
	
	public Path(){
		
	}
/*	public Path(Network net){
		this.net = net;
	}*/
	/**
	 * 
	 *  bothNodesPath
	 *  计算两个节点间的路径，所有的路径
	 * @param preNodeId
	 * @param postNodeId
	 * @return ArrayList<ArrayList<Number>> path 保存节点的ID
	 * YEN算法，k短路径
	 */
	//@SuppressWarnings("unchecked")
	public ArrayList<Link> bothNodePath(Network net, Number preNodeId, Number postNodeId){
		/*
		 * 在此函数中假设节点的id不能为-1
		 */
		ArrayList<Link> pathList = new ArrayList<Link>();//s->的所有路径
		Stack<Number> tempPath = new Stack<Number>();//存放从源点到当前节点的路径。
		
		Stack<Number> stack = new Stack<Number>();//存储遍历的节点
		stack.push(preNodeId);
		
		Number nodeId = null;
		Set<Number> nodeAdjSet = new HashSet<Number>();
		
		Link link = null;
		while(!stack.empty()){
			nodeId = stack.pop();
			/**
			 * －1作为标记位，如果栈弹出的是-1，那么路径就回退一个元素
			 */
			if(nodeId.equals(-1)){
				tempPath.pop();
				continue;
			}
			
			tempPath.push(nodeId);
			
			if(nodeId.equals(postNodeId)){
				//输出次路径
				//pathList.add((Stack<Number>) tempPath.clone());
				link = new Link(preNodeId, postNodeId);
				Number tempId = null;
				for(Iterator<Number> it = tempPath.iterator(); it.hasNext();){
					tempId = (Number) it.next();
					link.add(tempId);
				}
				
				//弹出刚刚压缩栈的nodeId=postNodeId
				tempPath.pop();
				continue;
			}
			nodeAdjSet = net.getAdjNodeId(nodeId);//获取此点的邻接点
			stack.push(-1);
			for (Iterator<Number> iterator = nodeAdjSet.iterator(); iterator.hasNext();) {
				nodeId= (Number) iterator.next();
				//过滤掉回路
				//保证路径中的节点是唯一的
				if(!nodeId.equals(preNodeId) && !tempPath.contains(nodeId)){
					stack.push(nodeId);
				}
			}
		}
		return pathList;
	}
	
	
	/**
	 *  shortestPathNet
	 *  从一个节点到所有节点的最短路径网
	 * @param net
	 * @param preNodeId
	 * @return  HashMap<Number, Integer> 有preNodeId->v的层数 
	 */
	public Map<Number, Integer> nodeShortestPathNet(Network net, Number preNodeId){
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
		
		Number nodeId = null;
		//int curLevel = 0;//当前的层数
		
		
		Set<Number> adjNodesId = null;
		Number adjNodeId = null;
		double weight = 0;
		//int maxLevel = 0;//最短路径中的最大边数
		/**
		 * 采用广度优先遍历网络中的节点，获取节点在网络中的最小层并且插入到堆上
		 */

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
				adjNodeId = iterator.next();
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
	
	
	/**
	 * 
	 *  网络的平均路径长度
	 * @param net
	 * @return double
	 */
	public double netAvgPathLength(Network net){
		Map<Number, Integer> distance = null;//源点s到达Number的距离
		Set<Number> nodesId = net.getAllNodeId();
		Number nodeId1 = null, nodeId2 = null;
		long sum = 0, count = 0, len = 0;
		for (Iterator<Number> iterator = nodesId.iterator(); iterator.hasNext();) {
			nodeId1 = iterator.next();
			//distance = this.shortestPathNet(net, nodeId1);
			distance = this._shortestPathWithoutWeight(net, nodeId1);
			//if(distance.size() > 0)
			for (Iterator<Number> iterator2 = distance.keySet().iterator(); iterator2.hasNext();) {
				nodeId2 = iterator2.next();
				len = distance.get(nodeId2);
				sum += len;
			}
			count += distance.size();
		}
		return (double)sum/count;
	}
	
	/**
	 *  
	 *  只需寻找有源点s->postNdoeId的一条最短路径即可
	 * @param net
	 * @param preNodeId
	 * @return HashMap<Number, Number>
	 */
	public HashMap<Number, Number> nodeDependencyInSinglePath(Network net, Number preNodeId){
		preNodeId = MathTool.str2Number(Network.getNumberType(), preNodeId.toString());
		
		
		HashMap<Number, Number> parent = new HashMap<Number, Number>();//前驱节点
		HashMap<Number, Double> distance = new HashMap<Number, Double>();//源点s到达Number的距离
		HashSet<Number> s = new HashSet<Number>();//已遍历的节点
		Set<Number> q = net.getAllNodeId();//未遍历的节点
		
		Number nodeId = null;
		FibonacciHeap<Number> heap = new FibonacciHeap<Number>();
		double weight = 0;
		
		/**
		 * 初始化
		 */
		Iterator<Number> it = q.iterator();
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
		
		Set<Number> adjNodes = null;
		Number adjNodeId = null;
		double edgeWeight = 0;
		
		INode<Number> node = null;
		/**
		 * Dijsktra算法
		 */
		while(heap.elCount > 0){
			node = heap.extractMin();
			nodeId = node.value();
			s.add(nodeId);
			adjNodes = net.getAdjNodeId(nodeId);
			for (Iterator<Number> iterator = adjNodes.iterator(); iterator.hasNext();) {
				adjNodeId = iterator.next();
				if(!s.contains(adjNodeId)){
					edgeWeight = net.getEdgeWeight(nodeId, adjNodeId);
					heap.decreseKey(heap.getNode(adjNodeId), edgeWeight);
					
					weight = distance.get(nodeId)+edgeWeight;
					if(distance.get(adjNodeId) > weight){
						distance.put(adjNodeId, weight);
						parent.put(adjNodeId, nodeId);
					}
				}
			}
		}
		return parent;	
	}
	/**
	 *  
	 *  两个节点间的一条最短路径
	 * @param net
	 * @param preNodeId
	 * @param postNodeId
	 * @return Link
	 */
	public Link nodeShortestPath(Network net, Number preNodeId, Number postNodeId){
		Map<Number, Number> parent = this.nodeDependencyInSinglePath(net, preNodeId);
		
		Number temp = null;
		Link link = null;
		Number nodeId = null;
		if(parent.containsKey(postNodeId)){
			if(MathTool.compare(preNodeId, nodeId, Network.getNumberType())){
				link = new Link(nodeId, preNodeId);
			}else{
				link = new Link(preNodeId, nodeId);
			}
			link.add(nodeId);
			temp = parent.get(nodeId);
			
			while(!temp.equals(preNodeId)){
				link.add(temp);
				temp = parent.get(temp);
			}
			link.add(preNodeId);
		}
		return link;
	}
	/**
	 *  
	 *  源节点到其他所有节点的路径
	 * @param net
	 * @param preNodeId
	 * @return Set<Link>
	 */
	public Set<Link<Number>> nodeShortestPath(Network net, Number preNodeId){
		preNodeId = MathTool.str2Number(Network.getNumberType(), preNodeId.toString());
		//postNodeId = MathTool.str2Number(Network.getNumberType(), postNodeId.toString());
		
		
		HashMap<Number, Number> parent = new HashMap<Number, Number>();//前驱节点
		HashMap<Number, Double> distance = new HashMap<Number, Double>();//源点s到达Number的距离
		HashSet<Number> s = new HashSet<Number>();//已遍历的节点
		Set<Number> q = net.getAllNodeId();//未遍历的节点
		
		Number nodeId = null;
		FibonacciHeap<Number> heap = new FibonacciHeap<Number>();
		double weight = 0;
		
		/**
		 * 初始化
		 */
		Iterator<Number> it = q.iterator();
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
		
		Set<Number> adjNodes = null;
		Number adjNodeId = null;
		double edgeWeight = 0;
		
		INode<Number> node = null;
		/**
		 * Dijsktra算法
		 */
		while(heap.elCount > 0){
			node = heap.extractMin();
			nodeId = node.value();
			s.add(nodeId);
			////D.p("extract"+nodeId);
			adjNodes = net.getAdjNodeId(nodeId);
			for (Iterator<Number> iterator = adjNodes.iterator(); iterator.hasNext();) {
				adjNodeId = iterator.next();
				if(!s.contains(adjNodeId)){
					edgeWeight = net.getEdgeWeight(nodeId, adjNodeId);
					//heap.decreaseNode(adjNodeId, edgeWeight);//修改节点的值
					heap.decreseKey(heap.getNode(adjNodeId), edgeWeight);
					
					weight = distance.get(nodeId)+edgeWeight;
					if(distance.get(adjNodeId) > weight){
						distance.put(adjNodeId, weight);
						parent.put(adjNodeId, nodeId);
					}
				}
			}
		}
		//PairSet：表示链路的起点和终点，LinkedList<Number>:链路上的点
		Set<Link<Number>> links = new HashSet<Link<Number>>();
		Number temp = null;
		Link<Number> link = null;
		for(it = net.getAllNodeId().iterator(); it.hasNext();){
			nodeId = it.next();
			int i = 0;
			if(parent.containsKey(nodeId)){
/*				if(MathTool.compare(preNodeId, nodeId, Network.getNumberType())){
					link = new Link(nodeId, preNodeId);
				}else{
					link = new Link(preNodeId, nodeId);
				}*/
				link = new Link<Number>(preNodeId, nodeId);
				link.addFirst(nodeId);
				temp = parent.get(nodeId);
				
				while(!temp.equals(preNodeId)){
					link.addFirst(temp);
					temp = parent.get(temp);
					i++;
				}
				i++;
				link.addFirst(preNodeId);
				//link.setLength(i);
				links.add(link);
			}
		}
		return links;	
	}
	/*
	 * 源节点到其他所有节点的最短路径
	 * 没有权重
	 */
	@SuppressWarnings("unused")
	private Map<Number, Integer> _shortestPathWithoutWeight(Network net, Number preNodeId){
		Map<Number, Integer> distance = new HashMap<Number, Integer>();//从源节点到其他节点的跳数
		
		//D.p(preNodeId);
		//Set<Number> allNodeId = net.getAllNodeId();//所有的节点
		Set<Number> visited = new HashSet<Number>();//已访问的节点
		Queue<Number> visiting = new LinkedList<Number>();//已访问未遍历的节点
		visited.add(preNodeId);
		visiting.offer(preNodeId);
		visiting.offer(-1);
		
		Number nodeId = null, adjNodeId = null;
		int level = 0;//层数
		Set<Number> adjNodeIdSet = null;
		//广度优先搜索
		while(!visiting.isEmpty()){
			//遍历栈中的节点，访问它们的邻接点
			nodeId = visiting.poll();
			
			if(!nodeId.equals(-1)){
				//已遍历
				//visited.add(nodeId);
			}else if(visiting.size() > 0){
				//进入新的一层
				//D.m();
				level++;
				//if(visiting.size() >= 1){
				visiting.offer(-1);
				//}
				continue;
			}else{
				break;
			}
			
			//遍历邻居节点
			adjNodeIdSet = net.getAdjNodeId(nodeId);
			for(Iterator<Number> it = adjNodeIdSet.iterator();it.hasNext();){
				adjNodeId = it.next();
				if(!visited.contains(adjNodeId)){
					visited.add(adjNodeId);
					visiting.offer(adjNodeId);
					distance.put(adjNodeId, level+1);
				}
			}//for
		}//while
		
		return distance;
	}
	
	
	/**
	 *  
	 *  全网的最短路径集合
	 * @param net
	 * @return Set<Link>
	 */
	public Set<Link> netShortestPath(Network net){
		Set<Number> nodesSet = net.getAllNodeId();
		Set<Link> links = new HashSet<Link>();
		
		Number nodeId = null;
		for (Iterator<Number> iterator = nodesSet.iterator(); iterator.hasNext();) {
			nodeId = (Number) iterator.next();
			links.addAll(this.nodeShortestPath(net, nodeId));
		}
		
		return links;
	}
/*	public static void main(String[] args){
		Network net = new Network("../data/20131.txt", NetType.UNDIRECTED);
		Path p = new Path();
		//D.p(p.netAvgPathLength(net));
		//p.bothNodesShortestPath(net, 1);
		//p.netShortestPath(net);
		D.m();
		////D.p(p.netDiameter());
		//p.shortestPathNet(net, 6);
	}*/
}