package me.mervin.project.asRank.evolution;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import me.mervin.core.Network;
import me.mervin.core.Global.NetType;
import me.mervin.module.feature.Coreness;
import me.mervin.util.D;
import me.mervin.util.FileTool;
import me.mervin.util.Log;
import me.mervin.util.MapTool;
import me.mervin.util.MathTool;
import me.mervin.util.PairList;


 /**
 *   CoreEvolution.java
 *    
 *  @author Mervin.Wong  DateTime 2014年3月24日 上午10:03:20    
 *  @version 0.4.0
 */
public class CoreEvolution {

	/**
	 *  
	 *  @param args
	 */
	public static void main(String[] args) {
		// TODO 自动生成的方法存根
/*		ExecutorService pool = Executors.newFixedThreadPool(2);
		CoreEvolution ce = new CoreEvolution();
		String srcDir = "E:\\data\\as-relationship\\";
		String srcFile = null;
		String dstDir = "E:\\data\\as-rel-core\\";
		String dstFile = null;
		String date = null;
		PairList<Number, Number> edgeList = null;
		Network net = null;
		Coreness core = new Coreness();
		FileTool ft = new FileTool();
		Thread t = null;
		for(int y = 2004; y <= 2013; y++){
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
				t = new CoreCalculate(srcFile, dstDir, date);
				pool.execute(t);
				D.p("###################");
			}
		}
		pool.shutdown();*/
		
		CoreEvolution ce = new CoreEvolution();
		ce.maxCore();
		ce.coreFreq();
	}
	

	public void maxCore(){
		String srcDir = "E:\\data\\core-undirected\\";
		String srcFile = null;
		String dstDir = "E:\\data\\";
		String dstFile = null;
		String date = null;
		PairList<Number, Number> edgeList = null;
		Network net = null;
		Coreness core = new Coreness();
		FileTool ft = new FileTool();
		MapTool mt = new MapTool();
		Map<Number, Number>  coreMap = null;
		StringBuffer sb = new StringBuffer();
		StringBuffer sb2 = new StringBuffer();
		int i = 0;
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
				srcFile = srcDir+date+"-core.txt";
				coreMap = new HashMap<Number, Number>();
				if(ft.isExist(srcFile)){
					i++;
					coreMap = ft.read2Map(srcFile);
					sb.append(i).append("\t").append(mt.sort(coreMap, false, false).getR(0)).append("\r\n");
					
					sb2.append(i).append("\t").append(MathTool.average(MathTool.frequency(coreMap))).append("\r\n");
				}
				D.p("###################");
			}
		}
		//dstFile = dstDir+"core-undirected-max-evolution.txt";
		//ft.write(sb, dstFile);
		dstFile = dstDir+"core-undirected-avg-evolution.txt";
		ft.write(sb2, dstFile);
	}
	
	
	public void coreFreq(){
		String srcDir = "E:\\data\\core-undirected\\";
		String srcFile = null;
		String dstDir = "E:\\data\\";
		String dstFile = null;
		String date = null;
		PairList<Number, Number> edgeList = null;
		Network net = null;
		Coreness core = new Coreness();
		FileTool ft = new FileTool();
		MapTool mt = new MapTool();
		Map<Number, Number>  coreMap = null;
		StringBuffer sb = new StringBuffer();
		int i = 0;
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
				srcFile = srcDir+date+"-core.txt";
				coreMap = new HashMap<Number, Number>();
				if(ft.isExist(srcFile)){
					i++;
					D.p(i);
					coreMap = ft.read2Map(srcFile);
					Map<Number, Number> rate = MathTool.ratio(MathTool.frequency(coreMap));
					dstFile = dstDir+"core\\rate\\"+date+"-core-rate.txt";
					ft.write(rate, dstFile);
					for(Entry<Number, Number> e: rate.entrySet()){
						sb.append(i).append("\t").append(e.getKey()).append("\t").append(e.getValue()).append("\r\n");
					}
				}
				D.p("###################");
			}
		}
		dstFile = dstDir+"core\\core-undirected-rate-evolution.txt";
		ft.write(sb, dstFile);
	}
}

class CoreCalculate extends Thread{
	private String srcFile = null;
	private String dstDir = null;
	private String date = null;
	private String dstFile = null;
	
	CoreCalculate(String srcFile, String dstDir, String date){
		this.srcFile = srcFile;
		this.dstDir = dstDir;
		this.date = date;
	}
	public void run(){
		PairList<Number, Number> edgeList = null;
		dstFile = dstDir+date+"-core.txt";
		Network net = null;
		Coreness core = new Coreness();
		FileTool ft = new FileTool();
		if(new File(srcFile).exists() && ! new File(dstFile).exists()){
			edgeList = this.readFile(srcFile);
			net = new Network(edgeList, NetType.DIRECTED);
			D.p("Create Net:"+date+" done");
			ft.write(core.nodeCore(net, net.getAllNodeId()), dstFile);
			D.p("Net Core:"+date+" done");
		}else{
			Log.a(srcFile, dstDir+"file_noexist.txt");
		}
		D.p("###################");
	}
	public PairList<Number, Number> readFile(String srcFile){
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
					if(lineArr[2].equals("-1")){
						edgeList.add(l, r);
					}else{
						edgeList.add(l, r);
						edgeList.add(r, l);
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
}
