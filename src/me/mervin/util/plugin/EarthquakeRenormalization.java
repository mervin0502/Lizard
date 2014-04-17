package me.mervin.util.plugin;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import me.mervin.core.Global.NumberType;
import me.mervin.util.D;
import me.mervin.util.FileTool;
import me.mervin.util.Pair;
import me.mervin.util.PairList;

/**
 * 
 *  Class Name: NetRenormalization.java
 *  Function: 网络的重正化
 *  注：统一采用纬度 经度的顺序
 *  
 *     Modifications:   
 *  
 *  @author Mervin.Wong  DateTime 2013-5-26 下午6:28:10    
 *  @version 1.0
 */
public class EarthquakeRenormalization {

	
	
	/*
	 * *************************************************************
	 * 
	 * 
	 * *************************************************************
	 */
	/*
	 * 按月份分割文件
	 */
	public void splitFileByMonth(String file, String dstDir){
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line = null;
			String[] lineArr = null;
			String[] date = null;
			StringBuffer sb = new StringBuffer();
			String temp = null;
			
			FileTool f = new FileTool();
			
			while((line = reader.readLine()) != null){
				//D.p(line);
				lineArr = line.split("\t|\\s+");
				D.p(lineArr[0]+"####"+lineArr[1]+"####"+lineArr[2]);
				date = lineArr[0].split("-");
				if(temp != null){
					if(temp.equalsIgnoreCase(date[1])){
						sb.append(lineArr[1]).append("\t").append(lineArr[2]).append("\r\n");
					}else{
						f.write(sb, dstDir+date[0]+"-"+date[1]+".txt");
						temp = date[1];
						sb.delete(0, sb.length());
						sb.append(lineArr[1]).append("\t").append(lineArr[2]).append("\r\n");
					}
				}else{
					temp = date[1];
					sb.append(lineArr[1]).append("\t").append(lineArr[2]).append("\r\n");
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/*
	 * 基于经纬度的重正化 
	 * f,划分粒度
	 * 纬度：-90～90     => 0~180
	 * 经度：-180～180   => 0~360
	 * 序列：现经度后纬度
	 */
	public void renormalizationByLatLon(String srcFile, String dstFile, float f){
		int k = 1;
		Set<Number> set = new HashSet<Number>();
		try {
			@SuppressWarnings("resource")
			BufferedReader reader = new BufferedReader(new FileReader(srcFile));
			String line = null;
			String[] lineArr = null;
			StringBuffer sb = new StringBuffer();
			float lat, lon;
			int i, j, cur = 0;
			while((line = reader.readLine()) != null){
				lineArr = line.split("\t|\\s{1}");
				D.p(line);
				lat = Float.parseFloat(lineArr[0])+90;
				lon = Float.parseFloat(lineArr[1])+180;
				
				i = (int) Math.ceil(lon/f);//从1号开始
				j = (int) Math.floor(lat/f);
				cur = (int) (j*(Math.ceil(360/f))+i);
				set.add(cur);
				sb.append(cur).append("\t").append(lineArr[0]).append("\t").append(lineArr[1]).append("\r\n");
			}
			D.p(set.size());
			sb.insert(0, "#Node number:"+set.size()+"\r\n");
			new FileTool().write(sb, dstFile);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	/*
	 * 基于经纬度的重正化 取方块的中心点
	 * f,划分粒度
	 * 纬度：-90～90     => 0~180
	 * 经度：-180～180   => 0~360
	 * 序列：现经度后纬度
	 */
	public void renormalizationByLatLon2(String srcFile, String dstFile, float f){
		int k = 1;
//		float startLat = 26 , endLat = 35;//纬度
//		float startLog = 97,  endLog = 108;//经度
		Set<Number> set = new HashSet<Number>();
		try {
			@SuppressWarnings("resource")
			BufferedReader reader = new BufferedReader(new FileReader(srcFile));
			String line = null;
			String[] lineArr = null;
			StringBuffer sb = new StringBuffer();
			float lat, log;
			float i, j;
			while((line = reader.readLine()) != null){
				lineArr = line.split("\t|\\s{1}");
				D.p(line);
				lat = Float.parseFloat(lineArr[0]);
				log = Float.parseFloat(lineArr[1]);
				
				if(lat >= 0){
					i = lat - lat%f + f/2;
				}else{
					i = lat - lat%f - f/2;
				}
				if(log >= 0){
					j = log - log%f + f/2; 
				}else{
					j = log - log%f - f/2;
				}
				sb.append(lineArr[0]).append("\t").append(lineArr[1]).append("\t").append(i).append("\t").append(j).append("\r\n");
			}
			D.p(set.size());
			sb.insert(0, "#Node number:"+set.size()+"\r\n");
			new FileTool().write(sb, dstFile, false);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	/*
	 * 由索引号以及分割粒度取经纬度
	 */
	public void renormalizationByIndex(String srcFile, String dstFile, double f){
		Map<Number, double[]> index2LatLon = new HashMap<Number, double[]>(); //将索引号对应到经纬度
		double maxLat = Math.ceil(180/f);
		double maxLon = Math.ceil(360/f);
		int temp = 1;
		for(int i = 0; i< maxLat; i++){
			for(int j = 1; j <= maxLon; j++){
				double[] ll = new double[2];
				ll[0] = i*f+f/2-90;//纬度
				ll[1] = (j-1)*f+f/2-180;//经度
				index2LatLon.put(temp, ll);
				temp++;
			}
		}
		
		//读取文件中的索引号，并找到对应的纬度和经度
		StringBuffer sb = new StringBuffer();
		FileTool ft = new FileTool();
		List<Pair<Number>> net = ft.read2ListPair(srcFile, NumberType.INTEGER);
		Pair<Number> p = null;
		Number l, r;
		double[] ll ;
		for(Iterator<Pair<Number>> it = net.iterator(); it.hasNext();){
			p = it.next();
			l = p.getL();
			ll = index2LatLon.get(l);
			sb.append(ll[0]).append("\t").append(ll[1]).append("\t");
			
			r = p.getR();
			ll = index2LatLon.get(r);
			sb.append(ll[0]).append("\t").append(ll[1]).append("\r\n");
		}
		ft.write(sb, dstFile);
	}
	
	/**
	 * 转换为Gephi文件
	 * @param srcFile
	 * @param dstFile
	 * @param f 分割粒度
	 */
	public void convert2Gexf(String srcFile, String dstFile,  double f){
		Map<Pair<Double>, Integer> index2LatLon = new HashMap<Pair<Double>, Integer>();//经纬度与对应索引号的映射
	    PairList<Integer, Integer> edges = new PairList<Integer, Integer>();//边的集合
	    
	    /*
	     * 从文件中读取网络关系，保存边和节点
	     */
	    try {
			BufferedReader reader = new BufferedReader(new FileReader(srcFile));
			String line = null;
			String[] lineArr = null;
			int varIndex = 1, curIndexL = 1, curIndexR = 1;
			double lat1 = 0, lon1 = 0, lat2 =0, lon2 =0;
			Pair<Double> p = null;
			while((line = reader.readLine()) != null){
				if(line.charAt(0) != '#' && line.charAt(0) != '*' && line.charAt(0) != '%'){
					/*
					 * 获取一条边中两个端点的纬度和经度
					 */
					lineArr = line.split("\t|(\\s+)");
					//左端点
					lat1 = Double.parseDouble(lineArr[0]);
					lon1 = Double.parseDouble(lineArr[1]);
					p = new Pair<Double>(lat1, lon1);
					//纬度和经度 与  索引号 映射
					if(!index2LatLon.containsKey(p)){
						index2LatLon.put(p, varIndex);
						curIndexL = varIndex;
						varIndex++;
					}else{
						curIndexL = index2LatLon.get(p);
					}
					
					//右端点
					lat2 = Double.parseDouble(lineArr[2]);
					lon2 = Double.parseDouble(lineArr[3]);
					p = new Pair<Double>(lat2, lon2);
					//纬度和经度 与  索引号 映射
					if(!index2LatLon.containsKey(p)){
						index2LatLon.put(p, varIndex);
						curIndexL = varIndex;
						varIndex++;
					}else{
						curIndexR = index2LatLon.get(p);
					}
					
					/*
					 * 保存一条边的两个索引号
					 */
					edges.add(curIndexL, curIndexR);
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    /*
	     * 写入Gephi文件
	     */
	    StringBuffer sb = new StringBuffer();
	    Pair<Double> p = null;
	    //头部信息
	    sb.append("<graph defaultedgetype=\"undirected\" idtype=\"integer\" type=\"static\" timeformat=\"date\">");
	    sb.append("<attributes class=\"node\" mode=\"static\">");
	    sb.append("<attribute id=\"lat\" title=\"latitude\" type=\"double\" />");
	    sb.append("<attribute id=\"lon\" title=\"longitude\" type=\"double\" />");
	    sb.append("</attributes>"); 
	   
	    //节点信息
	    sb.append("<nodes count=\""+index2LatLon.size()+"\"");
	    for(Iterator<Pair<Double>> it = index2LatLon.keySet().iterator(); it.hasNext();){
	    	p = it.next();
	    	sb.append("<node id=\""+index2LatLon.get(p)+"\" label=\""+p.getL()+"#"+p.getR()+"\"");
	    	sb.append("<attvalues>");
	    	sb.append("<attvalue for=\"lat\" value=\""+p.getL()+"\"");
	    	sb.append("<attvalue for=\"lon\" value=\""+p.getR()+"\"");
	    	sb.append("</attvalues>");
	    	sb.append("</node>");
	    }
	    sb.append("</nodes>");
	    
	    //边的信息
	    int n = edges.size();
/*	    <edges>
	    <edge source="100" target="92"/>
	    <edge source="92" target="67"/>*/
	    sb.append("<edges>");
	    for(int i = 0; i < n; i++){
	    	sb.append("<edge source=\""+edges.getL(i)+"\" target=\""+edges.getR(i)+"\"");
	    }
	    sb.append("</edges>");
	    new FileTool().write(sb, dstFile);
	}
	
	
	/*
	 * 提取网络文件
	 */
	public void extractNet(String srcFile, String dstFile){
		int[] cols = {1};
		FileTool f = new FileTool();
		List<Number> list = f.read2List(srcFile, NumberType.INTEGER, cols);
		StringBuffer sb = new StringBuffer();
		for (int i = 1; i < list.size(); i++) {
			if(!list.get(i-1).equals(list.get(i))){
				sb.append(list.get(i-1)).append("\t").append(list.get(i)).append("\r\n");
			}
		}
		f.write(sb, dstFile);
		//EdgeDeweigh ed = new EdgeDeweigh("../data/Earthquake/zh-sc-net-f-0.125.txt", "../data/Earthquake/zh-sc-net-f-0.125-r.txt");
		//ed.run();
	}
	

	
	/*
	 *  Function:
	 * 
	 *  @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		EarthquakeRenormalization nr = new EarthquakeRenormalization();
		//nr.splitFileByMonth("../data/Earthquake/date_zh_sc.txt", "../data/Earthquake/split/");
		//nr.renormalizationByLatLon2((float) 0.75);
		//nr.extractNet();
		String root = "../data/Earthquake/";
		String srcFile = null;
		String dstFile = null;
		String temp = null;
		float f = (float) 0.25;
		File[] fileArr = new File(root+"split/").listFiles();
		for (int i = 0; i < fileArr.length; i++) {
			//File file = fileArr[i];
			try {
				srcFile = fileArr[i].getCanonicalPath();
				temp = root+"temp.txt";
				dstFile = root+"split2/"+f+"/"+fileArr[i].getName();
				nr.renormalizationByLatLon(srcFile, temp, f);
				nr.extractNet(temp, dstFile);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	

}
