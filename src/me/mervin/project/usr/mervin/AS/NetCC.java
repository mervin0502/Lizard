package me.mervin.project.usr.mervin.AS;

import me.mervin.core.Network;
import me.mervin.core.Global.NetType;
import me.mervin.core.Global.NumberType;
import me.mervin.module.feature.ClusterCofficient;
import me.mervin.util.D;
import me.mervin.util.FileTool;

/**
 * 聚类系数
 *   NetCC.java
 *    
 *  @author Mervin.Wong  DateTime 2013-9-12 下午7:05:28    
 *  @version 0.4.0
 */
public class NetCC {

	/**
	 *  
	 *  @param args
	 */
	public static void main(String[] args) {
		// TODO 自动生成的方法存根
		String srcDir = "../data/AS/netM/";
		String dstDir = "../data/AS/CC/";
		String srcFile = "";
		Network net = null;
		ClusterCofficient cc = new ClusterCofficient();
		FileTool ft = new FileTool();
		for(int i = 57; i <= 68; i++){
			srcFile = srcDir+i+".txt";
			D.p(srcFile);
			net = new Network(srcFile, NetType.UNDIRECTED, NumberType.INTEGER);
			ft.write(cc.nodeClusterCofficient(net, net.getAllNodeId()), dstDir+i+"-netCC.txt");
		}
	}

}
