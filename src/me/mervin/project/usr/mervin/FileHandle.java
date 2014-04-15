package me.mervin.project.usr.mervin;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import me.mervin.util.D;
import me.mervin.util.FileTool;

public class FileHandle {
	public static void main(String[] args){
		try {
			@SuppressWarnings("resource")
			BufferedReader reader = new BufferedReader(new FileReader("../data/test.txt"));
			String line = null;
			String[] lineArr = null;
			HashMap<String, Integer> map = new HashMap<String, Integer>();
			int i, j, w, m=1;
			StringBuffer sb1 = new StringBuffer();
			StringBuffer sb2 = new StringBuffer();
			while((line = reader.readLine()) != null){
				lineArr = line.split("\t|\\s{1,}");
				D.p(lineArr[0]+"###"+lineArr[1]);
				if(map.containsKey(lineArr[0])){
					i = map.get(lineArr[0]);
				}else{
					map.put(lineArr[0], m);
					i = m;
					sb1.append(i+"\t"+lineArr[0]).append("\r\n");
					m++;
				}
				
				if(map.containsKey(lineArr[1])){
					j = map.get(lineArr[1]);
				}else{
					map.put(lineArr[1], m);
					j = m;
					sb1.append(j+"\t"+lineArr[1]).append("\r\n");
					m++;
				}
				
				w = (int)Float.parseFloat(lineArr[2]);
				
				//sb2.append(+i+"\t"+j+"\t"+w+"\r\n");
				sb2.append(+i+"\t"+j+"\r\n");
				
			}
			sb1.append(sb2);
			new FileTool().write(sb1, "../data/20131.txt", false);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
