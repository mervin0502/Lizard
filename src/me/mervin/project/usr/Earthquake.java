package me.mervin.project.usr;

import java.io.File;
import java.io.IOException;

import me.mervin.core.Global.NetType;
import me.mervin.core.Global.NumberType;
import me.mervin.model.earthquake.RealOFC;
import me.mervin.util.D;
import me.mervin.util.plugin.EarthquakeRenormalization;

public class Earthquake {

	/**
	 *  Function:
	 * 
	 *  @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String root = "../data/Earthquake/";
		String srcFile = null;
		String dstFile = null;
		String dstDir = null;
		String temp = null;
		File[] fileArr = null;
		float f = (float) 0.25;//划分粒度
		int count = 500;//仿真次数
		
		
		EarthquakeRenormalization nr = new EarthquakeRenormalization();
		//将网络文件按月份划分
		nr.splitFileByMonth(root+"date_zh_sc.txt", root+"/splitByMonth/");
		//对每个月份的文件按粒度划分成格子
		
		fileArr = new File(root+"splitByMonth/").listFiles();
		for (int i = 0; i < fileArr.length; i++) {
			try {
				srcFile = fileArr[i].getCanonicalPath();
				temp = root+"temp.txt";
				dstFile = root+"splitByF/"+f+"/"+fileArr[i].getName();
				//划分
				nr.renormalizationByLatLon(srcFile, temp, f);
				//提取
				nr.extractNet(temp, dstFile);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		new File(root+"temp.txt").delete();
		
		
		fileArr = new File(root+"splitByF/"+f+"/").listFiles();
		for (int i = 0; i < fileArr.length; i++) {
			try {
				srcFile = fileArr[i].getCanonicalPath();
				String date = fileArr[i].getName().substring(0, fileArr[i].getName().indexOf('.'));
				dstDir = root+"Evolution/"+f+"/"+date+"/";
				RealOFC ofc = new RealOFC(srcFile, NetType.UNDIRECTED, NumberType.INTEGER, 1);
				ofc.evolution(count, dstDir);
				ofc.extractNet(dstDir+"collapse.txt", dstDir+date+"-net-index.txt");
				ofc.renormalizationByIndex(dstDir+date+"-net-index.txt", dstDir+date+"-net-latlon.txt", f);
				nr.convert2Gexf(dstDir+date+"-net-latlon.txt", dstDir+date+"-net-latlon.gexf", f);//Gephi文件
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
