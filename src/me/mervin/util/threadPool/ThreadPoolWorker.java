package me.mervin.util.threadPool;

import java.util.concurrent.locks.ReentrantLock;

import me.mervin.util.D;


 /**
 *   ThreadPoolWorker.java
 *    
 *  @author Mervin.Wong  DateTime 2014-4-17 上午9:04:06  
 *  @email:mervin0502@163.com  
 *  @version 0.5.0
 */
public class ThreadPoolWorker extends Thread {

	private int id = -1;
	//this thread is work?
	private boolean isRunning = true;
	//this thread is waiting the next task
	private boolean isWaiting = true;
	//this thread belong to the thread pool
	private ThreadPoolManager manager = null;
	
	/**
	 */
	public ThreadPoolWorker() {
		// TODO 自动生成的构造函数存根
	}

	public ThreadPoolWorker(int threadId){
		super(""+threadId);
		this.id = threadId;
	}
	/**
	 * @param name
	 */
	public ThreadPoolWorker(String name) {
		super(name);
		// TODO 自动生成的构造函数存根
	}
	public ThreadPoolWorker(ThreadPoolManager manager, int threadId){
		super(""+threadId);
		this.manager = manager;
		this.id = threadId;
	}
	public void stopWorker(){
		this.isRunning = false;
	}
	public boolean isRunning(){
		return this.isRunning;
	}
	public boolean isWaiting(){
		return this.isWaiting;
	}
	/**
	 * 
	 */
	public void run(){
		while(this.isRunning){
			Task runTask = manager.getTask();
			
			if(runTask == null){
				break;
			}
			this.isWaiting = false;
			runTask.run();
			this.isWaiting = true;
			synchronized (manager.isStop) {
				manager.isStop.notify();
			}
			D.m(getName()+"####"+runTask.getId());
		}//while
		D.p("exit");
	}

}
