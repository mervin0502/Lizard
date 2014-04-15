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

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import me.mervin.core.Network;
import me.mervin.util.D;
import me.mervin.util.MathTool;

/**
 * MutiNet.java
 * 
 *@author 王进法<Mervin.Wong>
 *@version  0.4
 */
/*
 * ********************************************************************************
 *
 *
 *
 **********************************************************************************/

public class MultiNet{
	private Network preNet = null;
	private Network postNet = null;
	
	//在网络初始化时已设定网络的preNet postNet的值
	private Set<Number> deathNodeSet = null;//死亡节点的集合
	private Set<Number> birthNodeSet = null;//新生节点的集合
	private Set<Number> intersectionNodeSet = null;//节点的交集
	private Network intersectionNetwork = null;//边的交集构成的网络

	public MultiNet(){
		
	}
	public MultiNet(Network preNet, Network postNet){
		this.preNet = preNet;
		this.postNet = postNet;
	}
	/**
	 * 
	 *  intersectionByNode
	 *  获取两个网络的节点交集
	 * @return HashSet<Number> 
	 */
	public Set<Number> intersectionByNode(){
		Set<Number> preNodeIdSet = (Set<Number>) this.preNet.getAllNodeId();
		Set<Number> postNodeIdSet = (Set<Number>) this.postNet.getAllNodeId();
		return MathTool.intersection(preNodeIdSet, postNodeIdSet);
	}
	
	/**
	 * 
	 *  
	 *  获取两个网络的节点交集
	 * @param  preNet
	 * @param  postNet
	 * @return HashSet<Number> 
	 */
	public Set<Number> intersectionByNode(Network preNet, Network postNet){
		Set<Number> preNodeIdSet = (Set<Number>) preNet.getAllNodeId();
		Set<Number> postNodeIdSet = (Set<Number>) postNet.getAllNodeId();
		return MathTool.intersection(preNodeIdSet, postNodeIdSet);
	}
	/**
	 *  birthNodes
	 *  the birth nodes in postNet
	 * @return HashSet<Number>
	 */
	public Set<Number> birthNode(){
		Set<Number> preNodeIdSet = (Set<Number>) this.preNet.getAllNodeId();
		Set<Number> postNodeIdSet = (Set<Number>) this.postNet.getAllNodeId();//新网络的节点集
		return MathTool.subtraction(postNodeIdSet, preNodeIdSet);
	}
	
	/**
	 *  birthNodes
	 *  the birth nodes in postNet
	 * @param preNet
	 * @param postNet
	 * @return HashSet<Number>
	 */
	public Set<Number> birthNode(Network preNet, Network postNet){
		Set<Number> preNodeIdSet = (Set<Number>) preNet.getAllNodeId();
		Set<Number> postNodeIdSet = (Set<Number>)postNet.getAllNodeId();//新网络的节点集		
		return MathTool.subtraction(postNodeIdSet, preNodeIdSet);
	}
	
	/**
	 *  deathNodes
	 *  the disappear nodes in preNet
	 * @return HashSet<Number>
	 */
	public Set<Number> deathNode(){
		Set<Number> preNodeIdSet = (Set<Number>) this.preNet.getAllNodeId();
		Set<Number> postNodeIdSet = (Set<Number>) this.postNet.getAllNodeId();//新网络的节点集
		return MathTool.subtraction(preNodeIdSet, postNodeIdSet);
	}
	
	/**
	 *  deathNodes
	 *  the disappear nodes in preNet
	 * @param preNet
	 * @param postNet
	 * @return HashSet<Number>
	 */
	public Set<Number> deathNode(Network preNet, Network postNet){
		Set<Number> preNodeIdSet = (Set<Number>) preNet.getAllNodeId();
		Set<Number> postNodeIdSet = (Set<Number>)postNet.getAllNodeId();
		return MathTool.subtraction(preNodeIdSet, postNodeIdSet);
	}
	/**
	 * 
	 *  获取两个网络的边交集，返回交集网络 
	 * @return Network 
	 */
	public Network intersectionByEdge(){
		Network net3 =  new Network();
		
		if(this.intersectionNodeSet == null){
			this.intersectionNodeSet = this.intersectionByNode();
		}
		Set<Number> nodeIdSet = this.intersectionNodeSet;
		
		Set<Number> preAdjNodes = null;
		Set<Number> postAdjNodes = null;
		Number preNodeId = 0;
		Number postNodeId = 0;
		boolean flag = false;

		for (Iterator<Number> iterator = nodeIdSet.iterator(); iterator.hasNext();) {
			preNodeId = (Number) iterator.next();
			preAdjNodes = this.preNet.getAdjNodeId(preNodeId);
			postAdjNodes = this.postNet.getAdjNodeId(preNodeId);
			for (Iterator<Number> iterator2 = preAdjNodes.iterator(); iterator2.hasNext();) {
				postNodeId = (Number) iterator2.next();
				if(postAdjNodes.contains(postNodeId)){
					net3.insertEdge(preNodeId, postNodeId);
					flag = true;
				}
			}
			if(!flag){
				net3.insertNode(preNodeId);
			}
		}
		return net3;
	}
	
	/**
	 * 
	 *  获取两个网络的边交集，返回交集网络 
	 * @param  preNet
	 * @param  postNet
	 * @return Network net
	 */
	public Network intersectionByEdge(Network preNet, Network postNet){
		Network net3 =  new Network();
		Set<Number> nodeIdSet = this.intersectionByNode(preNet, postNet);
		Set<Number> preAdjNodes = null;
		Set<Number> postAdjNodes = null;
		Number preNodeId = 0;
		Number postNodeId = 0;
		boolean flag = false;

		for (Iterator<Number> iterator = nodeIdSet.iterator(); iterator.hasNext();) {
			preNodeId = (Number) iterator.next();
			preAdjNodes = preNet.getAdjNodeId(preNodeId);
			postAdjNodes = postNet.getAdjNodeId(preNodeId);
			flag = false;
			for (Iterator<Number> iterator2 = preAdjNodes.iterator(); iterator2.hasNext();) {
				postNodeId = (Number) iterator2.next();
				if(postAdjNodes.contains(postNodeId)){
					//D.p("intersectionByEdge:"+preNodeId+"=>"+postNodeId);
					net3.insertEdge(preNodeId, postNodeId);
					flag = true;
				}
			}
			if(!flag){
				net3.insertNode(preNodeId);
			}
		}
		return net3;
	}
	
	/**
	 *  
	 *  消亡的内部边：A=》B  边的两个端点A和B均是两个网络的交集节点
	 * @return Network 
	 */
	public Network deathByInEdge(){
		Network net3 = new Network();
		
		if(this.intersectionNodeSet == null){
			this.intersectionNodeSet = this.intersectionByNode();
		}
		Set<Number> interNodes = this.intersectionNodeSet;

		if(this.intersectionNetwork == null){
			this.intersectionNetwork = this.intersectionByEdge();
		}
		Network interNet = this.intersectionNetwork;
		
		Set<Number> preAdjNodes = null;
		Set<Number> interAdjNodes = null;
		Number preNodeId = 0;
		Number interNodeId = 0;
		
		for (Iterator<Number> iterator = interNodes.iterator(); iterator.hasNext();) {
			preNodeId = (Number) iterator.next();
			preAdjNodes = this.preNet.getAdjNodeId(preNodeId);
			interAdjNodes = interNet.getAdjNodeId(preNodeId);
			for (Iterator<Number> iterator2 = preAdjNodes.iterator(); iterator2.hasNext();) {
				interNodeId = (Number) iterator2.next();
				if(!interAdjNodes.contains(interNodeId)&&interNodes.contains(interNodeId)){
					net3.insertEdge(preNodeId, interNodeId);
				}
			}
			
		}
		return net3;
	}
	
	/**
	 *  
	 *  消亡的内部边：A=》B  边的两个端点A和B均是两个网络的交集节点
	 * @param  preNet
	 * @param  postNet
	 * @return Network 
	 */
	public Network deathByInEdge(Network preNet, Network postNet){
		Network net3 = new Network();
		Set<Number> interNodes = this.intersectionByNode(preNet, postNet);	
		Network interNet = this.intersectionByEdge(preNet, postNet);
		Set<Number> preAdjNodes = null;
		Set<Number> interAdjNodes = null;
		Number preNodeId = 0;
		Number interNodeId = 0;
		
		for (Iterator<Number> iterator = interNodes.iterator(); iterator.hasNext();) {
			preNodeId = (Number) iterator.next();
			preAdjNodes = preNet.getAdjNodeId(preNodeId);
			interAdjNodes = interNet.getAdjNodeId(preNodeId);
			for (Iterator<Number> iterator2 = preAdjNodes.iterator(); iterator2.hasNext();) {
				interNodeId = (Number) iterator2.next();
				if(!interAdjNodes.contains(interNodeId)&&interNodes.contains(interNodeId)){
					net3.insertEdge(preNodeId, interNodeId);
				}
			}
		}
		return net3;
	}
	
	/**
	 *   
	 *   死亡的边界边，A=》B 边的两个端点A和B，其中A是消亡的节点，B是两个网络的交集节点
	 * @return Network 
	 */
	public Network deathByBoundaryEdge(){
		Network net3 = new Network();
		
		if(this.deathNodeSet == null){
			this.deathNodeSet = this.deathNode();
		}
		Set<Number> deathNodes = this.deathNodeSet;//死亡节点
		
		//交集节点
		if(this.intersectionNodeSet == null){
			this.intersectionNodeSet = this.intersectionByNode();
		}
		Set<Number> interNodes = this.intersectionNodeSet;
		
		Set<Number> deathAdjNodes = null;
		Number deathNodeId  = null;
		Number deathAdjNodeId = null;
		for (Iterator<Number> iterator = deathNodes.iterator(); iterator.hasNext();) {
			deathNodeId = (Number) iterator.next();
			deathAdjNodes = this.preNet.getAdjNodeId(deathNodeId);
			for (Iterator<Number> iterator2 = deathAdjNodes.iterator(); iterator2.hasNext();) {
				deathAdjNodeId = (Number) iterator2.next();
				if(interNodes.contains(deathAdjNodeId)){
					net3.insertEdge(deathNodeId, deathAdjNodeId);
				}
			}
			
		}
		return net3;
	}
	
	/**
	 *  
	 *  死亡的边界边，A=》B 边的两个端点A和B，其中A是消亡的节点，B是两个网络的交集节点
	 * @param  preNet
	 * @param  postNet
	 * @return Network 
	 */
	public Network deathByBoundaryEdge(Network preNet, Network postNet){
		Network net3 = new Network();
		Set<Number> deathNodes = this.deathNode(preNet, postNet);//死亡节点
		Set<Number> interNodes = this.intersectionByNode(preNet, postNet);	
		Set<Number> deathAdjNodes = null;
		Number deathNodeId  = null;
		Number deathAdjNodeId = null;
		for (Iterator<Number> iterator = deathNodes.iterator(); iterator.hasNext();) {
			deathNodeId = (Number) iterator.next();
			deathAdjNodes = preNet.getAdjNodeId(deathNodeId);
			for (Iterator<Number> iterator2 = deathAdjNodes.iterator(); iterator2.hasNext();) {
				deathAdjNodeId = (Number) iterator2.next();
				if(interNodes.contains(deathAdjNodeId)){
					net3.insertEdge(deathNodeId, deathAdjNodeId);
				}
			}
			
		}
		return net3;
	}
	
	/**
	 *  
	 *  新生的内部边：A=》B  边的两个端点A和B均是两个网络的交集节点
	 * @return Network 
	 */
	public Network birthByInEdge(){
		Network net3 = new Network();
		
		if(this.intersectionNodeSet == null){
			this.intersectionNodeSet = this.intersectionByNode();
		}
		Set<Number> interNodes = this.intersectionNodeSet;
		


		if(this.intersectionNetwork == null){
			this.intersectionNetwork = this.intersectionByEdge();
		}
		Network interNet = this.intersectionNetwork;
		
		Set<Number> postAdjNodes = null;
		Set<Number> interAdjNodes = null;
		Number interNodeId = 0;
		Number postNodeId = 0;
		D.m("for");
		for (Iterator<Number> iterator = interNodes.iterator(); iterator.hasNext();) {
			postNodeId = (Number) iterator.next();
			postAdjNodes = this.postNet.getAdjNodeId(postNodeId);
			interAdjNodes = interNet.getAdjNodeId(postNodeId);
			for (Iterator<Number> iterator2 = postAdjNodes.iterator(); iterator2.hasNext();) {
				interNodeId = (Number) iterator2.next();
				//D.p(interNodeId);
				if(!interAdjNodes.contains(interNodeId)&&interNodes.contains(interNodeId)){
					net3.insertEdge(postNodeId, interNodeId);
				}
			}
		}
		return net3;
	}
	
	/**
	 *  
	 *  新生的内部边：A=》B  边的两个端点A和B均是两个网络的交集节点
	 * @param  preNet
	 * @param  postNet
	 * @return Network 
	 */
	public Network birthByInEdge(Network preNet, Network postNet){
		Network net3 = new Network();
		Set<Number> interNodes = this.intersectionByNode(preNet, postNet);	
		Network interNet = this.intersectionByEdge(preNet, postNet);//两个网络的公共边
		Set<Number> postAdjNodes = null;
		Set<Number> interAdjNodes = null;
		Number interNodeId = 0;
		Number postNodeId = 0;
		
		for (Iterator<Number> iterator = interNodes.iterator(); iterator.hasNext();) {
			postNodeId = (Number) iterator.next();
			postAdjNodes = postNet.getAdjNodeId(postNodeId);
			interAdjNodes = interNet.getAdjNodeId(postNodeId);
			for (Iterator<Number> iterator2 = postAdjNodes.iterator(); iterator2.hasNext();) {
				interNodeId = (Number) iterator2.next();
				if(!interAdjNodes.contains(interNodeId)&&interNodes.contains(interNodeId)){
					net3.insertEdge(postNodeId, interNodeId);
				}
			}
		}
		return net3;
	}


	/**
	 *  
	 *  新生的边界边：A=》B 边的两个端点A和B，其中A是新生的节点，B是两个网络的交集节点
	 * @return Network
	 */
	public Network birthByBoundaryEdge(){
		Network net3 = new Network();
		
		if(this.birthNodeSet == null){
			this.birthNodeSet = this.birthNode();
		}
		Set<Number> birthNodes = this.birthNodeSet;//新生节点
		
		if(this.intersectionNodeSet == null){
			this.intersectionNodeSet = this.intersectionByNode();
		}
		Set<Number> interNodes = this.intersectionNodeSet;
		

		Set<Number> birthAdjNodes = null;
		Number birthNodeId  = null;
		Number birthAdjNodeId = null;
		for (Iterator<Number> iterator = birthNodes.iterator(); iterator.hasNext();) {
			birthNodeId = (Number) iterator.next();
			birthAdjNodes = this.postNet.getAdjNodeId(birthNodeId);
			for (Iterator<Number> iterator2 = birthAdjNodes.iterator(); iterator2.hasNext();) {
				birthAdjNodeId = (Number) iterator2.next();
				if(interNodes.contains(birthAdjNodeId)){
					net3.insertEdge(birthNodeId, birthAdjNodeId);
				}
			}
		}
		return net3;
	}
	
	/**
	 *  
	 *  新生的边界边：A=》B 边的两个端点A和B，其中A是新生的节点，B是两个网络的交集节点
	 * @param preNet
	 * @param postNet
	 * @return Network
	 */
	public Network birthByBoundaryEdge(Network preNet, Network postNet){
		Network net3 = new Network();
		Set<Number> birthNodes = this.birthNode(preNet, postNet);//新生节点
		Set<Number> interNodes = this.intersectionByNode(preNet, postNet);	
		Set<Number> birthAdjNodes = null;
		Number birthNodeId  = null;
		Number birthAdjNodeId = null;
		for (Iterator<Number> iterator = birthNodes.iterator(); iterator.hasNext();) {
			birthNodeId = (Number) iterator.next();
			birthAdjNodes = postNet.getAdjNodeId(birthNodeId);
			for (Iterator<Number> iterator2 = birthAdjNodes.iterator(); iterator2.hasNext();) {
				birthAdjNodeId = (Number) iterator2.next();
				if(interNodes.contains(birthAdjNodeId)){
					net3.insertEdge(birthNodeId, birthAdjNodeId);
				}
			}
		}
		return net3;
	}
	
	/**
	 *  
	 *  新生的外部边：A=》B 边的两个端点A和B均是新生的节点
	 * @return Network
	 */
	public Network birthByOutEdge(){
		Network net3 = new Network();
		
		if(this.birthNodeSet == null){
			this.birthNodeSet = this.birthNode();
		}
		Set<Number> birthNodes = this.birthNodeSet;//新生节点
		
		Set<Number> birthAdjNodes = null;
		Number birthNodeId  = null;
		Number birthAdjNodeId = null;
		for (Iterator<Number> iterator = birthNodes.iterator(); iterator.hasNext();) {
			birthNodeId = (Number) iterator.next();
			birthAdjNodes = this.postNet.getAdjNodeId(birthNodeId);
			for (Iterator<Number> iterator2 = birthAdjNodes.iterator(); iterator2.hasNext();) {
				birthAdjNodeId = (Number) iterator2.next();
				if(birthNodes.contains(birthAdjNodeId)){
					net3.insertEdge(birthNodeId, birthAdjNodeId);
				}
			}
		}
		return net3;
	}
	
	/**
	 *  
	 *  新生的外部边：A=》B 边的两个端点A和B均是新生的节点
	 * @param preNet
	 * @param postNet
	 * @return Network
	 */
	public Network birthByOutEdge(Network preNet, Network postNet){
		Network net3 = new Network();
		Set<Number> birthNodes = this.birthNode(preNet, postNet);//新生节点
		Set<Number> birthAdjNodes = null;
		Number birthNodeId  = null;
		Number birthAdjNodeId = null;
		for (Iterator<Number> iterator = birthNodes.iterator(); iterator.hasNext();) {
			birthNodeId = (Number) iterator.next();
			birthAdjNodes = postNet.getAdjNodeId(birthNodeId);
			for (Iterator<Number> iterator2 = birthAdjNodes.iterator(); iterator2.hasNext();) {
				birthAdjNodeId = (Number) iterator2.next();
				if(birthNodes.contains(birthAdjNodeId)){
					net3.insertEdge(birthNodeId, birthAdjNodeId);
				}
			}
		}
		return net3;
	}
		

	/**
	 *  
	 *   网络连接率
	 * @return int 
	 */
	public int netConnectRate(){
		
		return 0;
	}
	
	/**
	 *  
	 *   网络连接率
	 * @param preNet
	 * @param postNet
	 * @return int 
	 */
	public int netConnectRate(Network preNet, Network postNet){
		
		return 0;
	}
	/**
	 *  netAttractRate
	 *  网络吸引率
	 * @return int 
	 */
	public int netAttractRate(){
		
		return 0;
	}
	
	/**
	 *  netAttractRate
	 *  网络吸引率
	 * @param preNet
	 * @param postNet
	 * @return int 
	 */
	public int netAttractRate(Network preNet, Network postNet){
		
		return 0;
	}

}
