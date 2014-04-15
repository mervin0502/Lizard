package me.mervin.module.graphFormats;

import java.util.Collection;

import me.mervin.core.Network;
import me.mervin.core.Global.NetType;
import me.mervin.util.FileTool;
import me.mervin.util.PairList;


 /**
 *   GraphML.java
 *    
 *  @author Mervin.Wong  DateTime 2013-10-13 下午3:10:33    
 *  @version 0.4.0
 */
public class GraphML {

	
	private Network net = null;
	private String netName = null;
	private String dstFileName = null;
	
	private String ext = "gml";
	
	private FileTool ft = new FileTool();
	
	public GraphML(){
		
	}
	public GraphML(Network net){
		this.net = net;
	}
	
	public void script(String dstFile){
		this.script(net, dstFile);
	}
	public void script(Network net, String dstFile){
		this.script(net, "G", dstFile);
		
	}
	public void script(Network net, String netName, String dstFile){
		this.net = net;
		this.netName = netName;
		//this.dstFileName = dstFile;
		
		/*
		 * 头部信息
		 */
		StringBuffer head = new StringBuffer();
		head.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>").append("\r\n");
		head.append("<graphml xmlns=\"http://graphml.graphdrawing.org/xmlns\"").append("\r\n");
		head.append("\t").append("xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"").append("\r\n");
		head.append("\t").append("xsi:schemaLocation=\"http://graphml.graphdrawing.org/xmlns ").append("\r\n");
		head.append("\t\t").append("http://graphml.graphdrawing.org/xmlns/1.0/graphml.xsd\"> ").append("\r\n");
		
		
		
		/*
		 * 网络体
		 */
		StringBuffer body = new StringBuffer();
		if(net.getNetType().equals(NetType.DIRECTED)){
			body.append("\t").append("<graph id=\""+this.netName+"\" edgedefault=\"directed\" >").append("\r\n");
		}else{
			body.append("\t").append("<graph id=\""+this.netName+"\" edgedefault=\"undirected\" >").append("\r\n");
		}
		
		/*
		 * 节点信息
		 */
		StringBuffer nodes = new StringBuffer();
		Collection<Number> c = this.net.getAllNodeId();
		for(Number nodeId : c){
			nodes.append("\t\t").append("<node id=\"n"+nodeId+"\" />").append("\r\n");
		}
		
		/*
		 * 边信息
		 */
		StringBuffer edges = new StringBuffer();
		PairList<Number, Number> edgeList = this.net.traverseEdge();
		int size = edgeList.size();
		for(int i = 0; i < size; i++){
			edges.append("\t\t").append("<edge id=\"e"+(i+1)+"\" source=\"n"+edgeList.getL(i)+"\" target=\"n"+edgeList.getR(i)+"\" />").append("\r\n");
			
		}		
		
		StringBuffer foot = new StringBuffer();
		foot.append("\t").append("</graph>").append("\r\n");
		foot.append("</graphml>");
		this.ft.write(head, dstFile);
		this.ft.write(body, dstFile, true);
		this.ft.write(nodes, dstFile, true);
		this.ft.write(edges, dstFile, true);
		this.ft.write(foot, dstFile, true);
		
	}
	
	/**
	 *  
	 *  @param args
	 */
	public static void main(String[] args) {
		// TODO 自动生成的方法存根

	}

}
