package me.mervin.project.asRank.evolution;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import me.mervin.core.Global.NumberType;
import me.mervin.core.Network;
import me.mervin.core.Global.NetType;
import me.mervin.module.feature.AssortativityCoefficient;
import me.mervin.module.feature.Coreness;
import me.mervin.util.D;
import me.mervin.util.FileTool;
import me.mervin.util.PairList;

public class AssortativityEvolution {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String srcDir = "E:\\data\\extractNet\\";
		String srcFile = null;
		String dstDir = "E:\\data\\";
		String dstFile = null;
		String date = null;
		
		AssortativityCoefficient ac = new AssortativityCoefficient();
		
		FileTool ft = new FileTool();
		StringBuffer sb = new StringBuffer();
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
				srcFile = srcDir+date+".as-rel.txt";
				if(ft.isExist(srcFile)){
					sb.append(date).append("\t");
					sb.append(ac.script(new Network(srcFile, NetType.UNDIRECTED, NumberType.INTEGER))).append("\r\n");
					D.p("###################");
				}
			}
		}
		dstFile = dstDir+"assortativity-coefficient.txt";
		ft.write(sb, dstFile);
	}


}
