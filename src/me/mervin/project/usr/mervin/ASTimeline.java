package me.mervin.project.usr.mervin;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import me.mervin.core.Network;
import me.mervin.core.Global.ExtractAS;
import me.mervin.core.Global.NetType;
import me.mervin.core.Global.NumberType;
import me.mervin.module.feature.Degree;
import me.mervin.util.D;
import me.mervin.util.extract.*;
import me.mervin.util.FileTool;
import me.mervin.util.MathTool;
import me.mervin.util.StringTool;


/**
 * AS拓扑的时效性研究
 * @author mervin
 *
 */
public class ASTimeline {

	private String desPath = "../data/AS-2009-2012/";
	private NumberType numberType = NumberType.INTEGER;
	private FileTool f = new FileTool();

	
	/*
	 * ****************************************************************
	 * 
	 * public method
	 *
	 *	1. 提取网络
	 *  2，将team-1 team-2 team-3 合并
	 *****************************************************************/
	/*
	 * 网络提取
	 */
	public void extract(){
		File dir = new File("../data/AS-2009-2012");
		File[] team = null;
		File[] year = null;
		File[] month = null;
		//File[] files = null;
		
		ExtractASByLink eas = null;
		
		team = dir.listFiles();
		for (int i = 0; i < team.length; i++) {
			if(team[i].isDirectory()){
				year = team[i].listFiles();
				for (int j = 0; j < year.length; j++) {
					if(year[j].isDirectory()){
						D.p(year[j]);
						month = year[j].listFiles();
						for (int k = 0; k < month.length; k++) {
							//File file = month[k];
							eas = new ExtractASByLink(ExtractAS.ALL, month[k].getAbsoluteFile().toString(), "../data/AS-2009-2012/extract/"+team[i].getName()+"/"+year[j].getName()+"/"+month[k].getName()+"/");
							eas.script();
							D.p(month[k].getAbsoluteFile());
						}//for month
					}//if year[]
				}//for year
			}//if team
		}//for team
	}
	
	/*
	 * 网络月份合并
	 */
	public void combineByDay(){
		File dir = new File("../data/AS-2009-2012/extract/");
		File[] team = null;
		File[] year = null;
		File[] month = null;
		//File[] files = null;
		
		ExtractASByLink eas = null;
		FileTool f = new FileTool();
		
		String dstFile = null;
		
		team = dir.listFiles();
		for (int i = 0; i < team.length; i++) {
			if(team[i].isDirectory()){
				year = team[i].listFiles();
				for (int j = 0; j < year.length; j++) {
					if(year[j].isDirectory()){
						D.p(year[j]);
						month = year[j].listFiles();
						for (int k = 0; k < month.length; k++) {
							dstFile = "../data/AS-2009-2012/combine/"+team[i].getName()+"/"+year[j].getName()+"/"+year[j].getName()+"-"+month[k].getName()+".txt";
							f.combine(month[k].getAbsolutePath().toString(), dstFile, NumberType.INTEGER, 2);
							D.p(month[k].getAbsoluteFile());
						}//for month
					}//if year[]
				}//for year
			}//if team
		}//for team	
	}
	
	/*
	 * 合并一年的
	 * 合并边或者节点
	 */
	public void combineByMonth(){
		String srcFile = this.desPath+"node/";
		File file = new File(srcFile);
		File[] year = file.listFiles();
		for (int i = 0; i < year.length; i++) {
			String dstFile = this.desPath+"/nodeByYear/"+year[i].getName()+".txt";
			this.f.combine(year[i].getAbsolutePath().toString(), dstFile, NumberType.INTEGER, 1);
		}
	}
	/*
	 * 合并所有的数据集
	 */
	public void combineByYear(){
		String srcFile = this.desPath+"nodeByYear/";
		String dstFile = this.desPath+"node-2009-2012.txt";
		this.f.combine(srcFile, dstFile, NumberType.INTEGER, 1);
	}
	
	/*
	 * 提取节点
	 */
	public void extractNetNode(){
		String souPath = this.desPath+"team/";
		File file = new File(souPath);
		File[] year = file.listFiles();
		File[] month = null;
		
		String srcFile = null;
		String dstFile = null;
		
		for (int i = 0; i < year.length; i++) {
			if(year[i].isDirectory()){
				month = year[i].listFiles();
				for (int j = 0; j < month.length; j++) {
					srcFile = month[j].getAbsolutePath().toString();
					dstFile = this.desPath+"node/"+year[i].getName()+"/"+month[j].getName();
					this.f.write(this.f.read2Set(srcFile, NumberType.INTEGER), dstFile, false);
				}
			}
		}
	}
	/**
	 * team 合并
	 */
	public void ombineByTeam(){
		int[] year={2009, 2010, 2011, 2012};
		
		File[] fileArr = new File[3]; 
		FileTool f = new FileTool();
		String dstFile = null;
		for(int i = 1; i <=12 ; i++){
			//month
			for (int j = 0; j < year.length; j++) {
//				D.p("../data/AS-2009-2012/combine/team-1/"+year[j]+"/"+year[j]+"-"+(i<10?"0"+i:i)+".txt");
				fileArr[0] = new File("../data/AS-2009-2012/combine/team-1/"+year[j]+"/"+year[j]+"-"+(i<10?"0"+i:i)+".txt");
				fileArr[1] = new File("../data/AS-2009-2012/combine/team-2/"+year[j]+"/"+year[j]+"-"+(i<10?"0"+i:i)+".txt");
				fileArr[2] = new File("../data/AS-2009-2012/combine/team-3/"+year[j]+"/"+year[j]+"-"+(i<10?"0"+i:i)+".txt");
				dstFile = "../data/AS-2009-2012/team/"+year[j]+"/"+year[j]+"-"+(i<10?"0"+i:i)+".txt";
				f.combine(fileArr, dstFile, NumberType.INTEGER, 2);
			}
		}
	}

	/*
	 * 统计网络中的边和节点数量
	 */
	public void statNet(){
		File dir = new File(this.desPath+"all");
		File[] fileArr = dir.listFiles();
		File file = null;
		Network net = null;
		StringBuffer nodes = new StringBuffer();
		StringBuffer edges = new StringBuffer();
		StringBuffer rate = new StringBuffer();//连接率
		
		//HashSet<Pair<Integer>> edges = null;//边的集合
		//HashSet<Interger> nodes = null;//节点的集合
		
		FileTool f = new FileTool();
		for (int i = 0; i < fileArr.length; i++) {
			file = fileArr[i];
			int m;
			try {
				m = f.read2SetPair(file.getCanonicalPath(), this.numberType).size();
				ArrayList<Integer> cols = new ArrayList<Integer>();
				cols.add(1);
				cols.add(2);
				int n;
				int[] a= {1,2};
				n = f.read2Set(file.getCanonicalPath(), numberType,a ).size();
				String str = file.getName();
				str.replace('-', '/');
				nodes.append(str).append("\t").append(n).append("\r\n");
				edges.append(str).append("\t").append(m).append("\r\n");
				float r = (float)2*m/(n*(n-1));
				rate.append(str).append("\t").append(r).append("\r\n");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
/*			try {
				D.p(file.getCanonicalPath());
				net = new Network(file.getCanonicalPath(), NetType.UNDIRECTED, NumberType.INTEGER);
				String str = file.getName();
				str.replace('-', '/');
				nodes.append(str).append("\t").append(net.nodeNum).append("\r\n");
				edges.append(str).append("\t").append(net.edgeNum).append("\r\n");
				float r = (float)2*net.edgeNum/(net.nodeNum*(net.nodeNum-1));
				rate.append(str).append("\t").append(r).append("\r\n");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
			D.p(i+"############################");
			
		}
		//FileTool f = new FileTool();
		f.write(nodes, this.desPath+"nodes.txt", false);
		f.write(edges, this.desPath+"edges.txt", false);
		f.write(rate, this.desPath+"linkRate.txt", false);
	}
	/*
	 * 网络中叶子节点的数量
	 */
	public void leafNumByNet(){
		String srcDir = this.desPath+"all/";
		String srcFile = null;
		Network net = null;
		Degree d = new Degree();
//		HashMap<Number, Number> map = new HashMap<Number, Number>();
		StringBuffer sb = new StringBuffer();
		for(int i = 1; i <= 48; i++){
			srcFile = srcDir+i+".txt";
			net = new Network(srcFile, NetType.UNDIRECTED, NumberType.LONG);
			sb.append(i).append("\t").append(d.getNodeIdByDegree(net, 1).size()).append("\r\n");
		}
		new FileTool().write(sb, this.desPath+"node-degree/"+"leaf-num.txt", false);
	}
	/*****************************************************************
	 * 
	 *
	 *****************************************************************/	
	
	/*****************************************************************
	 * 二，节点的时效性
	 * 1，所有节点的生存时间
	 * 2，第一个网络的生存时间
	 *****************************************************************/
	public void lifetimeByNode(){
		HashMap<Number, Number> nodeLife = new HashMap<Number, Number>();//节点的寿命
		Set<Number> nodeSet = null;
		
//		nodeSet = this.f.read2Set(this.desPath+"node-2009-2012.txt", this.numberType);
		nodeSet = this.f.read2Set(this.desPath+"nodeByYear/2009.txt", this.numberType);
		for (Iterator<Number> iterator = nodeSet.iterator(); iterator.hasNext();) {
			nodeLife.put(iterator.next(), 0);
		}
		
		String souPath = this.desPath+"all/";
		
		File file = new File(souPath);
		File[] year = file.listFiles();
		File[] month = null;
		
		String srcFile = null;
		String dstFile = null;
		dstFile = this.desPath+"2009-nodeFrequency.txt";
		
		Number nodeId = null;
		int k = 0;
		for (int i = 0; i < year.length; i++) {
			if(year[i].isDirectory()){
				month = year[i].listFiles();
				for (int j = 0; j < month.length; j++) {
					srcFile = month[j].getAbsolutePath().toString();
					nodeSet = this.f.read2Set(srcFile, NumberType.INTEGER);
					for (Iterator<Number> iterator = nodeSet.iterator(); iterator.hasNext();) {
						nodeId = (Number) iterator.next();
						//k = (nodeLife.get(nodeId)==null?0:nodeLife.get(nodeId).intValue())+1;
						if(nodeLife.get(nodeId) != null){
							k = nodeLife.get(nodeId).intValue()+1;
							nodeLife.put(nodeId, k);
						}
					}//for
				}//for
			}//if
		}//for
		this.f.write(MathTool.frequency(nodeLife), this.desPath+"2009-frequency.txt", false);
		this.f.write(nodeLife, dstFile, false);
	}
	/*
	 * 节点出现的时间频率
	 */
	public void lifetimeRateByNode(){
		//String srcFile = this.desPath+"node-lifetime/2009-frequency.txt";
		String srcFile = this.desPath+"node-timeliness-jitter/timeliness-birthNode-1-2.txt";
		
		Map<Number, Number> frequency = null;
		frequency = this.f.read2Map(srcFile, this.numberType);
		//this.f.write(MathTool.ratio(frequency), this.desPath+"node-lifetime/2009-rate.txt", false);
		this.f.write(MathTool.ratio(frequency), this.desPath+"node-timeliness-jitter/timeliness-birthNode-1-2.txt", false);
	}
	
	
	/*
	 * 节点出现的时间累积频率
	 * 1,2009-2012 2009 2009-01
	 */
	public void lifetimeAccumulateRateByNode(){
		String srcFile = this.desPath+"all-rate.txt";
		Map<Number, Number> rate = null;
		rate = this.f.read2Map(srcFile, this.numberType, NumberType.DOUBLE);
		double sum = 0;
		for (int i = 1; i <= 48; i++) {
			sum += rate.get(i).doubleValue();
			rate.put(i, sum);
		}
		this.f.write(rate, this.desPath+"all-accumulate-rate.txt", false);
	}
	
	/*
	 * 节点的持续性
	 * 节点在新生到2012年12月之间持续时间与两者时间间隔的比例
	 */
	public void nodeContinuning(){
		//File[] fileArr =new File(this.desPath+"all/").listFiles();
		FileTool f = new FileTool();
		Set<Number> allNode = f.read2Set(this.desPath+"node-2009-2012.txt", NumberType.INTEGER);
		
		Map<Number, Number> birthTime = new HashMap<Number, Number>();//节点出现时间
		Map<Number, Number> continueTime = new HashMap<Number, Number>();//节点持续时间
		
		Set<Number> node = null;
		Number nodeId = null;
		for(int i = 1; i <= 48; i++){
			int[] cols = {1,2};
			//D.p(this.desPath+"all/"+i+".txt");
			node = f.read2Set(this.desPath+"all/"+i+".txt", NumberType.INTEGER,cols);
			for(Iterator<Number> it = allNode.iterator(); it.hasNext();){
				nodeId = it.next();
				if(node.contains(nodeId)){
					//该网络还有该点
					if(!birthTime.containsKey(nodeId)){
						//第一次出现
						birthTime.put(nodeId, i);
						continueTime.put(nodeId, 1);
					}else{
						continueTime.put(nodeId, continueTime.get(nodeId).intValue()+1);
					}
				}
			}
		}
		
		/*
		 * 计算比例
		 */
		StringBuffer sb = new StringBuffer();
		for(Iterator<Number> it = allNode.iterator(); it.hasNext();){
			nodeId = it.next();
			sb.append(nodeId).append("\t");
			sb.append((double)continueTime.get(nodeId).intValue()/(48-birthTime.get(nodeId).intValue()+1));
			sb.append("\r\n");
		}
		f.write(sb, this.desPath+"node-continuning.txt", false);
	}
	/*
	 * 统计度和时效的关系 
	 * 1,每个节点在每个网络中如果出现，则统计度值，最后求平均值。同时统计出现的次数(时效)
	 * 2，
	 */
	public void degree(){
		HashMap<Number, Number> nodeLife = new HashMap<Number, Number>();//节点的寿命
		HashMap<Number, Number> nodeDegreeSum = new HashMap<Number, Number>();//节点的度的和
		
		Set<Number> nodeSet = null;
		Number temp = null;
		
//		nodeSet = this.f.read2Set(this.desPath+"node-2009-2012.txt", this.numberType);
		nodeSet = this.f.read2Set(this.desPath+"nodeByYear/2009.txt", this.numberType);
		for (Iterator<Number> iterator = nodeSet.iterator(); iterator.hasNext();) {
			temp = iterator.next();
			nodeLife.put(temp, 0);
			nodeDegreeSum.put(temp, 0);
		}
		
		String souPath = this.desPath+"team/";
		
		File file = new File(souPath);
		File[] year = file.listFiles();
		File[] month = null;
		
		String srcFile = null;
		Network net = null;
		Degree d = new Degree(net);
		
		Number nodeId = null;
		int k = 0, l = 0;
		/*
		 * 遍历所有网络，统计节点的度和出现周期
		 */
		for (int i = 0; i < year.length; i++) {
			if(year[i].isDirectory()){
				month = year[i].listFiles();
				for (int j = 0; j < month.length; j++) {
					srcFile = month[j].getAbsolutePath().toString();
					net = new Network(srcFile, NetType.UNDIRECTED, this.numberType);
					nodeSet = net.getAllNodeId();
					for (Iterator<Number> iterator = nodeSet.iterator(); iterator.hasNext();) {
						nodeId = (Number) iterator.next();
						//k = (nodeLife.get(nodeId)==null?0:nodeLife.get(nodeId).intValue())+1;
						if(nodeLife.get(nodeId) != null){
							k = nodeLife.get(nodeId).intValue()+1;
							nodeLife.put(nodeId, k);
						}
						if(nodeDegreeSum.get(nodeId) != null){
							l = nodeDegreeSum.get(nodeId).intValue()+d.nodeDegree(nodeId);
							nodeDegreeSum.put(nodeId, l);
						}
					}//for
				}//for
			}//if
		}//for
		
		/*
		 * 求网络的
		 */
		HashMap<Number, Number> nodeDegreeAvg = new HashMap<Number, Number>();//节点的度的平均值
		for (Iterator<Number> iterator = nodeSet.iterator(); iterator.hasNext();) {
			temp = iterator.next();
			nodeDegreeAvg.put(temp, nodeDegreeSum.get(temp).doubleValue()/nodeLife.get(temp).doubleValue());
		}
		
		String dstFile = null;
		dstFile = this.desPath+"2009-nodeFrequency.txt";
		this.f.write(MathTool.frequency(nodeLife), this.desPath+"2009-frequency.txt", false);
		this.f.write(nodeLife, dstFile, false);		
	}


	/*
	 * 节点的抖动
	 * 节点出现->消失->出现成为一次抖动
	 * 记录：出现时间，消失的时间单位，
	 * 
	 */
	public void nodeShake(){
		HashSet<Integer> allNode = new HashSet<Integer>();//所有的节点
		
		HashMap<Number, Number> shakeNum = new HashMap<Number, Number>();//节点的抖动次数
		HashMap<Number, Number> continueTime = new HashMap<Number, Number>();//节点的持续时间
		HashMap<Number, Number> node = new HashMap<Number, Number>();//节点第一次出现的时间
		HashMap<Integer, ArrayList<Integer>> shakeTime = new HashMap<Integer,  ArrayList<Integer>>();//节点的抖动时间
		
		HashMap<Integer, Integer> lastTime = new HashMap<Integer, Integer>();//节点在最近消失的时间单位
		
		Set<Number> nodes = null;
		//HashSet<Number> nodes2 = new HashSet<Number>();
		
		String srcDir = this.desPath+"all/";
		String srcFile = null;
		//File file = null;
		//BufferedReader reader = null;
		
		Number nodeId1 = null;
		Integer nodeId2 = null;
		
		FileTool f = new FileTool();
		for(int i = 1; i <= 48; i++){
			srcFile = srcDir+i+".txt";
			//file = new File(srcFile);
			int[] col = {1,2};
			nodes = f.read2Set(srcFile, numberType, col);
			D.p(i+"网络###############"+"");
			//遍历nodes中的节点
			for(Iterator<Number> it = nodes.iterator(); it.hasNext();){
				nodeId1 = it.next();
				if(!allNode.contains(nodeId1)){
					//第一次出现
					allNode.add(nodeId1.intValue());
					node.put(nodeId1.intValue(), i);//第一次出现的时间
				}else{
					//不是第一次出现
					if(lastTime.containsKey(nodeId1)){
						//最近消失的节点集中包含该节点，那么说明节点有“消失->出现”
						//发生一次抖动
						if(shakeNum.containsKey(nodeId1)){
							shakeNum.put(nodeId1.intValue(), shakeNum.get(nodeId1).intValue()+1);
						}else{
							shakeNum.put(nodeId1.intValue(), 1);
						}
						//添加节点的抖动时间
						if(shakeTime.containsKey(nodeId1)){
							shakeTime.get(nodeId1).add(i-lastTime.get(nodeId1));
						}else{
							//第一添加抖动时间
							ArrayList<Integer> arr = new ArrayList<Integer>();
							arr.add(i-lastTime.get(nodeId1));
							shakeTime.put(nodeId1.intValue(), arr);
						}
					}
					lastTime.remove(nodeId1);//节点出现
				}
				if(continueTime.containsKey(nodeId1)){
					continueTime.put(nodeId1, continueTime.get(nodeId1).intValue()+1);
				}else{
					continueTime.put(nodeId1, 1);
				}
			}
			
			//遍历allNode中的节点
			for(Iterator<Integer> it =allNode.iterator(); it.hasNext();){
				nodeId2 = it.next();
				if(!nodes.contains(nodeId2) && !lastTime.containsKey(nodeId2)){
					//本网络的节点集中不包含nodeId2,说明nodeId2以前出现过，但是本次消失。
					//lastTime包含nodeId2，说明nodeId2早就消失了，否则是本次消失
					lastTime.put(nodeId2, i);
				}
			}
			//nodes2.clear();
			//nodes2.addAll(nodes);
		}
		
/*		StringBuffer sb2 = new StringBuffer();
		Number nodeId = null;
		for(Iterator<Number> it2=nodes.iterator(); it2.hasNext();){
			nodeId = it2.next();
			sb2.append(b)
		}*/
		f.write(shakeNum, this.desPath+"node-shake/shake-num.txt", false);
		f.write(node, this.desPath+"node-shake/node-time.txt", false);
		StringBuffer sb = new StringBuffer();
		ArrayList<Integer> list = null;
		for(Iterator<Integer> it=allNode.iterator();it.hasNext();){
			nodeId2 = it.next();
			list = shakeTime.get(nodeId2);
			if(list != null){
				for (Iterator<Integer> iterator = list.iterator(); iterator.hasNext();) {
					sb.append(nodeId2).append("\t");
					sb.append(node.get(nodeId2)).append("\t");
					sb.append((double)continueTime.get(nodeId2).intValue()/(49-node.get(nodeId2).intValue())).append("\t");
					sb.append(shakeNum.get(nodeId2)).append("\t");
					sb.append(iterator.next()).append("\r\n");
					//sb.append( (Integer) iterator.next()).append("\t");
				}
			}else{
				//无抖动
				sb.append(nodeId2).append("\t");
				sb.append(node.get(nodeId2)).append("\t");
				sb.append((double)continueTime.get(nodeId2).intValue()/(49-node.get(nodeId2).intValue())).append("\t");
				sb.append(0).append("\t");
				sb.append(0).append("\r\n");
			}
			//sb.append("\r\n");
		}
		f.write(sb, this.desPath+"node-shake/shake-time-continue.txt", false);
	}
	
	/*
	 * 节点在时效内的度值变化
	 */
	public void nodeDegree(){
		FileTool f = new FileTool();
		String root="../data/AS-2009-2012/";
		/*
		 * 获取所有节点集
		 * [ID，NET，Degree]
		 */
		Set<Number> nodes = f.read2Set(root+"node-2009-2012.txt", NumberType.INTEGER);
		Map<Number, Number> nodeDegree = null;
		//HashMap<Number, HashMap<Number, Number>> allNodeDegree = new HashMap<Number, HashMap<Number, Number>>();
		StringBuffer sb = new StringBuffer();
		//StringTool<Number, Number> st = new StringTool<Number, Number>();
		Number nodeId = null;
		/*
		 * 遍历每个网络
		 */
		Degree d = null;
		File srcDir = new File(root+"all/");
		File[] fileArr = srcDir.listFiles();
		File file = null;
		Network net = null;
		for (int i = 0; i < fileArr.length; i++) {
			D.m(i+"网络");
			file = fileArr[i];
			try {
				net = new Network(file.getCanonicalPath(), NetType.UNDIRECTED, NumberType.INTEGER);
				d = new Degree(net);
				nodeDegree = d.nodeDegree(nodes);
				for(Iterator<Number> it = nodes.iterator(); it.hasNext();){
					nodeId = it.next();
					sb.append(i).append("\t").append(nodeId).append("\t").append(nodeDegree.get(nodeId)).append("\r\n");
				}
				//sb.append(st.map2str(allNodeDegree.put(i, d.nodesDegree(nodes))));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		f.write(sb, this.desPath+"all-node-degree.txt", false);
	}
	/*
	 * 节点的平均度
	 */
	public void avgDegree(){
		String file = this.desPath+"node-degree/all-node-degree.txt";
		String line = null;
		String[] lineArr = null;
		
		BufferedReader reader;
		HashMap<Number, Number> map1 = new HashMap<Number, Number>();//nodeId sum
		HashMap<Number, Number> map2 = new HashMap<Number, Number>();//nodeId frequency
		HashMap<Number, Number> degreeMap = new HashMap<Number, Number>();//nodeId degee
		int nodeId = 0;
		int degree = 0;
		try {
			reader = new BufferedReader(new FileReader(file));
			while((line = reader.readLine()) != null){
				D.p(line);
				lineArr = line.split("\t|(\\s+)");
				if(lineArr.length >= 3){
					nodeId = Integer.parseInt(lineArr[1]);
					degree = Integer.parseInt(lineArr[2]);
					if(degree > 0){
						if(map1.containsKey(nodeId)){
							map1.put(nodeId, map1.get(nodeId).intValue()+degree);
							map2.put(nodeId, map2.get(nodeId).intValue()+1);
						}else{
							map1.put(nodeId, degree);
							map2.put(nodeId, 1);					
						}						
					}

				}
			}
			
			for(Iterator<Number> iterator = map1.keySet().iterator();iterator.hasNext();){
				nodeId = iterator.next().intValue();
				degreeMap.put(nodeId, (float)map1.get(nodeId).intValue()/map2.get(nodeId).intValue());
			}
			new FileTool().write(degreeMap, this.desPath+"node-degree/avg-degree.txt", false);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/*
	 * 节点在生存时间内的平均度和平均度相同的节点的时效的平均值
	 */
	public void avgOfdegreeAndLiftime(){
		String srcFile = this.desPath+"node-avgDegree-frequency.txt";
		String line = null;
		String[] lineArr = null;
		
		HashMap<Number, Number> map1 = new HashMap<Number, Number>();
		HashMap<Float, Integer> map2 = new HashMap<Float, Integer>();
		
		float temp, temp1 ;
		int temp2;
		try {
			BufferedReader reader = new BufferedReader(new FileReader(srcFile));
			while((line = reader.readLine()) != null){
				lineArr = line.split("\t|(\\s+)");
				if(lineArr.length >= 3){
					temp2 = Integer.parseInt(lineArr[1]); 
					temp1 = (float) Math.ceil(Float.parseFloat(lineArr[2]));
					if(map1.containsKey(temp1)){
						map1.put(temp1, (map2.get(temp1)*map1.get(temp1).floatValue()+temp2)/(map2.get(temp1)+1));
						map2.put(temp1, map2.get(temp1)+1);
					}else{
						map1.put(temp1, (float) temp2);
						map2.put(temp1, 1);
					}					
				}
			}
			new FileTool().write(map1, this.desPath+"avg-degree-lieftime-1.txt", false);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/*
	 * 网络的平均度
	 */
	public void netAvgDegree(){
		String file = this.desPath+"node-degree/all-node-degree.txt";
		String line = null;
		String[] lineArr = null;
		
		BufferedReader reader;
		HashMap<Number, Number> map1 = new HashMap<Number, Number>();//nodeId sum
//		HashMap<Number, Number> map2 = new HashMap<Number, Number>();//nodeId frequency
//		HashMap<Number, Number> degreeMap = new HashMap<Number, Number>();//nodeId degee
//		int nodeId = 0;
		int degree = 0;
		int netId = 0;
		int flag = 0;
		
		int sum = 0;
		int freq = 0;
		
		float total = 0;
		try {
			reader = new BufferedReader(new FileReader(file));
			while((line = reader.readLine()) != null){
				D.p(line);
				lineArr = line.split("\t|(\\s+)");
				if(lineArr.length >= 3){
					netId = Integer.parseInt(lineArr[0]);
//					nodeId = Integer.parseInt(lineArr[1]);
					degree = Integer.parseInt(lineArr[2]);
					
					if(netId == flag){
						//同一个网络
						if(degree > 0){
							sum += degree;
							freq++;
						}
					}else{
						//不同网络
						total += (float)sum/freq;
						map1.put(flag, (float)sum/freq);
						flag++;
						if(degree > 0){
							sum = degree;
							freq = 1;
						}
					}
					
				}
			}
			map1.put(49, total/48);

			new FileTool().write(map1, this.desPath+"node-degree/net-avg-degree.txt", false);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	/*
	 * 抖动的平均时间
	 */
	public void avgTimeOfShake(){
		String srcFile = this.desPath+"node-shake/shake-time1.txt";
		String line = null;
		String[] lineArr = null;
		
		HashMap<Number, Number> map = new HashMap<Number, Number>();
		int sum = 0;
		
		try {
			BufferedReader reader = new BufferedReader(new FileReader(srcFile));
			while((line = reader.readLine()) != null){
				D.p(line);
				lineArr = line.split("\t|(\\s+)");
				sum = 0;
				for(int i = 1; i < lineArr.length; i++){
					sum += Integer.parseInt(lineArr[i]);
				}
				map.put(Integer.parseInt(lineArr[0]), (float)sum/(lineArr.length-1));
			}
			
			new FileTool().write(map, this.desPath+"node-shake/shake-avgTime.txt", false);
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/*
	 * ****************************************************************
	 *
	 * other method
	 *
	 *****************************************************************/
	/*
	 * 合并文件
	 */
	private void combine(){
		//File[] fileArr = new File(this.desPath+"all-node-degree/");
		File file = null;
		BufferedReader reader = null;
		StringBuffer sb = new StringBuffer();
		String line = null;
		//String[] lineArr = null;
		for(int i = 0; i < 48; i++){
			file = new File(this.desPath+"all-node-degree/"+i+"-node-degree.txt");
			try {
				reader = new BufferedReader(new FileReader(file));

				while((line = reader.readLine()) != null){
					//lineArr = line.split("\t|\\s{1,}");
					//for(int j = 0; j < l)
					//D.p(line);
					//D.p(line.trim());
					sb.append(line.trim()).append("\r\n");
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		new FileTool().write(sb, this.desPath+"all-node-degree/all-node-degree.txt", false);
		
	}
	/*****************************************************************
	 * 
	 *
	*****************************************************************/
	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		String desPath = "../data/AS-2009-2012/";
		NumberType numberType = NumberType.INTEGER;
		// TODO Auto-generated method stub
		ASTimeline ast = new ASTimeline();
		//ast.extract();
		//ast.combine();
		//ast.teamCombine();
		//ast.combineByMonth();
		//ast.combineByYear();
		//ast.extractNetNode();
		//ast.lifetimeByNode();
		//ast.lifetimeRateByNode();
		//ast.lifetimeAccumulateRateByNode();
		//统计网络中的节点和边
		//ast.statNet();
		//节点的抖动
		//ast.nodeShake();
		//节点在不同网络中的度
		//ast.nodeDegree();
		//节点持续性
		//ast.nodeContinuning();
		//文件合并
		//ast.combine();
		//叶子节点的数量
		//ast.leafNumByNet();
		//平均度
		//ast.avgDegree();
		//平均度，对应的时效的平均值
		//ast.avgOfdegreeAndLiftime();
		//网络的平均度
		//ast.netAvgDegree();
		//抖动节点的平均时间
		ast.avgTimeOfShake();
	}
}
