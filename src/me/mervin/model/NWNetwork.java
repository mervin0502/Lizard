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
import me.mervin.util.D;
import me.mervin.util.MathTool;

/**
 * NWNetwork.java
 * 
 *@author 王进法<Mervin.Wong>
 *@version 0.1.0
 *@Date 2013-2-1下午6:26:57
 */
/*********************************************************************************
 * 
 * 基础网络是最近邻耦合网络
 *
 *********************************************************************************/

public class NWNetwork extends NetModel {

	private int startNodeId = 1; //起始节点的ID
	private int addNodeNum = 0; //节点的数量
	private int k = 2; //k为偶数
	private double p = 0;//以概率p加一条边
	
	public NWNetwork(){
		super();
	}
	
	/**
	 *  set
	 *  设置网络参数
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
	 *  创建模型网络
	 * @see me.mervin.core.NetModel#createModelNetwork()
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
				D.p(node.getNodeId()+"##"+preNode.getNodeId());
				this.insertEdge(node.getNodeId(), preNode.getNodeId());
				this.insertEdge(node.getNodeId(), postNode.getNodeId());
			}
		}
		/** 第二步：随机化加边 */
		Number preNodeId = null, postNodeId = null;
		int flag = 0;//控制结束的条件
		while(true){
			if(MathTool.random() < p){
				do {
					preNodeId = this.getRandNodeId();
					postNodeId = this.getRandNodeId();
					D.m("@@"+preNodeId+"##"+postNodeId);
					flag ++;
					if(flag++ > 100){
						break;
					}
				} while (preNodeId.equals(postNodeId) || this.isHasEdge(preNodeId, postNodeId));
				if(flag > 100){
					break;
				}
				flag = 0;
				D.m(preNodeId+"##"+postNodeId);
				this.insertEdge(preNodeId, postNodeId);
			}
		}
	}

}
