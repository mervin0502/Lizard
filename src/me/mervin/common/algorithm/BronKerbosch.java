package me.mervin.common.algorithm;

import java.util.HashSet;
import java.util.Set;

import me.mervin.core.Network;
import me.mervin.core.Global.NetType;
import me.mervin.core.Global.NumberType;
import me.mervin.module.feature.Degree;
import me.mervin.util.D;
import me.mervin.util.FileTool;
import me.mervin.util.MapTool;
import me.mervin.util.MathTool;


 /**
 *   BronKerbosch.java
 *    
 *  @author Mervin.Wong  DateTime 2013-11-27 下午3:40:04    
 *  @version 0.4.0
 *  
 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 *  REF:  1. Bron, Coen; Kerbosch, Joep (1973), "Algorithm 457: finding all cliques of an undirected graph", Commun. ACM (ACM) 16 (9): 575–577, doi:10.1145/362342.362367. 
 *  		2. http://en.wikipedia.org/wiki/Bron%E2%80%93Kerbosch_algorithm
 *  		3. http://www.cnblogs.com/yefeng1627/archive/2013/03/31/2991592.html
 */
public class BronKerbosch {
	/*
	 * network class
	 */
	private Network net = null;
	/*
	 * Degree class
	 */
	private Degree d = null;
	
	public Set<Set<Number>> maxiamlCliqueSet = new HashSet<Set<Number>>();

	/**
	 * initialization function
	 */
	public BronKerbosch() {
		// TODO 自动生成的构造函数存根
	}
	public BronKerbosch(Network net) {
		// TODO 自动生成的构造函数存根
		this.net = net;
	}

	/*
	 * 
	 */
	public Set<Set<Number>> script(){
		return this.script(this.net);		
	}
	/*
	 * 
	 */
	public Set<Set<Number>> script(Network net){
		/*
		 * node set
		 */
		Set<Number> r = new HashSet<Number>();
		Set<Number> p = null;
		Set<Number> x = new HashSet<Number>();
		p = net.getAllNodeId();
		this.d = new Degree(net);
		this._maximalClique(r, p, x);
		return this.maxiamlCliqueSet;
	}
	
	public boolean _maximalClique(Set<Number> r, Set<Number> p, Set<Number> x){
		/*
		 * if P and X are empty, R is a maximal clique
		 */
		if(p.isEmpty() && x.isEmpty()){
			//p(r);
			this.maxiamlCliqueSet.add(r);
			return true;
		} 
		/*
		 * choose a pivot node u in the union between P and X 
		 */
		Set<Number> tempSet = MathTool.union(p, x);
		//D.p(tempSet);
		Number tempNodeId = new MapTool().sort(this.d.nodeDegree(tempSet), false, false).getL(0);
		/*
		 * P-N(tempNodeId)
		 */
		tempSet = MathTool.subtraction(p, net.getAdjNodeId(tempNodeId));
		/*
		 * iterator the tempSet
		 */
		for(Number nodeId:tempSet){
			this._maximalClique(MathTool.union(r, nodeId), MathTool.intersection(p, net.getAdjNodeId(nodeId)), MathTool.intersection(x, net.getAdjNodeId(nodeId)));
			p.remove(nodeId);
			x.add(nodeId);
		}
		
		return false;
	}
	/**
	 *  
	 *  @param args
	 */
	public static void main(String[] args) {
		// TODO 自动生成的方法存根
		String srcFile = "../data/bk-1.txt";
		String dstFile = "../data/bk-r.txt";
		BronKerbosch bk = new BronKerbosch(new Network(srcFile, NetType.UNDIRECTED, NumberType.INTEGER));
		//D.p("###");
		Set<Set<Number>> set = bk.script();
		FileTool ft = new FileTool();
		StringBuffer sb = new StringBuffer();
		for(Set<Number> set1:set){
			D.p(set1);
			ft.write(set1.toString()+"", dstFile, true);
		}
	}

}
