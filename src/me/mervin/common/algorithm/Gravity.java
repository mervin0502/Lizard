package me.mervin.common.algorithm;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import me.mervin.core.Network;
import me.mervin.core.Global.NetType;
import me.mervin.core.Global.NumberType;
import me.mervin.module.feature.Degree;
import me.mervin.module.feature.PCB;
import me.mervin.util.D;
import me.mervin.util.FileTool;


 /**
 *   Gravity.java
 *    
 *  @author Mervin.Wong  DateTime 2013-11-28 下午6:21:24    
 *  @version 0.4.0
 */
public class Gravity {
	/*
	 * network class
	 */
	private Network net = null;

	/**
	 */
	public Gravity() {
		// TODO 自动生成的构造函数存根
	}
	public Gravity(Network net) {
		// TODO 自动生成的构造函数存根
		this.net = net;
	}

	
	public void script(String dstFile){
		this.script(this.net, dstFile);
	}
	public void script(Network net, String dstFile){
		//Map<Pair<Number>, Number> temp = new HashMap<Pair<Number>, Number>();
		Degree d = new Degree(net);
		Set<Number> nodeIdSet = net.getAllNodeId();
		Map<Number, Number> degreeMap = d.nodeDegree(nodeIdSet);// the degree of all nodes in the net
		//Set<Pair<Number>> pairSet = new HashSet<Pair<Number>>();
		StringBuffer sb = new StringBuffer();
		
		PCB pcb = new PCB();
		Map<Number, Number> distanceMap = null;
		//Pair<Number> pair = null;
		double p = 0;
		Number nodeId1=null, nodeId2 = null;
		
		FileTool ft = new FileTool();
		int i = 0;		
		for(Iterator<Number> it1 = nodeIdSet.iterator(); it1.hasNext();){
			nodeId1 = it1.next();
			distanceMap = pcb.shortestPath(net, nodeId1);
			for(Iterator<Number> it2 = distanceMap.keySet().iterator(); it2.hasNext();){
				nodeId2 = it2.next();
				//pair = new Pair<Number>(nodeId1, nodeId2, true);
/*				if(!temp.containsKey(pair)){
					p = degreeMap.get(nodeId1).intValue()*degreeMap.get(nodeId2).intValue()/Math.pow(distanceMap.get(nodeId2).doubleValue(), 2);
					temp.put(pair, p);
				}*/
				//if(!pairSet.contains(pair)){
					p = degreeMap.get(nodeId1).intValue()*degreeMap.get(nodeId2).intValue()/Math.pow(distanceMap.get(nodeId2).doubleValue(), 2);
				//	pairSet.add(pair);
					sb.append(nodeId1).append("\t").append(nodeId2).append("\t").append(p).append("\r\n");
			//	}
			}
			ft.write(sb, dstFile, true);
			sb.delete(0, sb.length());
			D.p(nodeIdSet.size()-(++i));
		}
		
	}
	/**
	 *  
	 *  @param args
	 */
	public static void main(String[] args) {
		// TODO 自动生成的方法存根
		Gravity g = new Gravity();
		String srcFile = "../data/1.txt";
		String dstFile = "../data/gravity.txt";
		g.script(new Network(srcFile, NetType.UNDIRECTED, NumberType.INTEGER), dstFile);
/*		StringBuffer sb = new StringBuffer();
		for(Pair<Number> p : temp.keySet()){
			sb.append(p.getL()).append("\t").append(p.getR()).append("\t").append(temp.get(p)).append("\r\n");
		}
		new FileTool().write(sb, dstFile);*/
	}

}
