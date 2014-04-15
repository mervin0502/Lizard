package me.mervin.project.asRank.evolution;
import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import me.mervin.core.Network;
import me.mervin.core.Global.NetType;
import me.mervin.module.feature.Degree;
import me.mervin.util.D;
import me.mervin.util.FileTool;
import me.mervin.util.Log;
import me.mervin.util.PairList;


 /**
 *   Degree.java
 *    
 *  @author Mervin.Wong  DateTime 2014年3月24日 下午2:05:28    
 *  @version 0.4.0
 */



public class NetDegreeEvoluation{
	
	public static void main(String[] args){
		ExecutorService pool = Executors.newFixedThreadPool(6);
		String srcDir = "E:\\data\\as-relationship\\";
		String srcFile = null;
		String dstDir = "E:\\data\\as-rel-degree2\\";
		String date = null;
		Thread t = null;
		for(int y = 2006; y <= 2013; y++){
			if(y == 2010){
				continue;
			}
			for(int m = 1; m <= 12; m++){
				if(m < 10){
					date = y+"0"+m+"01";
				}else{
					date = y+""+m+"01";
				}
				D.p(date);
				srcFile = srcDir+date+".as-rel.txt";
				t = new NetDegree(srcFile, dstDir, date);
				pool.execute(t);
				D.p("###################");
			}
		}
		
		pool.shutdown();
	}
}
class NetDegree extends Thread{
	private String srcFile = null;
	private String dstDir = null;
	private String date = null;
	public static void main(String[] args){
//		String srcDir = "E:\\data\\as-relationship\\";
//		String dstDir = "E:\\data\\as-rel-degree\\";
//		//NetDegree nd = new NetDegree();
//		nd.script(srcDir, dstDir);
	}
	public NetDegree(String srcFile, String dstDir, String date){
		this.srcFile = srcFile;
		this.dstDir = dstDir;
		this.date = date;
	}
	//网络的入度、出度
	public void run(){
		String dstFile = null;
		FileTool ft = new FileTool();
		Degree degree = new Degree();
		if(new File(srcFile).exists()){
			PairList<Number, Number> edgeList = new ReadFile().readFile(srcFile);
			Network net = new Network(edgeList, NetType.UNDIRECTED);
			D.p("Create Net:"+date+" done");
			//dstFile = dstDir+date+"-inDegree.txt";
			//ft.write(degree.nodeInDegree(net, net.getAllNodeId()), dstFile);
			//dstFile = dstDir+date+"-outDegree.txt";
			//ft.write(degree.nodeOutDegree(net, net.getAllNodeId()), dstFile);
			dstFile = dstDir+"\\degree\\"+date+"-degree.txt";
			ft.write(degree.nodeDegree(net, net.getAllNodeId()), dstFile);
			D.p("Net degree:"+date+" done");
		}else{
			Log.a(srcFile, dstDir+"file_noexist.txt");
		}
		D.p("###################");

	}
}
