package me.mervin.project.usr.mervin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import me.mervin.core.Network;
import me.mervin.core.Global.NetType;
import me.mervin.core.Global.NumberType;
import me.mervin.module.feature.ClusterCofficient;
import me.mervin.util.D;
import me.mervin.util.FileTool;
import me.mervin.util.Pair;
import me.mervin.util.PairList;
import me.mervin.util.PairMap;

/*
 * 
 *   ASConnectionEvolution.java
 * 
 *    AS级网络拓扑的连接（边）的演化
 *  @author Mervin.Wong  DateTime 2013-6-24 上午8:26:55    
 *  @version 0.4.0
 */
public class ASConnectionEvolution {

	/*
	 * 数据目录
	 */
	private String srcDir = "/home/mervin/note/project/data/AS-2009-2012/";
	/*
	 * 保存目录
	 */
	private String dstDir = "/home/mervin/note/project/data/AS-2009-2012/Connection/";
	/*
	 * 写文件
	 */
	private FileTool ft = new FileTool();
	
	/*
	 * 节点的类型
	 */
	private NumberType numberType = NumberType.INTEGER;
	/*
	 *  
	 *  @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ASConnectionEvolution ace = new ASConnectionEvolution();
		//内部连接的度，核, 聚类系数变化
		//ace.internalConnectionDegreeCoreCC();
		//边界连接的度，核, 聚类系数变化
		//ace.borderConnectionDegreeCoreCC();
		//外部连接的度，核, 聚类系数变化
		//ace.outConnectionDegreeCoreCC();
		//在每个网络中的每个节点的聚类系数
		//ace.clusterCofficient();
		//内部连接的抖动
		//ace.connectionJitter("birthByInEdge");
		//边界连接的抖动
		//ace.connectionJitter("birthByBoundaryEdge");
		//外部连接的抖动
		//ace.connectionJitter("birthByOutEdge");
		//内部消亡连接
		//ace.deathLink("deathByInEdge", true);
		//ace.deathLink("deathByInEdge", false);
		//边界消亡连接
		//ace.deathLink("deathByBoundaryEdge", true);
		//ace.deathLink("deathByBoundaryEdge", false);
		//内部消失边的抖动
		//ace.connectionJitter("deathByInEdge", "before");
		//ace.connectionJitter("deathByInEdge", "after");
		//边界消失边的抖动
		ace.connectionJitter("deathByBoundaryEdge", "before");
		ace.connectionJitter("deathByBoundaryEdge", "after");
	}

	/*
	 * 网络中各个点的聚类系数
	 */
	public void clusterCofficient(){
		String srcDir = "../data/AS-2009-2012/all/";
		String srcFile = null;
		Network net = null;
		ClusterCofficient cc = new ClusterCofficient();
		for(int i = 1; i <= 48; i++){
			srcFile = srcDir+i+".txt";
			net = new Network(srcFile, NetType.UNDIRECTED, this.numberType);
			this.ft.write(cc.nodeClusterCofficient(net, net.getAllNodeId()), "../data/AS-2009-2012/CC/"+i+"-cc.txt");
		}
	}
	/*
	 ********************************************************************************
	 *	新生连接：内部连接，边界连接，外部连接
	 *	
	 */
	/*
	 * 内部连接的度，核变化 聚类系数
	 */
	public void internalConnectionDegreeCoreCC(){
		//PairList<Number, Number> pl = null;//连接
		/*
		 * 基本参数
		 */
		Set<Pair<Number>> sp = null;//连接的集合
		Map<Number, Number> degree = null;//度
		Map<Number, Number> core = null;//核
		Map<Number, Number> cc = null;//聚类系数
		
		
		String srcFile = null;//源文件
		
		int count1 = 0;
		int sumDegree1 = 0;
		int sumCore1 = 0;
		int sumCC1 = 0;
		double avgDegree1 = 0;
		double avgCore1 = 0;
		double avgCC1 = 0;
		
		int count2 = 0;
		int sumDegree2 = 0;
		int sumCore2 = 0;
		int sumCC2 = 0;
		double avgDegree2 = 0;
		double avgCore2 = 0;
		double avgCC2 = 0;
		
		StringBuffer sb1 = null;//度
		StringBuffer sb2 = null;//平均度
		StringBuffer sb3 = null;//核
		StringBuffer sb4 = null;//平均核
		StringBuffer sb5 = null;//平均度
		StringBuffer sb6 = null;//聚类系数
		StringBuffer sb7 = null;//局部聚类系数
		StringBuffer sb8 = null;//连接第一次出现
		
		
		
		Number preNodeId = null;
		Number postNodeId = null;
		Number tempDegree1 = null;
		Number tempDegree2 = null;
		Number tempCore1 = null;
		Number tempCore2 = null;
		Number tempCC1 = null;
		Number tempCC2 = null;
		
		
		
		PairMap<Number, ArrayList<Number>> pmDegree = null;
		PairMap<Number, ArrayList<Number>> pmCore = null;
		PairMap<Number, ArrayList<Number>> pmCC = null;
		Pair<Number> p1 = null;
		ArrayList<Number> al = null;
		
		for(int i = 1; i <= 47; i++){
			String dir = i+"-"+(i+1);
			srcFile = this.srcDir+"Evolution/"+dir+"/birthByInEdge.txt";//新生内部边
			sp = this.ft.read2SetPair(srcFile, this.numberType);//连接的集合
			
			pmDegree = new PairMap<Number, ArrayList<Number>>();
			pmCore = new PairMap<Number, ArrayList<Number>>();
			pmCC = new PairMap<Number, ArrayList<Number>>();
			
			sb8 = new StringBuffer();
			sb8.append("#preNodeId").append("\t").append("postNodeId").append("\t");
			sb8.append("degree").append("\t").append("core").append("\t").append("cc").append("\r\n");
			D.p("第"+dir+"分析");
			for(int j = i+1; j <= 48; j++){
				D.p("第"+dir+"分析, "+j+"网络");
				//度
				srcFile = this.srcDir+"all-node-degree/"+j+"-node-degree.txt";
				degree = this.ft.read2Map(srcFile);	
				//核
				srcFile = this.srcDir+"core/core/"+j+"-netCore.txt";
				core = this.ft.read2Map(srcFile);
				//聚类系数
				srcFile = this.srcDir+"CC/"+j+"-cc.txt";
				cc = this.ft.read2Map(srcFile, NumberType.INTEGER, NumberType.DOUBLE);
				//D.p(cc);
				//遍历连接
				//在某次采样网络中该节点不存在，则度 核 聚类系数为-1
				for(Iterator<Pair<Number>> it = sp.iterator(); it.hasNext();){
					p1 = it.next();
					preNodeId = p1.getL();
					postNodeId = p1.getR();
					
					/*
					 * 连接出现时的，节点度，核，聚类系数
					 */
					if(j == i+1){
						sb8.append(preNodeId).append("\t").append(postNodeId).append("\t");
						sb8.append(degree.get(preNodeId)).append("\t").append(degree.get(postNodeId)).append("\t");
						sb8.append(core.get(preNodeId)).append("\t").append(core.get(postNodeId)).append("\t");
						sb8.append(cc.get(preNodeId)).append("\t").append(cc.get(postNodeId)).append("\t");
						sb8.append("\r\n");
					}
					//D.p(pmCC);
					//preNodeId 节点存在
					if(degree.containsKey(preNodeId)){
						if(pmDegree.containsKey(p1)){
							//不是第一次添加
							pmDegree.get(p1).add(degree.get(preNodeId));
							pmCore.get(p1).add(core.get(preNodeId));
							//D.p(cc.get(preNodeId));
							pmCC.get(p1).add(cc.get(preNodeId));
						}else{
							//第一次添加
							//度
							al = new ArrayList<Number>();
							al.add(degree.get(preNodeId));
							pmDegree.put(p1, al);
							//核
							al = new ArrayList<Number>();
							al.add(core.get(preNodeId));
							pmCore.put(p1, al);
							//聚类系数
							al = new ArrayList<Number>();
							al.add(cc.get(preNodeId));
							pmCC.put(p1, al);
						}
					}else{
						//节点不存在
						if(pmDegree.containsKey(p1)){
							//不是第一次添加
							pmDegree.get(p1).add(-1);
							pmCore.get(p1).add(-1);
							pmCC.get(p1).add(-1);
						}else{
							//第一次添加
							al = new ArrayList<Number>();
							al.add(-1);
							pmDegree.put(p1, al);
							
							//D.p("##4");
							al = new ArrayList<Number>();
							al.add(-1);
							//al.add(-1);
							pmCore.put(p1, al);
							
							al = new ArrayList<Number>();
							al.add(-1);
							//al.add(-1);
							pmCC.put(p1, al);
						}
					}
					
					//postNodeId 节点存在
					if(degree.containsKey(postNodeId)){
						pmDegree.get(p1).add(degree.get(postNodeId));
						pmCore.get(p1).add(core.get(postNodeId));
						pmCC.get(p1).add(cc.get(postNodeId));
					}else{
						//节点不存在
						pmDegree.get(p1).add(-1);
						pmCore.get(p1).add(-1);
						pmCC.get(p1).add(-1);
					}			
				}
				
			}//for 1~i
			
			
			sb1 = new StringBuffer();
			sb2 = new StringBuffer();
			sb3 = new StringBuffer();
			sb4 = new StringBuffer();
			sb5 = new StringBuffer();
			sb6 = new StringBuffer();
			sb7 = new StringBuffer();
			//写文件
			sb1.append("#preNodeId").append("\t").append("postNodeId").append("\t").append("avgDegree").append("\t").append("Degree").append("\r\n");
			sb3.append("#preNodeId").append("\t").append("postNodeId").append("\t").append("avgDegree").append("\t").append("Degree").append("\r\n");
			sb5.append("#preNodeId").append("\t").append("postNodeId").append("\t").append("avgDegree").append("\t").append("avgCore").append("\t").append("avgCC").append("\r\n");
			sb6.append("#preNodeId").append("\t").append("postNodeId").append("\t").append("avgDegree").append("\t").append("Degree").append("\r\n");
			for(Iterator<Pair<Number>> it = sp.iterator(); it.hasNext();){
				p1 = it.next();
				al = pmDegree.get(p1);
//				D.p("p1"+p1.toString());
				sb1.append(p1.getL()).append("\t").append(p1.getR()).append("\t");//度
				sb3.append(p1.getL()).append("\t").append(p1.getR()).append("\t");//核
				sb5.append(p1.getL()).append("\t").append(p1.getR()).append("\t");//平均度 平均核 平均聚类系数
				sb6.append(p1.getL()).append("\t").append(p1.getR()).append("\t");//聚类系数
				
//				D.p("size:"+al.size()+"##"+al.toString());
				count1 = 0;
				count2 = 0;
				sumDegree1 = 0;
				avgDegree1 = 0;
				sumDegree2 = 0;
				avgDegree2 = 0;
				
				//遍历度
				for(Iterator<Number> it2 = al.iterator(); it2.hasNext();){
//					D.p("k:"+k++);
					tempDegree1 = it2.next();
//					D.p(tempDegree1+"####");
					sb2.append(tempDegree1).append("\t");
					if(tempDegree1.intValue() != -1){
						sumDegree1 += tempDegree1.intValue();
						count1++;
					}
					tempDegree2 = it2.next();
//					D.p(tempDegree2+"####@@@@");
					sb2.append(tempDegree2).append("\t");
					if(tempDegree2.intValue() != -1 ){
						sumDegree2 += tempDegree2.intValue();
						count2++;
					}
				}
				avgDegree1 = (double)sumDegree1/count1;
				avgDegree2 = (double)sumDegree2/count2;
				sb1.append(avgDegree1).append("\t").append(avgDegree2).append("\t");
				sb1.append(sb2);
				sb1.append("\r\n");
				sb2.delete(0, sb2.length());
				
				sb5.append(avgDegree1).append("\t").append(avgDegree2).append("\t");
				
				al = pmCore.get(p1);
//				D.p("core size:"+al.size());
				count1 = 0;
				count2 = 0;
				sumCore1 = 0;
				avgCore1 = 0;
				sumCore2 = 0;
				avgCore2 = 0;
				//遍历核
				for(Iterator<Number> it3 = al.iterator(); it3.hasNext();){
					tempCore1 = it3.next();
					sb4.append(tempCore1).append("\t");
					if(tempCore1.intValue() != -1){
						sumCore1 += tempCore1.intValue();
						count1++;
					}
					tempCore2 = it3.next();
					sb4.append(tempCore2).append("\t");
					if(tempCore2.intValue() != -1){
						sumCore2 += tempCore2.intValue();
						count2++;
					}
				}
				avgCore1 = (double)sumCore1/count1;
				avgCore2 = (double)sumCore2/count2;
				sb3.append(avgCore1).append("\t").append(avgCore2).append("\t");
				sb3.append(sb4);
				sb3.append("\r\n");
				sb4.delete(0, sb4.length());
				
				sb5.append(avgCore1).append("\t").append(avgCore2).append("\t");
				
				count1 = 0;
				count2 = 0;
				sumCC1 = 0;
				avgCC1 = 0;
				sumCC2 = 0;
				avgCC2 = 0;
				al = pmCC.get(p1);
				//D.p(al);
				//遍历聚类系数
				for(Iterator<Number> it4 = al.iterator(); it4.hasNext();){
					tempCC1 = it4.next();
					sb7.append(tempCC1).append("\t");
					if(tempCC1.doubleValue() != -1){
						sumCC1 += tempCC1.intValue();
						count1++;
					}
					tempCC2 = it4.next();
					sb7.append(tempCC2).append("\t");
					if(tempCC2.doubleValue() != -1){
						sumCC2 += tempCC2.intValue();
						count2++;
					}
				}
				avgCC1 = (double)sumCC1/count1;
				avgCC2 = (double)sumCC2/count2;
				sb6.append(avgCC1).append("\t").append(avgCC2).append("\t");
				sb6.append(sb7);
				sb6.append("\r\n");
				sb7.delete(0, sb7.length());
				
				sb5.append(avgCC1).append("\t").append(avgCC2).append("\r\n");
			}//遍历边
			//写入度
			this.ft.write(sb1, this.dstDir+"birth/internalConnection/"+dir+"-birth-inter-conn-degree.txt");
			//写入核
			this.ft.write(sb3, this.dstDir+"birth/internalConnection/"+dir+"-birth-inter-conn-core.txt");
			//写入聚类系数
			this.ft.write(sb6, this.dstDir+"birth/internalConnection/"+dir+"-birth-inter-conn-cc.txt");
			//写入平均度，平均核
			this.ft.write(sb5, this.dstDir+"birth/internalConnection/"+dir+"-birth-inter-conn-degree-core-cc.txt");
			this.ft.write(sb8, this.dstDir+"birth/internalConnection/"+dir+"-birth-inter-conn-degree-core-cc-1.txt");
		}
		
	}
	/*
	 * 边界连接的度，核变化 聚类系数
	 */
	public void borderConnectionDegreeCoreCC(){
		//PairList<Number, Number> pl = null;//连接
		/*
		 * 基本参数
		 */
		Set<Pair<Number>> sp = null;//连接的集合
		Map<Number, Number> degree = null;//度
		Map<Number, Number> core = null;//核
		Map<Number, Number> cc = null;//聚类系数
		
		
		String srcFile = null;//源文件
		
		int count1 = 0;
		int sumDegree1 = 0;
		int sumCore1 = 0;
		int sumCC1 = 0;
		double avgDegree1 = 0;
		double avgCore1 = 0;
		double avgCC1 = 0;
		
		int count2 = 0;
		int sumDegree2 = 0;
		int sumCore2 = 0;
		int sumCC2 = 0;
		double avgDegree2 = 0;
		double avgCore2 = 0;
		double avgCC2 = 0;
		
		StringBuffer sb1 = null;//度
		StringBuffer sb2 = null;//平均度
		StringBuffer sb3 = null;//核
		StringBuffer sb4 = null;//平均核
		StringBuffer sb5 = null;//平均度
		StringBuffer sb6 = null;//聚类系数
		StringBuffer sb7 = null;//局部聚类系数
		StringBuffer sb8 = null;//连接第一次出现
		
		
		
		Number preNodeId = null;
		Number postNodeId = null;
		Number tempDegree1 = null;
		Number tempDegree2 = null;
		Number tempCore1 = null;
		Number tempCore2 = null;
		Number tempCC1 = null;
		Number tempCC2 = null;
		
		
		
		PairMap<Number, ArrayList<Number>> pmDegree = null;
		PairMap<Number, ArrayList<Number>> pmCore = null;
		PairMap<Number, ArrayList<Number>> pmCC = null;
		Pair<Number> p1 = null;
		ArrayList<Number> al = null;
		
		for(int i = 1; i <= 47; i++){
			String dir = i+"-"+(i+1);
			srcFile = this.srcDir+"Evolution/"+dir+"/birthByBoundaryEdge.txt";//新生内部边
			sp = this.ft.read2SetPair(srcFile, this.numberType);//连接的集合
			
			pmDegree = new PairMap<Number, ArrayList<Number>>();
			pmCore = new PairMap<Number, ArrayList<Number>>();
			pmCC = new PairMap<Number, ArrayList<Number>>();
			
			sb8 = new StringBuffer();
			sb8.append("#preNodeId").append("\t").append("postNodeId").append("\t");
			sb8.append("degree").append("\t").append("core").append("\t").append("cc").append("\r\n");
			D.p("第"+dir+"分析");
			for(int j = i+1; j <= 48; j++){
				D.p("第"+dir+"分析, "+j+"网络");
				//度
				srcFile = this.srcDir+"all-node-degree/"+j+"-node-degree.txt";
				degree = this.ft.read2Map(srcFile);	
				//核
				srcFile = this.srcDir+"core/core/"+j+"-netCore.txt";
				core = this.ft.read2Map(srcFile);
				//聚类系数
				srcFile = this.srcDir+"CC/"+j+"-cc.txt";
				cc = this.ft.read2Map(srcFile, NumberType.INTEGER, NumberType.DOUBLE);
				//D.p(cc);
				//遍历连接
				//在某次采样网络中该节点不存在，则度 核 聚类系数为-1
				for(Iterator<Pair<Number>> it = sp.iterator(); it.hasNext();){
					p1 = it.next();
					preNodeId = p1.getL();
					postNodeId = p1.getR();
					
					/*
					 * 连接出现时的，节点度，核，聚类系数
					 */
					if(j == i+1){
						sb8.append(preNodeId).append("\t").append(postNodeId).append("\t");
						sb8.append(degree.get(preNodeId)).append("\t").append(degree.get(postNodeId)).append("\t");
						sb8.append(core.get(preNodeId)).append("\t").append(core.get(postNodeId)).append("\t");
						sb8.append(cc.get(preNodeId)).append("\t").append(cc.get(postNodeId)).append("\t");
						sb8.append("\r\n");
					}
					//D.p(pmCC);
					//preNodeId 节点存在
					if(degree.containsKey(preNodeId)){
						if(pmDegree.containsKey(p1)){
							//不是第一次添加
							pmDegree.get(p1).add(degree.get(preNodeId));
							pmCore.get(p1).add(core.get(preNodeId));
							//D.p(cc.get(preNodeId));
							pmCC.get(p1).add(cc.get(preNodeId));
						}else{
							//第一次添加
							//度
							al = new ArrayList<Number>();
							al.add(degree.get(preNodeId));
							pmDegree.put(p1, al);
							//核
							al = new ArrayList<Number>();
							al.add(core.get(preNodeId));
							pmCore.put(p1, al);
							//聚类系数
							al = new ArrayList<Number>();
							al.add(cc.get(preNodeId));
							pmCC.put(p1, al);
						}
					}else{
						//节点不存在
						if(pmDegree.containsKey(p1)){
							//不是第一次添加
							pmDegree.get(p1).add(-1);
							pmCore.get(p1).add(-1);
							pmCC.get(p1).add(-1);
						}else{
							//第一次添加
							al = new ArrayList<Number>();
							al.add(-1);
							pmDegree.put(p1, al);
							
							//D.p("##4");
							al = new ArrayList<Number>();
							al.add(-1);
							//al.add(-1);
							pmCore.put(p1, al);
							
							al = new ArrayList<Number>();
							al.add(-1);
							//al.add(-1);
							pmCC.put(p1, al);
						}
					}
					
					//postNodeId 节点存在
					if(degree.containsKey(postNodeId)){
						pmDegree.get(p1).add(degree.get(postNodeId));
						pmCore.get(p1).add(core.get(postNodeId));
						pmCC.get(p1).add(cc.get(postNodeId));
					}else{
						//节点不存在
						pmDegree.get(p1).add(-1);
						pmCore.get(p1).add(-1);
						pmCC.get(p1).add(-1);
					}			
				}
				
			}//for 1~i
			
			
			sb1 = new StringBuffer();
			sb2 = new StringBuffer();
			sb3 = new StringBuffer();
			sb4 = new StringBuffer();
			sb5 = new StringBuffer();
			sb6 = new StringBuffer();
			sb7 = new StringBuffer();
			//写文件
			sb1.append("#preNodeId").append("\t").append("postNodeId").append("\t").append("avgDegree").append("\t").append("Degree").append("\r\n");
			sb3.append("#preNodeId").append("\t").append("postNodeId").append("\t").append("avgDegree").append("\t").append("Degree").append("\r\n");
			sb5.append("#preNodeId").append("\t").append("postNodeId").append("\t").append("avgDegree").append("\t").append("avgCore").append("\t").append("avgCC").append("\r\n");
			sb6.append("#preNodeId").append("\t").append("postNodeId").append("\t").append("avgDegree").append("\t").append("Degree").append("\r\n");
			for(Iterator<Pair<Number>> it = sp.iterator(); it.hasNext();){
				p1 = it.next();
				al = pmDegree.get(p1);
//				D.p("p1"+p1.toString());
				sb1.append(p1.getL()).append("\t").append(p1.getR()).append("\t");//度
				sb3.append(p1.getL()).append("\t").append(p1.getR()).append("\t");//核
				sb5.append(p1.getL()).append("\t").append(p1.getR()).append("\t");//平均度 平均核 平均聚类系数
				sb6.append(p1.getL()).append("\t").append(p1.getR()).append("\t");//聚类系数
				
//				D.p("size:"+al.size()+"##"+al.toString());
				count1 = 0;
				count2 = 0;
				sumDegree1 = 0;
				avgDegree1 = 0;
				sumDegree2 = 0;
				avgDegree2 = 0;
				
				//遍历度
				for(Iterator<Number> it2 = al.iterator(); it2.hasNext();){
//					D.p("k:"+k++);
					tempDegree1 = it2.next();
//					D.p(tempDegree1+"####");
					sb2.append(tempDegree1).append("\t");
					if(tempDegree1.intValue() != -1){
						sumDegree1 += tempDegree1.intValue();
						count1++;
					}
					tempDegree2 = it2.next();
//					D.p(tempDegree2+"####@@@@");
					sb2.append(tempDegree2).append("\t");
					if(tempDegree2.intValue() != -1 ){
						sumDegree2 += tempDegree2.intValue();
						count2++;
					}
				}
				avgDegree1 = (double)sumDegree1/count1;
				avgDegree2 = (double)sumDegree2/count2;
				sb1.append(avgDegree1).append("\t").append(avgDegree2).append("\t");
				sb1.append(sb2);
				sb1.append("\r\n");
				sb2.delete(0, sb2.length());
				
				sb5.append(avgDegree1).append("\t").append(avgDegree2).append("\t");
				
				al = pmCore.get(p1);
//				D.p("core size:"+al.size());
				count1 = 0;
				count2 = 0;
				sumCore1 = 0;
				avgCore1 = 0;
				sumCore2 = 0;
				avgCore2 = 0;
				//遍历核
				for(Iterator<Number> it3 = al.iterator(); it3.hasNext();){
					tempCore1 = it3.next();
					sb4.append(tempCore1).append("\t");
					if(tempCore1.intValue() != -1){
						sumCore1 += tempCore1.intValue();
						count1++;
					}
					tempCore2 = it3.next();
					sb4.append(tempCore2).append("\t");
					if(tempCore2.intValue() != -1){
						sumCore2 += tempCore2.intValue();
						count2++;
					}
				}
				avgCore1 = (double)sumCore1/count1;
				avgCore2 = (double)sumCore2/count2;
				sb3.append(avgCore1).append("\t").append(avgCore2).append("\t");
				sb3.append(sb4);
				sb3.append("\r\n");
				sb4.delete(0, sb4.length());
				
				sb5.append(avgCore1).append("\t").append(avgCore2).append("\t");
				
				count1 = 0;
				count2 = 0;
				sumCC1 = 0;
				avgCC1 = 0;
				sumCC2 = 0;
				avgCC2 = 0;
				al = pmCC.get(p1);
				//D.p(al);
				//遍历聚类系数
				for(Iterator<Number> it4 = al.iterator(); it4.hasNext();){
					tempCC1 = it4.next();
					sb7.append(tempCC1).append("\t");
					if(tempCC1.doubleValue() != -1){
						sumCC1 += tempCC1.intValue();
						count1++;
					}
					tempCC2 = it4.next();
					sb7.append(tempCC2).append("\t");
					if(tempCC2.doubleValue() != -1){
						sumCC2 += tempCC2.intValue();
						count2++;
					}
				}
				avgCC1 = (double)sumCC1/count1;
				avgCC2 = (double)sumCC2/count2;
				sb6.append(avgCC1).append("\t").append(avgCC2).append("\t");
				sb6.append(sb7);
				sb6.append("\r\n");
				sb7.delete(0, sb7.length());
				
				sb5.append(avgCC1).append("\t").append(avgCC2).append("\r\n");
			}//遍历边
			//写入度
			this.ft.write(sb1, this.dstDir+"birth/borderConnection/"+dir+"-birth-border-conn-degree.txt");
			//写入核
			this.ft.write(sb3, this.dstDir+"birth/borderConnection/"+dir+"-birth-border-conn-core.txt");
			//写入聚类系数
			this.ft.write(sb6, this.dstDir+"birth/borderConnection/"+dir+"-birth-border-conn-cc.txt");
			//写入平均度，平均核
			this.ft.write(sb5, this.dstDir+"birth/borderConnection/"+dir+"-birth-border-conn-degree-core-cc.txt");
			this.ft.write(sb8, this.dstDir+"birth/borderConnection/"+dir+"-birth-border-conn-degree-core-cc-1.txt");
		}
		
	}
	/*
	 * 边界连接的度，核变化 聚类系数
	 */
	public void outConnectionDegreeCoreCC(){
		//PairList<Number, Number> pl = null;//连接
		/*
		 * 基本参数
		 */
		Set<Pair<Number>> sp = null;//连接的集合
		Map<Number, Number> degree = null;//度
		Map<Number, Number> core = null;//核
		Map<Number, Number> cc = null;//聚类系数
		
		
		String srcFile = null;//源文件
		
		int count1 = 0;
		int sumDegree1 = 0;
		int sumCore1 = 0;
		int sumCC1 = 0;
		double avgDegree1 = 0;
		double avgCore1 = 0;
		double avgCC1 = 0;
		
		int count2 = 0;
		int sumDegree2 = 0;
		int sumCore2 = 0;
		int sumCC2 = 0;
		double avgDegree2 = 0;
		double avgCore2 = 0;
		double avgCC2 = 0;
		
		StringBuffer sb1 = null;//度
		StringBuffer sb2 = null;//平均度
		StringBuffer sb3 = null;//核
		StringBuffer sb4 = null;//平均核
		StringBuffer sb5 = null;//平均度
		StringBuffer sb6 = null;//聚类系数
		StringBuffer sb7 = null;//局部聚类系数
		StringBuffer sb8 = null;//连接第一次出现
		
		
		
		Number preNodeId = null;
		Number postNodeId = null;
		Number tempDegree1 = null;
		Number tempDegree2 = null;
		Number tempCore1 = null;
		Number tempCore2 = null;
		Number tempCC1 = null;
		Number tempCC2 = null;
		
		
		
		PairMap<Number, ArrayList<Number>> pmDegree = null;
		PairMap<Number, ArrayList<Number>> pmCore = null;
		PairMap<Number, ArrayList<Number>> pmCC = null;
		Pair<Number> p1 = null;
		ArrayList<Number> al = null;
		
		for(int i = 1; i <= 47; i++){
			String dir = i+"-"+(i+1);
			srcFile = this.srcDir+"Evolution/"+dir+"/birthByOutEdge.txt";//新生内部边
			sp = this.ft.read2SetPair(srcFile, this.numberType);//连接的集合
			
			pmDegree = new PairMap<Number, ArrayList<Number>>();
			pmCore = new PairMap<Number, ArrayList<Number>>();
			pmCC = new PairMap<Number, ArrayList<Number>>();
			
			sb8 = new StringBuffer();
			sb8.append("#preNodeId").append("\t").append("postNodeId").append("\t");
			sb8.append("degree").append("\t").append("core").append("\t").append("cc").append("\r\n");
			D.p("第"+dir+"分析");
			for(int j = i+1; j <= 48; j++){
				D.p("第"+dir+"分析, "+j+"网络");
				//度
				srcFile = this.srcDir+"all-node-degree/"+j+"-node-degree.txt";
				degree = this.ft.read2Map(srcFile);	
				//核
				srcFile = this.srcDir+"core/core/"+j+"-netCore.txt";
				core = this.ft.read2Map(srcFile);
				//聚类系数
				srcFile = this.srcDir+"CC/"+j+"-cc.txt";
				cc = this.ft.read2Map(srcFile, NumberType.INTEGER, NumberType.DOUBLE);
				//D.p(cc);
				//遍历连接
				//在某次采样网络中该节点不存在，则度 核 聚类系数为-1
				for(Iterator<Pair<Number>> it = sp.iterator(); it.hasNext();){
					p1 = it.next();
					preNodeId = p1.getL();
					postNodeId = p1.getR();
					
					/*
					 * 连接出现时的，节点度，核，聚类系数
					 */
					if(j == i+1){
						sb8.append(preNodeId).append("\t").append(postNodeId).append("\t");
						sb8.append(degree.get(preNodeId)).append("\t").append(degree.get(postNodeId)).append("\t");
						sb8.append(core.get(preNodeId)).append("\t").append(core.get(postNodeId)).append("\t");
						sb8.append(cc.get(preNodeId)).append("\t").append(cc.get(postNodeId)).append("\t");
						sb8.append("\r\n");
					}
					//D.p(pmCC);
					//preNodeId 节点存在
					if(degree.containsKey(preNodeId)){
						if(pmDegree.containsKey(p1)){
							//不是第一次添加
							pmDegree.get(p1).add(degree.get(preNodeId));
							pmCore.get(p1).add(core.get(preNodeId));
							//D.p(cc.get(preNodeId));
							pmCC.get(p1).add(cc.get(preNodeId));
						}else{
							//第一次添加
							//度
							al = new ArrayList<Number>();
							al.add(degree.get(preNodeId));
							pmDegree.put(p1, al);
							//核
							al = new ArrayList<Number>();
							al.add(core.get(preNodeId));
							pmCore.put(p1, al);
							//聚类系数
							al = new ArrayList<Number>();
							al.add(cc.get(preNodeId));
							pmCC.put(p1, al);
						}
					}else{
						//节点不存在
						if(pmDegree.containsKey(p1)){
							//不是第一次添加
							pmDegree.get(p1).add(-1);
							pmCore.get(p1).add(-1);
							pmCC.get(p1).add(-1);
						}else{
							//第一次添加
							al = new ArrayList<Number>();
							al.add(-1);
							pmDegree.put(p1, al);
							
							//D.p("##4");
							al = new ArrayList<Number>();
							al.add(-1);
							//al.add(-1);
							pmCore.put(p1, al);
							
							al = new ArrayList<Number>();
							al.add(-1);
							//al.add(-1);
							pmCC.put(p1, al);
						}
					}
					
					//postNodeId 节点存在
					if(degree.containsKey(postNodeId)){
						pmDegree.get(p1).add(degree.get(postNodeId));
						pmCore.get(p1).add(core.get(postNodeId));
						pmCC.get(p1).add(cc.get(postNodeId));
					}else{
						//节点不存在
						pmDegree.get(p1).add(-1);
						pmCore.get(p1).add(-1);
						pmCC.get(p1).add(-1);
					}			
				}
				
			}//for 1~i
			
			
			sb1 = new StringBuffer();
			sb2 = new StringBuffer();
			sb3 = new StringBuffer();
			sb4 = new StringBuffer();
			sb5 = new StringBuffer();
			sb6 = new StringBuffer();
			sb7 = new StringBuffer();
			//写文件
			sb1.append("#preNodeId").append("\t").append("postNodeId").append("\t").append("avgDegree").append("\t").append("Degree").append("\r\n");
			sb3.append("#preNodeId").append("\t").append("postNodeId").append("\t").append("avgDegree").append("\t").append("Degree").append("\r\n");
			sb5.append("#preNodeId").append("\t").append("postNodeId").append("\t").append("avgDegree").append("\t").append("avgCore").append("\t").append("avgCC").append("\r\n");
			sb6.append("#preNodeId").append("\t").append("postNodeId").append("\t").append("avgDegree").append("\t").append("Degree").append("\r\n");
			for(Iterator<Pair<Number>> it = sp.iterator(); it.hasNext();){
				p1 = it.next();
				al = pmDegree.get(p1);
//				D.p("p1"+p1.toString());
				sb1.append(p1.getL()).append("\t").append(p1.getR()).append("\t");//度
				sb3.append(p1.getL()).append("\t").append(p1.getR()).append("\t");//核
				sb5.append(p1.getL()).append("\t").append(p1.getR()).append("\t");//平均度 平均核 平均聚类系数
				sb6.append(p1.getL()).append("\t").append(p1.getR()).append("\t");//聚类系数
				
//				D.p("size:"+al.size()+"##"+al.toString());
				count1 = 0;
				count2 = 0;
				sumDegree1 = 0;
				avgDegree1 = 0;
				sumDegree2 = 0;
				avgDegree2 = 0;
				
				//遍历度
				for(Iterator<Number> it2 = al.iterator(); it2.hasNext();){
//					D.p("k:"+k++);
					tempDegree1 = it2.next();
//					D.p(tempDegree1+"####");
					sb2.append(tempDegree1).append("\t");
					if(tempDegree1.intValue() != -1){
						sumDegree1 += tempDegree1.intValue();
						count1++;
					}
					tempDegree2 = it2.next();
//					D.p(tempDegree2+"####@@@@");
					sb2.append(tempDegree2).append("\t");
					if(tempDegree2.intValue() != -1 ){
						sumDegree2 += tempDegree2.intValue();
						count2++;
					}
				}
				avgDegree1 = (double)sumDegree1/count1;
				avgDegree2 = (double)sumDegree2/count2;
				sb1.append(avgDegree1).append("\t").append(avgDegree2).append("\t");
				sb1.append(sb2);
				sb1.append("\r\n");
				sb2.delete(0, sb2.length());
				
				sb5.append(avgDegree1).append("\t").append(avgDegree2).append("\t");
				
				al = pmCore.get(p1);
//				D.p("core size:"+al.size());
				count1 = 0;
				count2 = 0;
				sumCore1 = 0;
				avgCore1 = 0;
				sumCore2 = 0;
				avgCore2 = 0;
				//遍历核
				for(Iterator<Number> it3 = al.iterator(); it3.hasNext();){
					tempCore1 = it3.next();
					sb4.append(tempCore1).append("\t");
					if(tempCore1.intValue() != -1){
						sumCore1 += tempCore1.intValue();
						count1++;
					}
					tempCore2 = it3.next();
					sb4.append(tempCore2).append("\t");
					if(tempCore2.intValue() != -1){
						sumCore2 += tempCore2.intValue();
						count2++;
					}
				}
				avgCore1 = (double)sumCore1/count1;
				avgCore2 = (double)sumCore2/count2;
				sb3.append(avgCore1).append("\t").append(avgCore2).append("\t");
				sb3.append(sb4);
				sb3.append("\r\n");
				sb4.delete(0, sb4.length());
				
				sb5.append(avgCore1).append("\t").append(avgCore2).append("\t");
				
				count1 = 0;
				count2 = 0;
				sumCC1 = 0;
				avgCC1 = 0;
				sumCC2 = 0;
				avgCC2 = 0;
				al = pmCC.get(p1);
				//D.p(al);
				//遍历聚类系数
				for(Iterator<Number> it4 = al.iterator(); it4.hasNext();){
					tempCC1 = it4.next();
					sb7.append(tempCC1).append("\t");
					if(tempCC1.doubleValue() != -1){
						sumCC1 += tempCC1.intValue();
						count1++;
					}
					tempCC2 = it4.next();
					sb7.append(tempCC2).append("\t");
					if(tempCC2.doubleValue() != -1){
						sumCC2 += tempCC2.intValue();
						count2++;
					}
				}
				avgCC1 = (double)sumCC1/count1;
				avgCC2 = (double)sumCC2/count2;
				sb6.append(avgCC1).append("\t").append(avgCC2).append("\t");
				sb6.append(sb7);
				sb6.append("\r\n");
				sb7.delete(0, sb7.length());
				
				sb5.append(avgCC1).append("\t").append(avgCC2).append("\r\n");
			}//遍历边
			//写入度
			this.ft.write(sb1, this.dstDir+"birth/outConnection/"+dir+"-birth-out-conn-degree.txt");
			//写入核
			this.ft.write(sb3, this.dstDir+"birth/outConnection/"+dir+"-birth-out-conn-core.txt");
			//写入聚类系数
			this.ft.write(sb6, this.dstDir+"birth/outConnection/"+dir+"-birth-out-conn-cc.txt");
			//写入平均度，平均核
			this.ft.write(sb5, this.dstDir+"birth/outConnection/"+dir+"-birth-out-conn-degree-core-cc.txt");
			this.ft.write(sb8, this.dstDir+"birth/outConnection/"+dir+"-birth-out-conn-degree-core-cc-1.txt");
		}
		
	}

	/*
	 * 连接的抖动
	 */
	public void connectionJitter(String type, String prefix){
		String dir = null;
		Set<Pair<Number>> allEdge = null;
		Set<Pair<Number>> birthEdge = null;
		Map<Pair<Number>, LinkedList<Number>> edgeLive = null;//边在各个网络的存在 存在为1 不存在为0
		Map<Pair<Number>, Integer> timeliness = null;//时效
		Map<Pair<Number>, Integer> jitter = null;//抖动次数
		Map<Pair<Number>, LinkedList<Number>> jitterFreq = null;//抖动频率
		Map<Pair<Number>, Integer> lastJitter = null;//最近一次抖动
		Map<Pair<Number>, Double> continuning = null;//持续时间比
		LinkedList<Number> ll = null;
		String srcFile = null;
		
		StringBuffer sb1 = null,
				sb2 = null,
				sb3 = null,
				sb4 = null,
				sb5 = null;
		//遍历所有的比对
		for(int i = 1; i <= 47; i++){
			dir = i+"-"+(i+1);
			srcFile = this.srcDir+"Evolution/"+dir+"/"+type+".txt";
			//type 边
			birthEdge = this.ft.read2SetPair(srcFile, this.numberType);
			edgeLive = new HashMap<Pair<Number>, LinkedList<Number>>();//
			jitterFreq = new HashMap<Pair<Number>, LinkedList<Number>>();//
			timeliness = new HashMap<Pair<Number>, Integer>();//
			jitter = new HashMap<Pair<Number>, Integer>();//
			lastJitter = new HashMap<Pair<Number>, Integer>();
			continuning = new HashMap<Pair<Number>, Double>();//
			
			//查找i+1后的网络文件
			D.p("第"+dir+"分析");
			int s = 0, e = 0;
			if(prefix == null){
				//新生边
				s = i+1;
				e = 48;
			}else if(prefix.equals("before")){
				//消失边 之前
				s = 1;
				e = i;
			}else{
				//消失边 之后
				s = i+2;
				e = 48;
			}
			for(int j = s; j <= e; j++){
				D.p("第"+dir+"分析, "+j+"网络");
				srcFile = this.srcDir+"all/"+j+".txt";
				allEdge = this.ft.read2SetPair(srcFile, this.numberType);
				
				for(Pair<Number> edge:birthEdge){
					if(allEdge.contains(edge)){
						//存在这条边
						if(edgeLive.containsKey(edge)){
							edgeLive.get(edge).add(1);
						}else{
							ll = new LinkedList<Number>();
							ll.add(1);
							edgeLive.put(edge, ll);
						}
						
						//时效
						if(timeliness.containsKey(edge)){
							timeliness.put(edge, timeliness.get(edge)+1);
						}else{
							timeliness.put(edge, 1);
						}
						
						//抖动
						/*
						 * 连接存在于j网络中
						 */
						//不是第一次出现
						if(lastJitter.containsKey(edge)){
							if(lastJitter.get(edge) != j-1){
								//发生抖动
								if(jitter.containsKey(edge)){
									jitter.put(edge, jitter.get(edge)+1);
								}else{
									jitter.put(edge, 1);
								}
								
								//抖动时间
								if(jitterFreq.containsKey(edge)){
									jitterFreq.get(edge).add(j-lastJitter.get(edge)-1);
								}else{
									ll = new LinkedList<Number>();
									ll.add(j-lastJitter.get(edge)-1);
									jitterFreq.put(edge, ll);
								}
							}
							//记录最新一次出现的j
							lastJitter.put(edge, j);
						}else{
							//第一次出现
							lastJitter.put(edge, j);
						}
					}else{
						//不存在这条边
						if(edgeLive.containsKey(edge)){
							edgeLive.get(edge).add(0);
						}else{
							ll = new LinkedList<Number>();
							ll.add(0);
							edgeLive.put(edge, ll);
						}
					}
				}//birth-edge
			}//follow-up net
			
			for(Pair<Number> edge:birthEdge){
				if(timeliness.get(edge) != null){
					continuning.put(edge, (double) timeliness.get(edge)/(48-i));
				}else{
					continuning.put(edge, (double) 0);
				}
			}
			//写入文件
			sb1 = new StringBuffer();//存在
			sb2 = new StringBuffer();//抖动频率
			sb3 = new StringBuffer();//抖动次数
			sb4 = new StringBuffer();//时效
			sb5 = new StringBuffer();//持续时间比
			Number l, r;
			for(Pair<Number> edge:birthEdge){
				l = edge.getL();
				r = edge.getR();
				sb1.append(l).append("\t").append(r).append("\t");
				sb2.append(l).append("\t").append(r).append("\t");
				sb3.append(l).append("\t").append(r).append("\t");
				sb4.append(l).append("\t").append(r).append("\t");
				sb5.append(l).append("\t").append(r).append("\t");
				
				//边的存在情况
				ll = edgeLive.get(edge);
				for(Iterator<Number> it = ll.iterator(); it.hasNext();){
					sb1.append(it.next()).append("\t");
				}
				sb1.append("\r\n");
				
				//边的抖动频率
				ll = jitterFreq.get(edge);
				if(ll != null){
					for(Iterator<Number> it = ll.iterator(); it.hasNext();){
						sb2.append(it.next()).append("\t");
					}
				}
				sb2.append("\r\n");
				//抖动次数
				if(jitter.get(edge) != null){
					sb3.append(jitter.get(edge));
				}else{
					sb3.append(0);
				}
				sb3.append("\r\n");
				
				//时效
				sb4.append(timeliness.get(edge)).append("\r\n");
				
				//持续时间比
				sb5.append(continuning.get(edge)).append("\r\n");
				
			}//birth-edge
			String str = null;
			if(prefix !=null){
				str = prefix+"/";
			}
			this.ft.write(sb1, this.dstDir+"jitter/"+type+"/"+str+dir+"-living.txt");
			this.ft.write(sb2, this.dstDir+"jitter/"+type+"/"+str+dir+"-jitterFreq.txt");
			this.ft.write(sb3, this.dstDir+"jitter/"+type+"/"+str+dir+"-jitter.txt");
			this.ft.write(sb4, this.dstDir+"jitter/"+type+"/"+str+dir+"-timeliness.txt");
			this.ft.write(sb5, this.dstDir+"jitter/"+type+"/"+str+dir+"-continuning.txt");
			
		}//i-(i+1)
	}//fun
	
	
	
	
	/*
	 * *************************************************************************
	 * 
	 * death link
	 * 
	 * *************************************************************************
	 */
	/*
	 * 消亡连接的度，核变化 聚类系数
	 * type:interLink borderLink
	 * false: before death
	 * true: after death
	 * 
	 */
	public void deathLink(String type, boolean before){
		/*
		 * 基本参数
		 */
		Set<Pair<Number>> sp = null;//连接的集合
		Map<Number, Number> degree = null;//度
		Map<Number, Number> core = null;//核
		Map<Number, Number> cc = null;//聚类系数
		
		
		String srcFile = null;//源文件
		
		int count1 = 0;
		int sumDegree1 = 0;
		int sumCore1 = 0;
		int sumCC1 = 0;
		double avgDegree1 = 0;
		double avgCore1 = 0;
		double avgCC1 = 0;
		
		int count2 = 0;
		int sumDegree2 = 0;
		int sumCore2 = 0;
		int sumCC2 = 0;
		double avgDegree2 = 0;
		double avgCore2 = 0;
		double avgCC2 = 0;
		
		StringBuffer sb1 = new StringBuffer();//度
		StringBuffer sb2 = new StringBuffer();//平均度
		StringBuffer sb3 = new StringBuffer();//核
		StringBuffer sb4 = new StringBuffer();//平均核
		StringBuffer sb5 = new StringBuffer();//平均度
		StringBuffer sb6 = new StringBuffer();//聚类系数
		StringBuffer sb7 = new StringBuffer();//局部聚类系数
		StringBuffer sb8 = new StringBuffer();//连接第一次出现
		
		
		
		Number preNodeId = null;
		Number postNodeId = null;
		Number tempDegree1 = null;
		Number tempDegree2 = null;
		Number tempCore1 = null;
		Number tempCore2 = null;
		Number tempCC1 = null;
		Number tempCC2 = null;
		
		
		
		PairMap<Number, ArrayList<Number>> pmDegree =  new PairMap<Number, ArrayList<Number>>();
		PairMap<Number, ArrayList<Number>> pmCore =  new PairMap<Number, ArrayList<Number>>();
		PairMap<Number, ArrayList<Number>> pmCC =  new PairMap<Number, ArrayList<Number>>();
		Pair<Number> p1 = null;
		ArrayList<Number> al = null;
		
		for(int i = 1 ; i <= 47; i++){
			String dir = i+"-"+(i+1);
			srcFile = this.srcDir+"Evolution/"+dir+"/"+type+".txt";//新生内部边
			sp = this.ft.read2SetPair(srcFile, this.numberType);//连接的集合
			
/*			pmDegree = new PairMap<Number, ArrayList<Number>>();
			pmCore = new PairMap<Number, ArrayList<Number>>();
			pmCC = new PairMap<Number, ArrayList<Number>>();*/
			
			pmDegree.clear();
			pmCore.clear();
			pmCC.clear();
			sb8.delete(0, sb8.length());
			sb8.append("#preNodeId").append("\t").append("postNodeId").append("\t");
			sb8.append("degree").append("\t").append("core").append("\t").append("cc").append("\r\n");
			D.p("第"+dir+"分析");
			// true: 1~i
			// false: i+2~48
			int s = 0, e = 0;
			if(before){
				s = 1;
				e = i;
			}else{
				s = i+2;
				e = 48;
			}
			for(int j = s; j <= e; j++){
				D.p("第"+dir+"分析, "+j+"网络");
				//度
				srcFile = this.srcDir+"all-node-degree/"+j+"-node-degree.txt";
				degree = this.ft.read2Map(srcFile);	
				//核
				srcFile = this.srcDir+"core/core/"+j+"-netCore.txt";
				core = this.ft.read2Map(srcFile);
				//聚类系数
				srcFile = this.srcDir+"CC/"+j+"-cc.txt";
				cc = this.ft.read2Map(srcFile, NumberType.INTEGER, NumberType.DOUBLE);
				//D.p(cc);
				//遍历连接
				//在某次采样网络中该节点不存在，则度 核 聚类系数为-1
				for(Iterator<Pair<Number>> it = sp.iterator(); it.hasNext();){
					p1 = it.next();
					preNodeId = p1.getL();
					postNodeId = p1.getR();
					
					/*
					 * 连接出现时的，节点度，核，聚类系数
					 */
					if(j == i){
						sb8.append(preNodeId).append("\t").append(postNodeId).append("\t");
						sb8.append(degree.get(preNodeId)).append("\t").append(degree.get(postNodeId)).append("\t");
						sb8.append(core.get(preNodeId)).append("\t").append(core.get(postNodeId)).append("\t");
						sb8.append(cc.get(preNodeId)).append("\t").append(cc.get(postNodeId)).append("\t");
						sb8.append("\r\n");
					}
					//D.p(pmCC);
					//preNodeId 节点存在
					if(degree.containsKey(preNodeId)){
						if(pmDegree.containsKey(p1)){
							//不是第一次添加
							pmDegree.get(p1).add(degree.get(preNodeId));
							pmCore.get(p1).add(core.get(preNodeId));
							//D.p(cc.get(preNodeId));
							pmCC.get(p1).add(cc.get(preNodeId));
						}else{
							//第一次添加
							//度
							al = new ArrayList<Number>();
							al.add(degree.get(preNodeId));
							pmDegree.put(p1, al);
							//核
							al = new ArrayList<Number>();
							al.add(core.get(preNodeId));
							pmCore.put(p1, al);
							//聚类系数
							al = new ArrayList<Number>();
							al.add(cc.get(preNodeId));
							pmCC.put(p1, al);
						}
					}else{
						//节点不存在
						if(pmDegree.containsKey(p1)){
							//不是第一次添加
							pmDegree.get(p1).add(-1);
							pmCore.get(p1).add(-1);
							pmCC.get(p1).add(-1);
						}else{
							//第一次添加
							al = new ArrayList<Number>();
							al.add(-1);
							pmDegree.put(p1, al);
							
							//D.p("##4");
							al = new ArrayList<Number>();
							al.add(-1);
							//al.add(-1);
							pmCore.put(p1, al);
							
							al = new ArrayList<Number>();
							al.add(-1);
							//al.add(-1);
							pmCC.put(p1, al);
						}
					}
					
					//postNodeId 节点存在
					if(degree.containsKey(postNodeId)){
						pmDegree.get(p1).add(degree.get(postNodeId));
						pmCore.get(p1).add(core.get(postNodeId));
						pmCC.get(p1).add(cc.get(postNodeId));
					}else{
						//节点不存在
						pmDegree.get(p1).add(-1);
						pmCore.get(p1).add(-1);
						pmCC.get(p1).add(-1);
					}			
				}
				
			}//for 1~i
			
			
/*			sb1 = new StringBuffer();
			sb2 = new StringBuffer();
			sb3 = new StringBuffer();
			sb4 = new StringBuffer();
			sb5 = new StringBuffer();
			sb6 = new StringBuffer();
			sb7 = new StringBuffer();*/
			//写文件
			sb1.append("#preNodeId").append("\t").append("postNodeId").append("\t").append("avgDegree").append("\t").append("Degree").append("\r\n");
			sb3.append("#preNodeId").append("\t").append("postNodeId").append("\t").append("avgDegree").append("\t").append("Degree").append("\r\n");
			sb5.append("#preNodeId").append("\t").append("postNodeId").append("\t").append("avgDegree").append("\t").append("avgCore").append("\t").append("avgCC").append("\r\n");
			sb6.append("#preNodeId").append("\t").append("postNodeId").append("\t").append("avgDegree").append("\t").append("Degree").append("\r\n");
			for(Iterator<Pair<Number>> it = sp.iterator(); it.hasNext();){
				p1 = it.next();
				al = pmDegree.get(p1);
//				D.p("p1"+p1.toString());
				sb1.append(p1.getL()).append("\t").append(p1.getR()).append("\t");//度
				sb3.append(p1.getL()).append("\t").append(p1.getR()).append("\t");//核
				sb5.append(p1.getL()).append("\t").append(p1.getR()).append("\t");//平均度 平均核 平均聚类系数
				sb6.append(p1.getL()).append("\t").append(p1.getR()).append("\t");//聚类系数
				
//				D.p("size:"+al.size()+"##"+al.toString());
				count1 = 0;
				count2 = 0;
				sumDegree1 = 0;
				avgDegree1 = 0;
				sumDegree2 = 0;
				avgDegree2 = 0;
				
				//遍历度
				for(Iterator<Number> it2 = al.iterator(); it2.hasNext();){
//					D.p("k:"+k++);
					tempDegree1 = it2.next();
//					D.p(tempDegree1+"####");
					sb2.append(tempDegree1).append("\t");
					if(tempDegree1.intValue() != -1){
						sumDegree1 += tempDegree1.intValue();
						count1++;
					}
					tempDegree2 = it2.next();
//					D.p(tempDegree2+"####@@@@");
					sb2.append(tempDegree2).append("\t");
					if(tempDegree2.intValue() != -1 ){
						sumDegree2 += tempDegree2.intValue();
						count2++;
					}
				}
				avgDegree1 = (double)sumDegree1/count1;
				avgDegree2 = (double)sumDegree2/count2;
				sb1.append(avgDegree1).append("\t").append(avgDegree2).append("\t");
				sb1.append(sb2);
				sb1.append("\r\n");
				sb2.delete(0, sb2.length());
				
				sb5.append(avgDegree1).append("\t").append(avgDegree2).append("\t");
				
				al = pmCore.get(p1);
//				D.p("core size:"+al.size());
				count1 = 0;
				count2 = 0;
				sumCore1 = 0;
				avgCore1 = 0;
				sumCore2 = 0;
				avgCore2 = 0;
				//遍历核
				for(Iterator<Number> it3 = al.iterator(); it3.hasNext();){
					tempCore1 = it3.next();
					sb4.append(tempCore1).append("\t");
					if(tempCore1.intValue() != -1){
						sumCore1 += tempCore1.intValue();
						count1++;
					}
					tempCore2 = it3.next();
					sb4.append(tempCore2).append("\t");
					if(tempCore2.intValue() != -1){
						sumCore2 += tempCore2.intValue();
						count2++;
					}
				}
				avgCore1 = (double)sumCore1/count1;
				avgCore2 = (double)sumCore2/count2;
				sb3.append(avgCore1).append("\t").append(avgCore2).append("\t");
				sb3.append(sb4);
				sb3.append("\r\n");
				sb4.delete(0, sb4.length());
				
				sb5.append(avgCore1).append("\t").append(avgCore2).append("\t");
				
				count1 = 0;
				count2 = 0;
				sumCC1 = 0;
				avgCC1 = 0;
				sumCC2 = 0;
				avgCC2 = 0;
				al = pmCC.get(p1);
				//D.p(al);
				//遍历聚类系数
				for(Iterator<Number> it4 = al.iterator(); it4.hasNext();){
					tempCC1 = it4.next();
					sb7.append(tempCC1).append("\t");
					if(tempCC1.doubleValue() != -1){
						sumCC1 += tempCC1.intValue();
						count1++;
					}
					tempCC2 = it4.next();
					sb7.append(tempCC2).append("\t");
					if(tempCC2.doubleValue() != -1){
						sumCC2 += tempCC2.intValue();
						count2++;
					}
				}
				avgCC1 = (double)sumCC1/count1;
				avgCC2 = (double)sumCC2/count2;
				sb6.append(avgCC1).append("\t").append(avgCC2).append("\t");
				sb6.append(sb7);
				sb6.append("\r\n");
				sb7.delete(0, sb7.length());
				
				sb5.append(avgCC1).append("\t").append(avgCC2).append("\r\n");
			}//遍历边
			String str = null;
			if(before){
				 str = "before";
			}else{
				 str = "after";
			}
			//写入度
			this.ft.write(sb1, this.dstDir+"death/"+type+"/"+str+"/"+dir+"-degree.txt");
			//写入核
			this.ft.write(sb3, this.dstDir+"death/"+type+"/"+str+"/"+dir+"-core.txt");
			//写入聚类系数
			this.ft.write(sb6, this.dstDir+"death/"+type+"/"+str+"/"+dir+"-cc.txt");
			//写入平均度，平均核
			this.ft.write(sb5, this.dstDir+"death/"+type+"/"+str+"/"+dir+"-degree-core-cc.txt");
			this.ft.write(sb8, this.dstDir+"death/"+type+"/"+str+"/"+dir+"-degree-core-cc-1.txt");
			sb1.delete(0, sb1.length());
			sb2.delete(0, sb2.length());
			sb3.delete(0, sb3.length());
			sb4.delete(0, sb4.length());
			sb5.delete(0, sb5.length());
			sb6.delete(0, sb6.length());
			sb7.delete(0, sb7.length());
			sb8.delete(0, sb8.length());
		}
		
	}
}
