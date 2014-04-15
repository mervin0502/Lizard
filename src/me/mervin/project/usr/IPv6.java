package me.mervin.project.usr;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import me.mervin.core.Network;
import me.mervin.core.Global.NetType;
import me.mervin.core.Global.NumberType;
import me.mervin.module.feature.ClusterCofficient;
import me.mervin.module.feature.Coreness;
import me.mervin.module.feature.Degree;
import me.mervin.module.feature.PCB;
import me.mervin.module.feature.Path;
import me.mervin.util.FileTool;


 /**
 *   IPv6.java
 *    
 *  @author Mervin.Wong  DateTime 
 *  @version 0.4.0
 */
public class IPv6 {

	/**
	 *  
	 *  @param args
	 */
	public static void main(String[] args) {
		// TODO 自动生成的方法存根
		String srcDir = "";
		String dstDir = "";
		String srcFile = null;
		String dstFile = null;
		String name = null;
		FileTool ft = new FileTool();
		File[] fileArr = ft.fileArr(srcDir);
		for(int i = 0; i < fileArr.length; i++){
			srcFile = fileArr[i].getAbsolutePath();
			name = fileArr[i].getName();
			Network net = new Network(srcFile, NetType.UNDIRECTED, NumberType.INTEGER);
			dstDir = dstDir+name+"\\";
			Path path = new Path();
			dstFile = dstDir+"APL.txt";
			ft.write(path.netAvgPathLength(net)+"", dstFile);
			
			ClusterCofficient cc = new ClusterCofficient();
			dstFile = dstDir+"CC.txt";
			ft.write(cc.nodeClusterCofficient(net, net.getAllNodeId()), dstFile);
			
			Coreness core = new Coreness();
			dstFile = dstDir+"maxCore.txt";
			ft.write(core.netCore()+"", dstFile);
			
			Degree degree = new Degree(net);
			dstFile = dstDir+"avgDegree.txt";
			ft.write(degree.netDegreeAvg()+"", dstFile);
			dstFile = dstDir+"maxDegree.txt";
			ft.write(degree.netDegreeMax()+"", dstFile);
			
		}

	}
}
