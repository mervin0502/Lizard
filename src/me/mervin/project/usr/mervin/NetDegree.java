package me.mervin.project.usr.mervin;

import java.io.File;
import java.util.Map;

import me.mervin.core.Network;
import me.mervin.core.Global.NetType;
import me.mervin.core.Global.NumberType;
import me.mervin.module.feature.Coreness;
import me.mervin.util.FileTool;
import me.mervin.util.MapTool;

public class NetDegree {
	
	public void core(String srcDir, String dstDir){
		File[] fileArr = new File(srcDir).listFiles();
		Network net = null;
		Coreness c = new Coreness();
		FileTool f = new FileTool();
		/*
		 * 结果有问题
		 */
		for (int i = 0; i < fileArr.length; i++) {
			net = new Network(fileArr[i].getAbsolutePath(), NetType.UNDIRECTED, NumberType.INTEGER);
			f.write(c.nodeCore(net, net.getAllNodeId()), dstDir+i+"-netCore.txt");
		}
	}

	/**
	 *  
	 *  @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
/*		NetDegree nc = new NetDegree();
		nc.core("../data/AS-2009-2012/", "../data/AS-2009-2012/");*/
		
		String srcDir = "../data/AS-2009-2012/Evolution/";
		String srcFile = null;
		MapTool mt = new MapTool();
		Map<Number, Number> map = null;
		FileTool f = new FileTool();
		int i = 0;
		for(i = 1; i  <= 47; i++){
			srcFile = srcDir+i+"-"+(i+1)+"/1-netDegreeRate.txt";
			map = f.read2Map(srcFile, NumberType.INTEGER, NumberType.DOUBLE);
			f.write(mt.sort(map), srcDir+"net-degree/"+i+"-net-degree.txt");
		}
		srcFile = srcDir+"47-48"+"/2-netDegreeRate.txt";
		map = f.read2Map(srcFile, NumberType.INTEGER, NumberType.DOUBLE);
		f.write(mt.sort(map), srcDir+"net-degree/48-net-degree.txt");
	}

}
