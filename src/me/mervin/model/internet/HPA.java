package me.mervin.model.internet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Random;
import java.util.Set;

import me.mervin.core.Edge;
import me.mervin.core.Global.NetType;
import me.mervin.core.Global.NumberType;
import me.mervin.core.NetModel;
import me.mervin.core.Network;
import me.mervin.core.Node;
import me.mervin.module.feature.AssortativityCoefficient;
import me.mervin.module.feature.Coreness;
import me.mervin.module.feature.Degree;
import me.mervin.module.feature.DegreeAndCC;
import me.mervin.module.feature.StructureEntropy;
import me.mervin.util.D;
import me.mervin.util.FileTool;
import me.mervin.util.MathTool;
/*
 * 
 * 
 * 
 */
public class HPA extends  NetModel {

	/**
	 * 模型的节点数量
	 */
	public int addNodeNum = 0;
	/**
	 * 派系节点的数量
	 */
	public int cliqueSize = 5;
	/**
	 * 增加一个节点的概率
	 */
	public double p = 0;
	/**
	 * 增加一条P2C内部边的概率
	 */
	public double q = 0;
	/**
	 * 增加一条外部P2C边的概率
	 */
	public double r = 1 - p -q;

	/**
	 * 
	 */
	public double s = 0;
	/*
	 * 当前节点的数量/最大节点编号
	 */
	private int curNodeNum = 0;
	
	/*
	 * 派系节点
	 */
	private List<Number> cliqueNodeList = new ArrayList<Number>();
	/*
	 * 记录派系节点构成的层次结构的深度
	 */
	//private Map<Number, Number> deepthByCliqueMap = new HashMap<Number, Number>();
	/*
	 * 记录每层的节点
	 */
	//private Map<Number, Level> levelByCliqueMap = new HashMap<Number, Level>();
	
	/*
	 * 度
	 */
	private Degree d= new Degree(this);
	
	/* * p2c边的数量*/
	 
	private int p2cNum = 0;
	/*private enum NodeType{CLIQUE, PROVIDER, CUSTOMER};
	private enum EdgeType{P2C,P2P,C2P}*/
	
	FileTool ft = new FileTool();
	/*
	 * 初始化
	 */
	public HPA(){
		//构建一个空的网络
		super();
		
	}
	public HPA(int n, double p, double q, double s){
		//构建一个空的网络
		super();
		this.addNodeNum = n;
		this.p = p;
		this.q = q;
		this.r = 1-p-q;
		this.s = s;
	}
	public HPA(int n, double p, double q, int cliqueSize){
		//构建一个空的网络
		super();
		this.addNodeNum = n;
		this.p = p;
		this.q = q;
		this.r = 1-p-q;
		this.cliqueSize = cliqueSize;
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int n = 2000;
		double p = 0.7;
		double q = 0.25;
		double s = 0.7;
		
		
		String prefix = n+"-"+p+"-"+q+"-"+s+"";
		HPA h = new HPA(n, p, q, s);
		h.createModelNetwork();
		FileTool ft = new FileTool();
		Set<Number> nodeSet = h.getAllNodeId();
		StringBuffer sb = new StringBuffer();
		sb.append("#clique:").append("\t");
		for(Number c:h.cliqueNodeList){
			sb.append(c).append("\t");
		}
		sb.append("\r\n");
		
		for(Number nodeId:nodeSet){
			List<Edge> list = h.getAdjEdgeList(nodeId);
			for(Edge edge:list){
				sb.append(edge.getPreNodeId()).append("\t");
				sb.append(edge.getPostNodeId()).append("\t");
				sb.append(edge.getAttr("type").equalsIgnoreCase("p2c")?-1:0).append("\r\n");
			}
		}
		String dstDir = "./data/hpa/"+prefix+"/";
		String dstFile = null;
		dstFile = dstDir+"hpa-1-"+prefix+".txt";
		ft.write(sb, dstFile);
		dstFile = dstDir+"hpa-2-"+prefix+".txt";
		ft.write(h.traverseEdge(), dstFile);
		String srcFile = dstFile;
		Network net = new Network(srcFile, NetType.UNDIRECTED, NumberType.INTEGER);
		Degree d = new Degree();
		dstFile = dstDir+"degree-"+prefix+".txt";
		ft.write(d.netDegreeDistributionRatio(net), dstFile);
//		D.p(d.netDegreeMax());
		Coreness core = new Coreness();
		dstFile = dstDir+"core-"+prefix+".txt";
		ft.write(core.nodeCore(net, net.getAllNodeId()), dstFile);
		DegreeAndCC dc = new DegreeAndCC();
		dstFile = dstDir+"dcc-"+prefix+".txt";
		ft.write(dc.script(net), dstFile);
		AssortativityCoefficient ac = new AssortativityCoefficient();
		dstFile = dstDir+"ac-"+prefix+".txt";
		ft.write(ac.script(net)+"", dstFile);
		StructureEntropy se = new StructureEntropy();
		dstFile = dstDir+"se-"+prefix+".txt";
		ft.write(se.script(net, false)+"", dstFile);
		
/*		
		HPA h = new HPA();
		for(int i = 0; i < 100; i++){
			D.p(h._levelSelect(6));
		}*/
		
	}

	@Override
	public void createModelNetwork() {
		// TODO Auto-generated method stub
		/*
		 * 构建一个有向空网络
		 */
		//有向
		Network.netType = NetType.DIRECTED;
		
		/*
		 * 1、生成派系网络
		 */
		this._initClique();
		D.p("init clique");
		/*
		 * 2、构造层次结构
		 */
//		D.p("##"+curNodeNum);
		for(;this.curNodeNum <= this.addNodeNum;){
			//系统生成概率s
//			Random rand = new Random(System.currentTimeMillis()+this.curNodeNum);
//			this._addNode();
			double s = Math.random();
			if(s <= this.p){
				this._addNode();
			}else if(s <= this.p+this.q){
				this._addIntraP2C();
			}else{
				this._addInterP2C();				
			}
		}//for
		D.p("network size: "+this.nodeNum+"##"+this.edgeNum);
		this._addP2P();

		

	}
	
	
	
	/*
	 * ******************************************************
	 * 
	 * private method
	 * 
	 */
	
	/*
	 * 初始化派系
	 */
	private void _initClique(){
//		D.p("init clique");
		Set<Node> cliqueNodeSet = new HashSet<Node>();
		//clique node
		for(int i = 0; i < this.cliqueSize; i++){
			Number nodeId = ++this.curNodeNum;
			Node node = new Node(nodeId);
			node.setAttr("type", "clique");
			this.cliqueNodeList.add(nodeId);
			
		}
		//clique p2p edge
		for(Node peer1:cliqueNodeSet){
			for(Node peer2:cliqueNodeSet){
				if(!peer1.equals(peer2)){
					
					 Edge edge = null;
					 edge = this.insertEdge(peer1.getNodeId(), peer2.getNodeId());
					 edge.setAtrr("type", "p2p");

					 edge = this.insertEdge(peer2.getNodeId(), peer1.getNodeId());
					 edge.setAtrr("type", "p2p");
				}
			}
		}
	}

	
	
	/*
	 * 增加点，同时添加一条P2C边
	 */
	private void _addNode(){
		/*
		 * 2.1、增加节点
		 * 生成节点a
		 * 随机选择一个层次结构 h
		 * 层选择概率选择相关层 l
		 * 节点选择概率选择节点 b
		 * 添加有向连接 b->a
		 */
		Number nodeId = null;
		nodeId = ++this.curNodeNum;
		D.p("add a node: "+nodeId);
		Node node = new Node(nodeId);  
		node.setAttr("type", "customer");
		
		//层次结构
		Number rootId = null;
		rootId = this._hierarchyStructureSelect();
		
		//rootId的深度
		int depth = this._getDeepth(rootId);
		//层选择
		int level = this._levelSelect(depth+1);
		//获取当前层节点
		//level = level==0?level:level-1;
		if(level == 0){
			level = 0;
		}else{
			level = level-1;
		}
		
		Set<Number> nodeSet = this._getNodeSetByLevel(rootId, level, level);
		//获取当前节点集的出度
		Map<Number, Number> nodeDegreeMap = new HashMap<Number, Number>();
		for(Number k:nodeSet){
			nodeDegreeMap.put(k, this.d.nodeOutDegree(k)+this.d.nodeInDegree(k));
//			nodeDegreeMap.put(k, this.d.nodeOutDegree(k));
		}
		//节点选择
//		D.p("add a p2c edge");
		Number preNodeId = this._nodeSelectByPro(nodeDegreeMap);
		//添加边
		Edge edge = this.insertEdge(preNodeId, nodeId);
		edge.setAtrr("type", "p2c");
		this.p2cNum++;
		
		D.p("add a node and a p2c edge"+preNodeId+"##"+nodeId);
	}
	
	/*
	 * 添加一条内部的P2C
	 */
	private void _addIntraP2C(){
		/*
		 * 2.2、增加一条内部P2C边
		 * 随机选择一个层次结构
		 * 选择层l
		 * 随机选择节点作为provider
		 * 从小于等于l层，且度小于provider的节点中优先选择一个节点
		 * 建立连接
		 * 更新节点层
		 */
		//层次结构
		Number rootId = null;
		rootId = this._hierarchyStructureSelect();
		
		//rootId的深度
		int depth = this._getDeepth(rootId);
		//层选择
		int level = this._levelSelect(depth);
		//获取当前层节点
		Set<Number> nodeSet = this._getNodeSetByLevel(rootId, level, level);
		//随机选择一个节点
		Number providerId = this._nodeSelectByRandom(nodeSet);
//		Number providerId = this._nodeSelectByPro(MathTool.add(this.d.nodeOutDegree(nodeSet), this.d.nodeInDegree(nodeSet)));
		//当前节点的度值
		int providerDegree = this.d.nodeOutDegree(providerId)+this.d.nodeInDegree(providerId);
//		int providerDegree = this.d.nodeOutDegree(providerId);
		
		//customer候选集
		Map<Number, Number> customerMap = new HashMap<Number, Number>();	
		int curLevel = level;
//		D.p(rootId+"#"+curLevel+"#"+depth);
		Set<Number> customerSet = this._getNodeSetByLevel(rootId, curLevel, depth);
		for(Number nodeId:customerSet){
			int degree = this.d.nodeOutDegree(nodeId)+this.d.nodeInDegree(nodeId);
//			int degree = this.d.nodeOutDegree(nodeId);
			if(degree < providerDegree){
				customerMap.put(nodeId, degree);
			}
		}

		//customer节点
		Number customerId = null;
		int flag = 0;
		do{
//			D.p("add a inter-p2c edge"+providerId+"###"+customerId);
//			customerId = this._nodeSelectByPro(customerMap);
			customerId = this._nodeSelectByRandom(customerMap.keySet());
			flag++;
			if(customerMap.size() < flag){
				break;
			}
		}while(providerId.equals(customerId) ||
				this.isHasEdge(providerId, customerId) || 
				this.isHasEdge(customerId, providerId) ||
				this._isHasNode(customerId, providerId));
		//添加边
		if(customerId != null && !this.isHasEdge(providerId, customerId)){
			Edge edge = this.insertEdge(providerId, customerId);
			edge.setAtrr("type", "p2c");
			D.p("add a inter-p2c edge"+providerId+"###"+customerId);
			this.p2cNum++;
		}
	}
	/*
	 * 添加一条之间的P2C
	 */
	private void _addInterP2C(){
		/*
		 * 2.3、增加一条外部P2C边
		 * 随机的选择两个层次结构h1 h2
		 * h1:选择层-》随机选择节点provider
		 * h2：优先选择节点，选择度比Provider小的节点
		 * 建立连接
		 */
		
		Number providerRootId = null, customerRootId = null;;
		do{
			providerRootId = this._hierarchyStructureSelect();
			customerRootId = this._hierarchyStructureSelect();
		}while(providerRootId.equals(customerRootId));
		
		//rootId的深度
		int providerDeepth = this._getDeepth(providerRootId);
		//层选择
		//inter-p2c: 统计分析，之间相差为1
		int level = Math.abs(this._levelSelect(providerDeepth)-2);
		//获取当前层节点
		Set<Number> nodeSet = this._getNodeSetByLevel(providerRootId, level, level);
		//随机选择一个节点
//		Number providerId = this._nodeSelectByRandom(nodeSet);
		Number providerId = this._nodeSelectByPro(MathTool.add(this.d.nodeOutDegree(nodeSet), this.d.nodeInDegree(nodeSet)));
		//当前节点的度值
		int providerDegree =  this.d.nodeOutDegree(providerId)+this.d.nodeInDegree(providerId);
//		int providerDegree =  this.d.nodeOutDegree(providerId);
		
		//层次结构2
		//rootId的深度
		int customerDeepth = this._getDeepth(customerRootId);
		Set<Number> customerSet = this._getNodeSetByLevel(customerRootId, level, customerDeepth);
		//customer候选集
		Map<Number, Number> customerMap = new HashMap<Number, Number>();
		for(Number nodeId:customerSet){
//			int degree = this.d.nodeOutDegree(nodeId);
			int degree = this.d.nodeOutDegree(nodeId)+this.d.nodeInDegree(nodeId);
			if(degree < providerDegree){
				customerMap.put(nodeId, degree);
			}
		}
		
		//customer节点
		Number customerId = null;
		do{
//			customerId = this._nodeSelectByPro(customerMap);
			customerId = this._nodeSelectByRandom(customerMap.keySet());
		}while(providerId.equals(customerId) || 
				this.isHasEdge(providerId, customerId) ||
				this.isHasEdge(customerId, providerId)||
				this._isHasNode(customerId, providerId));
		//添加边
		if(customerId != null){
			Edge edge = this.insertEdge(providerId, customerId);
			edge.setAtrr("type", "p2c");
			D.p("add a intra-p2c edge"+providerId+"###"+customerId);
			this.p2cNum++;
		}
	}
	
	/*
	 * 添加一条P2P边
	 */
	private void _addP2P(){
		/*
		 * 3、添加P2P边
		 *随机选择结构
		 *选择层
		 *随机节点peer1
		 *度想近的
		 * 
		 */
		/*
		 * 记录派系节点构成的层次结构的深度
		 */
		Map<Number, Integer> depthByCliqueMap = new HashMap<Number, Integer>();
		Map<Number, Set<Number>> nodeByCliqueMap = new HashMap<Number, Set<Number>>();
		Set<Number> allNodeSetByClique = null;
		for(Number nodeId:this.cliqueNodeList){
			//rootId的深度
			int depth = this._getDeepth(nodeId);
			depthByCliqueMap.put(nodeId, depth);
			allNodeSetByClique  = this._getNodeSetByLevel(nodeId, 0, depth);
			nodeByCliqueMap.put(nodeId, allNodeSetByClique);
		}
		for(int i =0; i<(int) this.p2cNum*this.s;){
			//层次结构
			Number rootId = null;
			rootId = this._hierarchyStructureSelect();
			
			int depth = depthByCliqueMap.get(rootId);
			//层选择
			int level = this._levelSelect(depth);
			//获取当前层节点
			Set<Number> nodeSet = this._getNodeSetByLevel(rootId, level, level);
			//随机选择一个节点
			Number proPeerId = this._nodeSelectByRandom(nodeSet);
			//度值
			int prePeerDegree = this.d.nodeInDegree(proPeerId)+this.d.nodeOutDegree(proPeerId);
//			int prePeerDegree = this.d.nodeOutDegree(proPeerId);
			
			Set<Number> postPeerSet = nodeByCliqueMap.get(rootId);
			//customer候选集
			Set<Number> customerSet = new HashSet<Number>();
			for(Number nodeId:postPeerSet){
				int degree = this.d.nodeOutDegree(nodeId)+this.d.nodeInDegree(nodeId);
//				int degree = this.d.nodeOutDegree(nodeId);
				if((double)Math.min(degree, prePeerDegree)/Math.max(degree, prePeerDegree) > 0.95){
					customerSet.add(nodeId);
				}
			}
			//customer节点
			Number postPeerId = null;
//			D.p(customerSet);
			if(customerSet.size() <= 0){
				continue;
			}
			
			int count = 0;
			boolean flag = true;
			do{
				postPeerId = this._nodeSelectByRandom(customerSet);
				count++;
				if(customerSet.size() < count){
					flag = false;
					break;
				}
			}while(this.isHasEdge(proPeerId, postPeerId) || this.isHasEdge(postPeerId, proPeerId) || proPeerId.equals(postPeerId));
//			}while(proPeerId.equals(postPeerId));
			//添加边
			if(flag){
				Edge edge = null;
//				if(this.isHasEdge(proPeerId, postPeerId)){
//					edge = this.getEdgeByNodeId(proPeerId, postPeerId);
//				}else{
					edge = this.insertEdge(proPeerId, postPeerId);
//				}
				edge.setAtrr("type", "p2p");
				
//				if(this.isHasEdge(postPeerId, proPeerId)){
//					edge = this.getEdgeByNodeId(postPeerId, proPeerId);
//				}else{
					edge = this.insertEdge(postPeerId, proPeerId);
//				}
				edge.setAtrr("type", "p2p");
				D.p("add a p2p edge"+i+"###"+(int)this.p2cNum*this.s+"@@@"+proPeerId+"###"+postPeerId);
				i++;
			}
		}
	}
	/*
	 * 随机选择一个层次结构
	 */
	private Number _hierarchyStructureSelect(){
//		D.p("select hierarchy structure");
		int index = (int) Math.floor(Math.random()*this.cliqueSize);
		return this.cliqueNodeList.get(index);
	}
	
	
	/*
	 * 层次选择概率
	 */
	private int _levelSelect(int curDeepth){
//		D.p("select level");
		//int l = 0;
		double sum = 0;
		int curLevel = 0;
		
		ArrayList<Double> list = new ArrayList<Double>();
		double p = 0;
		for(int i=0; i<= curDeepth; i++){
			p = (1/(Math.sqrt(2*Math.PI)*1.818))*Math.pow(Math.E, -Math.pow((i-3), 2)/(2*1.818*1.818));
			list.add(p);
			sum += p;
		}
//		D.p(list);
		for(int j = 0; j < list.size(); j++){
			list.set(j, list.get(j)/sum);
		}
//		D.p(list);
		sum = 0;
		double s = Math.random();
		int k = 0;
		do{
			sum += list.get(k++);
			curLevel++;
//			D.p("@@"+curDeepth+"#"+curLevel+"@"+s+"#"+sum);
		}while(sum < s);
		D.p("cur-level: "+(curLevel-1));
		return curLevel-1;
	}
	
	/*
	 * 节点选择概率
	 * LinkedHashMap nodeDegreeMap
	 */
	private Number _nodeSelectByPro(Map<Number, Number> nodeDegreeMap){
//		D.p("select node by probability");
		double s = Math.random();
		
		long degreeSum = 0;
		int a = 1;
		for(Number degree:nodeDegreeMap.values()){
			degreeSum += degree.longValue()+a;
		}
		//Math.round
		long curNum = Math.round(degreeSum*s);
		
		//select the node by the probability
		long curSum = 0;
		for(Entry<Number, Number> e:nodeDegreeMap.entrySet()){
			curSum += e.getValue().longValue()+a;
			if(curNum <= curSum){
//				D.p(e.getKey());
				return e.getKey();
			}
		}
		return null;
	}
	
	/*
	 * 随机选择一个节点
	 */
	private Number _nodeSelectByRandom(Set<Number> nodeSet){
//		D.p("select node by random");
		if(nodeSet.size() == 0){
			return null;
		}
		ArrayList<Number> nodeList = new ArrayList<Number>();
		for(Number nodeId:nodeSet){
			nodeList.add(nodeId);
		}
		
		return nodeList.get((int)Math.floor(Math.random()*nodeList.size()));
	}


	/*
	 * 获取minLevel~maxLevel之间的节点
	 */
	private Set<Number> _getNodeSetByLevel(Number rootId, int minLevel, int maxLevel){
//		D.p("get node set by level");
		Set<Number> nodeSet = new HashSet<Number>();
		
		Queue<Number> queue = new LinkedList<Number>();
		queue.add(rootId);
		queue.add(-1);
		Set<Number> visited = new HashSet<Number>();
		Set<Number> visting = new HashSet<Number>();
		visited.add(rootId);
		int curLevel = 0;
		if(maxLevel ==  0 && minLevel == 0){
			nodeSet.add(rootId);
		}else{
			while(!queue.isEmpty()){
				Number nodeId = queue.poll();
				visting.add(nodeId);
				if(nodeId.equals(-1)){
					visting.remove(nodeId);
					if(curLevel >= minLevel && curLevel <= maxLevel){
						nodeSet.addAll(visting);
						visting.clear();
					}
					queue.add(-1);
					curLevel++;
					if(curLevel > maxLevel){
						break;
					}else{
						continue;
					}
				}
				
				List<Edge> adjEdgeList = this.getAdjEdgeList(nodeId);
				if(adjEdgeList != null){
					for(Edge adjEdge:adjEdgeList){
						if(adjEdge.getAttr("type").equals("p2c")){
							Number adjNodeId = adjEdge.getPostNodeId();
//							if(!visited.contains(adjNodeId)){
								queue.add(adjNodeId);
//								visited.add(adjNodeId);
//							}
						}

					}//for
				}//if
			}
		}

		return nodeSet;
	}
	/*
	 * 获取当前的层次结构的深度
	 */
	private int _getDeepth(Number rootId){
//		D.p("get depth!");
		Queue<Number> queue = new LinkedList<Number>();
		queue.add(rootId);
		queue.add(-1);
		Set<Number> visited = new HashSet<Number>();
		visited.add(rootId);
		int curLevel = 0;
		while(!queue.isEmpty()){
			Number nodeId = queue.poll();
			if(nodeId.equals(-1)){
				if(queue.isEmpty()){
					break;
				}
				queue.add(-1);
				curLevel++;
				continue;
			}
			List<Edge> adjEdgeList = this.getAdjEdgeList(nodeId);
			if(adjEdgeList != null){
				for(Edge adjEdge:adjEdgeList){
					if(adjEdge.getAttr("type").equals("p2c")){
						Number adjNodeId = adjEdge.getPostNodeId();
//						if(!visited.contains(adjNodeId)){
							queue.add(adjNodeId);
//							visited.add(adjNodeId);
//						}
					}
				}//for
			}//if
		}
		D.p("depth: "+curLevel);
		return curLevel;
	}
	/*
	 * 获取当前的层次结构的深度
	 */
	private boolean _isHasNode(Number rootId, Number tempId){
//		D.p("get depth!");
		Queue<Number> queue = new LinkedList<Number>();
		queue.add(rootId);
		queue.add(-1);
		Set<Number> visited = new HashSet<Number>();
		visited.add(rootId);
		int curLevel = 0;
		while(!queue.isEmpty()){
			Number nodeId = queue.poll();
	/*		if(nodeId.equals(-1)){
				if(queue.isEmpty()){
					break;
				}
				queue.add(-1);
				curLevel++;
				continue;
			}*/
			List<Edge> adjEdgeList = this.getAdjEdgeList(nodeId);
			if(adjEdgeList != null){
				for(Edge adjEdge:adjEdgeList){
					if(adjEdge.getAttr("type").equals("p2c")){
						Number adjNodeId = adjEdge.getPostNodeId();
						if(!visited.contains(tempId)){
							queue.add(adjNodeId);
							visited.add(adjNodeId);
						}else{
							return true;
						}
					}
				}//for
			}//if
		}
		return false;
	}
}
