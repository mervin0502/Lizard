package me.mervin.project.usr;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import me.mervin.core.Network;
import me.mervin.core.Global.NetType;
import me.mervin.core.Global.NumberType;
import me.mervin.module.feature.ClusterCofficient;
import me.mervin.module.feature.Coreness;
import me.mervin.module.feature.Degree;
import me.mervin.util.D;
import me.mervin.util.FileTool;

/*
 * CoreEvolution
 *  度的演化
 * @category 
 * @author mervin
 * **********************************************
 *  * **********************************************
 *  注：sourcePath desPath  目录路径最后需要加斜杠
 */
public class DegreeEvolution {
	private String sourcePath = null;
	private String desPath = null;
	
	/*
	 * 构造函数
	 */
	public DegreeEvolution(String sourcePath, String desPath) {
		// TODO Auto-generated constructor stub
		this.sourcePath = sourcePath;
		this.desPath = desPath;
		
		//(new File(this.sourcePath)).mkdirs();//创建相应的目录
		//(new File(this.desPath)).mkdirs();
	}
	
	/*
	 * 新出现的节点在后续的网络中的度值, 及其所在的核
	 * 以及该节点的邻接点，及其度值 ，及其所在的核
	 * 
	 * 消亡节点在前网络中度值，以及所在的核
	 * 以及该节点的邻接点，及其度值，核
	 */
	public void nodeDegreeOrCore(int j){
		// j:1~11
		String netFileName = this.sourcePath+j+"/.txt";
		String nodstFileName = null;
		Set<Number> birthNodes = new HashSet<Number>();
		Map<Number, Number> birthNodesDegree = null;//新生节点的度值
		HashMap<Number, Number> birthNodesCore = null;//新生节点的核
//		HashMap<Number, Number> birthNodesAdjDegree = null;//新生邻接点的度值
//		HashMap<Number, Number> birthNodesAdjCore = null;//新生邻接点的核
		
		Set<Number> deathNodes = new HashSet<Number>();
		Map<Number, Number> deathNodesDegree = null;//消亡节点的度值
		HashMap<Number, Number> deathNodesCore = null;//消亡节点的核
//		HashMap<Number, Number> deathNodesAdjDegree = null;//消亡邻接点的度值
//		HashMap<Number, Number> deathNodesAdjCore = null;//消亡邻接点的核
		
		FileTool f = new FileTool();
		Network net = new Network(netFileName, NetType.UNDIRECTED, NumberType.LONG);
		Degree d = new Degree(net);
		Coreness c = new Coreness(net);
		
		StringBuffer sb = null;
		Number nodeId = null;
		/*
		 * 新生节点
		 */
		for(int i = 1; i < j; i++){
			sb = new StringBuffer();
			sb.append("#nodeId").append("\t").append("birthNodeDegree").append("\t").append("birthNodeCore").append("\r\n");
			
			nodstFileName = this.desPath+i+"-"+(i+1)+"/birthNodes.txt";
			birthNodes = f.read2Set(nodstFileName, NumberType.LONG);
			birthNodesDegree = d.nodeDegree(birthNodes);
			birthNodesCore = (HashMap<Number, Number>) c.nodeCore(birthNodes);
			//birthNodesAdjDegree = d.nodesAdjDegree(birthNodes);
			//birthNodesAdjCore = c.nodesCore(nodesId);
			for(Iterator<Number> it = birthNodes.iterator(); it.hasNext();){
				nodeId = it.next();
				sb.append(nodeId).append("\t");
				sb.append(birthNodesDegree.get(nodeId)).append("\t");
				sb.append(birthNodesCore.get(nodeId)).append("\r\n");
			}
			f.write(sb, this.desPath+i+"-"+(i+1)+"/birthNodeDegree-Core-"+j+".txt", false);
		}
		/*
		 * 死亡节点
		 */
		for(int i = j; i < 48; i++){
			sb = new StringBuffer();
			sb.append("#nodeId").append("\t").append("deathNodeDegree").append("\t").append("deathNodeCore").append("\r\n");
			
			nodstFileName = this.desPath+i+"-"+(i+1)+"/deathNodes.txt";
			deathNodes = f.read2Set(nodstFileName, NumberType.LONG);
			deathNodesDegree = d.nodeDegree(deathNodes);
			deathNodesCore = (HashMap<Number, Number>) c.nodeCore(deathNodes);
			//deathNodesAdjDegree = d.nodesAdjDegree(deathNodes);
			//deathNodesAdjCore = c.nodesCore(nodesId);
			for(Iterator<Number> it = deathNodes.iterator(); it.hasNext();){
				nodeId = it.next();
				sb.append(nodeId).append("\t");
				sb.append(deathNodesDegree.get(nodeId)).append("\t");
				sb.append(deathNodesCore.get(nodeId)).append("\r\n");
			}
			f.write(sb, this.desPath+i+"-"+(i+1)+"/deathNodeDegree-Core-"+j+".txt", false);
		}
	}
	public void adjNodeDegreeOrCore(int j){
		// j:1~11
		String netFileName = this.sourcePath+j+".txt";
		String nodstFileName = null;
		Set<Number> birthNodes = new HashSet<Number>();
		Map<Number, Number> birthNodesAdjDegree = null;//新生邻接点的度值
		Map<Number, Number> birthNodesAdjCore = null;//新生邻接点的核
		
		Set<Number> deathNodes = new HashSet<Number>();
		Map<Number, Number> deathNodesAdjDegree = null;//消亡邻接点的度值
		Map<Number, Number> deathNodesAdjCore = null;//消亡邻接点的核
		
		FileTool f = new FileTool();
		Network net = new Network(netFileName, NetType.UNDIRECTED, NumberType.LONG);
		Degree d = new Degree(net);
		Coreness c = new Coreness(net);
		ClusterCofficient cc = new ClusterCofficient(net);
		
		StringBuffer sb = null, sb1 = null, sb2 = null;
		Number nodeId = null;
		Number adjNodeId = null;
		Set<Number> adjNodeIdSet = null; 
		int degree = 0, core = 0, adjSum = 0, degreeSum = 0, coreSum = 0;
		/*
		 * 新生节点
		 */
		for(int i = 1; i < j; i++){
			sb = new StringBuffer();
			sb1 = new StringBuffer();
			sb2 = new StringBuffer();
			
			nodstFileName = this.desPath+i+"-"+(i+1)+"/birthNodes.txt";
			birthNodes = f.read2Set(nodstFileName, NumberType.LONG);
			birthNodesAdjDegree = d.nodeAdjDegree(birthNodes);
			birthNodesAdjCore = c.adjNodeCore(birthNodes);
			for(Iterator<Number> it = birthNodes.iterator(); it.hasNext();){
				nodeId = it.next();
				adjNodeIdSet = net.getAdjNodeId(nodeId);
				adjSum = adjNodeIdSet.size();
				sb.append(nodeId).append("\r\n");
				
				degreeSum = 0;
				coreSum = 0;
				for(Iterator<Number> it2 = adjNodeIdSet.iterator(); it2.hasNext();){
					adjNodeId = it2.next();
					degree = birthNodesAdjDegree.get(adjNodeId).intValue();
					core = birthNodesAdjCore.get(adjNodeId).intValue();
					degreeSum += degree;
					coreSum += core;
					sb.append("\t").append(adjNodeId).append("\t").append(degree).append("\t").append(core).append("\r\n");
					
				}
				sb.append("\t").append("average: ").append((float)degreeSum/adjSum).append("\t").append((float)coreSum/adjSum).append("\r\n");
				sb1.append(nodeId).append("\t").append((float)degreeSum/adjSum).append("\t").append((float)coreSum/adjSum).append("\r\n");;
				sb2.append(nodeId).append(cc.nodeClusterCofficient(nodeId)).append("\r\n");
			}
			f.write(sb, this.desPath+i+"-"+(i+1)+"/birthNodeAdjDegree-Core-1-"+j+".txt", false);
			f.write(sb1, this.desPath+i+"-"+(i+1)+"/birthNodeAdjeDegree-Core-2-"+j+".txt", false);
			f.write(sb1, this.desPath+i+"-"+(i+1)+"/birthNodeAdjeCC-"+j+".txt", false);
		}
		/*
		 * 死亡节点
		 */
		for(int i = j; i < 48; i++){
			sb = new StringBuffer();
			sb1 = new StringBuffer();
			sb2 = new StringBuffer();
			
			nodstFileName = this.desPath+i+"-"+(i+1)+"/deathNodes.txt";
			deathNodes = f.read2Set(nodstFileName, NumberType.LONG);
			deathNodesAdjDegree = d.nodeAdjDegree(deathNodes);
			deathNodesAdjCore = c.adjNodeCore(deathNodes);
			for(Iterator<Number> it = deathNodes.iterator(); it.hasNext();){
				nodeId = it.next();
				adjNodeIdSet = net.getAdjNodeId(nodeId);
				adjSum = adjNodeIdSet.size();
				sb.append(nodeId).append("\r\n");
				
				degreeSum = 0;
				coreSum = 0;
				for(Iterator<Number> it2 = adjNodeIdSet.iterator(); it2.hasNext();){
					adjNodeId = it2.next();
					degree = deathNodesAdjDegree.get(adjNodeId).intValue();
					core = deathNodesAdjCore.get(adjNodeId).intValue();
					degreeSum += degree;
					coreSum += core;
					sb.append("\t").append(adjNodeId).append("\t").append(degree).append("\t").append(core).append("\r\n");
					
				}
				sb.append("\t").append("average: ").append((float)degreeSum/adjSum).append("\t").append((float)coreSum/adjSum).append("\r\n");
				sb1.append(nodeId).append("\t").append((float)degreeSum/adjSum).append("\t").append((float)coreSum/adjSum).append("\r\n");;
				sb2.append(nodeId).append(cc.nodeClusterCofficient(nodeId)).append("\r\n");

			}
			f.write(sb, this.desPath+i+"-"+(i+1)+"/deathNodeAdjDegree-Core-1-"+j+".txt", false);
			f.write(sb1, this.desPath+i+"-"+(i+1)+"/deathNodeAdjDegree-Core-2-"+j+".txt", false);
			f.write(sb2, this.desPath+i+"-"+(i+1)+"/deathNodeAdjCC-"+j+".txt", false);
		}
	}
	
	
/*	public void deathNodesAdjDegree(int j){
		// j:1~11
		String netFileName = this.sourcePath+j+"/reduce-1.txt";
		String nodstFileName = null;
		HashSet<Number> deathNodes = new HashSet<Number>();
		HashMap<Number, Number> deathNodesAdjDegree = null;
		FileTool f = new FileTool();
 		Network net = new Network(netFileName, NetType.UNDIRECTED, NumberType.LONG);
 		//Coreness c = new Coreness(net);
 		Degree d = new Degree(net);
		for(int i = j; i > 0; i--){
			nodstFileName = this.sourcePath+j+"/deathNodes.txt";
			deathNodes = f.read2Set(nodstFileName, NumberType.LONG);
			//birthNodesCore = c.nodesCore(birthNodes);
			deathNodesAdjDegree = d.nodesAdjDegree(deathNodes);
			f.write(deathNodesAdjDegree, this.desPath+j+"/deathNodesAdjDegree-"+i+".txt", false);
		}
	}*/
	
	public static void main(String[] args){
		DegreeEvolution de = new DegreeEvolution("../data/AS-2009-2012/all/", "../data/AS-2009-2012/Evolution/");
		for(int i = 1; i <= 48; i++){
			de.adjNodeDegreeOrCore(i);	
		}
		//ce.deathNodesCore(1);
		D.m();
	}
	
}
