package me.mervin.project.usr;

import java.util.Map;

import me.mervin.core.Network;
import me.mervin.core.Global.NetType;
import me.mervin.model.BANetwork;
import me.mervin.util.FileTool;
import me.mervin.util.PairList;


 /**
 *   BATest.java
 *    
 *  @author Mervin.Wong  DateTime 2014年3月25日 下午9:58:52    
 *  @version 0.4.0
 */
public class BATest {

	public static void main(String[] args){
		FileTool ft = new FileTool();
		BANetwork ba = new BANetwork(5, NetType.UNDIRECTED);
		ba.set(1000, 3);
		ba.createModelNetwork();
		PairList<Number,Number> edge = ba.traverseEdge();
		String dstFile = "./data/ba.txt";
		ft.write(edge, dstFile);
	}
}
