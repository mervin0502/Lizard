package me.mervin.core.tree;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import me.mervin.core.Global.NumberType;
import me.mervin.util.D;
import me.mervin.util.FileTool;
import me.mervin.util.PairList;


 /**
 *   Tree.java
 *    
 *  @author Mervin.Wong  DateTime 2014年3月21日 下午4:56:36    
 *  @version 0.4.0
 */
public class Tree {
	public static NumberType numberType = NumberType.INTEGER;
	/*
	 * 树的根节点
	 */
	public Vertex root = null;
	/*
	 * 树中的节点和连接的数量
	 */
	public int vertexNum = 0;
	public int linkNum = 0;
	
	private FileTool ft = new FileTool();
	
	private Map<Number, Vertex> vertexMap = new HashMap<Number, Vertex>();
	/*
	 * 初始化
	 */
	public Tree(){
		
	}
	public Tree(PairList<Number, Number> linkList){
		
		this._initTree(linkList);
	}
	
	/*
	 * 注：左为父节点，右为子节点
	 */
	private void _initTree(PairList<Number, Number> linkList){
		Number parentId = null, childId = null;
		Vertex parent = null, child = null;
		for (int i = 0; i < linkList.size(); i++) {
			/*
			 * 构建树的步骤
			 * 1.提取顶点的信息p,c
			 * 2.建立顶点对象p,c;如果p不存在，建立p;如果c不存在，建立c
			 * 3.建立顶点之间的连接;将c加入到p的孩子节点队列中，将p加入到c的父节点集合；记录p->c出现的次数作为边的属性
			 */
			parentId = linkList.getL(i);
			childId = linkList.getR(i);
//			D.p(parentId+"####"+childId);
			
			parent = this.addVertex(parentId);
			child = this.addVertex(childId);
			
			this.insertLink(parent, child);
		}
	}
/***********************************************************************
*
*
*
***********************************************************************/
	
	
	/*****************************************************************
	 * 
	 * SET
	 * 
	*****************************************************************/
	public void setRoot(Vertex v){
		this.root = v;
	}
	
	
	/*****************************************************************
	 * 
	 * GET
	 * 
	 *****************************************************************/
	/**
	 * 
	 *  
	 *  @param id
	 *  @return
	 */
	public Vertex getVertexById(Number id){
		if(this.vertexMap.containsKey(id)){
			return this.vertexMap.get(id);
		}else{
			return null;
		}
		
	}
	
	public Set<Number> getAllVertexId(){
		return this.vertexMap.keySet();
	}
	
	/**
	 * 
	 *  
	 *  @param parentId
	 *  @return
	 */
	public Set<Number> getChildrenId(Number parentId){
		if(this.isHasVertex(parentId)){
		LinkedList<Link> childrenLink = this.vertexMap.get(parentId).getChildren();
			Set<Number> childrenIdSet = new HashSet<Number>();
			for(Link l:childrenLink){
				childrenIdSet.add(l.getChildId());
			}
			return childrenIdSet;
		}else{
			return null;
		}
	}
	/**
	 * 
	 *  
	 *  @param parentId
	 *  @param childId
	 *  @return
	 */
	public Link getLinkById(Number parentId, Number childId){
		if(this.isHasVertex(parentId)){
			LinkedList<Link> childrenLink = this.vertexMap.get(parentId).getChildren();
			for(Link l:childrenLink){
				if(l.getChildId().equals(childId)){
					return l;
				}
			}
			return null;
		}else{
			return null;
		}
	}
	/**
	 * 
	 *  
	 *  @param parent
	 *  @param child
	 *  @return
	 */
	public Link getLinkById(Vertex parent, Vertex child){
		return this.getLinkById(parent.id, child.id);
	}
	
	/*********************************************************
	 * 
	 * Judgement
	 *
	*********************************************************/
	/**
	 * 
	 *  
	 *  @param id
	 *  @return
	 */
	public boolean isHasVertex(Number id){
		if(this.vertexMap.containsKey(id)){
			return true;
		}else{
			return false;
		}
	}
	
	public boolean isHashLink(Vertex parent, Vertex child){
		return this.isHashLink(parent.id, child.id);
	}
	/**
	 * 
	 *  
	 *  @param parentId
	 *  @param childId
	 *  @return
	 */
	public boolean isHashLink(Number parentId, Number childId){
		Set<Number> childrenIdSet = this.getChildrenId(parentId);
		for(Number id:childrenIdSet){
			if(id.equals(childId)){
				return true;
			}
		}
		return false;
	}
	public boolean isHasChild(Vertex parent){
		
		return true;
	}
	/**
	 * 
	 *  
	 *  @param parentId
	 *  @return
	 */
	public boolean isHasChild(Number parentId){
		if(this.isHasVertex(parentId)){
			Vertex v = this.getVertexById(parentId);
			if(v.getChildren().isEmpty()){
				return false;
			}else{
				return true;
			}
		}else{
			return false;
		}
	}
	/*********************************************************
	 * 
	 * Judgement
	 *
	*********************************************************/
	/**
	 * 
	 *  
	 *  @param id
	 *  @return
	 */
	public Vertex addVertex(Number id){
		if(this.vertexMap.containsKey(id)){
			return this.vertexMap.get(id);
		}else{
			Vertex v = new Vertex(id);
			this.vertexMap.put(id, v);
			return v;
		}
	}
	/**
	 * 
	 *  
	 *  @param parentId
	 *  @param childId
	 *  @return
	 */
	public Link insertLink(Number parentId, Number childId){
		Link l = null;
		if(this.isHashLink(parentId, childId)){
			l = this.getLinkById(parentId, childId);
			l.setAtrr("count", (Integer.parseInt(l.getAttr("count"))+1)+"");
		}else{
			Vertex parent = this.addVertex(parentId);
			Vertex child = this.addVertex(childId);
			l = new Link(parent, child);
			l.setAtrr("count", 1+"");
			parent.addChild(l);
			child.setParent(parent);			
		}
		return l;
	}
	/**
	 * 
	 *  
	 *  @param parent
	 *  @param child
	 *  @return
	 */
	public Link insertLink(Vertex parent, Vertex child){
		Link l = null;
		if(this.isHashLink(parent, child)){
			l = this.getLinkById(parent, child);
			l.setAtrr("count", (Integer.parseInt(l.getAttr("count"))+1)+"");
		}else{
			l = new Link(parent, child);
			l.setAtrr("count", 1+"");
			parent.addChild(l);
			child.setParent(parent);
		}
		return l;
	}
	
	
	/********************************************************************
	 * Traverse
	 */
	/**
	 * 
	 *  广度优先遍历
	 *  @return
	 */
	public PairList<Number, Number> BFS(){
		PairList<Number, Number> linkList = new PairList<Number, Number>();
		Queue<Number> queue = new LinkedList<Number>();
		queue.add(this.root.id);
		Number vId = null, child = null;
		LinkedList<Link> children = null;
		while(!queue.isEmpty()){
			vId = queue.poll();
			children = this.getVertexById(vId).getChildren();
			for(Link link:children){
				child = link.getChildId();
				queue.add(child);
				linkList.add(vId, child);
			}
		}
		D.p("Out");
		return linkList;
	}
	/**
	 * 
	 *  广度优先遍历
	 *  @return
	 */
	public void BFS(String dstFile){
		PairList<Number, Number> linkList = new PairList<Number, Number>();
		StringBuffer sb = new StringBuffer();
		Queue<Number> queue = new LinkedList<Number>();
		queue.add(this.root.id);
		Number vId = null, child = null;
		LinkedList<Link> children = null;
		while(!queue.isEmpty()){
			vId = queue.poll();
			children = this.getVertexById(vId).getChildren();
			for(Link link:children){
				child = link.getChildId();
				queue.add(child);
				linkList.add(vId, child);
				sb.append(vId).append("\t").append(child).append("\r\n");
			}
			if(sb.length() > 50*1024*1024){
				this.ft.write(sb, dstFile, true);
				sb.delete(0, sb.length());
			}
		}
		this.ft.write(sb, dstFile, true);
	}
	
}

