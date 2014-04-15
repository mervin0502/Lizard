package me.mervin.project.usr;

import java.io.File;

import me.mervin.core.Global.NetType;
import me.mervin.core.Global.NumberType;
import me.mervin.util.D;
import me.mervin.util.EdgeDeweigh;


public class IpStable {
	public static void main(String[] args){
		/*
		 * 提取基本数据		
		File[] fList = new File("../data/IP/2012").listFiles();
		for (int i = 0; i < fList.length; i++) {
			D.p(fList[i].getPath());
			new ExtractIPByLink(fList[i].getPath(), fList[i].getParent()+"/extract-"+fList[i].getName()).script();
		}*/
		/**
		 * 网络合并和去重
		 */
		File[] fList = new File("../data/IP/2009/extract").listFiles();
		for (int i = 0; i < fList.length; i++) {
			new EdgeDeweigh(fList[i].getPath(), fList[i].getParent()+"/../reduce/"+fList[i].getName().substring(fList[i].getName().indexOf('-')+1), NetType.UNDIRECTED, NumberType.LONG).script();
		}
	}
}
