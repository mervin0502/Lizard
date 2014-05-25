package me.mervin.project.asRank.evolution;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import me.mervin.core.Global.NetType;
import me.mervin.core.Global.NumberType;
import me.mervin.core.Network;
import me.mervin.module.feature.ClusterCofficient;
import me.mervin.module.feature.Coreness;
import me.mervin.util.D;
import me.mervin.util.FileTool;
import me.mervin.util.PairList;

public class CCEvolution {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
	/*	ExecutorService pool = Executors.newFixedThreadPool(2);
		String srcDir = "E:\\data\\extractNet\\";
		String srcFile = null;
		String dstDir = "E:\\data\\as-rel-cc\\";
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
				t = new CCC(srcFile, dstDir, date);
				pool.execute(t);
				D.p("###################");
			}
		}
		
		pool.shutdown();*/
		CCEvolution cce = new CCEvolution();
		cce.CCAndDegree();
	}
	
	
	public void CCAndDegree(){
		Map<Number, LinkedList<Number>> map = new HashMap<Number, LinkedList<Number>>();
		Map<Number, Number> map2 = new HashMap<Number, Number>();
		
		String srcDir = "E:\\data\\";
		String ccSrcFile = null;
		String degreeSrcFile =null;
		String dstDir = "E:\\data\\cc-degree\\";
		String dstFile = null;
		String date = null;
		Network net = null;
		FileTool ft = new FileTool();
		Thread t = null;
		Map<Number, Number> degreeMap = null;
		Map<Number, Number> ccMap = null;
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
				ccSrcFile = srcDir+"as-rel-cc\\"+date+"-cc.txt";
				degreeSrcFile = srcDir+"degree\\"+date+"-degree.txt";
				if(ft.isExist(ccSrcFile) && ft.isExist(degreeSrcFile)){
					degreeMap = ft.read2Map(degreeSrcFile, NumberType.INTEGER, NumberType.INTEGER);
					ccMap = ft.read2Map(ccSrcFile, NumberType.INTEGER, NumberType.DOUBLE);
					for(Entry<Number, Number> e:degreeMap.entrySet()){
						Number nodeId = e.getKey();
						Number degree = e.getValue();
						if(map.containsKey(degree)){
							map.get(degree).add(ccMap.get(nodeId));
						}else{
							LinkedList<Number> l = new LinkedList<Number>();
							l.add(ccMap.get(nodeId));
							map.put(degree, l);
						}
					}
					
					for(Entry<Number, LinkedList<Number>> e:map.entrySet()){
						double sum = 0;
						for(Number v:e.getValue()){
							sum += v.doubleValue();
						}
						map2.put(e.getKey(), sum/e.getValue().size());
					}
					dstFile = dstDir+date+"-cc.txt";
					ft.write(map2, dstFile);
				}
				
				D.p("###################");
			}
		}
	}

}


class CCC extends Thread{
	private String srcFile;
	private String dstDir;
	private String date;
	CCC(String srcFile, String dstDir, String date){
		this.srcFile = srcFile;
		this.dstDir = dstDir;
		this.date = date;
	}
	public void run(){
		FileTool ft= new FileTool();
		if(ft.isExist(srcFile)){
			ClusterCofficient cc = new ClusterCofficient();
			Network net = new Network(srcFile, NetType.UNDIRECTED, NumberType.INTEGER);
			String dstFile = dstDir+date+"-core.txt";
			ft.write(cc.nodeClusterCofficient(net, net.getAllNodeId()), dstFile);
		}
	}
}