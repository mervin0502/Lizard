package me.mervin.project.asRank.evolution;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import me.mervin.core.Network;
import me.mervin.core.Global.NetType;
import me.mervin.core.Global.NumberType;
import me.mervin.module.feature.Degree;
import me.mervin.util.D;
import me.mervin.util.FileTool;
import me.mervin.util.Log;
import me.mervin.util.Pair;
import me.mervin.util.PairList;


 /**
 *   P2P.java
 *    P2P连接偏好性
 *  @author Mervin.Wong  DateTime 2014年3月24日 下午2:04:54    
 *  @version 0.4.0
 */
public class P2P {

	/**
	 *  
	 *  @param args
	 */
	public static void main(String[] args) {
		// TODO 自动生成的方法存根
		/*String srcDir = "E:\\data\\as-relationship\\";
		String srcFile = null;
		String dstDir = "E:\\data\\as-rel-p2p\\";
		String dstFile = null;
		String date = null;
		PairList<Number, Number> edgeList = null;
		Network net = null;
		FileTool ft = new FileTool();
		Degree degree = new Degree();
		P2P pp = new P2P();
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
				srcFile = srcDir+date+".as-rel.txt";
				dstFile = dstDir+date+"-p2p.txt";
				if(new File(srcFile).exists()){
					ft.write(pp.extractP2P(srcFile), dstFile);
				}else{
					Log.a(srcFile, dstDir+"file_noexist.txt");
				}
				D.p("###################");
			}
		}*/
		
		
		P2P pp = new P2P();
		pp.P2PDist();
	}
	
	
	public PairList<Number, Number> extractP2P(String srcFile){
		PairList<Number, Number> edgeList = new PairList<Number, Number>();
		
		String line = null;
		String[] lineArr = null;
		try {
			BufferedReader reader = new BufferedReader(new FileReader(srcFile));
			while((line = reader.readLine())!= null){
				if(line.charAt(0) != '#'){
					lineArr = line.split("\\|");
					int l = Integer.parseInt(lineArr[0]);
					int r = Integer.parseInt(lineArr[1]);
					if(lineArr[2].equals("0")){
						edgeList.add(l, r);
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
		return edgeList;
	}
	
	
	//inter intra  outer
	public void P2PDist(){
		//度、聚类系数、核、Customer和peer
		String srcDir = "E:\\data\\";
		String coreNodeSrcFile ="";
		String peerSrcFile = "";
		
		String dstFile = "";
		
		
		FileTool ft = new FileTool();
		String date = null;
		int i = 0;
		StringBuffer sb = new StringBuffer();
		StringBuffer sb1 = new StringBuffer();
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
				D.p(coreNodeSrcFile);
				if(ft.isExist(coreNodeSrcFile)){
					i++;
					
					Set<Number> coreNodeSet = ft.read2Set(coreNodeSrcFile);
					
					peerSrcFile = srcDir+"as-rel-p2p\\"+date+"-p2p.txt";
					D.p(peerSrcFile);
					Set<Pair<Number>> peerSetPair =  ft.read2SetPair(peerSrcFile, NumberType.INTEGER);
					
					Set<Pair<Number>> intraSetPair = new HashSet<Pair<Number>>();
					Set<Pair<Number>> interSetPair = new HashSet<Pair<Number>>();
					Set<Pair<Number>> outerSetPair = new HashSet<Pair<Number>>();
					
					for(Pair<Number> p:peerSetPair){
						Number l = p.getL();
						Number r = p.getR();
						
						if(coreNodeSet.contains(l) && coreNodeSet.contains(r)){
							intraSetPair.add(p);
						}else if(!coreNodeSet.contains(l) && !coreNodeSet.contains(r)){
							outerSetPair.add(p);
						}else{
							interSetPair.add(p);
						}
					}
					D.p(interSetPair.size()+intraSetPair.size()+outerSetPair.size()+"##"+peerSetPair.size());
					sb.append(date).append("\t");
					sb.append(i).append("\t");
					sb.append((double)intraSetPair.size()/peerSetPair.size()).append("\t");
					sb.append((double)interSetPair.size()/peerSetPair.size()).append("\t");
					sb.append((double)outerSetPair.size()/peerSetPair.size()).append("\r\n");
					sb1.append(date).append("\t");
					sb1.append(i).append("\t");
					sb1.append(intraSetPair.size()).append("\t");
					sb1.append(interSetPair.size()).append("\t");
					sb1.append(outerSetPair.size()).append("\r\n");
				}
				D.p("###################");
			}
		}
		
		dstFile = srcDir+"p2p-rate.txt";
		ft.write(sb, dstFile);
		dstFile = srcDir+"p2p-dist.txt";
		ft.write(sb1, dstFile);
		
	}

}
