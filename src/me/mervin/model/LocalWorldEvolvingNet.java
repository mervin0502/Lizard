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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import me.mervin.core.NetModel;
import me.mervin.core.Global.NetType;
import me.mervin.core.Global.NumberType;
import me.mervin.module.feature.Degree;
import me.mervin.util.D;
import me.mervin.util.MathTool;

/**
 * LocalWorldEvolvingNet.java
 * 
 *@author 王进法<Mervin.Wong>
 *@version 
 *@Date 2013-2-2下午8:59:21
 */
/*********************************************************************************
 *
 * 局域世界演化模型网络
 *
 **********************************************************************************/

public class LocalWorldEvolvingNet extends NetModel {

	private int addNodeNum = 0; //网络新增节点的数量
	private int M = 0;//局域世界的节点数量
	private int m = 0;//与已有的m个节点相连
	
	private Set<Number> MSet = new HashSet<Number>(); //选择的局域世界节点集合
	ArrayList<Number> MList = null;
	private Number currentNodeId = null;//当前节点的ID
	private Map<Number, Number> degreeMap = null;
	
	private int degreeSum = 0;//M中度的总和
	private int degreeMax = 0;//M中度的最大值
	
	private Degree degree = new Degree(this);
	public LocalWorldEvolvingNet(int initNodeNum){
		super(initNodeNum);
		
	}
	public LocalWorldEvolvingNet(String fileName, NetType netType){
		super(fileName, netType);
		
	}
	public LocalWorldEvolvingNet(String fileName, NetType netType, NumberType numberType){
		super(fileName, netType, numberType);
		
	}
	/**
	 *  setNetParam
	 *  设置模型参数
	 * @param addNodeNum
	 * @param M
	 * @param m
	 */
	public void set(int addNodeNum, int M, int m){
		this.addNodeNum = addNodeNum;
		this.M = M;
		this.m = m;
	}
	/**
	 *  createModelNetwork
	 *  网络模型构造函数
	 * @see me.mervin.core.NetModel#createModelNetwork()
	 */
	@Override
	public void createModelNetwork() {
		// TODO Auto-generated method stub
		Number newNodeId = this.currentNodeId, oldNodeId = null;
		
		if(this.getNumberType().equals(NumberType.LONG)){
			newNodeId = this.maxNodeId.longValue()+1;
		}else{
			newNodeId = this.maxNodeId.intValue()+1;
		}
		
		for (int i = 0; i < addNodeNum; i++) {
			/** 1,产生一个新节点 currentNodeId*/
			/** 2, 在局域世界M中选择与已存在的m个节点相连 */
			for (int j = 0; j < this.m; j++) {
				oldNodeId = this._prioritySelectionFun(newNodeId);
				D.p("Insert:"+newNodeId+"##"+oldNodeId);
				this.insertEdge(newNodeId, oldNodeId);
			}
			if(this.getNumberType().equals(NumberType.LONG)){
				newNodeId = newNodeId.longValue()+1;
			}else{
				newNodeId = newNodeId.intValue()+1;
			}
		}
	}

	private Number _prioritySelectionFun(Number nodeId){
		Number oldNodeId = null;
		double p = MathTool.random()*((double)this.degreeMax/this.degreeSum);//产生一个0~degreeMax/degreeSum的随机数
		if(!nodeId.equals(this.currentNodeId)){
			//有新节点加入时，修改当前节点ID
			this.currentNodeId = nodeId;
			//随机的选取M个节点，组成局域世界
			do{
				this.MSet.add(this.getRandNodeId());
			}while(this.MSet.size() <= this.M);
			//获取M个节点的度
			this.degreeMap = this.degree.nodeDegree(MSet);
			Collection<Number> collection = degreeMap.values();
			//节点集中度的总和以及最大的度值
			int temp = 0;
			for (Iterator<Number> iterator = collection.iterator(); iterator.hasNext();) {
				temp = (Integer) iterator.next();
				this.degreeSum += temp;
				if(this.degreeMax < temp){
					this.degreeMax =  temp;
				}
			}
			//将MSet-》Arraylist
			this.MList = new ArrayList<Number>(this.MSet);
		}
		
		int i , flag = 0;
		do{
			i = (Integer) MathTool.random(0, this.MSet.size());
			oldNodeId = MList.get(i);
			if(++flag > this.MSet.size()*2 ){
				p = MathTool.random()*((double)this.degreeMax/this.degreeSum);//产生一个0~degreeMax/degreeSum的随机数
			}
		}while(p >= ((double)this.M/this.nodeNum)*((double)this.degreeMap.get(oldNodeId).intValue()/this.degreeSum) || this.isHasEdge(nodeId, oldNodeId));
		
		
		return oldNodeId;
	}
}
