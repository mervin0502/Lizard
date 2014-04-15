package me.mervin.util.plugin;

import me.mervin.core.Network;

/**
 * Renormalization
 * 网络重正化
 * @author mervin
 *******************************************************************************
 * boxCover()//盒子覆盖法
 */
public class Renormalization {

	private Network net = null;
	
	/**
	 * 初始化
	 */
	public Renormalization(){}
	public Renormalization(Network net){
		this.net = net;
	}
	
	/***************************************************************************
	 * 
	 * public method
	 * 
	 * 
	 ***************************************************************************/
	/**
	 *  
	 *  采用盒子覆盖法来重正化网络
	 * @param l 盒子尺寸
	 * @param i 重正化次数
	 * @return Network
	 */
	public Network boxCover(int l, int i){
		return this.boxCover(this.net, l, i);
	}
	/**
	 *  
	 *  采用盒子覆盖法来重正化网络
	 * @param net 重正化的网络
	 * @param l 盒子尺寸
	 * @param i 重正化次数
	 * @return Network
	 */
	public Network boxCover(Network net, int l, int i){
		
		
		return null;
	}
	/***************************************************************************
	 * 
	 * private method
	 * 
	 * 
	 ***************************************************************************/
	/***************************************************************************
	 * 
	 * static method
	 * 
	 * 
	***************************************************************************/
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
