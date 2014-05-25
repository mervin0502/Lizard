package me.mervin.project.asRank.evolution;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;

import me.mervin.core.Network;
import me.mervin.core.Global.NetType;
import me.mervin.core.Global.NumberType;
import me.mervin.module.feature.AssortativityCoefficient;
import me.mervin.util.D;
import me.mervin.util.FileTool;

public class Widgets {
	
	FileTool ft = new FileTool();
	public static void main(String[] args) {
		Widgets w = new Widgets();
		w.statEdge();
		
	}
	
	public void convertDate(){
		String srcFile = "./data/date.txt";
		byte[] b = null;
		try {
			RandomAccessFile f= new RandomAccessFile(srcFile, "r");
			String line = null;
			
			while((line = f.readLine()) != null){
				String str = "";
				str += line.substring(0, 4)+"/";
				str += line.substring(4, 6)+"/";
				str += line.substring(6, 8);
				D.p(str);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void statEdge(){
		String srcDir = "E:\\data\\as-relationship\\";
		String srcFile = null;
		String dstDir = "E:\\data\\";
		String dstFile = null;
		String date = null;
		
		FileTool ft = new FileTool();
		StringBuffer sb = new StringBuffer();
		int i = 0;
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
					int c = 0;
					int p = 0;
					String line = null;
					String[] lineArr = null;
					try {
						BufferedReader reader = new BufferedReader(new FileReader(srcFile));
						while((line = reader.readLine())!= null){
							if(line.charAt(0) != '#'){
								lineArr = line.split("\\|");
								if(lineArr[2].equals("-1")){
									c++;
								}else{
									p++;
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
					sb.append(date).append("\t").append(i).append("\t");
					sb.append(c).append("\t").append((double)c/(c+p)).append("\t");
					sb.append(p).append("\t").append((double)p/(c+p)).append("\r\n");
					D.p("###################");
				}
			}
		}
		dstFile = dstDir+"p2c-p2p-num.txt";
		ft.write(sb, dstFile);
	}
}
