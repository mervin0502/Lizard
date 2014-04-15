package me.mervin.project.usr.mervin.ctrl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import me.mervin.core.Edge;
import me.mervin.core.Network;
import me.mervin.core.Global.DegreeType;
import me.mervin.core.Global.NetType;
import me.mervin.model.ER;
import me.mervin.module.feature.AssortativityCoefficient;
import me.mervin.module.feature.Degree;
import me.mervin.module.graphFormats.GraphML;
import me.mervin.module.graphFormats.GraphMLReader;
import me.mervin.util.D;
import me.mervin.util.FileTool;
import me.mervin.util.Link;
import me.mervin.util.MapTool;
import me.mervin.util.PairList;


 /**
 *   ERCtrl.java
 *    
 *  @author Mervin.Wong  DateTime 2013-10-18 下午7:37:00    
 *  @version 0.4.0
 */
public class ERCtrl {

	/**
	 */
	public ERCtrl() {
		// TODO 自动生成的构造函数存根
	}

	//生成网络
	public void testNetER(String srcDir, int n, int m){
		ER er = new ER(NetType.DIRECTED);
		er.set(n, m);
		er.createModelNetwork();
		GraphML g = new GraphML();
		g.script(er, srcDir+"net-"+n+"-"+m+".graphml");
	}
	
	//计算该网络的驱动节点和redundant link
	private int _statistics(String srcFile, String dstFile){
		int count = 0;
		try {
			//String cmd = "netctrl -m liu -M graph -o "+dstFile+" -f graphml -F graphml "+srcFile;
			String cmd = "netctrl -m liu -M graph -o "+dstFile+" -f graphml -F graphml "+srcFile;
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
		int count = 0;
		//PairList<Number, Number> redundantEdge = new PairList<Number, Number>();
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
				pl = new MapTool().sort(stemSize);
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
			
			  
			
			int nodeSize = inDegreeList.size();
			int s = 0, t = 0;
			boolean flag = true;
			for(int i = 0, j=0; i < count;j++){
				if(j >= (nodeSize-Math.max(s, t))){
					if(flag){
						s++;
						flag = false;
					}else{
						t++;
						flag = true;
					}
					j = 0;
				}
				l = inDegreeList.getL(j+t);
				r = outDegreeList.getL(j+s);
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
		public void script(){
		FileTool ft = new FileTool();
		String srcDir = "../data/ctrl2/er2/";
		String dstDir = "../data/ctrl2/er2/";
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
		for(int avgK = 1; avgK <= max; avgK++){
			temp = new StringBuffer();
			sb1 = new StringBuffer();
			m = avgK*n;
			/*
			 * create ER network
			 */
			this.testNetER(srcDir, n, m);
			srcFile = srcDir+"net"+"-"+n+"-"+m+".graphml";

			/*
			 * statistic the original net the driver node and redundant link
			 */
			dstFile2 = dstFile1 = dstFile = srcDir+"net"+"-"+n+"-"+m+"-0.graphml";
			driverNodeNum = this._statistics(srcFile, dstFile);
			driverNodeRate = (double)driverNodeNum/n;
			temp.append(n).append("\t").append(m).append("\t").append(avgK).append("\t").append(driverNodeRate).append("\t");
			//multiply 
			//for(int k = 1; k <= 5; k++){
			int k = 1;
			while(true){
				/*
				 * rewiring the links
				 */
				sb1.append(temp);
				
				srcFile1 = dstFile1;
				dstFile1 = srcDir+"net"+"-"+n+"-"+m+"-"+k+"-1.graphml";
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
				dstFile2 = srcDir+"net"+"-"+n+"-"+m+"-"+k+"-2.graphml";
				net = new GraphMLReader(srcFile2).createNet();
				/*if(!this._delRedundantLink(net, 1)){
					sb1.append("\r\n#################################################\r\n");
					break;
				}*/
				g.script(net, dstFile2);
				srcFile2 = dstFile2;
				driverNodeNum = this._statistics(srcFile2, dstFile2);
				driverNodeRate = (double)driverNodeNum/n;
				sb1.append(driverNodeRate).append("\r\n");
				if(k > 20){
					break;
					
				}
				k++;
				D.p("#############");
			}
			
			ft.write(sb1, dstDir+"driverNodeRate.txt", true);
		}
		
		
	}
		public void scriptAddLink(){
			FileTool ft = new FileTool();
			String srcDir = "../data/ctrl2/er4/";
			String dstDir = "../data/ctrl2/er4/";
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
			
			DecimalFormat df = new DecimalFormat();
			df.setMaximumFractionDigits(15);
			int driverNodeNum = 0;
			double driverNodeRate = 0;
			double netAC = 0;
			ft.clear(dstDir+"driverNodeRate.txt");
			ft.clear(dstDir+"netAC.txt");
			Map<Number, Link<Number>> stems = null;
			Vector<Integer> ns = new Vector<Integer>();
			//ns.add(100);
			//ns.add(500);
			//ns.add(1000);
			ns.add(5000);
			for(int n:ns){
				for(int avgK = 2; avgK <= max; avgK+=2){
					temp = new StringBuffer();
					temp2 = new StringBuffer();
					sb1 = new StringBuffer();
					sb2 = new StringBuffer();
					m = avgK*n;
					/*
					 * create ER network
					 */
					this.testNetER(srcDir, n, m);
					srcFile = srcDir+"net"+"-"+n+"-"+m+".graphml";
					
					/*
					 * statistic the original net the driver node and redundant link
					 */
					dstFile2 = dstFile1 = dstFile = srcDir+"net"+"-"+n+"-"+m+"-0.graphml";
					driverNodeNum = this._statistics(srcFile, dstFile);
					driverNodeRate = (double)driverNodeNum/n;
					temp.append(n).append("\t").append(m).append("\t").append(avgK).append("\t").append(df.format(driverNodeRate)).append("\t");
					temp2.append(n).append("\t").append(m).append("\t").append(avgK).append("\t").append(df.format(netAC)).append("\t");
					//multiply 
					//for(int k = 1; k <= 5; k++){
					int k = 1;
					while(true){
						/*
						 * rewiring the links
						 */
						sb1.append(temp);
						sb2.append(temp2);
						srcFile1 = dstFile1;
						dstFile1 = srcDir+"net"+"-"+n+"-"+m+"-"+k+"-1.graphml";
						net = new GraphMLReader(srcFile1).createNet();
						this._rewiringByRedundantLink(net);
						/*if(!this._rewiringByRedundantLink(net)){
							sb1.append("\r\n################################################\r\n");
							break;
						}*/
						g.script(net, dstFile1);
						netAC = ac.script(net, DegreeType.IN, DegreeType.OUT);
						sb2.append(k).append("\t").append(df.format(netAC)).append("\t");
						srcFile1 = dstFile1;
						driverNodeNum = this._statistics(srcFile1, dstFile1);
						driverNodeRate = (double)driverNodeNum/n;
						sb1.append(k).append("\t").append(df.format(driverNodeRate)).append("\t");
						/*
						 * del the redundant links
						 */
						srcFile2 = dstFile2;
						dstFile2 = srcDir+"net"+"-"+n+"-"+m+"-"+k+"-2.graphml";
						net = new GraphMLReader(srcFile2).createNet();
	/*					if(!this._delRedundantLink(net, 1)){
							sb1.append("\r\n#################################################\r\n");
							break;
						}*/
						//stems = this._getStems(srcFile2);
						//D.p(stems.get(2002).getList());
						//D.p(stems);
						int count = this._delRedundantLink(net, 1);
						//this._addLink(net, stems, count);
						this._addLinkByInAndOut(net, count);
						netAC = ac.script(net, DegreeType.IN, DegreeType.OUT);
						sb2.append(df.format(netAC)).append("\r\n");
						g.script(net, dstFile2);
						srcFile2 = dstFile2;
						driverNodeNum = this._statistics(srcFile2, dstFile2);
						driverNodeRate = (double)driverNodeNum/n;
						sb1.append(df.format(driverNodeRate)).append("\r\n");
						if(k >= 1){
							break;
							
						}
						k++;
						D.p("#############");
					}
					
					ft.write(sb1, dstDir+"driverNodeRate.txt", true);
					ft.write(sb2, dstDir+"netAC.txt",true);
				}
			}

			
			
		}
	
	/**
	 *  
	 *  @param args
	 */
	public static void main(String[] args) {
		// TODO 自动生成的方法存根
		ERCtrl erc = new ERCtrl();
		//erc.script();
		erc.scriptAddLink();
	}

}
