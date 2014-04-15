package me.mervin.core.tree;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import me.mervin.core.Attribute;


 /**
 *   Vertex.java
 *    
 *  @author Mervin.Wong  DateTime 2014年3月21日 下午6:30:10    
 *  @version 0.4.0
 */
public class Vertex{
	public Number id = 0;//网络的ID
	public float weight = 0;//在网络中点的权重
	
	private Set<Vertex> parent = new HashSet<Vertex>();//父节点
	private Attribute attr = null;//节点的属性
	private LinkedList<Link> children = new LinkedList<Link>();//邻接边
	
	
	//初始化
	public Vertex(){
		super();
	}
	public Vertex(Number id){
		this.id = id;
		
	}
	public Vertex(Number id, int weight){
		this.id = id;
		this.weight = weight;
	}
	public Vertex(Number id, int weight, LinkedList<Link> children){
		this.id = id;
		this.weight = weight;
		this.children = children;
	}
	
	//SET GET
	public void setNodeId(Number id){
		this.id = id;
	}
	public void setParent(Vertex vertex){
		this.parent.add(vertex);
	}
	public void setWeight(float weight){
		this.weight = weight;
	}
	public void setChildren(LinkedList<Link> children){
		this.children = children;
	}
	
	public void addChild(Link child){
		this.children.add(child);
	}
	public void setAttr(String key, String value){
		if(this.attr == null){
			this.attr = new Attribute();
		}
		this.attr.set(key, value);
	}
	
	
	public Number getId(){
		return this.id;
	}
	public Set<Vertex> getParent(){
		return this.parent;
	}
	public float getWeight(){
		return this.weight;
	}
	public LinkedList<Link> getChildren(){
		return this.children;
	}
	public Link getFirstChild(){
		return this.children.getFirst();
	}
	public Number getFirstChildId(){
		return this.children.getFirst().getChildId();
	}
	public Link getLastChild(){
		return this.children.getLast();
	}
	public Number getLastChildId(){
		return this.children.getLast().getChildId();
	}
	public Link getChildByIndex(int index){
		return this.children.get(index);
	}
	public Number getChildIdByIndex(int index){
		return this.children.get(index).getChildId();
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
		return this.id.toString()+"##"+this.weight;
	}
	/**
	 * 
	 */
	public int hashCode(){
		return this.id.hashCode();
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
        Vertex other = (Vertex) obj; 
        if(!this.id.equals(other.id))
        	return false;
		return true;
	}
}

