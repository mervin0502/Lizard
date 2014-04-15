package me.mervin.core;

public class Global {
	/**
	 * 有向网  or 无向网
	 */
	public static enum NetType{DIRECTED, UNDIRECTED};
	/**
	 * 将网络拓扑输出到文件时的格式
	 */
	public static enum NetFileFormat{ID, ID_NWEIGHT, ID_EWEIGHT, ID_NWEIGHT_EWEIGHT};
	/*
	 * 网络节点ID的类型
	 */
	//public static enum NumberType{LONG, INTEGER, FLOAT, DOUBLE};
	/**
	 * Number的类型
	 */
	public static enum NumberType{LONG, INTEGER, FLOAT, DOUBLE};
	/**
	 * 网络的输入方式
	 */
	public static enum NetInputType{EMPTY, NODENUM, PAIRLIST, FILE};
	
	/**
	 * 算术运算符
	 * G：大于 L:小于 E:等于 NE:不等于 GE:不小于 LE:不大于
	 */
	public static enum ArithmeticOperators{G, L, E, NE, GE, LE};
	/**
	 * 逻辑运算符
	 * AND:与 OR:或 NOT:非
	 */
	public static enum LogicalOperators{AND, OR, NOT};
	/**
	 * 提取AS内容格式 AS
	 */
	public static enum ExtractAS{ALL, I, D};
	
	/**
	 * 提起IP内容的格式
	 */
	public enum extractIP {SOURCE_DESTINATION, LINK_I, LINK_C, LINK_ALL, LINK_I_RTT, LINK_C_RTT,LINK_ALL_RTT,LINK_I_TRY, LINK_C_TRY, LINK_ALL_TRY};


	/**
	 * 节点对的形式
	 * DIRECT:有向，(l,r) != (r,l)
	 * INDIRECT:无向，(l,r) == (r,l)
	 */
	public enum PairFormat{DIRECT, INDIRECT};
	
	
	/**
	 * 度：
	 * all:无向
	 * in：入度
	 * out:出度
	 */
	public enum DegreeType{ALL ,IN, OUT};
	
	/**
	 * 
	 */
	public enum AttributeType{STRING, LONG, INTEGER, DOUBLE, FLOAT};
	
}
