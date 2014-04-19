package me.mervin.util.threadPool;

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
		for(int i = 0; i < 5; i++){
			t = new TestTask(i);
			m.addTask(t);
		}
		m.stop();
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
	/*	try {
			if(i % 2 == 0){
				Thread.sleep(1000);
			}else{
				Thread.sleep(1000);
			}
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		D.p(i+"<<Hello world"+i);
//		D.p(i);
	}
}
