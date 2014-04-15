package me.mervin.project.asRank.evolution;
import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import me.mervin.core.Global.NetType;
import me.mervin.util.D;
import me.mervin.util.EdgeDeweigh;


/**
 *   Test.java
 *    
 *  @author Mervin.Wong  DateTime 2014年3月23日 下午5:54:19    
 *  @version 0.4.0
 */
public class Test {

	public static void main(String[] args){
		Test t = new Test();
		t.t1();
		D.m();
	
	}
	
	public void t1(){
//		String srcFile = "E:\\Code\\java\\CNAT\\data\\t2.txt";
//		FileTool ft = new FileTool();
//		Tree t = new Tree(ft.read2PairList(srcFile, Tree.numberType));
//		t.root = t.getVertexById(1);
////		D.p(t);
//		TreeTool tt = new TreeTool(t); 
//		Queue<Number> path = new LinkedList<Number>();
////		tt.script(t.root.id, 0, path);
////		tt.script2();
//		tt.script3();
//		D.p("size:"+tt.sizeMap);
////		D.p("########");
////		D.p("level:"+tt.levelMap);
////		D.p("########");
////		D.p("parent:"+tt.parentMap);
////		D.p("########");
////		D.p("branch:"+tt.childrenMap);
////		D.p("########");
////		D.p("leaf:"+tt.leafSet);
////		D.p("########");
////		D.p(tt.h);
		ExecutorService pool = Executors.newFixedThreadPool(4);
//		ExecutorService pool = Executors.newSingleThreadExecutor();
		Thread he = null;
		String srcDir = "E:\\data\\as-rel-tree\\";
		String srcFile = null;
		String dstDir = "E:\\data\\as-rel-tree_1\\";
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
				//srcFile = srcDir+date+".as-rel.txt";
				//if(new File(srcFile).exists()){
					he = new edgeEweigh(srcDir, dstDir, date);
					pool.execute(he);
				//}else{
				//	Log.a(srcFile+"\r\n", dstDir+"file_noexist.txt");
				//}
				//break;
			}
			
		}//for
		pool.shutdown();
	}
}

class edgeEweigh extends Thread{
	private String srcDir = null;
	private String dstDir = null;
	private String date = null;
	
	/**
	 */
	public edgeEweigh(String srcDir, String dstDir, String date) {
		// TODO 自动生成的构造函数存根
		this.srcDir = srcDir;
		this.dstDir = dstDir;
		this.date = date;
	}
	public void run(){
		String srcFile = srcDir+date;
		File file = new File(srcFile);
		if(file.isDirectory()){
			File[] fileArr = file.listFiles();
			for (int i = 0; i < fileArr.length; i++) {
				String dstFile = dstDir+date+"\\"+fileArr[i].getName();
				new EdgeDeweigh(fileArr[i].getAbsolutePath(), dstFile, NetType.DIRECTED).script();
				D.p("Done: "+fileArr[i].getAbsolutePath());
			}
		}
	}
}
