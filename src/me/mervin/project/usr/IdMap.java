package me.mervin.project.usr;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

import me.mervin.util.D;
import me.mervin.util.FileTool;

/**
 * 将ip转换成具体的ID
 * @author mervin
 *
 */
public class IdMap {
	
	
	public void initMap(){
		String souPath = "../data/ipp/";
		String desPath = "../data/ipp/";
		int curIndex = 1;
		HashMap<Number, Integer> nodesIdSet = new HashMap<Number, Integer>();
		File f = new File(souPath);
		BufferedReader reader = null;
		BufferedWriter writer = null;
		String line = null;
		String[] lineArr = null;
		Number preNode = null;
		Number postNode = null;
		StringBuffer sb = new StringBuffer();
		int index = 0;
		String[] fileArr = f.list();
		try {
			writer = new BufferedWriter(new FileWriter(desPath+"idMap.txt"));
			for (int i = 0; i < fileArr.length; i++) {
				try {
					index = 0;
					reader = new BufferedReader(new FileReader(souPath+"/"+fileArr[i]));
					while((line = reader.readLine()) != null){
						lineArr = line.split("\t|(\\s{1,})");
						preNode = Long.parseLong(lineArr[0]);
						postNode = Long.parseLong(lineArr[1]);
						D.p(lineArr[0]+"####"+lineArr[1]);
						if(nodesIdSet.containsKey(preNode)){
							sb.append(preNode).append("\t").append(nodesIdSet.get(preNode)).append("\t");;
						}else{
							nodesIdSet.put(preNode, curIndex);
							sb.append(preNode).append("\t").append(curIndex).append("\t");
							curIndex++;
						}
						if(nodesIdSet.containsKey(postNode)){
							sb.append(postNode).append("\t").append(nodesIdSet.get(postNode)).append("\r\n");;
						}else{
							nodesIdSet.put(postNode, curIndex);
							sb.append(postNode).append("\t").append(curIndex).append("\r\n");
							curIndex++;
						}
					}
					writer.append(sb.toString());
					writer.flush();
					sb.delete(0, sb.length());
					D.m();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			writer.close();
			
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	
	public void getMap(String srcFile, String dstFile, boolean num2Int){
		String mapFile = "";//映射文件

		HashMap<Number, Number> map = new HashMap<Number, Number>();
		try {
			//初始化映射
			BufferedReader reader = new BufferedReader(new FileReader(mapFile));
			String line = null;
			String[] lineArr = null;
			while((line = reader.readLine()) != null){
				lineArr = line.split("\t|\\s{1,}");
				if(num2Int){
					map.put(Long.parseLong(lineArr[0]), Integer.parseInt(lineArr[1]));
				}else{
					map.put(Long.parseLong(lineArr[1]), Integer.parseInt(lineArr[0]));
				}
			}
			
			//从映射中查找对应节点
			reader = new BufferedReader(new FileReader(srcFile));
			StringBuffer sb = new StringBuffer();
			while((line=reader.readLine()) != null){
				lineArr = line.split("\t|\\s{1,}");
				for(int i = 0; i < lineArr.length; i++){
					sb.append(map.get(lineArr[i])).append("\t");
				}
				sb.append("\r\n");
			}
			new FileTool().write(sb, dstFile, false);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String args[]){
		IdMap im = new IdMap();
		im.initMap();
		im.getMap("../data/", "../data/", true);
		
		
	}
}
