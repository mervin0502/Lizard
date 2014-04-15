package me.mervin.project.usr.mervin.AS;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import me.mervin.core.Network;
import me.mervin.core.Global.NetType;
import me.mervin.core.Global.NumberType;
import me.mervin.module.feature.Coreness;
import me.mervin.module.feature.Degree;
import me.mervin.util.D;
import me.mervin.util.FileTool;
import me.mervin.util.MapTool;
import me.mervin.util.MathTool;

public class NetCore {

	/**
	 *  
	 *  @param args
	 */
	public static void main(String[] args) {
		// TODO 自动生成的方法存根
		NetCore nc = new NetCore();
		String srcDir = "../data/AS/netM/";
		String dstDir = "../data/AS/core/";
		nc.core(srcDir, dstDir);

	}
	
	/*
	 * 求全网的核和平均度
	 */
	public void core(String srcDir, String dstDir){
		//File[] fileArr = new File(srcDir).listFiles();
		String srcFile = null;
		Network net = null;
		Coreness c = new Coreness();
		FileTool f = new FileTool();
		HashMap<Number, Number> core = new HashMap<Number, Number>();
		for (int i = 1; i <= 68; i++) {
			srcFile = srcDir+i+".txt";
			D.p(srcFile);
			net = new Network(srcFile, NetType.UNDIRECTED, NumberType.INTEGER);
			core.put(i, c.netCore(net));
			f.write(c.nodeCore(net, net.getAllNodeId()), dstDir+i+"-netCore.txt");
			//break;
		}
		f.write(core, dstDir+"netCore.txt");
	}

	/*
	 * 统计每层的节点数量
	 */
	public void nodeInLevel(String srcDir, String dstDir){
		String srcFile = null;
		Map<Number, Number> core = null;
		HashMap<Number, Number> node = null;
		FileTool f = new FileTool();
		for(int i = 1; i <= 68; i++){
			node = new HashMap<Number, Number>();
			srcFile = srcDir+i+"-netCore.txt";
			core = f.read2Map(srcFile);
			Collection<Number> collection = core.keySet();
			Number coreNum = 0;
			//Number nodeId = null;
			for (Iterator<Number> iterator = collection.iterator(); iterator.hasNext();) {
				//nodeId = (Number) iterator.next();
				coreNum = core.get(iterator.next());
				if(node.containsKey(coreNum)){
					node.put(coreNum, node.get(coreNum).intValue()+1);
				}else{
					node.put(coreNum, 1);
				}
			}
			f.write(node, dstDir+i+"-coreNum.txt");
		}
	}
	/*
	 * 统计每层的节点数量比例
	 */
	public void ratioInLevel(String srcDir, String dstDir){
		String srcFile = null;
		Map<Number, Number> core = null;
		Map<Number, Number> node = null;
		FileTool f = new FileTool();
		MapTool sm = new MapTool();
		for(int i = 1; i <= 48; i++){
			srcFile = srcDir+i+"-coreNum.txt";
			core = f.read2Map(srcFile);
			node = MathTool.ratio(core);
			f.write(sm.sort(node), dstDir+i+"-coreRatio.txt");
		}
	}
	
	public void netAvgCore(){
		FileTool f = new FileTool();
		Map<Number, Number> map = new TreeMap<Number,Number>();
		double sum = 0;
		for(int i= 1; i <= 48; i++){
			String srcFile = "../data/AS-2009-2012/core/ratio/"+i+"-core-ratio.txt";
			Map<Number, Number> ratio = f.read2Map(srcFile, NumberType.INTEGER, NumberType.DOUBLE);
			sum = 0;
			Number temp = null;
			for(Iterator<Number> it = ratio.keySet().iterator(); it.hasNext();){
				temp = it.next();
				sum += temp.intValue()*ratio.get(temp).doubleValue();
			}
			map.put(i, sum);
		}
		f.write(map, "../data/AS-2009-2012/core/netAvgCore.txt");
	}

}
