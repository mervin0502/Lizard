package me.mervin.project.usr.mervin.AS;

import me.mervin.core.Network;
import me.mervin.core.Global.NetType;
import me.mervin.core.Global.NumberType;
import me.mervin.module.feature.Degree;
import me.mervin.util.D;
import me.mervin.util.FileTool;

public class NetDegree {

	private Degree d = new Degree();
	private FileTool ft = new FileTool();
	/**
	 *  
	 *  @param args
	 */
	public static void main(String[] args) {
		// TODO 自动生成的方法存根
		NetDegree nd = new NetDegree();
		nd.degree();
	}

	/*
	 * 节点的度
	 * 度分布
	 * 平均度
	 */
	public void degree(){
		String srcDir = "../data/AS/netM/";
		String srcFile = "";
		String dstDir = "../data/AS/Degree/";
		
		Network net = null;
		for(int i = 41; i <= 68; i++){
			srcFile = srcDir+i+".txt";
			D.p(srcFile);
			net = new Network(srcFile, NetType.UNDIRECTED, NumberType.INTEGER);
			this.ft.write(this.d.nodeDegree(net, net.getAllNodeId()), dstDir+"degree/"+i+"-degree.txt");
			this.ft.write(this.d.netDegreeDistributionRatio(net), dstDir+"ratio/"+i+"-ratio.txt");
			this.ft.write(i+"\t"+this.d.netDegreeAvg(net)+"\r\n", dstDir+"avgDegree.txt", true);
			this.ft.write(i+"\t"+this.d.netDegreeMax(net)+"\r\n", dstDir+"maxDegree.txt", true);
		}
	}
}
