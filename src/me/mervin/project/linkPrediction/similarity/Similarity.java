package me.mervin.project.linkPrediction.similarity;

import java.util.Set;

import me.mervin.core.Network;
import me.mervin.project.linkPrediction.LinkPrediction;
import me.mervin.util.E;
import me.mervin.util.MathTool;

/**
 * 
 *   Similarity.java
 *    链路预测中的相似性算法
 *    
 *  @author Mervin.Wong  DateTime 2013-7-25 下午6:53:43    
 *  @version 0.4.0
 */
public abstract class Similarity extends LinkPrediction{
	/**
	 * 构造函数
	 * @param net 网络
	 */
	public Similarity(Network net) {
		super(net);
		// TODO Auto-generated constructor stub
	}
	/**
	 * 构造函数
	 * @param net 网络
	 * @param k 存在边集合的划分个数
	 * @throws E 异常输出
	 */
	public Similarity(Network net, int k) throws E {
		super(net, k);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 * preNodeId和postNodeId的共同邻居 
	 *  @param preNodeId 节点的ID
	 *  @param postNodeId 节点的ID
	 *  @return Set<Number>
	 */
	public Set<Number> commonAdjNodeId(Network net2, Number preNodeId, Number postNodeId){
		Set<Number> nodeIdSet = null;
		nodeIdSet = MathTool.intersection(net2.getAdjNodeId(preNodeId), net2.getAdjNodeId(postNodeId));
		return nodeIdSet;
	}

}
