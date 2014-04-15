package me.mervin.project.usr.mervin;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import me.mervin.core.Global.ArithmeticOperators;
import me.mervin.core.Global.NumberType;
import me.mervin.util.D;
import me.mervin.util.FileTool;
import me.mervin.util.MapTool;
import me.mervin.util.MathTool;
import me.mervin.util.PairList;

public class Timeliness {
	private String desPath = "../data/AS-2009-2012/";
	private NumberType numberType = NumberType.INTEGER;
	private FileTool f = new FileTool();

	/**
	 *  
	 *  @param args
	 */
	public static void main(String[] args) {
		NumberType numberType = NumberType.INTEGER;
		Timeliness t = new Timeliness();
//		t.birthNodeTimelinessFreq();
//		提取稳定节点
//		t.extractStableNode((float) 0.9);
		//提取稳定节点的度和核
//		t.extractStableNodeFearture((float) 1);
//		t.extractStableNodeFearture((float) 0.9);
//		t.extractStableNodeFearture((float) 0);
		//节点的持续时间比与节点数量累积比例
		//t.birthNodeContinuingRatioAndNumAccumulateRatio();
		//死亡节点在后续的采样网络中，是否出现，是否出现抖动
		//t.death_jitter_timeliness();
		
		//消亡节点的度  核
		//t.deathFeature();
		//消亡节点的邻接点度 核
		//t.deathAdjFeature();
		//真正消亡的节点
		//t.realDeath();
		//真正消亡节点的度 核
		//t.deathFeature();
		//抖动对应的数量比例
		t.jitter_ratio();
		//时效以及比例
		//t.timeliness_ratio();
	}
	/*
	 * 抖动对应的数量比例
	 */
	public void jitter_ratio(){
		String srcDir = "../data/AS-2009-2012/death_jitter_timeliness/";
		Map<Number, Number> jitter = null;//抖动
		Map<Number, Number> frequency = null;//频数
		PairList<Number, Number> pl = null;//排序
		String srcFile = null;
		FileTool f = new FileTool();
		for(int i = 1; i <= 47; i++){
			srcFile = srcDir+"jitter-deathNode-"+i+"-"+(i+1)+".txt";
			jitter = f.read2Map(srcFile);
			frequency = MathTool.frequency(jitter);
			MapTool mt = new MapTool();
			pl = mt.sort(frequency);
			f.write(pl, this.desPath+"death_jitter_timeliness/jitter/jitter-frequency-"+i+"-"+(i+1)+".txt");
			pl = mt.sort(MathTool.ratio(frequency));
			f.write(pl, this.desPath+"death_jitter_timeliness/jitter/jitter-ratio-"+i+"-"+(i+1)+".txt");
			
			f.write(MathTool.accumulate(pl), this.desPath+"death_jitter_timeliness/jitter/jitter-accumulate-ratio-"+i+"-"+(i+1)+".txt");
		}
	}
	
	/*
	 * 时效对应的数量比例
	 */
	public void timeliness_ratio(){
		String srcDir = "../data/AS-2009-2012/death_jitter_timeliness/";
		Map<Number, Number> timeliness = null;//抖动
		Map<Number, Number> frequency = null;//频数
		PairList<Number, Number> pl = null;//排序
		String srcFile = null;
		FileTool f = new FileTool();
		for(int i = 1; i <= 47; i++){
			srcFile = srcDir+"timeliness-deathNode-"+i+"-"+(i+1)+".txt";
			timeliness = f.read2Map(srcFile);
			frequency = MathTool.frequency(timeliness);
			MapTool mt = new MapTool();
//			pl = mt.sort(frequency);
			f.write(mt.sort(frequency), this.desPath+"death_jitter_timeliness/timeliness/timeliness-frequncy-"+i+"-"+(i+1)+".txt");
			//比例
			pl = mt.sort(MathTool.ratio(frequency));
			f.write(pl, this.desPath+"death_jitter_timeliness/timeliness/timeliness-ratio-"+i+"-"+(i+1)+".txt");
			//累积比例
			f.write(MathTool.accumulate(pl), this.desPath+"death_jitter_timeliness/timeliness/timeliness-accumulate-ratio-"+i+"-"+(i+1)+".txt");
			
		}
	}
	
 	public void timeliness_jitter_continuing(){
		String souPath = "../data/AS-2009-2012/Evolution/";
		String souPath2 = "../data/AS-2009-2012/node-degree-core/";
		String desPath = "../data/AS-2009-2012/node-timeliness-jitter/";
		FileTool f = new FileTool();
		HashMap<Number, Number> birthNodeLife = null;//节点的寿命
		HashMap<Number, Number> deathNodeLife =null ;//节点的寿命
		HashMap<Number, Number> birthNodeContinuing =null ;//节点的持续性
		HashMap<Number, Number> deathNodeContinuing =null ;//节点的持续性
		HashMap<Number, Number> birthNodeJitter = null;//节点的抖动
		HashMap<Number, Number> deathNodeJitter = null;//节点的抖动
		HashMap<Number, Number> flag = null;//标记
		
		Set<Number> birthNodeSet = null;
		Set<Number> deathNodeSet = null;
		
		String srcFile = null;
		String dstFile = null;
		String temp = null;
		String tempFile = null;
		
		Map<Number, Number> degreeMap = null;// 节点，以及对应的度
		Map<Number, Number> coreMap = null;// 节点，以及对应的核
		
		Number nodeId = null;

		for(int i = 1; i <= 47; i++){
			srcFile = souPath+i+"-"+(i+1)+"/birthNodes.txt";
			birthNodeSet = f.read2Set(srcFile);
			srcFile = souPath+i+"-"+(i+1)+"/deathNodes.txt";
			deathNodeSet = f.read2Set(srcFile);
			
			temp = souPath2+i+"-"+(i+1)+"/";
			//新生节点的度 核的变化
			birthNodeLife = new HashMap<Number, Number>();//节点的寿命
			birthNodeContinuing = new HashMap<Number, Number>();//节点的持续性
			birthNodeJitter = new HashMap<Number, Number>();//节点的抖动
			flag = new HashMap<Number, Number>();
			for(int j = i+1; j <= 48; j++){
				tempFile = temp+"birthNodeDegree-Core-"+j+".txt";
				degreeMap = f.read2Map(tempFile, 1, 2);
				for(Iterator<Number> it = birthNodeSet.iterator(); it.hasNext();){
					nodeId = (Number)it.next();
					D.p(nodeId);
					if(degreeMap.get(nodeId).intValue() != 0){
						//nodeId节点存在于i网络中
						//时效
						if(birthNodeLife.containsKey(nodeId)){
							birthNodeLife.put(nodeId, birthNodeLife.get(nodeId).intValue()+1);
						}else{
							birthNodeLife.put(nodeId, 1);
						}
						
						//抖动
						if(flag.containsKey(nodeId) && flag.get(nodeId).intValue() == 0){
							if(birthNodeJitter.containsKey(nodeId)){
								birthNodeJitter.put(nodeId, birthNodeJitter.get(nodeId).intValue()+1);
							}else{
								birthNodeJitter.put(nodeId, 1);
							}
						}
						flag.put(nodeId, 1);
					}else{
						flag.put(nodeId, 0);
					}
				}
			}
			//持续性
			for(Iterator<Number> it = birthNodeLife.keySet().iterator(); it.hasNext();){
				nodeId = it.next();
				birthNodeContinuing.put(nodeId, birthNodeLife.get(nodeId).doubleValue()/(48-i));
			}
			//保存持续性数据
			f.write(birthNodeContinuing, desPath+"continuing-birthNode-"+i+"-"+(i+1)+".txt");
			//保存时效数据
			f.write(birthNodeLife, desPath+"timeliness-birthNode-"+i+"-"+(i+1)+".txt");
			//保存抖动数据
			f.write(birthNodeJitter, desPath+"jitter-birthNode-"+i+"-"+(i+1)+".txt");
			
			//死亡节点的度 核的变化
			deathNodeLife = new HashMap<Number, Number>();//节点的寿命
			deathNodeContinuing = new HashMap<Number, Number>();//节点的持续性
			deathNodeJitter = new HashMap<Number, Number>();//节点的抖动
			flag = new HashMap<Number, Number>();//标记
			for(int k = 1; k <= i; k++){
				tempFile = temp+"deathNodeDegree-Core-"+k+".txt";
				degreeMap = f.read2Map(tempFile, 1, 2);
				//coreMap = f.read2Map(tempFile, 1, 3);
				for(Iterator<Number> it = deathNodeSet.iterator(); it.hasNext();){
					nodeId = it.next();
					if(degreeMap.get(nodeId).intValue() != 0){
						//nodeId节点存在于i网络中
						//时效
						if(deathNodeLife.containsKey(nodeId)){
							deathNodeLife.put(nodeId, deathNodeLife.get(nodeId).intValue()+1);
						}else{
							deathNodeLife.put(nodeId, 1);
						}
						
						//抖动
						if(flag.containsKey(nodeId) && flag.get(nodeId).intValue() == 0){
							if(deathNodeJitter.containsKey(nodeId)){
								deathNodeJitter.put(nodeId, deathNodeJitter.get(nodeId).intValue()+1);
							}else{
								deathNodeJitter.put(nodeId, 1);
							}
						}
						flag.put(nodeId, 1);
					}else{
						flag.put(nodeId, 0);
					}
				}				
			}
			//持续性
			for(Iterator<Number> it = deathNodeLife.keySet().iterator(); it.hasNext();){
				nodeId = it.next();
				deathNodeContinuing.put(nodeId, deathNodeLife.get(nodeId).doubleValue()/i);
			}
			//保存持续性数据
			f.write(deathNodeContinuing, desPath+"continuing-deathNode-"+i+"-"+(i+1)+".txt");
			//保存时效数据
			f.write(deathNodeLife, desPath+"timeliness-deathNode-"+i+"-"+(i+1)+".txt");
			//保存抖动数据
			f.write(deathNodeJitter, desPath+"jitter-deathNode-"+i+"-"+(i+1)+".txt");
		}
	}


	/*
	 * 死亡节点在后续的网络采样中，是否出现，是否出现抖动
	 */
	public void death_jitter_timeliness_continuing(){
		String souPath = "../data/AS-2009-2012/Evolution/";
		String souPath2 = "../data/AS-2009-2012/all-node-degree/";
		String desPath = "../data/AS-2009-2012/death_jitter_timeliness/";
		FileTool f = new FileTool();
		HashMap<Number, Number> deathNodeLife =null ;//节点的寿命
		HashMap<Number, Number> deathNodeContinuing =null ;//节点的持续性
		HashMap<Number, Number> deathNodeJitter = null;//节点的抖动
		HashMap<Number, Number> flag = null;//标记
		
		Set<Number> deathNodeSet = null;
		
		String srcFile = null;
		String dstFile = null;
		String temp = null;
		String tempFile = null;
		
		Map<Number, Number> degreeMap = null;// 节点，以及对应的度
		Map<Number, Number> coreMap = null;// 节点，以及对应的核
		
		Number nodeId = null;

		for(int i = 1; i <= 47; i++){
			srcFile = souPath+i+"-"+(i+1)+"/deathNodes.txt";
			deathNodeSet = f.read2Set(srcFile);
			
			//temp = souPath2+i+"-"+(i+1)+"/";

			temp = souPath2;
			//死亡节点的度 核的变化
			deathNodeLife = new HashMap<Number, Number>();//节点的寿命
			deathNodeContinuing = new HashMap<Number, Number>();//节点的持续性
			deathNodeJitter = new HashMap<Number, Number>();//节点的抖动
			flag = new HashMap<Number, Number>();//标记
			for(int k = i+2; k <= 48; k++){
				//tempFile = temp+"deathNodeDegree-Core-"+k+".txt";
				tempFile = temp+k+"-node-degree.txt";
				degreeMap = f.read2Map(tempFile, 1, 2);
				//coreMap = f.read2Map(tempFile, 1, 3);
				for(Iterator<Number> it = deathNodeSet.iterator(); it.hasNext();){
					nodeId = (Number)it.next();
					//if(degreeMap.get(nodeId).intValue() != 0){
					if(degreeMap.containsKey(nodeId)){
						//nodeId节点存在于i网络中
						//时效
						if(deathNodeLife.containsKey(nodeId)){
							deathNodeLife.put(nodeId, deathNodeLife.get(nodeId).intValue()+1);
						}else{
							deathNodeLife.put(nodeId, 1);
						}
						
						//抖动
						if(flag.containsKey(nodeId) && flag.get(nodeId).intValue() == 0){
							if(deathNodeJitter.containsKey(nodeId)){
								deathNodeJitter.put(nodeId, deathNodeJitter.get(nodeId).intValue()+1);
							}else{
								deathNodeJitter.put(nodeId, 1);
							}
						}
						flag.put(nodeId, 1);
					}else{
						flag.put(nodeId, 0);
					}
				}				
			}
			//持续性
			for(Iterator<Number> it = deathNodeLife.keySet().iterator(); it.hasNext();){
				nodeId = it.next();
				deathNodeContinuing.put(nodeId, deathNodeLife.get(nodeId).doubleValue()/(48-i));
			}
			//保存持续性数据
			f.write(deathNodeContinuing, desPath+"continuing-deathNode-"+i+"-"+(i+1)+".txt");
			//保存时效数据
			f.write(deathNodeLife, desPath+"timeliness-deathNode-"+i+"-"+(i+1)+".txt");
			//保存抖动数据
			f.write(deathNodeJitter, desPath+"jitter-deathNode-"+i+"-"+(i+1)+".txt");
		}
	}

	/*
	 * 抖动节点 对应的 持续时间比
	 */
	public void death_jitter_continuing(){
		String root = "../data/AS-2009-2012/death_jitter_timeliness/";
		FileTool f = new FileTool();
		StringBuffer sb = new StringBuffer();
		Number nodeId = null;
		for(int i = 1; i <= 47; i++){
			Map<Number, Number> jitter = f.read2Map(root+"jitter-deathNode-"+i+"-"+(i+1)+".txt");//抖动
			Map<Number, Number> continuing = f.read2Map(root+"continuing-deathNode-"+i+"-"+(i+1)+".txt", NumberType.INTEGER, numberType.DOUBLE);//持续时间比
			sb = new StringBuffer();
			for(Iterator<Number> it = jitter.keySet().iterator(); it.hasNext();){
				nodeId = it.next();
				sb.append(nodeId).append("\t");
				sb.append(continuing.get(nodeId)).append("\t");
				sb.append(jitter.get(nodeId)).append("\r\n");
			}
			f.write(sb, root+"death-jitter-continuing-"+i+"-"+(i+1)+".txt");
		}
	}
	/*
	 * 抖动节点 对应的 时效
	 */
	public void death_jitter_timeliness(){
		String root = "../data/AS-2009-2012/death_jitter_timeliness/";
		FileTool f = new FileTool();
		StringBuffer sb = new StringBuffer();
		Number nodeId = null;
		for(int i = 1; i <= 47; i++){
			Map<Number, Number> jitter = f.read2Map(root+"jitter-deathNode-"+i+"-"+(i+1)+".txt");//抖动
			Map<Number, Number> timeliness = f.read2Map(root+"timeliness-deathNode-"+i+"-"+(i+1)+".txt", NumberType.INTEGER, numberType.DOUBLE);//持续时间比
			//Map<Number, Number> count = new HashMap<Number, Number>();
			Map<Number, Number> count =  f.read2Map(root+"death-timeliness-frequency-"+i+"-"+(i+1)+".txt", NumberType.DOUBLE, NumberType.INTEGER);
			sb = new StringBuffer();
			for(Iterator<Number> it = jitter.keySet().iterator(); it.hasNext();){
				nodeId = it.next();
				sb.append(nodeId).append("\t");
				sb.append(timeliness.get(nodeId)).append("\t");
				sb.append(jitter.get(nodeId)).append("\t");
				sb.append(count.get(timeliness.get(nodeId))).append("\r\n");
				//count.put(nodeId, timeliness.get(nodeId));
			}
			//f.write(MathTool.frequency(count), root+"death-timeliness-frequency-"+i+"-"+(i+1)+".txt");
			f.write(sb, root+"death-jitter-timeliness-"+i+"-"+(i+1)+".txt");
		}
	}
	/*
	 * 节点出现的时间频率
	 */
	public void birthNodeTimelinessFreq(){
		//String srcFile = this.desPath+"node-lifetime/2009-frequency.txt";
		for(int i = 1; i <= 47; i++){
			String srcFile = this.desPath+"node-timeliness-jitter/timeliness-birthNode-"+i+"-"+(i+1)+".txt";
			
			Map<Number, Number> timeliness = null;
			timeliness = this.f.read2Map(srcFile, this.numberType);
			//this.f.write(MathTool.ratio(frequency), this.desPath+"node-lifetime/2009-rate.txt", false);
			Map<Number, Number> frequency = MathTool.frequency(timeliness);
			this.f.write(frequency, this.desPath+"node-timeliness-jitter/timelinessFreq-birthNode-"+i+"-"+(i+1)+".txt", false);
			Map<Number, Number> ratio = new HashMap<Number, Number>();
			Number nodeId = null;
			int n = timeliness.size();
			for(Iterator<Number> it = frequency.keySet().iterator(); it.hasNext();){
				nodeId = it.next();
				ratio.put(nodeId.doubleValue()/(48-i), frequency.get(nodeId).doubleValue()/n);
			}
			//节点持续时间比与节点数量的分布关系
			this.f.write(ratio, this.desPath+"node-timeliness-jitter/continuning-numRatio-birthNode-"+i+"-"+(i+1)+".txt");
			//this.f.write(MathTool.ratio(frequency), this.desPath+"node-timeliness-jitter/timelinessFreq-birthNode-1-2.txt", false);
			
		}

	}
	/*
	 * 新生节点的持续时间比与节点数量的累积比例分布
	 */
	public void birthNodeContinuingRatioAndNumAccumulateRatio(){
		MapTool mt = new MapTool();
		for(int i = 1; i <= 47; i++){
			String srcFile = this.desPath+"node-timeliness-jitter/continuning-numRatio-birthNode-"+i+"-"+(i+1)+".txt";
			
			Map<Number, Number> cn = null;
			cn = this.f.read2Map(srcFile, NumberType.DOUBLE);
			//this.f.write(MathTool.ratio(frequency), this.desPath+"node-lifetime/2009-rate.txt", false);
			PairList<Number, Number> sort = mt.sort(cn, true, true);
			this.f.write(sort, this.desPath+"node-timeliness-jitter-1/continuing-numRatio-birthNode-"+i+"-"+(i+1)+".txt", false);
			this.f.write(MathTool.accumulate(sort), this.desPath+"node-timeliness-jitter/continuing-accumulatRatio-birthNode-"+i+"-"+(i+1)+".txt", false);
		}		
	}
	
	/*
	 * 提取新生的稳定节点 根据持续时间比：
	 * 1，等于1
	 * 2，不小于0.9
	 * 3，不小于0.8
	 * 
	 */
	public void extractStableNode(float f){
		MapTool mt = new MapTool();
		for(int i = 1; i <= 47; i++){
			String srcFile = this.desPath+"node-timeliness-jitter/timeliness-birthNode-"+i+"-"+(i+1)+".txt";
			
			Map<Number, Number> timeliness = null;
			timeliness = this.f.read2Map(srcFile, this.numberType);
			int v = (int) Math.floor(f*(48-i));
			Map<Number, Number> nodeSet = mt.select(timeliness, ArithmeticOperators.GE, v);

			this.f.write(nodeSet.keySet(), this.desPath+"node-timeliness-jitter/"+f+"/stable-birth-"+i+"-"+(i+1)+".txt");
			
		}
	}
	
	/*
	 * 稳定节点的度，核，
	 * ?邻接点度，核
	 */
	public void extractStableNodeFearture(float f){
		String srcFile = null;//稳定节点
		String srcFile2 = null;//节点的度和核
		
		String souPath2 = "../data/AS-2009-2012/birth-node-degree-core/";
		Set<Number> nodeSet = null;
		
		Map<Number, Number> degree = null;//节点在i网络中的度
		Map<Number, Number> core = null;//节点在i网络中的核
		
		Map<Number, LinkedList<Number>> degreeMap = null;//每个节点的度
		Map<Number, LinkedList<Number>> coreMap = null;//每个节点的核
		
		Number nodeId = null;
		for(int i = 1; i <= 47; i++){
			srcFile = this.desPath+"Evolution/"+i+"-"+(i+1)+"/birthNodes.txt";
//			srcFile = this.desPath+"node-timeliness-jitter/"+f+"/stable-birth-"+i+"-"+(i+1)+".txt";
			nodeSet = this.f.read2Set(srcFile);//稳定节点集
			
			degreeMap = new HashMap<Number, LinkedList<Number>>();//每个节点的度
			coreMap = new HashMap<Number, LinkedList<Number>>();//每个节点的核
			for(int j = i; j <= 47; j++){
				//新生
				srcFile2 = souPath2+i+"-"+(i+1)+"/birthNodeDegree-Core-"+(j+1)+".txt";
//				D.p("####"+srcFile2);
				degree = this.f.read2Map(srcFile2, 1, 2);
				core = this.f.read2Map(srcFile2, 1, 3);
				for(Iterator<Number> it = nodeSet.iterator(); it.hasNext();){
					nodeId = it.next();
					if(degreeMap.containsKey(nodeId)){
						degreeMap.get(nodeId).add(degree.get(nodeId));
					}else{
						LinkedList<Number> ll = new LinkedList<Number>();
						ll.add(degree.get(nodeId));
						degreeMap.put(nodeId, ll);
					}
					
					if(coreMap.containsKey(nodeId)){
						coreMap.get(nodeId).add(core.get(nodeId));
					}else{
						LinkedList<Number> ll = new LinkedList<Number>();
						ll.add(core.get(nodeId));
						coreMap.put(nodeId, ll);
					}
				}
				
			}
			
			StringBuffer sb1 = new StringBuffer();
			for(Iterator<Number> it = nodeSet.iterator(); it.hasNext();){
				//遍历所有的节点
				nodeId = it.next();
				sb1.append(nodeId).append("\t");
				LinkedList<Number> ll = degreeMap.get(nodeId);
				int sum = 0, k =0;
				StringBuffer sb2 = new StringBuffer();
				for(Iterator<Number> it2 = ll.iterator(); it2.hasNext();){
					//遍历每个节点的所有度
					int v = it2.next().intValue();
					if(v != 0){
						k++;
					}
					sum += v;
					sb2.append(v).append("\t");
				}
				sb1.append((float)sum/k).append("\t");//添加平均度
				sb1.append(sb2).append("\r\n");
			}
			
			//写如文件
			this.f.write(sb1, this.desPath+"birth-degree-core/"+f+"/birth-degree-"+i+"-"+(i+1)+"-"+f+".txt");

			
			sb1 = new StringBuffer();
			for(Iterator<Number> it = nodeSet.iterator(); it.hasNext();){
				//遍历所有的节点
				nodeId = it.next();
				sb1.append(nodeId).append("\t");
				LinkedList<Number> ll = coreMap.get(nodeId);
				int sum = 0, k = 0;
				StringBuffer sb2 = new StringBuffer();
				for(Iterator<Number> it2 = ll.iterator(); it2.hasNext();){
					//遍历每个节点的所有核
					int v = it2.next().intValue();
					if(v != 0 && v != -1 ){
						k++;
						sum += v;
					}
					sb2.append(v).append("\t");
				}
				if(k == 0){
					sb1.append(0).append("\t");//添加平均核
				}else{
					sb1.append((float)sum/k).append("\t");//添加平均核
				}
				sb1.append(sb2).append("\r\n");
			}
			
			//写如文件
			this.f.write(sb1, this.desPath+"birth-degree-core/"+f+"/birth-core-"+i+"-"+(i+1)+"-"+f+".txt");
		}		
	}
	
	//deathNodeAdjDegree-Core-2-6.txt
	/*
	 * 消亡节点的度 核的变化
	 */
	public void deathFeature(){
		String srcFile = null;//消亡节点
		String srcFile2 = null;//节点的度和核
		
		String souPath2 = "../data/AS-2009-2012/birth-node-degree-core/";
		Set<Number> nodeSet = null;
		
		Map<Number, Number> degree = null;//节点在i网络中的度
		Map<Number, Number> core = null;//节点在i网络中的核
		
		Map<Number, LinkedList<Number>> degreeMap = null;//每个节点的所有度
		Map<Number, LinkedList<Number>> coreMap = null;//每个节点的所有核
		
		Number nodeId = null;
		for(int i = 1; i <= 47; i++){
			//srcFile = this.desPath+"Evolution/"+i+"-"+(i+1)+"/deathNodes.txt";
			srcFile =  this.desPath+"death-degree-core/real/real-death-node-"+i+"-"+(i+1)+".txt";
//			srcFile = this.desPath+"node-timeliness-jitter/"+f+"/stable-birth-"+i+"-"+(i+1)+".txt";
			nodeSet = this.f.read2Set(srcFile);//消亡节点集
			
			degreeMap = new HashMap<Number, LinkedList<Number>>();//每个节点的所有度
			coreMap = new HashMap<Number, LinkedList<Number>>();//每个节点的所有核
			for(int j = 1; j <= i; j++){
				//新生
				srcFile2 = souPath2+i+"-"+(i+1)+"/deathNodeDegree-Core-"+j+".txt";
//				D.p("####"+srcFile2);
				degree = this.f.read2Map(srcFile2, 1, 2);
				core = this.f.read2Map(srcFile2, 1, 3);
				for(Iterator<Number> it = nodeSet.iterator(); it.hasNext();){
					nodeId = it.next();
					if(degreeMap.containsKey(nodeId)){
						degreeMap.get(nodeId).add(degree.get(nodeId));
					}else{
						LinkedList<Number> ll = new LinkedList<Number>();
						ll.add(degree.get(nodeId));
						degreeMap.put(nodeId, ll);
					}
					
					if(coreMap.containsKey(nodeId)){
						coreMap.get(nodeId).add(core.get(nodeId));
					}else{
						LinkedList<Number> ll = new LinkedList<Number>();
						ll.add(core.get(nodeId));
						coreMap.put(nodeId, ll);
					}
				}
				
			}
			
			StringBuffer sb1 = new StringBuffer();
			//StringBuffer sb3 = new StringBuffer();
			for(Iterator<Number> it = nodeSet.iterator(); it.hasNext();){
				//遍历所有的节点
				nodeId = it.next();
				sb1.append(nodeId).append("\t");
				LinkedList<Number> ll = degreeMap.get(nodeId);
				int sum = 0, k =0;
				StringBuffer sb2 = new StringBuffer();
				for(Iterator<Number> it2 = ll.iterator(); it2.hasNext();){
					//遍历每个节点的所有度
					int v = it2.next().intValue();
					if(v != 0){
						k++;
					}
					sum += v;
					sb2.append(v).append("\t");
				}
				sb1.append((float)sum/k).append("\t");//添加平均度
				sb1.append(sb2).append("\r\n");
				
			}
			
			//写如文件
			this.f.write(sb1, this.desPath+"death-degree-core/real/death-degree-"+i+"-"+(i+1)+".txt");

			
			sb1 = new StringBuffer();
			for(Iterator<Number> it = nodeSet.iterator(); it.hasNext();){
				//遍历所有的节点
				nodeId = it.next();
				sb1.append(nodeId).append("\t");
				LinkedList<Number> ll = coreMap.get(nodeId);
				int sum = 0, k = 0;
				StringBuffer sb2 = new StringBuffer();
				for(Iterator<Number> it2 = ll.iterator(); it2.hasNext();){
					//遍历每个节点的所有核
					int v = it2.next().intValue();
					if(v != 0 && v != -1 ){
						k++;
						sum += v;
					}
					sb2.append(v).append("\t");
				}
				if(k == 0){
					sb1.append(0).append("\t");//添加平均核
				}else{
					sb1.append((float)sum/k).append("\t");//添加平均核
				}
				sb1.append(sb2).append("\r\n");
			}
			
			//写如文件
			this.f.write(sb1, this.desPath+"death-degree-core/real/death-core-"+i+"-"+(i+1)+".txt");
			
			
			
		}		
	}



	/*
	 * 消亡节点邻接点的度 核的变化
	 */
	public void deathAdjFeature(){
		String srcFile = null;//消亡节点
		String srcFile2 = null;//节点的度和核
		
		String souPath2 = "../data/AS-2009-2012/adjNode-degree-core/";
		Set<Number> nodeSet = null;
		
		Map<Number, Number> degree = null;//节点在i网络中的度
		Map<Number, Number> core = null;//节点在i网络中的核
		
		Map<Number, LinkedList<Number>> degreeMap = null;//每个节点的所有度
		Map<Number, LinkedList<Number>> coreMap = null;//每个节点的所有核
		
		Number nodeId = null;
		for(int i = 1; i <= 47; i++){
			srcFile = this.desPath+"Evolution/"+i+"-"+(i+1)+"/deathNodes.txt";
//			srcFile = this.desPath+"node-timeliness-jitter/"+f+"/stable-birth-"+i+"-"+(i+1)+".txt";
			nodeSet = this.f.read2Set(srcFile);//消亡节点集
			
			degreeMap = new HashMap<Number, LinkedList<Number>>();//每个节点的所有度
			coreMap = new HashMap<Number, LinkedList<Number>>();//每个节点的所有核
			for(int j = 1; j <= i; j++){
				//消亡节点的邻接点
				srcFile2 = souPath2+i+"-"+(i+1)+"/deathNodeAdjDegree-Core-2-"+j+".txt";
//				D.p("####"+srcFile2);
				degree = this.f.read2Map(srcFile2, 1, NumberType.INTEGER, 3, NumberType.DOUBLE);
				core = this.f.read2Map(srcFile2, 1, NumberType.INTEGER, 4, NumberType.DOUBLE);
				for(Iterator<Number> it = nodeSet.iterator(); it.hasNext();){
					nodeId = it.next();
					if(degreeMap.containsKey(nodeId)){
						if(degree.get(nodeId) == null){
							degreeMap.get(nodeId).add(0);
						}else{
							degreeMap.get(nodeId).add(degree.get(nodeId));
						}
					}else{
						LinkedList<Number> ll = new LinkedList<Number>();
						if(degree.get(nodeId) == null){
							ll.add(0);
						}else{
							ll.add(degree.get(nodeId));
						}
						degreeMap.put(nodeId, ll);
					}
					
					if(coreMap.containsKey(nodeId)){
						if(core.get(nodeId) == null){
							coreMap.get(nodeId).add(-1);
						}else{
							coreMap.get(nodeId).add(core.get(nodeId));
						}
					}else{
						LinkedList<Number> ll = new LinkedList<Number>();
						if(core.get(nodeId) == null){
							ll.add(-1);
						}else{
							ll.add(core.get(nodeId));
						}
						coreMap.put(nodeId, ll);
					}
				}
				
			}
			
			StringBuffer sb1 = new StringBuffer();
			//StringBuffer sb3 = new StringBuffer();
			for(Iterator<Number> it = nodeSet.iterator(); it.hasNext();){
				//遍历所有的节点
				nodeId = it.next();
				D.p("$$$"+nodeId);
				sb1.append(nodeId).append("\t");
				LinkedList<Number> ll = degreeMap.get(nodeId);
				double sum = 0, k =0;
				StringBuffer sb2 = new StringBuffer();
				Number temp = null;
				for(Iterator<Number> it2 = ll.iterator(); it2.hasNext();){
					//遍历每个节点在每次采样网络中的平均度
					//D.p("####"+it2.next().toString());
					temp = it2.next();
					D.p("####"+temp.toString());
//					double v = it2.next().doubleValue();
					double v = temp.doubleValue();
					if(v != 0){
						k++;
					}
					sum += v;
					sb2.append(v).append("\t");
				}
				//D.p("@@@@@@");
				sb1.append((double)sum/k).append("\t");//添加平均度
				sb1.append(sb2).append("\r\n");
				
			}
			
			//写如文件
			this.f.write(sb1, this.desPath+"death-degree-core/adj/deathAdj-degree-"+i+"-"+(i+1)+".txt");

			
			sb1 = new StringBuffer();
			for(Iterator<Number> it = nodeSet.iterator(); it.hasNext();){
				//遍历所有的节点
				nodeId = it.next();
				sb1.append(nodeId).append("\t");
				LinkedList<Number> ll = coreMap.get(nodeId);
				double sum = 0, k = 0;
				StringBuffer sb2 = new StringBuffer();
				for(Iterator<Number> it2 = ll.iterator(); it2.hasNext();){
					//遍历每个节点的所有核
					double v = it2.next().doubleValue();
					if(v != 0 && v != -1 ){
						k++;
						sum += v;
					}
					sb2.append(v).append("\t");
				}
				if(k == 0){
					sb1.append(0).append("\t");//添加平均核
				}else{
					sb1.append((double)sum/k).append("\t");//添加平均核
				}
				sb1.append(sb2).append("\r\n");
			}
			
			//写如文件
			this.f.write(sb1, this.desPath+"death-degree-core/adj/deathAdj-core-"+i+"-"+(i+1)+".txt");
			
		}		
	}

	/*
	 * 真正消亡的节点
	 */
	public void realDeath(){
		
		Set<Number> deathNode = new HashSet<Number>();//死亡节点
		Set<Number> timelinessNode = new HashSet<Number>();//时效大于0的节点
		Set<Number> realNode = new HashSet<Number>();//真正消亡的节点
		String srcFile = null;
		String srcFile2 = null;
		for(int i = 1; i <= 47; i++){
			srcFile = this.desPath+"Evolution/"+i+"-"+(i+1)+"/deathNodes.txt";
			srcFile2 = this.desPath+"death_jitter_timeliness/timeliness-deathNode-"+i+"-"+(i+1)+".txt";
			deathNode = this.f.read2Set(srcFile);//消亡节点
			int[] col = new int[1];
			col[0] = 1;
			timelinessNode = this.f.read2Set(srcFile2, NumberType.INTEGER, col);
			realNode = MathTool.subtraction(deathNode, timelinessNode);//差集
			this.f.write(realNode, this.desPath+"death-degree-core/real/real-death-node-"+i+"-"+(i+1)+".txt");
		}
	}
}
