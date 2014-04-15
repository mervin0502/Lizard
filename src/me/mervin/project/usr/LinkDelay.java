package me.mervin.project.usr;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

import me.mervin.core.Global.NumberType;
import me.mervin.util.D;
import me.mervin.util.FileTool;
import me.mervin.util.MathTool;
import me.mervin.util.Pair;

/**
 * LinkDelay
 * 获取链路中的延迟
 * @author Mervin.Wong
 * @version 0.1.0
 *
 */
public class LinkDelay {

	public LinkDelay(){
		
	}
	
	@SuppressWarnings("null")
	public static void main(String[] args){
		try {
			FileTool f = new FileTool();
			//只提取tracerout 到该IP的链路
			Set<Number> objNodeSet = f.read2Set("", NumberType.LONG);
			
			@SuppressWarnings("resource")
			BufferedReader reader = new BufferedReader(new FileReader("../data/amw.txt"));
			String line = null;
			String[] lineArr = null;
			ArrayList<Link> links = new ArrayList<Link>();
			HashMap<Long, LinkedList<Edge>> avgDelay = new HashMap<Long, LinkedList<Edge>>();
			int i = 1;
			Link link = null;
			
			long souIp = 0, desIp = 0,  tempIp1 = 0, tempIp2 = 0 ;;
			double curDelay = 0;
			/*
			 * 预处理链路
			 */
			boolean flag2 = true;
			while((line = reader.readLine()) != null){
				D.p(line);
				lineArr = line.split("\t|(\\s{1,})");
				if(lineArr[0].equalsIgnoreCase("trace")&& objNodeSet.contains(MathTool.ip2Long(lineArr[2]))){
					flag2 = true;
					link = new Link((Long) MathTool.ip2Long(lineArr[2]));
					i = 1;
				}else{ 
					flag2 = false;
				}
				
				if(flag2){
					if(lineArr.length == 4){
						if(Double.parseDouble(lineArr[0]) < 0){
							curDelay = 0;
						}else{
							curDelay = Double.parseDouble(lineArr[0]);
						}
						souIp = (Long) MathTool.ip2Long( lineArr[1]);
						desIp = (Long) MathTool.ip2Long(lineArr[3]);
						
						if(link.time < curDelay){
							link.time = curDelay;
							link.souIp = souIp;
							link.desIp = desIp;
							link.index = i;
						}

						Edge edge1 = new Edge(souIp, desIp, curDelay);
						link.edges.add(edge1);
						LinkedList<Edge> edges = null;
						if(souIp < desIp){
							tempIp1 = souIp;
							tempIp2 = desIp;
						}else{
							tempIp1 = desIp;
							tempIp2 = souIp;
						}
						if(avgDelay.get(tempIp1) != null){
							edges = avgDelay.get(tempIp1);
							boolean flag = false;
							for (Iterator<Edge> iterator = edges.iterator(); iterator.hasNext();) {
								Edge edge2 = (Edge) iterator.next();
								if((edge2.souIp == tempIp2) || (edge2.desIp == tempIp2)){
									edge2.time = (edge2.time*edge2.avgNum+curDelay)/(edge2.avgNum+1);
									edge2.avgNum++;
									flag = true;
									break;
								}
							}
							if(flag == false){
								edges.add(new Edge(souIp, desIp, curDelay));
							}
						}else{
							edges  = new LinkedList<Edge>();
							edges.add(new Edge(souIp, desIp, curDelay));
							avgDelay.put(tempIp1, edges);
						}
						i++;
					}else if(lineArr.length == 1){
						links.add(link);
					}else{
						
					}
				}
			}
			
			StringBuffer str = new StringBuffer();
			HashSet<Pair> l = new HashSet<Pair>();
			for(Iterator it = avgDelay.keySet().iterator(); it.hasNext();){
				LinkedList<Edge> es = avgDelay.get(it.next());
				for (Iterator iterator = es.iterator(); iterator.hasNext();) {
					Edge e = (Edge) iterator.next();
					//Pair pe = new Pair(e.souIp, e.desIp, e.time);
					//if(!l.contains(pe)){
						//D.p(e.souIp+"##"+e.desIp+"###"+e.time);
						//D.p(l..getValue());
						//l.add(pe);
				//	}
					str.append(e.souIp+"\t"+e.desIp+"\t"+e.time).append("\r\n");
				}
			}
			//D.m();
/*			double tempTime = 0;
			int tempIndex = 1, j = 1;
			Edge tempEdge = null, edge = null;
			//long tempIp1 = 0, tempIp2 = 0;
			LinkedList<Edge> edges1 = null, edges2 = null;	
			StringBuffer str = new StringBuffer();
			StringBuffer tempStr = new StringBuffer();
			for (Iterator<Link> iterator = links.iterator(); iterator.hasNext();) {
				//遍历所有的链路
				j = 1;
				tempTime = 0;
				link = (Link) iterator.next();
				tempStr.append("Trace "+MathTool.ip2Str(link.traceIp)).append(" ");
				tempStr.append("old ").append(link.index+" ").append(link.time+" ").append(MathTool.ip2Str(link.souIp)+" ").append(MathTool.ip2Str(link.desIp)).append(" ");

				edges1 = link.edges;
				
				Edge edge2 = null;
				for (Iterator<Edge> iterator2 = edges1.iterator(); iterator2.hasNext();) {
					//遍历一条链路的所有路径
					edge = (Edge) iterator2.next();
					if(edge.souIp < edge.desIp){
						tempIp1 = edge.souIp;
						tempIp2 = edge.desIp;
					}else{
						tempIp1 = edge.desIp;
						tempIp2 = edge.souIp;
					}
					//D.p(MathTool.ip2Str(tempIp1));
					//D.p(MathTool.ip2Str(tempIp2));
					edges2 =avgDelay.get(tempIp1);
					for (Iterator<Edge> iterator3 = edges2.iterator(); iterator3.hasNext();) {
						edge2 = (Edge) iterator3.next();
						//D.p(tempIp2+"##");
						if((edge2.souIp == tempIp2) || (edge2.desIp == tempIp2)){
							//D.p(edge2.desIp+"!!!!"+edge2.time);
							if(tempTime < edge2.time){
								//D.p("@@@"+edge2.desIp);
								tempTime = edge2.time;
								tempIndex = j;
								tempEdge = edge2;
								break;
							}
						}
					}
					j++;
				}
				tempStr.append("new ").append(tempIndex+" ").append(tempTime+" ").append(MathTool.ip2Str(tempEdge.souIp)+" ").append(MathTool.ip2Str(tempEdge.desIp)).append("\n");
				D.m(tempStr.toString());
				str.append(tempStr);
				tempStr.delete(0, tempStr.length());
			}*/
			new FileTool().write(str, "../data/delayNet.txt", false);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			D.m("文件不存在");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			D.m("这一行为空");
		}
	}
	
}

class Link{
	public long traceIp;
	public double time = 0;
	public int index = 0;
	public long souIp = 0;
	public long desIp = 0;
	public LinkedList<Edge> edges;
	
	Link(long ip){
		this.traceIp = ip;
		this.time = 0;
		this.index = 0;
		this.edges = new LinkedList<Edge>();
	}
}

class Edge{
	public long souIp;
	public long desIp;
	public double time;
	public int avgNum = 0;
	Edge(long souIp, long desIp, double time){
		this.souIp = souIp;
		this.desIp = desIp;
		this.time = time;
		this.avgNum = 1;
	}
}