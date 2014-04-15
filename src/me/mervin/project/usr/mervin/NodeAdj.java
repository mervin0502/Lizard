package me.mervin.project.usr.mervin;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import me.mervin.core.Global.NumberType;
import me.mervin.util.FileTool;

public class NodeAdj {

	/**
	 *  
	 *  @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		NodeAdj na = new NodeAdj();
		na.adjDegreeAndCore();
	}

	/*
	 * 邻接点的度 核 的变化
	 */
	public void adjDegreeAndCore(){
		FileTool f = new FileTool();
		
		String srcDir = "../data/AS-2009-2012/";
		String birthNodeFile1 = null;//核
		String birthNodeFile2 = null;//度
		String birthNodeAdjFile = null;//邻接点核
		String birthNodeAdjFile2 = null;//邻接点度
		
		Map<Number, Number> core = null;
		Map<Number, Number> degree = null;
		Map<Number, Number> adjCore = null;
		Map<Number, Number> adjDegree = null;
		
		Map<Number, LinkedList<Number>> coreMap = null; 
		Map<Number, LinkedList<Number>> degreeMap = null; 
		
		Number nodeId = null;
		Number c, d = null;
		for(int i = 1; i <= 47; i++){
			//读取新生节点
			birthNodeFile1 = srcDir+"birth-degree-core/0.0/birth-core-"+i+"-"+(i+1)+"-0.0.txt";
			birthNodeFile2 = srcDir+"birth-degree-core/0.0/birth-degree-"+i+"-"+(i+1)+"-0.0.txt";			
			core = f.read2Map(birthNodeFile1, 1, 2, NumberType.DOUBLE);
			degree = f.read2Map(birthNodeFile2, 1, 2, NumberType.DOUBLE);
			
			coreMap = new HashMap<Number, LinkedList<Number>>();
			degreeMap = new HashMap<Number, LinkedList<Number>>();
			for(int j = i+1 ; j<= 48; j++){
				//新生节点的变化
				birthNodeAdjFile = srcDir+"adjNode-degree-core/"+i+"-"+(i+1)+"/birthNodeAdjeDegree-Core-2-"+j+".txt";
				adjCore = f.read2Map(birthNodeAdjFile, 1, 4, NumberType.DOUBLE);
				adjDegree = f.read2Map(birthNodeAdjFile, 1, 3, NumberType.DOUBLE);
				for(Iterator<Number> it = core.keySet().iterator(); it.hasNext();){
					nodeId = it.next();
					if(adjCore.containsKey(nodeId)){
						c = adjCore.get(nodeId);
						d = adjDegree.get(nodeId);
					}else{
						c = -1;
						d = 0;
					}
					if(coreMap.containsKey(nodeId)){
						coreMap.get(nodeId).add(c);
						degreeMap.get(nodeId).add(d);
					}else{
						LinkedList<Number> ll1 = new LinkedList<Number>();
						LinkedList<Number> ll2 = new LinkedList<Number>();
						ll1.add(c);
						ll2.add(d);
						coreMap.put(nodeId, ll1);
						degreeMap.put(nodeId, ll2);
					}
				}
			}
			
			//写入文件
			StringBuffer sb1 = new StringBuffer();
			StringBuffer sb2 = new StringBuffer();
			LinkedList<Number> ll = null;
			for(Iterator<Number> it = core.keySet().iterator();it.hasNext();){
				nodeId = it.next();
				//core
				sb1.append(nodeId).append("\t");//id
				sb1.append(core.get(nodeId)).append("\t");//node avg core
				ll = coreMap.get(nodeId);
				for(Iterator<Number> it2 = ll.iterator(); it2.hasNext();){
					sb1.append(it2.next()).append("\t");//node adj core
				}
				sb1.append("\r\n");
				
				//degree
				sb2.append(nodeId).append("\t");//id
				sb2.append(degree.get(nodeId)).append("\t");//node avg degree
				ll = degreeMap.get(nodeId);
				for(Iterator<Number> it2 = ll.iterator(); it2.hasNext();){
					sb2.append(it2.next()).append("\t");//node adj core
				}
				sb2.append("\r\n");
			}
			f.write(sb1, srcDir+"birth-degree-core/adj/birth-adj-degree-core-"+i+"-"+(i+1)+".txt");
			f.write(sb2, srcDir+"birth-degree-core/adj/birth-adj-degree-degree-"+i+"-"+(i+1)+".txt");
		}
	}
}
