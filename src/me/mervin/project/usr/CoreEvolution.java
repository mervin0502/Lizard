package me.mervin.project.usr;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import me.mervin.core.Network;
import me.mervin.core.Global.NetType;
import me.mervin.core.Global.NumberType;
import me.mervin.module.feature.Coreness;
import me.mervin.util.D;
import me.mervin.util.FileTool;

/**
 * CoreEvolution
 *  计算核的演化
 * @author Mervin.Wong
 * **********************************************
 * == 死亡节点的核的变化
 * 在比对中死亡节点位于旧网络中，比如1～2中的1，2～3中的2
 * 因此，在文件夹中包含所有的网络 net-XX(xx代表月份) birthNodes-XX(XX表示第几次的比对)
 * 
 *  * **********************************************
 *  注：sourcePath desPath  目录路径最后需要加斜杠
 */
public class CoreEvolution {
	private String sourcePath = null;
	private String desPath = null;
	
	/**
	 * 构造函数
	 */
	public CoreEvolution(String sourcePath, String desPath) {
		// TODO Auto-generated constructor stub
		this.sourcePath = sourcePath;
		this.desPath = desPath;
		
		//(new File(this.sourcePath)).mkdirs();//创建相应的目录
		//(new File(this.desPath)).mkdirs();
	}
	
	public void birthNodesCore(int j){
		// j:1~11
		String netFileName = this.sourcePath+j+"/reduce-1.txt";
		String nodstFileName = null;
		Set<Number> birthNodes = new HashSet<Number>();
		HashMap<Number, Number> birthNodesCore = null;
		FileTool f = new FileTool();
		Network net = new Network(netFileName, NetType.UNDIRECTED, NumberType.LONG);
		Coreness c = new Coreness(net);
		for(int i = 1; i <= j; i++){
			nodstFileName = this.sourcePath+i+"/birthNodes.txt";
			birthNodes = f.read2Set(nodstFileName, NumberType.LONG);
			birthNodesCore = (HashMap<Number, Number>) c.nodeCore(birthNodes);
			f.write(birthNodesCore, this.desPath+j+"/birthNodesCore-"+i+".txt", false);
		}
	}
	
	
	public void deathNodesCore(int j){
		// j:1~11
		String netFileName = this.sourcePath+j+"/reduce-"+(j+1)+".txt";
		String nodstFileName = null;
		Set<Number> birthNodes = new HashSet<Number>();
		HashMap<Number, Number> birthNodesCore = null;
		FileTool f = new FileTool();
 		Network net = new Network(netFileName, NetType.UNDIRECTED, NumberType.LONG);
 		Coreness c = new Coreness(net);
		for(int i = 1; i <= j; i++){
			nodstFileName = this.sourcePath+j+"/deathNodes.txt";
			birthNodes = f.read2Set(nodstFileName, NumberType.LONG);
			birthNodesCore = (HashMap<Number, Number>) c.nodeCore(birthNodes);
			f.write(birthNodesCore, this.desPath+j+"/deathNodesCore-"+i+".txt", false);
		}
	}
	
	public static void main(String[] args){
		//CoreEvolution ce = new CoreEvolution("../data/ip/sj2/", "../data/ip/sj2/");
		//ce.birthNodesCore(1);
		//ce.deathNodesCore(1);
		FileTool ft = new FileTool();
		String srcDir = "E:\\data\\core\\";
		File[] fileArr = ft.fileArr(srcDir);
		Network net = null;
		String dstDir = "E:\\data\\core_1\\";
		String srcFile = null, dstFile = null;
		Coreness core = new Coreness();
		for (int i = 0; i < fileArr.length; i++) {
			srcFile = fileArr[i].getAbsolutePath();
			D.p(fileArr[i].getAbsolutePath());
			net = new Network(srcFile, NetType.UNDIRECTED, NumberType.LONG);
			dstFile = dstDir+fileArr[i].getName();
			D.p(core.nodeCore(net, 1657));
			ft.write(core.nodeCore(net, net.getAllNodeId()), dstFile);
//			D.p(core.nodeCore(net, net.getAllNodeId()));
			
		}
	}
	
}
