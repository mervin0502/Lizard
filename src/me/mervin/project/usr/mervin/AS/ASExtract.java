package me.mervin.project.usr.mervin.AS;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import me.mervin.core.Global.ExtractAS;
import me.mervin.core.Global.NumberType;
import me.mervin.util.D;
import me.mervin.util.extract.*;
import me.mervin.util.FileTool;

public class ASExtract {
	
	public String srcDir = "../data/AS/origin/";
	public String dstDir = "../data/AS/extractByMonth1/";

	private FileTool ft = new FileTool();
	/**
	 *  
	 *  @param args
	 */
	public static void main(String[] args) {
		// TODO 自动生成的方法存根
		ASExtract e = new ASExtract();
/*		e.srcDir = "../data/AS/origin/";
		e.dstDir = "../data/AS/extractByDate/";
		e.extractByFile();*/
/*		e.srcDir = "../data/AS/origin/";
		e.dstDir = "../data/AS/extractByAS/";
		e.extractByAS();*/
/*		e.srcDir = "../data/AS/extractByDate/";
		e.dstDir = "../data/AS/extractByMonth/";
		e.combineBYMonth();*/
/*		e.srcDir = "../data/AS/extractByMonth/";
		e.dstDir = "../data/AS/extractByMonth2/";
		e.combineByMonth2();*/
/*		e.srcDir = "../data/AS/netM/";
		e.dstDir = "../data/AS/netHY/";
		e.combineByHalfYear();*/
		e.srcDir = "../data/AS/netM/";
		e.dstDir = "../data/AS/netY/";
		e.combineByYear();
		
	}
	
	/*
	 * 从每天的数据文件中提取数据
	 */
	public void extractByFile(){
		int[] team = {1, 2, 3};
		int[] y = {2008, 2009, 2010, 2011, 2012, 2013};
		File[] m = null;
		ExtractASByLink as = null;
		for(int i = 0; i < team.length; i++){
			for(int j = 0; j < y.length; j++){
				m = new File(this.srcDir+"team-"+team[i]+"/"+y[j]+"/").listFiles();
				for(File f:m){
					try {
						D.p(f.getAbsolutePath());
						as = new ExtractASByLink(ExtractAS.ALL, f.getCanonicalPath(), this.dstDir+"team-"+team[i]+"/"+y[j]+"/"+f.getName()+"/");
						as.script();
					} catch (IOException e) {
						// TODO 自动生成的 catch 块
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	/*
	 * 从每天的文件中提前网络，并按照探测点分成不同网络文件
	 */
	public void extractByAS(){
		int[] team = {1, 2, 3};
		int[] y = {2008, 2009, 2010, 2011, 2012, 2013};
		//int[] team = {1};
		//int[] y = {2012};
		File[] m = null;
		File[] d = null;
		ExtractASByLink as = null;
		String str = null;
		Map<Integer, StringBuffer> map = null;
		StringBuffer sb = null;
		FileTool ft = new FileTool();
		int ASN = 0;
		for(int i = 0; i < team.length; i++){
			//team
			int k = 0;
			for(int j = 0; j < y.length; j++){
				//year
				m = new File(this.srcDir+"team-"+team[i]+"/"+y[j]+"/").listFiles();
				for(File f1:m){
					//month
					d = f1.listFiles();
					for(File f2:d){
						//day
						k++;
/*						D.p(f2.getAbsoluteFile());
						str = f2.getName().substring(f2.getName().lastIndexOf('.')-8, f2.getName().lastIndexOf('.'));
						as = new ExtractASByLink(ExtractAS.ALL);
						map = as.extractNetByMonitor(f2.getAbsoluteFile());
						for(Iterator<Integer> it = map.keySet().iterator(); it.hasNext();){
							ASN = it.next();
							sb = map.get(ASN);
							ft.write(sb, this.dstDir+"/"+ASN+"/team-"+team[i]+"-"+str+".txt");
						}*/

					}
				}
			}
			D.p("team"+team[i]+"####"+k);
		}
	}
	
	
	/*
	 * **************************************************************************************
	 * 
	 * 网络合并
	 * 1 按照月份合并
	 * 2 按照
	 */

	public void combineBYMonth(){
		FileTool ft = new FileTool();
		
		File[] teamFileArr = new File(this.srcDir).listFiles();
		for(File team:teamFileArr){
			//team
			for(File year:team.listFiles()){
				//year
				for(File month:year.listFiles()){
					//month
					D.p(month.getAbsolutePath());
					ft.combine(month.getAbsolutePath(), dstDir+team.getName()+"/"+year.getName()+"/"+year.getName()+"-"+month.getName()+".txt", NumberType.INTEGER);
					
				}
			}
		}
	}
	/*
	 * 所有team合并
	 */
	public void combineByMonth2(){
		String srcFile = null;
		String[] year = {"2008", "2009", "2010", "2011", "2012", "2013"};
		String[] month = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};
		File temp = null;
		for(int i = 0; i < year.length; i++){
			for(int j = 0; j < month.length; j++){
				//srcFile = this.dstDir
				Vector<File> v = new Vector<File>();
				int k = 0;
				D.p(year[i]+"##"+month[j]);
				temp = new File(this.srcDir+"team-1/"+year[i]+"/"+year[i]+"-"+month[j]+".txt");
				if(temp.exists()){
					v.add(temp);
				}
				temp = new File(this.srcDir+"team-2/"+year[i]+"/"+year[i]+"-"+month[j]+".txt");
				if(temp.exists()){
					v.add(temp);
				}
				temp = new File(this.srcDir+"team-3/"+year[i]+"/"+year[i]+"-"+month[j]+".txt");
				if(temp.exists()){
					v.add(temp);
				}
				File[] fileArr =  new File[v.size()];
				ft.combine(v.toArray(fileArr), this.dstDir+year[i]+"/"+year[i]+"-"+month[j]+".txt", NumberType.INTEGER, 2);
			}
		}
	}
	
	public void combineByHalfYear(){
		int k = 6;
		int s = 1;
		for(int i = 1; i <= 68;){
			Vector<File> v = new Vector<File>();
			for(int j = i; j < i+k; j++){
				String srcFile = this.srcDir+j+".txt";
				D.p(srcFile);
				File temp = new File(srcFile);
				if(temp.exists()){
					v.add(temp);
				}
			
			}
			File[] fileArr = new File[v.size()];
			this.ft.combine(v.toArray(fileArr), this.dstDir+s+".txt", NumberType.INTEGER, 2);
			s++;
			i+=k;
		}
	}
	
	public void combineByYear(){
		int k = 12;
		int s = 1;
		for(int i = 1; i <= 68;){
			Vector<File> v = new Vector<File>();
			for(int j = i; j < i+k; j++){
				String srcFile = this.srcDir+j+".txt";
				D.p(srcFile);
				File temp = new File(srcFile);
				if(temp.exists()){
					v.add(temp);
				}
			
			}
			File[] fileArr = new File[v.size()];
			this.ft.combine(v.toArray(fileArr), this.dstDir+s+".txt", NumberType.INTEGER, 2);
			s++;
			i+=k;
		}
	}
}
