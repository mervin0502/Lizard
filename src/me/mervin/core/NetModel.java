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
package me.mervin.core;

import me.mervin.core.Global.NetType;
import me.mervin.core.Global.NumberType;

/**
 * AbstractNetworkModel.java
 * 网络模型的抽象类
 * @author 王进法<Mervin.Wong>
 * @version 0.4.1
 * ***********************************************
 * ChangLog:
 * == 2013/10/18
 * # class: name->NetModel
 * == 2013/10/17
 * + method: NetModel(NetType netType)
 *  2013/10/3
 *  + NetModel(int initNodeNum, NetType netType) 
 */
public abstract class NetModel extends Network{

	/**
	 * 初始化一个空的网络模型
	 */
	public NetModel(){
		super();
	}
	/**
	 *  初始化一个空的网络模型:网络类型为netType
	 * @param netType
	 */
	public NetModel(NetType netType){
		super(netType);
	}
	/**
	 * 初始化一个全连通网络模型
	 * @param initNodeNum 网络模型的节点数
	 */
	public NetModel(int initNodeNum){
		super(initNodeNum);
	}
	/**
	 * 初始化一个全连通网络模型
	 * @param initNodeNum 网络模型的节点数
	 * @param nettype 网络的类型
	 */
	public NetModel(int initNodeNum, NetType netType){
		super(initNodeNum, netType);
	}
	/**
	 * 初始化已给出的网络模型
	 * @param netFile 网络文件
	 * @param netType 网络类型:有向，无向
	 */
	public NetModel(String netFile, NetType netType){
		super(netFile, netType);
	}	
	/**
	 * 初始化已给出的网络模型
	 * @param netFile 网络文件
	 * @param netType 网络类型：有向，无向
	 * @param numberType 节点ID类型
	 */
	public NetModel(String netFile, NetType netType, NumberType numberType){
		super(netFile, netType, numberType);
	}
	/**
	 *  createModelNetwork
	 *  构造模型网络
	 */
	public abstract void createModelNetwork();
		
}
