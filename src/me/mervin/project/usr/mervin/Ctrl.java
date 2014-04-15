package me.mervin.project.usr.mervin;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Vector;

import me.mervin.core.Edge;
import me.mervin.core.NetModel;
import me.mervin.core.Network;
import me.mervin.core.Node;
import me.mervin.core.Global.DegreeType;
import me.mervin.core.Global.NetType;
import me.mervin.model.BANetwork;
import me.mervin.model.ER;
import me.mervin.model.Price;
import me.mervin.model.WSNetwork;
import me.mervin.module.feature.AssortativityCoefficient;
import me.mervin.module.feature.Degree;
import me.mervin.module.feature.Path;
import me.mervin.module.graphFormats.GraphML;
import me.mervin.module.graphFormats.GraphMLReader;
import me.mervin.util.D;
import me.mervin.util.FileTool;
import me.mervin.util.Link;
import me.mervin.util.MapTool;
import me.mervin.util.MathTool;
import me.mervin.util.Pair;
import me.mervin.util.PairList;


 /**
 *   Ctrl.java
 *    
 *  @author Mervin.Wong  DateTime 2013-10-15 下午6:47:28    
 *  @version 0.4.0
 */
public class Ctrl {

	/**
	 *  
	 *  @param args
	 */
	public static void main(String[] args) {
		// TODO 自动生成的方法存根
		Ctrl c = new Ctrl();
	   //c.testNet(2000, 4);
		//Network net = new GraphMLReader("../data/ctrl/ba-2000-4-0.graphml").createNet();
		//new GraphML().script(c._rewiringByRedundantLink(net), "../data/ctrl/ba-2000-4-0-2.graphml");
		//c._statistics("../data/ctrl/ba-2000-4.graphml", "../data/ctrl/ba-2000-4-1.graphml");
		
		//c.rewiringsByDegree();
		
		//Network net = new GraphMLReader("../data/ctrl/ba-2000-4-1.graphml").createNet();
/*		AssortativityCoefficient ac = new AssortativityCoefficient();
		for(double p = 0.1; p <= 1; p+=0.1){
			Network net = new GraphMLReader("../data/ctrl/ba-2000-4-1.graphml").createNet();
			c._rewiringByDegree(net, p, true);
			D.p(ac.script(net, DegreeType.OUT, DegreeType.IN));
		}*/
		
		//c.rewiringsByRedundantAndDegree();
		c.rewiringsByRedundantAndDegreeER();
		
		/*Network net = new Network("../data/ctrl/other/Caltech36.edges", NetType.DIRECTED);
		new GraphML().script(net, "../data/ctrl/other/fk-1.graphml");
		c._statistics("../data/ctrl/other/fk-1.graphml", "../data/ctrl/other/fk-2.graphml");
		net = new GraphMLReader("../data/ctrl/other/fk-2.graphml").createNet();
		new GraphML().script(c._rewiringByRedundantLinkAndDegree(net, 1), "../data/ctrl/other/fk-3.graphml");*/
		
	}
	
	
	
	public void rewiringsByRedundant(){
		String srcDir = "../data/ctrl/";
		String dstDir = "../data/ctrl/redundant/";
		
		
		
		String srcFile = null, dstFile = null;
		BufferedReader reader = null;
		String line = null;
		
		StringBuffer sb1 = new StringBuffer();
		StringBuffer sb2 = new StringBuffer();
		
		Network net = null;
		AssortativityCoefficient ac = new AssortativityCoefficient();
		GraphML g = new GraphML();
		FileTool ft = new FileTool();
		
		ft.mkdir(dstDir);
		
		/*  
		 * random rewiring redundant's edge
		 */
		 
		 
		int n = 2000;
		while(true){
			for(int m = 1; m <= 20; m++){
				//ba网络
				//this.testNet(n, m);
				
				for(int k = 0; k < 3; k++){
					srcFile = srcDir+"ba-"+n+"-"+m+".graphml";
					dstFile = dstDir+"ba-"+n+"-"+m+"-"+k+".graphml";
					
					try {
						int count = this._statistics(new File(srcFile).getCanonicalPath(), new File(dstFile).getCanonicalPath());
						net = new GraphMLReader(dstFile).createNet();
						double p = ac.script(net, DegreeType.OUT, DegreeType.IN);
						sb1.append(m).append("\t").append((double)count/n).append("\t");
						sb2.append(m).append("\t").append(p).append("\t");
					} catch (IOException e) {
						// TODO 自动生成的 catch 块
						e.printStackTrace();
					}
					net = new GraphMLReader(dstFile).createNet();
					dstFile = dstDir+"ba-"+n+"-"+m+"-"+k+k+".graphml";
					g.script(this._rewiringByDegree(net, 0.4, true), dstFile);
					int count;
					try {
						srcFile = dstFile;
						dstFile = dstDir+"ba-"+n+"-"+m+"-"+k+k+k+".graphml";
						count = this._statistics(new File(srcFile).getCanonicalPath(), new File(dstFile).getCanonicalPath());
						net = new GraphMLReader(dstFile).createNet();
						double p = ac.script(net, DegreeType.OUT, DegreeType.IN);
						sb1.append(m).append("\t").append((double)count/n).append("\r\n");
						sb2.append(m).append("\t").append(p).append("\r\n");
					} catch (IOException e) {
						// TODO 自动生成的 catch 块
						e.printStackTrace();
					}
					
					//srcFile = dstFile;
				}
				ft.write(sb1, dstDir+"driver_node.txt");
				ft.write(sb2, dstDir+"AC.txt");
			}
			
			break;
		}
	}
	public void rewiringsByDegree(){
		String srcDir = "../data/ctrl/";
		String dstDir = "../data/ctrl/degree/";
		
		
		
		String srcFile = null, dstFile = null;
		BufferedReader reader = null;
		String line = null;
		
		StringBuffer sb1 = new StringBuffer();
		StringBuffer sb2 = new StringBuffer();
		
		Network net = null;
		AssortativityCoefficient ac = new AssortativityCoefficient();
		GraphML g = new GraphML();
		FileTool ft = new FileTool();
		
		ft.mkdir(dstDir);
		
		/*  
		 * random rewiring redundant's edge
		 */
		
		
		int n = 2000;
		while(true){
			for(int m = 1; m <= 20; m++){
				//ba网络
				//this.testNet(n, m);
				
				for(int k = 0; k < 5; k++){
					srcFile = srcDir+"ba-"+n+"-"+m+".graphml";
					dstFile = dstDir+"ba-"+n+"-"+m+"-"+k+".graphml";
					
					try {
						int count = this._statistics(new File(srcFile).getCanonicalPath(), new File(dstFile).getCanonicalPath());
						net = new GraphMLReader(dstFile).createNet();
						double p = ac.script(net, DegreeType.OUT, DegreeType.IN);
						sb1.append(m).append("\t").append((double)count/n).append("\t");
						sb2.append(m).append("\t").append(p).append("\t");
					} catch (IOException e) {
						// TODO 自动生成的 catch 块
						e.printStackTrace();
					}
					net = new GraphMLReader(dstFile).createNet();
					dstFile = dstDir+"ba-"+n+"-"+m+"-"+k+k+".graphml";
					g.script(this._rewiringByRedundantLink(net), dstFile);
					int count;
					try {
						srcFile = dstFile;
						dstFile = dstDir+"ba-"+n+"-"+m+"-"+k+k+k+".graphml";
						count = this._statistics(new File(srcFile).getCanonicalPath(), new File(dstFile).getCanonicalPath());
						net = new GraphMLReader(dstFile).createNet();
						double p = ac.script(net, DegreeType.OUT, DegreeType.IN);
						sb1.append(m).append("\t").append((double)count/n).append("\r\n");
						sb2.append(m).append("\t").append(p).append("\r\n");
					} catch (IOException e) {
						// TODO 自动生成的 catch 块
						e.printStackTrace();
					}
					
					//srcFile = dstFile;
				}
				ft.write(sb1, dstDir+"driver_node.txt");
				ft.write(sb2, dstDir+"AC.txt");
			}
			
			break;
		}
	}
	public void rewiringsByRedundantAndDegree(){
		String srcDir = "../data/ctrl/rd3/";
		String dstDir = "../data/ctrl/rd3/";
		
		
		
		String srcFile = null, srcFile1 = null, srcFile2 = null, dstFile = null, dstFile1 = null, dstFile2 = null;
		BufferedReader reader = null;
		String line = null;
		
		StringBuffer sb1 = new StringBuffer();
		StringBuffer sb2 = new StringBuffer();
		
		Network net = null;
		AssortativityCoefficient ac = new AssortativityCoefficient();
		GraphML g = new GraphML();
		FileTool ft = new FileTool();
		
		ft.mkdir(dstDir);
		
		/*  
		 * random rewiring redundant's edge
		 */
		
		
		int n = 2000;
		int count = 0;
		while(true){
			for(int m = 2; m <= 20; m++){
				//ba网络
				
				for(double a = 0; a < 4*m; a += 0.5*m){
					double pp = (double)m/(m+a);
					this.testNetPrice(srcDir, n, m, pp);
					this.testNetPrice(srcDir, n, m, pp);
					
					for(int k = 0; k < 1; k++){
						srcFile = srcDir+"ba-"+n+"-"+m+"-"+pp+".graphml";
						dstFile = dstDir+"ba-"+n+"-"+m+"-"+k+"-"+pp+"-0.graphml";
						
						try {
							count = this._statistics(new File(srcFile).getCanonicalPath(), new File(dstFile).getCanonicalPath());
							net = new GraphMLReader(dstFile).createNet();
							double p = ac.script(net, DegreeType.OUT, DegreeType.IN);
							sb1.append(n).append("\t").append(m).append("\t").append(2+(double)a/m).append("\t").append((double)count/n).append("\t");
							sb2.append(m).append("\t").append(p).append("\t");
						} catch (IOException e) {
							// TODO 自动生成的 catch 块
							e.printStackTrace();
						}
						srcFile1 = dstFile;
						net = new GraphMLReader(srcFile1).createNet();
						dstFile1 = dstDir+"ba-"+n+"-"+m+"-"+k+"-"+pp+"-1.graphml";
						g.script(this._rewiringByRedundantLink(net), dstFile1);
						try {
							srcFile1 = dstFile1;
							dstFile1 = dstDir+"ba-"+n+"-"+m+"-"+k+"-"+pp+"-2.graphml";
							count = this._statistics(new File(srcFile1).getCanonicalPath(), new File(dstFile1).getCanonicalPath());
							net = new GraphMLReader(dstFile1).createNet();
							double p = ac.script(net, DegreeType.OUT, DegreeType.IN);
							sb1.append((double)count/n).append("\t");
							sb2.append(m).append("\t").append(p).append("\t");
						} catch (IOException e) {
							// TODO 自动生成的 catch 块
							e.printStackTrace();
						}
						
						srcFile2 = dstFile;
						net = new GraphMLReader(srcFile2).createNet();
						dstFile2 = dstDir+"ba-"+n+"-"+m+"-"+k+"-"+pp+"-3.graphml";
						g.script(this._rewiringByRedundantLinkAndDegree(net, 1), dstFile2);
						//g.script(this._rewiringByRedundantLinkAndDegree1(net, 1), dstFile2);
						try {
							srcFile2 = dstFile2;
							dstFile2 = dstDir+"ba-"+n+"-"+m+"-"+k+"-"+pp+"-4.graphml";
							count = this._statistics(new File(srcFile2).getCanonicalPath(), new File(dstFile2).getCanonicalPath());
							net = new GraphMLReader(dstFile2).createNet();
							double p = ac.script(net, DegreeType.OUT, DegreeType.IN);
							sb1.append((double)count/n).append("\r\n");
							sb2.append(m).append("\t").append(p).append("\r\n");
						} catch (IOException e) {
							// TODO 自动生成的 catch 块
							e.printStackTrace();
						}
						
						//srcFile = dstFile;
					}
					ft.write(sb1, dstDir+"driver_node.txt");
					ft.write(sb2, dstDir+"AC.txt");
				}
			}
			break;
		}
	}
	public void rewiringsByRedundantAndDegreeER(){
		String srcDir = "../data/ctrl/er/";
		String dstDir = "../data/ctrl/er/";
		
		
		
		String srcFile = null, srcFile1 = null, srcFile2 = null, dstFile = null, dstFile1 = null, dstFile2 = null;
		BufferedReader reader = null;
		String line = null;
		
		StringBuffer sb1 = new StringBuffer();
		StringBuffer sb2 = new StringBuffer();
		
		Network net = null;
		AssortativityCoefficient ac = new AssortativityCoefficient();
		GraphML g = new GraphML();
		FileTool ft = new FileTool();
		
		ft.mkdir(dstDir);
		
		/*  
		 * random rewiring redundant's edge
		 */
		
		
		int n = 2000;
		int count = 0;
		while(true){
			
			for(double pp = 0.001; pp <= 0.001; pp +=0.0005){
				this.testNetER(srcDir, n, pp);
				for(int k = 0; k < 1; k++){
					srcFile = srcDir+"net-"+n+"-"+pp+".graphml";
					dstFile = dstDir+"net-"+n+"-"+pp+"-"+k+"-0.graphml";
					
					try {
						count = this._statistics(new File(srcFile).getCanonicalPath(), new File(dstFile).getCanonicalPath());
						net = new GraphMLReader(dstFile).createNet();
						//double p = ac.script(net, DegreeType.OUT, DegreeType.IN);
						sb1.append(n).append("\t").append(pp).append("\t").append((double)count/n).append("\t");
						//sb2.append(n).append("\t").append(p).append("\t");
					} catch (IOException e) {
						// TODO 自动生成的 catch 块
						e.printStackTrace();
					}
					srcFile1 = dstFile;
					net = new GraphMLReader(srcFile1).createNet();
					D.p("@@@");
					dstFile1 = dstDir+"net-"+n+"-"+pp+"-"+k+"-1.graphml";
					g.script(this._rewiringByRedundantLink(net), dstFile1);
					D.p("@@@");
					try {
						srcFile1 = dstFile1;
						dstFile1 = dstDir+"net-"+n+"-"+pp+"-"+k+"-2.graphml";
						count = this._statistics(new File(srcFile1).getCanonicalPath(), new File(dstFile1).getCanonicalPath());
						net = new GraphMLReader(dstFile1).createNet();
						//double p = ac.script(net, DegreeType.OUT, DegreeType.IN);
						sb1.append((double)count/n).append("\t");
						//sb2.append(n).append("\t").append(p).append("\t");
					} catch (IOException e) {
						// TODO 自动生成的 catch 块
						e.printStackTrace();
					}
					
					srcFile2 = dstFile;
					net = new GraphMLReader(srcFile2).createNet();
					dstFile2 = dstDir+"net-"+n+"-"+pp+"-"+k+"-3.graphml";
					g.script(this._rewiringByRedundantLinkAndDegree(net, 1), dstFile2);
					//g.script(this._rewiringByRedundantLinkAndDegree1(net, 1), dstFile2);
					try {
						srcFile2 = dstFile2;
						dstFile2 = dstDir+"net-"+n+"-"+pp+"-"+k+"-4.graphml";
						count = this._statistics(new File(srcFile2).getCanonicalPath(), new File(dstFile2).getCanonicalPath());
						net = new GraphMLReader(dstFile2).createNet();
						//double p = ac.script(net, DegreeType.OUT, DegreeType.IN);
						sb1.append((double)count/n).append("\r\n");
						//sb2.append(n).append("\t").append(p).append("\r\n");
					} catch (IOException e) {
						// TODO 自动生成的 catch 块
						e.printStackTrace();
					}
					
					//srcFile = dstFile;
				}
				ft.write(sb1, dstDir+"driver_node.txt");
				ft.write(sb2, dstDir+"AC.txt");
			}
		}
	}
	
	
	public void testNetER(String srcDir, int n, double p){
		ER er = new ER(NetType.DIRECTED);
		er.set(n, p);
		er.createModelNetwork();
		GraphML g = new GraphML();
		g.script(er, srcDir+"net-"+n+"-"+p+".graphml");
	}
	
	public void testNetPrice(String srcDir, int n, int m, double p){
		/*BANetwork ba = new BANetwork(m+1, NetType.DIRECTED);
		ba.set(n, m);
		ba.createModelNetwork();*/
		
		Price price = new Price(m+1, NetType.DIRECTED);
		price.set(n, m, p);
		price.createModelNetwork();
		GraphML g = new GraphML();
		g.script(price, srcDir+"ba-"+n+"-"+m+"-"+p+".graphml");
		
	}
	
	public void testNetWS(String srcDir, int n, int k, double p){
		WSNetwork net = new WSNetwork();
		net.set(n, k, p);
		net.createModelNetwork();
		GraphML g = new GraphML();
		g.script(net, srcDir+"net-"+n+"-"+k+"-"+p+".graphml");
	}
	
	public void testNet(int n, int m, double p){
		/*BANetwork ba = new BANetwork(m+1, NetType.DIRECTED);
		ba.set(n, m);
		ba.createModelNetwork();*/
		
		Price price = new Price(m+1, NetType.DIRECTED);
		price.set(n, m, p);
		GraphML g = new GraphML();
		g.script(price, "../data/ctrl/ba-"+n+"-"+m+".graphml");
		
	}
	
	private int _statistics(String srcFile, String dstFile){
		int count = 0;
		try {
			//String cmd = "netctrl -m liu -M graph -o "+dstFile+" -f graphml -F graphml "+srcFile;
			String cmd = "netctrl  -M graph -o "+dstFile+" -f graphml -F graphml "+srcFile;
			Process pid = Runtime.getRuntime().exec(cmd);
			//BufferedReader reader = new BufferedReader(new InputStreamReader(pid.getInputStream()));
			BufferedReader reader = new BufferedReader(new InputStreamReader(pid.getErrorStream()));
			String line = null;
			while((line = reader.readLine()) != null){
				if(line.contains("found")){
					D.p(line);
					count = Integer.parseInt(line.substring(line.indexOf("found")+5, line.indexOf("driver")).trim());
				}
			}
			
			//D.p(count);
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		return count;
	}
	
	
	private Network _rewiringByRedundantLink(Network net){
		PairList<Number, Number> edgeList = net.traverseEdge();
		
		Number l1, r1, l2, r2;
		
		Edge edge = null;
		D.p(edgeList.size());
		for(int i = 0; i < edgeList.size(); i++){
			l1 = edgeList.getL(i);
			r1 = edgeList.getR(i);
			
			edge = net.getEdgeByNodeId(l1, r1);
			if(edge.getAttr("edge_class").equalsIgnoreCase("redundant")){
				do{
					l2 = net.getRandNodeId();
					r2 = net.getRandNodeId();
				}while(l2.equals(r2) || net.isHasEdge(l2, r2));
				
				net.insertEdge(l2, r2);
				net.deleteEdge(l1, r1);
			}
		}
		return net;
	}
	private Network _rewiringByRedundantLinkAndDegree(Network net, double p){
		PairList<Number, Number> edgeList = net.traverseEdge();
		
		Number l1, r1, l2, r2;
		
		Edge edge = null;
		
		int count = edgeList.size();
		
		/*
		 * get the set of driver node
		 */
		Vector<Number> driverNode = new Vector<Number>();
		for(Number nodeId:net.getAllNodeId()){
			if(net.getNodeAttr(nodeId, "is_driver") != null){
				if(net.getNodeAttr(nodeId, "is_driver").equalsIgnoreCase("1")){
					driverNode.add(nodeId);
				}
			}
		}
		int driverNodeSize = driverNode.size();
		PairList<Number, Number> redundantEdge = new PairList<Number, Number>();
		int kk = 0;
		for(int i = 0; i < edgeList.size(); i++){
			l1 = edgeList.getL(i);
			r1 = edgeList.getR(i);
			
			edge = net.getEdgeByNodeId(l1, r1);
			Random rand = new Random();
			if(edge.getAttr("edge_class").equalsIgnoreCase("redundant")){
//				redundantEdge.add(l1, r1);
				do{
//					l2 = net.getRandNodeId();
//					r2 = net.getRandNodeId();
					l2 = driverNode.get(rand.nextInt(driverNodeSize));
					r2 = driverNode.get(rand.nextInt(driverNodeSize));
				}while(l2.equals(r2) || net.isHasEdge(l2, r2));
				
				//net.insertEdge(l2, r2);
				
				net.deleteEdge(l1, r1);
				kk++;
			}
			
		}
		

		/*
		 * random choose a redundant edge
		 */
//		Random rand = new Random();
//		Degree d = new Degree(net);
/*		int num = (int) Math.ceil(redundantEdge.size()*p);
		for(int j = 0; j < num;){
			int edgeNo1 = rand.nextInt(num);
			l1 = redundantEdge.getL(edgeNo1);
			r1 = redundantEdge.getR(edgeNo1);
			
			int lOutDegree1 = d.nodeOutDegree(l1);
			int lInDegree1 = d.nodeInDegree(l1);
			int rOutDegree1 = d.nodeOutDegree(r1);
			int rInDegree1 = d.nodeInDegree(r1);
			
			int edgeNo2 = rand.nextInt(num);
			l2 = redundantEdge.getL(edgeNo2);
			r2 = redundantEdge.getR(edgeNo2);
			
			int lOutDegree2 = d.nodeOutDegree(l2);
			int lInDegree2 = d.nodeInDegree(l2);
			int rOutDegree2 = d.nodeOutDegree(r2);
			int rInDegree2 = d.nodeInDegree(r2);
			
			double p1 = (double)lInDegree1/lOutDegree1*rOutDegree1;
			double p2 = (double)lInDegree2/lOutDegree2*rOutDegree2;
			double p3 = (double)lInDegree1/lOutDegree1*rOutDegree2;
			double p4 = (double)lInDegree1/lOutDegree1*rOutDegree1;
			
			 * l1->r1 l2->r2
			 * 节点不想等:l1 != l2 l1!= r2 r1 != l2 r1 != r2
			 
			D.p("###");
			if(l1.equals(l2) || l1.equals(r2) || r1.equals(l2) || r1.equals(r2)){
				continue;
			}		
			if(p1+p2 < p3+p4){
				net.insertEdge(l1, r2);
				net.insertEdge(l2, r1);
				net.deleteEdge(l1, r1);
				net.deleteEdge(l2, r2);
				
				
			}
			j++;
			D.p(j);
		}*/
		return net;
	}
	private Network _rewiringByRedundantLinkAndDegree1(Network net, double p){
		PairList<Number, Number> edgeList = net.traverseEdge();
		
		Number l1, r1, l2, r2;
		
		Edge edge = null;
		
		int count = edgeList.size();
		
		/*
		 * get the set of driver node
		 */
		Vector<Number> driverNode = new Vector<Number>();
		for(Number nodeId:net.getAllNodeId()){
			if(net.getNodeAttr(nodeId, "is_driver").equalsIgnoreCase("1")){
				driverNode.add(nodeId);
			}
		}
		int driverNodeSize = driverNode.size();
		
		/*
		 * rand rewiring the driver node by the shortest path
		 */
		
		Path path = new Path();
		Set<Link<Number>> sp =  null;
//		Link link = null;
		Map<Number, Number> spl1 = new HashMap<Number, Number>();
		Map<Integer, Pair<Number>> spl2 = new HashMap<Integer, Pair<Number>>();
		int k = 0;
		for(Number nodeId:driverNode){
			sp = path.nodeShortestPath(net, nodeId);
			for(Link<Number> link:sp){
				//D.p(link.length+"##"+link.getEdges().size());
			    //D.p(sp.size());
				if(driverNode.contains(link.end)){
					D.p("###");
					spl1.put(k, link.getLength());
					spl2.put(k,new Pair<Number>(link.top, link.end));
					k++;
				}
			}
		}
		
		double apl = path.netAvgPathLength(net);
		D.p("apl:"+apl);
		
		
		
		PairList<Number, Number> pl = new MapTool().sort(spl1, false, false);
		D.p(pl);
		int k1 = 0;
		Pair<Number> pair = null;
		Number l, r;
		do{
			D.p(pl.getR(k1));
			pair = spl2.get(pl.getL(k1));
			l = pair.getL();
			r = pair.getR();
			if(!net.isHasEdge(l, r)){
				net.insertEdge(l, r);
			}
			k1++;
		}while(pl.getR(k1).doubleValue() > apl);
		
		
/*		PairList<Number, Number> redundantEdge = new PairList<Number, Number>();
		
		for(int i = 0; i < edgeList.size(); i++){
			l1 = edgeList.getL(i);
			r1 = edgeList.getR(i);
			
			edge = net.getEdgeByNodeId(l1, r1);
			Random rand = new Random();
			if(edge.getAttr("edge_class").equalsIgnoreCase("redundant")){
//				redundantEdge.add(l1, r1);
				do{
//					l2 = net.getRandNodeId();
//					r2 = net.getRandNodeId();
					l2 = driverNode.get(rand.nextInt(driverNodeSize));
					r2 = driverNode.get(rand.nextInt(driverNodeSize));
				}while(l2.equals(r2) || net.isHasEdge(l2, r2));
				
				//net.insertEdge(l2, r2);
				net.deleteEdge(l1, r1);
			}
		}*/
		
		return net;
	}
	private Network _rewiringByRedundantLinkAndDegree2(Network net, double p){
		PairList<Number, Number> edgeList = net.traverseEdge();
		
		Number l1, r1, l2, r2;
		
		Edge edge = null;
		
		int count = edgeList.size();
		
		/*
		 * get the set of driver node
		 */
		Vector<Number> driverNode = new Vector<Number>();
		for(Number nodeId:net.getAllNodeId()){
			if(net.getNodeAttr(nodeId, "is_driver").equalsIgnoreCase("1")){
				driverNode.add(nodeId);
			}
		}
		int driverNodeSize = driverNode.size();
		
		/*
		 * rand rewiring the driver node by the shortest path
		 */
		
		Path path = new Path();
		Set<Link<Number>> sp =  null;
//		Link link = null;
		Map<Number, Number> spl1 = new HashMap<Number, Number>();
		Map<Integer, Pair<Number>> spl2 = new HashMap<Integer, Pair<Number>>();
		int k = 0;
		for(Number nodeId:driverNode){
			sp = path.nodeShortestPath(net, nodeId);
			for(Link<Number> link:sp){
				//D.p(link.length+"##"+link.getEdges().size());
				//D.p(sp.size());
				if(driverNode.contains(link.end)){
					D.p("###");
					spl1.put(k, link.getLength());
					spl2.put(k,new Pair<Number>(link.top, link.end));
					k++;
				}
			}
		}
		
		double apl = path.netAvgPathLength(net);
		D.p("apl:"+apl);
		
		
		
		PairList<Number, Number> pl = new MapTool().sort(spl1, false, false);
		D.p(pl);
		int k1 = 0;
		Pair<Number> pair = null;
		Number l, r;
		do{
			D.p(pl.getR(k1));
			pair = spl2.get(pl.getL(k1));
			l = pair.getL();
			r = pair.getR();
			if(!net.isHasEdge(l, r)){
				net.insertEdge(l, r);
			}
			k1++;
		}while(pl.getR(k1).doubleValue() > apl);
		
		
		/*		PairList<Number, Number> redundantEdge = new PairList<Number, Number>();
		
		for(int i = 0; i < edgeList.size(); i++){
			l1 = edgeList.getL(i);
			r1 = edgeList.getR(i);
			
			edge = net.getEdgeByNodeId(l1, r1);
			Random rand = new Random();
			if(edge.getAttr("edge_class").equalsIgnoreCase("redundant")){
//				redundantEdge.add(l1, r1);
				do{
//					l2 = net.getRandNodeId();
//					r2 = net.getRandNodeId();
					l2 = driverNode.get(rand.nextInt(driverNodeSize));
					r2 = driverNode.get(rand.nextInt(driverNodeSize));
				}while(l2.equals(r2) || net.isHasEdge(l2, r2));
				
				//net.insertEdge(l2, r2);
				net.deleteEdge(l1, r1);
			}
		}*/
		
		
		
		
		return net;
	}
	private Network _rewiringByDegree(Network net, double p, boolean  flag){
		PairList<Number, Number> edgeList = net.traverseEdge();
		
		Number  l1, r1, l2, r2;
//		Edge edge = null;
		int count = edgeList.size();
		//D.p(count);
		Degree d = new Degree(net);
		for(int i = 0; i < count * p; ){
			Random random = new Random();
			int edgeNo1 = random.nextInt(count);
			l1 = edgeList.getL(edgeNo1);
			r1 = edgeList.getR(edgeNo1);
			int edgeNo2 = random.nextInt(count);
			l2 = edgeList.getL(edgeNo2);
			r2 = edgeList.getR(edgeNo2);
			
			/*
			 * 两条边不想等
			 */
			if(edgeNo1 == edgeNo2){
				continue;
			}
			/*
			 * l1->r1 l2->r2
			 * 节点不想等:l1 != l2 l1!= r2 r1 != l2 r1 != r2
			 */
			if(l1.equals(l2) || l1.equals(r2) || r1.equals(l2) || r1.equals(r2)){
				continue;
			}
			
//			int degree[][] = new int[4][2];
			
			Map<Number, int[]> map2 = new HashMap<Number, int[]>();
			int[] temp = new int[2];
			int lOutDegree1 = d.nodeOutDegree(l1);
			int lInDegree1 = d.nodeInDegree(l1);
			temp[0] = lOutDegree1;
			temp[1] = lInDegree1;
			map2.put(l1, temp);
					
			int rOutDegree1 = d.nodeOutDegree(r1);
			int rInDegree1 = d.nodeInDegree(r1);
			temp[0] = rOutDegree1;
			temp[1] = rInDegree1;
			map2.put(r1, temp);
			
			int lOutDegree2 = d.nodeOutDegree(l2);
			int lInDegree2 = d.nodeInDegree(l2);
			temp[0] = lOutDegree2;
			temp[1] = lInDegree2;
			map2.put(l2, temp);
			
			int rOutDegree2 = d.nodeOutDegree(r2);
			int rInDegree2 = d.nodeInDegree(r2);
			temp[0] = rOutDegree2;
			temp[1] = rInDegree2;
			map2.put(r2, temp);
			
			int lMixDegree1 = lOutDegree1*lInDegree1;
			int rMixDegree1 = rOutDegree1*rInDegree1;
			
			int lMixDegree2 = lOutDegree2*lInDegree2;
			int rMixDegree2 = rOutDegree2*rInDegree2;
			
			Map<Number, Number> map = new HashMap<Number, Number>();
			//D.p(l1+"##"+r1+"##"+l2+"##"+r2);
			map.put(l1, lMixDegree1);
			map.put(r1, rMixDegree1);
			map.put(l2, lMixDegree2);
			map.put(r2, rMixDegree2);
			
			//D.p(map);
			Number[] idByMixDegree = this._sort(map);
			
			//同配
			if(flag){
				if(net.isHasEdge(idByMixDegree[0], idByMixDegree[1]) && net.isHasEdge(idByMixDegree[2], idByMixDegree[3])){
					lOutDegree1 = (map2.get(idByMixDegree[0]))[0];
					lInDegree1 = (map2.get(idByMixDegree[0]))[1];
					
					rOutDegree1 = (map2.get(idByMixDegree[1]))[0];
					rInDegree1 = (map2.get(idByMixDegree[1]))[1];
					
					double p1 = (double)lInDegree1/lOutDegree1;
					double p2 = (double)rInDegree1/rOutDegree1;
					
					
					if(p1 >= p2){
						net.insertEdge(idByMixDegree[0], idByMixDegree[1]);
					}else{
						net.insertEdge(idByMixDegree[1], idByMixDegree[0]);
					}
					
					
					
					lOutDegree2 = (map2.get(idByMixDegree[2]))[0];
					lInDegree2 = (map2.get(idByMixDegree[2]))[1];
					
					rOutDegree2 = (map2.get(idByMixDegree[3]))[0];
					rInDegree2 = (map2.get(idByMixDegree[3]))[1];
					
					double p3 = (double)lInDegree2/lOutDegree2;
					double p4 = (double)rInDegree2/rOutDegree2;
					
					if(p3 >= p4){
						net.insertEdge(idByMixDegree[2], idByMixDegree[3]);
					}else{
						net.insertEdge(idByMixDegree[2], idByMixDegree[2]);
					}
					
					net.deleteEdge(l1, r1);
					net.deleteEdge(l2, r2);
					
					i++;
					D.p(count*p+"###"+i);
				}
			}else{
				if(net.isHasEdge(idByMixDegree[0], idByMixDegree[3]) && net.isHasEdge(idByMixDegree[1], idByMixDegree[2])){
					lOutDegree1 = (map2.get(idByMixDegree[0]))[0];
					lInDegree1 = (map2.get(idByMixDegree[0]))[1];
					
					rOutDegree1 = (map2.get(idByMixDegree[1]))[0];
					rInDegree1 = (map2.get(idByMixDegree[1]))[1];
					
					if((lOutDegree1+1)*rInDegree1 >= rOutDegree1*(lInDegree1+1)){
						net.insertEdge(idByMixDegree[0], idByMixDegree[1]);
					}else{
						net.insertEdge(idByMixDegree[1], idByMixDegree[0]);
					}
					
					
					
					lOutDegree2 = (map2.get(idByMixDegree[2]))[0];
					lInDegree2 = (map2.get(idByMixDegree[2]))[1];
					
					rOutDegree2 = (map2.get(idByMixDegree[3]))[0];
					rInDegree2 = (map2.get(idByMixDegree[3]))[1];
					
					if((lOutDegree2+1)*rInDegree2 >= rOutDegree2*(lInDegree2+1)){
						net.insertEdge(idByMixDegree[2], idByMixDegree[3]);
					}else{
						net.insertEdge(idByMixDegree[2], idByMixDegree[2]);
					}
					
					net.deleteEdge(l1, r1);
					net.deleteEdge(l2, r2);
					
					i++;
				}
			}

		}

		return net;
	}
	
	/*
	 * 
	 * 对节点按nodeMap的value值降序排序  
	 *  @param nodeMap nodeId->degree
	 *  @return Number[]
	 */
	private Number[] _sort(Map<Number, Number> nodeMap){
		Number[] nodeArr2 = new Number[4];
//		D.p(nodeMap.size());
		PairList<Number, Number> pl = new MapTool().sort(nodeMap, false, false);
		for(int i = 0; i < 4; i++){
			nodeArr2[i] = pl.getL(i);
		}
		return nodeArr2;
	}

}
