package me.mervin.project.usr;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


 /**
 *   ThreadPool.java
 *    
 *  @author Mervin.Wong  DateTime 2014年3月24日 下午2:36:56    
 *  @version 0.4.0
 */
public class ThreadPool {

	/**
	 *  
	 *  @param args
	 */
	public static void main(String[] args) {
		// TODO 自动生成的方法存根
		 //创建一个可重用固定线程数的线程池
        ExecutorService pool = Executors.newFixedThreadPool(2);
        //创建实现了Runnable接口对象，Thread对象当然也实现了Runnable接口
        Thread t1 = new MyThread("aaa");
        Thread t2 = new MyThread("bbb");
        Thread t3 = new MyThread("ccc");
        Thread t4 = new MyThread("ddd");
        Thread t5 = new MyThread("eeee");
        //将线程放入池中进行执行
        pool.execute(t1);
        pool.execute(t2);
        pool.execute(t3);
        pool.execute(t4);
        pool.execute(t5);
        //关闭线程池
        pool.shutdown();
	}

}
class MyThread extends Thread {
    private String name = null;
    MyThread(String str){
    	name = str;
    }
    public void run() {
        System.out.println(Thread.currentThread().getName() + "正在执行。。。"+name);
        try {
			Thread.sleep(2000);
			System.out.println(name+"Clocked");
		} catch (InterruptedException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
        
    }
}