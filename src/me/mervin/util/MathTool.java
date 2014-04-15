/*****************************************************************************
 * 
 * Copyright [2013] [Mervin.Wong]
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 *       
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *  
 *****************************************************************************/
package me.mervin.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import me.mervin.core.Global.NumberType;

/**
 * MathTool.java
 *  常用数学工具类
 *@author 王进法<Mervin.Wong>
 *@version 0.4
 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 *  ChangLog:
 *  
 *  ==2013/11/27
 *  + public static union(Set<Number> preSet, Set<Number> postSet)
 */
public class MathTool {
	public static int seed = 3;
	/**
	 *  str2Number
	 *  节点的ID时int 还是 long
	 * @param  numberType Number类型
	 * @param  numStr 字符串
	 * @return Number
	 */
	public static Number str2Number(NumberType numberType, String numStr){
		if(numberType == NumberType.LONG){
			return Long.parseLong(numStr);
		}else if(numberType.equals(NumberType.INTEGER)){
			return Integer.parseInt(numStr);
		}else if(numberType.equals(NumberType.FLOAT)){
			return Float.parseFloat(numStr);
		}else{
			return Double.parseDouble(numStr);
		}
	}
	/**
	 * 
	 *  num1与num2对比
	 *  @param num1
	 *  @param num2
	 *  @param numberType Number类型
	 *  @return boolean
	 */
	public static boolean compare(Number num1, Number num2, NumberType numberType){
		if(numberType.equals(NumberType.INTEGER)){
			if(num1.intValue() > num2.intValue()){
				return true;
			}else{
				return false;
			}
		}else{
			if(num1.longValue() > num2.longValue()){
				return true;
			}else{
				return false;
			}
		}
	}
	/*
	 *  str2Num
	 *  将字符串转换成指定基本类型的数
	 * @param numType
	 * @param numStr
	 * @return
	 */
/*	public static Number str2Num(NumberType numType, String numStr){
		if(numType.equals(NumberType.LONG)){
			return Long.parseLong(numStr);
		} else if (numType.equals(NumberType.FLOAT)) {
			return Float.parseFloat(numStr);
		} else {
			return Integer.parseInt(numStr);
		}
	}*/
	/**
	 *  ip2Int
	 *  ip地址转换成Long整型的数
	 * @param ipStr
	 * @return long
	 */
	public static long ip2Long(String ipStr){
		String[] ipArr = ipStr.split("\\.");
		if(ipArr.length > 1){
			return (Long.parseLong(ipArr[0].toString())<<24)+(Long.parseLong(ipArr[1].toString())<<16)+(Long.parseLong(ipArr[2].toString())<<8)+(Long.parseLong(ipArr[3].toString()));
		}else{
			return Long.parseLong(ipArr[0]);
		}
	}
	
	/**
	 *  将整数的IP地址转换程标准的表示方式 
	 * @param ipInt 
	 * @return String
	 */
	public static StringBuilder ip2Str(Long ipInt){
		StringBuilder ipStr = new StringBuilder();
		ipStr.append((ipInt>>24)).append(".").append((ipInt>>16) & 0xFF).append(".").append((ipInt>>8) & 0xFF).append(".").append(ipInt & 0xFF);
		return ipStr;
	}
	/*
	 * *********************************************************************************
	 * 
	 * 
	**********************************************************************************/
	/**
	 * 
	 *  两个节点集的并集
	 *  @param preSet pre set
	 *  @param postSet post set
	 *  @return Set<Number>
	 */
	public static Set<Number> union(Set<Number> preSet, Number postNodeId){
		Set<Number> temp = new HashSet<Number>();
		temp.addAll(preSet);
		temp.add(postNodeId);
		return temp;
	}
	/**
	 * 
	 *  两个节点集的并集
	 *  @param preSet pre set
	 *  @param postSet post set
	 *  @return Set<Number>
	 */
	public static Set<Number> union(Set<Number> preSet, Set<Number> postSet){
		Set<Number> temp = new HashSet<Number>();
		temp.addAll(preSet);
		temp.addAll(postSet);
		return temp;
	}
	/**
	 *  
	 *  两个节点集的交集
	 * @param preSet
	 * @param postSet
	 * @return Set<Number>
	 */
	public static Set<Number> intersection(Set<Number> preSet, Set<Number> postSet){
		HashSet<Number> intersection = new HashSet<Number>();
		for (Iterator<Number> iterator = preSet.iterator(); iterator.hasNext();) {
			Number nodeId = (Number) iterator.next();
			if(postSet.contains(nodeId)){
				//D.p("intersection:"+nodeId);
				intersection.add(nodeId);
			}
		}
		return intersection;
	}
	/**
	 *  
	 *  求preSet postSet网络节点集的差集，preSet-postSet
	 * @param preSet
	 * @param postSet
	 * @return Set<Number>
	 */
	public static Set<Number> subtraction(Set<Number> preSet, Set<Number> postSet){
		Set<Number> intersection = MathTool.intersection(preSet, postSet);
		HashSet<Number> subtraction = new HashSet<Number>();
		
		for (Iterator<Number> iterator = preSet.iterator(); iterator.hasNext();) {
			Number nodeId = (Number) iterator.next();
			if(!intersection.contains(nodeId)){
				//D.p("subtraction:"+nodeId);
				subtraction.add(nodeId);
			}
		}
		return subtraction;
	}
	
	/**
	 *  
	 *  统计map中相同value数值出现的频数
	 * @param map 统计的数据
	 * @return Map<Number, Number>
	 */
	public static Map<Number, Number> frequency(Map<Number, Number> map){
		Map<Number, Number> frequency = new HashMap<Number, Number>();
		Number nodeId = null;
		Number value = null;
		for(Iterator<Number> it = map.keySet().iterator(); it.hasNext();){
			nodeId = it.next();
			value = map.get(nodeId);
			if(frequency.containsKey(value)){
				frequency.put(value, frequency.get(value).longValue()+1);
			}else{
				frequency.put(value, 1);
			}
		}
		return frequency;
	}
	/**
	 *  
	 *  计算节点的分布频率
	 * @param map
	 * @return Map<Number, Number>
	 */
	public static Map<Number, Number> ratio(Map<Number, Number> map){
		Map<Number, Number> ratioMap = new HashMap<Number, Number>();
		Set<Number> degreeSet = map.keySet();
		Number degree = 0;
		int totalNode = 0;
		for (Iterator<Number> iterator = degreeSet.iterator(); iterator.hasNext();) {
			 degree= (Number) iterator.next();
			totalNode += map.get(degree).intValue();
		}
		for (Iterator<Number> iterator2 = degreeSet.iterator(); iterator2.hasNext();) {
			 degree= (Number) iterator2.next();
			 ratioMap.put(degree, map.get(degree).floatValue()/totalNode);
		}
		return ratioMap;
	}
	
	/**
	 * 按照PairList<Number, Number>对象的存储顺序来求其累积值
	 *  
	 *  @param pl 比例分布
	 *  @return PairList<Number,Number>
	 */
	public static PairList<Number,Number> accumulate(PairList<Number, Number> pl){
		PairList<Number, Number> pl2 = new PairList<Number, Number>();
		double sum = 0;
		for(int i = 0; i < pl.size(); i++){
			sum += pl.getR(i).doubleValue();
			//map.put(pl.getL(i), sum);
			pl2.add(pl.getL(i), sum);
		}
		return pl2;
	}	
	/**
	 * 
	 *  Function:计算degreeMap中的平均值
	 * 
	 *  @param degreeMap
	 *  @return float
	 */
	public static float average(Map<Number, Number> degreeMap){
		int count = 0;
		int totalNodes = 0;
		Collection<Number> c = degreeMap.keySet();
		Number key = null,
				value = null;
		
		for (Iterator<Number> iterator = c.iterator(); iterator.hasNext();) {
			key = (Number) iterator.next();
			value = degreeMap.get(key);
			count += key.intValue()*value.intValue();
			totalNodes += value.intValue();
		}
		return count/(float)totalNodes;
	}
	/**
	 *  
	 *  集合中的最大值
	 * @param collection
	 * @return int
	 */
	public static int max(Collection<Number> collection){
		int max = 0;
		Integer temp = null;
		for (Iterator<Number> iterator = collection.iterator(); iterator.hasNext();) {
			temp = (Integer) iterator.next();
			if(max < temp){
				max = temp;
			}
			
		}
		return max;
	}
	/**
	 * 
	 *  Function:获取set集合中的最小值
	 * 
	 *  @param set
	 *  @return int
	 */
	public static int min(Set<Integer> set){
		int min = Integer.MAX_VALUE;
		Integer temp = null;
		for (Iterator<Integer> iterator = set.iterator(); iterator.hasNext();) {
			temp = (Integer) iterator.next();
			if(min > temp){
				min = temp;
			}
		}
		return min;
	}
	/**
	 *  
	 *  产生一个0.0~1.0之间的随机数
	 * @return float
	 */
	public static float random(){
		float p = 0;
		p = (float) Math.random();
		return p;
	}
	/**
	 *  
	 *  产生一个min~max之间的随机数,0 <= nextInt(n) < n
	 *  有问题，待修正
	 * @param min
	 * @param max
	 * @return Number
	 */
	public static Number random(Number min, Number max){
		Number randNumber = 0;
		Random random = new Random(System.currentTimeMillis());
		//randNumber = random.nextInt(max.intValue())%(max.intValue()-min.intValue()+1)+min.intValue();
		randNumber = random.nextInt(max.intValue()-min.intValue())+min.intValue();
		return randNumber;
	}
	/**
	 *  
	 *  产生一个min~max之间的随机数
	 * @param min
	 * @param max
	 * @param numberType
	 * @return Number
	 */
	public static Number random(Number min, Number max, NumberType numberType){
		Number randNumber = 0;
		Random random = new Random(System.currentTimeMillis()+(seed++));
		if(numberType.equals(NumberType.LONG)){
			randNumber = random.nextLong()%(max.longValue()-min.longValue()+1)+min.longValue();
		}else {
			randNumber = random.nextInt(max.intValue())%(max.intValue()-min.intValue()+1)+min.intValue();
		}
		return randNumber;
	}

	/**
	 * 根据索引号以及分割粒度去对应格子的纬度和经度
	 *  
	 *  @param index 索引号
	 *  @param f 分割粒度
	 *  @return Pair<Double>
	 */
	public static Pair<Double> index2LatLon(int index, double f){
		Pair<Double> ll = null;
		//划分的格子
		//int rows = (int) Math.ceil(180/f);//行数
		int cols = (int)Math.ceil(360/f);//列数
		
		int curRow = index/cols+1;//当前所在行
		int curCol = index%cols;//当前所在列
		
		double lat = curRow*f-f/2-90;
		double lon = curCol*f-f/2-180;
		if(lat > 90){
			lat = 90;
		}
		if(lon > 180){
			lon = 180;
		}
		ll = new Pair(lat, lon);
		return ll;
	}
	
	/**
	 * 
	 *  根据纬度和经度以及划分粒度来确定格子的索引号
	 *  索引号：有左到右，有下到上
	 *  @param lat 纬度
	 *  @param lon 经度
	 *  @param f 分割粒度
	 *  @return int
	 */
	public static int latLon2Index(double lat, double lon, double f){
		int index = 0;
		
		int cols = (int) Math.ceil(360/f);//最大列数
		
		int curRow = (int) Math.floor((lat+90)/f);//当前行
		int curCol = (int) Math.ceil((lon+180)/f);//当前列
		index = curRow*cols+curCol;
		return index;
	}

}
