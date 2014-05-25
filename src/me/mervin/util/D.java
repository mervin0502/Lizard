/*********************************************************************************
 *
 *
 *
 **********************************************************************************/
package me.mervin.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;


/**
 * D.java
 * 
 *@author 王进法<Mervin.Wong>
 *@version 
 *@Date 2013-1-18上午10:23:40
 */
/*********************************************************************************
 *
 *
 *
 **********************************************************************************/

public class D {
	public static void out(Object o){
		System.out.println(o.toString());
	}
	
	public static void warning(Object o){
		System.out.println(o.toString());
	}
	public static void p(Object o){
		if(o != null){
		System.out.println(o.toString());
		}else{
			System.out.println("null");
		}
	}
	public static void f(Object o){
		if(o != null){
			System.out.print(o.toString());
		}else{
			System.out.print("null");
		}
	}
	
	public static void e(){
		System.exit(0);
	}
	public static void e(Object o){
		System.out.println("Exception:"+o.toString());
	}

	/**
	 *  m
	 *  测试标记
	 */
	public static void m(){
		System.out.println("Marked");
	}
	/**
	 *  m
	 *  测试标记
	 */
	public static void m(String str){
		System.out.println("Marked:"+str);
	}
	/**
	 *  m
	 *  测试标记
	 */
	public static void m(Number str){
		System.out.println("Marked:"+str.toString());
	}
		
	/**
	 *  s
	 *  程序成功运行！
	 */
	public static void s(){
		System.out.println("Success!");
	}
	/**
	 *  
	 *  程序成功运行！
	 * @param  str
	 */
	public static void s(String str){
		System.out.println("Success:"+str);
	}
	/**
	 *  
	 *  程序成功运行！
	 * @param  str
	 */
	public static void s(Number str){
		System.out.println("Success:"+str.toString());
	}	
	/**
	 *  
	 *  False
	 */
	public static void f(){
		System.out.println("False!");
	}
	public static void list(int[] intArr){
		for(int n: intArr){
			System.out.print(n+" ");
		}
		System.out.println();
	}
	public static void list(String[] strArr){
		for(String str : strArr){
			System.out.print(str+"##");
		}
		System.out.println();
	}
	
	public static void list(Map<Object, Object> map){
		Set<Object> set = map.keySet();
		for (Iterator<Object> iterator = set.iterator(); iterator.hasNext();) {
			Object object = (Object) iterator.next();
			D.p(object+"\t"+map.get(object));
		}
	}
	

	public static String t(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(new Date());
	}

}
