package me.mervin.model;

import java.util.Set;

import me.mervin.core.NetModel;
import me.mervin.core.Global.NetType;
import me.mervin.core.Global.NumberType;
import me.mervin.util.D;


 /**
 *   ER.java
 *    
 *  @author Mervin.Wong  DateTime 2013-10-17 下午2:24:15    
 *  @version 0.4.0
 */
public class ER extends NetModel {

	/*
	 * ER 的节点数
	 */
	private int addNodeNum = 0;
	
	/*
	 * ER 的边数
	 */
	private int addEdgeNum = 0;
	/*
	 * 节点连边概率
	 */
	private double p = 0;
	/*
	 * NM:节点和边的数量来构造网络
	 * NP:节点以及连边概率来构造网络
	 */
	private enum AlgType{NM, NP};
	private AlgType type = null; 
	/**
	 */
	public ER() {
		// TODO 自动生成的构造函数存根
	}
	/**
	 */
	public ER(NetType netType) {
		// TODO 自动生成的构造函数存根
		super(netType);
	}

	/**
	 * @param initNodeNum
	 */
	public ER(int initNodeNum) {
		super(initNodeNum);
		// TODO 自动生成的构造函数存根
	}

	/**
	 * @param initNodeNum
	 * @param netType
	 */
	public ER(int initNodeNum, NetType netType) {
		super(initNodeNum, netType);
		// TODO 自动生成的构造函数存根
	}

	/**
	 * @param netFile
	 * @param netType
	 */
	public ER(String netFile, NetType netType) {
		super(netFile, netType);
		// TODO 自动生成的构造函数存根
	}

	/**
	 * @param netFile
	 * @param netType
	 * @param numberType
	 */
	public ER(String netFile, NetType netType, NumberType numberType) {
		super(netFile, netType, numberType);
		// TODO 自动生成的构造函数存根
	}

	public void set(int addNodeNum, int addEdgeNum){
		this.addNodeNum = addNodeNum;
		this.addEdgeNum = addEdgeNum;
		this.type = AlgType.NM;
	}
	
	public void set(int addNodeNum, double p){
		this.addNodeNum = addNodeNum;
		this.p = p;
		this.type = AlgType.NP;
	}
	/* (non-Javadoc)
	 * @see core.NetModel#createModelNetwork()
	 */
	@Override
	public void createModelNetwork() {
		// TODO 自动生成的方法存根
		if(this.type.equals(AlgType.NM)){
			this._createNetByNM();
		}else{
			this._createNetByNP();
		}
	}

	
	private void _createNetByNM(){
		/*
		 * 节点ID从0开始
		 */
		
		for(int i = 0; i < this.addNodeNum; i++){
			this.insertNode(i);
		}
		
		Number l = null, r = null;
		for(int j = 0; j < this.addEdgeNum; j++){
			
			do{
				l = this.getRandNodeId();
				r = this.getRandNodeId();
				
			}while(l.equals(r) || this.isHasEdge(l, r));
			
			this.insertEdge(l, r);
		}
	}
	/*
	 * 
	 */
	private void _createNetByNP(){
		/*
		 * 节点ID从0开始
		 */
		
		for(int i = 0; i < this.addNodeNum; i++){
			this.insertNode(i);
		}
		Set<Number> nodeIdSet = this.getAllNodeId();
		double q = 0;
		for(Number l:nodeIdSet){
			for(Number r:nodeIdSet){
				if(!l.equals(r) && !this.isHasEdge(l, r)){
					q = Math.random();
					if(q < this.p){
						this.insertEdge(l, r);
						//D.p(l+"###"+r);
					}//if
				}//if
					
			}//for
		}//for
		//D.p("###");
	}
}
