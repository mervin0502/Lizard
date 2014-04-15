package me.mervin.core;

import java.util.ArrayList;
import java.util.Set;

import me.mervin.util.PairList;

/**
 *  InterfaceNetwork
 *  网络的基本功能
 * @author Mervin.Wong
 * @version 0.1.0
 */
public interface InterfaceNet {
	
	/*
	 *  获取当前节点的数量
	 * @return int currentNodeNum
	 */
	//public int getCurrentNodeNum();
	
	/*
	 *  获取当前节点的数量
	 * @return int currentEdgeNum
	 */
	//public int getCurrentEdgeNum();
	
	/*
	 *  获取拓扑上的一个节点\
	 * @param  index 节点数组的索引号  ArrayList<Map<String, Object>> topology 
	 * @return  node 
	 */
	//public Node getNodeByIndex(int index);
	/**
	 *  得到所有节点的ID
	 * @return Set<Number>
	 */
	public Set<Number> getAllNodeId();
	/**
	 *  获取网络中的第一个节点
	 *  @return Node
	 */
	public Node getFirstNode();
	
	/**
	 *  获取网络中的第一个节点的ID
	 * @return Number
	 */
	public Number getFirstNodeId();
	
	/**
	 *  获取最后一个节点
	 * @return Node
	 */
	public Node getLastNode();
	/**
	 *  获取最后一个节点的ID
	 * @return Number
	 */
	public Number getLastNodeId();
	/**
	 *  获取拓扑上的一个节点
	 * @param  nodeId 节点id
	 * @return  Node 
	 */
	public Node getNodeById(Number nodeId);
	
	/*
	 *  getNodeIndex
	 *  获取节点在拓扑网络数组中的索引号
	 * @param Map node
	 * @return  nodeIndex
	 */
	//public int getNodeIndex(Map<String, Object> node);
		
	/*
	 *  getNodeIndex
	 *  获取节点在拓扑网络数组中的索引号
	 * @param Number nodeId
	 * @return int nodeIndex
	 */
	//public int getNodeIndex(Number nodeId);
	
	/**
	 *  
	 *  获取节点的ID
	 * @param node
	 * @return  Number
	 */
	public Number getNodeId(Node node);
	
	/*
	 *  getNodeId
	 *  获取节点的ID
	 * @param int nodeIndex
	 * @return Number nodeId
	 */
	//public Number getNodeId(int nodeIndex);
	
	/**
	 *  
	 *  获取节点的权重
	 * @param node
	 * @return Integer
	 */
	public float getNodeWeight(Node node);
	/**
	 * 
	 *  Function:设置节点的权重
	 * 
	 *  @param nodeId 网络节点的ID
	 *  @param w 网络节点的权重
	 */
	public void setNodeWeight(Number nodeId, float w);
	/**
	 *  
	 *  获取节点的权重
	 * @param nodeId
	 * @return Integer
	 */
	public float getNodeWeight(Number nodeId);
	
	/**
	 *  
	 *  获取边的权重
	 * @param preNode
	 * @param postNode
	 * @return float
	 */
	public float getEdgeWeight(Node preNode, Node postNode);
	
	/**
	 *  
	 *  获取边的权重
	 * @param preNodeId
	 * @param postNodeId
	 * @return float
	 */
	public float getEdgeWeight(Number preNodeId, Number postNodeId);
	/*
	 * @getNodeIndexByNodeId
	 *  通过节点的ID获取该节点在拓扑数组中的索引
	 * @param Number nodeId
	 * @return int nodeIndex 
	 */
	//public int getNodeIndexByNodeId(Number nodeId);
	
	/*
	 *  getNodeIdByNodeIndex
	 *  通过拓扑数组中的索引获取该节点的ID
	 * @param int nodeIndex
	 * @return Number nodeId
	 */
	//public Number getNodeIdByNodeIndex(int nodeIndex);
	
	/**
	 *  获取该节点的邻接点
	 * @param  node
	 * @return LinkedList<Edge> 
	 */
	//public LinkedList<Edge> getAdjNodes(Node node);
	
	/**
	 *  获取该节点的第一个邻接点
	 * @param  nodeId
	 * @return LinkedList<Edge> 
	 */
	//public LinkedList<Edge> getAdjNodes(Number nodeId);
	
	/**
	 *  
	 *  获取节点的邻接点,注：在数据结构上
	 * @param nodeId
	 * @return Set<Number>
	 */
	public Set<Number> getAdjNodeId(Number nodeId);
	
	/**
	 * 
	 *  Function:获取nodeIdSet的所有邻接点
	 * 
	 *  @param nodeIdSet 节点集合
	 *  @return Set<Number>
	 */
	public Set<Number> getAdjNodeId(Set<Number> nodeIdSet);
	/**
	 *  
	 *  查找某节点的入度节点，注在有向网络中
	 * @param  nodeId
	 * @return Set<Number> inDegreeNode
	 */
	public Set<Number> getInDegreeNodeId(Number nodeId);
	/**
	 *  
	 *  查找节点的出度节点，注在有向网络中
	 * @param  nodeId
	 * @return Set<Number> outDegreeNode
	 */
	public Set<Number> getOutDegreeNodeId(Number nodeId);
	
	/**
	 *   
	 *  获取节点nodeId的前一个节点
	 * @param nodeId
	 * @return Node
	 */
	public Node getPreNode(Number nodeId);
	/**
	 *   
	 *  获取节点nodeId的后一个节点
	 * @param nodeId
	 * @return Node
	 */
	public Node getPostNode(Number nodeId);
	
	/**
	 *   
	 *  获取一个随机节点
	 * @return Node
	 */
	public Node getRandNode();
	/**
	 *   
	 *  获取一个随机节点ID
	 * @return Number
	 */
	public Number getRandNodeId();
	/**
	 *   
	 *  更新节点的权值
	 * @param node
	 * @param nodeWeight
	 * 
	 */
	public void updateNodeWeight(Node node, float nodeWeight);
	/**
	 *   
	 *  更新节点的权值
	 * @param nodeId
	 * @param nodeWeight
	 * 
	 */
	public void updateNodeWeight(Number nodeId, float nodeWeight);
	
	/**
	 *   
	 *  更新变得权值
	 * @param preNode
	 * @param postNode
	 * @param edgeWeight
	 * @return boolean
	 */
	public boolean updateEdgeWeight(Node preNode, Node postNode, float edgeWeight);
	
	/**
	 *   
	 *  更新变得权值
	 * @param preNodeId
	 * @param postNodeId
	 * @param edgeWeight
	 * @return boolean
	 */
	public boolean updateEdgeWeight(Number preNodeId, Number postNodeId, float edgeWeight);
	
	/**
	 *   
	 *  判断节点nodeId是否存在
	 * @param nodeId
	 * @return boolean
	 */
	public boolean isHasNode(Number nodeId);
	/**
	 *   
	 *  判断两个节点之间是否相连
	 * @param preNode
	 * @param postNode
	 * @return boolean
	 */
	public boolean isHasEdge(Node preNode, Node postNode);
	/**
	 *  isHasEdge
	 * @param  preNodeId
	 * @param  postNodeId
	 * @return boolean
	 */
	public boolean isHasEdge(Number preNodeId, Number postNodeId);

	/**
	 *   
	 *  判断两点之间是否存在链路
	 * @param preNode
	 * @param postNode
	 * @return boolean
	 */
	public boolean isHasLink(Node preNode, Node postNode);
	
	/**
	 *   
	 *  判断两点之间是否存在链路
	 * @param preNodeId
	 * @param postNodeId
	 * @return boolean
	 */
	public boolean isHasLink(Number preNodeId, Number postNodeId);
	
	/**
	 *   
	 *  判断是否是连通的网络
	 * @return boolean
	 */
	public boolean isConnectedNet();
	/**
	 *  insertNode
	 *  插入一个节点
	 * @param  node
	 * @return boolean
	 */	
	public boolean insertNode(Node node);

	/**
	 *   
	 *  插入一个节点
	 * @param nodeId
	 * @return Edge
	 */
	public boolean insertNode(Number nodeId);
	/**
	 *  insertEdge
	 *  插入一条边
	 * @param  preNodeId
	 * @param  postNodeId
	 * @return  Edge
	 */
	public Edge insertEdge(final Number preNodeId, final Number postNodeId);
	
	/**
	 *  
	 *  插入一条边
	 * @param  preNodeId
	 * @param  postNodeId
	 * @param  edgeWeight
	 * @return Edge
	 */
	public Edge insertEdge(final Number preNodeId, final Number postNodeId, float edgeWeight);
	
	/**
	 *  
	 *  插入一条边
	 * @param  preNodeId
	 * @param  preNodeWeight
	 * @param  postNodeId
	 * @param  postNodeWeight
	 * @return Edge
	 */
	public Edge insertEdge(final Number preNodeId, float preNodeWeight, final Number postNodeId, float postNodeWeight);

	/**
	 *  
	 *  插入一条边
	 * @param  preNodeId
	 * @param  preNodeWeight
	 * @param  postNodeId
	 * @param  postNodeWeight
	 * @param  edgeWeight
	 * @return Edge
	 */
	public Edge insertEdge(final Number preNodeId, float preNodeWeight, final Number postNodeId, float postNodeWeight, float edgeWeight);


	/**
	 *  
	 *  删除一个节点
	 * @param  nodeId
	 * 
	 */
	public void deleteNode(Number nodeId);
	
	/**
	 *  
	 *  删除一条边
	 * @param  preNodeId
	 * @param  postNodeId
	 * 
	 */
	public void deleteEdge(Number preNodeId, Number postNodeId);
	
	/**
	 * 
	 *  深度优先遍历网络
	 *  @return ArrayList<Number>
	 */
	public ArrayList<Number> DFS();
	/**
	 * 
	 *  广度优先遍历网络
	 *  @return ArrayList<Number>
	 */
	public ArrayList<Number>  BFS();
	
	/**
	 * 
	 *  遍历网络中的边
	 * @return PairList<Number, Number>
	 */
	public PairList<Number, Number> traverseEdge();
	
	/**
	 *  
	 *  创建网络拓扑副本
	 * @return Network
	 */
	public Network copyNet();
	
	/**
	 *  
	 *  获取最大子网
	 * @return Network
	 */
	public Network getMaxSubNet();
	/**
	 *  
	 *  获取所有子网
	 * @return Set<Network>
	 */
	public Set<Network> getAllSubNet();
	/*
	 *  matrix 
	 *  构建邻接矩阵
	 * @return
	 */
//	public int[][] matrix();
	/**
	 *  
	 *  将网络拓扑保存到文件
	 * @param  fileName
	 * @param  netFileFormat
	 * @return boolean
	 */
	public boolean saveNetTofile(String fileName, Global.NetFileFormat netFileFormat);
}
