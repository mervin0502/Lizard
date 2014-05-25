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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import me.mervin.core.Global.NetInputType;
import me.mervin.core.Global.NetType;
import me.mervin.core.Global.NumberType;
import me.mervin.util.D;
import me.mervin.util.MathTool;
import me.mervin.util.PairList;

/**
 *  <p>说明：1,numberType的取值，如果是自建网络，那么默认时Integer类型，如果是读取文件，那么默认是Long<p>
 *  注：1.在有向网中，方向为由左边的参数到右边的参数，例如(a,b):a->b
 * 		2.对于构建的网络，如果是有向网络，则在邻接表中a=>b,b是a的邻接点。 无向网络中，b是a的邻接点，同时a也是b的邻接点
 * 		3.网络中默认情况下，节点的权值是1，边的权值是1。权值 > 0
 *  构建基础网络 采用邻接矩阵的数据结构
 *  
 * @author 王进法<Mervin.Wong>
 * @version 0.1.0
 * 
 * 
 * ********************************************************************************
 * ChangeLog
 * == 2013/10/17
 * + method: public Network();
 * == 2013/10/16
 * + attribute:public static boolean isLoop = false;
 * + method:public String getNodeAttr(Node node, String key)
 * + method:public String getNodeAttr(Number nodeId, String key)
 * 
 * # method:public Number getRandNodeId()
 * == 2013/10/15
 * + method: public void setEdgeAttr(Number preNodeId, Number postNodeId, String key, String value){
 * + method: public void setEdgeAttr(Node preNode, Node postNode, String key, String value){
 * + method: public void setEdgeAttr(Number preNodeId, Number postNodeId, Map<String, String> attrs){
 * + method: public Edge getEdgeByNode(Node preNode, Node postNode){
 * + method: public Edge getEdgeByNode(Number preNodeId, Number postNodeId){
 * + method:
 * 
 * # method: insertEdge return boolean => Edge
 * == 2013/10/3
 * + public Network(int initNodeNum, NetType netType) 
 */

public class Network implements InterfaceNet{
	public static NetType netType = NetType.UNDIRECTED; // 网络的类型，有向，无向;默认是无向的
	public static NumberType numberType = NumberType.INTEGER; //网络节点ID类型 Integer Long
	public static boolean isLoop = false; //网络是否运行子环 v->v
	
	private int initNodeNum = 0; //初始化节点的数量
	private PairList<Number, Number> edges = null;
	private String fileName = null;// 网络拓扑文件

	public int nodeNum = 0; // 网络中节点的数量
	public int edgeNum = 0;// 网络中边的数量
	public Number maxNodeId = 0;//网络中ID的最大值
	public Number minNodeId = Long.MAX_VALUE;//网络中ID的最小值
	protected  HashMap<Number, Node> topology = null;//网络拓扑
	//public String name = "net";//网络别名
	/*
	 * ****************************************************************************
	 * 网络的初始化 
	 *  
	 *****************************************************************************/
	/**
	 *  
	 *  构造器 初始化一个50个节点的无向全连通图
	 * 
	 */
	public Network() {
		//this.initNodeNum = 50;// 网络中初始化的节点时50		
		initNetwork(NetInputType.EMPTY);
	}
	/**
	 *  
	 *  构造器 初始化一个50个节点的无向全连通图
	 * 
	 */
	public Network(NetType netType) {
		//this.initNodeNum = 50;// 网络中初始化的节点时50	
		Network.netType = netType;
		initNetwork(NetInputType.EMPTY);
	}

	/**
	 * 
	 *  构造器 初始化一个initNum个节点的无向全连通图
	 * @param  initNodeNum 初始网络节点的个数，全连通
	 */
	public Network(int initNodeNum) {
		this.initNodeNum = initNodeNum;
		initNetwork(NetInputType.NODENUM);
	}
	/**
	 * 
	 *  构造器 初始化一个initNum个节点的无向全连通图
	 * @param  initNodeNum 初始网络节点的个数，全连通
	 */
	public Network(int initNodeNum, NetType netType) {
		this.initNodeNum = initNodeNum;
		Network.netType = netType;
		initNetwork(NetInputType.NODENUM);
	}

	/**
	 *  
	 * 依照edges中的边关系构造一个网络
	 * 
	 * @param edges 所有的边(PairList<Number, Number>)
	 * @param netType 网络类型：有向，无向
	 */
	public Network(PairList<Number, Number> edges, NetType netType){
		this.edges = edges;
		Network.netType = netType;
		initNetwork(NetInputType.PAIRLIST);
	}
	/**
	 * 依照edges中的边关系构造一个网络
	 * @param edges 所有的边(PairList<Number, Number>)
	 * @param netType 网络类型：有向，无向
	 * @param numberType 节点的ID类型
	 */
	public Network(PairList<Number, Number> edges, NetType netType, NumberType numberType){
		this.edges = edges;
		Network.setNumberType(numberType);
		Network.netType = netType;
		initNetwork(NetInputType.PAIRLIST);
	}
	/**
	 *  
	 * 通过读取一个网络文件，来构建网络
	 * @param fileName 文件路径
	 * @param netType  网络类型：有向，无向
	 */
	public Network(String fileName, NetType netType) {
		this.fileName = fileName;
		Network.netType = netType;
		initNetwork(NetInputType.FILE);
	}
	
	/**
	 *  
	 * 通过读取一个网络文件，来构建网络
	 * @param fileName 文件路径
	 * @param netType  网络类型：有向，无向
	 * @param numberType 节点的ID类型
	 */
	public Network(String fileName, NetType netType, NumberType numberType) {
		this.fileName = fileName;
		Network.setNumberType(numberType);
		Network.netType = netType;
		initNetwork(NetInputType.FILE);
	}

	/*
	 * @initNetwork 
	 *  初始化网络结构
	 * 
	 */
	private void initNetwork(NetInputType type) {
		topology = new HashMap<Number, Node>();
		switch(type){
		case EMPTY:
			//D.p("此网络为空网络");
			break;
		case NODENUM:
			//创建一个全连通图
			for(int preNodeId = 1; preNodeId <= initNodeNum; preNodeId++){
				for (int postNodeId = preNodeId+1; postNodeId <= initNodeNum; postNodeId++){
					//D.p(preNodeId+"\t"+postNodeId);
					insertEdge(preNodeId, 1, postNodeId, 1, 1);
				}
			}
			break;
		case PAIRLIST:
			Number l = null, r = null;
			for(int i = 0; i < this.edges.size(); i++){
				l = this.edges.getL(i);
				r = this.edges.getR(i);
				//D.p(l+"\t"+r);
				this.insertEdge(l, r);
			}
			break;
		case FILE:
			try {
				File file = new File(this.fileName);
				BufferedReader reader = new BufferedReader(new FileReader(file));
				String line = null;//在文本中读取的每行数据
				String[] lineArr = null;//将变量line进行分割
					
				Number preNodeId = null;
				double preNodeWeight = 1;
				double postNodeWeight = 1;
				Number postNodeId = null;
				double edgeWeight = 1;
				
				while((line = reader.readLine()) != null){
					//D.p(line);
					if(!line.startsWith("#")){
						//去掉line两边的空格，然后匹配分割
						lineArr = line.trim().split("\\s+|,");
						switch(lineArr.length){
						case 0:
							continue;
						case 1:preNodeId = MathTool.str2Number(Network.getNumberType(), lineArr[0]);
							insertEdge(preNodeId);
							break;
						case 2:
							//该行只有两个数，那么就是两个点的ID
							preNodeId = MathTool.str2Number(Network.getNumberType(), lineArr[0]);
							postNodeId = MathTool.str2Number(Network.getNumberType(), lineArr[1]);
							insertEdge(preNodeId, postNodeId);
							break;
						case 3:
							//该行有三个数，
							//第一个表示第一个节点ID，
							//第二个表示第二个节点ID，
							//第三个表示边的权重
							preNodeId = MathTool.str2Number(Network.getNumberType(), lineArr[0]);
							postNodeId = MathTool.str2Number(Network.getNumberType(), lineArr[1]);
							edgeWeight = Double.parseDouble((lineArr[2]));
							insertEdge(preNodeId,  postNodeId,  edgeWeight);
							break;
						case 4:
							//改行有四个数，
							//第一个数表示第一个节点ID
							//第二个数表示第一个节点权重
							//第三个数表示第二个节点ID
							//第四个数表示第二个节点权重
							preNodeId = MathTool.str2Number(Network.getNumberType(), lineArr[0]);
							preNodeWeight = Double.parseDouble(lineArr[1]);
							postNodeId = MathTool.str2Number(Network.getNumberType(), lineArr[2]);
							postNodeWeight = Double.parseDouble(lineArr[3]);
							insertEdge(preNodeId, preNodeWeight, postNodeId, postNodeWeight);
							break;
						case 5:
							//改行有四个数，
							//第一个数表示第一个节点ID
							//第二个数表示第一个节点权重
							//第三个数表示第二个节点ID
							//第四个数表示第二个节点权重
							//第五个数表示边的权重
							preNodeId = MathTool.str2Number(Network.getNumberType(), lineArr[0]);
							preNodeWeight = Double.parseDouble(lineArr[1]);
							postNodeId = MathTool.str2Number(Network.getNumberType(), lineArr[2]);
							postNodeWeight = Double.parseDouble(lineArr[3]);
							edgeWeight = Double.parseDouble(lineArr[4]);
							insertEdge(preNodeId, preNodeWeight, postNodeId, postNodeWeight, edgeWeight);
							break;
						}//switch
					}

				}//while
				reader.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("输入的文件不存在！");
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		}
	}
	
	// SET GET
	public int getNodeNum(){
		return this.nodeNum;
	}
	public int getEdgeNum(){
		return this.edgeNum;
	}
	public static NetType getNetType(){
		return Network.netType;
	}
	public static NumberType getNumberType() {
		return Network.numberType;
	}
	
	public static void setNetType(NetType netType){
		Network.netType = netType;
	}
	public static void setNumberType(NumberType numberType) {
		Network.numberType = numberType;
	}
/*****************************************************************************
 * 网络的基本操作
 * 
 *****************************************************************************/
	/**
	 *  
	 *  得到所有节点的ID
	 * @return Set<Number>
	 */
	public Set<Number> getAllNodeId(){
		return this.topology.keySet();
	}
	/**
	 *  
	 *  获取网络中的第一个节点
	 *  @return Node
	 */
	public Node getFirstNode(){
		return this.topology.get(this.getFirstNodeId());
	}
	/**
	 * 获取网络的第一个节点ID
	 * @return Number
	 */
	public Number getFirstNodeId(){
		Set<Number> nodesId = this.getAllNodeId();
		Iterator<Number> it = nodesId.iterator();
		return (Number)it.next();
	}
	
	/**
	 *  
	 *  获取最后一个节点
	 * @return Node
	 */
	public Node getLastNode(){
		Node node = null;
		Collection<Node> collection = this.topology.values();
		for (Iterator<Node> iterator = collection.iterator(); iterator.hasNext();) {
			node = (Node) iterator.next();
		}
		return node;
	}
	/**
	 *  
	 *  获取最后一个节点的ID
	 * @return Number
	 */
	public Number getLastNodeId(){
		Number nodeId = null;
		Collection<Number> collection = this.topology.keySet();
		for (Iterator<Number> iterator = collection.iterator(); iterator.hasNext();) {
			nodeId = (Number) iterator.next();
		}
		return nodeId;
	}
	/**
	 *  
	 *  由ID获取拓扑上的一个节点Node
	 * @param  nodeId 节点id
	 * @return Node  
	 */
	public Node getNodeById(Number nodeId){
		return this.topology.get(nodeId);
	}	
	/**
	 *  
	 *  获取节点的ID
	 * @param  node
	 * @return Number 
	 */
	public Number getNodeId(Node node){
		return node.getNodeId();
	}	
	/**
	 *  
	 *  获取节点的权重
	 * @param  node
	 * @return Integer 
	 */
	public double getNodeWeight(Node node){
		return node.getWeight();
	}
	/**
	 *  
	 *  获取节点的权重
	 * @param  nodeId
	 * @return Integer 
	 */
	public double getNodeWeight(Number nodeId){
		return this.getNodeById(nodeId).getWeight();
	}
	
	/**
	 * 获取节点的属性值
	 *  
	 *  @param node 节点
	 *  @param key 节点属性
	 *  @return String 
	 */
	public String getNodeAttr(Node node, String key){
		return node.getAttr(key);
	}
	/**
	 * 
	 *  获取节点的属性值
	 *  @param nodeId 节点ID
	 *  @param key 节点属性
	 *  @return String
	 */
	public String getNodeAttr(Number nodeId, String key){
		return this.getNodeAttr(this.getNodeById(nodeId), key);
	}
	/**
	 * 
	 *  Function:设置节点的权重
	 * 
	 *  @param nodeId 网络节点的ID
	 *  @param temp 网络节点的权重
	 */
	public void setNodeWeight(Number nodeId, double temp){
		this.getNodeById(nodeId).setWeight(temp);
	}
	
	/**
	 * 
	 *  设置边的属性
	 *  @param preNode 节点
	 *  @param postNode 节点
	 *  @param key 边的属性
	 *  @param value 边的属性值
	 */
	public void setEdgeAttr(Node preNode, Node postNode, String key, String value){
		Edge edge = null;
		edge = this.getEdgeByNode(preNode, postNode);
		edge.setAtrr(key, value);
		
		//无向
		if(Network.netType.equals(NetType.UNDIRECTED)){
			edge = this.getEdgeByNode(postNode, preNode);
			edge.setAtrr(key, value);
		}
	}
	/**
	 * 
	 *  设置边的属性
	 *  @param preNodeId 节点
	 *  @param postNodeId 节点
	 *  @param key 边的属性
	 *  @param value 边的属性值
	 */
	public void setEdgeAttr(Number preNodeId, Number postNodeId, String key, String value){
		this.setEdgeAttr(this.getNodeById(preNodeId), this.getNodeById(postNodeId), key, value);
	}
	/**
	 * 
	 *  设置边的属性
	 *  @param preNodeId 节点
	 *  @param postNodeId 节点
	 *  @param attrs 多个属性
	 */
	public void setEdgeAttr(Number preNodeId, Number postNodeId, Map<String, String> attrs){
		Edge edge = null;
		edge = this.getEdgeByNodeId(preNodeId, postNodeId);
		edge.setAtrr(attrs);
		
		//无向
		if(Network.netType.equals(NetType.UNDIRECTED)){
			edge = this.getEdgeByNodeId(postNodeId, preNodeId);
			edge.setAtrr(attrs);			
		}
	}
	/**
	 *  
	 *  获取边的权重
	 * @param preNode
	 * @param postNode
	 * @return double
	 */
	public double getEdgeWeight(Node preNode, Node postNode){
		double weight = 0;
		if(this.isHasEdge(preNode, postNode)){
			LinkedList<Edge> adjEdges = preNode.getAdjEdges();
			Edge edge = null;
			for (Iterator<Edge> iterator = adjEdges.iterator(); iterator.hasNext();) {
				edge = (Edge) iterator.next();
				if(edge.getPostNode().equals(postNode)){
					weight = edge.getWeight();
				}
			}			
		}else{
			weight = -1;//两点之间不存在边。
		}

		return weight;
	}
	
	/**
	 *  
	 *  获取边的权重
	 * @param preNodeId
	 * @param postNodeId
	 * @return double
	 */
	public double getEdgeWeight(Number preNodeId, Number postNodeId){
		return this.getEdgeWeight(this.getNodeById(preNodeId), this.getNodeById(postNodeId));		
	}
	
	/**
	 * 
	 * 由preNodeId postNodeId获取边的对象 
	 *  @param preNode
	 *  @param postNode
	 *  @return Edge
	 */
	public Edge getEdgeByNode(Node preNode, Node postNode){
		if(this.isHasEdge(preNode, postNode)){
			LinkedList<Edge> adjEdges = preNode.getAdjEdges();
			Edge edge = null;
			for (Iterator<Edge> iterator = adjEdges.iterator(); iterator.hasNext();) {
				edge = (Edge) iterator.next();
				if(edge.getPostNode().equals(postNode)){
					return edge;
				}
			}			
		}
		return null;
	}
	/**
	 * 
	 * 由preNodeId postNodeId获取边的对象 
	 *  @param preNode
	 *  @param postNode
	 *  @return Edge
	 */
	public Edge getEdgeByNodeId(Number preNodeId, Number postNodeId){
		return this.getEdgeByNode(this.getNodeById(preNodeId), this.getNodeById(postNodeId));
	}
	/**
	 *  
	 *  获取该节点的邻接边
	 * @param  node
	 * @return List<Edge>
	 */
	public List<Edge> getAdjEdgeList(Node node){
		return node.getAdjEdges();
	}
	
	/**
	 *  获取该节点的邻接边
	 * @param  nodeId
	 * @return List<Edge> 
	 */
	public List<Edge> getAdjEdgeList(Number nodeId){
		Node node = this.getNodeById(nodeId);
		if(node == null){
			return null;
		}else{
			return this.getAdjEdgeList(node);
		}
	}
	
	/**
	 *  获取节点的邻接点
	 * @param  nodeId
	 * @return HashSet<Number> 
	 */
	public Set<Number> getAdjNodeId(Number nodeId){
		Set<Number> adjNodesId = new HashSet<Number>();
		if(this.isHasNode(nodeId)){
			List<Edge> adjEdges = this.getAdjEdgeList(nodeId);
			Edge edge = null;
			for(Iterator<Edge> it = adjEdges.iterator(); it.hasNext();){
				edge = it.next();
				adjNodesId.add(edge.getPostNodeId());
			}
		}
		return  adjNodesId;
	}
	
	/**
	 * 
	 *  获取nodeIdSet的所有邻接点
	 * 
	 *  @param nodeIdSet 节点集合
	 *  @return Set<Number>
	 */
	public Set<Number> getAdjNodeId(Set<Number> nodeIdSet){
		HashSet<Number> adjNodeIdSet = new HashSet<Number>();
		for(Iterator<Number> it = nodeIdSet.iterator();it.hasNext();){
			adjNodeIdSet.addAll(this.getAdjNodeId(it.next()));
		}
		return adjNodeIdSet;
	}
	/**
	 *  查找某节点的入度节点，注在有向网络中
	 * @param  postNodeId
	 * @return Set<Number> 
	 */
	public Set<Number> getInDegreeNodeId(Number postNodeId){
		if(Network.netType.equals(NetType.DIRECTED)){
			HashSet<Number> inDegreeNodesId = new HashSet<Number>();
			Collection<Number> c = this.getAllNodeId();
			Number preNodeId = null;
			for (Iterator<Number> iterator = c.iterator(); iterator.hasNext();) {
				preNodeId = (Number) iterator.next();
				if(this.isHasEdge(preNodeId, postNodeId)){
					inDegreeNodesId.add(preNodeId);
				}
			}
	 		return inDegreeNodesId;
		}else{
			return null;
		}
	}

	/**
	 *  
	 *  查找节点的出度节点，注在有向网络中
	 * @param  nodeId
	 * @return Set<Number>
	 */
	public Set<Number> getOutDegreeNodeId(Number nodeId){
		if(Network.netType.equals(NetType.DIRECTED)){
			return getAdjNodeId(nodeId);
		}else{
			return null;
		}
	}
	
	/**
	 *  
	 *  获取节点nodeId的前一个节点
	 * @param nodeId
	 * @return Node
	 */
	public Node getPreNode(Number nodeId){
		Node preNode = null, 
				tempNode = null,
				node = this.getNodeById(nodeId);
		boolean flag = false;
		Collection<Node> c = this.topology.values();
		for (Iterator<Node> iterator = c.iterator(); iterator.hasNext();) {
			tempNode = iterator.next();
			if(tempNode.equals(node)){
				break;
			}
			preNode = tempNode;
			flag = true;
		}
		if(flag){
			return preNode;
		}else{
			return this.getLastNode();
		}
	}
	/**
	 *  
	 *  获取节点nodeId的后一个节点
	 * @param nodeId
	 * @return Node
	 */
	public Node getPostNode(Number nodeId){
		Node postNode = null,
				tempNode = null,
				node = this.getNodeById(nodeId);
		boolean flag = false;
		Collection<Node> collection = this.topology.values();
		for (Iterator<Node> iterator = collection.iterator(); iterator.hasNext();) {
			tempNode = (Node) iterator.next();
			if(tempNode.equals(node)){
				if(iterator.hasNext()){
					postNode = iterator.next();
				}else {
					//最后一个元素
					flag = true;
				}
			}
		}
		
		if(flag){
			return this.getFirstNode();
		}else{
			return postNode;
		}
	}
	
	/**
	 *  
	 *  获取一个随机节点
	 * @return Node
	 */
	public Node getRandNode(){
		return this.getNodeById(this.getRandNodeId());
	}
	/**
	 *  
	 *  获取一个随机节点ID
	 * @return Number
	 */
	public Number getRandNodeId(){
		Number nodeId = null;
		do{
			nodeId = MathTool.random(this.minNodeId, this.maxNodeId, Network.getNumberType());
		}while(!this.isHasNode(nodeId));
		return nodeId;
	}
	/**
	 *  
	 *  更新节点的权值
	 * @param node
	 * @param nodeWeight
	 */
	public void updateNodeWeight(Node node, double nodeWeight){
		node.setWeight(nodeWeight);
	}
	/**
	 *  
	 *  更新节点的权值
	 * @param nodeId
	 * @param nodeWeight
	 */
	public void updateNodeWeight(Number nodeId, double nodeWeight){
		this.getNodeById(nodeId).setWeight(nodeWeight);
	}
	
	/**
	 *  更新边的权值 对于无向网可以更新数据结构的两条边  A->B B->A
	 * @param preNode
	 * @param postNode
	 * @param edgeWeight
	 * @return boolean
	 */
	public boolean updateEdgeWeight(Node preNode, Node postNode, double edgeWeight){
		LinkedList<Edge> adjEdges = null;
		Edge edge = null;
		boolean flag = false;
		
		if(isHasEdge(preNode, postNode)){
			adjEdges = preNode.getAdjEdges();
			for(Iterator<Edge> it = adjEdges.iterator();it.hasNext();){
				edge = (Edge) it.next();
				if(edge.getPostNode().equals(postNode)){
					edge.setweight(edgeWeight);
					flag = true;
					break;
				}
			}
		}
		if(Network.netType.equals(NetType.UNDIRECTED)){
			//无向网，更新两个边
			if(isHasEdge(postNode, preNode)){
				adjEdges = postNode.getAdjEdges();
				for(Iterator<Edge> it = adjEdges.iterator();it.hasNext();){
					edge = (Edge)it.next();
					if(edge.getPostNode().equals(preNode)){
						edge.setweight(edgeWeight);
						flag = true;
						break;
					}
				}
			}
		}
		return flag;
	}
	
	/**
	 *  
	 *  更新变得权值
	 * @param preNodeId
	 * @param postNodeId
	 * @param edgeWeight
	 * @return boolean
	 */
	public boolean updateEdgeWeight(Number preNodeId, Number postNodeId, double edgeWeight){
		return this.updateEdgeWeight(this.getNodeById(preNodeId), this.getNodeById(postNodeId), edgeWeight);
	}

	/**
	 *  
	 *  判断节点nodeId是否存在
	 * @param node
	 * @return boolean
	 */
	public boolean isHasNode(Node node){
		if(this.topology.containsValue(node)){
			return true;
		}else {
			return false;
		}
	}
	/**
	 *  
	 *  判断节点nodeId是否存在
	 * @param nodeId
	 * @return boolean
	 */
	public boolean isHasNode(Number nodeId){
		if(this.topology.containsKey(nodeId)){
			return true;
		}else {
			return false;
		}
	}
	
	/**
	 *  
	 *  判断两个节点之间是否相连 单向  A->B 
	 * @param preNode
	 * @param postNode
	 * @return boolean
	 */
	public boolean isHasEdge(Node preNode, Node postNode){
		return this.isHasEdge(preNode.getNodeId(), postNode.getNodeId());
	}
	
	/**
	 *  
	 *  判断两个节点之间是否相连 单向  A->B 
	 * @param  preNodeId
	 * @param  postNodeId
	 * @return boolean
	 */
	public boolean isHasEdge(Number preNodeId, Number postNodeId){
		if(this.getNodeById(preNodeId) != null && this.getNodeById(postNodeId) != null){
			Set<Number> adjNodeId = this.getAdjNodeId(preNodeId);
			if(adjNodeId.contains(postNodeId)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 *  
	 *  判断两点之间是否存在链路
	 * @param preNode
	 * @param postNode
	 * @return boolean
	 */
	public boolean isHasLink(Node preNode, Node postNode){
		Number preNodeId = preNode.getNodeId();
		Number nodeId = null, 
				adjNodeId = null;
		Number postNodeId = postNode.getNodeId();
		
		Set<Number> visited = new HashSet<Number>();
		Set<Number> adjNodesId = null;
		
		Queue<Number> queue = new LinkedList<Number>();
		queue.offer(preNodeId);
		while(!queue.isEmpty()){
			nodeId = queue.poll();
			if(visited.contains(nodeId)){
				continue;
			}
			visited.add(nodeId);
			adjNodesId = this.getAdjNodeId(nodeId); 
			for (Iterator<Number> iterator = adjNodesId.iterator(); iterator.hasNext();) {
				adjNodeId = (Number) iterator.next();
				if(!visited.contains(adjNodeId)){
					//D.p("isHasLink=>"+nodeId+"##"+adjNodeId);
					if(adjNodeId.equals(postNodeId)){
						return true;
					}else{
						queue.offer(adjNodeId);
					}
				}
			}
		}
		
		return false;
	}
	
	/**
	 *  
	 *  判断两点之间是否存在链路
	 * @param preNodeId
	 * @param postNodeId
	 * @return boolean
	 */
	public boolean isHasLink(Number preNodeId, Number postNodeId){
		return this.isHasLink(this.getNodeById(preNodeId), this.getNodeById(postNodeId));
	}
	
	/**
	 *  
	 *  判断是否是连通的网络
	 * @return boolean
	 */
	public boolean isConnectedNet(){
		Node node = this.getFirstNode();
		Number nodeId = node.getNodeId();
		Number adjNodeId = null ;
		
		HashSet<Number> visited = new HashSet<Number>();
		Set<Number> adjNodesId = null;	
		
		Queue<Number> queue = new LinkedList<Number>();
		queue.offer(nodeId);
		while(!queue.isEmpty()){
			nodeId = queue.poll();
			if(visited.contains(nodeId)){
				continue;
			}
			visited.add(nodeId);
			adjNodesId = this.getAdjNodeId(nodeId);
			for (Iterator<Number> iterator = adjNodesId.iterator(); iterator.hasNext();) {
				adjNodeId = (Number) iterator.next();
				if(!visited.contains(adjNodeId)){
					queue.offer(adjNodeId);
				}
			}
		}
		if(this.nodeNum == visited.size()){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 *  
	 *  插入一个节点
	 * @param  node
	 * @return boolean
	 */	
	public boolean insertNode(Node node){
		if(!this.isHasNode(node.getNodeId())){
			//如果节点不存在存在
			this.topology.put(node.getNodeId(), node);
			if(this.maxNodeId.longValue() < node.getNodeId().longValue()){
				//网络中节点ID最大值
				this.maxNodeId = node.getNodeId();
			}
			if(this.minNodeId.longValue() > node.getNodeId().longValue()){
				//网络中节点ID最小值
				this.minNodeId = node.getNodeId();
			}
			this.nodeNum++;
			return true;
		}else{
			//D.p("节点已存在");
			return false;
		}
	}
	/**
	 *  
	 *  插入一个节点
	 * @param nodeId
	 * @return boolean
	 */
	public boolean insertNode(final Number nodeId, double weight){
		Node preNode = this.getNodeById(nodeId);
		if(preNode == null){
			//新加入的节点preNode不存在
			//如果preNodeId不存在 加入节点preNode
			preNode = new Node();
			preNode.setNodeId(nodeId);
			preNode.setWeight(weight);
			preNode.setAdjEdges(new LinkedList<Edge>());

			if(!insertNode(preNode)){
				//此节点为新插入节点
				//D.p("preNode节点创建失败!");
				return false;
			}
		}
		return true;
	}
	/**
	 *  
	 *  插入一个节点
	 * @param nodeId
	 * @return boolean
	 */
	public boolean insertNode(final Number nodeId){
		return this.insertNode(nodeId, 1);
	}
	/**
	 *  
	 *  插入一个结点边
	 * @param preNodeId
	 * @return boolean
	 */
	public Edge insertEdge(final Number preNodeId){
		this.insertNode(preNodeId);
		return null;
	}	
	/**
	 *  
	 *  插入一条边
	 * @param  preNodeId
	 * @param  postNodeId
	 * @return boolean
	 */
	public Edge insertEdge(final Number preNodeId, final Number postNodeId){
		return insertEdge(preNodeId, 1,  postNodeId, 1, 1);
	}
	
	/**
	 *  
	 *  插入一条边
	 * @param  preNodeId
	 * @param  postNodeId
	 * @param  edgeWeight
	 * @return boolean
	 */
	public Edge insertEdge(final Number preNodeId, final Number postNodeId, double edgeWeight){
		return insertEdge(preNodeId, 1,  postNodeId, 1, edgeWeight);
	}
	
	/**
	 *  
	 *  插入一条边
	 * @param  preNodeId
	 * @param  preNodeWeight
	 * @param  postNodeId
	 * @param  postNodeWeight
	 * @return boolean
	 */
	public Edge insertEdge(final Number preNodeId, double preNodeWeight, final Number postNodeId, double postNodeWeight){
		return insertEdge(preNodeId, preNodeWeight,  postNodeId, postNodeWeight, 1);
	}

	/**
	 *  
	 *  插入一条边
	 * @param  preNodeId
	 * @param  preNodeWeight
	 * @param  postNodeId
	 * @param  postNodeWeight
	 * @param  edgeWeight
	 * @return boolean
	 */
	public Edge insertEdge(final Number preNodeId, double preNodeWeight, final Number postNodeId, double postNodeWeight, double edgeWeight){
		
		if(preNodeId.equals(postNodeId) && !Network.isLoop){
			//D.p("不允许子环");
			return null;
		}
		Node preNode = getNodeById(preNodeId);
		Node postNode = getNodeById(postNodeId);
		
		LinkedList<Edge> preAdjEdges = null;
		LinkedList<Edge> postAdjEdges = null;
		
		Edge edge = null;
		
		//如果preNode postNode存在，则直接将邻接点插入，同时更新点的权值nodeWeight
		//如果preNode postNode不存在，则需创建preNode postNode节点
		if(preNode == null){
			//新加入的节点preNode不存在
			//如果preNodeId不存在 新加入节点preNode
			preNode = new Node();
			preNode.setNodeId(preNodeId);
			preNode.setWeight(preNodeWeight);
			preAdjEdges = new LinkedList<Edge>();
			preNode.setAdjEdges(preAdjEdges);

			if(!insertNode(preNode)){
				//此节点为新插入节点
				//D.p("preNode节点创建失败!");
				return null;
			}
		}else {
			preAdjEdges = preNode.getAdjEdges();
			preNode.setWeight(preNodeWeight);
		}
		
		
		
		
		if (postNode == null) {
			//新加入的节点postNode不存在
			postNode = new Node();
			postNode.setNodeId(postNodeId);
			postNode.setWeight(postNodeWeight);
			postAdjEdges = new LinkedList<Edge>();
			postNode.setAdjEdges(postAdjEdges);
			
			if(!insertNode(postNode)){
				//D.p("postNode节点创建失败!");
				return null;
			}
		}else {
			postAdjEdges = postNode.getAdjEdges();
			postNode.setWeight(postNodeWeight);
		}
		
		
		
		//如果preNode postNode之间没有边链接 将postNode节点插入到preNode节点的邻接点链表上
		//如果preNode postNode之间有边链接 只需更新边的权值edgeWeight
		if(!isHasEdge(preNode, postNode)){
			edge = new Edge();
			edge.setPreNode(preNode);
			edge.setPostNode(postNode);
			edge.setweight(edgeWeight);
			preAdjEdges.add(edge);
			
			this.edgeNum++;
		}else{
			this.updateEdgeWeight(preNode, postNode, edgeWeight);
			edge = this.getEdgeByNode(preNode, postNode);
		}
		
		
		
		if(Network.netType.equals(NetType.UNDIRECTED)){
			//无向图网络
			//如果preNode postNode之间没有边链接，将preNode节点插入到postNode节点的邻接点链表上
			//如果preNode postNode之间有边的链接， 只需要更新边的权值edgeWeight
			if(!isHasEdge(postNode, preNode)){
				edge = new Edge();
				edge.setPreNode(postNode);
				edge.setPostNode(preNode);
				edge.setweight(edgeWeight);
				postAdjEdges.add(edge);
			}else {
				this.updateEdgeWeight(postNode, preNode, edgeWeight);
			}
		}
		return edge;
	}
	
	/**
	 *  
	 *  删除一个节点
	 * @param  nodeId
	 */
	public void deleteNode(Number nodeId){
//		Node node = getNodeById(nodeId);
		List<Edge> preAdjEdges = null;
//		LinkedList<Edge> postAdjEdges = null;
		
		Edge edge = null;
		
		Set<Number> adjNodeSet = null;
		if(Network.netType.equals(NetType.UNDIRECTED)){
			//无向网
			adjNodeSet = this.getAdjNodeId(nodeId);//获取nodeId 的邻居节点
			for(Iterator<Number> it1 = adjNodeSet.iterator(); it1.hasNext();){
				preAdjEdges = this.getAdjEdgeList(it1.next());
				for(Iterator<Edge> it2 = preAdjEdges.iterator(); it2.hasNext();){
					edge = it2.next();
					if(edge.getPostNodeId().equals(nodeId)){
						it2.remove();
						break;
					}
				}
			}
			//删除边
			this.edgeNum -= adjNodeSet.size();
			
		}else {
			//有向网
			Set<Number> inDegreeNodesId = this.getInDegreeNodeId(nodeId);
			
			for(Iterator<Number> it1 = inDegreeNodesId.iterator(); it1.hasNext();){
				preAdjEdges = this.getAdjEdgeList(it1.next());
				for(Iterator<Edge> it2 = preAdjEdges.iterator(); it2.hasNext();){
					edge = it2.next();
					if(edge.getPostNodeId().equals(nodeId)){
						it2.remove();
						break;
					}
				}
			}

			this.edgeNum -= inDegreeNodesId.size();
			this.edgeNum -= this.getOutDegreeNodeId(nodeId).size();
		}
		
		//删除节点本身
		this.topology.remove(nodeId);
		this.nodeNum--;
	}
	
	/**
	 *  
	 *  删除一条边
	 * @param  preNodeId
	 * @param  postNodeId
	 */
	public void deleteEdge(Number preNodeId, Number postNodeId){
		Node preNode = this.getNodeById(preNodeId);
		Node postNode = this.getNodeById(postNodeId);
		
		LinkedList<Edge> adjEdges = null;
		
		Edge edge = null;
		
		if(isHasEdge(preNode, postNode)){
			adjEdges = preNode.getAdjEdges();
			Iterator<Edge> it = adjEdges.iterator();
			//int i = 0;
			while (it.hasNext()) {
				edge = it.next();
				if(edge.getPostNode().equals(postNode)){
					//adjEdges.remove(i);
					it.remove();
					this.edgeNum--;
					break;
				}
				//i++;
			}
		}
		if(Network.netType.equals(NetType.UNDIRECTED)){
			//无向网络
			if(isHasEdge(postNode, preNode)){
				adjEdges = postNode.getAdjEdges();
				Iterator<Edge> it = adjEdges.iterator();
				//int j = 0;
				while (it.hasNext()) {
					edge = (Edge) it.next();
					if(edge.getPostNode().equals(preNode)){
						//adjEdges.remove(j);
						it.remove();
						break;
					}
					//j++;
				}//while
			}//fi
		}//if
	}
	
	/**
	 *  DFS
	 *  深度优先遍历网络
	 *  @return  ArrayList<Number>
	 */
	public ArrayList<Number> DFS(){
		HashMap<Number, Integer> visited = new HashMap<Number, Integer>();//已访问的节点
		ArrayList<Number> nodes = new ArrayList<Number>();//节点遍历
		Number nodeId = null;
		for(Iterator<Number> iterator = this.getAllNodeId().iterator(); iterator.hasNext();){
			nodeId = iterator.next();
			if(visited.get(nodeId) == null){
				_DFSRecursiveFun(nodeId, visited, nodes);
			}
		}
		return nodes;
	}
	
	/*
	 * 
	 *  DFSRecursiveFun
	 *  深度优先搜索递归函数
	 * @param Number nodeId
	 * @param HashMap<Number, Integer> visited
	 * @param ArrayList<Number> 
	 */
	private void _DFSRecursiveFun(Number nodeId, HashMap<Number, Integer> visited, ArrayList<Number> nodes){
		visited.put(nodeId, 1);
		nodes.add(nodeId);
		//D.p("nodeId:"+nodeId);
		
		Set<Number> preAdjNodeSet = this.getAdjNodeId(nodeId);
		Number adjNodeId = null;
		for(Iterator<Number> preIt = preAdjNodeSet.iterator(); preIt.hasNext();){
			adjNodeId = preIt.next();
			if(visited.get(adjNodeId) == null){
				_DFSRecursiveFun(adjNodeId, visited, nodes);
			}
		}
		visited.put(nodeId, 2);
	}
	
	/**
	 *  BFS
	 *  网络广度优先搜索  
	 *  @return ArrayList<Number>
	 */
	public ArrayList<Number> BFS(){
		HashSet<Number> visited = new HashSet<Number>();//如果节点已访问，则放入该集合中
		PairList<Number, Number> edges = new PairList<Number, Number>();
		LinkedList<Node> queue = new LinkedList<Node>();
		
		if(Network.netType.equals(NetType.UNDIRECTED)){
			//如果时无向图
			Collection<Node> c = this.topology.values();
			Iterator<Node> it = c.iterator();
			Number nodeId, preNodeId, postNodeId;//node id
			Node node = null;
			Node preNode = null;
			Node postNode = null;
			while(it.hasNext()){
				node = (Node) it.next();
				nodeId = getNodeId(node);
				if(visited.contains(nodeId)){
					continue;
				}
				//visited[nodeId] = 1;
				queue.addLast(node);
				while(queue.size() != 0){
					preNode = queue.removeFirst();
					preNodeId = getNodeId(preNode);
					if(!visited.contains(preNodeId)){
						visited.add(preNodeId);
						LinkedList<Edge> preAdjNodes = (LinkedList<Edge>)getAdjEdgeList(preNode);					
						Edge preAdjEdge = null;
						Iterator<Edge> preIt = preAdjNodes.iterator();
						while(preIt.hasNext()){
							preAdjEdge = preIt.next();
							postNode = preAdjEdge.getPostNode();
							postNodeId = postNode.getNodeId();
							if(!visited.contains(postNodeId)){
								queue.addLast(postNode);
								edges.add(preNodeId, postNodeId);
								//D.p("BFS=>"+preNodeId+"\t"+postNodeId);
							}
						}

					}
				}
			}
		}else{
			//如果是有向图
			Collection<Node> collection = this.topology.values();
			Iterator<Node> it = collection.iterator();
			Number preNodeId, postNodeId;
			Node preNode = null;
			Node postNode = null;
			while(it.hasNext()){
				preNode = (Node) it.next();
				preNodeId = getNodeId(preNode);
				
				LinkedList<Edge> preAdjNodes = (LinkedList<Edge>)getAdjEdgeList(preNode);					
				Edge preAdjEdge = null;
				Iterator<Edge> preIt = preAdjNodes.iterator();
				while(preIt.hasNext()){
					preAdjEdge = preIt.next();
					postNode = preAdjEdge.getPostNode();
					postNodeId = postNode.getNodeId();
					edges.add(preNodeId, postNodeId);
					//D.p("BFS=>"+preNodeId+"\t"+postNodeId);
				}
			}//while
		}//else
		return null;	
	}
	
	/**
	 * 
	 *  遍历网络中的边
	 * @return PairList<Number, Number>
	 */
	public PairList<Number, Number> traverseEdge(){
		PairList<Number, Number> edges = new PairList<Number, Number>();//存放边
		HashSet<Number> visited = new HashSet<Number>();
		
		Collection<Number> collection = this.getAllNodeId();
		Iterator<Number> it = collection.iterator();
		
		Number preNodeId, postNodeId;
		Node preNode = null;
		List<Edge> preAdjEdges = null;
		Edge preAdjEdge = null;
		if(Network.netType.equals(NetType.UNDIRECTED)){
			//indirect network
			while(it.hasNext()){
				preNodeId = it.next();
				preAdjEdges = this.getAdjEdgeList(preNodeId);
				Iterator<Edge> preIt = preAdjEdges.iterator();
				while(preIt.hasNext()){
					preAdjEdge = preIt.next(); 
					//postNode = preAdjEdge.getPostNode();
					postNodeId = preAdjEdge.getPostNodeId();
					if(!visited.contains(postNodeId)){
						edges.add(preNodeId, postNodeId);
						//D.p("Traverse:"+preNodeId+"=>"+postNodeId);
					}
				}
				visited.add(preNodeId);
			}
			
		}else{
			//D.p("direct");
			//a direct network
			while(it.hasNext()){
				//preNode = (Node)it.next();
				preNodeId = it.next();
				preAdjEdges = getAdjEdgeList(preNodeId);
				Iterator<Edge> preIt = preAdjEdges.iterator();
				while(preIt.hasNext()){
					preAdjEdge = preIt.next();
					postNodeId = preAdjEdge.getPostNodeId();
					edges.add(preNodeId, postNodeId);
					//D.p("Traverse:"+preNodeId+"=>"+postNodeId);
				}
			}
		}
		return edges;
	}
	
	/**
	 *  
	 *  创建网络拓扑副本 注意网络有向和无向的区别
	 * @return Network
	 */
	public Network copyNet(){
		Network net3 = new Network();
		
		Number nodeId = null,
				preNodeId = null,
				adjNodeId = null;
		Set<Number> adjNodesId = null;
		if(Network.netType.equals(NetType.UNDIRECTED)){
			HashSet<Number> visited = new HashSet<Number>();
			
			Queue<Number> queue = new LinkedList<Number>();
			
			Collection<Number> c = this.topology.keySet();
			Iterator<Number> it = c.iterator();
			while (it.hasNext()) {
				nodeId = (Number) it.next();
				if(visited.contains(nodeId)){
					continue;
				}
				queue.offer(nodeId);
				while (!queue.isEmpty()) {
					preNodeId = queue.poll();
					if(!visited.contains(preNodeId)){
						visited.add(preNodeId);
						adjNodesId = this.getAdjNodeId(preNodeId);
						for(Iterator<Number> it2 = adjNodesId.iterator();it2.hasNext();){
							adjNodeId = it2.next();
							if(!visited.contains(adjNodeId)){
								queue.add(adjNodeId);
								net3.insertEdge(preNodeId, adjNodeId);
							}
						}//for
					}//if
				}//while
				
			}
		} else {
			//有向网络
			Collection<Number> c = this.topology.keySet();
			Iterator<Number> it = c.iterator();
			while(it.hasNext()){
				preNodeId = it.next();
				adjNodesId = this.getAdjNodeId(preNodeId);
				for(Iterator<Number> it2 = adjNodesId.iterator();it2.hasNext();){
					adjNodeId = it2.next();
					net3.insertEdge(preNodeId, adjNodeId);
				}
			}
		}
		return net3;
	}
	
	/**
	 *  
	 *  获取最大子网
	 * @return Network
	 */
	public Network getMaxSubNet(){
		Set<Number> nodeSet = this.getAllNodeId();//网络中所有节点
		Iterator<Number> it = nodeSet.iterator();
		Set<Number> maxNodeSetByNum = new HashSet<Number>();//当前节点最多的集合
		Set<Number> tempNodeSet = null;//当前节点集合
		
		Set<Number> s = new HashSet<Number>();
		Queue<Number> queue = new LinkedList<Number>();
		
		Number nodeId = null;
		Number adjNodeId = null;
		Set<Number> adjNodeIdSet = null; 
		
		while(it.hasNext()){
			//遍历所有节点
			nodeId = (Number) it.next();
			if(!s.contains(nodeId)){
				queue.offer(nodeId);
				tempNodeSet = new HashSet<Number>();//当前节点集合	
				//遍历子网
				while(!queue.isEmpty()){
					nodeId = queue.poll();
					s.add(nodeId);
					tempNodeSet.add(nodeId);
					adjNodeIdSet = this.getAdjNodeId(nodeId);
					for (Iterator<Number> iterator = adjNodeIdSet.iterator(); iterator.hasNext();) {
						adjNodeId = (Number) iterator.next();
						if(!s.contains(adjNodeId) && !queue.contains(adjNodeId)){
							//未遍历
							queue.add(adjNodeId);
						}
					}
				}
				//遍历完一个连通的子网
				//判断当前网络的节点数量是否大于目前的最大值
				if(tempNodeSet.size() > maxNodeSetByNum.size()){
					maxNodeSetByNum = tempNodeSet;
				}
			}
			
			
		}//while
		
		//提取maxNodeSetByNum中节点的边构成网络
		PairList<Number, Number> edges = new PairList<Number, Number>();
		it = maxNodeSetByNum.iterator();
		while(it.hasNext()){
			nodeId = (Number) it.next();
			adjNodeIdSet = this.getAdjNodeId(nodeId);
			for (Iterator<Number> iterator = adjNodeIdSet.iterator(); iterator.hasNext();) {
				adjNodeId = (Number) iterator.next();
				edges.add(nodeId, adjNodeId);
			}
		}
		return new Network(edges, Network.getNetType(), Network.getNumberType());
		
	}
	/**
	 *  
	 *  获取所有子网
	 * @return Set<Network>
	 */
	public Set<Network> getAllSubNet(){
		Set<Number> nodeSet = this.getAllNodeId();//网络中所有节点
		Iterator<Number> it = nodeSet.iterator();
		
		Set<Network> netSet = new HashSet<Network>();//子网集合
		PairList<Number, Number> edges = null;//子网边的集合
		
		Set<Number> s = new HashSet<Number>();
		Queue<Number> queue = new LinkedList<Number>();
		
		Number nodeId = null;
		Number adjNodeId = null;
		Set<Number> adjNodeIdSet = null; 
		
		while(it.hasNext()){
			//遍历所有节点
			nodeId = (Number) it.next();
			if(!s.contains(nodeId)){
				queue.offer(nodeId);
				edges = new PairList<Number, Number>();//子网边的集合
				//遍历子网
				while(!queue.isEmpty()){
					nodeId = queue.poll();
					s.add(nodeId);
					adjNodeIdSet = this.getAdjNodeId(nodeId);
					for (Iterator<Number> iterator = adjNodeIdSet.iterator(); iterator.hasNext();) {
						adjNodeId = (Number) iterator.next();
						if(!s.contains(adjNodeId) && !queue.contains(adjNodeId)){
							//未遍历
							queue.add(adjNodeId);
						}
						edges.add(nodeId, adjNodeId);
					}
				}
			}
			netSet.add(new Network(edges, Network.getNetType(), Network.getNumberType()) );
			
		}//while
		
		return netSet;
		
	}
	/**
	 * 获取该网络中子网的数量
	 *  
	 *  @return
	 */
	public int getSubNetNum(){
		int num = 0;
		Set<Number> nodeSet = this.getAllNodeId();//网络中所有节点
		Iterator<Number> it = nodeSet.iterator();
		Set<Number> s = new HashSet<Number>();
		Queue<Number> queue = new LinkedList<Number>();
		
		Number nodeId = null;
		Number adjNodeId = null;
		Set<Number> adjNodeIdSet = null; 
		
		while(it.hasNext()){
			//遍历所有节点
			nodeId = (Number) it.next();
			if(!s.contains(nodeId)){
				queue.offer(nodeId);
				s.add(nodeId);
				//遍历子网
				while(!queue.isEmpty()){
					nodeId = queue.poll();
					adjNodeIdSet = this.getAdjNodeId(nodeId);
					for (Iterator<Number> iterator = adjNodeIdSet.iterator(); iterator.hasNext();) {
						adjNodeId = (Number) iterator.next();
						if(!s.contains(adjNodeId)){
							//未遍历
							queue.add(adjNodeId);
							s.add(adjNodeId);
						}
					}
				}//while
				num++;
			}
		}//while
		return num;
	}	
	/*
	 *  matrix
	 *  邻接矩阵
	 * 
	 */
/*	public int[][] matrix(){
		return null;
		
	}*/
	/**
	 *  
	 *  将网络拓扑保存到文件 广度优先
	 * @param  fileName
	 * @return boolean
	 */
	public boolean saveNetTofile(String fileName, Global.NetFileFormat netFileFormat){
		HashSet<Number> visited = new HashSet<Number>();//如果节点已访问，则放入该集合中
		LinkedList<Node> queue = new LinkedList<Node>();
		
		File file = new File(fileName);
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			//将节点数和边数写入第一行
			writer.write(this.nodeNum+" "+this.edgeNum+" "+netFileFormat);
			writer.newLine();
			String str = new String();
			if(Network.netType.equals(Global.NetType.UNDIRECTED)){
				Collection<Node> c = this.topology.values();
				Iterator<Node> it = c.iterator();
				Number nodeId, preNodeId, postNodeId, edgeWeight;
				Node node = null;
				Node preNode = null;
				Node postNode = null;
				while(it.hasNext()){
					node = (Node) it.next();
					nodeId = getNodeId(node);
					if(visited.contains(nodeId)){
						continue;
					}
					//visited[nodeId] = 1;
					queue.addLast(node);
					while(queue.size() != 0){
						preNode = queue.removeFirst();
						preNodeId = getNodeId(preNode);
						if(!visited.contains(preNodeId)){
							visited.add(preNodeId);
							LinkedList<Edge> preAdjNodes = (LinkedList<Edge>)getAdjEdgeList(preNode);					
							Edge preAdjEdge = null;
							Iterator<Edge> preIt = preAdjNodes.iterator();
							while(preIt.hasNext()){
								preAdjEdge = preIt.next();
								postNode = preAdjEdge.getPostNode();
								postNodeId = postNode.getNodeId();
								edgeWeight = preAdjEdge.getWeight();
								queue.addLast(postNode);
								switch(netFileFormat){
								case ID_EWEIGHT:
										 str += preNodeId+"\t"+postNodeId+"\t"+edgeWeight+"\r\n";
									break;
								case ID_NWEIGHT:
									str += preNodeId+"\t"+getNodeWeight(preNode)+"\t"+postNodeId+"\t"+getNodeWeight(postNode)+"\r\n";
									break;
								case ID_NWEIGHT_EWEIGHT:
									str += preNodeId+"\t"+getNodeWeight(preNode)+"\t"+postNodeId+"\t"+getNodeWeight(postNode)+"\t"+edgeWeight+"\r\n";
									break;
								case ID:
								default:
									str +=preNodeId+"\t"+postNodeId+"\r\n";
									break;
								}
							}
							//System.out.println(str);
							if(str.length() > 1024){
								writer.append(str);
								str = "";
							}
						}
					}
				}
				writer.append(str);
				writer.close();
			}else{
				Collection<Node> collection = this.topology.values();
				Iterator<Node> it = collection.iterator();
				Number preNodeId, postNodeId, edgeWeight;
				Node preNode = null;
				Node postNode = null;
				while(it.hasNext()){
					preNode = (Node) it.next();
					preNodeId = getNodeId(preNode);
					
					LinkedList<Edge> preAdjNodes = (LinkedList<Edge>)getAdjEdgeList(preNode);					
					Edge preAdjEdge = null;
					Iterator<Edge> preIt = preAdjNodes.iterator();
					while(preIt.hasNext()){
						preAdjEdge = preIt.next();
						postNode = preAdjEdge.getPostNode();
						postNodeId = postNode.getNodeId();
						edgeWeight = preAdjEdge.getWeight();
						switch(netFileFormat){
						case ID_EWEIGHT:
							str += preNodeId+" "+postNodeId+" "+edgeWeight+"\r\n";
							break;
						case ID_NWEIGHT:
							str += preNodeId+" "+getNodeWeight(preNode)+" "+postNodeId+" "+getNodeWeight(postNode)+"\r\n";
							break;
						case ID_NWEIGHT_EWEIGHT:
							str += preNodeId+" "+getNodeWeight(preNode)+" "+postNodeId+" "+getNodeWeight(postNode)+edgeWeight+"\r\n";
							break;
						case ID:
						default:
							str +=preNodeId+" "+postNodeId+"\r\n";
							break;
						}
					}
					if(str.length() > 2024){
						writer.append(str);
						str = null;
					}
				}//while
				writer.append(str);
				writer.close();
			}//else
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
		return true;
	}

	/*
	*****************************************************************************
	* 自定义功能函数
	* 
	*****************************************************************************
	*/	
	//public abstract void setParam();
}