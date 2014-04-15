package me.mervin.project.usr.mervin;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import me.mervin.core.Network;
import me.mervin.core.Global.NetType;
import me.mervin.core.Global.NumberType;
import me.mervin.module.feature.Degree;
import me.mervin.util.D;
import me.mervin.util.FileTool;
import me.mervin.util.MathTool;

/**
 * 
 *   Species.java
 *   利用索引表构建昆虫网络   
 *  @author Mervin.Wong  DateTime 2013-8-30 下午9:07:49    
 *  @version 0.4.0
 */
public class SpeciesNet {
	

	/**
	 * 索引表
	 * <p>界 门 纲 目 科 属 种 个体</p>
	 */
	public static enum Index{KINGDOM, PHYLUM, CLASS, ORDER, FAMILY, GENUS, SPECIES, UNIT};
	
	/*
	 * 连边参数
	 */
	private float k1 = 0;
	private float k2 = 0;
	private float k3 = 0;
	private float k4 = 0;
	private float k5 = 0;
	private float k6 = 0;
	private float k7 = 0;
	
	/*
	 * 文件目录
	 */
	private String srcDir = "../data/species/";
	private String dstDir = this.srcDir;
	/*
	 * 提取文本中节点的属性
	 */
	public Map<Integer, Map<Index, String>> extractNode(String srcFile){
		Map<Integer, Map<Index, String>> units = new HashMap<Integer, Map<Index,String>>();//存储所有个体的属性  
		int curIndex = 1;
		
		
		try {
			BufferedReader reader = new BufferedReader(new FileReader(srcFile));
			String line = null;
			String[] lineArr = null;
			Map<Index, String> unit = null;//一个个体
			/*
			 * 已收集到的所属的类型
			 */
			Set<String> _kingDom = new HashSet<String>();
			Set<String> _phylum = new HashSet<String>();
			Set<String> _class = new HashSet<String>();
			Set<String> _order = new HashSet<String>();
			Set<String> _family = new HashSet<String>();
			Set<String> _genus = new HashSet<String>();
			Set<String> _species = new HashSet<String>();
			Set<String> _unit = new HashSet<String>();
			
			String temp = null;
			
			while((line = reader.readLine()) != null){
				lineArr = line.split("\\s+");
				//D.p(line+"####"+lineArr.length);
				/*
				 * 存储个体属性
				 */
				/*
				 * 个体名唯一
				 */
				unit = new HashMap<SpeciesNet.Index, String>();//一个个体
				if(!_unit.contains(lineArr[7].trim())){
				//if(lineArr.length ==  8 && ){
					temp = lineArr[0].trim();
					unit.put(Index.KINGDOM, temp);//界
					_kingDom.add(temp);
					temp = lineArr[1].trim();
					unit.put(Index.PHYLUM, temp);//门
					_phylum.add(temp);
					temp = lineArr[2].trim();
					unit.put(Index.CLASS, temp);//纲
					_class.add(temp);
					temp = lineArr[3].trim();
					unit.put(Index.ORDER, temp);//目
					_order.add(temp);
					temp = lineArr[4].trim();
					unit.put(Index.FAMILY, temp);//科
					_family.add(temp);
					temp = lineArr[5].trim();
					unit.put(Index.GENUS, temp);//属
					_genus.add(temp);
					temp = lineArr[6].trim();
					unit.put(Index.SPECIES, temp);//种
					_species.add(temp);
					temp = lineArr[7].trim();
					unit.put(Index.UNIT, temp);//个体
					_unit.add(temp);
				
					units.put(curIndex, unit);//一个个体
					curIndex++;
					//D.p(curIndex);
				}
			}
			reader.close();
		} catch (FileNotFoundException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		
		return units;
	}
	
	/**
	 * 由索引表构造一棵树
	 */
	public _Node createTreeByIndex(String str, String dstFile, double p){
		String srcFile = this.srcDir+str;
		Map<Integer, Map<Index, String>> units = this.extractNode(srcFile);
		Map<Index, String> unit = null;
		_Node root = new _Node();//根节点
		_Node temp = null, preTemp = root;
		String name = null;
		Map<String, _Node> nodeMap = new HashMap<String, _Node>();//对所有的节点做索引
		ArrayList<_Node> unitList = new ArrayList<_Node>(units.size());//记录个体（叶子）
		Index flag = null;
		//D.p(units);
		for(int id : units.keySet()){
			unit = units.get(id);
			
			//界
			if(unit.containsKey(Index.KINGDOM)){
				name = unit.get(Index.KINGDOM);
				if(nodeMap.containsKey(name)){
					temp = nodeMap.get(name);
				}else{
					//第一次加入
					temp = new _Node(Index.KINGDOM, preTemp, name);
					nodeMap.put(name, temp);
					preTemp.addChild(temp);
				}
				preTemp = temp;
				flag = Index.KINGDOM;
			}
			
			//门
			if(unit.containsKey(Index.PHYLUM)){
				if(!preTemp.equals(root) && !flag.equals(Index.KINGDOM)){
					try {
						throw new Exception("PHYLUM");
					} catch (Exception e) {
						// TODO 自动生成的 catch 块
						e.printStackTrace();
						System.exit(0);
					}
				}
				
				name = unit.get(Index.PHYLUM);
				if(nodeMap.containsKey(name)){
					temp = nodeMap.get(name);
				}else{
					//第一次加入
					temp = new _Node(Index.PHYLUM, preTemp, name);
					preTemp.addChild(temp);

					nodeMap.put(name, temp);
				}
				preTemp = temp;
				flag = Index.PHYLUM;
			}
			
			//纲
			if(unit.containsKey(Index.CLASS)){
				if(!preTemp.equals(root) && !flag.equals(Index.PHYLUM)){
					try {
						throw new Exception("CLASS");
					} catch (Exception e) {
						// TODO 自动生成的 catch 块
						e.printStackTrace();
						System.exit(0);
					}
				}
				name = unit.get(Index.CLASS);
				if(nodeMap.containsKey(name)){
					temp = nodeMap.get(name);
				}else{
					//第一次加入
					temp = new _Node(Index.CLASS, preTemp, name);
					preTemp.addChild(temp);

					nodeMap.put(name, temp);
				}
				preTemp = temp;
				flag = Index.CLASS;
			}
			
			
			//目
			if(unit.containsKey(Index.ORDER)){
				if(!preTemp.equals(root) && !flag.equals(Index.CLASS)){
					try {
						throw new Exception("ORDER");
					} catch (Exception e) {
						// TODO 自动生成的 catch 块
						e.printStackTrace();
						System.exit(0);
					}
				}
				name = unit.get(Index.ORDER);
				if(nodeMap.containsKey(name)){
					temp = nodeMap.get(name);
				}else{
					//第一次加入
					temp = new _Node(Index.ORDER, preTemp, name);
					preTemp.addChild(temp);
					
					nodeMap.put(name, temp);
				}
				preTemp = temp;
				flag = Index.ORDER;
			}
			
			
			//科
			if(unit.containsKey(Index.FAMILY)){
				if(!preTemp.equals(root) && !flag.equals(Index.ORDER)){
					try {
						throw new Exception("FAMILY");
					} catch (Exception e) {
						// TODO 自动生成的 catch 块
						e.printStackTrace();
						System.exit(0);
					}
				}				
				name = unit.get(Index.FAMILY);
				if(nodeMap.containsKey(name)){
					temp = nodeMap.get(name);
				}else{
					//第一次加入
					temp = new _Node(Index.FAMILY, preTemp, name);
					preTemp.addChild(temp);
					
					nodeMap.put(name, temp);
				}
				preTemp = temp;
				flag = Index.FAMILY;
			}
			
			
			//属
			if(unit.containsKey(Index.GENUS)){
				if(!preTemp.equals(root) && !flag.equals(Index.FAMILY)){
					try {
						throw new Exception("GENUS");
					} catch (Exception e) {
						// TODO 自动生成的 catch 块
						e.printStackTrace();
						System.exit(0);
					}
				}	
				name = unit.get(Index.GENUS);
				if(nodeMap.containsKey(name)){
					temp = nodeMap.get(name);
				}else{
					//第一次加入
					temp = new _Node(Index.GENUS, preTemp, name);
					preTemp.addChild(temp);
					
					nodeMap.put(name, temp);
				}
				preTemp = temp;
				flag = Index.GENUS;
			}
			
			
			//种
			if(unit.containsKey(Index.SPECIES)){
				if(!preTemp.equals(root) && !flag.equals(Index.GENUS)){
					try {
						throw new Exception("SPECIES");
					} catch (Exception e) {
						// TODO 自动生成的 catch 块
						e.printStackTrace();
						System.exit(0);
					}
				}	
				name = unit.get(Index.SPECIES);
				if(nodeMap.containsKey(name)){
					temp = nodeMap.get(name);
				}else{
					//第一次加入
					temp = new _Node(Index.SPECIES, preTemp, name);
					preTemp.addChild(temp);
					
					nodeMap.put(name, temp);
				}
				preTemp = temp;
				flag = Index.SPECIES;
			}
			
			//个体
			if(unit.containsKey(Index.UNIT)){
				if(!preTemp.equals(root) && !flag.equals(Index.SPECIES)){
					try {
						throw new Exception("UNIT");
					} catch (Exception e) {
						// TODO 自动生成的 catch 块
						e.printStackTrace();
						System.exit(0);
					}
				}
				name = unit.get(Index.UNIT);
//				if(nodeMap.containsKey(name)){
//					temp = nodeMap.get(name);
//				}else{
					//第一次加入
					temp = new _Node(Index.UNIT, preTemp, name);
					temp.id = id;
					preTemp.addChild(temp);
					
					nodeMap.put(name, temp);
//				}
				preTemp = temp;
				flag = Index.UNIT;
				unitList.add(temp);
			}	
		}
/*		StringBuffer sb = new StringBuffer();
		for(int i = 0; i< unitList.size(); i++){
			D.p(unitList.get(i).parent.childs.size());
			sb.append(unitList.get(i).parent.childs.size()).append("\r\n");
		}
		FileTool ft = new FileTool();
		ft.write(sb, this.dstDir+dstFile);*/
		/*
		
		*//**
		 * 提取网络
		 */
		StringBuffer sb = new StringBuffer();
		double r = 0;//系统产生随机数
		double q = 0;
		int unitListSize = unitList.size();
		_Node pre = null, post = null;
		int k = 0;
		D.p(unitListSize);
		for(int i = 0; i < unitListSize; i++){
			for(int j = i+1; j < unitListSize; j++){
				r = MathTool.random();
				pre = unitList.get(i);
				post = unitList.get(j);
				q = this._getLinkProbability(pre, post, p);
				//D.p(r+"####"+q);
				if(r <= q){
					sb.append(pre.id).append("\t").append(post.id).append("\r\n");
					//D.p(sb.length());
					k++;
				}
			}
		}
		D.p(k);
		FileTool ft = new FileTool();
		ft.write(sb, this.dstDir+dstFile);
		return root;
	}
	
	private double _getLinkProbability(_Node pre, _Node post, double p){
		//double p = 0.5;
		boolean flag = false;
//		int i = 0;
		_Node l = pre, r = post;
		//int k=1, v=1;
		//int level = 0;
		while(l.parent != null && r.parent != null){
			//D.p(l.childs+"###"+r.childs);
			//D.p(l.parent.aliases+""+r.parent.aliases);
			if(l.parent.equals(r.parent)){
				flag = true;
				break;
			}
			//p = Math.sqrt(p);
			p = Math.pow(p, 2);
			l = l.parent;
			r = r.parent;
			//k *= l.childs.size();
			//v *= r.childs.size();
		}
//		if(flag == true){
			//D.p(l.index+"##"+r.index);
/*			if(Index.UNIT.equals(l.index) && Index.UNIT.equals(r.index)){
				p = 1;
			}else{
				p = (double)1/l.childs.size()+(double)1/r.childs.size();
			}*/
			//p = (double) Math.pow(1/2, k-1)*Math.pow(1/2, v-1);
//		}else{
//			p = 0;
//		}
		return p;
	}
	/**
	 *  
	 *  @param args
	 */
	public static void main(String[] args) {
		// TODO 自动生成的方法存根
		double p = 0.5;
		SpeciesNet sn = new SpeciesNet();
		sn.createTreeByIndex("mla-1.txt", "net-"+p+".edges", p);
		
		FileTool ft = new FileTool();
		Network net = new Network("../data/species/"+"net-"+p+".edges", NetType.UNDIRECTED, NumberType.INTEGER);
		D.p(net.isConnectedNet());
		Degree d = new Degree();
		ft.write(d.netDegreeDistributionRatio(net), "../data/species/"+"net-"+p+"degreeDisRatio.txt");
	}

}
/*
 * 
 *   SpeciesNet.java
 *    
 *  @author Mervin.Wong  DateTime 2013-8-31 上午11:19:16    
 *  @version 0.4.0
 */
class _Node{
	public SpeciesNet.Index index = null;
	public _Node parent = null;//父节点
	public LinkedList<_Node> childs = null;//子节点
	public String aliases = null;//别名
	
	public int id = 0;//个体的ID

	_Node(){
		
	}
	_Node(SpeciesNet.Index index, _Node p, String aliases){
		this.index = index;
		this.parent = p;
		this.aliases = aliases;
	}
	
	public boolean addChild(_Node e){
		if(this.childs == null){
			this.childs = new LinkedList<_Node>();
		}
		this.childs.add(e);
		return true;
	}
}