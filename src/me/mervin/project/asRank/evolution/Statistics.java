package me.mervin.project.asRank.evolution;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.w3c.dom.NodeList;

import me.mervin.core.Global.NumberType;
import me.mervin.util.D;
import me.mervin.util.FileTool;
import me.mervin.util.Pair;
import me.mervin.util.PairList;


 /**
 *   Statistics.java
 *    
 *  @author Mervin.Wong  DateTime 2014年3月25日 下午7:37:19    
 *  @version 0.4.0
 */
public class Statistics {
	
	FileTool ft = new FileTool();

	public static void main(String[] args){
		Statistics s = new Statistics();
//		s.treeHeight();
//		s.treeLevelAndBranch();
//		s.levelAndCore();
	
//		s.levelAndRate();
//		s.p2pDegree();
//		s.p2pLevel();
		s.p2pDiff();
//		s.p2pConnection();
//		s.p2pBetweenness();
		///s.PathInfo();
		//s.treeHeight();
	}
	
	
	/*
	 * 树的高度，时序
	 */
	public void treeHeight(){
		String srcDir = "E:\\data\\as-rel-res\\";
		String srcFile = null;
		String srcFile1 = null;
		String dstDir = "E:\\data\\as-rel-stat\\";
		String date = null;
		Thread t = null;
		List<LinkedList<String>> str = new LinkedList<LinkedList<String>>();
		for(int i = 0; i < 34; i++){
			LinkedList<String> l = new LinkedList<String>();
			str.add(l);
		}
		SortedSet<Integer> set = null;
		SortedSet<Integer> h = null;
		Set<Number> allCoreNodeSet = ft.read2Set(dstDir+"all-core-node.txt");
		Map<Number, Number> hDist = null;
		Map<Number, Number> maxMap = new HashMap<Number, Number>();
		Map<Number, Number> minMap = new HashMap<Number, Number>();
		Map<Number, Number> avgMap = new HashMap<Number, Number>();
		Map<Number, LinkedList<Number>> nodeLifeMap = new HashMap<Number, LinkedList<Number>>();
		for(Number node:allCoreNodeSet){
			LinkedList<Number> l = new LinkedList<Number>();
			nodeLifeMap.put(node, l);
		}
		int fileNum = 0;
		StringBuffer sb4  = new StringBuffer();
		for(int y = 1998; y <= 2013; y++){
			for(int m = 1; m <= 12; m+=1){
				if(m < 10){
					date = y+"0"+m+"01";
				}else{
					date = y+""+m+"01";
				}
				D.p(date);
				srcFile = srcDir+date+"\\h.txt";
				
				if(ft.isExist(srcFile)){
					fileNum++;
					String line = null;
					String[] lineArr = null;
					set = new TreeSet<Integer>();
					h = new TreeSet<Integer>();
					hDist = new HashMap<Number, Number>();
					try {
						BufferedReader reader = new BufferedReader(new FileReader(srcFile));
						//BufferedReader reader1 = new BufferedReader(new FileReader(dstFile));
						StringBuffer sb = new StringBuffer();
						int j = 34, i = 0;
						int count = 0, max = 0, min = Integer.MAX_VALUE, sum = 0;
						double avg = 0;
						Map<Number, Number> oneRecord = new HashMap<Number, Number>();
						while((line = reader.readLine())!= null){
//							D.p(line);
//							D.p(lineArr[0]);
							lineArr = line.split("\\s+");
							//allCoreNodeSet.add(Integer.parseInt(lineArr[0]));
							if(!set.contains(Integer.parseInt(lineArr[0]))){
								set.add(Integer.parseInt(lineArr[0]));
							}else{
								D.p("###"+date+"###");
								System.exit(0);
							}
//							D.p(line);
							//lineArr = line.split("\\s+");
//							/D.p(lineArr[1]);
//							D.p(i);
							//str.get(i).add(lineArr[1]);
							int height = Integer.parseInt(lineArr[1]);
							if(hDist.containsKey(height)){
								hDist.put(height, hDist.get(height).intValue()+1);
							}else{
								hDist.put(height, 1);
							}
							h.add(height);
							count++;
							
							if(height > max){
								max = height;
							}
							if(height < min){
								min = height;
							}
							sum += height;
							oneRecord.put(Integer.parseInt(lineArr[0]), height);
						}//while
						
						
						for(Number node2:allCoreNodeSet ){
							if(oneRecord.containsKey(node2)){
								nodeLifeMap.get(node2).add(oneRecord.get(node2));
								if((1 <= oneRecord.get(node2).intValue() && oneRecord.get(node2).intValue() <= 4)){
									sb4.append(node2).append("\r\n");
								}
							}else{
								nodeLifeMap.get(node2).add(0);
							}
						}
						
						avg = (double)sum/count;
						maxMap.put(fileNum, max);
						avgMap.put(fileNum, avg);
						minMap.put(fileNum, min);
						for(Integer n:h){
							str.get(i).add(n+"");
							i++;
						}
						for(;i < j; i++){
							str.get(i).add("#");
						}
//						D.p(set);
						for(Number he:hDist.keySet()){
							hDist.put(he, (double)hDist.get(he).doubleValue()/count);
						}
						String dstFile = dstDir+"\\height\\"+date+"-dist.txt";
						ft.write(hDist, dstFile);

					} catch (FileNotFoundException e) {
						// TODO 自动生成的 catch 块
						e.printStackTrace();
					} catch (IOException e) {
						// TODO 自动生成的 catch 块
						e.printStackTrace();
					}
					
					
				}else{
					
				}
				D.p("###################");
			}
		}//for
		
		String dstFile = dstDir+"\\height\\"+date+"-max.txt";
		D.p(maxMap);
		ft.write(maxMap, dstFile);
		 dstFile = dstDir+"\\height\\"+date+"-min.txt";
		ft.write(minMap, dstFile);
		 dstFile = dstDir+"\\height\\"+date+"-avg.txt";
		ft.write(avgMap, dstFile);
		StringBuffer sb = new StringBuffer();
		for(LinkedList<String> l:str){
			for(String s:l){
				if(!s.equals("#")){
					sb.append(s).append("\t");
				}else{
					sb.append("#").append("\t");
				}
			}//for
			sb.append("\r\n");
		}
		 dstFile = dstDir+"height_1.txt";
//		 D.p(allCoreNodeSet);
		 D.p(sb4);
/*		 for(Entry<Number, LinkedList<Number>> e:nodeLifeMap.entrySet()){
			 System.out.print(e.getKey()+"\t");
			 for(Number temp:e.getValue()){
				 System.out.print(temp+"\t");
			 }
			 System.out.print("\r\n");
		 }*/
		ft.write(sb, dstFile);
	}

	/*
	 * 节点的分叉和节点所在的层次
	 */
	
	public void treeLevelAndBranch(){
		String srcDir = "E:\\data\\as-rel-res\\";
		String srcFile1 = null;
		String srcFile2 = null;
		String dstDir = "E:\\data\\as-rel-stat\\branch-level\\";
		String dstFile = null;
		String date = null;
		Thread t = null;
		Set<Number> set = null;
		ArrayList<String> array = new ArrayList<String>();
		for(int y = 1998; y <= 2013; y++){
			for(int m = 2; m <= 2; m++){
				if(m < 10){
					date = y+"0"+m+"01";
				}else{
					date = y+""+m+"01";
				}
				D.p(date);
				int rootId = 701;
//				int rootId = 1239;
				srcFile1 = srcDir+date+"\\"+rootId+"-children.txt";
				srcFile2 = srcDir+date+"\\"+rootId+"-level.txt";
				
				if(ft.isExist(srcFile1)){
					Map<Number, Number> children = ft.read2Map(srcFile1);
					Map<Number, Number> level = ft.read2Map(srcFile2);
					
					PairList<Number, Number> pair = new PairList<Number, Number>();
					for(Number nodeId:children.keySet()){
						pair.add(level.get(nodeId), children.get(nodeId));
					}
					
					SortedSet<Number> levelSet = new TreeSet<Number>();
					Map<Number, SortedSet<Number>> levelBranchMap = new HashMap<Number, SortedSet<Number>>();
					Number l, r;
					for(int i = 0; i < pair.size(); i++){
						l = pair.getL(i);
						r = pair.getR(i);
						/*
						 * 包括叶子节点
						 */
						if(levelBranchMap.containsKey(l)){
							levelBranchMap.get(l).add(r);
						}else{
							levelSet = new TreeSet<Number>();
							levelSet.add(r);
							levelBranchMap.put(l, levelSet);
						}
					}
					int sum = 0, count = 0, max = 0, min = 0;
					StringBuffer sb1 = new StringBuffer();
					StringBuffer sb2 = new StringBuffer();
					for(Number temp:levelBranchMap.keySet()){
						levelSet = levelBranchMap.get(temp);
						sb1.append(temp).append("\t").append(levelSet.last()).append("\r\n");
						sum = 0;
						for(Number t2:levelSet){
							 sum += t2.intValue();
						}
						sb2.append(temp).append("\t").append((double)sum/levelSet.size()).append("\r\n");
					}
					
					
					dstFile = dstDir+"branch\\max\\"+date+"-"+rootId+"-max.txt";
					ft.write(sb1, dstFile);
					dstFile = dstDir+"branch\\avg\\"+date+"-"+rootId+"-avg.txt";
					ft.write(sb2, dstFile);
					dstFile = dstDir+rootId+"\\"+date+"-"+rootId+"-level-branch.txt";
					//ft.write(pair, dstFile);
				}else{
					
				}
				
				D.p("###################");
			}
		}//for
	}
	
	
	
	/*
	 * 节点的层次和k-核的关系
	 * 
	 */
	
	public void levelAndCore(){
		String date = "20130601";
		String srcDir = "E:\\data\\";
		String srcFile = null;
		//date的网络k-core
		srcFile = srcDir+"\\as-rel-core\\"+date+"-core.txt";
		Map<Number,Number> coreMap = ft.read2Map(srcFile);
		
		PairList<Number, Number> list = new PairList<Number, Number>();
		String filter = "level";
		File[] fileArr = ft.fileArr(srcDir+"\\as-rel-res\\"+date+"\\", filter);
		for (int i = 0; i < fileArr.length; i++) {
			//D.p(fileArr[i].getAbsoluteFile());
			Map<Number, Number> levelMap = ft.read2Map(fileArr[i].getAbsolutePath());
			for(Number nodeId:levelMap.keySet()){
				list.add(levelMap.get(nodeId), coreMap.get(nodeId));
			}
		}
		String dstFile = srcDir+"as-rel-stat\\core-level\\"+date+"-core-level.txt";
		ft.write(list, dstFile);
	}
	
	/*
	 * 各层节点所占比例
	 */
	public void levelAndRate(){
		String srcDir = "E:\\data\\as-rel-res\\";
		String srcFile = null;
		String dstFile = null;
//		int rootId = 701;
//		int rootId = 1239;
//		int rootId = 1239;
//		int rootId = 3561;
		int rootId = 7018;
		Map<Number, Number> levelCount = null;
		String date = null;
		int temp = 0;
		for(int y = 1998; y <= 2013; y++){
			
			for(int m = 1; m<=12; m++){
				if(m < 10){
					date = y+"0"+m+"01";
				}else{
					date = y+""+m+"01";
				}
				srcFile = srcDir+date+"\\"+rootId+"-level.txt";
				if(ft.isExist(srcFile)){
					levelCount = new HashMap<Number, Number>();
					Map<Number, Number> levelMap = ft.read2Map(srcFile);
					for(Number nodeId:levelMap.keySet()){
						temp = levelMap.get(nodeId).intValue();
						if(levelCount.containsKey(temp)){
							levelCount.put(temp, levelCount.get(temp).intValue()+1);
						}else{
							levelCount.put(temp, 1);
						}
					}
					for(Number n1:levelCount.keySet()){
						levelCount.put(n1, (double)levelCount.get(n1).doubleValue()/levelMap.size());
					}
					dstFile = "E:\\data\\as-rel-stat\\level-rate\\"+rootId+"\\"+date+"-"+rootId+".txt";
					ft.write(levelCount, dstFile);
				}
			}
		}
		
	}


	/*
	 * P2P连接偏好
	 */
	public void p2pDegree(){
		String srcDir = "E:\\data\\";
		String srcFile = null;
		String dstFile = null;
		Map<Number, Number> levelCount = null;
		String date = null;
		int temp = 0;
		
		PairList<Number, Number> pair = null;
		Map<Number, Number> inDegreeMap = null;
		Map<Number, Number> outDegreeMap = null;
		Map<Number, Number> degreeMap = null;
		
		StringBuffer sb =null;
		StringBuffer sb1 =null;
		Number l, r;
		for(int y = 1998; y <= 2013; y++){
			
			for(int m = 1; m<=12; m++){
				if(m < 10){
					date = y+"0"+m+"01";
				}else{
					date = y+""+m+"01";
				}
				srcFile = srcDir+"\\as-rel-p2p\\"+date+"-p2p.txt";
				if(ft.isExist(srcFile)){
					sb = new StringBuffer();
					sb1 = new StringBuffer();
					pair = ft.read2PairList(srcFile, NumberType.INTEGER);
					srcFile = srcDir+"\\as-rel-degree\\"+date+"-inDegree.txt";
					inDegreeMap = ft.read2Map(srcFile);
					srcFile = srcDir+"\\as-rel-degree\\"+date+"-outDegree.txt";
					outDegreeMap = ft.read2Map(srcFile);
					srcFile = srcDir+"\\as-rel-degree\\"+date+"-degree.txt";
					degreeMap = ft.read2Map(srcFile);
					
					for(int i = 0; i < pair.size(); i++){
						l = pair.getL(i);
						r = pair.getR(i);
						sb.append(inDegreeMap.get(l)).append("\t").append(outDegreeMap.get(l)).append("\t");
						sb.append(inDegreeMap.get(r)).append("\t").append(outDegreeMap.get(r)).append("\r\n");
						sb1.append(degreeMap.get(l)).append("\t").append(degreeMap.get(r)).append("\r\n");
						
					}
					dstFile = srcDir+"\\as-rel-p2p-pre\\"+date+".txt";
//					ft.write(sb, dstFile);
					dstFile = srcDir+"\\as-rel-p2p-pre\\"+date+"-degree.txt";
					ft.write(sb1, dstFile);
				}

				
			}
		}
	}



	public void p2pLevel(){
		String srcDir = "E:\\data\\";
		String srcFile = null;
		String dstFile = null;
		Map<Number, Number> levelCount = null;
		String date = null;
		int temp = 0;
		
		PairList<Number, Number> pair = null;
		Map<Number, Number> levelMap = null;
		
		StringBuffer sb =null; 
		Number l, r;
		for(int y = 1998; y <= 2013; y++){
			
			for(int m = 1; m<=12; m++){
				if(m < 10){
					date = y+"0"+m+"01";
				}else{
					date = y+""+m+"01";
				}
				srcFile = srcDir+"\\as-rel-p2p\\"+date+"-p2p.txt";
				if(ft.isExist(srcFile)){
					sb = new StringBuffer();
					levelMap = new HashMap<Number, Number>();
					pair = ft.read2PairList(srcFile, NumberType.INTEGER);
					
					srcFile = srcDir+"\\as-rel-res\\"+date+"\\";
					File[] fileArr = ft.fileArr(srcFile, "level");
					for (int i = 0; i < fileArr.length; i++) {
						levelMap.putAll(ft.read2Map(fileArr[i].getAbsolutePath()));
					}
					
					for(int i = 0; i < pair.size(); i++){
						l = pair.getL(i);
						r = pair.getR(i);
						if(levelMap.get(l) != null && levelMap.get(r)!= null){
							sb.append(levelMap.get(l)).append("\t").append(levelMap.get(r)).append("\r\n");
						}
						
					}
					dstFile = srcDir+"\\as-rel-p2p-pre\\"+date+"-level.txt";
					ft.write(sb, dstFile);
				}

				
			}
		}		
	}


	public void p2pDiff(){
		String srcDir = "E:\\data\\as-rel-p2p-pre\\";
		FileTool ft = new FileTool();
		for(int y = 1998; y <= 2013; y++){
			for(int m = 1; m <= 12; m++){
				if(m != 2 ){
					continue;
				}
				String date = null;
				if(m < 10){
					date = y + "0"+m+"01";
				}else{
					date = y +""+m+"01";
				}
				Map<Number, Number> map = new TreeMap<Number,Number>();
				String srcFile = srcDir+date+"-level.txt";
				if(!ft.isExist(srcFile)){
					continue;
				}
				PairList<Number, Number> pairList = ft.read2PairList(srcFile, NumberType.INTEGER);
				Number l, r ;
				int count = 0;
				for(int i = 0; i < pairList.size(); i++){
					count++;
					l = pairList.getL(i);
					r = pairList.getR(i);
					int t = Math.abs(l.intValue()-r.intValue());
					if(map.containsKey(t)){
						map.put(t, map.get(t).intValue()+1);
					}else{
						map.put(t, 1);
					}
				}
				
				D.p(date);
				String dstFile = srcDir+date+"-diff.txt";
				ft.write(map, dstFile);
				int sum = 0;
				StringBuffer sb = new StringBuffer();
				for(Number t1:map.keySet()){
					int c = map.get(t1).intValue();
					sum += c;
					map.put(t1, (double)c/count);
					sb.append(t1).append("\t").append((double)sum/count).append("\r\n");
				}
				dstFile = srcDir+"diff\\"+date+"-diff-rate.txt";
				ft.write(map, dstFile);
				dstFile = srcDir+"diff\\"+date+"-diff-acc-rate.txt";
				ft.write(sb, dstFile);
			}
		}
	}
	
	
	public void p2pConnection(){
		String srcDir = "E:\\data\\";
		FileTool ft = new FileTool();
		StringBuffer sb = new StringBuffer();
		int fileNum = 0;
		for(int y = 1998; y <= 2013; y++){
			for(int m = 1; m <= 12; m++){
				if(m != 2 ){
					continue;
				}
				String date = null;
				if(m < 10){
					date = y + "0"+m+"01";
				}else{
					date = y +""+m+"01";
				}
				Map<Number, Number> map = new HashMap<Number,Number>();
				String srcFile = srcDir+"\\as-rel-p2p\\"+date+"-p2p.txt";
				if(!ft.isExist(srcFile)){
					continue;
				}
				PairList<Number, Number> pairList = ft.read2PairList(srcFile, NumberType.INTEGER);
				
				Map<Number, Set<Number>> treeMap = new HashMap<Number, Set<Number>>();
				srcFile = srcDir+"\\as-rel-res\\"+date+"\\";
				File[] fileArr = ft.fileArr(srcFile, "level");
				for (Integer i = 0; i < fileArr.length; i++) {
					srcFile = fileArr[i].getAbsolutePath();
					treeMap.put(i,  ft.read2Map(srcFile).keySet());
				}
				
				
				Number l, r ;
				//p1 inter-tree
				//p2 extra-tree
				int p1 = 0, p2 =0;
				boolean flag = false;
				for(int i = 0; i < pairList.size(); i++){
					l = pairList.getL(i);
					r = pairList.getR(i);
					
					for(Set<Number> tempSet:treeMap.values()){
						if(tempSet.contains(l) && tempSet.contains(r)){
							p1++;
							flag = true;
							break;
						}
					}
					
					if(!flag){
						p2++;
					}
					
				}//for
				fileNum++;
				sb.append(fileNum).append("\t").append((double)p1/(p2+p1)).append("\t").append((double)p2/(p2+p1)).append("\r\n");
			}
		}//for
		String dstFile = srcDir+"as-rel-stat\\p2p-rate.txt";
		ft.write(sb, dstFile);
	}
	
	
	
	public void p2pBetweenness(){
		
		String prefix = "c2p";
		
		
		String srcFile = "./data/p2p/201308-"+prefix+".txt";
		String nodeBetweennessFile = "./data/p2p/201308-node-betweenness.txt";
		String nodeTransitDegreeFile = "./data/p2p/201308-node-transit-degree.txt";
		String edgeBetweennessFile = "./data/p2p/201308-edge-betweenness.txt";
		String coreNodeFile = "./data/p2p/201308-clique.txt";
		
		String dstFile = null;
		FileTool ft = new FileTool();
		Set<Pair<Number>> edgeSet = ft.read2SetPair(srcFile, NumberType.INTEGER);
		Map<Number, Number> nodeBetweennessMap = ft.read2Map(nodeBetweennessFile, NumberType.INTEGER, NumberType.DOUBLE);
		Map<Number, Number> nodeTransitDegreeMap = ft.read2Map(nodeTransitDegreeFile, NumberType.INTEGER, NumberType.DOUBLE);
		Map<Pair<Number>, Number> edgeBetweennessMap = ft.read2MapPair(edgeBetweennessFile, NumberType.INTEGER);
		Set<Number> coreNodeSet = ft.read2Set(coreNodeFile);
		
		
		StringBuffer sb = new StringBuffer();
		StringBuffer sb1 = new StringBuffer();
		StringBuffer sb2 = new StringBuffer();
		StringBuffer sb3 = new StringBuffer();
		Number l = null;
		Number r = null;
		for(Pair<Number> p:edgeSet){
			l = p.getL();
			r = p.getR();
			if(edgeBetweennessMap.get(p) != null){
				double l2 = 0;
				double l3 = 0;
				if(nodeBetweennessMap.get(l) != null){
					l2 = nodeBetweennessMap.get(l).doubleValue();
					l3 = nodeTransitDegreeMap.get(l).doubleValue();
				}
				double r2 = 0;
				double r3 = 0;
				
				double l4 = 0;
				double l5 = 0;
				if(nodeBetweennessMap.get(r) != null){
					r2 = nodeBetweennessMap.get(r).doubleValue();
					r3 = nodeTransitDegreeMap.get(r).doubleValue();
					
					l4 = l2/r2;
					if(l4 < 1){
						l4 = 1/l4;
					}
					l5 = l3/r3;
					if(l5 < 1){
						l5 = 1/l5;
					}
				}else{
					l4 = -1;
					l5 = -1;
				}
				
				
				if(coreNodeSet.contains(l) & coreNodeSet.contains(r)){
					sb1.append(l).append("\t").append(r).append("\t");
					sb1.append(edgeBetweennessMap.get(p)).append("\t");
					sb1.append(l2).append("\t");
					sb1.append(r2).append("\t");
					sb1.append(l4).append("\t");
					sb1.append(l3).append("\t");
					sb1.append(r3).append("\t");
					sb1.append(l5).append("\r\n");
				}else if(!coreNodeSet.contains(l) & !coreNodeSet.contains(r)){
					sb2.append(l).append("\t").append(r).append("\t");
					sb2.append(edgeBetweennessMap.get(p)).append("\t");
					sb2.append(l2).append("\t");
					sb2.append(r2).append("\t");
					sb2.append(l4).append("\t");
					sb2.append(l3).append("\t");
					sb2.append(r3).append("\t");
					sb2.append(l5).append("\r\n");
				}else{
					sb3.append(l).append("\t").append(r).append("\t");
					sb3.append(edgeBetweennessMap.get(p)).append("\t");
					sb3.append(l2).append("\t");
					sb3.append(r2).append("\t");
					sb3.append(l4).append("\t");
					sb3.append(l3).append("\t");
					sb3.append(r3).append("\t");
					sb3.append(l5).append("\r\n");
				}
				sb.append(l).append("\t").append(r).append("\t");
				sb.append(edgeBetweennessMap.get(p)).append("\t");
				sb.append(l2).append("\t");
				sb.append(r2).append("\t");
				sb.append(l4).append("\t");
				sb.append(l3).append("\t");
				sb.append(r3).append("\t");
				sb.append(l5).append("\r\n");
			}
			
		}
		dstFile = "./data/p2p/201308-"+prefix+"-all-betweenness-trasit-rate.txt";
		ft.write(sb, dstFile);
		dstFile = "./data/p2p/201308-"+prefix+"-intra-betweenness-trasit-rate.txt";
		ft.write(sb1, dstFile);
		dstFile = "./data/p2p/201308-"+prefix+"-inter-betweenness-trasit-rate.txt";
		ft.write(sb2, dstFile);
		dstFile = "./data/p2p/201308-"+prefix+"-outer-betweenness-trasit-rate.txt";
		ft.write(sb3, dstFile);
		
	}
	
	
	public void PathInfo(){
		
		String srcFile = "F:\\data\\path\\2013\\201308";
		String nodeBetweennessFile = "./data/p2p/201308-node-betweenness.txt";
		String nodeTransitDegreeFile = "./data/p2p/201308-node-transit-degree.txt";
		String edgeBetweennessFile = "./data/p2p/201308-edge-betweenness.txt";
		String p2pEdgeFile = "./data/p2p/201308-p2p.txt";
		String coreNodeFile = "./data/p2p/201308-clique.txt";
		
		String dstFile = null;
		String dstDir = "./data/p2p/201308/";
		FileTool ft = new FileTool();
		Set<Pair<Number>> p2pEdgeSet = ft.read2SetPair(p2pEdgeFile, NumberType.INTEGER);
		Map<Number, Number> nodeBetweennessMap = ft.read2Map(nodeBetweennessFile, NumberType.INTEGER, NumberType.DOUBLE);
		Map<Number, Number> nodeTransitDegreeMap = ft.read2Map(nodeTransitDegreeFile, NumberType.INTEGER, NumberType.DOUBLE);
		Map<Pair<Number>, Number> edgeBetweennessMap = ft.read2MapPair(edgeBetweennessFile, NumberType.INTEGER);
		Set<Number> coreNodeSet = ft.read2Set(coreNodeFile);
		Map<Integer, Integer> indexMap = new HashMap<Integer, Integer>();
		File[] fileArr = ft.fileArr(srcFile);
		StringBuffer flagStr = new StringBuffer();
		for (int i = 0; i < fileArr.length; i++) {
			String name = fileArr[i].getName();
			String line = null;
			String[] lineArr= null;
			BufferedReader reader;
			D.p(i);
			try {
				reader = new BufferedReader(new FileReader(fileArr[i]));
				StringBuffer sb = new StringBuffer();
				while((line = reader.readLine())!= null){
					boolean flag = false;
					//D.p(line);
					lineArr = line.split("\\s+");
					int l = 0;
					int r = 0;
					l = Integer.parseInt(lineArr[0]);
					
					sb.append(l).append("\t").append(nodeBetweennessMap.get(l)).append("\t");
					if(coreNodeSet.contains(l)){
						flagStr.append(1+"\t");
						flag = true;
						if(indexMap.containsKey(1)){
							indexMap.put(1, indexMap.get(1)+1);
						}else{
							indexMap.put(1, 1);
						}
					}
					for (int j = 1; j < lineArr.length; j++) {
						r = Integer.parseInt(lineArr[j]);
						if(coreNodeSet.contains(r)){
							flagStr.append(j+"\t");
							flag = true;
							if(indexMap.containsKey(j)){
								indexMap.put(j, indexMap.get(j)+1);
							}else{
								indexMap.put(j, 1);
							}
						}
						
						Pair<Number> p = new Pair<Number>(l,r, true);
						Pair<Number> p2 = new Pair<Number>(l,r);
						if(edgeBetweennessMap.containsKey(p)){
							sb.append(edgeBetweennessMap.get(p)).append("\t");
						}

						if(p2pEdgeSet.contains(p)){
							sb.append(0).append("\t");
						}else{
							sb.append(1).append("\t");
						}
						
						sb.append(r).append("\t");
						if(nodeBetweennessMap.containsKey(r)){
							sb.append(nodeBetweennessMap.get(r)).append("\t");
						}else{
							sb.append(0).append("\t");
						}
						
						l = r;
					}//for
					if(!flag){
						flagStr.append(-1);
						if(indexMap.containsKey(-1)){
							indexMap.put(-1, indexMap.get(-1)+1);
						}else{
							indexMap.put(-1, 1);
						}
					}
					flagStr.append("\r\n");
					sb.append("\r\n");
				}
				dstFile = dstDir+name;
				ft.write(sb, dstFile);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			

		}//for
		dstFile = dstDir+"index.txt";
		D.p(indexMap);
		ft.write(flagStr, dstFile);
	}

}



