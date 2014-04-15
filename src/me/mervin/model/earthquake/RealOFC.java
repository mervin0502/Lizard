package me.mervin.model.earthquake;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import me.mervin.core.NetModel;
import me.mervin.core.Global.NetType;
import me.mervin.core.Global.NumberType;
import me.mervin.util.D;
import me.mervin.util.FileTool;
import me.mervin.util.MapTool;
import me.mervin.util.MathTool;
import me.mervin.util.Pair;
import me.mervin.util.PairList;

/**
 * 提取真实的网络，然后是增加能量，倒塌分配能量
 * @author Mervin.Wong<Mervin0502@163.com>
 * @version 0.4
 *
 */
public class RealOFC extends NetModel {

	/*
	 * 节点的能量阈值
	 */
	private float threshold = 0;
	/*
	 * 当前的能量最大值
	 */
//	private float maxPower = 0;
	/*
	 * 在一次地震中，地震位形更新总次数
	 * ???概念的准确性
	 */
	private int shapeUpdate = 0;
	
	/*
	 * 倒塌的节点在时序上的排列
	 */
	private ArrayList<Number> list = null;
	
	/**
	 * 读取文件，初始化网络
	 * @param fileName 网络文件
	 * @param netType 网络类型：有向（NetType.DIRECTED）五向(NetType.UNDIRECTED)
	 * @param numberType 网络的节点ID类型长整型（NumberType.LONG）和整型（NumberType.INTEGER）
	 */
	public RealOFC(String fileName, NetType netType, NumberType numberType, float threshold){
		super(fileName, netType, numberType);
		this.threshold =threshold;
		this.list = new ArrayList<Number>();
		this.createModelNetwork();
	}
	/**
	 * 
	 *  Function:设置节点的能量阈值
	 * 
	 *  @param threshold 设置节点的能量阈值
	 */
 	public void setParam(float threshold){
		this.threshold =threshold;
	}
	
	/**
	 * 
	 * Function:在网络上初始化节点能量。范围：[0, threshold);
	 * 
	 */
	public void createModelNetwork() {
		// TODO Auto-generated method stub
		Set<Number> nodeSet = this.getAllNodeId();
		Number nodeId = null;
		float temp = 0;
		for(Iterator<Number> it = nodeSet.iterator(); it.hasNext();){
			nodeId = it.next();
			temp = (float) ((MathTool.random()-Math.pow(1, -6))*this.threshold);
			D.p("rand"+temp);
			this.setNodeWeight(nodeId, temp);
/*			if(this.maxPower < temp){
				this.maxPower = temp;
			}*/
		}
	}
	
	/**
	 * 
	 *  Function:进行地震网络的演化仿真
	 * 
	 *  @param cycle 仿真该网络的时间步。
	 */
	public void evolution(int cycle, String dstDir){
		Map<Number, Number> earthquakeSpot = null;//初始化网格矩阵
		Map<Number, Number> collapseMap = null;//一次地震中，倒塌的格点,以及格点的倒塌次数
		
		HashMap<Number, Number> collapseCount = new HashMap<Number, Number>();//倒塌的数量，
		HashMap<Number, Number> collapseSpot = new HashMap<Number, Number>();//倒塌的节点
		HashMap<Number, Number> collapseUpdate = new HashMap<Number, Number>();//震性更新次数
		
		Number temp = null;
		float maxPower = 0;
		
		FileTool f = new FileTool();
		String desPath = dstDir+"log.txt";
		f.clear(desPath);
		for(int i = 0; i < cycle; i++){
			collapseMap = new HashMap<Number, Number>();
			this.shapeUpdate = 0;
			
			maxPower = this._getMaxPower();
			earthquakeSpot = this._increasePower(maxPower);
			if(earthquakeSpot.size() > 0){
				//发生地震
				this._nodeCollapse(earthquakeSpot, collapseMap);
				
				int sum = 0;
				for(Iterator<Number> it = collapseMap.keySet().iterator(); it.hasNext();){
					temp = it.next();
					sum += collapseMap.get(temp).intValue();
				}
				collapseCount.put(i, sum);
				collapseSpot.put(i, collapseMap.size());
				collapseUpdate.put(i, this.shapeUpdate);
				
				f.write("#"+i+"次发生地震时，所有不稳定点倒塌的次数：\t", desPath, true);
				f.write(sum+"\r\n", desPath, true);
				f.write("#"+i+"次发生地震时，所有倒塌点的数量：      \t", desPath, true);
				f.write(collapseMap.size()+"\r\n", desPath, true);
				f.write("#"+i+"次发生地震时，地震位形的更新的总次数：\t", desPath, true);
				f.write(this.shapeUpdate+"\r\n", desPath, true);
				f.write("##############################################################\r\n", desPath, true);
				
			}
		}
		f.write(list, dstDir+"collapse.txt");
	}
	/*
	 * 提取网络文件
	 */
	public void extractNet(String srcFile, String dstFile){
		int[] cols = {1};
		FileTool f = new FileTool();
		List<Number> list = f.read2List(srcFile, NumberType.INTEGER, cols);
		StringBuffer sb = new StringBuffer();
		for (int i = 1; i < list.size(); i++) {
			if(!list.get(i-1).equals(list.get(i))){
				sb.append(list.get(i-1)).append("\t").append(list.get(i)).append("\r\n");
			}
		}
		f.write(sb, dstFile);
		//去重
		//EdgeDeweigh ed = new EdgeDeweigh("../data/Earthquake/zh-sc-net-f-0.125.txt", "../data/Earthquake/zh-sc-net-f-0.125-r.txt");
		//ed.run();
	}
	/**
	 * 由索引号以及分割粒度取经纬度
	 * @param srcFile 源文件
	 * @param dstFile 目标文件
	 * @param f 分割粒度
	 */
	public void renormalizationByIndex(String srcFile, String dstFile, double f){
		Map<Number, double[]> index2LatLon = new HashMap<Number, double[]>(); //将索引号对应到经纬度
		double maxLat = Math.ceil(180/f);
		double maxLon = Math.ceil(360/f);
		int temp = 1;
		for(int i = 0; i< maxLat; i++){
			for(int j = 1; j <= maxLon; j++){
				double[] ll = new double[2];
				ll[0] = i*f+f/2-90;//纬度
				ll[1] = (j-1)*f+f/2-180;//经度
				index2LatLon.put(temp, ll);
				temp++;
			}
		}
		
		//读取文件中的索引号，并找到对应的纬度和经度
		StringBuffer sb = new StringBuffer();
		FileTool ft = new FileTool();
		List<Pair<Number>> net = ft.read2ListPair(srcFile, NumberType.INTEGER);
		Pair<Number> p = null;
		Number l, r;
		double[] ll ;
		for(Iterator<Pair<Number>> it = net.iterator(); it.hasNext();){
			p = it.next();
			l = p.getL();
			ll = index2LatLon.get(l);
			sb.append(ll[0]).append("\t").append(ll[1]).append("\t");
			
			r = p.getR();
			ll = index2LatLon.get(r);
			sb.append(ll[0]).append("\t").append(ll[1]).append("\r\n");
		}
		ft.write(sb, dstFile);
	}
	/*
	 * **************************************************************
	 * private method
	 */
	
	/*
	 * 在网络均匀增加能量之前，找出网络中能量的最大值
	 */
	private float _getMaxPower(){
		Set<Number> nodeSet = this.getAllNodeId();
		
		float maxPower = 0;
		float temp = 0;
		Number nodeId = null;
		for(Iterator<Number> it = nodeSet.iterator(); it.hasNext();){
			nodeId = it.next();
			temp = this.getNodeWeight(nodeId);
			if(maxPower < temp){
				maxPower = temp;
			}
		}
		return maxPower;
	}
	/*
	 * 在上一时间步，完成一次地震。
	 * 在该时间步，网络均匀的增加能量，并且记录下能量不小于阈值的节点
	 */
	private Map<Number, Number> _increasePower(float maxPower){
		Set<Number> nodeSet = this.getAllNodeId();
		Map<Number, Number> collapseNodeSet = new HashMap<Number, Number>();//将要发生倒塌的节点,以及该节点处的能量值
		Number nodeId = null;
		float temp = 0;
		float addPower = this.threshold-maxPower;
		for(Iterator<Number> it = nodeSet.iterator(); it.hasNext();){
			nodeId = it.next();
			temp = this.getNodeWeight(nodeId)+addPower;//能量的均匀增加
			D.p("increase:"+temp);
			this.setNodeWeight(nodeId, temp);
			//记录不小于阈值的节点
			if(temp >= this.threshold){
				//节点的能量不小于阈值
				collapseNodeSet.put(nodeId, temp);
			}
		}
		return collapseNodeSet;
	}
	
	/*
	 * nodeSet中的所有节点发生倒塌，向邻接点分配能量
	 * 记录下能量不小于阈值的节点，进入下一次的倒塌。
	 * oldNodeMap 能量不小于阈值的节点
	 * collapseMap 记录倒塌的节点以及相应的倒塌次数
	 */
	private void _nodeCollapse(Map<Number, Number> oldNodeMap, Map<Number, Number> collapseMap){
		Map<Number, Number> newNodeMap = new HashMap<Number, Number>();//将要发生倒塌的节点
		Number nodeId = null, adjNodeId = null;
		Set<Number> adjNodeSet = null;
		
		float q = 0;//邻接点获取能量的比例（ F(i) = F(i)+q*F(j) ）i是j的邻接点
		float nodeIdPower = 0, temp = 0;
		
		MapTool mt = new MapTool();
		/*
		 * 按照节点的能量排序，降序
		 * 能量越高，认为该节点倒塌时间越早
		 */
		PairList<Number, Number> pl = mt.sort(oldNodeMap, false, false);//按照节点的能量，降序, 认
		//for(Iterator<Number> it = oldNodeMap.keySet().iterator(); it.hasNext();){
			//nodeId = it.next();
		for(int i = 0; i < pl.size(); i++){
			nodeId = pl.getL(i);
			D.p("###"+nodeId);
			list.add(nodeId);//持续的添加倒塌节点，时序
			//nodeIdPower = (Float) oldNodeMap.get(nodeId);//保证节点的同步倒塌
			nodeIdPower = (Float) pl.getR(i);//保证节点的同步倒塌
			adjNodeSet = this.getAdjNodeId(nodeId);//获取邻接点
			q = MathTool.random()*((float)1/adjNodeSet.size());
			
			/*
			 * 节点倒塌
			 */
			this.setNodeWeight(nodeId, 0);//设置nodeId节点的能量为0
			if(collapseMap.containsKey(nodeId)){
				collapseMap.put(nodeId, collapseMap.get(nodeId).intValue()+1);
			}else{
				collapseMap.put(nodeId, 1);
			}
			this.shapeUpdate++;
			
			/*
			 * 分配给邻接点能量
			 */
			D.p("adjNode"+adjNodeSet.size());
			for(Iterator<Number> it2 = adjNodeSet.iterator(); it2.hasNext();){
				adjNodeId = it2.next();
				//邻接点重新分配能量
				temp = this.getNodeWeight(adjNodeId)+q*nodeIdPower;
				this.setNodeWeight(adjNodeId, temp);
				if(temp >= this.threshold){
					//节点的能量不小于阈值
					newNodeMap.put(adjNodeId, temp);
				}
				this.shapeUpdate++;
			}//for

		}//for
		D.p("newNodeMap:"+collapseMap.size());
		if(newNodeMap.size() > 0){
			this._nodeCollapse(newNodeMap, collapseMap);//雪崩
		}
	}
	
	/*
	 * *********************************************************************
	 * 
	 * 
	 */

/*	*//**
	 * @param args
	 *//*
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String root = "../data/Earthquake/";
		File[] fileArr = new File(root+"split2/0.25/").listFiles();
		String netFile = null;
		String dstDir = null;
		for (int i = 0; i < fileArr.length; i++) {
			//File file = fileArr[i];
			try {
				netFile = fileArr[i].getCanonicalPath();
				D.p(fileArr[i].getName());
				String date = fileArr[i].getName().substring(0, fileArr[i].getName().indexOf('.'));
				dstDir = root+"Evolution/"+date+"/";
				RealOFC ofc = new RealOFC(netFile, NetType.UNDIRECTED, NumberType.INTEGER, 1);
				ofc.evolution(500, dstDir);
				ofc.extractNet(dstDir+"collapse.txt", dstDir+date+"-net.txt");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}*/

}
