/*****************************************************************************
 * 
 * Copyright [2013] [Mervin.Wong]
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 *       
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *  
 *****************************************************************************/
package me.mervin.module.feature;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import me.mervin.core.Network;
import me.mervin.util.D;
import me.mervin.util.MapTool;
import me.mervin.util.MathTool;
import me.mervin.util.PairList;

/**
 * Degree.java
 * 
 *@author 王进法<Mervin.Wong>
 *@version 0.4
 */
/*
 * ********************************************************************************
 *
 * 关于网络的度的计算：
 *
 **********************************************************************************/

public class Degree{
	private Network net = null;
	
	public Degree(){
		
	}
	public Degree(Network net){
		this.net = net;
	}
	
	public void set(Network net){
		this.net = net;
	}
	
	public Network get(){
		return this.net;
	}
	/*
	 * ********************************************************************************
	 *
	 * 基本功能
	 *
	 **********************************************************************************/
	/**
	 *  
	 *  单个节点的度
	 * @param nodeId
	 * @return int 
	 */
	public int nodeDegree(Number nodeId){
		if(this.net.isHasNode(nodeId)){
			return this.net.getAdjNodeId(nodeId).size();
		}else{
			return 0;
		}
	}
	
	/**
	 *  
	 *  单个节点的度
	 * @param net
	 * @param nodeId
	 * @return int 
	 */
	public int nodeDegree(Network net, Number nodeId){
		return net.getAdjNodeId(nodeId).size();
	}
	
	/**
	 *  
	 *  获取多个节点的度
	 * @param nodeIdSet
	 * @return Map<Number,Number> 
	 */
	public  Map<Number,Number> nodeDegree(Set<Number> nodeIdSet){
		HashMap<Number,Number> degreeMap = new HashMap<Number,Number>();
		for (Iterator<Number> iterator = nodeIdSet.iterator(); iterator.hasNext();) {
			Number nodeId = (Number) iterator.next();
			degreeMap.put(nodeId, this.nodeDegree(nodeId));
			
		}
		return degreeMap;
	}
	
	/**
	 *  
	 *  获取多个节点的度
	 * @param net
	 * @param nodeIdSet
	 * @return Map<Number,Number> 
	 */
	public Map<Number,Number> nodeDegree(Network net, Set<Number> nodeIdSet){
		HashMap<Number,Number> degreeMap = new HashMap<Number, Number>();
		for (Iterator<Number> iterator = nodeIdSet.iterator(); iterator.hasNext();) {
			Number nodeId = (Number) iterator.next();
			degreeMap.put(nodeId, this.nodeDegree(net,nodeId));
			
		}
		return degreeMap;
	}
	
	/**
	 *  
	 *  节点的入度
	 * @param nodeId
	 * @return int 
	 */
	public int nodeInDegree(Number nodeId){
		return this.net.getInDegreeNodeId(nodeId).size();
	}
	
	/**
	 *  
	 *  节点的入度
	 * @param  net
	 * @param nodeId
	 * @return int 
	 */
	public int nodeInDegree(Network net, Number nodeId){
		return net.getInDegreeNodeId(nodeId).size();
	}
	
	/**
	 *  
	 *  多个节点的入度
	 * @param nodeIdSet
	 * @return Map<Number,Number> 
	 */
	public Map<Number,Number> nodeInDegree(Set<Number> nodeIdSet){
		HashMap<Number,Number> degreeMap = new HashMap<Number,Number>();
		for (Iterator<Number> iterator = nodeIdSet.iterator(); iterator.hasNext();) {
			Number nodeId = (Number) iterator.next();
			degreeMap.put(nodeId, this.nodeInDegree(nodeId));
			
		}
		return degreeMap;
	}
	
	/**
	 *  
	 *  多个节点的入度
	 * @param net
	 * @param nodeIdSet
	 * @return Map<Number,Number> 
	 */
	public Map<Number,Number> nodeInDegree(Network net, Set<Number> nodeIdSet){
		HashMap<Number,Number> degreeMap = new HashMap<Number,Number>();
		for (Iterator<Number> iterator = nodeIdSet.iterator(); iterator.hasNext();) {
			Number nodeId = (Number) iterator.next();
			degreeMap.put(nodeId, this.nodeInDegree(net,nodeId));
		}
		return degreeMap;
	} 
	
	/**
	 * 
	 *  单个节点的出度
	 * @param nodeId
	 * @return int 
	 */
	public int nodeOutDegree(Number nodeId){
		return this.net.getOutDegreeNodeId(nodeId).size();
	}
	
	/**
	 * 
	 *  
	 *  单个节点的出度
	 * @param net
	 * @param nodeId
	 * @return int 
	 */
	public int nodeOutDegree(Network net, Number nodeId){
		return net.getOutDegreeNodeId(nodeId).size();
	}
	
	/**
	 *  
	 *  多个节点的出度
	 * @param nodeIdSet
	 * @return Map<Number,Number> 
	 */
	public HashMap<Number,Number> nodeOutDegree(Set<Number> nodeIdSet){
		HashMap<Number,Number> degreeMap = new HashMap<Number,Number>();
		for (Iterator<Number> iterator = nodeIdSet.iterator(); iterator.hasNext();) {
			Number nodeId = (Number) iterator.next();
			degreeMap.put(nodeId, this.nodeOutDegree(nodeId));
		}
		return degreeMap;
	} 
	
	/**
	 *  
	 *  多个节点的出度
	 * @param net
	 * @param nodeIdSet
	 * @return Map<Number,Number> 
	 */
	public Map<Number,Number> nodeOutDegree(Network net, Set<Number> nodeIdSet){
		HashMap<Number,Number> degreeMap = new HashMap<Number,Number>();
		for (Iterator<Number> iterator = nodeIdSet.iterator(); iterator.hasNext();) {
			Number nodeId = (Number) iterator.next();
			degreeMap.put(nodeId, this.nodeOutDegree(net, nodeId));
		}
		return degreeMap;
	}
	
	/**
	 *  
	 *  邻接点的度
	 * @param nodeId
	 * @return  HashMap<Number, Number>
	 */ 
	public Map<Number, Number> nodeAdjDegree(Number nodeId){
		return this.nodeAdjDegree(this.net, nodeId);
	}
	
	/**
	 *  
	 *  邻接点的度
	 * @param net
	 * @param nodeId
	 * @return HashMap<Number, Number>
	 */
	public Map<Number, Number> nodeAdjDegree(Network net, Number nodeId){
		HashMap<Number, Number> degreeMap = new HashMap<Number, Number>();
		if(net.isHasNode(nodeId)){
			Set<Number> adjNodes = net.getAdjNodeId(nodeId);
			Number adjNodeId = null;
			for (Iterator<Number> iterator = adjNodes.iterator(); iterator.hasNext();) {
				adjNodeId = (Number) iterator.next();
				degreeMap.put(adjNodeId, net.getAdjNodeId(adjNodeId).size());
			}
		}
		return degreeMap;
	}
	/**
	 *  
	 *  多个节点的邻接点的度
	 * @param nodeIdSet
	 * @return HashMap<Number, Number>
	 */
	public Map<Number, Number> nodeAdjDegree(Set<Number> nodeIdSet){
		return this.nodeAdjDegree(this.net, nodeIdSet);
	}
	
	/**
	 *  
	 *  多个节点的邻接点的度
	 * @param net
	 * @param nodeIdSet
	 * @return HashMap<Number, Number>
	 */
	public Map<Number, Number> nodeAdjDegree(Network net, Set<Number> nodeIdSet){
		HashMap<Number, Number> degreeMap = new HashMap<Number, Number>();
		Set<Number> adjNodes = null;
		Number adjNodeId = null;
		Number nodeId = null;
		for (Iterator<Number> iterator = nodeIdSet.iterator(); iterator.hasNext();) {
			nodeId = (Number) iterator.next();
			if(net.isHasNode(nodeId)){
				adjNodes = net.getAdjNodeId(nodeId);
				for (Iterator<Number> iterator2 = adjNodes.iterator(); iterator2.hasNext();) {
					adjNodeId = (Number) iterator2.next();
					degreeMap.put(adjNodeId, net.getAdjNodeId(adjNodeId).size());
				}
			}
		}
		return degreeMap;
	}
	/**
	 * 
	 *  获取度为degree的节点集
	 *  @param degree 度值
	 *  @return Set<Number>
	 */
	public Set<Number> getNodeIdByDegree(int degree){
		return this.getNodeIdByDegree(this.net, degree);
	}
	/**
	 * 
	 *  获取度为degree的节点集
	 *  @param net 网络
	 *  @param degree 度值
	 *  @return Set<Number>
	 */
	public Set<Number> getNodeIdByDegree(Network net, int degree){
		HashSet<Number> nodeSet = new HashSet<Number>();//度值为degree的节点集
		Set<Number> allNodeSet = net.getAllNodeId();//所有的节点
		Number nodeId = null;
		for(Iterator<Number> it = allNodeSet.iterator(); it.hasNext();){
			nodeId = it.next();
			if(net.getAdjNodeId(nodeId).size() == degree){
				nodeSet.add(nodeId);
			}
		}
		return nodeSet;
	}
	
	/**
	 * 
	 * 将网络按度的由小到大或由大到小排列，获取网络中，比例为ratio的节点数量 
	 *  @param ratio 获取节点数量的比例
	 *  @param des true:由大到小；false:由小到大
	 *  @return Set<Number>
	 */
	public Set<Number> getNodeIdByRatio(double ratio, boolean des){
		return this.getNodeIdByRatio(net, ratio, des);
	}
	/**
	 * 
	 * 将网络按度的由小到大或由大到小排列，获取网络中，比例为ratio的节点数量 
	 * @param net 网络
	 *  @param ratio 获取节点数量的比例
	 *  @param des true:由大到小；false:由小到大
	 *  @return Set<Number>
	 */
	public Set<Number> getNodeIdByRatio(Network net, double ratio, final boolean des){
		Set<Number> nodeIdSet = new HashSet<Number>();//获取的节点集
		//网络节点与对应的度值
		SortedMap<Number, Number> sortedMap = new TreeMap<Number, Number>();
		sortedMap.putAll(this.nodeDegree(net, net.getAllNodeId()));
		/*
		 * 按照节点的度值来排序网络网络节点
		 */
		SortedSet<Map.Entry<Number, Number>> sortedSet = new TreeSet<Map.Entry<Number, Number>>(
	            new Comparator<Map.Entry<Number, Number>>() {
	                @Override
	                public int compare(Map.Entry<Number, Number> e1, Map.Entry<Number, Number> e2) {
	                    //return -e1.getValue().compareTo(e2.getValue());
	                	int d1 = e1.getValue().intValue();
	                	int d2 = e2.getValue().intValue();
	                	if(des){
	                		//由大到小
		                	if(d1 < d2){
		                		return 1;
		                	/*}else if(d1 == d2){
		                		return 0;*/
		                	}else{
		                		return -1;
		                	}
	                	}else{
	                		//由小到大
		                	if(d1 > d2){
		                		return 1;
		                	/*}else if(d1 == d2){
		                		return 0;*/
		                	}else{
		                		return -1;
		                	}
	                	}

	                }
	            });
		sortedSet.addAll(sortedMap.entrySet());//网络中的节点按度排序
		
		int nodeNum = net.nodeNum;
		int tempNum = (int) Math.ceil(nodeNum*ratio); 
		int i = 1;
		Entry<Number, Number> e = null;
		for(Iterator<Entry<Number, Number>> it = sortedSet.iterator(); it.hasNext() && i <= tempNum; i++){
			e = it.next();
			nodeIdSet.add(e.getKey());
			//D.p(e.getKey()+"###"+e.getValue());
		}
		return nodeIdSet;
	}
	/**
	 *  
	 *  节点集合中度的最大值
	 * @return int
	 */
	public int nodeDegreeMax(Map<Number, Number> degreeMap){
		int max = 0;
		max = MathTool.max(degreeMap.values());
		return max;
	}
	/**
	 *  
	 *  获取网络net的度最大值
	 * @return int
	 */
	public int netDegreeMax(){
		Set<Number> nodeIdSet  = (Set<Number>) this.net.getAllNodeId();
		int max = 0;
		int temp = 0;
		
 		for (Iterator<Number> iterator = nodeIdSet.iterator(); iterator.hasNext();) {
			temp = this.nodeDegree((Number) iterator.next());
			if(temp > max){
				max = temp;
			}
		}
 		return max;
	}
	
	/**
	 *  
	 *  获取网络net的度最大值
	 * @param net
	 * @return int
	 */
	public int netDegreeMax(Network net){
		Set<Number> nodeIdSet  = (Set<Number>) net.getAllNodeId();
		int max = 0;
		int temp = 0;
		
 		for (Iterator<Number> iterator = nodeIdSet.iterator(); iterator.hasNext();) {
			temp = this.nodeDegree(net, (Number) iterator.next());
			if(temp > max){
				max = temp;
			}
		}
 		return max;		
	}
	
	/**
	 *  
	 *  网络的度分布,无向
	 * @return Map<Number,Number> 
	 */
	public Map<Number, Number> netDegreeDistribution(){
/*		Set<Number> nodeIdSet  = (Set<Number>) this.net.topology.keySet();
		int[] degreeArr = new int[this.netDegreeMax()+1];
 		for (Iterator<Number> iterator = nodeIdSet.iterator(); iterator.hasNext();) {
			degreeArr[this.nodeDegree((Number)iterator.next())]++;
		}
 		
 		Map<Number, Number> degreeMap = new HashMap<Number, Number>();
 		for (int i = 0; i < degreeArr.length; i++) {
			if(degreeArr[i] > 0){
				degreeMap.put(i, degreeArr[i]);
			}
		}
		return degreeMap;*/
		return this.netDegreeDistribution(net);
	}
	
	/**
	 *  
	 *  网络的度分布
	 * @param net
	 * @return Map<Number,Number> 
	 */
	public Map<Number, Number> netDegreeDistribution(Network net){
/*		Set<Number> nodeIdSet  = (Set<Number>) net.topology.keySet();
		int[] degreeArr = new int[this.netDegreeMax(net)+1];
 		for (Iterator<Number> iterator = nodeIdSet.iterator(); iterator.hasNext();) {
			degreeArr[this.nodeDegree(net, (Number)iterator.next())]++;
		}
 		
 		Map<Number, Number> degreeMap = new HashMap<Number, Number>();
 		for (int i = 0; i < degreeArr.length; i++) {
			if(degreeArr[i] > 0){
				degreeMap.put(i, degreeArr[i]);
			}
		}
		return degreeMap;*/
		Map<Number, Number> degreeDistribution = new HashMap<Number, Number>();
		Set<Number> nodeIdSet = net.getAllNodeId();
		Number degreeValue = null;
		for(Number nodeId:nodeIdSet){
			degreeValue = this.nodeDegree(net, nodeId);
			if(degreeDistribution.containsKey(degreeValue)){
				degreeDistribution.put(degreeValue, degreeDistribution.get(degreeValue).intValue()+1);
			}else{
				degreeDistribution.put(degreeValue, 1);
			}
		}
		return degreeDistribution;
	}
	
	/**
	 * 网络的入度分布
	 *  
	 *  @return Map<Number, Number>
	 */
	public Map<Number, Number> netInDegreeDistribution(){
		return this.netInDegreeDistribution(this.net);
	}
	/**
	 * 
	 * 网络的入度分布 
	 *  @param net 网络
	 *  @return Map<Number, Number>
	 */
	public Map<Number, Number> netInDegreeDistribution(Network net){
		Map<Number, Number> degreeDistribution = new HashMap<Number, Number>();
		Set<Number> nodeIdSet = net.getAllNodeId();
		Number degreeValue = null;
		for(Number nodeId:nodeIdSet){
			degreeValue = this.nodeInDegree(net, nodeId);
			if(degreeDistribution.containsKey(degreeValue)){
				degreeDistribution.put(degreeValue, degreeDistribution.get(degreeValue).intValue()+1);
			}else{
				degreeDistribution.put(degreeValue, 1);
			}
		}
		return degreeDistribution;
	}
	/**
	 * 网络的出度分布
	 *  
	 *  @return Map<Number, Number>
	 */
	public Map<Number, Number> netOutDegreeDistribution(){
		return this.netOutDegreeDistribution(net);
	}
	/**
	 * 
	 * 网络的出度分布 
	 *  @param net 网络
	 *  @return Map<Number, Number>
	 */
	public Map<Number, Number> netOutDegreeDistribution(Network net){
		Map<Number, Number> degreeDistribution = new HashMap<Number, Number>();
		Set<Number> nodeIdSet = net.getAllNodeId();
		Number degreeValue = null;
		for(Number nodeId:nodeIdSet){
			degreeValue = this.nodeOutDegree(net, nodeId);
			if(degreeDistribution.containsKey(degreeValue)){
				degreeDistribution.put(degreeValue, degreeDistribution.get(degreeValue).intValue()+1);
			}else{
				degreeDistribution.put(degreeValue, 1);
			}
		}
		return degreeDistribution;
	}
	
	/**
	 *  
	 *  网络的度分布率
	 * @return HashMap<Integer,double> 
	 */
	public Map<Number, Number> netDegreeDistributionRatio(){
		Map<Number, Number> degreeMap = this.netDegreeDistribution();
		return MathTool.ratio(degreeMap);
	}
	
	/**
	 *  
	 *  网络的度分布率
	 * @param net
	 * @return HashMap<Integer,double> 
	 */
	public Map<Number, Number> netDegreeDistributionRatio(Network net){
		Map<Number, Number> degreeMap = this.netDegreeDistribution(net);
		return MathTool.ratio(degreeMap);
	}
	
	/**
	 * 
	 *  网络的入度分布率
	 *  @return Map<Number, Number>
	 */
	public Map<Number, Number> netInDegreeDistributionRatio(){
		return this.netInDegreeDistributionRatio(this.net);
	}
	/**
	 * 
	 * 网络的入度分布率 
	 *  @param net 网络
	 *  @return Map<Number, Number>
	 */
	public Map<Number, Number> netInDegreeDistributionRatio(Network net){
		return MathTool.ratio(this.netInDegreeDistribution(net));
	}
	/**
	 * 
	 *  网络的出度分布率
	 *  @return Map<Number, Number>
	 */
	public Map<Number, Number> netOutDegreeDistributionRatio(){
		return this.netOutDegreeDistributionRatio(this.net);
	}
	/**
	 * 
	 * 网络的出度分布率 
	 *  @param net 网络
	 *  @return Map<Number, Number>
	 */
	public Map<Number, Number> netOutDegreeDistributionRatio(Network net){
		return MathTool.ratio(this.netOutDegreeDistribution(net));
	}
	
	/**
	 * 累积度分布
	 * @param net 网络
	 * @return PairList<Number, Number>
	 */
	public PairList<Number, Number> ccdf(Network net){
		Map<Number, Number> degreeDistRatio = this.netDegreeDistributionRatio(net);
		MapTool mt = new MapTool();
		PairList<Number, Number> cumulateDegreeDistRatio = mt.sort(degreeDistRatio, true, true);
		PairList<Number, Number> cumulateDegreeDistRatio2 = new PairList<Number, Number>();
		cumulateDegreeDistRatio2.add(cumulateDegreeDistRatio.getL(0), cumulateDegreeDistRatio.getR(0).doubleValue());
		for(int i = 1; i < cumulateDegreeDistRatio.size(); i++){
			cumulateDegreeDistRatio2.add(cumulateDegreeDistRatio.getL(i), cumulateDegreeDistRatio.getR(i).doubleValue()+cumulateDegreeDistRatio2.getR(i-1).doubleValue());
		}
		return cumulateDegreeDistRatio2;
	}
	
	/**
	 * 累积度分布
	 * @return PairList<Number, Number>
	 */
	public PairList<Number, Number> ccdf(){
		return this.ccdf(this.net);
	}
	/**
	 *  netDegreeAvg
	 *  网络度的平均值
	 * @return double
	 */
	public double netDegreeAvg(){
		Map<Number, Number> degreeMap = this.netDegreeDistribution();
		return MathTool.average(degreeMap);
	}
	
	/**
	 *  
	 *  网络度的平均值
	 * @param net
	 * @return double
	 */
	public double netDegreeAvg(Network net){
		Map<Number, Number> degreeMap = this.netDegreeDistribution(net);
		return MathTool.average(degreeMap);
	}
}
