package me.mervin.project.usr.mervin.AS;

import java.text.SimpleDateFormat;
import java.util.Date;

import me.mervin.core.Network;
import me.mervin.core.Global.NetType;
import me.mervin.core.Global.NumberType;
import me.mervin.module.feature.PCB;
import me.mervin.util.D;

public class ASPCB {

	/**
	 *  
	 *  @param args
	 */
	public static void main(String[] args) {
		// TODO 自动生成的方法存根
		String srcDir = "../data/AS/netM/";
		String dstDir = "../data/AS/APL/";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		for(int i = 1; i <= 68; i++){
			D.p(i+"->"+sdf.format(new Date()));
			new PCB().script(new Network(srcDir+i+".txt", NetType.UNDIRECTED, NumberType.INTEGER), dstDir, i+"");
		}
	}

}
