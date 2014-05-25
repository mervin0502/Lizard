package me.mervin.project.asRank.evolution;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import me.mervin.core.Global.NetType;
import me.mervin.core.Global.NumberType;
import me.mervin.core.Network;
import me.mervin.util.D;
import me.mervin.util.FileTool;

public class RegionEvolution {

	public static void main(String[] args) throws NumberFormatException, IOException{
		
		Map<Number, String> asnMap = new HashMap<Number, String>();
		String asnFile = "E:\\data\\ASN\\asn.txt";
		RandomAccessFile f = new RandomAccessFile(asnFile, "r");
		String line = null;
		String[] lineArr = null;
		while((line = f.readLine())!= null){
			lineArr = line.split("\\s+");
			asnMap.put(Integer.parseInt(lineArr[2]), lineArr[0]);
		}
			
		String srcDir = "E:\\data\\extractNet\\";
		String srcFile = null;
		String dstDir = "E:\\data\\";
		String dstFile = null;
		String date = null;
		FileTool ft = new FileTool();
		StringBuffer sb = new StringBuffer();
		int i=0;
		sb.append("#").append("\t");
		sb.append("afrinic").append("\t");
		sb.append("apnic").append("\t");
		sb.append("arin").append("\t");
		sb.append("lacnic").append("\t");
		sb.append("ripencc").append("\r\n");
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
				if(ft.isExist(srcFile)){
					i++;
					Network net = new Network(srcFile, NetType.UNDIRECTED, NumberType.INTEGER);
					Set<Number> nodeSet = net.getAllNodeId();
					Map<String, Integer> asnStatMap = new HashMap<String, Integer>();
					for(Number nodeId:nodeSet){
						String str = asnMap.get(nodeId);
						if(asnStatMap.containsKey(str)){
							asnStatMap.put(str, asnStatMap.get(str)+1);
						}else{
							asnStatMap.put(str, 1);
						}
					}//for
					sb.append(i).append("\t");
					sb.append(asnStatMap.get("afrinic")).append("\t");
					sb.append(asnStatMap.get("apnic")).append("\t");
					sb.append(asnStatMap.get("arin")).append("\t");
					sb.append(asnStatMap.get("lacnic")).append("\t");
					sb.append(asnStatMap.get("ripencc")).append("\r\n");
				}
					
				D.p("###################");
			}
		}
		dstFile = dstDir+"region-stat.txt";
		ft.write(sb, dstFile);
	}
}
