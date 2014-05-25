package me.mervin.project.asRank.evolution;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;

import me.mervin.core.Network;
import me.mervin.core.Global.NetType;
import me.mervin.core.Global.NumberType;
import me.mervin.module.feature.AssortativityCoefficient;
import me.mervin.module.feature.Degree;
import me.mervin.util.D;
import me.mervin.util.FileTool;
import me.mervin.util.Log;
import me.mervin.util.MathTool;
import me.mervin.util.Pair;
import me.mervin.util.PairList;

public class Widgets {
	
	FileTool ft = new FileTool();
	public static void main(String[] args) {
		Widgets w = new Widgets();
		//w.statEdge();
		//w.DegreeDistRate();
//		w.LevelLinkePref();
		w.FreqStat();
//		w.PA();
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


	public void DegreeDistRate(){
		String srcFile = "E:\\data\\as-rel-degree\\20130801-inDegree.txt";
		String dstFile = "E:\\data\\as-rel-degree\\rate\\20130801-inDegree-rate.txt";
		
		FileTool ft = new FileTool();
		Map<Number, Number> m = ft.read2Map(srcFile);
		ft.write(MathTool.ratio(MathTool.frequency(m)), dstFile);
	}
	
	
	public void LevelLinkePref(){
		String srcDir = "E:\\data\\";
		String srcFile = null;
		String dstFile = null;
		String dstDir = "E:\\data\\degree-level\\";
		String date = null;
		for(int y = 2013; y <= 2013; y++){
			for(int m = 5; m <= 6; m++){
				//y = 2000;
				//m = 5;
				if(m < 10){
					date = y+"0"+m+"01";
				}else{
					date = y+""+m+"01";
				}
				D.p(date);
				srcFile = srcDir+"as-rel-res\\"+date;
				if(new File(srcFile).exists()){
					String degreeFile = srcDir+"degree\\"+date+"-degree.txt";
					Map<Number, Number> nodeDegreeMap = ft.read2Map(degreeFile);
					File[] fileArr = ft.fileArr(srcFile, "level");
					StringBuffer sb = new StringBuffer();
					Map<Number, LinkedList<Number>> nodeDegreeAndLevel = new HashMap<Number, LinkedList<Number>>();
					
					for(File f:fileArr){
						Map<Number, HashMap<Number, Number>> levelDereeMap = new HashMap<Number, HashMap<Number, Number>>();
						Map<Number, Number> levelMap = ft.read2Map(f.getAbsolutePath());
						for(Entry<Number, Number> e:levelMap.entrySet()){
							if(nodeDegreeAndLevel.containsKey(e.getKey())){
								nodeDegreeAndLevel.get(e.getKey()).add(e.getValue());
							}else{
								LinkedList<Number> ll = new LinkedList<Number>();
								ll.add(e.getValue());
								nodeDegreeAndLevel.put(e.getKey(), ll);
							}
							if(levelDereeMap.containsKey(e.getValue())){
								levelDereeMap.get(e.getValue()).put(e.getKey(), nodeDegreeMap.get(e.getKey()));
							}else{
								HashMap<Number, Number> m2 = new HashMap<Number, Number>();
								m2.put(e.getKey(), nodeDegreeMap.get(e.getKey()));
								levelDereeMap.put(e.getValue(), m2);
							}
						}//for
						for(Entry<Number, HashMap<Number, Number>> e:levelDereeMap.entrySet()){
							Map<Number, Number> degreeDistRate = MathTool.ratio(MathTool.frequency(e.getValue()));
							String prefix = f.getName();
							prefix = prefix.substring(0, prefix.indexOf('-'));
							dstFile = dstDir+"level-degree-dist-rate\\"+date+"\\"+prefix+"-"+e.getKey()+".txt";
							ft.write(degreeDistRate, dstFile);
						}
						
					}//for
					
					for(Entry<Number, LinkedList<Number>> e:nodeDegreeAndLevel.entrySet()){
						sb.append(e.getKey()).append("\t");
						sb.append(nodeDegreeMap.get(e.getKey())).append("\t");
						for(Number l:e.getValue()){
							sb.append(l).append("\t");
						}
						sb.append("\r\n");
					}
					
					dstFile = dstDir+date+"-degree-level.txt";
					ft.write(sb, dstFile);
				}else{
					Log.a(srcFile+"\r\n", dstDir+"file_noexist.txt");
				}
				//break;
			}
			
		}//for
	}


	public void FreqStat(){
		Map<Pair<Number>, Number> m = new HashMap<Pair<Number>, Number>();
		String srcFile = "E:\\data\\as-rel-p2p-pre\\pre\\20130801-in-in.txt";
		FileTool ft = new FileTool();
		PairList<Number, Number> list = ft.read2PairList(srcFile, NumberType.INTEGER);
		for(int i = 0; i < list.size(); i++){
			Number l = list.getL(i);
			Number r = list.getR(i);
			Pair<Number> p = new Pair<Number>(l, r, false);
			if(m.containsKey(p)){
				m.put(p, m.get(p).intValue()+1);
			}else{
				m.put(p, 1);
			}
		}
		
		int a = 3000;
		int b = 3000;
		int[][] matrix = new int[a][b];
		StringBuffer sb = new StringBuffer();
		for(Entry<Pair<Number>, Number> e : m.entrySet()){
			sb.append(e.getKey().getL()).append("\t");
			sb.append(e.getKey().getR()).append("\t");
			sb.append(e.getValue()).append("\r\n");
			if(e.getKey().getL().intValue() < a && e.getKey().getR().intValue() < b){
				matrix[e.getKey().getL().intValue()][e.getKey().getR().intValue()] =  e.getValue().intValue();
			}
		}
		StringBuffer sb1 = new StringBuffer();
		for(int i = 0; i < matrix.length; i ++){
			for(int j = 0; j < matrix[0].length; j++){
				//D.f(matrix[i][j]+"\t");
				sb1.append(matrix[i][j]+"\t");
			}
			sb1.append("\r\n");
			//D.f("\r\n");
		}
		String dstFile ="E:\\data\\as-rel-p2p-pre\\pre\\20130801-in-in-freq.txt";
		ft.write(sb, dstFile);
		dstFile ="E:\\data\\as-rel-p2p-pre\\pre\\20130801-in-in-matrix.txt";
		ft.write(sb1, dstFile);
	}

	public void PA(){
		String srcFile = "E:\\data\\as-rel-degree\\20130801-peer.txt";
		FileTool ft = new FileTool();
		String dstFile = "E:\\data\\as-rel-degree\\20130801-peer-rate.txt";
		ft.write(MathTool.ratio(MathTool.frequency(ft.read2Map(srcFile))), dstFile);
		 srcFile = "E:\\data\\as-rel-degree\\20130801-outDegree.txt";
		 dstFile = "E:\\data\\as-rel-degree\\20130801-outDegree-rate.txt";
		ft.write(MathTool.ratio(MathTool.frequency(ft.read2Map(srcFile))), dstFile);
		 srcFile = "E:\\data\\as-rel-degree\\20130801-inDegree.txt";
		 dstFile = "E:\\data\\as-rel-degree\\20130801-inDegree-rate.txt";
		ft.write(MathTool.ratio(MathTool.frequency(ft.read2Map(srcFile))), dstFile);
	}

}
