package me.mervin.model.earthquake;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import me.mervin.util.D;
import me.mervin.util.Pair;
import me.mervin.util.PairList;
import me.mervin.util.PairMap;
import me.mervin.util.PairSet;

/************************************************************************************
 * 一次地震（雪崩）：在网格矩阵在上一次所有格点能量小于阈值后，重新分配能量（所有的格点均匀增加能量），
 * 然后所有能量不小于阈值的格点发生倒塌，重新分配邻接点的能量，如果还有能量不小于阈值，则进一步发送倒塌，
 * 直到所有的格点能量小于阈值。
 * 
 * 总体思想：先就所有发生地震的格点重新分配能量，然后在判断是否还有发生地震的可能
 * 1,初始化网格矩阵
 * 2,遍历网格矩阵，判断是否有格点的能量不小于阈值，如果是则发生地震，转入3，如果无，转入4
 * 3,发生地震的所有(i,j)重新分配能量，转入2
 * 4，在一个周期内，网格矩阵的每个格点增加能量，转入2
 *************************************************************************************/
public class OFC{
	/*
	 * 初始化网格的行，列
	 */
	private int cols = 50;
	private int rows = 50;
	/*
	 * 网格矩阵
	 */
	private float[][] grid = null;
	/*
	 * 格点能量的分配比率
	 */
	private float v = 0;
	/*
	 * 模型中的阈值
	 */
	private float threshold = 0;
	/*
	 * 网格矩阵中的最大值，及其坐标
	 */
	private float max = 0;
	//private int rowByMax = 0;
	//private int colByMax = 0;
	/*
	 * 模型的测量值
	 */
	/**
	 * 在一次地震中，所有格点的倒塌次数 
	 */
	//public int collpaseCount = 0;
	/**
	 * 在一次地震中，倒塌的格点数量
	 */
	//public int collapseSpot = 0;
	/**
	 * 在一次地震中，地震位形更新总次数
	 * ???概念的准确性
	 */
	private int shapeUpdate = 0;
	
	/************************************************************************************
	 * 
	 * init object
	 * 
	 *************************************************************************************/
	/**
	 * 初始化网络
	 */
	public OFC() {
	}

	public  OFC(float v, float threshold) {
		this.v = v;
		this.threshold = threshold;
	}
	public  OFC(int cols, int rows, float v, float threshold) {
		this.cols = cols;
		this.rows = rows;
		this.v = v;
		this.threshold = threshold;
	}
	
	public void setGrid(int cols, int rows){
		this.cols = cols;
		this.rows = rows;
	}
	public void setV(float v){
		this.v = v;
	}
	public void setThreshold(float threshold){
		this.threshold = threshold;
	}
	
	public float getV(){
		return this.v;
	}
	public float getThreshold(){
		return this.threshold;
	}
	/************************************************************************************
	 * 
	 * public method
	 * 
	 *************************************************************************************/
	/**
	 * 模拟地震
	 * @param cycle 模拟地震的次数
	 */
	public void script(int cycle){
		PairMap<Integer, Float> earthquakeSpot = this._initGrid();//初始化网格矩阵
		PairMap<Integer, Integer> collapse = null;//一次地震中，倒塌的格点,以及格点的倒塌次数
		
		HashMap<Number, Number> collapseCount = new HashMap<Number, Number>();
		HashMap<Number, Number> collapseSpot = new HashMap<Number, Number>();
		HashMap<Number, Number> collapseUpdate = new HashMap<Number, Number>();
		
		Pair<Integer> temp = null;
		for(int i = 0; i < cycle; i++){
			D.p("##############"+i);
			collapse = new PairMap<Integer, Integer>();
			this.shapeUpdate = 0;
			if(this._happenEarthquake(earthquakeSpot, collapse)){
				//true 发生了一次地震
				int sum = 0;
				for(Iterator<Pair<Integer>> it = collapse.keySet().iterator(); it.hasNext();){
					temp = it.next();
					sum += collapse.get(temp);
				}
				collapseCount.put(i, sum);
				collapseSpot.put(i, collapse.size());
				collapseUpdate.put(i, this.shapeUpdate);
				
				D.p(i+"###"+sum+"#####"+collapse.size()+"###"+this.shapeUpdate);
			}
			earthquakeSpot = this._increasePower();
			if(earthquakeSpot.size() > 0){
				D.p("##############");
			}
		}
	}
	
	/************************************************************************************
	 * 
	 * private  method 
	 * 
	 *************************************************************************************/
	/*
	 * 初始化网络（矩阵）
	 *  返回的是所有格点的能量不小于阈值的行列值
	 */
	private PairMap<Integer, Float> _initGrid(){
		this.grid = new float[this.rows][this.cols];
		Random rand = new Random(System.currentTimeMillis());
		PairMap<Integer, Float> earthquakeSpot = new PairMap<Integer, Float>();//所有格点的能量不小于阈值的行列值
		float value = 0;
		for(int i = 0; i < this.rows; i++){
			for(int j = 0; j < this.cols; j++){
				value = (rand.nextFloat()*this.threshold);//在0~this.threshold 之间取随机数
				this.grid[i][j] = value;
				/*
				 * 标记最大的格点
				 */
				if(this.max < value){
					this.max = value;
					//this.rowByMax = i;
					//this.colByMax = j;
				}
				/*
				 * 标记格点的能量不小于阈值的点
				 */
				if(value >= this.threshold){
					earthquakeSpot.put(new Pair<Integer>(i, j), value);
				}
			}
		}
		return earthquakeSpot;
	}
	/*
	 * 在一个周期内均匀的增加格点的能量
	 * ???保证公平，随机的选取网格中的一个格点
	 */
	private PairMap<Integer, Float> _increasePower(){
		PairMap<Integer, Float> earthquakeSpot = new PairMap<Integer, Float>();//所有格点的能量不小于阈值的行列值
		float value = 0;
		for(int i = 0; i < this.rows; i++){
			for(int j = 0; j < this.cols; j++){
				value = this.grid[i][j];
				value += this.threshold-this.max;//增加能量
				this.grid[i][j] = value;
				//D.p(this.grid[i][j]+"$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
				/*
				 * 标记最大的格点
				 */
				if(this.max < value){
					this.max = value;
				}
				/*
				 * 标记格点的能量不小于阈值的点
				 */
				if(value >= this.threshold){
					earthquakeSpot.put(new Pair<Integer>(i, j), value);
				}
			}
		}
		return earthquakeSpot;
	}
	
	/*
	 * 发生一次地震, 调整位形,  标记格点的能量不小于阈值的点
	 * earthquakeSpot 至少不为空
	 */
	private boolean _happenEarthquake(PairMap<Integer, Float> oldEarthquakeSpot, PairMap<Integer, Integer> collapse){
		int i, j;
		float value = 0;
		boolean flag = true;
		Pair<Integer> spot = null;
		PairMap<Integer, Float> newEarthquakeSpot = new PairMap<Integer, Float>();//所有格点的能量不小于阈值的行列值
		//D.p("0@@@"+newEarthquakeSpot.size()+collapse.size()+"####");
		//for(int k = 0; k < oldEarthquakeSpot.size(); k++){
		for(Iterator<Pair<Integer>> it = oldEarthquakeSpot.keySet().iterator(); it.hasNext();){
			//i = oldEarthquakeSpot.getL(k);
			//j = oldEarthquakeSpot.getR(k);
			//oldEarthquakeSpot.
			//D.p("1@@@"+newEarthquakeSpot.size());
			spot = it.next();
			i = spot.getL();
			j = spot.getR();
			//value = this.grid[i][j];
			value = oldEarthquakeSpot.get(spot);
			D.p("%%%%%%%%"+value+"");
			/*
			 * 记录该格点倒塌
			 *
			 */
			
			spot = new Pair<Integer>(i,j);
			if(collapse.containsKey(spot)){
				collapse.put(spot, collapse.get(spot)+1);
				D.p(i+"****"+j+"$$$$"+collapse.size());
			}else{
				collapse.put(spot, 1);
				D.p(i+"&&&&&&&&&&&&&&&&&&"+j+"$$$$"+collapse.size());
			}
			/*
			 * 该点发生倒塌，调整4个邻接点
			 */
			if(i-1 >=0 ){
				//上
				if((this.grid[i-1][j] += value*this.v) >= this.threshold){
					D.p(this.grid[i-1][j]+"~~~~~~~~~~~~~~~~~~~~~~~~"+this.grid[i-1][j]);
/*					try {
						//Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}*/
					newEarthquakeSpot.put(new Pair<Integer>(i-1,j), this.grid[i-1][j]);
				}
				/*
				 * 所有格点的最大值
				 */
/*				if(this.grid[i-1][j] > this.max){
					this.max = this.grid[i-1][j];
				}*/
				/*
				 * 地震的位形更新次数
				 */
				this.shapeUpdate++;
			}
			
			if(i+1 < this.rows){
				//下
				if((this.grid[i+1][j] += value*this.v) >= this.threshold){
					newEarthquakeSpot.put(new Pair<Integer>(i+1,j), this.grid[i+1][j]);
				}
				/*
				 * 所有格点的最大值
				 */
/*				if(this.grid[i+1][j] > this.max){
					this.max = this.grid[i+1][j];
				}*/
				/*
				 * 地震的位形更新次数
				 */
				this.shapeUpdate++;
			}
			
			if(j-1 >= 0){
				//左
				if((this.grid[i][j-1] += value*this.v) >= this.threshold){
					newEarthquakeSpot.put(new Pair<Integer>(i,j-1), this.grid[i][j-1]);
				}
				/*
				 * 所有格点的最大值
				 */
/*				if(this.grid[i][j-1] > this.max){
					this.max = this.grid[i][j-1];
				}*/
				/*
				 * 地震的位形更新次数
				 */
				this.shapeUpdate++;
			}
			
			if(j+1 < this.cols){
				//右
				if((this.grid[i][j+1] += value*this.v) >= this.threshold){
					newEarthquakeSpot.put(new Pair<Integer>(i, j+1), this.grid[i][j+1]);
				}
				/*
				 * 所有格点的最大值
				 */
/*				if(this.grid[i][j+1] > this.max){
					this.max = this.grid[i][j+1];
				}*/
				/*
				 * 地震的位形更新次数
				 */
				this.shapeUpdate++;
			}
			//格点本身=0
			this.grid[i][j] = 0;
			/*
			 * 地震的位形更新次数
			 */
			this.shapeUpdate++;
			D.p("^^^^^^^^^^^^"+value+"   "+this.grid[i][j]);
		}//for
		//D.p("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		//D.p("@@@"+newEarthquakeSpot.size());
/*		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		if(newEarthquakeSpot.size() > 0){
			this._happenEarthquake(newEarthquakeSpot, collapse);//递归，直到所有的格点的能量小于阈值
		}
		if(oldEarthquakeSpot.size() > 0){
			flag  =  true;
		}else{
			flag = false;
		}
		return flag;
	}

}
