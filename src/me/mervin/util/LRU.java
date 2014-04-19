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
	
	public Map.Entry<K, V> eldestItem = null;
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
		if(size() > this.capacity){
			this.eldestItem = eldest;
			return true;
		}else{
			this.eldestItem = null;
			return false;
		}
	}
	
	public static void main(String[] args) {  
		   LinkedHashMap<String, String> map = new LRU<String, String>(3, 0.75f, true, 3);  
		    map.put("a", "a"); //a  a  
		    map.put("b", "b"); //a  a b  
		    map.put("c", "c"); //a  a b c  
		    map.put("a", "a"); //   b c a       
		    map.put("d", "d"); //b  b c a d  
		    map.put("a", "a"); //   b c d a  
		    map.put("b", "b"); //   c d a b       
		    map.put("f", "f"); //c  c d a b f  
		    map.put("g", "g"); //c  c d a b f g  
		  
		    map.get("d"); //c a b f g d  
		    for (Entry<String, String> entry : map.entrySet()) {  
		        System.out.print(entry.getValue() + ", ");  
		    }  
		    System.out.println();  
		  
		    map.get("a"); //c b f g d a  
		    for (Entry<String, String> entry : map.entrySet()) {  
		        System.out.print(entry.getValue() + ", ");  
		    }  
		    System.out.println();  
		  
		    map.get("c"); //b f g d a c  
		    for (Entry<String, String> entry : map.entrySet()) {  
		        System.out.print(entry.getValue() + ", ");  
		    }  
		    System.out.println();  
		  
		    map.get("b"); //f g d a c b  
		    for (Entry<String, String> entry : map.entrySet()) {  
		        System.out.print(entry.getValue() + ", ");  
		    }  
		    System.out.println();  
		  
		    map.put("h", "h"); //f  f g d a c b h  
		    for (Entry<String, String> entry : map.entrySet()) {  
		        System.out.print(entry.getValue() + ", ");  
		    }  
		    System.out.println();  
	}  

}
