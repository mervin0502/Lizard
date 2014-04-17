package me.mervin.project.usr;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import me.mervin.core.Global.NumberType;
import me.mervin.util.D;
import me.mervin.util.EdgeDeweigh;
import me.mervin.util.extract.*;
import me.mervin.util.FileTool;
import me.mervin.util.MathTool;
import me.mervin.util.Pair;
import me.mervin.util.PairList;

public class ArkVsBGP {
	String srcDir = "../data/AVR/origin/";
	String dstDir = "../data/AVR/ip/";
	String srcFile = null;
	
	FileTool ft = new FileTool();
	/**
	 *  
	 *  @param args
	 */
	public static void main(String[] args) {
		// TODO 自动生成的方法存根
		ArkVsBGP avr = new ArkVsBGP();
		//avr.extractNet();
		//avr.combine();
		//avr.ip2Str();
		//avr.extractNetByARK();
		avr.extractNetByBGP();
	}
	
	/*
	 * 提取IP数据
	 */
	public void extractNet(){
		
		for(int i = 1; i <= 11; i++){
			D.p(i);
			ExtractIPv4ByLink e = new ExtractIPv4ByLink(this.srcDir+i+".txt", this.dstDir+i+"-ip.txt");
			e.script();
		}
		
	}
	
	/*
	 * 合并多个文件
	 */
	public void combine(){
		this.ft.combine("../data/AVR/ip/", "../data/AVR/ip.txt", NumberType.LONG);
		new EdgeDeweigh("../data/AVR/ip.txt", "../data/AVR/ip-reduce.txt", NumberType.LONG).script();
	}
	public void ip2Str(){
		String srcDir = "../data/AVR/";
		String dstDir = "";
			
		List<Pair<Number>> data = this.ft.read2ListPair(srcDir+"ip.txt", NumberType.LONG);
		Number l, r ;
		StringBuffer sb = new StringBuffer();
		for(Pair<Number> p:data){
			l = p.getL();
			r = p.getR();
			sb.append(MathTool.ip2Str(l.longValue())).append("\t").append(MathTool.ip2Str(r.longValue())).append("\n");
		}
		
		this.ft.write(sb, "../data/AVR/ipStr.txt");
	}

	/*
	 * 提取cn的AS
	 */
	public void extractNetByARK(){
		String srcDir = "../data/AVR/";
		String dstDir = "../data/AVR/";
		String srcFile = null;
		
		//Vector<Number> cnAS = new Vector<Number>();
		HashSet<Number> cnAS = new HashSet<Number>();
		BufferedReader read = null;
		
		String line = null;
		String[] lineArr = null;
		/*
		 * get the ASes of China
		 */
		srcFile = srcDir+"asn.txt";
		try {
			read = new BufferedReader(new FileReader(srcFile));
			while((line = read.readLine()) != null){
				lineArr = line.split("\\s+");
				if(lineArr[2].equalsIgnoreCase("CN")){
					cnAS.add(Integer.parseInt(lineArr[0]));
				}
			}
		} catch (FileNotFoundException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		
		
		/*
		 * get the link (AS-AS) that 
		 */
		srcFile = srcDir+"AStxt.txt";
		try {
			read = new BufferedReader(new FileReader(srcFile));
			
			String lStr = null;
			String rStr = null;
			
			String lAS = null;
			String rAS = null;
			
			String[] lArr = null;
			String[] rArr = null;
			
			String[] lASArr = null;
			String[] rASArr = null;

			PairList<Number, Number> pl = new PairList<Number, Number>();
			Set<Pair<Number>> pSet = new HashSet<Pair<Number>>();
			Set<Pair<Number>> pSet1 = new HashSet<Pair<Number>>();
			//Set<Pair<Number>> pSet = new HashSet<Pair<Number>>();
			while((line = read.readLine()) != null){
				if(!line.contains(",")){
					lineArr = line.trim().split("\\s*AS:\\s+");
					lStr = lineArr[1];
					rStr = lineArr[2];
					lArr = lStr.trim().split("\\s*IP:\\s+");
					rArr = rStr.trim().split("\\s*IP:\\s+");
					lAS = lArr[0].trim();
					rAS = rArr[0].trim();
					if( !lAS.isEmpty() && !rAS.isEmpty() ){
						if(lAS.contains("_")){
							lASArr = lAS.split("_");
						}else{
							lASArr = new String[1];
							lASArr[0] = lAS;
						}
						if(rAS.contains("_")){
							rASArr = rAS.split("_");
						}else{
							rASArr = new String[1];
							rASArr[0] = rAS;
						}
						
						for(String l:lASArr){
							for(String r:rASArr){
								//D.p(line);
								int lNum = Integer.parseInt(l);
								int rNum = Integer.parseInt(r);
								if(lNum != rNum){
									if(cnAS.contains(lNum) && cnAS.contains(rNum)){
									  //D.p("###");
									  pl.add(lNum, rNum);
									  pSet.add(new Pair<Number>(lNum, rNum));
									}else if(cnAS.contains(lNum) && !cnAS.contains(rNum)){
										pSet1.add(new Pair<Number>(lNum, rNum));
									}else if(!cnAS.contains(lNum) && cnAS.contains(rNum)){
										pSet1.add(new Pair<Number>(rNum, lNum));
									}
								}
							}//for
						}//for
					}//if
				}//if
			}//while
			this.ft.write(pSet, dstDir+"as-cn-ark.txt");
			this.ft.write("#cn-AS \t external-AS \r\n", dstDir+"as-cn-ark.txt", true);
			this.ft.write(pSet1, dstDir+"as-cn-ark.txt", true);
		} catch (FileNotFoundException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}
	public void extractNetByBGP(){
		String srcDir = "../data/AVR/";
		String dstDir = "../data/AVR/";
		String srcFile = null;
		
		//Vector<Number> cnAS = new Vector<Number>();
		HashSet<Number> cnAS = new HashSet<Number>();
		BufferedReader read = null;
		
		String line = null;
		String[] lineArr = null;
		/*
		 * get the ASes of China
		 */
		srcFile = srcDir+"asn.txt";
		try {
			read = new BufferedReader(new FileReader(srcFile));
			while((line = read.readLine()) != null){
				lineArr = line.split("\\s+");
				if(lineArr[2].equalsIgnoreCase("CN")){
					cnAS.add(Integer.parseInt(lineArr[0]));
				}
			}
		} catch (FileNotFoundException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		
		
		/*
		 * get the link (AS-AS) that 
		 */
		srcFile = srcDir+"bgp.txt";
		try {
			read = new BufferedReader(new FileReader(srcFile));
			
			String lStr = null;
			String rStr = null;
			
			Set<Pair<Number>> pSet = new HashSet<Pair<Number>>(); 
			Set<Pair<Number>> pSet1 = new HashSet<Pair<Number>>(); 
			while((line = read.readLine()) != null){
				if(!line.contains("#")){
					lineArr = line.trim().split("\\|");
					D.p(lineArr.length);
					D.p(line);
					if(lineArr.length == 3){
						lStr = lineArr[0].trim();
						rStr = lineArr[1].trim();
						int lNum = Integer.parseInt(lStr);
						int rNum = Integer.parseInt(rStr);
						if(lNum != rNum){
						if(cnAS.contains(lNum) && cnAS.contains(rNum)){
							pSet.add(new Pair<Number>(lNum, rNum));
						}else if(cnAS.contains(lNum) && !cnAS.contains(rNum)){
							pSet1.add(new Pair<Number>(lNum, rNum));
						}else if(!cnAS.contains(lNum) && cnAS.contains(rNum)){
							pSet1.add(new Pair<Number>(rNum, lNum));
						}
						}
					}

				}//if
			}//while
			this.ft.write(pSet, dstDir+"as-cn-bgp.txt");			
			this.ft.write("#cn-AS \t external-AS \r\n", dstDir+"as-cn-bgp.txt", true);
			this.ft.write(pSet1, dstDir+"as-cn-bgp.txt", true);
		} catch (FileNotFoundException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}
}
