package me.mervin.module.feature;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import me.mervin.core.Network;
import me.mervin.core.Global.NetType;
import me.mervin.util.D;

public class Coreness {
	private Network net = null;
	//private HashMap<Number, Integer> nodesDegreeMap = new HashMap<Number, Integer>();
	/**
	 * 构造函数
	 */
	public Coreness(){
		
	}
	public Coreness(Network net){
		this.net = net;
	}
	/**
	 *  nodeCore
	 *  单个节点的核
	 * @param  nodeId
	 * @return int
	 */
	public int nodeCore(Number nodeId){
		return this.nodeCore(this.net, nodeId);
	}
	/**
	 *  
	 *  单个节点的核
	 * @param  net
	 * @param  nodeId
	 * @return int
	 */
	public int nodeCore(Network net, Number nodeId){
		Network net3 = net.copyNet();
		Set<Number> nodeSet = net3.getAllNodeId();
		int i = 1;
		while(true){
			if(!nodeSet.contains(nodeId)){
				return i-2;
			}
			if(net3.nodeNum <= 0){
				D.s("节点："+nodeId+"不存在！");
				return 0;
			}
			_delNode(net3, i);
			i++;
		}
	}
	/**
	 *  
	 *  多个节点的核
	 * @param  nodesId
	 * @return HashMap<Number, Integer>
	 */
	public Map<Number, Number>  nodeCore(Set<Number> nodesId){
		return this.nodeCore(this.net, nodesId);
	}
	/**
	 *  
	 *  多个节点的核
	 * @param  net
	 * @param  nodesId
	 * @return  HashMap<Number, Number>
	 */
	public Map<Number, Number>  nodeCore(Network net, Set<Number> nodesId){
		Set<Number> nodeIdSet = new HashSet<Number>(nodesId);
		
		Network net3 = net.copyNet();
		Set<Number> allNodeIdSet = net3.getAllNodeId();
		Map<Number, Number> nodeCore = new HashMap<Number, Number>();//节点的核
		Number nodeId = null;
		int i = 1;
		while(true){
			if(nodeIdSet.size() <= 0){
				break;
			}
			for (Iterator<Number> iterator = nodeIdSet.iterator(); iterator.hasNext();) {
				nodeId = (Number) iterator.next();
				if(!allNodeIdSet.contains(nodeId)){
					nodeCore.put(nodeId, i-2);
					iterator.remove();
				}
			}
			if(net3.nodeNum <= 0){
				break;
			}
			_delNode(net3, i);
			i++;
		}
		return nodeCore;
	}
	/**
	 * 节点的邻接点的核
	 *  @param nodeId
	 *  @return Map<Number, Number>
	 */
	public Map<Number, Number> adjNodeCore(Number nodeId){
		return this.adjNodeCore(this.net, nodeId);
	}
	/**
	 * 
	 *  节点的邻接点的核
	 *  @param net
	 *  @param nodeId
	 *  @return Map<Number, Number>
	 */
	public Map<Number, Number> adjNodeCore(Network net, Number nodeId){
		Set<Number> adjNodeSet = net.getAdjNodeId(nodeId);
		return this.nodeCore(net, adjNodeSet);
	}
	/**
	 * 
	 *  多个节点的邻接点的核
	 *  @param nodeIdSet
	 *  @return Map<Number, Number>
	 */
	public Map<Number, Number> adjNodeCore(Set<Number> nodeIdSet){
		return this.adjNodeCore(this.net, nodeIdSet);
	}
	/**
	 * 
	 * 多个节点的邻接点的核 
	 *  @param net
	 *  @param nodeIdSet
	 *  @return Map<Number, Number>
	 */
	public Map<Number, Number> adjNodeCore(Network net, Set<Number> nodeIdSet){
		Set<Number> adjNodeSet = net.getAdjNodeId(nodeIdSet);
		return this.nodeCore(net, adjNodeSet);
	}
	/**
	 *  netCore
	 *  网络的核:k-core
	 * @return int
	 */
	public int netCore(){
		return this.netCore(this.net);
	}
	/**
	 *  netCore
	 *  网络的核:k-core
	 * @param  net 网络
	 * @return int
	 */
	public int netCore(Network net){
		int k = 0, i = 1;
		Network net3 = net.copyNet();
		while(true){
			if(net3.nodeNum <= 0){
				k = i-1;
				break;
			}
			this._delNode(net3, i);
			i++;
		}
		return k;
	}
	
	/**
	 *  
	 *  k核网络
	 * @param  k
	 * @return Network
	 */
	public Network kCoreNet(int k){
		return this.kCoreNet(this.net, k);
	}
	
	/**
	 *  
	 *  k核网络
	 * @param  net
	 * @param  k
	 * @return Network
	 */
	public Network kCoreNet(Network net, int k){
		Network net3 = net.copyNet();
		_delNode(net3, k);
		return net3;		
	}
	
	
	
	
	/*
	 *  
	 * @param 删除网络中所有度为k的节点
	 * @param Network netCopy this.net网络的副本
	 * @param k 
	 * @return HashSet<Number>
	 */
	private HashSet<Number> _delNode(Network netCopy, int k){
		HashSet<Number> delNodeSet = new HashSet<Number>();//要删除的节点
		Set<Number> allNodeSet = new HashSet<Number>(netCopy.getAllNodeId());//所有节点
		
		Number nodeId = null;
		int degreeNum = 0;//当前节点的度值
		boolean flag = false;
		
		Degree degree = new Degree(netCopy); 
		do{
			flag = false;
			for (Iterator<Number> iterator = allNodeSet.iterator(); iterator.hasNext();) {
				nodeId = (Number) iterator.next();
				if(Network.netType.equals(NetType.DIRECTED)){
					//对于有向网络，以从入度的角度去分析
					degreeNum = degree.nodeInDegree(nodeId);
				}else{
					degreeNum = degree.nodeDegree(nodeId);
				}
				if(degreeNum <= k){
					flag = true;
					delNodeSet.add(nodeId);
					netCopy.deleteNode(nodeId);
					iterator.remove();
				}
			}			
		}while(flag && (allNodeSet.size() > 0));
		return delNodeSet;
	}
	
}
