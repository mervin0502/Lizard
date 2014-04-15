package me.mervin.project.usr.ASRank;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;

import me.mervin.core.Edge;
import me.mervin.core.Network;
import me.mervin.core.Global.NetType;
import me.mervin.core.Global.NumberType;


 /**
 *   ASRank.java
 *    
 *  @author Mervin.Wong  DateTime 2013-11-13 下午3:06:13    
 *  @version 0.4.0
 */
public class ASRank {

	/**
	 */
	public ASRank() {
		// TODO 自动生成的构造函数存根
	}

	
	/*
	 * create a net by the ASes relationships
	 */
	private Network _createNet(String srcFile){
		Network net = new Network();
		Network.setNetType(NetType.DIRECTED);//有向
		Network.setNumberType(NumberType.INTEGER);
		
		/*
		 * read the xxx.as-rel.txt
		 */
		try {
			BufferedReader read = new BufferedReader(new FileReader(srcFile));
			String line = null;
			String[] lineArr = null;
			int preNodeId = 0, postNodeId = 0;
			Edge edge = null;
			
			while((line = read.readLine()) != null){
				if(line.charAt(0) != '#'){
					//the first character of every line != '#'
					lineArr = line.trim().split("|");
					preNodeId = Integer.parseInt(lineArr[0]);
					postNodeId = Integer.parseInt(lineArr[1]);
					//flag = Integer.parseInt(lineArr[2]);
					
					//insert a edge
					if(lineArr[2].equalsIgnoreCase("0")){
						edge = net.insertEdge(preNodeId, postNodeId);
						edge.setAtrr("relationship", "0");	
						edge = net.insertEdge(postNodeId, preNodeId);
						edge.setAtrr("relationship", "0");
					}else{
						edge = net.insertEdge(preNodeId, postNodeId);
						edge.setAtrr("relationship", "-1");							
					}
				}
			}
		} catch (FileNotFoundException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		
		return net;
	}
	
	private Tree customerCone(Network net, int nodeId){
		Tree tree =  new Tree(nodeId);
		
		return tree;
	}
	
	
	
	
	/**
	 *  
	 *  @param args
	 */
	public static void main(String[] args) {
		// TODO 自动生成的方法存根

	}

}


/*
 * DS: the implement of Tree
 */
class Tree{	
	
	private HashMap<Integer, Node> nodeMap = new HashMap<Integer, Node>();//the map between Tree's node and nodeId
	 /**
	 * @param nodeId
	 */
	public Tree(int nodeId) {
		// TODO 自动生成的构造函数存根
		this.nodeMap.put(nodeId, new Node(nodeId));
	}
	
	public void add(){
		
	}
	
	
	/*
	 * inner class Node
	 */
	private class Node{
		private LinkedList<Node> parents = null;
		private LinkedList<Node> children = null;
		private int nodeId = 0;
		
		public Node(int nodeId){
			this.nodeId = nodeId;
		}
		public void setNodeId(int nodeId){
			this.nodeId = nodeId;
		}
		public void setParent(Node node){
			if(this.parents == null){
				this.parents = new LinkedList<Tree.Node>();
			}
			this.parents.add(node);
		}
		public void setChild(Node node){
			if(this.children == null){
				this.children = new LinkedList<Tree.Node>();
			}
			this.children.add(node);
		}
		
	
	}
}