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

/**
 * NearestNeighborCoupledNet.java
 * 最近邻耦合网络
 *@author 王进法<Mervin.Wong>
 *@version 
 *@Date 2013-1-31上午10:43:26
 */
/*********************************************************************************
 *
 * 最近邻耦合网络
 *
 **********************************************************************************/

public class NearestNeighborCoupledNet extends NetModel {

	private int startNodeId = 1;//起始节点的ID
	private int addNodeNum = 1; //增加节点的数量
	private int k = 2;//k为偶数
	
	public NearestNeighborCoupledNet(){
		//初始化空网络
		super();
	}
	/**
	 *  set
	 *  设置网络参数
	 * @param addNodeNum
	 * @param k
	 */
	public void set(int addNodeNum, int k){
		this.addNodeNum = addNodeNum;
		this.k = k;
	}
	/**
	 *  set
	 *  设置网络参数
	 * @param startNodeId
	 * @param addNodeNum
	 * @param k
	 */
	public void set(int startNodeId, int addNodeNum, int k){
		this.startNodeId = startNodeId;
		this.addNodeNum = addNodeNum;
		this.k = k;
	}
	
	/**
	 *  createModelNetwork
	 *  创建模型网络
	 * @see me.mervin.core.NetModel#createModelNetwork()
	 */
	@Override
	public void createModelNetwork() {
		// TODO Auto-generated method stub
		int endNodeId = this.startNodeId+this.addNodeNum-1;
		int preNodeId = 0;
		for(preNodeId = this.startNodeId; preNodeId <= endNodeId; preNodeId++){
			this.insertNode(preNodeId);
		}
		
		//添加紧邻边
		Node preNode = null, postNode = null, node = null;
		Collection<Node> c = this.topology.values();
		for (Iterator<Node> iterator = c.iterator(); iterator.hasNext();) {
			preNode = postNode = node = (Node) iterator.next();
			for(int i = 1; i <= (int)k/2; i++){
				preNode = this.getPreNode(preNode.getNodeId());
				postNode = this.getPostNode(postNode.getNodeId());
				D.p(node.getNodeId()+"##"+preNode.getNodeId()+"##"+postNode.getNodeId());
				this.insertEdge(node.getNodeId(), preNode.getNodeId());
				this.insertEdge(node.getNodeId(), postNode.getNodeId());
			}
		}
	}

}
