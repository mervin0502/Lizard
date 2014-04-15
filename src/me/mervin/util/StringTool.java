package me.mervin.util;

import java.util.Iterator;
import java.util.Map;

public class StringTool<T1, T2> {
	public StringTool(){
		
	}
	
	/*
	 *  split
	 *  字符分割
	 */
	public String[] split(String str){
		return null;
	}
	
	/**
	 * 将Map类型的数据转换成StringBuffer
	 */
	public StringBuffer map2str(Map<T1,T2> map){
		StringBuffer sb = new StringBuffer();
		T1 t1 = null;
		T2 t2 = null;
		for(Iterator<T1> it=map.keySet().iterator();it.hasNext();){
			t1 = it.next();
			t2 = map.get(t1);
			sb.append(t1.toString()).append("\t").append(t2.toString()).append("\r\n");
		}
		return sb;
	}
}
