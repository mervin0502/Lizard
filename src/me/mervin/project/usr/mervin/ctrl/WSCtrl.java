package me.mervin.project.usr.mervin.ctrl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import me.mervin.core.Edge;
import me.mervin.core.Network;
import me.mervin.core.Global.NetType;
import me.mervin.model.ER;
import me.mervin.model.WSNetwork;
import me.mervin.module.feature.AssortativityCoefficient;
import me.mervin.module.feature.Degree;
import me.mervin.module.graphFormats.GraphML;
import me.mervin.module.graphFormats.GraphMLReader;
import me.mervin.util.D;
import me.mervin.util.FileTool;
import me.mervin.util.PairList;


 /**
 *   ERCtrl.java
 *    
 *  @author Mervin.Wong  DateTime 2013-10-18 下午7:37:00    
 *  @version 0.4.0
 */
public class WSCtrl {

	/**
	 */
	public WSCtrl() {
		// TODO 自动生成的构造函数存根
	}

	//生成网络
	public void testNetWS(String srcDir, int n, int k, double p){
		WSNetwork ws = new WSNetwork(NetType.DIRECTED);
		ws.set(n, k, p);
		ws.createModelNetwork();
		GraphML g = new GraphML();
		g.script(ws, srcDir+"net-"+n+"-"+k+"-"+p+".graphml");
	}
	
	//计算该网络的驱动节点和redundant link
	private int _statistics(String srcFile, String dstFile){
		int count = 0;
		try {
			//String cmd = "netctrl -m liu -M graph -o "+dstFile+" -f graphml -F graphml "+srcFile;
			String cmd = "netctrl  -M graph -o "+dstFile+" -f graphml -F graphml "+srcFile;
//			D.p(cmd);
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
				
				net.insertEdge(l2, r2);
				net.deleteEdge(l1, r1);
			}
		}
		return flag;
	}
	
	
	private boolean _delRedundantLink(Network net, double p){
		PairList<Number, Number> edgeList = net.traverseEdge();
		
		Number l1, r1, l2, r2;
		
		Edge edge = null;
		
		boolean flag = false;
		//PairList<Number, Number> redundantEdge = new PairList<Number, Number>();
		for(int i = 0; i < edgeList.size(); i++){
			l1 = edgeList.getL(i);
			r1 = edgeList.getR(i);
			
			edge = net.getEdgeByNodeId(l1, r1);
			Degree d = new Degree(net);
			if(edge.getAttr("edge_class").equalsIgnoreCase("redundant")){
				flag = true;
				if((d.nodeInDegree(l1)+d.nodeOutDegree(l1) > 1)  && (d.nodeInDegree(r1)+d.nodeOutDegree(r1) > 1)){
					net.deleteEdge(l1, r1);
				}
			}
		}
		return flag;
	}
	
	public void script(){
		FileTool ft = new FileTool();
		String srcDir = "../data/ctrl2/ws2/";
		String dstDir = "../data/ctrl2/ws2/";
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
			for(double p = 0.1; p < 1; p += 0.1){
				temp = new StringBuffer();
				sb1 = new StringBuffer();
				/*
				 * create ws network
				 */
				this.testNetWS(srcDir, n, m, p);
				srcFile = srcDir+"net"+"-"+n+"-"+m+"-"+p+".graphml";

				/*
				 * statistic the original net the driver node and redundant link
				 */
				dstFile2 = dstFile1 = dstFile = srcDir+"net"+"-"+n+"-"+m+"-"+p+"-0.graphml";
				driverNodeNum = this._statistics(srcFile, dstFile);
				driverNodeRate = (double)driverNodeNum/n;
				temp.append(n).append("\t").append(n*m).append("\t").append(avgK).append("\t").append(p).append("\t").append(driverNodeRate).append("\t");
				//multiply 
				//for(int k = 1; k <= 5; k++){
				int k = 1;
				while(true){
					/*
					 * rewiring the links
					 */
					sb1.append(temp);
					
					srcFile1 = dstFile1;
					dstFile1 = srcDir+"net"+"-"+n+"-"+m+"-"+k+"-"+p+"-1.graphml";
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
					dstFile2 = srcDir+"net"+"-"+n+"-"+m+"-"+k+"-"+p+"-2.graphml";
					net = new GraphMLReader(srcFile2).createNet();
					if(!this._delRedundantLink(net, 1)){
						sb1.append("\r\n#################################################\r\n");
						break;
					}
					g.script(net, dstFile2);
					srcFile2 = dstFile2;
					driverNodeNum = this._statistics(srcFile2, dstFile2);
					driverNodeRate = (double)driverNodeNum/n;
					sb1.append(driverNodeRate).append("\r\n");
					
					if(k > 10){
						break;
					}
					
					k++;
					D.p("#############");
				}
				
				ft.write(sb1, dstDir+"driverNodeRate.txt",true);
			}
		}
		
	}
	
	/**
	 *  
	 *  @param args
	 */
	public static void main(String[] args) {
		// TODO 自动生成的方法存根
		WSCtrl ctrl = new WSCtrl();
		ctrl.script();
	}

}
