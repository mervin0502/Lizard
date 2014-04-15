package me.mervin.project.asRank.evolution;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import me.mervin.core.Network;
import me.mervin.core.Global.NetType;
import me.mervin.module.feature.Degree;
import me.mervin.util.D;
import me.mervin.util.FileTool;
import me.mervin.util.Log;
import me.mervin.util.PairList;


 /**
 *   P2P.java
 *    P2P连接偏好性
 *  @author Mervin.Wong  DateTime 2014年3月24日 下午2:04:54    
 *  @version 0.4.0
 */
public class P2P {

	/**
	 *  
	 *  @param args
	 */
	public static void main(String[] args) {
		// TODO 自动生成的方法存根
		String srcDir = "E:\\data\\as-relationship\\";
		String srcFile = null;
		String dstDir = "E:\\data\\as-rel-p2p\\";
		String dstFile = null;
		String date = null;
		PairList<Number, Number> edgeList = null;
		Network net = null;
		FileTool ft = new FileTool();
		Degree degree = new Degree();
		P2P pp = new P2P();
		for(int y = 1998; y <= 2013; y++){
			for(int m = 1; m <= 12; m++){
				//y = 2000;
				//m = 5;
				if(m < 10){
					date = y+"0"+m+"01";
				}else{
					date = y+""+m+"01";
				}
				D.p(date);
				srcFile = srcDir+date+".as-rel.txt";
				dstFile = dstDir+date+"-p2p.txt";
				if(new File(srcFile).exists()){
					ft.write(pp.extractP2P(srcFile), dstFile);
				}else{
					Log.a(srcFile, dstDir+"file_noexist.txt");
				}
				D.p("###################");
			}
		}
	}
	
	
	public PairList<Number, Number> extractP2P(String srcFile){
		PairList<Number, Number> edgeList = new PairList<Number, Number>();
		
		String line = null;
		String[] lineArr = null;
		try {
			BufferedReader reader = new BufferedReader(new FileReader(srcFile));
			while((line = reader.readLine())!= null){
				if(line.charAt(0) != '#'){
					lineArr = line.split("\\|");
					int l = Integer.parseInt(lineArr[0]);
					int r = Integer.parseInt(lineArr[1]);
					if(lineArr[2].equals("0")){
						edgeList.add(l, r);
					}
				}
			}
		} catch (FileNotFoundException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		return edgeList;
	}

}
