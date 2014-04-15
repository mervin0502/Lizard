package me.mervin.util.plugin;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import me.mervin.util.D;
import me.mervin.util.FileTool;

/**
 * 
 *   ASNCountry.java
 *  由ASN定位到国家和注册时间   
 *  @author Mervin.Wong  DateTime 2013-9-25 下午4:22:31    
 *  @version 0.4.0
 * ****************************************************************
 * 
 * ftp://ftp.arin.net/pub/stats/arin/delegated-arin-latest \
 * ftp://ftp.ripe.net/ripe/stats/delegated-ripencc-latest \
 * ftp://ftp.afrinic.net/pub/stats/afrinic/delegated-afrinic-latest \
 * ftp://ftp.apnic.net/pub/stats/apnic/delegated-apnic-latest \
 * ftp://ftp.lacnic.net/pub/stats/lacnic/delegated-lacnic-latest \
 * 
 * ftp://ftp.apnic.net/pub/apnic/stats/apnic/README.TXT
 */
public class ASNCountry {

	private FileTool ft = new FileTool();
	/**
	 *  
	 *  @param args
	 */
	public static void main(String[] args) {
		// TODO 自动生成的方法存根
		ASNCountry asn = new ASNCountry();
		asn.extractASN();
	}
	
	/**
	 * 提取AS号以及相关信息
	 */
	public void extractASN(){
		String srcDir = "../data/AVR/ASN/";
		String dstDir = "../data/AVR/";
		String srcFile = null;
		
		File[] files = new File(srcDir).listFiles();
		String line = null;
		String[] lineArr = null;
		StringBuffer sb = new StringBuffer();
		for(File file : files){
			try {
				BufferedReader read = new BufferedReader(new FileReader(file));
				while((line = read.readLine()) != null){
					if(line.contains("asn") && (line.contains("assigned") || line.contains("allocated"))){
						//D.p(line);
						int endIndex = (line.contains("assigned"))?line.indexOf("assigned"):line.indexOf("allocated");
						line = line.substring(0, endIndex-1);
						lineArr = line.split("\\|");
						D.list(lineArr);
						sb.append(lineArr[3]).append("\t");
						sb.append(lineArr[0]).append("\t");
						sb.append(lineArr[1]).append("\t");
						if(lineArr.length >= 6 ){
							sb.append(lineArr[5]).append("\n");
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
		}
		this.ft.write(sb, dstDir+"asn.txt");
	}

	/**
	 * 
	 */
	
}
