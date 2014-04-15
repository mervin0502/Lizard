package me.mervin.model.internet;

import java.util.Map;

import me.mervin.core.NetModel;
import me.mervin.core.Network;
import me.mervin.core.Global.NetType;
import me.mervin.core.Global.NumberType;
import me.mervin.module.feature.Degree;
import me.mervin.util.D;
import me.mervin.util.MapTool;
import me.mervin.util.MathTool;
import me.mervin.util.PairList;

/**
 * 
 *   PFP.java
 *   PFP模型
 *  @author Mervin.Wong  DateTime 2013-6-26 下午12:42:40    
 *  @version 0.4.0
 */
public class PFP extends NetModel {

	/*
	 * ***********************************
	 * 网络参数
	 */
	/*
	 * 增加节点的数量
	 */
	private int addNodeNum = 0;
	/*
	 * 当前以增加的节点数量
	 */
	private int curNodeNum = 0;
	/*
	 * 算法中的参数
	 */
	private double p = 0.3;
	private double q = 0.1;
	private double r = 0;
	private double s = 0.048;//最优值
	
	/*
	 * ***************************************
	 * 全局变量
	 */
	/*
	 * 求全网中的所有度值
	 */
	private Degree d = new Degree();
	/*
	 * 按照度值降序
	 */
	private MapTool sm = new MapTool();
	/*
	 * 保存有序的度值
	 */
	private PairList<Number, Number> pl = null;
	/**
	 * 初始化一个空的网络模型
	 */
	public PFP(){
		super();
	}
	/**
	 * 初始化一个全连通网络模型
	 * @param initNodeNum 网络模型的节点数
	 */
	public PFP(int initNodeNum){
		super(initNodeNum);
	}
	/**
	 * 初始化已给出的网络模型
	 * @param netFile 网络文件
	 * @param netType 网络类型:有向，无向
	 */
	public PFP(String netFile, NetType netType){
		super(netFile, netType);
	}
	/**
	 * 初始化已给出的网络模型
	 * @param netFile 网络文件
	 * @param netType 网络类型：有向，无向
	 * @param numberType 节点ID类型
	 */
	public PFP(String netFile, NetType netType, NumberType numberType){
		super(netFile, netType, numberType);
	}
	
	/**
	 * 
	 *  初始化算法中的参数
	 *  @param addNodeNum 增加的节点数量
	 *  @param p 
	 *  @param q
	 */
	public void set(int addNodeNum, double p, double q){
		this.addNodeNum = addNodeNum;
		this.p = p;
		this.q = q;
		this.r = 1 - p -q;
	}
	/**
	 * 
	 *  初始化算法中的参数
	 *  @param addNodeNum 增加的节点数量
	 *  @param p 
	 *  @param q
	 *  @param s 
	 */
	public void set(int addNodeNum, double p, double q, double s){
		this.addNodeNum = addNodeNum;
		this.p = p;
		this.q = q;
		this.r = 1 - p -q;
		this.s = s;
	}
	/**
	 *  createModelNetwork
	 *  由BA算法来演化网络结构
	 */
	@Override
	public void createModelNetwork() {
		// TODO Auto-generated method stub
		while(true){
			_pFun();
			if(this.curNodeNum >= this.addNodeNum){
				break;
			}
			_qFun();
			if(this.curNodeNum >= this.addNodeNum){
				break;
			}
			_rFun();
			if(this.curNodeNum >= this.addNodeNum){
				break;
			}
		}
	}
	
	/*
	 * ***************************************************
	 * 新生点  选host点 加边
	 * 
	 */
	/*
	 * 以概率p新增一个点
	 */
	private void _pFun(){
		double rand = MathTool.random();
		if(rand >= this.p){
			//新增一个点
			Number newNodeId = null;
			Number oldNodeId1 = null;
			Number oldNodeId2 = null;
			if(Network.getNumberType().equals(NumberType.INTEGER)){
				newNodeId = this.maxNodeId.intValue()+1;
			}else{
				newNodeId = this.maxNodeId.longValue()+1;
			}
			this.curNodeNum++;
			
			//优先选择一个点
			oldNodeId1 = this._chooseNode();
			//插入一条边
			this.insertEdge(newNodeId, oldNodeId1);
			D.p("s11:"+newNodeId+"##"+oldNodeId1);
			//在选择一个host节点
			do{
				oldNodeId2 = this._chooseNode(); 
			}while(oldNodeId2.equals(oldNodeId1));
			this.insertEdge(oldNodeId1, oldNodeId2);
			D.p("s12:"+oldNodeId1+"##"+oldNodeId2);
		}
	}
	/*
	 * 以概率q新增一个点
	 */
	private void _qFun(){
		double rand = MathTool.random();
		if(rand >= this.q){
			//新增一个点
			Number newNodeId = null;
			Number oldNodeId1 = null;
			Number oldNodeId2 = null;
			Number oldNodeId3 = null;
			if(Network.getNumberType().equals(NumberType.INTEGER)){
				newNodeId = this.maxNodeId.intValue()+1;
			}else{
				newNodeId = this.maxNodeId.longValue()+1;
			}
			this.curNodeNum++;
			
			//优先选择一个点
			oldNodeId1 = this._chooseNode();
			//插入一条边
			this.insertEdge(newNodeId, oldNodeId1);
			D.p("s21:"+newNodeId+"##"+oldNodeId1);
			//选择一个已存在host节点
			do{
				oldNodeId2 = this._chooseNode();
			}while(oldNodeId2.equals(oldNodeId1));
//			}while(oldNodeId2.equals(oldNodeId1) || this.isHasEdge(oldNodeId1, oldNodeId2));
			this.insertEdge(oldNodeId1, oldNodeId2);
			D.p("s22:"+oldNodeId1+"##"+oldNodeId2);
			//选择一个已存在host节点
			do{
				oldNodeId3 = this._chooseNode();
			}while(oldNodeId3.equals(oldNodeId1)|| oldNodeId3.equals(oldNodeId2));
//			}while(oldNodeId3.equals(oldNodeId1) || this.isHasEdge(oldNodeId1, oldNodeId3) || oldNodeId3.equals(oldNodeId2) || this.isHasEdge(oldNodeId2, oldNodeId3));
			this.insertEdge(oldNodeId1, oldNodeId3);
			D.p("s23:"+oldNodeId1+"##"+oldNodeId3);
		}
	}
	/*
	 * 以概率r新增一个点
	 */
	private void _rFun(){
		double rand = MathTool.random();
		if(rand >= this.q){
			//新增一个点
			Number newNodeId = null;
			Number oldNodeId1 = null;
			Number oldNodeId2 = null;
			Number oldNodeId3 = null;
			if(Network.getNumberType().equals(NumberType.INTEGER)){
				newNodeId = this.maxNodeId.intValue()+1;
			}else{
				newNodeId = this.maxNodeId.longValue()+1;
			}
			this.curNodeNum++;
			
			//优先选择一个点
			oldNodeId1 = this._chooseNode();
			//插入一条边
			this.insertEdge(newNodeId, oldNodeId1);
			D.p("s31:"+newNodeId+"##"+oldNodeId1);
			//选择一个已存在host节点
			do{
				oldNodeId2 = this._chooseNode();
			}while(oldNodeId2.equals(oldNodeId1));
//			}while(oldNodeId2.equals(oldNodeId1) || this.isHasEdge(oldNodeId1, oldNodeId2));
			this.insertEdge(oldNodeId1, oldNodeId2);
			D.p("s32:"+oldNodeId1+"##"+oldNodeId2);
			//选择一个已存在host节点
			do{
				oldNodeId3 = this._chooseNode();
			}while(oldNodeId3.equals(oldNodeId1));
//			}while(oldNodeId3.equals(oldNodeId1) || this.isHasEdge(oldNodeId1, oldNodeId3));
			this.insertEdge(newNodeId, oldNodeId3);
			D.p("s33:"+newNodeId+"##"+oldNodeId3);
		}
	}
	
	/*
	 * ********************************************************
	 * 优先选择函数，选择一个节点
	 */
	private Number _chooseNode(){
		Number oldNodeId = null;
		//获取网络中所有节点的度
		Map<Number, Number> degree = d.nodeDegree(this, this.getAllNodeId());
		//按度值降序
		pl = sm.sort(degree, false, false);
		//优先选择函数式的分母
		double denominator = 0;
		int plSize = pl.size();
		int degreeValue = 0;
		for(int i = 0; i < plSize; i++){
			degreeValue = pl.getR(i).intValue();
			denominator += Math.pow(degreeValue, 1+this.s*Math.log10(degreeValue));
		}
		
		//系统产生一个随机数
		double rand = MathTool.random();
		//优先选择点
		int j = 0;//pl的索引号
		double numrator = 0;//分子
		double t = 0;//分子比分母
		
		while(true){
			degreeValue = pl.getR(j).intValue();
			numrator = Math.pow(degreeValue, 1+this.s*Math.log10(degreeValue));
			t = numrator/denominator;
//			D.p("@2@"+pl.getL(j)+"###"+degreeValue+"$$$"+j+"###"+plSize);
			if(rand <= t){
				oldNodeId = pl.getL(j);
				break;
			}else{
				j++;
			}
			rand = MathTool.random();
			//遍历完pl后，在重新产生一个rand
			if(j >= plSize){
				j = 0;
			}
		}
//		D.p("+++++++++++++++"+oldNodeId);
		return oldNodeId;
	}
}
