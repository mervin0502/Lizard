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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import me.mervin.core.Network;

/**
 * ClusterCofficient.java
 * 
 *@author 王进法<Mervin.Wong>
 *@version 
 *@Date 2013-1-13上午10:46:38
 */
/*********************************************************************************
 *
 *
 *
 **********************************************************************************/

public class ClusterCofficient{
	private Network net = null;
	
	public ClusterCofficient(){
		
	}
	public ClusterCofficient(Network net){
		this.net = net;
	}

	public void set(Network net){
		this.net = net;
	}
	public Network get(){
		return this.net;
	}
	/*********************************************************************************
	 *
	 * 基本函数
	 *
	 **********************************************************************************/
	/**
	 *  
	 *  节点的聚类系数
	 * @param nodeId
	 * @return float  返回值为0表示节点不存在或者节点的邻接点只有一个节点
	 */
	public double nodeClusterCofficient (Number nodeId) {
		// TODO Auto-generated method stub
		return this.nodeClusterCofficient(net, nodeId);
	}
	
	/**
	 *  
	 *  节点的聚类系数:
	 * @param net
	 * @param nodeId
	 * @return float 返回值为0表示节点不存在或者节点的邻接点只有一个节点
	 */
	
	public double nodeClusterCofficient(Network net, Number nodeId) {
		// TODO Auto-generated method stub
		double nodeCC = 0;
		if(net.isHasNode(nodeId)){
			Set<Number> adjNodesId = net.getAdjNodeId(nodeId);
			int nodeCount = adjNodesId.size();
			if(nodeCount > 1){
				Number firstNodeId = null;
				Number secondNodeId =null;
				int i = 1, j;
				int edgeCount = 0;
				for (Iterator<Number> iterator = adjNodesId.iterator(); iterator.hasNext();) {
					firstNodeId = (Number) iterator.next();
					j = 1;
					for (Iterator<Number> iterator2 = adjNodesId.iterator(); iterator2.hasNext();) {
						secondNodeId = (Number) iterator2.next();
						if(j > i){
							if(net.isHasEdge(firstNodeId, secondNodeId)){
								edgeCount++;
							}
						}
						j++;
					}
					i++;
				}
				nodeCC = edgeCount/((float)nodeCount*(nodeCount-1)/2);					
			}
		}
		return nodeCC;
	}

	/**
	 *  
	 *  多个节点的聚类系数
	 * @param nodesId
	 * @return Map<Number, Number> 
	 */
	
	public Map<Number, Number> nodeClusterCofficient(Set<Number> nodesId) {
		// TODO Auto-generated method stub
		Map<Number, Number> cc = new HashMap<Number, Number>();
		Number nodeId = null;
		for (Iterator<Number> iterator = nodesId.iterator(); iterator.hasNext();) {
			nodeId = (Number) iterator.next();
			cc.put(nodeId, this.nodeClusterCofficient(nodeId));
		}
		return cc;
	}
	
	/**
	 *  
	 *  多个节点的聚类系数
	 * @param net
	 * @param nodesId
	 * @return HashSet<Number> 
	 */
	
	public Map<Number, Number> nodeClusterCofficient(Network net, Set<Number> nodesId) {
		// TODO Auto-generated method stub
		Map<Number, Number> cc = new HashMap<Number, Number>();
		Number nodeId = null;
		for (Iterator<Number> iterator = nodesId.iterator(); iterator.hasNext();) {
			nodeId = (Number) iterator.next();
			cc.put(nodeId, this.nodeClusterCofficient(net, nodeId));
		}
		return cc;
	}

	/**
	 *  
	 *  网络的聚类系数
	 * @return float 
	 */
	
	public double netClusterCoefficient() {
		// TODO Auto-generated method stub
		Set<Number> nodesId = this.net.getAllNodeId();
		int nodeNum = this.net.nodeNum;
		double cc = 0;
		Number nodeId = null;
		for (Iterator<Number> iterator = nodesId.iterator(); iterator.hasNext();) {
			nodeId = (Number)iterator.next();
			cc += this.nodeClusterCofficient(nodeId);
		}
		return cc/nodeNum;
	}
	
	/**
	 *  
	 *  网络的聚类系数
	 * @param net
	 * @return float 
	 */
	
	public float netClusterCoefficient(Network net) {
		// TODO Auto-generated method stub
		Set<Number> nodesId = net.getAllNodeId();
		int nodeNum = net.nodeNum;
		float cc = 0;
		for (Iterator<Number> iterator = nodesId.iterator(); iterator.hasNext();) {
			cc += this.nodeClusterCofficient(net, (Number)iterator.next());
		}
		return cc/nodeNum;
	}

}
