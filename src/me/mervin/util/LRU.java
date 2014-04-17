/**
 * 
 */
package me.mervin.util;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import sun.security.action.GetBooleanAction;

/**
 * 	LRU.java
 * Least Recently Used algorithm
 * this is a page replacement algorithm and implement the cache performance
 * @author Mervin Time: 2014年4月17日 下午7:39:06
 * @email:mervin0502@163.com
 * @version 0.5.0
 */
public class LRU<K, V> extends LinkedHashMap<K, V>{

	/**
	 * maximal number cached
	 */
	public static final int MAX_CAPACITY = 20;
	
	/*
	 * have cached number currently 
	 */
	private  int capacity = 0;
	
	private final Lock lock = new ReentrantLock();
	/**
	 * constructor function
	 */

	public LRU(){
		super(16, 0.75f, true);
	}
	/**
	 * 
	 * @param capacity
	 */
	public LRU(int capacity){
		super(16, 0.75f, true);
		this.capacity = capacity;
	}
	/**
	 * 
	 * @param initialCapacity initial capacity
	 * @param loadFactor 
	 * @param isLRU true:visit order; false: insert order
	 */
	public LRU(int initialCapacity, float loadFactor, boolean isLRU){
		super(initialCapacity, loadFactor, isLRU);
		capacity = MAX_CAPACITY;
	}
	/**
	 * 
	 * @param initialCapacity initial capacity
	 * @param loadFactor 
	 * @param isLRU true:visit order; false: insert order
	 * @param capacity: this cache capacity
	 */
	public LRU(int initialCapacity, float loadFactor, boolean isLRU, int capacity){
		super(initialCapacity, loadFactor, isLRU);
		this.capacity = capacity;
	}
	/**
	 * @param k
	 * @return V
	 */
	public V get(Object k){
		lock.lock();
		try{
			return super.get(k);
		}finally{
			lock.unlock();
		}
	}
	/**
	 * @param k
	 * @param v
	 * @return V
	 */
	public V put(K k, V v){
		lock.lock();
		try{
			return super.put(k, v);
		}finally{
			lock.unlock();
		}
	}
	/*
	 * 
	 */
	protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
		return size() > this.capacity;
		
	}
	
	public static void main(String[] args) {  
		  
	}  
	
	

}
