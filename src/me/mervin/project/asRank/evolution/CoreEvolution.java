package me.mervin.project.asRank.evolution;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import me.mervin.core.Network;
import me.mervin.core.Global.NetType;
import me.mervin.module.feature.Coreness;
import me.mervin.util.D;
import me.mervin.util.FileTool;
import me.mervin.util.Log;
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
		ExecutorService pool = Executors.newFixedThreadPool(8);
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
		
		pool.shutdown();
		
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
		if(new File(srcFile).exists()){
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
