package me.mervin.common.algorithm;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import me.mervin.core.Network;
import me.mervin.module.feature.Degree;
import me.mervin.util.MapTool;
import me.mervin.util.MathTool;
import me.mervin.util.PairList;


 /**
  *
 * 	边重连算法(Degree-preserving rewiring)
 *   DPR.java
 *    
 *  @author Mervin.Wong  DateTime 2013-10-13 下午8:59:27    
 *  @version 0.4.0
 *  ************************************************************
 *  
 */
public class DPR {

	/*
	 * 网络拓扑对象
	 */
	private Network net = null;
	
	/*
	 * 改变匹配系数：true:同配;false:异配
	 */
	private boolean isAssortativity = true;
	/*
	 * 重连的比例
	 * count = p*edgeNum
	 */
	private double p = 1;
	/**
	 * 构造方法
	 */
	public DPR(){
		
	}
	public DPR(Network net){
		this.net = net;
	}
	public DPR(Network net, boolean isAssortativity){
		this.net = net;
		this.isAssortativity = isAssortativity;
	}
	
	public void script(Network net, double p, boolean isAssotativity){
		
	}
	

	public void script(Network net, PairList<Number, Number> edgeList, boolean isAssortativity){
		
	}
	
	public void script(Network net, PairList<Number, Number> edgeList, double p, boolean isAssortativity){
		int edgeNum = edgeList.size();
		int i = 0, j = 0, curIndex = 0;
		int n = (int) Math.round(edgeNum*p);
		
		Number[] nodeArr11 = new Number[4];
		Degree d = new Degree();
		Set<Number> nodeSet = null;
		Map<Number, Number> nodeMap = null;
		for(int k = 0; k < n;){
			i = MathTool.random(0, edgeNum-1).intValue();//边的索引号
			j = MathTool.random(0, edgeNum-1).intValue();//边的索引号
			
			/*
			 * 获取两条边，四个节点
			 * 0-1 2-3
			 */
			nodeArr11[0] = edgeList.getL(i);
			nodeArr11[1] = edgeList.getR(i);
			nodeArr11[2] = edgeList.getL(j);
			nodeArr11[3] = edgeList.getR(j);
			
			/*
			 * 按度值排序 降序
			 */
			nodeSet = new HashSet<Number>();
			nodeSet.add(nodeArr11[0]);
			nodeSet.add(nodeArr11[1]);
			nodeSet.add(nodeArr11[2]);
			nodeSet.add(nodeArr11[3]);
			nodeMap = d.nodeAdjDegree(net, nodeSet);
			//
			Number[] nodeArr2 = this._sort(nodeMap);
		
			/*
			 * 检查是否存在公共节点
			 */
			if(nodeArr11[0].equals(nodeArr11[2])){
				continue;
			}
			if(nodeArr11[0].equals(nodeArr11[3])){
				continue;
			}
			if(nodeArr11[1].equals(nodeArr11[2])){
				continue;
			}
			if(nodeArr11[1].equals(nodeArr11[3])){
				continue;
			}
			
			/*
			 * 0-1 2-3有4中连接可能
			 * 0-2 1-3;0-3 1-2;
			 * 0-1 2-3不存在最大和次大度值的节点已连或者最小和次小度值节点已连
			 */
			
			/*
			 * 删除0-1 2-3 依然连通
			 */
			
			
			
		}
		
		
		
	}
	
	/*
	 * 
	 * 对节点按nodeMap的value值降序排序  
	 *  @param nodeMap nodeId->degree
	 *  @return Number[]
	 */
	private Number[] _sort(Map<Number, Number> nodeMap){
		Number[] nodeArr2 = new Number[4];
		
		PairList<Number, Number> pl = new MapTool().sort(nodeMap, false, false);
		for(int i = 0; i < 4; i++){
			nodeArr2[i] = pl.getL(i);
		}
		return nodeArr2;
	}
	/**
	 *  
	 *  @param args
	 */
	public static void main(String[] args) {
		// TODO 自动生成的方法存根

	}

}
