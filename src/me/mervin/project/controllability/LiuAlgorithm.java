package me.mervin.project.controllability;

import java.util.Map;
import java.util.Set;

import me.mervin.core.Network;
import me.mervin.core.Global.NetType;
import me.mervin.project.controllability.lib.HopcroftKarp;
import me.mervin.util.D;
import me.mervin.util.FileTool;
import me.mervin.util.MapTool;
import me.mervin.util.MathTool;
import me.mervin.util.Pair;
import me.mervin.util.PairList;


 /**
 *   LiuAlgorithm.java
 *    
 *  @author Mervin.Wong  DateTime 2013-10-11 下午4:54:44    
 *  @version 0.4.0
 *  **********************************************************<br/>
 *  Ref: Liu, Y. Y., Slotine, J. J., & Barabási, A. L. (2011). Controllability of complex networks. Nature, 473(7346), 167-173.
 *  
 *  matched node
 *  unmatched node
 *  matching link
 *  
 *  critical link
 *  redundant link
 *  ordinary link
 *  
 */
public class LiuAlgorithm {
	
	private Network net = null;
	
	private Set<Number> matchedNodeSet = null;
	private Set<Number> unmatchedNodeSet = null;
	
	private Map<Number, Number> matchingLinkMap = null;
	private PairList<Number, Number> allLinkList = null;
	private PairList<Number, Number> matchingLinkList = null;
	private PairList<Number, Number> criticalLinkList = null;
	private PairList<Number, Number> redundantLinkList = null;
	private PairList<Number, Number> ordinaryLinkList = null;
	
	public LiuAlgorithm(){
		
	}
	public LiuAlgorithm(Network net){
		this.net = net;
		this._maximumMatching();
	}

	public void setNet(Network net){
		this.net = net;
		this._maximumMatching();
	}
	
	private void _maximumMatching(){
		if(Network.netType.equals(NetType.DIRECTED)){
			HopcroftKarp hk = new HopcroftKarp();
			PairList<Number, Number> edges = this.net.traverseEdge();
			for(int i = 0; i < edges.size(); i++){
				hk.addEdge(edges.getL(i).intValue(), edges.getR(i).intValue());
			}
			this.matchingLinkMap = hk.matching();
			D.p(this.matchingLinkMap.size());
			this.matchingLinkList = new MapTool().map2List(this.matchingLinkMap, false);
		}else{
			
		}
	}
	
	
	public Set<Number> getMatchedNode(){
		return this.matchingLinkMap.keySet();
	}
	
	public Set<Number> getUnmatchedNode(){
		return MathTool.subtraction(this.net.getAllNodeId(), this.matchingLinkMap.keySet());
	}
	
	public Set<Number> getDriverNode(){
		return MathTool.subtraction(this.net.getAllNodeId(), this.matchingLinkMap.keySet());
	}
	
	
	public PairList<Number,Number> getMatchingLink(){
		return this.matchingLinkList;
	}
	
	public PairList<Number, Number> getCriticalLink(){
		PairList<Number, Number> pl = new PairList<Number, Number>();
		
		return pl;
	}
	
	public PairList<Number, Number> getRedundantLink(){
		PairList<Number, Number> pl = new PairList<Number, Number>();
		
		return pl;
	}
	
	
	public PairList<Number, Number> getOrdinaryLink(){
		PairList<Number, Number> pl = new PairList<Number, Number>();
		
		return pl;
	}
	public void filtering(){
		
	}
	/**
	 *  
	 *  @param args
	 */
	public static void main(String[] args) {
		// TODO 自动生成的方法存根
		
		Network net = new Network("../data/ba.txt", NetType.DIRECTED);
		LiuAlgorithm l = new LiuAlgorithm(net);
		FileTool ft = new FileTool();
		D.p(l.getDriverNode());
		D.p(l.getDriverNode().size());
		ft.write(l.getDriverNode(), "../data/ctrl/driverNode.txt");

	}

}
