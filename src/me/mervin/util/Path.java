package me.mervin.util;

import java.util.LinkedList;
import java.util.List;

/**
 * 表示一条链路
 * 
 * @author mervin
 * **************************************
 * ChangeLog:
 * ==2013/10/19
 *  # class:Link<T> 
 *  # topId->top
 *  # endId->end
 *  # edges->list
 *  
 *  
 *  + attribute: public int length =0;
 *  + method: public void setLength(int l)
 *  + method: public int getLength(int l)
 *  + method: public void addFirst(T item)
 *  + method: public void addLast(T item)
 *  
 *  # attribute: top
 *  # attribute: end
 */
public class Path<T> {


	private LinkedList<T> list = null;
	
	//public int length = this.list.size();
	public T top = null;
	public T end = null;	
	/*************************************************************************
	 * 
	 */
	/*************************************************************************
	/**
	 * 初始化
	 */
	public Path(){
		
	}
	public Path(T top, T end){
		this.top = top;
		this.end = end;
		this.list =  new LinkedList<T>();
	}
	public Path(T top, T end, LinkedList<T> list){
		this.top = top;
		this.end = end;
		this.list = list;
	}
	
	public void setTop(T item){
		this.top = item;
	}
	public void setEnd(T item){
		this.end = item;
	}
	public void setList(LinkedList<T> list){
		this.list = list;
	}
/*	public void setLength(int l){
		this.length = l;
	}*/
	
	
	public T getTop(){
		return this.top;
	}
	public T getEnd(){
		return this.end;
	}
	public LinkedList<T> getList(){
		return this.list;
	}
	public int getLength(){
		return this.list.size();
	}
	/*************************************************************************
	 * 
	 */
	/*************************************************************************/
	public void add(T item){
		if(this.list == null){
			this.list = new LinkedList<T>();
		}
		this.list.add(item);
	}
	public void addFirst(T item){
		if(this.list == null){
			this.list = new LinkedList<T>();
		}
		this.list.addFirst(item);
	}
	public void addLast(T item){
		this.list.addLast(item);
	}
	public void addAll(List<T> list1){
		for(T item:list1){
			this.list.add(item);
		}
	}
	public void remove(T item){
		
		this.list.remove(item);
	}
	public void clear(){
		this.list.clear();
	}
	public boolean contain(T item){
		return this.list.contains(item);
	}
	/*************************************************************************
	 * 
	 */
	/*************************************************************************/
   @Override  
    public String toString() {  
	   	 return this.top + "\t" + this.end+"\t"+this.list.toString();
    }  
  
    @Override  
    public int hashCode() {  
        final int prime = 31;  
        int result = 1;  
        result = prime * result + this.top.hashCode();  
        result = prime * result + this.end.hashCode();  
        return result;  
    }  
  
    @Override  
    public boolean equals(Object obj) {  
        if (this == obj)  
            return true;  
        if (obj == null)  
            return false;  
        if (getClass() != obj.getClass())  
            return false;  
        Path<T> other = (Path<T>) obj; 
        //去掉左右端点相同的边
        if (other.top == other.end)  
            return false; 
        if (!this.top.equals(other.top))  
            return false;  
        if (!this.end.equals(other.end))  
            return false; 
        //如果是无向的，去掉左右相同的
       return true; 	
    }
	
	
}
