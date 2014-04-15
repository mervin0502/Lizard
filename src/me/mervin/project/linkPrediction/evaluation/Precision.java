package me.mervin.project.linkPrediction.evaluation;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedSet;
import java.util.TreeSet;

import me.mervin.core.Network;
import me.mervin.core.Global.NetType;
import me.mervin.util.D;
import me.mervin.util.E;
import me.mervin.util.MathTool;
import me.mervin.util.Pair;
import me.mervin.util.PairList;

public class Precision {

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
	public Precision(Map<Pair<Number>, Double> score, PairList<Number, Number> probeEdgeSet){
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
	public Precision(Map<Pair<Number>, Double> score, PairList<Number, Number> probeEdgeSet, int n) throws E{
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
		
		//排序，降序
		SortedSet<Map.Entry<Pair<Number>, Double>> sort = new TreeSet<Map.Entry<Pair<Number>, Double>>(
				new Comparator<Map.Entry<Pair<Number>, Double>>(){

					@Override
					public int compare(Entry<Pair<Number>, Double> o1,
							Entry<Pair<Number>, Double> o2) {
						// TODO Auto-generated method stub
						double d1 = o1.getValue();
						double d2 = o2.getValue();
						if(d1 < d2){
							return 1;
						}else{
							return -1;
						}
					}
					
				}
				);
		sort.addAll(this.score.entrySet());
		//D.p(sort);
		Map.Entry<Pair<Number>, Double> m = null;
		Pair<Number> p = null;
		int i = 1, k = 0;
		Number l, r;
		for(Iterator<Map.Entry<Pair<Number>, Double>> it = sort.iterator(); it.hasNext();){
			if(this.n < i){
				break;
			}
			m = it.next();
			p = m.getKey();
			l = p.getL();
			r = p.getR();
			if(this.probeEdgeSet.contains(l, r) || this.probeEdgeSet.contains(r, l)){
				k++;
			}
			i++;
		}
		
		quota = (double)k/this.n;
		return quota;
	}
	
	

}
