package me.mervin.project.asRank.evolution;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import me.mervin.core.Global.NumberType;
import me.mervin.util.D;
import me.mervin.util.FileTool;
import me.mervin.util.PairList;


 /**
 *   CoreNodeEvolution.java
 *    
 *  @author Mervin.Wong  DateTime 2014年3月25日 下午4:43:27    
 *  @version 0.4.0
 */
public class CoreNodeEvolution {
	
	
	public static void main(String[] args){
		CoreNodeEvolution cne = new CoreNodeEvolution();
		//cne.CoreNodeLifeTime();
		cne.coreNodeIndex();
	}
	
	/*
	 * 核心节点的生命期
	 */
	public void CoreNodeLifeTime(){
		
		String line = null;
		String[] lineArr = null;
		Map<Number, LinkedList<Number>> cliqueList = new HashMap<Number, LinkedList<Number>>();
		Map<Number, Number> numberMap = new HashMap<Number,Number>();
		Set<Number> nodeSet =  null;
		FileTool ft = new FileTool();
		
		PairList<Number, Number> p = new PairList<Number, Number>();
		String srcDir = "E:\\data\\as-relationship\\";
		String srcFile = null;
		String dstDir = "E:\\data\\as-rel-coreNode\\";
		String dstFile = null;
		String date = null;
		
		nodeSet = ft.read2Set(dstDir+"coreNode.txt");
		numberMap = ft.read2Map(dstDir+"nodeMap.txt");
		int i = 1;
		for(Number nodeId:nodeSet){
			cliqueList.put(nodeId, new LinkedList<Number>());
			//numberMap.put(nodeId, i);
			i++;
		}
		i = 1;
		for(int y = 1998; y <= 2013; y++){
			for(int m = 1; m <= 12; m++){
				if(m < 10){
					date = y+"0"+m+"01";
				}else{
					date = y+""+m+"01";
				}
				D.p(date);
				srcFile = srcDir+date+".as-rel.txt";
				if(y == 2013 & m >8 ){
					break;
				}
				if(new File(srcFile).exists()){
					try {
						nodeSet = new HashSet<Number>();
						BufferedReader reader = new BufferedReader(new FileReader(srcFile));
						while((line = reader.readLine())!= null){
							if(line.charAt(0) == '#'){
								if(line.contains("clique:")){
									line = line.substring(line.indexOf(":")+1).trim();
									lineArr = line.split("\\s+");
									for(String str:lineArr){
										nodeSet.add(Integer.parseInt(str));
										//cliqueList.get(Integer.parseInt(str)).add(1);
										p.add(i, numberMap.get(Integer.parseInt(str)));
										
									}
								}
							}
						}
						dstFile = dstDir+date+"\\core-node.txt";
						ft.write(nodeSet, dstFile);
						
						for(Number tempId:cliqueList.keySet()){
							if(nodeSet.contains(tempId)){
								cliqueList.get(tempId).add(1);
							}else{
								//cliqueList.get(tempId).add(0);
							}
						}
						
					} catch (FileNotFoundException e) {
						// TODO 自动生成的 catch 块
						e.printStackTrace();
					} catch (IOException e) {
						// TODO 自动生成的 catch 块
						e.printStackTrace();
					}
					i++;
				}else{
					for(Number tempId:cliqueList.keySet()){
						//cliqueList.get(tempId).add(1);
					}
					D.p("File Not Exist!");
				}
				D.p("###################");
				
			}
			
		}//for
		
		dstFile = dstDir+"coreNodeLifeTime.txt";
		StringBuffer sb = new StringBuffer();
		Number temp = null;
		for(Number node:cliqueList.keySet()){
			sb.append(node).append("\t");
			LinkedList ll = cliqueList.get(node);
//			for(Iterator<Number> it = ll.iterator();it.hasNext();){
//				temp = it.next();
//				sb.append(temp).append("\t");
//			}
			sb.append(ll.size());
			sb.append("\r\n");
		}
		ft.write(p, dstFile);
		
//		dstFile = dstDir+"nodeSize.txt";
//		ft.write(sb, dstFile);
//		dstFile = dstDir+"nodeMap.txt";
//		ft.write(numberMap, dstFile);

	}


	public void coreNodeIndex(){
		//度、聚类系数、核、Customer和peer
		String srcDir = "E:\\data\\";
		String coreNodeSrcFile ="";
		String degreeSrcFile = "";
		String ccSrcFile = "";
		String coreSrcFile = "";
		String srcFile = "";
		String peerSrcFile = "";
		
		String dstFile = "";
		
		
		FileTool ft = new FileTool();
		Thread t = null;
		String date = null;
		
		for(int y = 1998; y <= 2013; y++){
			for(int m = 1; m <= 12; m++){
				//y = 2000;
				//m = 5;
				if(m < 10){
					date = y+"0"+m+"01";
				}else{
					date = y+""+m+"01";
				}
				D.p(date);
				coreNodeSrcFile = srcDir+"as-rel-res\\"+date+"\\core-node.txt";
				
				if(ft.isExist(coreNodeSrcFile)){
					StringBuffer sb = new StringBuffer();
					Set<Number> coreNodeSet = ft.read2Set(coreNodeSrcFile);
					degreeSrcFile = srcDir+"degree\\"+date+"-degree.txt";
					Map<Number, Number> degreeMap = ft.read2Map(degreeSrcFile);
					ccSrcFile = srcDir+"as-rel-cc\\"+date+"-cc.txt";
					Map<Number, Number> ccMap = ft.read2Map(ccSrcFile, NumberType.INTEGER, NumberType.DOUBLE);
					coreSrcFile = srcDir+"core-undirected\\"+date+"-core.txt";
					Map<Number, Number> coreMap = ft.read2Map(coreSrcFile);
					
					
					srcFile = srcDir+"as-relationship\\"+date+".as-rel.txt";
					Map<Number, Number> cusMap = new HashMap<Number, Number>();
					peerSrcFile = srcDir+"as-rel-degree\\"+date+"-peer.txt";
					Map<Number, Number> peerMap =  new HashMap<Number, Number>();
					String line = null;
					String[] lineArr = null;
					try {
						BufferedReader reader = new BufferedReader(new FileReader(srcFile));
						while((line = reader.readLine())!= null){
							if(line.charAt(0) != '#'){
								lineArr = line.split("\\|");
								int l = Integer.parseInt(lineArr[0]);
								int r = Integer.parseInt(lineArr[1]);
								if(lineArr[2].equals("-1")){
									if(coreNodeSet.contains(l)){
										if(cusMap.containsKey(l)){
											cusMap.put(l, cusMap.get(l).intValue()+1);
										}else{
											cusMap.put(l, 1);
										}
									}
									
								}else{
									if(coreNodeSet.contains(l)){
										if(peerMap.containsKey(l)){
											peerMap.put(l, peerMap.get(l).intValue()+1);
										}else{
											peerMap.put(l, 1);
										}
									}
									if(coreNodeSet.contains(r)){
										if(peerMap.containsKey(r)){
											peerMap.put(r, peerMap.get(r).intValue()+1);
										}else{
											peerMap.put(r, 1);
										}
									}
								}
							}
						}
					} catch (FileNotFoundException e) {
						// TODO 自动生成的 catch 块
						e.printStackTrace();
					} catch (IOException e) {
						// TODO 自动生成的 catch 块
						e.printStackTrace();
					}
					
					
					
					for(Number temp:coreNodeSet){
						sb.append(temp).append("\t");
						sb.append(degreeMap.get(temp)).append("\t");
						sb.append(ccMap.get(temp)).append("\t");
						sb.append(coreMap.get(temp)).append("\t");
						sb.append(cusMap.get(temp)).append("\t");
						sb.append(peerMap.get(temp)).append("\r\n");
					}
					dstFile = srcDir+"core-node\\"+date+".txt";
					ft.write(sb, dstFile);
				}
				D.p("###################");
			}
		}
		
		
		
	}
	
	
	
	
}
