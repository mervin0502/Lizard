package me.mervin.project.usr.mervin.AS;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import me.mervin.core.Network;
import me.mervin.core.Global.NetType;
import me.mervin.core.Global.NumberType;
import me.mervin.module.feature.Coreness;
import me.mervin.module.feature.PCB;
import me.mervin.module.feature.Path;
import me.mervin.util.D;
import me.mervin.util.FileTool;

/**
 * 
 *   ASAPL.java
 *   AS网络的平均最短路径长度   
 *  @author Mervin.Wong  DateTime 2013-8-21 上午10:40:54    
 *  @version 0.4.0
 */
public class ASAPL implements Runnable{
	
	//private String srcDir = null;
	/**
	 *  
	 *  @param args
	 */
	public static void main(String[] args) {
		// TODO 自动生成的方法存根
/*		String srcDir = "../data/AS-2009-2012/all/";
		String dstDir = "../data/AS-2009-2012/APL/";
		String dstFile = dstDir+"apl.txt";
		FileTool ft = new FileTool();
		
		
		String srcFile = null;
		Network net = null;
		PCB pcb = new PCB();
		StringBuffer sb = new StringBuffer();
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		//Date d = null;
		for(int i = 1; i <= 48; i++){
			srcFile = srcDir+i+".txt";
			D.p(sdf.format(new Date()));
			net = new Network(srcFile, NetType.UNDIRECTED, NumberType.INTEGER);
			D.p(sdf.format(new Date()));
			pcb.script(net, dstDir, i);
			D.p(sdf.format(new Date()));
			break;
		}
		ft.write(sb, dstFile);*/
/*		ASAPL apl = new ASAPL();
		int n = 2;
		Thread t = null;
		for(int i = 1; i <= n; i++){
			t = new Thread(apl);
			t.setName(i+"");
			t.start();
		}*/
		ASAPL apl = new ASAPL();
		Map<Integer, Thread> pool = new HashMap<Integer, Thread>();
		int n = 2;//每次开n个线程
		int cur = 0;//当前正在运行线程Id
		int all = 68;//总共是all个
		Thread t = null;
		while(cur < 48 ){
			//遍历有多少线程在执行
			for(Iterator<Thread> it = pool.values().iterator(); it.hasNext();){
				t = it.next();
				if(!t.isAlive()){
					it.remove();
				}
			}
			
			//启动新的线程
			int i;
			for(i = cur+1; i <= cur+n - pool.size() && i <= all ; i++){
				t = new Thread(apl);
				t.setName(i+"");
				t.start();
				pool.put(i, t);
			}
			cur = i-1;
		}
		
	}
	
	public void run(){
		String srcDir = "../data/AS/netM/";
		String dstDir = "../data/AS-2009-2012/APL/";
		//String srcDir = "../data/test/test/";
		//String dstDir = "../data/test/";
		D.p(srcDir+Thread.currentThread().getName()+".txt");
		new PCB().script(new Network(srcDir+Thread.currentThread().getName()+".txt", NetType.UNDIRECTED, NumberType.INTEGER), dstDir, Thread.currentThread().getName());
	
/*		String srcFile = "../data/AS-2009-2012/all/1.txt";
		Network net = new Network(srcFile, NetType.UNDIRECTED, NumberType.INTEGER);
		Coreness c = new Coreness();
		D.p(c.adjNodeCore(3356));*/
	
	}

}
