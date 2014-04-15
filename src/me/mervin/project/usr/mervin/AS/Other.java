package me.mervin.project.usr.mervin.AS;

import me.mervin.core.Network;
import me.mervin.core.Global.NetType;
import me.mervin.module.feature.AssortativityCoefficient;
import me.mervin.util.D;
import me.mervin.util.FileTool;

public class Other {

	private String srcDir = "../data/AS/netM/";
	private String dstDir = "../data/AS/other/";	
	private FileTool ft = new FileTool();
	/**
	 *  
	 *  @param args
	 */
	public static void main(String[] args) {
		// TODO 自动生成的方法存根
		Other o = new Other();
		o.AssortativityCoefficient();
	}
	
	public void AssortativityCoefficient(){
		AssortativityCoefficient ac = new AssortativityCoefficient();

		Network net = null;
		String srcFile = null;
		for(int i = 1; i <= 68; i++){
			srcFile = this.srcDir+i+".txt";
			D.p(srcFile);
			net = new Network(srcFile, NetType.UNDIRECTED);
			this.ft.write(i+"\t"+ac.script(net)+"\r\n", this.dstDir+"ac.txt", true);
		}
	}
}
