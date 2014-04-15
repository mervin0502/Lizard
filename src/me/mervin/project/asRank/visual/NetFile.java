package me.mervin.project.asRank.visual;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import me.mervin.core.Network;
import me.mervin.core.Global.NetType;
import me.mervin.core.Global.NumberType;
import me.mervin.util.D;
import me.mervin.util.FileTool;
import me.mervin.util.PairList;

import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.DefaultGraph;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.stream.file.FileSinkImages;
import org.graphstream.stream.file.FileSinkImages.LayoutPolicy;
import org.graphstream.stream.file.FileSinkImages.OutputType;
import org.graphstream.stream.file.FileSinkImages.Resolutions;


 /**
 *   NetFile.java
 *    
 *  @author Mervin.Wong  DateTime 2014年4月3日 下午7:29:06    
 *  @version 0.4.0
 */
public class NetFile {

	/**
	 *  
	 *  @param args
	 */
	public static void main(String[] args) {
		// TODO 自动生成的方法存根
/*		DefaultGraph g = new DefaultGraph("my beautiful graph");
		FileSinkImages pic = new FileSinkImages(OutputType.PNG, Resolutions.VGA);
		 
		pic.setLayoutPolicy(LayoutPolicy.COMPUTED_FULLY_AT_NEW_IMAGE);
		 
		g.addNode("A");
		g.addNode("B");
		g.addNode("C");
		 
		g.addEdge("AB", "A", "B");
		g.addEdge("AC", "A", "C");
		g.addEdge("BC", "B", "C");
		 
		try {
			pic.writeAll(g, "sample.png");
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}*/
		String srcFile = "E:\\data\\as-rel-tree_1\\19980101\\701-tree.txt";
		String dstFile = "E:\\data\\19980101\\1-tree.txt";
		Network net = new Network(srcFile, NetType.DIRECTED, NumberType.INTEGER);
		D.p(net.nodeNum);
		D.p(net.edgeNum);
		Map<Number, Number> nodeMap = new HashMap<Number, Number>();
		int k = 0;
		for(Number nodeId:net.getAllNodeId()){
			nodeMap.put(nodeId, k);
			k++;
		}
		StringBuffer sb = new StringBuffer();
		PairList<Number, Number> list = net.traverseEdge();
		for(int i = 0; i< list.size(); i++){
			sb.append("{ @source=").append(nodeMap.get(list.getL(i))).append("; @destination=").append(nodeMap.get(list.getR(i))).append("; },\r\n");
		}
		D.p(nodeMap.get(1));
		FileTool ft = new FileTool();
		//ft.write(sb, dstFile);
		
		Graph graph = new SingleGraph("Tutorial 1");
		for(Number nodeId:net.getAllNodeId()){
			graph.addNode(nodeId+"");
		}
		for(int i = 0; i< list.size(); i++){
			graph.addEdge(list.getL(i)+"-"+list.getR(i), list.getL(i)+"", list.getR(i)+"");
		}
		graph.display();
	}
	

}
