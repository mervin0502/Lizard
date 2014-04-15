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

import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import me.mervin.core.NetModel;
import me.mervin.core.Network;
import me.mervin.core.Global.NetType;
import me.mervin.core.Global.NumberType;
import me.mervin.module.feature.Degree;
import me.mervin.util.D;
import me.mervin.util.MathTool;

/*********************************************************************************
 *
 * 生成BA模型的方式：1，在随机网络上生成；2，在已构造的网络上生成
 * BA模型的参数： 1，基础网络（生成BA模型的方式）
 * 				 2，BA模型新增网络节点的数量
 * 				 3，一个新增节点连接到网络中m个节点，m值的确定
 *
 **********************************************************************************/
/**
 *  BaNetwork
 *  BA无标度网络模型
 * @author 王进法<Mervin.Wong>
 * @version 0.4.3
 * 
 * *******************************************************************
 *  ChangLog:
 *  2013/10/13
 *  + directed BANetwork
 *  # core.global.NetType{DIRECTED, UNDIRECTED}
 *  
 */
public class BANetwork extends NetModel{
	private int addNodeNum = 0;//添加节点的数量
	private int m = 0;
	
	private int degreeSum = 0;//网络中节点度的总和
	private int nodeDegreeMax = 0; //网络中节点度的最大值
	private Degree degree = null;
	private Map<Number, Number> nodeDegree = null;//网络的节点度
	//网络节点与对应的度值
	private SortedMap<Number, Number> sortedMap = new TreeMap<Number, Number>();
	/*
	 * 按照节点的度值来排序网络网络节点
	 */
	private SortedSet<Map.Entry<Number, Number>> sortedset = new TreeSet<Map.Entry<Number, Number>>(
            new Comparator<Map.Entry<Number, Number>>() {
                @Override
                public int compare(Map.Entry<Number, Number> e1, Map.Entry<Number, Number> e2) {
                    //return -e1.getValue().compareTo(e2.getValue());
                	double d1 = e1.getValue().doubleValue();
                	double d2 = e2.getValue().doubleValue();
                	if(d1 < d2){
                		return 1;
                	}else{
                		return -1;
                	}
                }
            });

	
	
	/**
	 * 初始化全连通网络，节点数为initNodeNum
	 * @param initNodeNum 节点数
	 */
	public BANetwork(int initNodeNum){
		super(initNodeNum);
		this.degree = new Degree(this);
		this.nodeDegree = degree.nodeDegree(this.getAllNodeId());
		this.sortedMap.putAll(degree.nodeDegree(this.getAllNodeId()));
		this.sortedset.addAll(this.sortedMap.entrySet());//网络节点按度值排序
		this.nodeDegreeMax = degree.nodeDegreeMax(this.nodeDegree);
		if(Network.getNetType().equals(NetType.UNDIRECTED)){
			//无向网络
			this.degreeSum = this.edgeNum*2;
		}else {
			//有向网络
			this.degreeSum = this.edgeNum;
		}
	}
	/**
	 * 初始化全连通网络，节点数为initNodeNum
	 * @param initNodeNum 节点数
	 */
	public BANetwork(int initNodeNum, NetType netType){
		super(initNodeNum, netType);
		this.degree = new Degree(this);
		this.nodeDegree = degree.nodeDegree(this.getAllNodeId());
		this.sortedMap.putAll(degree.nodeDegree(this.getAllNodeId()));
		this.sortedset.addAll(this.sortedMap.entrySet());//网络节点按度值排序
		this.nodeDegreeMax = degree.nodeDegreeMax(this.nodeDegree);
		if(Network.getNetType().equals(NetType.UNDIRECTED)){
			//无向网络
			this.degreeSum = this.edgeNum*2;
		}else {
			//有向网络
			this.degreeSum = this.edgeNum;
		}
	}
	/**
	 * 初始化已给出的网络
	 * @param netFile 网络的文件
	 * @param netType 网络类型：有向，无向
	 */
	public BANetwork(String netFile, NetType netType){
		super(netFile, netType);
		this.degree = new Degree(this);
		this.nodeDegree = degree.nodeDegree(this.getAllNodeId());
		this.sortedMap.putAll(degree.nodeDegree(this.getAllNodeId()));
		this.sortedset.addAll(this.sortedMap.entrySet());//网络节点按度值排序
		this.nodeDegreeMax = degree.nodeDegreeMax(this.nodeDegree);
		if(Network.getNetType().equals(NetType.UNDIRECTED)){
			//无向网络
			this.degreeSum = this.edgeNum*2;
		}else {
			//有向网络
			this.degreeSum = this.edgeNum;
		}
	}
	/**
	 * 初始化已给出的网络
	 * @param netFile 网络文件
	 * @param netType 网络类型：有向，无向
	 * @param numberType 节点ID类型
	 */
	public BANetwork(String netFile, NetType netType, NumberType numberType){
		super(netFile, netType, numberType);
		this.degree = new Degree(this);
		this.nodeDegree = degree.nodeDegree(this.topology.keySet());
		this.sortedMap.putAll(degree.nodeDegree(this.getAllNodeId()));
		this.sortedset.addAll(this.sortedMap.entrySet());//网络节点按度值排序
		this.nodeDegreeMax = degree.nodeDegreeMax(this.nodeDegree);
		if(Network.getNetType().equals(NetType.UNDIRECTED)){
			//无向网络
			this.degreeSum = this.edgeNum*2;
		}else {
			//有向网络
			this.degreeSum = this.edgeNum;
		}
	}
	
	/**
	 *  setNetParam
	 *  设置模型参数
	 * @param addNodeNum
	 * @param m
	 */
	public void set(int addNodeNum, int m){
		this.addNodeNum = addNodeNum;
		this.m = m;
	}
	/**
	 *  createModelNetwork
	 *  由BA算法来演化网络结构
	 */
	@Override
	public void createModelNetwork() {
		// TODO Auto-generated method stub
		Number currentNodeId = null;
		if(Network.getNumberType().equals(NumberType.LONG)){
			currentNodeId = this.maxNodeId.longValue()+1;
		}else{
			currentNodeId = this.maxNodeId.intValue()+1;
		}
		
		for(int i = 0; i < this.addNodeNum; i++){
			/** 1,产生一个新节点 currentNodeId*/
			/** 2, 与已存在的m个节点相连 */
			this._addEdge(currentNodeId);
			
			if(Network.getNumberType().equals(NumberType.LONG)){
				currentNodeId = currentNodeId.longValue()+1;
			}else{
				currentNodeId = currentNodeId.intValue()+1;
			}
		}
	}

	/*
	 *  添加边 
	 *  
	 *  @param currentNodeId
	 */
	private void _addEdge(Number currentNodeId) {
		
		Entry<Number, Number> e = null;
		Number oldNodeId = null;
		int degree = 0;
		int  i = 1;
		boolean flag = true;
		double p = 0.0;
		double q = 0.0;
		//插入this.m 条边
		do{
			Iterator<Entry<Number, Number>> it = this.sortedset.iterator();
			flag = true;
			do{
				p = MathTool.random();//产生一个0~1的随机数
				
				if(!it.hasNext()){
					flag = false;
					break;
				}
				e = (Entry<Number, Number>) it.next();

				oldNodeId = e.getKey();
				degree = e.getValue().intValue();
				
				q= (double)degree/this.degreeSum;
			}while(p > q || this.isHasEdge(currentNodeId, oldNodeId) ||currentNodeId.equals(oldNodeId));
			
			/*
			 * flag = false 循环了一圈也没找到一个节点
			 */
			if(flag){
				this.insertEdge(currentNodeId, oldNodeId);
				//D.p(currentNodeId+"####"+oldNodeId);
				i++;
				
				/*
				 * 更新度值
				 */
				int temp1 = 0;
				
				 //* 更新currentNodeId的度
				if(this.sortedMap.get(currentNodeId) == null){
					//第一次添加该节点
					this.sortedMap.put(currentNodeId, 1);
					
				}else{
					temp1 = (Integer) this.sortedMap.get(currentNodeId);
					this.sortedMap.put(currentNodeId, temp1+1);
				}
				
				 //* 更新oldNodeId的度
				 
				temp1 = (Integer) this.sortedMap.get(oldNodeId);
				this.sortedMap.put(oldNodeId, temp1+1);
				
				 //* 获取目前的最大的度
				 
				if(this.nodeDegreeMax < temp1+1){
					this.nodeDegreeMax = temp1+1;
				}
				this.sortedset.clear();
				this.sortedset.addAll(this.sortedMap.entrySet());
				this.degreeSum +=2;
			}
		}while(i <= this.m);
	}
}
