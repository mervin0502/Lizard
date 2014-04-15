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

import java.util.HashMap;
import java.util.Map;

/**
 * Edge.java
 * 
 *@author 王进法<Mervin.Wong>
 *@version 0.4.2
 *@Date 2013-1-17下午12:50:47
 * 
 * 修改Edge的基本参数
 * *******************************************
 * ChangeLog
 * 2013/10/19
 * + method: public boolean containsAttrKey(String key)
 * + method: public boolean containsAttrValue(String value)
 * ==2013/10/15
 * + attribute:attr
 * + method: public void setAtrr(String key, String value){
 * + method: public void setAtrr(Map<String, String> attrs){
 * + method: public String getAttr(String key){
 * 
 * # class: add public Edge
 */
/*********************************************************************************
 *
 * 网络拓扑中的边类
 *
 * preNode:起始节点
 * postNode:邻接点
 **********************************************************************************/

public class Edge {
	//private Number adjNodeId = 0;//邻接点的ID
	private Node preNode = null;//源节点
	private Node postNode = null;//邻接点
	private float weight = 0;//边的权重
	private Attribute attr = null;
	//初始化
	public Edge(){
		super();
	}
	public Edge(Node preNode, Node postNode){
		this.preNode = preNode;
		this.postNode = postNode;
	}
	
	public Edge(Node preNode, Node postNode, int edgeWeight){
		this.preNode = preNode;
		this.postNode = postNode;
		this.weight = edgeWeight;
	}
	
	// GET SET
	public void setPreNode(Node preNode){
		this.preNode = preNode;
	}
	public void setPostNode(Node postNode){
		this.postNode = postNode;
	}
	public void setweight(float edgeWeight){
		this.weight = edgeWeight;
	}
	public void setAtrr(String key, String value){
		if(this.attr == null){
			this.attr = new Attribute();
		}
		this.attr.set(key, value);
	}
	public void setAtrr(Map<String, String> attrs){
		if(this.attr == null){
			this.attr = new Attribute();
		}
		this.attr.set(attrs);
	}
	
	public Node getPreNode(){
		return this.preNode;
	}
	public Number getPreNodeId(){
		return this.preNode.getNodeId();
	}
	public Node getPostNode(){
		return this.postNode;
	}
	public Number getPostNodeId(){
		return this.postNode.getNodeId();
	}
	public float getWeight(){
		return this.weight;
	}
	public String getAttr(String key){
		if(this.attr != null){
			return this.attr.get(key);
		}else{
			return null;
		}
	}
	
	public boolean isEmpty(){
		if(this.attr != null){
			return true;
		}else{
			return false;
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
	 * @Override
	 */
	public String toString(){
		return this.preNode.getNodeId()+"=="+this.postNode.getNodeId()+"##"+this.weight;
	}
	/**
	 * @Override
	 */
	public int hashCode(){
		return this.postNode.hashCode();
	}
	/**
	 * @Override
	 */
	public boolean equals(Object obj){
        if (this == obj)  
            return true;  
        if (obj == null)  
            return false;  
        if (getClass() != obj.getClass())  
            return false;  
        Edge other = (Edge) obj; 
        if(!this.preNode.equals(other.preNode) && !this.postNode.equals(other.postNode))
        	return false;
		return true;
	}
	
}
