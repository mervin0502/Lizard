package me.mervin.core.tree;

import java.util.Map;

import me.mervin.core.Attribute;


 /**
 *   Link.java
 *    
 *  @author Mervin.Wong  DateTime 2014年3月21日 下午6:29:40    
 *  @version 0.4.0
 */
public class Link {
	//private Number adjNodeId = 0;//邻接点的ID
	private Vertex parent = null;//源节点
	private Vertex child = null;//邻接点
	public float weight = 0;//边的权重
	private Attribute attr = null;
	//初始化
	public Link(){
		super();
	}
	public Link(Vertex parent, Vertex child){
		this.parent = parent;
		this.child = child;
	}
	
	public Link(Vertex parent, Vertex child, int linkWeight){
		this.parent = parent;
		this.child = child;
		this.weight = linkWeight;
	}
	
	// GET SET
	public void setParent(Vertex parent){
		this.parent = parent;
	}
	public void setChild(Vertex child){
		this.child = child;
	}
	public void setweight(float linkWeight){
		this.weight = linkWeight;
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
	
	public Vertex getParent(){
		return this.parent;
	}
	public Number getParentId(){
		return this.parent.getId();
	}
	public Vertex getChild(){
		return this.child;
	}
	public Number getChildId(){
		return this.child.getId();
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
		return this.parent.getId()+"=="+this.child.getId()+"##"+this.weight;
	}
	/**
	 * @Override
	 */
	public int hashCode(){
		return this.child.hashCode();
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
        Link other = (Link) obj; 
        if(!this.parent.equals(other.parent) && !this.child.equals(other.child))
        	return false;
		return true;
	}
	
}
