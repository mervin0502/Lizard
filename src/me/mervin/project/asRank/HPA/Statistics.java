package me.mervin.project.asRank.HPA;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import me.mervin.core.Global.NetType;
import me.mervin.core.Global.NumberType;
import me.mervin.core.Network;
import me.mervin.module.feature.AssortativityCoefficient;
import me.mervin.module.feature.Coreness;
import me.mervin.module.feature.Degree;
import me.mervin.module.feature.PCB;
import me.mervin.module.feature.StructureEntropy;
import me.mervin.util.D;
import me.mervin.util.FileTool;
import me.mervin.util.MathTool;

public class Statistics {

	public static void main(String[] args){
		String srcDir = "./data/m/";
		String srcFile = null;
		String dstFile = null;
		
		FileTool ft = new FileTool();
		File[] fileArr = ft.fileArr(srcDir);
		for(File f:fileArr){
			Collection<String> filter = new ArrayList<String>();
			filter.add("hpa-2");
			filter.add("txt");
			for(File f1:ft.fileArr(f.getAbsolutePath(), filter)){
				D.p(f1.getAbsolutePath());
				Network net = new Network(f1.getAbsolutePath(), NetType.UNDIRECTED, NumberType.INTEGER);
				StringBuffer sb = new StringBuffer();
				sb.append(net.nodeNum).append("\t");
				sb.append(net.edgeNum).append("\t");
				
				Degree d = new Degree(net);
				sb.append(d.netDegreeMax()).append("\t").append(d.netDegreeAvg()).append("\t");
				
				PCB pcb = new PCB();
				pcb.script(net, f1.getParent(), "/pcb");
				
				AssortativityCoefficient ac = new AssortativityCoefficient();
				sb.append(ac.script(net)).append("\t");
				
				Coreness c = new Coreness(net);
				sb.append(c.netCore()).append("\t");
				
				StructureEntropy se = new StructureEntropy(net);
				sb.append(se.script(false)).append("\t");
				dstFile = f1.getParent()+"/all.txt";
				ft.write(sb, dstFile);
			}
			
		}
		
/*		String srcFile = "./data/m/2000-0.5-0.45-0.7/core-2000-0.5-0.45-0.7.txt";
		FileTool ft = new FileTool();
		String dstFile = "./data/m/2000-0.5-0.45-0.7/core-2000-0.5-0.45-0.7-rate.txt";
		ft.write(MathTool.ratio(MathTool.frequency(ft.read2Map(srcFile))), dstFile);*/
	}
}
