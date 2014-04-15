package me.mervin.util;

import me.mervin.core.Global.NetType;
import me.mervin.core.Global.PairFormat;

/*********************************************************************************
 *
 * 说明: 表示的类型为T1的节点对，以及在该节点对上的T2对象
 *
 *********************************************************************************
 */
public class Pair<T1> implements Comparable<Object>{
	//T1 Long/Integer
	/*
	 * 左边元素
	 */
	private T1 l;
	/*
	 * 右边元素
	 */
	private T1 r;
	/*
	 * 当左右的形式
	 * false (l,r) != (r,l)
	 * yes (l,r) == (r,l)
	 */
	//private PairFormat pf = PairFormat.INDIRECT;
	private boolean lrType = true;

	/**
	 * 构造函数
	 * @param l 左元素
	 * @param r 右元素
	 */
	public Pair(T1 l, T1 r){
		this.l = l;
		this.r = r;

	}
	/**
	 * 构造函数
	 * @param l 左元素
	 * @param r 右元素
	 * @param type 节点对形式:false:(l, r)!=(r, l) true:(l, r)==(r,l)
	 */
	public Pair(T1 l, T1 r, boolean type){
		this.l = l;
		this.r = r;
		this.lrType = type;
	}
	// GET SET方法
	public T1 getL(){
		return this.l;
	}
	public void setL(T1 l){
		this.l = l;
	}
	public T1 getR(){
		return this.r;
	}
	public void setR(T1 r){
		this.r = r;
	}
	
	/********************************************************************
	 *  Override
	 * 
	********************************************************************/
   @Override  
    public String toString() {  
	   return this.l.toString() + "\t" + this.r.toString();
    }  
  
    @Override  
    public int hashCode() {  
        final int prime = 31;  
        int result1 = 1, result2 = 1;  
        result1 = prime * result1 + this.l.hashCode();  
        result1 = prime * result1 + this.r.hashCode(); 
        result2 = prime * result2 + this.r.hashCode();  
        result2 = prime * result2 + this.l.hashCode(); 
        
        return result1+result2;  
    }  
  
    @Override  
    public boolean equals(Object obj) {
        if (this == obj)  
            return true;  
        if (obj == null)  
            return false;  
        if (getClass() != obj.getClass())  
            return false;  
        @SuppressWarnings("unchecked")
		Pair<T1> other = (Pair<T1>) obj; 
        /*
         * 特殊情况
         */
        if(this.lrType){
            //如果是无向的，去掉左右相同的
            if(this.l.equals(other.r) && this.r.equals(other.l))
            	return true;
        }
        
        /*
         * 最基本的情况
         */
        if (!this.l.equals(other.l))  
            return false;  
        if (!this.r.equals(other.r))  
            return false;         
       return true; 	
    }
    
    
	@Override
	public int compareTo(Object obj) {
		// TODO Auto-generated method stub
		
		return 0;
	}
    
}