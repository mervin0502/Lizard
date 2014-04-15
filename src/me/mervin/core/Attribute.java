package me.mervin.core;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import me.mervin.core.Global.AttributeType;



import me.mervin.util.D;


 /**
 *   Attribute.java
 *    
 *  @author Mervin.Wong  DateTime 2013-10-14 下午9:42:45    
 *  @version 0.4.0
 *  *********************************************************
 *  ChangeLog:
 *  == 2013/10/19
 * + method: public boolean containsKey(String key)
 * + method: public boolean containsValue(String value) 
 */
public class Attribute {


	/*
	 * 属性内容
	 */
	private Map<String, String> attrValueMap = null;
	/*
	 * 对应key值的value类型
	 */
	private Map<String, AttributeType> attrTypeMap = null;
	
	/*
	 * 构造函数
	 */
	public Attribute(){
		if(this.attrValueMap == null){
			this.attrValueMap = new HashMap<String, String>(); 
			this.attrTypeMap = new HashMap<String, AttributeType>();
		}
	}

	public Attribute(String k, String v, AttributeType type){
		if(this.attrValueMap == null){
			this.attrValueMap = new HashMap<String, String>(); 
			this.attrTypeMap = new HashMap<String, AttributeType>();
		}
		
		this.attrValueMap.put(k, v);
		this.attrTypeMap.put(k, type);
	}
	
	
	public void set(String k, String v){
		this.attrValueMap.put(k, v);
		this.attrTypeMap.put(k, AttributeType.STRING);		
	}
	public void set(String k, String v, AttributeType type){
		this.attrValueMap.put(k, v);
		this.attrTypeMap.put(k, type);		
	}
	public void set(Map<String, String> attrs){
		this.attrValueMap.putAll(attrs);
	}
	
	public String get(String key){
		return this.attrValueMap.get(key);
		
	}
	
	public boolean containsKey(String key){
		if(this.attrValueMap != null){
			return this.attrValueMap.containsKey(key);
		}else{
			return false;
		}
	}
	
	public boolean containsValue(String value){
		return this.attrValueMap.containsValue(value);
	}
}