package me.mervin.util;

public class E extends Exception {
	
	/**
	 * 构造函数
	 */
	public E(){
		super();
	}
	/**
	 * 错误信息
	 * @param str 错误信息
	 */
	public E(String str){
		super(str);
	}
	/**
	 * 错误信息及原因
	 * @param str 错误信息
	 * @param t 错误原因
	 */
	public E(String str, Throwable t){
		super(str, t);
	}
}
