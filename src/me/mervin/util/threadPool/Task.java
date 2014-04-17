package me.mervin.util.threadPool;


 /**
 *   Task.java
 *    
 *  @author Mervin.Wong  DateTime 2014-4-17 上午8:49:34  
 *  @email:mervin0502@163.com  
 *  @version 0.5.0
 */
public abstract class Task implements Runnable {
	private int id = 0;
	public Task(){
		
	}
	/**
	 */
	public Task(int taskId) {
		// TODO 自动生成的构造函数存根
		//super(taskId+"");
		this.id = taskId;
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		// TODO 自动生成的方法存根

	}
	
	public void setId(int taskId){
		this.id = taskId;
	}
	public int getId(){
		return this.id;
	}

}
