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
package me.mervin.model;

import java.util.Collection;
import java.util.Iterator;

import me.mervin.core.NetModel;
import me.mervin.core.Node;
import me.mervin.core.Global.NetType;
import me.mervin.util.D;
import me.mervin.util.MathTool;
import me.mervin.util.PairList;

/**
 * NWNetwork.java
 * 
 *@author 王进法<Mervin.Wong>
 *@version 0.1.0
 *@Date 2013-1-14上午11:34:56
 */
/*********************************************************************************
 * 
 * 基础网络是最近邻耦合网络
 *
 *********************************************************************************
 */
public class WSNetwork extends NetModel {

	private int startNodeId = 1;
	private int addNodeNum = 0;//最近邻耦合网络的节点数
	private int k = 0; //k是偶数
	private double p = 0; //随机化重连的概率
	
	public WSNetwork(){
		super();
	}
	/**
	 * 构造函数
	 * @param netType 网络类型 有向or无向
	 */
	public WSNetwork(NetType netType){
		super(netType);
	}
	/**
	 *  set
	 *  模型网络参数设置
	 * @param addNodeNum
	 * @param k
	 * @param p
	 */
	public void set(int addNodeNum, int k, double p){
		this.addNodeNum = addNodeNum;
		this.k = k;
		this.p = p;
	}
	public void set(int startNodeId, int addNodeNum, int k, float p){
		this.startNodeId = startNodeId;
		this.addNodeNum = addNodeNum;
		this.k = k;
		this.p = p;
	}
	/**
	 *  createModelNetwork
	 *  由算法来演化网络结构
	 */
	@Override
	public void createModelNetwork() {
		// TODO Auto-generated method stub
		/** 第一步：最近邻耦合网络 */
		int endNodeId = this.startNodeId+this.addNodeNum-1;
		int nodeId = 0;
		for(nodeId = this.startNodeId; nodeId <= endNodeId; nodeId++){
			this.insertNode(nodeId);
		}
		//添加紧邻边
		Node preNode = null, postNode = null, node = null;
		Collection<Node> c = this.topology.values();
		for (Iterator<Node> iterator = c.iterator(); iterator.hasNext();) {
			preNode = postNode = node = (Node) iterator.next();
			for(int i = 1; i <= (int)k/2; i++){
				preNode = this.getPreNode(preNode.getNodeId());
				postNode = this.getPostNode(postNode.getNodeId());
				this.insertEdge(node.getNodeId(), preNode.getNodeId());
				this.insertEdge(node.getNodeId(), postNode.getNodeId());
				
			}
			//D.p(node+"###");
		}
		//D.m();
		/** 第二步：随机化重连 */
	    PairList<Number, Number> edgeList = this.traverseEdge();
	    int len = edgeList.size();
	    Number  preNodeId = null, postNodeId = null, randNodeId = null;
	    int loopNum = 0;
	    for (int i = 0; i < len; i++) {
			if(MathTool.random() <= p){
				//随机的选取一个固定节点
				if(MathTool.random() > 0.5){
					preNodeId = edgeList.getL(i);
					postNodeId = edgeList.getR(i);
				}else {
					preNodeId = edgeList.getR(i);
					postNodeId = edgeList.getL(i);
				}
				//随机选取一个点
				do{
					randNodeId = this.getRandNodeId();
					loopNum++;
					if(loopNum >= Math.sqrt(len)){
						break;
					}
				}while(preNodeId.equals(randNodeId) || this.isHasEdge(preNodeId, randNodeId));
				//D.p(preNodeId+"###"+randNodeId);
				this.insertEdge(preNodeId, randNodeId);
				this.deleteEdge(preNodeId, postNodeId);
			}//if
		}//for
	}
}
