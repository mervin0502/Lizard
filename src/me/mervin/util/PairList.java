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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * PairList.java
 * 
 *@author 王进法<Mervin.Wong>
 *@version 
 *@Date 2013-1-28下午6:50:49
 */
/*********************************************************************************
 *
 *
 **********************************************************************************/

public class PairList<L, R> {
	private ArrayList<L> valuesL = new ArrayList<L>();
	private ArrayList<R> valuesR = new ArrayList<R>();
	private int length = 0;
	
	public PairList(){
	}
	
	/**
	 *  clear
	 *  清空list
	 */
	public void clear(){
		this.valuesR.clear();
		this.valuesL.clear();
	}
	/**
	 *  
	 *  判断是否含有valueL valueR数值对
	 * @param valueL
	 * @param valueR
	 * @return boolean
	 */
	public boolean contains(L valueL, R valueR){
		if(this.getIndex(valueL, valueR) != -1){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 *  
	 *  判断是否为空
	 * @return boolean
	 */
	public boolean isEmpty(){
		if(this.length == 0){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 *  
	 *  获取索引i的左值
	 * @param i
	 * @return L
	 */
	public L getL(int i){
		return this.valuesL.get(i);
	}
	/**
	 *  
	 *  取索引i的右值
	 * @param i
	 * @return R
	 */
	public R getR(int i){
		return this.valuesR.get(i);
	}
	/**
	 *  
	 *  获取left＝l的所有的右值
	 * @param l
	 * @return ArrayList<R>
	 */
	public ArrayList<R> getRs(L l){
		ArrayList<R> r = new ArrayList<R>();
		for(int i = 0 ; i < this.valuesL.size(); i++){
			if(this.valuesL.get(i).equals(l)){
				r.add(this.valuesR.get(i));
			}
		}
		return r;
	}
	/**
	 *  
	 *  获取right = r 的所有的左值
	 * @param r
	 * @return ArrayList<L>
	 */
	public ArrayList<L> getLs(R r){
		ArrayList<L> l = new ArrayList<L>();
		for(int i = 0 ; i < this.valuesR.size(); i++){
			if(this.valuesR.get(i).equals(r)){
				l.add(this.valuesL.get(i));
			}
		}
		return l;		
	}	
	/**
	 * 
	 * 获取所有的右值 
	 *  @return ArrayList<R>
	 */
	public ArrayList<R> getRs(){
		return this.valuesR;
	}
	/**
	 * 
	 * 获取所有的左值 
	 *  @return  ArrayList<T>
	 */
	public ArrayList<L> getLs(){
		return this.valuesL;
	}
	/**
	 *  
	 *  移除valueL valueR数值对
	 * @param valueL
	 * @param valueR
	 */
	public void remove(L valueL, R valueR){
		int i = 0;
		if((i = this.getIndex(valueL, valueR)) != -1 ){
			this.valuesL.remove(i);
			this.valuesR.remove(i);
			this.length--;
		}
	}
	/**
	 *  
	 *  移出left = l的所有值
	 * @param l
	 */
	public void removeByL(L l){
		Iterator<L> itL = this.valuesL.iterator();
		Iterator<R> itR = this.valuesR.iterator();
		L tempL = null;
		while(itL.hasNext()){
			tempL = itL.next();
			itR.next();
			if(tempL.equals(l)){
				itL.remove();
				itR.remove();
				this.length--;
			}
		}
	}
	/**
	 *  
	 *  移出所有right = r 的值
	 * @param r
	 */
	public void removeByR(R r){
		Iterator<L> itL = this.valuesL.iterator();
		Iterator<R> itR = this.valuesR.iterator();
		R tempR = null;
		while(itR.hasNext()){
			tempR = itR.next();
			itL.next();
			if(tempR.equals(r)){
				itR.remove();
				itL.remove();
				this.length--;
			}
		}		
	}
	/**
	 *  
	 *  添加valueL valueR数值对
	 * @param valueL
	 * @param valueR
	 */
	public void add(L valueL, R valueR){
		this.valuesL.add(valueL);
		this.valuesR.add(valueR);
		this.length++;
	}
	/**
	 * 
	 *  添加valueL valueR数值对
	 *  @param pl PairList<Number, Number>
	 */
	public void add(PairList<L, R> pl){
		int size = pl.size();
		for(int i = 0; i < size; i++){
			//D.p(pl.getL(i)+"####"+pl.getR(i));
			this.valuesL.add(pl.getL(i));
			this.valuesR.add(pl.getR(i));
			this.length++;
		}
	}
	/**
	 *  size
	 *  
	 * @return int
	 */
	public int size(){
		return this.length;
	}
	/**
	 * 
	 * 将pairList转换成Set<Pair<Number>> ,L 与R相同
	 *  @return Set<Pair<L>>
	 */
	//public Set<Pair<Number>> list2Set(){
	@SuppressWarnings("unchecked")
	public Set<Pair<L>> list2Set(){
		Set<Pair<L>> pairSet = new HashSet<Pair<L>>();
		for(int i = 0; i < this.length; i++ ){
			pairSet.add(new Pair<L>(this.getL(i), (L) this.getR(i)));
		}
		return pairSet;
	}

	/**
	 *  getIndex
	 *  获取valueL,valueR 所在的索引号
	 * @param valueL
	 * @param valueR
	 * @return int
	 */
	public int getIndex(L valueL, R valueR){
		int i = 0, flag = -1;
		for (Iterator<L> iterator = this.valuesL.iterator(); iterator.hasNext();) {
			if(iterator.next().equals(valueL) && this.valuesR.get(i).equals(valueR)){
				flag = i;
				break;
			}
			i++;
		}
		return flag;
	}
	
}
