package me.mervin.project.usr.mervin.AS;

import me.mervin.core.Network;
import me.mervin.core.Global.NetType;
import me.mervin.module.feature.MultiNet;
import me.mervin.util.D;
import me.mervin.util.FileTool;

public class NetEvolving {

	/**
	 *  
	 *  @param args
	 */
	public static void main(String[] args) {
		// TODO 自动生成的方法存根
		Network preNet = null, postNet = null;
		String srcDir = "../data/AS/netM/";
		String dstDir = "../data/AS/evolving/";
		String srcFile = "";
		String prefix = null;
		
		srcFile = srcDir+1+".txt";
		MultiNet m = new MultiNet();
		FileTool ft = new FileTool();
		preNet = new Network(srcFile, NetType.UNDIRECTED);
		for(int i = 2; i <= 68; i++){
			srcFile = srcDir+i+".txt";
			D.p(srcFile);
			postNet = new Network(srcFile, NetType.UNDIRECTED);
			
			prefix = (i-1)+"-"+i;
			//稳定节点
			ft.write(m.intersectionByNode(preNet, postNet), dstDir+"node/intersection/"+prefix+".txt");
			//消失节点
			ft.write(m.deathNode(preNet, postNet), dstDir+"node/disappear/"+prefix+".txt");
			//出现节点
			ft.write(m.birthNode(preNet, postNet), dstDir+"node/appear/"+prefix+".txt");
			
			//消失内部边
			ft.write(m.deathByInEdge(preNet, postNet).traverseEdge(), dstDir+"edge/disappear/"+prefix+"-in.txt");
			//消失边界边
			ft.write(m.deathByBoundaryEdge(preNet, postNet).traverseEdge(), dstDir+"edge/disappear/"+prefix+"-boundary.txt");
			
			//稳定边
			ft.write(m.intersectionByEdge(preNet, postNet).traverseEdge(), dstDir+"edge/intersection/"+prefix+".txt");
			//出现内部边
			ft.write(m.birthByInEdge(preNet, postNet).traverseEdge(), dstDir+"edge/appear/"+prefix+"-in.txt");
			//出现的边界边
			ft.write(m.birthByBoundaryEdge(preNet, postNet).traverseEdge(), dstDir+"edge/appear/"+prefix+"-boundary.txt");
			//出现的外部边
			ft.write(m.birthByOutEdge(preNet, postNet).traverseEdge(), dstDir+"edge/appear/"+prefix+"-out.txt");
		}
		
	}

}
