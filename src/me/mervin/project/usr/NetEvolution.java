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
package me.mervin.project.usr;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import me.mervin.core.Network;
import me.mervin.core.Global.NetType;
import me.mervin.core.Global.NumberType;
import me.mervin.module.feature.Degree;
import me.mervin.module.feature.MultiNet;
import me.mervin.util.D;
import me.mervin.util.EdgeDeweigh;
import me.mervin.util.FileTool;
import me.mervin.util.MathTool;
/**
 * NetEvolution.java
 * 
 *@author 王进法<Mervin.Wong>
 *@version 0.4
 */

public class NetEvolution {
	private String sourcePath = null;
	private String desPath = null;
	//private String file1 = null;
	//private String file2 = null;
	
	
	//private Network net1 = null;
	//private Network net2 = null;
	
	/**
	 * 
	 */
	public NetEvolution(String sourcePath, String desPath) {
		// TODO Auto-generated constructor stub
		this.sourcePath = sourcePath;
		this.desPath = desPath;
		
		(new File(this.sourcePath)).mkdirs();
		(new File(this.desPath)).mkdirs();
		
		//this.file1 = file1;//旧网络
		//this.file2 = file2;//新网络
	}
	
	//合并多个文件
	public void combineFile(){
		String[] fileNames = null;
		String curFileName = null;
		String dstFileName = null;
		String prefix = null;
		String temp = null;
		
		BufferedReader reader = null;
		BufferedWriter writer = null;
		
		File dirFile = new File(this.sourcePath);
		if(dirFile.isDirectory()){
			fileNames = dirFile.list();
			int i = 0;
			for(i = 0; i < fileNames.length; i++){
				if(prefix == null || prefix.isEmpty()){
					//合并文件的条件
					prefix = fileNames[i].substring(0, 2);
				}
				
				if(!prefix.equals(fileNames[i].substring(0, 2))){
					prefix = fileNames[i].substring(0, 2);
				}
				
				curFileName = this.sourcePath+fileNames[i];
				dstFileName = this.desPath+prefix+".txt";
				
				try {
					reader = new BufferedReader(new FileReader(curFileName));
					writer = new BufferedWriter(new FileWriter(dstFileName,true));
					while((temp = reader.readLine()) != null){
						writer.write(temp);
						writer.newLine();
					}
					reader.close();
					writer.flush();
				} catch (FileNotFoundException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				} catch (IOException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
			}
		}
	}
	public void  extract(){
		/**
		 *   提取IP链路为地址对
		 */
		//(new ExtractIPByLink(this.sourcePath+this.file1+".txt","../data/jfk2/temp1.txt")).run();
		D.s("提取IP链路为地址对");
		/**
		 *   地址对去重
		 */
		//(new EdgeDeweigh(this.sourcePath+this.file1,this.desPath+"reduce-1.txt")).run();
		D.s("地址对去重");
		
	//	(new ExtractIPByLink(this.sourcePath+this.file2+".txt","../data/jfk2/temp2.txt")).run();
		D.s("提取IP链路为地址对");
		/**
		 *   地址对去重
		 */
	//	(new EdgeDeweigh(this.sourcePath+this.file2,this.desPath+"reduce-2.txt")).reduce();
		D.s("地址对去重");
		
	}
	
/*	public  createNet(){
		net1 = new Network(this.sourcePath+"reduce-1.txt", NetType.UNDIRECTED, NumberType.INTEGER);
		//net2 = new Network(this.sourcePath+"reduce-2.txt", NetType.UNDIRECTED, NumberType.INTEGER);
	}*/
	public void nodeComputing(Network net1, Network net2){
		FileTool fileTool = new FileTool();
		Degree degree = new Degree();
		/**
		 *   新生 死亡节点
		 */
		
		Set<Number> deathNodesSet = null;
		Set<Number> birthNodesSet = null;
		
		MultiNet m = new MultiNet(net1, net2);
		deathNodesSet = m.deathNode();//死亡节点
		new FileTool().write(deathNodesSet, this.desPath+"deathNodes.txt", false);//保存新生节点
		
		birthNodesSet = m.birthNode();//新生节点
		fileTool.write(birthNodesSet, this.desPath+"birthNodes.txt", false);//保存新生节点
		D.s("新生 死亡节点");
		
		
		Map<Number, Number> deathMap =null;
		/**
		 *   新生 死亡节点度
		 */
		Map<Number, Number> birthMap = null;
		
		deathMap= degree.nodeDegree(net1, deathNodesSet); 
		fileTool.write(deathMap, this.desPath+"deathNodesDegree.txt", false);
		
		birthMap = degree.nodeDegree(net2, birthNodesSet); 
		fileTool.write(birthMap, this.desPath+"birthNodesDegree.txt", false);
		D.s("新生 死亡节点度");
		
		
		Map<Number, Number> deathNodesFrequency = null;
		/**
		 *   新生 死亡节点度分布
		 */
		Map<Number, Number> birthNodesFrequency = null;
		
		deathNodesFrequency = MathTool.frequency(deathMap);
		fileTool.write(deathNodesFrequency, this.desPath+"deathNodesDegreeFrequency.txt", false);
		
		birthNodesFrequency = MathTool.frequency(deathMap);
		fileTool.write(birthNodesFrequency, this.desPath+"birthNodesDegreeFrequency.txt", false);
		
		D.s("新生 死亡节点度分布");
		
		
		/**
		 *  新生 死亡节点度分布率
		 */
		
		Map<Number, Number> deathNodesRate = null, birthNodesRate = null;
		
		deathNodesRate = MathTool.ratio(deathNodesFrequency);
		fileTool.write(deathNodesRate, this.desPath+"deathNodesDegreeRate.txt", false);
		
		birthNodesRate = MathTool.ratio(birthNodesFrequency);
		fileTool.write(birthNodesRate, this.desPath+"birthNodesDegreeRate.txt", false);
		D.s("新生 死亡节点度分布率");
		
		/**
		 *   新生 死亡节点的邻接点
		 */
		Set<Number> adjNodes = null;
		HashSet<Number> deathAdjNodesSet = new HashSet<Number>();
		
		Number nodeId = null;
		for (Iterator<Number> iterator = deathNodesSet.iterator(); iterator.hasNext();) {
			nodeId = (Number) iterator.next();
			adjNodes = net1.getAdjNodeId(nodeId);
			for (Iterator<Number> iterator2 = adjNodes.iterator(); iterator2.hasNext();) {
				deathAdjNodesSet.add(iterator2.next());
			}
		}
		fileTool.write(deathAdjNodesSet, this.desPath+"deathAdjNoes.txt", false);
		
		HashSet<Number> birthAdjNodesSet = new HashSet<Number>();
		for (Iterator<Number> iterator = birthNodesSet.iterator(); iterator.hasNext();) {
			nodeId = (Number) iterator.next();
			adjNodes = net2.getAdjNodeId(nodeId);
			for (Iterator<Number> iterator2 = adjNodes.iterator(); iterator2.hasNext();) {
				birthAdjNodesSet.add(iterator2.next());
			}
		}
		fileTool.write(birthAdjNodesSet, this.desPath+"birthAdjNoes.txt", false);
		D.s("新生节点的邻接点");
		
		
		/**
		 *   新生节点的邻接点度
		 */
		deathMap = degree.nodeDegree(net1, deathAdjNodesSet); 
		fileTool.write(deathMap, this.desPath+"deathAdjNoesDegre.txt", false);
		
		birthMap = degree.nodeDegree(net2, birthAdjNodesSet); 
		fileTool.write(birthMap, this.desPath+"birthAdjNoesDegre.txt", false);
		D.s("新生邻接点度");
		
		/**
		 * 新生节点的邻接点度分布
		 */
		deathNodesFrequency = MathTool.frequency(deathMap);
		fileTool.write(deathNodesFrequency, this.desPath+"deathAdjNodesDegreeFrequency.txt", false);
		
		birthNodesFrequency = MathTool.frequency(birthMap);
		fileTool.write(birthNodesFrequency, this.desPath+"birthAdjNodesDegreeFrequency.txt", false);
		D.s("新生邻接点度分布");
		
		/**
		 * 新生节点的邻接点度分布率
		 */
		deathNodesRate = MathTool.ratio(deathNodesFrequency);
		fileTool.write(deathNodesRate, this.desPath+"deathAdjNodesDegreeRate.txt", false);
		
		birthNodesRate = MathTool.ratio(birthNodesFrequency);
		fileTool.write(birthNodesRate, this.desPath+"birthAdjNodesDegreeRate.txt", false);
		D.s("新生邻接点度分布率");
		
		/**
		 *   网络的节点度分布
		 */
		Map<Number, Number> map = null;
		map = degree.netDegreeDistribution(net1);
		fileTool.write(map, this.desPath+"1-netDegreeFrequency.txt", false);
		
		map = degree.netDegreeDistribution(net2);
		fileTool.write(map, this.desPath+"2-netDegreeFrequency.txt", false);
		D.s("网络的节点度分布");
		
		/**
		 *   网络的节点度分布率
		 */
		map = degree.netDegreeDistributionRatio(net1);
		fileTool.write(map, this.desPath+"1-netDegreeRate.txt", false);
		
		map = degree.netDegreeDistributionRatio(net2);
		fileTool.write(map, this.desPath+"2-netDegreeRate.txt", false);
		D.s(" 网络的节点度分布率");
	}
	public void edgeComputing(Network net1, Network net2){
				
		FileTool fileTool = new FileTool();
		
		/**
		 * 新生内部边
		 */
		MultiNet multiNet = new MultiNet();
		Network net3  = null;
		net3 = multiNet.birthByInEdge(net1, net2);//注意net1, net2参数的顺序
		fileTool.write(net3.traverseEdge(), this.desPath+"birthByInEdge.txt", false);
		D.s("新生内部边");
		
		
		/**
		 * 新生边界边
		 */
		net3 = multiNet.birthByBoundaryEdge(net1, net2);
		fileTool.write(net3.traverseEdge(), this.desPath+"birthByBoundaryEdge.txt", false);
		D.s("新生边界边");
		
		/**
		 * 新生外部边
		 */
		net3 = multiNet.birthByOutEdge(net1, net2);
		fileTool.write(net3.traverseEdge(), this.desPath+"birthByOutEdge.txt", false);
		D.s("新生外部边");
		
		/**
		 * 死亡内部边
		 */
		net3 = multiNet.deathByInEdge(net1, net2);
		fileTool.write(net3.traverseEdge(), this.desPath+"deathByInEdge.txt", false);
		D.s("死亡内部边");
		
		/**
		 * 死亡边界边
		 */
		net3 = multiNet.deathByBoundaryEdge(net1, net2);
		fileTool.write(net3.traverseEdge(),this.desPath+"deathByBoundaryEdge.txt", false);
		D.s("死亡边界边");
	}
	
	/**
	 * 新生的外部边 边界边
	 */
	public void birthEdge(Network net1, Network net2){
		Set<Number> birthNodesSet = new HashSet<Number>();
		MultiNet multiNet = new MultiNet(net1, net2);
		birthNodesSet = multiNet.birthNode();
		
		Network outEdgeNet = multiNet.birthByOutEdge();
		Network boundaryEdgeNet = multiNet.birthByBoundaryEdge();
		
		StringBuffer buffer = new StringBuffer();
		Number nodeId = null;
		for (Iterator<Number> iterator = birthNodesSet.iterator(); iterator.hasNext();) {
			nodeId = (Number) iterator.next();
			buffer.append(nodeId);
			if(outEdgeNet.isHasNode(nodeId)){
				buffer.append("\t").append(outEdgeNet.getAdjNodeId(nodeId).size());
			}else {
				buffer.append("\t").append(0);

			}
			if(boundaryEdgeNet.isHasNode(nodeId)){
				buffer.append("\t").append(boundaryEdgeNet.getAdjNodeId(nodeId).size());
			}else {
				buffer.append("\t").append(0);
			}
			buffer.append("\r\n");
		}
		new FileTool().write(buffer.toString(), this.desPath+"birthEdge.txt", false);
		
	}
	
	public static void main(String[] args){
		
		String srcDir = "../data/all";
		String dstDir = "../data/AS-2009-2012/";
		NetEvolution ne = null;
		
		//File[] fileArr = new File(srcDir).listFiles();
		Network preNet = new Network(srcDir+"1.txt", NetType.UNDIRECTED, NumberType.INTEGER);
		Network postNet = null;
		for(int i = 2; i <= 48; i++ ){
			ne = new NetEvolution(srcDir,dstDir+(i-1)+"-"+i+"/");
			postNet = new Network(srcDir+i+".txt", NetType.UNDIRECTED, NumberType.INTEGER);
			ne.nodeComputing(preNet, postNet);
			ne.edgeComputing(preNet, postNet);
			ne.birthEdge(preNet, postNet);
			
			preNet = postNet;
		}
		
		
	}
}
