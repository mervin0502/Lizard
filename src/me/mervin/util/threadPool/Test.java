package me.mervin.util.threadPool;

import java.util.Random;

import me.mervin.util.D;


 /**
 *   Test.java
 *    
 *  @author Mervin.Wong  DateTime 2014-4-17 上午9:54:04  
 *  @email:mervin0502@163.com  
 *  @version 0.5.0
 */
public class Test {

	/**
	 */
	public Test() {
		// TODO 自动生成的构造函数存根
	}
	
	public static void main(String[] args){
		ThreadPoolManager m = new ThreadPoolManager("simple thread pool");
		m.start();
		Task t = null;
		for(int i = 1; i < 35; i++){
			t = new TestTask(i);
			m.addTask(t);
		}
		if(m.isFinish()){
			D.m("isFinish");
			try {
				Thread.sleep(30000);
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			for(int i = 100; i < 350; i++){
				t = new TestTask(i);
				m.addTask(t);
			}
			m.stop();
		}
		//m.stop();
		
		//D.m();
	}
}

class TestTask extends Task{
	private int i = 0;
	
	public TestTask(int taskId){
		super(taskId);
		this.i = taskId;
	}
	public void run(){
		D.p(i+">>Hello world"+getId());
		try {
			Thread.sleep((int)Math.random()*2000);
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//D.p(i+"<<Hello world"+i);
//		D.p(i);
	}
}
