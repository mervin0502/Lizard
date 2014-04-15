package me.mervin.project.usr;

import java.util.Map;
import java.util.Set;

import me.mervin.core.Network;
import me.mervin.core.Global.ArithmeticOperators;
import me.mervin.core.Global.NetType;
import me.mervin.core.Global.NumberType;
import me.mervin.module.feature.Degree;
import me.mervin.util.FileTool;
import me.mervin.util.MapTool;
import me.mervin.util.MathTool;

public class NoClass {

	/**
	 *  
	 *  @param args
	 */
	public static void main(String[] args) {
		// TODO 自动生成的方法存根
		int defDegree = 500;
		
		String srcDir = "../data/AS/nets/";
		String dstDir = "../data/test/";
		String srcFile = null;
		String dstFile = null;
		
		Network preNet = null, postNet = null;
		Degree d = new Degree();
		MapTool mt = new MapTool();
		FileTool ft = new FileTool();
		
		
		Map<Number, Number> preNetDegree = null;//前一个网络的度
		Map<Number, Number> postNetDegree = null;//后一个网络的度
		
		Set<Number> intersection1  = null, intersection2 = null, select2 = null;
		
		srcFile = srcDir+"1.txt";
		preNet = new Network(srcFile, NetType.UNDIRECTED, NumberType.INTEGER);
		for(int i = 2; i <= 48; i++){
			srcFile = srcDir+i+".txt";
			postNet = new Network(srcFile, NetType.UNDIRECTED, NumberType.INTEGER);
			
			intersection1 = MathTool.intersection(preNet.getAllNodeId(), postNet.getAllNodeId());
			
			preNetDegree = mt.select(d.nodeDegree(preNet, intersection1), ArithmeticOperators.GE, defDegree);
			postNetDegree = mt.select(d.nodeDegree(postNet, intersection1), ArithmeticOperators.GE, defDegree);
			
			intersection2 = MathTool.intersection(preNetDegree.keySet(), postNetDegree.keySet());
			
			select2 = mt.select(d.nodeDegree(postNet, postNet.getAllNodeId()), ArithmeticOperators.GE, defDegree).keySet();
			
			ft.write(intersection2, dstDir+(i-1)+"-"+i+".txt");
			ft.write(((double)intersection2.size()/select2.size())+"\r\n", dstDir+"rate.txt", true);
			
			preNet = postNet;
			break;
		}
	}

}
