package me.mervin.project.usr.mervin.ctrl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Vector;

import me.mervin.core.Edge;
import me.mervin.core.Network;
import me.mervin.core.Global.DegreeType;
import me.mervin.core.Global.NetType;
import me.mervin.model.ER;
import me.mervin.model.Price;
import me.mervin.model.WSNetwork;
import me.mervin.module.feature.AssortativityCoefficient;
import me.mervin.module.feature.Degree;
import me.mervin.module.graphFormats.GraphML;
import me.mervin.module.graphFormats.GraphMLReader;
import me.mervin.util.D;
import me.mervin.util.FileTool;
import me.mervin.util.Link;
import me.mervin.util.MapTool;
import me.mervin.util.Pair;
import me.mervin.util.PairList;


 /**
 *   ERCtrl.java
 *    
 *  @author Mervin.Wong  DateTime 2013-10-18 下午7:37:00    
 *  @version 0.4.0
 */
public class SFCtrl {

	/**
	 */
	public SFCtrl() {
		// TODO 自动生成的构造函数存根
	}

	//生成网络
	public void testNetSF(String srcDir, int n, int m, double p, double r){
		Price ws = new Price(m+1, NetType.DIRECTED);
		ws.set(n, m, p);
		ws.createModelNetwork();
		GraphML g = new GraphML();
		g.script(ws, srcDir+"net-"+n+"-"+m+"-"+r+".graphml");
	}
	
	//计算该网络的驱动节点和redundant link
	private int _statistics(String srcFile, String dstFile){
		int count = 0;
		try {
			String cmd = "netctrl -m liu -M graph -o "+dstFile+" -f graphml -F graphml "+srcFile;
			//String cmd = "netctrl  -M graph -o "+dstFile+" -f graphml -F graphml "+srcFile;
			//D.p(cmd);
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
	
	//删除redundant link 随机重连
	private boolean _rewiringByRedundantLink(Network net){
		PairList<Number, Number> edgeList = net.traverseEdge();
		
		Number l1, r1, l2, r2;
		
		Edge edge = null;
		boolean flag = false;
		Degree d = new Degree(net);
		for(int i = 0; i < edgeList.size(); i++){
			l1 = edgeList.getL(i);
			r1 = edgeList.getR(i);
			
			edge = net.getEdgeByNodeId(l1, r1);
			if(edge.getAttr("edge_class").equalsIgnoreCase("redundant")){
				flag = true;
				do{
					l2 = net.getRandNodeId();
					r2 = net.getRandNodeId();
				}while(l2.equals(r2) || net.isHasEdge(l2, r2));
				if((d.nodeInDegree(l1)+d.nodeOutDegree(l1) > 1)  && (d.nodeInDegree(r1)+d.nodeOutDegree(r1) > 1)){
					net.insertEdge(l2, r2);
					net.deleteEdge(l1, r1);
				}
			}
		}
		return flag;
	}
	
	
	private int _delRedundantLink(Network net, double p){
		PairList<Number, Number> edgeList = net.traverseEdge();
		
		Number l1, r1, l2, r2;
		
		Edge edge = null;
		
		boolean flag = false;
		//PairList<Number, Number> redundantEdge = new PairList<Number, Number>();
		int count = 0;
		for(int i = 0; i < edgeList.size(); i++){
			l1 = edgeList.getL(i);
			r1 = edgeList.getR(i);
			
			edge = net.getEdgeByNodeId(l1, r1);
			Degree d = new Degree(net);
			if(edge.containsAttrKey("edge_class")){
				if(edge.getAttr("edge_class").equalsIgnoreCase("redundant")){
					flag = true;
					if((d.nodeInDegree(l1)+d.nodeOutDegree(l1) > 1)  && (d.nodeInDegree(r1)+d.nodeOutDegree(r1) > 1)){
						net.deleteEdge(l1, r1);
						count++;
					}
				}
			}
		}
		return count;
	}

	//获取网络的干
	private Map<Number, Link<Number>> _getStems(String srcFile){
		Map<Number, Link<Number>> stems = new HashMap<Number, Link<Number>>();
		String cmd = "netctrl -m liu  -M control_paths -f graphml  "+srcFile;
		//String cmd = "netctrl -M control_paths -f graphml  "+srcFile;
		try {
			Process pid = Runtime.getRuntime().exec(cmd);
			BufferedReader reader = new BufferedReader(new InputStreamReader(pid.getInputStream()));
			//BufferedReader reader = new BufferedReader(new InputStreamReader(pid.getErrorStream()));
			String line = null;
			String[] lineArr = null;
			Link<Number> link = null;
			while((line = reader.readLine()) != null){
				//D.p(line);
				if(line.startsWith("Stem")){
					lineArr = line.split("\\s+");
					link = new Link<Number>();
					link.setTop(Integer.parseInt(lineArr[1]));
					int i = 1;
					for(i = 1; i < lineArr.length; i++){
						link.add(Integer.parseInt(lineArr[i]));
					}
					link.setEnd(Integer.parseInt(lineArr[i-1]));
					
					stems.put(Integer.parseInt(lineArr[1]), link);
				}
			}
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		
		return stems;
	}
	private void _addLink(Network net, Map<Number, Link<Number>> stems, int count){
		Map<Number, Number> stemSize = new HashMap<Number, Number>();
		
		PairList<Number, Number> pl = null;
		Number l, r;
		int k = 0;
		while(stems.size() > 1 && k < count){
			stemSize.clear();
			for(Number nodeId:stems.keySet()){
				stemSize.put(nodeId, stems.get(nodeId).getLength());
			}
			pl = new MapTool().sort(stemSize, false, false);
			
			
			for(int i = 0, j = pl.size()-1; i < j; i++, j--){
				l = pl.getL(i);
				r = pl.getL(j);
				net.insertEdge(l,r);
				List<Number> list = stems.get(r).getList();
				stems.get(l).addAll(list);
				stems.get(l).setEnd(stems.get(r).getEnd());
				stems.remove(r);
				k++;
			}
		}
		
	}
	
	private void _addLinkByInAndOut(Network net, int count){
		Degree d = new Degree(net);
		Map<Number, Number> inDegreeMap = d.nodeInDegree(net.getAllNodeId());
		Map<Number, Number> outDegreeMap = d.nodeOutDegree(net.getAllNodeId());
		
	/*	Map<Number, Pair<Number>> map1 = new HashMap<Number, Pair<Number>>();
		Map<Number, Number> map2 = new HashMap<Number, Number>();
		int k = 1;
		for(Number nodeId1:inDegreeMap.keySet()){
			
			for(Number nodeId2:outDegreeMap.keySet()){
				map1.put(k, new Pair<Number>(nodeId1, nodeId2));
				map2.put(k, inDegreeMap.get(nodeId1).intValue()*outDegreeMap.get(nodeId2).intValue());
				k++;
			}
		}
		
		PairList<Number, Number> list = new MapTool().sort(map2, false, false);*/
		
	
		PairList<Number, Number> inDegreeList = new MapTool().sort(inDegreeMap, false, false);
		PairList<Number, Number> outDegreeList = new MapTool().sort(outDegreeMap, false, false);
		Number l, r;                                                       
		
		  
		
		int nodeSize = Math.min(inDegreeList.size(), outDegreeList.size());
		int s = 0, t = 0;
		boolean flag = true;
		int v = 1;
		int u = nodeSize-Math.max(s, t);
		//int w = Math.min(s, t);
		for(int i = 0, j=0; i < count;j++){
			if(s >= nodeSize || t >= nodeSize){
				s = 0;
				t = 0;
				v++;
			}
			if(j >= nodeSize-Math.max(s, t)){
				if(flag){
					s+=v;
					flag = false;
				}else{
					t+=v;
					flag = true;
				}
				j = 0;
			}
			l = inDegreeList.getL((j+t)%inDegreeList.size());
			//D.p((nodeSize-Math.max(s, t)-1)+"###"+j+"##"+s+"##"+outDegreeList.size()+"###"+nodeSize);
			r = outDegreeList.getL((j+s)%outDegreeList.size());
			if(!l.equals(r) && !net.isHasEdge(l, r)){
				net.insertEdge(l, r);
				i++;
			}
			
			//D.p("%%%%%%%%%");
			
			//l = inDegreeList.getL(j);
			//r = outDegreeList.getL(j);
			//if(!l.equals(r)){
			//	net.insertEdge(l, r);
			//}
			
			
		}
	}
	
	private void _rewiring(Network net, double p){
		
		PairList<Number, Number> edgeList = null;
		Random rand = new Random();
		Number l, r, l1, r1, l2, r2, ll1 = null, rr1 = null, ll2 = null, rr2 = null; 
		int count = (int) Math.round(net.edgeNum*p);
		
		int k1, k2;
		Degree d = new Degree();
		Map<Number, Number> inDegreeMap = new HashMap<Number, Number>();
		Map<Number, Number> outDegreeMap = new HashMap<Number, Number>();
		
		PairList<Number, Number> inDegreeList = null;
		PairList<Number, Number> outDegreeList = null;
		boolean flag1 = false, flag2 = false;
		for(int i = 0; i < count;){
			edgeList = net.traverseEdge();
			flag1 = false;
			flag2 = false;
			
			k1 = rand.nextInt(net.edgeNum);
			l1 = edgeList.getL(k1);
			r1 = edgeList.getR(k1);
			
			k2 = rand.nextInt(net.edgeNum);
			l2 = edgeList.getL(k2);
			r2 = edgeList.getR(k2);
			
			inDegreeMap.put(l1, d.nodeInDegree(net, l1));
			inDegreeMap.put(r1, d.nodeInDegree(net, r1));
			inDegreeMap.put(l2, d.nodeInDegree(net, l2));
			inDegreeMap.put(r2, d.nodeInDegree(net, r2));
			
			outDegreeMap.put(l1, d.nodeOutDegree(net, l1));
			outDegreeMap.put(r1, d.nodeOutDegree(net, r1));
			outDegreeMap.put(l2, d.nodeOutDegree(net, l2));
			outDegreeMap.put(r2, d.nodeOutDegree(net, r2));
			
			
			Map<Number, Pair<Number>> edgeMap = new HashMap<Number, Pair<Number>>();
			Map<Number, Number> degreeMap = new HashMap<Number, Number>();
			int k = 1;
			for(Number nodeId1:inDegreeMap.keySet()){
				for(Number nodeId2:outDegreeMap.keySet()){
					if(!nodeId1.equals(nodeId2)){
						edgeMap.put(k, new Pair<Number>(nodeId1, nodeId2));
						degreeMap.put(k, inDegreeMap.get(nodeId1).intValue()+outDegreeMap.get(nodeId2).intValue());
						k++;
					}
				}
			}
			PairList<Number, Number> degreePL = new MapTool().sort(degreeMap, false, false);
			Number kk = 0;
			int kkk = 0;
			Vector<Number> lV = new Vector<Number>();
			Vector<Number> rV = new Vector<Number>();
			for(int j = 0; j < degreePL.size(); j++){
				kk = degreePL.getL(j);
				l = edgeMap.get(kk).getL();
				r = edgeMap.get(kk).getR();
				if((l.equals(l1)&& r.equals(r1)) ||(l.equals(l2)&& r.equals(r2))){
					break;
				}
				if(!net.isHasEdge(l, r)){
					//net.insertEdge(l, r);
					lV.add(l);
					rV.add(r);
					kkk++;
				}
				if(kkk == 2){
					break;
				}
			}
			if(kkk == 2){
				net.insertEdge(lV.get(0), rV.get(0));
				net.insertEdge(lV.get(1), rV.get(1));
				net.deleteEdge(l1, r1);
				net.deleteEdge(l2, r2);
				i++;
				//D.m();
			}
			//D.p(count+"#######"+i);
		}
			/*inDegreeList = new MapTool().sort(inDegreeMap, false, false);//value des
			outDegreeList = new MapTool().sort(outDegreeMap, false, false);//value des
			
			if((inDegreeList.getL(0).equals(l1) && outDegreeList.getL(0).equals(r1)) || (inDegreeList.getL(0).equals(l2) && outDegreeList.getL(0).equals(r2))){
				continue;
			}
			
			if(!net.isHasEdge(inDegreeList.getL(0), outDegreeList.getL(0))){
				if(!inDegreeList.getL(0).equals(outDegreeList.getL(0))){
					ll1 = inDegreeList.getL(0);
					rr1 = outDegreeList.getL(0);
					flag1 = true;
				}else if(!net.isHasEdge(inDegreeList.getL(0), outDegreeList.getL(1))){
					ll1 = inDegreeList.getL(0);
					rr1 = outDegreeList.getL(1);
					flag1 = true;
				}
			}
			
			if(!net.isHasEdge(inDegreeList.getL(1), outDegreeList.getL(0))){
				if(!inDegreeList.getL(1).equals(outDegreeList.getL(0))){
					ll2 = inDegreeList.getL(1);
					rr2 = outDegreeList.getL(0);
					flag2 = true;
				}else if(!net.isHasEdge(inDegreeList.getL(1), outDegreeList.getL(1))){
					ll2 = inDegreeList.getL(1);
					rr2 = outDegreeList.getL(1);
					flag2 = true;
				}
			}
			
			if(flag1 && flag2){
				net.insertEdge(ll1, rr1);
				net.insertEdge(ll2, rr2);
				
				net.deleteEdge(l1, r1);
				net.deleteEdge(l2, r2);
				
				//D.p("######");
			}
			i++;
		}*/
		
			
		
		
		
	}
	public void script(){
		FileTool ft = new FileTool();
		String srcDir = "../data/ctrl2/sf/";
		String dstDir = "../data/ctrl2/sf/";
		ft.mkdir(dstDir);
		String srcFile = null, dstFile = null, srcFile1 = null, dstFile1= null, srcFile2 = null, dstFile2 = null;
		int max = 20; //平均度的最大值
		int n = 2000; //节点的数量
		int m = 0; //边的数量
		
		StringBuffer temp = new StringBuffer();
		StringBuffer sb1 = new StringBuffer();
		StringBuffer sb2 = new StringBuffer();
		
		Network net = null;
		AssortativityCoefficient ac = new AssortativityCoefficient();
		GraphML g = new GraphML();
		
		
		int driverNodeNum = 0;
		double driverNodeRate = 0;
		ft.clear(dstDir+"driverNodeRate.txt");
		for(int avgK = 2; avgK <= max; avgK +=2){
			m = avgK;
			//for(double p = 0.1; p < 1; p += 0.1){
			for(double a = 0; a <= 8*m; a += 0.1*m){
				double p = (double)m/(m+a);
				double r = 2+(double)a/m;
				temp = new StringBuffer();
				sb1 = new StringBuffer();
				/*
				 * create sf network
				 */
				this.testNetSF(srcDir, n, m, p, r);
				srcFile = srcDir+"net"+"-"+n+"-"+m+"-"+r+".graphml";

				/*
				 * statistic the original net the driver node and redundant link
				 */
				dstFile2 = dstFile1 = dstFile = srcDir+"net"+"-"+n+"-"+m+"-"+r+"-0.graphml";
				driverNodeNum = this._statistics(srcFile, dstFile);
				driverNodeRate = (double)driverNodeNum/n;
				temp.append(n).append("\t").append(n*m).append("\t").append(avgK).append("\t").append(r).append("\t").append(driverNodeRate).append("\t");
				//multiply 
				//for(int k = 1; k <= 5; k++){
				int k = 1;
				while(true){
					/*
					 * rewiring the links
					 */
					sb1.append(temp);
					
					srcFile1 = dstFile1;
					dstFile1 = srcDir+"net"+"-"+n+"-"+m+"-"+k+"-"+r+"-1.graphml";
					net = new GraphMLReader(srcFile1).createNet();
					if(!this._rewiringByRedundantLink(net)){
						sb1.append("\r\n################################################\r\n");
						break;
					}
					g.script(net, dstFile1);
					srcFile1 = dstFile1;
					driverNodeNum = this._statistics(srcFile1, dstFile1);
					driverNodeRate = (double)driverNodeNum/n;
					sb1.append(k).append("\t").append(driverNodeRate).append("\t");
					/*
					 * del the redundant links
					 */
					srcFile2 = dstFile2;
					dstFile2 = srcDir+"net"+"-"+n+"-"+m+"-"+k+"-"+r+"-2.graphml";
					net = new GraphMLReader(srcFile2).createNet();
	/*				if(!this._delRedundantLink(net, 1)){
						sb1.append("\r\n#################################################\r\n");
						break;
					}*/
					g.script(net, dstFile2);
					srcFile2 = dstFile2;
					driverNodeNum = this._statistics(srcFile2, dstFile2);
					driverNodeRate = (double)driverNodeNum/n;
					sb1.append(driverNodeRate).append("\r\n");
					
					if(k > 1){
						break;
					}
					
					k++;
					D.p("#############");
				}
				
				ft.write(sb1, dstDir+"driverNodeRate.txt",true);
			}
		}
		
	}
	public void scriptAddLink(){
		FileTool ft = new FileTool();
		String srcDir = "../data/ctrl2/sf17/";
		String dstDir = "../data/ctrl2/sf17/";
		ft.mkdir(dstDir);
		String srcFile = null, dstFile = null, srcFile1 = null, dstFile1= null, srcFile2 = null, dstFile2 = null;
		int max = 20; //平均度的最大值
		//int n = 2000; //节点的数量
		int m = 0; //边的数量
		
		StringBuffer temp = new StringBuffer();
		StringBuffer temp2 = new StringBuffer();
		StringBuffer sb1 = new StringBuffer();
		StringBuffer sb2 = new StringBuffer();
		
		Network net = null;
		AssortativityCoefficient ac = new AssortativityCoefficient();
		GraphML g = new GraphML();

		
		int driverNodeNum = 0;
		double driverNodeRate = 0;
		double netAC = 0;
		DecimalFormat df = new DecimalFormat();
		df.setMaximumFractionDigits(15);
		Map<Number, Link<Number>> stems = null;
		ft.clear(dstDir+"driverNodeRate.txt");
		ft.clear(dstDir+"netAC.txt");
		for(int avgK = 8; avgK <= max; avgK +=2){
		Vector<Integer> ns = new Vector<Integer>();
		ns.add(100);
		ns.add(500);
		ns.add(1000);
		ns.add(5000);
		
		//int avgK = 4;
		for(int n:ns){
			m = avgK;
			//for(double p = 0.1; p < 1; p += 0.1){
			//for(double a = 0; a <= 8*m; a += m){
				double a = 2*m;
				
				double p = (double)m/(m+a);
				double r = 2+(double)a/m;
				temp = new StringBuffer();
				temp2 = new StringBuffer();
				sb1 = new StringBuffer();
				sb2 = new StringBuffer();
				/*
				 * create sf network
				 */
				this.testNetSF(srcDir, n, m, p, r);
				srcFile = srcDir+"net"+"-"+n+"-"+m+"-"+r+".graphml";
				
				/*
				 * statistic the original net the driver node and redundant link
				 */
				dstFile2 = dstFile1 = dstFile = srcDir+"net"+"-"+n+"-"+m+"-"+r+"-0.graphml";
				driverNodeNum = this._statistics(srcFile, dstFile);
				D.p(dstFile);
				net = new GraphMLReader(dstFile).createNet();
				netAC = ac.script(net, DegreeType.IN, DegreeType.OUT);
				
				driverNodeRate = (double)driverNodeNum/n;
				temp.append(n).append("\t").append(n*m).append("\t").append(avgK).append("\t").append(r).append("\t").append(df.format(driverNodeRate)).append("\t");
				temp2.append(n).append("\t").append(n*m).append("\t").append(avgK).append("\t").append(r).append("\t").append(df.format(netAC)).append("\t");
				//multiply 
				//for(int k = 1; k <= 5; k++){
				int k = 1;
				//D.p("#############");
				//while(true){
					/*
					 * rewiring the links
					 */
					sb1.append(temp);
					sb2.append(temp2);
					
					//srcFile1 = dstFile1;
					//dstFile1 = srcDir+"net"+"-"+n+"-"+m+"-"+k+"-"+r+"-1.graphml";
					//D.p(srcFile1);
					//net = new GraphMLReader(dstFile1).createNet();
					//this._rewiringByRedundantLink(net);
					/*if(!this._rewiringByRedundantLink(net)){
						sb1.append("\r\n################################################\r\n");
						break;
					}*/
					//g.script(net, dstFile1);
					//netAC = ac.script(net, DegreeType.IN, DegreeType.OUT);
					//sb2.append(k).append("\t").append(df.format(netAC)).append("\t");
					
					//srcFile1 = dstFile1;
					//driverNodeNum = this._statistics(srcFile1, dstFile1);
					//driverNodeRate = (double)driverNodeNum/n;
					//sb1.append(k).append("\t").append(df.format(driverNodeRate)).append("\t");
					/*
					 * del the redundant links
					 */
					srcFile2 = dstFile2;
					dstFile2 = srcDir+"net"+"-"+n+"-"+m+"-"+k+"-"+r+"-2.graphml";
					//D.p(srcFile2);
					net = new GraphMLReader(srcFile2).createNet();
					/*if(!this._delRedundantLink(net, 1)){
						sb1.append("\r\n#################################################\r\n");
						break;
					}*/
					
					//stems = this._getStems(srcFile2);
					//D.p(stems.get(2002).getList());
					//D.p(stems);
					int count = this._delRedundantLink(net, 1);
					//this._addLink(net, stems, count);
					this._addLinkByInAndOut(net, count);
					//this._rewiring(net, 0.05);
					netAC = ac.script(net, DegreeType.IN, DegreeType.OUT);
					sb2.append(df.format(netAC)).append("\r\n");
					
					g.script(net, dstFile2);
					srcFile2 = dstFile2;
					driverNodeNum = this._statistics(srcFile2, dstFile2);
					driverNodeRate = (double)driverNodeNum/n;
					sb1.append(df.format(driverNodeRate)).append("\r\n");
					
					/*if(k > 2){
						break;
					}*/
					
					k++;
					D.p("#############");
				//}
				//D.p("@@@@@@@@@@@@@@@@@");
				//sb1.append("################################################\r\n");
				ft.write(sb1, dstDir+"driverNodeRate.txt",true);
				ft.write(sb2, dstDir+"netAC.txt",true);
			}
		//}
		}
		
	}
	
	/**
	 *  
	 *  @param args
	 */
	public static void main(String[] args) {
		// TODO 自动生成的方法存根
		SFCtrl ctrl = new SFCtrl();
		//ctrl.script();
		ctrl.scriptAddLink();
	}

}
