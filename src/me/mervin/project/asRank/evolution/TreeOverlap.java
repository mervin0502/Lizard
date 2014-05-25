package me.mervin.project.asRank.evolution;

import java.io.File;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import me.mervin.core.Global.NumberType;
import me.mervin.core.Network;
import me.mervin.util.D;
import me.mervin.util.FileTool;
import me.mervin.util.Pair;
import me.mervin.util.PairList;

public class TreeOverlap {
	FileTool ft = new FileTool();

	public static void main(String[] args){
		TreeOverlap to = new TreeOverlap();
		String srcDir = "E:\\data\\as-rel-tree\\tree\\";
		String srcFile = null;
		String dstDir = "E:\\data\\as-rel-tree\\overlap_1\\";
		String dstFile = null;
		String date = null;
		PairList<Number, Number> edgeList = null;
		Network net = null;
		FileTool ft = new FileTool();
		Thread t = null;
		for(int y = 1998; y <= 2013; y++){
			for(int m = 1; m <= 1; m++){
				//y = 2000;
				//m = 5;
				m = 8;
				if(m < 10){
					date = y+"0"+m+"01";
				}else{
					date = y+""+m+"01";
				}
				D.p(date);
				srcFile = srcDir+date+"\\";
				if(ft.isExist(srcFile)){
					to.Overlap(new File(srcFile), dstDir);
				}
				D.p("###################");
				//System.exit(0);
			}
		}
	}
	
	@SuppressWarnings("null")
	public void Overlap(File treeDir, String dstDir){
		File[] fileArr = ft.fileArr(treeDir, "txt");
		String dstFile = null;
		Set<Pair<Number>> preEdgeSet = null;
		Set<Pair<Number>> postEdgeSet = null;
		
		
		Set<Number> preNodeSet = null;
		
		Set<Number> postNodeSet = null;
		
		Map<Number, Number> preLevelMap = null;
		Map<Number, Number> postLevelMap = null;
		Set<Pair<Number>> allOverlapEdgeSet = null;
		for(int i = 0; i < fileArr.length; i++){
			preEdgeSet = ft.read2SetPair(fileArr[i].getAbsolutePath(), NumberType.INTEGER);
			preNodeSet = new HashSet<Number>();
			for(Pair<Number> p: preEdgeSet){
				Number l = p.getL();
				Number r = p.getR();
				if(!preNodeSet.contains(l)){
					preNodeSet.add(l);
				}
				if(!preNodeSet.contains(r)){
					preNodeSet.add(r);
				}
			}
			allOverlapEdgeSet = preEdgeSet;
			
			preLevelMap = ft.read2Map("E:\\data\\as-rel-res\\"+treeDir.getName()+"\\"+fileArr[i].getName().substring(0,fileArr[i].getName().indexOf('-'))+"-level.txt");
			
			for(int j = i+1; j < fileArr.length; j++){
				postEdgeSet = ft.read2SetPair(fileArr[j].getAbsolutePath(), NumberType.INTEGER);
				postNodeSet = new HashSet<Number>();
				for(Pair<Number> p: postEdgeSet){
					Number l = p.getL();
					Number r = p.getR();
					if(!postNodeSet.contains(l)){
						postNodeSet.add(l);
					}
					if(!postNodeSet.contains(r)){
						postNodeSet.add(r);
					}
				}
				
				postLevelMap = ft.read2Map("E:\\data\\as-rel-res\\"+treeDir.getName()+"\\"+fileArr[j].getName().substring(0,fileArr[j].getName().indexOf('-'))+"-level.txt");

				/*
				 * 对比两个文件
				 */
				StringBuffer preOuterEdge = new StringBuffer();
				StringBuffer postOuterEdge = new StringBuffer();
				StringBuffer preBorderEdge = new StringBuffer();
				StringBuffer postBorderEdge = new StringBuffer();
				StringBuffer interEdge = new StringBuffer();
				
				for(Pair<Number> p:preEdgeSet){
					Number l = p.getL();
					Number r = p.getR();
					if(postNodeSet.contains(l) && postNodeSet.contains(r)){
						interEdge.append(l).append("\t").append(r).append("\r\n");
					}
					if(!postNodeSet.contains(l) && postNodeSet.contains(r)){
						preBorderEdge.append(l).append("\t").append(r).append("\t");
						preBorderEdge.append(preLevelMap.get(l)).append("\t").append(preLevelMap.get(r)).append("\t");
//<<<<<<< HEAD
//						preBorderEdge.append(Math.abs(preLevelMap.get(l).intValue()-preLevelMap.get(r).intValue())).append("\r\n");
						preBorderEdge.append(preLevelMap.get(l).intValue()-preLevelMap.get(r).intValue()).append("\r\n");
					}
					if(!postNodeSet.contains(l) && !postNodeSet.contains(r)){
						preOuterEdge.append(l).append("\t").append(r).append("\r\n");
					}
/*					if(postNodeSet.contains(l) && !postNodeSet.contains(r)){
						D.p(l+"##"+r);
					}*/
				}
				for(Pair<Number> p:postEdgeSet){
					Number l = p.getL();
					Number r = p.getR();
					/*if(preEdgeSet.contains(p.getL()) && preEdgeSet.contains(p.getR())){
						interEdge.append(l).append("\t").append(r).append("\r\n");
					}*/
					if(!preNodeSet.contains(l) && preNodeSet.contains(r)){
						postBorderEdge.append(l).append("\t").append(r).append("\t");
						postBorderEdge.append(postLevelMap.get(l)).append("\t").append(postLevelMap.get(r)).append("\t");
//						postBorderEdge.append(Math.abs(postLevelMap.get(l).intValue()-postLevelMap.get(r).intValue())).append("\r\n");
						postBorderEdge.append(postLevelMap.get(l).intValue()-postLevelMap.get(r).intValue()).append("\r\n");
/*=======
						preBorderEdge.append(Math.abs(preLevelMap.get(l).intValue()-preLevelMap.get(r).intValue())).append("\r\n");
					}
					if(!postNodeSet.contains(l) && !postNodeSet.contains(r)){
						preOuterEdge.append(l).append("\t").append(r).append("\r\n");
					}
					if(postNodeSet.contains(l) && !postNodeSet.contains(r)){
						D.p(l+"##"+r);
					}
				}
				for(Pair<Number> p:postEdgeSet){
					Number l = p.getL();
					Number r = p.getR();
					if(preEdgeSet.contains(p.getL()) && preEdgeSet.contains(p.getR())){
						interEdge.append(l).append("\t").append(r).append("\r\n");
					}
					if(!preNodeSet.contains(l) && preNodeSet.contains(r)){
						postBorderEdge.append(l).append("\t").append(r).append("\t");
						postBorderEdge.append(postLevelMap.get(l)).append("\t").append(postLevelMap.get(r)).append("\t");
						postBorderEdge.append(Math.abs(postLevelMap.get(l).intValue()-postLevelMap.get(r).intValue())).append("\r\n");
>>>>>>> branch 'master' of http://git.oschina.net/mervin/Lizard.git
*/					}
					if(!preNodeSet.contains(l) && !preNodeSet.contains(r)){
						postOuterEdge.append(l).append("\t").append(r).append("\r\n");
					}
					
/*					if(preNodeSet.contains(l) && !preNodeSet.contains(r)){
						D.p(l+"##"+r);
					}*/
				}
				
				for(Iterator<Pair<Number>> it = allOverlapEdgeSet.iterator(); it.hasNext();){
					Pair<Number> p = it.next();
					if(!postEdgeSet.contains(p)){
						it.remove();
					}
				}
				String prefix = fileArr[i].getName();
				prefix = prefix.substring(0,prefix.indexOf('-'));
				String post = fileArr[j].getName();
				post = post.substring(0,post.indexOf('-'));
				dstFile = dstDir+treeDir.getName()+"\\"+prefix+"-"+post+"-preOuter.txt";
				ft.write(preOuterEdge, dstFile);
				dstFile = dstDir+treeDir.getName()+"\\"+prefix+"-"+post+"-postOuter.txt";
				ft.write(postOuterEdge, dstFile);
				dstFile = dstDir+treeDir.getName()+"\\"+prefix+"-"+post+"-preBorder.txt";
				ft.write(preBorderEdge, dstFile);
				dstFile = dstDir+treeDir.getName()+"\\"+prefix+"-"+post+"-postBorder.txt";
				ft.write(postBorderEdge, dstFile);
				dstFile = dstDir+treeDir.getName()+"\\"+prefix+"-"+post+"-inter.txt";
				ft.write(interEdge, dstFile);
				if(i == 0){
					dstFile = dstDir+treeDir.getName()+"\\inter.txt";
					ft.write(allOverlapEdgeSet, dstFile);
				}
				//D.p(dstFile);
			}//for

		}//for
		
	}
}
