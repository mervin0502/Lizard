package me.mervin.util;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import me.mervin.core.Attribute;
import me.mervin.core.Network;
import me.mervin.core.Global.NumberType;


 /**
 *   Tree.java
 *   Tree.java实现的是一个树的基本数据结构
 *  @author Mervin.Wong  DateTime 2014年3月11日 上午9:15:24    
 *  @version 0.4.0
 */
public class Tree {
	
	/**
	 * the root TNode of Tree
	 */
	public TNode root = null;
	
	private NumberType numberType = NumberType.INTEGER;
	/*
	 * TNode与ID的映射，便于查询
	 */
	private HashMap<Number, TNode> nodeMap = null;
	
	Tree(){
		
		this.nodeMap = new HashMap<Number, TNode>();
	}
	
	public void addTNode(){
		
	}
	public void addTEdge(){
		
	}
	
	
}

/**
 * 
 *   TNode
 *   Tree结构的节点类
 *    
 */
class TNode{
	public Number nodeId = 0;//网络的ID
	public TNode parent = null;//ID节点的父节点
	
	private float weight = 0;//在网络中点的权重
	private Attribute attr = null;//节点的属性
	private LinkedList<TEdge> children = new LinkedList<TEdge>();//邻接边
	
	
	//初始化
	public TNode(){
		super();
	}
	public TNode(Number nodeId){
		this.nodeId = nodeId;
	}
	public TNode(Number nodeId, int nodeWeight){
		this.nodeId = nodeId;
		this.weight = nodeWeight;
	}
	public TNode(Number nodeId, int nodeWeight, LinkedList<TEdge> children){
		this.nodeId = nodeId;
		this.weight = nodeWeight;
		this.children = children;
	}
	
	//SET GET
	public void setNodeId(Number nodeId){
		this.nodeId = nodeId;
	}
	public void setWeight(float nodeWeight){
		this.weight = nodeWeight;
	}
	public void setAdjEdges(LinkedList<TEdge> children){
		this.children = children;
	}
	
	public void addAdjEdge(TEdge child){
		this.children.add(child);
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
	public LinkedList<TEdge> getAdjEdges(){
		return this.children;
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
        TNode other = (TNode) obj; 
        if(!this.nodeId.equals(other.nodeId))
        	return false;
		return true;
	}
}
/*
 * TEdge
 */
class TEdge {
	//private Number adjNodeId = 0;//邻接点的ID
	private TNode preNode = null;//源节点
	private TNode postNode = null;//邻接点
	private float weight = 0;//边的权重
	private Attribute attr = null;
	//初始化
	public TEdge(){
		super();
	}
	public TEdge(TNode preNode, TNode postNode){
		this.preNode = preNode;
		this.postNode = postNode;
	}
	
	public TEdge(TNode preNode, TNode postNode, int edgeWeight){
		this.preNode = preNode;
		this.postNode = postNode;
		this.weight = edgeWeight;
	}
	
	// GET SET
	public void setPreNode(TNode preNode){
		this.preNode = preNode;
	}
	public void setPostNode(TNode postNode){
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
	
	public TNode getPreNode(){
		return this.preNode;
	}
	public Number getPreNodeId(){
		return this.preNode.getNodeId();
	}
	public TNode getPostNode(){
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
        TEdge other = (TEdge) obj; 
        if(!this.preNode.equals(other.preNode) && !this.postNode.equals(other.postNode))
        	return false;
		return true;
	}
}

