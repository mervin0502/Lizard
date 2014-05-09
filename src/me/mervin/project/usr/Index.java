package me.mervin.project.usr;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.InetAddress;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.Stack;
import java.util.TreeSet;
import java.util.prefs.BackingStoreException;

import com.fasterxml.jackson.databind.JsonNode;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.City;
import com.maxmind.maxminddb.MaxMindDbReader;

import me.mervin.core.Network;
import me.mervin.core.Global.NetType;
import me.mervin.core.Global.NumberType;
import me.mervin.model.BANetwork;
import me.mervin.model.GloballyCoupledNet;
import me.mervin.model.LocalWorldEvolvingNet;
import me.mervin.model.NWNetwork;
import me.mervin.model.NearestNeighborCoupledNet;
import me.mervin.model.WSNetwork;
import me.mervin.module.extract.EdgeDeweigh;
import me.mervin.module.feature.ClusterCofficient;
import me.mervin.module.feature.Coreness;
import me.mervin.module.feature.Degree;
import me.mervin.module.feature.StructureEntropy;
import me.mervin.module.graphFormats.GraphML;
import me.mervin.module.graphFormats.GraphMLReader;
import me.mervin.util.D;
import me.mervin.util.FileTool;
import me.mervin.util.MapTool;
import me.mervin.util.MathTool;
import me.mervin.util.Pair;
import me.mervin.util.PairList;

/*****************************************************************************
 * 
 * Copyright [2013] [Mervin.Wong]
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 *       
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *  
 *****************************************************************************/


public class Index {
	public static void main(String[] args) throws IOException, GeoIp2Exception{
		
/*		GloballyCoupledNet net = new GloballyCoupledNet();
		net.setNetParam(10, 10);
		net.createModelNetwork();
		D.p(net.edgeNum+"##"+net.nodeNum);
		net.BFS();
		D.s();*/
/*		NearestNeighborCoupledNet net = new NearestNeighborCoupledNet();
		net.setNetParam(6, 4);
		net.createModelNetwork();
		net.BFS();*/
		
/*		WSNetwork network = new WSNetwork();
		network.setNetParam(1200, 4, (float) 0.50);
		network.createModelNetwork();
		network.BFS();*/
		
/*		NWNetwork network = new NWNetwork();
		network.setNetParam(10000, 4, (float)0.001);
		network.createModelNetwork();
		network.BFS();*/
		
/*		BANetwork network = new BANetwork(50);
		network.setNetParam(1000, 20);
		network.createModelNetwork();
		network.BFS();*/
		
/*		LocalWorldEvolvingNet net = new LocalWorldEvolvingNet(50);
		net.setNetParam(50, 15, 5);
		net.createModelNetwork();
		net.BFS();*/
		
/*		Network net = new Network("../data/net3.txt", NetType.DIRECTED);
		net.traverseEdge();
		//net.DFS();
		//D.p(net.getEdgeWeight(1, 2));
		//D.p("aaa");
		Path p = new Path(net);
		Collection<Stack<Number>> list = p.bothNodesPath(5, 6);
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			Stack<Number> stack = (Stack<Number>) iterator.next();
			for (Iterator iterator2 = stack.iterator(); iterator2.hasNext();) {
				Number number = (Number) iterator2.next();
				D.p(number+"=>");
			}
			D.m();
		}
		*/
		
		//Network net = new Network("../data/net2.txt", NetType.DIRECTED, NumberType.LONG);
/*		Network net = new Network("../data/net2.txt", NetType.DIRECTED);
		//Coreness core = new Coreness(net);
		//D.p("core"+core.netCore());
		//D.p(core.nodeCore(4));
		//core.kCoreNet(1).traverseEdge();
		Path p = new Path(net);
		Collection<Stack<Number>> list = p.bothNodesPath(1, 5);
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			Stack<Number> stack = (Stack<Number>) iterator.next();
			for (Iterator iterator2 = stack.iterator(); iterator2.hasNext();) {
				Number number = (Number) iterator2.next();
				D.p(number+"=>");
			}
			D.m();
		}
		LinkedList<Number> l = p.bothNodesShortestPath(1, 5);
		for (Iterator<Number> iterator = l.iterator(); iterator.hasNext();) {
			D.p((Number) iterator.next());
			
		}
		D.p(p.bothNodesPathLength(net, 1, 5));*/
		
		//(new ExtractIPByLink("../data/jfk-us-201201.txt","../data/201201.txt")).run();
		//D.s("提取IP链路为地址对");
		/**
		 *   地址对去重
		 */
		//(new EdgeDeweigh("../data/201201.txt","../data/201201-reduce.txt")).reduce();
		//Network net = new Network("../data/net4.txt", NetType.UNDIRECTED, NumberType.INTEGER);
/*		Network net = new Network("../data/ip/11/reduce-1.txt", NetType.UNDIRECTED, NumberType.LONG);
		//Degree d = new Degree(net);
		//D.p(d.netDegreeAvg());
		ClusterCofficient cc = new ClusterCofficient(net);
		D.p(cc.netClusterCoefficient());
		//D.p(cc.nodeClusterCofficient(12));
		//Path p = new Path(net);
		//D.p(p.netAvgPathLength());
		//D.p(p.netDiameter());
		//D.p(p.netAvgPathLength());
		Coreness c = new Coreness(net);
		D.p(c.netCore());
		D.m();*/
/*		int[] a = new int[1000000];
		a[0]= 1;
		D.p(a[0]);*/
		//D.p(Math.ceil(0.1/0.25));
/*		Network net = new Network("/home/mervin/CN/Data/filename.dat", NetType.UNDIRECTED, NumberType.INTEGER);
		new FileTool().write(net.traverseEdge(), "/home/mervin/CN/Data/net.edges");*/
		
/*		FileTool ft = new FileTool();
		String srcFile = "../data/test/yeast.txt";
		Network net = new Network(srcFile, NetType.UNDIRECTED, NumberType.INTEGER);
		Coreness c = new Coreness();
		//D.p(c.adjNodeCore(net, 3356));
		//D.p(c.nodeCore(net, net.getAllNodeId()));
		ft.write(c.nodeCore(net, net.getAllNodeId()), "../data/test/yeast-core.txt");
		//D.p(c.netCore(net));
		 * 
*/	
/*		DatabaseReader reader = new DatabaseReader(new File("../data/AVR/GeoLite2-Country.mmdb"));

		City model = reader.city(InetAddress.getByName("128.101.101.101"));

		System.out.println(model.getCountry().getIsoCode()); // 'US'
		System.out.println(model.getCountry().getName()); // 'United States'
		System.out.println(model.getCountry().getNames().get("zh-CN")); // '美国'

		System.out.println(model.getMostSpecificSubdivision().getName()); // 'Minnesota'
		System.out.println(model.getMostSpecificSubdivision().getIsoCode()); // 'MN'

		System.out.println(model.getCity().getName()); // 'Minneapolis'

		System.out.println(model.getPostal().getCode()); // '55455'

		System.out.println(model.getLocation().getLatitude()); // 44.9733
		System.out.println(model.getLocation().getLongitude()); // -93.2323
	*/
		
		
/*		File database = new File("/path/to/database/GeoIP2-City.mmdb");
		MaxMindDbReader reader = new MaxMindDbReader(database);

		InetAddress address = InetAddress.getByName("24.24.24.24");

		JsonNode result = reader.get(address);

		System.out.println(result);

		result.close();*/
/*		int k = 20;
		double f = 0.2;
		WSNetwork net = new WSNetwork();
		net.set(1000, k, f);
		net.createModelNetwork();
		new FileTool().write(net.traverseEdge(), "../data/ws-"+k+"-"+f+" .edges");*/
		
/*		BANetwork ba = new BANetwork(3, NetType.DIRECTED);
		ba.set(1000, 2);
		ba.createModelNetwork();
		//D.p(ba.traverseEdge().size());
		GraphML g = new GraphML();
		g.script(ba, "ba", "../data/ba.graphml");*/
		//FileTool().write(ba.traverseEdge(), "../data/ba.txt");
		
	/*	GraphMLReader r = new GraphMLReader("../data/ba-2.graphml");
		Network net = r.createNet();
		D.p(net.nodeNum+"###"+net.edgeNum);
		GraphML g = new GraphML();
		g.script(net, "../data/ba-0.graphml");*/
/*		
		Map<Integer, Pair<Integer>> map = new HashMap<Integer, Pair<Integer>>();
		map.put(1, new Pair(1,2));
		map.put(2, new Pair(1,2));
		map.put(3, new Pair(2,1));
		D.p(map.get(1));
		D.p(map.get(2));
		D.p(map.get(3));*/
/*		FileTool ft = new FileTool();
		Map<Number, Number> freq = ft.read2Map("../data/g_freq.txt", NumberType.DOUBLE, NumberType.INTEGER);
		Map<Number, Number> rate = MathTool.ratio(freq);
		ft.write(rate, "../data/g_r.txt");
		PairList<Number, Number> pl = new MapTool().sort(rate, true, false);
		double a = 0;
		PairList<Number, Number> pl2 = new PairList<Number, Number>();
		for(int i= 0; i < pl.size(); i++){
			a += pl.getR(i).doubleValue();
			pl2.add(pl.getL(i), a);
		}
		ft.write(pl2, "../data/g_ar.txt");*/
		
/*		Map<Pair<Number>, Integer> m = new HashMap<Pair<Number>, Integer>();
		Pair<Number> p = null, p2 = null;
		p = new Pair<Number>(1,2);
		m.put(p, 1);
		m.put(p, 2);
		p2 = new Pair<Number>(1, 2);
		D.p(m.get(p2));*/
		
//		LinkedList<Integer> l = new LinkedList<Integer>();
//		l.add(1);
//		l.add(2);
//		l.add(3);
//		for(Integer i:l){
//			D.p(i);
//		}
/*		SortedSet<Number> levelSet = new TreeSet<Number>();
		levelSet.add(4);
		levelSet.add(1);
		levelSet.add(0);
		D.p(levelSet.first()+""+levelSet.last());*/
		
//		String srcDir="C:\\Users\\Mervin.Wong\\Desktop\\WSN\\H\\";
//		String dstDir = "C:\\Users\\Mervin.Wong\\Desktop\\WSN_1\\H\\";
//		
//		FileTool ft = new FileTool();
//		File[] fileArr = ft.fileArr(srcDir);
//		Degree d = new Degree();
//		for (int i = 0; i < fileArr.length; i++) {
//			String srcFile = fileArr[i].getAbsolutePath();
//			Map<Number, Number> degreeMap = ft.read2Map(srcFile);
//			Map<Number, Number> degreeRateMap = MathTool.ratio(MathTool.frequency(degreeMap));
//			String dstFile = dstDir+fileArr[i].getName();
//			ft.write(degreeRateMap, dstFile);
//		}
/*		Set<LinkedList<Integer>> set = new HashSet<LinkedList<Integer>>();
		LinkedList list = null;
		list = new LinkedList<Integer>();
		list.add(1);
		list.add(2);
		set.add(list);
		list = new LinkedList<Integer>();
		list.add(2);
		list.add(3);
		set.add(list);
		list = new LinkedList<Integer>();
		list.add(1);
		list.add(2);
		set.add(list);
		for(LinkedList l:set){
			D.p(l);
		}*/
		
/*		LinkedList<Integer> l = new LinkedList<Integer>();
		l.add(1);
		l.add(3000);
		l.add(30000);
		
		LinkedList<Integer> l1 = new LinkedList<Integer>();
		l1.add(1);
		l1.add(3000);
		if(l.equals(l1)){
			D.m("#");
		}
		l1.add(30000);
		if(l.equals(l1)){
			D.m("@");
		}
		while(true){
			
		}*/
		/*String srcFile = "./data/soft.txt";
		String dstFile = "";
		Network net = new Network(srcFile, NetType.DIRECTED, NumberType.INTEGER);
		Degree degree = new Degree(net);
		FileTool ft = new FileTool();
		dstFile = "./data/soft_degreeDistRatio.txt";
		ft.write(degree.netDegreeDistributionRatio(), dstFile);
		dstFile = "./data/soft_degreeInDistRatio.txt";
		ft.write(degree.netInDegreeDistributionRatio(), dstFile);
		dstFile = "./data/soft_degreeOutDistRatio.txt";
		ft.write(degree.netOutDegreeDistributionRatio(), dstFile);*/
		
		
		
		String srcDir = "./data/as/";
		FileTool ft = new FileTool();
		//File[] fileArr = ft.fileArr(srcDir, "20070701");
		File[] fileArr = ft.fileArr(srcDir);
		StructureEntropy se = new StructureEntropy();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < fileArr.length; i++) {
			D.p(fileArr[i].getAbsoluteFile());
			String name = fileArr[i].getName();
			String prefix = name.substring(0, name.indexOf("."));
			Network net = new Network(fileArr[i].getAbsolutePath(), NetType.UNDIRECTED, NumberType.LONG);
			//false:结构熵，true:标准结构熵
			sb.append(prefix).append("\t").append(se.script(net, false)).append("\r\n");
		}
		new FileTool().write(sb, "./data/struct_entropy_vMax-v.txt");
		
		
		
		
		
/*		String srcFile = "./data/t2.txt";
		Network net = new Network(srcFile, NetType.UNDIRECTED, NumberType.INTEGER);
		D.p(net.isConnectedNet());
		Degree d = new Degree(net);
		D.p(d.netDegreeAvg());
		D.p(net.edgeNum);
		new FileTool().write(net.traverseEdge(), "./data/t3.txt");*/
	}
}
