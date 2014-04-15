package me.mervin.util;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Map.Entry;

import me.mervin.core.Global.ArithmeticOperators;

/**
 * 
 *   MapTool.java
 *    
 *  @author Mervin.Wong  DateTime 2013-10-12 下午8:57:18    
 *  @version 0.4.0
 *  **********************************<br/>
 *  +2013-10-12 20:57:18 add map2List method 
 */
public class MapTool {
	
	/*
	 * 需要操作的map
	 */
	private Map<Number, Number> map;

	public MapTool(){
		
	}
	public MapTool(Map<Number, Number> map){
		this.map = map;
	}
	/*
	 * **********************************************************************
	 * 
	 * public method
	 * 
	 * **********************************************************************
	 */
	/*
	 * *******************************************************************
	 * 提取map中value值满足一定条件的值
	 */
	/**
	 * 查询value满足条件的值
	 *  
	 *  @param ao 算术运算符
	 *  @param op 运算子
	 *  @return Map<Number, Number>
	 */
	public Map<Number, Number> select(ArithmeticOperators ao, int op){
		return this.select(this.map, ao, op);
	}
	/**
	 * 查询value满足条件的值
	 *  
	 *  @param map 要操作的对象
	 *  @param ao 算术运算符
	 *  @param op 运算子
	 *  @return Map<Number, Number>
	 */
	public Map<Number, Number> select(Map<Number, Number> map, ArithmeticOperators ao, int op){
		Map<Number, Number> result = new HashMap<Number, Number>();
		
		Number nodeId = null;
		Number value = null;
		switch(ao){
		case G:
			//去value大于op的key=>value
			for(Iterator<Number> it = map.keySet().iterator(); it.hasNext();){
				nodeId = it.next();
				value = map.get(nodeId);
				if(value.longValue() > op){
					result.put(nodeId, value);
				}
			}
			break;
		case L:
			//去value小于op的key=>value
			for(Iterator<Number> it = map.keySet().iterator(); it.hasNext();){
				nodeId = it.next();
				value = map.get(nodeId);
				if(value.longValue() < op){
					result.put(nodeId, value);
				}
			}
			break;
		case E:
			//去value等于op的key=>value
			for(Iterator<Number> it = map.keySet().iterator(); it.hasNext();){
				nodeId = it.next();
				value = map.get(nodeId);
				if(value.longValue() == op){
					result.put(nodeId, value);
				}
			}
			break;
		case NE:
			//去value不等于op的key=>value
			for(Iterator<Number> it = map.keySet().iterator(); it.hasNext();){
				nodeId = it.next();
				value = map.get(nodeId);
				if(value.longValue() != op){
					result.put(nodeId, value);
				}
			}
			break;
		case GE:
			//去value不小于op的key=>value
			for(Iterator<Number> it = map.keySet().iterator(); it.hasNext();){
				nodeId = it.next();
				value = map.get(nodeId);
				if(value.longValue() >= op){
					result.put(nodeId, value);
				}
			}
			break;
		case LE:
			//去value不大于op的key=>value
			for(Iterator<Number> it = map.keySet().iterator(); it.hasNext();){
				nodeId = it.next();
				value = map.get(nodeId);
				if(value.longValue() <= op){
					result.put(nodeId, value);
				}
			}
			break;
		}
		return result;
	}
	
	/*
	 * *******************************************************************
	 * 对map进行排序，按key or value
	 * 将Map<Number, Number>,按照key 或者value值进行排序   
	 */
	/**
	 * 进行排序
	 *  
	 *  @return PairList<Number, Number>
	 */
	public PairList<Number, Number> sort(){
		PairList<Number, Number> pl = new PairList<Number, Number>();
		Set<Map.Entry<Number, Number>> sortedSet = this._sort(true, true);
		sortedSet.addAll(this.map.entrySet());
		for (Iterator<Entry<Number, Number>> iterator = sortedSet.iterator(); iterator.hasNext();) {
			Entry<Number, Number> entry = (Entry<Number, Number>) iterator.next();
			pl.add(entry.getKey(), entry.getValue());
		}
		return pl;
	}
	/**
	 * 进行排序
	 *  @param map 需要排序的map
	 *  @return PairList<Number, Number>
	 */
	public PairList<Number, Number> sort(Map<Number, Number> map){
		PairList<Number, Number> pl = new PairList<Number, Number>();
		Set<Map.Entry<Number, Number>> sortedSet = this._sort(true, true);
		sortedSet.addAll(map.entrySet());
		for (Iterator<Entry<Number, Number>> iterator = sortedSet.iterator(); iterator.hasNext();) {
			Entry<Number, Number> entry = (Entry<Number, Number>) iterator.next();
			pl.add(entry.getKey(), entry.getValue());
		}
		return pl;
	}	
	/**
	 * 进行排序
	 *  
	 *  @param key true: 对key; flase:对value值排序
	 *  @param asc true：升序；false:降序
	 */
	public PairList<Number, Number> sort(Map<Number, Number> map, final boolean key, final boolean asc){
		PairList<Number, Number> pl = new PairList<Number, Number>();
		Set<Map.Entry<Number, Number>> sortedSet = this._sort(key, asc);
		sortedSet.addAll(map.entrySet());
		for (Iterator<Entry<Number, Number>> iterator = sortedSet.iterator(); iterator.hasNext();) {
			Entry<Number, Number> entry = (Entry<Number, Number>) iterator.next();
			pl.add(entry.getKey(), entry.getValue());
		}
		return pl;
	}
	
	/**
	 * 
	 * 将map转换成PairList类型 
	 *  @param map Map对象
	 *  @return PairList<Number, Number>
	 */
	public PairList<Number, Number> map2List(Map<Number, Number> map){
		return this.map2List(map, true);
	}
	
	/**
	 * 
	 * 将map转换成PairList类型 
	 *  @param map Map对象
	 *  @param isKey2L true:l=key, r =value; false:l=value,r=key;
	 *  @return PairList<Number, Number>
	 */
	public PairList<Number, Number> map2List(Map<Number, Number> map, boolean isKey2L){
		PairList<Number, Number> pl = new PairList<Number, Number>();
		Number k = null;
		Number v = null;
		for(Iterator<Number> it = map.keySet().iterator(); it.hasNext();){
			k = it.next();
			v = map.get(k);
			if(isKey2L){
				pl.add(k, v);
			}else{
				pl.add(v, k);
			}
		}
		return pl;
	}
	/*
	 * **********************************************************************
	 * 
	 * private method
	 * 
	 * **********************************************************************
	 */
	/*
	 * 设置按key或者value排序：升序或者降序
	 */
	private SortedSet<Map.Entry<Number, Number>> _sort(final boolean key, final boolean asc){
		SortedSet<Map.Entry<Number, Number>> sortedSet = new TreeSet<Map.Entry<Number, Number>>(
	            new Comparator<Map.Entry<Number, Number>>() {
	                @Override
	                public int compare(Map.Entry<Number, Number> e1, Map.Entry<Number, Number> e2) {
	                	
	                	if(key){
	                		//对key排序
	                		double k1 = e1.getKey().doubleValue();
	                		double k2 = e2.getKey().doubleValue();
	                		if(asc){
	                			//升序
	                			if(k1 > k2){
	                				return 1;
	                			}else{
	                				return -1;
	                			}
	                		}else{
	                			//降序
	                			if(k1 < k2){
	                				return 1;
	                			}else{
	                				return -1;
	                			}	                			
	                		}
	                	}else{
	                		//对value排序
	                		double k1 = e1.getValue().doubleValue();
	                		double k2 = e2.getValue().doubleValue();
	                		if(asc){
	                			//升序
	                			if(k1 > k2){
	                				return 1;
	                			}else{
	                				return -1;
	                			}
	                		}else{
	                			//降序
	                			if(k1 < k2){
	                				return 1;
	                			}else{
	                				return -1;
	                			}	                			
	                		}	                		
	                	}
	                }
	            });
		return sortedSet;
	}
	/**
	 *  
	 *  @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
