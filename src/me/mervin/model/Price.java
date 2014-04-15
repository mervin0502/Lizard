package me.mervin.model;

import java.util.ArrayList;
import java.util.Random;
import java.util.Vector;

import me.mervin.core.NetModel;
import me.mervin.core.Global.NetType;
import me.mervin.core.Global.NumberType;
import me.mervin.util.PairList;


 /**
 *   Price.java
 *    
 *  @author Mervin.Wong  DateTime 2013-10-16 下午9:04:19    
 *  @version 0.4.0
 */
public class Price extends NetModel {
	
	/*
	 *  新加入的节点的每次连边数量
	 */
	private int m = 0;
	
	/*
	 * 添加的节点数量
	 */
	private int addNodeNum = 0;
	
	/*
	 * p = m/(m+a)
	 */
	private double p = 0;

	/**
	 */
	public Price() {
		// TODO 自动生成的构造函数存根
	}

	/**
	 * @param initNodeNum
	 */
	public Price(int initNodeNum) {
		super(initNodeNum);
		// TODO 自动生成的构造函数存根
	}

	/**
	 * @param initNodeNum
	 * @param netType
	 */
	public Price(int initNodeNum, NetType netType) {
		super(initNodeNum, netType);
		// TODO 自动生成的构造函数存根
	}

	/**
	 * @param netFile
	 * @param netType
	 */
	public Price(String netFile, NetType netType) {
		super(netFile, netType);
		// TODO 自动生成的构造函数存根
	}

	/**
	 * @param netFile
	 * @param netType
	 * @param numberType
	 */
	public Price(String netFile, NetType netType, NumberType numberType) {
		super(netFile, netType, numberType);
		// TODO 自动生成的构造函数存根
	}

	public void set(int addNodeNum, int m, double p){
		this.addNodeNum = addNodeNum;
		this.m = m;
		this.p = p;
	}
	
	/* (non-Javadoc)
	 * @see core.NetModel#createModelNetwork()
	 */
	@Override
	public void createModelNetwork() {
		// TODO 自动生成的方法存根
		
		ArrayList<Number> postNodeIdList = new ArrayList<Number>();//所有边中的post节点
		PairList<Number, Number> edgeList = this.traverseEdge();//已有的边
		for(int i = 0; i < edgeList.size(); i++){
			postNodeIdList.add(edgeList.getR(i));
		}
		
		double r = 0;
		Number nodeId = null;
		Vector<Number> mNodeId = new Vector<Number>();//找到m个节点
		Random rand = null;
		int j = 0, currentNodeId = this.maxNodeId.intValue();
		for(int i = 0; i < this.addNodeNum; i++){
			currentNodeId++;
			j = 0;
			while(j < this.m){
				r = Math.random();
				rand = new Random();
				if(r < p){
					nodeId = postNodeIdList.get(rand.nextInt(postNodeIdList.size()));
				}else{
					nodeId = this.getRandNodeId();
				}
				
				if(currentNodeId != nodeId.intValue() && !mNodeId.contains(nodeId)){
					this.insertEdge(currentNodeId, nodeId);
					postNodeIdList.add(nodeId);
					j++;
				}
				mNodeId.add(nodeId);
			}//while
			mNodeId.clear();
		}

	}
}
