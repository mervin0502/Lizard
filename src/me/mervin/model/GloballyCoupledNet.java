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

import me.mervin.core.NetModel;

/**
 * GloballyCoupledNet.java
 * 全局耦合网络
 *@author 王进法<Mervin.Wong>
 *@version 0.1.0
 *@Date 2013-1-31上午10:10:11
 */
/*********************************************************************************
 *
 * 
 *
 **********************************************************************************/

public final class GloballyCoupledNet extends NetModel {

	private int addNodeNum = 0; 
	private int startNodeId = 1;
	
	public GloballyCoupledNet(){
		super();
	}
	/**
	 *  set
	 *  设置模型参数
	 * @param addNodeNum
	 */
	public void set(int addNodeNum){
		this.addNodeNum = addNodeNum;
	}
	/**
	 *  set
	 *  设置模型参数
	 * @param startNodeId 起始节点的ID
	 * @param addNodeNum
	 */
	public void set(int startNodeId, int addNodeNum){
		this.startNodeId = startNodeId;
		this.addNodeNum = addNodeNum;
	}
	/**
	 *  createModelNetwork
	 *  创建模型网络
	 * @see me.mervin.core.NetModel#createModelNetwork()
	 */
	@Override
	public void createModelNetwork() {
		// TODO Auto-generated method stub
		int endNodeId = startNodeId+addNodeNum-1;
		for(int preNodeId = startNodeId; preNodeId <= endNodeId; preNodeId++){
			for(int postNodeId = preNodeId+1; postNodeId <= endNodeId; postNodeId++){
				this.insertEdge(preNodeId, postNodeId);
			}
		}
	}

}
