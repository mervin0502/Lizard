package me.mervin.model.Wireless;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Vector;

import me.mervin.core.Network;
import me.mervin.core.Global.NetType;
import me.mervin.core.Global.NumberType;
import me.mervin.module.feature.Degree;
import me.mervin.util.D;
import me.mervin.util.FileTool;
import me.mervin.util.MathTool;
import me.mervin.util.Pair;


 /**
 *   MUCR.java
 *  非均匀分簇路由策略  
 *  @author Mervin.Wong  DateTime 2013-12-19 下午6:33:53    
 *  @version 0.4.0
 *  
 *  
 */
public class MUCR {

	private Map<Pair<Number>, Sensor> sensors = null;
	private Map<Pair<Number>, Cluster> clusters = null;
	/*
	 * 格子的大小
	 */
	private int cellSize = 1;//格子大小（正方形）	
	/*
	 * 传感器数量
	 */
	public int sensorNum = 0;// 传感器数量
	
	public double p = 1;
	public double e = 100;
	/*
	 * 簇的长度，是cellSize的倍数
	 */
	public double d = 0;
	
	/*
	 * 衰减比率
	 */
	public double r1 = 0;
	public double r2 = 0;
	/**
	 */
	public MUCR() {
		// TODO 自动生成的构造函数存根
	}
	public MUCR(int sensorNum, double d){
		this.sensorNum = sensorNum;
		this.d = d;
	}
	public MUCR(int sensorNum, double d, double p, double e){
		this.sensorNum = sensorNum;
		this.d = d;
		this.p = p;
		this.e = e;
	}

	
	/**
	 * 
	 *  
	 *  @param n 执行次数
	 */
	public void script(String dstDir, int n){
		/*
		 * 将sensorNum个传感器分布在大小为cellSize的格子上
		 */
		this._sensorDis(this.p, this.e);
		/*
		 * 划分簇
		 */
		this._clustering(this.d);
		/*
		 * 获取簇内成员
		 */
		for(Cluster c:this.clusters.values()){
			this._getClusterMember(c, this.d);
		}
		
		/*
		 * 遍历
		 */
		int i = 0;
		String dstFile = null;
		do{
			/*
			 * 获取簇首
			 */
			for(Cluster c:this.clusters.values()){
				this._setClusterHead(c);
			}
			/*
			 * 获取簇的下一跳
			 */
			for(Cluster c:this.clusters.values()){
				this._setNextHop(c);
			}
			/*
			 * 遍历 
			 */
			dstFile = dstDir +i+".txt";
			this._travese(dstFile);
			
			/*
			 * 能量衰减
			 */
			for(Cluster c:this.clusters.values()){
				this._reviseSensorEnergy(c, this.r1, this.r2);
			}
			
			i++;
		}while(i < n);
		
	}
	
	/**
	 * 将多次结果叠加成网络
	 *  
	 *  @param srcDir
	 *  @param dstFile
	 */
	public void createNet(String srcDir, String dstFile){
		FileTool ft = new FileTool();
		File[] fileArr = new File(srcDir).listFiles();
		Map<Pair<Number>, Integer> link = new HashMap<Pair<Number>, Integer>();
		Pair<Number> pair = null;
		String line = null;
		String[] lineArr = null;
		BufferedReader read = null;
		Number l = null, r = null;
		for(File f:fileArr){
			try {
				D.p(f.getAbsoluteFile());
				read = new BufferedReader(new FileReader(f));
				while((line = read.readLine()) != null){
					//D.p(line);
					if(line.charAt(0) != '#'){
						lineArr = line.split("\\s+");
						l = Integer.parseInt(lineArr[0]);
						r = Integer.parseInt(lineArr[1]);
						pair = new Pair<Number>(l, r, false);
						if(link.containsKey(pair)){
							link.put(pair, link.get(pair)+1);
						}else{
							link.put(pair, 1);
						}
					}
				}
			} catch (FileNotFoundException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			} catch (IOException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
			
			
		}//for
		StringBuffer sb = new StringBuffer();
		for(Pair<Number> p:link.keySet()){
			//sb.append(p.getL()).append("\t").append(p.getR()).append("\t").append(link.get(p)).append("\r\n");
			sb.append(p.getL()).append("\t").append(p.getR()).append("\r\n");
		}
		ft.write(sb, dstFile);
	}
	/*
	 * ************************************************
	 * private method
	 */
	/*
	 * 无线传感器的分布：均匀(概率为1)和非均匀（概率小于1大于0）
	 * Idea:
	 * 	1,构造一个格子背景(正方形，以sink节点为源点的平面直角坐标系)
	 *  2,每个格子可容纳一个传感器
	 *  3,以一定要的概率的分布传感器
	 *  
	 *  int n 传感器的数量
	 *  double p 传感器分布的概率值
	 *  double e 默认的能量值
	 *  return void
	 */
	private void _sensorDis(double p, double e){
		this.sensors = new HashMap<Pair<Number>, Sensor>();
		Pair<Number> pair = null;
		Sensor sensor = null;
		
		double q = 0;
		boolean flag = true;
		/*
		 * 当前的坐标值
		 */
		double x = 0;
		double y = 0;
		
		int k = 1;
		int i = 0, j = 0;
		Random rand = new Random();//随机数
		/*
		 * 以坐标的源点为中心，顺时针旋转，遍历每层的格子
		 * 且0号为sink节点
		 */
		while(i < this.sensorNum){
			x = (double)k*this.cellSize/2;
			y = (double)k*this.cellSize/2;
			/*
			 * 右侧
			 */
			for(j = 0; j < k;j++){
				q = rand.nextDouble();
				if(q <= p){
					pair = new Pair<Number>(x, y, false);
					//D.p(x+" "+y);
					sensor = new Sensor(i+1, x, y, e);//sensor id 从1开始，0为sink节点
					this.sensors.put(pair, sensor);
					i++;
				}
				y -= this.cellSize;
				//分布完成
				if(i >= this.sensorNum){
					flag = false;
					break;
				}
			}
			if(flag == false){
				break;
			}
			/*
			 * 下侧
			 */
			for(j = 0; j < k; j++){
				q = rand.nextDouble();
				if(q <= p){
					pair = new Pair<Number>(x, y, false);
					sensor = new Sensor(i+1, x, y, e);
					this.sensors.put(pair, sensor);
					//D.p(x+" "+y);
					i++;
				}
				x -= this.cellSize;
				//分布完成
				if(i >= this.sensorNum){
					flag = false;
					break;
				}				
			}
			if(flag == false){
				break;
			}
			/*
			 * 左侧
			 */
			for(j = 0; j < k;j++){
				q = rand.nextDouble();
				if(q <= p){
					pair = new Pair<Number>(x, y, false);
					sensor = new Sensor(i+1, x, y, e);
					this.sensors.put(pair, sensor);
					//D.p(x+" "+y);
					i++;
				}
				y += this.cellSize;
				//分布完成
				if(i >= this.sensorNum){
					flag = false;
					break;
				}
			}
			if(flag == false){
				break;
			}
			/*
			 * 上侧
			 */
			for(j = 0; j < k; j++){
				q = rand.nextDouble();
				if(q <= p){
					pair = new Pair<Number>(x, y, false);
					sensor = new Sensor(i+1, x, y, e);
					this.sensors.put(pair, sensor);
					//D.p(x+" "+y);
					i++;
				}
				x += this.cellSize;
				//分布完成
				if(i >= this.sensorNum){
					flag = false;
					break;
				}				
			}
			if(flag == false){
				break;
			}
			
			k +=2;
			
		}//while
		
		//D.p(sensors);
	}
	
	/*
	 * 分簇并选择簇首
	 * double d 簇的大小（正方形）且为cellSize的倍数
	 */
	private void _clustering(double d){
		this.clusters = new HashMap<Pair<Number>, Cluster>();
		Pair<Number> pair = null;
		Cluster cluster = null;
		
		boolean flag = true;
		/*
		 * 当前的坐标值
		 */
		double x = 0;
		double y = 0;
		
		int k = 1;
		int i = 0, j = 0;
		/*
		 * 以坐标的源点为中心，顺时针旋转，遍历每层的格子
		 * 
		 */
		while(i < this.sensorNum){
			x = (double)k*d/2;
			y = (double)k*d/2;
			/*
			 * 右侧
			 */
			for(j = 0; j < k;j++){
				cluster = new Cluster(i+1, x, y, d);
				pair = new Pair<Number>(x, y, false);
				this.clusters.put(pair, cluster);
				i += this._getClusterMember(cluster, d);
				y -= d;
				//分布完成
				if(i >= this.sensorNum){
					flag = false;
					break;
				}
			}
			if(flag == false){
				break;
			}
			/*
			 * 下侧
			 */
			for(j = 0; j < k; j++){
				cluster = new Cluster(i+1, x, y, d);
				pair = new Pair<Number>(x, y, false);
				this.clusters.put(pair, cluster);
				i += this._getClusterMember(cluster, d);
				x -= d;
				//分布完成
				if(i >= this.sensorNum){
					flag = false;
					break;
				}				
			}
			if(flag == false){
				break;
			}
			/*
			 * 左侧
			 */
			for(j = 0; j < k;j++){
				cluster = new Cluster(i+1, x, y, d);
				pair = new Pair<Number>(x, y, false);
				this.clusters.put(pair, cluster);
				i += this._getClusterMember(cluster, d);
				y += d;
				//分布完成
				if(i >= this.sensorNum){
					flag = false;
					break;
				}
			}
			if(flag == false){
				break;
			}
			/*
			 * 上侧
			 */
			for(j = 0; j < k; j++){
				cluster = new Cluster(i+1, x, y, d);
				pair = new Pair<Number>(x, y, false);
				this.clusters.put(pair, cluster);
				i += this._getClusterMember(cluster, d);
				x += d;
				//分布完成
				if(i >= this.sensorNum){
					flag = false;
					break;
				}				
			}
			if(flag == false){
				break;
			}
			
			k +=2;
			
		}//while		
	}
	
	/*
	 * 获取簇内的成员
	 */
	private int _getClusterMember(Cluster c, double d){
		int sensorNumInCluster = 0;
		/*
		 * 左下角到右上角
		 */
		double x1 = c.x - d/2;
		double y1 = c.y - d/2 ;
		
		Pair<Number> pair = null;
		for(double x2 = x1 + (double)this.cellSize/2; x2 < x1+d; x2 +=this.cellSize){
			for(double y2 = y1 + (double)this.cellSize/2; y2 < y1+d; y2 +=this.cellSize){
				pair = new Pair<Number>(x2, y2, false);
				if(this.sensors.containsKey(pair)){
					c.member.add(this.sensors.get(pair));
					sensorNumInCluster++;
				}
			}
		}//for
		
		return sensorNumInCluster;
	}
	
	/*
	 * 获取簇首
	 */
	private void _setClusterHead(Cluster c){
		double maxE = 0;
		for(Sensor s:c.member){
			if(maxE < s.e){
				maxE = s.e;
			}
		}
		List<Sensor> maximalSensor = new ArrayList<Sensor>();
		for(Sensor s:c.member){
			if(s.e == maxE){
				maximalSensor.add(s);
			}
		}
		int index = MathTool.random(0, maximalSensor.size()).intValue();
		c.head = maximalSensor.get(index);
	}
	
	/*
	 * 获取簇的下一跳
	 */
	private void _setNextHop(Cluster c){
		double x = c.x;
		double y = c.y;
		double d = c.d;
		
		Pair<Number> pair = null;
		/*
		 * 右上角 对角线
		 */
		if(x == y && x >= 0 && y >= 0){
			D.p(x+" "+y);
			if(x-d/2 == 0 && y-d/2 == 0){
				c.next = null;
			}else{
				pair = new Pair<Number>(x-d, y-d, false);
				c.next = this.clusters.get(pair);
			}
		}
		/*
		 * 左下角 对角线
		 */
		if(x == y && x < 0 && y < 0){
			D.p(x+" "+y);
			if(x+d/2 == 0 && y+d/2 == 0){
				c.next = null;
			}else{
				pair = new Pair<Number>(x+d, y+d, false);
				c.next = this.clusters.get(pair);
			}
		}
		/*
		 * 左上角 对角线
		 */
		if(x == -y && x <= 0 && y >= 0){
			D.p(x+" "+y);
			if(x+d/2 == 0 && y-d/2 == 0){
				c.next = null;
			}else{
				pair = new Pair<Number>(x+d, y-d, false);
				c.next = this.clusters.get(pair);
			}
		}
		/*
		 * 右下角 对角线
		 */
		if(x == -y && x > 0 && y < 0){
			D.p(x+" "+y);
			if(x-d/2 == 0 && y+d/2 == 0){
				c.next = null;
			}else{
				pair = new Pair<Number>(x-d, y+d, false);
				c.next = this.clusters.get(pair);
			}
		}
		
		double x1, x2, x3, y1, y2, y3;
		/*
		 * 上
		 */
		if(y >= 0 && y > x && y > -x){
			x1 = x;
			y1 = y - d;
			x2 = x1 - d;
			y2 = y1;
			x3 = x1 + d;
			y3 = y1;
			
			c.next = this._getNearestCluster(x1, y1, x2, y2, x3, y3);
		}
		/*
		 * 右
		 */
		if(x >= 0 && y < x && y > -x){
			x1 = x - d;
			y1 = y;
			x2 = x1;
			y2 = y1 + d;
			x3 = x1;
			y3 = y1 - d;
			
			c.next = this._getNearestCluster(x1, y1, x2, y2, x3, y3);			
		}
		/*
		 * 下
		 */
		if(y < 0 && y < x && y< -x){
			x1 = x;
			y1 = y + d;
			x2 = x1 - d;
			y2 = y1;
			x3 = x1 + d;
			y3 = y1;
			
			c.next = this._getNearestCluster(x1, y1, x2, y2, x3, y3);
		}
		/*
		 * 左
		 */
		if(x < 0 && y > x && y < -x){
			x1 = x + d;
			y1 = y;
			x2 = x1;
			y2 = y1 + d;
			x3 = x1;
			y3 = y1 - d;
			
			c.next = this._getNearestCluster(x1, y1, x2, y2, x3, y3);				
		}
		
		
	}
	

	/*
	 * 能量衰减, r1 r2取值为0～1
	 * double r1 簇首的衰减比率
	 * double r2 簇内其他成员的衰减比率
	 */
	private void _reviseSensorEnergy(Cluster c, double r1, double r2){
		for(Sensor s: c.member){
			if(s.equals(c.head)){
				s.e = s.e*(1-r1);
			}else{
				s.e = s.e*(1-r2);
			}
		}
	}
	
	/*
	 * 遍历关系，并输出
	 */
	private void _travese(String dstFile){
		//D.p(this.clusters.size());
		StringBuffer sb = new StringBuffer();
		StringBuffer sb1 = new StringBuffer();
		//sb1.append("#");
		for(Cluster c:this.clusters.values()){
			/*
			 * 有簇成员到簇首的关系
			 */
			for(Sensor s:c.member){
				if(!s.equals(c.head)){
					sb.append(c.head.id).append("\t").append(s.id).append("\r\n");
				}
			}
			/*
			 * 有本簇到下一跳
			 */
			if(c.next != null){
				sb.append(c.head.id).append("\t").append(c.next.head.id).append("\r\n");
				sb1.append("#").append(c.head.id).append("\t").append(c.next.head.id).append("\r\n");
			}else{
				sb.append(c.head.id).append("\t").append(0).append("\r\n");
				sb1.append("#").append(c.head.id).append("\t").append(0).append("\r\n");
			}
			
			
		}//for
		//sb1.append("\r\n");
		sb.insert(0, sb1);
		new FileTool().write(sb, dstFile);
	}
	/*
	 * *****************************************************
	 * 
	 */
	 /*
	 *  
	 *  @param x1
	 *  @param y1
	 *  @param x2
	 *  @param y2
	 *  @param x3
	 *  @param y3
	 *  @return
	 */
	private Cluster _getNearestCluster(double x1, double y1, double x2, double y2, double x3, double y3) {
		Pair<Number> pair = null;
		Cluster c = null, c1 = null, c2 = null, c3 =null;
		double d1 = 0, d2 = 0, d3 = 0;
		pair = new Pair<Number>(x1, y1, false);
		c1 = this.clusters.get(pair);
		pair = new Pair<Number>(x2, y2, false);
		c2 = this.clusters.get(pair);
		pair = new Pair<Number>(x3, y3, false);
		c3 = this.clusters.get(pair);
		
		if(c1 != null){
			d1 = Math.sqrt(Math.pow(c1.head.x, 2)+Math.pow(c1.head.y, 2));
		}else{
			d1 = Integer.MAX_VALUE;
		}
		if(c2 != null){
			d2 = Math.sqrt(Math.pow(c2.head.x, 2)+Math.pow(c2.head.y, 2));
		}else{
			d2 = Integer.MAX_VALUE;
		}
		if(c3 != null){
			d3 = Math.sqrt(Math.pow(c3.head.x, 2)+Math.pow(c3.head.y, 2));
		}else{
			d3 = Integer.MAX_VALUE;
		}
		if(d1 <= d2 && d1 <= d3){
			c = c1;
		}
		if(d2 < d1 && d2 <= d3){
			c = c2;
		}
		if(d3 < d1 && d3 < d2){
			c = c3;
		}
		return c;
	}
	/**
	 *  
	 *  @param args
	 */
	public static void main(String[] args) {
		// TODO 自动生成的方法存根
		FileTool ft = new FileTool();
		MUCR mucr = new MUCR();
		mucr.sensorNum = 10000;//传感器节点的数量
		mucr.d = 4;//簇的长度(正方形)
		mucr.e = 100;//能量的最大值
		mucr.p = 1;//传感器分布概率
		mucr.r1 = 0.1;//簇首的衰减概率
		mucr.r2 = 0.05;//非簇首的衰减概率
		int n = 100;//循环次数
		String dir = "../data/wireless/w1/net/"; //文件目录
		mucr.script(dir, n);
		String dstFile = "../data/w1/net.txt";
		mucr.createNet(dir, dstFile);
		Network net = new Network(dstFile, NetType.UNDIRECTED, NumberType.INTEGER);
		Degree d = new Degree(net);
		ft.write(d.netDegreeDistributionRatio(), "../data/w_r.txt");
	}

}

/*
 * 传感器类
 * 
 */
class Sensor{
	public int id = 0;
	/*
	 * 该传感器的坐标
	 */
	public double x = 0;
	public double y = 0;
	/*
	 * 该传感器的能量值
	 */
	public double e = 0;
	
	Sensor(){
		
	}
	Sensor(int id, double x, double y, double e){
		this.id = id;
		this.x = x;
		this.y = y;
		this.e = e;
	}
	
}
/*
 * 簇类
 */
class Cluster{
	/*
	 * 簇的ID
	 */
	public int id = 0;
	/*
	 * 簇首
	 */
	public Sensor head = null;
	/*
	 * 簇的下一跳
	 * 
	 */
	public Cluster next = null;
	/*
	 * 簇的位置（目前假设簇为正方形，在此只记录中心点的坐标值）
	 */
	public double x = 0;
	public double y = 0;
	
	/*
	 * 簇的边长度
	 */
	public double d = 0;
	/*
	 * 簇成员
	 */
	public Vector<Sensor> member = new Vector<Sensor>(); 
	
	Cluster(){
		
	}
	Cluster(int id, double x, double y, double d){
		this.id = id;
		this.x = x;
		this.y = y;
		this.d = d;
	}
}