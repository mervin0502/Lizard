package me.mervin.util.threadPool;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import me.mervin.util.D;


 /**
 *   ThreadPoolManager.java
 *    
 *  @author Mervin.Wong  DateTime 2014-4-17 上午8:43:03  
 *  @email:mervin0502@163.com  
 *  @version 0.5.0
 */
public class ThreadPoolManager {
	
	/**
	 * the thread number in the pool
	 */
	public static final int TRHEAD_COUNT=2;
	
	public enum Status{
		NEW, RUNNING, TERMINATED
	}
	/**
	 * all the thread worker in the pool
	 */
	public List<ThreadPoolWorker> workerList = new ArrayList<ThreadPoolWorker>();
	
	private static ThreadPoolManager instance = ThreadPoolManager.getInstance();
	/*
	 * the task number finished
	 */
	private static int taskCounter = 0;
	/*
	 * the thread pool status:New, Running, Terminated 
	 */
	private Status status = Status.NEW;
	
	/*
	 * the task queue(FIFO)
	 */
	private List<Task> taskQueue = Collections.synchronizedList(new LinkedList<Task>());
	
	protected Boolean isStop = new Boolean(false);
	 /**
	 */
	public ThreadPoolManager() {
		// TODO 自动生成的构造函数存根
	}	
	/**
	 * @param name
	 */
	public ThreadPoolManager(String name) {
		//super(name);
		// TODO 自动生成的构造函数存根
		//extends the method of  parent class, set is daemon?
		//setDaemon(true);
/*		if(ThreadPoolManager.TRHEAD_COUNT == 0 || !this.status.equals(Status.NEW)){
			try{
				throw new Exception();
			}catch(Exception e){
				e.printStackTrace();
			}
			return;
		}//if
*/		ThreadPoolWorker t = null;
		for(int i = 0; i < ThreadPoolManager.TRHEAD_COUNT; i++){
			t = new ThreadPoolWorker(this, i+1);
			this.workerList.add(t);
		}
	}
	

	public static synchronized ThreadPoolManager getInstance(){
		if(ThreadPoolManager.instance == null){
			return new ThreadPoolManager();
		}
		return ThreadPoolManager.instance;
	}
	/**
	 * start the thread pool
	 */
	public synchronized void start(){
		
		// create the thread
		for(Thread t:workerList){
			t.start();
		}
		this.status = Status.RUNNING;
	}
	
	/**
	 * close the thread pool
	 */
	public  void stop(){
		
	/*	if(!this.taskQueue.isEmpty()){
			synchronized (this.isStop) {
				try {
					this.isStop.wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		for(ThreadPoolWorker w:workerList){
			if(w.isRunning() && !w.isWaiting()){
				synchronized (this.isStop) {
					try {
						this.isStop.wait();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}else{
				w.stopWorker();
			}
		}*/
		
		/*
		boolean flag ;
		while(true){
			if(taskQueue.isEmpty()){
				do{
					flag = true;
					for(ThreadPoolWorker w:workerList){
						D.p(w.getId()+"###"+w.isRunning()+"####"+w.isWaiting());
						if(w.isRunning()){
							if(w.isWaiting()){
								w.stopWorker();
							}else{
								flag = false;
							}
						}
					}
				}while(!flag);
				break;
			}
	
		}*/
		this.status = Status.TERMINATED;
		
		workerList.clear();
		taskQueue.clear();
		
		//extend the parent class, interrupt all the threads in  ThreadGroup
		//interrupt();
	}
	/**
	 * is running ?
	 */
	public boolean isRunning(){
		return status == Status.RUNNING;
	}
	/**
	 * add a task
	 *  
	 *  @param task
	 */
	public  void addTask(Task task){
		synchronized(this.taskQueue){
			task.setId(++taskCounter);
			taskQueue.add(task);
			//wake the taskQueue
			D.p("notify");
			taskQueue.notifyAll();
		}
	}
	
	public  Task getTask(){
		synchronized(this.taskQueue){
			while(taskQueue.isEmpty()){
				try{
					D.p("wait");
					taskQueue.wait();
				}catch (InterruptedException e){
					e.printStackTrace();
				}
			}//while
			if(this.status.equals(Status.TERMINATED)){
				return null;
			}
			return taskQueue.remove(0);
		}
	}

}
