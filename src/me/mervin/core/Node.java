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

import java.util.LinkedList;
import java.util.Map;

/**
 * Node.java
 * 
 *@author 王进法<Mervin.Wong>
 *@version 
 *@Date 2013-1-17上午11:56:15
 *
 ****************************************
 *== 2013/10/19
 * + method: public boolean containsAttrKey(String key)
 * + method: public boolean containsAttrValue(String value)
 * == 2013/10/15
 * - Attribute:private Map<String, String> attr = null;
 * + Attribute:private Attribute attr = null;
 * + method: public void setAtrr(String key, String value){
 * + method: public String getAttr(String key){
 */
/*********************************************************************************
 *
 * 网络的节点类：网络ID，网络权重，网络的邻接点，节点属性
 *
 **********************************************************************************/

public class Node {
	private Number nodeId = 0;//网络的ID
	private float weight = 0;//在网络中点的权重
	private Attribute attr = null;//节点的属性
	private LinkedList<Edge> adjEdges = new LinkedList<Edge>();//邻接边
	
	
	//初始化
	public Node(){
		super();
	}
	public Node(Number nodeId){
		this.nodeId = nodeId;
	}
	public Node(Number nodeId, int nodeWeight){
		this.nodeId = nodeId;
		this.weight = nodeWeight;
	}
	public Node(Number nodeId, int nodeWeight, LinkedList<Edge> adjEdges){
		this.nodeId = nodeId;
		this.weight = nodeWeight;
		this.adjEdges = adjEdges;
	}
	
	//SET GET
	public void setNodeId(Number nodeId){
		this.nodeId = nodeId;
	}
	public void setWeight(float nodeWeight){
		this.weight = nodeWeight;
	}
	public void setAdjEdges(LinkedList<Edge> adjEdges){
		this.adjEdges = adjEdges;
	}
	
	public void addAdjEdge(Edge adjEdge){
		this.adjEdges.add(adjEdge);
	}
	public void setAttr(String key, String value){
		if(this.attr == null){
			this.attr = new Attribute();
		}
		this.attr.set(key, value);
	}
	
	
	public Number getNodeId(){
		return this.nodeId;
	}
	public float getWeight(){
		return this.weight;
	}
	public LinkedList<Edge> getAdjEdges(){
		return this.adjEdges;
	}
	public String getAttr(String key){
		if(this.attr != null){
			return this.attr.get(key);
		}else{
			return null;
		}
	}
	
	public boolean containsAttrKey(String key){
		if(this.attr != null){
			return this.attr.containsKey(key);
		}else{
			return false;
		}
	}
	
	public boolean containsAttrValue(String value){
		if(this.attr != null){
			return this.attr.containsValue(value);
		}else{
			return false;
		}
	}
	/**
	 * 
	 */
	public String toString(){
		return this.nodeId.toString()+"##"+this.weight;
	}
	/**
	 * 
	 */
	public int hashCode(){
/*        final int prime = 31;  
        int result = 1;  
        result = prime * result + this.nodeId.hashCode();  
        result = prime * result + this.nodeWeightt;  
        //result = prime * result + (this.nodeAdjNodes == null?0:this.nodeAdjNodes.hashCode());  
        return result; 	*/	
		return this.nodeId.hashCode();
	}
	/**
	 * 
	 */
	public boolean equals(Object obj){
        if (this == obj)  
            return true;  
        if (obj == null)  
            return false;  
        if (getClass() != obj.getClass())  
            return false;  
        Node other = (Node) obj; 
        if(!this.nodeId.equals(other.nodeId))
        	return false;
		return true;
	}
	
	public static void main(String[] args){

	}
}
