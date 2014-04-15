package me.mervin.project.usr.mervin;

import java.util.List;

import me.mervin.core.Network;
import me.mervin.core.Global.NetType;
import me.mervin.core.Global.NumberType;
import me.mervin.module.feature.Degree;
import me.mervin.util.FileTool;

public class D3 {

	/**
	 *  
	 *  @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String srcFile = "../data/3d.txt";
		
		FileTool f = new FileTool();
		/*
		int[] cols = new int[1];
		cols[0] = 1;
		List<Number> l1 = f.read2List(srcFile, NumberType.INTEGER, cols);
		cols[0] = 2;
		List<Number> l2 = f.read2List(srcFile, NumberType.INTEGER, cols);
		cols[0] = 3;
		List<Number> l3 = f.read2List(srcFile, NumberType.INTEGER, cols);
		cols[0] = 4;
		List<Number> l4 = f.read2List(srcFile, NumberType.INTEGER, cols);
		cols[0] = 5;
		List<Number> l5 = f.read2List(srcFile, NumberType.INTEGER, cols);
		cols[0] = 6;
		List<Number> l6 = f.read2List(srcFile, NumberType.INTEGER, cols);

		StringBuffer sb = new StringBuffer();
		int n = l1.size();
//		sb.append(l1.get(0)).append(l2.get(0)).append(l3.get(0)).append("\t");
		for(int i = 0; i < n-1; i++){
			sb.append(l4.get(i)).append(l5.get(i)).append(l6.get(i)).append("\t");
			sb.append(l1.get(i)).append(l2.get(i)).append(l3.get(i)).append("\r\n");
			sb.append(l1.get(i)).append(l2.get(i)).append(l3.get(i)).append("\t");
			sb.append(l4.get(i+1)).append(l5.get(i+1)).append(l6.get(i+1)).append("\r\n");
		}
		f.write(sb, "../data/3d-net.txt");*/
		
		Network net = new Network("../data/3d-net.txt", NetType.UNDIRECTED, NumberType.INTEGER);
		Degree d = new Degree(net);
//		f.write(d.netDegreeDistributionRate(), "../data/3d-degree-distribution.txt");
		f.write(d.nodeDegree(net.getAllNodeId()), "../data/3d-degree.txt");
	}

}
