package me.mervin.project.usr.mervin;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Map.Entry;

import me.mervin.core.Network;
import me.mervin.core.Global.NetType;
import me.mervin.core.Global.NumberType;
import me.mervin.model.BANetwork;
import me.mervin.model.internet.PFP;
import me.mervin.module.feature.Degree;
import me.mervin.module.linkPrediction.LinkPrediction;
import me.mervin.project.linkPrediction.evaluation.AUC;
import me.mervin.project.linkPrediction.evaluation.Precision;
import me.mervin.project.linkPrediction.similarity.local.CN;
import me.mervin.project.linkPrediction.similarity.local.RA;
import me.mervin.project.linkPrediction.similarity.quasi.LP;
import me.mervin.project.linkPrediction.similarity.quasi.LRW;
import me.mervin.util.D;
import me.mervin.util.E;
import me.mervin.util.FileTool;
import me.mervin.util.Pair;
import me.mervin.util.PairList;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
/*		BANetwork ba = new BANetwork(5);
		ba.setNetType(NetType.UNDIRECTED);
		ba.setNumberType(NumberType.INTEGER);
		ba.setNetParam(5000, 2);
		ba.createModelNetwork();
		Degree d = new Degree(ba);
		
		new FileTool().write(ba.traverseEdge(), "../data/ba-net.txt");
		new FileTool().write(d.netDegreeDistributionRate(), "../data/ba.txt");*/
		//D.p((double)48/47);
		///D.p(0.5%0.2);
/*		String str = "aaaa";
		try {
			BufferedWriter w = new BufferedWriter(new FileWriter("../data/t.txt", true));
			w.append(str);
			w.flush();
			w.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
//		D.p(32.875%0.25);
/*		
		//网络节点与对应的度值
		SortedMap<Number, Number> sortedMap = new TreeMap<Number, Number>();
		//sortedMap.putAll(this.nodesDegree(net, net.getAllNodeId()));
		Map<Number, Number> m = new HashMap<Number, Number>();
		m.put(11, 1);
		m.put(12, 9);
		m.put(32, 5);
		m.put(5, 4);
		m.put(45, 9);
		sortedMap.putAll(m);
		final boolean des = false;
		
		 * 按照节点的度值来排序网络网络节点
		 
		SortedSet<Map.Entry<Number, Number>> sortedSet = new TreeSet<Map.Entry<Number, Number>>(
	            new Comparator<Map.Entry<Number, Number>>() {
	                @Override
	                public int compare(Map.Entry<Number, Number> e1, Map.Entry<Number, Number> e2) {
	                    //return -e1.getValue().compareTo(e2.getValue());
	                	int d1 = e1.getValue().intValue();
	                	int d2 = e2.getValue().intValue();
	                	if(des){
	                		//由大到小
		                	if(d1 < d2){
		                		return 1;
		                	}else if(d1 == d2){
		                		return 0;
		                	}else{
		                		return -1;
		                	}
	                	}else{
	                		//由小到大
		                	if(d1 > d2){
		                		return 1;
		                	}else if(d1 == d2){
		                		return 0;
		                	}else{
		                		return -1;
		                	}
	                	}

	                }
	            });
		sortedSet.addAll(sortedMap.entrySet());//网络中的节点按度排序
		
		Entry<Number, Number> e = null;
		for(Iterator<Entry<Number, Number>> it = sortedSet.iterator(); it.hasNext();){
			e = it.next();
			D.p(e.getKey()+"###"+e.getValue());
		}
		*/
		
/*		PFP pfp = new PFP(5);
		pfp.setParam(6000, 0.3, 0.1, 0.048);
		pfp.createModelNetwork();
		Degree d = new Degree(pfp);
		new FileTool().write(d.netDegreeDistributionRatio(), "../data/AS-2009-2012/pfp.txt");*/
		
/*		Map<Integer, Integer> m = new HashMap<Integer, Integer>();
		
		int k = 1;
		m.put(k, 2);
		k = 3 ;
		m.put(k, 4);
		D.p(m.toString());*/
		
/*		BANetwork ba = new BANetwork(5);
		ba.setNetParam(3000, 2);
		ba.createModelNetwork();
		Degree d = new Degree(ba);
		new FileTool().write(d.netDegreeDistributionRatio(), "../data/AS-2009-2012/pfp.txt");*/
		
		//Network net = new Network("../data/test/2014.txt", NetType.UNDIRECTED, NumberType.INTEGER);
		Network net = new Network("../data/test/karate.txt", NetType.UNDIRECTED, NumberType.INTEGER);
		LinkPrediction lp2 = new CN(net);
		lp2.script();
		try {
			lp2.auc();
		} catch (E e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		//Network net = new Network("../data/test/power.txt", NetType.UNDIRECTED, NumberType.INTEGER);
		try {
			//LinkPrediction lp = new CN(net, 6);
			LinkPrediction lp = new LRW(net, 2);
			//D.p(lp.probeEdgeSet());
			for(int i = 0; i < lp.probeEdgeSet().size(); i++){
				//D.p("@@@@"+lp.probeEdgeSet().getL(i)+"###"+lp.probeEdgeSet().getR(i));
			}
			//D.p(lp.aucAvg());
			
			D.p(lp.precisionAvg());
//			LinkPrediction lp2 = new CN(net);
			D.p(lp2.precisionAvg());
			AUC auc = new AUC(lp.script(), lp.probeEdgeSet(), 10);
			D.p(auc.script());
			
			Precision p = new Precision(lp.script(), lp.probeEdgeSet(), 20);
			D.p(p.script());
		} catch (E e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
/*		Pair<Integer> p = null;
		Map<Pair<Integer>, Integer> map = new HashMap<Pair<Integer>, Integer>();
		PairList<Pair<Integer>, Integer> pl = new PairList<Pair<Integer>, Integer>();
		p = new Pair<Integer>(1, 2);
		map.put(p, 1);
		pl.add(p,1);
		D.p(p.hashCode());
		p = new Pair<Integer>(2, 1);
		map.put(p, 2);
		pl.add(p,2);
		D.p(p.hashCode());
		D.p(map);
		D.p(pl.getLs());
		D.p(pl.getRs());*/
		
		T t = new T();
		
		new Thread(t).start();
		new Thread(t).start();
		new Thread(t).start();
		new Thread(t).start();
	}
	

}

class T implements Runnable{
	
	private int n = 20;

	@Override
	public void run() {
		// TODO 自动生成的方法存根
//		synchronized(this){
		while(true){
			synchronized(this){
				if(n > 0 && n < 5){
					try{
						Thread.sleep(20000);
					}catch(Exception e){
						
					}
					System.out.println(Thread.currentThread().getName()+"Number:"+(n--));
				}else if(n >= 5){
					System.out.println(Thread.currentThread().getName()+"###Number:"+(n--));
				}else
				{
					System.out.println(Thread.currentThread().getName()+"Existed");
					break;
				}
				
			}
			
		}
	}
	
}

