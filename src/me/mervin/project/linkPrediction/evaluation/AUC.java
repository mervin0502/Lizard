package me.mervin.project.linkPrediction.evaluation;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import me.mervin.util.D;
import me.mervin.util.E;
import me.mervin.util.MathTool;
import me.mervin.util.Pair;
import me.mervin.util.PairList;

public class AUC {

	/*
	 * 选择的节点对数量 
	 */
	private int n=0;
	/*
	 * 探测集
	 */
	private PairList<Number, Number> probeEdgeSet = null;
	/*
	 * 已赋值的未知边
	 */
	private Map<Pair<Number>, Double> score = null;
	/**
	 * 构造函数
	 * @param score 已赋值的未知边
	 * @param probeEdgeSet 探测集
	 */
	public AUC(Map<Pair<Number>, Double> score, PairList<Number, Number> probeEdgeSet){
		this.score = score;
		this.probeEdgeSet = probeEdgeSet;
		this.n = probeEdgeSet.size();
	}
	/**
	 * 构造函数
	 * @param score 已赋值的未知边
	 * @param probeEdgeSet 探测集
	 * @param n 选择节点对的数量
	 * @throws E 
	 */
	public AUC(Map<Pair<Number>, Double> score, PairList<Number, Number> probeEdgeSet, int n) throws E{
		this.score = score;
		this.probeEdgeSet = probeEdgeSet;
		if(n > this.probeEdgeSet.size()){
			throw new E("n大于探测集的数量");
		}else{
			this.n = n;
		}
	}
	
	public double script(){
		double quota = 0;
		PairList<Pair<Number>, Double> probeEdgeScore = new PairList<Pair<Number>, Double>();//探测边的赋值
		PairList<Pair<Number>, Double> nonexistEdgeScore = new PairList<Pair<Number>, Double>();//不存在边的赋值
		
		Number l, r;
		double d = 0;
		Pair<Number> p = null;
		// 将score分成探测边集和不存在边集
		D.p("####"+this.score.size());
		for(Iterator<Pair<Number>> it = this.score.keySet().iterator();it.hasNext();){
			p = it.next();
			l = p.getL();
			r = p.getR();
			d = this.score.get(p);
			p = new Pair<Number>(l, r);
			
			if(this.probeEdgeSet.contains(l, r) || this.probeEdgeSet.contains(r, l)){
				probeEdgeScore.add(p, d);
			}else{
				nonexistEdgeScore.add(p, d);
			}
			
		}
		
		/*
		 * 产生一个随机数
		 */
		int m1=0, m2=0;
		int s1 = probeEdgeScore.size();
		int s2 = nonexistEdgeScore.size();
		int k1, k2;
		double l1, l2;
		for(int i = 0; i < this.n; i++){
			k1 = (Integer) MathTool.random(0, s1);
			k2 = (Integer) MathTool.random(0, s2);
			
			l1 = probeEdgeScore.getR(k1);
			l2 = nonexistEdgeScore.getR(k2);
			//D.p(l1+"####"+l2);
			if(l1 > l2){
				m1++;
			}else if(l1 == l2){
				m2++;
			}else{
				
			}
		}
		quota = (double)(m1+0.5*m2)/this.n;
		return quota;
	}
	
	
/*	*//**
	 *  
	 *  @param args
	 *//*
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
*/
}
